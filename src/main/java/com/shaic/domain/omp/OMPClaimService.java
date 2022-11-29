package com.shaic.domain.omp;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;














//import com.google.common.util.concurrent.CycleDetectingLockFactory.Policy;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.validation.ValidatorUtils;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.OMPClaimPayment;
import com.shaic.claim.OMPProcessOmpAcknowledgementDocuments.Upload.OMPAcknowledgementDocumentsViewTableDTO;
import com.shaic.claim.OMPSearchAndCreateintimation.wizard.pages.OMPAckDetailsDTO;
import com.shaic.claim.OMPSearchAndCreateintimation.wizard.pages.OMPPaymentDetailsDTO;
import com.shaic.claim.OMPSearchAndCreateintimation.wizard.pages.OMPProposerDetailsDTO;
import com.shaic.claim.omp.searchregistration.OMPSearchClaimRegistrationTableDTO;
import com.shaic.claim.policy.search.ui.BancsSevice;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.domain.Country;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.Hospitals;
import com.shaic.domain.MASClaimAdvancedProvision;
import com.shaic.domain.MasOMPRejectionCategory;
import com.shaic.domain.MasterOMPEventProcedure;
import com.shaic.domain.MastersEvents;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPDocAcknowledgement;
import com.shaic.domain.OMPDocumentDetails;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.OMPReimbursement;
import com.shaic.domain.OMPRodRejection;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyNominee;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.ViewTmpIntimation;
import com.shaic.domain.preauth.ProvisionUploadHistory;
import com.shaic.domain.preauth.Speciality;
import com.shaic.domain.preauth.Stage;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.OMPClaimMapper;
import com.shaic.newcode.wizard.domain.omp.OMPPreviousClaimMapper;
import com.shaic.newcode.wizard.domain.omp.OMPProcessRejectionMapper;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.newcode.wizard.dto.ProcessRejectionDTO;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource.Builder;
import com.vaadin.v7.data.util.BeanItemContainer;
@Stateless
public class OMPClaimService {
	
		@PersistenceContext
		protected EntityManager entityManager;
		
		@EJB
		private OMPIntimationService intimationService ;
		
		@EJB
		private PolicyService policyService;
		
		/*@EJB
		private OMPTransaction ompTrans;*/
		
//		private FieldVisitRequest fvrRequest ;
		
		private final Logger log = LoggerFactory.getLogger(OMPClaimService.class);
		
		public OMPClaimService() {
			super();
		}

