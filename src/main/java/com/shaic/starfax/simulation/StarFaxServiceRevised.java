package com.shaic.starfax.simulation;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
//import com.shaic.ims.bpm.claim.corev2.Output;



















import oracle.sql.DATE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.common.APIService;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.domain.Claim;
import com.shaic.domain.DMSDocMetaData;
import com.shaic.domain.DocUploadToPremia;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.GpaPolicy;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasCpuLimit;
import com.shaic.domain.MasProductCpuRouting;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.PaayasPolicy;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.StarJioPolicy;
import com.shaic.domain.StarKotakPolicy;
import com.shaic.domain.Status;
import com.shaic.domain.TataPolicy;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.ZUAQueryHistoryTable;
import com.shaic.domain.preauth.CashlessWorkFlow;
import com.shaic.domain.preauth.ClaimAmountDetails;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.PreauthQuery;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.preauth.StageInformation;
import com.shaic.domain.preauth.StarFaxReverseFeed;
import com.shaic.domain.preauth.StarfaxProvisionHistory;
import com.shaic.ims.bpm.claim.DBCalculationService;


/**
 * @author Saravana kumar P
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class StarFaxServiceRevised {


private final Logger log = LoggerFactory.getLogger(StarFaxServiceRevised.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@Resource
	private UserTransaction utx;
	
	@EJB
	private IntimationService intimationService;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void processProcessAuthorizationPremiaReqWITHOUTFVRRevised(String batchSize){
		try {
			log.info("********* BATCH SIZE FOR DOC UPLOAD ******------> " + (batchSize != null ? String.valueOf(batchSize)  : "NULL"));
			List<DocUploadToPremia> docUploadPremiaList = fetchRecFromPremiaDocUploadTblWithoutFVR(batchSize);
			log.info("&&&&&&****** LIST TO BE PROCESSED fOR WITHOUT FVR ******&&&&&&&------>" + (docUploadPremiaList != null ? String.valueOf(docUploadPremiaList.size()) : "0"));
			//String success = startProcessRevised(docUploadPremiaList);
			startProcessRevised(docUploadPremiaList);	
		} catch(Exception e) {
			
		}
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void processProcessAuthorizationPremiaReqWITHOUTFVRRevisedForStub(String intimationNo){
		try {
			
			List<DocUploadToPremia> docUploadPremiaList = getUploadedDataDocumentDetails(intimationNo);
			log.info("&&&&&&****** LIST TO BE PROCESSED fOR WITHOUT FVR ******&&&&&&&------>" + (docUploadPremiaList != null ? String.valueOf(docUploadPremiaList.size()) : "0"));
			String success = startProcessRevised(docUploadPremiaList);
		} catch(Exception e) {
			e.printStackTrace();
			
		}
	}
	
	
	public List<DocUploadToPremia> getUploadedDataDocumentDetails(String intimationNo)
	{
		Query query = entityManager.createNamedQuery("DocUploadToPremia.findByIntimation");
		query = query.setParameter("intimationNo", intimationNo); 
		List<DocUploadToPremia> listOfDocUploadData = query.getResultList();
		if(null != listOfDocUploadData && !listOfDocUploadData.isEmpty())
		{
			for (DocUploadToPremia docUploadToPremia : listOfDocUploadData) {
			//	entityManager.refresh(docUploadToPremia);
			}
			return listOfDocUploadData;
		}
		return null;
	}
	
	
	public List<DocUploadToPremia> fetchRecFromPremiaDocUploadTblWithoutFVR(String batchSize)
	{ 
		Query query = entityManager
				.createNamedQuery("DocUploadToPremia.findByDocUploadWithoutFVR");
		if(batchSize != null) {
			query.setMaxResults(SHAUtils.getIntegerFromString(batchSize));
		}
		List<DocUploadToPremia> docUploadPremiaList = query.getResultList();
		log.info("********* COUNT FOR DOC UPLAOD ******------> " + docUploadPremiaList != null ? String.valueOf(docUploadPremiaList.size()) : "NO RECORDS TO DOC UPLOAD");
		return docUploadPremiaList;
	}
	
	public Intimation getIntimationByKey(Long intimationKey) {
		Query query = entityManager.createNamedQuery("Intimation.findByKey");
		query = query.setParameter("intiationKey", intimationKey);
		List<Intimation> intimationList = query.getResultList();
		if(null != intimationList && !intimationList.isEmpty())
		{
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);
		}
		return null;
	}
	
	public Intimation getIntimationObject(String strIntimationNo) {
		Query query = entityManager.createNamedQuery("Intimation.findByIntimationNumber");
		query = query.setParameter("intimationNo",strIntimationNo);
		@SuppressWarnings("unchecked")
		List<Intimation>	resultList = query.getResultList();
		if (null != resultList && !resultList.isEmpty()){
			return resultList.get(0);
		}
		return null;
	}
	
	/**
	 * Method to obtain claim object 
	 * with given intimation number.
	 * */
	public Claim getClaimObject(Long lIntimationKey) {
		TypedQuery<Claim> query = entityManager.createNamedQuery("Claim.findByIntimationKey", Claim.class);
		query.setParameter("intimationKey", lIntimationKey);
		List<Claim>	resultList = query.getResultList();
		if (null != resultList && 0 != resultList.size()) {
			entityManager.refresh(resultList.get(0));
			return resultList.get(0);
		}
		return null;
	}
	
	private String getProvisionAmountPercentage(String a_key) {
		Query query = entityManager.createNamedQuery("MastersValue.findByMasterListKey");
		query = query.setParameter("parentKey",a_key);
		List<MastersValue> mastersValueList = query.getResultList();
		if(null != mastersValueList && !mastersValueList.isEmpty()){
			MastersValue masValue = mastersValueList.get(0);
			String masterValue  = null;
			if(null != masValue){
				masterValue = masValue.getValue();
			}
			return masterValue;
		}
		return null;
	}
	
	/*private Boolean isWaitingForPreauth(Intimation objIntimation){
		try {	
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			mapValues.put(SHAConstants.INTIMATION_NO, objIntimation.getIntimationId());
			mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.WAITING_FOR_PREATH_QUEUE);
			
			Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
			
			DBCalculationService db = new DBCalculationService();
			 List<Map<String, Object>> taskProcedure = db.getTaskProcedure(setMapValues);
			if (taskProcedure != null && !taskProcedure.isEmpty()){
				return true;
			} 
			return false;
		}catch(Exception e){
			return false;
		}

	}*/
	
	private Boolean isDBWaitingForPreauth(Intimation objIntimation){
		Map<String, Object> mapValues = new WeakHashMap<String, Object>();
		mapValues.put(SHAConstants.INTIMATION_NO, objIntimation.getIntimationId());
		
//		Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
		Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
		
		DBCalculationService db = new DBCalculationService();
		log.info("@@@@CALL GET TASK PROCEDURE STARTING TIME  --------> "+System.currentTimeMillis());
		 List<Map<String, Object>> taskProcedure = db.revisedGetTaskProcedureForBatch(setMapValues);
		 log.info("@@@@CALL GET TASK PROCEDURE ENDING TIME  --------> "+System.currentTimeMillis());
		if (taskProcedure != null && !taskProcedure.isEmpty()){
			return true;
		} 
		return false;
		
	}
	
	public Boolean isIntimationExist(String strIntimationNo) {
		Query query = entityManager.createNamedQuery("Intimation.findByIntimationNumber");
		query = query.setParameter("intimationNo",strIntimationNo);
		List<Intimation>	resultList = query.getResultList();
		if (null != resultList && !resultList.isEmpty()){
			return true;
		}
		return false;
	}
	
	/**
	 * Method to obtain hospital object 
	 * with given intimation number.
	 * */
	public Hospitals getHospitalObject(Long hospitalKey) {
		TypedQuery<Hospitals> query = entityManager.createNamedQuery("Hospitals.findByKey", Hospitals.class);
		query.setParameter("key", hospitalKey);
		List<Hospitals>	resultList = query.getResultList();
		if (null != resultList && 0 != resultList.size()) {
			return resultList.get(0);
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
	
	public Boolean isBalanceSIAvailable(Long insuredId , Long policyKey) {
		DBCalculationService dbCalculationService = new DBCalculationService();
		
		Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(insuredId.toString(), policyKey,"H");

		if(insuredSumInsured > 0){
			return true;
		}
		return false;

	}
	
//	public void invokeBPMProcessForPreMedPreAuth(Claim claimObject, Intimation objIntimation, String strType) throws Exception {
//		ReceivePreAuthTask receivePreauthTask = BPMClientContext.getPreAuthReceived(BPMClientContext.BPMN_TASK_USER, "Star@123");
//		
//		PayloadBOType payloadBO = new PayloadBOType();
//		IntimationType objIntimationType = new IntimationType();
//		objIntimationType.setIntimationNumber(objIntimation.getIntimationId());
//		payloadBO.setIntimation(objIntimationType);
//		com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = receivePreauthTask.getTasks(BPMClientContext.BPMN_TASK_USER, new Pageable(), payloadBO);
//		List<HumanTask> humanTaskList = tasks.getHumanTasks();
//		
//		if(null != humanTaskList && !humanTaskList.isEmpty()) {
//			SubmitReceivePreAuthTask submitReceivePreAuthTask = BPMClientContext.getSubmitPreAuthReceivedTask(BPMClientContext.BPMN_TASK_USER, "Star@123");
//			for (HumanTask humanTask : humanTaskList) {
//				PayloadBOType payload = humanTask.getPayloadCashless();
//				if(null != payload) {
//					if (null != claimObject) {
//					
//						//payloadBO.setIntimation(a_message);
//						//payloadBO.setClaim(claimType);
//						//payloadBO.setClaimRequest(claimReqType);
//						//payloadBO.setHospitalInfo(hospitalInfoType);
//						//payloadBO.setPolicy(policyType);
//						//payloadBO.setPreAuthReq(preauthReqType);;
//						
//						Intimation intimation = claimObject.getIntimation();
//						IntimationType intimationType = payload.getIntimation();
//						IntimationRule intimationRule = new IntimationRule();
//						//IntimationMessage intimationMessage = new IntimationMessage();
//						/*IntimationType intimationType = new IntimationType();*/
//						//PolicyType policyType = new PolicyType();
//						
//						//PaymentInfoType paymentInfoType = new PaymentInfoType();
//						PaymentInfoType paymentInfoType = payload.getPaymentInfo();
//						//HospitalInfoType hospitalInfoType = new HospitalInfoType();
//						HospitalInfoType hospitalInfoType = payload.getHospitalInfo();
//						//PreAuthReqType preauthReqType = new PreAuthReqType();
//						
//						/*ClaimType claimType = new ClaimType();
//						ClaimRequestType claimReqType = new ClaimRequestType();*/
//						ClaimType claimType = payload.getClaim();
//						ClaimRequestType claimReqType = payload.getClaimRequest();
//						
//						Output output = new Output();
//						//PreAuthReq preAuthReq = new PreAuthReq();
//						//PreAuthReqDetails preAuthReqDetails = new PreAuthReqDetails();
//						
//						if(null != intimationType)
//							intimationType.setIntimationNumber(objIntimation.getIntimationId());
//						if(null != paymentInfoType)
//						{
//							paymentInfoType.setClaimedAmount(claimObject.getClaimedAmount());
//							paymentInfoType.setProvisionAmount(claimObject.getProvisionAmount());
//						}
//						//hospitalInfoType.setHospitalType(objHosp.getHospitalTypeName().toUpperCase());
//						intimationType.setIntimationSource(intimation.getIntimationSource() != null ? intimation.getIntimationSource().getValue() : "");
//						if(null != hospitalInfoType)
//							hospitalInfoType.setHospitalType(intimation.getHospitalType().getValue()); 
//						{
//							if(intimation.getHospital() != null){
//								Hospitals hospitalById = getHospitalById(intimation.getHospital());
//								if(hospitalById.getNetworkHospitalTypeId() != null && hospitalById.getNetworkHospitalTypeId().equals(ReferenceTable.NETWORK_HOSPITAL)){
//									hospitalInfoType.setNetworkHospitalType(SHAConstants.NETWORK_HOSPITAL);
//								} else if (hospitalById.getNetworkHospitalTypeId() != null && hospitalById.getNetworkHospitalTypeId().equals(ReferenceTable.AGREED_NETWORK_HOSPITAL)){
//									hospitalInfoType.setNetworkHospitalType(SHAConstants.AGREED_NETWORK_HOSPITAL);
//								} else if (hospitalById.getNetworkHospitalTypeId() != null && hospitalById.getNetworkHospitalTypeId().equals(ReferenceTable.GREEN_NETWORK_HOSPITAL)){
//									hospitalInfoType.setNetworkHospitalType(SHAConstants.GREEN_NETWORK_HOSPITAL);
//								} else {
//									hospitalInfoType.setNetworkHospitalType("");
//								}
//	//							hospitalInfoType.setNetworkHospitalType(hospitalById.getNetworkHospitalType() != null ? hospitalById.getNetworkHospitalType() : "");
//							}
//						}
//						
//						if(null != claimReqType){
//							claimReqType.setOption(claimObject.getStage() != null ? claimObject.getStage().getStageName() : " ");
//							if(("PREAUTHRECEIVED").equals(strType)){
//								claimReqType.setOption(claimObject.getStage() != null ? claimObject.getStage().getStageName() : " ");
//								
//							}
//						
//						if(null != intimation.getCpuCode()){
//							if(null != intimation.getCpuCode().getCpuCode()) {
//								claimReqType.setCpuCode(String.valueOf(intimation.getCpuCode().getCpuCode()));
//							}
//						}
//						
//						}
//						if(null != claimType){
//							claimType.setClaimType(claimObject.getClaimType().getValue().toUpperCase());
//						}
//	
//						Boolean isBalSIAvailable = isBalanceSIAvailable(claimObject.getIntimation().getInsured().getInsuredId(),claimObject.getIntimation().getPolicy().getKey());
//						if(null != intimationType) {
//							intimationType.setIsBalanceSIAvailable(isBalSIAvailable);
//							intimationType.setIsPolicyValid(intimationRule.isPolicyValid(intimation));
//							intimationType.setKey(intimation.getKey());
//							intimationType.setStatus(intimation.getStatus().getProcessValue());
//						}
//						PreAuthReqType preauthReqType = payload.getPreAuthReq();
//						if(null != preauthReqType) {
//							preauthReqType.setIsFVRReceived(false);
//							preauthReqType.setOutcome(strType);
//							preauthReqType.setResult(strType);
//							preauthReqType.setPreAuthAmt(0d); 
//							preauthReqType.setKey(claimObject.getKey());
//						}
//						
//						// Newly added for Query process (newly implemented)...... 
//						QueryType queryType = new QueryType();
//						queryType.setStatus("No Query");
//						payload.setQuery(queryType);
//						
//						PolicyType policyType = payload.getPolicy();
//						if(null != policyType) {
//							policyType.setPolicyId(objIntimation.getPolicy().getPolicyNumber());
//						}
//						
//	                    ClassificationType classificationType = new ClassificationType();
//	                    
//	                    Insured insured = intimation.getInsured();
//	        
//	        			if(claimObject != null && claimObject.getIsVipCustomer() != null && claimObject.getIsVipCustomer().equals(1l)){
//	        				classificationType.setPriority(SHAConstants.VIP_CUSTOMER);
//	        			} else if (insured != null && insured.getInsuredAge() != null && insured.getInsuredAge()>60){
//	        				classificationType.setPriority(SHAConstants.SENIOR_CITIZEN);
//	        			} else {
//	        				classificationType.setPriority(SHAConstants.NORMAL);
//	        			}
//	        		
//	        			classificationType.setType(SHAConstants.TYPE_FRESH);
//	        			
//	        			if(claimObject != null){
//	        				classificationType.setSource(claimObject.getStage().getStageName());
//	        			}
//	        			
//						payload.setIntimation(intimationType);
//						payload.setPolicy(policyType);
//						payload.setPreAuthReq(preauthReqType);
//						payload.setClaim(claimType);
//						payload.setClaimRequest(claimReqType);
//						payload.setPaymentInfo(paymentInfoType);
//						payload.setHospitalInfo(hospitalInfoType);
//						payload.setClassification(classificationType);
//						humanTask.setPayloadCashless(payload);
//						humanTask.setOutcome("APPROVE");
//							
//						log.info("BPMN CALLLINGGGGGGG_________________");
//						submitReceivePreAuthTask.execute(BPMClientContext.BPMN_TASK_USER, humanTask);
//						log.info("BPMN CALLLED SUCCESSFULLY_________________");
//						if(intimation != null)
//						log.info("INTIMATION MOVED TO FIRST LEVEL PREAUTH --->"+intimation.getIntimationId());
//					}
//				}
//			}
//		}
//		
//	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void updateIntimation(Intimation intimation) {
		if (null != intimation) {
			entityManager.merge(intimation);
			entityManager.flush();
		}
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
	
	/**
	 * The below method will return the minimum of Balance SI 
	 * and provision amt.
	 * */
	
	public Double calculateAmtBasedOnBalanceSI(Long policyKey , Long insuredId, Long insuredKey, Double provisionAmout,Long intimationKey,Claim objClaim) {
		DBCalculationService dbCalculationService = new DBCalculationService();
		Double insuredSumInsured = 0d;
		if(null != objClaim.getIntimation() && null != objClaim.getIntimation().getPolicy() && 
				null != objClaim.getIntimation().getPolicy().getProduct() && 
				null !=objClaim.getIntimation().getPolicy().getProduct().getKey() &&
				!(ReferenceTable.getGPAProducts().containsKey(objClaim.getIntimation().getPolicy().getProduct().getKey()))){	
		
			insuredSumInsured = dbCalculationService.getInsuredSumInsured(insuredId.toString(), policyKey,objClaim.getIntimation().getInsured().getLopFlag());
		}
		else
		{
			 insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(insuredId.toString(), policyKey);
		}
		System.out.println("--policy key---"+policyKey+"----insuredSumInsured----"+insuredSumInsured+"----insured key----"+insuredKey);
		/**
		 * For PA lob type, claim key needs to be passed for balance si procedure. Based on this only, balance si will be returned for PA claims
		 * which will help in further processing of cashless pa claims. This is  done as per DB team suggestion.
		 * 
		 * The else block is for health , which remains untouched.
		 * */
		if(null != objClaim && null != objClaim.getIntimation() && null != objClaim.getIntimation().getProcessClaimType() &&  (SHAConstants.PA_TYPE).equalsIgnoreCase(objClaim.getIntimation().getProcessClaimType()))
		{
			if(null != objClaim.getIntimation() && null != objClaim.getIntimation().getPolicy() && 
					null != objClaim.getIntimation().getPolicy().getProduct() && 
					null != objClaim.getIntimation().getPolicy().getProduct().getKey() &&
					!(ReferenceTable.getGPAProducts().containsKey(objClaim.getIntimation().getPolicy().getProduct().getKey()))){	
				
			return Math.min(dbCalculationService.getBalanceSI(policyKey, insuredKey, objClaim.getKey(), insuredSumInsured,intimationKey).get(SHAConstants.TOTAL_BALANCE_SI) , provisionAmout);
			}
			else
			{
				return Math.min(dbCalculationService.getGPABalanceSI(policyKey, insuredKey, objClaim.getKey(), insuredSumInsured,intimationKey).get(SHAConstants.TOTAL_BALANCE_SI) , provisionAmout);
			}
		}
		else
		{
			if(null != objClaim.getIntimation() && null != objClaim.getIntimation().getPolicy() && 
					null != objClaim.getIntimation().getPolicy().getProduct() && 
					null != objClaim.getIntimation().getPolicy().getProduct().getKey() &&
					(ReferenceTable.getGMCProductList().containsKey(objClaim.getIntimation().getPolicy().getProduct().getKey()))){
				return Math.min(dbCalculationService.getBalanceSIForGMC(policyKey, insuredKey, objClaim.getKey()), provisionAmout);
			}else{
				return Math.min(dbCalculationService.getBalanceSI(policyKey, insuredKey, 0l, insuredSumInsured,intimationKey).get(SHAConstants.TOTAL_BALANCE_SI) , provisionAmout);	
			}
		}
	}
	
	private void updateClaimObject(Claim objClaim,Intimation objIntimation, String provisionPercentage , Long eStarFaxAmt) {
		Double provisionAmt = (Double.valueOf(provisionPercentage)*eStarFaxAmt)/100f;
		Double amt = calculateAmtBasedOnBalanceSI(objIntimation.getPolicy().getKey(),objIntimation.getInsured().getInsuredId(),objIntimation.getInsured().getKey()
				 , provisionAmt,objIntimation.getKey(),objClaim);
		Status claimStatus = new Status();
		claimStatus.setKey(ReferenceTable.CLAIM_REGISTERED_STATUS);
		objClaim.setStatus(claimStatus);
		objClaim.setClaimRegisteredDate((new Timestamp(System
				.currentTimeMillis())));
		objClaim.setProvisionAmount(amt);
		objClaim.setCurrentProvisionAmount(amt);
		objClaim.setClaimedAmount(amt);
		objClaim.setNormalClaimFlag("N");
		objClaim.setDocumentReceivedDate(new Timestamp(System.currentTimeMillis()));
		entityManager.merge(objClaim);
		entityManager.flush();
		entityManager.refresh(objClaim);
		
		log.info("CLAIM STATUS -----> AFTER RECEIVED PREAUTH---->" + (objClaim.getStatus() !=  null ? String.valueOf(objClaim.getStatus().getKey()) : "NO STATUS")  + (objClaim != null ? String.valueOf(objClaim.getClaimId()) : "NO CLAIM NO"));

		Hospitals hospitalDetailsByKey = getHospitalDetailsByKey(objClaim.getIntimation().getHospital());
		//Claim claim = getClaimObject(objClaim.getKey());
		if(null != amt){
			
			if(objClaim != null && objClaim.getIntimation() != null && objClaim.getIntimation().getIntimationId() != null){
				
				log.info("@@@@@@@@@ PROVISION UPDATED STARTING TIME FOR DOCUMENT UPLOAD @@@@@@@@@@-----> "+"-----> " +objClaim.getIntimation().getIntimationId()+"-----> "+ System.currentTimeMillis());
				
				}
			
			String provisionAmtInput = SHAUtils.getProvisionAmtInput(objClaim, hospitalDetailsByKey.getName(), String.valueOf(amt));
			Integer iProvisionAmt = SHAUtils.getIntegerFromString(provisionAmtInput);
			if(null != iProvisionAmt) {
				/*APIService apiService = new APIService();
				apiService.updateProvisionAmountToPremia(String.valueOf(provisionAmtInput));*/
				updateProvisionAmountDetails(objClaim, amt.intValue());
			}
			
			if(objClaim != null && objClaim.getIntimation() != null && objClaim.getIntimation().getIntimationId() != null){
				
				log.info("@@@@@@@@@ PROVISION UPDATED ENDING TIME FOR DOCUMENT UPLOAD @@@@@@@@@@-----> "+"-----> " +objClaim.getIntimation().getIntimationId()+"-----> "+ System.currentTimeMillis());
				
				}
		}
		PremiaService.getInstance().getPolicyLock(objClaim, hospitalDetailsByKey.getHospitalCode());
	}
	
/*	public void invokeBPMProcessForPreMedPreAuth(Claim claimObject, Intimation objIntimation, String strType) throws Exception {
		ReceivePreAuthTask receivePreauthTask = BPMClientContext.getPreAuthReceived(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
		
		PayloadBOType payloadBO = new PayloadBOType();
		IntimationType objIntimationType = new IntimationType();
		objIntimationType.setIntimationNumber(objIntimation.getIntimationId());
		payloadBO.setIntimation(objIntimationType);
		com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = receivePreauthTask.getTasks(BPMClientContext.BPMN_TASK_USER, new Pageable(), payloadBO);
		List<HumanTask> humanTaskList = tasks.getHumanTasks();
		
		if(null != humanTaskList && !humanTaskList.isEmpty()) {
			SubmitReceivePreAuthTask submitReceivePreAuthTask = BPMClientContext.getSubmitPreAuthReceivedTask(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
			for (HumanTask humanTask : humanTaskList) {
				PayloadBOType payload = humanTask.getPayloadCashless();
				if(null != payload) {
					if (null != claimObject) {
						
						Intimation intimation = claimObject.getIntimation();
						IntimationType intimationType = payload.getIntimation();
						IntimationRule intimationRule = new IntimationRule();
						PaymentInfoType paymentInfoType = payload.getPaymentInfo();
						HospitalInfoType hospitalInfoType = payload.getHospitalInfo();
						ClaimType claimType = payload.getClaim();
						ClaimRequestType claimReqType = payload.getClaimRequest();
						
//						Output output = new Output();
						//PreAuthReq preAuthReq = new PreAuthReq();
						//PreAuthReqDetails preAuthReqDetails = new PreAuthReqDetails();
						
						if(null != intimationType)
							intimationType.setIntimationNumber(objIntimation.getIntimationId());
						if(null != paymentInfoType)
						{
							paymentInfoType.setClaimedAmount(claimObject.getClaimedAmount());
							paymentInfoType.setProvisionAmount(claimObject.getProvisionAmount());
						}
						//hospitalInfoType.setHospitalType(objHosp.getHospitalTypeName().toUpperCase());
						intimationType.setIntimationSource(intimation.getIntimationSource() != null ? intimation.getIntimationSource().getValue() : "");
						if(null != hospitalInfoType)
							hospitalInfoType.setHospitalType(intimation.getHospitalType().getValue());
						{
							if(intimation.getHospital() != null){
								Hospitals hospitalById = getHospitalById(intimation.getHospital());
								if(hospitalById.getNetworkHospitalTypeId() != null && hospitalById.getNetworkHospitalTypeId().equals(ReferenceTable.NETWORK_HOSPITAL)){
									hospitalInfoType.setNetworkHospitalType(SHAConstants.NETWORK_HOSPITAL);
								}else if(hospitalById.getNetworkHospitalTypeId() != null && hospitalById.getNetworkHospitalTypeId().equals(ReferenceTable.AGREED_NETWORK_HOSPITAL)){
									hospitalInfoType.setNetworkHospitalType(SHAConstants.AGREED_NETWORK_HOSPITAL);
								}else if(hospitalById.getNetworkHospitalTypeId() != null && hospitalById.getNetworkHospitalTypeId().equals(ReferenceTable.GREEN_NETWORK_HOSPITAL)){
									hospitalInfoType.setNetworkHospitalType(SHAConstants.GREEN_NETWORK_HOSPITAL);
								}else{
									hospitalInfoType.setNetworkHospitalType("");
								}
	//							hospitalInfoType.setNetworkHospitalType(hospitalById.getNetworkHospitalType() != null ? hospitalById.getNetworkHospitalType() : "");
							}
						}
						
						if(null != claimReqType)
						{
						claimReqType.setOption(claimObject.getStage() != null ? claimObject.getStage().getStageName() : " ");
						
						if(("PREAUTHRECEIVED").equals(strType)){
							
							claimReqType.setOption(claimObject.getStage() != null ? claimObject.getStage().getStageName() : " ");
							
						}
						
						if(null != intimation.getCpuCode())
						{
							if(null != intimation.getCpuCode().getCpuCode())
							{
								claimReqType.setCpuCode(String.valueOf(intimation.getCpuCode().getCpuCode()));
							}
						}
						
						}
						if(null != claimType)
						{
	//					hospitalInfoType.setNetworkHospitalType(intimation.get);
						claimType.setClaimType(claimObject.getClaimType().getValue().toUpperCase());
						
						}
	
						Boolean isBalSIAvailable = isBalanceSIAvailable(claimObject.getIntimation().getInsured().getInsuredId(),claimObject.getIntimation().getPolicy().getKey());
						if(null != intimationType)
						{
							intimationType.setIsBalanceSIAvailable(isBalSIAvailable);
							intimationType.setIsPolicyValid(intimationRule.isPolicyValid(intimation));
							intimationType.setKey(intimation.getKey());
						
							intimationType.setStatus(intimation.getStatus().getProcessValue());
						}
						PreAuthReqType preauthReqType = payload.getPreAuthReq();
						if(null != preauthReqType) {
							preauthReqType.setIsFVRReceived(false);
							preauthReqType.setOutcome(strType);
							preauthReqType.setResult(strType);
							preauthReqType.setPreAuthAmt(0d); 
							preauthReqType.setKey(claimObject.getKey());
						}
						// Newly added for Query process (newly implemented)...... 
						QueryType queryType = new QueryType();
						queryType.setStatus("No Query");
						payload.setQuery(queryType);
						
						PolicyType policyType = payload.getPolicy();
						if(null != policyType) {
							policyType.setPolicyId(objIntimation.getPolicy().getPolicyNumber());
						}
						
	                   ClassificationType classificationType = new ClassificationType();
	                    
	                   Insured insured = intimation.getInsured();
	        
	        			if(claimObject != null && claimObject.getIsVipCustomer() != null && claimObject.getIsVipCustomer().equals(1l)){
	        				classificationType.setPriority(SHAConstants.VIP_CUSTOMER);
	        			} else if(insured != null && insured.getInsuredAge() != null && insured.getInsuredAge()>60){
	        				classificationType.setPriority(SHAConstants.SENIOR_CITIZEN);
	        			} else {
	        				classificationType.setPriority(SHAConstants.NORMAL);
	        			}
	        		
	        			classificationType.setType(SHAConstants.TYPE_FRESH);
	        			
	        			if(claimObject != null){
	        				classificationType.setSource(claimObject.getStage().getStageName());
	        			}
	        			
						payload.setIntimation(intimationType);
						payload.setPolicy(policyType);
						payload.setPreAuthReq(preauthReqType);
						payload.setClaim(claimType);
						payload.setClaimRequest(claimReqType);
						payload.setPaymentInfo(paymentInfoType);
						payload.setHospitalInfo(hospitalInfoType);
						payload.setClassification(classificationType);
						humanTask.setPayloadCashless(payload);
						humanTask.setOutcome("APPROVE");
						log.info("&*BPMN CALLLING____________");
						submitReceivePreAuthTask.execute(BPMClientContext.BPMN_TASK_USER, humanTask);
						log.info("&*BPMN CALLLING COMPLETED SUCCESSFULLY____________");
						if(intimation != null)
						log.info("INTIMATION MOVED TO FIRST LEVEL PREAUTH --->"+ intimation.getIntimationId());
					}
				}
			}
		}
		
	}*/
	
	public Map<String, String> processPreMedical(String strIntimationNo, Intimation objIntimation,  Long estarFaxAmt,String claimRequest,String strType,Date pfdUpFfaxSubmitDate, DocUploadToPremia docUploadToPremia) throws Exception {
		Map <String , String> statusMap = new HashMap<String, String>();
		String strIntimationStatus = "";
		if(isIntimationExist(strIntimationNo)) {
			strIntimationStatus = objIntimation.getStatus().getProcessValue();
			Claim objClaim;
			String provisionAmtPercentage = getProvisionAmountPercentage(ReferenceTable.PROVISION_AMOUNT_PERCENTAGE);
			/**
			 * If the intimation Id status is SUBMITTED, further processing is 
			 * done. Else error message is displayed.
			 * */
			
			if(SHAConstants.SUBMITTED.equalsIgnoreCase(strIntimationStatus) || SHAConstants.CLAIM_WISE_APPROVAL.equalsIgnoreCase(strIntimationStatus)) {
				/**
				 * Datas from VW_HOSPITALS is loaded , since 
				 * hosptial type is required when data is 
				 * submitted to BPM. Hence with 
				 * HOSPITAL_NAME_ID column value, we populate 
				 * hospital data from VW_HOSPITAL table d.
				 * 
				 * */
				//Hospitals hospitalObject = getHospitalObject(objIntimation.getHospital());
				log.info("&&&&&&&INTIMATION STATUS IS SUBMITTED &&&&&");

				if(SHAConstants.REGISTERED.equalsIgnoreCase(objIntimation.getRegistrationStatus())) {
					log.info("&&&&&&&INTIMATION IS REGISTRED &&&&&");
					objClaim = getClaimObject(objIntimation.getKey());
					if(null != objClaim) {			
						
						objIntimation.setRegistrationType("MANUAL");
						
						setProcessingCpuCodeBasedOnLimit(objIntimation,
								estarFaxAmt);
						
						updateIntimation(objIntimation);
						updateClaimObject(objClaim,objIntimation, provisionAmtPercentage,estarFaxAmt);
						
						if(("CASHLESS").equals(claimRequest)) {
							log.info("*****CASHELESS CLAIM --> INVOKING PROCESS FOR PREMED PREAUTH ******" + objIntimation.getIntimationId());
//							invokeBPMProcessForPreMedPreAuth(objClaim,objIntimation, strType);
							submitReceivePreauthToDBprocedure(objClaim, estarFaxAmt,pfdUpFfaxSubmitDate,docUploadToPremia);
						}
						
						statusMap.put("1","Intimation '"+ strIntimationNo +"' is successfully submitted to BPMN for further processing");
						statusMap.put(SHAConstants.CLAIM_NUMBER, objClaim.getClaimId());
						statusMap.put(SHAConstants.INTIMATION_NUMBER, objIntimation.getIntimationId());
					}
				} else if(null == objIntimation.getRegistrationStatus() || ("").equals(objIntimation.getRegistrationStatus())) {
					/**
					 * Service for invoking claim registration needs to be added.
					 * */
					log.info("**** REGISTRATION STATUS IS EMPTY OR NULLLL *****");
					//objClaim = populateClaimObject(objIntimation,claimRequest);
					objClaim = getClaimObject(objIntimation.getKey());
					if(null != objClaim) {
//						/createClaim(objClaim);
						
						objIntimation.setRegistrationType("AUTO");
						objIntimation.setRegistrationStatus(SHAConstants.REGISTERED);
						MastersValue claimMaster = new  MastersValue();
						claimMaster.setKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
						objIntimation.setClaimType(claimMaster);
						
						setProcessingCpuCodeBasedOnLimit(objIntimation,
								estarFaxAmt);
						
						updateIntimation(objIntimation);
						updateClaimObject(objClaim, objIntimation, provisionAmtPercentage,estarFaxAmt);
						
						if(("CASHLESS").equals(claimRequest)){
							log.info("*****CASHELESS CLAIM --> INVOKING PROCESS FOR PREMED PREAUTH ******" + objIntimation.getIntimationId());
//							invokeBPMProcessForPreMedPreAuth(objClaim,objIntimation, strType);
							submitReceivePreauthToDBprocedure(objClaim, estarFaxAmt,pfdUpFfaxSubmitDate,docUploadToPremia);
						}
						
						if(objClaim != null) {
							statusMap.put("1","Intimation '"+strIntimationNo+"' is successfully submitted to BPMN for further processing");
							statusMap.put(SHAConstants.CLAIM_NUMBER, objClaim.getClaimId());
							statusMap.put(SHAConstants.INTIMATION_NUMBER, objIntimation.getIntimationId());
						}
					} else {
						statusMap.put("2","Create Claim before processing pre authorization");
					}
				} else {
					statusMap.put("2",objIntimation.getRegistrationStatus());
				}
			} else {
				statusMap.put("3", strIntimationStatus);
			}
		} else {
			log.debug("**************THIS INTIMATION IS ALREADY EXISTING IN OUR DB PLEASE CEHCK------>" + strIntimationNo);
			statusMap.put("4", strIntimationNo);
		}
		log.info("#############AFTER INVOKING BPMN PROCESS PREMED PREAUTH------>" + strIntimationNo + " ---->STATUS MAP-->" + statusMap.toString());
		return statusMap;
	}


	private Intimation setProcessingCpuCodeBasedOnLimit(Intimation objIntimation,
			Long estarFaxAmt) {
		TmpCPUCode existingCpuCode = objIntimation.getCpuCode();
		//added foe CR GMC CPU Routing GLX2020075
			MasProductCpuRouting gmcRoutingProduct= getMasProductForGMCRouting(objIntimation.getPolicy().getProduct().getKey());
			if(gmcRoutingProduct != null){
				if(objIntimation.getPolicy().getHomeOfficeCode() != null) {
					OrganaizationUnit branchOffice = getInsuredOfficeNameByDivisionCode(objIntimation.getPolicy().getHomeOfficeCode());
					if(branchOffice != null && branchOffice.getCpuCode() != null){
						String officeCpuCode = branchOffice.getCpuCode();
						Long processingCpuCode = getProcessingCpuCode(estarFaxAmt, Long.valueOf(officeCpuCode), SHAConstants.PROCESSING_CPU_CODE_GMC);
						if(processingCpuCode != null){
							TmpCPUCode masCpuCode = getMasCpuCode(processingCpuCode);
							objIntimation.setCpuCode(masCpuCode);
							entityManager.merge(objIntimation);
							entityManager.flush();
							//saveStarFaxReverseFeed(objIntimation,existingCpuCode.getCpuCode(), masCpuCode.getCpuCode(),Double.valueOf(estarFaxAmt),"SYSTEM");
						}else{
							Boolean paayasPolicyDetails = getPaayasPolicyDetails(objIntimation.getPolicy().getPolicyNumber());
							TmpCPUCode masCpuCode = null;
							//added foe CR GMC CPU Routing GLX2020075
							masCpuCode = objIntimation.getCpuCode();
							
							if(paayasPolicyDetails){
								 masCpuCode = getCpuDetails(ReferenceTable.PAAYAS_CPU_CODE);
							}else{
								
								Boolean jioPolicy = getJioPolicyDetails(objIntimation.getPolicy().getPolicyNumber());
								if(jioPolicy){
									 masCpuCode = getCpuDetails(ReferenceTable.JIO_CPU_CODE);
								}
								//GLX2020075 commented for this GMC CPU CR since CPU Routing for GMC already done on above code -noufel
//								else{
//									masCpuCode = getCpuDetails(ReferenceTable.GMC_CPU_CODE);
//								}
								
								
							}
							
							Long tataPolicyCpuCode = getTataPolicy(objIntimation.getPolicy().getPolicyNumber());
							if(tataPolicyCpuCode != null){
								 masCpuCode = getMasCpuCode(tataPolicyCpuCode);
							}
							
							//added for CR GMC CPU Routing GLX2020075 only for kotak cpu routing
							Boolean kotakPolicy = getKotakPolicyDetails(objIntimation.getPolicy().getPolicyNumber());
							if(kotakPolicy){
								Long kotakCpuCode = getKotakProcessingCpuCode(estarFaxAmt, ReferenceTable.KOTAK_PROCESSING_CPU_CODE, SHAConstants.PROCESSING_CPU_CODE_GMC);
								masCpuCode = getMasCpuCode(kotakCpuCode);
							}
							
							objIntimation.setCpuCode(masCpuCode);
							entityManager.merge(objIntimation);
							entityManager.flush();
							//saveStarFaxReverseFeed(objIntimation,existingCpuCode.getCpuCode(), masCpuCode.getCpuCode(),Double.valueOf(estarFaxAmt),"SYSTEM");
						}
					}
				}
				//GLX2020075 commented for this GMC CPU CR since CPU Routing for GMC already done on above code -noufel
//		if(ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(objIntimation.getPolicy().getProduct().getKey())
//				|| objIntimation.getPolicy().getProduct().getKey().equals(ReferenceTable.JET_PRIVILEGE_PRODUCT)){
//			if(objIntimation.getPolicy().getHomeOfficeCode() != null) {
//				OrganaizationUnit branchOffice = getInsuredOfficeNameByDivisionCode(objIntimation.getPolicy().getHomeOfficeCode());
//				if(branchOffice != null && branchOffice.getCpuCode() != null){
//					String officeCpuCode = branchOffice.getCpuCode();
//					Long processingCpuCode = getProcessingCpuCode(estarFaxAmt, Long.valueOf(officeCpuCode), SHAConstants.PROCESSING_CPU_CODE_GMC);
//					if(processingCpuCode != null){
//						TmpCPUCode masCpuCode = getMasCpuCode(processingCpuCode);
//						objIntimation.setCpuCode(masCpuCode);
//						entityManager.merge(objIntimation);
//						entityManager.flush();
//						//saveStarFaxReverseFeed(objIntimation,existingCpuCode.getCpuCode(), masCpuCode.getCpuCode(),Double.valueOf(estarFaxAmt),"SYSTEM");
//					}else{
//						Boolean paayasPolicyDetails = getPaayasPolicyDetails(objIntimation.getPolicy().getPolicyNumber());
//						TmpCPUCode masCpuCode = null;
//						if(paayasPolicyDetails){
//							 masCpuCode = getCpuDetails(ReferenceTable.PAAYAS_CPU_CODE);
//						}else{
//							
//							Boolean jioPolicy = getJioPolicyDetails(objIntimation.getPolicy().getPolicyNumber());
//							if(jioPolicy){
//								 masCpuCode = getCpuDetails(ReferenceTable.JIO_CPU_CODE);
//							}else{
//								masCpuCode = getCpuDetails(ReferenceTable.GMC_CPU_CODE);
//							}
//							
//							
//						}
//						
//						Long tataPolicyCpuCode = getTataPolicy(objIntimation.getPolicy().getPolicyNumber());
//						if(tataPolicyCpuCode != null){
//							 masCpuCode = getMasCpuCode(tataPolicyCpuCode);
//						}
//						
//						Boolean kotakPolicy = getKotakPolicyDetails(objIntimation.getPolicy().getPolicyNumber());
//						if(kotakPolicy){
//							 masCpuCode = getCpuDetails(ReferenceTable.KOTAK_CPU_CODE);
//						}
//						
//						objIntimation.setCpuCode(masCpuCode);
//						entityManager.merge(objIntimation);
//						entityManager.flush();
//						//saveStarFaxReverseFeed(objIntimation,existingCpuCode.getCpuCode(), masCpuCode.getCpuCode(),Double.valueOf(estarFaxAmt),"SYSTEM");
//					}
//				}
//			}
//			
//		}
	 }else{
			Long processingCpuCode = getProcessingCpuCode(estarFaxAmt, objIntimation.getOriginalCpuCode() != null ? objIntimation.getOriginalCpuCode() : objIntimation.getCpuCode().getCpuCode(), SHAConstants.PROCESSING_CPU_CODE_RETAIL);
			if(processingCpuCode != null){
				TmpCPUCode masCpuCode = getMasCpuCode(processingCpuCode);
				objIntimation.setCpuCode(masCpuCode);
				entityManager.merge(objIntimation);
				entityManager.flush();
				//saveStarFaxReverseFeed(objIntimation,existingCpuCode.getCpuCode(), masCpuCode.getCpuCode(),Double.valueOf(estarFaxAmt),"SYSTEM");
			}else{
				Hospitals hospitalById = getHospitalById(objIntimation.getHospital());
				if(hospitalById != null){
					TmpCPUCode masCpuCode = getCpuDetails(hospitalById.getCpuId());
					objIntimation.setCpuCode(masCpuCode);
					entityManager.merge(objIntimation);
					entityManager.flush();
					//saveStarFaxReverseFeed(objIntimation,existingCpuCode.getCpuCode(), masCpuCode.getCpuCode(),Double.valueOf(estarFaxAmt),"SYSTEM");
				}
			}
		}
			//GLX2020075 commented for this GMC CPU CR since CPU Routing for GMC already done on above code -noufel
//		//added for CPU routing
//		if(objIntimation.getPolicy() != null && objIntimation.getPolicy().getProduct().getKey() != null){
//			String CpuCode= getMasProductCpu(objIntimation.getPolicy().getProduct().getKey());
//			if(CpuCode != null){
//				TmpCPUCode masCpuCode = getMasCpuCode(Long.valueOf(CpuCode));
//				objIntimation.setCpuCode(masCpuCode);
//				entityManager.merge(objIntimation);
//				entityManager.flush();
//			}
//		}
//		//added for CPU routing
		String gpaPolicyDetails = getGpaPolicyDetails(objIntimation.getPolicy().getPolicyNumber());
		if(gpaPolicyDetails != null){
			TmpCPUCode masCpuCode = getMasCpuCode(Long.valueOf(gpaPolicyDetails));
			objIntimation.setCpuCode(masCpuCode);
			entityManager.merge(objIntimation);
			entityManager.flush();
		}
		
		return objIntimation;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private Boolean updateDocUploadPremiaTbl(Map<String, String> statusMap, DocUploadToPremia docUploadToPremia) {
		Boolean isSuccess = false;
		try {
			if(null != statusMap && !statusMap.isEmpty()) {
				for (String keys : statusMap.keySet()) {
					if((keys).equals("1") || (keys).equals("5")) {
						isSuccess = updatePremiaValues(SHAConstants.SUCCESS_FLAG,SHAConstants.YES_FLAG,docUploadToPremia);
						
					}
					else if(!((keys).equalsIgnoreCase(SHAConstants.CLAIM_NUMBER) || (keys).equalsIgnoreCase(SHAConstants.INTIMATION_NUMBER) || 
							 (keys).equalsIgnoreCase(SHAConstants.CASHLESS_NUMBER) || (keys).equalsIgnoreCase(SHAConstants.REIMBURSEMENT_NUMBER)))
					{
						isSuccess = updatePremiaValues(SHAConstants.FAILURE_FLAG,SHAConstants.N_FLAG,docUploadToPremia);
					}
				}
			}
		/***
		 * If map is null , then it is a failure case and hence update of table
		 * is done accordingly.
		 * 
		 * */
		else
		{
			isSuccess = updatePremiaValues(SHAConstants.FAILURE_FLAG_REVISED,SHAConstants.N_FLAG,docUploadToPremia);
			isSuccess = false;
		}
		return isSuccess;
		}
		catch(Exception e) {
			return isSuccess;
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private Boolean updatePremiaValues(String statusFlag , String flexFlag,DocUploadToPremia docUploadToPremia) {
		Boolean successFlag = false;
		try {
			log.info("@@@@UPDATE STARFAX TABLE STARTING TIME--------> "+System.currentTimeMillis());
			docUploadToPremia.setPfdUpPremiaAckDt((new Timestamp(System.currentTimeMillis())));
			docUploadToPremia.setPfdUpPremiaUploadSts(statusFlag);
			docUploadToPremia.setPfdUpFlexYN01(flexFlag);				
			docUploadToPremia.setGalaxyReadFlag(SHAConstants.YES_FLAG);
			entityManager.merge(docUploadToPremia);
			entityManager.flush();
			log.info("------DocUploadToPremia------>"+ docUploadToPremia.getPfdUpIntmNo() +"<------------");
			log.info("@@@@UPDATE STARFAX TABLE ENDING TIME--------> "+System.currentTimeMillis());
			successFlag = true;
		} catch(Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			successFlag = false;
		}
		return successFlag;
	}
	

	private List<DMSDocMetaData> getDocMetaDataByApplicationId(String applicationId) {
		if(null != applicationId) {
			Query query = entityManager.createNamedQuery("DMSDocMetaData.findByApplicationId");
			query.setParameter("applicationId", Long.valueOf(applicationId));
			List<DMSDocMetaData> docMetaDataList = query.getResultList();
			if(null != docMetaDataList && !docMetaDataList.isEmpty()) {
				for (DMSDocMetaData dmsDocMetaData : docMetaDataList) {
					entityManager.refresh(dmsDocMetaData);
				}
			}
			return docMetaDataList;
		}
		return null;
	}
	
	private DocumentDetails populateDocumentDetailsObject(DMSDocMetaData docMetaData, Claim objClaim) {
		DocumentDetails documentDetails = new DocumentDetails();
		documentDetails.setIntimationNumber(docMetaData.getIntNumber());
		if(null != docMetaData.getIntNumber())
		{
//			Claim objClaim = getClaimsByIntimationNumber(docMetaData.getIntNumber());
			if(null != objClaim) {
				documentDetails.setClaimNumber(objClaim.getClaimId());
			}
		}
		documentDetails.setFileName(docMetaData.getFileName());
		documentDetails.setDocumentType(docMetaData.getDocType());
		documentDetails.setDocumentSource(SHAConstants.STARFAX);
		documentDetails.setDocumentUrl(docMetaData.getDocUrl());
		/*
		 * Document token needs to be set. After completing the EJB call with DMS , 
		 * the same will be set.
		 * */
		documentDetails.setSfApplicationId(docMetaData.getApplicationId());
		documentDetails.setSfDocumentId(docMetaData.getDocIdFax());
		documentDetails.setSfFileSize(docMetaData.getFileSize());
		documentDetails.setSfFileName(docMetaData.getActualFileName());
		return documentDetails;
		
	}
	
	public DocumentDetails getDocumentDetailsByKey(String docKey) {
		if(null != docKey && !("").equalsIgnoreCase(docKey)) {
			Query query = entityManager.createNamedQuery("DocumentDetails.findByKey");
			query = query.setParameter("key", Long.valueOf(docKey));
			List<DocumentDetails> docDetailsList = query.getResultList();
			if(null != docDetailsList && !docDetailsList.isEmpty()) {
				entityManager.refresh(docDetailsList.get(0));
				return docDetailsList.get(0);
			}
		}
		return null;
	}
	
	private String getDocumentToken(String strUrl,String actualFileName,Long fileSize) {
		HashMap documentMap = SHAFileUtils.uploadDocumentByUrlWebService(strUrl,actualFileName,fileSize);
		if(null != documentMap) {
			String tokenId = (String)documentMap.get("fileKey");
			return tokenId;
		}
		return null;
	}
	
	public String getDocumentToken(String docKey) {
		DocumentDetails details = getDocumentDetailsByKey(docKey);
		if(null != details)
		{
			String token = getDocumentToken(details.getDocumentUrl(),details.getFileName(),details.getSfFileSize());
			if(token != null){
				details.setDocumentToken(Long.valueOf(token));
				entityManager.merge(details);
				entityManager.flush();
			}
			return token;
		}
		return null;
	}
	
	/**
	 * The below method will fetch record from IMS_DMS_DOC_METADATA table based on application id.
	 * Application id will be equated against the report id which is available in IMS_SFX_DOC_METADATA table.
	 * 
	 *  For each application id, a separate record will be inserted into IMS_CLS_DOCUMENT_DETAILS table.
	 * */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private String processReportIdForPremiaDocument(DocUploadToPremia docUploadToPremia,Map<String,String> statusMap, Claim claim, String curenntQ)
	{
		String isSuccess = "";
		List<DMSDocMetaData> dmsDocMetaDataList = getDocMetaDataByApplicationId(docUploadToPremia.getPfdUpReportId());
		if(null != dmsDocMetaDataList && !dmsDocMetaDataList.isEmpty())
		{
			for (DMSDocMetaData dmsDocMetaData : dmsDocMetaDataList) {
				log.info("@@@@@@@@@ DOCUMENT UPLOADED EJB CALL STARTING TIME @@@@@@@@@@-----> "+"-----> " +docUploadToPremia.getPfdUpIntmNo()+"-----> "+ System.currentTimeMillis());
				DocumentDetails docDetails = populateDocumentDetailsObject(dmsDocMetaData, claim);
				if(null != docDetails.getDocumentUrl())
				{
					String strActualFileName = "";
					//if(null != docDetails.getSfFileName())
					if(null != docDetails.getFileName())
					{
						//strActualFileName = docDetails.getSfFileName().trim();
						strActualFileName = docDetails.getFileName().trim();
					}
						//String strDocumentToken = getDocumentToken(docDetails.getDocumentUrl().trim(),strActualFileName,docDetails.getSfFileSize());
						
					if(null != dmsDocMetaData.getFileKey())
						docDetails.setDocumentToken(dmsDocMetaData.getFileKey());
				}
				/***
				 * Need to integrate the DMS service for fetching document token.
				 * Based on doc url, the document token will be retreived.
				 */
				docDetails.setDocSubmittedDate(docUploadToPremia.getPfdUpFfaxSubmitId());
				docDetails.setDocAcknowledgementDate(docUploadToPremia.getPfdUpPremiaAckDt());
				
				if(null != statusMap && !statusMap.isEmpty())
				{
					/*if(statusMap.get(SHAConstants.INTIMATION_NUMBER) != null){
						docDetails.setIntimationNumber((String)statusMap.get(SHAConstants.INTIMATION_NUMBER));
					}*/
					/*if(statusMap.get(SHAConstants.CLAIM_NUMBER) != null){
						docDetails.setClaimNumber((String)statusMap.get(SHAConstants.CLAIM_NUMBER));
					}*/
					if(statusMap.get(SHAConstants.CASHLESS_NUMBER) != null){
						docDetails.setCashlessNumber((String)statusMap.get(SHAConstants.CASHLESS_NUMBER));
					}
					if(statusMap.get(SHAConstants.REIMBURSEMENT_NUMBER) != null){
						docDetails.setReimbursementNumber((String)statusMap.get(SHAConstants.REIMBURSEMENT_NUMBER));
					}
			
				}
				entityManager.persist(docDetails);
				entityManager.flush();
				entityManager.clear();
				log.info("------DocumentDetails------>"+docDetails.getSfApplicationId()+"<----Intimation no-------->"+docDetails.getIntimationNumber()+"<------------");
				//isSuccess = true;
				isSuccess = "Premia intimation request successfully processed";
//				try
//				{
//					if(null != utx)
//					{
//						utx.commit();
//					}
//				}catch(Exception e)
//				{
//					log.error("*******************************ERROR OCCURED DURING UTX COMMIT*************THIS CAN BE INGORED***************");
//				}
//				entityManager.getTransaction().commit();
				//docDetails.set
				log.info("@@@@@@@@@ DOCUMENT UPLOADED EJB CALL ENDING TIME @@@@@@@@@@-----> "+"-----> " +docUploadToPremia.getPfdUpIntmNo()+"-----> "+ System.currentTimeMillis());
			}
		}
		else{
			isSuccess ="Premia intimation request successfully processed. For this request, no documents has been uploaded at premia end";
		}
// added for starfax reversefeed value update or insert / claim.getIntimation().getCpuCode().getCpuCode() to docUploadToPremia.getFromCpu()
		//if(docUploadToPremia.getPfdUpFFAXAmt() != null && claim!=null &&claim.getIntimation()!=null && claim.getIntimation().getCpuCode()!=null && claim.getIntimation().getCpuCode().getCpuCode() != null )
		if(docUploadToPremia.getPfdUpFFAXAmt() != null && claim!=null &&claim.getIntimation()!=null && claim.getIntimation().getCpuCode()!=null && claim.getIntimation().getCpuCode().getCpuCode() != null
				 && curenntQ != null && !(curenntQ.equalsIgnoreCase(SHAConstants.QUERY_REPLY_QUEUE) || curenntQ.equalsIgnoreCase(SHAConstants.OTHER_POL_SERIVICE)))
		{
			log.info("@@@@@@@@@ STARFAX REVERSEFEED getStarFaxCpu input values @@@@@@@@@@-----> "+"----->Intimation No " +claim.getIntimation().getIntimationId()+"----->Int Cpu Code "+claim.getIntimation().getCpuCode().getCpuCode()+"----->FAXAmount "+docUploadToPremia.getPfdUpFFAXAmt()+"----->CurrentQ "+curenntQ+"----->Prodkey "+claim.getIntimation().getPolicy().getProduct().getKey());
			Long strFaxNewCPu = getStarFaxCpu(claim.getIntimation().getCpuCode().getCpuCode(), docUploadToPremia.getPfdUpFFAXAmt(),curenntQ,claim.getIntimation().getPolicy().getProduct().getKey());
			System.out.println("StarFax New CPU Code :"+strFaxNewCPu);
			Long strFaxOldCPu=claim.getIntimation().getCpuCode().getCpuCode();
			if(strFaxNewCPu != 0L)
			{
			String userNameForStarFax="GALAXY";
			Double sfxClaimAmount=Double.parseDouble(docUploadToPremia.getPfdUpFFAXAmt().toString());
			log.info("@@@@@@@@@ STARFAX REVERSEFEED STARTING TIME @@@@@@@@@@-----> "+"-----> " +docUploadToPremia.getPfdUpIntmNo()+"-----> "+ System.currentTimeMillis());
			log.info("@@@@@@@@@ STARFAX REVERSEFEED INSERT OR UPDATE @@@@@@@@@@-----> "+"-----> " +docUploadToPremia.getPfdUpIntmNo()+"-----> "+strFaxOldCPu+"-----> "+strFaxNewCPu+"-----> "+sfxClaimAmount+"-----> "+userNameForStarFax);
			saveStarFaxReverseFeed(claim.getIntimation(),strFaxOldCPu, strFaxNewCPu, sfxClaimAmount,userNameForStarFax);
			saveClaimCpuCode(claim,strFaxOldCPu, strFaxNewCPu);
			log.info("@@@@@@@@@ STARFAX REVERSEFEED CALL ENDING TIME @@@@@@@@@@-----> "+"-----> " +docUploadToPremia.getPfdUpIntmNo()+"-----> "+ System.currentTimeMillis());
		}
			if(strFaxNewCPu == 0L){
				saveClaimCpuCode(claim,strFaxOldCPu, strFaxOldCPu);
			}
		}
		return isSuccess;	

	}
	
	public Preauth getLatestPreauthByClaimKey(Long claimKey) {
		Query query = entityManager.createNamedQuery("Preauth.findByClaimKeyInDescendingOrder");
		query.setParameter("claimkey", claimKey);
		List<Preauth> preauthList = (List<Preauth>) query.getResultList();
		
		if(preauthList != null && ! preauthList.isEmpty()){
			entityManager.refresh(preauthList.get(0));
			return preauthList.get(0);
		}
		return null;
	}
	
	private Boolean isPreauthDenialOfCashlessOrReject(Intimation intimation, Claim claim){
		if(claim != null){
			Preauth preauth = getLatestPreauthByClaimKey(claim.getKey());
			if(preauth != null && preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS)
					|| preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS))){
				return true;
			}else if(preauth != null && preauth.getStatus() != null && preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REOPENED_STATUS)){
				Boolean alreadyPreauthApprovedForReopen = isAlreadyDeniedForReopen(preauth.getKey());
				if(alreadyPreauthApprovedForReopen){
					return true;
				}
			}
		}
		return false;
	}
	
	public Preauth getPreauthForReconsider(Intimation intimation, Claim claim){
		if(claim != null){
			Preauth preauth = getLatestPreauthByClaimKey(claim.getKey());
			if(preauth != null && preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS)
					|| preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS)
					|| preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REOPENED_STATUS))){

				return preauth;
			}
		}
		return null;
	}
	
	public String getPreauthReqAmt(Long preAuthKey, Long claimKey) {
		Query query = entityManager.createNamedQuery("Preauth.findByClaimKeyInDesc");
		query.setParameter("claimkey", claimKey);
		List<Preauth> resultList = (List<Preauth>) query.getResultList();
		Preauth currentPreauth = new Preauth();
		for (Preauth preauth : resultList) {
			
			entityManager.refresh(preauth);
			if(preauth.getKey().equals(preAuthKey)) {
				currentPreauth = preauth;
			}
		}
		String calculatePreRequestedAmt = calculatePreRequestedAmt(preAuthKey, claimKey);
		if(currentPreauth != null) {
			//String[] split = currentPreauth.getPreauthId().split("/");
			//String string = split[split.length - 1];
				for (Preauth preauthDO : resultList) {
					if(!currentPreauth.equals(preauthDO)) {
						//String[] splitedStr = preauthDO.getPreauthId().split("/");
						//String number = splitedStr[splitedStr.length - 1];
						//Integer previousNumber = SHAUtils.getIntegerFromString(string) - 1;
						if(currentPreauth.getKey() > preauthDO.getKey()) {
							String calculatePreRequestedAmt2 = calculatePreRequestedAmt(preauthDO.getKey(), claimKey);
							Double value =  SHAUtils.getDoubleValueFromString(calculatePreRequestedAmt) - SHAUtils.getDoubleValueFromString(calculatePreRequestedAmt2);
							calculatePreRequestedAmt = String.valueOf(value.intValue());
							break;
						}
					}
					
				}
		}
		return calculatePreRequestedAmt;
	}
	
	public String calculatePreRequestedAmt(Long preAuthKey, Long claimKey) {
		String requestAmt = "0";
		float lAmt= 0f;
		Query findAll = entityManager
				.createNamedQuery("ClaimAmountDetails.findByPreauthKey");
		findAll.setParameter("preauthKey", preAuthKey);		
		List<ClaimAmountDetails> claimAmtDetails = (List<ClaimAmountDetails>)findAll.getResultList();
		for (ClaimAmountDetails claimAmountDetails : claimAmtDetails) {
			entityManager.refresh(claimAmountDetails);			
		}
		if(null !=claimAmtDetails ) {
			for(ClaimAmountDetails objClaim :claimAmtDetails ) {
				entityManager.refresh(objClaim);
				if(null!= objClaim.getClaimedBillAmount()) {
					lAmt = lAmt+ objClaim.getClaimedBillAmount();
				}
			}
			//return String.valueOf(lAmt);
			requestAmt = String.valueOf(lAmt);
		}
		return requestAmt;
	}
	
