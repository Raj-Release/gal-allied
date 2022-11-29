package com.shaic.starfax.simulation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import oracle.sql.DATE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.domain.Claim;
import com.shaic.domain.DMSDocMetaData;
import com.shaic.domain.DocUploadToPremia;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Stage;
import com.shaic.ims.bpm.claim.DBCalculationService;



/**
 * @author Saravana kumar P
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class StarFaxFVRServiceRevised {


	private final Logger log = LoggerFactory.getLogger(StarFaxFVRServiceRevised.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@Resource
	private UserTransaction utx;
	
	
	public List<DocUploadToPremia> fetchRecFromPremiaDocUploadTblWithFVR(String batchSize) { 
		Query query = entityManager
				.createNamedQuery("DocUploadToPremia.findByDocUploadWithFVR");
		if(batchSize != null) {
			query.setMaxResults(SHAUtils.getIntegerFromString(batchSize));
		}
		List<DocUploadToPremia> docUploadPremiaList = query.getResultList();
		log.info("********* COUNT FOR DOC UPLAOD ******------> " + (docUploadPremiaList != null ? String.valueOf(docUploadPremiaList.size()) : "NO RECORDS TO DOC UPLOAD"));
		return docUploadPremiaList;
	}
	
	public List<DocUploadToPremia> fetchRecFromPremiaDocUploadTblWithFVRError(String batchSize) { 
		Query query = entityManager
				.createNamedQuery("DocUploadToPremia.findByDocUploadWithFVRERROR");
		if(batchSize != null) {
			query.setMaxResults(SHAUtils.getIntegerFromString(batchSize));
		}
		List<DocUploadToPremia> docUploadPremiaList = query.getResultList();
		log.info("********* COUNT FOR FVR FOR DB OR BPMN ISSUE ******------> " + (docUploadPremiaList != null ? String.valueOf(docUploadPremiaList.size()) : "NO RECORDS TO DOC UPLOAD"));
		return docUploadPremiaList;
	}
	
	public List<DocUploadToPremia> fetchRecFromPremiaDocUploadTblWithNoClaim(String batchSize) { 
		Query query = entityManager
				.createNamedQuery("DocUploadToPremia.findByDocUploadWithNoClaim");
		if(batchSize != null) {
			query.setMaxResults(SHAUtils.getIntegerFromString(batchSize));
		}
		List<DocUploadToPremia> docUploadPremiaList = query.getResultList();
		log.info("********* COUNT FOR DOC UPLAOD FOR NO CLAIM******------> " + (docUploadPremiaList != null ? String.valueOf(docUploadPremiaList.size()) : "NO RECORDS TO DOC UPLOAD"));
		return docUploadPremiaList;
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
	
	public List<Map<String, Object>> getReimbursementFVRTask(String intimationNo) {

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
	
//	private Boolean isFvrReplyTask(Intimation intimation){
//		try {
//			List<HumanTask> humanTaskList = getReimbursementFVRTask(intimation.getIntimationId());
//			if(humanTaskList != null && ! humanTaskList.isEmpty()){
//				return true;
//			}
//			
//		}catch(Exception e){
//			log.error("*************ERROR OCCURED IN FVR TASK *********----->");
//		}
//		
//		return false;
//	}
	
	private List<FieldVisitRequest> getFieldVisitRequest(Long intimationKey) {
		try {
			Query query = entityManager
					.createNamedQuery("FieldVisitRequest.findByIntimationKey");
			query = query.setParameter("intimationKey", intimationKey);
			List<FieldVisitRequest> fvrList  = query.getResultList();
			                                       
			List<FieldVisitRequest> resultList = new ArrayList<FieldVisitRequest>();
			
			for (FieldVisitRequest fieldVisitRequest : fvrList) {
				if(fieldVisitRequest.getStatus().getKey().equals(ReferenceTable.ASSIGNFVR)){
					resultList.add(fieldVisitRequest);
				}
			}
			return resultList;
		}
		catch(Exception e) {
			log.error("********** ERROR OCCURED IN GET FIELD VISIT METHOD********----INTIMATION KEY----->"+ intimationKey);
			return null;
		}
	}
	
	/*private Map<String, String> submitReimbursementFVRTask(List<Map<String, Object>> reimbursementFVRTask,String strIntimationNo)
	{
		Map<String , String> statusMap = new HashMap<String, String>();
		if(null != reimbursementFVRTask && !reimbursementFVRTask.isEmpty())
		{
			DBCalculationService dbCalService = new DBCalculationService();
			for (Map<String, Object> wrkFlowMap : reimbursementFVRTask) {
				wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.FVR_ASSIGN_OUTCOME);
				Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
				//dbCalService.initiateTaskProcedure(objArrayForSubmit);
				dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
			}
			statusMap.put("5", "Intimation '"+strIntimationNo+"' is successfully submitted to BPMN for further processing");
			return statusMap;	
		}
		return null;
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
	
	public Status getStatusByKey(Long key) {

		try {
			Query findType = entityManager.createNamedQuery("Status.findByKey")
					.setParameter("statusKey", key);
			List<Status> status = findType.getResultList();
			if(null != status && !status.isEmpty())
			{
				entityManager.refresh(status.get(0));
				return status.get(0);
			}
			return null ;
		} catch (Exception e) {
			return null;
		}

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
	
	private void submitFVR(DocUploadToPremia docUploadToPremia,
			List<FieldVisitRequest> fvrList, Map<String, String> statusMap)  throws Exception{
		for (FieldVisitRequest fieldVisitRequest : fvrList) {
			
			if(! fieldVisitRequest.getStatus().getKey().equals(ReferenceTable.SKIPFVR)){
			
			Stage reqStage = fieldVisitRequest.getStage();
			if( null != reqStage) {
				Long key = reqStage.getKey();
				/**
				 * Only for reimbursement, FVR needs to be submitted back to the
				 * respective inbox and db update should happen. In case of
				 * cashelss, only db update will happen. This is becasue for cashless
				 * FVR is a parallel process and tasks ends once FVR process is done. 
				 **/
				if((ReferenceTable.CLAIM_REQUEST_STAGE).equals(key) || (ReferenceTable.FINANCIAL_STAGE).equals(key)
						|| ReferenceTable.ZONAL_REVIEW_STAGE.equals(key) || ReferenceTable.CLAIM_APPROVAL_STAGE.equals(key)) {
//					List<HumanTask> listOfHumanTask = getReimbursementFVRTask(docUploadToPremia.getPfdUpIntmNo());
					
//					statusMap = submitReimbursementFVRTask(listOfHumanTask, docUploadToPremia.getPfdUpIntmNo());
					Map<String, Object> mapValues = new WeakHashMap<String, Object>();
					mapValues.put(SHAConstants.INTIMATION_NO, fieldVisitRequest.getClaim().getIntimation().getIntimationId());
					mapValues.put(SHAConstants.CURRENT_Q,  SHAConstants.FVR_REPLY_CURRENT_QUEUE);
					
//					Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
					
					Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
					
					DBCalculationService dbCalculationService = new DBCalculationService();
					List<Map<String, Object>> revisedGetTaskProcedure = dbCalculationService.revisedGetTaskProcedureForBatch(setMapValues);	
					for (Map<String, Object> outPutArray : revisedGetTaskProcedure) {	
						String reimReqQ = (String) outPutArray.get(SHAConstants.PAYLOAD_REIMB_REQ_BY);
						String outCome = SHAConstants.OUTCOME_FOR_FVR_REPLY_END;
						String lob = (String) outPutArray.get(SHAConstants.LOB);
						
						if(reimReqQ != null && reimReqQ.equalsIgnoreCase(SHAConstants.MA_CURRENT_QUEUE)){
						outCome = SHAConstants.OUTCOME_FOR_FVR_REPLY_MA_END;
							//Commented for pA parallel CR changes by pavithran
//							if(null != lob && SHAConstants.PA_LOB.equalsIgnoreCase(lob)){
//								outCome = SHAConstants.OUTCOME_FOR_FVR_REPLY_MA;	
//							}
						}else if(reimReqQ != null && reimReqQ.equalsIgnoreCase(SHAConstants.ZMR_CURRENT_QUEUE)){
							//outCome = SHAConstants.OUTCOME_FOR_FVR_REPLY_ZONAL;
							outCome = SHAConstants.OUTCOME_FOR_FVR_REPLY_MA_END;	
						}else if(reimReqQ != null && reimReqQ.equalsIgnoreCase(SHAConstants.FA_CURRENT_QUEUE)){
							outCome = SHAConstants.OUTCOME_FOR_FVR_REPLY_FA;
						}
						else if(reimReqQ != null && reimReqQ.equalsIgnoreCase(SHAConstants.CURRENT_QUEUE_CLAIM_APPROVAL)){
							outCome = SHAConstants.OUTCOME_FOR_FVR_REPLY_CLAIM_APPROVAL;
						}
//						
						outPutArray.put(SHAConstants.OUTCOME, outCome);
						outPutArray.put(SHAConstants.STAGE_SOURCE, SHAConstants.SOURCE_FVR_REPLY);
						outPutArray.put(SHAConstants.PROCESSED_DATE, new DATE());
						
						Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(outPutArray);
						
						DBCalculationService dbCalService = new DBCalculationService();
						dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
						
						Long wkFlowKey = (Long) outPutArray.get(SHAConstants.WK_KEY);
						String intimationNo = (String) outPutArray.get(SHAConstants.INTIMATION_NO);
						
						dbCalService.workFlowEndCallProcedure(wkFlowKey, intimationNo);
						
						//submitDBprocedure(fieldVisitRequest.getClaim(), SHAConstants.FVR_REPLY_CURRENT_QUEUE, outCome);
					}
					
					
					
					
					statusMap.put("5", "Intimation '"+docUploadToPremia.getPfdUpIntmNo()+"' is successfully submitted to BPMN for further processing");
					
					/*if(null == statusMap) {
						statusMap = new HashMap<String, String>();
					}*/
					
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
						
						String outCome = SHAConstants.OUTCOME_FOR_FVR_REPLY_END;
//						
						submitDBprocedure(fieldVisitRequest.getClaim(), SHAConstants.FVR_REPLY_CURRENT_QUEUE, outCome);
						
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
//			Stage fvrStage = new Stage();
//			fvrStage.setKey(ReferenceTable.FVR_STAGE_KEY);
			Status fvrStatus = new Status();
			fvrStatus.setKey(ReferenceTable.FVR_REPLY_RECEIVED);
//			fieldVisitRequest.setStage(fvrStage);
			fieldVisitRequest.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			fieldVisitRequest.setStatus(fvrStatus);
			if(null != fieldVisitRequest.getKey()) {
				entityManager.merge(fieldVisitRequest);
				entityManager.flush();
				entityManager.clear();
				
				if(null != fieldVisitRequest.getTransactionFlag() && ("R").equalsIgnoreCase(fieldVisitRequest.getTransactionFlag())){
					Reimbursement reimbursement = getReimbursementObject(fieldVisitRequest.getTransactionKey());
					if(reimbursement != null && !(reimbursement.getClaim().getStatus().getKey().equals(ReferenceTable.CLAIM_CLOSED_STATUS) ||
							reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS)
							|| reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS) ||
							reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS)
                            || reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_CANCEL_ROD))){
					reimbursement.setStatus(fvrStatus);
					reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.merge(reimbursement);
					entityManager.flush();
					entityManager.clear();
					}
				}
				
				updateFvrResponseTable(fieldVisitRequest);
				updateFvrRequestTable(fieldVisitRequest);
				
			if(statusMap == null){
					statusMap = new HashMap<String, String>();
				}
				statusMap.put("1", "FVR Processed successfully");
			 } 
			   
			 break;
			
			}else{
				
				if(statusMap == null){
					statusMap = new HashMap<String, String>();
				}
				statusMap.put("1", "FVR Processed successfully");
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private Boolean updatePremiaValues(String statusFlag , String flexFlag,DocUploadToPremia docUploadToPremia) {
		Boolean successFlag = false;
		try {
			docUploadToPremia.setPfdUpPremiaAckDt((new Timestamp(System.currentTimeMillis())));
			docUploadToPremia.setPfdUpPremiaUploadSts(statusFlag);
			docUploadToPremia.setPfdUpFlexYN01(flexFlag);				
			docUploadToPremia.setGalaxyReadFlag(SHAConstants.YES_FLAG);
			entityManager.merge(docUploadToPremia);
			entityManager.flush();
			entityManager.clear();
			log.info("------DocUploadToPremia------>"+ docUploadToPremia.getPfdUpIntmNo() +"<------------");
			successFlag = true;
		} catch(Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			successFlag = false;
		}
		return successFlag;
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
	
	private List<DMSDocMetaData> getDocMetaDataByApplicationId(String applicationId) {
		if(null != applicationId) {
			Query query = entityManager.createNamedQuery("DMSDocMetaData.findByApplicationId");
			query.setParameter("applicationId", Long.valueOf(applicationId));
			List<DMSDocMetaData> docMetaDataList = query.getResultList();
			if(null != docMetaDataList && !docMetaDataList.isEmpty()) {
				for (DMSDocMetaData dmsDocMetaData : docMetaDataList) {
					//entityManager.refresh(dmsDocMetaData);
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
				entityManager.clear();
			}
			return token;
		}
		return null;
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private String processReportIdForPremiaDocument(DocUploadToPremia docUploadToPremia,Map<String,String> statusMap, Claim claim) {
		String isSuccess = "";
		try {
		List<DMSDocMetaData> dmsDocMetaDataList = getDocMetaDataByApplicationId(docUploadToPremia.getPfdUpReportId());
		if(null != dmsDocMetaDataList && !dmsDocMetaDataList.isEmpty())
		{
				for (DMSDocMetaData dmsDocMetaData : dmsDocMetaDataList) {
					DocumentDetails docDetails = populateDocumentDetailsObject(
							dmsDocMetaData, claim);
					if (null != docDetails.getDocumentUrl()) {
						String strActualFileName = "";
						// if(null != docDetails.getSfFileName())
						if (null != docDetails.getFileName()) {
							// strActualFileName =
							// docDetails.getSfFileName().trim();
							strActualFileName = docDetails.getFileName().trim();
						}
						/*String strDocumentToken = getDocumentToken(docDetails
								.getDocumentUrl().trim(), strActualFileName,
								docDetails.getSfFileSize());*/
						if(null != dmsDocMetaData.getFileKey())
							docDetails.setDocumentToken(dmsDocMetaData.getFileKey());
					}
					/***
					 * Need to integrate the DMS service for fetching document
					 * token. Based on doc url, the document token will be
					 * retreived.
					 */
					docDetails.setDocSubmittedDate(docUploadToPremia
							.getPfdUpFfaxSubmitId());
					docDetails.setDocAcknowledgementDate(docUploadToPremia
							.getPfdUpPremiaAckDt());

					if (null != statusMap && !statusMap.isEmpty()) {
						/*
						 * if(statusMap.get(SHAConstants.INTIMATION_NUMBER) !=
						 * null){
						 * docDetails.setIntimationNumber((String)statusMap
						 * .get(SHAConstants.INTIMATION_NUMBER)); }
						 */
						/*
						 * if(statusMap.get(SHAConstants.CLAIM_NUMBER) != null){
						 * docDetails
						 * .setClaimNumber((String)statusMap.get(SHAConstants
						 * .CLAIM_NUMBER)); }
						 */
						if (statusMap.get(SHAConstants.CASHLESS_NUMBER) != null) {
							docDetails.setCashlessNumber((String) statusMap
									.get(SHAConstants.CASHLESS_NUMBER));
						}
						if (statusMap.get(SHAConstants.REIMBURSEMENT_NUMBER) != null) {
							docDetails
									.setReimbursementNumber((String) statusMap
											.get(SHAConstants.REIMBURSEMENT_NUMBER));
						}

					}
					entityManager.persist(docDetails);
					entityManager.flush();
					entityManager.clear();
					log.info("------DocumentDetails------>" + docDetails
							+ "<------------");
					// isSuccess = true;
					isSuccess = "Premia intimation request successfully processed";
				}
		}
		else{
			isSuccess ="Premia intimation request successfully processed. For this request, no documents has been uploaded at premia end";
		}

		return isSuccess;	
		}
		catch(Exception e)
		{
			return null;
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
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void processProcessAuthorizationPremiaReqWITHOUTFVRRevisedForStub(String intimationNo){
		try {
			
			List<DocUploadToPremia> docUploadPremiaList = getUploadedDataDocumentDetails(intimationNo);
			log.info("&&&&&&****** LIST TO BE PROCESSED fOR WITHOUT FVR ******&&&&&&&------>" + (docUploadPremiaList != null ? String.valueOf(docUploadPremiaList.size()) : "0"));
			startProcessForFVRRevised(docUploadPremiaList);
		} catch(Exception e) {
			e.printStackTrace();
			
		}
	}
	
	public void startProcessForFVRRevised(
			List<DocUploadToPremia> docUploadPremiaList) {
		if (null != docUploadPremiaList && !docUploadPremiaList.isEmpty()) {
			for (DocUploadToPremia docUploadToPremia : docUploadPremiaList) {
				try {
					utx.begin();
					Intimation intimation = getIntimationObject(docUploadToPremia
							.getPfdUpIntmNo());
					Claim claim = null;
					if(intimation != null ) {
						claim = getClaimObject(intimation.getKey());
					} else {
						log.debug("############# INTIMATION NOT PULLED YETTT ####################");
					}
					if(claim != null) {
						String docType = docUploadToPremia.getPfdUpDocType();
						if (intimation != null && getDBTaskForPreauth(intimation,SHAConstants.FVR_REPLY_CURRENT_QUEUE)) {
							Map<String, String> statusMap = new HashMap<String, String>();

							List<FieldVisitRequest> fvrList = getFieldVisitRequest(intimation
									.getKey());
							statusMap = new HashMap<String, String>();
							if (null != fvrList && !fvrList.isEmpty()) {
								submitFVR(docUploadToPremia, fvrList, statusMap);
								statusMap.put("1", "Attached upload document only");
								statusMap = setValueForStatusMap(claim, statusMap);
							} else {
								statusMap.put("1", "Attached upload document only");
								statusMap = setValueForStatusMap(claim, statusMap);
							}

							if (updateDocUploadPremiaTbl(statusMap,
									docUploadToPremia)) {
								processReportIdForPremiaDocument(docUploadToPremia,
										statusMap, claim);
							}

						} else if (null != docType
								&& !("").equalsIgnoreCase(docType)
								&& ((SHAConstants.PREMIA_DOC_TYPE_FVR)
										.equalsIgnoreCase(docType))) {
							/**
							 * Implementation is kept on hold, since query for
							 * cashless can be raised from pre auth and enhancement.
							 * Hence to be discussed with sathish sir , which needs
							 * to be invoked.
							 * */
							// Intimation intimationObject =
							// starFaxStimulatorService.getIntimationObject(docUploadToPremia.getPfdUpIntmNo());
							// if(intimationObject.getIntimationId().equalsIgnoreCase("CLI/2015/111100/0004252")){
							// System.out.print("query first level");
							// }
							Map<String, String> statusMap = null;
							//String strOutCome = "";
							statusMap = new HashMap<String, String>();
							if (null != intimation) {
								if (null != claim && null != claim.getStage()) {
									//Long stageId = claim.getStage().getKey();
									/*String processValue = claim.getStatus()
											.getProcessValue();*/
									if ((SHAConstants.PREMIA_DOC_TYPE_FVR)
											.equalsIgnoreCase(docType)) {
										List<FieldVisitRequest> fvrList = getFieldVisitRequest(intimation
												.getKey());
										statusMap = new HashMap<String, String>();
										if (null != fvrList && !fvrList.isEmpty()) {
											submitFVR(docUploadToPremia, fvrList, statusMap);
											statusMap
													.put("1",
															"Attached upload document only");
											statusMap = setValueForStatusMap(claim,
													statusMap);

										} else {

											statusMap
													.put("1",
															"Attached upload document only");
											statusMap = setValueForStatusMap(claim,
													statusMap);
										}

									}
									if (updateDocUploadPremiaTbl(statusMap,
											docUploadToPremia)) {
										processReportIdForPremiaDocument(
												docUploadToPremia, statusMap, claim);
									}

								} else {
									updateDocUploadPremiaTbl(statusMap,
											docUploadToPremia);
								}
							}

						}
						log.info("****FVR DOC UPLOAD SUCCESSFULLY COMPLETED _______ INTIMATION NO-->" + docUploadToPremia.getPfdUpIntmNo() + " REPORT ID--->" + docUploadToPremia.getPfdUpReportId());
					} else {
						updatePremiaValues(SHAConstants.FAILURE_FLAG_REVISED,SHAConstants.N_FLAG,docUploadToPremia);
						log.debug("&&&&&&&&&&$$$$$$$$$$$ NO INTIMATION OR NO CLAIM FOR THIS INTIMATION ******%%%%%%% --->" + docUploadToPremia.getPfdUpIntmNo() + " REPORT ID--->" + docUploadToPremia.getPfdUpReportId());
					}
					utx.commit();
				} catch (Exception e) {
					e.printStackTrace();
					try {
						utx.rollback();
					} catch (IllegalStateException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SecurityException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SystemException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					try{
						utx.begin();
						log.debug("##################################"
								+ " ISSUE WHILE FVR DOC UPLOAD PLEASE LOOK INTO THIS INTIMATION  ##################----->" + docUploadToPremia.getPfdUpIntmNo());
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
					
					log.error("*******ERROR OCCURED WHILE PROCESSING RECORDS FROM E STARFAX***************"
							+ "Intimation NO : --> "
							+ docUploadToPremia.getPfdUpIntmNo()
							+ "  REPORT ID--->"
							+ docUploadToPremia.getPfdUpReportId());
				}

			}
		} else {
			log.debug("*******The requested intimation in starfax have been processed already. Please create new request in starfax , before executing the batch.***************");
		}

	}
	
	public void submitDBprocedure(Claim claim,String currentQueue,String outCome){
		
		Map<String, Object> mapValues = new WeakHashMap<String, Object>();
		mapValues.put(SHAConstants.INTIMATION_NO, claim.getIntimation().getIntimationId());
		mapValues.put(SHAConstants.CURRENT_Q, currentQueue);
		
//		Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
		
		Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		List<Map<String, Object>> taskProcedure = dbCalculationService.revisedGetTaskProcedureForBatch(setMapValues);
		
		for (Map<String, Object> map : taskProcedure) {
			map.put(SHAConstants.STAGE_SOURCE, SHAConstants.SOURCE_FVR_REPLY);
		}

		List<Object[]> arrayListFromGettask = SHAUtils.getArrayListFromGettask(taskProcedure);	
		
		 
		for (int i = 0; i < arrayListFromGettask.size(); i++) {
			Object[] resultArrayObj = arrayListFromGettask.get(i);
			Object[] object1 = (Object[]) resultArrayObj[0];
			object1[SHAConstants.INDEX_OUT_COME] = outCome;
			object1[SHAConstants.INDEX_PROECSSED_DATE] = new DATE();
			Object[] parameter = new Object[1];
			parameter[0] = object1;
//			String initiateTaskProcedure = dbCalculationService.initiateTaskProcedure(parameter);
			dbCalculationService.revisedInitiateTaskProcedure(parameter);
		}
		
	}
	
	public Boolean getDBTaskForPreauth(Intimation intimation,String currentQ){
		
		Map<String, Object> mapValues = new WeakHashMap<String, Object>();
		mapValues.put(SHAConstants.INTIMATION_NO, intimation.getIntimationId());
		mapValues.put(SHAConstants.CURRENT_Q, currentQ);
		
//		Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
		
		Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);

		DBCalculationService db = new DBCalculationService();
//		 List<Map<String, Object>> taskProcedure = db.getTaskProcedure(setMapValues);
		
		 List<Map<String, Object>> taskProcedure = db.revisedGetTaskProcedureForBatch(setMapValues);
		if (taskProcedure != null && !taskProcedure.isEmpty()){
			return true;
		} 
		return false;
	}
	
	public void updateFvrResponseTable(FieldVisitRequest fvrKey){
//		entityManager.merge(claim);
//		entityManager.flush();
		try{
		
			String query = "UPDATE GLX_FVR_RESPONSE SET GFRS_STATUS = 'Replied',GFRS_SUB_DT=SYSDATE"
					+ ",GFRS_DOC_REC_DT=SYSDATE WHERE GFRS_INTM_NO="+"'"
					+fvrKey.getClaim().getIntimation().getIntimationId()+"'"+" AND GFRS_STATUS IN ('Assigned','Reassigned') AND GFRS_SUB_DT IS NULL";
		
		Query nativeQuery = entityManager.createNativeQuery(query);
		nativeQuery.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public void updateFvrRequestTable(FieldVisitRequest fvrKey){
//		entityManager.merge(claim);
//		entityManager.flush();
		try{
		
			String query = "UPDATE GLX_FVR_REQUEST SET GFR_STATUS = 'Replied' WHERE GFR_INTM_NO="+"'"
					+fvrKey.getClaim().getIntimation().getIntimationId()+"'"+" AND GFR_STATUS IN ('Assigned','Reassigned')";
		
		Query nativeQuery = entityManager.createNativeQuery(query);
		nativeQuery.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}

	}	
	
	public String getFvrRequestPendingReason(String fvrNumber){

		String pendingRsn = "";
		try{
		
			String query = "SELECT * FROM GLX_FVR_REQUEST WHERE GFR_FVR_NO="+"'"
					+fvrNumber+"'";
		
			Query nativeQuery = entityManager.createNativeQuery(query);
			List resultList = nativeQuery.getResultList();
		
			if(resultList != null && !resultList.isEmpty()){
				Object row[] = (Object []) resultList.get(0);
				 pendingRsn =  row[24] != null ? (String)row[24] : "";
			}
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return pendingRsn;

	}	
}