		@TransactionAttribute(TransactionAttributeType.REQUIRED)
		public void create(OMPClaim claim) {
//			ompTrans.insertOrUpdateOmpClaim(claim);
			if (claim != null) {
				if(null == claim.getKey()){
					entityManager.persist(claim);
					
				}else if(claim != null && claim.getKey() != null){
					/*OMPClaim createdClaim = entityManager.find(OMPClaim.class, claim.getKey());
				entityManager.refresh(createdClaim);
				createdClaim.setModifiedDate(new Date());
				createdClaim.setCurrentProvisionAmount(claim.getCurrentProvisionAmount());
				createdClaim.setIsVipCustomer(claim.getIsVipCustomer());
				createdClaim.setRegistrationRemarks(claim.getRegistrationRemarks());
				createdClaim.setClaimedAmount(claim.getClaimedAmount());
				entityManager.merge(createdClaim);*/
					entityManager.merge(claim);
				}
				entityManager.flush();
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

		public OMPClaim searchByClaimNum(String claimNumber) {
			Query findByClaimNumber = entityManager.createNamedQuery(
					"OMPClaim.findOMPByClaimNumber").setParameter("claimNumber",
					claimNumber);
			findByClaimNumber.setParameter("lobId", ReferenceTable.OMP_LOB_KEY);

			OMPClaim resultClaim = (OMPClaim) findByClaimNumber.getSingleResult();

			return resultClaim;

		}
		
		@SuppressWarnings("unchecked")
		public String getSpecialityName(Long key){
			
			Query findByClaimNumber = entityManager.createNamedQuery(
					"Speciality.findByClaimKey").setParameter("claimKey",
							key);

			List<Speciality> resultClaim = (List<Speciality>) findByClaimNumber.getResultList();
			String specialityName = "";
			for (Speciality speciality : resultClaim) {
				if(speciality.getSpecialityType() != null){
				specialityName +=speciality.getSpecialityType().getValue()+",";
				}
			}
			return specialityName;
		}
		
		public Hospitals getHospitalDetailsByKey(Long hospitalKey) {
			try{
			Query query = entityManager.createNamedQuery(
					"Hospitals.findByHospitalKey").setParameter("key", hospitalKey);
			Hospitals hospitals = (Hospitals) query.getSingleResult();
			if (hospitals != null) {
				return hospitals;
			}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}
		
//		public void setOutComeForCoveringLetterTask(Object outcome){
//			Map<String,Object> taskoutCome = (Map<String,Object>)outcome;
//			NewIntimationDto newIntimationDto = (NewIntimationDto) taskoutCome.get("fvrDetailsBean");
//			
//				String userId = taskoutCome.get(BPMClientContext.USERID).toString();
//				String password = taskoutCome.get(BPMClientContext.PASSWORD).toString();
//				GenerateCoveringLetterSearchTableDto bean = (GenerateCoveringLetterSearchTableDto)taskoutCome.get("Bean");
//				bean.getHumanTask().setOutcome(taskoutCome.get("OUTCOME").toString());
//				
////				if(autoRegisterFVR(newIntimationDto,userId))
////				{
////				PayloadBOType payloadBO = bean.getHumanTask().getPayloadCashless();
////				//com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType payloadBO = bean.getHumanTask().getPayload();
////				
////				FieldVisitType fieldVisit = new FieldVisitType();
////					if (null != fvrRequest && null != fvrRequest.getKey()) {
////						fieldVisit.setKey(fvrRequest.getKey());
////						if(null == payloadBO)
////						{
////							payloadBO = new PayloadBOType();
////						}
////					//payloadBO.setFieldVisit(fiel);
////					bean.getHumanTask().getPayloadCashless().setFieldVisit(fieldVisit);
//////					bean.getHumanTask().setPayloadCashless(payloadBO);
////					}
////				}
//				
//		//		InvokeHumanTask invokeHT=new InvokeHumanTask();
//		//		invokeHT.execute(userId,password,bean.getHumanTask(),null,null,null,null);
//				
//				com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType reimbPayLoad = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType();
//				newIntimationDto = bean.getClaimDto().getNewIntimationDto();
//				
//				if(null != newIntimationDto){
//				
//					com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.policy.PolicyType policyBo = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.policy.PolicyType();
//					
//					policyBo.setPolicyId(newIntimationDto.getPolicy().getPolicyNumber());
//					reimbPayLoad.setPolicy(policyBo);
//					
//					com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.intimation.IntimationType intimationBo = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.intimation.IntimationType();
//					intimationBo.setIntimationNumber(newIntimationDto.getIntimationId());
//					intimationBo.setKey(newIntimationDto.getKey());
//					reimbPayLoad.setIntimation(intimationBo);
//				}
//				
//					com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claimrequest.ClaimRequestType claimReq = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claimrequest.ClaimRequestType();
//					if(null != newIntimationDto.getCpuId())
//					{
//						claimReq.setCpuCode(String.valueOf(newIntimationDto.getCpuCode()));
//					}	
//					claimReq.setKey(bean.getClaimDto().getKey());
//					claimReq.setOption(SHAConstants.BILLS_NOT_RECEIVED); 
//					reimbPayLoad.setClaimRequest(claimReq);				
//					
//					com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claim.ClaimType claimBo = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claim.ClaimType();
//					claimBo.setClaimId(bean.getClaimDto().getClaimId());
//					claimBo.setKey(bean.getClaimDto().getKey());
//					claimBo.setClaimType(bean.getClaimDto().getClaimType() != null ? (bean.getClaimDto().getClaimType().getValue() != null ? bean.getClaimDto().getClaimType().getValue().toUpperCase(): "") : "");		
//					reimbPayLoad.setClaim(claimBo);
//					
//					com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.query.QueryType queryType = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.query.QueryType();
//					reimbPayLoad.setQuery(queryType);
//				
//				com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.classification.ClassificationType callsificationBo = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.classification.ClassificationType();
//				
//				callsificationBo.setPriority("");
//				callsificationBo.setSource("");
//				callsificationBo.setType("");
//				reimbPayLoad.setClassification(callsificationBo);
//				
//				SubmitGenCovLetterRegTask submitcovertingLetterTask = BPMClientContext.getSubmitGenerateCoveringLetterTask(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD);
//				
////				CLReminder  cashlessReminderTask = BPMClientContext.getCashlessReimnderLetterInitiateTask(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD);
//				
//				ReimbReminder reimburseReimnderLetterInitiateTask = BPMClientContext.getReimburseReimnderLetterInitiateTask(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
//				
//				try{
//							
//				reimburseReimnderLetterInitiateTask.initiate(BPMClientContext.BPMN_TASK_USER, reimbPayLoad);
//				submitcovertingLetterTask.execute(userId,bean.getHumanTask());
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//			
////			Claim claim = entityManager.find(Claim.class, bean.getClaimDto().getKey());
//			
//			
//			
//		}
//		
//		private Boolean autoRegisterFVR(NewIntimationDto newIntimationDto, String userName)
//		{
//			try
//			{
//				fvrRequest = new FieldVisitRequest();
//
//				Query findByIntimationKey = entityManager
//						.createNamedQuery("Claim.findByIntimationKey");
//				findByIntimationKey = findByIntimationKey.setParameter(
//						"intimationKey", newIntimationDto.getKey());
//				Intimation objIntimation = entityManager.find(Intimation.class, newIntimationDto.getKey());
//				Claim claim = (Claim) findByIntimationKey.getSingleResult();
//				
//				
//				Stage objStage = new Stage();
//				objStage.setKey(ReferenceTable.CLAIM_REGISTRATION_STAGE);
//				
//				Status fvrStatus = new Status();
//				fvrStatus.setKey(ReferenceTable.INITITATE_FVR);
//				
//				if(claim != null && claim.getIntimation() != null){
//					Intimation intimation = claim.getIntimation();
//					Long hospital = intimation.getHospital();
//					
//					Hospitals hospitalById = getHospitalById(hospital);
//					
//					TmpCPUCode tmpCPUCode = getTmpCPUCode(hospitalById.getCpuId());
//					if(tmpCPUCode != null){
//						fvrRequest.setFvrCpuId(tmpCPUCode.getCpuCode());
//					}
//					
//					
//				}
//				
//				MastersValue value = new MastersValue();
//				value.setKey(ReferenceTable.FVR_ALLOCATION_TO);
//				value.setValue(SHAConstants.FVR_ALLOCATION_ANY_ONE);
//				fvrRequest.setAllocationTo(value);
//				fvrRequest.setIntimation(objIntimation);
//				fvrRequest.setClaim(claim);
//				fvrRequest.setCreatedBy(userName);
//				fvrRequest.setFvrTriggerPoints(SHAConstants.REGISTERED_CLAIM);
//				fvrRequest.setPolicy(newIntimationDto.getPolicy());
//				fvrRequest.setAllocationTo(value);
//				fvrRequest.setActiveStatus(1L);
//				fvrRequest.setOfficeCode(newIntimationDto.getPolicy().getHomeOfficeCode());	
//				fvrRequest.setTransactionFlag("R");
//				fvrRequest.setStatus(fvrStatus);
//				fvrRequest.setStage(objStage);
//				entityManager.persist(fvrRequest);
//				entityManager.flush();
////				this.fvrRequest = fvrRequest;
//				return true;
//			}
//			catch (Exception e)
//			{
//				e.printStackTrace();
//				return false;
//			}
//		}
//		
		public TmpCPUCode getTmpCPUCode(Long cpuId){
			try{
			Query findCpuCode = entityManager.createNamedQuery("TmpCPUCode.findByKey").setParameter("cpuId", cpuId);
			 TmpCPUCode tmpCPUCode = (TmpCPUCode) findCpuCode.getSingleResult();
			return tmpCPUCode;
			}catch(Exception e){
					
			}
			return null;
		}
//		
		public Hospitals getHospitalById(Long key){
			
			Query query = entityManager.createNamedQuery("Hospitals.findByKey");
			query.setParameter("key", key);
			
			List<Hospitals> resultList = (List<Hospitals>) query.getResultList();
			
			if(resultList != null && ! resultList.isEmpty()){
				return resultList.get(0);
			}
			
			return null;
			
		}

//		public ClaimDto submitClaim(ClaimDto claimDto) {
//			Claim claim = new ClaimMapper().getClaim(claimDto);
//			create(claim);
//			entityManager.flush();
//			claimDto.setKey(claim.getKey());
//			claimDto.setStageId(claim.getStage().getKey());
//			claimDto.setStatusId(claim.getStatus().getKey());
//			claimDto.setClaimId(claim.getClaimId());
	//
//			return claimDto;
//		}
//
		@TransactionAttribute(TransactionAttributeType.REQUIRED)
		public ClaimDto submitClaim(OMPSearchClaimRegistrationTableDTO resultTask, ClaimDto claimDto) {
			claimDto.setCreatedBy(resultTask.getUsername() != null ? SHAUtils.getUserNameForDB(resultTask.getUsername()) : ""); 
			OMPClaim claim =  OMPClaimMapper.getInstance().getClaim(claimDto);
			OMPClaim createdClaim = getClaimforIntimation(claim.getIntimation().getKey());
			if(createdClaim != null && createdClaim.getIntimation().getKey() == claimDto.getNewIntimationDto().getKey()){
				claim.setKey(createdClaim.getKey());
			}	
			MastersValue climTyp = new MastersValue();
			climTyp.setKey(claimDto.getClaimType().getId());
			climTyp.setValue(claimDto.getClaimType().getValue());
			claim.setClaimType(climTyp);
//			claim.setRegistrationRemarks(SHAConstants.CLAIM_MANUAL_REGISTRATION);
			/******
			 * As per Prakash, We have set the Date of admission from Intimation to
			 * Claim while creating the Claim .....
			 **************/
			claim.setDataOfAdmission(claimDto.getNewIntimationDto().getAdmissionDate());
			claim.setClaimedAmount(claimDto.getClaimedAmount());
			claim.setClaimRegisteredDate(new Date());
			claim.setLobId(ReferenceTable.OMP_LOB_KEY);
			claim.setPlaceOfAccident(claimDto.getPlaceOfEvent());
			claim.setPlaveOfVisit(claimDto.getPlaceOfvisit());
			claim.setLossDetails(claimDto.getLossDetails());
			claim.setLossTime(claimDto.getLossTime());
			claim.setPlaveOfLossOrDelay(claimDto.getPlaceLossDelay());
			if(claimDto.getHospitalCountry()!=null){
				claim.setCountryId(claimDto.getHospitalCountry().getId());
			}
			if(claimDto.getHospitalId()!= null){
				claim.setHospital(claimDto.getHospitalId());
			}
			Policy policyByKey = getPolicyByKey(claimDto.getNewIntimationDto().getPolicy().getKey());
			//MASClaimAdvancedProvision claimAdvProvision = getClaimAdvProvision(Long.valueOf(policyByKey.getHomeOfficeCode()));
			if(claimDto.getNewIntimationDto().getInsuredPatient()!=null){
				claim.setInsuredKey(claimDto.getNewIntimationDto().getInsuredPatient());
			}
			
			
//			Double amt =  claimAdvProvision.getAvgAmt();
			
			NewIntimationDto newIntimationDto = claimDto.getNewIntimationDto();
			
			/*Double amt = calculateAmtBasedOnBalanceSI(newIntimationDto.getPolicy().getKey(),newIntimationDto.getInsuredPatient().getInsuredId(),newIntimationDto.getInsuredPatient().getKey()
					 , claimAdvProvision.getAvgAmt() != null ? claimAdvProvision.getAvgAmt() : 0d, claimDto.getNewIntimationDto().getKey(),newIntimationDto);*/
			
//			claim.setProvisionAmount(amt);
//			claim.setClaimedAmount(amt);
			//claim.setCurrentProvisionAmount(claimDto.getDollarInitProvisionAmount());
			claim.setProvisionAmount(claimDto.getDollarInitProvisionAmount());
			claim.setNormalClaimFlag("O");
			claim.setCreatedBy(claimDto.getCreatedBy());
			
//			claim.setCurrentProvisionAmount(claimDto.getDollarInitProvisionAmount());
			//entityManager.refresh(claim);
			//claim = entityManager.find(OMPClaim.class, claim.getKey());
			
			ClaimDto responseClaim = new OMPClaimMapper().getClaimDto(claim);
			responseClaim.setNewIntimationDto(claimDto.getNewIntimationDto());
				
			/*String outCome = "";
				
				if(claimDto.getStatusId().equals(ReferenceTable.PROCESS_REJECTED)){
				outCome = SHAConstants.OUTCOME_FOR_MANUAL_SUGGEST_REJECTION;
				Stage stgObj = new Stage();
				stgObj.setKey(ReferenceTable.CLAIM_REGISTRATION_STAGE);
				Status statusObj = new Status();
				statusObj.setKey(ReferenceTable.CLAIM_SUGGEST_REJECTION_STATUS);
				claim.setStage(stgObj);
				claim.setStatus(statusObj);
				claim.setProvisionAmount(0d);
				
				//As per Raju.M comment if rejected, need to update Modified By
				claim.setModifiedBy(resultTask.getUsername() != null ? SHAUtils.getUserNameForDB(resultTask.getUsername()) : "");
				claim.setModifiedDate(new Date());
				}else{
					outCome = SHAConstants.OUTCOME_FOR_OMP_MANUAL_REGISTRATION;
					Stage stgObj = new Stage();
					stgObj.setKey(ReferenceTable.CLAIM_REGISTRATION_STAGE);
					Status statusObj = new Status();
					statusObj.setKey(ReferenceTable.CLAIM_REGISTERED_STATUS);
					claim.setStage(stgObj);
					claim.setStatus(statusObj);
				}*/
				
								
//				setBPMOutcome(resultTask, responseClaim);	
				
//				submitDBProcedureForRegistration(resultTask, claim, claimDto, outCome);   
				
				if(claim != null && claim.getClaimType() != null && claim.getClaimType().getKey() != null && (ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY).equals(claim.getClaimType().getKey())){
					if(claimDto.getStatusId() != null && claimDto.getStatusId().equals(ReferenceTable.INTIMATION_REGISTERED_STATUS)){
						
						initiateBPMNforReimbursement(claim);
						
//						autoRegisterFVR(claim.getIntimation(),resultTask.getUserId());
					}
				}
			claimDto.setClaimId(claim.getClaimId());
			responseClaim.setClaimId(claim.getClaimId());
			responseClaim.setIntimationRemarks(claimDto.getIntimationRemarks());
			create(claim);
			String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
			if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag) && claim.getIntimation().getHospital() != null) {
				try {
					Hospitals hospitalDetailsByKey = getHospitalDetailsByKey(claim.getIntimation().getHospital());
//					String provisionAmtInput = SHAUtils.getProvisionAmtInput(claim, hospitalDetailsByKey.getName(), String.valueOf(claim.getCurrentProvisionAmount().longValue()));
//					PremiaService.getInstance().updateProvisionAmount(provisionAmtInput);
					PremiaService.getInstance().getOMPPolicyLock(claim, hospitalDetailsByKey.getHospitalCode());
					updateProvisionAmountToPremia(claim);

					
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			

			return responseClaim;
		}
//		
//		public void initiateBPMNforReimbursement(Claim claim, EntityManager em){
//			
//			this.entityManager = em;
//			initiateBPMNforReimbursement(claim);
//		}
//		
		public void initiateBPMNforReimbursement(OMPClaim claim){/*
			
//			OMPIntimation intimation = getIntimationByKey(intimationObj.getKey());
//			log.info("**************WHILE PROCESSING SAVED INTIMATION ***********-----> "
//					+ intimation != null ? intimation.getIntimationId()
//					: (intimation != null ? intimation.getIntimationId()
//							+ "--->THIS INTIMATION NOT YET SAVED IN OUR DB.. SO IT LEADS TO PROBLEM FOR US"
//							: "NO INTIMATIONS"));
			OMPIntimationRule intimationRule = new OMPIntimationRule();
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
			
			
		*/}
//
//		
		public Double calculateAmtBasedOnBalanceSI(Long policyKey , Long insuredId, Long insuredKey, Double provisionAmout,Long intimationKey,NewIntimationDto newIntimationDto)
		{
			DBCalculationService dbCalculationService = new DBCalculationService();
			SelectValue eventCodeValue = newIntimationDto.getEventCodeValue();
			if(eventCodeValue!=null){
				String eventCode = eventCodeValue.getValue();
				Double insuredSumInsured = dbCalculationService.getOmpInsuredSumInsured(policyKey, eventCode);
				Map balanceSIMap = dbCalculationService.getOmpBalanceSI(policyKey, insuredKey, 0l, null, insuredSumInsured,intimationKey,eventCode);
				Double balanceSI = (Double)balanceSIMap.get(SHAConstants.TOTAL_BALANCE_SI);
				System.out.println("--policy key---"+policyKey+"----insuredSumInsured----"+insuredSumInsured+"----insured key----"+insuredKey);
				return Math.min(balanceSI,provisionAmout);
			}
			return provisionAmout;
			
			//Double balanceSI = (Double)balanceSIMap.get(SHAConstants.TOTAL_BALANCE_SI);
			//Double amount = 0d;
//			TODO under Discussion to get balance SumInsured
			//Double insuredSumInsured = dbCalculationService.getOMPInsuredSumInsured(insuredId.toString(), policyKey);
			//amount = Math.min(dbCalculationService.getBalanceSI(policyKey, insuredKey ,insuredSumInsured) , provisionAmout);
			
			//Math.min(dbCalculationService.getOmpBalanceSI(policyKey, insuredKey, 0l, insuredSumInsured,intimationKey).get(SHAConstants.TOTAL_BALANCE_SI) , provisionAmout)
			//return Math.min(dbCalculationService.getOMPBalanceSI(policyKey, insuredKey, 0l, insuredSumInsured,intimationKey).get(SHAConstants.TOTAL_BALANCE_SI) , provisionAmout);
			
			//return amount;
		}
//		
		public void updateProvisionAmountToPremia(OMPClaim claim){
			
			String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
			if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
				try {
					Hospitals hospitalDetailsByKey = getHospitalDetailsByKey(claim.getIntimation().getHospital());
					String provisionAmtInput = SHAUtils.getOMPProvisionAmtInput(claim, hospitalDetailsByKey.getName(), String.valueOf(claim.getCurrentProvisionAmount().longValue()));
					updateProvisionAmountToPremia(provisionAmtInput);
				} catch(Exception e) {
					
				}
			}
		}
//		
//
		public String updateProvisionAmountToPremia(String input) {
			try {
				//Bancs Changes Start
				JSONObject jsonObject = new JSONObject(input);
				String policyNo = jsonObject.getString("policyNo");
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
//		
		private Policy  getPolicyByKey(Long policyKey) {
			
			Query query = entityManager.createNamedQuery("Policy.findByKey");
	    	query = query.setParameter("policyKey", policyKey);
	    	Policy policyList = (Policy) query.getSingleResult();
	    	if (policyList != null){
	    		return policyList;
	    	}
			return null;
		}
//		
		private MASClaimAdvancedProvision  getClaimAdvProvision(Long branchCode) {
			
			Query query = entityManager.createNamedQuery("MASClaimAdvancedProvision.findByBranchCode");
	    	query = query.setParameter("branchCode", branchCode);
	    	MASClaimAdvancedProvision claimAdvProvsionList = (MASClaimAdvancedProvision) query.getSingleResult();
	    	if (claimAdvProvsionList != null){
	    		return claimAdvProvsionList;
	    	}
			return null;
		}
//		
	/*	private Boolean autoRegisterFVR(OMPIntimation objIntimation, String userName)
		{
			try
			{
				FieldVisitRequest fvrRequest = new FieldVisitRequest();
				
				IntimationRule intimationRule = new IntimationRule();

				Query findByIntimationKey = entityManager
						.createNamedQuery("OMPClaim.findByOMPIntimationKey");
				findByIntimationKey = findByIntimationKey.setParameter(
						"intimationKey", objIntimation.getKey());
				findByIntimationKey.setParameter("lobId",ReferenceTable.OMP_LOB_KEY);
//				Intimation objIntimation = entityManager.find(Intimation.class, newIntimationDto.getKey());
				OMPClaim claim = (OMPClaim) findByIntimationKey.getSingleResult();
				
				
				Stage objStage = new Stage();
				objStage.setKey(ReferenceTable.CLAIM_REGISTRATION_STAGE);
				
				Status fvrStatus = new Status();
				fvrStatus.setKey(ReferenceTable.INITITATE_FVR);
				
				if(claim != null && claim.getIntimation() != null){
					OMPIntimation intimation = claim.getIntimation();
					Long hospital = intimation.getHospital();
					
					Hospitals hospitalById = getHospitalById(hospital);
					
//					TmpCPUCode tmpCPUCode = getTmpCPUCode(hospitalById.getCpuId());
//					if(tmpCPUCode != null){
//						fvrRequest.setFvrCpuId(tmpCPUCode.getCpuCode());
//					}

				}
				
				MastersValue value = new MastersValue();
				value.setKey(ReferenceTable.FVR_ALLOCATION_TO);
				value.setValue(SHAConstants.FVR_ALLOCATION_ANY_ONE);
				fvrRequest.setAllocationTo(value);
//	uc			fvrRequest.setIntimation(objIntimation);
//				fvrRequest.setClaim(claim);
				fvrRequest.setCreatedBy(userName);
				fvrRequest.setFvrTriggerPoints(SHAConstants.REGISTERED_CLAIM);
				fvrRequest.setPolicy(objIntimation.getPolicy());
				fvrRequest.setAllocationTo(value);
				fvrRequest.setActiveStatus(1L);
				fvrRequest.setOfficeCode(objIntimation.getPolicy().getHomeOfficeCode());	
				fvrRequest.setTransactionFlag("R");
				fvrRequest.setStatus(fvrStatus);
				fvrRequest.setStage(objStage);
				entityManager.persist(fvrRequest);
				entityManager.flush();
//				this.fvrRequest = fvrRequest;
//		uc		callReimbursmentFVRTask(fvrRequest, objIntimation, claim, userName);
				return true;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
		}*/
//		
//		public void callReimbursmentFVRTask(FieldVisitRequest fiedvisitRequest,Intimation objIntimation,Claim claim, String userName){
//			
//			com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType reimbursementPayload = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType();
//			
//			com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.policy.PolicyType policyBo = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.policy.PolicyType();
//			
//			policyBo.setPolicyId(objIntimation.getPolicy().getPolicyNumber());
//			reimbursementPayload.setPolicy(policyBo);
//			
//			com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.intimation.IntimationType intimationBo = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.intimation.IntimationType();
//			intimationBo.setIntimationNumber(objIntimation.getIntimationId());
//			intimationBo.setKey(objIntimation.getKey());
//			intimationBo.setStatus("TOFVR");
//			reimbursementPayload.setIntimation(intimationBo);
//			
//			com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claimrequest.ClaimRequestType claimReq = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claimrequest.ClaimRequestType();
//			if(null != objIntimation.getCpuCode())
//			{
//				claimReq.setCpuCode(String.valueOf(objIntimation.getCpuCode().getCpuCode()));
//			}	
//			claimReq.setKey(claim.getKey());
//			claimReq.setOption(SHAConstants.BILLS_NOT_RECEIVED); 
//			reimbursementPayload.setClaimRequest(claimReq);	
//			
//			com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claim.ClaimType claimBo = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claim.ClaimType();
//			claimBo.setClaimId(claim.getClaimId());
//			claimBo.setKey(claim.getKey());
//			claimBo.setClaimType(claim.getClaimType() != null ? (claim.getClaimType().getValue() != null ? claim.getClaimType().getValue().toUpperCase(): "") : "");		
//			reimbursementPayload.setClaim(claimBo);
//			
//			com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.query.QueryType queryType = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.query.QueryType();
//			reimbursementPayload.setQuery(queryType);
//			
//			com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.classification.ClassificationType callsificationBo = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.classification.ClassificationType();
//			
//			callsificationBo.setPriority("");
//			callsificationBo.setSource("");
//			callsificationBo.setType("");
//			reimbursementPayload.setClassification(callsificationBo);
//			
//			ProcessActorInfoType processActor = new ProcessActorInfoType();
//			processActor.setEscalatedByRole("");
//			processActor.setEscalatedByUser(BPMClientContext.BPMN_TASK_USER);
//			reimbursementPayload.setProcessActorInfo(processActor);
//			
//			ProductInfoType productInfo = new ProductInfoType();
//			productInfo.setLob("HEALTH");
//			
//			if(objIntimation.getPolicy() != null && objIntimation.getPolicy().getProduct() != null && objIntimation.getPolicy().getProduct().getKey() != null){
//				productInfo.setProductId(objIntimation.getPolicy().getProduct().getKey().toString());
//				productInfo.setProductName(objIntimation.getPolicy().getProduct().getValue());
//				reimbursementPayload.setProductInfo(productInfo);
//			}
//			
//			com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.hospitalinfo.HospitalInfoType hospitalInfoType = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.hospitalinfo.HospitalInfoType();
//			
//			Long hospital = objIntimation.getHospital();
//			
//			Hospitals hospitalById = getHospitalById(hospital);
//			hospitalInfoType.setKey(hospital);
//			hospitalInfoType.setHospitalType(hospitalById.getHospitalType().getValue());
//			hospitalInfoType.setNetworkHospitalType(hospitalById.getNetworkHospitalType());
//			reimbursementPayload.setHospitalInfo(hospitalInfoType);
//			
//			
//			if(objIntimation.getAdmissionDate() != null){
//				String intimDate = SHAUtils.formatIntimationDateValue(objIntimation.getAdmissionDate());
//				RRCType rrcType = new RRCType();
//				rrcType.setFromDate(intimDate);
//				reimbursementPayload.setRrc(rrcType);
//			}
//			
//			com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.fieldvisit.FieldVisitType fieldVisitType = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.fieldvisit.FieldVisitType();
//			fieldVisitType.setKey(fiedvisitRequest.getKey());
//			
//			Long cpuId = hospitalById.getCpuId();
//			if(cpuId != null){
//			TmpCPUCode tmpCPUCode = getTmpCPUCode(cpuId);
//			fieldVisitType.setRequestedBy(tmpCPUCode.getCpuCode() != null ? tmpCPUCode.getCpuCode().toString() : null);
//			}
//			reimbursementPayload.setFieldVisit(fieldVisitType);
//			
//			 FVR reimbursementFVR = BPMClientContext.getReimbursementFVR(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
//		        try {
//					reimbursementFVR.initiate(userName, reimbursementPayload);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			
//			//need to be implemented
//		}
//
//		public void setBPMOutcome(SearchClaimRegistrationTableDto resultTask,
//				ClaimDto claimDto) {
//			if (null != claimDto.getKey()) {          
//				if (resultTask != null) {
////					XMLElement payload = resultTask.getHumanTask().getPayload();
////					Map<String, String> regIntDetailsReq = new HashMap<String, String>();
////					Map<String, String> preAuthReqMap = new HashMap<String, String>();
//					
//					PaymentInfoType paymentInfo = new PaymentInfoType();
//					
//					
//					PayloadBOType payloadBO = resultTask.getHumanTask().getPayloadCashless();
//					
//					try {
//						
//						if(null != claimDto.getProvisionAmount()){
//							
////							regIntDetailsReq.put("provisionAmount", claimDto.getProvisionAmount().toString());
//							
//							paymentInfo.setProvisionAmount(claimDto.getProvisionAmount());
//						}
//						
//						if(null != claimDto.getClaimedAmount()){
//							
////							regIntDetailsReq.put("amountClaimed", claimDto.getClaimedAmount().toString());
//							
//							paymentInfo.setClaimedAmount(claimDto.getClaimedAmount());
//						}
//						
//						
//						ClaimType claimtype = new ClaimType();
//						
//						if(claimDto.getClaimType() != null && claimDto.getClaimType().getValue() != null){
//							claimtype.setClaimType(claimDto.getClaimType().getValue().toUpperCase());
//						}
//
//						claimtype.setClaimId(claimDto.getClaimId());
//						
//						claimtype.setKey(claimDto.getKey());
//						
//						payloadBO.getClaimRequest().setCpuCode(claimDto.getNewIntimationDto().getCpuCode());
//						
////						regIntDetailsReq.put("claimType", claimDto.getClaimType().getValue());					
////						payload = BPMClientContext.setNodeValue(payload, "RegIntDetails", "isBalanceSIAvailable", "");
//		
////						preAuthReqMap.put("key", claimDto.getKey().toString());
//						
//						PreAuthReqType prauthreqType = new PreAuthReqType();
//						
//						prauthreqType.setIsVIP(claimDto.getIsVipCustomer() != null && claimDto.getIsVipCustomer() == 1 ? true : false);
//						prauthreqType.setKey(claimDto.getKey());
//						prauthreqType.setOutcome("PREAUTHNOTRECEIVED");
//						
////						preAuthReqMap.put("isVIP", claimDto.getIsVipCustomer().toString());
//						
//						int age = 0;
//						if(claimDto.getNewIntimationDto().getInsuredAge() != null && claimDto.getNewIntimationDto().getInsuredAge() != "")
//						{
//								age = Integer.parseInt(claimDto.getNewIntimationDto().getInsuredAge());
//						}				
//						Boolean isSrCitizen =  age > 59 ? true : false ;
//
////						preAuthReqMap.put("isSrCitizen", isSrCitizen);
//						prauthreqType.setIsSrCitizen(isSrCitizen);
//										
//						payloadBO.setClaim(claimtype);
//						payloadBO.setPreAuthReq(prauthreqType);
//						
//						
////						PreAuthReq preauthRequest = new PreAuthReq();
////						preauthRequest.setKey(Long.valueOf(preAuthReqMap.get("key")));
////						preauthRequest.setIsVIP(Boolean.valueOf(preAuthReqMap.get("isVIP")));
////						preauthRequest.setIsSrCitizen(Boolean.valueOf(preAuthReqMap.get("isSrCitizen")));
//						
//						
////						payload = BPMClientContext.setNodeValue(payload, "RegIntDetails", regIntDetailsReq);
////						payload = BPMClientContext.setNodeValue(payload, "PreAuthReq", preAuthReqMap);
////						System.out.println("--------------------------------------------------------------------------");
////						BPMClientContext.printPayloadElement(payload);
////						System.out.println("--------------------------------------------------------------------------");					
//					
////					resultTask.getHumanTask().setPayload(payloadBO);
//					
//					IntimationType intimationType = payloadBO.getIntimation();
//					if (claimDto.getStatusId().equals(ReferenceTable.CLAIM_SUGGEST_REJECTION_STATUS)) {
//						
//						
//								resultTask.getHumanTask().setOutcome("SUGGEST REJECTION");
//								
//								if(intimationType!= null){
//									intimationType.setApproveRegistration("SUGGEST REJECTION");
//								}else{
//									intimationType = new IntimationType();
//									intimationType.setApproveRegistration("SUGGEST REJECTION");
//								}
//								
//								payloadBO.setIntimation(intimationType);
//
//						} else {
//								
//								resultTask.getHumanTask().setOutcome("NEW");
//								if(intimationType!= null){
//									intimationType.setApproveRegistration("NEW");
//								}else{
//									intimationType = new IntimationType();
//									intimationType.setApproveRegistration("NEW");
//								}
//								payloadBO.setIntimation(intimationType);
//					}
//					
//					
//					    ClassificationType classification = payloadBO.getClassification();
//					    
//					    Stage stage = entityManager.find(Stage.class,ReferenceTable.CLAIM_REGISTRATION_STAGE);
//					    classification.setSource(stage.getStageName());
//					    payloadBO.setClassification(classification);
//
//						resultTask.getHumanTask().setPayloadCashless(payloadBO);
//						
//						
////							BPMClientContext.printPayloadElement(resultTask.getHumanTask().getPayload());
////							InvokeHumanTask invokeHT = new InvokeHumanTask();
//							
//							SubmitClaimRegTask  submitRegTask =  BPMClientContext.getClaimRegistrationSubmitTask(resultTask.getUserId(),resultTask.getPassword());
//							
////							invokeHT.execute(resultTask.getUserId(),resultTask.getPassword(), resultTask.getHumanTask(), null, preauthRequest, null, null);						
//							
//							submitRegTask.execute(resultTask.getUserId(), resultTask.getHumanTask());
//							
//							Intimation intimation = new NewIntimationMapper().getNewIntimation(claimDto.getNewIntimationDto());
//							TmpCPUCode cpuObject = entityManager.find(TmpCPUCode.class, claimDto.getNewIntimationDto().getCpuId()); 
//							intimation.setCpuCode(cpuObject);
//
//						    if(claimDto.getStatusId().equals(ReferenceTable.CLAIM_SUGGEST_REJECTION_STATUS)){
//								intimation.setRegistrationStatus("REJECTED");
//							}else {
//								intimation.setRegistrationStatus("REGISTERED");	
//							}
//						    
//							Intimation response = entityManager.find(Intimation.class, intimation.getKey());
//							response.setRegistrationStatus(intimation.getRegistrationStatus());
//							entityManager.merge(response);
//							entityManager.flush();
//							
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					
//						// TODO handle Exception when BPM process invocation failed
//				}	
//			}
//				
//		}
//		
		public void submitDBProcedureForRegistration(OMPSearchClaimRegistrationTableDTO searchDto,OMPClaim claim, ClaimDto claimDto,String outCome){
			Map<String, Object> wrkFlowMap = new HashMap<String, Object>();
			/*System.out.println("Claim Key :"+claim.getKey());
			System.out.println("Claim Number :"+claim.getClaimId());
			System.out.println("Claim Type :"+claim.getClaimType().getValue());
			System.out.println("Claim AMount :"+claim.getClaimedAmount());
			System.out.println("User ID :"+searchDto.getUserId());
			System.out.println("Outcome :"+outCome);
			
			System.out.println("SHAConstants.WK_KEY "+searchDto.getWorkFlowKey());
			System.out.println("SHAConstants.INTIMATION_NO "+ claimDto.getNewIntimationDto().getIntimationId());
			System.out.println("SHAConstants.INTIMATION_KEY "+claimDto.getNewIntimationDto().getKey());
			System.out.println("SHAConstants.BAL_SI_FLAG "+ "Y");
			System.out.println("SHAConstants.ADMISSION_DATE "+ new java.sql.Date(claimDto.getNewIntimationDto().getAdmissionDate().getTime()));
			System.out.println("SHAConstants.INTIMATION_DATE "+ new java.sql.Date(claimDto.getNewIntimationDto().getIntimationDate().getTime()));
			System.out.println("SHAConstants.POLICY_KEY "+ claimDto.getNewIntimationDto().getPolicy().getKey());
			System.out.println("SHAConstants.POLICY_NUMBER "+ claimDto.getNewIntimationDto().getPolicy().getPolicyNumber());
			System.out.println("SHAConstants.LOB "+ "HEALTH");
			System.out.println("SHAConstants.LOB_TYPE "+ "H");
			System.out.println("SHAConstants.RECORD_TYPE "+ "FRESH");
			System.out.println("SHAConstants.STAGE_SOURCE "+ claimDto.getNewIntimationDto().getStage().getStageName());
			System.out.println("SHAConstants.CASHLESS_KEY "+ 0);
			System.out.println("SHAConstants.ESCALATE_ROLE_ID "+ 0);
			System.out.println("SHAConstants.RECONSIDER_FLAG "+ "N");*/
			
			wrkFlowMap.put(SHAConstants.WK_KEY,searchDto.getWorkFlowKey());
			wrkFlowMap.put(SHAConstants.INTIMATION_NO, claimDto.getNewIntimationDto().getIntimationId());
			wrkFlowMap.put(SHAConstants.INTIMATION_KEY,claimDto.getNewIntimationDto().getKey());
			wrkFlowMap.put(SHAConstants.BAL_SI_FLAG, "Y");
			if(claimDto.getNewIntimationDto().getAdmissionDate() != null){
				wrkFlowMap.put(SHAConstants.ADMISSION_DATE, new java.sql.Date(claimDto.getNewIntimationDto().getAdmissionDate().getTime()));
	    	}
			if(claimDto.getNewIntimationDto().getIntimationDate() != null){
				wrkFlowMap.put(SHAConstants.INTIMATION_DATE, new java.sql.Date(claimDto.getNewIntimationDto().getIntimationDate().getTime()));
			}
			wrkFlowMap.put(SHAConstants.POLICY_KEY, claimDto.getNewIntimationDto().getPolicy().getKey());
			wrkFlowMap.put(SHAConstants.POLICY_NUMBER, claimDto.getNewIntimationDto().getPolicy().getPolicyNumber());
			
			wrkFlowMap.put(SHAConstants.LOB, "HEALTH");
			wrkFlowMap.put(SHAConstants.LOB_TYPE, "H");
			
			wrkFlowMap.put(SHAConstants.RECORD_TYPE, "FRESH");
			wrkFlowMap.put(SHAConstants.STAGE_SOURCE, claimDto.getNewIntimationDto().getStage().getStageName());
			wrkFlowMap.put(SHAConstants.CASHLESS_KEY, 0);
			wrkFlowMap.put(SHAConstants.ESCALATE_ROLE_ID, 0);
			wrkFlowMap.put(SHAConstants.RECONSIDER_FLAG, "N");

			
			wrkFlowMap.put(SHAConstants.DB_CLAIM_KEY,claim.getKey());
			wrkFlowMap.put(SHAConstants.DB_CLAIM_NUMBER,claim.getClaimId());
			wrkFlowMap.put(SHAConstants.CLAIM_TYPE, claim.getClaimType().getValue());
			wrkFlowMap.put(SHAConstants.CLAIMED_AMOUNT,claim.getClaimedAmount() < 0 ? 0 :claim.getClaimedAmount());
			wrkFlowMap.put(SHAConstants.USER_ID,searchDto.getUserId());
			wrkFlowMap.put(SHAConstants.OUTCOME,outCome);
			
			Object[] objArrayForSubmit = SHAUtils.getOMPRevisedObjArrayForSubmit(wrkFlowMap);
			
			DBCalculationService dbCalService = new DBCalculationService();
			dbCalService.initiateOMPTaskProcedure(objArrayForSubmit);
		}
//

		public OMPClaim searchByKey(Long a_key) {
			OMPClaim find = entityManager.find(OMPClaim.class, a_key);
			entityManager.refresh(find);
			return find;
		}

		public OMPClaim getClaimforIntimation(Long intimationKey) {
			OMPClaim a_claim = null;
			if (intimationKey != null) {

				Query findByIntimationKey = entityManager
						.createNamedQuery("OMPClaim.findByOMPIntimationKey");
				findByIntimationKey.setParameter(
						"intimationKey", intimationKey);
				findByIntimationKey.setParameter("lobId",ReferenceTable.OMP_LOB_KEY);
				try {

					if (findByIntimationKey.getResultList().size() > 0) {					
						a_claim = (OMPClaim)findByIntimationKey.getResultList().get(0);
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
		
//		public OMPClaim getPreauthByIntimationKey(Long intimationKey, EntityManager entityManager) {
//			this.entityManager = entityManager;
//			Query query = entityManager
//					.createNamedQuery("OMPClaim.findByIntimationKey");
//			query.setParameter("intimationKey", intimationKey);
//			OMPClaim singleResult = (OMPClaim) query.getSingleResult();
//			return singleResult;
//		}
		

		@SuppressWarnings("unchecked")
		public OMPClaim getClaimsByIntimationNumber(String intimationNumber) {
			List<OMPClaim> resultClaim = null;
			if (intimationNumber != null) {

				Query findByIntimationNum = entityManager.createNamedQuery(
						"OMPClaim.findOMPByIntimationNumber").setParameter(
						"intimationNumber", intimationNumber);
				findByIntimationNum.setParameter("lobId", ReferenceTable.OMP_LOB_KEY);

				try {
					resultClaim = (List<OMPClaim>) findByIntimationNum.getResultList();
					
					if(resultClaim != null && !resultClaim.isEmpty()){
						return resultClaim.get(0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		public List<OMPClaim> getClaimforIntimationByParams(
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

					final CriteriaQuery<OMPIntimation> intimationCriteriaQuery = builder
							.createQuery(OMPIntimation.class);
//					final CriteriaQuery<Policy> policyCriteriaQuery = builder
//							.createQuery(Policy.class);
					final CriteriaQuery<OMPClaim> claimCriteriaQuery = builder
							.createQuery(OMPClaim.class);

					// Root<Policy> policyRoot =
					// policyCriteriaQuery.from(Policy.class);
					Root<OMPIntimation> intimationRoot = intimationCriteriaQuery
							.from(OMPIntimation.class);

					// Join<Intimation,Root<Policy>> policyJoin =
					// intimationRoot.join("policy", JoinType.INNER);

					Root<OMPClaim> claimRoot = claimCriteriaQuery.from(OMPClaim.class);
					Join<OMPIntimation, TmpCPUCode> cpuCodeJoin = intimationRoot.join(
							"cpuCode", JoinType.INNER);
					Join<OMPIntimation, Policy> policyJoin = intimationRoot.join(
							"policy", JoinType.INNER);

					Join<OMPClaim, OMPIntimation> intimationJoin = claimRoot.join(
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
							.isNotNull(claimRoot.<OMPIntimation> get("intimation"));
					predicates.add(intimationNotNullPredicate);

					if (!ValidatorUtils.isNull(intimationNumber)) {
						Predicate intimationNumberPredicate = builder.like(
								intimationJoin.<String> get("intimationId"),
								intimationNumber);
						predicates.add(intimationNumberPredicate);
					}

					if (!ValidatorUtils.isNull(policyNumber)) {
						Predicate policyNumberPredicate = builder
								.like(claimRoot.<OMPIntimation> get("intimation")
										.<Policy> get("policy")
										.<String> get("policyNumber"), policyNumber);
						predicates.add(policyNumberPredicate);
					}
					if (!ValidatorUtils.isNull(cpuValue)) {
						Predicate cpuPredicate = builder.like(
								claimRoot.<OMPIntimation> get("intimation")
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

					final TypedQuery<OMPClaim> query = entityManager
							.createQuery(claimCriteriaQuery);
					List<OMPClaim> claimResult = query.getResultList();

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
		public List<OMPClaim> getClaimsByPolicyNumber(String policyNumber) {
			
			List<OMPClaim> resultList = new ArrayList<OMPClaim>();
			if (policyNumber != null) {

				Query findByPolicyNumber = entityManager.createNamedQuery(
						"OMPClaim.findByPolicyNumber").setParameter("policyNumber",
						policyNumber);
				findByPolicyNumber.setParameter("lobId", ReferenceTable.OMP_LOB_KEY);
				try {
					resultList = findByPolicyNumber.getResultList();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(resultList != null && !resultList.isEmpty()) {
				for (OMPClaim claim : resultList) {
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
			if(resultList != null && !resultList.isEmpty()) {
				for (ViewTmpClaim claim : resultList) {
//					entityManager.refresh(claim);
				}
			}
			return resultList;
		}
		
		public List<ViewTmpClaim> getViewTmpClaimsByIntimationKeys(List<ViewTmpIntimation> intimationKeys) {
			
			if(intimationKeys != null){
				Query query = entityManager.createNamedQuery("ViewTmpClaim.findByIntimationKeys");
				query.setParameter("intimationKeys", intimationKeys);
		
				List<ViewTmpClaim> resultList =  (List<ViewTmpClaim>) query.getResultList();
				if(resultList != null && resultList.size()!=0){
//					entityManager.refresh(resultList);
					return resultList;
				}
			}
			return null;
		}

//		@SuppressWarnings("unchecked")
//		public List<PreviousClaimsTableDTO> getPreviousClaimForPolicy(NewIntimationDto newIntimationDto) {
//			
//			List<PreviousClaimsTableDTO> previousClaimsListDTO = null;
//			if(newIntimationDto != null && newIntimationDto.getPolicy() != null && newIntimationDto.getPolicy().getPolicyNumber() != null){	
//				
//			List<ViewTmpClaim> claimResultList = getViewTmpClaimsByPolicyNumber(newIntimationDto.getPolicy().getPolicyNumber());
//			List<ViewTmpClaim> resultList = new ArrayList<ViewTmpClaim>();
//			if(claimResultList != null && !claimResultList.isEmpty())
//			{
//				for (ViewTmpClaim claim : claimResultList) {
//					if(StringUtils.containsIgnoreCase(claim.getIntimation().getIntimationId(), newIntimationDto.getIntimationId())){
//						continue;
//					}
//					resultList.add(claim);
//						
//				}
//			}		
//			
//			if(!resultList.isEmpty()){
//				previousClaimsListDTO = new ArrayList<PreviousClaimsTableDTO>();
//				for(ViewTmpClaim claim :resultList){				
//					PreviousClaimsTableDTO previousClaimsTableDTO = PreviousClaimMapper.getInstance().getPreviousClaimsTableDTO(claim);
//					previousClaimsTableDTO.setInsuredName(claim.getIntimation().getPolicy().getProposerFirstName());
//					previousClaimsTableDTO.setPatientName(claim.getIntimation().getInsuredPatientName());
//					
//					if(claim.getIntimation().getHospital() != null){
//						Hospitals hospital = entityManager.find(Hospitals.class, claim.getIntimation().getHospital());
//						if(hospital != null){
//							previousClaimsTableDTO.setHospitalName(hospital.getName());
//						}
//					}
//					
//					String diagnosisName = " ";
//					List<PedValidation> pedValidationsList = getDiagnosis(previousClaimsTableDTO.getIntimationKey());
//					if (!pedValidationsList.isEmpty()) {
//						for (PedValidation pedValidation : pedValidationsList) {
//							diagnosisName = (diagnosisName == " " ? ""
//									: diagnosisName + ", ")
//									+ getDiagnosisNames(pedValidation);
//						}
//					}
//					previousClaimsTableDTO.setDiagnosis(diagnosisName);				
//					
//					previousClaimsListDTO.add(previousClaimsTableDTO);	
//				}			 
//			}
//		  }
//				
//			return previousClaimsListDTO;
//		}
//		
//		private String getDiagnosisNames(PedValidation pedValidation){
//			MasterService masterService = new MasterService();
//			String diagnosisName = masterService.getDiagnosis(pedValidation
//					.getDiagnosisId(),this.entityManager);
//			
//			return diagnosisName;
//		}
//		
//		private List<PedValidation> getDiagnosis(Long intimationKey){
//			
//			PEDValidationService pedValidationService =  new PEDValidationService();
//			List<PedValidation> pedList = pedValidationService.getDiagnosis(intimationKey,this.entityManager);
//			
//			return pedList;
//		}
		

		@SuppressWarnings("unchecked")
		public List<OMPClaim> getClaimsByInsuredId(Long insuredId) {
			List<OMPClaim> resultList = new ArrayList<OMPClaim>();
			if (insuredId != null) {

				Query findByInsuredId = entityManager.createNamedQuery(
						"OMPClaim.findByInsuredID").setParameter("insuredId",
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

		public OMPClaim getClaimByKey(Long key) {
			Query query = entityManager.createNamedQuery("OMPClaim.findByKeyOMP");
			query.setParameter("primaryKey", key);
			query.setParameter("lobId", ReferenceTable.OMP_LOB_KEY);
			List<OMPClaim> claimList = (List<OMPClaim>)query.getResultList();
			
			if(claimList != null && ! claimList.isEmpty()){
				entityManager.refresh(claimList.get(0));
				return claimList.get(0);
			}
			else{
				return null;
			}
		}
		
//		@SuppressWarnings("unchecked")
//		public OMPClaim getClaimByClaimKey(Long claimKey) {
//			Query query = entityManager.createNamedQuery("OMPClaim.findByKeyOMP");
//			query.setParameter("primaryKey", claimKey);
//			query.setParameter("lobId", ReferenceTable.OMP_LOB_KEY);
//			
//			List<OMPClaim> claim = (List<OMPClaim>)query.getResultList();
//			
//			if(claim != null && ! claim.isEmpty()){
//				for (OMPClaim claim2 : claim) {
//					entityManager.refresh(claim2);
//				}
//				return claim.get(0);
//			}
//				return null;
//			
//		}
		
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

		public OMPClaim getClaimByClaimKey(Long claimKey,EntityManager em){
			this.entityManager = em;
			return getClaimByKey(claimKey);
		}

//		public ConvertClaimDTO searchByClaimKey(Long key) {
//			OMPClaim find = entityManager.find(OMPClaim.class, key);
//			entityManager.refresh(find);
//
//			if (find != null) {
//				ConvertClaimDTO convertClaimDto = ConvertClaimMapper.getInstance()
//						.getClaimDTO(find);
//				return convertClaimDto;
//			}
//			return null;
//		}
//
//		public Boolean saveConversionReason(ConvertClaimDTO convertClaim,
//				String option,SearchConvertClaimTableDto searchFormDto) {
//
//			Long key = convertClaim.getKey();
//
//			Claim claimData = ConvertClaimMapper.getInstance().getClaim(convertClaim);
//			
//			//convertClaim.setIntimationNumber(claimData.getIntimation().getIntimationId());
//
//			Claim find = entityManager.find(Claim.class, key);
//			entityManager.refresh(find);
//
//			if (find != null) {
//				find.setConversionReason(claimData.getConversionReason());
//				if (option.equals("submit")) {
//					entityManager.merge(find);
//
//				} else {
//					MastersValue claimTypeId = new MastersValue();
//					claimTypeId.setKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
//					find.setClaimType(claimTypeId);
//					find.setConversionFlag(1l);
//					find.setClaimSectionCode(null);
//					find.setClaimCoverCode(null);
//					find.setClaimSubCoverCode(null);
//					find.setConversionDate(new Timestamp(System.currentTimeMillis()));
//					entityManager.merge(find);
//					entityManager.flush();
//					
//					updateCpuCodeForIntimation(find.getIntimation());
//					
//					setBPMOutComeForConvertClaim(convertClaim, find,searchFormDto, "APPROVE");
//					
//					autoRegisterFVR(find.getIntimation(), searchFormDto.getUsername());
//					
//				}
//				
//				return true;
//			}
//			return false;
//		}
//		
//		public Boolean saveCashlessConversion(ConvertClaimDTO convertClaim,
//				String option,SearchConverClaimCashlessTableDTO searchFormDto) {
//
//			Long key = convertClaim.getKey();
//
//			Claim claimData = ConvertClaimMapper.getInstance().getClaim(convertClaim);
//			
//			//convertClaim.setIntimationNumber(claimData.getIntimation().getIntimationId());
//
//			Claim find = entityManager.find(Claim.class, key);
//			entityManager.refresh(find);
//
//			if (find != null) {
//				find.setConversionReason(claimData.getConversionReason());
//				if (option.equals("submit")) {
//					entityManager.merge(find);
//
//				} else {
//					MastersValue claimTypeId = new MastersValue();
//					claimTypeId.setKey(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
//					find.setClaimType(claimTypeId);
//					find.setConversionFlag(1l);
//					find.setConversionDate(new Timestamp(System.currentTimeMillis()));
//					entityManager.merge(find);
//					entityManager.flush();
//					
//					updateCpuCodeForCashlessConversion(find.getIntimation());
//					
//					
//				}
//				
//				return true;
//			}
//			return false;
//		}
//		
//		
//		
//		public void updateCpuCodeForIntimation(Intimation intimation){
//			
//			Policy policy = intimation.getPolicy();
//			
//			if(policy.getHomeOfficeCode() != null) {
//				OrganaizationUnit branchOffice = getInsuredOfficeNameByDivisionCode(policy.getHomeOfficeCode());
//				if(branchOffice != null){
//					String officeCpuCode = branchOffice.getCpuCode();
//					if(officeCpuCode != null) {
//						TmpCPUCode masCpuCode = getMasCpuCode(Long.valueOf(officeCpuCode));
//						if(masCpuCode != null){
//						intimation.setCpuCode(masCpuCode);
//						entityManager.merge(intimation);
//						entityManager.flush();
//						}
//						
//					}
//				}
//			}
//		}
//		
//	public void updateCpuCodeForCashlessConversion(Intimation intimation) {
//			
//		 Hospitals searchbyHospitalKey = searchbyHospitalKey(intimation.getHospital());
//		 if (searchbyHospitalKey != null) {
//			 TmpCPUCode tmpCPUCode = getTmpCPUCode(searchbyHospitalKey.getCpuId());
//			 if (tmpCPUCode != null) {
//				 intimation.setCpuCode(tmpCPUCode);
//					entityManager.merge(intimation);
//					entityManager.flush();
//			 }
//			 
//		 }
//		
//			
//						
//		}
//

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
		
//		
//		public Boolean submitConvertToReimbursement(ConvertClaimDTO convertClaim,
//				String option,SearchConvertClaimTableDto searchFormDto) {
//
//			Long key = convertClaim.getKey();
//
//			Claim claimData = ConvertClaimMapper.getInstance().getClaim(convertClaim);
//			
//			//convertClaim.setIntimationNumber(claimData.getIntimation().getIntimationId());
//
//			Claim find = entityManager.find(Claim.class, key);
//			entityManager.refresh(find);
//
//			if (find != null) {
//				find.setConversionReason(claimData.getConversionReason());
//				if (option.equals("submit")) {
//					entityManager.merge(find);
//					entityManager.flush();
//
//				} else {
//					MastersValue claimTypeId = new MastersValue();
//					claimTypeId.setKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
//					find.setClaimType(claimTypeId);
//					find.setConversionFlag(1l);
//					find.setClaimSectionCode(null);
//					find.setClaimCoverCode(null);
//					find.setClaimSubCoverCode(null);
//					find.setConversionDate(new Timestamp(System.currentTimeMillis()));
//					entityManager.merge(find);
//					entityManager.flush();
//					
//					updateCpuCodeForIntimation(find.getIntimation());
//					
//					Map<String, Object> wrkFlowMap = (Map<String, Object>) searchFormDto.getDbOutArray();
//					
//					if (wrkFlowMap != null ){
//						
//						if (wrkFlowMap.get(SHAConstants.CASHLESS_KEY) != null){
//							wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.OUTCOME_FOR_REIM_SEARCH_CONVERTION_WPA);
//						} else {
//							wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.OUTCOME_FOR_REIM_SEARCH_CONVERTION_WQRPLY);
//						}
//						
//						wrkFlowMap.put(SHAConstants.CLAIM_TYPE, find.getClaimType().getValue());
//						
//						Object[] objArrayForSubmit = SHAUtils.getObjArrayForSubmit(wrkFlowMap);
//						
//						DBCalculationService dbCalService = new DBCalculationService();
//						dbCalService.initiateTaskProcedure(objArrayForSubmit);
//					} else {
//						submitDBForReimConvrSearch(find);
//					}
//					
//					 initiateBPMNforReimbursement(find);
//					 autoRegisterFVR(find.getIntimation(), searchFormDto.getUsername());
//				}
//				
//				return true;
//			}
//			return false;
//		}
//		
//		
//		@SuppressWarnings("unused")
//		public void setBPMOutComeForConvertClaim(ConvertClaimDTO convertClaim,Claim claim,SearchConvertClaimTableDto searchFormDto,String outCome){
//			if (searchFormDto != null) {
//				//XMLElement payload = searchFormDto.getHumanTask().getPayload();
//				HumanTask humanTask = searchFormDto.getHumanTask();
////				PayloadBOType payload = humanTask.getPayloadCashless();
//				
//				Map<String, String> regIntDetailsReq = new HashMap<String, String>();
//				Map<String,String> pedReq=new HashMap<String, String>();
//				
//				//ClaimRequestType claimRequest = payload.getClaimRequest();
//				
//				
//				/*if(searchFormDto.getKey()!=null){
//					
//					pedReq.put("key", searchFormDto.getKey().toString());	
//				}*/			
//				/*if(searchFormDto.getClaimNumber()!=null){
//					regIntDetailsReq.put("ClaimNumber",searchFormDto.getClaimNumber());
//					//regIntDetailsReq.put("IntimationNumber",convertClaim.getIntimationNumber());
//				}*/
//				MastersValue claimTypeValue = getMaster(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);		
//					
//				if(humanTask == null){
//					
////					ClaimType claimType = payload.getClaim();
////					
////					if(null != claimType)
////					{
////						claimType.setClaimId(searchFormDto.getClaimNumber());
////						claimType.setKey(claim.getKey());
////						if(claimTypeValue != null && claimTypeValue.getValue() != null){
////							claimType.setClaimType(claimTypeValue.getValue().toUpperCase());
////						}
////					}
////					else
////					{
////						claimType = new ClaimType();
////						claimType.setClaimId(searchFormDto.getClaimNumber());
////						claimType.setKey(claim.getKey());
////						if(claimTypeValue != null && claimTypeValue.getValue() != null){
////							claimType.setClaimType(claimTypeValue.getValue().toUpperCase());
////						}
////					}
//	//
////				
////				ClassificationType classification = payload.getClassification();
////				classification.setSource(SHAConstants.CONVERT_CLAIM);
////				payload.setClassification(classification);
////				
////				if(payload.getClaimRequest() != null){
////					if(payload.getClaimRequest().getCpuCode() == null || (payload.getClaimRequest().getCpuCode() != null && payload.getClaimRequest().getCpuCode().equalsIgnoreCase(""))){
////						if(claim != null && claim.getIntimation() != null && claim.getIntimation().getCpuCode() != null && claim.getIntimation().getCpuCode().getCpuCode() != null){
////							ClaimRequestType claimRequest = payload.getClaimRequest();
////							claimRequest.setCpuCode(claim.getIntimation().getCpuCode().getCpuCode().toString());
////						}
////					}
////				}
////				
////				payload.setClaim(claimType);
////				humanTask.setOutcome(outCome);
////				humanTask.setPayloadCashless(payload);
//				
//				/*DB WORK FLOW SUBMIT*/
//				
//				Map<String, Object> wrkFlowMap = (Map<String, Object>) convertClaim.getDbOutArray();
//				wrkFlowMap.put(SHAConstants.CLAIM_TYPE,claimTypeValue.getValue().toUpperCase());
//				wrkFlowMap.put(SHAConstants.USER_ID,searchFormDto.getUsername());
//				wrkFlowMap.put(SHAConstants.CPU_CODE,claim.getIntimation().getCpuCode().getCpuCode().toString());
//				
//				String dbOutCome = SHAConstants.OUTCOME_FOR_REIM_CONVR_INPROCESS;
//				wrkFlowMap.put(SHAConstants.OUTCOME,dbOutCome);
//				
//				String ackKey = (String)wrkFlowMap.get(SHAConstants.REFERENCE_USER_ID);
//				
//				if(ackKey != null && NumberUtils.isNumber(ackKey)){
//					DocAcknowledgement docAcknowledgementBasedOnKey = getDocAcknowledgment(Long.valueOf(ackKey));
//					if(docAcknowledgementBasedOnKey != null){
//					wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_CONVERT_REIMB_CREATE_ROD);
//					
//					submitBpmnForRod(Long.valueOf(ackKey));
//					
//					}
//
//				}
//				
//
//				Object[] objArrayForSubmit = SHAUtils.getObjArrayForSubmit(wrkFlowMap);
//				
//				DBCalculationService dbCalService = new DBCalculationService();
//				dbCalService.initiateTaskProcedure(objArrayForSubmit);
//				
//				
//			     initiateBPMNforReimbursement(claim);
//				
////					SubmitClaimConvTask submitClaimConvTask = BPMClientContext.getSubmitClaimConvTask(searchFormDto.getUsername(), searchFormDto.getPassword());
////					try{
////					submitClaimConvTask.execute(searchFormDto.getUsername(), humanTask);
////					}catch(Exception e){
////						e.printStackTrace();
////					}
//				}else if (humanTask.getPayload() != null){
//					com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType reimbursementPayload = humanTask.getPayload();
//					com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.classification.ClassificationType reimbursementClassification = reimbursementPayload.getClassification();
//					reimbursementClassification.setSource(SHAConstants.CONVERT_CLAIM);
//					reimbursementPayload.setClassification(reimbursementClassification);
//					
//					com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claim.ClaimType claimType2 = reimbursementPayload.getClaim();
//					
//					if(null != claimType2)
//					{
//						claimType2.setClaimId(searchFormDto.getClaimNumber());
//						claimType2.setKey(claim.getKey());
//						if(claimTypeValue != null && claimTypeValue.getValue() != null){
//							claimType2.setClaimType(claimTypeValue.getValue().toUpperCase());
//						}
//					}
//					else
//					{
//						claimType2 = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claim.ClaimType();
//						claimType2.setClaimId(searchFormDto.getClaimNumber());
//						claimType2.setKey(claim.getKey());
//						if(claimTypeValue != null && claimTypeValue.getValue() != null){
//							claimType2.setClaimType(claimTypeValue.getValue().toUpperCase());
//						}
//					}
//					
//					reimbursementPayload.setClaim(claimType2);
//					humanTask.setOutcome("APPROVE");
//					
//					SubmitAckProcessConvertClaimToReimbTask submitConvertReimbursement = BPMClientContext.getSubmitConvertReimbursement(searchFormDto.getUsername(), searchFormDto.getPassword());
//					try{
//						submitConvertReimbursement.execute(searchFormDto.getUsername(), humanTask);
//					}catch(Exception e){
//						e.printStackTrace();
//					}
//					
//					
//				}
//					//InvokeHumanTask invokeHT=new InvokeHumanTask();
//					//invokeHT.execute(searchFormDto.getUsername(), searchFormDto.getPassword(), searchFormDto.getHumanTask(), null, null, pedReqDetails, null);
//					
//					System.out.println("BPM Executed Successfully");
//			}		
//		}
//		
//		public void submitBpmnForRod(Long ackKey){
//			
//			
//			DocAcknowledgement docAcknowledgementBasedOnKey = getDocAcknowledgment(ackKey);
//			
//			Claim claimByKey = docAcknowledgementBasedOnKey.getClaim();
//			
//			NewIntimationDto newIntimationDto = new NewIntimationDto();
//			ReceiptOfDocumentsDTO rodDTO = new ReceiptOfDocumentsDTO();
//			ClaimDto claimDTO = null;
//			if (claimByKey != null) {
//				// setClaimValuesToDTO(preauthDTO, claimByKey);
//				IntimationService intimationService = new IntimationService();
//				
//				newIntimationDto = intimationService
//						.getIntimationDto(claimByKey.getIntimation(),entityManager);
//				claimDTO =  ClaimMapper.getInstance().getClaimDto(claimByKey);
//				claimDTO.setNewIntimationDto(newIntimationDto);
//				rodDTO.setClaimDTO(claimDTO);
//			}
//			rodDTO.setStrUserName("claimshead");
//			rodDTO.setStrPassword("Star@123");
//			
//			AcknowledgementDocumentsReceivedService ackDocReceivedService = new AcknowledgementDocumentsReceivedService();
//
//			ackDocReceivedService.submitTaskFromConvertToROD(rodDTO,
//					docAcknowledgementBasedOnKey, false, false,entityManager);
//		}
//		
//		
//		
//		@SuppressWarnings("unchecked")
//		public DocAcknowledgement getDocAcknowledgment(Long acknowledgementKey) {
//
//			Query query = entityManager
//					.createNamedQuery("DocAcknowledgement.findByKey");
//			query.setParameter("ackDocKey", acknowledgementKey);
//
//			List<DocAcknowledgement> ackObjList = (List<DocAcknowledgement>) query
//					.getResultList();
//
//			for (DocAcknowledgement docAcknowledgement : ackObjList) {
//				entityManager.refresh(docAcknowledgement);
//			}
//
//			if (ackObjList.size() > 0) {
//				return ackObjList.get(0);
//			}
//
//			return null;
//
//		}
//		
//		public DocAcknowledgement getDocAcknowledgment(Long acknowledgementKey,EntityManager em) {
//			this.entityManager = em;
//			return getDocAcknowledgment(acknowledgementKey);
//		}
		
		@SuppressWarnings("unused")
		public ProcessRejectionDTO processRejectionByClaimKey(Long key) {
//			Claim find = entityManager.find(Claim.class, key);
//			entityManager.refresh(find);
			Query findByClaimKey = entityManager.createNamedQuery(
					"OMPClaim.findByOMPIntimationKey").setParameter("intimationKey", key);
			findByClaimKey.setParameter("lobId",ReferenceTable.OMP_LOB_KEY);
			if(findByClaimKey.getResultList() != null && !findByClaimKey.getResultList().isEmpty())
			{
				OMPClaim find =(OMPClaim) findByClaimKey.getResultList().get(0);
			
			if (find != null) {
				ProcessRejectionDTO convertClaimDto = OMPProcessRejectionMapper.getInstance()
						.getProcessRejectionDTO(find);
				convertClaimDto.setClaimedAmount(find.getClaimedAmount());
				convertClaimDto.setRejectionRemarks(find.getSuggestedRejectionRemarks());
				
				if(find.getIntimation().getHospital() != null){
				Hospitals hospObj = getHospitalDetailsByKey(find.getIntimation().getHospital());
				
				if(hospObj != null){
					convertClaimDto.setHospitalName(hospObj.getName());
					convertClaimDto.setHospitalCity(hospObj.getCity());
					convertClaimDto.setHospitalCounty(hospObj.getCountry());
				}
				}
				
				convertClaimDto.setIntimationNumber(find.getIntimation().getIntimationId());
				convertClaimDto.setIntimationKey(find.getIntimation().getKey());
				return convertClaimDto;
			}
			}
			return null;
		}
		public Boolean saveProcessRejection(ProcessRejectionDTO rejectionDto,
				Boolean submitDescion,String outCome,Status status) {

			OMPClaim find = getClaimByKey(rejectionDto.getKey());
			
//			Preauth preauth = getLatestPreauthByClaimKey(rejectionDto.getKey());
			
			String username = rejectionDto.getUsername();
			String userNameForDB = SHAUtils.getUserNameForDB(username);
			Stage stage=new Stage();
			stage.setKey(ReferenceTable.PROCESS_REJECTION_STAGE);

			if (find != null) {
				OMPClaim claim = OMPProcessRejectionMapper.getInstance()
						.getClaimForRejection(rejectionDto);
				claim.setClaimType(find.getClaimType());
				if (claim != null) {
					
					if (!submitDescion) {						
							find.setRejectionCategoryId(claim.getRejectionCategoryId());
							find.setRegistrationRemarks(claim.getRejectionRemarks());
							find.setMedicalRemarks(claim.getMedicalRemarks());
							find.setDoctorNote(claim.getDoctorNote());
							find.setStage(stage);
							find.setStatus(status);
							entityManager.merge(find);
							entityManager.flush();
					} else {
												
							find.setProvisionAmount(claim.getProvisionAmount());
							find.setWaiverRemarks(claim.getWaiverRemarks());
							find.setStage(stage);
							find.setStatus(status);
							entityManager.merge(find);
							entityManager.flush();
								
							OMPIntimation intimated = find.getIntimation();
							intimated.setRegistrationStatus("REGISTERED");
							entityManager.merge(intimated);
							entityManager.flush();
						}
					}
					
					setDBOutcomeForProcessRejection(outCome,rejectionDto,status);

					return true;
			}
			return false;
		}
		
//		private void documentUploadedFromDMS(SearchOMPProcessRejectionTableDTO preauthDTO,OMPClaim claim){
//			
//			try{
//				if(null != preauthDTO.getDocFilePath() && !("").equalsIgnoreCase(preauthDTO.getDocFilePath()))
//				{
//					HashMap dataMap = new HashMap();
//					dataMap.put("intimationNumber",claim.getIntimation().getIntimationId());
//					dataMap.put("claimNumber",claim.getClaimId());
//					
//					if(null != claim.getClaimType())
//					{
//						if((ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(claim.getClaimType().getKey()))
//							{
//								Preauth preauthObj = getLatestPreauthByClaimKey( claim.getKey());
//								if(null != preauthObj)
//									dataMap.put("cashlessNumber", preauthObj.getPreauthId());
//							}
//					}
//					
//					dataMap.put("filePath", preauthDTO.getDocFilePath());
//					dataMap.put("docType", preauthDTO.getDocType());
//					
//					dataMap.put("docSources",  "Process Rejection");
//					dataMap.put("createdBy", preauthDTO.getUsername());
//		
//					String docToken = SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);
//				}
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//				
//		  }
//			public Preauth getLatestPreauthByClaimKey(Long claimKey)
//			{
//				Query query = entityManager.createNamedQuery("Preauth.findLatestPreauthByClaim");
//				query.setParameter("claimkey", claimKey);
//				@SuppressWarnings("unchecked")
//				List<Preauth> singleResult = (List<Preauth>) query.getResultList();
//				if(singleResult != null && ! singleResult.isEmpty()) {
//					entityManager.refresh(singleResult.get(0));
//					Preauth preauth = singleResult.get(0);
//					for(int i=0; i <singleResult.size(); i++) {
//						entityManager.refresh(singleResult.get(i));
//						if(!ReferenceTable.getRejectedPreauthKeys().containsKey(singleResult.get(i).getStatus().getKey())) {
//							entityManager.refresh(singleResult.get(i));
//							preauth = singleResult.get(i);
//							break;
//						} 
//					}
//					return preauth;
//				}
//				
//				return null;
//				
//				
//			}
//		
//		@SuppressWarnings("unchecked")
//		public Preauth getPreauthListByIntimationKey(Long claimKey) {
//
//			Query findByKey = entityManager.createNamedQuery(
//					"Preauth.findByIntimationKey").setParameter("intimationKey",
//					claimKey);
//
//			List<Preauth> preauth = (List<Preauth>) findByKey.getResultList();
//
//			if ( ! preauth.isEmpty()) {
//				return preauth.get(0);
//			}
//			return null;
//		}
//		
//		@SuppressWarnings("unchecked")
//		public Preauth getPreauthListByKey(Long claimKey) {
//
//			Query findByKey = entityManager.createNamedQuery("Preauth.findByKey")
//					.setParameter("preauthKey", claimKey);
//
//			List<Preauth> preauthList = (List<Preauth>) findByKey.getResultList();
//
//			if (!preauthList.isEmpty()) {
//				return preauthList.get(0);
//
//			}
//			return null;
//		}
		
//		public void setBPMOutcomeForProcessRejection(SearchOMPProcessRejectionTableDTO resultTask,String outCome,ProcessRejectionDTO rejectionDto,Status status) {
//			if (true) {          
//				if (resultTask != null) {
//					//XMLElement payload = resultTask.getHumanTask().getPayload();
//					HumanTask humanTask = resultTask.getHumanTask();
//					PayloadBOType payloadCashless = humanTask.getPayloadCashless();
//					PedReqType pedReqType = null;
//					IntimationType intimationType = null;
//					Map<String, String> regIntDetailsReq = new HashMap<String, String>();
//					Map<String, String> preAuthReqMap = new HashMap<String, String>();
//					Map<String,String> pedReq=new HashMap<String, String>();
//					
//				try {
//					
//					if(resultTask.getKey()!=null){
//						if(null != payloadCashless)
//						{
//							if(null != payloadCashless.getPedReq())
//							{
//								pedReqType = payloadCashless.getPedReq();
//								pedReqType.setKey(resultTask.getKey());
//							}
//							else 
//							{
//								pedReqType = new PedReqType();
//								pedReqType.setKey(resultTask.getKey());
//							}
//						}
//						
//						pedReq.put("key", resultTask.getKey().toString());
//							
//					}
////						
//					if(null != rejectionDto.getIntimationNumber()){
//					//if(resultTask.getIntimationNo()!=null){
//						if(null != payloadCashless)
//						{
//							if(null != payloadCashless.getIntimation())
//							{
//								intimationType = payloadCashless.getIntimation();
//								intimationType.setIntimationNumber(rejectionDto.getIntimationNumber());
//							}
//							else
//							{
//								intimationType = new IntimationType();
//								intimationType.setIntimationNumber(rejectionDto.getIntimationNumber());
//							}
//						}
//						regIntDetailsReq.put("IntimationNumber",rejectionDto.getIntimationNumber());
//					}
//					
//					PreAuthReqType preAuthReq = payloadCashless.getPreAuthReq();
//					if(preAuthReq != null){
//						preAuthReq.setOutcome(outCome);
//						payloadCashless.setPreAuthReq(preAuthReq);
//					}
//					
//					if(outCome != null && outCome.equalsIgnoreCase("PREAUTHNOTRECEIVED")){
//						intimationType.setRejectionType("WAIVE");
//					}
//					
//					payloadCashless.setPedReq(pedReqType);
//					payloadCashless.setIntimation(intimationType);
//					//payload = BPMClientContext.setNodeValue(payload, "RegIntDetails", regIntDetailsReq);
//					//payload = BPMClientContext.setNodeValue(payload, "PedReq", pedReq);
//					
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				if(outCome != null && outCome.equalsIgnoreCase("PREAUTHNOTRECEIVED")){
//					humanTask.setOutcome("WAIVE");
//				}else{
//					humanTask.setOutcome(outCome);
//				}
//				
//				ClassificationType classification = payloadCashless.getClassification();
//				status = entityManager.find(Status.class, status.getKey());
//				classification.setSource(status.getProcessValue());
//				payloadCashless.setClassification(classification);
//				
//				humanTask.setPayloadCashless(payloadCashless);
//				SubmitProcessRejectionTask submitProcessRejectionTask = BPMClientContext.getRefractoredProcessRejectionTask(resultTask.getUsername(), resultTask.getPassword());
//				
//				try{
//				submitProcessRejectionTask.execute(resultTask.getUsername(), humanTask);
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//			   /* PedReq pedReqDetails=new PedReq();
//			    pedReqDetails.setKey(Long.valueOf(pedReq.get("key")));
//				resultTask.getHumanTask().setPayload(payload);*/
//						/*
//						try {
//							BPMClientContext.printPayloadElement(resultTask.getHumanTask().getPayload());
//						} catch (TransformerException e) {
//							e.printStackTrace();
//						}
//						InvokeHumanTask invokeHT=new InvokeHumanTask();
//						invokeHT.execute("weblogic", "Star@123", resultTask.getHumanTask(), null, null, pedReqDetails, null);*/
//						
//						System.out.println("BPM Executed Successfully");
//				}	
//			}
//				
//		}
		
		public void setDBOutcomeForProcessRejection(String outCome,ProcessRejectionDTO rejectionDto,Status status) {
		
			Map<String, Object> wrkFlowMap = (Map<String, Object>) rejectionDto.getDbOutArray();
			
			if (true) {          
				if (wrkFlowMap != null) {
					
				try {
					wrkFlowMap.put(SHAConstants.OUTCOME,outCome);
					
					if(SHAConstants.OUTCOME_REG_WAIVE_REJECTION.equalsIgnoreCase(outCome)){
						wrkFlowMap.put(SHAConstants.STAGE_SOURCE, SHAConstants.SOURCE_REJECTION_WAIVED);
					}

					Object[] objArrayForSubmit = SHAUtils.getObjArrayForSubmit(wrkFlowMap);					
					
					DBCalculationService dbCalService = new DBCalculationService();
					dbCalService.initiateTaskProcedure(objArrayForSubmit);
				}catch(Exception e){
					e.printStackTrace();
				}
						System.out.println("DB Submit Task Executed Successfully");
				}	
			}
				
		}

		@SuppressWarnings("unchecked")
		public List<OMPClaim> getClaimByIntimation(Long intimationKey) {
			List<OMPClaim> a_claimList = new ArrayList<OMPClaim>();
			if (intimationKey != null) {

				Query findByIntimationKey = entityManager.createNamedQuery("OMPClaim.findByOMPIntimationKey");
				findByIntimationKey.setParameter("intimationKey", intimationKey);
				findByIntimationKey.setParameter("lobId",ReferenceTable.OMP_LOB_KEY);
				try {

					a_claimList = (List<OMPClaim>) findByIntimationKey.getResultList();
					
					for (OMPClaim claim : a_claimList) {
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
		
//		public BeanItemContainer<SelectValue> getRelpseClaimsForPolicy(String policyNumber,String claimNumber){
//			BeanItemContainer<SelectValue> relapseClaimContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
//			
//			if(policyNumber != null){
//			try {
//				   List<Claim> resultList = getClaimsByPolicyNumber(policyNumber);
//				   List<Claim> previousClaimsList = new ArrayList<Claim>();
//				   Claim currentClaim = null; 
//				   if(!resultList.isEmpty()){
//					   for(Claim claim :resultList){
//						   if(claim.getClaimId() == claimNumber){
//							   currentClaim = claim;
//						   } else {
//							   previousClaimsList.add(claim);
//						   }
//					   } 
//					   	  Date currentClaimAdmissionDate = currentClaim.getIntimation().getAdmissionDate();
//						  for(Claim claim :previousClaimsList) {
//							   Date previousClaimAdmissionDate = claim.getIntimation().getAdmissionDate();
//							   long diff = currentClaimAdmissionDate.getTime() - previousClaimAdmissionDate.getTime();
//						   
//						   int diffDays = (int) (diff/(24*60*60*1000));
//						   if(diffDays <= 45) {
//							   SelectValue selectClaim = new SelectValue();
//							   selectClaim.setId(claim.getKey());
//							   selectClaim.setValue(claim.getClaimId());
//							   relapseClaimContainer.addBean(selectClaim);
//						    }
//						   
//						   }   
//					   }
//					
//			}
//			catch(Exception e){
//				e.printStackTrace();
//			}
//
//			}
//			return relapseClaimContainer;
//
//		}
//		
//		public List<Claim> getPreviousClaimsforPolicy(String policyNumber) {
//			List<Claim> claimResult = null;
//			if (null != policyNumber && "" != policyNumber) {
//				policyNumber = "%"
//						+ StringUtils.trim(policyNumber) + "%";
//				try {
//					final CriteriaBuilder builder = entityManager
//							.getCriteriaBuilder();
	//
//					final CriteriaQuery<Intimation> intimationCriteriaQuery = builder
//							.createQuery(Intimation.class);
//					final CriteriaQuery<Policy> policyCriteriaQuery = builder
//							.createQuery(Policy.class);
//					final CriteriaQuery<Claim> claimCriteriaQuery = builder
//							.createQuery(Claim.class);
	//
//					Root<Intimation> intimationRoot = intimationCriteriaQuery
//							.from(Intimation.class);
//					 
//					Root<Claim> claimRoot = claimCriteriaQuery.from(Claim.class);
//				
//					Join<Claim, Intimation> intimationJoin = claimRoot.join(
//							"intimation", JoinType.INNER);
	//
//					Join<Intimation, Policy> policyJoin = intimationRoot.join(
//							"policy", JoinType.INNER);
//					
//					List<Predicate> predicates = new ArrayList<Predicate>();
	//
//					Predicate intimationNotNullPredicate = builder
//							.isNotNull(claimRoot.<Intimation> get("intimation"));
//					
//					predicates.add(intimationNotNullPredicate);
	//
//					if (!ValidatorUtils.isNull(policyNumber)) {
//						Predicate policyNumberPredicate = builder
//								.like(policyJoin
//										.<String> get("policyNumber"), policyNumber);
//						predicates.add(policyNumberPredicate);
//					}
//					claimCriteriaQuery.select(claimRoot).where(
//							builder.and(predicates.toArray(new Predicate[] {})));
	//
//					final TypedQuery<Claim> query = entityManager
//							.createQuery(claimCriteriaQuery);
//					claimResult = query.getResultList();
	//
//					
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			} else {
//				// empty parameters
//			}
//			return claimResult;
//		}
//		
//		public ClaimLimit getClaimLimitByKey(Long key) {
//			ClaimLimit find = entityManager.find(ClaimLimit.class, key);
//			entityManager.refresh(find); 
//			return find;
//		}
		
		public ClaimDto claimToClaimDTO(OMPClaim claim) {
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
		
//		public ClaimDto claimToClaimDTO(ViewTmpClaim claim) {
//			ClaimDto claimDto = null;
//			if(claim!=null){
//				claimDto = new ClaimDto();
//				claimDto.setKey(claim.getKey());
//				claimDto.setClaimId(claim.getClaimId());
//				claimDto.setCreatedDate(claim.getCreatedDate());
//				claimDto.setClaimedAmount(claim.getClaimedAmount());
//				claimDto.setProvisionAmount(claim.getProvisionAmount());
//				claimDto.setClaimType(getSelectValue(claim.getClaimType()));
//				claimDto.setClaimStatus(claim.getStatus().getProcessValue());
//				claimDto.setCurrencyId(getSelectValue(claim.getCurrencyId()));
//
//				NewIntimationDto intimationDto = getIntimationService().getIntimationDto(claim.getIntimation(), entityManager);
//				claimDto.setNewIntimationDto(intimationDto);			
//			}
//			return claimDto;
//		}
		
		private SelectValue getSelectValue(MastersValue masValue)
		{
			SelectValue selValue = new SelectValue();
			if(masValue != null) {
				selValue.setId(masValue.getKey());
				selValue.setValue(masValue.getValue());
			}
			
			return selValue;
		}
		
		public List<OMPClaim> getPreviousClaimsByPolicyNumber(String policyNumber) {
			Query query = entityManager.createNamedQuery("OMPClaim.findByPolicyNumber");
			query.setParameter("policyNumber", policyNumber);
			query.setParameter("lobId",ReferenceTable.OMP_LOB_KEY);
			@SuppressWarnings("unchecked")
			List<OMPClaim> singleResult =  query.getResultList();
			return singleResult;
			
		}
		
		private OMPIntimationService getIntimationService()
		{
			if(null == intimationService)
			{
				intimationService = new OMPIntimationService();
			}
			
			return intimationService;
		}
		
//		public List<ClaimsDailyReportDto> getClaimsDailyReport(Map<String,Object> filters){
//			List<ClaimsDailyReportDto> resultDto = new ArrayList<ClaimsDailyReportDto>();
//			
//			Date fromDate = null;
//			Date endDate = null;
//			if(filters != null && !filters.isEmpty()){
//				
//				if(filters.containsKey("fromDate") && filters.get("fromDate") != null){
//					
//					fromDate = (Date)filters.get("fromDate");
//				}
//				
//				if(filters.containsKey("endDate") && filters.get("endDate") != null){
//					
//					endDate = (Date)filters.get("endDate");
//				}
//					
//			List<Claim> resultClaimList = new ArrayList<Claim>();
//			
//			try{
//			
//				if (fromDate != null && endDate != null) {			
//				final CriteriaBuilder builder = entityManager
//						.getCriteriaBuilder();
//				final CriteriaQuery<Claim> criteriaClaimQuery = builder
//						.createQuery(Claim.class);
//
//				Root<Claim> claimRoot = criteriaClaimQuery.from(Claim.class);
//				Join<Claim,Intimation> intimationJoin = claimRoot.join(
//						"intimation", JoinType.INNER);
//				List<Predicate> predicates = new ArrayList<Predicate>();
//				
//				
//					Expression<Date> fromDateExpression = claimRoot
//							.<Date> get("createdDate");
//					Predicate fromDatePredicate = builder
//							.greaterThanOrEqualTo(fromDateExpression,
//									fromDate);
//					predicates.add(fromDatePredicate);
//
//					Expression<Date> toDateExpression = claimRoot
//							.<Date> get("createdDate");
//					Calendar c = Calendar.getInstance();
//					c.setTime(endDate);
//					c.add(Calendar.DATE, 1);
//					endDate = c.getTime();
//					Predicate toDatePredicate = builder
//							.lessThanOrEqualTo(toDateExpression, endDate);
//					predicates.add(toDatePredicate);
//				
//				criteriaClaimQuery.select(claimRoot).where(
//						builder.and(predicates
//								.toArray(new Predicate[] {})));
//
//				final TypedQuery<Claim> claimQuery = entityManager
//						.createQuery(criteriaClaimQuery);
//			
//				resultClaimList = claimQuery.getResultList();
//			
//			if(resultClaimList != null && !resultClaimList.isEmpty()){
//				
//				for(Claim claim : resultClaimList){
//					entityManager.refresh(claim);
//					ClaimMapper clmMapper =  ClaimMapper.getInstance();
//					ClaimDto claimDto = clmMapper.getClaimDto(claim);
//					NewIntimationDto intimationDto = getIntimationService().getIntimationDto(claim.getIntimation(), entityManager);
//					claimDto.setNewIntimationDto(intimationDto);
//					
//					ClaimsDailyReportDto clmDailyReportDto = new ClaimsDailyReportDto(claimDto);
//					clmDailyReportDto.setSno(resultClaimList.indexOf(claim)+1);
//					DBCalculationService dbcalService = new DBCalculationService();
//					Double sumInsured = dbcalService.getInsuredSumInsured(String.valueOf(claimDto.getNewIntimationDto().getInsuredPatient().getInsuredId()), claimDto.getNewIntimationDto().getPolicy().getKey());
//					clmDailyReportDto.setSuminsured(String.valueOf(sumInsured.intValue()));
//					
//					Query fvrQuery = entityManager.createNamedQuery("FieldVisitRequest.findByClaimKey");
//					fvrQuery.setParameter("claimKey", claimDto.getKey());
//					List<FieldVisitRequest> fvrReqList = (List<FieldVisitRequest>)fvrQuery.getResultList();
//					
//					if(fvrReqList != null && !fvrReqList.isEmpty()){
//						FieldVisitRequest fvrReq = fvrReqList.get(fvrReqList.size()-1);
//						entityManager.refresh(fvrReq);
//						clmDailyReportDto.setFieldDoctorNameAllocated(fvrReq.getRepresentativeName() != null ? fvrReq.getRepresentativeName() : "");
//				
//						if(fvrReq.getRepresentativeCode() != null){
//							Query q = entityManager.createNamedQuery("TmpFvR.findByCode");
//							q.setParameter("code", fvrReq.getRepresentativeCode());
//							List<TmpFvR> fvrList = q.getResultList();
//							if(fvrList != null && !fvrList.isEmpty()){
//								entityManager.refresh(fvrList.get(0));
//								String contactNo = "";
//								if(fvrList.get(0).getMobileNumber() != null){
//									contactNo = fvrList.get(0).getMobileNumber() != null ? fvrList.get(0).getMobileNumber().toString() : "";
//								}
//								if(fvrList.get(0).getPhoneNumber() != null){
//									contactNo = ("").equalsIgnoreCase(contactNo) ? ( fvrList.get(0).getPhoneNumber().toString()) : ( contactNo + " / " + fvrList.get(0).getPhoneNumber().toString());
//								}
//								clmDailyReportDto.setContactNumOfDoctor(contactNo);
//							}
//						}
//						
//						SimpleDateFormat dtFrmt = new SimpleDateFormat("MMM-dd-yyyy HH:mm:ss");
//						clmDailyReportDto.setDataOfAllocationWithTime(fvrReq.getAssignedDate() != null ? dtFrmt.format(fvrReq.getAssignedDate()).toUpperCase():"");
//					}				
//					
//					resultDto.add(clmDailyReportDto);
//				}
//				
//			}
//			}
//			
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//			
//		  }	
//			return resultDto;
//		}

//		@SuppressWarnings({ "deprecation", "unchecked" })
//		public List<ClaimsStatusReportDto> getClaimsStatusReport(
//				Map<String, Object> filters) {
//			List<ClaimsStatusReportDto> resultDtoList = new ArrayList<ClaimsStatusReportDto>();
//			List<ClaimsStatusReportDto> paidResultDtoList = new ArrayList<ClaimsStatusReportDto>();
//			Date fromDate = null;
//			Date endDate = null;
//			String stageName = null;
//			Long cpuKey = null;
//			if (filters != null && !filters.isEmpty()) {
//
//				if (filters.containsKey("fromDate")
//						&& filters.get("fromDate") != null) {
//
//					fromDate = (Date) filters.get("fromDate");
//				}
//
//				if (filters.containsKey("endDate")
//						&& filters.get("endDate") != null) {
//
//					endDate = (Date) filters.get("endDate");
//				}
//
//				if (filters.containsKey("claimStageName")
//						&& filters.get("claimStageName") != null) {
//
//					stageName = (String) filters.get("claimStageName");
//					
//				}
//
//				if (filters.containsKey("cpuKey") && filters.get("cpuKey") != null) {
//
//					cpuKey = (Long) filters.get("cpuKey");
//				}
//
//				List<Reimbursement> resultReimbursementList = new ArrayList<Reimbursement>();
//				List<Claim> resultClaimList = new ArrayList<Claim>();
//				List<Preauth> resultPreauthList = new ArrayList<Preauth>();
//				List<StageInformation> stageInfoList = new ArrayList<StageInformation>();
//				
//				try {				
//
//					final CriteriaBuilder builder = entityManager
//							.getCriteriaBuilder();
//					
//					final CriteriaQuery<StageInformation> criteriaStageInfoQuery = builder
//							.createQuery(StageInformation.class);
//					
//					
////					final CriteriaQuery<Reimbursement> criteriaReimbQuery = builder
////							.createQuery(Reimbursement.class);
////					final CriteriaQuery<Preauth> criteriaCashlessQuery = builder
////							.createQuery(Preauth.class);				
////					final CriteriaQuery<Claim> criteriaClaimQuery = builder
////							.createQuery(Claim.class);
//					
//					
//					Root<StageInformation> stageInfoRoot = criteriaStageInfoQuery.from(StageInformation.class);
//
////					Root<Preauth> preauthRoot = criteriaCashlessQuery.from(Preauth.class);
////					Root<Claim> claimRoot = criteriaClaimQuery.from(Claim.class);
//					
//					
//					
////					Join<Claim,Intimation> intimationJoin = claimRoot.join(
////							"intimation", JoinType.INNER);				
////					
////					Root<Reimbursement> reimbRoot = criteriaReimbQuery.from(Reimbursement.class);
////					Join<Reimbursement,Claim> claimJoin = reimbRoot.join(
////							"claim", JoinType.INNER);
//					List<Predicate> predicates = new ArrayList<Predicate>();
//
//					if (stageName != null) {
//						
//						if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLOSED_CLAIMS)){
//							
//							Predicate rodStagePred = builder.equal(stageInfoRoot.<Stage>get("stage").<Long>get("key"),ReferenceTable.CREATE_ROD_STAGE_KEY);
//							Predicate rodStatusPred = builder.equal(stageInfoRoot.<Status>get("status").<Long>get("key"), ReferenceTable.CREATE_ROD_CLOSED);
//							
//							Predicate rodClosedPred = builder.and(rodStagePred,rodStatusPred);
//						
//							Predicate billEntryStagePred = builder.equal(stageInfoRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.BILL_ENTRY_STAGE_KEY);
//							Predicate billEntryStatusPred = builder.equal(stageInfoRoot.<Status>get("status").<Long>get("key"), ReferenceTable.BILL_ENTRY_PROCESS_CLOSED_STATUS);
//						
//							Predicate billingClosedPred = builder.and(billEntryStagePred,billEntryStatusPred);
//							
//							Predicate zonalReviewStagePred = builder.equal(stageInfoRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.PROCESS_CLAIM_REQUEST_ZONAL_STAGE_KEY);
//							Predicate zonalReviewStatusPred = builder.equal(stageInfoRoot.<Status>get("status").<Long>get("key"), ReferenceTable.ZONAL_REVIEW_CLOSED);
//							
//							Predicate zonalreviewClosedPed = builder.and(zonalReviewStagePred,zonalReviewStatusPred);
//							
//							Predicate processClaimStagePred = builder.equal(stageInfoRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
//							Predicate processClaimStatusPred = builder.equal(stageInfoRoot.<Status>get("status").<Long>get("key"), ReferenceTable.CLAIM_REQUEST_CLOSED);
//							
//							Predicate processClaimReqClosedPred = builder.and(processClaimStagePred,processClaimStatusPred); 
//							
//							Predicate claimBillingStagePred = builder.equal(stageInfoRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.BILLING_STAGE);
//							Predicate claimBillingStatusPred = builder.equal(stageInfoRoot.<Status>get("status").<Long>get("key"), ReferenceTable.BILLING_CLOSED_STATUS);
//
//							Predicate claimBillingClosedPred = builder.and(claimBillingStagePred,claimBillingStatusPred);
//												
//							Predicate finStagePred = builder.equal(stageInfoRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.FINANCIAL_STAGE);
//							Predicate finStatusPred = builder.equal(stageInfoRoot.<Status>get("status").<Long>get("key"), ReferenceTable.FINANCIAL_CLOSED);
//
//							Predicate finanClosedPred = builder.and(finStagePred,finStatusPred);
//							
//							Predicate claimClosedPred = builder.or(rodClosedPred,billingClosedPred,zonalreviewClosedPed,processClaimReqClosedPred,claimBillingClosedPred,finanClosedPred);
//							
//							predicates.add(claimClosedPred);
//							
//						}
//						else if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.REJECTED_CLAIMS)){
//					
//							Predicate finRejectStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"), ReferenceTable.FINANCIAL_STAGE);
//							
////							List<Long> financialRejKeyList = new ArrayList<Long>();
//							
////							financialRejKeyList.add(ReferenceTable.FINANCIAL_REJECT_STATUS);
////							financialRejKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS);
////							financialRejKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_DISAPPROVE_REJECT_STATUS);
////							financialRejKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_REDRAFT_REJECT_STATUS);
//							
//							Predicate finRejectStatusPredicate = builder.equal(stageInfoRoot.<Status> get("status").<Long> get("key"),ReferenceTable.FINANCIAL_REJECT_STATUS);
//							
//							Predicate financialRejectpred = builder.and(finRejectStagePredicate,finRejectStatusPredicate);
//							
//							Predicate medRejectStagePredicate = builder.equal( stageInfoRoot.<Stage> get("stage").<Long> get("key"), ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
//							
////							List<Long> medicalRejKeyList = new ArrayList<Long>();
////							medicalRejKeyList.add(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS);
////							medicalRejKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS);
////							medicalRejKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_DISAPPROVE_REJECT_STATUS);
////							medicalRejKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_REDRAFT_REJECT_STATUS);
//							
//							Predicate medRejectStatusPredicate = builder.equal( stageInfoRoot.<Status> get("status").<Long> get("key"),ReferenceTable.CLAIM_REQUEST_REJECT_STATUS);
//							
//							Predicate  medRejectPredicate = builder.and(medRejectStagePredicate,medRejectStatusPredicate);
//							
//							Predicate claimRejectPred = builder.or(medRejectPredicate,financialRejectpred);
//							
//							predicates.add(claimRejectPred);
//							
//						}
//						else if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLAIMS_PAID_STAUS)){
//							
//							Predicate finApproveStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"), ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY);
//							Predicate finApproveStatusPredicate = builder.equal(stageInfoRoot.<Status> get("status").<Long> get("key"), ReferenceTable.FINANCIAL_APPROVE_STATUS);
//							Predicate approvePred = builder.and(finApproveStagePredicate,finApproveStatusPredicate); 
//							predicates.add(approvePred);
//						}
//						else if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLAIMS_BILL_RECEIVED_STATUS)){       
//							
////							List<Long> rodStageKey = new ArrayList<Long>();
////							rodStageKey.add(ReferenceTable.ACKNOWLEDGE_STAGE_KEY);					ONlY  ACK to be handled in case of bills received status.
////							rodStageKey.add(ReferenceTable.CREATE_ROD_STAGE_KEY);
////							rodStageKey.add(ReferenceTable.BILL_ENTRY_STAGE_KEY);
////							rodStageKey.add(ReferenceTable.PROCESS_CLAIM_REQUEST_ZONAL_STAGE_KEY);
////							
////							Predicate rodStagePredicate = claimRoot.<Stage> get("stage").<Long> get("key").in(rodStageKey);						
//							
//							List<Long> rodStageKey = new ArrayList<Long>();
//							rodStageKey.add(ReferenceTable.CREATE_ROD_STAGE_KEY);
//							rodStageKey.add(ReferenceTable.BILL_ENTRY_STAGE_KEY);
//							rodStageKey.add(ReferenceTable.PROCESS_CLAIM_REQUEST_ZONAL_STAGE_KEY);
//							Predicate rodStagePredicate = stageInfoRoot.<Stage> get("stage").<Long> get("key").in(rodStageKey);
//							
//							predicates.add(rodStagePredicate);
//						}
//						else if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLAIMS_PRE_AUTH_STATUS)){
//							
//							
////							preauthKeyList.add(ReferenceTable.PRE_MEDICAL_PRE_AUTH_STAGE);
//							
//							Predicate preAuthPremedicalStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"), ReferenceTable.PRE_MEDICAL_PRE_AUTH_STAGE);
//					
//							List<Long> preauthPremedicalstatusKeyList = new ArrayList<Long>();
//							preauthPremedicalstatusKeyList.add(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS);
//							preauthPremedicalstatusKeyList.add(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS);
//							preauthPremedicalstatusKeyList.add(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS);
//							
//							Predicate preAuthPremedicalStatusPredicate = stageInfoRoot.<Status> get("status").<Long> get("key").in(preauthPremedicalstatusKeyList);
//							
//							Predicate preAuthPremedicalPredicate = builder.and(preAuthPremedicalStagePredicate,preAuthPremedicalStatusPredicate);
//							
//							
////							preauthKeyList.add(ReferenceTable.PREAUTH_STAGE);
//							Predicate preAuthStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"),ReferenceTable.PREAUTH_STAGE);
//							
//							List<Long> preauthstatusKeyList = new ArrayList<Long>();
//							preauthstatusKeyList.add(ReferenceTable.PREAUTH_APPROVE_STATUS);
//							preauthstatusKeyList.add(ReferenceTable.PREAUTH_REJECT_STATUS);
//							preauthstatusKeyList.add(ReferenceTable.PREAUTH_QUERY_STATUS);
//							preauthstatusKeyList.add(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS);
//							
//							Predicate preAuthStatusPredicate = stageInfoRoot.<Status> get("status").<Long> get("key").in(preauthstatusKeyList);
//							
//							Predicate preAuthPredicate = builder.and(preAuthStagePredicate,preAuthStatusPredicate);
//							
//							
////							preauthKeyList.add(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_STAGE);
//							Predicate preMedicalEnhStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"),ReferenceTable.PRE_MEDICAL_ENHANCEMENT_STAGE);
//							
//							List<Long> preMedicalEnhstatusKeyList = new ArrayList<Long>();
//							preMedicalEnhstatusKeyList.add(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS);
//							preMedicalEnhstatusKeyList.add(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SUGGEST_REJECTION_STATUS);
//							preMedicalEnhstatusKeyList.add(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SEND_FOR_PROCESSING_STATUS);
//							
//							Predicate preMedicalEnhStatusPredicate = stageInfoRoot.<Status> get("status").<Long> get("key").in(preMedicalEnhstatusKeyList);
//							
//							Predicate preMedicalEnhPredicate = builder.and(preMedicalEnhStagePredicate,preMedicalEnhStatusPredicate);
//							
//							
////							preauthKeyList.add(ReferenceTable.ENHANCEMENT_STAGE);
//							Predicate enhStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"),ReferenceTable.ENHANCEMENT_STAGE);
//							
//							List<Long> enhstatusKeyList = new ArrayList<Long>();
//							enhstatusKeyList.add(ReferenceTable.ENHANCEMENT_APPROVE_STATUS);
//							enhstatusKeyList.add(ReferenceTable.ENHANCEMENT_REJECT_STATUS);
//							enhstatusKeyList.add(ReferenceTable.ENHANCEMENT_QUERY_STATUS);
//							enhstatusKeyList.add(ReferenceTable.ENHANCEMENT_DENIAL_OF_CASHLESS_STATUS);
//							enhstatusKeyList.add(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS);
//							enhstatusKeyList.add(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS);
//							
//							Predicate enhStatusPredicate = stageInfoRoot.<Status> get("status").<Long> get("key").in(enhstatusKeyList);
//							
//							Predicate enhPredicate = builder.and(enhStagePredicate,enhStatusPredicate);
//							
//							
////							preauthKeyList.add(ReferenceTable.PROCESS_REJECTION_STAGE);
//							Predicate processRejectionStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"),ReferenceTable.PROCESS_REJECTION_STAGE);
//							
//							List<Long> processRejectionstatusKeyList = new ArrayList<Long>();
//							processRejectionstatusKeyList.add(ReferenceTable.PROCESS_REJECTED);
//							processRejectionstatusKeyList.add(ReferenceTable.PREMEDICAL_WAIVED_REJECTION);
//							
//							Predicate processRejectionStatusPredicate = stageInfoRoot.<Status> get("status").<Long> get("key").in(processRejectionstatusKeyList);
//							
//							Predicate processRejectionPredicate = builder.and(processRejectionStagePredicate,processRejectionStatusPredicate);	
//									
//									
//									
////							preauthKeyList.add(ReferenceTable.DOWNSIZE_STAGE);
//							Predicate downsizeStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"),ReferenceTable.DOWNSIZE_STAGE);
//							
//							List<Long> downsizestatusKeyList = new ArrayList<Long>();
//							downsizestatusKeyList.add(ReferenceTable.DOWNSIZE_APPROVED_STATUS);
//							
//							Predicate downsizeStatusPredicate = stageInfoRoot.<Status> get("status").<Long> get("key").in(downsizestatusKeyList);
//							
//							Predicate downsizePredicate = builder.and(downsizeStagePredicate,downsizeStatusPredicate);	
//							
//							
////							preauthKeyList.add(ReferenceTable.WITHDRAW_STAGE);
//							Predicate withDrawStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"),ReferenceTable.WITHDRAW_STAGE);
//							
//							List<Long> withDrawstatusKeyList = new ArrayList<Long>();
//							withDrawstatusKeyList.add(ReferenceTable.STANDALONE_WITHDRAW_STATUS);
//							
//							Predicate withDrawStatusPredicate = stageInfoRoot.<Status> get("status").<Long> get("key").in(withDrawstatusKeyList);
//							
//							Predicate withDrawPredicate = builder.and(withDrawStagePredicate,withDrawStatusPredicate);	
//							
//									
//							Predicate cashlessPredicate = builder.or(preAuthPremedicalPredicate,preAuthPredicate,preMedicalEnhPredicate,enhPredicate,processRejectionPredicate,downsizePredicate,withDrawPredicate);
//							predicates.add(cashlessPredicate); 
//						}
//						else if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.BILLING_COMPLETED)){
//							Predicate billingStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"), ReferenceTable.BILLING_STAGE);
//							Predicate billingStatusPredicate = builder.equal(stageInfoRoot.<Status> get("status").<Long> get("key"), ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER);
//							
//							Predicate billingApprovedPred = builder.and(billingStagePredicate,billingStatusPredicate);
//							predicates.add(billingApprovedPred);
//						}
//						else if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.MEDICAL_APPROVAL)){
//							Predicate medicalApprovStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"),ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
//							Predicate medicalApprovStatusPredicate = builder.equal(stageInfoRoot.<Status> get("status").<Long> get("key"), ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS);
//
//							Predicate medicalApprovedPred = builder.and(medicalApprovStagePredicate,medicalApprovStatusPredicate);
//							predicates.add(medicalApprovedPred);
//						}
//						else if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLAIM_QUERY)){
//							
//							Predicate medicalQueryStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"),ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
//							
//							List<Long> queryStatusKeyList = new ArrayList<Long>();
//							queryStatusKeyList.add(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
////							queryStatusKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS);
////							queryStatusKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REDRAFT_STATUS);
////							queryStatusKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS);
//							
//							Predicate medicalQueryStatusPredicate = stageInfoRoot.<Status> get("status").<Long> get("key").in(queryStatusKeyList);
//							
//							Predicate clmMedicalQueryPred = builder.and(medicalQueryStagePredicate,medicalQueryStatusPredicate);						
//				
//							
//							Predicate financialQueryStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"),ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY);
//							List<Long> finQuerystatusKeyList = new ArrayList<Long>();
//							
//							finQuerystatusKeyList.add(ReferenceTable.FINANCIAL_QUERY_STATUS);
////							finQuerystatusKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS);
////							finQuerystatusKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REDRAFT_STATUS);
////							finQuerystatusKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REJECT_STATUS);
//							
//							Predicate financialQueryStatusPredicate = stageInfoRoot.<Status> get("status").<Long> get("key").in(finQuerystatusKeyList);
//
//							Predicate financialQueryPred = builder.and(financialQueryStagePredicate,financialQueryStatusPredicate);
//							
//							
//							Predicate claimQueryPred = builder.or(clmMedicalQueryPred,financialQueryPred);
//							
//							
//							predicates.add(claimQueryPred);
//							
//						}
//					}
//
//					if(fromDate != null && endDate != null){
////						if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLAIMS_BILL_RECEIVED_STATUS)){      ONlY  ACK to be handled in case of bills received status.
//	//
////							Expression<Date> fromDateExpression = claimRoot
////									.<Date> get("modifiedDate");
////							Predicate fromDatePredicate = builder
////									.greaterThanOrEqualTo(fromDateExpression,
////											fromDate);
////							predicates.add(fromDatePredicate);
//	//
////							Expression<Date> toDateExpression = claimRoot
////									.<Date> get("modifiedDate");
////							Calendar c = Calendar.getInstance();
////							c.setTime(endDate);
////							c.add(Calendar.DATE, 1);
////							endDate = c.getTime();					
////							Predicate toDatePredicate = builder
////									.lessThanOrEqualTo(toDateExpression, endDate);
////							predicates.add(toDatePredicate);						
////						}else
////						if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLAIMS_PRE_AUTH_STATUS)){
////							Expression<Date> fromDateExpression = stageInfoRoot
////									.<Date> get("createdDate");
////							Predicate fromDatePredicate = builder
////									.greaterThanOrEqualTo(fromDateExpression,
////											fromDate);
////							predicates.add(fromDatePredicate);
//	//
////							Expression<Date> toDateExpression = stageInfoRoot
////									.<Date> get("createdDate");
////							Calendar c = Calendar.getInstance();
////							c.setTime(endDate);
////							c.add(Calendar.DATE, 1);
////							endDate = c.getTime();					
////							Predicate toDatePredicate = builder
////									.lessThanOrEqualTo(toDateExpression, endDate);
////							predicates.add(toDatePredicate);
////						}
////						
////						else{
//							Expression<Date> fromDateExpression = stageInfoRoot
//									.<Date> get("createdDate");
//							Predicate fromDatePredicate = builder
//									.greaterThanOrEqualTo(fromDateExpression,
//											fromDate);
//							predicates.add(fromDatePredicate);
//
//							Expression<Date> toDateExpression = stageInfoRoot
//									.<Date> get("createdDate");
//							Calendar c = Calendar.getInstance();
//							c.setTime(endDate);
//							c.add(Calendar.DATE, 1);
//							endDate = c.getTime();					
//							Predicate toDatePredicate = builder
//									.lessThanOrEqualTo(toDateExpression, endDate);
//							predicates.add(toDatePredicate);
////						}					
//					}
//					
//					
//					
//						
////						if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLAIMS_BILL_RECEIVED_STATUS)){           ONlY  ACK to be handled in case of bills received status.
////							
////							Predicate clmCpuPredicate = builder.equal(claimRoot.<Intimation> get("intimation").<TmpCPUCode> get("cpuCode")
////														.<Long> get("key"), cpuKey);
////							predicates.add(clmCpuPredicate);
////						}
////						else{
////						Predicate reimbCpuPredicate = builder.equal(
////								stageInfoRoot.<Claim>get("claim").<Intimation> get("intimation")
////										.<TmpCPUCode> get("cpuCode")
////										.<Long> get("key"), cpuKey);
////						predicates.add(reimbCpuPredicate);
////						}				
//
////					if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLAIMS_PRE_AUTH_STATUS)){
//////						criteriaCashlessQuery.select(preauthRoot).where(
//////								builder.and(predicates.toArray(new Predicate[] {})));
////						
//////						final TypedQuery<Preauth> finalpreauthQuery = entityManager
//////								.createQuery(criteriaCashlessQuery);
//////						resultPreauthList = finalpreauthQuery.getResultList();
////						
////						
////						criteriaStageInfoQuery.select(stageInfoRoot).where(
////								builder.and(predicates.toArray(new Predicate[] {})));
////						
////						final TypedQuery<StageInformation> finalpreauthQuery = entityManager
////								.createQuery(criteriaStageInfoQuery);
////											
////						stageInfoList = finalpreauthQuery.getResultList();
////						
////						if(stageInfoList != null && !stageInfoList.isEmpty()){
////							
////							for(StageInformation stageInfo : stageInfoList){
////								resultClaimList.add(stageInfo.getClaim());	
////							}
////						}					
////					}
//					
////					if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLAIMS_BILL_RECEIVED_STATUS)){     ONlY  ACK to be handled in case of bills received status.
////						criteriaClaimQuery.select(claimRoot).where(
////								builder.and(predicates.toArray(new Predicate[] {})));
////						
////						final TypedQuery<Claim> finalClaimQuery = entityManager
////								.createQuery(criteriaClaimQuery);
////						resultClaimList = finalClaimQuery.getResultList();
////					}
////					else{
////					}
//					
//					
//						criteriaStageInfoQuery.select(stageInfoRoot).where(
//								builder.and(predicates.toArray(new Predicate[] {})));	
//						final TypedQuery<StageInformation> reimbQuery = entityManager
//								.createQuery(criteriaStageInfoQuery);
//						stageInfoList = reimbQuery.getResultList();
//
//									
//
////					ArrayList<Reimbursement> tempResultReimbursementList = null; 
////					if (resultClaimList != null && !resultClaimList.isEmpty()){
////						tempResultReimbursementList= new ArrayList<Reimbursement>();
////						for(Claim claimObj : resultClaimList){
////							Reimbursement reimbObj = new Reimbursement();
////							List<Reimbursement> reimbObjList = (new ReimbursementService()).getReimbursementByClaimKey(claimObj.getKey(), entityManager);
////							if(reimbObjList != null && reimbObjList.size() > 0 ){							
////								Reimbursement reimb = reimbObjList.get(0);
////								entityManager.refresh(reimb);
////								reimbObj = reimb;
////							}
////							reimbObj.setClaim(claimObj);
////							tempResultReimbursementList.add(reimbObj);
////						}					
////					}			
//	//
////					if(stageInfoList == null || stageInfoList.isEmpty()){     
////						stageInfoList= new ArrayList<StageInformation>();
////						if(tempResultReimbursementList != null && !tempResultReimbursementList.isEmpty()){
////							stageInfoList.addAll(tempResultReimbursementList);
////						}
////					}
////					else{
////						if(tempResultReimbursementList != null && !tempResultReimbursementList.isEmpty()){
////							resultReimbursementList.addAll(tempResultReimbursementList);
////						}	
////					}				
//						List<StageInformation> finalStageList = new ArrayList<StageInformation>();
//						
//					if (stageInfoList != null && !stageInfoList.isEmpty()) {					
//						
//						if (cpuKey != null) {						
//							for(StageInformation stageInfoObj :  stageInfoList){
//								entityManager.refresh(stageInfoObj);
//								Reimbursement reimbObj = (new ReimbursementService()).getReimbursementbyRod(stageInfoObj.getReimbursement().getKey(), entityManager);
//								entityManager.refresh(reimbObj);
//								Claim claim = stageInfoObj.getClaim();
//								entityManager.refresh(claim);
//								if(claim.getIntimation().getCpuCode().getKey().equals(cpuKey)){
//									finalStageList.add(stageInfoObj);
//								}
//							}
//						}
//						else{
//							finalStageList.addAll(stageInfoList);	
//						}
//						
//						
//						
//						for(StageInformation stageInfo :  finalStageList){
//							
//							entityManager.refresh(stageInfo);
//							Reimbursement reimbObj = null;
//							if(stageInfo.getReimbursement().getKey() != null){
//								reimbObj = (new ReimbursementService()).getReimbursementbyRod(stageInfo.getReimbursement().getKey(), entityManager);
//								entityManager.refresh(reimbObj);	
//							}
//							
//							Claim claim = stageInfo.getClaim();
//							entityManager.refresh(claim);
//							ClaimMapper clmMapper =  ClaimMapper.getInstance();
//							ClaimDto claimDto = clmMapper.getClaimDto(claim);
//							NewIntimationDto intimationDto = getIntimationService()
//									.getIntimationDto(claim.getIntimation(),
//											entityManager);
//							claimDto.setNewIntimationDto(intimationDto);
//							
//							ClaimsStatusReportDto clmsStatusReportDto = new ClaimsStatusReportDto(
//									claimDto);
//							
//							clmsStatusReportDto.setReimbursementKey(stageInfo.getReimbursement().getKey());
//							clmsStatusReportDto.setSno(finalStageList
//									.indexOf(stageInfo) + 1);
//							String finyear="";
//							if(claim.getIntimation().getCreatedDate() != null ){
//								StringBuffer dateStr = new StringBuffer( new SimpleDateFormat("dd/MM/yyyy").format(claim.getIntimation().getCreatedDate()));
//								finyear = dateStr.substring(dateStr.length()-4, dateStr.length());
//							}
//							clmsStatusReportDto.setFinacialYear(finyear);
//							String diagnosis = "";
//							String icdCode = "";
//						    if(stageName.equalsIgnoreCase(SHAConstants.CLAIMS_PRE_AUTH_STATUS) && stageInfo.getPreauth().getKey() != null){
//						    	
//						    	Preauth preauthObj =  null;
//								Query q = entityManager
//										.createNamedQuery("Preauth.findByKey");
//								q.setParameter("preauthKey", stageInfo.getPreauth().getKey());
//								List<Preauth> preauthList = q.getResultList();
//								if(preauthList != null && !preauthList.isEmpty()){
//							
//								preauthObj =  preauthList.get(0);
//								entityManager.refresh(preauthObj);
//								diagnosis = (new PreauthService())
//										.getDiagnosisByPreauthKey(preauthObj.getKey(), entityManager);	
//								Query diagQuery = entityManager
//										.createNamedQuery("PedValidation.findByTransactionKey");
//								diagQuery.setParameter("transactionKey", 
//										preauthObj.getKey());
//
//								List<PedValidation> diagList = diagQuery
//										.getResultList();
//								if (diagList != null && !diagList.isEmpty()) {
//
//									List<DiagnosisDetailsTableDTO> diagListDto = (PreMedicalMapper.getInstance())
//											.getNewPedValidationTableListDto(diagList);
//
//									for (DiagnosisDetailsTableDTO diagDto : diagListDto) {
//										
//										if (diagDto.getIcdCode() != null) {
//											IcdCode icdCodeObj = entityManager
//													.find(IcdCode.class, diagDto
//															.getIcdCode().getId());
//											if (icdCodeObj != null) {
//												icdCode = ("")
//														.equalsIgnoreCase(icdCode) ? icdCodeObj
//														.getValue() : icdCode
//														+ " , "
//														+ icdCodeObj.getValue();
//											}
//
//										}
//									}
//									clmsStatusReportDto.setIcdCode(icdCode);
//									clmsStatusReportDto.setDiagnosis(diagnosis);
//								}
//						     }
//								
//								Query historyQ = entityManager.createNamedQuery("StageInformation.findCashlessByStageInfoKeyNCashlessKey");
//								historyQ.setParameter("stageInfoKey",stageInfo.getKey());
//								historyQ.setParameter("preauthKey",stageInfo.getPreauth().getKey());
//								historyQ.setParameter("stageKey",ReferenceTable.PREAUTH_STAGE);
//								historyQ.setParameter("statusKey",ReferenceTable.PREAUTH_APPROVE_STATUS);
//								
//								List<StageInformation> stageList = historyQ.getResultList();
//								
//								if(stageList != null && !stageList.isEmpty()){
//									StageInformation stageObj = stageList.get(0);
//									if(stageObj != null){
//										entityManager.refresh(stageObj);
//										clmsStatusReportDto.setPreauthApprovalDate(stageObj.getCreatedDate() != null ? new SimpleDateFormat("dd-MM-yy").format(stageObj.getCreatedDate()) : "");
//									}
//									if(preauthObj != null && preauthObj.getTotalApprovalAmount() != null){
//										clmsStatusReportDto.setPreauthAmt(String.valueOf(preauthObj.getTotalApprovalAmount().intValue()));
//									}
//								}
//								
//								String statusValue =  stageInfo.getStage() != null && stageInfo.getStage().getStageName() != null ? stageInfo.getStage().getStageName() + " - " : ""; 
//								statusValue = statusValue + ( stageInfo.getStatus() != null && stageInfo.getStatus().getProcessValue() != null ? stageInfo.getStatus().getProcessValue() : "") ;
//								clmsStatusReportDto.setStatus(statusValue);
//								resultDtoList.add(clmsStatusReportDto);
//						    }
//						    else
//						    {						
//						    clmsStatusReportDto.setCpuCode(claimDto.getNewIntimationDto().getCpuCode());
//						    clmsStatusReportDto.setInitialProvisionAmount(claimDto.getCurrentProvisionAmount() != null ? String.valueOf(claimDto.getCurrentProvisionAmount().intValue()) : "");
//						    clmsStatusReportDto.setFvrUploaded("N");
//						    
//						    Double deductAmt = 0d;
//							Double faApprovedAmt = 0d;
//							String paidDate = "";
//							Double claimedAmt = 0d;
//							Double billingApproveAmt = 0d;
//							
////							claimedAmt =   reimbObj.getCurrentProvisionAmt() != null ? reimbObj.getCurrentProvisionAmt() : 0d;
//							
//							if(clmsStatusReportDto.getReimbursementKey() != null){
//							Query docSummaryQuery = entityManager.createNamedQuery("RODDocumentSummary.findByReimbursementKey");
//							docSummaryQuery.setParameter("reimbursementKey", clmsStatusReportDto.getReimbursementKey());
//							
//							List<RODDocumentSummary> docSummaryList = docSummaryQuery.getResultList();
//							if(docSummaryList != null && !docSummaryList.isEmpty()){
//								
//								reimbObj = docSummaryList.get(0).getReimbursement();
//								if(reimbObj != null){
//									entityManager.refresh(reimbObj);
//								}
//								for(RODDocumentSummary rodBillSummary : docSummaryList){
//									
//									claimedAmt += rodBillSummary.getBillAmount() != null ? rodBillSummary.getBillAmount() : 0d; 		
//								}						
//							}
//							
//							}
//							
//							
//							List<ClaimPayment> paymentListByRodNumber = (new PaymentProcessCpuService()).getPaymentDetailsByRodNumber(reimbObj.getRodNumber(),this.entityManager);
//							
//							if(paymentListByRodNumber != null && !paymentListByRodNumber.isEmpty()){
//								
//								for(ClaimPayment paymentObj : paymentListByRodNumber){
//									paidDate =	paymentObj.getCreatedDate() != null ?	(("").equalsIgnoreCase(paidDate) 
//															? new SimpleDateFormat("dd/MM/yy").format(paymentObj.getCreatedDate()) 
//																		: paidDate + " - " + new SimpleDateFormat("dd/MM/yy").format(paymentObj.getCreatedDate())) : paidDate;
//									faApprovedAmt += ( paymentObj.getApprovedAmount() != null ?  paymentObj.getApprovedAmount() : 0d); 
//								
//								}
//							}
//							
//							clmsStatusReportDto.setPaidDate(paidDate);
//							clmsStatusReportDto.setPaidAmout(String.valueOf(faApprovedAmt.intValue()));
//							
//							Reimbursement rodObj = (new ReimbursementService()).getReimbursementbyRod(clmsStatusReportDto.getReimbursementKey(), entityManager);
//							
//							paidDate = "";
//							faApprovedAmt = 0d;
//								
//								if(reimbObj != null && (ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY).equals(reimbObj.getStage().getKey()) && (ReferenceTable.FINANCIAL_APPROVE_STATUS).equals(reimbObj.getStatus().getKey())){
//								
//									if(("0").equalsIgnoreCase(clmsStatusReportDto.getPaidAmout())){
//										faApprovedAmt += reimbObj.getFinancialApprovedAmount() != null ? reimbObj.getFinancialApprovedAmount() : 0d;
//									}
//									
//									if(("").equalsIgnoreCase(clmsStatusReportDto.getPaidDate())){
//										paidDate = reimbObj.getFinancialCompletedDate() != null ? (("").equalsIgnoreCase(paidDate) ? new SimpleDateFormat("dd/MM/yy").format(reimbObj.getFinancialCompletedDate()) : paidDate + " - " + new SimpleDateFormat("dd/MM/yy").format(reimbObj.getFinancialCompletedDate())):paidDate;
//									}
//								}
//							
////							List<RODDocumentSummary> billsList = (new CreateRODService()).getBillDetailsByRodKey(reimbObj.getKey(),entityManager);
////							
////							if(billsList != null && !billsList.isEmpty()){
////								
////								for(RODDocumentSummary rodBill : billsList){
////									entityManager.refresh(rodBill);
////									List<RODBillDetails> rodBillDetailsList = (new CreateRODService()).getBillEntryDetails(rodBill.getKey(),this.entityManager);
////									if(rodBillDetailsList != null && !rodBillDetailsList.isEmpty()){
////									
////										for(RODBillDetails rodBillDetail :rodBillDetailsList){
////											entityManager.refresh(rodBillDetail);
////											deductAmt += rodBillDetail.getNonPayableAmount() != null ? rodBillDetail.getNonPayableAmount() : 0d;
////										}
////									}
////								}
////							}
//									
//							if(stageName.equalsIgnoreCase(SHAConstants.SETTLED_STATUS))
//							{	
//								if(claimedAmt != null && claimedAmt > 0 && clmsStatusReportDto.getPaidAmout() != null ) {
//									deductAmt = claimedAmt.doubleValue() > faApprovedAmt ? claimedAmt.doubleValue() - faApprovedAmt : Integer.valueOf("0");
//								}
//							}
//							
//							
//							billingApproveAmt = reimbObj.getBillingApprovedAmount() != null ? reimbObj.getBillingApprovedAmount() : 0d;
//							clmsStatusReportDto.setClaimedAmount(claimedAmt != null ? String.valueOf(claimedAmt.intValue()) : "");
//							clmsStatusReportDto.setBillAmount(clmsStatusReportDto.getClaimedAmount());
//							clmsStatusReportDto.setBillingApprovedAmount(String.valueOf(billingApproveAmt.intValue()));
//							clmsStatusReportDto.setDeductedAmount(String.valueOf(deductAmt.toString()));
//							clmsStatusReportDto.setPaidAmout(("0").equalsIgnoreCase(clmsStatusReportDto.getPaidAmout()) ? String.valueOf(faApprovedAmt.intValue()) : clmsStatusReportDto.getPaidAmout());
//							clmsStatusReportDto.setPaidDate(("").equalsIgnoreCase(clmsStatusReportDto.getPaidDate()) ? paidDate : clmsStatusReportDto.getPaidDate());					    
//						    
//							DBCalculationService dbcalService = new DBCalculationService();
//							Double sumInsured = dbcalService.getInsuredSumInsured(
//									String.valueOf(claimDto.getNewIntimationDto()
//											.getInsuredPatient().getInsuredId()),
//									claimDto.getNewIntimationDto().getPolicy()
//											.getKey());
//							clmsStatusReportDto.setSuminsured(String
//									.valueOf(sumInsured.intValue()));
//							
//							ReimbursementQuery reimbQueryObj = (new ReimbursementService()).getReimbursementQueryByReimbursmentKey(reimbObj.getKey(),entityManager);
//							if(reimbQueryObj != null){
//								entityManager.refresh(reimbQueryObj);
//								clmsStatusReportDto.setQueryRaisedDate(reimbQueryObj.getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yy").format(reimbQueryObj.getCreatedDate()) : "");
//								clmsStatusReportDto.setQueryRaisedRemarks(reimbQueryObj.getQueryRemarks() != null ? reimbQueryObj.getQueryRemarks() : "");
//								clmsStatusReportDto.setQueryReplyDate(reimbQueryObj.getQueryReplyDate() != null ? new SimpleDateFormat("dd/MM/yy").format(reimbQueryObj.getQueryReplyDate()) : "");
//							
//								if(stageName.equalsIgnoreCase(SHAConstants.CLAIM_QUERY))
//								{
//									clmsStatusReportDto.setUserId(reimbQueryObj.getModifiedBy() != null ? reimbQueryObj.getModifiedBy() : "");
//								}
//							
//							}
//							
//							ReimbursementRejection reimbreject = (new ReimbursementRejectionService()).getRejectionByReimbursementKey(reimbObj.getKey(), entityManager);
//							if(reimbreject != null){
//								entityManager.refresh(reimbreject);
//								clmsStatusReportDto.setRejectDate(reimbreject.getModifiedDate() != null ? new SimpleDateFormat("dd/MM/yy").format(reimbreject.getModifiedDate()) : "");
//								clmsStatusReportDto.setRejectionRemarks(reimbreject.getRejectionRemarks() != null ? reimbreject.getRejectionRemarks(): "");
//							
//								if(stageName.equalsIgnoreCase(SHAConstants.REJECTED_CLAIMS))
//								{
//									clmsStatusReportDto.setUserId(reimbreject.getModifiedBy() != null ? reimbreject.getModifiedBy() : "");
//								}
//							
//							}													
//									icdCode ="";
//									diagnosis = "";
//									Query diagQuery = entityManager
//											.createNamedQuery("PedValidation.findByTransactionKey");
//									diagQuery.setParameter("transactionKey",reimbObj.getKey());
//									List<PedValidation> diagnosisList = (List<PedValidation>) diagQuery
//					 					.getResultList();
//									diagnosis = "";
//									if (diagnosisList != null
//											&& !diagnosisList.isEmpty()) {
//										for (PedValidation pedDiagnosis : diagnosisList) {
//
//											if (pedDiagnosis != null) {
//												if (pedDiagnosis.getDiagnosisId() != null) {
//													Diagnosis diag = entityManager
//															.find(Diagnosis.class,
//																	pedDiagnosis
//																			.getDiagnosisId());
//													if (diag != null) {
//														diagnosis = !("")
//																.equalsIgnoreCase(diagnosis) ? diagnosis
//																+ " , "
//																+ diag.getValue()
//																: diag.getValue();
//													}
//												}
//											}
//											
//											if (pedDiagnosis.getIcdCodeId() != null) {
//												IcdCode icdCodeObj = entityManager
//														.find(IcdCode.class, pedDiagnosis.getIcdCodeId());
//												if (icdCodeObj != null) {
//													icdCode = ("")
//															.equalsIgnoreCase(icdCode) ? icdCodeObj
//															.getValue() : icdCode
//															+ " , "
//															+ icdCodeObj.getValue();
//												}
//
//											}
//											
//											
//											
//										}
//									}
//									
//									Query closeClaimQuery = entityManager
//											.createNamedQuery("CloseClaim.getByReimbursmentKey");
//									closeClaimQuery.setParameter("reimbursmentKey", reimbObj.getKey());
//
//									List<CloseClaim> closedClaimList = closeClaimQuery.getResultList();
//									
//									if(closedClaimList != null && !closedClaimList.isEmpty()){
//										CloseClaim closedClaim = closedClaimList.get(0);
//										entityManager.refresh(closedClaim);
//										String closeDate = closedClaim.getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yy").format(closedClaim.getCreatedDate()) : "";
//										clmsStatusReportDto.setCloseDate(closeDate);
//										clmsStatusReportDto.setClosedRemarks(closedClaim.getClosingRemarks() != null ? closedClaim.getClosingRemarks() :"");
//										clmsStatusReportDto.setCloseStage(closedClaim.getStage().getStageName() != null ? closedClaim.getStage().getStageName() : "");
//										clmsStatusReportDto.setUserId(closedClaim.getCreatedBy() != null ? closedClaim.getCreatedBy() : "");
//										clmsStatusReportDto.setUserName(clmsStatusReportDto.getUserId());
//										
//									}
//									
//									Query docReceivedQuery = entityManager
//											.createNamedQuery("DocAcknowledgement.getByClaimKey");
//									docReceivedQuery.setParameter("claimKey", reimbObj.getClaim().getKey());
//									List<DocAcknowledgement> docAckList = docReceivedQuery.getResultList();
//									
//									if( docAckList != null && !docAckList.isEmpty()){									
//										clmsStatusReportDto.setNoofTimeBillRec(String.valueOf(docAckList.size()));
//										DocAcknowledgement docObj = docAckList.get(0);
//										entityManager.refresh(docObj);
//										String billReceivedDate = docObj.getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yy").format(docObj.getCreatedDate()) : ""; 
//										clmsStatusReportDto.setBillReceivedDate(billReceivedDate);
////										Double billAmount = 0d;
////										for(DocAcknowledgement docKObj : docAckList){
////										billAmount = docKObj.getHospitalizationClaimedAmount() != null ? docKObj.getHospitalizationClaimedAmount(): billAmount;
////										billAmount = docKObj.getPreHospitalizationClaimedAmount() != null ? billAmount + docKObj.getPreHospitalizationClaimedAmount() : billAmount;
////										billAmount = docKObj.getPostHospitalizationClaimedAmount() != null ? billAmount + docKObj.getPostHospitalizationClaimedAmount() : billAmount;
////										}
////										clmsStatusReportDto.setBillAmount(billAmount != null && billAmount != 0d ? String.valueOf(billAmount.intValue()) :"");
//										//clmsStatusReportDto.setClaimedAmount(billAmount.toString());
//										}
//									
//									clmsStatusReportDto.setIcdCode(icdCode);
//									clmsStatusReportDto.setDiagnosis(diagnosis);
//							
//							String clmClassification = "";
//							
//							if(reimbObj != null){
//								
//								Query diagQ = entityManager.createNamedQuery("PedValidation.findByTransactionKey");
//								diagQ.setParameter("transactionKey", reimbObj.getKey());
//								
//								List<PedValidation> diagList = diagQ.getResultList();
//								
//								if(diagList != null && !diagList.isEmpty()){
//									
//									for(PedValidation diagObj : diagList){
//										
//										Query pedValidQ = entityManager.createNamedQuery("DiagnosisPED.findByPEDValidationKey");
//										pedValidQ.setParameter("pedValidationKey", diagObj.getKey());
//										List<DiagnosisPED> pedValidationList = pedValidQ.getResultList();
//										
//										if(null != pedValidationList && !pedValidationList.isEmpty()){
//											for(DiagnosisPED pedValidObj : pedValidationList){
//												clmClassification += (pedValidObj.getExclusionDetails().getExclusion() != null ? pedValidObj.getExclusionDetails().getExclusion() : clmClassification) + " - " +( pedValidObj.getDiagonsisImpact() != null && pedValidObj.getDiagonsisImpact().getValue() != null ? pedValidObj.getDiagonsisImpact().getValue() : clmClassification );
//											}
//										}
//									}
//								}
//							}
//							
//							clmsStatusReportDto.setClaimCoverage(clmClassification);
//							
//							String billingUser ="";
//							String medicalApprover="";
//							String fAApprover="";
//							StageInformation result=null;
//							Query stageUserquery = entityManager.createNamedQuery("StageInformation.findByReimbStageNStatus");
//							
//							stageUserquery.setParameter("rodKey",reimbObj.getKey());
//							stageUserquery.setParameter("stageKey", ReferenceTable.BILLING_STAGE);
//							stageUserquery.setParameter("statusKey", ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER);
//							
//							if(stageUserquery.getResultList() != null && !stageUserquery.getResultList().isEmpty() ){
//								
//								result = (StageInformation)stageUserquery.getResultList().get(0);
//								
//								entityManager.refresh(result);
//								if(result != null){
//									billingUser = result.getCreatedBy() != null ? result.getCreatedBy() : "";
//								
//									if(stageName.equalsIgnoreCase(SHAConstants.BILLING_COMPLETED))
//									{
//										clmsStatusReportDto.setUserId(billingUser);
//										
//										deductAmt = claimedAmt > billingApproveAmt ? claimedAmt - billingApproveAmt : 0d;
//
//										clmsStatusReportDto.setDeductedAmount(String.valueOf(deductAmt));
//
//									}
//								
//								}
//							}
//							stageUserquery.setParameter("stageKey", ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
//							stageUserquery.setParameter("statusKey", ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS);    
//							
//							if(stageUserquery.getResultList() != null && !stageUserquery.getResultList().isEmpty()){
//								
//								result = (StageInformation)stageUserquery.getResultList().get(0);
//								
//								entityManager.refresh(result);
//								if(result != null){
//								
//								medicalApprover = result.getCreatedBy() != null ? result.getCreatedBy() : "";
//								clmsStatusReportDto.setMaApprovedDate(result.getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yy").format(result.getCreatedDate()): "");
//							
//								if(stageName.equalsIgnoreCase(SHAConstants.MEDICAL_APPROVAL)){
//									clmsStatusReportDto.setUserId(medicalApprover);	
//								}
//								
//								}
//							}
//														
//							stageUserquery.setParameter("stageKey", ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY); 
//							stageUserquery.setParameter("statusKey", ReferenceTable.FINANCIAL_APPROVE_STATUS);
//							if(stageUserquery.getResultList() != null && !stageUserquery.getResultList().isEmpty()){
//								result = (StageInformation)stageUserquery.getResultList().get(0);
//								
//								entityManager.refresh(result);
//								if(result != null){
//									fAApprover = result.getCreatedBy() != null ? result.getCreatedBy() : "";
//								}
//							}
//							
//							clmsStatusReportDto.setBillingPerson(billingUser != null ? billingUser : "");
//							clmsStatusReportDto.setMedicalApprovalPerson(medicalApprover != null ? medicalApprover : "");
//							clmsStatusReportDto.setFinancialApprovalPerson(fAApprover != null ? fAApprover : "");
//							clmsStatusReportDto.setUserName(clmsStatusReportDto.getUserId());						
//							
//											
//							resultDtoList.add(clmsStatusReportDto);
//					} 
//					}
//						if(stageName.equalsIgnoreCase(SHAConstants.CLAIMS_PAID_STAUS)){
//							
//							List<ClaimsStatusReportDto> paidList = new ArrayList<ClaimsStatusReportDto>();
//							SimpleDateFormat dtFrmt = new SimpleDateFormat("dd/MM/yy");
//							if(null != fromDate )
//							{
//								fromDate = dtFrmt.parse(fromDate.toString());
//							}
//							if(null != endDate)
//							{
//								endDate =  dtFrmt.parse(endDate.toString());
//							}
//							for(ClaimsStatusReportDto resultDto : resultDtoList){
//								
//								resultDto.setUserId(resultDto.getFinancialApprovalPerson());
//								resultDto.setUserName(resultDto.getUserId());
//								if(fromDate.compareTo(endDate) == 0){
//									if(dtFrmt.parse(resultDto.getPaidDate()).compareTo(fromDate) == 0){
//										paidList.add(resultDto);
//									}
//								}
//								else{
//									if(dtFrmt.parse(resultDto.getPaidDate()).compareTo(fromDate) == 0){
//										paidList.add(resultDto);
//									}
//									if(dtFrmt.parse(resultDto.getPaidDate()).compareTo(fromDate) > 0){
//										paidList.add(resultDto);
//									}
//									if(dtFrmt.parse(resultDto.getPaidDate()).compareTo(endDate) < 0){
//										paidList.add(resultDto);
//									}
//									
//								}
//							}
//							
//							if(paidList != null && !paidList.isEmpty()){
//								resultDtoList.clear();
//								for(ClaimsStatusReportDto paidDto :paidList){
//									paidDto.setSno(paidList.indexOf(paidDto)+1);
//									resultDtoList.add(paidDto);
//								}
//							}
//							
//						}					
//						
//					}
//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//			}
//			
//			return resultDtoList;
//		}
		
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
		
//		public List<MedicalAuditCashlessIssuanceReportDto> getMedicalAuditCashlessIssuanceDetails(Map<String,Object> searchFilter){
//			List<MedicalAuditCashlessIssuanceReportDto> resultListDto = new ArrayList<MedicalAuditCashlessIssuanceReportDto>();
//			
//			if(searchFilter != null && !searchFilter.isEmpty()){
//				
//				try{
//
//					String status = null;
//					Date fromDate = null;
//					Date toDate = null;
//
//					if (searchFilter.containsKey("fromDate") && searchFilter.get("fromDate") != null){
//						fromDate = (Date)searchFilter.get("fromDate");
//					}
//
//					if (searchFilter.containsKey("toDate") && searchFilter.get("toDate") != null) {
//						toDate = (Date)searchFilter.get("toDate");
//						Calendar c = Calendar.getInstance();
//						c.setTime(toDate);
//						c.add(Calendar.DATE, 1);
//						toDate = c.getTime();
//					}
//
//					if (searchFilter.containsKey("status") && searchFilter.get("status") != null) {
//						status = StringUtils.trim(searchFilter.get("status").toString());
//					}
//
//					if (status != null || (fromDate != null && toDate != null)) {
//
//						final CriteriaBuilder builder = entityManager
//								.getCriteriaBuilder();
//						
//						final CriteriaQuery<Reimbursement> criteriaReimbQuery = builder
//								.createQuery(Reimbursement.class);
//						
//						Root<Reimbursement> reimbRoot =  criteriaReimbQuery.from(Reimbursement.class);
//						
//						Join<Reimbursement,Claim> claimJoin = reimbRoot.join(
//								"claim", JoinType.INNER);
//						
//						List<Predicate> predicates = new ArrayList<Predicate>();
//						
//						Predicate claimTypePred = builder.equal(reimbRoot.<Claim>get("claim").<MastersValue>get("claimType").<Long>get("key"), ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
//						predicates.add(claimTypePred);
//						
//						Predicate billClassificationPred = builder.like(builder.lower(reimbRoot.<DocAcknowledgement>get("docAcknowLedgement").<String>get("hospitalisationFlag")),"y"); 
//						predicates.add(billClassificationPred);
//						
//					//			SHAConstants.SETTLED_STATUS
//					
//						
//						Predicate claimStagePred = builder.equal(reimbRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY);
//						Predicate claimStatusPred = builder.equal(reimbRoot.<Status>get("status").<Long>get("key"),ReferenceTable.FINANCIAL_APPROVE_STATUS);
//						Predicate approvedPred = builder.and(claimStagePred,claimStatusPred);
//						
//						if(status.equalsIgnoreCase(SHAConstants.SETTLED_STATUS)){
//							predicates.add(approvedPred);
//						}
//					
//					//			SHAConstants.REJECTED_STATUS
//						
//						Predicate medicalRejectStagePred = builder.equal(reimbRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
//						Predicate medicalRejectstatusPred = builder.equal(reimbRoot.<Status>get("status").<Long>get("key"),ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS);
//						
//						Predicate medicalRejectPred = builder.and(medicalRejectStagePred,medicalRejectstatusPred);
//
//						
//						Predicate finanRejectStagePred = builder.equal(reimbRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY);
//						Predicate finanRejectstatusPred = builder.equal(reimbRoot.<Status>get("status").<Long>get("key"),ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS);
//						
//						Predicate financialRejectPred = builder.and(finanRejectStagePred,finanRejectstatusPred);
//						
//						
//						Predicate claimRejectPred = builder.or(medicalRejectPred,financialRejectPred);
//					
//						if(status.equalsIgnoreCase(SHAConstants.REJECTED_STATUS)){
//							predicates.add(claimRejectPred);
//						}
//						
//					
//					//				SHAConstants.CLOSED_STATUS
//										
//						Predicate rodStagePred = builder.equal(reimbRoot.<Stage>get("stage").<Long>get("key"),ReferenceTable.CREATE_ROD_STAGE_KEY);
//						Predicate rodStatusPred = builder.equal(reimbRoot.<Status>get("status").<Long>get("key"), ReferenceTable.CREATE_ROD_CLOSED);
//						
//						Predicate rodClosedPred = builder.and(rodStagePred,rodStatusPred);
//					
//						Predicate billEntryStagePred = builder.equal(reimbRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.BILL_ENTRY_STAGE_KEY);
//						Predicate billEntryStatusPred = builder.equal(reimbRoot.<Status>get("status").<Long>get("key"), ReferenceTable.BILL_ENTRY_PROCESS_CLOSED_STATUS);
//					
//						Predicate billingClosedPred = builder.and(billEntryStagePred,billEntryStatusPred);
//						
//						Predicate zonalReviewStagePred = builder.equal(reimbRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.PROCESS_CLAIM_REQUEST_ZONAL_STAGE_KEY);
//						Predicate zonalReviewStatusPred = builder.equal(reimbRoot.<Status>get("status").<Long>get("key"), ReferenceTable.ZONAL_REVIEW_CLOSED);
//						
//						Predicate zonalreviewClosedPed = builder.and(zonalReviewStagePred,zonalReviewStatusPred);
//						
//						Predicate processClaimStagePred = builder.equal(reimbRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
//						Predicate processClaimStatusPred = builder.equal(reimbRoot.<Status>get("status").<Long>get("key"), ReferenceTable.CLAIM_REQUEST_CLOSED);
//						
//						Predicate processClaimReqClosedPred = builder.and(processClaimStagePred,processClaimStatusPred); 
//						
//						Predicate claimBillingStagePred = builder.equal(reimbRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.BILLING_STAGE);
//						Predicate claimBillingStatusPred = builder.equal(reimbRoot.<Status>get("status").<Long>get("key"), ReferenceTable.BILLING_CLOSED_STATUS);
//
//						Predicate claimBillingClosedPred = builder.and(claimBillingStagePred,claimBillingStatusPred);
//											
//						Predicate finStagePred = builder.equal(reimbRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.FINANCIAL_STAGE);
//						Predicate finStatusPred = builder.equal(reimbRoot.<Status>get("status").<Long>get("key"), ReferenceTable.FINANCIAL_CLOSED);
//
//						Predicate finanClosedPred = builder.and(finStagePred,finStatusPred);
//						
//						Predicate claimClosedPred = builder.or(rodClosedPred,billingClosedPred,zonalreviewClosedPed,processClaimReqClosedPred,claimBillingClosedPred,finanClosedPred);
//						
//						if(status.equalsIgnoreCase(SHAConstants.CLOSED_STATUS)){
//							predicates.add(claimClosedPred);
//						}
//					
//					//		SHAConstants.PENDING_STATUS
//						
////						Predicate finPendingStagePred = builder.equal(reimbRoot.<Claim>get("claim").<Stage>get("stage").<Long>get("key"), ReferenceTable.FINANCIAL_STAGE);
////						
////						List<Long> claimFinstatusKeyList = new ArrayList<Long>();
////						claimFinstatusKeyList.add(ReferenceTable.FINANCIAL_QUERY_STATUS);
////						claimFinstatusKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS);
////						claimFinstatusKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REDRAFT_STATUS);
////						claimFinstatusKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REJECT_STATUS);
////						claimFinstatusKeyList.add(ReferenceTable.FINANCIAL_REJECT_STATUS);
////						claimFinstatusKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_DISAPPROVE_REJECT_STATUS);
////						claimFinstatusKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_REDRAFT_REJECT_STATUS);
////						
////						Predicate finPendingStatusPred = reimbRoot.<Claim>get("claim").<Status>get("status").<Long>get("key").in(claimFinstatusKeyList);
////						
////						Predicate finClaimPedingPred = builder.and(finPendingStagePred,finPendingStatusPred);
////						
////						
////						Predicate medicalStagePred = builder.equal(reimbRoot.<Claim>get("claim").<Stage>get("stage").<Long>get("key"),ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY); 
////						List<Long> medicalStatusKeyList = new ArrayList<Long>();
////						
////						medicalStatusKeyList.add(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
////						medicalStatusKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS);
////						medicalStatusKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REDRAFT_STATUS);
////						medicalStatusKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS);
////						medicalStatusKeyList.add(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS);
////						medicalStatusKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_DISAPPROVE_REJECT_STATUS);
////						medicalStatusKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_REDRAFT_REJECT_STATUS);
////						
////						Predicate medicalStatusPred = reimbRoot.<Claim>get("claim").<Status>get("status").<Long>get("key").in(medicalStatusKeyList);
////						
////						Predicate medicalPedndingPred = builder.and(medicalStagePred,medicalStatusPred);
////						
////						List<Long> pendingStageKeyList = new ArrayList<Long>();
////						
////						pendingStageKeyList.add(ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
////						pendingStageKeyList.add(ReferenceTable.CREATE_ROD_STAGE_KEY);
////						pendingStageKeyList.add(ReferenceTable.BILL_ENTRY_STAGE_KEY);
////						pendingStageKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_ZONAL_STAGE_KEY);
////						pendingStageKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
////						pendingStageKeyList.add(ReferenceTable.BILLING_STAGE);
////						Predicate otherPendingStagePred = reimbRoot.<Claim>get("claim").<Stage>get("stage").<Long>get("key").in(pendingStageKeyList);
////						
////						Predicate claimPendingPred = builder.or(finClaimPedingPred,medicalPedndingPred,otherPendingStagePred);
//						
//						if(status.equalsIgnoreCase(SHAConstants.PENDING_STATUS)){
//							
//							Predicate notClosedPred = builder.not(claimClosedPred);
//							Predicate notRejectedPred = builder.not(claimRejectPred);
//							Predicate notSettledPred = builder.not(approvedPred);
//							
//							Predicate pedingPred = builder.and(notClosedPred,notRejectedPred,notSettledPred);
//							predicates.add(pedingPred);
//							
////							predicates.add(claimPendingPred);
//						
//						}
//					
//					if(fromDate != null && toDate != null) {
//					Expression<Date> fromDateExpression = reimbRoot.<Claim>get("claim")
//							.<Date> get("modifiedDate");
//					Predicate fromDatePredicate = builder
//							.greaterThanOrEqualTo(fromDateExpression,
//									fromDate);
//					predicates.add(fromDatePredicate);
//
//					Expression<Date> toDateExpression = reimbRoot.<Claim>get("claim")
//							.<Date> get("modifiedDate");
//					Calendar c = Calendar.getInstance();
//					c.setTime(toDate);
//					c.add(Calendar.DATE, 1);
//					toDate = c.getTime();					
//					Predicate toDatePredicate = builder
//							.lessThanOrEqualTo(toDateExpression, toDate);
//					predicates.add(toDatePredicate);
//					
//					}
//					criteriaReimbQuery.select(reimbRoot).where(
//							builder.and(predicates.toArray(new Predicate[] {})));
//
//					final TypedQuery<Reimbursement> reimbusementQuery = entityManager
//							.createQuery(criteriaReimbQuery);
//
//					List<Reimbursement> resultreimbList = reimbusementQuery.getResultList();
//
//					if (resultreimbList != null && !resultreimbList.isEmpty()) {
//
//						for (Reimbursement reimbursementObj : resultreimbList) {
//							entityManager.refresh(reimbursementObj);
//							Claim claimObj = reimbursementObj.getClaim();
//							ClaimMapper clmMapper =  ClaimMapper.getInstance();
//							ClaimDto claimDto = clmMapper.getClaimDto(claimObj);
//							NewIntimationDto intimationDto = getIntimationService().getIntimationDto(claimObj.getIntimation(), entityManager);
//							claimDto.setNewIntimationDto(intimationDto);
//							MedicalAuditCashlessIssuanceReportDto auditClaimDto = new MedicalAuditCashlessIssuanceReportDto(claimDto);
//							List<Preauth> preauthObjList = (new PreauthService()).getPreauthListByDescendingOrder(claimObj.getKey(),entityManager);
//							if(preauthObjList != null && !preauthObjList.isEmpty()){
//								Preauth preauthObj = preauthObjList.get(0);
//								entityManager.refresh(preauthObj);
//								auditClaimDto.setPreAuthAmount(preauthObj.getTotalApprovalAmount() != null ? String.valueOf(preauthObj.getTotalApprovalAmount().intValue()) : "");
//							}
//							
////							List<Reimbursement> reimbList = (new ReimbursementService()).getReimbursementByClaimKey(claimObj.getKey(),entityManager);
//							entityManager.refresh(reimbursementObj);
//							auditClaimDto.setGeneralRemarks(reimbursementObj.getApprovalRemarks() != null ? reimbursementObj.getApprovalRemarks() : "");
//							auditClaimDto.setDoctorNote(reimbursementObj.getDoctorNote() != null ? reimbursementObj.getDoctorNote() : "");
//							auditClaimDto.setFinalRemarks(reimbursementObj.getFinancialApprovalRemarks() != null ? reimbursementObj.getFinancialApprovalRemarks() : "");
//								
//							ReimbursementQuery reimbQuery = (new ReimbursementService()).getReimbursementQueryByReimbursmentKey(reimbursementObj.getKey(),entityManager);
//								
//							if(reimbQuery != null){
//								String queryRemarks = reimbQuery.getQueryRemarks() != null ? reimbQuery.getQueryRemarks() : "";
//								auditClaimDto.setQueryRaisedOrMedRejReq(reimbursementObj.getRejectionRemarks() != null ? queryRemarks + " / " + reimbursementObj.getRejectionRemarks() : queryRemarks);
//							}							
//								
//							String diagnosis = "";
//							Query diagQuery = entityManager
//										.createNamedQuery("PedValidation.findByTransactionKey");
//							diagQuery.setParameter("transactionKey",reimbursementObj.getKey());
//							List<PedValidation> diagnosisList = (List<PedValidation>) diagQuery.getResultList();
//							diagnosis = "";
//								if (diagnosisList != null
//										&& !diagnosisList.isEmpty()) {
//									for (PedValidation pedDiagnosis : diagnosisList) {
//
//										if (pedDiagnosis != null) {
//											if (pedDiagnosis.getDiagnosisId() != null) {
//												Diagnosis diag = entityManager
//														.find(Diagnosis.class,
//																pedDiagnosis
//																		.getDiagnosisId());
//												if (diag != null) {
//													diagnosis = !("")
//															.equalsIgnoreCase(diagnosis) ? diagnosis
//															+ " , "
//															+ diag.getValue()
//															: diag.getValue();
//												}
//											}
//										}
//									}
//								}							
//							auditClaimDto.setDiagnosis(diagnosis);
//							auditClaimDto.setReBillingOrReQuery("");
//							resultListDto.add(auditClaimDto);	
//							
//						}
//					}
//					
//					}	
//					
//				
//				}
//				catch(Exception e){
//					e.printStackTrace();
//				}
//			}
//			
//			return resultListDto;
//		}
		
		public ClaimDto getClaimDto(OMPClaim claimObj, EntityManager em){
			this.entityManager = em;
			ClaimDto claimDto = new ClaimDto();
			if(claimObj != null){
				claimDto = OMPClaimMapper.getInstance().getClaimDto(claimObj);
				NewIntimationDto intimationDto = (new OMPIntimationService()).getIntimationDto(claimObj.getIntimation(), entityManager);
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
		
		/***
		 * Method to update the claim. Specially used for
		 * generate covering letter service.
		 * **/
		public void updateClaim(Long claimKey)
		{
			 OMPClaim claim = getClaimByKey(claimKey);
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
		
//		public CloseClaim getCloseClaimByReimbursementKey(Long reimbursementKey){
//			
//			Query closeClaimQuery = entityManager
//					.createNamedQuery("CloseClaim.getByReimbursmentKey");
//			closeClaimQuery.setParameter("reimbursmentKey", reimbursementKey);
//
//			List<CloseClaim> closedClaimList = closeClaimQuery.getResultList();
//			
//			if(closedClaimList != null && ! closedClaimList.isEmpty()){
//				return closedClaimList.get(0);
//			}
//			
//			return null;
//		}
		
//		public TmpFvR getMasFVR(String representativeCode)
//		{	TmpFvR tmpFVR =null;
//			if (representativeCode != null) {
//				Query tmpFVRQuery = entityManager.createNamedQuery(
//						"TmpFvR.findByCode")
//						.setParameter("code", representativeCode);
//				 tmpFVR = (TmpFvR) tmpFVRQuery.getSingleResult();
//				 if (tmpFVR != null){
//					 return tmpFVR;
//				 }
//			}
//			return tmpFVR;
//		}
		
//		public void submitDBForReimConvrSearch(Claim claim){
//			
//			Hospitals hospitalById = getHospitalById(claim.getIntimation().getHospital());
//			
//			Object[] arrayListForDBCall = SHAUtils.getArrayListForDBCall(claim, hospitalById);
//			
//			
//			Object[] inputArray = (Object[])arrayListForDBCall[0];
//			inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.OUTCOME_FOR_REIM_SEARCH_CONVERTION;
//
//			Object[] parameter = new Object[1];
//			parameter[0] = inputArray;
//			
//			DBCalculationService dbCalculationService = new DBCalculationService();
//			dbCalculationService.initiateTaskProcedure(parameter);
//			
//		}
		
		public void submitProvisionHistory(ProvisionUploadHistory provisionHistory){
			
//			Query query = entityManager.createQuery("select SEQ_HISTORY_KEY.nextval as num from ProvisionUploadHistory");
//			Long nextValue = ((BigInteger)query.getSingleResult()).longValue();
//			
//			String batchNo = "BU"+nextValue;
			
//			provisionHistory.setBatchNumber(batchNo);
			
			
			
			entityManager.persist(provisionHistory);
			entityManager.flush();
			
		}
		
		public void updateClaimForRevisedProvision(OMPClaim claim){
//			entityManager.merge(claim);
//			entityManager.flush();
			
			String query = "UPDATE IMS_CLS_OMP_CLAIM SET REVISED_CURRENT_PROVISION_AMT = "+claim.getRevisedProvisionAmount()+ "WHERE CLAIM_KEY ="+claim.getKey();
			
			Query nativeQuery = entityManager.createNativeQuery(query);
			nativeQuery.executeUpdate();
		}
		
		@SuppressWarnings("unchecked")
		public OMPReimbursement getReimbursement(Long rodkey) {

			Query rodQuery = entityManager
					.createNamedQuery("OMPReimbursement.findByKey");
			rodQuery = rodQuery.setParameter("primaryKey", rodkey);
			List<OMPReimbursement> reimbursementList = (List<OMPReimbursement>) rodQuery
					.getResultList();

			if (reimbursementList != null && !reimbursementList.isEmpty()) {

				entityManager.refresh(reimbursementList.get(0));
				return reimbursementList.get(0);
			}

			return null;

		}
		
		public List<PreviousClaimsTableDTO> getPreviousClaims(
				List<OMPClaim> claimList, String strClaimNumber) {
			List<OMPClaim> objClaimList = new ArrayList<OMPClaim>();
			List<PreviousClaimsTableDTO> previousClaimList;
			
			for (OMPClaim objClaim : claimList) {
				if (!(strClaimNumber.equalsIgnoreCase(objClaim.getClaimId()))) {
					objClaimList.add(objClaim);
				}
			}
			previousClaimList = OMPPreviousClaimMapper.getInstance()
					.getPreviousClaimDTOList(objClaimList);
			
			for (PreviousClaimsTableDTO previousClaimsTableDTO : previousClaimList) {
				
				Long claimedAmountForROD = getClaimedAmountForROD(previousClaimsTableDTO.getKey());
				if(claimedAmountForROD != null && claimedAmountForROD >= 0){
					previousClaimsTableDTO.setClaimAmount(String.valueOf(claimedAmountForROD));
				}
				
				Double reimbursementApprovedAmount = getReimbursementApprovedAmount(previousClaimsTableDTO.getKey());
				if(reimbursementApprovedAmount != null && reimbursementApprovedAmount >= 0){
					previousClaimsTableDTO.setApprovedAmount(reimbursementApprovedAmount.toString());
				}
				
//				if(previousClaimsTableDTO != null && previousClaimsTableDTO.getRecordFlag() != null && previousClaimsTableDTO.getRecordFlag().equalsIgnoreCase("P")){
				if(previousClaimsTableDTO != null && previousClaimsTableDTO.getRecordFlag() != null){
					if(previousClaimsTableDTO.getSettledAmountForPreviousClaim() != null){
						previousClaimsTableDTO.setApprovedAmount(previousClaimsTableDTO.getSettledAmountForPreviousClaim().toString());
					}
				}
				if(previousClaimsTableDTO!= null && previousClaimsTableDTO.getClaimType()==null){
					previousClaimsTableDTO.setClaimType("Non Medical");
				}
				if(previousClaimsTableDTO!= null && previousClaimsTableDTO.getAilmentLoss()==null){
					previousClaimsTableDTO.setAilmentLoss(previousClaimsTableDTO.getLossDetails());
				}if(previousClaimsTableDTO!= null && previousClaimsTableDTO.getClaimStatus().equalsIgnoreCase("Rejection suggested")){
					previousClaimsTableDTO.setClaimStatus("Rejected");
				}
				if(claimList!= null && !claimList.isEmpty()){
					
					for(OMPClaim claim :claimList){/*
						if(claim!= null){
							if(previousClaimsTableDTO.getAdmissionDate() != null){
								previousClaimsTableDTO.setAdmissionDate(SHAUtils.formatDate(claim.getDataOfAdmission()));
							}
							if(claim.getHospital()!= null ){
								OMPHospitals hospitals = intimationService.getHospitalDetailsBykey(claim.getHospital());
//								Hospitals hospitals = getHospitalById(intimation.getHospital());
								if(hospitals != null){
									previousClaimsTableDTO.setHospitalName(hospitals.getName());
								}
							}
						}
					*/}
				}
				
				List<OMPReimbursement> reimbursement = getReimbursmentListByClaimKey(previousClaimsTableDTO.getKey());
				
				if(reimbursement != null){
					List<Double> copayValues = new ArrayList<Double>();
					
					for (OMPReimbursement ompReimbursement : reimbursement) {
						
						OMPReimbursement reimbursementObjectByKey = getReimbursement(ompReimbursement.getKey());
						if(reimbursementObjectByKey != null && reimbursementObjectByKey.getAmtConsCopayPercentage() != null){
							copayValues.add(reimbursementObjectByKey.getAmtConsCopayPercentage().doubleValue());
						}
					}
					
					if(! copayValues.isEmpty()){
						Double maximumCopay = Collections.max(copayValues);		
						previousClaimsTableDTO.setCopayPercentage(maximumCopay);
					}

				}
			}
			try{
				if (objClaimList != null && previousClaimList != null && !objClaimList.isEmpty() && !previousClaimList.isEmpty()) {
					
					for(OMPClaim claim :objClaimList){
						if(claim!= null){
							if(previousClaimList.get(0).getAdmissionDate() != null){
		                         previousClaimList.get(0).setAdmissionDate(SHAUtils.formatDate(claim.getDataOfAdmission()));
							}
							if(claim.getHospital()!= null ){/*
								OMPHospitals hospitals = intimationService.getHospitalDetailsBykey(claim.getHospital());
//								Hospitals hospitals = getHospitalById(intimation.getHospital());
								if(hospitals != null){
									previousClaimList.get(0).setHospitalName(hospitals.getName());
								}
							*/}
						}
					}
			
				}
			}catch(Exception e){
				e.printStackTrace();
				log.error(e.toString());
			}
			
			return previousClaimList;
		}
		@SuppressWarnings("unchecked")
		public List<OMPReimbursement> getReimbursmentListByClaimKey(Long clmkey) {

			Query rodQuery = entityManager
					.createNamedQuery("OMPReimbursement.findLatestRODByClaimKey");
			rodQuery = rodQuery.setParameter("claimKey", clmkey);
			List<OMPReimbursement> reimbursementList = (List<OMPReimbursement>) rodQuery
					.getResultList();

			if (reimbursementList != null && !reimbursementList.isEmpty()) {
				for (OMPReimbursement ompReimbursement : reimbursementList) {
					
					entityManager.refresh(ompReimbursement);
				}
				return reimbursementList;
			}

			return null;

		}
		
		public Long getClaimedAmountForROD(Long claimKey){
			Long amount = 0l;
			Double claimedAmount = 0d;
			List<OMPReimbursement> rodList =getReimbursmentListByClaimKey(claimKey);
			
			if(rodList != null && !rodList.isEmpty()){
				for (OMPReimbursement reimbursement : rodList) {
					List<OMPBenefitCover> benefitCoverList = getOMPBenefitCoverByKey(reimbursement.getKey());
					if(benefitCoverList != null && benefitCoverList.size()>0){
						OMPBenefitCover ompBenefitCover = benefitCoverList.get(0);
						claimedAmount = ( ompBenefitCover.getNetAmt() != null ? ompBenefitCover.getNetAmt() : 0d );
					}
				}
			amount = claimedAmount.longValue();
			}
			return amount;
			
		}
		public Double getReimbursementApprovedAmount(Long claimKey){
			
			Double approvedAmount = 0d;
			List<OMPReimbursement> rodList = getReimbursmentListByClaimKey(claimKey);
			
			if(rodList != null && !rodList.isEmpty()){
				for (OMPReimbursement reimbursement : rodList) {
					if(reimbursement.getFinalApprovedAmtInr() != null && reimbursement.getFaSubmitFlg()!=null 
							&& reimbursement.getFaSubmitFlg().equalsIgnoreCase("Y") && reimbursement.getClassificationId()!=null
							&& reimbursement.getClassificationId().getValue()!=null && reimbursement.getClassificationId().getValue().equalsIgnoreCase("OMP Claim Related")){
						approvedAmount += reimbursement.getFinalApprovedAmtUsd();// we have to confirm usd or inr
					}
				}
			}
			
			return approvedAmount;
			
		}
		
		public Long getACknowledgeNumberCountByClaimKey(Long claimKey) {
			Query query = entityManager
					.createNamedQuery("OMPReimbursement.CountAckByClaimKey");
			query = query.setParameter("claimkey", claimKey);
			// Integer.parseInt(Strin)
			Long countOfAck = (Long) query.getSingleResult();
			return countOfAck;
		}
		
		@SuppressWarnings("unchecked")
		public List<OMPBenefitCover> getOMPBenefitCoverByKey(Long rodKey) {
			Query query = entityManager.createNamedQuery("OMPBenefitCover.findByRodKey")
					.setParameter("rodKey", rodKey);
			List<OMPBenefitCover> rodList = query.getResultList();

			if (rodList != null && !rodList.isEmpty()) {
				for (OMPBenefitCover reimbursement : rodList) {
					entityManager.refresh(reimbursement);
				}
				return rodList;
			}
			return null;
		}
		
		//R1276
		public List<OMPAckDetailsDTO> getOMPAckByClaim(Long claimKey){
			List<OMPAckDetailsDTO> listOfDto = new ArrayList<OMPAckDetailsDTO>();
			OMPAckDetailsDTO dtoObj = null;
			Query query = entityManager.createNamedQuery("OMPDocAcknowledgement.findByClaimKey");
			query.setParameter("claimkey", claimKey);
			List<OMPDocAcknowledgement> listOfAckDetails = query.getResultList();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");  
			int i = 1;
			if(!listOfAckDetails.isEmpty()){
				for (OMPDocAcknowledgement ompDocAcknowledgement : listOfAckDetails) {
					dtoObj = new OMPAckDetailsDTO();
					dtoObj.setSno(i);
					dtoObj.setAckNo((ompDocAcknowledgement.getAcknowledgeNumber() == null)?"":ompDocAcknowledgement.getAcknowledgeNumber());
					dtoObj.setAckCreatedDate(formatter.format(ompDocAcknowledgement.getCreatedDate()).toString());
					dtoObj.setAckClassification("");
					dtoObj.setAckCategory("");
					dtoObj.setAckDocReceivedFrom((ompDocAcknowledgement.getDocumentReceivedFromId() == null)?"":ompDocAcknowledgement.getDocumentReceivedFromId().getValue());
					dtoObj.setAckDocReceivedDate((ompDocAcknowledgement.getDocumentReceivedDate() == null)?"":formatter.format(ompDocAcknowledgement.getDocumentReceivedDate()).toString());
					dtoObj.setAckCreatedName(ompDocAcknowledgement.getCreatedBy());
					dtoObj.setAckStatus(ompDocAcknowledgement.getStatus().getProcessValue());
					listOfDto.add(dtoObj);
					i++;
				}
			}
			return listOfDto;
		}
		
		public List<OMPProposerDetailsDTO> getOMPProposerByClaim(Long claimKey){
			List<OMPProposerDetailsDTO> listOfDto = new ArrayList<OMPProposerDetailsDTO>();
			OMPProposerDetailsDTO dtoObj = null;
			OMPBenefitCover ompBen = null;
			Query query = entityManager.createNamedQuery("OMPReimbursement.findByOMPClmKey");
			query.setParameter("claimkey", claimKey);
			List<OMPReimbursement> listOfReimDetails = query.getResultList();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");  
			int i = 1;
			if(!listOfReimDetails.isEmpty()){
				for (OMPReimbursement ompReim : listOfReimDetails) {
					dtoObj = new OMPProposerDetailsDTO();
					dtoObj.setSno(i);
					if(ompReim.getDocAcknowLedgement()!=null && ompReim.getDocAcknowLedgement().getAcknowledgeNumber()!=null){
						dtoObj.setAckNo(ompReim.getDocAcknowLedgement().getAcknowledgeNumber());
					}else{
						dtoObj.setAckNo("");
					}
					dtoObj.setRodNo(ompReim.getRodNumber());
					dtoObj.setAckClassification(ompReim.getClassificationId().getValue());
					dtoObj.setAckCategory(ompReim.getCategoryId().getValue());
					dtoObj.setRodClaimType(ompReim.getRodClaimType().getValue());
					
					Query query2 = entityManager.createNamedQuery("OMPBenefitCover.findByRodKey");
					query2.setParameter("rodKey", ompReim.getKey());
					List<OMPBenefitCover> listOfBeneDetails = query2.getResultList();
					if(!listOfBeneDetails.isEmpty()){
						ompBen = listOfBeneDetails.get(0);
					}
					dtoObj.setTotalAmount((ompBen.getTotalAmtPayBleDollar() == null)?0L:ompBen.getTotalAmtPayBleDollar().longValue());
					dtoObj.setFinalAmount((ompReim.getFinalApprovedAmtUsd() == null)?0L:ompReim.getFinalApprovedAmtUsd().longValue());
					dtoObj.setApprovedDate(formatter.format(ompReim.getCreatedDate()));
					if(ompReim.getStatus() != null){
						dtoObj.setClmStatus(ompReim.getStatus().getProcessValue());
					}else{
						dtoObj.setClmStatus("");
					}
					listOfDto.add(dtoObj);
					i++;
				}
			}
			return listOfDto;
		}
		
		public List<OMPPaymentDetailsDTO> getOMPPaymentByClaim(String argClaimNumber, Long claimKey){
			List<OMPPaymentDetailsDTO> listOfDto = new ArrayList<OMPPaymentDetailsDTO>();
			OMPPaymentDetailsDTO dtoObj = null;
			OMPReimbursement ompReim = null;
			/*Query query = entityManager.createNamedQuery("OMPReimbursement.findByOMPClmKey");
			query.setParameter("claimkey", claimKey);
			List<OMPReimbursement> listOfReimDetails = query.getResultList();
			if(!listOfReimDetails.isEmpty()){
				ompReim = listOfReimDetails.get(0);
			}*/
			
			Query query2 = entityManager.createNamedQuery("OMPClaimPayment.findByClaimNumber");
			query2.setParameter("claimNumber", argClaimNumber);
			List<OMPClaimPayment> listOfPayDetails = query2.getResultList();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");  
			int i = 1;
			if(!listOfPayDetails.isEmpty()){
				for (OMPClaimPayment ompPay : listOfPayDetails) {
					dtoObj = new OMPPaymentDetailsDTO();
					dtoObj.setSno(i);
					dtoObj.setRodNo(ompPay.getRodNumber());
					
					Query query = entityManager.createNamedQuery("OMPReimbursement.findOMPRodByNumber");
					query.setParameter("rodNumber", ompPay.getRodNumber());
					List<OMPReimbursement> listOfReimDetails = query.getResultList();
					if(!listOfReimDetails.isEmpty()){
						ompReim = listOfReimDetails.get(0);
					}
					
					dtoObj.setAckClassification(ompReim.getClassificationId().getValue());
					dtoObj.setAckCategory(ompReim.getCategoryId().getValue());
					dtoObj.setRodClaimType(ompReim.getRodClaimType().getValue());
					dtoObj.setSettledAmount((ompPay.getTotApprovedAmt() == null)?0L:ompPay.getTotApprovedAmt().longValue());
					dtoObj.setUserName("");//ompPay.getPayeeName()
					if(ompPay.getPaymentType() != null){
						dtoObj.setPaymentMode(ompPay.getPaymentType().getValue());
					}else{
						dtoObj.setPaymentMode(null);
					}
					
					if(ompPay.getPaymentdate() != null){
						dtoObj.setPaymentDate(formatter.format(ompPay.getPaymentdate()));
					}else{
						dtoObj.setPaymentDate(null);
					}
					
					if(ompPay.getStatusId() != null){
						dtoObj.setPaymentStatus(ompPay.getStatusId().getProcessValue());
					}else{
						dtoObj.setPaymentStatus(null);
					}
					listOfDto.add(dtoObj);
					i++;
				}
			}
			return listOfDto;
		}
		
		
		@TransactionAttribute(TransactionAttributeType.REQUIRED)
		public ClaimDto submitClaimRegistration(OMPSearchClaimRegistrationTableDTO resultTask, ClaimDto claimDto) {
			claimDto.setCreatedBy(resultTask.getUsername() != null ? SHAUtils.getUserNameForDB(resultTask.getUsername()) : ""); 
			OMPClaim claim =  OMPClaimMapper.getInstance().getClaim(claimDto);
			OMPClaim createdClaim = getClaimforIntimation(claim.getIntimation().getKey());
			
			if(createdClaim != null) {
				claim.setCreatedBy(createdClaim.getCreatedBy());
				claim.setCreatedDate(createdClaim.getCreatedDate());
				claim.setKey(createdClaim.getKey());
				claim.setClaimId(createdClaim.getClaimId());
			}
			MastersValue climTyp = null;
			if(claimDto.getClaimType().getId() != null){
				climTyp = new MastersValue();
				climTyp.setKey(claimDto.getClaimType().getId());
				climTyp.setValue(claimDto.getClaimType().getValue());
				claim.setClaimType(climTyp);
			}else{
				// if the selected claimType is non Medical we are updating as reimbursement ClaimType.
				climTyp = new MastersValue();
				climTyp.setKey(402L);
				climTyp.setValue("Reimbursement");
				claim.setClaimType(climTyp);
			}
//			claim.setRegistrationRemarks(SHAConstants.CLAIM_MANUAL_REGISTRATION);
			/******
			 * As per Prakash, We have set the Date of admission from Intimation to
			 * Claim while creating the Claim .....
			 **************/
			claim.setDataOfAdmission(claimDto.getNewIntimationDto().getAdmissionDate());
			claim.setClaimedAmount(claimDto.getClaimedAmount());
			claim.setClaimRegisteredDate(new Date());
			claim.setLobId(ReferenceTable.OMP_LOB_KEY);
			claim.setPlaceOfAccident(claimDto.getPlaceOfEvent());
			claim.setHospitalisationFlag(claimDto.getHospitalisationFlag());
			claim.setNonHospitalisationFlag(claimDto.getNonHospitalisationFlag());
			claim.setPlaveOfVisit(claimDto.getPlaceOfvisit());
			claim.setLossDetails(claimDto.getLossDetails());
			claim.setLossTime(claimDto.getLossTime());
			claim.setPlaveOfLossOrDelay(claimDto.getPlaceLossDelay());
			if(claimDto.getHospitalCountry()!=null){
				claim.setCountryId(claimDto.getHospitalCountry().getId());
			}
			if(claimDto.getHospitalId()!= null){
				claim.setHospital(claimDto.getHospitalId());
			}
			Policy policyByKey = getPolicyByKey(claimDto.getNewIntimationDto().getPolicy().getKey());
			//MASClaimAdvancedProvision claimAdvProvision = getClaimAdvProvision(Long.valueOf(policyByKey.getHomeOfficeCode()));
			if(claimDto.getNewIntimationDto().getInsuredPatient()!=null){
				claim.setInsuredKey(claimDto.getNewIntimationDto().getInsuredPatient());
			}
			
			
//			Double amt =  claimAdvProvision.getAvgAmt();
			
			NewIntimationDto newIntimationDto = claimDto.getNewIntimationDto();
			
			/*Double amt = calculateAmtBasedOnBalanceSI(newIntimationDto.getPolicy().getKey(),newIntimationDto.getInsuredPatient().getInsuredId(),newIntimationDto.getInsuredPatient().getKey()
					 , claimAdvProvision.getAvgAmt() != null ? claimAdvProvision.getAvgAmt() : 0d, claimDto.getNewIntimationDto().getKey(),newIntimationDto);*/
			
//			claim.setProvisionAmount(amt);
//			claim.setClaimedAmount(amt);
			//claim.setCurrentProvisionAmount(claimDto.getDollarInitProvisionAmount());
			claim.setProvisionAmount(claimDto.getDollarInitProvisionAmount());
			claim.setInrConversionRate(claimDto.getInrConversionRate());
			claim.setInrTotalAmount(claimDto.getInrTotalAmount());
			
			claim.setNormalClaimFlag("O");
			
//			claim.setCurrentProvisionAmount(claimDto.getDollarInitProvisionAmount());
			//entityManager.refresh(claim);
			//claim = entityManager.find(OMPClaim.class, claim.getKey());
			
			ClaimDto responseClaim = new OMPClaimMapper().getClaimDto(claim);
			responseClaim.setNewIntimationDto(claimDto.getNewIntimationDto());
				
				String outCome = "";
				
//				if(claimDto.getStatusId().equals(ReferenceTable.CLAIM_SUGGEST_REJECTION_STATUS)){
				if(claimDto.getStatusId().equals(ReferenceTable.OMP_REGISTRATION_REJECTED)){
				outCome = SHAConstants.OUTCOME_FOR_MANUAL_SUGGEST_REJECTION;
				/*Stage stgObj = new Stage();
				stgObj.setKey(ReferenceTable.CLAIM_REGISTRATION_STAGE);
				Status statusObj = new Status();
				statusObj.setKey(ReferenceTable.CLAIM_SUGGEST_REJECTION_STATUS);*/
				Stage stgObj = new Stage();
                stgObj.setKey(ReferenceTable.CLAIM_REGISTRATION_STAGE);
                Status statusObj = new Status();
                statusObj.setKey(ReferenceTable.OMP_REGISTRATION_REJECTED);
				claim.setStage(stgObj);
				claim.setStatus(statusObj);
				claim.setProvisionAmount(0d);
				claim.setRejectionRemarks(claimDto.getSuggestedRejectionRemarks());
				//As per Raju.M comment if rejected, need to update Modified By
				
				}else{
					outCome = SHAConstants.OUTCOME_FOR_OMP_MANUAL_REGISTRATION;
					Stage stgObj = new Stage();
					stgObj.setKey(ReferenceTable.CLAIM_REGISTRATION_STAGE);
					Status statusObj = new Status();
					statusObj.setKey(ReferenceTable.CLAIM_REGISTERED_STATUS);
					claim.setStage(stgObj);
					claim.setStatus(statusObj);
					claim.setRegistrationRemarks(claimDto.getRegistrationRemarks());
				}
				claim.setModifiedBy(resultTask.getUserId());
				claim.setModifiedDate(new Date());
//				setBPMOutcome(resultTask, responseClaim);	

				create(claim);
				
				claimDto.setClaimId(claim.getClaimId());
				responseClaim.setClaimId(claim.getClaimId());	
				
				submitDBProcedureForRegistration(resultTask, claim, claimDto, outCome);   
				
				if(claim != null && claim.getClaimType() != null && claim.getClaimType().getKey() != null && (ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY).equals(claim.getClaimType().getKey())){
					if(claimDto.getStatusId() != null && claimDto.getStatusId().equals(ReferenceTable.INTIMATION_REGISTERED_STATUS)){
						initiateBPMNforReimbursement(claim);
//						autoRegisterFVR(claim.getIntimation(),resultTask.getUserId());
					}
				}
			responseClaim.setIntimationRemarks(claimDto.getIntimationRemarks());
			
			String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
			if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag) && claim.getIntimation().getHospital() != null) {
				try {
					Hospitals hospitalDetailsByKey = getHospitalDetailsByKey(claim.getIntimation().getHospital());
//					String provisionAmtInput = SHAUtils.getProvisionAmtInput(claim, hospitalDetailsByKey.getName(), String.valueOf(claim.getCurrentProvisionAmount().longValue()));
//					PremiaService.getInstance().updateProvisionAmount(provisionAmtInput);
					PremiaService.getInstance().getOMPPolicyLock(claim, hospitalDetailsByKey.getHospitalCode());
					updateProvisionAmountToPremia(claim);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			return responseClaim;
		}

		@SuppressWarnings("unchecked")
		public OMPClaim getOMPClaimByKey(Long argClaimKey) {
			List<OMPClaim> resultClaim = null;
			if (argClaimKey != null) {
				Query findByIntimationNum = entityManager.createNamedQuery("OMPClaim.findOMPByClaimKey").setParameter("claimkey", argClaimKey);
				try {
					resultClaim = (List<OMPClaim>) findByIntimationNum.getResultList();
					if(resultClaim != null && !resultClaim.isEmpty()){
						return resultClaim.get(0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}
		
		@SuppressWarnings("unchecked")
		public OMPReimbursement getOMPRodByKey(Long argRodKey) {
			List<OMPReimbursement> resultClaim = null;
			if (argRodKey != null) {
				Query findByIntimationNum = entityManager.createNamedQuery("OMPReimbursement.findByKey").setParameter("primaryKey", argRodKey);
				try {
					resultClaim = (List<OMPReimbursement>) findByIntimationNum.getResultList();
					if(resultClaim != null && !resultClaim.isEmpty()){
						return resultClaim.get(0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		public BeanItemContainer<SelectValue> getRejectionCategories(Long argProductKey, String argEventCode){
			System.out.println(argProductKey+"<------>"+argEventCode);
			List<MasOMPRejectionCategory> resultList = null;
			Query qry1 = entityManager.createNamedQuery("MasOMPRejectionCategory.findByCode").setParameter("productKey", argProductKey);
			qry1.setParameter("eventCode", argEventCode);
			resultList = (List<MasOMPRejectionCategory>) qry1.getResultList();
			
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			if(resultList != null){
				for (MasOMPRejectionCategory rec : resultList) {
					SelectValue selected = new SelectValue();
					selected.setId(rec.getKey());
					selected.setValue(rec.getRejectionReason());
					selectValueList.add(selected);
				}
			}
			
			Query qry2 = entityManager.createNamedQuery("MasOMPRejectionCategory.findByGen").setParameter("eventCode", "OMP-CVR-OTHR");
			resultList = null;
			resultList = (List<MasOMPRejectionCategory>) qry2.getResultList();
			if(resultList != null){
				for (MasOMPRejectionCategory rec : resultList) {
					SelectValue selected = new SelectValue();
					selected.setId(rec.getKey());
					selected.setValue(rec.getRejectionReason());
					selectValueList.add(selected);
				}
			}
			
			BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(selectValueList);
			return container;
		}
		
		public MastersEvents getEventTypeByKey(Long eventKey) {
			MastersEvents a_mastersValue = new MastersEvents();
			Query query = entityManager.createNamedQuery("MastersEvents.findByKey");
			query = query.setParameter("primaryKey", eventKey);
			List<MastersEvents> mastersValueList = query.getResultList();
			for (MastersEvents mastersValue : mastersValueList)
				a_mastersValue = mastersValue;
			return a_mastersValue;
		}
		
		public List<SelectValue> getRejectionIdByRodKey(Long argRodKey){
			List<SelectValue> selectedList =  new ArrayList<SelectValue>();
			SelectValue selVal = null;
			Query query = entityManager.createNamedQuery("OMPRodRejection.findByRODKey");
			query = query.setParameter("rodKey", argRodKey);
			List<OMPRodRejection> rejectionList = query.getResultList();
			for(OMPRodRejection rec : rejectionList){
				selVal = new SelectValue();
				selVal.setId(rec.getRejectionCategoryId());
				Query qry2 = entityManager.createNamedQuery("MasOMPRejectionCategory.findByRejKey").setParameter("rejKey", rec.getRejectionCategoryId());
				List<MasOMPRejectionCategory> resultList = (List<MasOMPRejectionCategory>) qry2.getResultList();
				if(resultList != null){
					for (MasOMPRejectionCategory cRec : resultList) {
						selVal.setValue(cRec.getEventDesc());
					}
				}
				selectedList.add(selVal);
			}
			return selectedList;
		}
		
		public BeanItemContainer<SelectValue> getPatientStatus() {
			Query query = entityManager.createNamedQuery("MastersValue.findByMasterTypeCodeWithStatus");
			query.setParameter("masterTypeCode", ReferenceTable.REIMBURSEMENT_PATIENT_STATUS);
			List<MastersValue> mastersValueList = query.getResultList();
			
			List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
			BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			SelectValue select = null;
			for (MastersValue value : mastersValueList) {
				select = new SelectValue();
				select.setId(value.getKey());
				select.setValue(value.getValue());
				selectValuesList.add(select);
			}
			mastersValueContainer.addAll(selectValuesList);

			return mastersValueContainer;
		}

		public List<PolicyNominee> getPolicyInsuredNomineeList(Long insuredKey) {
			List<PolicyNominee> resultList = new ArrayList<PolicyNominee>();
			try{
				Query query = entityManager.createNamedQuery("PolicyNominee.findByInsuredKey");
				query = query.setParameter("insuredKey", insuredKey);
				resultList = (List<PolicyNominee>) query.getResultList();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return resultList;
		}
		
		private MastersValue getMastersValueByKey(Long key){
			Query query = entityManager.createNamedQuery("MastersValue.findByKey");
			query = query.setParameter("parentKey", key);
			List<MastersValue> masterValueList = (List<MastersValue>)query.getResultList();
			if(null != masterValueList && !masterValueList.isEmpty()){
				return masterValueList.get(0);
			}
			return null;
		}
		
		@SuppressWarnings("unused")
		public void uploadCoveringLetterToDMs(ClaimDto claimDto){

			if(null != claimDto.getDocFilePath() && !claimDto.getDocFilePath().isEmpty())
			{
				String strUserName = claimDto.getModifiedBy();
				String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
				
				if(null != claimDto)
				{
					WeakHashMap<String, Object> dataMap = SHAUtils.uploadFileToDMS(claimDto.getDocFilePath());
					if(dataMap != null) {
						dataMap.put("intimationNumber",claimDto.getNewIntimationDto().getIntimationId());
						dataMap.put("claimNumber",claimDto.getClaimId());
						dataMap.put("filePath", claimDto.getDocFilePath());
						dataMap.put("docType", claimDto.getDocType());
						dataMap.put("docSources", SHAConstants.GENERATE_COVERING_LETTER);
						dataMap.put("createdBy", userNameForDB);
						saveOmpDocumentDetails(dataMap);
					}	
				}
			}
		}
		
		public void saveOmpDocumentDetails(WeakHashMap dataMap){
			
			OMPDocumentDetails ompDocument = new OMPDocumentDetails();
			String docToken = (String)dataMap.get("fileKey");
			ompDocument.setDocumentToken((null != docToken && !docToken.isEmpty())? Long.parseLong(docToken):null);
			ompDocument.setIntimationNumber((String)dataMap.get("intimationNumber"));
			ompDocument.setClaimNumber((String)dataMap.get("claimNumber"));
			ompDocument.setReimbursementNumber(dataMap.get("reimbursementNumber") != null ? (String)dataMap.get("reimbursementNumber") : null);
			ompDocument.setFileName(String.valueOf(dataMap.get("fileName")));
			ompDocument.setDocumentType((String)dataMap.get("docType"));
			ompDocument.setDocumentSource((String)dataMap.get("docSources"));
			ompDocument.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			ompDocument.setCreatedBy(dataMap.get("createdBy") != null ? (String)dataMap.get("createdBy") : null);
			entityManager.persist(ompDocument);
		}
		
		public MasterOMPEventProcedure getOmpClaimProcedureDetailsyByEventKey(Long eventKey) {
			
			MasterOMPEventProcedure resultObj = null;
			
			try{
				Query qry = entityManager.createNamedQuery("MasterOMPEventProcedure.findByEventKey");
				qry.setParameter("eventKey", eventKey);
				
				List<MasterOMPEventProcedure> resultList = qry.getResultList();
				
				if(resultList != null && !resultList.isEmpty()) {
					entityManager.refresh(resultList.get(0));
					resultObj = resultList.get(0);
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			
			return resultObj;			
		}
		
		public void generateAcknowledgeNo(ReceiptOfDocumentsDTO rodDTO) {
			 Long claimKey = rodDTO.getClaimDTO().getKey();
			 Long count = getCountOfAckByClaimKey(claimKey);
			 StringBuffer ackNoBuf = new StringBuffer();
			 Long lackCount = count + 001;
			 ackNoBuf.append("ACK/")
			 .append(rodDTO.getClaimDTO().getNewIntimationDto()
					 .getIntimationId()).append("/").append(lackCount);
			 rodDTO.getDocumentDetails().setAcknowledgementNumber(ackNoBuf.toString());
		 }
		
		public Long getCountOfAckByClaimKey(Long a_key) {
			Query query = entityManager
					.createNamedQuery("OMPDocAcknowledgement.CountAckByClaimKey");
			query = query.setParameter("claimkey", a_key);
			// Integer.parseInt(Strin)
			Long countOfAck = (Long) query.getSingleResult();
			return countOfAck;
		}
}