/*	private Boolean submitReconsiderationForCashless(Intimation intimation,
			Preauth preauth) {
=======
	private Boolean submitReconsiderationForCashless(Intimation intimation,
			Preauth preauth, Claim claim) {
>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6

		PayloadBOType payloadBO = new PayloadBOType();

		IntimationType intimationType = new IntimationType();
		if (intimation != null) {
			intimationType.setIntimationNumber(intimation.getIntimationId());
			intimationType.setIsBalanceSIAvailable(true);
			intimationType.setIsClaimPending(false);
			intimationType.setIsPolicyValid(true);
			if (intimation.getIntimationSource() != null) {
				intimationType.setIntimationSource(intimation
						.getIntimationSource().getValue());
			}
			if (null != intimation.getCreatedDate()) {
				String date = String.valueOf(intimation.getCreatedDate());
				String date1 = date.replaceAll("-", "/");
				intimationType.setIntDate(SHAUtils.formatIntimationDate(date1));
				intimationType.setIntDate(new Timestamp(System
						.currentTimeMillis()));
				Timestamp timestamp = new Timestamp(
						System.currentTimeMillis() + 60 * 60 * 1000);
				intimationType.setIntDate(timestamp);
			}
			payloadBO.setIntimation(intimationType);

			PolicyType policyType = new PolicyType();
			policyType.setPolicyId(intimation.getPolicy().getPolicyNumber());
			payloadBO.setPolicy(policyType);

			HospitalInfoType hospitalType = new HospitalInfoType();
			hospitalType.setHospitalType(intimation.getHospitalType()
					.getValue());
			payloadBO.setHospitalInfo(hospitalType);
		}

		if (preauth.getClaim() != null) {
			ClaimRequestType claimRequestType = new ClaimRequestType();
			claimRequestType.setCpuCode(intimation.getCpuCode().getCpuCode()
					.toString());
			payloadBO.setClaimRequest(claimRequestType);

			ClaimType claimType = new ClaimType();
			claimType.setClaimType(preauth.getClaim().getClaimType().getValue()
					.toUpperCase());
			claimType.setKey(preauth.getClaim().getKey());
			claimType.setClaimId(preauth.getClaim().getClaimId());
			payloadBO.setClaim(claimType);
		}

		PreAuthReqType preAuthReqType = new PreAuthReqType();
		preAuthReqType.setIsSrCitizen(false);

		String preauthReqAmt = getPreauthReqAmt(preauth.getKey(), preauth
				.getClaim().getKey());
		PaymentInfoType paymentInfoType = new PaymentInfoType();
		if (preauthReqAmt != null) {
			preAuthReqType.setPreAuthAmt(Double.valueOf(preauthReqAmt));
			paymentInfoType.setClaimedAmount(Double.valueOf(preauthReqAmt));
		} else {
			preAuthReqType.setPreAuthAmt(0d);
			paymentInfoType.setClaimedAmount(0d);
		}

		preAuthReqType.setOutcome("RECONSIDER");
		preAuthReqType.setKey(preauth.getKey());
		payloadBO.setPreAuthReq(preAuthReqType);

		payloadBO.setPaymentInfo(paymentInfoType);

		ClassificationType classificationType = new ClassificationType();
		classificationType.setPriority(SHAConstants.NORMAL);
		classificationType.setSource("Cashless Denied");
		classificationType.setType(SHAConstants.RECONSIDERATION);
		payloadBO.setClassification(classificationType);

		QueryType queryType = new QueryType();
		queryType.setStatus("value");
		payloadBO.setQuery(queryType);
		
		ProductInfoType productInfoType = new ProductInfoType();
			
		productInfoType.setLob(SHAConstants.HEALTH_LOB);
		productInfoType.setLobType(SHAConstants.HEALTH_LOB_FLAG);
		if(claim.getIncidenceFlag() != null) {
			productInfoType.setLob(SHAConstants.PA_LOB);
			productInfoType.setLobType(SHAConstants.PA_CASHLESS_LOB_TYPE);
		}
		payloadBO.setProductInfo(productInfoType);

		CustomerType customerType = new CustomerType();
		if (preauth.getTreatmentType() != null) {
			customerType
					.setTreatmentType(preauth.getTreatmentType().getValue());
			payloadBO.setCustomer(customerType);
		}

		SimulateStarfax callSimulateStarfaxBPM = BPMClientContext
				.callSimulateStarfaxBPM();
		log.info("****CASHELESS RECONSIDERATION *****" + intimation.getIntimationId());
		try {
			callSimulateStarfaxBPM.initiate(BPMClientContext.BPMN_TASK_USER,
					payloadBO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	*/
	public void submitDBProcedureForReconsideration(Claim claim, Preauth preauth,Long efaxAmount,Date pfdUpFfaxSubmitDate,DocUploadToPremia docUploadToPremia){
		
		log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL STARTING TIME FOR RECONSIDERATION @@@@@@@@@@-----> "+"-----> " +claim.getIntimation().getIntimationId()+"-----> "+ System.currentTimeMillis());
		
		Hospitals hospitalById = getHospitalById(claim.getIntimation().getHospital());
		
		Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(claim, hospitalById);
		
		Object[] inputArray = (Object[])arrayListForDBCall[0];
		if(preauth != null && preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS))){
			inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.OUTCOME_FOR_PA_DOC_RECONSIDERATION;
		}else if(preauth != null && preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS))){
			inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.OUTCOME_FOR_PA_REJECT_RECONSIDERATION;
		}
		inputArray[SHAConstants.INDEX_CASHLESS_KEY] = preauth.getKey();
		inputArray[SHAConstants.INDEX_CASHLESS_NO] = preauth.getPreauthId();
		
		inputArray[SHAConstants.INDEX_STAGE_SOURCE] = SHAConstants.RECONSIDERATION;
		
		String preauthReqAmt = getPreauthReqAmt(preauth.getKey(), preauth
				.getClaim().getKey());
		if (efaxAmount != null) {
			inputArray[SHAConstants.INDEX_CLAIMED_AMT] = efaxAmount;
		} else {
			inputArray[SHAConstants.INDEX_CLAIMED_AMT] = 0d;
		}
		
		if(preauth.getTreatmentType() != null){
			inputArray[SHAConstants.INDEX_TREATMENT_TYPE] = preauth.getTreatmentType().getValue();
		}
		
		if(hospitalById.getFspFlag() != null && hospitalById.getFspFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
			 MastersValue master = getMaster(ReferenceTable.FSP);
			 if(master != null){
				 inputArray[SHAConstants.INDEX_NETWORK_TYPE] = master.getValue();
			 }
		}
		
		Date clubmemberType = intimationService.getCMDClubMemberTypeForPreauth(claim,pfdUpFfaxSubmitDate);
		//code added by noufel for setting Club member time differnce in workflow table
		if(clubmemberType != null ){
			inputArray[SHAConstants.INDEX_ALLOCATED_DATE] =SHAUtils.parseDate(clubmemberType);
		}
		//code added for updating starfax application ID and match Q date in workflow table
		if(docUploadToPremia.getPfdUpFfaxSubmitId() != null){
			inputArray[SHAConstants.INDEX_ROD_CREATED_DATE] = docUploadToPremia.getPfdUpFfaxSubmitId();
		}
		if(docUploadToPremia.getPfdUpReportId() != null && !docUploadToPremia.getPfdUpReportId().isEmpty()){
			inputArray[SHAConstants.INDEX_ACK_NUMBER] = docUploadToPremia.getPfdUpReportId();
		}

		Object[] parameter = new Object[1];
		parameter[0] = inputArray;
		
		DBCalculationService dbCalculationService = new DBCalculationService();
