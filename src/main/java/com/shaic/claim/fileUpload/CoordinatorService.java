package com.shaic.claim.fileUpload;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.processtranslation.search.SearchProcessTranslationTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Coordinator;
import com.shaic.domain.preauth.Preauth;
import com.shaic.ims.bpm.claim.DBCalculationService;

@Stateless
public class CoordinatorService {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	
	public Coordinator getCoordinatorByIntimationKey(Long Intimationkey){
		
		Query findByIntimationKey = entityManager.createNamedQuery(
				"Coordinator.findByIntimationKey").setParameter("intimationKey", Intimationkey);
		Coordinator coordinatorList = (Coordinator) findByIntimationKey.getSingleResult();
		
		if(coordinatorList!=null){
			return coordinatorList;
		}
		return null;
	}
	
	public Coordinator getCoordinatorByPreauthKey(Long key){
		
		Query findByIntimationKey = entityManager.createNamedQuery(
				"Coordinator.findByClaimKey").setParameter("claimKey", key);
		List<Coordinator> coordinatorList = (List<Coordinator>) findByIntimationKey.getResultList();
		
		List<Long> keys = new ArrayList<Long>();
       for (Coordinator coordinator : coordinatorList) {
    	   
    	   keys.add(coordinator.getKey());
			
		}
       if(!keys.isEmpty()){
       Long maxKey = Collections.max(keys);
       
       Query query  = entityManager.createNamedQuery(
				"Coordinator.findByKey").setParameter("primaryKey", maxKey);
		Coordinator coordinator = (Coordinator) query.getSingleResult();
		entityManager.refresh(coordinator);
		return coordinator;
       }
       return null;
	}
	
	public Boolean updateCoordinatorRemarks(FileUploadDTO bean,SearchProcessTranslationTableDTO fileUploadDTO){
		Boolean coordinatorRemarks = Boolean.FALSE;
		if(null != fileUploadDTO.getRodKey())
		{
			coordinatorRemarks = updateCoordinatorForReimbursement(bean, fileUploadDTO); 
		}
		else
		{
			Query findByKey=entityManager.createNamedQuery("Coordinator.findByKey").setParameter("primaryKey", bean.getKey());
			
			Coordinator coordinatorList=(Coordinator)findByKey.getSingleResult();
			Claim claim=coordinatorList.getClaim();
			
			Preauth preauth = null;
			
			if(coordinatorList.getTransactionKey() != null){
				preauth = getPreauthById(coordinatorList.getTransactionKey());
			}
			else{
				Query findByClaimKey=entityManager.createNamedQuery("Preauth.findByClaimKey").setParameter("claimkey", claim.getKey());
			
				preauth = (Preauth) findByClaimKey.getResultList().get(0);
			}
			
			String strUserName = fileUploadDTO.getUsername();
			strUserName = SHAUtils.getUserNameForDB(strUserName);
			
			Status status=new Status();
			if(preauth.getStage().getKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_STAGE)){
				status.setKey(ReferenceTable.PREMEDICAL_PREAUTH_COORDINATOR_REPLY);
			}else if(preauth.getStage().getKey().equals(ReferenceTable.PREAUTH_STAGE)){
				status.setKey(ReferenceTable.PREAUTH_COORDINATOR_REPLY_RECEIVED_STATUS);
			}else if(preauth.getStage().getKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_STAGE)){
				status.setKey(ReferenceTable.PREMEDICAL_ENHANCEMENT_COORDINATOR_REPLY);
			}else if(preauth.getStage().getKey().equals(ReferenceTable.ENHANCEMENT_STAGE)){
				status.setKey(ReferenceTable.ENHANCEMENT_COORDINATOR_REPLY_RECEIVED_STATUS);
			}
			
			
			
			if(coordinatorList!=null){
				
				//preauth.setStage(stage);
				preauth.setStatus(status);
				preauth.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				preauth.setModifiedBy(strUserName);
				entityManager.merge(preauth);
				entityManager.flush();
				entityManager.clear();
			}
			
