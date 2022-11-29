package com.shaic.claim.corpbuffer.allocation.search;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.corpbuffer.allocation.wizard.AllocateCorpBufferDetailDTO;
import com.shaic.claim.corpbuffer.allocation.wizard.AllocateCorpBufferWizard;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.CoorporateBuffer;
import com.shaic.domain.GmcCoorporateBufferLimit;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.MasterService;
import com.shaic.domain.Policy;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.StageInformation;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.server.VaadinSession;

@Stateless
public class AllocateCorpBufferService {

	@PersistenceContext
	protected EntityManager entityManager;

	@EJB
	private ClaimService claimService;

	@EJB
	private PreauthService preauthService;

	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private MasterService masterService;

	public Page<AllocateCorpBufferTableDTO> search(AllocateCorpBufferFormDTO searchFormDTO) {
		List<Claim> listOfClaim = new ArrayList<Claim>(); 

		try {
			String intimationNo = searchFormDTO.getIntimationNo() != null ? searchFormDTO.getIntimationNo() : null;
			String policyNo = searchFormDTO.getPolicyNo() != null ? searchFormDTO.getPolicyNo() : null;

			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<Claim> criteriaQuery = criteriaBuilder.createQuery(Claim.class);

			Root<Claim> root = criteriaQuery.from(Claim.class);
			List<Predicate> conditionList = new ArrayList<Predicate>();

			if (intimationNo != null && !intimationNo.isEmpty()) {
				Predicate intimationCriteria = criteriaBuilder.like(root.<Intimation>get("intimation").<String>get("intimationId"), "%"+intimationNo+"%");
				conditionList.add(intimationCriteria);
			}

			if (policyNo != null && !policyNo.isEmpty()) {
				Predicate policyCriteria = criteriaBuilder.like(root.<Intimation>get("intimation").<Policy>get("policy").<String>get("policyNumber"), "%"+policyNo+"%");
				conditionList.add(policyCriteria);
			}

			Predicate closeCriteria =  criteriaBuilder.notEqual(root.<Status>get("status").<Long>get("key"), ReferenceTable.CLAIM_CLOSED_STATUS);
			conditionList.add(closeCriteria);		

			criteriaQuery.select(root).where(conditionList.toArray(new Predicate[] {}));
			final TypedQuery<Claim> typedQuery = entityManager.createQuery(criteriaQuery);
			listOfClaim = typedQuery.getResultList();
			List<Claim> doList = listOfClaim;

			List<AllocateCorpBufferTableDTO> tableDTO = AllocateCorpBufferMapper.getInstance().getClaimDTO(doList);
			tableDTO = getHospitalDetails(tableDTO);

			DBCalculationService dbCalculationService = new DBCalculationService();
			for (AllocateCorpBufferTableDTO bufferTableDto : tableDTO) {
				Long lPolicyNumber = bufferTableDto.getPolicyKey();
				Long insuredId = bufferTableDto.getInsuredId();
				Double sumInsured = dbCalculationService.getInsuredSumInsured(insuredId.toString(), lPolicyNumber,"H");
				Long insuredKey = bufferTableDto.getInsuredKey();
				Long claimKey = bufferTableDto.getKey();

				if ((ReferenceTable.getGMCProductList().containsKey(Long.valueOf(bufferTableDto.getProductKey())))) {
					bufferTableDto.setSumInsured(dbCalculationService.getBalanceSIForGMC(lPolicyNumber, insuredKey, claimKey));
				} else if ((ReferenceTable.GPA_PRODUCT_KEY.equals(bufferTableDto.getProductKey()))) {
					bufferTableDto.setSumInsured(dbCalculationService.getBalanceSI(lPolicyNumber, insuredKey , claimKey , sumInsured , bufferTableDto.getIntimationKey()).get(SHAConstants.TOTAL_BALANCE_SI));
				} else {
					bufferTableDto.setSumInsured(dbCalculationService.getBalanceSI(lPolicyNumber, insuredKey , claimKey , sumInsured , bufferTableDto.getIntimationKey()).get(SHAConstants.TOTAL_BALANCE_SI));
				}
			}

			List<AllocateCorpBufferTableDTO> result = new ArrayList<AllocateCorpBufferTableDTO>();
			result.addAll(tableDTO);
			Page<AllocateCorpBufferTableDTO> page = new Page<AllocateCorpBufferTableDTO>();
			page.setPageItems(result);
			page.setIsDbSearch(false);
			page.setTotalRecords(listOfClaim.size());
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}

	private List<AllocateCorpBufferTableDTO> getHospitalDetails(List<AllocateCorpBufferTableDTO> tableDTO) {
		Hospitals hospitalDetail;
		for (int index = 0; index < tableDTO.size(); index++) {
			Query findByHospitalKey = entityManager.createNamedQuery("Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalNameID());
			try {
				hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
				if (hospitalDetail != null) {
					tableDTO.get(index).setHospitalName(hospitalDetail.getName());
				}
			} catch(Exception e) {
				continue;
			}
		}
		return tableDTO;
	}

	public void updateCorpBufferLimit(AllocateCorpBufferDetailDTO corpBufferDto) {
		if (corpBufferDto != null && corpBufferDto.getClaimKey() != null) {
			/*if(corpBufferDto.getDisBufferInsuredLimit() !=null){
				Claim claim = claimService.getClaimByKey(corpBufferDto.getClaimKey());
				if (claim != null) {
					claim.setGmcCorpBufferLmt(corpBufferDto.getDisBufferInsuredLimit());
					claim.setGmcCorpBufferFlag("Y");
					claim.setModifiedBy(corpBufferDto.getUserName() != null ? corpBufferDto.getUserName().toUpperCase() : "");
					claim.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.merge(claim);
					entityManager.flush();
					entityManager.clear();
				}
			}*/
			
			if(corpBufferDto.getDisBufferInsuredLimit() !=null && corpBufferDto.getIsDisbufferClicked().equals(true)){
				saveDiscretionaryBufferdetails(corpBufferDto);
			}if(corpBufferDto.getWintageBufferLimit() !=null && corpBufferDto.getIsWinbufferClicked().equals(true)){
				saveWintageBufferdetails(corpBufferDto);
			}if(corpBufferDto.getNacBufferLimit() !=null && corpBufferDto.getIsNacbufferClicked().equals(true)){
				saveNACBufferdetails(corpBufferDto);
			}
			updateStageInformation(corpBufferDto);
		}
	}

	private void updateStageInformation(AllocateCorpBufferDetailDTO corpBufferDto) {
		if (corpBufferDto != null && corpBufferDto.getClaimKey() != null && corpBufferDto.getNewIntimationDto() != null
				&& corpBufferDto.getNewIntimationDto().getStage() != null) {
			Claim claim = claimService.getClaimByKey(corpBufferDto.getClaimKey());
			if (claim != null) {
				Preauth preauth = preauthService.getLatestPreauthDetails(claim.getKey());

				/*if(preauth != null){
					preauth.setCorporateBufferFlag(1L);
					if(corpBufferDto.getDisBufferInsuredLimit() !=null && corpBufferDto.getDisBufferInsuredLimit() > 0){
						preauth.setCorporateUtilizedAmt(corpBufferDto.getDisBufferInsuredLimit().intValue());
					}if(corpBufferDto.getWintageBufferLimit() !=null && corpBufferDto.getWintageBufferLimit() > 0){		
						if(preauth.getWintageBufferAmt() !=null && preauth.getWintageBufferAmt() > 0){
							preauth.setWintageBufferAmt(preauth.getWintageBufferAmt() + corpBufferDto.getWintageBufferLimit());	
						}else{
							preauth.setWintageBufferAmt(corpBufferDto.getWintageBufferLimit());						
						}
					}if(corpBufferDto.getNacBufferLimit() !=null && corpBufferDto.getNacBufferLimit() > 0){
						if(preauth.getNacbBufferAmt() !=null && preauth.getNacbBufferAmt() > 0){
							preauth.setNacbBufferAmt(preauth.getNacbBufferAmt() + corpBufferDto.getNacBufferLimit());	
						}else{
							preauth.setNacbBufferAmt(corpBufferDto.getNacBufferLimit());
						}
					}
					preauth.setModifiedBy(corpBufferDto.getUserName() != null ? corpBufferDto.getUserName().toUpperCase() : "");
					preauth.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.merge(preauth);
					entityManager.flush();
					entityManager.clear();
				}*/

				Reimbursement reimbursement = reimbursementService.getLatestActiveROD(claim.getKey());
				/*if(reimbursement !=null){
					reimbursement.setCorporateBufferFlag(1);
					if(corpBufferDto.getDisBufferInsuredLimit() !=null  && corpBufferDto.getDisBufferInsuredLimit() > 0){
						reimbursement.setCorporateUtilizedAmt(corpBufferDto.getDisBufferInsuredLimit().intValue());
					}if(corpBufferDto.getWintageBufferLimit() !=null && corpBufferDto.getWintageBufferLimit() > 0){	
						if(reimbursement.getWintageBufferAmt() !=null && reimbursement.getWintageBufferAmt() > 0){
							reimbursement.setWintageBufferAmt(reimbursement.getWintageBufferAmt() + corpBufferDto.getWintageBufferLimit());	
						}else{
							reimbursement.setWintageBufferAmt(corpBufferDto.getWintageBufferLimit());						
						}
						//reimbursement.setWintageBufferAmt(corpBufferDto.getWintageBufferLimit());						
					}if(corpBufferDto.getNacBufferLimit() !=null && corpBufferDto.getNacBufferLimit() > 0){
						if(reimbursement.getNacbBufferAmt() !=null && reimbursement.getNacbBufferAmt() > 0){
							reimbursement.setNacbBufferAmt(reimbursement.getNacbBufferAmt() + corpBufferDto.getNacBufferLimit());	
						}else{
							reimbursement.setNacbBufferAmt(corpBufferDto.getNacBufferLimit());
						}
						//reimbursement.setNacbBufferAmt(corpBufferDto.getNacBufferLimit());						
					}
					reimbursement.setModifiedBy(corpBufferDto.getUserName() != null ? corpBufferDto.getUserName().toUpperCase() : "");
					reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.merge(reimbursement);
					entityManager.flush();
					entityManager.clear();
				}*/

				StageInformation stgInformation = new StageInformation();
				stgInformation.setIntimation(claim.getIntimation());				
				stgInformation.setClaimType(claim.getClaimType());
				stgInformation.setStage(corpBufferDto.getNewIntimationDto().getStage());
				Status status = new Status();
				status.setKey(ReferenceTable.ALLOCATE_CORP_BUFFER_STATUS_KEY);
				status.setProcessValue(ReferenceTable.ALLOCATE_CORP_BUFFER_STATUS_VALUE);
				stgInformation.setStatus(status);
				stgInformation.setClaim(claim);
				if (reimbursement == null) {
					stgInformation.setPreauth(preauth);
				}else if(reimbursement != null) {
					stgInformation.setReimbursement(reimbursement);
				}
				stgInformation.setCreatedBy(corpBufferDto.getUserName() != null ? corpBufferDto.getUserName().toUpperCase() : "");
				stgInformation.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.persist(stgInformation);
				entityManager.flush();
				entityManager.clear();
			}

		}

	}

	private void saveDiscretionaryBufferdetails(AllocateCorpBufferDetailDTO corpBufferDto){
		CoorporateBuffer discretionaryBuffer = getBufferbyMainMemeberID(corpBufferDto.getInsuredmainNo(),SHAConstants.PRC_BUFFERTYPE_CB);
		if(discretionaryBuffer !=null){
			//Double limit = discretionaryBuffer.getAllocatedAmount() + corpBufferDto.getDisBufferInsuredLimit();
			discretionaryBuffer.setLimitApplicable(corpBufferDto.getDisBufferApplTo() != null ?corpBufferDto.getDisBufferApplTo() :"");
			discretionaryBuffer.setAllocatedAmount(corpBufferDto.getDisBufferInsuredLimit());
			discretionaryBuffer.setModifiedBy(corpBufferDto.getUserName() != null ? corpBufferDto.getUserName().toUpperCase() : "");
			discretionaryBuffer.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			discretionaryBuffer.setInsuredNumber(corpBufferDto.getInsuredNo());
			discretionaryBuffer.setActiveStatus(1L);
			entityManager.merge(discretionaryBuffer);
		}else{
			discretionaryBuffer = new CoorporateBuffer();
			discretionaryBuffer.setBufferType(SHAConstants.PRC_BUFFERTYPE_CB);
			discretionaryBuffer.setPolicyNumber(corpBufferDto.getPolicyNo());
			discretionaryBuffer.setMainMember(corpBufferDto.getInsuredmainNo());
			discretionaryBuffer.setInsuredNumber(corpBufferDto.getInsuredNo());
			discretionaryBuffer.setAllocatedAmount(corpBufferDto.getDisBufferInsuredLimit());
			discretionaryBuffer.setLimitApplicable(corpBufferDto.getDisBufferApplTo());
			if(corpBufferDto.getDisBufferIndviSI()!=null && corpBufferDto.getDisBufferIndviSI().equals("YES")){
				discretionaryBuffer.setIndividualSi(1L);
			}else{
				discretionaryBuffer.setIndividualSi(0L);
			}
			discretionaryBuffer.setActiveStatus(1L);
			discretionaryBuffer.setCreatedBy(corpBufferDto.getUserName() != null ? corpBufferDto.getUserName().toUpperCase() : "");
			discretionaryBuffer.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			discretionaryBuffer.setBiDate(new Timestamp(System.currentTimeMillis()));
			entityManager.persist(discretionaryBuffer);		
		}
		
		entityManager.flush();
		entityManager.clear();
	}

	private void saveWintageBufferdetails(AllocateCorpBufferDetailDTO corpBufferDto){

		CoorporateBuffer wintageBuffer  = getBufferbyMainMemeberID(corpBufferDto.getInsuredmainNo(),SHAConstants.PRC_BUFFERTYPE_WINTAGE);
		if(wintageBuffer !=null){
			//Double limit = wintageBuffer.getAllocatedAmount() + corpBufferDto.getWintageBufferLimit();
			wintageBuffer.setLimitApplicable(corpBufferDto.getWinBufferApplTo().getValue());
			wintageBuffer.setAllocatedAmount(corpBufferDto.getWintageBufferLimit());
			wintageBuffer.setModifiedBy(corpBufferDto.getUserName() != null ? corpBufferDto.getUserName().toUpperCase() : "");
			wintageBuffer.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			wintageBuffer.setInsuredNumber(corpBufferDto.getInsuredNo());
			wintageBuffer.setActiveStatus(1L);
			entityManager.merge(wintageBuffer);
		}else{
			wintageBuffer = new CoorporateBuffer();
			wintageBuffer.setBufferType(SHAConstants.PRC_BUFFERTYPE_WINTAGE);
			wintageBuffer.setPolicyNumber(corpBufferDto.getPolicyNo());
			wintageBuffer.setMainMember(corpBufferDto.getInsuredmainNo());
			wintageBuffer.setInsuredNumber(corpBufferDto.getInsuredNo());
			wintageBuffer.setAllocatedAmount(corpBufferDto.getWintageBufferLimit());
			wintageBuffer.setLimitApplicable(corpBufferDto.getWinBufferApplTo().getValue());
			if(corpBufferDto.getDisBufferIndviSI()!=null && corpBufferDto.getDisBufferIndviSI().equals("YES")){
				wintageBuffer.setIndividualSi(1L);
			}else{
				wintageBuffer.setIndividualSi(0L);
			}
			wintageBuffer.setActiveStatus(1L);
			wintageBuffer.setCreatedBy(corpBufferDto.getUserName() != null ? corpBufferDto.getUserName().toUpperCase() : "");
			wintageBuffer.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			wintageBuffer.setBiDate(new Timestamp(System.currentTimeMillis()));
			entityManager.persist(wintageBuffer);
		}
		entityManager.flush();
		entityManager.clear();
	}

	private void saveNACBufferdetails(AllocateCorpBufferDetailDTO corpBufferDto){
		CoorporateBuffer nacBuffer  = getBufferbyMainMemeberID(corpBufferDto.getInsuredmainNo(),SHAConstants.PRC_BUFFERTYPE_NACB);
		if(nacBuffer !=null){
			//Double limit = nacBuffer.getAllocatedAmount() + corpBufferDto.getNacBufferLimit();
			nacBuffer.setLimitApplicable(corpBufferDto.getNacbBufferApplTo().getValue());
			nacBuffer.setAllocatedAmount(corpBufferDto.getNacBufferLimit());
			nacBuffer.setModifiedBy(corpBufferDto.getUserName() != null ? corpBufferDto.getUserName().toUpperCase() : "");
			nacBuffer.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			nacBuffer.setInsuredNumber(corpBufferDto.getInsuredNo());
			nacBuffer.setActiveStatus(1L);
			entityManager.merge(nacBuffer);
		}else{
			nacBuffer = new CoorporateBuffer();
			nacBuffer.setBufferType(SHAConstants.PRC_BUFFERTYPE_NACB);
			nacBuffer.setPolicyNumber(corpBufferDto.getPolicyNo());
			nacBuffer.setMainMember(corpBufferDto.getInsuredmainNo());
			nacBuffer.setInsuredNumber(corpBufferDto.getInsuredNo());
			nacBuffer.setAllocatedAmount(corpBufferDto.getNacBufferLimit());
			nacBuffer.setLimitApplicable(corpBufferDto.getNacbBufferApplTo().getValue());
			if(corpBufferDto.getDisBufferIndviSI()!=null && corpBufferDto.getDisBufferIndviSI().equals("YES")){
				nacBuffer.setIndividualSi(1L);
			}else{
				nacBuffer.setIndividualSi(0L);
			}
			nacBuffer.setActiveStatus(1L);
			nacBuffer.setCreatedBy(corpBufferDto.getUserName() != null ? corpBufferDto.getUserName().toUpperCase() : "");
			nacBuffer.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			nacBuffer.setBiDate(new Timestamp(System.currentTimeMillis()));
			entityManager.persist(nacBuffer);
		}
		entityManager.flush();
		entityManager.clear();
	}
	
	public CoorporateBuffer getBufferbyinsuerdNo(Long insuredNumber,String bufferType){
		Query query = entityManager.createNamedQuery("CoorporateBuffer.findbyinsuredNumberBuffertype");
		query = query.setParameter("insuredNumber", insuredNumber);
		query = query.setParameter("bufferType", bufferType);
		List<CoorporateBuffer> bufferLimits = query.getResultList();
		if(bufferLimits !=null && !bufferLimits.isEmpty()){
			return bufferLimits.get(0);
		}
		return null;
	}
	
	public AllocateCorpBufferDetailDTO getbufferViewDetails (Intimation intimation){

		AllocateCorpBufferDetailDTO allocateCorpBufferDetailDTO = new AllocateCorpBufferDetailDTO();

		Policy policy = intimation.getPolicy();
		Insured insured = intimation.getInsured();
		allocateCorpBufferDetailDTO.setPolicyNo(policy.getPolicyNumber());
		Long claimKey =0l;
		Claim claim = masterService.getClaimByIntimationKey(intimation.getKey());
		if(claim != null && claim.getKey() != null){
			claimKey = claim.getKey();
		}
		allocateCorpBufferDetailDTO.setInsuredKey(insured.getKey());
		if(insured.getDependentRiskId() !=null){
			allocateCorpBufferDetailDTO.setInsuredmainNo(insured.getDependentRiskId());
			allocateCorpBufferDetailDTO.setIsDependent(true);
			allocateCorpBufferDetailDTO.setInsuredKey(insured.getDependentRiskId());
		}else{
			allocateCorpBufferDetailDTO.setInsuredmainNo(insured.getInsuredId());
			allocateCorpBufferDetailDTO.setIsEmployee(true);
			allocateCorpBufferDetailDTO.setInsuredKey(insured.getInsuredId());
		}

		if(policy.getPolicyNumber() !=null && insured.getKey()!=null 
				&& allocateCorpBufferDetailDTO.getInsuredmainNo() !=null){
			Map<String, Double> disBufferValues = dbCalculationService.getGmcCorpBufferASIForVwDeatils(SHAConstants.PRC_BUFFERTYPE_CB,
					policy.getPolicyNumber(),insured.getKey(),allocateCorpBufferDetailDTO.getInsuredmainNo(),claimKey);
			allocateCorpBufferDetailDTO.setDisBufferSI(disBufferValues.get(SHAConstants.LN_POLICY_BUFFER_SI));
			allocateCorpBufferDetailDTO.setDisBufferUtilizedAmt(disBufferValues.get(SHAConstants.LN_BUFFER_UTILISED_AMT));
			if(disBufferValues.get(SHAConstants.LN_MAX_INSURED_ALLOCATE_AMT) !=null){
				allocateCorpBufferDetailDTO.setMaxdisBufferInsuredLimit(disBufferValues.get(SHAConstants.LN_MAX_INSURED_ALLOCATE_AMT));
			}
			if(allocateCorpBufferDetailDTO.getDisBufferSI() !=null 
					&& allocateCorpBufferDetailDTO.getDisBufferUtilizedAmt() !=null){
				Double avlbln = allocateCorpBufferDetailDTO.getDisBufferSI() - allocateCorpBufferDetailDTO.getDisBufferUtilizedAmt();
				allocateCorpBufferDetailDTO.setPolicywisedisBufferAvlBlnc(avlbln);
			}
			CoorporateBuffer disBuffer  = getBufferbyMainMemeberID(allocateCorpBufferDetailDTO.getInsuredKey(),SHAConstants.PRC_BUFFERTYPE_CB);
			if(disBuffer != null && disBuffer.getAllocatedAmount() != null){	
			allocateCorpBufferDetailDTO.setIsDisBufferApplicable(true);
				allocateCorpBufferDetailDTO.setDisBufferInsuredLimit(disBufferValues.get(SHAConstants.LN_INSURED_ALLOCATE_AMT));
				allocateCorpBufferDetailDTO.setDisAllocatedLimit(disBufferValues.get(SHAConstants.LN_INSURED_ALLOCATE_AMT));
				allocateCorpBufferDetailDTO.setDiscretionaryUtilizedInsured(disBufferValues.get(SHAConstants.LN_INSURED_UTIL_AMT) != null ? disBufferValues.get(SHAConstants.LN_INSURED_UTIL_AMT) : 0d);
				Double avlbln = allocateCorpBufferDetailDTO.getDisAllocatedLimit() - allocateCorpBufferDetailDTO.getDiscretionaryUtilizedInsured();
				if(avlbln < 0){
					avlbln =0d;
				}
				allocateCorpBufferDetailDTO.setDisAvlBalnc(avlbln);
				allocateCorpBufferDetailDTO.setDisBufferAvailBalnc(avlbln);
			}
//			CoorporateBuffer discretionaryBuffer = corpBufferService.getBufferbyinsuerdNo(insured.getInsuredId(),SHAConstants.PRC_BUFFERTYPE_CB);

			Map<String, Double> WintageBufferValues = dbCalculationService.getGmcCorpBufferASIForVwDeatils(SHAConstants.PRC_BUFFERTYPE_WINTAGE,
					policy.getPolicyNumber(),insured.getKey(),allocateCorpBufferDetailDTO.getInsuredmainNo(),claimKey);
			allocateCorpBufferDetailDTO.setMaxwintageBufferLimit(WintageBufferValues.get(SHAConstants.LN_POLICY_BUFFER_SI));
			if(allocateCorpBufferDetailDTO.getMaxwintageBufferLimit() !=null 
					&& WintageBufferValues.get(SHAConstants.LN_BUFFER_UTILISED_AMT) !=null){
				//allocateCorpBufferDetailDTO.setWintageBufferLimit(WintageBufferValues.get(SHAConstants.LN_BUFFER_UTILISED_AMT));
				allocateCorpBufferDetailDTO.setWintageAllocatedLimit(WintageBufferValues.get(SHAConstants.LN_BUFFER_UTILISED_AMT));
				Double avlbln = allocateCorpBufferDetailDTO.getMaxwintageBufferLimit() - WintageBufferValues.get(SHAConstants.LN_BUFFER_UTILISED_AMT);
				allocateCorpBufferDetailDTO.setWintageBufferAvlBalnc(avlbln);
			}
			
			CoorporateBuffer wintageBuffer  = getBufferbyMainMemeberID(allocateCorpBufferDetailDTO.getInsuredKey(),SHAConstants.PRC_BUFFERTYPE_WINTAGE);
			if(wintageBuffer !=null 
					&& wintageBuffer.getAllocatedAmount()!=null){
				allocateCorpBufferDetailDTO.setIsWintageBufferApplicable(true);
				allocateCorpBufferDetailDTO.setWintageBufferLimit(WintageBufferValues.get(SHAConstants.LN_INSURED_ALLOCATE_AMT));
				allocateCorpBufferDetailDTO.setWintageUtilizedInsured(WintageBufferValues.get(SHAConstants.LN_INSURED_UTIL_AMT) != null ? WintageBufferValues.get(SHAConstants.LN_INSURED_UTIL_AMT) : 0d);
				if(allocateCorpBufferDetailDTO.getWintageBufferLimit() !=null){
					Double avlbln = allocateCorpBufferDetailDTO.getWintageBufferLimit() - allocateCorpBufferDetailDTO.getWintageUtilizedInsured();
					allocateCorpBufferDetailDTO.setWintageAvlBalnc(avlbln);
				}
				
			}
			Map<String, Double> nacBufferValues = dbCalculationService.getGmcCorpBufferASIForVwDeatils(SHAConstants.PRC_BUFFERTYPE_NACB,
					policy.getPolicyNumber(),insured.getKey(),allocateCorpBufferDetailDTO.getInsuredmainNo(),claimKey);
			allocateCorpBufferDetailDTO.setMaxnacBufferLimit(nacBufferValues.get(SHAConstants.LN_POLICY_BUFFER_SI));
			if(allocateCorpBufferDetailDTO.getMaxnacBufferLimit() !=null 
					&& nacBufferValues.get(SHAConstants.LN_BUFFER_UTILISED_AMT) !=null){
				//allocateCorpBufferDetailDTO.setNacBufferLimit(nacBufferValues.get(SHAConstants.LN_BUFFER_UTILISED_AMT));
				allocateCorpBufferDetailDTO.setNacAllocatedLimit(nacBufferValues.get(SHAConstants.LN_BUFFER_UTILISED_AMT));
				Double avlbln = allocateCorpBufferDetailDTO.getMaxnacBufferLimit() - nacBufferValues.get(SHAConstants.LN_BUFFER_UTILISED_AMT);
				allocateCorpBufferDetailDTO.setNacBufferAvlBalnc(avlbln);			
			}
			CoorporateBuffer nacBuffer  = getBufferbyMainMemeberID(allocateCorpBufferDetailDTO.getInsuredKey(),SHAConstants.PRC_BUFFERTYPE_NACB);
			if(nacBuffer !=null 
					&& nacBuffer.getAllocatedAmount()!=null){
				allocateCorpBufferDetailDTO.setIsNacBufferApplicable(true);
				allocateCorpBufferDetailDTO.setNacBufferLimit(nacBufferValues.get(SHAConstants.LN_INSURED_ALLOCATE_AMT));
				allocateCorpBufferDetailDTO.setNacbUtilizedInsured(nacBufferValues.get(SHAConstants.LN_INSURED_UTIL_AMT) != null ? nacBufferValues.get(SHAConstants.LN_INSURED_UTIL_AMT) : 0d);
				if(allocateCorpBufferDetailDTO.getNacBufferLimit() !=null){
					Double avlbln = allocateCorpBufferDetailDTO.getNacBufferLimit() - allocateCorpBufferDetailDTO.getNacbUtilizedInsured();
					allocateCorpBufferDetailDTO.setNacAvlBalnc(avlbln);
				}
				
			}
		}
		return allocateCorpBufferDetailDTO;
	}
	
	public CoorporateBuffer getBufferbyMainMemeberID(Long insuredNumber,String bufferType){
		Query query = entityManager.createNamedQuery("CoorporateBuffer.findbyinsuredMainMember");
		query = query.setParameter("mainMember", insuredNumber);
		query = query.setParameter("bufferType", bufferType);
		List<CoorporateBuffer> bufferLimits = query.getResultList();
		if(bufferLimits !=null && !bufferLimits.isEmpty()){
			return bufferLimits.get(0);
		}
		return null;
	}
}