//		dbCalculationService.initiateTaskProcedure(parameter);
		log.info("@@@@CALL SUBMIT TASK PROCEDURE STARTING TIME  --------> "+System.currentTimeMillis());
		dbCalculationService.revisedInitiateTaskProcedure(parameter);
		log.info("@@@@CALL SUBMIT TASK PROCEDURE ENDING TIME  --------> "+System.currentTimeMillis());
		
		log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL ENDING TIME FOR RECONSIDERATION @@@@@@@@@@-----> "+"-----> " +claim.getIntimation().getIntimationId()+"-----> "+ System.currentTimeMillis());
	}
	
	public void submitDBProcedureForFirstLevelEnhancement(Claim claim, Preauth preauth, Long estarFaxAmt,Date pfdUpFfaxSubmitDate,DocUploadToPremia docUploadToPremia){
		
		log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL STARTING TIME FOR FIRST LEVEL ENHANCEMENT@@@@@@@@@@-----> "+"-----> " +claim.getIntimation().getIntimationId()+"-----> "+ System.currentTimeMillis());
		
		Hospitals hospitalById = getHospitalById(claim.getIntimation().getHospital());
		DBCalculationService dbCalculationService = new DBCalculationService();

		Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(claim, hospitalById);
		
		
		Object[] inputArray = (Object[])arrayListForDBCall[0];
		inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.OUTCOME_FOR_FIRST_LEVEL_ENHANCEMENT;
		inputArray[SHAConstants.INDEX_CASHLESS_KEY] = preauth.getKey();
		inputArray[SHAConstants.INDEX_CASHLESS_NO] = preauth.getPreauthId();
		
		/*Below Source & Channel implemented as per satish sir instruction*/ 
		inputArray[SHAConstants.INDEX_REFERENCE_USER_ID] =SHAConstants.CHANNEL_EMAIL;
		inputArray[SHAConstants.INDEX_RRC_REQUEST_NUMBER] = SHAConstants.SOURCE_STARFAX;
		
		String preauthReqAmt = getPreauthReqAmt(preauth.getKey(), preauth
				.getClaim().getKey());
		Double parseDouble = Double.parseDouble(preauthReqAmt);
		
		inputArray[SHAConstants.INDEX_CLAIMED_AMT] = 0d;
		if (parseDouble != null) {
			inputArray[SHAConstants.INDEX_CLAIMED_AMT] = parseDouble.intValue();
		} 
		
		inputArray[SHAConstants.INDEX_CLAIMED_AMT] = estarFaxAmt;
		
		if(preauth.getTreatmentType() != null){
			inputArray[SHAConstants.INDEX_TREATMENT_TYPE] = preauth.getTreatmentType().getValue();
		}
		
		String cpuLimitForUser = "";
		Double totalLimitAmt = 0d;
		Preauth latestPreauth = getLatestPreauthByClaimForWithdrawReject(claim.getKey());
		
		if(latestPreauth != null){
			totalLimitAmt = (latestPreauth.getTotalApprovalAmount() != null ? latestPreauth.getTotalApprovalAmount() : 0d) + (estarFaxAmt != null ? estarFaxAmt.doubleValue() : 0d);
		}
		
		if(claim.getIntimation().getCpuCode() != null){
			cpuLimitForUser = dbCalculationService.getCPULimitForUser(claim.getIntimation().getCpuCode().getCpuCode().toString(), totalLimitAmt.toString());	
		}
		
		if(cpuLimitForUser != null && !cpuLimitForUser.isEmpty()){
			inputArray[SHAConstants.INDEX_DOCUMENT_RECEVIED_FROM] = cpuLimitForUser;
			
			if(cpuLimitForUser.equalsIgnoreCase(SHAConstants.CPU_LIMIT_EXCEEDED_CPPA) || cpuLimitForUser.equalsIgnoreCase(SHAConstants.CPU_LIMIT_EXCEEDED_CP)){
				inputArray[SHAConstants.INDEX_ROD_NUMBER] = SHAConstants.YES_FLAG;
			}
		}
		
		if(claim.getIncidenceFlag() == null ||(claim.getIncidenceFlag() != null && claim.getIncidenceFlag().isEmpty()) ||
				(("A").equals(claim.getIncidenceFlag()) && claim.getClaimType().getKey() != null && claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY))){
			//payloadBO.getProductInfo().setLobType(SHAConstants.HEALTH_LOB_FLAG);
			//wrkFlowMap.put(SHAConstants.LOB_TYPE, SHAConstants.HEALTH_LOB_FLAG);
			inputArray[SHAConstants.INDEX_LOB_TYPE] = SHAConstants.HEALTH_LOB_FLAG;
		}
		else if(!(ReferenceTable.HEALTH_LOB_KEY).equals(claim.getIntimation().getPolicy().getLobId())){
			//payloadBO.getProductInfo().setLobType(SHAConstants.PA_LOB_TYPE);
			//wrkFlowMap.put(SHAConstants.LOB_TYPE, SHAConstants.PA_LOB_TYPE);
			inputArray[SHAConstants.INDEX_LOB_TYPE] = SHAConstants.PA_LOB_TYPE;
		}
		else{
			//payloadBO.getProductInfo().setLobType(SHAConstants.HEALTH_LOB_FLAG);
			//wrkFlowMap.put(SHAConstants.LOB_TYPE, SHAConstants.HEALTH_LOB_FLAG);
			inputArray[SHAConstants.INDEX_LOB_TYPE] = SHAConstants.HEALTH_LOB_FLAG;
		}
		
		if(hospitalById.getFspFlag() != null && hospitalById.getFspFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
			 MastersValue master = getMaster(ReferenceTable.FSP);
			 if(master != null){
				 inputArray[SHAConstants.INDEX_NETWORK_TYPE] = master.getValue();
			 }
		}
		Date clubmemberType = intimationService.getCMDClubMemberTypeForEnhancement(claim,pfdUpFfaxSubmitDate);
		//code added by noufel for setting Club member time differnce in workflow table
		if(clubmemberType != null ){
			inputArray[SHAConstants.INDEX_ALLOCATED_DATE] =SHAUtils.parseDate(clubmemberType);
		}
		
		//code added for updating starfax application ID and match Q date in workflow table
		if(docUploadToPremia.getPfdUpFfaxSubmitId() != null){
			inputArray[SHAConstants.INDEX_ROD_CREATED_DATE] = docUploadToPremia.getPfdUpFfaxSubmitId();
		}
		if(docUploadToPremia.getPfdUpReportId() != null && !docUploadToPremia.getPfdUpReportId().isEmpty()){
			inputArray[SHAConstants.INDEX_ACK_NUMBER] = docUploadToPremia.getPfdUpReportId();
		}
		Object[] parameter = new Object[1];
		parameter[0] = inputArray;
		
		
