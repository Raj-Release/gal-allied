package com.shaic.claim.cashlessprocess.processicac.search;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
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
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.RevisedClaimMapper;
import com.shaic.claim.preauth.search.PreauthMapper;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.premedical.mapper.PreMedicalMapper;
import com.shaic.claim.processdatacorrection.dto.ProcessDataCorrectionDTO;
import com.shaic.claim.processdatacorrection.dto.SpecialityCorrectionDTO;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.withdrawWizard.WithdrawPreauthMapper;
import com.shaic.domain.Claim;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.DocUploadToPremia;
import com.shaic.domain.Hospitals;
import com.shaic.domain.IncurredClaimRatio;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasUser;
import com.shaic.domain.MastersValue;
import com.shaic.domain.PreauthService;
import com.shaic.domain.RODBillDetails;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementBenefits;
import com.shaic.domain.ReimbursementRejection;
import com.shaic.domain.ReimbursementRejectionService;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.ClaimAmountDetails;
import com.shaic.domain.preauth.Diagnosis;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Speciality;
import com.shaic.domain.preauth.SpecialityType;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.preauth.StageInformation;
import com.shaic.domain.preauth.TreatingDoctorDetails;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.medicalapproval.processclaimrequest.search.SearchProcessClaimRequestTableDTO;
import com.shaic.reimbursement.rod.allowReconsideration.search.AllowRecosniderMapper;
import com.shaic.reimbursement.rod.allowReconsideration.search.SearchAllowReconsiderationTableDTO;

@Stateless
public class ProcessICACService  {

	@PersistenceContext
	protected EntityManager entityManager;


	@EJB
	private IntimationService intimationService;

	@EJB
	private DBCalculationService dbCalculationService;

	@EJB
	private PreauthService preauthService;
	
	@EJB
	private ReimbursementService reimbursementService;

	@EJB
	private CreateRODService rodService;

	public ProcessICACService(){
		super();
	}

