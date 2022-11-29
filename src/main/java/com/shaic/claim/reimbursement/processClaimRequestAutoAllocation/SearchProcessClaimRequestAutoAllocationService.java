package com.shaic.claim.reimbursement.processClaimRequestAutoAllocation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.Page;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.Claim;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Hospitals;
import com.shaic.domain.IncurredClaimRatio;
import com.shaic.domain.Intimation;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementBenefits;
import com.shaic.domain.ReimbursementQuery;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.preauth.Coordinator;
import com.shaic.domain.preauth.Speciality;
import com.shaic.domain.preauth.SpecialityType;
import com.shaic.domain.reimbursement.Specialist;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsTableDTO;
import com.shaic.reimbursement.medicalapproval.processclaimrequest.search.SearchProcessClaimRequestTableDTO;

@Stateless
public class SearchProcessClaimRequestAutoAllocationService {


	@PersistenceContext
	protected EntityManager entityManager;

	@EJB
	private DBCalculationService dbCalculationService;

	@EJB
	private MasterService masterService;

	Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
	
	private Reimbursement objReimbursement ;
	
	private final Logger log = LoggerFactory.getLogger(SearchProcessClaimRequestAutoAllocationService.class);
	
	public  Page<SearchProcessClaimRequestTableDTO> search(
			SearchProcessClaimRequestTableDTO searchFormDTO,
			String userName, String passWord,ImsUser imsUserID) {
		try 
		{
			log.info("%%%%%%%%%%%%%%%%%% STARTING TIME FOR COMMON BILLING FA AUTOALLOCATION%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+new Date());

			List<Map<String, Object>> taskProcedure = null ;		
			Integer totalRecords = 0; 
			List<String> intimationNoList = new ArrayList<String>();
			List<Long> rodList = new ArrayList<Long>();
			Long rodKey = null;
			String intimationNo = null;
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			mapValues.put(SHAConstants.USER_ID, userName);

			taskProcedure = dbCalculationService.getTaskProcedureForProcessClaimAutoAllocation(userName);	
			if (null != taskProcedure) {
				for (Map<String, Object> outPutArray : taskProcedure) {							
						Long keyValue = (Long) outPutArray.get(SHAConstants.PAYLOAD_ROD_KEY);
						String intimationNumber = (String) outPutArray.get(SHAConstants.INTIMATION_NO);
						workFlowMap.put(keyValue,outPutArray);
						intimationNoList.add(intimationNumber);
						rodList.add(keyValue);
						totalRecords = (Integer) outPutArray.get(SHAConstants.TOTAL_RECORDS);
				}
			}
			log.info("%%%%%%%%%%%%%%%%%%% BEFORE DATA FETCHING FROM DB %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+ new Date());
			List<SearchProcessClaimRequestTableDTO> tableDTO = new ArrayList<SearchProcessClaimRequestTableDTO>();
			
			for(int index = 0; index < intimationNoList.size(); index++){
				 if(index < rodList.size()){
					 intimationNo = intimationNoList.get(index);
					 rodKey = rodList.get(index);
					 tableDTO.add(getIntimationData(intimationNo, rodKey,imsUserID,userName));
				 }
			}
			log.info("%%%%%%%%%%%%%%%%%%% AFTER DATA FETCHING FROM DB %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+ new Date());
			
			Page<SearchProcessClaimRequestTableDTO> page = new Page<SearchProcessClaimRequestTableDTO>();
			page.setTotalRecords(totalRecords);
			page.setPageItems(tableDTO);
			if(null != searchFormDTO.getIntimationNo() || ("").equalsIgnoreCase(searchFormDTO.getIntimationNo()))
			{
				page.setSearchId(searchFormDTO.getIntimationNo());
			}
			
			if(!(null != page && null != page.getPageItems() && 0!= page.getPageItems().size()) && intimationNo != null && intimationNo.length() > 0) {
				Intimation intimationByNo = getIntimationByNo(intimationNo);
				if(intimationByNo == null) {
					page.setCorrectMsg("Intimation Not Found");
				} 
			}
			log.info("%%%%%%%%%%%%%%%%%%% END TIME OF GET TASK %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "+ new Date() + ": "+ System.currentTimeMillis());
			return page;
			}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
		}
			
		return null;	
	}
	
	public SearchProcessClaimRequestTableDTO getIntimationData(String intimationNo,  Long rodKey,ImsUser imsUserID,String userName){
		objReimbursement = getReimbursementByKey(rodKey); 
		Claim objClaim = objReimbursement.getClaim();
		try{

			SearchProcessClaimRequestTableDTO tableDTO = new SearchProcessClaimRequestTableDTO();

			populateClaimData(tableDTO, objReimbursement.getClaim());
			tableDTO.setCoverCode(objClaim.getClaimCoverCode());
			tableDTO.setSubCoverCode(objClaim.getClaimSubCoverCode());
			tableDTO.setSectionCode(objClaim.getClaimSectionCode());
			tableDTO.setUsername(userName);
			getSpecilityAndTreatementType(tableDTO, rodKey);
			getHospitalDetails(tableDTO, rodKey);
			tableDTO.setImsUser(imsUserID);
			tableDTO.setIsAutoAllocationTrue(true);
			return tableDTO;
		}catch(Exception e){
			return null;
		}
		
	}
	
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}
	

	private void  getHospitalDetails(
			SearchProcessClaimRequestTableDTO tableDTO, Long rodKey) {
	try
		{
			if(null != tableDTO && null != rodKey)
			{
				tableDTO.setRodKey(rodKey);
				Hospitals hospitalDetail;
				Object workflowKey = workFlowMap.get(rodKey);
				Map<String, Object> outPutArray = (Map<String, Object>)workflowKey;
				String claimedAmount = (String) outPutArray.get(SHAConstants.CLAIMED_AMOUNT);
				if(claimedAmount != null){
					tableDTO.setClaimedAmountAsPerBill(Double.valueOf(claimedAmount));
				}
				tableDTO.setDbOutArray(workflowKey);
				if(null != objReimbursement)
				{
					objReimbursement = getReimbursementByKey(rodKey);
					if(objReimbursement.getDocAcknowLedgement() != null && objReimbursement.getDocAcknowLedgement().getDocumentReceivedFromId() != null
							&& objReimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getValue() != null){
						tableDTO.setDocumentReceivedFrom(objReimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getValue());
					}
					String rodAgeing = SHAUtils.getRodAgeing(objReimbursement.getCreatedDate());
				 	tableDTO.setRodAgeing(rodAgeing);
					tableDTO.setOriginatorID(objReimbursement.getModifiedBy());
					
					 String ackAgeing = SHAUtils.getAckAgeing(objReimbursement.getDocAcknowLedgement().getCreatedDate());
					 tableDTO.setAckAgeing(ackAgeing);
					 tableDTO.setRodNo(objReimbursement.getRodNumber());

					TmpEmployee employeeName = getEmployeeName(objReimbursement.getModifiedBy() != null ? objReimbursement.getModifiedBy().toLowerCase() : "");
				 	if(employeeName != null) {
				 		tableDTO.setOriginatorName(employeeName.getEmpFirstName());
				 	}
				 	
				 	if(objReimbursement != null && objReimbursement.getStatus() != null && (objReimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_SPECIALIST_REPLY_RECEIVED_STATUS))) {
						Specialist specialistByClaimKey = getSpecialistByClaimKey(objReimbursement.getClaim().getKey());
						if(specialistByClaimKey != null) {
							 TmpEmployee specialistRequestedEmp = getEmployeeName(specialistByClaimKey.getCreatedBy() != null ? specialistByClaimKey.getCreatedBy().toLowerCase() : "");
							 tableDTO.setSpecialistOrQueryOrCoordinatorReqId(specialistByClaimKey.getCreatedBy());
							 tableDTO.setSpecialistOrQueryOrCoordinatorReqName(specialistRequestedEmp != null ? specialistRequestedEmp.getEmpFirstName() : "");
						}
					} else if(objReimbursement != null && objReimbursement.getStatus() != null && (objReimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_COORDINATOR_REPLY_RECEIVED_STATUS))) {
						 Coordinator findCoordinatorByClaimKey = findCoordinatorByClaimKey(objReimbursement.getClaim().getKey());
						 if(findCoordinatorByClaimKey != null) {
							 TmpEmployee coordinatorRequestedEmp = getEmployeeName(findCoordinatorByClaimKey.getCreatedBy() != null ? findCoordinatorByClaimKey.getCreatedBy().toLowerCase() : "");
							 tableDTO.setSpecialistOrQueryOrCoordinatorReqId(findCoordinatorByClaimKey.getCreatedBy());
							 tableDTO.setSpecialistOrQueryOrCoordinatorReqName(coordinatorRequestedEmp != null ? coordinatorRequestedEmp.getEmpFirstName(): "");
						}
					 } else {
						 ReimbursementQuery reimbursementQueryDocAckKey = getReimbursementQueryDocAckKey(objReimbursement.getDocAcknowLedgement() != null ? objReimbursement.getDocAcknowLedgement().getKey() : 0l);
						 if(reimbursementQueryDocAckKey != null) {
							 TmpEmployee queryRequestedEmp = getEmployeeName(reimbursementQueryDocAckKey.getCreatedBy() != null ? reimbursementQueryDocAckKey.getCreatedBy().toLowerCase() : "");
							 tableDTO.setSpecialistOrQueryOrCoordinatorReqId(reimbursementQueryDocAckKey.getCreatedBy());
							 tableDTO.setSpecialistOrQueryOrCoordinatorReqName(queryRequestedEmp != null ? queryRequestedEmp.getEmpFirstName(): "");
						 }
					 } 
					 tableDTO.setBalanceSI(getBalanceSI(tableDTO.getPolicyKey(),tableDTO.getInsuredKey(), tableDTO.getClaimKey(),rodKey));
					 
					 Query findByHospitalKey = entityManager.createNamedQuery("Hospitals.findByKey").setParameter("key", tableDTO.getHospitalNameID());
					 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
					 if(hospitalDetail != null){
						 tableDTO.setHospitalName(hospitalDetail.getName());
					 }
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private Reimbursement getType(Long key){
		try{
		Query findType = entityManager.createNamedQuery("Reimbursement.findByKey").setParameter("primaryKey", key);
		Reimbursement reimbursement = (Reimbursement) findType.getSingleResult();
		return reimbursement;
		}catch(Exception e){
			
		}
		return null;
	}
	

	private MastersValue getLOBValue(Long getlOBId) {
		try{
		Query query = entityManager
				.createNamedQuery("MastersValue.findByKey");
		query = query.setParameter("parentKey", getlOBId);
		MastersValue value = (MastersValue) query.getSingleResult();
	    return value;
		}catch(Exception e){
			
		}
		return null;
	}
	
	
	
	public Claim getClaimByIntimation(Long intimationKey) {
		List<Claim> a_claimList = new ArrayList<Claim>();
		if (intimationKey != null) {

			Query findByIntimationKey = entityManager.createNamedQuery("Claim.findByIntimationKey");
			findByIntimationKey = findByIntimationKey.setParameter("intimationKey", intimationKey);
			try {

				a_claimList = (List<Claim>) findByIntimationKey.getResultList();
				
				for (Claim claim : a_claimList) {
					entityManager.refresh(claim);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} 
		} else {
			// intimationKey null
		}
		return a_claimList.get(0);

	}
	
	public IncurredClaimRatio getIncurredClaimRatio(String policyNumber, String insuredNumber){
		
		Query query = entityManager
				.createNamedQuery("IncurredClaimRatio.findByPolicyNumber");
		query.setParameter("policyNumber", policyNumber);
		List<IncurredClaimRatio> result = (List<IncurredClaimRatio>) query.getResultList();
		if(result != null && ! result.isEmpty()){
			return result.get(0);
		}
		return null;
		
	}


	public String getColorCodeForGMC(String policyNumber, String insuredNumber){
		IncurredClaimRatio incurredClaimRatio = getIncurredClaimRatio(policyNumber, insuredNumber);
		if(incurredClaimRatio != null){
			return incurredClaimRatio.getClaimColour();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Intimation getIntimationByNo(String intimationNo) {
		Query findByKey = entityManager.createNamedQuery(
				"Intimation.findByIntimationNumber").setParameter(
				"intimationNo", intimationNo);

		List<Intimation> intimationList = (List<Intimation>) findByKey
				.getResultList();

		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);

		}
		return null;
	}
	private void populateClaimData(SearchProcessClaimRequestTableDTO dto , Claim objClaim)
	{
		dto.setClaimKey(objClaim.getKey());
		Intimation objIntimation = objClaim.getIntimation();
		Policy objPolicy = objIntimation.getPolicy();
		dto.setIntimationKey(objIntimation.getKey());
		dto.setIntimationNo(objIntimation.getIntimationId());
		dto.setPolicyKey(objPolicy.getKey());
		dto.setPolicyNumber(objPolicy.getPolicyNumber());
		dto.setSumInsured(objPolicy.getTotalSumInsured());
		dto.setInsuredPatientName(objIntimation.getInsured().getInsuredName());
		
		if(objIntimation != null){
			if(ReferenceTable.getGMCProductList().containsKey(objIntimation.getPolicy().getProduct().getKey())){
				String colorCodeForGMC = getColorCodeForGMC(objIntimation.getPolicy().getPolicyNumber(), objIntimation.getInsured().getInsuredId().toString());
				dto.setColorCode(colorCodeForGMC);
			}
		}
		dto.setCrmFlagged(objClaim.getCrcFlag());
		
		if(dto.getCrmFlagged() != null){
			if(dto.getCrmFlagged().equalsIgnoreCase("Y")){
				if (objClaim.getCrcFlag() != null && objClaim.getCrcFlag().equalsIgnoreCase("Y")) {
					dto.setColorCodeCell("OLIVE");
				}
				dto.setCrmFlagged(null);
			}
		}
		if (objClaim.getIsVipCustomer() != null && objClaim.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
			dto.setColorCodeCell("VIP");
		}
		if (objClaim.getCrcFlag() != null && objClaim.getCrcFlag().equalsIgnoreCase("Y") 
				&& objClaim.getIsVipCustomer() != null && objClaim.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
			dto.setColorCodeCell("CRMVIP");
		}
		
		dto.setHospitalNameID(objIntimation.getHospital());
		dto.setProductName(objPolicy.getProductName());
		dto.setInsuredKey(objIntimation.getInsured().getKey());
		dto.setCpuName(objIntimation.getCpuCode().getDescription());
		MastersValue sourceValue = objIntimation.getIntimationSource();
		if(null != sourceValue)
			dto.setIntimationSource(sourceValue.getValue());
		MastersValue hospital = objIntimation.getHospitalType();
		if(null != hospital)
			dto.setHospitalType(hospital.getValue());
		dto.setReasonForAdmission(objIntimation.getAdmissionReason());
		MastersValue claimType = objClaim.getClaimType();
		if(null != claimType)
			dto.setClaimType(claimType.getValue());
	}

	private void getSpecilityAndTreatementType(SearchProcessClaimRequestTableDTO tableDTO, Long rodKey){
		if(null != tableDTO)
		{
			tableDTO.setSpeciality(getSpecialityType(tableDTO.getClaimKey()));
			tableDTO.setTreatementType(getTreatementType(getAcknowledgementKey(rodKey)));
			tableDTO.setRequestedAmt(getRequestAmt(rodKey));
			if(objReimbursement != null){
				tableDTO.setOriginatorID(objReimbursement.getCreatedBy());
			}
		}
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
	public Reimbursement getReimbursementByKey(Long rodKey){
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByKey").setParameter(
						"primaryKey", rodKey);
		List<Reimbursement> rodList = query.getResultList();
		
		if(rodList != null && !rodList.isEmpty()) {
			for (Reimbursement reimbursement : rodList) {
				entityManager.refresh(reimbursement);
			}
			return rodList.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public ReimbursementQuery getReimbursementQueryDocAckKey(Long rodKey){
		Query query = entityManager
				.createNamedQuery("ReimbursementQuery.findLatestDocAckKey").setParameter(
						"docAckKey", rodKey);
		List<ReimbursementQuery> rodList = query.getResultList();
		
		if(rodList != null && !rodList.isEmpty()) {
			for (ReimbursementQuery reimbursement : rodList) {
				entityManager.refresh(reimbursement);
			}
			return rodList.get(0);
		}
		return null;
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
	
	public Specialist getSpecialistByClaimKey(Long claimKey){
		
		Query query = entityManager
				.createNamedQuery("Specialist.findByClaimKey");
		query.setParameter("claimKey", claimKey);
		List<Specialist> resultList = (List<Specialist>) query
				.getResultList();
		
		if(resultList != null && ! resultList.isEmpty()){
			return resultList.get(0);
		}
		return null;
		
	}
	
	public Coordinator findCoordinatorByClaimKey(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("Coordinator.findByClaimKey");
		query.setParameter("claimKey", claimKey);
		List resultList = query.getResultList();
		Coordinator object = null;
		if(!resultList.isEmpty()) {
			 object = (Coordinator) resultList.get(0);
		}
		return object;
	}
	
	  private TmpEmployee getEmployeeName(String initiatorId)
	  {
		  TmpEmployee fvrInitiatorDetail;
		  Query findByTransactionKey = entityManager.createNamedQuery(
				  "TmpEmployee.getEmpByLoginId").setParameter("loginId", initiatorId);
		  try{
			  fvrInitiatorDetail =(TmpEmployee) findByTransactionKey.getSingleResult();
			  return fvrInitiatorDetail;
		  }
		  catch(Exception e)
		  {
			  return null;
		  }

	  }
	  
	  private Double getBalanceSI(Long policyKey, Long insuredKey, Long claimKey,Long rodKey){
			Double balanceSI = 0.0;
			if(policyKey !=null && insuredKey != null){
				Map<String, Double> balanceSIForReimbursement = dbCalculationService.getBalanceSIForReimbursement(policyKey, insuredKey, claimKey, rodKey);
				if(null != balanceSIForReimbursement && !balanceSIForReimbursement.isEmpty()){
					balanceSI = balanceSIForReimbursement.get(SHAConstants.TOTAL_BALANCE_SI);
				}
			}
					
			return balanceSI;
		}
}