//		dbCalculationService.initiateTaskProcedure(parameter);
		log.info("@@@@CALL SUBMIT TASK PROCEDURE STARTING TIME  --------> "+System.currentTimeMillis());
		dbCalculationService.revisedInitiateTaskProcedure(parameter);
		log.info("@@@@CALL SUBMIT TASK PROCEDURE ENDING TIME  --------> "+System.currentTimeMillis());
		
		log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL ENDING TIME FOR FIRST LEVEL ENHANCEMENT@@@@@@@@@@-----> "+"-----> " +claim.getIntimation().getIntimationId()+"-----> "+ System.currentTimeMillis());
		
	}
	
	private Reimbursement getReimbursementObjectByClaim(Long key) {
		Query query = entityManager.createNamedQuery("Reimbursement.findLatestRODByClaimKey");
		query = query.setParameter("claimKey", key);
		List<Reimbursement> reimbursementList = query.getResultList();
		if(null != reimbursementList && !reimbursementList.isEmpty()) {
			entityManager.refresh(reimbursementList.get(0));
			return reimbursementList.get(0);
		}
		return null;
	}
	
	private Map<String, String> setValueForStatusMap(Claim claim,Map<String, String> statusMap) {
		if(claim != null){
			if(claim.getIntimation() != null && claim.getIntimation().getIntimationId() != null){
				statusMap.put(SHAConstants.INTIMATION_NUMBER, claim.getIntimation().getIntimationId());
			}
			if(claim.getClaimId() != null){
				statusMap.put(SHAConstants.CLAIM_NUMBER, claim.getClaimId());
			}
			Preauth preauth = getLatestPreauthByClaimKey(claim.getKey());
			
			if(preauth != null){
				statusMap.put(SHAConstants.CASHLESS_NUMBER, preauth.getPreauthId());
			}
			
			Reimbursement reimbursement = getReimbursementObjectByClaim(claim.getKey());
			if(reimbursement != null){
				statusMap.put(SHAConstants.REIMBURSEMENT_NUMBER, reimbursement.getRodNumber());
			}
		}
		
		return statusMap;
	}
/*	
	private List<HumanTask> getCashlessQueryHumanTask(String intimationNo){
		QueryReplyDocTask queryReplyDocTask = BPMClientContext.getQueryReplyDocTask(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
		PayloadBOType payloadBO = new PayloadBOType();
		IntimationType intimationType = new IntimationType();
		intimationType.setIntimationNumber(intimationNo);
		payloadBO.setIntimation(intimationType);
		com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = queryReplyDocTask.getTasks(BPMClientContext.BPMN_TASK_USER, new Pageable(), payloadBO);
		List<HumanTask> humanTaskList = tasks.getHumanTasks();
		return humanTaskList;
	}
	
//	private List<HumanTask> getCashlessQueryEnhancementHumanTask(String intimationNo){
//		QueryReplyDocTask queryReplyDocTask = BPMClientContext.getQueryReplyDocTask(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
//		PayloadBOType payloadBO = new PayloadBOType();
//		IntimationType intimationType = new IntimationType();
//		intimationType.setIntimationNumber(intimationNo);
//		payloadBO.setIntimation(intimationType);
//		com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = queryReplyDocTask.getTasks(BPMClientContext.BPMN_TASK_USER, new Pageable(), payloadBO);
//		List<HumanTask> humanTaskList = tasks.getHumanTasks();
//		return humanTaskList;
//	}
	
	private List<HumanTask> getReimbursementQueryHumanTask(String intimationNo) {
		QueryReplyDocReimbTask reimbQueryReplyDocTask = BPMClientContext.getReimbQueryReplyDocTask(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
		
		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType payloadBO = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType();
		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.intimation.IntimationType intimationType = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.intimation.IntimationType();
		intimationType.setIntimationNumber(intimationNo);
		payloadBO.setIntimation(intimationType);
		com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = reimbQueryReplyDocTask.getTasks(BPMClientContext.BPMN_TASK_USER, new Pageable(), payloadBO);
		List<HumanTask> humanTaskList = tasks.getHumanTasks();
		return humanTaskList;
	}

	private Boolean isQueryReplyDocForCashless(String intimationNo){
		List<HumanTask> cashlessQueryHumanTask = getCashlessQueryHumanTask(intimationNo);
		if(cashlessQueryHumanTask != null && ! cashlessQueryHumanTask.isEmpty()){
			return true;
		}
		return false;
	}
	
	private Boolean isQueryReplyDocForReimbursement(String intimationNo){
		List<HumanTask> reimbursementQueryHumanTask = getReimbursementQueryHumanTask(intimationNo);
		if(reimbursementQueryHumanTask != null && ! reimbursementQueryHumanTask.isEmpty()){
			return true;
		}
		return false;
	}*/
	
	private Map<String, List<BigDecimal>> getUnionForReimbAndCashless() throws Exception

	{
		Map<String,List<BigDecimal>> unionMap = null;
		List<BigDecimal> cashlessKey = null;
		List<BigDecimal> reimbursementKey = null;
		String unionAllQuery = "Select reimbursement_key,'R' from ims_cls_reimbursement_query  where status_id NOT IN ( "+ ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_RECEIVED_STATUS +","+ 
	ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_RECEIVED_STATUS+","+ ReferenceTable.PREAUTH_QUERY_RECEIVED_STATUS+","+ReferenceTable.ENHANCEMENT_QUERY_RECEIVED_STATUS+") "
				+ "union all select cashless_key,'C' from ims_cls_cashless_query WHERE STATUS_ID NOT IN ("+ ReferenceTable.MA_CORPORATE_QUERY_REPLY_STATUS+","+ReferenceTable.FA_QUERY_REPLY_STATUS+")";
		Query query = entityManager.createNativeQuery(unionAllQuery);
		List<Object> objList = query.getResultList();
		if(null != objList && !objList.isEmpty())
		{
			unionMap = new HashMap<String, List<BigDecimal>>();
			cashlessKey = new ArrayList<BigDecimal>();
			reimbursementKey = new ArrayList<BigDecimal>();
			for (Iterator it = objList.iterator(); it.hasNext();)
			{
				 Object[] myResult = (Object[]) it.next();
				 BigDecimal transactionKey = (BigDecimal) myResult[0];
				 Character type = (Character)myResult[1];
				 String.valueOf(type);
				 if((SHAConstants.CASHLESS_CHAR).equalsIgnoreCase(String.valueOf(type)))
				 {
					 cashlessKey.add(transactionKey);
				 }
				 else if((SHAConstants.REIMBURSEMENT_CHAR).equalsIgnoreCase(String.valueOf(type)))
				 {
					 reimbursementKey.add(transactionKey);
				 }

			}
			unionMap.put(SHAConstants.CASHLESS_CHAR, cashlessKey);
			unionMap.put(SHAConstants.REIMBURSEMENT_CHAR, reimbursementKey);
		}
		return unionMap;
	}
	
	/*private Reimbursement getReimbursementObjectForQuery(Long key) {
		
		List<Long> statusList = new ArrayList<Long>();
		statusList.add(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
		statusList.add(ReferenceTable.FINANCIAL_QUERY_STATUS);
		
		Query query = entityManager.createNamedQuery("Reimbursement.findByStatusForQuery");
		query = query.setParameter("claimKey", key);
		query = query.setParameter("statusList", statusList);
		List<Reimbursement> reimbursementList = query.getResultList();
		if(null != reimbursementList && !reimbursementList.isEmpty())
		{
			entityManager.refresh(reimbursementList.get(0));
			return reimbursementList.get(0);
		}
		else
		{
			return null;
		}
	}*/
	
	private Preauth getPreauthForQuery(Long key) {
		List<Long> statusList = new ArrayList<Long>();
		statusList.add(ReferenceTable.PREAUTH_QUERY_STATUS);
		statusList.add(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS);
		statusList.add(ReferenceTable.ENHANCEMENT_QUERY_STATUS);
		statusList.add(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS);
		statusList.add(ReferenceTable.PREAUTH_REOPENED_STATUS);
		
		Query query = entityManager.createNamedQuery("Preauth.findByQueryRaisedStatus");
		query = query.setParameter("claimKey", key);
		query = query.setParameter("statusList", statusList);
		List<Preauth> preauthList = query.getResultList();
		if(null != preauthList && !preauthList.isEmpty()){
			entityManager.refresh(preauthList.get(0));
			return preauthList.get(0);
		}
		return null;
	}
	
	public PreauthQuery getPreauthQueryRecord(Long preauthKey) {
		Query query = entityManager.createNamedQuery("PreauthQuery.findByLatestpreauth");
		query = query.setParameter("preAuthKey", preauthKey);
		List<PreauthQuery> preauthQueryList = query.getResultList();
		if(null != preauthQueryList && !preauthQueryList.isEmpty())
		{
			entityManager.refresh(preauthQueryList.get(0));
			return preauthQueryList.get(0);
		}
		return null;
	}
	
	public Preauth getPreauthById(Long preauthKey) {
		Query query = entityManager.createNamedQuery("Preauth.findByKey");
		query.setParameter("preauthKey", preauthKey);
		@SuppressWarnings("unchecked")
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		if(singleResult != null && ! singleResult.isEmpty()) {
			entityManager.refresh(singleResult.get(0));
			return singleResult.get(0);
		}
		
		return null;
		
		
	}		
	
	private void updatePreauthStatusForQuery(Preauth preauth,String strOutCome,DocUploadToPremia docUploadToPremia)
	{
		Status status = new Status();
//		if(("PREMEDICALQUERYREPLY").equals(strOutCome))
//		{
//			status.setKey(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_RECEIVED_STATUS);
//		}
//		else if (("PREMEDICALQUERYREPLYENH").equals(strOutCome))
//		{
//			status.setKey(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_RECEIVED_STATUS);
//		}else if(("QUERYREPLY").equals(strOutCome)){
//			status.setKey(ReferenceTable.PREAUTH_QUERY_RECEIVED_STATUS);
//		}else if(("QUERYREPLYENH").equals(strOutCome)){
//			status.setKey(ReferenceTable.ENHANCEMENT_QUERY_RECEIVED_STATUS);
//		}
		if((SHAConstants.OUTCOME_FOR_QUERY_REPLY_FLP).equals(strOutCome))
		{
			status.setKey(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_RECEIVED_STATUS);
		}
		else if ((SHAConstants.OUTCOME_FOR_QUERY_REPLY_FLE).equals(strOutCome))
		{
			status.setKey(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_RECEIVED_STATUS);
		}else if((SHAConstants.OUTCOME_FOR_QUERY_REPLY_PP).equals(strOutCome)){
			status.setKey(ReferenceTable.PREAUTH_QUERY_RECEIVED_STATUS);
		}else if((SHAConstants.OUTCOME_FOR_QUERY_REPLY_PE).equals(strOutCome)){
			status.setKey(ReferenceTable.ENHANCEMENT_QUERY_RECEIVED_STATUS);
		}
		
		//added for query reply date to update in cashless table by noufel
		if(docUploadToPremia.getPfdUpFfaxSubmitId() != null){
			preauth.setSfxMatchedQDate(docUploadToPremia.getPfdUpFfaxSubmitId());
		}
		if(docUploadToPremia.getPfdUpPremiaAckDt() != null){
			preauth.setSfxRegisteredQDate(docUploadToPremia.getPfdUpPremiaAckDt());
		}
		
		preauth.setStatus(status);
		entityManager.merge(preauth);
		entityManager.flush();
		entityManager.clear();
//		try
//		{
//			if(null != utx)
//			{
//				utx.commit();
//			}
//		}catch(Exception e)
//		{
//			log.error("*******************************ERROR OCCURED DURING UTX COMMIT*************THIS CAN BE INGORED***************");
//		}
		log.info("------Preauth------>"+preauth+"<------------");
	}
	
	private void updateCashlessQueryTable(Preauth preauth,String strOutCome)
	{
		Status status = new Status();
		PreauthQuery preauthQuery = getPreauthQueryRecord(preauth.getKey());
//		if(("PREMEDICALQUERYREPLY").equals(strOutCome))
//		{
//			status.setKey(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_RECEIVED_STATUS);
//		}
//		else if (("PREMEDICALQUERYREPLYENH").equals(strOutCome))
//		{
//			status.setKey(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_RECEIVED_STATUS);
//		}else if(("QUERYREPLY").equals(strOutCome)){
//			status.setKey(ReferenceTable.PREAUTH_QUERY_RECEIVED_STATUS);
//		}else if(("QUERYREPLYENH").equals(strOutCome)){
//			status.setKey(ReferenceTable.ENHANCEMENT_QUERY_RECEIVED_STATUS);
//		}
		
		if((SHAConstants.OUTCOME_FOR_QUERY_REPLY_FLP).equals(strOutCome))
		{
			status.setKey(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_RECEIVED_STATUS);
		}
		else if ((SHAConstants.OUTCOME_FOR_QUERY_REPLY_FLE).equals(strOutCome))
		{
			status.setKey(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_RECEIVED_STATUS);
		}else if((SHAConstants.OUTCOME_FOR_QUERY_REPLY_PP).equals(strOutCome)){
			status.setKey(ReferenceTable.PREAUTH_QUERY_RECEIVED_STATUS);
		}else if((SHAConstants.OUTCOME_FOR_QUERY_REPLY_PE).equals(strOutCome)){
			status.setKey(ReferenceTable.ENHANCEMENT_QUERY_RECEIVED_STATUS);
		}
		
		preauthQuery.setStatus(status);
		preauthQuery.setDocumentReceivedDate(((new Timestamp(System
				.currentTimeMillis()))));
		entityManager.merge(preauthQuery);
		entityManager.flush();
		entityManager.clear();
//		try
//		{
//			if(null != utx)
//			{
//				utx.commit();
//			}
//		}catch(Exception e)
//		{
//			log.error("*******************************ERROR OCCURED DURING UTX COMMIT*************THIS CAN BE INGORED***************");
//		}
		log.info("------PreauthQuery------>"+preauthQuery+"<------------");
	}