	@SuppressWarnings("null")
	public Page<SearchProcessICACTableDTO> search(SearchProcessICACReqFormDTO formDTO, String userName, String passWord)
	{
		try 
		{
			String intimationNo = 
					formDTO.getIntimationNo();

			Boolean isValidIcac = false;

			if(intimationNo != null && intimationNo.isEmpty()){
				isValidIcac = true;
			}
			else{
				isValidIcac = getTocheckIcacIntimation(intimationNo);
			}
			if(formDTO != null){		
				Page<SearchProcessICACTableDTO> pageList = null;
				if(isValidIcac){
					pageList =getDosearchInICACReq(formDTO,userName,passWord);
				}else{
					pageList =getDosearchInClaim(formDTO,userName,passWord);
				}
				return pageList;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;		
	}

	public Page<SearchProcessICACTableDTO> getDosearchInICACReq(SearchProcessICACReqFormDTO formDTO, String userName, String passWord){
		try {
			String intimationNo = formDTO.getIntimationNo();
			Long clmType = null;
			if(formDTO.getClmType() !=null){
				clmType = formDTO.getClmType().getId();
			}
			final CriteriaBuilder icacbuilder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<IcacRequest> criteriaIcacQuery = icacbuilder
					.createQuery(IcacRequest.class);

			Root<IcacRequest> search = criteriaIcacQuery.from(IcacRequest.class);

			List<Predicate> predicate = new ArrayList<Predicate>();

			if (intimationNo != null && !intimationNo.isEmpty()) {
				Predicate intimationPredicate = icacbuilder.like(
						search.<String> get("intimationNum"),
						"%"+intimationNo+"%" );

				predicate.add(intimationPredicate);
			}
			if(clmType != null){
				Predicate clmcondition = icacbuilder.equal(search.<MastersValue>get("claimType").<Long> get("key"), clmType);
				predicate.add(clmcondition);
			}
			Predicate Flagcondition = icacbuilder.equal(search.<String>get("finalDecFlag"),"N");
			predicate.add(Flagcondition);
			
			criteriaIcacQuery.select(search).where(
					icacbuilder.and(predicate.toArray(new Predicate[] {})));

			final TypedQuery<IcacRequest> searchIcacQuery = entityManager.createQuery(criteriaIcacQuery);
			List<IcacRequest> pageItemIcacList  = searchIcacQuery.getResultList();

			if(pageItemIcacList != null && !pageItemIcacList.isEmpty()){

				ProcesssICACSearchMapper icacSearchMapper = ProcesssICACSearchMapper.getInstance();
				List<SearchProcessICACTableDTO>	resultList = icacSearchMapper.getICACReqResultDTO(pageItemIcacList);
				List<SearchProcessICACTableDTO> finalList = new ArrayList<SearchProcessICACTableDTO>();

				for (SearchProcessICACTableDTO searchProcessICACTableDTO : resultList) {

					Long icacKey = searchProcessICACTableDTO.getIcacKey();
					Claim claimByKey = getClaimDetailForView(searchProcessICACTableDTO.getIntimationNo());
					searchProcessICACTableDTO = icacSearchMapper.getICACFromClaim(claimByKey);
					Long lPolicyNumber = searchProcessICACTableDTO.getPolicyKey();
					Long insuredId = searchProcessICACTableDTO.getInsuredId();
					Double sumInsured = dbCalculationService.getInsuredSumInsured(insuredId.toString(), lPolicyNumber,"H");
					Long insuredKey = searchProcessICACTableDTO.getInsuredKey();


					if (claimByKey != null) {
						if(searchProcessICACTableDTO.getCrmFlagged() != null){
							if(searchProcessICACTableDTO.getCrmFlagged().equalsIgnoreCase("Y")){
								if (claimByKey.getCrcFlag() != null && claimByKey.getCrcFlag().equalsIgnoreCase("Y")) {
									searchProcessICACTableDTO.setColorCodeCell("OLIVE");
								}
								searchProcessICACTableDTO.setCrmFlagged(null);
							}
						}
						if (claimByKey.getIsVipCustomer() != null && claimByKey.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
							searchProcessICACTableDTO.setColorCodeCell("VIP");
						}
						if (claimByKey.getCrcFlag() != null && claimByKey.getCrcFlag().equalsIgnoreCase("Y") 
								&& claimByKey.getIsVipCustomer() != null && claimByKey.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
							searchProcessICACTableDTO.setColorCodeCell("CRMVIP");
						}
					}
					if(ReferenceTable.getGMCProductList().containsKey(searchProcessICACTableDTO.getProductKey())){
						searchProcessICACTableDTO.setBalanceSI(dbCalculationService.getBalanceSIForGMC(lPolicyNumber, insuredKey, claimByKey.getKey()));
					}
					else{
						searchProcessICACTableDTO.setBalanceSI(dbCalculationService.getBalanceSI(lPolicyNumber, insuredKey , searchProcessICACTableDTO.getClaimKey(), sumInsured,searchProcessICACTableDTO.getIntimationKey()).get(SHAConstants.TOTAL_BALANCE_SI));
					}

					if(claimByKey.getIntimation().getHospital() !=null ){
						Hospitals hospitals = gethospitalsByKey(claimByKey.getIntimation().getHospital());
						SearchProcessICACTableDTO icacTableDTO = icacSearchMapper.getHospICACTable(hospitals);
						searchProcessICACTableDTO.setHospitalName(icacTableDTO.getHospitalName());
						searchProcessICACTableDTO.setNetworkHospType(icacTableDTO.getHospitalTypeName());
					}

					searchProcessICACTableDTO.setDirectIcacReq(SHAConstants.N_FLAG);
					searchProcessICACTableDTO.setIcacKey(icacKey);
					
					searchProcessICACTableDTO.setSpeciality(getSpecialityType(searchProcessICACTableDTO.getClaimKey()));
					
					DocUploadToPremia premiaData = getUploadedDataDocument(searchProcessICACTableDTO.getIntimationNo());
					if(premiaData != null && null != premiaData.getPfdUpFfaxSubmitId())
						searchProcessICACTableDTO.setDocReceivedTimeForMatchDate(premiaData.getPfdUpFfaxSubmitId());
					if(premiaData != null && null != premiaData.getPfdUpPremiaAckDt())
						searchProcessICACTableDTO.setDocReceivedTimeForRegDate(premiaData.getPfdUpPremiaAckDt());
					if(premiaData != null && null != premiaData.getPfdUpFFAXAmt())
						searchProcessICACTableDTO.setPreAuthReqAmt(String.valueOf(premiaData.getPfdUpFFAXAmt()));
					
					if(claimByKey.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
						Preauth preauth = preauthService.getPreauthClaimKey(searchProcessICACTableDTO.getClaimKey());		
						if(preauth !=null && preauth.getKey() != null){
							searchProcessICACTableDTO.setTransactionKey(preauth.getKey());
							List<ClaimAmountDetails> findClaimAmountDetailsByPreauthKey = preauthService
									.findClaimAmountDetailsByPreauthKey(preauth.getKey());
							Float amountConsider = 0.0f;
							if (findClaimAmountDetailsByPreauthKey != null) {
								for (ClaimAmountDetails claimAmountDetails : findClaimAmountDetailsByPreauthKey) {
									amountConsider += claimAmountDetails.getPaybleAmount();
								}
							}
							if(amountConsider != null){
								searchProcessICACTableDTO.setClaimedAmountAsPerBill((double) amountConsider.intValue());
							}
							searchProcessICACTableDTO.setTreatmentType(preauth.getTreatmentType().getValue());
						}
					}else if(claimByKey.getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)){
						Reimbursement remObj = findRODByKey(searchProcessICACTableDTO.getClaimKey());
						if(remObj != null && remObj.getKey() != null){
							
							searchProcessICACTableDTO =getSpecilityAndTreatementType(searchProcessICACTableDTO,remObj.getKey());
							searchProcessICACTableDTO.setTransactionKey(remObj.getKey());
						}
					}
				
					
					finalList.add(searchProcessICACTableDTO);
				}

				Page<SearchProcessICACTableDTO> pageList = new Page<SearchProcessICACTableDTO>();
				pageList.setPageItems(finalList);
				pageList.setTotalRecords(finalList.size());
				return pageList;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;	
	}

	public Page<SearchProcessICACTableDTO> getDosearchInClaim(SearchProcessICACReqFormDTO formDTO, String userName, String passWord){
		try 
		{
			String intimationNo = formDTO.getIntimationNo();
			Long clmType = null;
			if(formDTO.getClmType() !=null){
				clmType = formDTO.getClmType().getId();
			}
			if(formDTO != null){

				final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
				final CriteriaQuery<Claim> criteriaQuery = builder
						.createQuery(Claim.class);

				Root<Claim> searchRoot = criteriaQuery.from(Claim.class);

				List<Predicate> predicates = new ArrayList<Predicate>();

				if (intimationNo != null && !intimationNo.isEmpty()) {
					Predicate intimationPredicate = builder.like(
							searchRoot.<Intimation>get("intimation").<String> get("intimationId"),
							"%"+intimationNo+"%");

					predicates.add(intimationPredicate);
				}
				if(clmType != null){
					Predicate clmcondition = builder.equal(searchRoot.<MastersValue>get("claimType").<Long> get("key"), clmType);
					predicates.add(clmcondition);
				}

				criteriaQuery.select(searchRoot).where(
						builder.and(predicates.toArray(new Predicate[] {})));

				final TypedQuery<Claim> searchClaimQuery = entityManager
						.createQuery(criteriaQuery);

				List<Claim> pageItemList  = searchClaimQuery.getResultList();

				ProcesssICACSearchMapper icacSearchMapper = ProcesssICACSearchMapper.getInstance();
				List<SearchProcessICACTableDTO> resultList = icacSearchMapper.getResultDTO(pageItemList);
				List<SearchProcessICACTableDTO> finalList = new ArrayList<SearchProcessICACTableDTO>();

				for (SearchProcessICACTableDTO searchProcessICACTableDTO : resultList) {

					Long lPolicyNumber = searchProcessICACTableDTO.getPolicyKey();
					Long insuredId = searchProcessICACTableDTO.getInsuredId();
					Double sumInsured = dbCalculationService.getInsuredSumInsured(insuredId.toString(), lPolicyNumber,"H");
					Long insuredKey = searchProcessICACTableDTO.getInsuredKey();


					Claim claimByKey = getClaimDetailForView(searchProcessICACTableDTO.getIntimationNo());
					if (claimByKey != null) {

						if(searchProcessICACTableDTO.getCrmFlagged() != null){
							if(searchProcessICACTableDTO.getCrmFlagged().equalsIgnoreCase("Y")){
								if (claimByKey.getCrcFlag() != null && claimByKey.getCrcFlag().equalsIgnoreCase("Y")) {
									searchProcessICACTableDTO.setColorCodeCell("OLIVE");
								}
								searchProcessICACTableDTO.setCrmFlagged(null);
							}
						}
						if (claimByKey.getIsVipCustomer() != null && claimByKey.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
							searchProcessICACTableDTO.setColorCodeCell("VIP");
						}
						if (claimByKey.getCrcFlag() != null && claimByKey.getCrcFlag().equalsIgnoreCase("Y") 
								&& claimByKey.getIsVipCustomer() != null && claimByKey.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
							searchProcessICACTableDTO.setColorCodeCell("CRMVIP");
						}
					}
					if(ReferenceTable.getGMCProductList().containsKey(searchProcessICACTableDTO.getProductKey())){
						searchProcessICACTableDTO.setBalanceSI(dbCalculationService.getBalanceSIForGMC(lPolicyNumber, insuredKey, claimByKey.getKey()));
					}
					else{
						searchProcessICACTableDTO.setBalanceSI(dbCalculationService.getBalanceSI(lPolicyNumber, insuredKey , searchProcessICACTableDTO.getClaimKey(), sumInsured,searchProcessICACTableDTO.getIntimationKey()).get(SHAConstants.TOTAL_BALANCE_SI));
					}

					if(claimByKey.getIntimation().getHospital() !=null ){
						Hospitals hospitals = gethospitalsByKey(claimByKey.getIntimation().getHospital());
						SearchProcessICACTableDTO icacTableDTO = icacSearchMapper.getHospICACTable(hospitals);
						searchProcessICACTableDTO.setHospitalName(icacTableDTO.getHospitalName());
						searchProcessICACTableDTO.setNetworkHospType(icacTableDTO.getHospitalTypeName());
					}	

					searchProcessICACTableDTO.setDirectIcacReq(SHAConstants.YES_FLAG);
					
					searchProcessICACTableDTO.setSpeciality(getSpecialityType(searchProcessICACTableDTO.getClaimKey()));

					DocUploadToPremia premiaData = getUploadedDataDocument(searchProcessICACTableDTO.getIntimationNo());
					if(premiaData != null && null != premiaData.getPfdUpFfaxSubmitId())
						searchProcessICACTableDTO.setDocReceivedTimeForMatchDate(premiaData.getPfdUpFfaxSubmitId());
					if(premiaData != null && null != premiaData.getPfdUpPremiaAckDt())
						searchProcessICACTableDTO.setDocReceivedTimeForRegDate(premiaData.getPfdUpPremiaAckDt());
					if(premiaData != null && null != premiaData.getPfdUpFFAXAmt())
						searchProcessICACTableDTO.setPreAuthReqAmt(String.valueOf(premiaData.getPfdUpFFAXAmt()));

					if(claimByKey.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
						Preauth preauth = preauthService.getPreauthClaimKey(searchProcessICACTableDTO.getClaimKey());
						if(preauth != null && preauth.getKey() != null){
							searchProcessICACTableDTO.setTransactionKey(preauth.getKey());
							List<ClaimAmountDetails> findClaimAmountDetailsByPreauthKey = preauthService
									.findClaimAmountDetailsByPreauthKey(preauth.getKey());

							Float amountConsider = 0.0f;
							if (findClaimAmountDetailsByPreauthKey != null) {
								for (ClaimAmountDetails claimAmountDetails : findClaimAmountDetailsByPreauthKey) {
									amountConsider += claimAmountDetails.getPaybleAmount();
								}
							}
							if(amountConsider != null){
								searchProcessICACTableDTO.setClaimedAmountAsPerBill((double) amountConsider.intValue());
							}
							searchProcessICACTableDTO.setTreatmentType(preauth.getTreatmentType().getValue());
						}
					}else if(claimByKey.getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)){
						Reimbursement remObj = findRODByKey(searchProcessICACTableDTO.getClaimKey());
						if(remObj != null && remObj.getKey() != null){
						searchProcessICACTableDTO =getSpecilityAndTreatementType(searchProcessICACTableDTO,remObj.getKey());
						searchProcessICACTableDTO.setTransactionKey(remObj.getKey());
						}
					}
				
					
					finalList.add(searchProcessICACTableDTO);
					
				}

				Page<SearchProcessICACTableDTO> pageList = new Page<SearchProcessICACTableDTO>();
				pageList.setPageItems(finalList);
				pageList.setTotalRecords(finalList.size());
				return pageList;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;		
	}


	@SuppressWarnings("unchecked")
	public Claim getClaimByKey(Long claimkey) {
		Query findByKey = entityManager.createNamedQuery("Claim.findByKey").setParameter("primaryKey", claimkey);
		List<Claim> claims = (List<Claim>) findByKey.getResultList();
		if (!claims.isEmpty()) {
			entityManager.refresh(claims.get(0));
			return claims.get(0);
		}
		return null;
	}

	public SearchProcessICACTableDTO getSetRequestData(SearchProcessICACTableDTO dto){

		if(dto.getDirectIcacReq() != null && dto.getDirectIcacReq().equalsIgnoreCase(SHAConstants.N_FLAG)){
			dto.setProcessingDoctorDetailsDTOs(getProcessingDoctorDetails(dto));
			dto.setProcessingICACTeamResponseDTO(getProcesResponseTeamDetails(dto));
		}

		return dto;
	}

	private List<ProcessingDoctorDetailsDTO> getProcessingDoctorDetails(SearchProcessICACTableDTO dto) {
		List<IcacRequest> icacReqList =findIcacProcessReqByKey(dto.getIcacKey());
		if (icacReqList !=null && !icacReqList.isEmpty()) {
			List<ProcessingDoctorDetailsDTO> finalListdto = new ArrayList<ProcessingDoctorDetailsDTO>();	
			int sno =1;
			for(IcacRequest icacprocess : icacReqList){
				if(icacprocess != null && icacprocess.getIntimationNum() != null){
					ProcessingDoctorDetailsDTO doctorDetailsdto = new ProcessingDoctorDetailsDTO();
					MasUser userTypes = getEmpById(icacprocess.getCreatedBy().toUpperCase());
					doctorDetailsdto.setDoctorIdAndName(userTypes.getUserId()+" / "+userTypes.getUserName());
					doctorDetailsdto.setReferToIcacRemarks(icacprocess.getRequestRemark());
					doctorDetailsdto.setRemarksRaisedDateTime(icacprocess.getCreatedDate());
					finalListdto.add(doctorDetailsdto);
				}	
			}
			return finalListdto;
		}
		return null;
	}

	private List<ProcessingICACTeamResponseDTO> getProcesResponseTeamDetails(SearchProcessICACTableDTO dto) {
		//		 IcacRequest recentIcacKey = findResponseByIcacKey(dto.getIcacKey());

		if(dto.getIcacKey() != null && dto.getIcacKey() != null){
			List<IcacResponse> icacResTeamList =findIcacResponseByICACkey(dto.getIcacKey());

			if (icacResTeamList !=null && !icacResTeamList.isEmpty()) {
				List<ProcessingICACTeamResponseDTO> finalListdto = new ArrayList<ProcessingICACTeamResponseDTO>();	
				int sno =1;
				for(IcacResponse icacresponse : icacResTeamList){
					if(icacresponse != null && icacresponse.getIcacKey() != null){
						ProcessingICACTeamResponseDTO doctorDetailsdto = new ProcessingICACTeamResponseDTO();
						MasUser userTypes = getEmpById(icacresponse.getCreatedBy().toUpperCase());
						doctorDetailsdto.setResponseGivenBY(userTypes.getUserId()+" / "+userTypes.getUserName());
						doctorDetailsdto.setIcacResponseRemarks(icacresponse.getResponseRemarks());
						doctorDetailsdto.setRepliedDate(icacresponse.getCreatedDate());

						finalListdto.add(doctorDetailsdto);
					}	
				}
				return finalListdto;
			}
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public List<IcacRequest> findIcacProcessReqByKey(Long icackey) {
		Query reqIcac = entityManager.createNamedQuery("IcacRequest.findByReqByIcacKey").setParameter("key", icackey);
		List<IcacRequest> resulticacReq = (List<IcacRequest>) reqIcac.getResultList();
		if (resulticacReq !=null && !resulticacReq.isEmpty()) {
			entityManager.refresh(resulticacReq.get(0));
			return resulticacReq;
		}
		return null;
	}

	public MasUser getEmpById(String initiatorId) {
		MasUser userDetail;
		Query findByTransactionKey = entityManager.createNamedQuery(
				"MasUser.getEmpById").setParameter("userId", initiatorId);
		try {
			userDetail = (MasUser) findByTransactionKey.getSingleResult();
			return userDetail;
		} catch (Exception e) {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public IcacRequest findResponseByIcacKey(Long icacKey) {
		Query reqIcac = entityManager.createNamedQuery("IcacRequest.findByReqByIcacKey").setParameter("key", icacKey);
		List<IcacRequest> resulticacReq = (List<IcacRequest>) reqIcac.getResultList();
		if (resulticacReq !=null && !resulticacReq.isEmpty()) {
			entityManager.refresh(resulticacReq.get(0));
			return  resulticacReq.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<IcacResponse> findIcacResponseByICACkey(Long icacKey) {
		Query reqIcac = entityManager.createNamedQuery("IcacResponse.findResponseByIcacKey").setParameter("icacKey", icacKey);
		List<IcacResponse> resulticacReq = (List<IcacResponse>) reqIcac.getResultList();
		if (resulticacReq !=null && !resulticacReq.isEmpty()) {
			entityManager.refresh(resulticacReq.get(0));
			return resulticacReq;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Claim getClaimDetailForView(String intmNumber) {
		Query findByKey = entityManager.createNamedQuery("Claim.findByIntimationNo").setParameter("intimationNumber", intmNumber);
		List<Claim> claims = (List<Claim>) findByKey.getResultList();
		if (!claims.isEmpty()) {
			entityManager.refresh(claims.get(0));
			return claims.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Boolean getTocheckIcacIntimation(String intitmationNum) {
		final CriteriaBuilder icacbuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<IcacRequest> criteriaIcacQuery = icacbuilder
				.createQuery(IcacRequest.class);

		Root<IcacRequest> search = criteriaIcacQuery.from(IcacRequest.class);

		List<Predicate> predicate = new ArrayList<Predicate>();

		if (intitmationNum != null && !intitmationNum.isEmpty()) {
			Predicate intimationPredicate = icacbuilder.like(
					search.<String> get("intimationNum"),
					"%"+intitmationNum+"%");
			predicate.add(intimationPredicate);
		}
		Predicate Flagcondition = icacbuilder.equal(search.<String>get("finalDecFlag"),"N");
		predicate.add(Flagcondition);
		
		criteriaIcacQuery.select(search).where(
				icacbuilder.and(predicate.toArray(new Predicate[] {})));

		final TypedQuery<IcacRequest> searchIcacQuery = entityManager.createQuery(criteriaIcacQuery);
		List<IcacRequest> pageItemIcacList  = searchIcacQuery.getResultList();
		
		if(pageItemIcacList != null &&  pageItemIcacList.size() > 0){
			if(pageItemIcacList.get(0) != null){
			return true;
			}
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	public Boolean getTocheckIcacClmType(Long key) {
		Query reqIcac = entityManager.createNamedQuery("IcacRequest.findByclmType").setParameter("key", key);
		List<IcacRequest> resulticacReq = (List<IcacRequest>) reqIcac.getResultList();
		if (resulticacReq !=null && !resulticacReq.isEmpty()) {
			entityManager.refresh(resulticacReq.get(0));
			if(resulticacReq.get(0) != null){
				return true;
			}
		}
		return false;
	}
	@SuppressWarnings("unchecked")
	public Hospitals gethospitalsByKey(Long key) {
		Query findByKey = entityManager.createNamedQuery("Hospitals.findByKey").setParameter("key", key);
		List<Hospitals> hospitals = (List<Hospitals>) findByKey.getResultList();
		if (!hospitals.isEmpty()) {
			entityManager.refresh(hospitals.get(0));
			return hospitals.get(0);
		}
		return null;
	}


	public SearchProcessICACTableDTO getProcessICACViewDetails(String intimationNumber){

		SearchProcessICACTableDTO viewResultDetails = new SearchProcessICACTableDTO();
		if(intimationNumber != null && !intimationNumber.isEmpty()){
			viewResultDetails.setIntimationNo(intimationNumber);
			Claim clmDetails = getClaimDetailForView(intimationNumber);
			viewResultDetails.setViewTransactoinType(clmDetails.getClaimType().getValue());
			viewResultDetails.setProcessingDoctorDetailsDTOs(getViewProcessDocDetails(viewResultDetails));
			viewResultDetails.setProcessingICACTeamResponseDTO(getViewProcesResponseTeamDetails(viewResultDetails));
			viewResultDetails.setProcessingICACFinalTeamResDTO(getViewProcesFinalResponseTeamDetails(viewResultDetails));
		}


		return viewResultDetails;
	}

	private List<ProcessingDoctorDetailsDTO> getViewProcessDocDetails(SearchProcessICACTableDTO dto) {
		List<IcacRequest> icacReqList =findIcacProcessReqByIntiNum(dto.getIntimationNo());
		if (icacReqList !=null && !icacReqList.isEmpty()) {
			List<ProcessingDoctorDetailsDTO> finalListdto = new ArrayList<ProcessingDoctorDetailsDTO>();	
			int sno =1;
			for(IcacRequest icacprocess : icacReqList){
				if(icacprocess != null && icacprocess.getIntimationNum() != null){
					ProcessingDoctorDetailsDTO doctorDetailsdto = new ProcessingDoctorDetailsDTO();
					MasUser userTypes = getEmpById(icacprocess.getCreatedBy().toUpperCase());
					doctorDetailsdto.setDoctorIdAndName(userTypes.getUserId()+" / "+userTypes.getUserName());
					doctorDetailsdto.setReferToIcacRemarks(icacprocess.getRequestRemark());
					doctorDetailsdto.setRemarksRaisedDateTime(icacprocess.getCreatedDate());
					finalListdto.add(doctorDetailsdto);
				}	
			}
			return finalListdto;
		}
		return null;
	}

	private List<ProcessingICACTeamResponseDTO> getViewProcesResponseTeamDetails(SearchProcessICACTableDTO dto) {
		List<IcacRequest> recentIcacKey = findIcacKeyForRespTeam(dto.getIntimationNo());

		if(recentIcacKey != null ){
			for(IcacRequest icacView : recentIcacKey){
				List<IcacResponse> icacResTeamList =findIcacResponseByICACkey(icacView.getKey());

				if (icacResTeamList !=null && !icacResTeamList.isEmpty()) {
					List<ProcessingICACTeamResponseDTO> finalListdto = new ArrayList<ProcessingICACTeamResponseDTO>();	
					int sno =1;
					for(IcacResponse icacresponse : icacResTeamList){
						if(icacresponse != null && icacresponse.getIcacKey() != null){
							ProcessingICACTeamResponseDTO doctorDetailsdto = new ProcessingICACTeamResponseDTO();
							MasUser userTypes = getEmpById(icacresponse.getCreatedBy().toUpperCase());
							doctorDetailsdto.setResponseGivenBY(userTypes.getUserId()+" / "+userTypes.getUserName());
							doctorDetailsdto.setIcacResponseRemarks(icacresponse.getResponseRemarks());
							doctorDetailsdto.setRepliedDate(icacresponse.getCreatedDate());
							finalListdto.add(doctorDetailsdto);
						}	
					}
					return finalListdto;
				}
			}

		}
		return null;
	}
	
	private List<ProcessingICACTeamResponseDTO> getViewProcesFinalResponseTeamDetails(SearchProcessICACTableDTO dto) {
		List<IcacRequest> recentIcacKey = findIcacKeyForRespTeam(dto.getIntimationNo());

		if(recentIcacKey != null ){
			for(IcacRequest icacView : recentIcacKey){
				List<IcacResponse> icacResTeamList =findIcacResponseByICACkey(icacView.getKey());

				if (icacResTeamList !=null && !icacResTeamList.isEmpty()) {
					List<ProcessingICACTeamResponseDTO> finalListdto = new ArrayList<ProcessingICACTeamResponseDTO>();	
					int sno =1;
					for(IcacResponse icacresponse : icacResTeamList){
						if(icacresponse != null && icacresponse.getIcacKey() != null 
								&& icacresponse.getFinalDecFlag() != null && icacresponse.getFinalDecFlag().equalsIgnoreCase(SHAConstants.YES_FLAG) ){
							ProcessingICACTeamResponseDTO doctorDetailsdto = new ProcessingICACTeamResponseDTO();
							MasUser userTypes = getEmpById(icacresponse.getCreatedBy().toUpperCase());
							doctorDetailsdto.setResponseGivenBY(userTypes.getUserId()+" / "+userTypes.getUserName());
							doctorDetailsdto.setIcacFinalResponseRemarks(icacresponse.getResponseFinalRemarks());
							doctorDetailsdto.setRepliedDate(icacresponse.getCreatedDate());
							if(icacresponse.getFinalDecFlag() != null && icacresponse.getFinalDecFlag().equalsIgnoreCase(SHAConstants.YES_FLAG) ){
								dto.setFinailDecValue(true);
							}

							finalListdto.add(doctorDetailsdto);
						}	
					}
					return finalListdto;
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<IcacRequest> findIcacProcessReqByIntiNum(String intitmationNum) {
		Query reqIcac = entityManager.createNamedQuery("IcacRequest.findByintimationNumber").setParameter("intimationNum", intitmationNum);
		List<IcacRequest> resulticacReq = (List<IcacRequest>) reqIcac.getResultList();
		if (resulticacReq !=null && !resulticacReq.isEmpty()) {
			entityManager.refresh(resulticacReq.get(0));
			return resulticacReq;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<IcacRequest> findIcacKeyForRespTeam(String intitmationNum) {
		Query reqIcac = entityManager.createNamedQuery("IcacRequest.findByResponseByOrderDes").setParameter("intimationNum", intitmationNum);
		List<IcacRequest> resulticacReq = (List<IcacRequest>) reqIcac.getResultList();
		if (resulticacReq !=null && !resulticacReq.isEmpty()) {
			entityManager.refresh(resulticacReq.get(0));
			return  resulticacReq;
		}
		return null;
	}


	public void submitForDirectRequest(SearchProcessICACTableDTO requestIcacDTO,String userID){
		IcacRequest icacRequest = new IcacRequest();
		String strUserName = userID;
		String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
		if(icacRequest != null){
			icacRequest.setIntimationNum(requestIcacDTO.getIntimationNo());
			icacRequest.setPolicyNumber(requestIcacDTO.getPolicyNumber());
			Status status = null;
			Stage stage = null;
			if(requestIcacDTO.getClaimDto() != null && requestIcacDTO.getClaimDto().getStageId() !=null 
					&& requestIcacDTO.getClaimDto().getStageId().equals(ReferenceTable.PROCESS_PREAUTH)){
				stage = entityManager.find(Stage.class,ReferenceTable.PROCESS_PREAUTH);
				
				if(requestIcacDTO.getFinalResDecFlag() != null 
						&& requestIcacDTO.getFinalResDecFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
					status = entityManager.find(Status.class,
							ReferenceTable.PREAUTH_ICAC_RESPONSE_FINAL_RECEIVED);	
				}else{
					status = entityManager.find(Status.class,
							ReferenceTable.PREAUTH_ICAC_RESPONSE_RECEIVED);
				}
				
			}else if(requestIcacDTO.getClaimDto() != null && requestIcacDTO.getClaimDto().getStageId() !=null 
					&& requestIcacDTO.getClaimDto().getStageId().equals(ReferenceTable.ENHANCEMENT_STAGE)){
				
				stage = entityManager.find(Stage.class,ReferenceTable.ENHANCEMENT_STAGE);
				if(requestIcacDTO.getFinalResDecFlag() != null 
						&& requestIcacDTO.getFinalResDecFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
					status = entityManager.find(Status.class,
							ReferenceTable.ENHANCEMENT_ICAC_RESPONSE_FINAL_RECEIVED);	
				}else{
					status = entityManager.find(Status.class,
							ReferenceTable.ENHANCEMENT_ICAC_RESPONSE_RECEIVED);
				}
			}else if (requestIcacDTO.getClaimDto() != null && requestIcacDTO.getClaimDto().getStageId() !=null 
					&& requestIcacDTO.getClaimDto().getStageId().equals(ReferenceTable.CLAIM_REQUEST_STAGE)){
				stage = entityManager.find(Stage.class,ReferenceTable.CLAIM_REQUEST_STAGE);
				
				if(requestIcacDTO.getFinalResDecFlag() != null 
						&& requestIcacDTO.getFinalResDecFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
					status = entityManager.find(Status.class,
							ReferenceTable.CLAIM_REQUEST_ICAC_RESPONSE_FINAL_RECEIVED);	
				}else{
					status = entityManager.find(Status.class,
							ReferenceTable.CLAIM_REQUEST_ICAC_RESPONSE_RECEIVED);
				}
				
			}else{
				stage = entityManager.find(Stage.class,requestIcacDTO.getClaimDto().getStageId());
				
				if(requestIcacDTO.getFinalResDecFlag() != null 
						&& requestIcacDTO.getFinalResDecFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
					status = entityManager.find(Status.class,
							ReferenceTable.PREAUTH_ICAC_RESPONSE_FINAL_RECEIVED);	
				}else{
					status = entityManager.find(Status.class,
							ReferenceTable.PREAUTH_ICAC_RESPONSE_RECEIVED);
				}
				
			}
			if(status != null && stage != null){
				icacRequest.setClaimStage(stage);
				icacRequest.setStatusId(status);
				MastersValue claimTypeId = new MastersValue();
				claimTypeId.setKey(requestIcacDTO.getClaimDto().getClaimType().getId());
				icacRequest.setClaimType(claimTypeId);
				icacRequest.setRequestRemark(requestIcacDTO.getDirectIcacRemarks());
				String srcStage = SHAConstants.ICAC_DIRECT_CASE;
				if(srcStage != null && !srcStage.isEmpty()){
					icacRequest.setIcacRequestSource(srcStage);
				}
				if(requestIcacDTO.getFinalResDecFlag() != null && requestIcacDTO.getFinalResDecFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
					icacRequest.setFinalDecFlag(SHAConstants.YES_FLAG);
					if(requestIcacDTO.getFinalResponseIcacRemark() != null){
						icacRequest.setFinalrequestRemark(requestIcacDTO.getFinalResponseIcacRemark());
					}
				}else{
					icacRequest.setFinalDecFlag(SHAConstants.N_FLAG);
				}
				icacRequest.setCreatedBy(userNameForDB);
				icacRequest.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				
				if(requestIcacDTO.getResponseIcacRemark() != null){
					icacRequest.setResponseRemark(requestIcacDTO.getResponseIcacRemark());
				}

				entityManager.persist(icacRequest);
				entityManager.flush();
			}

		}
		if(icacRequest.getKey() !=null){
			IcacResponse icacResponse = new IcacResponse();

			icacResponse.setIcacKey(icacRequest.getKey());
			icacResponse.setResponseRemarks(requestIcacDTO.getResponseIcacRemark());
			if(requestIcacDTO.getFinalResDecFlag() != null && requestIcacDTO.getFinalResDecFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
				icacResponse.setFinalDecFlag(SHAConstants.YES_FLAG);
				icacResponse.setResponseFinalRemarks(requestIcacDTO.getFinalResponseIcacRemark());
			}else{
				icacResponse.setFinalDecFlag(SHAConstants.N_FLAG);
			}
			icacResponse.setStatusId(icacRequest.getStatusId());
			icacResponse.setCreatedBy(userNameForDB);
			icacResponse.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			icacResponse.setRepliedBy(userNameForDB);
			icacResponse.setRepliedDate(new Timestamp(System.currentTimeMillis()));
			

			entityManager.persist(icacResponse);
			entityManager.flush();
		}
		
	}

	public void submitResponseTeamSubmit(SearchProcessICACTableDTO requestIcacDTO,String userID){
		String strUserName = userID;
		String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
		
		
		IcacRequest icacReqObj = getICACRequestObj(requestIcacDTO.getIcacKey());
		
		if(icacReqObj.getKey() != null ){
			Status status = null;
			Stage stage = null;
			if(icacReqObj.getClaimStage() != null && icacReqObj.getClaimStage().getKey() !=null 
					&& icacReqObj.getClaimStage().getKey().equals(ReferenceTable.PROCESS_PREAUTH)){
				stage = entityManager.find(Stage.class,ReferenceTable.PROCESS_PREAUTH);
				
				if(requestIcacDTO.getFinalResDecFlag() != null 
						&& requestIcacDTO.getFinalResDecFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
					status = entityManager.find(Status.class,
							ReferenceTable.PREAUTH_ICAC_RESPONSE_FINAL_RECEIVED);	
				}else{
					status = entityManager.find(Status.class,
							ReferenceTable.PREAUTH_ICAC_RESPONSE_RECEIVED);
				}
				
			}else if(icacReqObj.getClaimStage() != null && icacReqObj.getClaimStage().getKey() !=null 
					&& icacReqObj.getClaimStage().getKey().equals(ReferenceTable.ENHANCEMENT_STAGE)){
				
				stage = entityManager.find(Stage.class,ReferenceTable.ENHANCEMENT_STAGE);
				if(requestIcacDTO.getFinalResDecFlag() != null 
						&& requestIcacDTO.getFinalResDecFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
					status = entityManager.find(Status.class,
							ReferenceTable.ENHANCEMENT_ICAC_RESPONSE_FINAL_RECEIVED);	
				}else{
					status = entityManager.find(Status.class,
							ReferenceTable.ENHANCEMENT_ICAC_RESPONSE_RECEIVED);
				}
			}else if (icacReqObj.getClaimStage() != null && icacReqObj.getClaimStage().getKey() !=null 
					&& icacReqObj.getClaimStage().getKey().equals(ReferenceTable.CLAIM_REQUEST_STAGE)){
				stage = entityManager.find(Stage.class,ReferenceTable.CLAIM_REQUEST_STAGE);
				
				if(requestIcacDTO.getFinalResDecFlag() != null 
						&& requestIcacDTO.getFinalResDecFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
					status = entityManager.find(Status.class,
							ReferenceTable.CLAIM_REQUEST_ICAC_RESPONSE_FINAL_RECEIVED);	
				}else{
					status = entityManager.find(Status.class,
							ReferenceTable.CLAIM_REQUEST_ICAC_RESPONSE_RECEIVED);
				}
				
			}else{
				stage = entityManager.find(Stage.class,icacReqObj.getClaimStage().getKey());
				
				if(requestIcacDTO.getFinalResDecFlag() != null 
						&& requestIcacDTO.getFinalResDecFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
					status = entityManager.find(Status.class,
							ReferenceTable.PREAUTH_ICAC_RESPONSE_FINAL_RECEIVED);	
				}else{
					status = entityManager.find(Status.class,
							ReferenceTable.PREAUTH_ICAC_RESPONSE_RECEIVED);
				}
			}
			
			if(status != null && stage != null){
				icacReqObj.setClaimStage(stage);
				icacReqObj.setStatusId(status);
				
			}
			if(requestIcacDTO.getFinalResDecFlag() != null && requestIcacDTO.getFinalResDecFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
				icacReqObj.setFinalDecFlag(SHAConstants.YES_FLAG);
				if(requestIcacDTO.getFinalResponseIcacRemark() != null){
					icacReqObj.setFinalrequestRemark(requestIcacDTO.getFinalResponseIcacRemark());
				}
			}else{
				icacReqObj.setFinalDecFlag(SHAConstants.N_FLAG);
			}
			icacReqObj.setModifiedBy(userNameForDB);
			icacReqObj.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			
			if(requestIcacDTO.getResponseIcacRemark() != null){
				icacReqObj.setResponseRemark(requestIcacDTO.getResponseIcacRemark());
			}
			
			entityManager.merge(icacReqObj);
			entityManager.flush();
		}
		
		IcacResponse icacResponse = new IcacResponse();

		icacResponse.setIcacKey(requestIcacDTO.getIcacKey());
		icacResponse.setResponseRemarks(requestIcacDTO.getResponseIcacRemark());
		if(requestIcacDTO.getFinalResDecFlag() != null && requestIcacDTO.getFinalResDecFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
			icacResponse.setFinalDecFlag(SHAConstants.YES_FLAG);
			icacResponse.setResponseFinalRemarks(requestIcacDTO.getFinalResponseIcacRemark());
		}else{
			icacResponse.setFinalDecFlag(SHAConstants.N_FLAG);
		}
		icacResponse.setStatusId(icacReqObj.getStatusId());
		icacResponse.setCreatedBy(userNameForDB);
		icacResponse.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		icacResponse.setRepliedBy(userNameForDB);
		icacResponse.setRepliedDate(new Timestamp(System.currentTimeMillis()));
		entityManager.persist(icacResponse);
		entityManager.flush();
	}
	
	public IcacRequest getICACRequestObj(Long a_key) {
		IcacRequest find = entityManager.find(IcacRequest.class, a_key);
		entityManager.refresh(find);
		return find;
	}
	private String getSpecialityType(Long claimKey){
		try{
			StringBuffer specilityValue = new StringBuffer();
			Query findCpuCode = entityManager.createNamedQuery("Speciality.findByClaimKey").setParameter("claimKey", claimKey);
			List<Speciality> SpecialityList = findCpuCode.getResultList();
			for(Speciality speciality : SpecialityList){ 
				Query findSpecilty = entityManager.createNamedQuery("SpecialityType.findByKey").setParameter("key", speciality.getSpecialityType().getKey());
				SpecialityType result = (SpecialityType) findSpecilty.getSingleResult(); 
				specilityValue.append(result.getValue()).append(", ");
			}
			return specilityValue.toString();
			}catch(Exception e){
				return null;
			}
	}
	
	private DocUploadToPremia getUploadedDataDocument(String intimationNo)
	{
		Query query = entityManager.createNamedQuery("DocUploadToPremia.findByIntimationAndDocType");
		query = query.setParameter("intimationNo", intimationNo); 
		query.setParameter("docType", SHAConstants.PREMIA_DOC_TYPE_PRE_AUTHORISATION);
		List<DocUploadToPremia> listOfDocUploadData = query.getResultList();
		if(null != listOfDocUploadData && !listOfDocUploadData.isEmpty())
		{
			entityManager.refresh(listOfDocUploadData.get(0));
			return listOfDocUploadData.get(0);
		}
		return null;
	}
	

	private SearchProcessICACTableDTO getSpecilityAndTreatementType(SearchProcessICACTableDTO tableDTO, Long rodKey){
		if(null != tableDTO)
		{
			tableDTO.setSpeciality(getSpecialityType(tableDTO.getClaimKey()));
			tableDTO.setTreatmentType(getTreatementType(getAcknowledgementKey(rodKey)));
			tableDTO.setPreAuthReqAmt(getRequestAmt(rodKey).toString());
			
			List<UploadDocumentDTO> rodSummaryDetails = rodService.getRODSummaryDetails(rodKey);
			List<Long> documentSummaryKeys = new ArrayList<Long>();
			for (UploadDocumentDTO uploadDocumentDTO : rodSummaryDetails) {
				documentSummaryKeys.add(uploadDocumentDTO.getDocSummaryKey());
			}
			Double totalBilledAmount = reimbursementService.getTotalBilledAmount(documentSummaryKeys);
			if(totalBilledAmount != null){
				tableDTO.setClaimedAmountAsPerBill(totalBilledAmount);
			}
			
		}
		return tableDTO;
	}
	private Long getAcknowledgementKey(Long rodKey){
		try{
			
			Long value = 0L;
			Query findCpuCode = entityManager.createNamedQuery("DocAcknowledgement.findByRODKey").setParameter("rodKey", rodKey);
			List<DocAcknowledgement> SpecialityList = findCpuCode.getResultList();
			for(DocAcknowledgement docAcknowledgement : SpecialityList){ 
				 value = docAcknowledgement.getKey();
				
			}
			
			return value;
			}catch(Exception e){
				
				return null;
			}
	}
	private Double getRequestAmt(Long rodKey){
		try{
			Double claimedAmount = 0.0;
			Query findType = entityManager.createNamedQuery(
					"DocAcknowledgement.findByLatestAcknowledge").setParameter("rodKey", rodKey);
			List<DocAcknowledgement> reimbursement = (List<DocAcknowledgement>) findType
					.getResultList();
			
			DocAcknowledgement docAcknowledgement = new DocAcknowledgement();
		    if(reimbursement != null && ! reimbursement.isEmpty()){
		    	docAcknowledgement = reimbursement.get(0);
		    }
			
			Query findType1 = entityManager.createNamedQuery(
					"ReimbursementBenefits.findByRodKey").setParameter("rodKey", rodKey);
			List<ReimbursementBenefits> reimbursementBenefitsList = (List<ReimbursementBenefits>) findType1.getResultList();
			Double currentProvisionalAmount = 0.0;
			for(ReimbursementBenefits reimbursementBenefits : reimbursementBenefitsList){
				currentProvisionalAmount += reimbursementBenefits.getTotalClaimAmountBills();
				
			}
			Double hospitalizationClaimedAmount = null != docAcknowledgement.getHospitalizationClaimedAmount() ? docAcknowledgement.getHospitalizationClaimedAmount() : 0.0;
			Double postHospitalizationClaimedAmount = null != docAcknowledgement.getPostHospitalizationClaimedAmount() ? docAcknowledgement.getPostHospitalizationClaimedAmount() : 0.0;
			Double preHospitalizationClaimedAmount = null != docAcknowledgement.getPreHospitalizationClaimedAmount() ? docAcknowledgement.getPreHospitalizationClaimedAmount() : 0.0;
			claimedAmount = hospitalizationClaimedAmount + postHospitalizationClaimedAmount + preHospitalizationClaimedAmount+currentProvisionalAmount;
			
			return claimedAmount;
		} catch (Exception e) {
			return null;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public Reimbursement findRODByKey(Long claimKey) {
		Query rodByKey = entityManager.createNamedQuery("Reimbursement.findByClaimKey").setParameter("claimKey", claimKey);
		List<Reimbursement> reimbursements = (List<Reimbursement>) rodByKey.getResultList();
		if (reimbursements !=null && !reimbursements.isEmpty()) {
			return reimbursements.get(0);
		}
		return null;
	}
	private String getTreatementType(Long ackKey){
		try{
			StringBuffer specilityValue = new StringBuffer();
			String specialitst = "";
			Query findCpuCode = entityManager.createNamedQuery("Reimbursement.findByAcknowledgement").setParameter("docAcknowledgmentKey", ackKey);
			List<Reimbursement> SpecialityList = findCpuCode.getResultList();
			for(Reimbursement speciality : SpecialityList){ 
				System.out.println("treatement type"+speciality.getTreatmentType().getValue());
				specilityValue.append(speciality.getTreatmentType().getValue()).append(",");
			}
			
			if(specilityValue != null){
				specialitst = SHAUtils.removeLastChar(specilityValue.toString());
				
			}
			
			return specialitst;
			}catch(Exception e){
				
				return null;
			}
	}
	
	@SuppressWarnings("unchecked")
	public Boolean getTocheckIcacreqValidOrNot(String intitmationNum,List<String> icacSources) {
		
		Boolean isaAllow = getIsDirectCaseOrNot(intitmationNum);
		if(isaAllow){
			final CriteriaBuilder icacbuilder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<IcacRequest> criteriaIcacQuery = icacbuilder
					.createQuery(IcacRequest.class);

			Root<IcacRequest> search = criteriaIcacQuery.from(IcacRequest.class);

			List<Predicate> predicate = new ArrayList<Predicate>();

			if (intitmationNum != null && !intitmationNum.isEmpty()) {
				Predicate intimationPredicate = icacbuilder.equal(
						search.<String> get("intimationNum"),intitmationNum);
				predicate.add(intimationPredicate);
			}

			Predicate statusCondition =  search.<String>get("icacRequestSource").in(icacSources); 
			predicate.add(statusCondition);
			
			
			criteriaIcacQuery.select(search).where(
					icacbuilder.and(predicate.toArray(new Predicate[] {})));

			final TypedQuery<IcacRequest> searchIcacQuery = entityManager.createQuery(criteriaIcacQuery);
			List<IcacRequest> pageItemIcacList  = searchIcacQuery.getResultList();
			
			if(pageItemIcacList != null &&  pageItemIcacList.size() > 0){
				if(pageItemIcacList.get(0) != null){
					isaAllow = false;
				}
			}else{
					isaAllow = true;
			}
		}
		return isaAllow;
		
		
	}
	
	@SuppressWarnings("unchecked")
	public Boolean getIsDirectCaseOrNot(String intitmationNum) {
		
		final CriteriaBuilder icacbuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<IcacRequest> criteriaIcacQuery = icacbuilder
				.createQuery(IcacRequest.class);

		Root<IcacRequest> search = criteriaIcacQuery.from(IcacRequest.class);

		List<Predicate> predicate = new ArrayList<Predicate>();

		if (intitmationNum != null && !intitmationNum.isEmpty()) {
			Predicate intimationPredicate = icacbuilder.equal(
					search.<String> get("intimationNum"),intitmationNum);
			predicate.add(intimationPredicate);
		}

		Predicate statusCondition =  search.<String>get("icacRequestSource").in(SHAConstants.ICAC_DIRECT_CASE); 
		predicate.add(statusCondition);
		
		Predicate Flagcondition = icacbuilder.equal(search.<String>get("finalDecFlag"),"N");
		predicate.add(Flagcondition);
		
		criteriaIcacQuery.select(search).where(
				icacbuilder.and(predicate.toArray(new Predicate[] {})));

		final TypedQuery<IcacRequest> searchIcacQuery = entityManager.createQuery(criteriaIcacQuery);
		List<IcacRequest> pageItemIcacList  = searchIcacQuery.getResultList();
		
		if(pageItemIcacList != null &&  pageItemIcacList.size() > 0){
			if(pageItemIcacList.get(0) != null){
			return false;
			}
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public Boolean getTocheckIcacrDirestcase(String intitmationNum,List<String> icacSources) {
		
			final CriteriaBuilder icacbuilder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<IcacRequest> criteriaIcacQuery = icacbuilder
					.createQuery(IcacRequest.class);

			Root<IcacRequest> search = criteriaIcacQuery.from(IcacRequest.class);

			List<Predicate> predicate = new ArrayList<Predicate>();

			if (intitmationNum != null && !intitmationNum.isEmpty()) {
				Predicate intimationPredicate = icacbuilder.equal(
						search.<String> get("intimationNum"),intitmationNum);
				predicate.add(intimationPredicate);
			}

			Predicate statusCondition =  search.<String>get("icacRequestSource").in(icacSources); 
			predicate.add(statusCondition);
			
			Predicate Flagcondition = icacbuilder.equal(search.<String>get("finalDecFlag"),"N");
			predicate.add(Flagcondition);
			
			criteriaIcacQuery.select(search).where(
					icacbuilder.and(predicate.toArray(new Predicate[] {})));

			final TypedQuery<IcacRequest> searchIcacQuery = entityManager.createQuery(criteriaIcacQuery);
			List<IcacRequest> pageItemIcacList  = searchIcacQuery.getResultList();
			
			if(pageItemIcacList != null &&  pageItemIcacList.size() > 0){
				if(pageItemIcacList.get(0) != null){
					return  false;
				}
			}
		return true;
	}
	
}
