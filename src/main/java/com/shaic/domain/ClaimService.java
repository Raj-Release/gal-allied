package com.shaic.domain;


import static com.shaic.domain.ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.poifs.storage.RawDataBlock;
import org.json.JSONObject;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.validation.ValidatorUtils;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.intimation.rule.IntimationRule;
import com.shaic.claim.policy.search.ui.BancsSevice;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;
import com.shaic.claim.registration.GenerateCoveringLetterSearchTableDto;
import com.shaic.claim.registration.SearchClaimRegistrationTableDto;
import com.shaic.claim.registration.convertClaim.search.SearchConvertClaimTableDto;
import com.shaic.claim.registration.convertclaimcashless.SearchConverClaimCashlessTableDTO;
import com.shaic.claim.reimbursement.dto.ExtraEmployeeEffortDTO;
import com.shaic.claim.reimbursement.rrc.services.ReviewRRCRequestService;
import com.shaic.claim.reports.claimsdailyreportnew.ClaimsDailyReportDto;
import com.shaic.claim.reports.medicalAuditCashlessIssueanceReport.MedicalAuditCashlessIssuanceReportDto;
import com.shaic.claim.reports.shadowProvision.SearchShadowProvisionDTO;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.viewEarlierRodDetails.EsclateToRawTableDTO;
import com.shaic.domain.outpatient.OPHealthCheckup;
import com.shaic.domain.preauth.CashlessWorkFlow;
import com.shaic.domain.preauth.ClaimLimit;
import com.shaic.domain.preauth.CloseClaim;
import com.shaic.domain.preauth.Diagnosis;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.ProvisionUploadHistory;
import com.shaic.domain.preauth.Speciality;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.preauth.TmpFvR;
import com.shaic.domain.preauth.UserLoginDetails;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.gpaclaim.unnamedriskdetails.UnnamedRiskDetailsPageDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.ConvertClaimMapper;
import com.shaic.newcode.wizard.domain.PreviousClaimMapper;
import com.shaic.newcode.wizard.domain.ProcessRejectionMapper;
import com.shaic.newcode.wizard.dto.ConvertClaimDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.newcode.wizard.dto.ProcessRejectionDTO;
import com.shaic.restservices.bancs.lockPolicy.LockPolicyIntegrationService;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource.Builder;
import com.vaadin.v7.data.util.BeanItemContainer;
//import com.shaic.ims.bpm.claim.ped.PedReq;

@Stateless
public class ClaimService {

	@PersistenceContext
	protected EntityManager entityManager;

	private IntimationService intimationService ;
	
	private FieldVisitRequest fvrRequest ;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private ReviewRRCRequestService requestService;