/*	
	private Map<String, String> submitCashlessQueryTask(List<HumanTask> listOfHumanTasks,String strIntimationNo,String strOutCome) {
		Map<String , String> statusMap = new HashMap<String, String>();
		Preauth preauth = null;
		if(null != listOfHumanTasks && !listOfHumanTasks.isEmpty()) {
			SubmitQueryReplyDocTask submitQueryReplyTask = BPMClientContext.getSubmitQueryReplyDocTask(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
			for (HumanTask humanTask : listOfHumanTasks) {
				PayloadBOType payloadBO =  humanTask.getPayloadCashless();
				//ClaimType claimType = payloadBO.getClaim();
				PreAuthReqType preauthType = payloadBO.getPreAuthReq();
				Long key = preauthType.getKey();
				if(null != key) {
					preauth = getPreauthById(key);
					if(("PREMEDICALQUERYREPLY").equals(strOutCome) || ("PREMEDICALQUERYREPLYENH").equals(strOutCome)) {
						preauthType.setKey(preauth.getClaim().getKey());
						log.info("*****GETTING PREMEDICALQUERYREPLY TASK *****");
					}
					
					updatePreauthStatusForQuery(preauth,strOutCome);
					updateCashlessQueryTable(preauth,strOutCome);
				}
				payloadBO.setPreAuthReq(preauthType);
				
				ClassificationType classification = payloadBO.getClassification();
				classification.setType(SHAConstants.QUERY_REPLY);
				payloadBO.setClassification(classification);
				
				humanTask.setPayloadCashless(payloadBO);
				humanTask.setOutcome(strOutCome);
				
				try {
					submitQueryReplyTask.execute(BPMClientContext.BPMN_TASK_USER, humanTask);
						statusMap.put(SHAConstants.CASHLESS_NUMBER,preauth.getPreauthId());
					if(null != preauth.getClaim()) {
						statusMap.put(SHAConstants.CLAIM_NUMBER,preauth.getClaim().getClaimId());
						statusMap.put(SHAConstants.INTIMATION_NUMBER, preauth.getClaim().getIntimation().getIntimationId());
					}
				
				} catch(Exception e) {
					e.printStackTrace();
					log.error(e.toString());
				}
			}
			
			statusMap.put("5", "Intimation '"+strIntimationNo+"' is successfully submitted to BPMN for further processing");
			return statusMap;	
		}
		return null;
		
			
	}*/
	
	
	private Map<String, String> submitQueryReplyTask(String strIntimationNo,Preauth preauth,String strOutCome,DocUploadToPremia docUploadToPremia) {
		Map<String , String> statusMap = new HashMap<String, String>();
				//ClaimType claimType = payloadBO.getClaim();
			try {
		           submitDBprocedure(preauth.getClaim(), SHAConstants.QUERY_REPLY_QUEUE, strOutCome,docUploadToPremia);
				   updatePreauthStatusForQuery(preauth,strOutCome,docUploadToPremia);
				   updateCashlessQueryTable(preauth,strOutCome);
				   
				  
				   
				   statusMap.put(SHAConstants.CASHLESS_NUMBER,preauth.getPreauthId());
				   statusMap.put(SHAConstants.STAGE_SOURCE, SHAConstants.QUERY_REPLY);
					if(null != preauth.getClaim()) {
						statusMap.put(SHAConstants.CLAIM_NUMBER,preauth.getClaim().getClaimId());
						statusMap.put(SHAConstants.INTIMATION_NUMBER, preauth.getClaim().getIntimation().getIntimationId());
					}

				
				} catch(Exception e) {
					e.printStackTrace();
					log.error(e.toString());
				}
			
			statusMap.put("5", "Intimation '"+strIntimationNo+"' is successfully submitted to BPMN for further processing");
			return statusMap;	
		
		
			
	}
	
	/*private ReimbursementQuery getReimbursementQuery(Long key)
	{
		Query query = entityManager.createNamedQuery("ReimbursementQuery.findByReimbursement");
		query = query.setParameter("reimbursementKey", key);
		List<ReimbursementQuery> reimbQueryList = query.getResultList();
		if(null != reimbQueryList && !reimbQueryList.isEmpty())
		{
			entityManager.refresh(reimbQueryList.get(0));
			return reimbQueryList.get(0);
		}
		return null;
			
	}*/
	
	/*private Reimbursement getReimbursementObject(String rodNo)
	{
		Query query = entityManager.createNamedQuery("Reimbursement.findRodByNumber");
		query = query.setParameter("rodNumber", rodNo);
		List<Reimbursement> reimbursementList = query.getResultList();
		if(null != reimbursementList && !reimbursementList.isEmpty())
		{
			entityManager.refresh(reimbursementList.get(0));
			return reimbursementList.get(0);
		}
		else
		{
			return null;
		}
	}*/
	
	private Reimbursement getReimbursementObject(Long key)
	{
		Query query = entityManager.createNamedQuery("Reimbursement.findByKey");
		query = query.setParameter("primaryKey", key);
		List<Reimbursement> reimbursementList = query.getResultList();
		if(null != reimbursementList && !reimbursementList.isEmpty())
		{
			entityManager.refresh(reimbursementList.get(0));
			return reimbursementList.get(0);
		}
		return null;
	}
	
	/*private void updateReimbursementTable(Reimbursement reimb , Long stageId)
	{
		Status status = new Status();
		 if((ReferenceTable.MA_CORPORATE_QUERY_STAGE).equals(stageId))
		 {
			 status.setKey(ReferenceTable.MA_CORPORATE_QUERY_REPLY_STATUS);
		 }
		 else if ((ReferenceTable.FINANCIAL_QUERY_STAGE).equals(stageId))
		 {
			 status.setKey(ReferenceTable.FA_QUERY_REPLY_STATUS);
		 }
			 reimb.setStatus(status);
			 entityManager.merge(reimb);
			 log.info("------Reimbursement------>"+reimb+"<------------");
			 entityManager.flush();
//			 try
//				{
//					if(null != utx)
//					{
//						utx.commit();
//					}
//				}catch(Exception e)
//				{
//					log.error("*******************************ERROR OCCURED DURING UTX COMMIT*************THIS CAN BE INGORED***************");
//				}
	}*/
	
	/*private void updateReimbursementQuery(Reimbursement reimb,  Long stageId)
	{
		Status status = new Status();
		ReimbursementQuery reimbQuery = getReimbursementQuery(reimb.getKey());
		if((ReferenceTable.MA_CORPORATE_QUERY_STAGE).equals(stageId))
		 {
			 status.setKey(ReferenceTable.MA_CORPORATE_QUERY_REPLY_STATUS);
		 }
		 else if ((ReferenceTable.FINANCIAL_QUERY_STAGE).equals(stageId))
		 {
			 status.setKey(ReferenceTable.FA_QUERY_REPLY_STATUS);
		 }
			reimbQuery.setStatus(status);
			reimbQuery.setQueryReplyDate(((new Timestamp(System
					.currentTimeMillis()))));
			entityManager.merge(reimbQuery);
			log.info("------ReimbursementQuery------>"+reimbQuery+"<------------");
			entityManager.flush();
//			try
//			{
//				if(null != utx)
//				{
//					utx.commit();
//				}
//			}catch(Exception e)
//			{
//				log.error("*******************************ERROR OCCURED DURING UTX COMMIT*************THIS CAN BE INGORED***************");
//			}
			
		}*/
	
/*	private Map<String, String> submitReimbursementQueryTask(List<HumanTask> listOfHumanTasks,String strIntimationNo,long stageId)
	{
		Map<String , String> statusMap = new HashMap<String, String>();
		if(null != listOfHumanTasks && !listOfHumanTasks.isEmpty())
		{
			SubmitQueryReplyDocReimbTask submitReimbQueryReplyTask = BPMClientContext.getReimbSubmitQueryReplyDocTask(BPMClientContext.BPMN_TASK_USER, "Star@123");
			for (HumanTask humanTask : listOfHumanTasks) {
				
				com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType reimbPayload = humanTask.getPayload();
				RODType rodType = reimbPayload.getRod();
				if(null != rodType)
				{
					Long rodKey = rodType.getKey();
					Reimbursement reimbObj = getReimbursementObject(rodKey);
					updateReimbursementTable(reimbObj,stageId);
					updateReimbursementQuery(reimbObj,stageId);
					humanTask.setOutcome("QUERY");
					
					try{
						
					com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType payload = humanTask.getPayload();
					if(payload != null && payload.getClassification() != null){
						payload.getClassification().setType(SHAConstants.QUERY_REPLY);
						humanTask.setPayload(payload);
					}
					submitReimbQueryReplyTask.execute(BPMClientContext.BPMN_TASK_USER, humanTask);
					
					statusMap.put(SHAConstants.REIMBURSEMENT_NUMBER,reimbObj.getRodNumber());
					if(null != reimbObj.getClaim())
					{
						statusMap.put(SHAConstants.CLAIM_NUMBER,reimbObj.getClaim().getClaimId());
					//else if(null != preauth.getIntimation())
						statusMap.put(SHAConstants.INTIMATION_NUMBER, reimbObj.getClaim().getIntimation().getIntimationId());
					}
					
					}catch(Exception e){
						e.printStackTrace();
						log.error(e.toString());
					}
				}
			}
			
			statusMap.put("5", "Intimation '"+strIntimationNo+"' is successfully submitted to BPMN for further processing");
			return statusMap;	
		}
		return null;
	}*/
	
	private Map<String, String> submitQueryReport(
			DocUploadToPremia docUploadToPremia,
			Map<String, String> statusMap, String strOutCome,
			Claim claimObj, List<BigDecimal> cashlesskeys,
			List<BigDecimal> reimbursementKeys) throws Exception {
		Long stageId;
		//Reimbursement reimb = getReimbursementObjectForQuery(claimObj.getKey());
		Preauth preauth = getPreauthForQuery(claimObj.getKey());
		
		if(null != preauth && null != cashlesskeys && !cashlesskeys.isEmpty()) {
			PreauthQuery preauthQuery = getPreauthQueryRecord(preauth.getKey());
			if(null != preauthQuery && null != preauthQuery.getStage()) {
				stageId = preauthQuery.getStage().getKey();
				if(cashlesskeys.contains(BigDecimal.valueOf(preauth.getKey()))) {
					switch(String.valueOf(stageId)) {
						case "11":
//							strOutCome = "PREMEDICALQUERYREPLY";;
							strOutCome = SHAConstants.OUTCOME_FOR_QUERY_REPLY_FLP;
							break;
						case "12":
//							strOutCome = "PREMEDICALQUERYREPLYENH";
							strOutCome = SHAConstants.OUTCOME_FOR_QUERY_REPLY_FLE;
							break;
						case "13":
//							strOutCome = "QUERYREPLY";
							strOutCome = SHAConstants.OUTCOME_FOR_QUERY_REPLY_PP;
							if(docUploadToPremia.getPfdUpDocType() != null && docUploadToPremia.getPfdUpDocType().equalsIgnoreCase(SHAConstants.QUERY_REPLY_WITH_FINAL_BILL)){
								strOutCome = SHAConstants.OUTCOME_FOR_QUERY_REPLY_FLP;
							}
							break;
						case "14":
//							strOutCome = "QUERYREPLYENH";
							strOutCome = SHAConstants.OUTCOME_FOR_QUERY_REPLY_PE;
							if(docUploadToPremia.getPfdUpDocType() != null && docUploadToPremia.getPfdUpDocType().equalsIgnoreCase(SHAConstants.QUERY_REPLY_WITH_FINAL_BILL)){
								strOutCome = SHAConstants.OUTCOME_FOR_QUERY_REPLY_FLE;
							}
							break;
					}
//					List<HumanTask> listOfHumanTask = getCashlessQueryHumanTask(docUploadToPremia.getPfdUpIntmNo());
//					statusMap = submitCashlessQueryTask(listOfHumanTask, docUploadToPremia.getPfdUpIntmNo(),strOutCome);
//					
//					List<HumanTask> cashlessQueryEnhancementHumanTask = getCashlessQueryEnhancementHumanTask(docUploadToPremia.getPfdUpIntmNo());
//					if(cashlessQueryEnhancementHumanTask != null && !cashlessQueryEnhancementHumanTask.isEmpty()){
//					 statusMap = submitCashlessQueryEnhancementTask(cashlessQueryEnhancementHumanTask, docUploadToPremia.getPfdUpIntmNo(),strOutCome);
//					}
					
					/*setProcessingCpuCodeBasedOnLimit(preauth.getClaim().getIntimation(),
							docUploadToPremia.getPfdUpFFAXAmt());*/
					 
					 statusMap = submitQueryReplyTask(docUploadToPremia.getPfdUpIntmNo(), preauth, strOutCome,docUploadToPremia);
					 

				}
			}
		}
		
		
		
//		if(null != reimb && null != reimbursementKeys && !reimbursementKeys.isEmpty()) {
//			ReimbursementQuery reimbQuery = getReimbursementQuery(reimb.getKey());
//			if(null != reimbQuery && null != reimbQuery.getStage()) {
//				stageId = reimbQuery.getStage().getKey();
//				if(reimbursementKeys.contains(BigDecimal.valueOf(reimb.getKey()))) {
//					List<HumanTask> listOfHumanTask = getReimbursementQueryHumanTask(docUploadToPremia.getPfdUpIntmNo());
//					statusMap = submitReimbursementQueryTask(listOfHumanTask, docUploadToPremia.getPfdUpIntmNo(),stageId);
//				}
//			}
//		}
		
		/*if((ReferenceTable.PREMEDICAL_QUERY_STAGE).equals(stageId) || (ReferenceTable.PREAUTH_QUERY_STAGE).equals(stageId)
				|| (ReferenceTable.PREMEDICAL_ENHANCEMENT_QUERY_STAGE).equals(stageId) || (ReferenceTable.PREAUTH_ENHANCEMENT_QUERY_STAGE).equals(stageId) && (SHAConstants.QUERY_STAGE).equalsIgnoreCase(processValue))
		{
			switch(String.valueOf(stageId))
			{
				case "11":
					strOutCome = "PREMEDICALQUERYREPLY";
					break;
				case "12":
					strOutCome = "PREMEDICALQUERYREPLYENH";
					break;
				case "13":
					strOutCome = "QUERYREPLY";
					break;
				case "14":
					strOutCome = "QUERYREPLYENH";
					break;
			}
			List<HumanTask> listOfHumanTask = getCashlessQueryHumanTask(docUploadToPremia.getPfdUpIntmNo());
			statusMap = submitCashlessQueryTask(listOfHumanTask, docUploadToPremia.getPfdUpIntmNo(),strOutCome);
		}*/
		/*else if((ReferenceTable.MA_CORPORATE_QUERY_STAGE).equals(stageId) || (ReferenceTable.FINANCIAL_QUERY_STAGE).equals(stageId))
		{
			
			
			List<HumanTask> listOfHumanTask = getReimbursementQueryHumanTask(docUploadToPremia.getPfdUpIntmNo());
			statusMap = submitReimbursementQueryTask(listOfHumanTask, docUploadToPremia.getPfdUpIntmNo(),stageId);
		}*/
		return statusMap;
	}
	
/*	private Map<String, String> submitCashlessQueryEnhancementTask(List<HumanTask> listOfHumanTasks,String strIntimationNo,String strOutCome)
	{
		Map<String , String> statusMap = new HashMap<String, String>();
		Preauth preauth = null;
		if(null != listOfHumanTasks && !listOfHumanTasks.isEmpty())
		{
			SubmitQueryReplyEnhDocTask submitQueryReplyTask = BPMClientContext.getSubmitQueryReplyEnhancementDocTask(BPMClientContext.BPMN_TASK_USER, "Star@123");
			for (HumanTask humanTask : listOfHumanTasks) {
				PayloadBOType payloadBO =  humanTask.getPayloadCashless();
				//ClaimType claimType = payloadBO.getClaim();
				PreAuthReqType preauthType = payloadBO.getPreAuthReq();
				Long key = preauthType.getKey();
				if(null != key)
				{
					preauth = getPreauthById(key);
					if(("PREMEDICALQUERYREPLY").equals(strOutCome) || ("PREMEDICALQUERYREPLYENH").equals(strOutCome))
					{
						preauthType.setKey(preauth.getClaim().getKey());
						//updatePreauthStatusForQuery(preauth,strOutCome);
						//updateCashlessQueryTable(preauth,strOutCome);
					}
					
					updatePreauthStatusForQuery(preauth,strOutCome);
					updateCashlessQueryTable(preauth,strOutCome);
					
					else{
						if(("QUERYREPLY").equals(strOutCome) || ("QUERYREPLYENH").equals(strOutCome)){
							updatePreauthStatusForQuery(preauth, strOutCome);
							updateCashlessQueryTable(preauth,strC)
						}
						
						
					}
					
					
				}
				payloadBO.setPreAuthReq(preauthType);
				
				ClassificationType classification = payloadBO.getClassification();
				classification.setType(SHAConstants.QUERY_REPLY);
				payloadBO.setClassification(classification);
				
				humanTask.setPayloadCashless(payloadBO);
				humanTask.setOutcome(strOutCome);
				
				try{
				submitQueryReplyTask.execute(BPMClientContext.BPMN_TASK_USER, humanTask);
					statusMap.put(SHAConstants.CASHLESS_NUMBER,preauth.getPreauthId());
				if(null != preauth.getClaim())
				{
					statusMap.put(SHAConstants.CLAIM_NUMBER,preauth.getClaim().getClaimId());
				//else if(null != preauth.getIntimation())
					statusMap.put(SHAConstants.INTIMATION_NUMBER, preauth.getClaim().getIntimation().getIntimationId());
				}
				
				}catch(Exception e){
					e.printStackTrace();
					log.error(e.toString());
				}
			}
			
			statusMap.put("5", "Intimation '"+strIntimationNo+"' is successfully submitted to BPMN for further processing");
			return statusMap;	
		}
		return null;
		
			
	}*/
	
	private Boolean isPreauthApprovedStatus(Intimation intimation, Claim claim){
		if(claim != null){
			Preauth preauth = getLatestPreauthByClaimKey(claim.getKey());
			if(preauth != null && (preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS)
					|| preauth.getStatus().getKey().equals(ReferenceTable.ACKNOWLEDGE_HOSPTIAL_STATUS)
					|| preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REOPENED_STATUS)))){
				return true;
			}
		}
		return false;
		
	}
	
	private Boolean isEnhancementApprovedOrDownsize(Intimation intimation, Claim claim){
		if(claim != null){
			Preauth preauth = getLatestPreauthByClaimKey(claim.getKey());
			if(preauth != null && preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)
					|| preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS)
					|| preauth.getStatus().getKey().equals(ReferenceTable.DOWNSIZE_APPROVED_STATUS)
					|| preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT)
					|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS)
					|| preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)
					|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS))){
				/**
				 * As discussed with satish sir, the below code is commended.
				 */
//				if((preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT)
//					|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS)) && preauth.getWithdrawReason() != null && preauth.getWithdrawReason().getKey().equals(ReferenceTable.PATIENT_NOT_ADMITTED_FOR_WITHDRAW)) {
//					return false;
//				}
				return true;
			}
		}
		return false;
	}
	
	private Boolean isEnhancementRejectStatus(Intimation intimation, Claim claim){
		if(claim != null){
			Preauth preauth = getLatestPreauthByClaimKey(claim.getKey());
			if(preauth != null && (preauth.getStatus() != null && preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS))){
				return true;
			}
		}
		return false;
	 }
	
	private Preauth getPreauthForClaim(Long key) {
		Query query = entityManager.createNamedQuery("Preauth.findLatestPreauthByClaim");
		query = query.setParameter("claimkey", key);
		List<Preauth> preauthList = query.getResultList();
		if(null != preauthList && !preauthList.isEmpty()) {
			Preauth preauth = preauthList.get(0);
			entityManager.refresh(preauth);
			return preauth;
		}
		return null;
	}
	
	/**
	 * Method to invoke BPM process for star fax
	 * @throws Exception 
	 * 
	 * */
