package com.shaic.claim.clearcashless;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.clearcashless.dto.SearchClearCashlessDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.DocUploadToPremia;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Preauth;
import com.shaic.ims.bpm.claim.DBCalculationService;


@Stateless
public class ClearCashlessService {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
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
	
	public Hospitals getHospitalObject(Long hospitalKey) {
		TypedQuery<Hospitals> query = entityManager.createNamedQuery("Hospitals.findByKey", Hospitals.class);
		query.setParameter("key", hospitalKey);
		List<Hospitals>	resultList = query.getResultList();
		if (null != resultList && 0 != resultList.size())
		{
			return resultList.get(0);
			
		}
		else
		{
			return null;
		}
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

	
	
	
	
	public Map<String, Boolean> upDateClearCashLess(SearchClearCashlessDTO dto) {
		Map<String, Boolean> returnMap = new HashMap<String, Boolean>();
		returnMap.put(SHAConstants.IS_CLEARED, true);
		returnMap.put(SHAConstants.IS_FIRST_LEVEL_QUERY, false);
		Boolean isRecievePreauth = false;
		try {
			Preauth preauthByKey = null;
			if(dto.getPreauthKey() != null){
				preauthByKey = getPreauthById(dto.getPreauthKey());
			}
				
//				HumanTask humanTask = dto.getHumanTask();
				Map<String, Object> dbOutArray = (Map<String, Object>)dto.getDbOutArray();
 
				if(dto.getPreauthKey() != null && dto.getIsQueryReplyReceived()) {
					// ################### QUERY REPLY RECEIVED  ####################
					
					if(dto.getIsEnhancement()) {
//						humanTask.getPayloadCashless().getPreAuthReq().setOutcome(SHAConstants.QUERY_ENH_END);
//						humanTask.getPayloadCashless().getPreAuthReq().setResult(SHAConstants.QUERY_ENH_END);
//						humanTask.setOutcome(SHAConstants.QUERY_ENH_END);
						
						dbOutArray.put(SHAConstants.OUTCOME,SHAConstants.OUTCOME_PROCESS_ENHANCEMENT_QUERY);
						
						
//						SubmitPreAuthEnhTask task = BPMClientContext.getPreauthEnhancementTask(dto.getUsername(), dto.getPassword());
//						task.execute(dto.getUsername(), humanTask);
						
						//Object[] objArrayForSubmit = SHAUtils.getObjArrayForSubmit(dbOutArray);
						Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(dbOutArray);
						
						DBCalculationService dbCalService = new DBCalculationService();
						//dbCalService.initiateTaskProcedure(objArrayForSubmit);
						dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
						
					} else {
//						humanTask.getPayloadCashless().getPreAuthReq().setOutcome(SHAConstants.QUERY_PRE_END);
//						humanTask.getPayloadCashless().getPreAuthReq().setResult(SHAConstants.QUERY_PRE_END);
//						humanTask.setOutcome(SHAConstants.QUERY_PRE_END);
						
						dbOutArray.put(SHAConstants.OUTCOME,SHAConstants.OUTCOME_PROCESS_PREAUTH_QUERY);
						
						/*Object[] objArrayForSubmit = SHAUtils.getObjArrayForSubmit(dbOutArray);
						
						DBCalculationService dbCalService = new DBCalculationService();
						dbCalService.initiateTaskProcedure(objArrayForSubmit);*/
						Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(dbOutArray);						
						DBCalculationService dbCalService = new DBCalculationService();						
						dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
						
					}
				} else if(dto.getPreauthKey() != null && dto.getIsReconsideration()) {
					// Clearing Reconsideration
					if(!dto.getIsEnhancement()) {
//						humanTask.getPayloadCashless().getPreAuthReq().setOutcome(SHAConstants.PREAUTH_END);
//						humanTask.getPayloadCashless().getPreAuthReq().setResult(SHAConstants.PREAUTH_END);
//						humanTask.setOutcome(SHAConstants.PREAUTH_END);
						
						dbOutArray.put(SHAConstants.OUTCOME,SHAConstants.OUTCOME_PROCESS_PREAUTH_DENIEL_OF_CASHLESS);
						
//						Object[] objArrayForSubmit = SHAUtils.getObjArrayForSubmit(dbOutArray);
						
						Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(dbOutArray);
						
						DBCalculationService dbCalService = new DBCalculationService();
						dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
						
//						SubmitPreAuthEnhTask task = BPMClientContext.getPreauthEnhancementTask(dto.getUsername(), dto.getPassword());
//						task.execute(dto.getUsername(), humanTask);
						
						
						
					} else {
						
						
//						humanTask.getPayloadCashless().getPreAuthReq().setOutcome(SHAConstants.ENHANCEMENT_END);
//						humanTask.getPayloadCashless().getPreAuthReq().setResult(SHAConstants.ENHANCEMENT_END);
//						humanTask.setOutcome(SHAConstants.ENHANCEMENT_END);
						
						dbOutArray.put(SHAConstants.OUTCOME,SHAConstants.OUTCOME_PROCESS_ENHANCEMENT_REJECT);
						
						/*Object[] objArrayForSubmit = SHAUtils.getObjArrayForSubmit(dbOutArray);
						
						DBCalculationService dbCalService = new DBCalculationService();
						dbCalService.initiateTaskProcedure(objArrayForSubmit);*/
						Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(dbOutArray);						
						DBCalculationService dbCalService = new DBCalculationService();						
						dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
						
//						SubmitPreAuthTask task = BPMClientContext.getPreauthTask(dto.getUsername(), dto.getPassword());
//						task.execute(dto.getUsername(), humanTask);
					}
				} else if(!dto.getIsEnhancement() && (dto.getPreauthKey() == null || dto.getIsFirstLevelQueryReplyReceived())) {
					// ############## Clearing First Level PREAUTH #################
//					humanTask.getPayloadCashless().getPreAuthReq().setOutcome(SHAConstants.FLP_END);
//					humanTask.getPayloadCashless().getPreAuthReq().setResult(SHAConstants.FLP_END);
//					humanTask.setOutcome(SHAConstants.FLP_END);
//					
//					ClaimRequestType claimRequestTyp = humanTask.getPayloadCashless().getClaimRequest();
//					if(claimRequestTyp == null) {
//						claimRequestTyp = new ClaimRequestType();
//					}
//					
//					claimRequestTyp.setOption("");
//					humanTask.getPayloadCashless().setClaimRequest(claimRequestTyp);
//					CustomerType customer = humanTask.getPayloadCashless().getCustomer();
//					if(customer == null){
//						 customer = new CustomerType();
//					}
//					customer.setTreatmentType("");
//					customer.setSpeciality("");
//					
//					
//					humanTask.getPayloadCashless().getClassification().setSource("First Level Process(Pre Auth)");
//					
//					humanTask.getPayloadCashless().setCustomer(customer);
//					
//					
//					SubmitPreMedicalPreAuthTask task = BPMClientContext.getPremedicalPreauth(dto.getUsername(), dto.getPassword());
//					task.execute(dto.getUsername(), humanTask);
					
					dbOutArray.put(SHAConstants.OUTCOME,SHAConstants.PREMEDICAL_FIRST_LEVEL_END);
					
					if(dto.getIsFirstLevelQueryReplyReceived()){
						dbOutArray.put(SHAConstants.OUTCOME,SHAConstants.OUTCOME_FLP_QUERY);
					}
					
					/*Object[] objArrayForSubmit = SHAUtils.getObjArrayForSubmit(dbOutArray);
					
					DBCalculationService dbCalService = new DBCalculationService();
					dbCalService.initiateTaskProcedure(objArrayForSubmit);*/
					Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(dbOutArray);						
					DBCalculationService dbCalService = new DBCalculationService();						
					dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
					
					if(dto.getPreauthKey() == null){
						isRecievePreauth =true;
					}
					
//					isRecievePreauth =true;
					
					
				} else if(dto.getIsEnhancement() && (dto.getPreauthKey() == null || dto.getIsFirstLevelQueryReplyReceived())) {
					// Clearing First Level Enh
//					humanTask.getPayloadCashless().getPreAuthReq().setOutcome(SHAConstants.FLE_END);
//					humanTask.getPayloadCashless().getPreAuthReq().setResult(SHAConstants.FLE_END);
//					humanTask.setOutcome(SHAConstants.FLE_END);
//					SubmitPreMedicalPreAuthEnhTask task = BPMClientContext.getPremedicalEnhancementTask(dto.getUsername(), dto.getPassword());
//					task.execute(dto.getUsername(), humanTask);
					
					dbOutArray.put(SHAConstants.OUTCOME,SHAConstants.PREMEDICAL_ENHANCEMENT_END);
					
					if(dto.getIsFirstLevelQueryReplyReceived()){
						dbOutArray.put(SHAConstants.OUTCOME,SHAConstants.OUTCOME_FLE_QUERY);
					}
					
					/*Object[] objArrayForSubmit = SHAUtils.getObjArrayForSubmit(dbOutArray);
					
					DBCalculationService dbCalService = new DBCalculationService();
					dbCalService.initiateTaskProcedure(objArrayForSubmit);*/
					Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(dbOutArray);						
					DBCalculationService dbCalService = new DBCalculationService();						
					dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
					
					
					
				} else if(!dto.getIsEnhancement() && dto.getPreauthKey() != null) {
					// Clearing  Preauth
//					humanTask.getPayloadCashless().getPreAuthReq().setOutcome(SHAConstants.PREAUTH_END);
//					humanTask.getPayloadCashless().getPreAuthReq().setResult(SHAConstants.PREAUTH_END);
//					humanTask.setOutcome(SHAConstants.PREAUTH_END);
//					SubmitPreAuthEnhTask task = BPMClientContext.getPreauthEnhancementTask(dto.getUsername(), dto.getPassword());
//					task.execute(dto.getUsername(), humanTask);
					
					dbOutArray.put(SHAConstants.OUTCOME,SHAConstants.PREAUTH_END_OUTCOME);
					
					/*Object[] objArrayForSubmit = SHAUtils.getObjArrayForSubmit(dbOutArray);
					
					DBCalculationService dbCalService = new DBCalculationService();
					dbCalService.initiateTaskProcedure(objArrayForSubmit);*/
					Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(dbOutArray);						
					DBCalculationService dbCalService = new DBCalculationService();						
					dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
					
					isRecievePreauth =true;
					
				} else if(dto.getIsEnhancement() && dto.getPreauthKey() != null) {
					// Clearing  enhancement
//					humanTask.getPayloadCashless().getPreAuthReq().setOutcome(SHAConstants.ENHANCEMENT_END);
//					humanTask.getPayloadCashless().getPreAuthReq().setResult(SHAConstants.ENHANCEMENT_END);
//					humanTask.setOutcome(SHAConstants.ENHANCEMENT_END);
//					SubmitPreAuthTask task = BPMClientContext.getPreauthTask(dto.getUsername(), dto.getPassword());
//					task.execute(dto.getUsername(), humanTask);
					dbOutArray.put(SHAConstants.OUTCOME,SHAConstants.ENHANCEMENT_END_OUTCOME);
					
					/*Object[] objArrayForSubmit = SHAUtils.getObjArrayForSubmit(dbOutArray);
					
					DBCalculationService dbCalService = new DBCalculationService();
					dbCalService.initiateTaskProcedure(objArrayForSubmit);*/
					Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(dbOutArray);						
					DBCalculationService dbCalService = new DBCalculationService();						
					dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
					
				}
				
				if(preauthByKey != null) {
					Status status = new Status();
					if(dto.getIsQueryReplyReceived()) {
						status.setKey(ReferenceTable.PREAUTH_QUERY_STATUS);
						if(dto.getIsEnhancement()) {
							status.setKey(ReferenceTable.ENHANCEMENT_QUERY_STATUS);
						} 
					} else if(dto.getIsFirstLevelQueryReplyReceived()){
						status.setKey(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS);
						if(dto.getIsEnhancement()) {
							status.setKey(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS);
						} 
					} else if (dto.getIsReconsideration()) {
						status.setKey(preauthByKey.getStatus().getKey());
					} else {
						if(preauthByKey.getStatus() != null && (preauthByKey.getStatus().getKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_RECEIVED_STATUS) || preauthByKey.getStatus().getKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_RECEIVED_STATUS)) ) {
							returnMap.put(SHAConstants.IS_FIRST_LEVEL_QUERY, true);
						}
						status.setKey(ReferenceTable.CLEAR_CASHLESS_STATUS_FOR_PREAUTH);
						if(dto.getIsEnhancement()) {
							status.setKey(ReferenceTable.CLEAR_CASHLESS_STATUS_FOR_ENHANCEMENT);
						} 
					}
					
					preauthByKey.setTotalApprovalAmount(0d);
					if((!preauthByKey.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_QUERY_RECEIVED_STATUS)) && (!preauthByKey.getStatus().getKey().equals(ReferenceTable.PREAUTH_QUERY_RECEIVED_STATUS))){
						preauthByKey.setModifiedBy(dto.getUsername());
					} 
					preauthByKey.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					preauthByKey.setClearingRemarks(dto.getClearCashlessRemarks());
					preauthByKey.setStatus(status);
					entityManager.merge(preauthByKey);
					entityManager.flush();
					entityManager.clear();
				}
		} catch(Exception e){
			e.printStackTrace();
			isRecievePreauth = false;
			returnMap.put(SHAConstants.IS_CLEARED, false);
		}
		returnMap.put(SHAConstants.RECEIVED_PREAUTH, isRecievePreauth);
		return returnMap;
	}
	
	public DocUploadToPremia getUploadedDataDocument(String intimationNo, String docType)
	{
		Query query = entityManager.createNamedQuery("DocUploadToPremia.findByIntimationAndDocType");
		query = query.setParameter("intimationNo", intimationNo); 
		query.setParameter("docType", docType );
		List<DocUploadToPremia> listOfDocUploadData = query.getResultList();
		if(null != listOfDocUploadData && !listOfDocUploadData.isEmpty())
		{
			entityManager.refresh(listOfDocUploadData.get(0));
			return listOfDocUploadData.get(0);
		}
		return null;
	}
	
	
	public List<DocUploadToPremia> getUploadedDataDocument(String intimationNo, String docType1, String docType2)
	{
		Query query = entityManager.createNamedQuery("DocUploadToPremia.findByIntimationAndAllDocType");
		query = query.setParameter("intimationNo", intimationNo); 
		query.setParameter("docType1", docType1 );
		query.setParameter("docType2", docType2 );
		List<DocUploadToPremia> listOfDocUploadData = query.getResultList();
		if(null != listOfDocUploadData && !listOfDocUploadData.isEmpty())
		{
			for (DocUploadToPremia docUploadToPremia : listOfDocUploadData) {
				entityManager.refresh(docUploadToPremia);
			}
			return listOfDocUploadData;
		}
		return null;
	}
	
	public void updateDocumentDetails(String intimationNo, Long applicationId)
	{
		Query query = entityManager.createNamedQuery("DocumentDetails.findLatestDocumentByIntimationNoAndApplicationID");
		query = query.setParameter("intimationNumber", intimationNo); 
		query.setParameter("applicationId", applicationId );
		List<DocumentDetails> listOfDocUploadData = query.getResultList();
		if(null != listOfDocUploadData && !listOfDocUploadData.isEmpty())
		{
			for (DocumentDetails documentDetails : listOfDocUploadData) {
				documentDetails.setDeletedFlag("Y");
				entityManager.merge(documentDetails);
				entityManager.flush();
				entityManager.clear();
				
			}
			//entityManager.flush();
		}
	}
	
	
	
}