	public ClaimService() {
		super();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void create(Claim claim) {
		if (claim != null) {
			if(null == claim.getKey()){
				
				if((ReferenceTable.HEALTH_LOB_KEY).equals(claim.getIntimation().getPolicy().getLobId())){
					claim.setCurrentProvisionAmount(0d);	
				}				
				System.out.println("----claim key"+claim.getKey());
				entityManager.persist(claim);
				entityManager.flush();
				entityManager.refresh(claim);
			}
			else if(claim != null && claim.getKey() != null ){
				
				System.out.println("----claim key in case of merge---"+claim.getKey());
				Claim createdClaim = entityManager.find(Claim.class, claim.getKey());
				entityManager.refresh(createdClaim);
				createdClaim.setModifiedDate(new Date());
				createdClaim.setCurrentProvisionAmount(claim.getCurrentProvisionAmount());
				createdClaim.setIsVipCustomer(claim.getIsVipCustomer());
				createdClaim.setRegistrationRemarks(claim.getRegistrationRemarks());
				createdClaim.setClaimedAmount(claim.getClaimedAmount());
				entityManager.merge(createdClaim);
				entityManager.flush();
				entityManager.refresh(createdClaim);
			}
			
//			entityManager.flush();
//			entityManager.refresh(claim);
		}
	}
	
	public DocumentDetails getQueryDocTypeByIntimation(String docType) {
		Query query = entityManager.createNamedQuery(
				"DocumentDetails.findQueryByIntimationNo").setParameter("intimationNumber", docType);
		DocumentDetails documentDetail = (DocumentDetails) query.getSingleResult();
		if (documentDetail != null) {
			return documentDetail;
		}
		return null;
	}

	public Claim searchByClaimNum(String claimNumber) {
		Query findByClaimNumber = entityManager.createNamedQuery(
				"Claim.findByClaimNumber").setParameter("claimNumber",
				claimNumber);

		Claim resultClaim = (Claim) findByClaimNumber.getResultList();

		return resultClaim;

	}
	
	public OPClaim searchByOPClaimNum(String claimNumber) {
		Query findByClaimNumber = entityManager.createNamedQuery("OPClaim.findByClaimNumber").setParameter("claimNumber", claimNumber);
		OPClaim returnObj = null;
		List<OPClaim> resultClaim = (List<OPClaim>) findByClaimNumber.getResultList();
		if(resultClaim != null && resultClaim.size() > 0){
			returnObj = resultClaim.get(0);
		}else{
			returnObj = null;
		}
		return returnObj;
	}
	
	@SuppressWarnings("unchecked")
	public String getSpecialityName(Long key){
		
		Query findByClaimNumber = entityManager.createNamedQuery(
				"Speciality.findByClaimKey").setParameter("claimKey",
						key);

		List<Speciality> resultClaim = (List<Speciality>) findByClaimNumber.getResultList();
		StringBuffer specialityName = new StringBuffer();
		for (Speciality speciality : resultClaim) {
			if(speciality.getSpecialityType() != null){
			specialityName.append(speciality.getSpecialityType().getValue()).append(",");
			}
		}
		return specialityName.toString();
	}
	
	public Hospitals getHospitalDetailsByKey(Long hospitalKey) {
		Query query = entityManager.createNamedQuery(
				"Hospitals.findByHospitalKey").setParameter("key", hospitalKey);
		Hospitals hospitals = (Hospitals) query.getSingleResult();
		if (hospitals != null) {
			return hospitals;
		}
		return null;
	}
	
	public void saveCoveringLetterDocList(List<DocumentCheckListDTO> docCheckList, Claim find){
		
		PACoveringLetterDocument paCoveringDoc = null;
		try{
			if(docCheckList != null && !docCheckList.isEmpty()){
				for (DocumentCheckListDTO documentCheckListDTO : docCheckList) {
					paCoveringDoc = new PACoveringLetterDocument();
					paCoveringDoc.setClaim(find);
					paCoveringDoc.setDocMaster(documentCheckListDTO.getParticulars() != null  && documentCheckListDTO.getParticulars().getId() != null ? findDocByKey(documentCheckListDTO.getParticulars().getId()) : null);
					paCoveringDoc.setDocDescription(documentCheckListDTO.getParticulars() != null  && documentCheckListDTO.getParticulars().getValue() != null ? documentCheckListDTO.getParticulars().getValue() : "");
					paCoveringDoc.setCreatedDate(new Date());
					paCoveringDoc.setCreatedBy(find.getModifiedBy());
					paCoveringDoc.setActiveStatus(1);
					entityManager.persist(paCoveringDoc);
					entityManager.flush();
				}
			}	
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private DocumentCheckListMaster findDocByKey(Long docKey){
		DocumentCheckListMaster result = null;
		try{
			
			Query findByDocumentKey = entityManager.createNamedQuery("DocumentCheckListMaster.findByKey");
			findByDocumentKey = findByDocumentKey.setParameter("primaryKey", docKey);
			List<DocumentCheckListMaster> documentMasterList = (List<DocumentCheckListMaster>) findByDocumentKey.getResultList();
			
			if(documentMasterList != null && ! documentMasterList.isEmpty()){
				result = documentMasterList.get(0);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	public void setOutComeForCoveringLetterTask(Object outcome){
		Map<String,Object> taskoutCome = (Map<String,Object>)outcome;
		NewIntimationDto newIntimationDto = (NewIntimationDto) taskoutCome.get("fvrDetailsBean");
		
		try{
			String userId = taskoutCome.get(BPMClientContext.USERID).toString();
			String password = taskoutCome.get(BPMClientContext.PASSWORD).toString();
			GenerateCoveringLetterSearchTableDto bean = (GenerateCoveringLetterSearchTableDto)taskoutCome.get("Bean");
			if(null != bean && bean.getClaimDto() != null && bean.getClaimDto().getKey() != null){
				Claim find = entityManager.find(Claim.class, bean.getClaimDto().getKey());
				entityManager.refresh(find);	
				find.setModifiedBy(userId);
				find.setModifiedDate(new Date());
				find.setConversionLetter(bean.getClaimDto().getConversionLetter());
				if(ReferenceTable.PA_LOB_KEY.equals(find.getIntimation().getPolicy().getLobId())){
					saveCoveringLetterDocList(bean.getClaimDto().getDocumentCheckListDTO(),find);
				}	
				entityManager.merge(find);
				entityManager.flush();
			
/*			bean.getHumanTask().setOutcome(taskoutCome.get("OUTCOME").toString());
//			
//			if(autoRegisterFVR(newIntimationDto,userId))
//			{
//			PayloadBOType payloadBO = bean.getHumanTask().getPayloadCashless();
//			//com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType payloadBO = bean.getHumanTask().getPayload();
//			
//			FieldVisitType fieldVisit = new FieldVisitType();
//				if (null != fvrRequest && null != fvrRequest.getKey()) {
//					fieldVisit.setKey(fvrRequest.getKey());
//					if(null == payloadBO)
//					{
//						payloadBO = new PayloadBOType();
//					}
//				//payloadBO.setFieldVisit(fiel);
//				bean.getHumanTask().getPayloadCashless().setFieldVisit(fieldVisit);
////				bean.getHumanTask().setPayloadCashless(payloadBO);
//				}
//			}
			
	//		InvokeHumanTask invokeHT=new InvokeHumanTask();
	//		invokeHT.execute(userId,password,bean.getHumanTask(),null,null,null,null);
			
//			com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType reimbPayLoad = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType();
*/
			
		/*		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.policy.PolicyType policyBo = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.policy.PolicyType();
				
				policyBo.setPolicyId(newIntimationDto.getPolicy().getPolicyNumber());
				reimbPayLoad.setPolicy(policyBo);
				
				com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.intimation.IntimationType intimationBo = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.intimation.IntimationType();
				intimationBo.setIntimationNumber(newIntimationDto.getIntimationId());
				intimationBo.setKey(newIntimationDto.getKey());
				reimbPayLoad.setIntimation(intimationBo);
			}
			
				com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claimrequest.ClaimRequestType claimReq = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claimrequest.ClaimRequestType();
				if(null != newIntimationDto.getCpuId())
				{
					claimReq.setCpuCode(String.valueOf(newIntimationDto.getCpuCode()));
				}	
				claimReq.setKey(bean.getClaimDto().getKey());
				claimReq.setOption(SHAConstants.BILLS_NOT_RECEIVED); 
				reimbPayLoad.setClaimRequest(claimReq);				
				
				com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claim.ClaimType claimBo = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claim.ClaimType();
				claimBo.setClaimId(bean.getClaimDto().getClaimId());
				claimBo.setKey(bean.getClaimDto().getKey());
				claimBo.setClaimType(bean.getClaimDto().getClaimType() != null ? (bean.getClaimDto().getClaimType().getValue() != null ? bean.getClaimDto().getClaimType().getValue().toUpperCase(): "") : "");		
				reimbPayLoad.setClaim(claimBo);
				
				com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.query.QueryType queryType = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.query.QueryType();
				reimbPayLoad.setQuery(queryType);
			
			com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.classification.ClassificationType callsificationBo = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.classification.ClassificationType();
			
			callsificationBo.setPriority("");
			callsificationBo.setSource("");
			callsificationBo.setType("");
			reimbPayLoad.setClassification(callsificationBo);
			
			ProductInfoType productBO = new ProductInfoType();
			
			if(ReferenceTable.HEALTH_LOB_KEY.equals(newIntimationDto.getPolicy().getLobId())){
				productBO.setLob(SHAConstants.HEALTH_LOB);	
			}
			else{
				productBO.setLob(SHAConstants.PA_LOB);
			}		
			reimbPayLoad.setProductInfo(productBO);
			
			SubmitGenCovLetterRegTask submitcovertingLetterTask = BPMClientContext.getSubmitGenerateCoveringLetterTask(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD);
			
//			CLReminder  cashlessReminderTask = BPMClientContext.getCashlessReimnderLetterInitiateTask(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD);
			
			ReimbReminder reimburseReimnderLetterInitiateTask = BPMClientContext.getReimburseReimnderLetterInitiateTask(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);*/
				
			
			
				Map<String, Object> wrkFlowMap = (Map<String, Object>) bean.getDbOutArray();
				if(wrkFlowMap != null){
				wrkFlowMap.put(SHAConstants.DB_CLAIM_KEY, bean.getClaimDto().getKey());
				wrkFlowMap.put(SHAConstants.DB_CLAIM_NUMBER, bean.getClaimDto().getClaimId());
				wrkFlowMap.put(SHAConstants.CLAIM_TYPE, bean.getClaimDto().getClaimType().getValue());
				wrkFlowMap.put(SHAConstants.PAYLOAD_REGISTRATION_DATE, bean.getClaimDto().getCreatedDate());
				wrkFlowMap.put(SHAConstants.PROCESS_TYPE, bean.getClaimDto().getClaimType().getValue());
				wrkFlowMap.put(SHAConstants.USER_ID, userId);
				wrkFlowMap.put(SHAConstants.OUTCOME, taskoutCome.get("OUTCOME").toString());
				
				Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
				DBCalculationService dbCalService = new DBCalculationService();
				dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);	
				
				if(bean.getClaimDto().getDocFilePath() != null){
					updateClaim(bean.getClaimDto().getKey());
					uploadCoveringLetterToDMs(bean.getClaimDto());
				}
				
				}
				
//			reimburseReimnderLetterInitiateTask.initiate(BPMClientContext.BPMN_TASK_USER, reimbPayLoad);
//			submitcovertingLetterTask.execute(userId,bean.getHumanTask());
			}
			}catch(Exception e){
				e.printStackTrace();
			}		
//		Claim claim = entityManager.find(Claim.class, bean.getClaimDto().getKey());
		
	}
	
	private Boolean autoRegisterFVR(NewIntimationDto newIntimationDto, String userName)
	{
		try
		{
			fvrRequest = new FieldVisitRequest();

			Query findByIntimationKey = entityManager
					.createNamedQuery("Claim.findByIntimationKey");
			findByIntimationKey = findByIntimationKey.setParameter(
					"intimationKey", newIntimationDto.getKey());
			Intimation objIntimation = entityManager.find(Intimation.class, newIntimationDto.getKey());
			Claim claim = (Claim) findByIntimationKey.getSingleResult();
			
			
			Stage objStage = new Stage();
			objStage.setKey(ReferenceTable.CLAIM_REGISTRATION_STAGE);
			
			Status fvrStatus = new Status();
			fvrStatus.setKey(ReferenceTable.INITITATE_FVR);
			
			if(claim != null && claim.getIntimation() != null){
				Intimation intimation = claim.getIntimation();
				Long hospital = intimation.getHospital();
				
				Hospitals hospitalById = getHospitalById(hospital);
				
				TmpCPUCode tmpCPUCode = getTmpCPUCode(hospitalById.getCpuId());
				if(tmpCPUCode != null){
					fvrRequest.setFvrCpuId(tmpCPUCode.getCpuCode());
				}
				
				
			}
			
			MastersValue value = new MastersValue();
			value.setKey(ReferenceTable.FVR_ALLOCATION_TO);
			value.setValue(SHAConstants.FVR_ALLOCATION_ANY_ONE);
			fvrRequest.setAllocationTo(value);
			fvrRequest.setIntimation(objIntimation);
			fvrRequest.setClaim(claim);
			fvrRequest.setCreatedBy(userName);
			fvrRequest.setFvrTriggerPoints(SHAConstants.REGISTERED_CLAIM);
			fvrRequest.setPolicy(newIntimationDto.getPolicy());
			fvrRequest.setAllocationTo(value);
			fvrRequest.setActiveStatus(1L);
			fvrRequest.setOfficeCode(newIntimationDto.getPolicy().getHomeOfficeCode());	
			fvrRequest.setTransactionFlag("R");
			fvrRequest.setStatus(fvrStatus);
			fvrRequest.setStage(objStage);
			entityManager.persist(fvrRequest);
			entityManager.flush();
//			this.fvrRequest = fvrRequest;
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public TmpCPUCode getTmpCPUCode(Long cpuId){
		try{
		Query findCpuCode = entityManager.createNamedQuery("TmpCPUCode.findByKey").setParameter("cpuId", cpuId);
		 TmpCPUCode tmpCPUCode = (TmpCPUCode) findCpuCode.getSingleResult();
		return tmpCPUCode;
		}catch(Exception e){
				
		}
		return null;
	}
	
	public Hospitals getHospitalById(Long key){
		
		Query query = entityManager.createNamedQuery("Hospitals.findByKey");
		query.setParameter("key", key);
		
		List<Hospitals> resultList = (List<Hospitals>) query.getResultList();
		
		if(resultList != null && ! resultList.isEmpty()){
			return resultList.get(0);
		}
		
		return null;
		
	}

//	public ClaimDto submitClaim(ClaimDto claimDto) {
//		Claim claim = new ClaimMapper().getClaim(claimDto);
//		create(claim);
//		entityManager.flush();
//		claimDto.setKey(claim.getKey());
//		claimDto.setStageId(claim.getStage().getKey());
//		claimDto.setStatusId(claim.getStatus().getKey());
//		claimDto.setClaimId(claim.getClaimId());
//
//		return claimDto;
//	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ClaimDto submitClaim(SearchClaimRegistrationTableDto resultTask, ClaimDto claimDto) {
		claimDto.setCreatedBy(resultTask.getUsername() != null ? SHAUtils.getUserNameForDB(resultTask.getUsername()) : "");
		
		// R1053  -  GHI Allow Reg.  12-04-2018
		claimDto.setGhiAllowFlag(SHAConstants.YES_FLAG);
		claimDto.setGhiAllowUser(claimDto.getCreatedBy());
		
		
		Claim claim =  ClaimMapper.getInstance().getClaim(claimDto);
		System.out.println("----the intimation key for created claim ----"+claim.getIntimation().getKey());
		Claim createdClaim = getClaimforIntimation(claim.getIntimation().getKey());
		if(createdClaim != null && createdClaim.getIntimation().getKey() == claimDto.getNewIntimationDto().getKey()){
			claim.setKey(createdClaim.getKey());
		}
		
		/******
		 * As per Prakash, We have set the Date of admission from Intimation to
		 * Claim while creating the Claim .....
		 **************/
		claim.setDataOfAdmission(claimDto.getNewIntimationDto().getAdmissionDate());
		

		Policy policyByKey = getPolicyByKey(claimDto.getNewIntimationDto().getPolicy().getKey());
		
//		Double amt =  claimAdvProvision.getAvgAmt();
		
		NewIntimationDto newIntimationDto = claimDto.getNewIntimationDto();

		claim.setNormalClaimFlag("O");
		
		if(claimDto.getNewIntimationDto().getLobId() != null){
		
			claim.setLobId(claimDto.getNewIntimationDto().getLobId().getId());
		}
		//added by noufel for setting priority event
		if(claimDto.getPriorityWeightage() != null){
			
			claim.setPriorityWeightage(claimDto.getPriorityWeightage());
		}
		if(claimDto.getPriorityEvent() != null && !claimDto.getPriorityEvent().isEmpty()){
			
			claim.setPriorityEvent(claimDto.getPriorityEvent());
		}
		//code added by noufel for adding CMD memeber at register level
		if(claimDto.getNewIntimationDto() != null && claimDto.getNewIntimationDto().getPolicy() != null && claimDto.getNewIntimationDto().getPolicy().getKey() != null) {
			DBCalculationService dbCalService = new DBCalculationService();
			String memberType = dbCalService.getCMDMemberType(claimDto.getNewIntimationDto().getPolicy().getKey());
			claim.setClaimClubMember(memberType);
		}
		create(claim);
//		entityManager.refresh(claim);
		claim = entityManager.find(Claim.class, claim.getKey());
		
		ClaimDto responseClaim = new ClaimMapper().getClaimDto(claim);
		responseClaim.setNewIntimationDto(claimDto.getNewIntimationDto());
		if(null != responseClaim.getKey()){
			

			String outCome = "";
			
			if(claimDto.getStatusId().equals(ReferenceTable.CLAIM_SUGGEST_REJECTION_STATUS)){
				outCome = SHAConstants.OUTCOME_FOR_MANUAL_SUGGEST_REJECTION;
			}else{
				if(claim != null && claim.getClaimType() != null && claim.getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)){
					outCome = SHAConstants.AUTO_REGISTRATION_OUTCOME_FOR_REIMBURSEMENT;
				}else{
					outCome = SHAConstants.AUTO_REGISTRATION_OUTCOME;
				}
			}
			
//			setBPMOutcome(resultTask, responseClaim);	
			if((ReferenceTable.HEALTH_LOB_KEY).equals(responseClaim.getLobId())){
				submitDBProcedureForRegistration(resultTask, claim, claimDto, outCome);
			}	
			
			
			if(claim != null && claim.getClaimType() != null && claim.getClaimType().getKey() != null && (ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY).equals(claim.getClaimType().getKey())){

	
				if((ReferenceTable.HEALTH_LOB_KEY).equals(responseClaim.getNewIntimationDto().getPolicy().getLobId())){
	//				setBPMOutcome(resultTask, responseClaim);	
				}	
				
				
				/**
				 * From the below condition health lob type check has
				 * been removed for initiating FVR task.
				 * 
				 *  Since rrc type is required for FVR task , during manual 
				 *  registration , cashless payload is of no use. Hence reusing
				 *  the health code.
				 * */
				
				
				
				//if(claim != null &&	claim.getClaimType() != null && claim.getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) && claim.getIntimation().getPolicy().getLobId().equals(ReferenceTable.HEALTH_LOB_KEY)){
				if((ReferenceTable.HEALTH_LOB_KEY).equals(responseClaim.getLobId())){
				
					if((claim != null &&	claim.getClaimType() != null && claim.getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)) && claimDto.getStatusId() != null && claimDto.getStatusId().equals(ReferenceTable.INTIMATION_REGISTERED_STATUS)){
						/*****
						 * **
						 * BPM to DB Migration done hence below code was commented. this task will get submitted in the above DBprocedure call for submit
						 */
	//					initiateBPMNforReimbursement(claim);
	//					autoRegisterFVR(claim.getIntimation(),resultTask.getUserId());
						
						Map<String,Object> wrkflowMap = (Map<String,Object>)resultTask.getDbOutArray();
						wrkflowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_INITIATE_REIMINDER_PROCESS);
						initiateReminderLetterProcess(wrkflowMap);					
					}
					//
					//autoRegisterFVR(claim.getIntimation(),resultTask);
				}
			}
			
			//IMSSUPPOR-24475
			if (responseClaim
					.getNewIntimationDto() != null && responseClaim
							.getNewIntimationDto().getPolicy() != null && responseClaim
									.getNewIntimationDto().getPolicy().getLobId() != null && (ReferenceTable.HEALTH_LOB_KEY).equals(responseClaim
					.getNewIntimationDto().getPolicy().getLobId())) {
					autoRegisterFVR(claim.getIntimation(), resultTask);
				}
			
			claimDto.setClaimId(claim.getClaimId());
			String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
			if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
				try {
					Hospitals hospitalDetailsByKey = getHospitalDetailsByKey(claim.getIntimation().getHospital());
	//				String provisionAmtInput = SHAUtils.getProvisionAmtInput(claim, hospitalDetailsByKey.getName(), String.valueOf(claim.getCurrentProvisionAmount().longValue()));
	//				PremiaService.getInstance().updateProvisionAmount(provisionAmtInput);
					PremiaService.getInstance().getPolicyLock(claim, hospitalDetailsByKey.getHospitalCode());
					updateProvisionAmountToPremia(claim);
					
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		return responseClaim;
	}

	public void initiateReminderLetterProcess(Object workFlowMap) {
		Map<String, Object> wrkFlowMap = (Map<String, Object>) workFlowMap;
		Long intimKey = (Long) wrkFlowMap.get(SHAConstants.INTIMATION_KEY);
		if(intimKey != null){
		
			List<Claim> claimList = getClaimByIntimation(intimKey);
			
			if(claimList != null && !claimList.isEmpty()){
			
				Claim claim = claimList.get(0);	
				DBCalculationService dbService = new DBCalculationService();
				
				wrkFlowMap.put(SHAConstants.DB_CLAIM_KEY, claim.getKey());
				wrkFlowMap.put(SHAConstants.DB_CLAIM_NUMBER, claim.getClaimId());
				wrkFlowMap.put(SHAConstants.CLAIM_TYPE, claim.getClaimType().getValue());
				wrkFlowMap.put(SHAConstants.PAYLOAD_REGISTRATION_DATE, claim.getCreatedDate());
				wrkFlowMap.put(SHAConstants.PROCESS_TYPE, claim.getClaimType().getValue());
				if(ReferenceTable.HEALTH_LOB_KEY.equals(claim.getLobId())){
					wrkFlowMap.put(SHAConstants.PAYLOAD_REMINDER_CATEGORY, SHAConstants.BILLS_NOT_RECEIVED);
				}
				else{
					if(claim.getIncidenceFlag() != null && (SHAConstants.DEATH_FLAG).equalsIgnoreCase(claim.getIncidenceFlag())){
						wrkFlowMap.put(SHAConstants.PAYLOAD_REMINDER_CATEGORY, SHAConstants.PA_BILLS_NOT_RECEIVED_DEATH);
					}
					else{
						wrkFlowMap.put(SHAConstants.PAYLOAD_REMINDER_CATEGORY, SHAConstants.PA_BILLS_NOT_RECEIVED_OTHERS);
					}
				}
				wrkFlowMap.put(SHAConstants.USER_ID,claim.getCreatedBy());
				wrkFlowMap.put(SHAConstants.WK_KEY, 0);
				Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
				dbService.revisedInitiateTaskProcedure(objArrayForSubmit);
			}
		}
	}
	
	public void initiateBPMNforReimbursement(Claim claim, EntityManager em){
		
		this.entityManager = em;
	//	initiateBPMNforReimbursement(claim);
	}
	
/*	public void initiateBPMNforReimbursement(Claim claim){
		
//		Intimation intimation = getIntimationByKey(intimationObj.getKey());
//		log.info("**************WHILE PROCESSING SAVED INTIMATION ***********-----> "
//				+ intimation != null ? intimation.getIntimationId()
//				: (intimation != null ? intimation.getIntimationId()
//						+ "--->THIS INTIMATION NOT YET SAVED IN OUR DB.. SO IT LEADS TO PROBLEM FOR US"
//						: "NO INTIMATIONS"));
		IntimationRule intimationRule = new IntimationRule();
		IntimationType a_message = new IntimationType();
		ClaimRequestType claimReqType = new ClaimRequestType();
		ClaimType claimType = new ClaimType();
		String strClaimType = "";
		if (null != claim.getIntimation().getHospitalType()
				&& null != claim.getIntimation().getHospitalType().getKey()
				&& claim.getIntimation()
						.getHospitalType()
						.getKey()
						.equals(ReferenceTable.PREMIA_INTIMTION_PROCESS_NETWORK_HOSPITAL)) {
			strClaimType = SHAConstants.CASHLESS_CLAIM_TYPE;
		} else if (null != claim.getIntimation().getHospitalType()
				&& null != claim.getIntimation().getHospitalType().getKey()
				&& claim.getIntimation()
						.getHospitalType()
						.getKey()
						.equals(ReferenceTable.PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL)) {
			strClaimType = SHAConstants.REIMBURSEMENT_CLAIM_TYPE;
		}
		claimType.setClaimType(strClaimType);
		if (null != claim.getIntimation().getCpuCode()
				&& null != claim.getIntimation().getCpuCode().getCpuCode())
			claimReqType.setCpuCode(String.valueOf(claim.getIntimation().getCpuCode()
					.getCpuCode()));
		a_message.setKey(claim.getIntimation().getKey());
		if (null != claim.getIntimation().getAdmissionDate()) {
			String date = String.valueOf(claim.getIntimation().getAdmissionDate());
			String date1 = date.replaceAll("-", "/");
			a_message.setIntDate(SHAUtils.formatIntimationDate(date1));
			a_message.setIntDate(new Timestamp(System.currentTimeMillis()));
			Timestamp timestamp = new Timestamp(
					System.currentTimeMillis() + 60 * 60 * 1000);
			a_message.setIntDate(timestamp);
		}
		a_message.setIntimationNumber(claim.getIntimation().getIntimationId());
		a_message
				.setIntimationSource(claim.getIntimation().getIntimationSource() != null ? (claim.getIntimation()
						.getIntimationSource().getValue() != null ? claim.getIntimation()
						.getIntimationSource().getValue() : "")
						: "");
		a_message.setIsClaimPending(!intimationRule
				.isClaimExist(claim.getIntimation()));
		a_message
				.setIsPolicyValid(intimationRule.isPolicyValid(claim.getIntimation()));
		
		a_message.setIsBalanceSIAvailable(true);

		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.paymentinfo.PaymentInfoType paymentInfoType = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.paymentinfo.PaymentInfoType();

		
		claimReqType
		.setOption(claim.getStage() != null ? claim
				.getStage().getStageName() : " ");
		paymentInfoType
		.setClaimedAmount(claim.getClaimedAmount());
			claimType.setClaimId(claim.getClaimId());
					claimType.setKey(claim.getKey());

		ClassificationType classificationType = new ClassificationType();

		Insured insured = claim.getIntimation().getInsured();

		if (null != claim && null != claim.getIsVipCustomer()
				&& claim.getIsVipCustomer().equals(1l)) {

			classificationType.setPriority(SHAConstants.VIP_CUSTOMER);
		} else if (null != insured && null != insured.getInsuredAge()
				&& insured.getInsuredAge() > 60) {
			classificationType.setPriority(SHAConstants.SENIOR_CITIZEN);
		} else {
			classificationType.setPriority(SHAConstants.NORMAL);
		}

		classificationType.setType(SHAConstants.TYPE_FRESH);
		classificationType.setSource(SHAConstants.NORMAL);

		PreAuthReqType preauthReqType = new PreAuthReqType();
		preauthReqType.setOutcome("PREAUTHNOTRECEIVED");
		preauthReqType.setResult("PREAUTHNOTRECEIVED");
		preauthReqType.setPreAuthAmt(0d);

		// setting senior citizen hardcoded as of now. Later this needs to
		// be changed.

		if (null != claim.getIntimation().getPolicy()
				&& null != claim.getIntimation().getPolicy().getProduct()
				&& null != claim.getIntimation().getPolicy().getProduct().getKey()
				&& claim.getIntimation().getPolicy().getProduct().getKey().equals(30l))
			preauthReqType.setIsSrCitizen(true);
		else
			preauthReqType.setIsSrCitizen(false);

	
		HospitalInfoType hospitalInfoType = new HospitalInfoType();
		hospitalInfoType.setHospitalType(intimationRule
				.getHospitalTypeForPremia(claim.getIntimation()));

		
		 * if(intimation.getHospitalType() != null) {
		 
		
		Hospitals hospital = null;
		
		
		if (null != claim.getIntimation().getHospitalType().getKey()) {
			if (claim.getIntimation().getHospitalType().getKey()
					.equals(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID)) {
				hospital = getHospitalById(claim.getIntimation().getKey());
				if (hospital != null) {
					if (hospital.getNetworkHospitalTypeId() != null) {
						MastersValue networkHospitalType = getMaster(hospital
								.getNetworkHospitalTypeId());
						if (networkHospitalType != null) {
							hospitalInfoType
									.setNetworkHospitalType(networkHospitalType
											.getValue() != null ? networkHospitalType
											.getValue() : "");
						}
					}
				}
			}
		}
		
		if(hospital == null){
			hospital = getHospitalById(claim.getIntimation().getKey());
		}

		// }
		PolicyType policyType = new PolicyType();
		policyType.setPolicyId(claim.getIntimation().getPolicy().getPolicyNumber());
		String userId = BPMClientContext.BPMN_TASK_USER;
		String password = BPMClientContext.BPMN_PASSWORD;
		PayloadBOType payloadBO = new PayloadBOType();
		payloadBO.setIntimation(a_message);
		payloadBO.setClaim(claimType);
		payloadBO.setClaimRequest(claimReqType);
		payloadBO.setHospitalInfo(hospitalInfoType);
		payloadBO.setPolicy(policyType);
		payloadBO.setPreAuthReq(preauthReqType);;
		payloadBO.setPaymentInfo(paymentInfoType);
		payloadBO.setClassification(classificationType);
		
		IntMsg initiateInitmationTask = BPMClientContext
				.getIntimationIntiationTask(
						BPMClientContext.BPMN_TASK_USER,
						BPMClientContext.BPMN_PASSWORD);

		try {
			initiateInitmationTask.initiate(BPMClientContext.BPMN_TASK_USER,
					payloadBO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
*/
	
	public Double calculateAmtBasedOnBalanceSI(Long policyKey , Long insuredId, Long insuredKey, Double provisionAmout,Long intimationKey,String lobFlag)
	{
		DBCalculationService dbCalculationService = new DBCalculationService();
		//Double amount = 0d;
//		TODO under Discussion to get balance SumInsured
		Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(insuredId.toString(), policyKey,lobFlag);
		//amount = Math.min(dbCalculationService.getBalanceSI(policyKey, insuredKey ,insuredSumInsured) , provisionAmout);
		System.out.println("--policy key---"+policyKey+"----insuredSumInsured----"+insuredSumInsured+"----insured key----"+insuredKey);
		return Math.min(dbCalculationService.getBalanceSI(policyKey, insuredKey, 0l, insuredSumInsured,intimationKey).get(SHAConstants.TOTAL_BALANCE_SI) , provisionAmout);
		
		//return amount;
	}
	
	public Double calculateAmtBasedOnBalanceSIForGMC(Long policyKey , Long insuredId, Long insuredKey, Double provisionAmout,Long intimationKey,ClaimDto claimDto,String lobFlag)
	{
		DBCalculationService dbCalculationService = new DBCalculationService();
		//Double amount = 0d;
//		TODO under Discussion to get balance SumInsured
		Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(insuredId.toString(), policyKey,lobFlag);
		//amount = Math.min(dbCalculationService.getBalanceSI(policyKey, insuredKey ,insuredSumInsured) , provisionAmout);
		System.out.println("--policy key---"+policyKey+"----insuredSumInsured----"+insuredSumInsured+"----insured key----"+insuredKey);
		return Math.min(dbCalculationService.getBalanceSIForGMC(policyKey, insuredKey, 0l) , provisionAmout);
		
		//return amount;
	}
	
	public void updateProvisionAmountToPremia(Claim claim){
		
		String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
		if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
			try {
				Hospitals hospitalDetailsByKey = getHospitalDetailsByKey(claim.getIntimation().getHospital());
				String provisionAmtInput = SHAUtils.getProvisionAmtInput(claim, hospitalDetailsByKey.getName(), String.valueOf(claim.getCurrentProvisionAmount().longValue()));
				updateProvisionAmountToPremia(provisionAmtInput);
			} catch(Exception e) {
				
			}
		}
	}
	

	public String updateProvisionAmountToPremia(String input) {
		try {
			
			//Bancs Changes Start
			JSONObject jsonObject = new JSONObject(input);
			String policyNo = jsonObject.getString("PolicyNo");
			String intimationNo = jsonObject.getString("IntimationNo");
			String currentProvisionAmount = jsonObject.getString("ProvisionAmount");
			Policy policyObj = null;
			Builder builder = null;
			String output = null;
			
			if(policyNo != null){
				DBCalculationService dbService = new DBCalculationService();
				policyObj = dbService.getPolicyObject(policyNo);
				//policyObj = policyService.getByPolicyNumber(policyNo);
				if (policyObj != null) {
					if(policyObj.getPolicySource() != null && policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)){
						output = BancsSevice.getInstance().provisionAmountBancsUpdate(policyNo,intimationNo, currentProvisionAmount, input);
					}else{
						builder = PremiaService.getInstance().getBuilderForProvison();
						output = builder.post(new GenericType<String>() {}, input);
					}
				}
			}
			
			//Bancs Changes End
			
			//Builder builder = PremiaService.getInstance().getBuilderForProvison();
			//String output = builder.post(new GenericType<String>() {}, input);

			return output;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return "";

	}
	
	public Policy  getPolicyByKey(Long policyKey) {
		
		Query query = entityManager.createNamedQuery("Policy.findByKey");
    	query = query.setParameter("policyKey", policyKey);
    	Policy policyList = (Policy) query.getSingleResult();
    	if (policyList != null){
    		return policyList;
    	}
		return null;
	}
	
	public MASClaimAdvancedProvision  getClaimAdvProvision(Long branchCode) {
		
		Query query = entityManager.createNamedQuery("MASClaimAdvancedProvision.findByBranchCode");
    	query = query.setParameter("branchCode", branchCode);
    	List<MASClaimAdvancedProvision> claimAdvProvsionList = (List<MASClaimAdvancedProvision>) query.getResultList();
    	if (claimAdvProvsionList != null && !claimAdvProvsionList.isEmpty()){
    		return claimAdvProvsionList.get(0);
    	}
		return null;
	}
	
	private Boolean autoRegisterFVR(Intimation objIntimation, String userName){
		SearchClaimRegistrationTableDto searchDto = new SearchClaimRegistrationTableDto();
		Map<String,Object> workflow = new HashMap<String, Object>();
		searchDto.setDbOutArray(workflow);
		searchDto.setUserId(userName);
		return autoRegisterFVR(objIntimation,searchDto);
	}
	public Boolean autoRegisterFVR(Intimation objIntimation, SearchClaimRegistrationTableDto searchDto)
	{
		try
		{
			FieldVisitRequest fvrRequest = new FieldVisitRequest();
			
			IntimationRule intimationRule = new IntimationRule();

			Query findByIntimationKey = entityManager
					.createNamedQuery("Claim.findByIntimationKey");
			findByIntimationKey = findByIntimationKey.setParameter(
					"intimationKey", objIntimation.getKey());
//			Intimation objIntimation = entityManager.find(Intimation.class, newIntimationDto.getKey());
			Claim claim = (Claim) findByIntimationKey.getSingleResult();
			
			
			Stage objStage = new Stage();
			objStage.setKey(ReferenceTable.CLAIM_REGISTRATION_STAGE);
			
			Status fvrStatus = new Status();
			fvrStatus.setKey(ReferenceTable.INITITATE_FVR);
			
			if(claim != null && claim.getIntimation() != null){
				Intimation intimation = claim.getIntimation();
				Long hospital = intimation.getHospital();
				
				Hospitals hospitalById = getHospitalById(hospital);
				
				TmpCPUCode tmpCPUCode = getTmpCPUCode(hospitalById.getCpuId());
				if(tmpCPUCode != null){
					fvrRequest.setFvrCpuId(tmpCPUCode.getCpuCode());
				}

			}
			
			MastersValue value = new MastersValue();
			value.setKey(ReferenceTable.FVR_ALLOCATION_TO);
			value.setValue(SHAConstants.FVR_ALLOCATION_ANY_ONE);
			fvrRequest.setAllocationTo(value);
			fvrRequest.setIntimation(objIntimation);
			fvrRequest.setClaim(claim);
			fvrRequest.setCreatedBy(searchDto.getUserId());
			fvrRequest.setFvrTriggerPoints(SHAConstants.REGISTERED_CLAIM);
			fvrRequest.setPolicy(objIntimation.getPolicy());
			fvrRequest.setAllocationTo(value);
			fvrRequest.setActiveStatus(1L);
			fvrRequest.setOfficeCode(objIntimation.getPolicy().getHomeOfficeCode());	
			String transFlag = claim.getClaimType() != null && ReferenceTable.CASHLESS_CLAIM_TYPE_KEY.equals(claim.getClaimType().getKey())? "C" : "R";
			fvrRequest.setTransactionFlag(transFlag);
			fvrRequest.setStatus(fvrStatus);
			fvrRequest.setStage(objStage);
			entityManager.persist(fvrRequest);
			entityManager.flush();
//			this.fvrRequest = fvrRequest;
			
			/***
			 * **
			 * the bewlow code will be commented for the purpose of BPMN to DB Migration
			 */
			
//			callReimbursmentFVRTask(fvrRequest, objIntimation, claim, userName);
			
			Map<String, Object> workFlowObj = (Map<String, Object>) searchDto.getDbOutArray();
			DBCalculationService dbService = new DBCalculationService();
			
			if(workFlowObj == null || workFlowObj.isEmpty()){
				Hospitals hospital = getHospitalById(objIntimation.getHospital());
				
				Object[] outObjArray = SHAUtils.getRevisedArrayListForDBCall(claim, hospital);
				Object[] outObj = (Object[])outObjArray[0];
				outObj[SHAConstants.INDEX_USER_ID] = searchDto.getUserId();
				outObj[SHAConstants.INDEX_FVR_KEY] = fvrRequest.getKey();
				outObj[SHAConstants.INDEX_CLAIM_TYPE] = SHAConstants.REIMBURSEMENT_CLAIM_TYPE;
				outObj[SHAConstants.INDEX_PROCESS_TYPE] = SHAConstants.REIMBURSEMENT_CLAIM_TYPE;
				outObj[SHAConstants.INDEX_STAGE_SOURCE] = claim.getStage().getStageName();
				outObj[SHAConstants.INDEX_FVR_CPU_CODE] = fvrRequest.getFvrCpuId();
				outObj[SHAConstants.INDEX_WORK_FLOW_KEY] = 0;
				outObj[SHAConstants.INDEX_OUT_COME] = SHAConstants.OUTCOME_REG_INITIATE_FVR;
				dbService.revisedInitiateTaskProcedure(outObjArray);	
			}			
			if(workFlowObj != null && !workFlowObj.isEmpty()){			
				workFlowObj.put(SHAConstants.FVR_KEY, fvrRequest.getKey());
				workFlowObj.put(SHAConstants.PAYLOAD_FVR_CPU_CODE,fvrRequest.getFvrCpuId());
				workFlowObj.put(SHAConstants.WK_KEY,0);
				workFlowObj.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_REG_INITIATE_FVR);
				
				Object[] outObject = SHAUtils.getRevisedObjArrayForSubmit(workFlowObj);
				dbService.revisedInitiateTaskProcedure(outObject);	
			}
			
//			workFlowObj.put(SHAConstants.LOB_TYPE,claim.getProcessClaimType());
			
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	public void initiateReimbFVRTask(SearchClaimRegistrationTableDto searchDto, EntityManager em){
		this.entityManager = em;
		
		if(searchDto != null && searchDto.getNewIntimationDto() != null && searchDto.getNewIntimationDto().getKey() != null){
			NewIntimationDto intimationdto = searchDto.getNewIntimationDto();
			Intimation intimationObj = getIntimationByKey(intimationdto.getKey());
			if(intimationObj != null){
				autoRegisterFVR(intimationObj,searchDto.getUserId());	
			}
		}
//		initiateReimbFVRTask(searchDto);
	}
	
	public void initiateReimbFVRTask(SearchClaimRegistrationTableDto searchDto){
		try{
			DBCalculationService dbService = new DBCalculationService();
			Map<String,Object> outObject = (Map<String,Object>)searchDto.getDbOutArray();
			
			outObject.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_REG_INITIATE_FVR);
			
			if(outObject != null){
//				Object[] OutputObj = SHAUtils.getObjArrayForSubmit(outObject);

//				dbService.initiateTaskProcedure(OutputObj);
				
				Object[] OutputObj = SHAUtils.getRevisedObjArrayForSubmit(outObject);
				dbService.revisedInitiateTaskProcedure(OutputObj);
			}	
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	public void callReimbursmentFVRTask(FieldVisitRequest fiedvisitRequest,Intimation objIntimation,Claim claim, String userName){/*
		
		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType reimbursementPayload = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType();
		
		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.policy.PolicyType policyBo = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.policy.PolicyType();
		
		policyBo.setPolicyId(objIntimation.getPolicy().getPolicyNumber());
		reimbursementPayload.setPolicy(policyBo);
		
		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.intimation.IntimationType intimationBo = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.intimation.IntimationType();
		intimationBo.setIntimationNumber(objIntimation.getIntimationId());
		intimationBo.setKey(objIntimation.getKey());
		intimationBo.setStatus("TOFVR");
		reimbursementPayload.setIntimation(intimationBo);
		
		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claimrequest.ClaimRequestType claimReq = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claimrequest.ClaimRequestType();
		if(null != objIntimation.getCpuCode())
		{
			claimReq.setCpuCode(String.valueOf(objIntimation.getCpuCode().getCpuCode()));
		}	
		claimReq.setKey(claim.getKey());
		claimReq.setOption(SHAConstants.BILLS_NOT_RECEIVED); 
		reimbursementPayload.setClaimRequest(claimReq);	
		
		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claim.ClaimType claimBo = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claim.ClaimType();
		claimBo.setClaimId(claim.getClaimId());
		claimBo.setKey(claim.getKey());
		claimBo.setClaimType(claim.getClaimType() != null ? (claim.getClaimType().getValue() != null ? claim.getClaimType().getValue().toUpperCase(): "") : "");		
		reimbursementPayload.setClaim(claimBo);
		
		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.query.QueryType queryType = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.query.QueryType();
		reimbursementPayload.setQuery(queryType);
		
		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.classification.ClassificationType callsificationBo = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.classification.ClassificationType();
		
		callsificationBo.setPriority("");
		callsificationBo.setSource("");
		callsificationBo.setType("");
		reimbursementPayload.setClassification(callsificationBo);
		
		ProcessActorInfoType processActor = new ProcessActorInfoType();
		processActor.setEscalatedByRole("");
		processActor.setEscalatedByUser(BPMClientContext.BPMN_TASK_USER);
		reimbursementPayload.setProcessActorInfo(processActor);
		
		ProductInfoType productInfo = new ProductInfoType();
		if(ReferenceTable.PA_LOB_KEY.equals(objIntimation.getPolicy().getLobId()))
		{
			productInfo.setLob(SHAConstants.PA_LOB);
		}else
		{
			productInfo.setLob(SHAConstants.HEALTH_LOB);
		}
		
		if(objIntimation.getPolicy() != null && objIntimation.getPolicy().getProduct() != null && objIntimation.getPolicy().getProduct().getKey() != null){
			productInfo.setProductId(objIntimation.getPolicy().getProduct().getKey().toString());
			productInfo.setProductName(objIntimation.getPolicy().getProduct().getValue());
			reimbursementPayload.setProductInfo(productInfo);
		}
		
		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.hospitalinfo.HospitalInfoType hospitalInfoType = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.hospitalinfo.HospitalInfoType();
		
		Long hospital = objIntimation.getHospital();
		
		Hospitals hospitalById = getHospitalById(hospital);
		hospitalInfoType.setKey(hospital);
		hospitalInfoType.setHospitalType(hospitalById.getHospitalType().getValue());
		hospitalInfoType.setNetworkHospitalType(hospitalById.getNetworkHospitalType());
		reimbursementPayload.setHospitalInfo(hospitalInfoType);
		
		
		if(objIntimation.getAdmissionDate() != null){
			String intimDate = SHAUtils.formatIntimationDateValue(objIntimation.getAdmissionDate());
			RRCType rrcType = new RRCType();
			rrcType.setFromDate(intimDate);
			reimbursementPayload.setRrc(rrcType);
		}
		
		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.fieldvisit.FieldVisitType fieldVisitType = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.fieldvisit.FieldVisitType();
		fieldVisitType.setKey(fiedvisitRequest.getKey());
		
		Long cpuId = hospitalById.getCpuId();
		if(cpuId != null){
		TmpCPUCode tmpCPUCode = getTmpCPUCode(cpuId);
		fieldVisitType.setRequestedBy(tmpCPUCode.getCpuCode() != null ? tmpCPUCode.getCpuCode().toString() : null);
		}
		reimbursementPayload.setFieldVisit(fieldVisitType);
		
		 FVR reimbursementFVR = BPMClientContext.getReimbursementFVR(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
	        try {
				reimbursementFVR.initiate(userName, reimbursementPayload);
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		//need to be implemented
	*/}

/*	public void setBPMOutcome(SearchClaimRegistrationTableDto resultTask,
			ClaimDto claimDto) {
		if (null != claimDto.getKey()) {          
			if (resultTask != null) {
//				XMLElement payload = resultTask.getHumanTask().getPayload();
//				Map<String, String> regIntDetailsReq = new HashMap<String, String>();
//				Map<String, String> preAuthReqMap = new HashMap<String, String>();
				
				PaymentInfoType paymentInfo = new PaymentInfoType();
				
				PayloadBOType payloadBO = resultTask.getHumanTask().getPayloadCashless();
				
				try {
					
					if(null != claimDto.getProvisionAmount()){
						
//						regIntDetailsReq.put("provisionAmount", claimDto.getProvisionAmount().toString());
						
						paymentInfo.setProvisionAmount(claimDto.getProvisionAmount());
					}
					
					if(null != claimDto.getClaimedAmount()){
						
//						regIntDetailsReq.put("amountClaimed", claimDto.getClaimedAmount().toString());
						
						paymentInfo.setClaimedAmount(claimDto.getClaimedAmount());
					}
					
					
					ClaimType claimtype = new ClaimType();
					
					if(claimDto.getClaimType() != null && claimDto.getClaimType().getValue() != null){
						claimtype.setClaimType(claimDto.getClaimType().getValue().toUpperCase());
					}
					
					/**
					 * As per Sathish Sir's, instruction lobtype was set to "H"  for cashless Accident Cases
					 
					if(claimDto.getIncidenceFlagValue() == null ||(claimDto.getIncidenceFlagValue() != null && claimDto.getIncidenceFlagValue().isEmpty()) ||
							(("A").equals(claimDto.getIncidenceFlagValue()) && claimDto.getClaimType().getId() != null && claimDto.getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY))){
						payloadBO.getProductInfo().setLobType(SHAConstants.HEALTH_LOB_FLAG);
					}
					else if(!(ReferenceTable.HEALTH_LOB_KEY).equals(claimDto.getNewIntimationDto().getPolicy().getLobId())){
						payloadBO.getProductInfo().setLobType(SHAConstants.PA_LOB_TYPE);
					}
					else{
						payloadBO.getProductInfo().setLobType(SHAConstants.HEALTH_LOB_FLAG);
					}
					claimtype.setClaimId(claimDto.getClaimId());
					claimtype.setKey(claimDto.getKey());
					payloadBO.getClaimRequest().setCpuCode(claimDto.getNewIntimationDto().getCpuCode());
					
//					regIntDetailsReq.put("claimType", claimDto.getClaimType().getValue());					
//					payload = BPMClientContext.setNodeValue(payload, "RegIntDetails", "isBalanceSIAvailable", "");
	
//					preAuthReqMap.put("key", claimDto.getKey().toString());
					
					PreAuthReqType prauthreqType = new PreAuthReqType();
					
					prauthreqType.setIsVIP(claimDto.getIsVipCustomer() != null && claimDto.getIsVipCustomer() == 1 ? true : false);
					prauthreqType.setKey(claimDto.getKey());
					prauthreqType.setOutcome("PREAUTHNOTRECEIVED");
					
//					preAuthReqMap.put("isVIP", claimDto.getIsVipCustomer().toString());
					
					int age = 0;
					if(claimDto.getNewIntimationDto().getInsuredAge() != null && claimDto.getNewIntimationDto().getInsuredAge() != "")
					{
							age = Integer.parseInt(claimDto.getNewIntimationDto().getInsuredAge());
					}				
					Boolean isSrCitizen =  age > 59 ? true : false ;

//					preAuthReqMap.put("isSrCitizen", isSrCitizen);
					prauthreqType.setIsSrCitizen(isSrCitizen);
									
					payloadBO.setClaim(claimtype);
					payloadBO.setPreAuthReq(prauthreqType);
					
					
//					PreAuthReq preauthRequest = new PreAuthReq();
//					preauthRequest.setKey(Long.valueOf(preAuthReqMap.get("key")));
//					preauthRequest.setIsVIP(Boolean.valueOf(preAuthReqMap.get("isVIP")));
//					preauthRequest.setIsSrCitizen(Boolean.valueOf(preAuthReqMap.get("isSrCitizen")));
					
					
//					payload = BPMClientContext.setNodeValue(payload, "RegIntDetails", regIntDetailsReq);
//					payload = BPMClientContext.setNodeValue(payload, "PreAuthReq", preAuthReqMap);
//					System.out.println("--------------------------------------------------------------------------");
//					BPMClientContext.printPayloadElement(payload);
//					System.out.println("--------------------------------------------------------------------------");					
				
//				resultTask.getHumanTask().setPayload(payloadBO);
				
				IntimationType intimationType = payloadBO.getIntimation();
				if (claimDto.getStatusId().equals(ReferenceTable.CLAIM_SUGGEST_REJECTION_STATUS)) {
					
					
							resultTask.getHumanTask().setOutcome("SUGGEST REJECTION");
							
							if(intimationType!= null){
								intimationType.setApproveRegistration("SUGGEST REJECTION");
							}else{
								intimationType = new IntimationType();
								intimationType.setApproveRegistration("SUGGEST REJECTION");
							}
							
							if(claimDto.getIncidenceFlagValue() != null)
								intimationType.setReason(claimDto.getIncidenceFlagValue());
							
							payloadBO.setIntimation(intimationType);

					} else {
							
							resultTask.getHumanTask().setOutcome("NEW");
							if(intimationType!= null){
								intimationType.setApproveRegistration("NEW");
							}else{
								intimationType = new IntimationType();
								intimationType.setApproveRegistration("NEW");
							}
							
							if(claimDto.getIncidenceFlagValue() != null){
								if(intimationType == null){
									intimationType = new IntimationType();
								}
								intimationType.setReason(claimDto.getIncidenceFlagValue());
							}								
							
							payloadBO.setIntimation(intimationType);
				}
				
				
				    ClassificationType classification = payloadBO.getClassification();
				    
				    Stage stage = entityManager.find(Stage.class,ReferenceTable.CLAIM_REGISTRATION_STAGE);
				    classification.setSource(stage.getStageName());
				    payloadBO.setClassification(classification);
				    
				    InvestigationType investigationBO = new InvestigationType();
				    
				    if(claimDto.getIncidenceFlagValue() != null && (SHAConstants.DEATH_FLAG).equalsIgnoreCase(claimDto.getIncidenceFlagValue())){
				    	investigationBO.setKey(claimDto.getInvestigationKey());
				    	payloadBO.setInvestigation(investigationBO);
				    }				    
				    
				    
					
					
					

					resultTask.getHumanTask().setPayloadCashless(payloadBO);
					
					
//						BPMClientContext.printPayloadElement(resultTask.getHumanTask().getPayload());
//						InvokeHumanTask invokeHT = new InvokeHumanTask();
						
						SubmitClaimRegTask  submitRegTask =  BPMClientContext.getClaimRegistrationSubmitTask(resultTask.getUserId(),resultTask.getPassword());
						
//						invokeHT.execute(resultTask.getUserId(),resultTask.getPassword(), resultTask.getHumanTask(), null, preauthRequest, null, null);						
						
						submitRegTask.execute(resultTask.getUserId(), resultTask.getHumanTask());
						
						Intimation intimation = new NewIntimationMapper().getNewIntimation(claimDto.getNewIntimationDto());
						TmpCPUCode cpuObject = entityManager.find(TmpCPUCode.class, claimDto.getNewIntimationDto().getCpuId()); 
						intimation.setCpuCode(cpuObject);

					    if(claimDto.getStatusId().equals(ReferenceTable.CLAIM_SUGGEST_REJECTION_STATUS)){
							intimation.setRegistrationStatus("REJECTED");
						}else {
							intimation.setRegistrationStatus("REGISTERED");	
						}
					    
						Intimation response = entityManager.find(Intimation.class, intimation.getKey());
						response.setRegistrationStatus(intimation.getRegistrationStatus());
						entityManager.merge(response);
						entityManager.flush();
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				
					// TODO handle Exception when BPM process invocation failed
			*/	


	
	public void submitDBProcedureForRegistration(SearchClaimRegistrationTableDto searchDto,Claim claim, ClaimDto claimDto,String outCome){
		Map<String, Object> wrkFlowMap = (Map<String, Object>) searchDto.getDbOutArray();
		
		wrkFlowMap.put(SHAConstants.DB_CLAIM_KEY, claim.getKey());
		wrkFlowMap.put(SHAConstants.DB_CLAIM_NUMBER, claim.getClaimId());
		wrkFlowMap.put(SHAConstants.CLAIM_TYPE, claim.getClaimType().getValue());
		wrkFlowMap.put(SHAConstants.PAYLOAD_REGISTRATION_DATE, claim.getCreatedDate());
		wrkFlowMap.put(SHAConstants.PROCESS_TYPE, claim.getClaimType().getValue());
		wrkFlowMap.put(SHAConstants.STAGE_SOURCE,claim.getStage().getStageName());
		wrkFlowMap.put(SHAConstants.CLAIMED_AMOUNT, claim.getClaimedAmount() < 0 ? 0 :claim.getClaimedAmount());
		wrkFlowMap.put(SHAConstants.USER_ID, claim.getCreatedBy());
		wrkFlowMap.put(SHAConstants.OUTCOME, outCome);
		
		if(claimDto.getIncidenceFlagValue() == null ||(claimDto.getIncidenceFlagValue() != null && claimDto.getIncidenceFlagValue().isEmpty()) ||
				(("A").equals(claimDto.getIncidenceFlagValue()) && claimDto.getClaimType().getId() != null && claimDto.getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY))){
			//payloadBO.getProductInfo().setLobType(SHAConstants.HEALTH_LOB_FLAG);
			wrkFlowMap.put(SHAConstants.LOB_TYPE, SHAConstants.HEALTH_LOB_FLAG);
		}
		else if(!(ReferenceTable.HEALTH_LOB_KEY).equals(claimDto.getNewIntimationDto().getPolicy().getLobId())){
			//payloadBO.getProductInfo().setLobType(SHAConstants.PA_LOB_TYPE);
			wrkFlowMap.put(SHAConstants.LOB_TYPE, SHAConstants.PA_LOB_TYPE);
		}
		else{
			//payloadBO.getProductInfo().setLobType(SHAConstants.HEALTH_LOB_FLAG);
			wrkFlowMap.put(SHAConstants.LOB_TYPE, SHAConstants.HEALTH_LOB_FLAG);
		}
		
//		Object[] objArrayForSubmit = SHAUtils.getObjArrayForSubmit(wrkFlowMap);
//		dbCalService.initiateTaskProcedure(objArrayForSubmit);
		
		Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
		DBCalculationService dbCalService = new DBCalculationService();
		dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);	
		
	}


	public Claim searchByKey(Long a_key) {
		Claim find = entityManager.find(Claim.class, a_key);
		entityManager.refresh(find);
		return find;
	}

	public Claim getClaimforIntimation(Long intimationKey) {
		Claim a_claim = null;
		if (intimationKey != null) {

			Query findByIntimationKey = entityManager
					.createNamedQuery("Claim.findByIntimationKey");
			findByIntimationKey = findByIntimationKey.setParameter(
					"intimationKey", intimationKey);
			try {

				if (findByIntimationKey.getResultList().size() > 0) {					
					a_claim = (Claim)findByIntimationKey.getResultList().get(0);
					entityManager.refresh(a_claim);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {

			}

		} else {
			// intimationKey null
		}

		return a_claim;

	}
	
	public OPClaim getOPClaimforIntimation(Long intimationKey) {
		OPClaim a_claim = null;
		if (intimationKey != null) {

			Query findByIntimationKey = entityManager
					.createNamedQuery("OPClaim.findByIntimationKey");
			findByIntimationKey = findByIntimationKey.setParameter(
					"intimationKey", intimationKey);
			try {

				if (findByIntimationKey.getResultList().size() > 0) {					
					a_claim = (OPClaim)findByIntimationKey.getResultList().get(0);
					entityManager.refresh(a_claim);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {

			}

		} else {
			// intimationKey null
		}

		return a_claim;

	}
	
	
	public ViewTmpClaim getTmpClaimforIntimation(Long intimationKey) {
		ViewTmpClaim a_claim = null;
		if (intimationKey != null) {

			Query findByIntimationKey = entityManager
					.createNamedQuery("ViewTmpClaim.findByIntimationKey");
			findByIntimationKey = findByIntimationKey.setParameter(
					"intimationKey", intimationKey);
			try {

				if (findByIntimationKey.getResultList().size() > 0) {					
					a_claim = (ViewTmpClaim)findByIntimationKey.getResultList().get(0);
					entityManager.refresh(a_claim);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {

			}

		} else {
			// intimationKey null
		}

		return a_claim;

	}
	
	public ViewTmpOPClaim getTmpOPClaimforOPIntimation(Long intimationKey) {
		ViewTmpOPClaim a_claim = null;
		if (intimationKey != null) {

			Query findByIntimationKey = entityManager
					.createNamedQuery("ViewTmpOPClaim.findByIntimationKey");
			findByIntimationKey = findByIntimationKey.setParameter(
					"intimationKey", intimationKey);
			try {

				if (findByIntimationKey.getResultList().size() > 0) {					
					a_claim = (ViewTmpOPClaim)findByIntimationKey.getResultList().get(0);
					entityManager.refresh(a_claim);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {

			}

		} else {
			// intimationKey null
		}

		return a_claim;

	}
	
	public Claim getPreauthByIntimationKey(Long intimationKey, EntityManager entityManager) {
		this.entityManager = entityManager;
		Query query = entityManager
				.createNamedQuery("Claim.findByIntimationKey");
		query.setParameter("intimationKey", intimationKey);
		Claim singleResult = (Claim) query.getSingleResult();
		return singleResult;
	}
	

	@SuppressWarnings("unchecked")
	public Claim getClaimsByIntimationNumber(String intimationNumber) {
		List<Claim> resultClaim = null;
		if (intimationNumber != null) {

			Query findByIntimationNum = entityManager.createNamedQuery(
					"Claim.findByIntimationNumber").setParameter(
					"intimationNumber", intimationNumber);

			try {
				resultClaim = (List<Claim>) findByIntimationNum.getResultList();
				
				if(resultClaim != null && !resultClaim.isEmpty()){
					return resultClaim.get(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public List<Claim> getClaimforIntimationByParams(
			Map<String, Object> parameters) {
		if (!parameters.isEmpty()) {
			String intimationNumber;
			String claimNumber;
			Date registeredDate;
			String cpuValue;
			String policyNumber;
			String status;

			claimNumber = parameters.containsKey("claimNumber") ? "%"
					+ StringUtils
							.trim(parameters.get("claimNumber").toString())
					+ "%" : null;
			intimationNumber = parameters.containsKey("intimationNumber") ? "%"
					+ StringUtils.trim(parameters.get("intimationNumber")
							.toString()) + "%" : null;
			policyNumber = parameters.containsKey("policyNumber") ? "%"
					+ StringUtils.trim(parameters.get("policyNumber")
							.toString()) + "%" : null;
			registeredDate = parameters.containsKey("registeredDate") ? (Date) parameters
					.get("registeredDate") : null;
			cpuValue = parameters.containsKey("cpuCode") ? "%"
					+ StringUtils.trim(parameters.get("cpuCode").toString())
					+ "%" : null;
			status = parameters.containsKey("status") ? StringUtils
					.trim(parameters.get("status").toString()) : null;

			try {
				final CriteriaBuilder builder = entityManager
						.getCriteriaBuilder();

				final CriteriaQuery<Intimation> intimationCriteriaQuery = builder
						.createQuery(Intimation.class);
				final CriteriaQuery<Policy> policyCriteriaQuery = builder
						.createQuery(Policy.class);
				final CriteriaQuery<Claim> claimCriteriaQuery = builder
						.createQuery(Claim.class);

				// Root<Policy> policyRoot =
				// policyCriteriaQuery.from(Policy.class);
				Root<Intimation> intimationRoot = intimationCriteriaQuery
						.from(Intimation.class);

				// Join<Intimation,Root<Policy>> policyJoin =
				// intimationRoot.join("policy", JoinType.INNER);

				Root<Claim> claimRoot = claimCriteriaQuery.from(Claim.class);
				Join<Intimation, TmpCPUCode> cpuCodeJoin = intimationRoot.join(
						"cpuCode", JoinType.INNER);
				Join<Intimation, Policy> policyJoin = intimationRoot.join(
						"policy", JoinType.INNER);

				Join<Claim, Intimation> intimationJoin = claimRoot.join(
						"intimation", JoinType.INNER);

				List<Predicate> predicates = new ArrayList<Predicate>();

				if (!ValidatorUtils.isNull(claimNumber)) {
					Predicate claimNumberPredicate = builder.like(
							claimRoot.<String> get("claimId"), claimNumber);
					predicates.add(claimNumberPredicate);
				}
				if (!ValidatorUtils.isNull(registeredDate)) {
					Predicate createdDatePredicate = builder.equal(
							claimRoot.get("createdDate"), registeredDate);
					predicates.add(createdDatePredicate);
				}

				Predicate intimationNotNullPredicate = builder
						.isNotNull(claimRoot.<Intimation> get("intimation"));
				predicates.add(intimationNotNullPredicate);

				if (!ValidatorUtils.isNull(intimationNumber)) {
					Predicate intimationNumberPredicate = builder.like(
							intimationJoin.<String> get("intimationId"),
							intimationNumber);
					predicates.add(intimationNumberPredicate);
				}

				if (!ValidatorUtils.isNull(policyNumber)) {
					Predicate policyNumberPredicate = builder
							.like(claimRoot.<Intimation> get("intimation")
									.<Policy> get("policy")
									.<String> get("policyNumber"), policyNumber);
					predicates.add(policyNumberPredicate);
				}
				if (!ValidatorUtils.isNull(cpuValue)) {
					Predicate cpuPredicate = builder.like(
							claimRoot.<Intimation> get("intimation")
									.<TmpCPUCode> get("cpuCode")
									.<String> get("description"), cpuValue);
					predicates.add(cpuPredicate);
				}

				if (status != null) {
					Predicate statusPredicate = builder.like(claimRoot
							.<Status> get("status")
							.<String> get("processValue"), status);
					predicates.add(statusPredicate);

					if (status.equalsIgnoreCase("Registered")) {
						Predicate coveringLetterPredicate = builder.equal(
								claimRoot.get("conversionLetter"),
								Integer.parseInt("0"));
						predicates.add(coveringLetterPredicate);
					} else if (status.equalsIgnoreCase("Rejected")) {
						Predicate rejectionLetterPredicate = builder.equal(
								claimRoot.get("rejectionLetter"),
								Integer.parseInt("0"));
						predicates.add(rejectionLetterPredicate);
					}
				}
				claimCriteriaQuery.select(claimRoot).where(
						builder.and(predicates.toArray(new Predicate[] {})));

				final TypedQuery<Claim> query = entityManager
						.createQuery(claimCriteriaQuery);
				List<Claim> claimResult = query.getResultList();

				if (!claimResult.isEmpty())
					return claimResult;
				else
					return null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// empty parameters
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Claim> getClaimsByPolicyNumber(String policyNumber) {
		
		List<Claim> resultList = new ArrayList<Claim>();
		if (policyNumber != null) {

			Query findByPolicyNumber = entityManager.createNamedQuery(
					"Claim.findByPolicyNumber").setParameter("policyNumber",
					policyNumber);

			try {
				resultList = findByPolicyNumber.getResultList();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(resultList != null && !resultList.isEmpty()) {
			for (Claim claim : resultList) {
				entityManager.refresh(claim);
			}
		}
		return resultList;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<ViewTmpClaim> getViewTmpClaimsByPolicyNumber(String policyNumber) {
		
		List<ViewTmpClaim> resultList = new ArrayList<ViewTmpClaim>();
		if (policyNumber != null) {

			Query findByPolicyNumber = entityManager.createNamedQuery(
					"ViewTmpClaim.findByPolicyNumber").setParameter("policyNumber",
					policyNumber);

			try {
				resultList = findByPolicyNumber.getResultList();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Below code is unused so commenting it. 
		/*if(resultList != null && !resultList.isEmpty()) {
			for (ViewTmpClaim claim : resultList) {
//				entityManager.refresh(claim);
			}
		}*/
		return resultList;
	}
	
	public List<ViewTmpClaim> getViewTmpClaimsByIntimationKeys(List<ViewTmpIntimation> intimationKeys) {
		
		if(intimationKeys != null){
			Query query = entityManager.createNamedQuery("ViewTmpClaim.findByIntimationKeys");
			query.setParameter("intimationKeys", intimationKeys);
	
			List<ViewTmpClaim> resultList =  (List<ViewTmpClaim>) query.getResultList();
			if(resultList != null && resultList.size()!=0){
//				entityManager.refresh(resultList);
				return resultList;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<PreviousClaimsTableDTO> getPreviousClaimForPolicy(NewIntimationDto newIntimationDto) {
		
		List<PreviousClaimsTableDTO> previousClaimsListDTO = null;
		if(newIntimationDto != null && newIntimationDto.getPolicy() != null && newIntimationDto.getPolicy().getPolicyNumber() != null){	
			
		List<ViewTmpClaim> claimResultList = getViewTmpClaimsByPolicyNumber(newIntimationDto.getPolicy().getPolicyNumber());
		List<ViewTmpClaim> resultList = new ArrayList<ViewTmpClaim>();
		if(claimResultList != null && !claimResultList.isEmpty())
		{
			for (ViewTmpClaim claim : claimResultList) {
				if(StringUtils.containsIgnoreCase(claim.getIntimation().getIntimationId(), newIntimationDto.getIntimationId())){
					continue;
				}
				resultList.add(claim);
					
			}
		}		
		
		if(!resultList.isEmpty()){
			previousClaimsListDTO = new ArrayList<PreviousClaimsTableDTO>();
			for(ViewTmpClaim claim :resultList){				
				PreviousClaimsTableDTO previousClaimsTableDTO = PreviousClaimMapper.getInstance().getPreviousClaimsTableDTO(claim);
				Policy policyByKey = getPolicyByKey(claim.getIntimation().getPolicy());
				previousClaimsTableDTO.setInsuredName(policyByKey.getProposerFirstName());
				previousClaimsTableDTO.setPatientName(claim.getIntimation().getInsuredPatientName());
				
				if(claim.getIntimation().getHospital() != null){
					Hospitals hospital = entityManager.find(Hospitals.class, claim.getIntimation().getHospital());
					if(hospital != null){
						previousClaimsTableDTO.setHospitalName(hospital.getName());
					}
				}
				
				String diagnosisName = " ";
				List<PedValidation> pedValidationsList = getDiagnosis(previousClaimsTableDTO.getIntimationKey());
				if (!pedValidationsList.isEmpty()) {
					for (PedValidation pedValidation : pedValidationsList) {
						diagnosisName = (diagnosisName == " " ? ""
								: diagnosisName + ", ")
								+ getDiagnosisNames(pedValidation);
					}
				}
				previousClaimsTableDTO.setDiagnosis(diagnosisName);				
				
				previousClaimsListDTO.add(previousClaimsTableDTO);	
			}			 
		}
	  }
			
		return previousClaimsListDTO;
	}
	

	
	private String getDiagnosisNames(PedValidation pedValidation){
		MasterService masterService = new MasterService();
		String diagnosisName = masterService.getDiagnosis(pedValidation
				.getDiagnosisId(),this.entityManager);
		
		return diagnosisName;
	}
	
	private List<PedValidation> getDiagnosis(Long intimationKey){
		
		PEDValidationService pedValidationService =  new PEDValidationService();
		List<PedValidation> pedList = pedValidationService.getDiagnosis(intimationKey,this.entityManager);
		
		return pedList;
	}
	

	@SuppressWarnings("unchecked")
	public List<Claim> getClaimsByInsuredId(Long insuredId) {
		List<Claim> resultList = new ArrayList<Claim>();
		if (insuredId != null) {

			Query findByInsuredId = entityManager.createNamedQuery(
					"Claim.findByInsuredID").setParameter("insuredId",
					insuredId);

			try {
				resultList = findByInsuredId.getResultList();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public List<ViewTmpClaim> getTmpClaimsByInsuredId(Long insuredId) {
		List<ViewTmpClaim> resultList = new ArrayList<ViewTmpClaim>();
		if (insuredId != null) {

			Query findByInsuredId = entityManager.createNamedQuery(
					"ViewTmpClaim.findByInsuredID").setParameter("insuredId",
					insuredId);

			try {
				resultList = findByInsuredId.getResultList();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	public BeanItemContainer<TmpEmployee> getEmployeeByCPUId(Long cpuId) {
		List<TmpEmployee> employeeList = new ArrayList<TmpEmployee>();
		if (cpuId != null) {
			Query findByCPUId = entityManager.createNamedQuery(
					"TmpEmployee.findByCPUId").setParameter("cpuId", cpuId);

			try {
				employeeList = findByCPUId.getResultList();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		BeanItemContainer<TmpEmployee> employeeContainer = new BeanItemContainer<TmpEmployee>(
				TmpEmployee.class);
		employeeContainer.addAll(employeeList);
		return employeeContainer;
	}

	public Claim getClaimByKey(Long key) {
		Query query = entityManager.createNamedQuery("Claim.findByClaimKey");
		query.setParameter("claimKey", key);
		List<Claim> claim = (List<Claim>)query.getResultList();
		
		if(claim != null && ! claim.isEmpty()){
			for (Claim claim2 : claim) {
				entityManager.refresh(claim2);
			}
			return claim.get(0);
		}
		else{
			return null;
		}
	}
	
	public OPClaim getOPClaimByKey(Long key) {
		Query query = entityManager.createNamedQuery("OPClaim.findByClaimKey");
		query.setParameter("claimKey", key);
		List<OPClaim> claim = (List<OPClaim>)query.getResultList();
		if(claim != null && ! claim.isEmpty()){
			return claim.get(0);
		}
		else{
			return null;
		}
	}
	
	public OPClaim getOPClaimByIntimationNo(String argIntimationNo) {
		Query query = entityManager.createNamedQuery("OPClaim.findByIntimationNo");
		query.setParameter("intimationNumber", argIntimationNo);
		List<OPClaim> claim = (List<OPClaim>)query.getResultList();
		if(claim != null && ! claim.isEmpty()){
			return claim.get(0);
		}
		else{
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Claim getClaimByClaimKey(Long claimKey) {
		Query query = entityManager.createNamedQuery("Claim.findByClaimKey");
		query.setParameter("claimKey", claimKey);
		List<Claim> claim = (List<Claim>)query.getResultList();
		
		if(claim != null && ! claim.isEmpty()){
			for (Claim claim2 : claim) {
				entityManager.refresh(claim2);
			}
			return claim.get(0);
		}
			return null;
		
	}
	
	@SuppressWarnings("unchecked")
	public ViewTmpClaim getTmpClaimByClaimKey(Long claimKey) {
		Query query = entityManager.createNamedQuery("ViewTmpClaim.findByClaimKey");
		query.setParameter("claimKey", claimKey);
		List<ViewTmpClaim> claim = (List<ViewTmpClaim>)query.getResultList();
		
		if(claim != null && ! claim.isEmpty()){
			for (ViewTmpClaim claim2 : claim) {
				entityManager.refresh(claim2);
			}
			return claim.get(0);
		}
		else{
			return null;
		}
	}

	public Claim getClaimByClaimKey(Long claimKey,EntityManager em){
		this.entityManager = em;
		return getClaimByClaimKey(claimKey);
	}

	public ConvertClaimDTO searchByClaimKey(Long key) {
		Claim find = entityManager.find(Claim.class, key);
		entityManager.refresh(find);

		if (find != null) {
			ConvertClaimDTO convertClaimDto = ConvertClaimMapper.getInstance()
					.getClaimDTO(find);
			return convertClaimDto;
		}
		return null;
	}

	public Boolean saveConversionReason(ConvertClaimDTO convertClaim,
			String option,SearchConvertClaimTableDto searchFormDto) {

		Long key = convertClaim.getKey();

		Claim claimData = ConvertClaimMapper.getInstance().getClaim(convertClaim);
		
		//convertClaim.setIntimationNumber(claimData.getIntimation().getIntimationId());

		Claim find = entityManager.find(Claim.class, key);
		entityManager.refresh(find);

		if (find != null) {
			find.setConversionReason(claimData.getConversionReason());
			if (option.equals("submit")) {
				entityManager.merge(find);

			} else {
				MastersValue claimTypeId = new MastersValue();
				claimTypeId.setKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
				find.setClaimType(claimTypeId);
				find.setConversionFlag(1l);
				find.setClaimSectionCode(null);
				find.setClaimCoverCode(null);
				find.setClaimSubCoverCode(null);
				find.setConversionDate(new Timestamp(System.currentTimeMillis()));
				find.setPaHospExpenseAmt(0d);
				entityManager.merge(find);
				entityManager.flush();
				entityManager.refresh(find);
				////GLX2020075 commented for this GMC CPU CR since CPU Routing for GMC already done on above code -noufel
//				if(! ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(find.getIntimation().getPolicy().getProduct().getKey())){
					updateCpuCodeForIntimation(find.getIntimation());
//				}
				
				
				setBPMOutComeForConvertClaim(convertClaim, find,searchFormDto, "APPROVE");
				
				autoRegisterFVR(find.getIntimation(), searchFormDto.getUsername());
				
			}
			
			return true;
		}
		return false;
	}
	
	public Boolean saveCashlessConversion(ConvertClaimDTO convertClaim,
			String option,SearchConverClaimCashlessTableDTO searchFormDto) {

		Long key = convertClaim.getKey();

		Claim claimData = ConvertClaimMapper.getInstance().getClaim(convertClaim);
		
		//convertClaim.setIntimationNumber(claimData.getIntimation().getIntimationId());

		Claim find = entityManager.find(Claim.class, key);
		entityManager.refresh(find);

		if (find != null) {
			find.setConversionReason(claimData.getConversionReason());
			if (option.equals("submit")) {
				entityManager.merge(find);

			} else {
				MastersValue claimTypeId = new MastersValue();
				claimTypeId.setKey(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
				find.setClaimType(claimTypeId);
				find.setConversionFlag(1l);
				find.setConversionDate(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(find);
				entityManager.flush();
				
				////GLX2020075 commented for this GMC CPU CR since CPU Routing for GMC already done on above code -noufel
//				if(! (ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(find.getIntimation().getPolicy().getProduct().getKey()))){
				updateCpuCodeForCashlessConversion(find.getIntimation());
//				}
				
			}
			
			return true;
		}
		return false;
	}
	
	
	
	public void updateCpuCodeForIntimation(Intimation intimation){
		
		Policy policy = intimation.getPolicy();
		
		if(policy.getHomeOfficeCode() != null) {
			OrganaizationUnit branchOffice = getInsuredOfficeNameByDivisionCode(policy.getHomeOfficeCode());
			if(branchOffice != null){
				String officeCpuCode = branchOffice.getCpuCode();
				if(officeCpuCode != null) {
					TmpCPUCode masCpuCode = getMasCpuCode(Long.valueOf(officeCpuCode));
					//added foe CR GMC CPU Routing GLX2020075
					if(policy != null && policy.getProduct().getKey() != null){
						MasProductCpuRouting gmcRoutingProduct= getMasProductForGMCRouting(policy.getProduct().getKey());
						if(gmcRoutingProduct != null){
							if(masCpuCode != null && masCpuCode.getGmcRoutingCpuCode() != null){
								masCpuCode =  getMasCpuCode( masCpuCode.getGmcRoutingCpuCode());	
								intimation.setCpuCode(masCpuCode);
								intimation.setOriginalCpuCode(masCpuCode.getCpuCode());
								entityManager.merge(intimation);
								entityManager.flush();
							}
						}
						else if(masCpuCode != null){
							intimation.setCpuCode(masCpuCode);
							intimation.setOriginalCpuCode(masCpuCode.getCpuCode());
							entityManager.merge(intimation);
							entityManager.flush();
						}

					}
				}
			}
		}
		//GLX2020075 commented for this GMC CPU CR since CPU Routing for GMC already done on above code -noufel
//		//added for CPU routing
//		if(intimation.getPolicy() != null && intimation.getPolicy().getProduct().getKey() != null){
//			String CpuCode= getMasProductCpu(intimation.getPolicy().getProduct().getKey());
//			if(CpuCode != null){
//				TmpCPUCode masCpuCode = getMasCpuCode(Long.valueOf(CpuCode));
//				intimation.setCpuCode(masCpuCode);
//				intimation.setOriginalCpuCode(masCpuCode.getCpuCode());
//				entityManager.merge(intimation);
//				entityManager.flush();
//			}
//		}
//		//added for CPU routing
	}
	
public void updateCpuCodeForCashlessConversion(Intimation intimation) {
		
	 Hospitals searchbyHospitalKey = searchbyHospitalKey(intimation.getHospital());
	 if (searchbyHospitalKey != null) {
		 TmpCPUCode tmpCPUCode = getTmpCPUCode(searchbyHospitalKey.getCpuId());
		 MasProductCpuRouting gmcRoutingProduct= getMasProductForGMCRouting(intimation.getPolicy().getProduct().getKey());
		 if(gmcRoutingProduct != null){
			 if (tmpCPUCode != null && tmpCPUCode.getGmcRoutingCpuCode() != null) {
				//added for support fix IMSSUPPOR-32451
				 tmpCPUCode = getMasCpuCode(tmpCPUCode.getGmcRoutingCpuCode());
				 intimation.setCpuCode(tmpCPUCode);
				 intimation.setOriginalCpuCode(tmpCPUCode.getCpuCode());
				 entityManager.merge(intimation);
				 entityManager.flush();
			 }
		 }
		 else if (tmpCPUCode != null) {
			 intimation.setCpuCode(tmpCPUCode);
			 intimation.setOriginalCpuCode(tmpCPUCode.getCpuCode());
				entityManager.merge(intimation);
				entityManager.flush();
		 }
		 
	 }
//	//GLX2020075 commented for this GMC CPU CR since CPU Routing for GMC already done on above code -noufel
//	//added for CPU routing
//		if(intimation.getPolicy() != null && intimation.getPolicy().getProduct().getKey() != null){
//			String CpuCode= getMasProductCpu(intimation.getPolicy().getProduct().getKey());
//			if(CpuCode != null){
//				TmpCPUCode masCpuCode = getMasCpuCode(Long.valueOf(CpuCode));
//				intimation.setCpuCode(masCpuCode);
//				entityManager.merge(intimation);
//				entityManager.flush();
//			}
//		}
//		//added for CPU routing	
					
	}


public Hospitals searchbyHospitalKey(Long hosLongKey) {
	Query findByHospitalKey = entityManager.createNamedQuery(
			"Hospitals.findByKey").setParameter("key", hosLongKey);
	List<Hospitals> hospitalList = (List<Hospitals>) findByHospitalKey
			.getResultList();
	if (hospitalList.size() > 0) {
		return hospitalList.get(0);
	}
	return null;
}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public OrganaizationUnit getInsuredOfficeNameByDivisionCode(
			String issuingOfficeCode) {
		List<OrganaizationUnit> organizationList = new ArrayList<OrganaizationUnit>();
		if(issuingOfficeCode != null){
			Query findAll = entityManager.createNamedQuery("OrganaizationUnit.findByUnitId").setParameter("officeCode",issuingOfficeCode);
			organizationList = (List<OrganaizationUnit>) findAll.getResultList();
			if(organizationList != null && ! organizationList.isEmpty()){
				return organizationList.get(0);
			}
		}
		return null;
	}
	
	public TmpCPUCode getMasCpuCode(Long cpuCode){
		Query  query = entityManager.createNamedQuery("TmpCPUCode.findByCode");
		query = query.setParameter("cpuCode", cpuCode);
		List<TmpCPUCode> listOfTmpCodes = query.getResultList();
		if(null != listOfTmpCodes && !listOfTmpCodes.isEmpty()){
			return listOfTmpCodes.get(0);
		}
		return null;
	}
	
	
	public Boolean submitConvertToReimbursement(ConvertClaimDTO convertClaim,
			String option,SearchConvertClaimTableDto searchFormDto) {

		Long key = convertClaim.getKey();

		Claim claimData = ConvertClaimMapper.getInstance().getClaim(convertClaim);
		
		//convertClaim.setIntimationNumber(claimData.getIntimation().getIntimationId());

		Claim find = entityManager.find(Claim.class, key);
		entityManager.refresh(find);

		if (find != null) {
			find.setConversionReason(claimData.getConversionReason());
			if (option.equals("submit")) {
				entityManager.merge(find);
				entityManager.flush();

			} else {
				MastersValue claimTypeId = new MastersValue();
				claimTypeId.setKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
				find.setClaimType(claimTypeId);
				find.setConversionFlag(1l);
				find.setClaimSectionCode(null);
				find.setClaimCoverCode(null);
				find.setClaimSubCoverCode(null);
				find.setConversionDate(new Timestamp(System.currentTimeMillis()));
				find.setPaHospExpenseAmt(0d);
				entityManager.merge(find);
				entityManager.flush();
			////GLX2020075 commented for this GMC CPU CR since CPU Routing for GMC already done on above code -noufel
//				if(! (ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(find.getIntimation().getPolicy().getProduct().getKey()))){
					updateCpuCodeForIntimation(find.getIntimation());
//				}
				
				Map<String, Object> wrkFlowMap = (Map<String, Object>) searchFormDto.getDbOutArray();
				
				if (wrkFlowMap != null ){
					
					if(find.getLobId() != null && ReferenceTable.PA_LOB_KEY.equals(find.getLobId())){
						setBPMOutComeForConvertClaim(convertClaim, find,searchFormDto, "APPROVE");
					}
					else{
						if (wrkFlowMap.get(SHAConstants.CASHLESS_KEY) != null){
							wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.OUTCOME_FOR_REIM_SEARCH_CONVERTION_WPA);
						} else {
							wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.OUTCOME_FOR_REIM_SEARCH_CONVERTION_WQRPLY);
						}
						
						wrkFlowMap.put(SHAConstants.CLAIM_TYPE, find.getClaimType().getValue());
						
	//					Object[] objArrayForSubmit = SHAUtils.getObjArrayForSubmit(wrkFlowMap);
	//					
	//					DBCalculationService dbCalService = new DBCalculationService();
	//					dbCalService.initiateTaskProcedure(objArrayForSubmit);
						
						
						Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
						
						DBCalculationService dbCalService = new DBCalculationService();
						dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
						initiateReminderLetterProcess(wrkFlowMap);
					}

//					
//					if (wrkFlowMap.get(SHAConstants.CASHLESS_KEY) != null){
//						wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.OUTCOME_FOR_REIM_SEARCH_CONVERTION_WPA);
//					} else {
//						wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.OUTCOME_FOR_REIM_SEARCH_CONVERTION_WQRPLY);
//					}
//					
//					wrkFlowMap.put(SHAConstants.CLAIM_TYPE, find.getClaimType().getValue());
//					
////					Object[] objArrayForSubmit = SHAUtils.getObjArrayForSubmit(wrkFlowMap);
////					
////					DBCalculationService dbCalService = new DBCalculationService();
////					dbCalService.initiateTaskProcedure(objArrayForSubmit);
//					
//					
//					Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
//					
//					DBCalculationService dbCalService = new DBCalculationService();
//					dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
//					initiateReminderLetterProcess(wrkFlowMap);

				} else {
					submitDBForReimConvrSearch(find);
				}
				
//				 initiateBPMNforReimbursement(find);
				 autoRegisterFVR(find.getIntimation(), searchFormDto.getUsername());
			}
			
			return true;
		}
		return false;
	}
	
	
	public Boolean submitPAConvertToReimbursement(ConvertClaimDTO convertClaim,
			String option,SearchConvertClaimTableDto searchFormDto) {

		Long key = convertClaim.getKey();

		Claim claimData = ConvertClaimMapper.getInstance().getClaim(convertClaim);
		
		//convertClaim.setIntimationNumber(claimData.getIntimation().getIntimationId());

		Claim find = entityManager.find(Claim.class, key);
		entityManager.refresh(find);

		if (find != null) {
			find.setConversionReason(claimData.getConversionReason());
			if (option.equals("submit")) {
				entityManager.merge(find);
				entityManager.flush();

			} else {
				MastersValue claimTypeId = new MastersValue();
				claimTypeId.setKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
				find.setClaimType(claimTypeId);
				find.setProcessClaimType(SHAConstants.PA_TYPE);
				find.setConversionFlag(1l);
				find.setClaimSectionCode(null);
				find.setClaimCoverCode(null);
				find.setClaimSubCoverCode(null);
				find.setConversionDate(new Timestamp(System.currentTimeMillis()));
				find.setPaHospExpenseAmt(0d);
				entityManager.merge(find);
				entityManager.flush();
				entityManager.refresh(find);
				
				updateCpuCodeForIntimation(find.getIntimation());
				
//				initiateBPMNforReimbursement(find);
				autoRegisterFVR(find.getIntimation(),searchFormDto.getUsername());
//				setBPMOutComeForConvertClaim(convertClaim, find,searchFormDto, "APPROVE");
				
				Map<String, Object> wrkFlowMap = (Map<String, Object>) searchFormDto.getDbOutArray();
				if (wrkFlowMap != null ){
					setBPMOutComeForConvertClaim(convertClaim, find,searchFormDto, "APPROVE");
				}
				else{
					submitDBForReimConvrSearch(find);
				}
			}
			
			return true;
		}
		return false;
	}
	
	
	@SuppressWarnings("unused")
	public void setBPMOutComeForConvertClaim(ConvertClaimDTO convertClaim,Claim claim,SearchConvertClaimTableDto searchFormDto,String outCome){
		if (searchFormDto != null) {
			//XMLElement payload = searchFormDto.getHumanTask().getPayload();
		//	HumanTask humanTask = searchFormDto.getHumanTask();
//			PayloadBOType payload = humanTask.getPayloadCashless();
			
			Map<String, String> regIntDetailsReq = new HashMap<String, String>();
			Map<String,String> pedReq=new HashMap<String, String>();
			
			//ClaimRequestType claimRequest = payload.getClaimRequest();
			
			
			/*if(searchFormDto.getKey()!=null){
				
				pedReq.put("key", searchFormDto.getKey().toString());	
			}*/			
			/*if(searchFormDto.getClaimNumber()!=null){
				regIntDetailsReq.put("ClaimNumber",searchFormDto.getClaimNumber());
				//regIntDetailsReq.put("IntimationNumber",convertClaim.getIntimationNumber());
			}*/
			MastersValue claimTypeValue = getMaster(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);		
				
			/*if(humanTask == null) {*/
				
//				ClaimType claimType = payload.getClaim();
//				
//				if(null != claimType)
//				{
//					claimType.setClaimId(searchFormDto.getClaimNumber());
//					claimType.setKey(claim.getKey());
//					if(claimTypeValue != null && claimTypeValue.getValue() != null){
//						claimType.setClaimType(claimTypeValue.getValue().toUpperCase());
//					}
//				}
//				else
//				{
//					claimType = new ClaimType();
//					claimType.setClaimId(searchFormDto.getClaimNumber());
//					claimType.setKey(claim.getKey());
//					if(claimTypeValue != null && claimTypeValue.getValue() != null){
//						claimType.setClaimType(claimTypeValue.getValue().toUpperCase());
//					}
//				}
//
//			
//			ClassificationType classification = payload.getClassification();
//			classification.setSource(SHAConstants.CONVERT_CLAIM);
//			payload.setClassification(classification);
//			
//			if(payload.getClaimRequest() != null){
//				if(payload.getClaimRequest().getCpuCode() == null || (payload.getClaimRequest().getCpuCode() != null && payload.getClaimRequest().getCpuCode().equalsIgnoreCase(""))){
//					if(claim != null && claim.getIntimation() != null && claim.getIntimation().getCpuCode() != null && claim.getIntimation().getCpuCode().getCpuCode() != null){
//						ClaimRequestType claimRequest = payload.getClaimRequest();
//						claimRequest.setCpuCode(claim.getIntimation().getCpuCode().getCpuCode().toString());
//					}
//				}
//			}
//			
//			payload.setClaim(claimType);
//			humanTask.setOutcome(outCome);
//			humanTask.setPayloadCashless(payload);
			
			/*DB WORK FLOW SUBMIT*/
			
			Map<String, Object> wrkFlowMap = (Map<String, Object>) searchFormDto.getDbOutArray();
			wrkFlowMap.put(SHAConstants.PAYLOAD_REGISTRATION_DATE, claim.getCreatedDate());
			wrkFlowMap.put(SHAConstants.CLAIM_TYPE,claimTypeValue.getValue().toUpperCase());
			wrkFlowMap.put(SHAConstants.USER_ID,searchFormDto.getUsername());
			wrkFlowMap.put(SHAConstants.CPU_CODE,claim.getIntimation().getCpuCode().getCpuCode().toString());
			wrkFlowMap.put(SHAConstants.STAGE_SOURCE, claim.getStage().getStageName());
			wrkFlowMap.put(SHAConstants.PROCESS_TYPE,claimTypeValue.getValue().toUpperCase());
			String dbOutCome = SHAConstants.OUTCOME_FOR_REIM_CONVR_INPROCESS;
			wrkFlowMap.put(SHAConstants.OUTCOME,dbOutCome);
			
			Long ackKey = (Long)wrkFlowMap.get(SHAConstants.PAYLOAD_ACK_KEY);
			
			if(ackKey != null && ackKey != 0){
				
				wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_CONVERT_REIMB_CREATE_ROD);
			}			

			if(ackKey != null && ackKey != 0){
				
				DocAcknowledgement docAck = getDocAcknowledgment(ackKey);
				if(null != docAck){
					
					MastersValue masValue = new MastersValue();
					masValue.setKey(ReferenceTable.RECEIVED_FROM_INSURED);
					
					docAck.setDocumentReceivedFromId(masValue);
					
					entityManager.merge(docAck);
					entityManager.flush();
				}
				
			}
//			Object[] objArrayForSubmit = SHAUtils.getObjArrayForSubmit(wrkFlowMap);
//			
//			DBCalculationService dbCalService = new DBCalculationService();
//			dbCalService.initiateTaskProcedure(objArrayForSubmit);
			

			Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
			
			DBCalculationService dbCalService = new DBCalculationService();
			dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
			
			wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_INITIATE_REIMINDER_CNVT_PROCESS);
			initiateReminderLetterProcess(wrkFlowMap);
			
//		     initiateBPMNforReimbursement(claim);
			
//				SubmitClaimConvTask submitClaimConvTask = BPMClientContext.getSubmitClaimConvTask(searchFormDto.getUsername(), searchFormDto.getPassword());
//				try{
//				submitClaimConvTask.execute(searchFormDto.getUsername(), humanTask);
//				}catch(Exception e){
//					e.printStackTrace();
//				}
			//}
/*			else if (humanTask.getPayload() != null){
				com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType reimbursementPayload = humanTask.getPayload();
				com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.classification.ClassificationType reimbursementClassification = reimbursementPayload.getClassification();
				reimbursementClassification.setSource(SHAConstants.CONVERT_CLAIM);
				reimbursementPayload.setClassification(reimbursementClassification);
				
				com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claim.ClaimType claimType2 = reimbursementPayload.getClaim();
				
				if(null != claimType2)
				{
					claimType2.setClaimId(searchFormDto.getClaimNumber());
					claimType2.setKey(claim.getKey());
					if(claimTypeValue != null && claimTypeValue.getValue() != null){
						claimType2.setClaimType(claimTypeValue.getValue().toUpperCase());
					}
				}
				else
				{
					claimType2 = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claim.ClaimType();
					claimType2.setClaimId(searchFormDto.getClaimNumber());
					claimType2.setKey(claim.getKey());
					if(claimTypeValue != null && claimTypeValue.getValue() != null){
						claimType2.setClaimType(claimTypeValue.getValue().toUpperCase());
					}
				}
				
				reimbursementPayload.setClaim(claimType2);
				humanTask.setOutcome("APPROVE");
				
				SubmitAckProcessConvertClaimToReimbTask submitConvertReimbursement = BPMClientContext.getSubmitConvertReimbursement(searchFormDto.getUsername(), searchFormDto.getPassword());
				try{
					submitConvertReimbursement.execute(searchFormDto.getUsername(), humanTask);
				}catch(Exception e){
					e.printStackTrace();
				}
			}*/
				//InvokeHumanTask invokeHT=new InvokeHumanTask();
				//invokeHT.execute(searchFormDto.getUsername(), searchFormDto.getPassword(), searchFormDto.getHumanTask(), null, null, pedReqDetails, null);
				
				System.out.println("DB Submit Executed Successfully");
		}		
	}
	
	public void submitBpmnForRod(Long ackKey, Long workFlowKey){
		
		
		DocAcknowledgement docAcknowledgementBasedOnKey = getDocAcknowledgment(ackKey);
		
		Claim claimByKey = docAcknowledgementBasedOnKey.getClaim();
		
		NewIntimationDto newIntimationDto = new NewIntimationDto();
		ReceiptOfDocumentsDTO rodDTO = new ReceiptOfDocumentsDTO();
		Map<String,Object> workflowMap = new HashMap<String, Object>();
		workflowMap.put(SHAConstants.WK_KEY, workFlowKey);
		rodDTO.setDbOutArray(workflowMap);
		ClaimDto claimDTO = null;
		if (claimByKey != null) {
			// setClaimValuesToDTO(preauthDTO, claimByKey);
			IntimationService intimationService = new IntimationService();
			
			newIntimationDto = intimationService
					.getIntimationDto(claimByKey.getIntimation(),entityManager);
			claimDTO =  ClaimMapper.getInstance().getClaimDto(claimByKey);
			claimDTO.setNewIntimationDto(newIntimationDto);
			rodDTO.setClaimDTO(claimDTO);
		}
		rodDTO.setStrUserName("claimshead");
		rodDTO.setStrPassword("Star@123");
		
		AcknowledgementDocumentsReceivedService ackDocReceivedService = new AcknowledgementDocumentsReceivedService();

		ackDocReceivedService.submitTaskFromConvertToROD(rodDTO,
				docAcknowledgementBasedOnKey, false, false,entityManager);
	}
	
	
	
	@SuppressWarnings("unchecked")
	public DocAcknowledgement getDocAcknowledgment(Long acknowledgementKey) {

		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findByKey");
		query.setParameter("ackDocKey", acknowledgementKey);

		List<DocAcknowledgement> ackObjList = (List<DocAcknowledgement>) query
				.getResultList();

		for (DocAcknowledgement docAcknowledgement : ackObjList) {
			entityManager.refresh(docAcknowledgement);
		}

		if (ackObjList.size() > 0) {
			return ackObjList.get(0);
		}

		return null;

	}
	
	public DocAcknowledgement getDocAcknowledgment(Long acknowledgementKey,EntityManager em) {
		this.entityManager = em;
		return getDocAcknowledgment(acknowledgementKey);
	}
	
	@SuppressWarnings("unused")
	public ProcessRejectionDTO processRejectionByClaimKey(Long key) {
		Query findByClaimKey = entityManager.createNamedQuery(
				"Claim.findByIntimationKey").setParameter("intimationKey", key);
		Claim find =(Claim) findByClaimKey.getResultList().get(0);

		if (find != null) {
			ProcessRejectionDTO convertClaimDto = ProcessRejectionMapper.getInstance()
					.getProcessRejectionDTO(find);
			convertClaimDto.setClaimedAmount(find.getClaimedAmount());
			convertClaimDto.setRejectionRemarks(find.getSuggestedRejectionRemarks());
			try{
				Query findByKey = entityManager.createNamedQuery(
						"Preauth.findPreAuthIdInDescendingOrder").setParameter("intimationKey", key);
				Preauth preauth =(Preauth) findByKey.getResultList().get(0);	
				if(preauth!=null){
					convertClaimDto.setRejectionRemarks(preauth.getRemarks());
					convertClaimDto.setPremedicalCategoryId(preauth.getMedicalCategoryId());
					convertClaimDto.setIsPremedical(true);
				}
			}catch(Exception e)
			{
			}
			convertClaimDto.setIntimationNumber(find.getIntimation().getIntimationId());
			convertClaimDto.setIntimationKey(find.getIntimation().getKey());
			return convertClaimDto;
		}
		return null;
	}
	public Boolean saveProcessRejection(ProcessRejectionDTO rejectionDto,
			Boolean submitDescion,String outCome,SearchProcessRejectionTableDTO searchDTO) {

		Boolean rejectionValue = Boolean.FALSE;
		Claim find = getClaimByClaimKey(rejectionDto.getKey());
		
		Preauth preauth = getLatestPreauthByClaimKey(rejectionDto.getKey());
		
		Stage stage=new Stage();
		stage.setKey(ReferenceTable.PROCESS_REJECTION_STAGE);
		
		Status status=new Status();
		status.setKey(ReferenceTable.PROCESS_REJECTED);
		
		String username = searchDTO.getUsername();
		String userNameForDB = SHAUtils.getUserNameForDB(username);

		if (find != null) {
			Claim claim = ProcessRejectionMapper.getInstance()
					.getClaimForRejection(rejectionDto);
			claim.setClaimType(find.getClaimType());
			if (claim != null) {
				
				if (!submitDescion) {
					
					status.setKey(ReferenceTable.PROCESS_REJECTED);
					
					if(preauth != null){
						
						
						
						if(rejectionDto.getRejectionCategory() != null && rejectionDto.getRejectionCategory().getId() != null){
							MastersValue master = getMaster(rejectionDto.getRejectionCategory().getId());
							preauth.setRejectionCategorId(master);
							
						}						
						
						preauth.setRemarks(claim.getRejectionRemarks());
						preauth.setLetterVerified(rejectionDto.getPreauthDTO() != null && rejectionDto.getPreauthDTO().isLetterContentValidated() ? "Y" : "N");
						
						preauth.setStage(stage);
						preauth.setStatus(status);
						
						preauth.setModifiedBy(userNameForDB);
						preauth.setModifiedDate(new Timestamp(System.currentTimeMillis()));
						
						entityManager.merge(preauth);
						entityManager.flush();
						
						
					}else{
					
						find.setRejectionCategoryId(claim.getRejectionCategoryId());
						find.setRegistrationRemarks(claim.getRejectionRemarks());
						find.setMedicalRemarks(claim.getMedicalRemarks());
						find.setDoctorNote(claim.getDoctorNote());
						find.setStage(stage);
						find.setStatus(status);
						entityManager.merge(find);
						entityManager.flush();
				
					}
					
				} else {
					
					if(preauth != null){
						
						if((ReferenceTable.PROCESS_PRE_MEDICAL).equals(preauth.getStage().getKey())){
							outCome = SHAConstants.OUTCOME_FLP_WAIVE_REJECTION;
						}
						else if((ReferenceTable.PRE_MEDICAL_PROCESSING_ENHANCEMENT).equals(preauth.getStage().getKey())){
							outCome = SHAConstants.OUTCOME_FLE_WAIVE_REJECTION;
						}
						
						status.setKey(ReferenceTable.PREMEDICAL_WAIVED_REJECTION);
						
						preauth.setRemarks(claim.getRejectionRemarks());
						preauth.setStage(stage);
						preauth.setStatus(status);
						preauth.setModifiedBy(userNameForDB);
						preauth.setModifiedDate(new Timestamp(System.currentTimeMillis()));
						
						entityManager.merge(preauth);
						entityManager.flush();
						
					}else{
					
						status.setKey(ReferenceTable.PREMEDICAL_WAIVED_REJECTION);
					
						find.setProvisionAmount(claim.getProvisionAmount());
						find.setWaiverRemarks(claim.getWaiverRemarks());
						find.setStage(stage);
						find.setStatus(status);
						entityManager.merge(find);
						entityManager.flush();
							
						Intimation intimated=find.getIntimation();
						intimated.setRegistrationStatus("REGISTERED");
						entityManager.merge(intimated);
						entityManager.flush();

//						outCome = "PREAUTHNOTRECEIVED";
						outCome = SHAConstants.OUTCOME_REG_WAIVE_REJECTION;

						if(find != null && find.getClaimType() != null && find.getClaimType().getKey() != null && (REIMBURSEMENT_CLAIM_TYPE_KEY).equals(find.getClaimType().getKey())){
							outCome = SHAConstants.OUTCOME_REG_REIMB_WAIVE_REJECTION;
//							initiateBPMNforReimbursement(find);
							Map<String, Object> wrkFlowMap = (Map<String, Object>) searchDTO.getDbOutArray();
							wrkFlowMap.put(SHAConstants.USER_ID, searchDTO.getUsername());
							setDBOutcomeForProcessRejection(searchDTO, outCome,rejectionDto,status);
							autoRegisterFVR(find.getIntimation(),searchDTO.getUsername());
							DBCalculationService dbService = new DBCalculationService();
							wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_INITIATE_REIMINDER_PROCESS);
							wrkFlowMap.put(SHAConstants.WK_KEY, 0);
							Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
							dbService.revisedInitiateTaskProcedure(objArrayForSubmit);
							return true;

						}						
					}
				}
				
//				if(outCome.equalsIgnoreCase("REJECT")){
				
				if(SHAConstants.OUTCOME_FLP_NON_MED_CONFIRM_REJECTION.equalsIgnoreCase(outCome)){
					documentUploadedFromDMS(searchDTO, find);
				}

//				setBPMOutcomeForProcessRejection(searchDTO, outCome,rejectionDto,status);
				
				setDBOutcomeForProcessRejection(searchDTO, outCome,rejectionDto,status);

				//return true;
				rejectionValue = Boolean.TRUE;
			}

		} else {
			//return false;
			rejectionValue = Boolean.FALSE;
		}
		return rejectionValue;

	}
	
	private void documentUploadedFromDMS(SearchProcessRejectionTableDTO preauthDTO,Claim claim){
		
		try{
			if(null != preauthDTO.getDocFilePath() && !("").equalsIgnoreCase(preauthDTO.getDocFilePath()))
			{
				WeakHashMap dataMap = new WeakHashMap();
				dataMap.put("intimationNumber",claim.getIntimation().getIntimationId());
				dataMap.put("claimNumber",claim.getClaimId());
				
				if(null != claim.getClaimType())
				{
					if((ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(claim.getClaimType().getKey()))
						{
							Preauth preauthObj = getLatestPreauthByClaimKey( claim.getKey());
							if(null != preauthObj)
								dataMap.put("cashlessNumber", preauthObj.getPreauthId());
						}
				}
				
				dataMap.put("filePath", preauthDTO.getDocFilePath());
				dataMap.put("docType", preauthDTO.getDocType());
				
				dataMap.put("docSources",  "Process Rejection");
				dataMap.put("createdBy", preauthDTO.getUsername());
	
				String docToken = SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
			
	  }
		public Preauth getLatestPreauthByClaimKey(Long claimKey)
		{
			Query query = entityManager.createNamedQuery("Preauth.findLatestPreauthByClaim");
			query.setParameter("claimkey", claimKey);
			@SuppressWarnings("unchecked")
			List<Preauth> singleResult = (List<Preauth>) query.getResultList();
			if(singleResult != null && ! singleResult.isEmpty()) {
				entityManager.refresh(singleResult.get(0));
				Preauth preauth = singleResult.get(0);
				for(int i=0; i <singleResult.size(); i++) {
					entityManager.refresh(singleResult.get(i));
					if(!ReferenceTable.getRejectedPreauthKeys().containsKey(singleResult.get(i).getStatus().getKey())) {
						entityManager.refresh(singleResult.get(i));
						preauth = singleResult.get(i);
						break;
					} 
				}
				return preauth;
			}
			
			return null;
			
			
		}
	
	@SuppressWarnings("unchecked")
	public Preauth getPreauthListByIntimationKey(Long claimKey) {

		Query findByKey = entityManager.createNamedQuery(
				"Preauth.findByIntimationKey").setParameter("intimationKey",
				claimKey);

		List<Preauth> preauth = (List<Preauth>) findByKey.getResultList();

		if ( ! preauth.isEmpty()) {
			return preauth.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Preauth getPreauthListByKey(Long claimKey) {

		Query findByKey = entityManager.createNamedQuery("Preauth.findByKey")
				.setParameter("preauthKey", claimKey);

		List<Preauth> preauthList = (List<Preauth>) findByKey.getResultList();

		if (!preauthList.isEmpty()) {
			return preauthList.get(0);

		}
		return null;
	}
	
	public void setBPMOutcomeForProcessRejection(SearchProcessRejectionTableDTO resultTask,String outCome,ProcessRejectionDTO rejectionDto,Status status) {
		if (true) {/*          
			if (resultTask != null) {
				//XMLElement payload = resultTask.getHumanTask().getPayload();
				HumanTask humanTask = resultTask.getHumanTask();
				PayloadBOType payloadCashless = humanTask.getPayloadCashless();
				PedReqType pedReqType = null;
				IntimationType intimationType = null;
				Map<String, String> regIntDetailsReq = new HashMap<String, String>();
//				Map<String, String> preAuthReqMap = new HashMap<String, String>();
				Map<String,String> pedReq=new HashMap<String, String>();
				
			try {
				
				if(resultTask.getKey()!=null){
					if(null != payloadCashless)
					{
						if(null != payloadCashless.getPedReq())
						{
							pedReqType = payloadCashless.getPedReq();
							pedReqType.setKey(resultTask.getKey());
						}
						else 
						{
							pedReqType = new PedReqType();
							pedReqType.setKey(resultTask.getKey());
						}
					}
					
					pedReq.put("key", resultTask.getKey().toString());
						
				}
//					
				if(null != rejectionDto.getIntimationNumber()){
				//if(resultTask.getIntimationNo()!=null){
					if(null != payloadCashless)
					{
						if(null != payloadCashless.getIntimation())
						{
							intimationType = payloadCashless.getIntimation();
							intimationType.setIntimationNumber(rejectionDto.getIntimationNumber());
						}
						else
						{
							intimationType = new IntimationType();
							intimationType.setIntimationNumber(rejectionDto.getIntimationNumber());
						}
					}
					regIntDetailsReq.put("IntimationNumber",rejectionDto.getIntimationNumber());
				}
				
				PreAuthReqType preAuthReq = payloadCashless.getPreAuthReq();
				if(preAuthReq != null){
					preAuthReq.setOutcome(outCome);
					payloadCashless.setPreAuthReq(preAuthReq);
				}
				
				if(outCome != null && outCome.equalsIgnoreCase("PREAUTHNOTRECEIVED")){
					intimationType.setRejectionType("WAIVE");
				}
				
				payloadCashless.setPedReq(pedReqType);
				payloadCashless.setIntimation(intimationType);
				//payload = BPMClientContext.setNodeValue(payload, "RegIntDetails", regIntDetailsReq);
				//payload = BPMClientContext.setNodeValue(payload, "PedReq", pedReq);
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			if(outCome != null && outCome.equalsIgnoreCase("PREAUTHNOTRECEIVED")){
				humanTask.setOutcome("WAIVE");
			}else{
				humanTask.setOutcome(outCome);
			}
			
			ClassificationType classification = payloadCashless.getClassification();
			status = entityManager.find(Status.class, status.getKey());
			classification.setSource(status.getProcessValue());
			payloadCashless.setClassification(classification);
			
			humanTask.setPayloadCashless(payloadCashless);
			SubmitProcessRejectionTask submitProcessRejectionTask = BPMClientContext.getRefractoredProcessRejectionTask(resultTask.getUsername(), resultTask.getPassword());
			
			try{
			submitProcessRejectionTask.execute(resultTask.getUsername(), humanTask);
			}catch(Exception e){
				e.printStackTrace();
			}
		    PedReq pedReqDetails=new PedReq();
		    pedReqDetails.setKey(Long.valueOf(pedReq.get("key")));
			resultTask.getHumanTask().setPayload(payload);
					
					try {
						BPMClientContext.printPayloadElement(resultTask.getHumanTask().getPayload());
					} catch (TransformerException e) {
						e.printStackTrace();
					}
					InvokeHumanTask invokeHT=new InvokeHumanTask();
					invokeHT.execute("weblogic", "Star@123", resultTask.getHumanTask(), null, null, pedReqDetails, null);
					
					System.out.println("BPM Executed Successfully");
			}	
		*/}
			
	}
	
	public void setDBOutcomeForProcessRejection(SearchProcessRejectionTableDTO resultTask,String outCome,ProcessRejectionDTO rejectionDto,Status status) {
	
		Map<String, Object> wrkFlowMap = (Map<String, Object>) resultTask.getDbOutArray();
		
		if (true) {          
			if (wrkFlowMap != null) {
				
			try {
				wrkFlowMap.put(SHAConstants.OUTCOME,outCome);
				
				if(SHAConstants.OUTCOME_FLP_NON_MED_WAIVE_REJECTION.equalsIgnoreCase(outCome)){
					wrkFlowMap.put(SHAConstants.STAGE_SOURCE, SHAConstants.SOURCE_REJECTION_WAIVED);
				}

//				Object[] objArrayForSubmit = SHAUtils.getObjArrayForSubmit(wrkFlowMap);
//				DBCalculationService dbCalService = new DBCalculationService();
//				dbCalService.initiateTaskProcedure(objArrayForSubmit);
				
				Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
				DBCalculationService dbCalService = new DBCalculationService();
				dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);				
				
			}catch(Exception e){
				e.printStackTrace();
			}
					System.out.println("DB Submit Task Executed Successfully");
			}	
		}
			
	}

	@SuppressWarnings("unchecked")
	public List<Claim> getClaimByIntimation(Long intimationKey) {
		List<Claim> a_claimList = new ArrayList<Claim>();
		if (intimationKey != null) {

			Query findByIntimationKey = entityManager.createNamedQuery("Claim.findByIntimationKey");
			findByIntimationKey = findByIntimationKey.setParameter("intimationKey", intimationKey);
			try {

				a_claimList = (List<Claim>) findByIntimationKey.getResultList();
				
				for (Claim claim : a_claimList) {
					entityManager.refresh(claim);
				}

				System.out.println("size++++++++++++++" + a_claimList.size());

			} catch (Exception e) {
				e.printStackTrace();
			} finally {

			}

		} else {
			// intimationKey null
		}
		return a_claimList;

	}
	
	@SuppressWarnings("unchecked")
	public List<OPClaim> getOPClaimByIntimation(Long intimationKey) {
		List<OPClaim> a_claimList = new ArrayList<OPClaim>();
		if (intimationKey != null) {

			Query findByIntimationKey = entityManager.createNamedQuery("OPClaim.findByIntimationKey");
			findByIntimationKey = findByIntimationKey.setParameter("intimationKey", intimationKey);
			try {

				a_claimList = (List<OPClaim>) findByIntimationKey.getResultList();
				
				for (OPClaim claim : a_claimList) {
					entityManager.refresh(claim);
				}

				System.out.println("size++++++++++++++" + a_claimList.size());

			} catch (Exception e) {
				e.printStackTrace();
			} finally {

			}

		} else {
			// intimationKey null
		}
		return a_claimList;

	}
	
	@SuppressWarnings("unchecked")
	public List<ViewTmpClaim> getTmpClaimByIntimation(Long intimationKey) {
		List<ViewTmpClaim> a_claimList = new ArrayList<ViewTmpClaim>();
		if (intimationKey != null) {

			Query findByIntimationKey = entityManager.createNamedQuery("ViewTmpClaim.findByIntimationKey");
			findByIntimationKey = findByIntimationKey.setParameter("intimationKey", intimationKey);
			try {

				a_claimList = (List<ViewTmpClaim>) findByIntimationKey.getResultList();
				
				for (ViewTmpClaim claim : a_claimList) {
					entityManager.refresh(claim);
				}

				System.out.println("size++++++++++++++" + a_claimList.size());

			} catch (Exception e) {
				e.printStackTrace();
			} finally {

			}

		} else {
			// intimationKey null
		}
		return a_claimList;

	}

	/*public BeanItemContainer<SelectValue> getRelpseClaimsForPolicy(
			String policyNumber, String claimNumber) {
		BeanItemContainer<SelectValue> relapseClaimContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);

		if (policyNumber != null) {
			try {
				List<Claim> resultList = getClaimsByPolicyNumber(policyNumber);
				List<Claim> previousClaimsList = new ArrayList<Claim>();
				Claim currentClaim = null;
				if (!resultList.isEmpty()) {
					for (Claim claim : resultList) {
						if (claim.getClaimId() == claimNumber) {
							currentClaim = claim;
						} else {
							previousClaimsList.add(claim);
						}
					}
					Date currentClaimAdmissionDate = currentClaim
							.getIntimation().getAdmissionDate();
					for (Claim claim : previousClaimsList) {
						Date previousClaimAdmissionDate = claim.getIntimation()
								.getAdmissionDate();
						long diff = currentClaimAdmissionDate.getTime()
								- previousClaimAdmissionDate.getTime();

						int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
						if (diffDays <= 45) {
							SelectValue selectClaim = new SelectValue();
							selectClaim.setId(claim.getKey());
							selectClaim.setValue(claim.getClaimId());
							relapseClaimContainer.addBean(selectClaim);
						}

					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}*/
	
	public BeanItemContainer<SelectValue> getRelpseClaimsForPolicy(String policyNumber,String claimNumber){
		BeanItemContainer<SelectValue> relapseClaimContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		if(policyNumber != null){
		try {
			   List<Claim> resultList = getClaimsByPolicyNumber(policyNumber);
			   List<Claim> previousClaimsList = new ArrayList<Claim>();
			   Claim currentClaim = null; 
			   if(!resultList.isEmpty()){
				   for(Claim claim :resultList){
					   if(claim.getClaimId() == claimNumber){
						   currentClaim = claim;
					   } else {
						   previousClaimsList.add(claim);
					   }
				   } 
				   	  Date currentClaimAdmissionDate = currentClaim.getIntimation().getAdmissionDate();
					  for(Claim claim :previousClaimsList) {
						   Date previousClaimAdmissionDate = claim.getIntimation().getAdmissionDate();
						   long diff = currentClaimAdmissionDate.getTime() - previousClaimAdmissionDate.getTime();
					   
					   int diffDays = (int) (diff/(24*60*60*1000));
					   if(diffDays <= 45) {
						   SelectValue selectClaim = new SelectValue();
						   selectClaim.setId(claim.getKey());
						   selectClaim.setValue(claim.getClaimId());
						   relapseClaimContainer.addBean(selectClaim);
					    }
					   
					   }   
				   }
				
		}
		catch(Exception e){
			e.printStackTrace();
		}

		}
		return relapseClaimContainer;

	}

	
	
//	public List<Claim> getPreviousClaimsforPolicy(String policyNumber) {
//		List<Claim> claimResult = null;
//		if (null != policyNumber && "" != policyNumber) {
//			policyNumber = "%"
//					+ StringUtils.trim(policyNumber) + "%";
//			try {
//				final CriteriaBuilder builder = entityManager
//						.getCriteriaBuilder();
//
//				final CriteriaQuery<Intimation> intimationCriteriaQuery = builder
//						.createQuery(Intimation.class);
//				final CriteriaQuery<Policy> policyCriteriaQuery = builder
//						.createQuery(Policy.class);
//				final CriteriaQuery<Claim> claimCriteriaQuery = builder
//						.createQuery(Claim.class);
//
//				Root<Intimation> intimationRoot = intimationCriteriaQuery
//						.from(Intimation.class);
//				 
//				Root<Claim> claimRoot = claimCriteriaQuery.from(Claim.class);
//			
//				Join<Claim, Intimation> intimationJoin = claimRoot.join(
//						"intimation", JoinType.INNER);
//
//				Join<Intimation, Policy> policyJoin = intimationRoot.join(
//						"policy", JoinType.INNER);
//				
//				List<Predicate> predicates = new ArrayList<Predicate>();
//
//				Predicate intimationNotNullPredicate = builder
//						.isNotNull(claimRoot.<Intimation> get("intimation"));
//				
//				predicates.add(intimationNotNullPredicate);
//
//				if (!ValidatorUtils.isNull(policyNumber)) {
//					Predicate policyNumberPredicate = builder
//							.like(policyJoin
//									.<String> get("policyNumber"), policyNumber);
//					predicates.add(policyNumberPredicate);
//				}
//				claimCriteriaQuery.select(claimRoot).where(
//						builder.and(predicates.toArray(new Predicate[] {})));
//
//				final TypedQuery<Claim> query = entityManager
//						.createQuery(claimCriteriaQuery);
//				claimResult = query.getResultList();
//
//				
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		} else {
//			// empty parameters
//		}
//		return claimResult;
//	}
	
	public ClaimLimit getClaimLimitByKey(Long key) {
		ClaimLimit find = entityManager.find(ClaimLimit.class, key);
		entityManager.refresh(find); 
		return find;
	}
	
	public ClaimDto claimToClaimDTO(Claim claim) {
		ClaimDto claimDto = null;
		if(claim!=null){
			claimDto = new ClaimDto();
			claimDto.setKey(claim.getKey());
			claimDto.setClaimId(claim.getClaimId());
			claimDto.setCreatedDate(claim.getCreatedDate());
			claimDto.setClaimedAmount(claim.getClaimedAmount());
			claimDto.setProvisionAmount(claim.getProvisionAmount());
			claimDto.setClaimType(getSelectValue(claim.getClaimType()));
			claimDto.setClaimStatus(claim.getStatus().getProcessValue());
			claimDto.setCurrencyId(getSelectValue(claim.getCurrencyId()));
			claimDto.setIncidenceDate(claim.getIncidenceDate());
			claimDto.setDeathDate(claim.getDeathDate());
			claimDto.setAccidentDate(claim.getAccidentDate());
			claimDto.setIncidenceFlag(("A").equalsIgnoreCase(claim.getIncidenceFlag()) ? true : false);
			claimDto.setInjuryRemarks(claim.getInjuryRemarks());

			NewIntimationDto intimationDto = getIntimationService().getIntimationDto(claim.getIntimation(), entityManager);
			claimDto.setNewIntimationDto(intimationDto);			
		}
		return claimDto;
	}
	
	public ClaimDto claimToClaimDTO(ViewTmpClaim claim) {
		ClaimDto claimDto = null;
		if(claim!=null){
			claimDto = new ClaimDto();
			claimDto.setKey(claim.getKey());
			claimDto.setClaimId(claim.getClaimId());
			claimDto.setCreatedDate(claim.getCreatedDate());
			claimDto.setClaimedAmount(claim.getClaimedAmount());
			claimDto.setProvisionAmount(claim.getProvisionAmount());
			claimDto.setClaimType(getSelectValue(claim.getClaimType()));
			claimDto.setClaimStatus(claim.getStatus().getProcessValue());
			claimDto.setCurrencyId(getSelectValue(claim.getCurrencyId()));

			NewIntimationDto intimationDto = getIntimationService().getIntimationDto(claim.getIntimation(), entityManager);
			claimDto.setNewIntimationDto(intimationDto);			
		}
		return claimDto;
	}
	
	private SelectValue getSelectValue(MastersValue masValue)
	{
		SelectValue selValue = new SelectValue();
		if(masValue != null) {
			selValue.setId(masValue.getKey());
			selValue.setValue(masValue.getValue());
		}
		
		return selValue;
	}
	
	public List<Claim> getPreviousClaimsByPolicyNumber(String policyNumber) {
		Query query = entityManager.createNamedQuery("Claim.findByPolicyNumber");
		query.setParameter("policyNumber", policyNumber);
		@SuppressWarnings("unchecked")
		List<Claim> singleResult =  query.getResultList();
		return singleResult;
		
	}
	
	private IntimationService getIntimationService()
	{
		if(null == intimationService)
		{
			intimationService = new IntimationService();
		}
		
		return intimationService;
	}
	
	public List<ClaimsDailyReportDto> getClaimsDailyReport(Map<String,Object> filters){
		List<ClaimsDailyReportDto> resultDto = new ArrayList<ClaimsDailyReportDto>();
		
		Date fromDate = null;
		Date endDate = null;
		if(filters != null && !filters.isEmpty()){
			
			if(filters.containsKey("fromDate") && filters.get("fromDate") != null){
				
				fromDate = (Date)filters.get("fromDate");
			}
			
			if(filters.containsKey("endDate") && filters.get("endDate") != null){
				
				endDate = (Date)filters.get("endDate");
			}
				
		List<Claim> resultClaimList = new ArrayList<Claim>();
		
		try{
		
			if (fromDate != null && endDate != null) {			
			final CriteriaBuilder builder = entityManager
					.getCriteriaBuilder();
			final CriteriaQuery<Claim> criteriaClaimQuery = builder
					.createQuery(Claim.class);

			Root<Claim> claimRoot = criteriaClaimQuery.from(Claim.class);
			Join<Claim,Intimation> intimationJoin = claimRoot.join(
					"intimation", JoinType.INNER);
			List<Predicate> predicates = new ArrayList<Predicate>();
			
			
				Expression<Date> fromDateExpression = claimRoot
						.<Date> get("createdDate");
				Predicate fromDatePredicate = builder
						.greaterThanOrEqualTo(fromDateExpression,
								fromDate);
				predicates.add(fromDatePredicate);

				Expression<Date> toDateExpression = claimRoot
						.<Date> get("createdDate");
				Calendar c = Calendar.getInstance();
				c.setTime(endDate);
				c.add(Calendar.DATE, 1);
				endDate = c.getTime();
				Predicate toDatePredicate = builder
						.lessThanOrEqualTo(toDateExpression, endDate);
				predicates.add(toDatePredicate);
			
			criteriaClaimQuery.select(claimRoot).where(
					builder.and(predicates
							.toArray(new Predicate[] {})));

			final TypedQuery<Claim> claimQuery = entityManager
					.createQuery(criteriaClaimQuery);
		
			resultClaimList = claimQuery.getResultList();
		
		if(resultClaimList != null && !resultClaimList.isEmpty()){
			
			for(Claim claim : resultClaimList){
				entityManager.refresh(claim);
				ClaimMapper clmMapper =  ClaimMapper.getInstance();
				ClaimDto claimDto = clmMapper.getClaimDto(claim);
				NewIntimationDto intimationDto = getIntimationService().getIntimationDto(claim.getIntimation(), entityManager);
				claimDto.setNewIntimationDto(intimationDto);
				
				ClaimsDailyReportDto clmDailyReportDto = new ClaimsDailyReportDto(claimDto);
				clmDailyReportDto.setSno(resultClaimList.indexOf(claim)+1);
				DBCalculationService dbcalService = new DBCalculationService();
				Double sumInsured = dbcalService.getInsuredSumInsured(String.valueOf(claimDto.getNewIntimationDto().getInsuredPatient().getInsuredId()), claimDto.getNewIntimationDto().getPolicy().getKey()
						,claimDto.getNewIntimationDto().getInsuredPatient().getLopFlag());
				clmDailyReportDto.setSuminsured(String.valueOf(sumInsured.intValue()));
				
				Query fvrQuery = entityManager.createNamedQuery("FieldVisitRequest.findByClaimKey");
				fvrQuery.setParameter("claimKey", claimDto.getKey());
				List<FieldVisitRequest> fvrReqList = (List<FieldVisitRequest>)fvrQuery.getResultList();
				
				if(fvrReqList != null && !fvrReqList.isEmpty()){
					FieldVisitRequest fvrReq = fvrReqList.get(fvrReqList.size()-1);
					entityManager.refresh(fvrReq);
					clmDailyReportDto.setFieldDoctorNameAllocated(fvrReq.getRepresentativeName() != null ? fvrReq.getRepresentativeName() : "");
			
					if(fvrReq.getRepresentativeCode() != null){
						Query q = entityManager.createNamedQuery("TmpFvR.findByCode");
						q.setParameter("code", fvrReq.getRepresentativeCode());
						List<TmpFvR> fvrList = q.getResultList();
						if(fvrList != null && !fvrList.isEmpty()){
							entityManager.refresh(fvrList.get(0));
							String contactNo = "";
							if(fvrList.get(0).getMobileNumber() != null){
								contactNo = fvrList.get(0).getMobileNumber() != null ? fvrList.get(0).getMobileNumber().toString() : "";
							}
							if(fvrList.get(0).getPhoneNumber() != null){
								contactNo = ("").equalsIgnoreCase(contactNo) ? ( fvrList.get(0).getPhoneNumber().toString()) : ( contactNo + " / " + fvrList.get(0).getPhoneNumber().toString());
							}
							clmDailyReportDto.setContactNumOfDoctor(contactNo);
						}
					}
					
					SimpleDateFormat dtFrmt = new SimpleDateFormat("MMM-dd-yyyy HH:mm:ss");
					clmDailyReportDto.setDataOfAllocationWithTime(fvrReq.getAssignedDate() != null ? dtFrmt.format(fvrReq.getAssignedDate()).toUpperCase():"");
				}				
				
				resultDto.add(clmDailyReportDto);
			}
			
		}
		}
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
	  }	
		return resultDto;
	}

	/*   TODO  MOVED TO CLAIMSREPORTSERVICE
	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<ClaimsStatusReportDto> getClaimsStatusReport(
			Map<String, Object> filters) {
		List<ClaimsStatusReportDto> resultDtoList = new ArrayList<ClaimsStatusReportDto>();
		List<ClaimsStatusReportDto> paidResultDtoList = new ArrayList<ClaimsStatusReportDto>();
		Date fromDate = null;
		Date endDate = null;
		String stageName = null;
		Long cpuKey = null;
		if (filters != null && !filters.isEmpty()) {

			if (filters.containsKey("fromDate")
					&& filters.get("fromDate") != null) {

				fromDate = (Date) filters.get("fromDate");
			}

			if (filters.containsKey("endDate")
					&& filters.get("endDate") != null) {

				endDate = (Date) filters.get("endDate");
			}

			if (filters.containsKey("claimStageName")
					&& filters.get("claimStageName") != null) {

				stageName = (String) filters.get("claimStageName");
				
			}

			if (filters.containsKey("cpuKey") && filters.get("cpuKey") != null) {

				cpuKey = (Long) filters.get("cpuKey");
			}

			List<Reimbursement> resultReimbursementList = new ArrayList<Reimbursement>();
			List<Claim> resultClaimList = new ArrayList<Claim>();
			List<Preauth> resultPreauthList = new ArrayList<Preauth>();
			List<StageInformation> stageInfoList = new ArrayList<StageInformation>();
			
			try {				

				final CriteriaBuilder builder = entityManager
						.getCriteriaBuilder();
				
				final CriteriaQuery<StageInformation> criteriaStageInfoQuery = builder
						.createQuery(StageInformation.class);
				
				
//				final CriteriaQuery<Reimbursement> criteriaReimbQuery = builder
//						.createQuery(Reimbursement.class);
//				final CriteriaQuery<Preauth> criteriaCashlessQuery = builder
//						.createQuery(Preauth.class);				
//				final CriteriaQuery<Claim> criteriaClaimQuery = builder
//						.createQuery(Claim.class);
				
				
				Root<StageInformation> stageInfoRoot = criteriaStageInfoQuery.from(StageInformation.class);

//				Root<Preauth> preauthRoot = criteriaCashlessQuery.from(Preauth.class);
//				Root<Claim> claimRoot = criteriaClaimQuery.from(Claim.class);
				
				
				
//				Join<Claim,Intimation> intimationJoin = claimRoot.join(
//						"intimation", JoinType.INNER);				
//				
//				Root<Reimbursement> reimbRoot = criteriaReimbQuery.from(Reimbursement.class);
//				Join<Reimbursement,Claim> claimJoin = reimbRoot.join(
//						"claim", JoinType.INNER);
				List<Predicate> predicates = new ArrayList<Predicate>();

				if (stageName != null) {
					
					if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLOSED_CLAIMS)){
						
						Predicate rodStagePred = builder.equal(stageInfoRoot.<Stage>get("stage").<Long>get("key"),ReferenceTable.CREATE_ROD_STAGE_KEY);
						Predicate rodStatusPred = builder.equal(stageInfoRoot.<Status>get("status").<Long>get("key"), ReferenceTable.CREATE_ROD_CLOSED);
						
						Predicate rodClosedPred = builder.and(rodStagePred,rodStatusPred);
					
						Predicate billEntryStagePred = builder.equal(stageInfoRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.BILL_ENTRY_STAGE_KEY);
						Predicate billEntryStatusPred = builder.equal(stageInfoRoot.<Status>get("status").<Long>get("key"), ReferenceTable.BILL_ENTRY_PROCESS_CLOSED_STATUS);
					
						Predicate billingClosedPred = builder.and(billEntryStagePred,billEntryStatusPred);
						
						Predicate zonalReviewStagePred = builder.equal(stageInfoRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.PROCESS_CLAIM_REQUEST_ZONAL_STAGE_KEY);
						Predicate zonalReviewStatusPred = builder.equal(stageInfoRoot.<Status>get("status").<Long>get("key"), ReferenceTable.ZONAL_REVIEW_CLOSED);
						
						Predicate zonalreviewClosedPed = builder.and(zonalReviewStagePred,zonalReviewStatusPred);
						
						Predicate processClaimStagePred = builder.equal(stageInfoRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
						Predicate processClaimStatusPred = builder.equal(stageInfoRoot.<Status>get("status").<Long>get("key"), ReferenceTable.CLAIM_REQUEST_CLOSED);
						
						Predicate processClaimReqClosedPred = builder.and(processClaimStagePred,processClaimStatusPred); 
						
						Predicate claimBillingStagePred = builder.equal(stageInfoRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.BILLING_STAGE);
						Predicate claimBillingStatusPred = builder.equal(stageInfoRoot.<Status>get("status").<Long>get("key"), ReferenceTable.BILLING_CLOSED_STATUS);

						Predicate claimBillingClosedPred = builder.and(claimBillingStagePred,claimBillingStatusPred);
											
						Predicate finStagePred = builder.equal(stageInfoRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.FINANCIAL_STAGE);
						Predicate finStatusPred = builder.equal(stageInfoRoot.<Status>get("status").<Long>get("key"), ReferenceTable.FINANCIAL_CLOSED);

						Predicate finanClosedPred = builder.and(finStagePred,finStatusPred);
						
						Predicate claimClosedPred = builder.or(rodClosedPred,billingClosedPred,zonalreviewClosedPed,processClaimReqClosedPred,claimBillingClosedPred,finanClosedPred);
						
						predicates.add(claimClosedPred);
						
					}
					else if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.REJECTED_CLAIMS)){
				
						Predicate finRejectStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"), ReferenceTable.FINANCIAL_STAGE);
						
//						List<Long> financialRejKeyList = new ArrayList<Long>();
						
//						financialRejKeyList.add(ReferenceTable.FINANCIAL_REJECT_STATUS);
//						financialRejKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS);
//						financialRejKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_DISAPPROVE_REJECT_STATUS);
//						financialRejKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_REDRAFT_REJECT_STATUS);
						
						Predicate finRejectStatusPredicate = builder.equal(stageInfoRoot.<Status> get("status").<Long> get("key"),ReferenceTable.FINANCIAL_REJECT_STATUS);
						
						Predicate financialRejectpred = builder.and(finRejectStagePredicate,finRejectStatusPredicate);
						
						Predicate medRejectStagePredicate = builder.equal( stageInfoRoot.<Stage> get("stage").<Long> get("key"), ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
						
//						List<Long> medicalRejKeyList = new ArrayList<Long>();
//						medicalRejKeyList.add(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS);
//						medicalRejKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS);
//						medicalRejKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_DISAPPROVE_REJECT_STATUS);
//						medicalRejKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_REDRAFT_REJECT_STATUS);
						
						Predicate medRejectStatusPredicate = builder.equal( stageInfoRoot.<Status> get("status").<Long> get("key"),ReferenceTable.CLAIM_REQUEST_REJECT_STATUS);
						
						Predicate  medRejectPredicate = builder.and(medRejectStagePredicate,medRejectStatusPredicate);
						
						Predicate claimRejectPred = builder.or(medRejectPredicate,financialRejectpred);
						
						predicates.add(claimRejectPred);
						
					}
					else if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLAIMS_PAID_STAUS)){
						
						Predicate finApproveStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"), ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY);
						Predicate finApproveStatusPredicate = builder.equal(stageInfoRoot.<Status> get("status").<Long> get("key"), ReferenceTable.FINANCIAL_APPROVE_STATUS);
						Predicate approvePred = builder.and(finApproveStagePredicate,finApproveStatusPredicate); 
						predicates.add(approvePred);
					}
					else if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLAIMS_BILL_RECEIVED_STATUS)){       
						
//						List<Long> rodStageKey = new ArrayList<Long>();
//						rodStageKey.add(ReferenceTable.ACKNOWLEDGE_STAGE_KEY);					ONlY  ACK to be handled in case of bills received status.
//						rodStageKey.add(ReferenceTable.CREATE_ROD_STAGE_KEY);
//						rodStageKey.add(ReferenceTable.BILL_ENTRY_STAGE_KEY);
//						rodStageKey.add(ReferenceTable.PROCESS_CLAIM_REQUEST_ZONAL_STAGE_KEY);
//						
//						Predicate rodStagePredicate = claimRoot.<Stage> get("stage").<Long> get("key").in(rodStageKey);						
						
						List<Long> rodStageKey = new ArrayList<Long>();
						rodStageKey.add(ReferenceTable.CREATE_ROD_STAGE_KEY);
						rodStageKey.add(ReferenceTable.BILL_ENTRY_STAGE_KEY);
						rodStageKey.add(ReferenceTable.PROCESS_CLAIM_REQUEST_ZONAL_STAGE_KEY);
						Predicate rodStagePredicate = stageInfoRoot.<Stage> get("stage").<Long> get("key").in(rodStageKey);
						
						predicates.add(rodStagePredicate);
					}
					else if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLAIMS_PRE_AUTH_STATUS)){
						
						
//						preauthKeyList.add(ReferenceTable.PRE_MEDICAL_PRE_AUTH_STAGE);
						
						Predicate preAuthPremedicalStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"), ReferenceTable.PRE_MEDICAL_PRE_AUTH_STAGE);
				
						List<Long> preauthPremedicalstatusKeyList = new ArrayList<Long>();
						preauthPremedicalstatusKeyList.add(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS);
						preauthPremedicalstatusKeyList.add(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS);
						preauthPremedicalstatusKeyList.add(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS);
						
						Predicate preAuthPremedicalStatusPredicate = stageInfoRoot.<Status> get("status").<Long> get("key").in(preauthPremedicalstatusKeyList);
						
						Predicate preAuthPremedicalPredicate = builder.and(preAuthPremedicalStagePredicate,preAuthPremedicalStatusPredicate);
						
						
//						preauthKeyList.add(ReferenceTable.PREAUTH_STAGE);
						Predicate preAuthStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"),ReferenceTable.PREAUTH_STAGE);
						
						List<Long> preauthstatusKeyList = new ArrayList<Long>();
						preauthstatusKeyList.add(ReferenceTable.PREAUTH_APPROVE_STATUS);
						preauthstatusKeyList.add(ReferenceTable.PREAUTH_REJECT_STATUS);
						preauthstatusKeyList.add(ReferenceTable.PREAUTH_QUERY_STATUS);
						preauthstatusKeyList.add(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS);
						
						Predicate preAuthStatusPredicate = stageInfoRoot.<Status> get("status").<Long> get("key").in(preauthstatusKeyList);
						
						Predicate preAuthPredicate = builder.and(preAuthStagePredicate,preAuthStatusPredicate);
						
						
//						preauthKeyList.add(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_STAGE);
						Predicate preMedicalEnhStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"),ReferenceTable.PRE_MEDICAL_ENHANCEMENT_STAGE);
						
						List<Long> preMedicalEnhstatusKeyList = new ArrayList<Long>();
						preMedicalEnhstatusKeyList.add(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS);
						preMedicalEnhstatusKeyList.add(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SUGGEST_REJECTION_STATUS);
						preMedicalEnhstatusKeyList.add(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SEND_FOR_PROCESSING_STATUS);
						
						Predicate preMedicalEnhStatusPredicate = stageInfoRoot.<Status> get("status").<Long> get("key").in(preMedicalEnhstatusKeyList);
						
						Predicate preMedicalEnhPredicate = builder.and(preMedicalEnhStagePredicate,preMedicalEnhStatusPredicate);
						
						
//						preauthKeyList.add(ReferenceTable.ENHANCEMENT_STAGE);
						Predicate enhStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"),ReferenceTable.ENHANCEMENT_STAGE);
						
						List<Long> enhstatusKeyList = new ArrayList<Long>();
						enhstatusKeyList.add(ReferenceTable.ENHANCEMENT_APPROVE_STATUS);
						enhstatusKeyList.add(ReferenceTable.ENHANCEMENT_REJECT_STATUS);
						enhstatusKeyList.add(ReferenceTable.ENHANCEMENT_QUERY_STATUS);
						enhstatusKeyList.add(ReferenceTable.ENHANCEMENT_DENIAL_OF_CASHLESS_STATUS);
						enhstatusKeyList.add(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS);
						enhstatusKeyList.add(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS);
						
						Predicate enhStatusPredicate = stageInfoRoot.<Status> get("status").<Long> get("key").in(enhstatusKeyList);
						
						Predicate enhPredicate = builder.and(enhStagePredicate,enhStatusPredicate);
						
						
//						preauthKeyList.add(ReferenceTable.PROCESS_REJECTION_STAGE);
						Predicate processRejectionStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"),ReferenceTable.PROCESS_REJECTION_STAGE);
						
						List<Long> processRejectionstatusKeyList = new ArrayList<Long>();
						processRejectionstatusKeyList.add(ReferenceTable.PROCESS_REJECTED);
						processRejectionstatusKeyList.add(ReferenceTable.PREMEDICAL_WAIVED_REJECTION);
						
						Predicate processRejectionStatusPredicate = stageInfoRoot.<Status> get("status").<Long> get("key").in(processRejectionstatusKeyList);
						
						Predicate processRejectionPredicate = builder.and(processRejectionStagePredicate,processRejectionStatusPredicate);	
								
								
								
//						preauthKeyList.add(ReferenceTable.DOWNSIZE_STAGE);
						Predicate downsizeStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"),ReferenceTable.DOWNSIZE_STAGE);
						
						List<Long> downsizestatusKeyList = new ArrayList<Long>();
						downsizestatusKeyList.add(ReferenceTable.DOWNSIZE_APPROVED_STATUS);
						
						Predicate downsizeStatusPredicate = stageInfoRoot.<Status> get("status").<Long> get("key").in(downsizestatusKeyList);
						
						Predicate downsizePredicate = builder.and(downsizeStagePredicate,downsizeStatusPredicate);	
						
						
//						preauthKeyList.add(ReferenceTable.WITHDRAW_STAGE);
						Predicate withDrawStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"),ReferenceTable.WITHDRAW_STAGE);
						
						List<Long> withDrawstatusKeyList = new ArrayList<Long>();
						withDrawstatusKeyList.add(ReferenceTable.STANDALONE_WITHDRAW_STATUS);
						
						Predicate withDrawStatusPredicate = stageInfoRoot.<Status> get("status").<Long> get("key").in(withDrawstatusKeyList);
						
						Predicate withDrawPredicate = builder.and(withDrawStagePredicate,withDrawStatusPredicate);	
						
								
						Predicate cashlessPredicate = builder.or(preAuthPremedicalPredicate,preAuthPredicate,preMedicalEnhPredicate,enhPredicate,processRejectionPredicate,downsizePredicate,withDrawPredicate);
						predicates.add(cashlessPredicate); 
					}
					else if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.BILLING_COMPLETED)){
						Predicate billingStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"), ReferenceTable.BILLING_STAGE);
						Predicate billingStatusPredicate = builder.equal(stageInfoRoot.<Status> get("status").<Long> get("key"), ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER);
						
						Predicate billingApprovedPred = builder.and(billingStagePredicate,billingStatusPredicate);
						predicates.add(billingApprovedPred);
					}
					else if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.MEDICAL_APPROVAL)){
						Predicate medicalApprovStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"),ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
						Predicate medicalApprovStatusPredicate = builder.equal(stageInfoRoot.<Status> get("status").<Long> get("key"), ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS);

						Predicate medicalApprovedPred = builder.and(medicalApprovStagePredicate,medicalApprovStatusPredicate);
						predicates.add(medicalApprovedPred);
					}
					else if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLAIM_QUERY)){
						
						Predicate medicalQueryStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"),ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
						
						List<Long> queryStatusKeyList = new ArrayList<Long>();
						queryStatusKeyList.add(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
//						queryStatusKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS);
//						queryStatusKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REDRAFT_STATUS);
//						queryStatusKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS);
						
						Predicate medicalQueryStatusPredicate = stageInfoRoot.<Status> get("status").<Long> get("key").in(queryStatusKeyList);
						
						Predicate clmMedicalQueryPred = builder.and(medicalQueryStagePredicate,medicalQueryStatusPredicate);						
			
						
						Predicate financialQueryStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"),ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY);
						List<Long> finQuerystatusKeyList = new ArrayList<Long>();
						
						finQuerystatusKeyList.add(ReferenceTable.FINANCIAL_QUERY_STATUS);
//						finQuerystatusKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS);
//						finQuerystatusKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REDRAFT_STATUS);
//						finQuerystatusKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REJECT_STATUS);
						
						Predicate financialQueryStatusPredicate = stageInfoRoot.<Status> get("status").<Long> get("key").in(finQuerystatusKeyList);

						Predicate financialQueryPred = builder.and(financialQueryStagePredicate,financialQueryStatusPredicate);
						
						
						Predicate claimQueryPred = builder.or(clmMedicalQueryPred,financialQueryPred);
						
						
						predicates.add(claimQueryPred);
						
					}
				}

				if(fromDate != null && endDate != null){
//					if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLAIMS_BILL_RECEIVED_STATUS)){      ONlY  ACK to be handled in case of bills received status.
//
//						Expression<Date> fromDateExpression = claimRoot
//								.<Date> get("modifiedDate");
//						Predicate fromDatePredicate = builder
//								.greaterThanOrEqualTo(fromDateExpression,
//										fromDate);
//						predicates.add(fromDatePredicate);
//
//						Expression<Date> toDateExpression = claimRoot
//								.<Date> get("modifiedDate");
//						Calendar c = Calendar.getInstance();
//						c.setTime(endDate);
//						c.add(Calendar.DATE, 1);
//						endDate = c.getTime();					
//						Predicate toDatePredicate = builder
//								.lessThanOrEqualTo(toDateExpression, endDate);
//						predicates.add(toDatePredicate);						
//					}else
//					if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLAIMS_PRE_AUTH_STATUS)){
//						Expression<Date> fromDateExpression = stageInfoRoot
//								.<Date> get("createdDate");
//						Predicate fromDatePredicate = builder
//								.greaterThanOrEqualTo(fromDateExpression,
//										fromDate);
//						predicates.add(fromDatePredicate);
//
//						Expression<Date> toDateExpression = stageInfoRoot
//								.<Date> get("createdDate");
//						Calendar c = Calendar.getInstance();
//						c.setTime(endDate);
//						c.add(Calendar.DATE, 1);
//						endDate = c.getTime();					
//						Predicate toDatePredicate = builder
//								.lessThanOrEqualTo(toDateExpression, endDate);
//						predicates.add(toDatePredicate);
//					}
//					
//					else{
						Expression<Date> fromDateExpression = stageInfoRoot
								.<Date> get("createdDate");
						Predicate fromDatePredicate = builder
								.greaterThanOrEqualTo(fromDateExpression,
										fromDate);
						predicates.add(fromDatePredicate);

						Expression<Date> toDateExpression = stageInfoRoot
								.<Date> get("createdDate");
						Calendar c = Calendar.getInstance();
						c.setTime(endDate);
						c.add(Calendar.DATE, 1);
						endDate = c.getTime();					
						Predicate toDatePredicate = builder
								.lessThanOrEqualTo(toDateExpression, endDate);
						predicates.add(toDatePredicate);
//					}					
				}
				
				
				
					
//					if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLAIMS_BILL_RECEIVED_STATUS)){           ONlY  ACK to be handled in case of bills received status.
//						
//						Predicate clmCpuPredicate = builder.equal(claimRoot.<Intimation> get("intimation").<TmpCPUCode> get("cpuCode")
//													.<Long> get("key"), cpuKey);
//						predicates.add(clmCpuPredicate);
//					}
//					else{
//					Predicate reimbCpuPredicate = builder.equal(
//							stageInfoRoot.<Claim>get("claim").<Intimation> get("intimation")
//									.<TmpCPUCode> get("cpuCode")
//									.<Long> get("key"), cpuKey);
//					predicates.add(reimbCpuPredicate);
//					}				

//				if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLAIMS_PRE_AUTH_STATUS)){
////					criteriaCashlessQuery.select(preauthRoot).where(
////							builder.and(predicates.toArray(new Predicate[] {})));
//					
////					final TypedQuery<Preauth> finalpreauthQuery = entityManager
////							.createQuery(criteriaCashlessQuery);
////					resultPreauthList = finalpreauthQuery.getResultList();
//					
//					
//					criteriaStageInfoQuery.select(stageInfoRoot).where(
//							builder.and(predicates.toArray(new Predicate[] {})));
//					
//					final TypedQuery<StageInformation> finalpreauthQuery = entityManager
//							.createQuery(criteriaStageInfoQuery);
//										
//					stageInfoList = finalpreauthQuery.getResultList();
//					
//					if(stageInfoList != null && !stageInfoList.isEmpty()){
//						
//						for(StageInformation stageInfo : stageInfoList){
//							resultClaimList.add(stageInfo.getClaim());	
//						}
//					}					
//				}
				
//				if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLAIMS_BILL_RECEIVED_STATUS)){     ONlY  ACK to be handled in case of bills received status.
//					criteriaClaimQuery.select(claimRoot).where(
//							builder.and(predicates.toArray(new Predicate[] {})));
//					
//					final TypedQuery<Claim> finalClaimQuery = entityManager
//							.createQuery(criteriaClaimQuery);
//					resultClaimList = finalClaimQuery.getResultList();
//				}
//				else{
//				}
				
				
					criteriaStageInfoQuery.select(stageInfoRoot).where(
							builder.and(predicates.toArray(new Predicate[] {})));	
					final TypedQuery<StageInformation> reimbQuery = entityManager
							.createQuery(criteriaStageInfoQuery);
					stageInfoList = reimbQuery.getResultList();

								

//				ArrayList<Reimbursement> tempResultReimbursementList = null; 
//				if (resultClaimList != null && !resultClaimList.isEmpty()){
//					tempResultReimbursementList= new ArrayList<Reimbursement>();
//					for(Claim claimObj : resultClaimList){
//						Reimbursement reimbObj = new Reimbursement();
//						List<Reimbursement> reimbObjList = (new ReimbursementService()).getReimbursementByClaimKey(claimObj.getKey(), entityManager);
//						if(reimbObjList != null && reimbObjList.size() > 0 ){							
//							Reimbursement reimb = reimbObjList.get(0);
//							entityManager.refresh(reimb);
//							reimbObj = reimb;
//						}
//						reimbObj.setClaim(claimObj);
//						tempResultReimbursementList.add(reimbObj);
//					}					
//				}			
//
//				if(stageInfoList == null || stageInfoList.isEmpty()){     
//					stageInfoList= new ArrayList<StageInformation>();
//					if(tempResultReimbursementList != null && !tempResultReimbursementList.isEmpty()){
//						stageInfoList.addAll(tempResultReimbursementList);
//					}
//				}
//				else{
//					if(tempResultReimbursementList != null && !tempResultReimbursementList.isEmpty()){
//						resultReimbursementList.addAll(tempResultReimbursementList);
//					}	
//				}				
					List<StageInformation> finalStageList = new ArrayList<StageInformation>();
					
				if (stageInfoList != null && !stageInfoList.isEmpty()) {					
					
					if (cpuKey != null) {						
						for(StageInformation stageInfoObj :  stageInfoList){
							entityManager.refresh(stageInfoObj);
							Reimbursement reimbObj = (new ReimbursementService()).getReimbursementbyRod(stageInfoObj.getReimbursement().getKey(), entityManager);
							entityManager.refresh(reimbObj);
							Claim claim = stageInfoObj.getClaim();
							entityManager.refresh(claim);
							if(claim.getIntimation().getCpuCode().getKey().equals(cpuKey)){
								finalStageList.add(stageInfoObj);
							}
						}
					}
					else{
						finalStageList.addAll(stageInfoList);	
					}
					
					
					
					for(StageInformation stageInfo :  finalStageList){
						
						entityManager.refresh(stageInfo);
						Reimbursement reimbObj = null;
						if(stageInfo.getReimbursement().getKey() != null){
							reimbObj = (new ReimbursementService()).getReimbursementbyRod(stageInfo.getReimbursement().getKey(), entityManager);
							entityManager.refresh(reimbObj);	
						}
						
						Claim claim = stageInfo.getClaim();
						entityManager.refresh(claim);
						ClaimMapper clmMapper =  ClaimMapper.getInstance();
						ClaimDto claimDto = clmMapper.getClaimDto(claim);
						NewIntimationDto intimationDto = getIntimationService()
								.getIntimationDto(claim.getIntimation(),
										entityManager);
						claimDto.setNewIntimationDto(intimationDto);
						
						ClaimsStatusReportDto clmsStatusReportDto = new ClaimsStatusReportDto(
								claimDto);
						
						clmsStatusReportDto.setReimbursementKey(stageInfo.getReimbursement().getKey());
						clmsStatusReportDto.setSno(finalStageList
								.indexOf(stageInfo) + 1);
						String finyear="";
						if(claim.getIntimation().getCreatedDate() != null ){
							StringBuffer dateStr = new StringBuffer( new SimpleDateFormat("dd/MM/yyyy").format(claim.getIntimation().getCreatedDate()));
							finyear = dateStr.substring(dateStr.length()-4, dateStr.length());
						}
						clmsStatusReportDto.setFinacialYear(finyear);
						String diagnosis = "";
						String icdCode = "";
					    if(stageName.equalsIgnoreCase(SHAConstants.CLAIMS_PRE_AUTH_STATUS) && stageInfo.getPreauth().getKey() != null){
					    	
					    	Preauth preauthObj =  null;
							Query q = entityManager
									.createNamedQuery("Preauth.findByKey");
							q.setParameter("preauthKey", stageInfo.getPreauth().getKey());
							List<Preauth> preauthList = q.getResultList();
							if(preauthList != null && !preauthList.isEmpty()){
						
							preauthObj =  preauthList.get(0);
							entityManager.refresh(preauthObj);
							diagnosis = (new PreauthService())
									.getDiagnosisByPreauthKey(preauthObj.getKey(), entityManager);	
							Query diagQuery = entityManager
									.createNamedQuery("PedValidation.findByTransactionKey");
							diagQuery.setParameter("transactionKey", 
									preauthObj.getKey());

							List<PedValidation> diagList = diagQuery
									.getResultList();
							if (diagList != null && !diagList.isEmpty()) {

								List<DiagnosisDetailsTableDTO> diagListDto = (PreMedicalMapper.getInstance())
										.getNewPedValidationTableListDto(diagList);

								for (DiagnosisDetailsTableDTO diagDto : diagListDto) {
									
									if (diagDto.getIcdCode() != null) {
										IcdCode icdCodeObj = entityManager
												.find(IcdCode.class, diagDto
														.getIcdCode().getId());
										if (icdCodeObj != null) {
											icdCode = ("")
													.equalsIgnoreCase(icdCode) ? icdCodeObj
													.getValue() : icdCode
													+ " , "
													+ icdCodeObj.getValue();
										}

									}
								}
								clmsStatusReportDto.setIcdCode(icdCode);
								clmsStatusReportDto.setDiagnosis(diagnosis);
							}
					     }
							
							Query historyQ = entityManager.createNamedQuery("StageInformation.findCashlessByStageInfoKeyNCashlessKey");
							historyQ.setParameter("stageInfoKey",stageInfo.getKey());
							historyQ.setParameter("preauthKey",stageInfo.getPreauth().getKey());
							historyQ.setParameter("stageKey",ReferenceTable.PREAUTH_STAGE);
							historyQ.setParameter("statusKey",ReferenceTable.PREAUTH_APPROVE_STATUS);
							
							List<StageInformation> stageList = historyQ.getResultList();
							
							if(stageList != null && !stageList.isEmpty()){
								StageInformation stageObj = stageList.get(0);
								if(stageObj != null){
									entityManager.refresh(stageObj);
									clmsStatusReportDto.setPreauthApprovalDate(stageObj.getCreatedDate() != null ? new SimpleDateFormat("dd-MM-yy").format(stageObj.getCreatedDate()) : "");
								}
								if(preauthObj != null && preauthObj.getTotalApprovalAmount() != null){
									clmsStatusReportDto.setPreauthAmt(String.valueOf(preauthObj.getTotalApprovalAmount().intValue()));
								}
							}
							
							String statusValue =  stageInfo.getStage() != null && stageInfo.getStage().getStageName() != null ? stageInfo.getStage().getStageName() + " - " : ""; 
							statusValue = statusValue + ( stageInfo.getStatus() != null && stageInfo.getStatus().getProcessValue() != null ? stageInfo.getStatus().getProcessValue() : "") ;
							clmsStatusReportDto.setStatus(statusValue);
							resultDtoList.add(clmsStatusReportDto);
					    }
					    else
					    {						
					    clmsStatusReportDto.setCpuCode(claimDto.getNewIntimationDto().getCpuCode());
					    clmsStatusReportDto.setInitialProvisionAmount(claimDto.getCurrentProvisionAmount() != null ? String.valueOf(claimDto.getCurrentProvisionAmount().intValue()) : "");
					    clmsStatusReportDto.setFvrUploaded("N");
					    
					    Double deductAmt = 0d;
						Double faApprovedAmt = 0d;
						String paidDate = "";
						Double claimedAmt = 0d;
						Double billingApproveAmt = 0d;
						
//						claimedAmt =   reimbObj.getCurrentProvisionAmt() != null ? reimbObj.getCurrentProvisionAmt() : 0d;
						
						if(clmsStatusReportDto.getReimbursementKey() != null){
						Query docSummaryQuery = entityManager.createNamedQuery("RODDocumentSummary.findByReimbursementKey");
						docSummaryQuery.setParameter("reimbursementKey", clmsStatusReportDto.getReimbursementKey());
						
						List<RODDocumentSummary> docSummaryList = docSummaryQuery.getResultList();
						if(docSummaryList != null && !docSummaryList.isEmpty()){
							
							reimbObj = docSummaryList.get(0).getReimbursement();
							if(reimbObj != null){
								entityManager.refresh(reimbObj);
							}
							for(RODDocumentSummary rodBillSummary : docSummaryList){
								
								claimedAmt += rodBillSummary.getBillAmount() != null ? rodBillSummary.getBillAmount() : 0d; 		
							}						
						}
						
						}
						
						
						List<ClaimPayment> paymentListByRodNumber = (new PaymentProcessCpuService()).getPaymentDetailsByRodNumber(reimbObj.getRodNumber(),this.entityManager);
						
						if(paymentListByRodNumber != null && !paymentListByRodNumber.isEmpty()){
							
							for(ClaimPayment paymentObj : paymentListByRodNumber){
								paidDate =	paymentObj.getCreatedDate() != null ?	(("").equalsIgnoreCase(paidDate) 
														? new SimpleDateFormat("dd/MM/yy").format(paymentObj.getCreatedDate()) 
																	: paidDate + " - " + new SimpleDateFormat("dd/MM/yy").format(paymentObj.getCreatedDate())) : paidDate;
								faApprovedAmt += ( paymentObj.getApprovedAmount() != null ?  paymentObj.getApprovedAmount() : 0d); 
							
							}
						}
						
						clmsStatusReportDto.setPaidDate(paidDate);
						clmsStatusReportDto.setPaidAmout(String.valueOf(faApprovedAmt.intValue()));
						
						Reimbursement rodObj = (new ReimbursementService()).getReimbursementbyRod(clmsStatusReportDto.getReimbursementKey(), entityManager);
						
						paidDate = "";
						faApprovedAmt = 0d;
							
							if(reimbObj != null && (ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY).equals(reimbObj.getStage().getKey()) && (ReferenceTable.FINANCIAL_APPROVE_STATUS).equals(reimbObj.getStatus().getKey())){
							
								if(("0").equalsIgnoreCase(clmsStatusReportDto.getPaidAmout())){
									faApprovedAmt += reimbObj.getFinancialApprovedAmount() != null ? reimbObj.getFinancialApprovedAmount() : 0d;
								}
								
								if(("").equalsIgnoreCase(clmsStatusReportDto.getPaidDate())){
									paidDate = reimbObj.getFinancialCompletedDate() != null ? (("").equalsIgnoreCase(paidDate) ? new SimpleDateFormat("dd/MM/yy").format(reimbObj.getFinancialCompletedDate()) : paidDate + " - " + new SimpleDateFormat("dd/MM/yy").format(reimbObj.getFinancialCompletedDate())):paidDate;
								}
							}
						
//						List<RODDocumentSummary> billsList = (new CreateRODService()).getBillDetailsByRodKey(reimbObj.getKey(),entityManager);
//						
//						if(billsList != null && !billsList.isEmpty()){
//							
//							for(RODDocumentSummary rodBill : billsList){
//								entityManager.refresh(rodBill);
//								List<RODBillDetails> rodBillDetailsList = (new CreateRODService()).getBillEntryDetails(rodBill.getKey(),this.entityManager);
//								if(rodBillDetailsList != null && !rodBillDetailsList.isEmpty()){
//								
//									for(RODBillDetails rodBillDetail :rodBillDetailsList){
//										entityManager.refresh(rodBillDetail);
//										deductAmt += rodBillDetail.getNonPayableAmount() != null ? rodBillDetail.getNonPayableAmount() : 0d;
//									}
//								}
//							}
//						}
								
						if(stageName.equalsIgnoreCase(SHAConstants.SETTLED_STATUS))
						{	
							if(claimedAmt != null && claimedAmt > 0 && clmsStatusReportDto.getPaidAmout() != null ) {
								deductAmt = claimedAmt.doubleValue() > faApprovedAmt ? claimedAmt.doubleValue() - faApprovedAmt : Integer.valueOf("0");
							}
						}
						
						
						billingApproveAmt = reimbObj.getBillingApprovedAmount() != null ? reimbObj.getBillingApprovedAmount() : 0d;
						clmsStatusReportDto.setClaimedAmount(claimedAmt != null ? String.valueOf(claimedAmt.intValue()) : "");
						clmsStatusReportDto.setBillAmount(clmsStatusReportDto.getClaimedAmount());
						clmsStatusReportDto.setBillingApprovedAmount(String.valueOf(billingApproveAmt.intValue()));
						clmsStatusReportDto.setDeductedAmount(String.valueOf(deductAmt.toString()));
						clmsStatusReportDto.setPaidAmout(("0").equalsIgnoreCase(clmsStatusReportDto.getPaidAmout()) ? String.valueOf(faApprovedAmt.intValue()) : clmsStatusReportDto.getPaidAmout());
						clmsStatusReportDto.setPaidDate(("").equalsIgnoreCase(clmsStatusReportDto.getPaidDate()) ? paidDate : clmsStatusReportDto.getPaidDate());					    
					    
						DBCalculationService dbcalService = new DBCalculationService();
						Double sumInsured = dbcalService.getInsuredSumInsured(
								String.valueOf(claimDto.getNewIntimationDto()
										.getInsuredPatient().getInsuredId()),
								claimDto.getNewIntimationDto().getPolicy()
										.getKey(),claimDto.getNewIntimationDto()
										.getInsuredPatient().getLopFlag());
						clmsStatusReportDto.setSuminsured(String
								.valueOf(sumInsured.intValue()));
						
						ReimbursementQuery reimbQueryObj = (new ReimbursementService()).getReimbursementQueryByReimbursmentKey(reimbObj.getKey(),entityManager);
						if(reimbQueryObj != null){
							entityManager.refresh(reimbQueryObj);
							clmsStatusReportDto.setQueryRaisedDate(reimbQueryObj.getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yy").format(reimbQueryObj.getCreatedDate()) : "");
							clmsStatusReportDto.setQueryRaisedRemarks(reimbQueryObj.getQueryRemarks() != null ? reimbQueryObj.getQueryRemarks() : "");
							clmsStatusReportDto.setQueryReplyDate(reimbQueryObj.getQueryReplyDate() != null ? new SimpleDateFormat("dd/MM/yy").format(reimbQueryObj.getQueryReplyDate()) : "");
						
							if(stageName.equalsIgnoreCase(SHAConstants.CLAIM_QUERY))
							{
								clmsStatusReportDto.setUserId(reimbQueryObj.getModifiedBy() != null ? reimbQueryObj.getModifiedBy() : "");
							}
						
						}
						
						ReimbursementRejection reimbreject = (new ReimbursementRejectionService()).getRejectionByReimbursementKey(reimbObj.getKey(), entityManager);
						if(reimbreject != null){
							entityManager.refresh(reimbreject);
							clmsStatusReportDto.setRejectDate(reimbreject.getModifiedDate() != null ? new SimpleDateFormat("dd/MM/yy").format(reimbreject.getModifiedDate()) : "");
							clmsStatusReportDto.setRejectionRemarks(reimbreject.getRejectionRemarks() != null ? reimbreject.getRejectionRemarks(): "");
						
							if(stageName.equalsIgnoreCase(SHAConstants.REJECTED_CLAIMS))
							{
								clmsStatusReportDto.setUserId(reimbreject.getModifiedBy() != null ? reimbreject.getModifiedBy() : "");
							}
						
						}													
								icdCode ="";
								diagnosis = "";
								Query diagQuery = entityManager
										.createNamedQuery("PedValidation.findByTransactionKey");
								diagQuery.setParameter("transactionKey",reimbObj.getKey());
								List<PedValidation> diagnosisList = (List<PedValidation>) diagQuery
				 					.getResultList();
								diagnosis = "";
								if (diagnosisList != null
										&& !diagnosisList.isEmpty()) {
									for (PedValidation pedDiagnosis : diagnosisList) {

										if (pedDiagnosis != null) {
											if (pedDiagnosis.getDiagnosisId() != null) {
												Diagnosis diag = entityManager
														.find(Diagnosis.class,
																pedDiagnosis
																		.getDiagnosisId());
												if (diag != null) {
													diagnosis = !("")
															.equalsIgnoreCase(diagnosis) ? diagnosis
															+ " , "
															+ diag.getValue()
															: diag.getValue();
												}
											}
										}
										
										if (pedDiagnosis.getIcdCodeId() != null) {
											IcdCode icdCodeObj = entityManager
													.find(IcdCode.class, pedDiagnosis.getIcdCodeId());
											if (icdCodeObj != null) {
												icdCode = ("")
														.equalsIgnoreCase(icdCode) ? icdCodeObj
														.getValue() : icdCode
														+ " , "
														+ icdCodeObj.getValue();
											}

										}
										
										
										
									}
								}
								
								Query closeClaimQuery = entityManager
										.createNamedQuery("CloseClaim.getByReimbursmentKey");
								closeClaimQuery.setParameter("reimbursmentKey", reimbObj.getKey());

								List<CloseClaim> closedClaimList = closeClaimQuery.getResultList();
								
								if(closedClaimList != null && !closedClaimList.isEmpty()){
									CloseClaim closedClaim = closedClaimList.get(0);
									entityManager.refresh(closedClaim);
									String closeDate = closedClaim.getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yy").format(closedClaim.getCreatedDate()) : "";
									clmsStatusReportDto.setCloseDate(closeDate);
									clmsStatusReportDto.setClosedRemarks(closedClaim.getClosingRemarks() != null ? closedClaim.getClosingRemarks() :"");
									clmsStatusReportDto.setCloseStage(closedClaim.getStage().getStageName() != null ? closedClaim.getStage().getStageName() : "");
									clmsStatusReportDto.setUserId(closedClaim.getCreatedBy() != null ? closedClaim.getCreatedBy() : "");
									clmsStatusReportDto.setUserName(clmsStatusReportDto.getUserId());
									
								}
								
								Query docReceivedQuery = entityManager
										.createNamedQuery("DocAcknowledgement.getByClaimKey");
								docReceivedQuery.setParameter("claimKey", reimbObj.getClaim().getKey());
								List<DocAcknowledgement> docAckList = docReceivedQuery.getResultList();
								
								if( docAckList != null && !docAckList.isEmpty()){									
									clmsStatusReportDto.setNoofTimeBillRec(String.valueOf(docAckList.size()));
									DocAcknowledgement docObj = docAckList.get(0);
									entityManager.refresh(docObj);
									String billReceivedDate = docObj.getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yy").format(docObj.getCreatedDate()) : ""; 
									clmsStatusReportDto.setBillReceivedDate(billReceivedDate);
//									Double billAmount = 0d;
//									for(DocAcknowledgement docKObj : docAckList){
//									billAmount = docKObj.getHospitalizationClaimedAmount() != null ? docKObj.getHospitalizationClaimedAmount(): billAmount;
//									billAmount = docKObj.getPreHospitalizationClaimedAmount() != null ? billAmount + docKObj.getPreHospitalizationClaimedAmount() : billAmount;
//									billAmount = docKObj.getPostHospitalizationClaimedAmount() != null ? billAmount + docKObj.getPostHospitalizationClaimedAmount() : billAmount;
//									}
//									clmsStatusReportDto.setBillAmount(billAmount != null && billAmount != 0d ? String.valueOf(billAmount.intValue()) :"");
									//clmsStatusReportDto.setClaimedAmount(billAmount.toString());
									}
								
								clmsStatusReportDto.setIcdCode(icdCode);
								clmsStatusReportDto.setDiagnosis(diagnosis);
						
						String clmClassification = "";
						
						if(reimbObj != null){
							
							Query diagQ = entityManager.createNamedQuery("PedValidation.findByTransactionKey");
							diagQ.setParameter("transactionKey", reimbObj.getKey());
							
							List<PedValidation> diagList = diagQ.getResultList();
							
							if(diagList != null && !diagList.isEmpty()){
								
								for(PedValidation diagObj : diagList){
									
									Query pedValidQ = entityManager.createNamedQuery("DiagnosisPED.findByPEDValidationKey");
									pedValidQ.setParameter("pedValidationKey", diagObj.getKey());
									List<DiagnosisPED> pedValidationList = pedValidQ.getResultList();
									
									if(null != pedValidationList && !pedValidationList.isEmpty()){
										for(DiagnosisPED pedValidObj : pedValidationList){
											clmClassification += (pedValidObj.getExclusionDetails().getExclusion() != null ? pedValidObj.getExclusionDetails().getExclusion() : clmClassification) + " - " +( pedValidObj.getDiagonsisImpact() != null && pedValidObj.getDiagonsisImpact().getValue() != null ? pedValidObj.getDiagonsisImpact().getValue() : clmClassification );
										}
									}
								}
							}
						}
						
						clmsStatusReportDto.setClaimCoverage(clmClassification);
						
						String billingUser ="";
						String medicalApprover="";
						String fAApprover="";
						StageInformation result=null;
						Query stageUserquery = entityManager.createNamedQuery("StageInformation.findByReimbStageNStatus");
						
						stageUserquery.setParameter("rodKey",reimbObj.getKey());
						stageUserquery.setParameter("stageKey", ReferenceTable.BILLING_STAGE);
						stageUserquery.setParameter("statusKey", ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER);
						
						if(stageUserquery.getResultList() != null && !stageUserquery.getResultList().isEmpty() ){
							
							result = (StageInformation)stageUserquery.getResultList().get(0);
							
							entityManager.refresh(result);
							if(result != null){
								billingUser = result.getCreatedBy() != null ? result.getCreatedBy() : "";
							
								if(stageName.equalsIgnoreCase(SHAConstants.BILLING_COMPLETED))
								{
									clmsStatusReportDto.setUserId(billingUser);
									
									deductAmt = claimedAmt > billingApproveAmt ? claimedAmt - billingApproveAmt : 0d;

									clmsStatusReportDto.setDeductedAmount(String.valueOf(deductAmt));

								}
							
							}
						}
						stageUserquery.setParameter("stageKey", ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
						stageUserquery.setParameter("statusKey", ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS);    
						
						if(stageUserquery.getResultList() != null && !stageUserquery.getResultList().isEmpty()){
							
							result = (StageInformation)stageUserquery.getResultList().get(0);
							
							entityManager.refresh(result);
							if(result != null){
							
							medicalApprover = result.getCreatedBy() != null ? result.getCreatedBy() : "";
							clmsStatusReportDto.setMaApprovedDate(result.getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yy").format(result.getCreatedDate()): "");
						
							if(stageName.equalsIgnoreCase(SHAConstants.MEDICAL_APPROVAL)){
								clmsStatusReportDto.setUserId(medicalApprover);	
							}
							
							}
						}
													
						stageUserquery.setParameter("stageKey", ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY); 
						stageUserquery.setParameter("statusKey", ReferenceTable.FINANCIAL_APPROVE_STATUS);
						if(stageUserquery.getResultList() != null && !stageUserquery.getResultList().isEmpty()){
							result = (StageInformation)stageUserquery.getResultList().get(0);
							
							entityManager.refresh(result);
							if(result != null){
								fAApprover = result.getCreatedBy() != null ? result.getCreatedBy() : "";
							}
						}
						
						clmsStatusReportDto.setBillingPerson(billingUser != null ? billingUser : "");
						clmsStatusReportDto.setMedicalApprovalPerson(medicalApprover != null ? medicalApprover : "");
						clmsStatusReportDto.setFinancialApprovalPerson(fAApprover != null ? fAApprover : "");
						clmsStatusReportDto.setUserName(clmsStatusReportDto.getUserId());						
						
										
						resultDtoList.add(clmsStatusReportDto);
				} 
				}
					if(stageName.equalsIgnoreCase(SHAConstants.CLAIMS_PAID_STAUS)){
						
						List<ClaimsStatusReportDto> paidList = new ArrayList<ClaimsStatusReportDto>();
						SimpleDateFormat dtFrmt = new SimpleDateFormat("dd/MM/yy");
						if(null != fromDate )
						{
							fromDate = dtFrmt.parse(fromDate.toString());
						}
						if(null != endDate)
						{
							endDate =  dtFrmt.parse(endDate.toString());
						}
						for(ClaimsStatusReportDto resultDto : resultDtoList){
							
							resultDto.setUserId(resultDto.getFinancialApprovalPerson());
							resultDto.setUserName(resultDto.getUserId());
							if(fromDate.compareTo(endDate) == 0){
								if(dtFrmt.parse(resultDto.getPaidDate()).compareTo(fromDate) == 0){
									paidList.add(resultDto);
								}
							}
							else{
								if(dtFrmt.parse(resultDto.getPaidDate()).compareTo(fromDate) == 0){
									paidList.add(resultDto);
								}
								if(dtFrmt.parse(resultDto.getPaidDate()).compareTo(fromDate) > 0){
									paidList.add(resultDto);
								}
								if(dtFrmt.parse(resultDto.getPaidDate()).compareTo(endDate) < 0){
									paidList.add(resultDto);
								}
								
							}
						}
						
						if(paidList != null && !paidList.isEmpty()){
							resultDtoList.clear();
							for(ClaimsStatusReportDto paidDto :paidList){
								paidDto.setSno(paidList.indexOf(paidDto)+1);
								resultDtoList.add(paidDto);
							}
						}
						
					}					
					
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
		return resultDtoList;
	 }*/
	
	@SuppressWarnings({ "unchecked", "unused" })
	public MastersValue getMaster(Long a_key) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		MastersValue a_mastersValue = new MastersValue();
		if (a_key != null) {
			Query query = entityManager
					.createNamedQuery("MastersValue.findByKey");
			query = query.setParameter("parentKey", a_key);
			List<MastersValue> mastersValueList = query.getResultList();
			for (MastersValue mastersValue : mastersValueList)
				a_mastersValue = mastersValue;
		}

		return a_mastersValue;
	}
	
	public List<MedicalAuditCashlessIssuanceReportDto> getMedicalAuditCashlessIssuanceDetails(Map<String,Object> searchFilter){
		List<MedicalAuditCashlessIssuanceReportDto> resultListDto = new ArrayList<MedicalAuditCashlessIssuanceReportDto>();
		
		if(searchFilter != null && !searchFilter.isEmpty()){
			
			try{

				String status = null;
				Date fromDate = null;
				Date toDate = null;

				if (searchFilter.containsKey("fromDate") && searchFilter.get("fromDate") != null){
					fromDate = (Date)searchFilter.get("fromDate");
				}

				if (searchFilter.containsKey("toDate") && searchFilter.get("toDate") != null) {
					toDate = (Date)searchFilter.get("toDate");
					Calendar c = Calendar.getInstance();
					c.setTime(toDate);
					c.add(Calendar.DATE, 1);
					toDate = c.getTime();
				}

				if (searchFilter.containsKey("status") && searchFilter.get("status") != null) {
					status = StringUtils.trim(searchFilter.get("status").toString());
				}

				if (status != null || (fromDate != null && toDate != null)) {

					final CriteriaBuilder builder = entityManager
							.getCriteriaBuilder();
					
					final CriteriaQuery<Reimbursement> criteriaReimbQuery = builder
							.createQuery(Reimbursement.class);
					
					Root<Reimbursement> reimbRoot =  criteriaReimbQuery.from(Reimbursement.class);
					
					Join<Reimbursement,Claim> claimJoin = reimbRoot.join(
							"claim", JoinType.INNER);
					
					List<Predicate> predicates = new ArrayList<Predicate>();
					
					Predicate claimTypePred = builder.equal(reimbRoot.<Claim>get("claim").<MastersValue>get("claimType").<Long>get("key"), ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
					predicates.add(claimTypePred);
					
					Predicate billClassificationPred = builder.like(builder.lower(reimbRoot.<DocAcknowledgement>get("docAcknowLedgement").<String>get("hospitalisationFlag")),"y"); 
					predicates.add(billClassificationPred);
					
				//			SHAConstants.SETTLED_STATUS
				
					
					Predicate claimStagePred = builder.equal(reimbRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY);
					Predicate claimStatusPred = builder.equal(reimbRoot.<Status>get("status").<Long>get("key"),ReferenceTable.FINANCIAL_APPROVE_STATUS);
					Predicate approvedPred = builder.and(claimStagePred,claimStatusPred);
					
					if(status.equalsIgnoreCase(SHAConstants.SETTLED_STATUS)){
						predicates.add(approvedPred);
					}
				
				//			SHAConstants.REJECTED_STATUS
					
					Predicate medicalRejectStagePred = builder.equal(reimbRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
					Predicate medicalRejectstatusPred = builder.equal(reimbRoot.<Status>get("status").<Long>get("key"),ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS);
					
					Predicate medicalRejectPred = builder.and(medicalRejectStagePred,medicalRejectstatusPred);

					
					Predicate finanRejectStagePred = builder.equal(reimbRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY);
					Predicate finanRejectstatusPred = builder.equal(reimbRoot.<Status>get("status").<Long>get("key"),ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS);
					
					Predicate financialRejectPred = builder.and(finanRejectStagePred,finanRejectstatusPred);
					
					
					Predicate claimRejectPred = builder.or(medicalRejectPred,financialRejectPred);
				
					if(status.equalsIgnoreCase(SHAConstants.REJECTED_STATUS)){
						predicates.add(claimRejectPred);
					}
					
				
				//				SHAConstants.CLOSED_STATUS
									
					Predicate rodStagePred = builder.equal(reimbRoot.<Stage>get("stage").<Long>get("key"),ReferenceTable.CREATE_ROD_STAGE_KEY);
					Predicate rodStatusPred = builder.equal(reimbRoot.<Status>get("status").<Long>get("key"), ReferenceTable.CREATE_ROD_CLOSED);
					
					Predicate rodClosedPred = builder.and(rodStagePred,rodStatusPred);
				
					Predicate billEntryStagePred = builder.equal(reimbRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.BILL_ENTRY_STAGE_KEY);
					Predicate billEntryStatusPred = builder.equal(reimbRoot.<Status>get("status").<Long>get("key"), ReferenceTable.BILL_ENTRY_PROCESS_CLOSED_STATUS);
				
					Predicate billingClosedPred = builder.and(billEntryStagePred,billEntryStatusPred);
					
					Predicate zonalReviewStagePred = builder.equal(reimbRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.PROCESS_CLAIM_REQUEST_ZONAL_STAGE_KEY);
					Predicate zonalReviewStatusPred = builder.equal(reimbRoot.<Status>get("status").<Long>get("key"), ReferenceTable.ZONAL_REVIEW_CLOSED);
					
					Predicate zonalreviewClosedPed = builder.and(zonalReviewStagePred,zonalReviewStatusPred);
					
					Predicate processClaimStagePred = builder.equal(reimbRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
					Predicate processClaimStatusPred = builder.equal(reimbRoot.<Status>get("status").<Long>get("key"), ReferenceTable.CLAIM_REQUEST_CLOSED);
					
					Predicate processClaimReqClosedPred = builder.and(processClaimStagePred,processClaimStatusPred); 
					
					Predicate claimBillingStagePred = builder.equal(reimbRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.BILLING_STAGE);
					Predicate claimBillingStatusPred = builder.equal(reimbRoot.<Status>get("status").<Long>get("key"), ReferenceTable.BILLING_CLOSED_STATUS);

					Predicate claimBillingClosedPred = builder.and(claimBillingStagePred,claimBillingStatusPred);
										
					Predicate finStagePred = builder.equal(reimbRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.FINANCIAL_STAGE);
					Predicate finStatusPred = builder.equal(reimbRoot.<Status>get("status").<Long>get("key"), ReferenceTable.FINANCIAL_CLOSED);

					Predicate finanClosedPred = builder.and(finStagePred,finStatusPred);
					
					Predicate claimClosedPred = builder.or(rodClosedPred,billingClosedPred,zonalreviewClosedPed,processClaimReqClosedPred,claimBillingClosedPred,finanClosedPred);
					
					if(status.equalsIgnoreCase(SHAConstants.CLOSED_STATUS)){
						predicates.add(claimClosedPred);
					}
				
				//		SHAConstants.PENDING_STATUS
					
//					Predicate finPendingStagePred = builder.equal(reimbRoot.<Claim>get("claim").<Stage>get("stage").<Long>get("key"), ReferenceTable.FINANCIAL_STAGE);
//					
//					List<Long> claimFinstatusKeyList = new ArrayList<Long>();
//					claimFinstatusKeyList.add(ReferenceTable.FINANCIAL_QUERY_STATUS);
//					claimFinstatusKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS);
//					claimFinstatusKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REDRAFT_STATUS);
//					claimFinstatusKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REJECT_STATUS);
//					claimFinstatusKeyList.add(ReferenceTable.FINANCIAL_REJECT_STATUS);
//					claimFinstatusKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_DISAPPROVE_REJECT_STATUS);
//					claimFinstatusKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_REDRAFT_REJECT_STATUS);
//					
//					Predicate finPendingStatusPred = reimbRoot.<Claim>get("claim").<Status>get("status").<Long>get("key").in(claimFinstatusKeyList);
//					
//					Predicate finClaimPedingPred = builder.and(finPendingStagePred,finPendingStatusPred);
//					
//					
//					Predicate medicalStagePred = builder.equal(reimbRoot.<Claim>get("claim").<Stage>get("stage").<Long>get("key"),ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY); 
//					List<Long> medicalStatusKeyList = new ArrayList<Long>();
//					
//					medicalStatusKeyList.add(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
//					medicalStatusKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS);
//					medicalStatusKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REDRAFT_STATUS);
//					medicalStatusKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS);
//					medicalStatusKeyList.add(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS);
//					medicalStatusKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_DISAPPROVE_REJECT_STATUS);
//					medicalStatusKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_REDRAFT_REJECT_STATUS);
//					
//					Predicate medicalStatusPred = reimbRoot.<Claim>get("claim").<Status>get("status").<Long>get("key").in(medicalStatusKeyList);
//					
//					Predicate medicalPedndingPred = builder.and(medicalStagePred,medicalStatusPred);
//					
//					List<Long> pendingStageKeyList = new ArrayList<Long>();
//					
//					pendingStageKeyList.add(ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
//					pendingStageKeyList.add(ReferenceTable.CREATE_ROD_STAGE_KEY);
//					pendingStageKeyList.add(ReferenceTable.BILL_ENTRY_STAGE_KEY);
//					pendingStageKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_ZONAL_STAGE_KEY);
//					pendingStageKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
//					pendingStageKeyList.add(ReferenceTable.BILLING_STAGE);
//					Predicate otherPendingStagePred = reimbRoot.<Claim>get("claim").<Stage>get("stage").<Long>get("key").in(pendingStageKeyList);
//					
//					Predicate claimPendingPred = builder.or(finClaimPedingPred,medicalPedndingPred,otherPendingStagePred);
					
					if(status.equalsIgnoreCase(SHAConstants.PENDING_STATUS)){
						
						Predicate notClosedPred = builder.not(claimClosedPred);
						Predicate notRejectedPred = builder.not(claimRejectPred);
						Predicate notSettledPred = builder.not(approvedPred);
						
						Predicate pedingPred = builder.and(notClosedPred,notRejectedPred,notSettledPred);
						predicates.add(pedingPred);
						
//						predicates.add(claimPendingPred);
					
					}
				
				if(fromDate != null && toDate != null) {
				Expression<Date> fromDateExpression = reimbRoot.<Claim>get("claim")
						.<Date> get("modifiedDate");
				Predicate fromDatePredicate = builder
						.greaterThanOrEqualTo(fromDateExpression,
								fromDate);
				predicates.add(fromDatePredicate);

				Expression<Date> toDateExpression = reimbRoot.<Claim>get("claim")
						.<Date> get("modifiedDate");
				Calendar c = Calendar.getInstance();
				c.setTime(toDate);
				c.add(Calendar.DATE, 1);
				toDate = c.getTime();					
				Predicate toDatePredicate = builder
						.lessThanOrEqualTo(toDateExpression, toDate);
				predicates.add(toDatePredicate);
				
				}
				criteriaReimbQuery.select(reimbRoot).where(
						builder.and(predicates.toArray(new Predicate[] {})));

				final TypedQuery<Reimbursement> reimbusementQuery = entityManager
						.createQuery(criteriaReimbQuery);

				SHAUtils.popinReportLog(entityManager, "", "MedicalAuditCashlessIssuanceReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
				List<Reimbursement> resultreimbList = reimbusementQuery.getResultList();

				if (resultreimbList != null && !resultreimbList.isEmpty()) {
					Claim claimObj;
					Hospitals hospObj;
					String hospCode;
					MedicalAuditCashlessIssuanceReportDto auditClaimDto;
					ReimbursementQuery reimbQuery;
					List<Preauth> preauthObjList;
					for (Reimbursement reimbursementObj : resultreimbList) {
						entityManager.refresh(reimbursementObj);
						claimObj = reimbursementObj.getClaim();
//						ClaimMapper clmMapper =  ClaimMapper.getInstance();
//						ClaimDto claimDto = clmMapper.getClaimDto(claimObj);
//						NewIntimationDto intimationDto = getIntimationService().getIntimationDto(claimObj.getIntimation(), entityManager);
//						claimDto.setNewIntimationDto(intimationDto);
//						MedicalAuditCashlessIssuanceReportDto auditClaimDto = new MedicalAuditCashlessIssuanceReportDto(claimDto);
						
						hospObj = getHospitalById(claimObj.getIntimation().getHospital());
						hospCode = hospObj.getHospitalCode() != null ? hospObj.getHospitalCode() : "";
						
						auditClaimDto = new MedicalAuditCashlessIssuanceReportDto(claimObj,hospCode);
						preauthObjList = (new PreauthService()).getPreauthListByDescendingOrder(claimObj.getKey(),entityManager);
						if(preauthObjList != null && !preauthObjList.isEmpty()){
							Preauth preauthObj = preauthObjList.get(0);
							entityManager.refresh(preauthObj);
							auditClaimDto.setPreAuthAmount(preauthObj.getTotalApprovalAmount() != null ? String.valueOf(preauthObj.getTotalApprovalAmount().intValue()) : "");
						}
						
//						List<Reimbursement> reimbList = (new ReimbursementService()).getReimbursementByClaimKey(claimObj.getKey(),entityManager);
						entityManager.refresh(reimbursementObj);
						auditClaimDto.setGeneralRemarks(reimbursementObj.getApprovalRemarks() != null ? reimbursementObj.getApprovalRemarks() : "");
						auditClaimDto.setDoctorNote(reimbursementObj.getDoctorNote() != null ? reimbursementObj.getDoctorNote() : "");
						auditClaimDto.setFinalRemarks(reimbursementObj.getFinancialApprovalRemarks() != null ? reimbursementObj.getFinancialApprovalRemarks() : "");
							
						reimbQuery = (new ReimbursementService()).getReimbursementQueryByReimbursmentKey(reimbursementObj.getKey(),entityManager);
							
						if(reimbQuery != null){
							String queryRemarks = reimbQuery.getQueryRemarks() != null ? reimbQuery.getQueryRemarks() : "";
							auditClaimDto.setQueryRaisedOrMedRejReq(reimbursementObj.getRejectionRemarks() != null ? queryRemarks + " / " + reimbursementObj.getRejectionRemarks() : queryRemarks);
						}							
							
						StringBuffer diagnosis = new StringBuffer("");
						Query diagQuery = entityManager
									.createNamedQuery("PedValidation.findByTransactionKey");
						diagQuery.setParameter("transactionKey",reimbursementObj.getKey());
						List<PedValidation> diagnosisList = (List<PedValidation>) diagQuery.getResultList();
							if (diagnosisList != null
									&& !diagnosisList.isEmpty()) {
								for (PedValidation pedDiagnosis : diagnosisList) {

									if (pedDiagnosis != null) {
										if (pedDiagnosis.getDiagnosisId() != null) {
											Diagnosis diag = entityManager
													.find(Diagnosis.class,
															pedDiagnosis
																	.getDiagnosisId());
											if (diag != null) {
												diagnosis = !("")
														.equalsIgnoreCase(diagnosis.toString()) ? diagnosis.append(" , "+ diag.getValue())
														: diagnosis.append(diag.getValue());
											}
										}
									}
								}
							}							
						auditClaimDto.setDiagnosis(diagnosis.toString());
						auditClaimDto.setReBillingOrReQuery("");
						resultListDto.add(auditClaimDto);	
						auditClaimDto = null;
					}
				}
				
				SHAUtils.popinReportLog(entityManager, "", "MedicalAuditCashlessIssuanceReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
				}	
				
			
			}
			catch(Exception e){
				SHAUtils.popinReportLog(entityManager, "", "MedicalAuditCashlessIssuanceReport",new Date(),new Date(),SHAConstants.RPT_ERROR);
				e.printStackTrace();
			}
		}
		
		return resultListDto;
	}
	
	public ClaimDto getClaimDto(Claim claimObj, EntityManager em){
		this.entityManager = em;
		ClaimDto claimDto = new ClaimDto();
		if(claimObj != null){
			claimDto = ClaimMapper.getInstance().getClaimDto(claimObj);
			NewIntimationDto intimationDto = (new IntimationService()).getIntimationDto(claimObj.getIntimation(), entityManager);
			claimDto.setNewIntimationDto(intimationDto);
		}
	return claimDto;
	}
	
	
	public DocumentDetails getDocumentDetailsBasedOnDocToken(String documentToken)
	{
		Query query = entityManager.createNamedQuery("DocumentDetails.findByDocumentToken");
		if(null != documentToken)
		{
			query = query.setParameter("documentToken", Long.valueOf(documentToken));
			List<DocumentDetails> documentDetailsList = query.getResultList();
			if(null != documentDetailsList && !documentDetailsList.isEmpty())
			{
				entityManager.refresh(documentDetailsList.get(0));
				return documentDetailsList.get(0);
			}
		}
		return null;
	}
	public DocumentDetails getRODBillStatus(String rodNumber)
	{
		Query query = entityManager.createNamedQuery("DocumentDetails.findRODBillByRodNoDesc");
		if(null != rodNumber)
		{
			query = query.setParameter("reimbursementNumber", rodNumber);
			List<DocumentDetails> documentDetailsList = query.getResultList();
			if(null != documentDetailsList && !documentDetailsList.isEmpty())
			{
				entityManager.refresh(documentDetailsList.get(0));
				return documentDetailsList.get(0);
			}
		}
		return null;
	}
	/***
	 * Method to update the claim. Specially used for
	 * generate covering letter service.
	 * **/
	public void updateClaim(Long claimKey)
	{
		 Claim claim = getClaimByKey(claimKey);
		 Long coveringLetterFlag = claim.getStatus().getProcessValue().toLowerCase().contains("Registered") ? 1l : 0l ;
		 claim.setConversionLetter(coveringLetterFlag);	
		
		if(null != claim && null != claim.getKey())
		{
			entityManager.merge(claim);
			entityManager.flush();
			entityManager.refresh(claim);
		}
	}
	
	public ViewTmpIntimation getTmpIntimationObject(Long key)
	{
		Query query = entityManager.createNamedQuery("ViewTmpIntimation.findByKey");
		query = query.setParameter("intiationKey", key);
		List<ViewTmpIntimation> tmpIntimationList = query.getResultList();
		if(null != tmpIntimationList && !tmpIntimationList.isEmpty())
		{
			entityManager.refresh(tmpIntimationList.get(0));
			return tmpIntimationList.get(0);
		}
		return null;
	}
	
	public CloseClaim getCloseClaimByReimbursementKey(Long reimbursementKey){
		
		Query closeClaimQuery = entityManager
				.createNamedQuery("CloseClaim.getByReimbursmentKey");
		closeClaimQuery.setParameter("reimbursmentKey", reimbursementKey);

		List<CloseClaim> closedClaimList = closeClaimQuery.getResultList();
		
		if(closedClaimList != null && ! closedClaimList.isEmpty()){
			return closedClaimList.get(0);
		}
		
		return null;
	}
	
	public TmpFvR getMasFVR(String representativeCode)
	{	TmpFvR tmpFVR =null;
		if (representativeCode != null) {
			Query tmpFVRQuery = entityManager.createNamedQuery(
					"TmpFvR.findByCode")
					.setParameter("code", representativeCode);
			 tmpFVR = (TmpFvR) tmpFVRQuery.getSingleResult();
			 if (tmpFVR != null){
				 return tmpFVR;
			 }
		}
		return tmpFVR;
	}
	
	public void uploadCoveringLetterToDMs(ClaimDto claimDto){

		if(null != claimDto.getDocFilePath() && !claimDto.getDocFilePath().isEmpty())
		{
			String strUserName = claimDto.getModifiedBy();
			String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
			
			WeakHashMap dataMap = new WeakHashMap();
			if(null != claimDto)
			{
				dataMap.put("intimationNumber",claimDto.getNewIntimationDto().getIntimationId());
				dataMap.put("claimNumber",claimDto.getClaimId());

				dataMap.put("filePath", claimDto.getDocFilePath());
				dataMap.put("docType", claimDto.getDocType());
				dataMap.put("docSources", SHAConstants.GENERATE_COVERING_LETTER);
				dataMap.put("createdBy", userNameForDB);
				String docToken = SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);
			}			
			
			Claim claimObj = getClaimByClaimKey(claimDto.getKey());
			
			if(claimObj != null){
				claimObj.setConversionLetter(claimDto.getConversionLetter());
				claimObj.setModifiedDate(new Date());
				claimObj.setModifiedBy(claimDto.getCreatedBy());
				entityManager.merge(claimObj);
				entityManager.flush();
				entityManager.refresh(claimObj);
//				saveCoveringLetterDocDetails(claimDto,claimObj);
			}			
		}
	}
	

	public void submitDBForReimConvrSearch(Claim claim){
		
		Hospitals hospitalById = getHospitalById(claim.getIntimation().getHospital());
		
//		Object[] arrayListForDBCall = SHAUtils.getArrayListForDBCall(claim, hospitalById);
		Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(claim, hospitalById);
		
		Object[] inputArray = (Object[])arrayListForDBCall[0];
		inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.OUTCOME_FOR_REIM_SEARCH_CONVERTION;

		Object[] parameter = new Object[1];
		parameter[0] = inputArray;
		
		DBCalculationService dbCalculationService = new DBCalculationService();
//		dbCalculationService.initiateTaskProcedure(parameter);
		dbCalculationService.revisedInitiateTaskProcedure(parameter);
		
		Map<String,Object> workFlowMap = SHAUtils.getRevisedPayloadMap(claim,hospitalById);
		
		workFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_INITIATE_REIMINDER_CNVT_PROCESS);
		initiateReminderLetterProcess(workFlowMap);
		
		
	}
	
	public void submitProvisionHistory(ProvisionUploadHistory provisionHistory){
		
//		Query query = entityManager.createQuery("select SEQ_HISTORY_KEY.nextval as num from ProvisionUploadHistory");
//		Long nextValue = ((BigInteger)query.getSingleResult()).longValue();
//		
//		String batchNo = "BU"+nextValue;
		
//		provisionHistory.setBatchNumber(batchNo);
		
		
		
		entityManager.persist(provisionHistory);
		entityManager.flush();
		
	}
	
	public void updateClaimForRevisedProvision(Claim claim){
//		entityManager.merge(claim);
//		entityManager.flush();
		
		String query = "UPDATE IMS_CLS_CLAIM SET CURRENT_PROVISION_AMOUNT = "+claim.getRevisedProvisionAmount()+ ",MODIFIED_DATE = systimestamp WHERE CLAIM_KEY ="+claim.getKey();
		
		Query nativeQuery = entityManager.createNativeQuery(query);
		nativeQuery.executeUpdate();

	}
	
	 public List<Claim> getClaimObjectByLikeOperator(String intimationNo , String policyNo)
	 {
	 	Query query = null;
	 	if(null != policyNo && !("").equalsIgnoreCase(policyNo))
	 	{
	 		query = entityManager.createNamedQuery("Claim.findByLikeIntimationNoAndPolicyNo");
	     	query = query.setParameter("policyNumber", "%" + policyNo +"%");
	 	}
	 	else
	 	{
	 		 query = entityManager.createNamedQuery("Claim.findByIntimationId");
	 	}
	 	query = query.setParameter("intimationNumber", "%" +intimationNo +"%" );
	 	List<Claim> claimList = query.getResultList();
	 	if(null != claimList && !claimList.isEmpty())
	 	{
	 		
	 		return claimList;
	 	}
	 	return null;
	 }
	public Intimation getIntimationByKey(Long intimationKey) {

		Query findByKey = entityManager
				.createNamedQuery("Intimation.findByKey").setParameter(
						"intiationKey", intimationKey);

		List<Intimation> intimationList = (List<Intimation>) findByKey
				.getResultList();

		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);

		}
		return null;
	}
	

//	public void uploadCoveringLetterToDMs(ClaimDto claimDto){
//
//		if(null != claimDto.getDocFilePath() && !claimDto.getDocFilePath().isEmpty())
//		{
//			String strUserName = claimDto.getModifiedBy();
//			String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
//			
//			HashMap dataMap = new HashMap();
//			if(null != claimDto)
//			{
//				dataMap.put("intimationNumber",claimDto.getNewIntimationDto().getIntimationId());
//				dataMap.put("claimNumber",claimDto.getClaimId());
//
//				dataMap.put("filePath", claimDto.getDocFilePath());
//				dataMap.put("docType", claimDto.getDocType());
//				dataMap.put("docSources", SHAConstants.GENERATE_COVERING_LETTER);
//				dataMap.put("createdBy", userNameForDB);
//				String docToken = SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);
//
//			}
//		}
//	}
	
	
	public List<SearchShadowProvisionDTO> getErrorLogDetailsForShadow(String batchNumber){
		
		Query provisionHistory = entityManager
				.createNamedQuery("ProvisionUploadHistory.findByBatchNumber");
		provisionHistory.setParameter("batchNumber", batchNumber.toLowerCase());

		List<ProvisionUploadHistory> history = (List<ProvisionUploadHistory>) provisionHistory.getResultList();
		
		List<SearchShadowProvisionDTO> resultList = new ArrayList<SearchShadowProvisionDTO>();
		
		for (ProvisionUploadHistory provisionUploadHistory : history) {
			SearchShadowProvisionDTO provisionDTO = new SearchShadowProvisionDTO();
			provisionDTO.setIntimationNumber(provisionUploadHistory.getIntimationNumber());
			provisionDTO.setExistingProvision(provisionUploadHistory.getExistingProvisionAmt() != null ? provisionUploadHistory.getExistingProvisionAmt().intValue() : 0);
			provisionDTO.setNewProvision(provisionUploadHistory.getCurrentProvisonAmt() != null ? provisionUploadHistory.getCurrentProvisonAmt().intValue() : 0);
			provisionDTO.setRemarks(provisionUploadHistory.getRemarks());
			provisionDTO.setErrDescription(provisionUploadHistory.getRemarks());
			resultList.add(provisionDTO);
		}
		
		return resultList;
		
	}
	public void insertLoginDetails(String userId){
		
		try{
			
		UserLoginDetails userLoginDetail = getUserLoginDetailBasedOnKey(userId);
		if(userLoginDetail != null && userLoginDetail.getActiveStatus() != null && userLoginDetail.getActiveStatus().equals(0L)){
			
				userLoginDetail.setLoginDateTime(new Date());
				userLoginDetail.setModifiedDate(new Date());
				userLoginDetail.setModifiedBy(userId);
				userLoginDetail.setLogoutDateTime(null);
				userLoginDetail.setLogOnFlag("Y");
				
				entityManager.merge(userLoginDetail);
				entityManager.flush();
		}else{
			
		UserLoginDetails userLoginDetails = new UserLoginDetails();
		userLoginDetails.setLoginId(userId);
		MasUser employeeName = getUserName(userId);
		if(employeeName != null){
			userLoginDetails.setLoginName(employeeName.getUserName());
		}
		userLoginDetails.setCreatedBY(userId);
		userLoginDetails.setLoginDateTime(new Date());
		userLoginDetails.setCurrentDate(new Date());
		userLoginDetails.setCreatedDate(new Date());
		userLoginDetails.setLogOnFlag("Y");
		userLoginDetails.setActiveStatus(1l);
		
		entityManager.persist(userLoginDetails);
		entityManager.flush();
		}
		}catch(Exception e){
			
		}
	}
	
	public void updateLoginDetails(String userId){
		try{
		UserLoginDetails userLoginDetail = getUserLoginDetail(userId);
		if(userLoginDetail != null){
			userLoginDetail.setLogoutDateTime(new Date());
			userLoginDetail.setModifiedBy(userId);
			userLoginDetail.setModifiedDate(new Date());
			userLoginDetail.setCurrentDate(new Date());
			userLoginDetail.setLogOnFlag("N");
			//userLoginDetail.setActiveStatus(0l); Removed for status update
			entityManager.merge(userLoginDetail);
			entityManager.flush();
		}
		}catch(Exception e){
			
		}
		
		
	}
	
	public UserLoginDetails getUserLoginDetail(String userId){
		
		Query findByTransactionKey = entityManager.createNamedQuery(
				"UserLoginDetails.findByUserId").setParameter("loginId", userId.toString());
		try{
			List<UserLoginDetails> userLoginDetails =(List<UserLoginDetails>) findByTransactionKey.getResultList();
			
			if(userLoginDetails != null && ! userLoginDetails.isEmpty()){
				return userLoginDetails.get(0);
			}
			return null;
		}
		catch(Exception e)
		{
			return null;
		}
		
		
	}
	
	public UserLoginDetails getUserLoginDetailBasedOnKey(String userId) {

		Query findByTransactionKey = entityManager.createNamedQuery(
				"UserLoginDetails.findKey").setParameter("loginId",
				userId.toString());
		try {
			List<UserLoginDetails> userLoginDetails = (List<UserLoginDetails>) findByTransactionKey
					.getResultList();

			if (userLoginDetails != null && !userLoginDetails.isEmpty()) {
				return userLoginDetails.get(0);
			}
			return null;
		} catch (Exception e) {
			return null;
		}

	}
	
	  public TmpEmployee getEmployeeName(String initiatorId)
		{
		  List<TmpEmployee> tmpEmployeeDetails;
			Query findByTransactionKey = entityManager.createNamedQuery(
					"TmpEmployee.getEmpByLoginId").setParameter("loginId", initiatorId.toLowerCase());
			try{
				
				tmpEmployeeDetails =(List<TmpEmployee>) findByTransactionKey.getResultList();
				if(tmpEmployeeDetails != null && ! tmpEmployeeDetails.isEmpty()){
					return tmpEmployeeDetails.get(0);
				}
				return null;
			}
			catch(Exception e)
			{
				return null;
			}
								
		}
	  
	  public MasUser getUserName(String initiatorId)
		{
		  List<MasUser> tmpEmployeeDetails;
			Query findByTransactionKey = entityManager.createNamedQuery(
					"MasUser.getById").setParameter("userId", initiatorId.toLowerCase());
			try{
				tmpEmployeeDetails =(List<MasUser>) findByTransactionKey.getResultList();
				if(tmpEmployeeDetails != null && ! tmpEmployeeDetails.isEmpty()){
					return tmpEmployeeDetails.get(0);
				}
				return null;
				
			}
			catch(Exception e)
			{
				return null;
			}								
		}
	  
//	private void saveCoveringLetterDocDetails(ClaimDto claimDto, Claim claimObj){
//	
//		List<DocumentCheckListDTO> docChekcList = claimDto.getDocumentCheckListDTO();
//		
//		if(docChekcList != null && !docChekcList.isEmpty() ){
//			entityManager.refresh(claimObj);
//			for (DocumentCheckListDTO documentCheckListDTO : docChekcList) {
//		
//				CoveringLetterDocDetails coveringLetterObj = new CoveringLetterDocDetails();
//				coveringLetterObj.setClaim(claimObj);
//				coveringLetterObj.setCreatedDate(new Date());
//				coveringLetterObj.setProcessType(ReferenceTable.PA_LOB_KEY.equals(claimDto.getNewIntimationDto().getPolicy().getLobId()) ? SHAConstants.PA_LOB : SHAConstants.HEALTH_LOB);
//				coveringLetterObj.setDocTypeId(documentCheckListDTO.getParticulars() != null ? documentCheckListDTO.getParticulars().getId() : null);
//				entityManager.persist(coveringLetterObj);
//			}			
//		}
//	}
	
/*public void initiateBPMNforReimbursement(Claim claim){
		
//		Intimation intimation = getIntimationByKey(intimationObj.getKey());
//		log.info("**************WHILE PROCESSING SAVED INTIMATION ***********-----> "
//				+ intimation != null ? intimation.getIntimationId()
//				: (intimation != null ? intimation.getIntimationId()
//						+ "--->THIS INTIMATION NOT YET SAVED IN OUR DB.. SO IT LEADS TO PROBLEM FOR US"
//						: "NO INTIMATIONS"));
		IntimationRule intimationRule = new IntimationRule();
		IntimationType a_message = new IntimationType();
		ClaimRequestType claimReqType = new ClaimRequestType();
		ClaimType claimType = new ClaimType();

		
		String strClaimType = "";
//		if (null != claim.getIntimation().getHospitalType()
//				&& null != claim.getIntimation().getHospitalType().getKey()
//				&& claim.getIntimation()
//						.getHospitalType()
//						.getKey()
//						.equals(ReferenceTable.PREMIA_INTIMTION_PROCESS_NETWORK_HOSPITAL)) {
//			strClaimType = SHAConstants.CASHLESS_CLAIM_TYPE;
//		} else if (null != claim.getIntimation().getHospitalType()
//				&& null != claim.getIntimation().getHospitalType().getKey()
//				&& claim.getIntimation()
//						.getHospitalType()
//						.getKey()
//						.equals(ReferenceTable.PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL)) {
//			strClaimType = SHAConstants.REIMBURSEMENT_CLAIM_TYPE;
//		}
		
		claimType.setClaimType(claim.getClaimType().getValue().toUpperCase());
		
//		claimType.setClaimType(strClaimType);
		if (null != claim.getIntimation().getCpuCode()
				&& null != claim.getIntimation().getCpuCode().getCpuCode())
			claimReqType.setCpuCode(String.valueOf(claim.getIntimation().getCpuCode()
					.getCpuCode()));
		a_message.setKey(claim.getIntimation().getKey());
		if (null != claim.getIntimation().getAdmissionDate()) {
			String date = String.valueOf(claim.getIntimation().getAdmissionDate());
			String date1 = date.replaceAll("-", "/");
			a_message.setIntDate(SHAUtils.formatIntimationDate(date1));
			a_message.setIntDate(new Timestamp(System.currentTimeMillis()));
			Timestamp timestamp = new Timestamp(
					System.currentTimeMillis() + 60 * 60 * 1000);
			a_message.setIntDate(timestamp);
		}
		a_message.setIntimationNumber(claim.getIntimation().getIntimationId());
		a_message
				.setIntimationSource(claim.getIntimation().getIntimationSource() != null ? (claim.getIntimation()
						.getIntimationSource().getValue() != null ? claim.getIntimation()
						.getIntimationSource().getValue() : "")
						: "");
		a_message.setIsClaimPending(!intimationRule
				.isClaimExist(claim.getIntimation()));
		a_message
				.setIsPolicyValid(intimationRule.isPolicyValid(claim.getIntimation()));
		
		a_message.setIsBalanceSIAvailable(true);

		//com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.paymentinfo.PaymentInfoType paymentInfoType = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.paymentinfo.PaymentInfoType();

		
		claimReqType
		.setOption(claim.getStage() != null ? claim
				.getStage().getStageName() : " ");
		paymentInfoType
		.setClaimedAmount(claim.getClaimedAmount());
			claimType.setClaimId(claim.getClaimId());
					claimType.setKey(claim.getKey());

		ClassificationType classificationType = new ClassificationType();

		Insured insured = claim.getIntimation().getInsured();

		if (null != claim && null != claim.getIsVipCustomer()
				&& claim.getIsVipCustomer().equals(1l)) {

			classificationType.setPriority(SHAConstants.VIP_CUSTOMER);
		} else if (null != insured && null != insured.getInsuredAge()
				&& insured.getInsuredAge() > 60) {
			classificationType.setPriority(SHAConstants.SENIOR_CITIZEN);
		} else {
			classificationType.setPriority(SHAConstants.NORMAL);
		}

		classificationType.setType(SHAConstants.TYPE_FRESH);
		classificationType.setSource(SHAConstants.NORMAL);

		PreAuthReqType preauthReqType = new PreAuthReqType();
		preauthReqType.setOutcome("PREAUTHNOTRECEIVED");
		preauthReqType.setResult("PREAUTHNOTRECEIVED");
		preauthReqType.setPreAuthAmt(0d);

		// setting senior citizen hardcoded as of now. Later this needs to
		// be changed.

		if (null != claim.getIntimation().getPolicy()
				&& null != claim.getIntimation().getPolicy().getProduct()
				&& null != claim.getIntimation().getPolicy().getProduct().getKey()
				&& claim.getIntimation().getPolicy().getProduct().getKey().equals(30l))
			preauthReqType.setIsSrCitizen(true);
		else
			preauthReqType.setIsSrCitizen(false);

	
		HospitalInfoType hospitalInfoType = new HospitalInfoType();
		hospitalInfoType.setHospitalType(intimationRule
				.getHospitalTypeForPremia(claim.getIntimation()));

		
		 * if(intimation.getHospitalType() != null) {
		 
		
		Hospitals hospital = null;
		
		
		if (null != claim.getIntimation().getHospitalType().getKey()) {
			if (claim.getIntimation().getHospitalType().getKey()
					.equals(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID)) {
				hospital = getHospitalById(claim.getIntimation().getKey());
				if (hospital != null) {
					if (hospital.getNetworkHospitalTypeId() != null) {
						MastersValue networkHospitalType = getMaster(hospital
								.getNetworkHospitalTypeId());
						if (networkHospitalType != null) {
							hospitalInfoType
									.setNetworkHospitalType(networkHospitalType
											.getValue() != null ? networkHospitalType
											.getValue() : "");
						}
					}
				}
			}
		}
		
		if(hospital == null){
			hospital = getHospitalById(claim.getIntimation().getKey());
		}
		
		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.productinfo.ProductInfoType productInfoType = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.productinfo.ProductInfoType();
		productInfoType.setLob(SHAConstants.PA_LOB);
		productInfoType.setLobType(SHAConstants.PA_LOB_TYPE);

		// }
		PolicyType policyType = new PolicyType();
		policyType.setPolicyId(claim.getIntimation().getPolicy().getPolicyNumber());
		String userId = BPMClientContext.BPMN_TASK_USER;
		String password = BPMClientContext.BPMN_PASSWORD;
		PayloadBOType payloadBO = new PayloadBOType();
		payloadBO.setIntimation(a_message);
		payloadBO.setClaim(claimType);
		payloadBO.setClaimRequest(claimReqType);
		payloadBO.setHospitalInfo(hospitalInfoType);
		payloadBO.setPolicy(policyType);
		payloadBO.setPreAuthReq(preauthReqType);;
		payloadBO.setPaymentInfo(paymentInfoType);
		payloadBO.setClassification(classificationType);
		payloadBO.setProductInfo(productInfoType);
		
		IntMsg initiateInitmationTask = BPMClientContext
				.getIntimationIntiationTask(
						BPMClientContext.BPMN_TASK_USER,
						BPMClientContext.BPMN_PASSWORD);

		try {
			initiateInitmationTask.initiate(BPMClientContext.BPMN_TASK_USER,
					payloadBO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}*/
	
	
	public void updateUnNamedInsuredDetails(ClaimDto claimDto)
	{
		if(null != claimDto && null != claimDto.getNewIntimationDto() && null != claimDto.getNewIntimationDto().getPolicy() &&
				null != claimDto.getNewIntimationDto().getPolicy().getProduct() && null != claimDto.getNewIntimationDto().getPolicy().getProduct().getKey() &&
				(ReferenceTable.getGPAProducts().containsKey(claimDto.getNewIntimationDto().getPolicy().getProduct().getKey())) &&
				(SHAConstants.GPA_UN_NAMED_POLICY_TYPE.equalsIgnoreCase(claimDto.getNewIntimationDto().getPolicy().getGpaPolicyType()))){
			
			
			if(null != claimDto.getNewIntimationDto().getKey()){
				
				Intimation intimationObj = getIntimationByKey(claimDto.getNewIntimationDto().getKey());
				if(null != intimationObj){
					
					intimationObj.setPaParentName(claimDto.getNewIntimationDto().getPaParentName());
					intimationObj.setPaParentDOB(claimDto.getNewIntimationDto().getParentDOB());
					intimationObj.setPaPatientName(claimDto.getNewIntimationDto().getPaPatientName());
					intimationObj.setPaParentAge(claimDto.getNewIntimationDto().getParentAge());
					
					if(null != intimationObj.getKey()){
					
						entityManager.merge(intimationObj);
						entityManager.flush();
					}else{
						entityManager.persist(intimationObj);
						entityManager.flush();
					}					
					
				}
				
				
			}
		}
	}
	  public void updateClaimProvisionAmount(Claim claim){
		  claim.setCurrentProvisionAmount(0d);
		  entityManager.merge(claim);
		  entityManager.flush();
	  }	  
	  
	  
	  @SuppressWarnings("unchecked")
		public List<Preauth> getPreauthByClaimKey(Long claimKey) {
			Query query = entityManager
					.createNamedQuery("Preauth.findByClaimKey");
			query.setParameter("claimkey", claimKey);
			List<Preauth> singleResult = (List<Preauth>) query.getResultList();
			return singleResult;
		}

	
	public void updateUnnamedRiskCategory(UnnamedRiskDetailsPageDTO unnamedRiskPageDTO){
		
		Claim claimObj = null;
		if(null != unnamedRiskPageDTO.getIntimationNo()){
			 claimObj = getClaimsByIntimationNumber(unnamedRiskPageDTO.getIntimationNo());
		}
		
		if(null != claimObj){
			
			String categoryWithDescription = unnamedRiskPageDTO.getGpaCategory();
			String[] categoryWithoutDescription = categoryWithDescription.split("-");
			String category = categoryWithoutDescription[0];
			claimObj.setGpaCategory(category);
			
			if(null != claimObj.getKey()){
				
				entityManager.merge(claimObj);
				entityManager.flush();
			}
		}
	}
	
	
	
	
	public List<PaymentModeTrail> getPaymentModeTrailByRodKey(Long reimbursementKey) {
		Query query = entityManager.createNamedQuery(
				"PaymentModeTrail.findByreimbursementKey").setParameter("reimbursementKey", reimbursementKey);
		List<PaymentModeTrail> paymentModeTrail = query.getResultList();
		if (paymentModeTrail != null && !paymentModeTrail.isEmpty()) {
			return paymentModeTrail;
		}
		return null;
	}
	

	public Claim getByClaimNum(String claimNumber) {
		Query findByClaimNumber = entityManager.createNamedQuery(
				"Claim.findByClaimNumber").setParameter("claimNumber",
				claimNumber);

		List<Claim> claimList = (List<Claim>) findByClaimNumber.getResultList();
		if(null != claimList && !claimList.isEmpty()){
			Claim claimObj = claimList.get(0);
			return claimObj;
		}
		return null;

	}
	
	public Reimbursement getReimbursementByRodNo(String rodNumber) {
		Query findByClaimNumber = entityManager.createNamedQuery(
				"Reimbursement.findRodByNumber").setParameter("rodNumber",
						rodNumber);

		List<Reimbursement> reimbList = (List<Reimbursement>) findByClaimNumber.getResultList();
		if(null != reimbList && !reimbList.isEmpty()){
			Reimbursement reimbObj = reimbList.get(0);
			return reimbObj;
		}
		return null;

	}
	
	public void saveNomineeDetails(){
		
		NomineeDetails nomineeDetails = new NomineeDetails();
		nomineeDetails.setNomineeName("VANI");
		MastersValue relationShip = new MastersValue();
		relationShip.setKey(4202l);
		relationShip.setValue("Spouse");
		nomineeDetails.setRelationId(relationShip);
		nomineeDetails.setNomineeAge(34l);
		nomineeDetails.setNomineeSharePercent(100l);
		
		Insured clsInsured = getCLSInsured(151524010l);
		nomineeDetails.setInsured(clsInsured);
		entityManager.persist(nomineeDetails);
		entityManager.flush();
		
		NomineeDetails nomineeDetails1 = new NomineeDetails();
		nomineeDetails1.setNomineeName("VANI");
		MastersValue relationShip1 = new MastersValue();
		relationShip1.setKey(4202l);
		relationShip1.setValue("Spouse");
		nomineeDetails1.setRelationId(relationShip1);
		nomineeDetails1.setNomineeAge(34l);
		nomineeDetails1.setNomineeSharePercent(100l);
		
		Insured clsInsured1 = getCLSInsured(151524011l);
		nomineeDetails1.setInsured(clsInsured1);
		entityManager.persist(nomineeDetails1);
		entityManager.flush();
		
		
	}
	
	public Insured getCLSInsured(Long key) {

		Query query = entityManager.createNamedQuery("Insured.findByInsured");
		query = query.setParameter("key", key);
		if (query.getResultList().size() != 0)
			return (Insured) query.getSingleResult();
		return null;

	}
	
	public CashlessWorkFlow getWorkFlowValues(Long wfKey){
		 Query wkQuery = entityManager.createNamedQuery("CashlessWorkFlow.findByKey");
		    wkQuery.setParameter("wk_key", wfKey);
		    List<CashlessWorkFlow> wk_list = (List<CashlessWorkFlow>)wkQuery.getResultList();
		    if(wk_list != null && ! wk_list.isEmpty()){
		    	return wk_list.get(0);
		    }
		    return null;
	}
	
	public void updateCpuCode(Long wfKey,String cpuCode){
		CashlessWorkFlow workFlowValues = getWorkFlowValues(wfKey);
		workFlowValues.setCpuCode(Long.valueOf(cpuCode));
		entityManager.merge(workFlowValues);
		entityManager.flush();
	}
	
	public void updateCurrentQ(Long wfKey,String currentQ){
		CashlessWorkFlow workFlowValues = getWorkFlowValues(wfKey);
		workFlowValues.setCurrentQ(currentQ);
		entityManager.merge(workFlowValues);
		entityManager.flush();
	}
	
	public void updateActiveFlag(Long wfKey,String activeFlag){
		CashlessWorkFlow workFlowValues = getWorkFlowValues(wfKey);
		workFlowValues.setActiveFlag(activeFlag);
		entityManager.merge(workFlowValues);
		entityManager.flush();
	}
	
	public void updateQueryDetails(String strQuery){
		Query nativeQuery = entityManager.createNativeQuery(strQuery);
		nativeQuery.executeUpdate();
	}
	
	
	public List<ZUAQueryHistoryTable> zuaQueryHistoryDetails(String policyNo){
//		entityManager.merge(claim);
//		entityManager.flush();
		
		String query = "SELECT * FROM  ZUC_QUERY_DETAILS  WHERE ZUQ_POLICY_NO =" + policyNo;
		
		Query nativeQuery = entityManager.createNativeQuery(query);
		List<ZUAQueryHistoryTable> resultList = (List<ZUAQueryHistoryTable>)nativeQuery.getResultList();
		
		return resultList;
		 

	}

	public Claim updateDoctorInternalRemarks(ClaimDto claimDto) {
		if (claimDto != null && claimDto.getKey() != null ){
				
			System.out.println("----claim key in update / merge---"+claimDto.getKey());
			Claim createdClaim = getClaimByKey(claimDto.getKey());
				
			if(createdClaim != null){
				//entityManager.refresh(createdClaim);
				createdClaim.setInternalNotes(claimDto.getDoctorNote());
				createdClaim.setModifiedDate(new Date());
				createdClaim.setModifiedBy(claimDto.getModifiedBy());
				entityManager.merge(createdClaim);
				entityManager.flush();
				//entityManager.refresh(createdClaim);
			return createdClaim;
			}
		}
		return null;
			
	}
	
	public void insertEmployeeDetails(TmpEmployee tmpEmployee) {
		System.out.println("insert = " + tmpEmployee.toString());
		entityManager.persist(tmpEmployee);
		entityManager.flush();
	}
	
	public void updateEmployeeDetails(TmpEmployee tmpEmployee) {
		System.out.println("update = " + tmpEmployee.toString());
		entityManager.merge(tmpEmployee);
		entityManager.flush();
	}
	
	public ReportLog popinReportLog(String userId, String reportName,Date exeDate,Date txnDate,String statusFlag){
		
		ReportLog reportLogObj = new ReportLog();
		try{
			reportLogObj.setReportName(reportName);
			reportLogObj.setCurrentDate(exeDate);
			reportLogObj.setReportTime(txnDate);
			reportLogObj.setStatusFlag(statusFlag);
			reportLogObj.setUserId(userId);
			reportLogObj.setAciveStatusFlag("Y");
			entityManager.persist(reportLogObj);
			entityManager.flush();
		}
		catch(Exception e){
			e.printStackTrace();	
		}
		
		return reportLogObj;
		
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ClaimDto submitPAClaim(SearchClaimRegistrationTableDto resultTask, ClaimDto claimDto) {
		claimDto.setCreatedBy(resultTask.getUsername() != null ? SHAUtils.getUserNameForDB(resultTask.getUsername()) : "");
		
		// R1053  -  GHI Allow Reg.  12-04-2018
		claimDto.setGhiAllowFlag(SHAConstants.YES_FLAG);
		claimDto.setGhiAllowUser(claimDto.getCreatedBy());
		
		
		Claim claim =  ClaimMapper.getInstance().getClaim(claimDto);
		System.out.println("----the intimation key for created claim ----"+claim.getIntimation().getKey());
		Claim createdClaim = getClaimforIntimation(claim.getIntimation().getKey());
		if(createdClaim != null && createdClaim.getIntimation().getKey() == claimDto.getNewIntimationDto().getKey()){
			claim.setKey(createdClaim.getKey());
		}
		
		/******
		 * As per Prakash, We have set the Date of admission from Intimation to
		 * Claim while creating the Claim .....
		 **************/
		claim.setDataOfAdmission(claimDto.getNewIntimationDto().getAdmissionDate());
		

		Policy policyByKey = getPolicyByKey(claimDto.getNewIntimationDto().getPolicy().getKey());
		
//		Double amt =  claimAdvProvision.getAvgAmt();
		
		NewIntimationDto newIntimationDto = claimDto.getNewIntimationDto();

		claim.setNormalClaimFlag("O");
		
		if(claimDto.getNewIntimationDto().getLobId() != null){
		
			claim.setLobId(claimDto.getNewIntimationDto().getLobId().getId());
		}	
		
		create(claim);
//		entityManager.refresh(claim);
		claim = entityManager.find(Claim.class, claim.getKey());
		
		ClaimDto responseClaim = new ClaimMapper().getClaimDto(claim);
		responseClaim.setNewIntimationDto(claimDto.getNewIntimationDto());
		if(null != responseClaim.getKey()){
			

			String outCome = "";
			
			if(claimDto.getStatusId().equals(ReferenceTable.CLAIM_SUGGEST_REJECTION_STATUS)){
				outCome = SHAConstants.OUTCOME_FOR_MANUAL_SUGGEST_REJECTION;
			}else{
				if(claim != null && claim.getClaimType() != null && claim.getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)){
					outCome = SHAConstants.AUTO_REGISTRATION_OUTCOME_FOR_REIMBURSEMENT;
				}else{
					outCome = SHAConstants.AUTO_REGISTRATION_OUTCOME;
				}
			}
			
//			setBPMOutcome(resultTask, responseClaim);	
			if((ReferenceTable.HEALTH_LOB_KEY).equals(responseClaim.getNewIntimationDto().getPolicy().getLobId())){
				submitDBProcedureForRegistration(resultTask, claim, claimDto, outCome);
			}	
			
			
			if(claim != null && claim.getClaimType() != null && claim.getClaimType().getKey() != null && (ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY).equals(claim.getClaimType().getKey())){

			if((ReferenceTable.HEALTH_LOB_KEY).equals(responseClaim.getNewIntimationDto().getPolicy().getLobId())){
//				setBPMOutcome(resultTask, responseClaim);	
			}	
			
			
			/**
			 * From the below condition health lob type check has
			 * been removed for initiating FVR task.
			 * 
			 *  Since rrc type is required for FVR task , during manual 
			 *  registration , cashless payload is of no use. Hence reusing
			 *  the health code.
			 * */
			
			
			
			//if(claim != null &&	claim.getClaimType() != null && claim.getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) && claim.getIntimation().getPolicy().getLobId().equals(ReferenceTable.HEALTH_LOB_KEY)){
			if((ReferenceTable.HEALTH_LOB_KEY).equals(responseClaim.getNewIntimationDto().getPolicy().getLobId())){
			if(/*(claim != null &&	claim.getClaimType() != null && claim.getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)) &&*/ 
					(null == claim.getIncidenceFlag() || SHAConstants.ACCIDENT_FLAG.equalsIgnoreCase(claim.getIncidenceFlag()))){

				if((claim != null &&	claim.getClaimType() != null && claim.getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)) && claimDto.getStatusId() != null && claimDto.getStatusId().equals(ReferenceTable.INTIMATION_REGISTERED_STATUS)){
					/*****
					 * **
					 * BPM to DB Migration done hence below code was commented. this task will get submitted in the above DBprocedure call for submit
					 */
//					initiateBPMNforReimbursement(claim);
//					autoRegisterFVR(claim.getIntimation(),resultTask.getUserId());
					
					Map<String,Object> wrkflowMap = (Map<String,Object>)resultTask.getDbOutArray();
					wrkflowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_INITIATE_REIMINDER_PROCESS);
					initiateReminderLetterProcess(wrkflowMap);					
				}
			}
				
			
			}
			
		}
			//IMSSUPPOR-24475
			//autoRegisterFVR(claim.getIntimation(),resultTask);
		claimDto.setClaimId(claim.getClaimId());
		String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
		if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
			try {
				Hospitals hospitalDetailsByKey = getHospitalDetailsByKey(claim.getIntimation().getHospital());
//				String provisionAmtInput = SHAUtils.getProvisionAmtInput(claim, hospitalDetailsByKey.getName(), String.valueOf(claim.getCurrentProvisionAmount().longValue()));
//				PremiaService.getInstance().updateProvisionAmount(provisionAmtInput);
				PremiaService.getInstance().getPolicyLock(claim, hospitalDetailsByKey.getHospitalCode());
				updateProvisionAmountToPremia(claim);

				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		}
		return responseClaim;
	}
	
	public void updateRawDetails(List<EsclateToRawTableDTO> values,PreauthDTO bean){
		if(values != null && ! values.isEmpty()){
			RawInvsHeaderDetails  headerDetail= getRawHeaderByIntimation(bean.getNewIntimationDTO().getIntimationId());
			if(null == headerDetail){
				headerDetail = new RawInvsHeaderDetails();
				headerDetail.setIntimationNo(bean.getNewIntimationDTO().getIntimationId());
				headerDetail.setPolicyNumber(bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
				headerDetail.setClaimType(bean.getClaimDTO().getClaimTypeValue());
				headerDetail.setCpuCode(bean.getNewIntimationDTO().getCpuCode());
				headerDetail.setCreatedBy(bean.getStrUserName());
				headerDetail.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				headerDetail.setHospitalCode(null != bean.getNewIntimationDTO().getHospitalDto()?bean.getNewIntimationDTO().getHospitalDto().getHospitalCode():null);
				entityManager.persist(headerDetail);
				entityManager.flush();
			}
			else{
				headerDetail.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				headerDetail.setModifyby(bean.getStrUserName());
				entityManager.merge(headerDetail);
				entityManager.flush();
			}
			for (EsclateToRawTableDTO esclateToRawTableDTO : values) {
				if(null == esclateToRawTableDTO.getRecordType()){
					RawInvsDetails rawDetails = new RawInvsDetails();
					rawDetails.setRawInvstigation(headerDetail);
					rawDetails.setRequestedBy(bean.getStrUserName());
					rawDetails.setRequestedDate(new Timestamp(System.currentTimeMillis()));
					RawCategory rawCategory = new RawCategory();
					rawCategory.setKey(null != esclateToRawTableDTO.getEsclateCategory() ? esclateToRawTableDTO.getEsclateCategory().getId():null);
					rawCategory.setCategoryDescription(null != esclateToRawTableDTO.getEsclateCategory() ? esclateToRawTableDTO.getEsclateCategory().getValue():null);
					rawDetails.setRawCategory(rawCategory);
					RawSubCategory rawSubCategory = new RawSubCategory();
					rawSubCategory.setKey(null != esclateToRawTableDTO.getEsclateSubCategory() ? esclateToRawTableDTO.getEsclateSubCategory().getId():null);
					rawSubCategory.setSubCategoryDescription(null != esclateToRawTableDTO.getEsclateSubCategory() ? esclateToRawTableDTO.getEsclateSubCategory().getValue():null);
					if(null != rawSubCategory.getKey()){
						rawDetails.setRawSubCategory(rawSubCategory);
					}
					rawDetails.setRequestedRemarks(esclateToRawTableDTO.getEsclateToRawRemarks());
					rawDetails.setRequestedStage(bean.getEsclateStageKey());
					rawDetails.setRequestedStatus(ReferenceTable.ESCLATE_TO_RAW);
					rawDetails.setCreatedBy(bean.getStrUserName());
					rawDetails.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					rawDetails.setKey(esclateToRawTableDTO.getKey());
					rawDetails.setRecordType(esclateToRawTableDTO.getRecordType());
					if(null != rawDetails.getKey()){
						entityManager.merge(rawDetails);
						entityManager.flush();
					}
					else
					{
						entityManager.persist(rawDetails);
						entityManager.flush();
						esclateToRawTableDTO.setKey(rawDetails.getKey());
					}
								
				}
		}
			if(null != bean.getDeletedEsclateRawList() && !bean.getDeletedEsclateRawList().isEmpty()){
				List<EsclateToRawTableDTO> deletedList = bean.getDeletedEsclateRawList();
				for (EsclateToRawTableDTO esclateToRaw : deletedList) {
					if(null != esclateToRaw.getKey()){
						updateDeletedRecordsOfRawClaim(esclateToRaw.getKey());
					}
				}
			}
	}
}
	

		public List<RawInvsHeaderDetails> getRawInvestDetailsByIntimation(String intimation) {
			List<RawInvsHeaderDetails> rawdtls = null;
			Query findByIntNo = entityManager
					.createNamedQuery("RawInvsHeaderDetails.findByIntimationNo");
			findByIntNo = findByIntNo.setParameter(
					"intimationNo", intimation);
		 	List<RawInvsHeaderDetails> rawList = findByIntNo.getResultList();
	    	return rawList;
		}
		
		public List<RawInvsDetails> getRawInvestDetails(Long invKey) {
			RawInvsDetails rawdtls = null;
			Query findByInvKey = entityManager
					.createNamedQuery("RawInvsDetails.findByRawInvKey");
			findByInvKey = findByInvKey.setParameter(
					"rawInvkey", invKey);
			List<Long> statusList = new ArrayList<Long>();
			statusList.add(ReferenceTable.ESCLATE_TO_RAW);
			findByInvKey.setParameter("statusList", statusList);
		 	List<RawInvsDetails> rawInvList = findByInvKey.getResultList();
		 	
		 	return rawInvList;
		}
		
	public RawInvsHeaderDetails getRawHeaderByIntimation(String intimationNo){
		
		Query query = entityManager.createNamedQuery("RawInvsHeaderDetails.findByIntimationNo");
		query.setParameter("intimationNo", intimationNo);
		List<RawInvsHeaderDetails> rawHeaderList = (List<RawInvsHeaderDetails>)query.getResultList();
		  if(rawHeaderList != null && ! rawHeaderList.isEmpty()){
		    	return rawHeaderList.get(0);
		   }
		return null;
	}
	
	public List<RawInvsDetails> getRawDetailsByRawInvKey(Long rawInvkey){
		
		Query query = entityManager.createNamedQuery("RawInvsDetails.findByRawInvKey");
		List<Long> statusList = new ArrayList<Long>();
		statusList.add(ReferenceTable.ESCLATE_TO_RAW);
		query.setParameter("rawInvkey", rawInvkey);
		query.setParameter("statusList", statusList);
		List<RawInvsDetails> esclateList = (List<RawInvsDetails>)query.getResultList();
		return esclateList;
	}
	
	public List<RawInvsDetails> getRawDetailsByRecordType(Long rawInvkey){
		
		Query query = entityManager.createNamedQuery("RawInvsDetails.findByRecordType");
		List<Long> statusList = new ArrayList<Long>();
		statusList.add(ReferenceTable.ESCLATE_TO_RAW);
		query.setParameter("rawInvkey", rawInvkey);
		List<RawInvsDetails> esclateList = (List<RawInvsDetails>)query.getResultList();
		return esclateList;
	}
	public void updateDeletedRecordsOfRawClaim(Long rawInsDtlsKey){
		try{
		String query = "UPDATE IMS_CLS_RAW_INV_DTLS SET DELETED_FLAG = "+"'"+SHAConstants.YES_FLAG+"'"+" WHERE RAW_INV_DTLS_KEY = "+rawInsDtlsKey;
		Query updateScript = entityManager.createNativeQuery(query);
		updateScript.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public List<RawInvsDetails> getRepliedRawData(Long rawInvkey){
		Query query = entityManager.createNamedQuery("RawInvsDetails.findByRawInvKey");
		List<Long> statusList = new ArrayList<Long>();
		statusList.add(ReferenceTable.RAW_TEAM_REPLIED);
		query.setParameter("rawInvkey", rawInvkey);
		query.setParameter("statusList", statusList);
		List<RawInvsDetails> esclateList = (List<RawInvsDetails>)query.getResultList();
		return esclateList;
	}
	public List<RawInvsDetails> getRawDetails(Long rawInvkey){
		Query query = entityManager.createNamedQuery("RawInvsDetails.findAllByRawInvKey");
		query.setParameter("rawInvkey", rawInvkey);
		List<RawInvsDetails> esclateList = (List<RawInvsDetails>)query.getResultList();
		return esclateList;
	}
	
	public String getTopUpPolicyDetails(String policyNumber,PreauthDTO preauthDto){
		   
		   DBCalculationService dbCalculationService = new DBCalculationService();
		   Map<String, Object> getTopUpPolicy = dbCalculationService.getTopUpPolicyDetails(policyNumber);
			String alertFlag = "";
			if(getTopUpPolicy != null){
				if(getTopUpPolicy.containsKey("alertFlag")){
					alertFlag = String.valueOf(getTopUpPolicy.get("alertFlag"));
					if(null != alertFlag && SHAConstants.YES_FLAG.equalsIgnoreCase(alertFlag)){
						String alertMsg = String.valueOf(getTopUpPolicy.get("alertMsg"));
						preauthDto.setTopUpPolicyAlertMessage(alertMsg);
				}
			}
	  }
			return alertFlag;
	}
	
	//IMSSUPPOR-27314
	public void updateMasUserDetails(MasUser masUser) {
		System.out.println("update = " + masUser.toString());
		entityManager.merge(masUser);
		entityManager.flush();
	}
	
	public MasUser getUserByUserName(String initiatorId)
	{
	  List<MasUser> tmpEmployeeDetails;
		Query findByTransactionKey = entityManager.createNamedQuery(
				"MasUser.getByIdWithUserName").setParameter("userId", initiatorId.toLowerCase());
		try{
			tmpEmployeeDetails =(List<MasUser>) findByTransactionKey.getResultList();
			if(tmpEmployeeDetails != null && ! tmpEmployeeDetails.isEmpty()){
				return tmpEmployeeDetails.get(0);
			}
			return null;
			
		}
		catch(Exception e)
		{
			return null;
		}								
	}

	public void submitConvertToReimbursementPostProcess(ConvertClaimDTO convertClaim,
			String option) {

		Long key = convertClaim.getKey();

		Claim claimData = ConvertClaimMapper.getInstance().getClaim(convertClaim);
		
		//convertClaim.setIntimationNumber(claimData.getIntimation().getIntimationId());

		Claim find = entityManager.find(Claim.class, key);
		entityManager.refresh(find);

		if (find != null) {
			find.setConversionReason(claimData.getConversionReason());
			if (option.equals("submit")) {
				entityManager.merge(find);
				entityManager.flush();

			} else {
				MastersValue claimTypeId = new MastersValue();
				claimTypeId.setKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
				find.setClaimType(claimTypeId);
				find.setConversionFlag(1l);
				/*find.setClaimSectionCode(null);
				find.setClaimCoverCode(null);
				find.setClaimSubCoverCode(null);*/
				find.setConversionDate(new Timestamp(System.currentTimeMillis()));
				find.setPaHospExpenseAmt(0d);
				entityManager.merge(find);
				entityManager.flush();
			////GLX2020075 commented for this GMC CPU CR since CPU Routing for GMC already done on above code -noufel
//				if(! (ReferenceTable.getGMCProductList().containsKey(find.getIntimation().getPolicy().getProduct().getKey()))){
					updateCpuCodeForIntimation(find.getIntimation());
//				}
			}
		}
	}
	
	//CR2019023
	public List<RawInvsDetails> getRawDetailsWithoutFilter(Long rawInvkey){
		
		Query query = entityManager.createNamedQuery("RawInvsDetails.findAllByRawInvKeyWithoutFilter");
		query.setParameter("rawInvkey", rawInvkey);
		List<RawInvsDetails> esclateList = (List<RawInvsDetails>)query.getResultList();
		return esclateList;

		/*RawInvsDetails rawDetails = null;
		Connection connection = null;
		try {
			connection = BPMClientContext.getConnection();
			if(null != connection) {

				String fetchQuery ="SELECT RAW_INV_DTLS_KEY,RAW_INV_KEY,REQUESTED_REMARKS,REQUESTED_BY,REQUESTED_DATE,REQUEST_STAGE_ID,REQUEST_STATUS_ID, " +
			"case when REQUEST_STATUS_ID = 271  then MODIFIED_BY else REPLIED_BY end  as \"REPLIED_BY\" , " +
			"case when REQUEST_STATUS_ID = 271  then MODIFIED_DATE else REPLIED_DATE end  \"REPLIED_DATE\", " +
			"RESOLUTION_TYPE,REPLIED_REMARKS, CREATED_DATE,CREATED_BY,MODIFIED_DATE,MODIFIED_BY,DELETED_FLAG, " +
			"RECORD_TYPE,CATEGORY_ID,SUB_CATEGORY_ID " +
			"from IMS_CLS_RAW_INV_DTLS " +
			"where RAW_INV_KEY = 100232 order by CREATED_DATE desc ";

				PreparedStatement preparedStatement = connection.prepareStatement(fetchQuery);

				if(null != preparedStatement) {
					ResultSet rs = preparedStatement.executeQuery();
					if(null != rs) {
						while (rs.next()) {
							rawDetails = new RawInvsDetails();
							magazineInfo.setMasMagKey(rs.getLong(1));
							magazineInfo.setMasDocKey(rs.getLong(2));
							magazineInfo.setMasMagCode(rs.getString(3));
							magazineInfo.setMasMagCategory(rs.getString(4));
							magazineInfo.setMasMagSubCategory(rs.getString(5));
							magazineInfo.setMasMagDesc(rs.getString(6));
							magazineInfo.setMasCreatedDate(rs.getString(7));
							magazineInfo.setMasCreatedBy(rs.getString(8));
							magazineInfo.setMasModifiedDate(rs.getString(9));
							magazineInfo.setMasModifiedBy(rs.getString(10));
							magazineInfo.setMasActiveFlag(rs.getString(11));
						}
					}
				}
			} 
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}*/
	}
	 public String getMasProductCpu(Long key){
			
		 Query query = entityManager.createNamedQuery("MasProductCpuRouting.findByKey");
		 query = query.setParameter("key", key);
		 List<MasProductCpuRouting> resultList = (List<MasProductCpuRouting>)query.getResultList();		 
		 if(resultList != null && !resultList.isEmpty()) {
			 return resultList.get(0).getCpuCode();
		 } 
				
		 return null;
	}
	 
		public Hospitals getHospitalSPOCDetailsByKey(Long key){
			
			Query query = entityManager.createNamedQuery("Hospitals.findByKey");
			query.setParameter("key", key);
			
			Hospitals resultList = (Hospitals) query.getSingleResult();
			
			if(resultList != null){
				return resultList;
			}
			
			return null;
			
		}
		
		public OPHealthCheckup getOpHealthByClaimKey(Long claimKey) {
			Query query = entityManager.createNamedQuery("OPHealthCheckup.findByClaim");
			query.setParameter("claimKey", claimKey);
			List<OPHealthCheckup> singleResult =  query.getResultList();
			if(singleResult != null && !singleResult.isEmpty()) {
				entityManager.refresh(singleResult.get(0));
				return singleResult.get(0);
			}
			return null;
		}
		
		public List<OPClaim> getOPClaimsByPolicyNumber(String policyNumber) {
			
			List<OPClaim> resultList = new ArrayList<OPClaim>();
			if (policyNumber != null) {

				Query findByPolicyNumber = entityManager.createNamedQuery(
						"OPClaim.findByPolicyNumber").setParameter("policyNumber",
						policyNumber);

				try {
					resultList = findByPolicyNumber.getResultList();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(resultList != null && !resultList.isEmpty()) {
				for (OPClaim claim : resultList) {
					entityManager.refresh(claim);
				}
			}
			return resultList;
		}
		

		public MasProductCpuRouting getMasProductForGMCRouting(Long key){

			Query query = entityManager.createNamedQuery("MasProductCpuRouting.findByKey");
			query = query.setParameter("key", key);
			List<MasProductCpuRouting> resultList = (List<MasProductCpuRouting>)query.getResultList();		 
			if(resultList != null && !resultList.isEmpty()) {
				return resultList.get(0);
			} 

			return null;
		}
		
		public BonusWaiver getBonusWaiver(String intimationNumber){
			
			 Query findByIntimationNo = entityManager.createNamedQuery(
						"BonusWaiver.findByIntimationNo").setParameter("intimationNumber",
								intimationNumber);
			 List<BonusWaiver> resultList = findByIntimationNo.getResultList();
			 if(resultList != null && !resultList.isEmpty()){
				 return resultList.get(0);
			 }
			 return null;	
		}
		
		public RRCRequest getRRCRequestByIntimationNo(String intimationNumber){
			
			 Query findByIntimationNo = entityManager.createNamedQuery("RRCRequest.findByintimationNo")
					 .setParameter("intimationId",intimationNumber);
			 List<RRCRequest> resultList = findByIntimationNo.getResultList();
			 if(resultList != null && !resultList.isEmpty()){
				 return resultList.get(0);
			 }
			 return null;	
		}
		
		public List<ExtraEmployeeEffortDTO> getCategoryDetailsFromTALKView(String intimationNumber)
		{
			 Query findByIntimationNo = entityManager.createNamedQuery("RRCRequest.findByintimationNo")
					 .setParameter("intimationId",intimationNumber);
			 List<RRCRequest> resultList = findByIntimationNo.getResultList();
			 List<ExtraEmployeeEffortDTO> employeeEffortDTO = new ArrayList<ExtraEmployeeEffortDTO>();
			 if(resultList != null && !resultList.isEmpty()){
				for(RRCRequest request: resultList){
					if(request !=null && request.getRrcRequestKey() !=null){
						Query query = entityManager.createNamedQuery("RRCCategory.findByRRCKeyAndCatId");
						query = query.setParameter("rrcRequestKey", request.getRrcRequestKey());
						query = query.setParameter("categoryId", SHAConstants.TALK_TALK_MASTER_ID);
						List<RRCCategory> rrcCategoryList = query.getResultList();
						if(null != rrcCategoryList && !rrcCategoryList.isEmpty())
						{
							Long count = 1L;
							for (RRCCategory rrcCategory : rrcCategoryList) {
								ExtraEmployeeEffortDTO extraEmpEffortDTO = new ExtraEmployeeEffortDTO();
								extraEmpEffortDTO.setCategoryKey(rrcCategory.getRrcCategoryKey());
								SelectValue selCategory = new SelectValue();
								selCategory.setId(rrcCategory.getCategoryId().getKey());
								selCategory.setValue(rrcCategory.getCategoryId().getValue());
								extraEmpEffortDTO.setCategory(selCategory);
								extraEmpEffortDTO.setRrcCategoryKey(rrcCategory.getKey());
								if(rrcCategory.getSubCategorykey() !=null){
									requestService.setRRCSubCatandSource(rrcCategory,extraEmpEffortDTO);
								}
								extraEmpEffortDTO.setTalkSpokento(rrcCategory.getTalkSpokento());
								extraEmpEffortDTO.setTalkSpokenDate(rrcCategory.getTalkSpokenDate());
								extraEmpEffortDTO.setTalkMobto(rrcCategory.getTalkMobto() !=null ? rrcCategory.getTalkMobto().toString() : null);
								extraEmpEffortDTO.setRemarks(request.getRequestRemarks());
								extraEmpEffortDTO.setSlNo(count);
								count++;
								employeeEffortDTO.add(extraEmpEffortDTO);
							}
						}
					}
				}
			 }
			//RRCRequest request = getRRCRequestByIntimationNo(intimationNumber);
			return employeeEffortDTO;	
		}
		
		/*public Long getInsuredKeyByClaimKey(Long claimKey) {
			Query query = entityManager.createNamedQuery("Claim.findInsuredKeyByClaimKey");
			query.setParameter("claimKey", claimKey);
			Long insuredKey = (Long) query.getSingleResult();
			return insuredKey;

		}*/
}