/*	public void invokeBPMProcess(Claim claimObject,String strType, Intimation objIntimation, Preauth objPreAuth) throws Exception {
		if (null != claimObject) {
			Intimation intimation = claimObject.getIntimation();
			IntimationRule intimationRule = new IntimationRule();
			//IntimationMessage intimationMessage = new IntimationMessage();
			IntimationType intimationType = new IntimationType();
			PolicyType policyType = new PolicyType();
			PaymentInfoType paymentInfoType = new PaymentInfoType();
			HospitalInfoType hospitalInfoType = new HospitalInfoType();
			PreAuthReqType preauthReqType = new PreAuthReqType();
			ClaimType claimType = new ClaimType();
			ClaimRequestType claimReqType = new ClaimRequestType();
			
//			Output output = new Output();
//			PreAuthReq preAuthReq = new PreAuthReq();
//			PreAuthReqDetails preAuthReqDetails = new PreAuthReqDetails();
			
			intimationType.setIntimationNumber(objIntimation.getIntimationId());
			paymentInfoType.setClaimedAmount(claimObject.getClaimedAmount());
			//hospitalInfoType.setHospitalType(objHosp.getHospitalTypeName().toUpperCase());
			hospitalInfoType.setHospitalType(intimation.getHospitalType().getValue());
			intimationType.setIntimationSource(intimation.getIntimationSource() != null ? intimation.getIntimationSource().getValue() : "");
			if(intimation.getHospital() != null){
				Hospitals hospitalById = getHospitalById(intimation.getHospital());
				if(hospitalById.getNetworkHospitalTypeId() != null && hospitalById.getNetworkHospitalTypeId().equals(ReferenceTable.NETWORK_HOSPITAL)){
					hospitalInfoType.setNetworkHospitalType(SHAConstants.NETWORK_HOSPITAL);
				}else if(hospitalById.getNetworkHospitalTypeId() != null && hospitalById.getNetworkHospitalTypeId().equals(ReferenceTable.AGREED_NETWORK_HOSPITAL)){
					hospitalInfoType.setNetworkHospitalType(SHAConstants.AGREED_NETWORK_HOSPITAL);
				}else if(hospitalById.getNetworkHospitalTypeId() != null && hospitalById.getNetworkHospitalTypeId().equals(ReferenceTable.GREEN_NETWORK_HOSPITAL)){
					hospitalInfoType.setNetworkHospitalType(SHAConstants.GREEN_NETWORK_HOSPITAL);
				}else{
					hospitalInfoType.setNetworkHospitalType("");
				}
//				hospitalInfoType.setNetworkHospitalType(hospitalById.getNetworkHospitalType() != null ? hospitalById.getNetworkHospitalType() : "");
			}
			
			claimReqType.setOption(claimObject.getStage() != null ? claimObject.getStage().getStageName() : " ");
			
			if(("PREAUTHRECEIVED").equals(strType)){
				
				claimReqType.setOption(claimObject.getStage() != null ? claimObject.getStage().getStageName() : " ");
				
			}
			
//			hospitalInfoType.setNetworkHospitalType(intimation.get);
			claimType.setClaimType(claimObject.getClaimType().getValue().toUpperCase());
			if(null != intimation.getCpuCode())
			{
				if(null != intimation.getCpuCode().getCpuCode())
				{
					claimReqType.setCpuCode(String.valueOf(intimation.getCpuCode().getCpuCode()));
				}
			}
			Boolean isBalSIAvailable = isBalanceSIAvailable(claimObject.getIntimation().getInsured().getInsuredId(),claimObject.getIntimation().getPolicy().getKey());
			intimationType.setIsBalanceSIAvailable(isBalSIAvailable);
			//intimationMessage.setIsBalanceSIAvailable(isBalSIAvailable);
			intimationType.setIsPolicyValid(intimationRule.isPolicyValid(intimation));
			intimationType.setKey(intimation.getKey());
			paymentInfoType.setProvisionAmount(claimObject.getProvisionAmount());
			intimationType.setStatus(intimation.getStatus().getProcessValue());
			preauthReqType.setIsFVRReceived(false);
			preauthReqType.setOutcome(strType);
			preauthReqType.setResult(strType);
			preauthReqType.setPreAuthAmt(0d);
			com.shaic.ims.bpm.claim.servicev2.dms.SimulateStarfax simulateStarfax = BPMClientContext.getSimulateStarfax(BPMClientContext.BPMN_TASK_USER, "Star@123");
			
			if(("QUERYREPLY").equals(strType) || ("QUERYREPLYENH").equals(strType))
			{
				preauthReqType.setKey(objPreAuth.getKey());
//				preAuthReq.setKey(objPreAuth.getKey());
			}
			else
			{
				
				preauthReqType.setKey(claimObject.getKey());
				//claimType.setKey(claimObject.getKey());
				//preAuthReq.setKey(claimObject.getKey());
			}
			policyType.setPolicyId(objIntimation.getPolicy().getPolicyNumber());
			
            ClassificationType classificationType = new ClassificationType();
            
            Insured insured = intimation.getInsured();
 
 			if(claimObject != null && claimObject.getIsVipCustomer() != null && claimObject.getIsVipCustomer().equals(1l)){
 				
 				classificationType.setPriority(SHAConstants.VIP_CUSTOMER);
 			}
 			else if(insured != null && insured.getInsuredAge() != null && insured.getInsuredAge()>60){
 				classificationType.setPriority(SHAConstants.SENIOR_CITIZEN);
 			}else{
 				classificationType.setPriority(SHAConstants.NORMAL);
 			}
 		
 			classificationType.setType(SHAConstants.TYPE_FRESH);
 			classificationType.setSource(SHAConstants.NORMAL);
 			
 			
 			
 			QueryType queryType = new QueryType();
 			queryType.setStatus("No Query");
 			
 			
 			ProductInfoType productInfoType = new ProductInfoType();
 			productInfoType.setLob(SHAConstants.HEALTH_LOB);
			productInfoType.setLobType(SHAConstants.HEALTH_LOB_FLAG);
			if(null != intimation.getLobId()) {
				if(ReferenceTable.PA_LOB_KEY.equals(intimation.getLobId().getKey())) {
					intimationType.setReason(intimation.getIncidenceFlag()); // will be A or D. A-accident and D-Death.
					intimationType.setIsBalanceSIAvailable(false);
					productInfoType.setLob(SHAConstants.PA_LOB);
					productInfoType.setLobType(SHAConstants.PA_LOB_TYPE);
					
					String strClaimType = "";
					
					if((SHAConstants.ACCIDENT_FLAG).equalsIgnoreCase(intimation.getIncidenceFlag()) && null != intimation.getHospitalType() && null != intimation.getHospitalType().getKey() && intimation.getHospitalType().getKey().equals(ReferenceTable.PREMIA_INTIMTION_PROCESS_NETWORK_HOSPITAL)) {
						strClaimType = SHAConstants.CASHLESS_CLAIM_TYPE;
					} else { //if(null != intimation.getHospitalType() && null != intimation.getHospitalType().getKey() && intimation.getHospitalType().getKey().equals(ReferenceTable.PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL)) {
						strClaimType = SHAConstants.REIMBURSEMENT_CLAIM_TYPE;
					}
					claimType.setClaimType(strClaimType);	
				}
				if((ReferenceTable.HEALTH_LOB_KEY).equals(intimation.getLobId().getKey())) {
					productInfoType.setLob(SHAConstants.HEALTH_LOB);
					productInfoType.setLobType(SHAConstants.HEALTH_LOB_FLAG);
				}
				if((ReferenceTable.PACKAGE_MASTER_VALUE).equals(intimation.getLobId().getKey())) {
//					productInfoType.setLob(SHAConstants.PACKAGE_LOB_TYPE);
//					productInfoType.setLobType(SHAConstants.PACKAGE_LOB_TYPE);
					productInfoType.setLob(SHAConstants.HEALTH_LOB);
					productInfoType.setLobType(SHAConstants.HEALTH_LOB_FLAG);
				}
			}
			
			
			PayloadBOType payload = new PayloadBOType();
			payload.setIntimation(intimationType);
			payload.setPolicy(policyType);
			payload.setPreAuthReq(preauthReqType);
			payload.setQuery(queryType);
			payload.setClaim(claimType);
			payload.setClaimRequest(claimReqType);
			payload.setPaymentInfo(paymentInfoType);
			payload.setHospitalInfo(hospitalInfoType);
			payload.setClassification(classificationType);
			payload.setProductInfo(productInfoType);
			//payload.set
			//preAuthReqDetails.setPolicyId(objIntimation.getPolicy().getPolicyNumber());
			//preAuthReq.setPreAuthReqDetails(preAuthReqDetails);
			log.info("BEFORE INITIATING BPMN----> Intimation Id-->" + objIntimation.getIntimationId());
			simulateStarfax.initiate(BPMClientContext.BPMN_TASK_USER, payload);
			log.info("AFTER INITIATING BPMN----> Intimation Id-->" + objIntimation.getIntimationId());
		}
	}*/
	
	private void lockPolicy(Claim objClaim, Hospitals hospitalObj) {
		if(null != objClaim && hospitalObj != null) {
			PremiaService.getInstance().getPolicyLock(objClaim, hospitalObj.getHospitalCode());
		}
	}
	
	public Map<String, String> processPreMedicalPreAuthAndPreMedicalEnhancement(String strIntimationNo, String claimRequest,String strType, Intimation objIntimation, Claim objClaim, Long estarFaxAmt,Date pfdUpFfaxSubmitDate,DocUploadToPremia docUploadToPremia ) throws Exception {
		Map <String , String> statusMap = new HashMap<String, String>();
		String strIntimationStatus = "";
		if(isIntimationExist(strIntimationNo)) {
			strIntimationStatus = objIntimation.getStatus().getProcessValue();
			/**
			 * If the intimation Id status is SUBMITTED, further processing is 
			 * done. Else error message is displayed.
			 * */
			
			if((SHAConstants.SUBMITTED).equalsIgnoreCase(strIntimationStatus) || SHAConstants.CLAIM_WISE_APPROVAL.equalsIgnoreCase(strIntimationStatus)) {
				/**
				 * Datas from VW_HOSPITALS is loaded , since 
				 * hosptial type is required when data is 
				 * submitted to BPM. Hence with 
				 * HOSPITAL_NAME_ID column value, we populate 
				 * hospital data from VW_HOSPITAL table d.
				 * 
				 * */
				Hospitals hospitalObject = getHospitalObject(objIntimation.getHospital());

				if(SHAConstants.REGISTERED.equalsIgnoreCase(objIntimation.getRegistrationStatus())) {
					if(("REIMBURSEMENT").equalsIgnoreCase(claimRequest)) {
//						objClaim = getClaimObject(objIntimation.getKey());
						MastersValue masClaimType = new MastersValue();
						masClaimType.setKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
						objClaim.setClaimType(masClaimType);
						if(null != objClaim.getKey()) {
							entityManager.merge(objClaim);
							entityManager.flush();
							entityManager.clear();
						}
					} else {
//						objClaim = getClaimObject(objIntimation.getKey());
					}
					if(null != objClaim) {			
						log.info("=======this is a claim object----------"+objClaim.getClaimId());
						log.info("----claim request --- "+ claimRequest);
						log.info(":-----the strtype----"+ strType);
						if(("CASHLESS").equals(claimRequest))
						{
							Preauth preauth = getPreauthForClaim(objClaim.getKey());
							if(null != preauth)
							{
								if(("ENHANCE").equalsIgnoreCase(strType))
								{
									log.info("--========-inside the ehnacement block======= "+objClaim.getStage().getKey());
									/*
									 * Enhancement can be intiated only if pre auth is approved.
									 * */
									if((null != preauth.getStage() && null != preauth.getStage().getKey() && preauth.getStage().getKey().equals(ReferenceTable.PREAUTH_STAGE) 
											&& null != preauth.getStatus() &&  null != preauth.getStatus().getKey() && preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS)) ||
											(null != preauth.getStage() && null != preauth.getStage().getKey() && preauth.getStage().getKey().equals(ReferenceTable.ENHANCEMENT_STAGE) 
											&& ((null != preauth.getStatus() &&  null != preauth.getStatus().getKey() && preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)) ||
													(null != preauth.getStatus() &&  null != preauth.getStatus().getKey() && preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS)) ||
													(null != preauth.getStatus() &&  null != preauth.getStatus().getKey() && preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_DOWNSIZE_STATUS)))) ||
											(null != preauth.getStage() && null != preauth.getStage().getKey() && preauth.getStage().getKey().equals(ReferenceTable.ENHANCEMENT_STAGE)
											&& null != preauth.getStatus() &&  null != preauth.getStatus().getKey() && preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS)) ||
											
											(null != preauth.getStage() && null != preauth.getStage().getKey() && preauth.getStage().getKey().equals(ReferenceTable.ENHANCEMENT_DOWNSIZE_STAGE) &&
													null != preauth.getStatus() &&  null != preauth.getStatus().getKey() && preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_DOWNSIZE_STATUS)) ||
													
													
													(null != preauth.getStage() && null != preauth.getStage().getKey() && preauth.getStage().getKey().equals(ReferenceTable.ENHANCEMENT_STAGE) &&
													null != preauth.getStatus() &&  null != preauth.getStatus().getKey() && preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT)) ||
													
													(null != preauth.getStage() && null != preauth.getStage().getKey() && preauth.getStage().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STAGE) &&
													null != preauth.getStatus() &&  null != preauth.getStatus().getKey() && preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS))||
													
												    (null != preauth.getStage() && null != preauth.getStage().getKey() && preauth.getStage().getKey().equals(ReferenceTable.ENHANCEMENT_STAGE) &&
													null != preauth.getStatus() &&  null != preauth.getStatus().getKey() && preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS))||
													
													 (null != preauth.getStage() && null != preauth.getStage().getKey() && preauth.getStage().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STAGE) &&
														null != preauth.getStatus() &&  null != preauth.getStatus().getKey() && preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS))

											|| (null != preauth.getStage() && null != preauth.getStage().getKey() && preauth.getStage().getKey().equals(ReferenceTable.ACKNOWLEDGE_HOSPITAL_STAGE) 
													&& null != preauth.getStatus() &&  null != preauth.getStatus().getKey() && preauth.getStatus().getKey().equals(ReferenceTable.ACKNOWLEDGE_HOSPTIAL_STATUS)))
									{
										System.out.println("---before invoking bpmn process");
//										invokeBPMProcess(objClaim,strType, objIntimation, preauth);
										
										setProcessingCpuCodeBasedOnLimit(objClaim.getIntimation(),
												estarFaxAmt);
										
										submitDBProcedureForFirstLevelEnhancement(objClaim,preauth,estarFaxAmt,pfdUpFfaxSubmitDate,docUploadToPremia);
										
										if(getDBTaskForPreauth(objIntimation, SHAConstants.REIM_CONVERSION_PROCESS_CURRENT_QUEUE,null)){
											submitDBprocedure(objClaim, SHAConstants.REIM_CONVERSION_PROCESS_CURRENT_QUEUE, SHAConstants.CONVERSION_REIMBURSEMENT_END,null);
										}
										
										System.out.println("---after invoking bpmn process");
									}else if(preauth.getStatus() != null && preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REOPENED_STATUS)){
										Boolean alreadyPreauthApprovedForReopen = isAlreadyPreauthApprovedForReopen(preauth.getKey());
										if(alreadyPreauthApprovedForReopen){
											
											setProcessingCpuCodeBasedOnLimit(objClaim.getIntimation(),
													estarFaxAmt);
											
											submitDBProcedureForFirstLevelEnhancement(objClaim,preauth,estarFaxAmt,pfdUpFfaxSubmitDate,docUploadToPremia);
											
											if(getDBTaskForPreauth(objIntimation, SHAConstants.REIM_CONVERSION_PROCESS_CURRENT_QUEUE,null)){
												submitDBprocedure(objClaim, SHAConstants.REIM_CONVERSION_PROCESS_CURRENT_QUEUE, SHAConstants.CONVERSION_REIMBURSEMENT_END,null);
											}
										}
									}
								}
							}
						}
						objIntimation.setRegistrationType("MANUAL");
						//objIntimation.setC
						updateIntimation(objIntimation);
						lockPolicy(objClaim, hospitalObject);
						statusMap.put("1","Intimation '"+strIntimationNo+"' is successfully submitted to BPMN for further processing");
						statusMap.put(SHAConstants.CLAIM_NUMBER, objClaim.getClaimId());
						statusMap.put(SHAConstants.INTIMATION_NUMBER, objIntimation.getIntimationId());
					}
				} else if(null == objIntimation.getRegistrationStatus() || ("").equals(objIntimation.getRegistrationStatus())) {
					/**
					 * Service for invoking claim registration needs to be added.
					 * */
					objClaim = getClaimObject(objIntimation.getKey());
					
					if(null != objClaim && ("REIMBURSEMENT").equalsIgnoreCase(claimRequest)) {
						MastersValue masClaimType = new MastersValue();
						masClaimType.setKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
						objClaim.setClaimType(masClaimType);
					}
					if(("REIMBURSEMENT").equalsIgnoreCase(claimRequest) && null != objClaim && null != objClaim.getKey()) {
						entityManager.merge(objClaim);
						entityManager.flush();
						entityManager.clear();
					}
					//createClaim(objClaim);
					if(("CASHLESS").equals(claimRequest) && null != objClaim )
					{
						Preauth preauth = getPreauthForClaim(objClaim.getKey());
						/*
						 * Enhancement can be intiated only if pre auth is approved.
						 * */
						if((null != preauth.getStage() && null != preauth.getStage().getKey() && preauth.getStage().getKey().equals(ReferenceTable.PREAUTH_STAGE) 
								&& null != preauth.getStatus() &&  null != preauth.getStatus().getKey() && preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS)) ||
								(null != preauth.getStage() && null != preauth.getStage().getKey() && preauth.getStage().getKey().equals(ReferenceTable.ENHANCEMENT_STAGE) 
								&& null != preauth.getStatus() &&  null != preauth.getStatus().getKey() && preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)) ||
								(null != preauth.getStage() && null != preauth.getStage().getKey() && preauth.getStage().getKey().equals(ReferenceTable.ENHANCEMENT_STAGE)
								&& null != preauth.getStatus() &&  null != preauth.getStatus().getKey() && preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS))
								)
						{
						//	invokeBPMProcess(objClaim,strType, objIntimation, preauth);
						}
					}
					objIntimation.setRegistrationType("AUTO");
					objIntimation.setRegistrationStatus(SHAConstants.REGISTERED);
					MastersValue claimMaster = new  MastersValue();
					//claimMaster.setKey(105l);
					claimMaster.setKey(ReferenceTable.CLAIM_TYPE_REIMBURSEMENT_ID);
					objIntimation.setClaimType(claimMaster);
					updateIntimation(objIntimation);
					lockPolicy(objClaim, hospitalObject);
					statusMap.put("1","Intimation '"+strIntimationNo+"' is successfully submitted to BPMN for further processing");
					if(null != objClaim)
					{	
						statusMap.put(SHAConstants.CLAIM_NUMBER, objClaim.getClaimId());
					}
					else
					{
						statusMap.put(SHAConstants.CLAIM_NUMBER, null);
					}
					statusMap.put(SHAConstants.INTIMATION_NUMBER, objIntimation.getIntimationId());
				} else  {
					statusMap.put("2",objIntimation.getRegistrationStatus());	
				}
			} else {
				statusMap.put("3", strIntimationStatus);
			}
		} else {
			statusMap.put("4", strIntimationNo);
		}
		return statusMap;
	}
	
	/*private Boolean isFvrReplyTask(Intimation intimation){
		try {
			List<Map<String, Object>> taskProcedure = getReimbursementFVRTask(intimation.getIntimationId());
			if(taskProcedure != null && ! taskProcedure.isEmpty()){
				return true;
			}
			
		}catch(Exception e){
			log.error("*************ERROR OCCURED IN FVR TASK *********----->");
		}
		
		return false;
	}*/
	
	private  List<Map<String, Object>>  getReimbursementFVRTask(String intimationNo)
	{

		Map<String, Object> mapValues = new WeakHashMap<String, Object>();
		mapValues.put(SHAConstants.INTIMATION_NO, intimationNo);
		mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.FVR_CURRENT_QUEUE);
		
		Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
		
		DBCalculationService db = new DBCalculationService();
		 List<Map<String, Object>> taskProcedure = db.getTaskProcedure(setMapValues);
		if (taskProcedure != null && !taskProcedure.isEmpty()){
			return taskProcedure;
		} 
		return null;
	}
	
	private List<FieldVisitRequest> getFieldVisitRequest(Long intimationKey) {
		try {
			Query query = entityManager
					.createNamedQuery("FieldVisitRequest.findByIntimationKey");
			query = query.setParameter("intimationKey", intimationKey);
			List<FieldVisitRequest> fvrList  = query.getResultList();
			return fvrList;
		}
		catch(Exception e) {
			log.error("********** ERROR OCCURED IN GET FIELD VISIT METHOD********----INTIMATION KEY----->"+ intimationKey);
			return null;
		}
	}
	
	private Map<String, String> submitReimbursementFVRTask(List<Map<String, Object>> reimbursementFVRTask,String strIntimationNo)
	{
		Map<String , String> statusMap = new HashMap<String, String>();
		if(null != reimbursementFVRTask && !reimbursementFVRTask.isEmpty())
		{
			DBCalculationService dbCalService = new DBCalculationService();
			for (Map<String, Object> wrkFlowMap : reimbursementFVRTask) {
				wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.FVR_ASSIGN_OUTCOME);
				Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
				//dbCalService.initiateTaskProcedure(objArrayForSubmit);
				log.info("@@@@CALL SUBMIT TASK PROCEDURE STARTING TIME  --------> "+System.currentTimeMillis());
				dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
				log.info("@@@@CALL SUBMIT TASK PROCEDURE ENDING TIME  --------> "+System.currentTimeMillis());
			}
			statusMap.put("5", "Intimation '"+strIntimationNo+"' is successfully submitted to BPMN for further processing");
			return statusMap;	
		}
		return null;
	}
	
	private void submitFVR(DocUploadToPremia docUploadToPremia,
			List<FieldVisitRequest> fvrList, Map<String, String> statusMap)  throws Exception{
		for (FieldVisitRequest fieldVisitRequest : fvrList) {
			Stage reqStage = fieldVisitRequest.getStage();
			if( null != reqStage) {
				Long key = reqStage.getKey();
				/**
				 * Only for reimbursement, FVR needs to be submitted back to the
				 * respective inbox and db update should happen. In case of
				 * cashelss, only db update will happen. This is becasue for cashless
				 * FVR is a parallel process and tasks ends once FVR process is done. 
				 **/
				if((ReferenceTable.CLAIM_REQUEST_STAGE).equals(key) || (ReferenceTable.FINANCIAL_STAGE).equals(key)) {
					 List<Map<String, Object>> reimbursementFVRTask = getReimbursementFVRTask(docUploadToPremia.getPfdUpIntmNo());
					statusMap = submitReimbursementFVRTask(reimbursementFVRTask, docUploadToPremia.getPfdUpIntmNo());
					if(null == statusMap) {
						statusMap = new HashMap<String, String>();
					}
					if(null != fieldVisitRequest.getTransactionFlag() && ("R").equalsIgnoreCase(fieldVisitRequest.getTransactionFlag()))
					{
						Reimbursement reimbursement = getReimbursementObject(fieldVisitRequest.getTransactionKey());
						if(null != reimbursement)
							statusMap.put(SHAConstants.REIMBURSEMENT_NUMBER,reimbursement.getRodNumber());
					}
					else if(null != fieldVisitRequest.getTransactionFlag() && ("C").equalsIgnoreCase(fieldVisitRequest.getTransactionFlag()))
					{
						Preauth preauth = getPreauthById(fieldVisitRequest.getTransactionKey());
						if(null != preauth)
							statusMap.put(SHAConstants.CASHLESS_NUMBER,preauth.getPreauthId());
					}
					if(null != fieldVisitRequest.getClaim())
						statusMap.put(SHAConstants.CLAIM_NUMBER,fieldVisitRequest.getClaim().getClaimId());
					if(null != fieldVisitRequest.getIntimation())
						statusMap.put(SHAConstants.INTIMATION_NUMBER, fieldVisitRequest.getIntimation().getIntimationId());
					
				}
				else
				{
					if(null != fieldVisitRequest.getTransactionFlag() && ("C").equalsIgnoreCase(fieldVisitRequest.getTransactionFlag()))
					{
						Preauth preauth = getPreauthById(fieldVisitRequest.getTransactionKey());
						if(null != preauth)
							statusMap.put(SHAConstants.CASHLESS_NUMBER,preauth.getPreauthId());
					}
					if(null != fieldVisitRequest.getClaim())
						statusMap.put(SHAConstants.CLAIM_NUMBER,fieldVisitRequest.getClaim().getClaimId());
					if(null != fieldVisitRequest.getIntimation())
						statusMap.put(SHAConstants.INTIMATION_NUMBER, fieldVisitRequest.getIntimation().getIntimationId());
				}
					
			 }	
			
			fieldVisitRequest.setFvrReceivedDate(new Timestamp(System.currentTimeMillis()));
			fieldVisitRequest.setDocumentReceivedFlag(1l);
			Stage fvrStage = new Stage();
			fvrStage.setKey(ReferenceTable.FVR_STAGE_KEY);
			Status fvrStatus = new Status();
			fvrStatus.setKey(ReferenceTable.FVR_REPLY_RECEIVED);
			fieldVisitRequest.setStage(fvrStage);
			fieldVisitRequest.setStatus(fvrStatus);
			if(null != fieldVisitRequest.getKey()) {
				entityManager.merge(fieldVisitRequest);
				entityManager.flush();
				entityManager.clear();
				if(statusMap == null){
					statusMap = new HashMap<String, String>();
				}
				statusMap.put("1", "FVR Processed successfully");
			}
		}
	}
	
	public Boolean isAnyRodActive(Long claimKey) {
		List<Reimbursement> previousRODByClaimKey = getPreviousRODByClaimKey(claimKey);
		if (previousRODByClaimKey != null && !previousRODByClaimKey.isEmpty()) {
			for (Reimbursement reimbursement : previousRODByClaimKey) {
				if(reimbursement.getStatus() != null && !ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey())) { 
//						&& !ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey())) {
						return true;					
				}
			}
		}

		return false;
	}
	
	public List<Reimbursement> getPreviousRODByClaimKey(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.findLatestRODByClaimKey");
		query.setParameter("claimKey", claimKey);
		List<Reimbursement> resultList = query.getResultList();
		if (!resultList.isEmpty()) {
			for (Reimbursement reimbursement : resultList) {
				entityManager.refresh(reimbursement);
			}
		}
		return resultList;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void updateDocUploadPremiaForROD(String statusFlag , String flexFlag,DocUploadToPremia docUploadToPremia) {
			docUploadToPremia.setPfdUpPremiaAckDt((new Timestamp(System.currentTimeMillis())));
			docUploadToPremia.setPfdUpPremiaUploadSts(statusFlag);
			docUploadToPremia.setPfdUpFlexYN01(flexFlag);				
			docUploadToPremia.setGalaxyReadFlag(SHAConstants.YES_FLAG);
			entityManager.merge(docUploadToPremia);
			entityManager.flush();
			entityManager.clear();
			log.info("------DocUploadToPremia------>"+docUploadToPremia.getPfdUpIntmNo()+"<------------");
	}
	
	/*private Boolean isTaskFirstLevelEnhancement(Intimation intimation){
		try{	
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			mapValues.put(SHAConstants.INTIMATION_NO, intimation.getIntimationId());
			mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.FLE_CURRENT_QUEUE);
			
			Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
			
			DBCalculationService db = new DBCalculationService();
			 List<Map<String, Object>> taskProcedure = db.getTaskProcedure(setMapValues);
			if (taskProcedure != null && !taskProcedure.isEmpty()){
				return true;
			} 
			return false;
		}catch(Exception e){
			return false;
		}
	}*/
	
	/*private Boolean isTaskAvailableProcessPreauth(Intimation intimation){
		try{	
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			mapValues.put(SHAConstants.INTIMATION_NO, intimation.getIntimationId());
			mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.PP_CURRENT_QUEUE);
			
			Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
			
			DBCalculationService db = new DBCalculationService();
			 List<Map<String, Object>> taskProcedure = db.getTaskProcedure(setMapValues);
			if (taskProcedure != null && !taskProcedure.isEmpty()){
				return true;
			} 
			return false;
		}catch(Exception e){
			return false;
		}
	}*/
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public String startProcessRevised(List<DocUploadToPremia> docUploadPremiaList) throws IllegalStateException, SecurityException, SystemException {
		
		String isSuccess = null;
		if(null != docUploadPremiaList && !docUploadPremiaList.isEmpty()) {
			for (DocUploadToPremia docUploadToPremia : docUploadPremiaList) {
				log.info("@@@@@@@@@ DOCUMENT UPLOADED STARTING TIME @@@@@@@@@@-----> "+"-----> " +docUploadToPremia.getPfdUpIntmNo()+"-----> "+ System.currentTimeMillis());
				Map<String, String> statusMapForPreauth = new HashMap<String, String>();
				String strType;
				String curenntQ; 
				try {
					utx.begin();
					Intimation intimation = getIntimationObject(docUploadToPremia.getPfdUpIntmNo());
					String docType = docUploadToPremia.getPfdUpDocType();
					Claim claim = null;
					if(intimation != null) {
						claim = getClaimObject(intimation.getKey());
					} else {
						log.debug("############################ INTIMATION NOT PULLED YET#############################");
					}
					
					if(null != claim) {
					  if(isAnyRodActive(claim.getKey())) {
						  updateDocUploadPremiaForROD(SHAConstants.ROD_CREATED_FLAG, SHAConstants.N_FLAG, docUploadToPremia);
					  } else if (isDBWaitingForPreauth(intimation) && claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
						  
						  log.info("############# WAITING FOR PREAUTH QUEUE ############### ----->" + intimation.getIntimationId());
							strType = SHAConstants.PREAUTH_RECEIVED;
							curenntQ ="FLPA";
							statusMapForPreauth = processPreMedical(docUploadToPremia.getPfdUpIntmNo(),intimation, docUploadToPremia.getPfdUpFFAXAmt(),SHAConstants.CLAIMREQUEST_CASHLESS,strType,docUploadToPremia.getPfdUpFfaxSubmitId(),docUploadToPremia);
							if(updateDocUploadPremiaTbl(statusMapForPreauth,docUploadToPremia)){
								isSuccess = processReportIdForPremiaDocument(docUploadToPremia,statusMapForPreauth, claim,curenntQ);
							}
						  
					  }	 
//					  else if(intimation != null && isWaitingForPreauth(intimation)) {
//						  //****************** WAITING FOR PREAUTH ***********************//
//							log.info("############# WAITING FOR PREAUTH QUEUE ############### ----->" + intimation.getIntimationId());
//							strType = SHAConstants.PREAUTH_RECEIVED;
//							statusMapForPreauth = processPreMedical(docUploadToPremia.getPfdUpIntmNo(),intimation, docUploadToPremia.getPfdUpFFAXAmt(),SHAConstants.CLAIMREQUEST_CASHLESS,strType);
//							if(updateDocUploadPremiaTbl(statusMapForPreauth,docUploadToPremia)){
//								isSuccess = processReportIdForPremiaDocument(docUploadToPremia,statusMapForPreauth, claim);
//							}
//							
//					 } 
					  
					  //getDBTaskForPreauth
					  
					  else if(intimation != null && isPreauthDenialOfCashlessOrReject(intimation, claim) && ! getDBTaskForPreauth(intimation,SHAConstants.PP_CURRENT_QUEUE,claim)
							  && claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
						  curenntQ ="PRAH";
						  CashlessWorkFlow workFlowByIntimation = getWorkFlowByIntimation(intimation.getIntimationId());
						  if(workFlowByIntimation == null){
								Preauth preauth = getPreauthForReconsider(intimation, claim);
								if(preauth != null) {
									setProcessingCpuCodeBasedOnLimit(intimation,
											docUploadToPremia.getPfdUpFFAXAmt());
	//								submitReconsiderationForCashless(intimation, preauth);
									submitDBProcedureForReconsideration(claim,preauth,docUploadToPremia.getPfdUpFFAXAmt(),docUploadToPremia.getPfdUpFfaxSubmitId(),docUploadToPremia);
									//added for reconsideration doc date to update in cashless table by noufel

									if(docUploadToPremia.getPfdUpFfaxSubmitId() != null){
										preauth.setSfxMatchedQDate(docUploadToPremia.getPfdUpFfaxSubmitId());
									}
									if(docUploadToPremia.getPfdUpPremiaAckDt() != null){
										preauth.setSfxRegisteredQDate(docUploadToPremia.getPfdUpPremiaAckDt());
									}
									entityManager.merge(preauth);
									entityManager.flush();
								}
						   }

							Map<String, String> statusMap = new HashMap<String, String>();
							
							statusMap.put("1", "Attached upload document only");
							statusMap = setValueForStatusMap(claim,statusMap);
							
							if(updateDocUploadPremiaTbl(statusMap,docUploadToPremia))
								isSuccess = processReportIdForPremiaDocument(docUploadToPremia,statusMap, claim,curenntQ);
							else
								isSuccess = "Failure occurred while processing the request.";
							
						}
//					  else if(intimation != null && isPreauthDenialOfCashlessOrReject(intimation, claim) && !isTaskAvailableProcessPreauth(intimation)) {
//						Preauth preauth = getPreauthForReconsider(intimation, claim);
//						if(preauth != null) {
//							log.info("____________ CHECKING FOR RECONSIDERATION ________");
//							submitReconsiderationForCashless(intimation, preauth);
//						}
//
//						Map<String, String> statusMap = new HashMap<String, String>();
//						
//						statusMap.put("1", "Attached upload document only");
//						statusMap = setValueForStatusMap(claim,statusMap);
//						
//						if(updateDocUploadPremiaTbl(statusMap,docUploadToPremia))
//							isSuccess = processReportIdForPremiaDocument(docUploadToPremia,statusMap, claim);
//						else
//							isSuccess = "Failure occurred while processing the request.";
//						
//					}
	//-----------------------------if intimation is getting from query reply document in BPMN Task and preauth and enhancement query status----------------------------------------------------------------------------------------------------------------------------------
					  
					  
					  else if(intimation != null && (getDBTaskForPreauth(intimation, SHAConstants.QUERY_REPLY_QUEUE,claim))
							  && claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
						  curenntQ ="WFQR";
							Map<String, String> statusMap = new HashMap<String, String>();
							String strOutCome = "";
							if(null != intimation) {
								if(null != claim && null != claim.getStage()) {
									//Long stageId = claim.getStage().getKey();
									//String processValue = claim.getStatus().getProcessValue();
									Map<String,List<BigDecimal>> unionMap = getUnionForReimbAndCashless();
									List<BigDecimal> cashlesskey = unionMap.get(SHAConstants.CASHLESS_CHAR);
									List<BigDecimal> reimbursementKey = unionMap.get(SHAConstants.REIMBURSEMENT_CHAR);
									if(null != unionMap && !unionMap.isEmpty()) {
										statusMap = submitQueryReport(docUploadToPremia, statusMap,strOutCome, claim,cashlesskey, reimbursementKey);
									}
									if(null != statusMap && statusMap.isEmpty()) {
										statusMap.put("2", "Failure occured while processing query");
									}
											
									if(updateDocUploadPremiaTbl(statusMap,docUploadToPremia))
										isSuccess = processReportIdForPremiaDocument(docUploadToPremia,statusMap, claim,curenntQ);
									else
										isSuccess = "Failure occurred while processing the request.";
		
								}
							}
		
						}
					  
//					else if(intimation != null && (isQueryReplyDocForCashless(intimation.getIntimationId())
//							|| isQueryReplyDocForReimbursement(intimation.getIntimationId()) || isQueryReplyDocEnhancementForCashless(intimation.getIntimationId()))) {
//						
//						Map<String, String> statusMap = new HashMap<String, String>();
//						String strOutCome = "";
//						if(null != intimation) {
//							if(null != claim && null != claim.getStage()) {
//								Long stageId = claim.getStage().getKey();
//								String processValue = claim.getStatus().getProcessValue();
//								Map<String,List<BigDecimal>> unionMap = getUnionForReimbAndCashless();
//								List<BigDecimal> cashlesskey = unionMap.get(SHAConstants.CASHLESS_CHAR);
//								List<BigDecimal> reimbursementKey = unionMap.get(SHAConstants.REIMBURSEMENT_CHAR);
//								if(null != unionMap && !unionMap.isEmpty()) {
//									statusMap = submitQueryReport(docUploadToPremia, statusMap,strOutCome, claim,cashlesskey, reimbursementKey);
//								}
//								if(null != statusMap && statusMap.isEmpty()) {
//									statusMap.put("2", "Failure occured while processing query");
//								}
//										
//								if(updateDocUploadPremiaTbl(statusMap,docUploadToPremia))
//									isSuccess = processReportIdForPremiaDocument(docUploadToPremia,statusMap, claim);
//								else
//									isSuccess = "Failure occurred while processing the request.";
//	
//							}
//						}
//	
//					}
					  
					  
						else if(intimation != null && ((isPreauthApprovedStatus(intimation, claim) || isEnhancementApprovedOrDownsize(intimation, claim)
								|| isEnhancementRejectStatus(intimation, claim)) && !getDBTaskForPreauth(intimation, SHAConstants.FLE_CURRENT_QUEUE,claim))
								&& claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
							 curenntQ ="FLEN";
							log.info("*****TRYING TO PUSH INTIMATION TO FIRST LEVEL ENHANCEMENT ******");
		
								strType = "ENHANCE";
								statusMapForPreauth = processPreMedicalPreAuthAndPreMedicalEnhancement(docUploadToPremia.getPfdUpIntmNo(),
										SHAConstants.CLAIMREQUEST_CASHLESS,strType, intimation, claim, docUploadToPremia.getPfdUpFFAXAmt(),docUploadToPremia.getPfdUpFfaxSubmitId(),docUploadToPremia);
								
							log.info("******INTIMATION MOVED TO FIRST LEVEL ENHANCEMENT ********" + intimation.getIntimationId());
								
							if(updateDocUploadPremiaTbl(statusMapForPreauth,docUploadToPremia)) {
								isSuccess = processReportIdForPremiaDocument(docUploadToPremia,statusMapForPreauth, claim,curenntQ);
							}
							else
								isSuccess = "Failure occurred while processing the request.";
						}
					  
//					else if(intimation != null && (isPreauthApprovedStatus(intimation, claim) || isEnhancementApprovedOrDownsize(intimation, claim)
//							|| isEnhancementRejectStatus(intimation, claim) && !isTaskFirstLevelEnhancement(intimation))){
//						
//						log.info("*****TRYING TO PUSH INTIMATION TO FIRST LEVEL ENHANCEMENT ******");
//	
//							strType = "ENHANCE";
//							statusMapForPreauth = processPreMedicalPreAuthAndPreMedicalEnhancement(docUploadToPremia.getPfdUpIntmNo(),
//									SHAConstants.CLAIMREQUEST_CASHLESS,strType, intimation, claim);
//							
//						log.info("******INTIMATION MOVED TO FIRST LEVEL ENHANCEMENT ********" + intimation.getIntimationId());
//							
//						if(updateDocUploadPremiaTbl(statusMapForPreauth,docUploadToPremia)) {
//							isSuccess = processReportIdForPremiaDocument(docUploadToPremia,statusMapForPreauth, claim);
//						}
//						else
//							isSuccess = "Failure occurred while processing the request.";
//					} 
					else if(null != docType && !("").equalsIgnoreCase(docType) && ((SHAConstants.PREMIA_DOC_TYPE_PRE_AUTHORISATION).equalsIgnoreCase(docType)
							|| (SHAConstants.PREMIA_DOC_TYPE_ENHANCEMENT).equalsIgnoreCase(docType))) {
						curenntQ="OTHERS";
						if ((SHAConstants.PREMIA_DOC_TYPE_PRE_AUTHORISATION).equalsIgnoreCase(docType)) {
							strType = "PREAUTHRECEIVED";
						}
						else if ((SHAConstants.PREMIA_DOC_TYPE_ENHANCEMENT).equalsIgnoreCase(docType)) {
							strType = "ENHANCE";
						}
						
						statusMapForPreauth.put("1", "Document mapping only");
						
						statusMapForPreauth = setValueForStatusMap(claim,statusMapForPreauth);
						 
						if(updateDocUploadPremiaTbl(statusMapForPreauth,docUploadToPremia)){
							isSuccess = processReportIdForPremiaDocument(docUploadToPremia,statusMapForPreauth,claim,curenntQ);
						}
						else
							isSuccess = "Failure occurred while processing the request.";
						
						
					} 
					else if (null != docType && !("").equalsIgnoreCase(docType) && ((SHAConstants.PREMIA_DOC_TYPE_CASHLESS_QUERY_REPORT).equalsIgnoreCase(docType) || (SHAConstants.PREMIA_DOC_TYPE_FVR).equalsIgnoreCase(docType)|| (SHAConstants.QUERY_REPLY_WITH_FINAL_BILL).equalsIgnoreCase(docType) )) {
						/**
						 * Implementation is kept on hold, since query for cashless can be raised from pre auth and enhancement.
						 * Hence to be discussed with sathish sir , which needs to be invoked.
						 * */
						Intimation intimationObject = getIntimationObject(docUploadToPremia.getPfdUpIntmNo());
						Map<String, String> statusMap = null;
						curenntQ="OTHERS";
						//String strOutCome = "";
						statusMap = new HashMap<String, String>();
						if(null != intimationObject) {
							Claim claimObj = getClaimObject(intimationObject.getKey());
							if(null != claimObj && null != claimObj.getStage()) {
								//Long stageId = claimObj.getStage().getKey();
								//String processValue = claimObj.getStatus().getProcessValue();
								
								if((SHAConstants.PREMIA_DOC_TYPE_CASHLESS_QUERY_REPORT).equalsIgnoreCase(docType) || (SHAConstants.QUERY_REPLY_WITH_FINAL_BILL).equalsIgnoreCase(docType)) {
									statusMap.put("1", "Document mapping only");
									statusMap = setValueForStatusMap(claim,statusMap);
								} else if((SHAConstants.PREMIA_DOC_TYPE_FVR).equalsIgnoreCase(docType)) {
									//if((ReferenceTable.FVR_STAGE.equals(stageId) && (SHAConstants.FVR_STAGE).contains(processValue)))
									//if((ReferenceTable.PREAUTH_STAGE).equals(stageId) || (ReferenceTable.ENHANCEMENT_STAGE))
										//List<Preauth> preauthList = getPreauthByIntimation(intimationObject.getKey());
										List<FieldVisitRequest> fvrList = getFieldVisitRequest(intimationObject.getKey());
										statusMap = new HashMap<String, String>();
										if(null != fvrList && !fvrList.isEmpty()) {
	
											submitFVR(docUploadToPremia,
													fvrList, statusMap);
											
											statusMap.put("1", "Attached upload document only");
											statusMap = setValueForStatusMap(claim,statusMap);
	
										} else {
											
											statusMap.put("1", "Attached upload document only");
											statusMap = setValueForStatusMap(claim,statusMap);
										}
								}
								
								if(null == claimObj.getClaimRegisteredDate() && claimObj.getDocumentReceivedDate() == null)
								{
									Status claimStatus = new Status();
									claimStatus.setKey(ReferenceTable.CLAIM_REGISTERED_STATUS);
									claimObj.setStatus(claimStatus);
									claimObj.setClaimRegisteredDate((new Timestamp(System
											.currentTimeMillis())));
									claimObj.setDocumentReceivedDate((new Timestamp(System
											.currentTimeMillis())));
									entityManager.merge(claimObj);
									entityManager.flush();
								}
								
								if(updateDocUploadPremiaTbl(statusMap,docUploadToPremia)) {
									isSuccess = processReportIdForPremiaDocument(docUploadToPremia,statusMap,claim,curenntQ);
								} else {
									isSuccess = "Failure occurred while processing the request.";
								}
							} else {
								 updateDocUploadPremiaTbl(statusMap,docUploadToPremia);
							}
						}
								
					} else if(null != docType && !("").equalsIgnoreCase(docType) && ((SHAConstants.PREMIA_DOC_TYPE_OTHERS).equalsIgnoreCase(docType))) {
								log.info("&%%%%%% OTHERS DOC TYPE &&&&&&&&********");
								Map<String, String> statusMap = new HashMap<String, String>();
								
								statusMap.put("1", "Document mapping only");
								curenntQ="OTHERS";
								statusMap = setValueForStatusMap(claim,statusMap);
								
								Intimation intimationObject = getIntimationObject(docUploadToPremia.getPfdUpIntmNo());
								if(null != intimationObject) {
									Claim claimObj = getClaimObject(intimationObject.getKey());
									if(claimObj != null && null == claimObj.getClaimRegisteredDate() && claimObj.getDocumentReceivedDate() == null)
									{
										Status claimStatus = new Status();
										claimStatus.setKey(ReferenceTable.CLAIM_REGISTERED_STATUS);
										claimObj.setStatus(claimStatus);
										claimObj.setClaimRegisteredDate((new Timestamp(System
												.currentTimeMillis())));
										claimObj.setDocumentReceivedDate((new Timestamp(System
												.currentTimeMillis())));
										entityManager.merge(claimObj);
										entityManager.flush();
									}
								}
								
								if(updateDocUploadPremiaTbl(statusMap,docUploadToPremia))
									isSuccess = processReportIdForPremiaDocument(docUploadToPremia,statusMap, claim,curenntQ);
								else
									isSuccess = "Failure occurred while processing the request.";
					} else if(null != docType && !("").equalsIgnoreCase(docType) && ((SHAConstants.OTHERS_DOC_TYPE).equalsIgnoreCase(docType)
							|| (SHAConstants.OTHERS_DOC_TYPE).equalsIgnoreCase(docType))) {
						updateDocUploadPremiaTbl(null,docUploadToPremia);
					}
				}
					/**
					 * If claim is null, update failure status in metadata table.
					 * */
					else {
						updateDocUploadPremiaTbl(null,docUploadToPremia);
					}
					
					utx.commit();
					log.info("********** DOC UPLOAD SUCCESSFULLY COMPLETED **** INTIMATION NO--->" + docUploadToPremia.getPfdUpIntmNo() + "   REPORT ID--->" + docUploadToPremia.getPfdUpReportId());
					} catch(Exception e) {
						e.printStackTrace();
						utx.rollback();
						try{
							utx.begin();
							log.debug("##################################"
									+ " ISSUE WHILE DOC UPLOAD PLEASE LOOK INTO THIS INTIMATION ##################----->" + docUploadToPremia.getPfdUpIntmNo());
							updatePremiaValues("DB OR BPM ISSUE", "Y", docUploadToPremia);
							utx.commit();
						} catch(Exception ex) {
							try {
								utx.rollback();
							} catch (IllegalStateException | SecurityException
									| SystemException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							ex.printStackTrace();
						}
						log.error("***ERROR OCCURED WHILE PROCESSING RECORDS FROM E STARFAX*****"+"INTIMATION NO :" + docUploadToPremia.getPfdUpIntmNo() + "  REPORT ID--->" +docUploadToPremia.getPfdUpPremiaUploadSts());
					}
				log.info("@@@@@@@@@ DOCUMENT UPLOADED ENDING TIME @@@@@@@@@@-----> "+"-----> " +docUploadToPremia.getPfdUpIntmNo()+"-----> "+ System.currentTimeMillis());
				}
			}
		return isSuccess;
	}
	
/*	private Boolean isQueryReplyDocEnhancementForCashless(String intimationNo){
		List<HumanTask> cashlessQueryHumanTask = getCashlessQueryEnhancementHumanTask(intimationNo);
		if(cashlessQueryHumanTask != null && ! cashlessQueryHumanTask.isEmpty()){
			return true;
		}
		return false;
	}
	
	private List<HumanTask> getCashlessQueryEnhancementHumanTask(String intimationNo)
	{
		QueryReplyEnhDocTask queryReplyDocTask = BPMClientContext.getQueryReplyEnhancementDocTask(BPMClientContext.BPMN_TASK_USER, "Star@123");
		PayloadBOType payloadBO = new PayloadBOType();
		IntimationType intimationType = new IntimationType();
		intimationType.setIntimationNumber(intimationNo);
		payloadBO.setIntimation(intimationType);
		com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = queryReplyDocTask.getTasks(BPMClientContext.BPMN_TASK_USER, new Pageable(), payloadBO);
		List<HumanTask> humanTaskList = tasks.getHumanTasks();
		return humanTaskList;
	}*/
	
	public void submitReceivePreauthToDBprocedure(Claim claim, Long estarFaxAmt,Date pfdUpFfaxSubmitDate, DocUploadToPremia docUploadToPremia){

		Map<String, Object> mapValues = new WeakHashMap<String, Object>();
		mapValues.put(SHAConstants.INTIMATION_NO, claim.getIntimation().getIntimationId());
		
//		Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
		
		Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
		
		DBCalculationService dbCalculationService = new DBCalculationService();
//		List<Map<String, Object>> taskProcedure = dbCalculationService.getTaskProcedure(setMapValues);	
		
		log.info("@@@@CALL GET TASK PROCEDURE STARTING TIME  --------> "+System.currentTimeMillis());
		List<Map<String, Object>> taskProcedure = dbCalculationService.revisedGetTaskProcedureForBatch(setMapValues);	
		log.info("@@@@CALL GET TASK PROCEDURE ENDING TIME  --------> "+System.currentTimeMillis());
		 
		List<Object[]> arrayListFromGettask = SHAUtils.getArrayListFromGettask(taskProcedure);
		
//		List<Object[]> arrayListFromGettask = SHAUtils.getRevisedObjArrayForSubmit(taskProcedure);
		String cpuLimitForUser = "";
		if(claim.getIntimation().getCpuCode() != null){
			cpuLimitForUser = dbCalculationService.getCPULimitForUser(claim.getIntimation().getCpuCode().getCpuCode().toString(), estarFaxAmt != null ? estarFaxAmt.toString() : "0");	
		}
		 
		for (int i = 0; i < arrayListFromGettask.size(); i++) {
			Object[] resultArrayObj = arrayListFromGettask.get(i);
			Object[] object1 = (Object[]) resultArrayObj[0];
			object1[SHAConstants.INDEX_OUT_COME] = "WFPAFLPA";
			object1[SHAConstants.INDEX_CLAIM_NUMBER] = claim.getClaimId();
			object1[SHAConstants.INDEX_PROECSSED_DATE] = new DATE();
			object1[SHAConstants.INDEX_STAGE_SOURCE] = SHAConstants.SOURCE_CLAIM_REGISTRATION;
			object1[SHAConstants.INDEX_CLAIMED_AMT] = estarFaxAmt;
			object1[SHAConstants.INDEX_CPU_CODE] = String.valueOf(claim.getIntimation().getCpuCode().getCpuCode());
			/*Below Source & Channel implemented as per satish sir instruction*/ 
			object1[SHAConstants.INDEX_REFERENCE_USER_ID] =SHAConstants.CHANNEL_EMAIL;
			object1[SHAConstants.INDEX_RRC_REQUEST_NUMBER] = SHAConstants.SOURCE_STARFAX;
			if(cpuLimitForUser != null && !cpuLimitForUser.isEmpty()){
				object1[SHAConstants.INDEX_DOCUMENT_RECEVIED_FROM] = cpuLimitForUser;
				
				if(cpuLimitForUser.equalsIgnoreCase(SHAConstants.CPU_LIMIT_EXCEEDED_CPPA) || cpuLimitForUser.equalsIgnoreCase(SHAConstants.CPU_LIMIT_EXCEEDED_CP)){
					object1[SHAConstants.INDEX_ROD_NUMBER] = SHAConstants.YES_FLAG;
				}
			}
			Date clubmemberType = intimationService.getCMDClubMemberTypeForPreauth(claim,pfdUpFfaxSubmitDate);
			//code added by noufel for setting Club member time differnce in workflow table
			if(clubmemberType != null ){
				
				object1[SHAConstants.INDEX_ALLOCATED_DATE] =SHAUtils.parseDate(clubmemberType);
			}
			//code added for updating starfax application ID and match Q date in workflow table
			if(docUploadToPremia.getPfdUpFfaxSubmitId() != null){
			object1[SHAConstants.INDEX_ROD_CREATED_DATE] = docUploadToPremia.getPfdUpFfaxSubmitId();
			}
			if(docUploadToPremia.getPfdUpReportId() != null && !docUploadToPremia.getPfdUpReportId().isEmpty()){
			object1[SHAConstants.INDEX_ACK_NUMBER] = docUploadToPremia.getPfdUpReportId();
			}
			Object[] parameter = new Object[1];
			parameter[0] = object1;
//			String initiateTaskProcedure = dbCalculationService.initiateTaskProcedure(parameter);
			
			//String initiateTaskProcedure = dbCalculationService.revisedInitiateTaskProcedure(parameter);
			log.info("@@@@CALL SUBMIT TASK PROCEDURE STARTING TIME  --------> "+System.currentTimeMillis());
			dbCalculationService.revisedInitiateTaskProcedure(parameter);
			log.info("@@@@CALL SUBMIT TASK PROCEDURE ENDING TIME  --------> "+System.currentTimeMillis());
		}
		
	
		
	}
	
	public void submitDBprocedure(Claim claim,String currentQueue,String outCome,DocUploadToPremia docUploadToPremia){
		
		log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL STARTING TIME @@@@@@@@@@-----> "+"-----> " +claim.getIntimation().getIntimationId()+"-----> "+ System.currentTimeMillis());
		
		Map<String, Object> mapValues = new WeakHashMap<String, Object>();
		mapValues.put(SHAConstants.INTIMATION_NO, claim.getIntimation().getIntimationId());
		mapValues.put(SHAConstants.CURRENT_Q, currentQueue);
		
//		Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
		
		Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		log.info("@@@@CALL GET TASK PROCEDURE STARTING TIME  --------> "+System.currentTimeMillis());
		List<Map<String, Object>> taskProcedure = dbCalculationService.revisedGetTaskProcedureForBatch(setMapValues);	
		log.info("@@@@CALL GET TASK PROCEDURE ENDING TIME  --------> "+System.currentTimeMillis());
		 
		List<Object[]> arrayListFromGettask = SHAUtils.getArrayListFromGettask(taskProcedure);	
		
		 
		for (int i = 0; i < arrayListFromGettask.size(); i++) {
			Object[] resultArrayObj = arrayListFromGettask.get(i);
			Object[] object1 = (Object[]) resultArrayObj[0];
			object1[SHAConstants.INDEX_OUT_COME] = outCome;
			object1[SHAConstants.INDEX_PROECSSED_DATE] = new DATE();
			object1[SHAConstants.INDEX_CPU_CODE] = String.valueOf(claim.getIntimation().getCpuCode().getCpuCode());
			if(currentQueue.equals(SHAConstants.QUERY_REPLY_QUEUE)){
				object1[SHAConstants.INDEX_RECORD_TYPE] = "QUERY REPLY";
				Date clubmemberType = docUploadToPremia.getPfdUpFfaxSubmitId();
				if(outCome != null && (outCome.equalsIgnoreCase(SHAConstants.OUTCOME_FOR_QUERY_REPLY_PE) || 
						outCome.equalsIgnoreCase(SHAConstants.OUTCOME_FOR_QUERY_REPLY_FLE)) ){
				 clubmemberType = intimationService.getCMDClubMemberTypeForEnhancement(claim,docUploadToPremia.getPfdUpFfaxSubmitId());
				}else {
					clubmemberType = intimationService.getCMDClubMemberTypeForPreauth(claim,docUploadToPremia.getPfdUpFfaxSubmitId());
				}
				//code added by noufel for setting Club member time differnce in workflow table
				if(clubmemberType != null ){
					object1[SHAConstants.INDEX_ALLOCATED_DATE] =SHAUtils.parseDate(clubmemberType);
				}
			}
			if(docUploadToPremia != null && docUploadToPremia.getPfdUpDocType() != null && docUploadToPremia.getPfdUpDocType().equalsIgnoreCase(SHAConstants.QUERY_REPLY_WITH_FINAL_BILL)){
				object1[SHAConstants.INDEX_RECORD_TYPE] = SHAConstants.QUERY_REPLY_WITH_FINAL_BILL;
			}
			//code added for updating starfax application ID and match Q date in workflow table
			if(docUploadToPremia.getPfdUpFfaxSubmitId() != null){
				object1[SHAConstants.INDEX_ROD_CREATED_DATE] = docUploadToPremia.getPfdUpFfaxSubmitId();
			}
			if(docUploadToPremia.getPfdUpReportId() != null && !docUploadToPremia.getPfdUpReportId().isEmpty()){
				object1[SHAConstants.INDEX_ACK_NUMBER] = docUploadToPremia.getPfdUpReportId();
			}
			
			Object[] parameter = new Object[1];
			parameter[0] = object1;
//			String initiateTaskProcedure = dbCalculationService.initiateTaskProcedure(parameter);
			
			//String initiateTaskProcedure = dbCalculationService.revisedInitiateTaskProcedure(parameter);
			log.info("@@@@CALL SUBMIT TASK PROCEDURE STARTING TIME  --------> "+System.currentTimeMillis());
			dbCalculationService.revisedInitiateTaskProcedure(parameter);
			log.info("@@@@CALL SUBMIT TASK PROCEDURE ENDING TIME  --------> "+System.currentTimeMillis());
		}
		
		log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL ENDING TIME "+"-----> " +claim.getIntimation().getIntimationId()+"-----> "+ System.currentTimeMillis());
		
	}
	
	public Boolean getDBTaskForPreauth(Intimation intimation,String currentQ, Claim claim){
		
		log.info("@@@@@@@@@ GET TASK PROCEDURE CALL STARTING TIME @@@@@@@@@@-----> "+"-----> " +intimation.getIntimationId()+"-----> "+ System.currentTimeMillis());
		
		Map<String, Object> mapValues = new WeakHashMap<String, Object>();
		mapValues.put(SHAConstants.INTIMATION_NO, intimation.getIntimationId());
		mapValues.put(SHAConstants.CURRENT_Q, currentQ);
		
		//IMSSUPPOR-35739
		/*if(claim!=null){
			mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, claim.getCrcFlag());
		}*/

//		Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
		
		Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);

		DBCalculationService db = new DBCalculationService();
//		 List<Map<String, Object>> taskProcedure = db.getTaskProcedure(setMapValues);
		log.info("@@@@CALL GET TASK PROCEDURE STARTING TIME  --------> "+System.currentTimeMillis());
		 List<Map<String, Object>> taskProcedure = db.revisedGetTaskProcedureForBatch(setMapValues);
		 log.info("@@@@CALL GET TASK PROCEDURE ENDING TIME  --------> "+System.currentTimeMillis());
		 
		 log.info("@@@@@@@@@ GET TASK PROCEDURE CALL ENDING TIME @@@@@@@@@@-----> "+"-----> " +intimation.getIntimationId()+"-----> "+ System.currentTimeMillis());
		if (taskProcedure != null && !taskProcedure.isEmpty()){
			return true;
		} 
		
		return false;
	}
	
	public Preauth getLatestPreauthByClaimForWithdrawReject(Long claimKey) {
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
			if(ReferenceTable.getWithdrawnKeysExceptWithdrawReject().containsKey(preauth.getStatus().getKey())) {
				return null;
			}
			
			return preauth;
		}
		
		return null;
	
	}
	
	public List<DocUploadToPremia> fetchRecFromPremiaDocUploadTblWithoutFVRDescending(String batchSize)
	{ 
		List<DocUploadToPremia> docUploadPremiaList = new ArrayList<DocUploadToPremia>();
		Query query = entityManager
				.createNamedQuery("DocUploadToPremia.findByPendingCount");
		
		Long pendingCount = (Long) query.getSingleResult();
		if(pendingCount >= 30l){
			Query query1 = entityManager
					.createNamedQuery("DocUploadToPremia.findByDocUploadWithoutFVRDesc");
			
			if(batchSize != null) {
				query1.setMaxResults(SHAUtils.getIntegerFromString(batchSize));
			}
			docUploadPremiaList = query1.getResultList();
		}
		log.info("********* COUNT FOR DOC UPLAOD DESC******------> " + (docUploadPremiaList != null && !docUploadPremiaList.isEmpty() ? String.valueOf(docUploadPremiaList.size()) : "NO RECORDS TO DOC UPLOAD DESC--->") + "@ Pending count desc" + pendingCount);
		return docUploadPremiaList;
	}
	
	public MasCpuLimit getMasCpuLimit(Long cpuId,String polType){
		try{
		Query findCpuCode = entityManager.createNamedQuery("MasCpuLimit.findByCode").setParameter("cpuCode", cpuId);
		findCpuCode.setParameter("polType", polType);
		//findCpuCode.setParameter("activeStatus", 1l);
		List<MasCpuLimit> cpuLimit = (List<MasCpuLimit>)findCpuCode.getResultList();
		
		if(cpuLimit != null && ! cpuLimit.isEmpty()){
			return cpuLimit.get(0);
		}
		
		}catch(Exception e){
				
		}
		return null;
	}
	
	public Long getProcessingCpuCode(Long estarFaxAmt,Long cpuCode, String polType){
		
		MasCpuLimit masCpuLimit = getMasCpuLimit(cpuCode,polType);
		if(masCpuLimit != null){
				if(polType.equalsIgnoreCase(SHAConstants.PROCESSING_CPU_CODE_GMC)){
					if(estarFaxAmt <= masCpuLimit.getCpuLimit()){
						return masCpuLimit.getProcessCpuCode();
					}else{
						return null;
					}
				} else if(polType.equalsIgnoreCase(SHAConstants.PROCESSING_CPU_CODE_RETAIL)){
						if(estarFaxAmt > masCpuLimit.getCpuLimit()){
							return masCpuLimit.getProcessCpuCode();
						}else{
							return null;
						}
				}
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
	
	public  TmpCPUCode getCpuDetails(Long cpuId) {
		TmpCPUCode ack = null;
		Query findByReimbursementKey = entityManager.createNamedQuery("TmpCPUCode.findByKey").setParameter("cpuId", cpuId);
		try {
			List resultList = findByReimbursementKey.getResultList();
			if(resultList != null && !resultList.isEmpty()) {
				ack = (TmpCPUCode) resultList.get(0);
			}
			return ack;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
public void saveStarFaxReverseFeed(Intimation intimation, Long fromCpuCode, Long toCpuCode, Double claimedAmt,String userName){
		
	    String maximumReportId = getMaximumReportId(intimation.getIntimationId());
//	    String starFaxCpuCode = getStarFaxCpuCode(maximumReportId);
	    if(fromCpuCode != null && ! fromCpuCode.equals(toCpuCode.toString())){
		StarFaxReverseFeed reverseFeed = new StarFaxReverseFeed();
		reverseFeed.setApplicationId(maximumReportId);
		reverseFeed.setClaimNumber(intimation.getIntimationId());
		reverseFeed.setFromCpu(String.valueOf(fromCpuCode));
		reverseFeed.setToCpu(String.valueOf(toCpuCode));
		reverseFeed.setAmount(String.valueOf(claimedAmt));
		reverseFeed.setUserId(userName);
		reverseFeed.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		// AS Per Nofel confirmation added below value in insert and update records
    	reverseFeed.setIsGlxProcessed("YES");
	    if(reverseFeed.getKey() != null){
	    	entityManager.merge(reverseFeed);
	    	entityManager.flush();
	    }else{
	    	entityManager.persist(reverseFeed);
	    	entityManager.flush();
	     }
	    }
		
	}

public String getMaximumReportId(String intimationNumber){
	
    String query = "SELECT MAX(A.PFDUP_REPORT_ID) FROM IMS_SFX_DOC_METADATA A WHERE PFDUP_INTM_NO='"+intimationNumber+"' AND PFDUP_DOC_TYPE<>'FVR'";
	Query nativeQuery = entityManager.createNativeQuery(query);
	String applicationId = (String)nativeQuery.getSingleResult();
	return applicationId;
}

public String getStarFaxCpuCode(String ReportId)
{
//	Query query = entityManager.createNamedQuery("DocUploadToPremia.findByIntimationAndDocType");
	Query query = entityManager.createNamedQuery("DocUploadToPremia.findByReportId");
	query = query.setParameter("reportId", ReportId); 
	List<DocUploadToPremia> listOfDocUploadData = (List<DocUploadToPremia>)query.getResultList();
	if(null != listOfDocUploadData && !listOfDocUploadData.isEmpty())
	{
		return listOfDocUploadData.get(0).getPfdUpCpuEsc();
	}
	return null;
}

public CashlessWorkFlow getWorkFlowByIntimation(String intimationNo){
	
	log.info("********************************* RECONSIDERATION GET TASK QUERY STARTED ********************************");
	
	List<CashlessWorkFlow> workFlow;
	
	List<String> currentList = new ArrayList<String>();
	currentList.add(SHAConstants.PP_CURRENT_QUEUE);
	
	Query findByIntimation = entityManager.createNamedQuery(
			"CashlessWorkFlow.findIntimationReallocate").setParameter("intimationNo",
					intimationNo).setParameter("current", currentList);
	try {
		workFlow = (List<CashlessWorkFlow>) findByIntimation.getResultList();
		
		log.info("********************************* RECONSIDERATION GET TASK QUERY ENDED SUCCESSFULLY ********************************");
		
		if(workFlow != null && ! workFlow.isEmpty()){
			log.info("********************************* INTIMATION IS PUSHED TO PREAUTH Q FOR RECONSIDERATION ********************************");
			
			return workFlow.get(0);
		}
		return null;
	} catch (Exception e) {
		return null;
	}
	
}

public Boolean getPaayasPolicyDetails(String policyNumber){
	
	 Query query = entityManager.createNamedQuery("PaayasPolicy.findByPolicyNumber");
	 query = query.setParameter("policyNumber", policyNumber);
	 List<PaayasPolicy> resultList = (List<PaayasPolicy>)query.getResultList();		 
	 if(resultList != null && !resultList.isEmpty()) {
		 return true;
	 } 
			
	 return false;
}

public Boolean getJioPolicyDetails(String policyNumber){
	
	 Query query = entityManager.createNamedQuery("StarJioPolicy.findByPolicyNumber");
	 query = query.setParameter("policyNumber", policyNumber);
	 List<StarJioPolicy> resultList = (List<StarJioPolicy>)query.getResultList();		 
	 if(resultList != null && !resultList.isEmpty()) {
		 return true;
	 } 
			
	 return false;
}

public String getGpaPolicyDetails(String policyNumber){
	
	 Query query = entityManager.createNamedQuery("GpaPolicy.findByPolicyNumber");
	 query = query.setParameter("policyNumber", policyNumber);
	 List<GpaPolicy> resultList = (List<GpaPolicy>)query.getResultList();		 
	 if(resultList != null && !resultList.isEmpty()) {
		 return resultList.get(0).getCpuCode();
	 } 
			
	 return null;
}

public Boolean isAlreadyPreauthApprovedForReopen(Long cashlessKey){

	
	String query = "SELECT * FROM  IMS_CLS_STAGE_INFORMATION  WHERE CASHLESS_KEY =" + cashlessKey
			+ " AND STATUS_ID IN (22,23,26,30,31,38,40,63,64,182,183)";
	
	Query nativeQuery = entityManager.createNativeQuery(query);
	List<StageInformation> resultList = (List<StageInformation>)nativeQuery.getResultList();
	
	if(resultList != null && ! resultList.isEmpty()){
		return true;
	}
	return false;
	 

}

public Boolean isAlreadyDeniedForReopen(Long cashlessKey){

	
	String query = "SELECT * FROM  IMS_CLS_STAGE_INFORMATION  WHERE CASHLESS_KEY =" + cashlessKey
			+ " AND STATUS_ID IN (23,26,30,31,38,40,63,64,182,183) AND STATUS_ID <> 22";
	
	Query nativeQuery = entityManager.createNativeQuery(query);
	List<StageInformation> resultList = (List<StageInformation>)nativeQuery.getResultList();
	
	if(resultList != null && ! resultList.isEmpty()){
		return true;
	}
	return false;
	 

}

public void updateProvisionAmountDetails(Claim objClaim,Integer provisionAmt){
	try{
		if(provisionAmt != null){
			StarfaxProvisionHistory provisionHistory = new StarfaxProvisionHistory();
			provisionHistory.setIntimationKey(objClaim.getIntimation().getKey());
			provisionHistory.setIntimationNumber(objClaim.getIntimation().getIntimationId());
			provisionHistory.setClaim(objClaim);
			provisionHistory.setClaimNumber(objClaim.getClaimId());
			provisionHistory.setCurrentProvisonAmt(provisionAmt.doubleValue());
			provisionHistory.setModifiedDate(new Timestamp(System.currentTimeMillis()));
		    entityManager.persist(provisionHistory);
		    entityManager.flush();
		}
	}catch(Exception e){
		e.printStackTrace();
	}
}

	public List<StarfaxProvisionHistory> fetchProvisonUpdateDetailsForStarfax(String batchSize)
	{ 
		List<StarfaxProvisionHistory> updateProvisionList = new ArrayList<StarfaxProvisionHistory>();
		Query query1 = entityManager
					.createNamedQuery("StarfaxProvisionHistory.findAll");
			
		if(batchSize != null) {
				query1.setMaxResults(SHAUtils.getIntegerFromString(batchSize));
		}
		updateProvisionList = query1.getResultList();
		return updateProvisionList;
	}
	
	public void updateProvisionToPremiaForStarfax(StarfaxProvisionHistory starfaxProvision){
		
		
		Claim claim = starfaxProvision.getClaim();
		Hospitals hospitalDetailsByKey = getHospitalDetailsByKey(claim.getIntimation().getHospital());
		String provisionAmtInput = SHAUtils.getProvisionAmtInput(claim, hospitalDetailsByKey.getName(), String.valueOf(starfaxProvision.getCurrentProvisonAmt()));
		Integer iProvisionAmt = SHAUtils.getIntegerFromString(provisionAmtInput);
		/*Below condition added as per satish sir & aswin instruction*/
		DBCalculationService dbService = new DBCalculationService();
		Policy policyObj = dbService.getPolicyObject(claim.getIntimation().getPolicy().getPolicyNumber());
		if(policyObj != null && ((policyObj.getPolicySource() != null 
				&& !policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY))
				|| policyObj.getPolicySource() == null)){
			if(null != iProvisionAmt && (iProvisionAmt).equals(0)) {
				APIService apiService = new APIService();
				String updateProvisionAmountToPremia = apiService.updateProvisionAmountToPremia(String.valueOf(provisionAmtInput));
				if(updateProvisionAmountToPremia != null && (! updateProvisionAmountToPremia.isEmpty())){
					starfaxProvision.setProvisionUpdateFlag("Y");
					starfaxProvision.setProvisionUpdateDate(new Timestamp(System.currentTimeMillis()));
					starfaxProvision.setRemarks(updateProvisionAmountToPremia);
				}else{
					starfaxProvision.setProvisionUpdateFlag("E");
					starfaxProvision.setProvisionUpdateDate(new Timestamp(System.currentTimeMillis()));
					starfaxProvision.setRemarks("ERROR");
				}
				
				try {
					utx.begin();
				} catch (NotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				entityManager.merge(starfaxProvision);
				entityManager.flush();
				
				
				try {
					utx.commit();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RollbackException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (HeuristicMixedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (HeuristicRollbackException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
	}
	
	public Long getTataPolicy(String policyNumber){
		
		 Query query = entityManager.createNamedQuery("TataPolicy.findByPolicyNumber");
		 query.setParameter("policyNumber", policyNumber);
		    List<TataPolicy> tataPolicy = (List<TataPolicy>)query.getResultList();
		    if(tataPolicy != null && ! tataPolicy.isEmpty()){
		    	return tataPolicy.get(0).getCpuCode();
		    }
		    return null;
	}

	public Boolean getKotakPolicyDetails(String policyNumber){
		
		 Query query = entityManager.createNamedQuery("StarKotakPolicy.findByPolicyNumber");
		 query = query.setParameter("policyNumber", policyNumber);
		 List<StarKotakPolicy> resultList = (List<StarKotakPolicy>)query.getResultList();		 
		 if(resultList != null && !resultList.isEmpty()) {
			 return true;
		 } 
				
		 return false;
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
	 
	 public void saveClaimCpuCode(Claim claim,Long fromCpuCode, Long toCpuCode){
			
		    claim.setSfxCpuCode(fromCpuCode);
		    claim.setSfxProcessingCpuCode(toCpuCode);
		    entityManager.merge(claim);
			entityManager.flush();
			
		}
	 
	 public Long getStarFaxCpu(Long pfdUpCpuEsc, Long pfdUpFFAXAmt,String curenntQ,Long productKey) 
		{
			Query cs = entityManager.createNativeQuery(
				"select FUN_SFX_CPU_ROUTING (?1,?2,?3,?4) from dual");
		cs.setParameter(1, pfdUpCpuEsc);
		cs.setParameter(2, pfdUpFFAXAmt);
		cs.setParameter(3, curenntQ);
		cs.setParameter(4, productKey);
		String result =cs.getSingleResult().toString();
		Long result1 = Long.parseLong(result);
		return result1;
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
		
	 public Long getKotakProcessingCpuCode(Long estarFaxAmt,Long cpuCode, String polType){
		 Long kotakCPUCode =cpuCode;
		 MasCpuLimit masCpuLimit = getMasCpuLimit(cpuCode,polType);
		 if(masCpuLimit != null){
			 if(estarFaxAmt >= masCpuLimit.getCpuLimit()){
				 kotakCPUCode = masCpuLimit.getProcessCpuCode();
			 }else {
				 kotakCPUCode = masCpuLimit.getCpuCode();
			 }
		 }
		 return kotakCPUCode;
	 }
	 
	 public MastersValue getMaster(Long masterKey) {
			MastersValue a_mastersValue = new MastersValue();
			if (masterKey != null) {
				Query query = entityManager.createNamedQuery("MastersValue.findByKey");
				query = query.setParameter("parentKey", masterKey);
				List<MastersValue> mastersValueList = query.getResultList();
				if(mastersValueList != null && !mastersValueList.isEmpty()) {
					a_mastersValue = mastersValueList.get(0);
					return a_mastersValue;
				}
			}
			return null;
		}
}