			if(coordinatorList!=null){
				coordinatorList.setCoordinatorRemarks(bean.getRemarks());
				coordinatorList.setFileToken(bean.getFileToken());
				coordinatorList.setFileName(bean.getFileName());
				coordinatorList.setStage(preauth.getStage());
				coordinatorList.setStatus(status);
				coordinatorList.setCoordinatorReplyDate(new Timestamp(System.currentTimeMillis()));
				coordinatorList.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				coordinatorList.setModifiedBy(strUserName);
				entityManager.merge(coordinatorList);
				entityManager.flush();
				entityManager.clear();
				
				//String outCome = "TRANSLATE";
				
				/*if(preauth.getStage().getKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_STAGE) || 
						preauth.getStage().getKey().equals(ReferenceTable.ENHANCEMENT_STAGE)){
					
					outCome = "TRANSLATEENH";
					
				}*/
				submitTaskToDB(bean, fileUploadDTO,status);
				coordinatorRemarks = Boolean.TRUE;
				//return coordinatorRemarks;
			}
		}
		return coordinatorRemarks;
	}
	
	public Boolean updateCoordinatorForReimbursement(FileUploadDTO bean,SearchProcessTranslationTableDTO fileUploadDTO){
		
		Boolean updateCoordinatorRemarks = Boolean.FALSE;
		
		Query findByKey=entityManager.createNamedQuery("Coordinator.findByKey").setParameter("primaryKey", bean.getKey());
		
		Coordinator coordinatorList=(Coordinator)findByKey.getSingleResult();
		
		Query findByClaimKey=entityManager.createNamedQuery("Reimbursement.findByKey").setParameter("primaryKey", fileUploadDTO.getRodKey());
		Reimbursement reimbursement = (Reimbursement) findByClaimKey.getSingleResult();
		
		String strUserName = fileUploadDTO.getUsername();
		strUserName = SHAUtils.getUserNameForDB(strUserName);
		
		Status status=new Status();
		status.setKey(ReferenceTable.CLAIM_REQUEST_COORDINATOR_REPLY_RECEIVED_STATUS);
		
		if(coordinatorList.getStage().getKey().equals(ReferenceTable.ZONAL_REVIEW_STAGE)){
			status.setKey(ReferenceTable.ZONAL_REVIEW_COORDINATOR_REPLY_STATUS);
		}else if(coordinatorList.getStage().getKey().equals(ReferenceTable.CLAIM_REQUEST_STAGE)){
			status.setKey(ReferenceTable.CLAIM_REQUEST_COORDINATOR_REPLY_RECEIVED_STATUS);
		}else if(coordinatorList.getStage().getKey().equals(ReferenceTable.BILLING_STAGE)){
			status.setKey(ReferenceTable.BILLING_COORDINATOR_REPLY_RECEIVED);
		}else if(coordinatorList.getStage().getKey().equals(ReferenceTable.FINANCIAL_STAGE)){
			status.setKey(ReferenceTable.FINANCIAL_COORDINATOR_REPLY_RECEIVED_STATUS);
		}
		
		if(coordinatorList!=null){
//			Claim claim=coordinatorList.getClaim();
//			claim.setStatus(status);
//			entityManager.merge(claim);
			entityManager.flush();
//			entityManager.refresh(claim);
			
			
			//preauth.setStage(stage);
			reimbursement.setStatus(status);
			reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			reimbursement.setModifiedBy(strUserName);
			entityManager.merge(reimbursement);
			entityManager.flush();
			entityManager.refresh(reimbursement);
		}
		
		if(coordinatorList!=null){
			coordinatorList.setCoordinatorRemarks(bean.getRemarks());
			//coordinatorList.setStage(stage);
			coordinatorList.setFileName(bean.getFileName());
			coordinatorList.setFileToken(bean.getFileToken());
			coordinatorList.setCoordinatorReplyDate(new Timestamp(System.currentTimeMillis()));
			coordinatorList.setStatus(status);
			coordinatorList.setStage(reimbursement.getStage());
			coordinatorList.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			coordinatorList.setModifiedBy(strUserName);
			entityManager.merge(coordinatorList);
			entityManager.flush();
			entityManager.clear();
			//setBPMOutComeForReimbursement(bean, fileUploadDTO, "SUBMIT",status);
			submitTaskToDB(bean,fileUploadDTO,status);
			updateCoordinatorRemarks = true;
			//return true;
		}
		return updateCoordinatorRemarks;
	}
	
	/*public void setBPMOutComeForReimbursement(FileUploadDTO bean,SearchProcessTranslationTableDTO fileUploadDTO,String outCome,Status status){
		
		SubmitCoordinatorReplyTask submitCoordinatorReplyTask = BPMClientContext.getSubmitCoordinatorReplyTask(fileUploadDTO.getUsername(), fileUploadDTO.getPassword());
		
		HumanTask humanTask = fileUploadDTO.getHumanTaskR3();
		
		humanTask.setOutcome(outCome);
		
		if(humanTask.getPayload() != null && humanTask.getPayload().getClaimRequest() != null){
			if(humanTask.getPayload().getClaimRequest().getReimbReqBy() != null){
				if(humanTask.getPayload().getClaimRequest().getReimbReqBy().equalsIgnoreCase("BILLING")){
					humanTask.getPayload().getClaimRequest().setResult("BILLING");
				}
			}
		}
		
		ClassificationType classification = humanTask.getPayload().getClassification();
		if(classification != null){
			
			status = entityManager.find(Status.class, status.getKey());
			
			if(status != null){
			classification.setSource(status.getProcessValue());
			}
			humanTask.getPayload().setClassification(classification);
		}
		
		try{
		submitCoordinatorReplyTask.execute(fileUploadDTO.getUsername(), humanTask);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}*/
	
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
	
	public void submitTaskToDB(FileUploadDTO bean,SearchProcessTranslationTableDTO searchFormDto,Status status){
		if (searchFormDto != null) {
			Map<String, Object> wrkFlowMap = (Map<String, Object>) searchFormDto.getDbOutArray();
				//Map<String, String> regIntDetailsReq = new HashMap<String, String>();
				//Map<String,String> preauthReq=new HashMap<String, String>();
				if(searchFormDto.getKey()!=null){
					
//					wrkFlowMap.put(SHAConstants.CASHLESS_KEY, searchFormDto.getKey());
//					wrkFlowMap.put(SHAConstants.PAYLOAD_ROD_KEY, searchFormDto.getKey());
							//preauthReqType.setKey(searchFormDto.getDecisionKey());
				}			
				if(searchFormDto.getClaimNo()!=null){
						//claimType.setClaimId(searchFormDto.getClaimNo());
//						wrkFlowMap.put(SHAConstants.CLAIM_NUMBER, searchFormDto.getClaimNo());
						//payloadCashless.setClaim(claimType);
				}
			wrkFlowMap.put(SHAConstants.STAGE_SOURCE, SHAConstants.SOURCE_COORDINATOR_REPLY);
			wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.OUTCOME_COORDINATOR_REPLY);
			wrkFlowMap.put(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER,SHAConstants.PARALLEL_MEDICAL_PENDING);
			Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
			DBCalculationService dbCalService = new DBCalculationService();
			try{
				dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);	
			}catch(Exception e){
				e.printStackTrace();
			}
			System.out.println("BPM Executed Successfully");
			
		}
	}
	
	public List<Coordinator> getCoordinatorListByIntimationKey(Long intimationkey){
		Query query = entityManager.createNamedQuery(
				"Coordinator.findByIntimationKey").setParameter("intimationKey", intimationkey);
		List<Coordinator> coordinatorList = query.getResultList();
		return coordinatorList;
	}
	
	//IMSSUPPOR-26461
	public Coordinator getCoordinatorByRodKey(Long rodkey) {

		Query findByIntimationKey = entityManager.createNamedQuery(
				"Coordinator.findByTransactionKey").setParameter("transaction",
				rodkey);
		List<Coordinator> coordinatorList = (List<Coordinator>) findByIntimationKey
				.getResultList();

		List<Long> keys = new ArrayList<Long>();
		for (Coordinator coordinator : coordinatorList) {

			keys.add(coordinator.getKey());

		}
		if (!keys.isEmpty()) {
			Long maxKey = Collections.max(keys);

			Query query = entityManager.createNamedQuery(
					"Coordinator.findByKey").setParameter("primaryKey", maxKey);
			Coordinator coordinator = (Coordinator) query.getSingleResult();
			entityManager.refresh(coordinator);
			return coordinator;
		}
		return null;
	}

}
