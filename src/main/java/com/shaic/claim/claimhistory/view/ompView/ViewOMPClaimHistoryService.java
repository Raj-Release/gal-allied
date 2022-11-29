package com.shaic.claim.claimhistory.view.ompView;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.claimhistory.view.ViewClaimHistoryDTO;
import com.shaic.domain.OMPDocAcknowledgement;
import com.shaic.domain.OMPReimbursement;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.omp.OMPStageInformation;

@Stateless
public class ViewOMPClaimHistoryService {
	
	@PersistenceContext
	protected EntityManager entityManager;

	public ViewOMPClaimHistoryService() {
		super();
	}

	public List<ViewClaimHistoryDTO> getClaimHistoryForCashless(Long claimKey){
		
		Query query = entityManager.createNamedQuery("OMPStageInformation.findByClaimKey");
		query.setParameter("claimkey", claimKey);
//		query.setParameter("preauthKey", preauthkey);
		List<OMPStageInformation> stageInformationList = (List<OMPStageInformation>) query.getResultList();
		
		List<ViewClaimHistoryDTO> searchClaimHistoryDTO = ViewOMPClaimHistoryMapper.getInstance().getViewClaimHistoryDTO(stageInformationList);
		
		for (ViewClaimHistoryDTO viewClaimHistoryDTO : searchClaimHistoryDTO) {
			
//			if(viewClaimHistoryDTO.getCashlessKey() != null){
//				Preauth preauthById = getPreauthById(viewClaimHistoryDTO.getCashlessKey());
//				if(preauthById != null){
//					if(preauthById.getEnhancementType() != null){
//						viewClaimHistoryDTO.setReferenceNo(preauthById.getPreauthId() + "- Enhancement");
//					}else{
//						viewClaimHistoryDTO.setReferenceNo(preauthById.getPreauthId() + "- Preauth");
//					}
//				}
//			}

			if(viewClaimHistoryDTO.getUserID() != null){
				TmpEmployee employeeName = getEmployeeName(viewClaimHistoryDTO.getUserID());
				if(employeeName != null){
					viewClaimHistoryDTO.setUserName(employeeName.getEmpFirstName());
				}
			}
		}
		
		return searchClaimHistoryDTO;
	}
	
public List<ViewClaimHistoryDTO> getClaimHistoryForOPHealthCheckUp(Long claimKey){
		
		Query query = entityManager.createNamedQuery("OMPStageInformation.findAllByClaimKey");
		query.setParameter("claimkey", claimKey);
		List<OMPStageInformation> stageInformationList = (List<OMPStageInformation>) query.getResultList();
		
		List<ViewClaimHistoryDTO> searchClaimHistoryDTO = ViewOMPClaimHistoryMapper.getInstance()
				.getViewClaimHistoryDTO(stageInformationList);
		
		for (ViewClaimHistoryDTO viewClaimHistoryDTO : searchClaimHistoryDTO) {

			if(viewClaimHistoryDTO.getUserID() != null){
				TmpEmployee employeeName = getEmployeeName(viewClaimHistoryDTO.getUserID());
				if(employeeName != null){
					viewClaimHistoryDTO.setUserName(employeeName.getEmpFirstName());
				}
			}
		}
		
		return searchClaimHistoryDTO;
	}
	
    public List<ViewClaimHistoryDTO> getClaimHistory(Long claimKey, Long rodKey,Long ackStageKey){
		
    	Query query = entityManager.createNamedQuery("OMPStageInformation.findAllByIntimationKey");
		query.setParameter("intimationKey", claimKey);
		//query.setParameter("claimkey", claimKey);
		//query.setParameter("rodKey", rodKey);
		//query.setParameter("stageKey", ackStageKey);		
		
		List<OMPStageInformation> stageInformationList = (List<OMPStageInformation>) query.getResultList();
		
		//List<OMPStageInformation> finalListInformation = new ArrayList<OMPStageInformation>();
		
		List<ViewClaimHistoryDTO> searchClaimHistoryDTO = ViewOMPClaimHistoryMapper.getInstance().getViewClaimHistoryDTO(stageInformationList);
		
		//List<OMPDocAcknowledgement> documentAcknowledgeByClaim = getDocumentAcknowledgeByClaim(claimKey);

		for (ViewClaimHistoryDTO viewClaimHistoryDTO : searchClaimHistoryDTO) {
			if(viewClaimHistoryDTO.getReimbursementKey() != null){
				OMPReimbursement reimbursementByKey = getReimbursementByKey(viewClaimHistoryDTO.getReimbursementKey());
				if(reimbursementByKey != null){
					viewClaimHistoryDTO.setReferenceNo(reimbursementByKey.getRodNumber());
					/*if(reimbursementByKey.getDocAcknowLedgement() != null ){
						if(reimbursementByKey.getDocAcknowLedgement().getDocumentReceivedFromId() != null					
								&& reimbursementByKey.getDocAcknowLedgement().getDocumentReceivedFromId().getValue() != null){
							viewClaimHistoryDTO.setDocrecdfrom(reimbursementByKey.getDocAcknowLedgement().getDocumentReceivedFromId().getValue());
						}
						else{
							viewClaimHistoryDTO.setDocrecdfrom("");
						}
						if(reimbursementByKey.getDocAcknowLedgement().getReconsiderationRequest() != null && SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursementByKey.getDocAcknowLedgement().getReconsiderationRequest())){
							viewClaimHistoryDTO.setRodtype(SHAConstants.RECONSIDERATION);
						}
						else{
							viewClaimHistoryDTO.setRodtype(SHAConstants.ORIGINAL);
						}
					}
					if(reimbursementByKey.getClassificationId() != null && reimbursementByKey.getClassificationId().getValue() != null){
						viewClaimHistoryDTO.setClassification(reimbursementByKey.getClassificationId().getValue());
					}
					else{
						viewClaimHistoryDTO.setClassification("");
					}
					if(reimbursementByKey.getSubClassificationId() != null && reimbursementByKey.getSubClassificationId().getValue() != null){
						viewClaimHistoryDTO.setSubclassification(reimbursementByKey.getSubClassificationId().getValue());
					}
					else{
						viewClaimHistoryDTO.setSubclassification("");
					}
					if(reimbursementByKey.getClassiDocumentRecivedFmId()!= null && reimbursementByKey.getClassiDocumentRecivedFmId().getValue()!= null){
						viewClaimHistoryDTO.setDocrecdfrom(reimbursementByKey.getClassiDocumentRecivedFmId().getValue());
					}else{
						viewClaimHistoryDTO.setDocrecdfrom("");
					}
					if(reimbursementByKey.getStatus()!= null && reimbursementByKey.getStatus().getProcessValue()!= null){
						viewClaimHistoryDTO.setStatus(reimbursementByKey.getStatus().getProcessValue());
					}else{
						viewClaimHistoryDTO.setStatus("");
					}
					if(reimbursementByKey.getStage()!= null && reimbursementByKey.getStage().getStageName()!= null){
						viewClaimHistoryDTO.setClaimStage(reimbursementByKey.getStage().getStageName());
					}else{
						viewClaimHistoryDTO.setClaimStage("");
					}*/
					if(viewClaimHistoryDTO!=null &&viewClaimHistoryDTO.getTypeofClaim()==null){
						viewClaimHistoryDTO.setTypeofClaim("Non Medical");
					}
				}
			}else{
				if(viewClaimHistoryDTO.getStatusID() != null && viewClaimHistoryDTO.getStatusID().equals(ReferenceTable.OMP_ACKNOWLEDGE_STATUS_KEY)){
					List<OMPDocAcknowledgement> documentAcknowledgeByClaim = getDocumentAcknowledgeByackkey(viewClaimHistoryDTO.getAckKey());
					for (OMPDocAcknowledgement docAcknowledgement : documentAcknowledgeByClaim) {
						if(viewClaimHistoryDTO.getCreatedDate() != null && docAcknowledgement.getCreatedDate().compareTo(viewClaimHistoryDTO.getCreatedDate()) == 0){

							if(docAcknowledgement.getDocumentReceivedFromId() != null					
									&& docAcknowledgement.getDocumentReceivedFromId().getValue() != null){
								viewClaimHistoryDTO.setDocrecdfrom(docAcknowledgement.getDocumentReceivedFromId().getValue());
							}
							else{
								viewClaimHistoryDTO.setDocrecdfrom("");
							}
							
							if(docAcknowledgement.getClassificationId() != null					
									&& docAcknowledgement.getClassificationId().getValue() != null){
								viewClaimHistoryDTO.setClassification(docAcknowledgement.getClassificationId().getValue());
							}
							else{
								viewClaimHistoryDTO.setClassification("");
							}
							if(docAcknowledgement.getSubClassificationId() != null					
									&& docAcknowledgement.getSubClassificationId().getValue() != null){
								viewClaimHistoryDTO.setSubclassification(docAcknowledgement.getSubClassificationId().getValue());
							}
							else{
								viewClaimHistoryDTO.setSubclassification("");
							}
							if(docAcknowledgement.getReconsiderationRequest() != null && SHAConstants.YES_FLAG.equalsIgnoreCase(docAcknowledgement.getReconsiderationRequest())){
								viewClaimHistoryDTO.setRodtype(SHAConstants.RECONSIDERATION);
							}
							else{
								viewClaimHistoryDTO.setRodtype(SHAConstants.ORIGINAL);
							}						
							
							viewClaimHistoryDTO.setReferenceNo(docAcknowledgement.getAcknowledgeNumber() + " - Acknowledgement");
							break;
						}
					}

				}
			}
			
			if(viewClaimHistoryDTO.getUserID() != null){
				TmpEmployee employeeName = getEmployeeName(viewClaimHistoryDTO.getUserID());
				if(employeeName != null){
					viewClaimHistoryDTO.setUserName(employeeName.getEmpFirstName());
					String temp = viewClaimHistoryDTO.getUserID().toUpperCase();
					viewClaimHistoryDTO.setUserID(temp);
				}
			}
		}
		
		return searchClaimHistoryDTO;
	}
    
	public List<OMPDocAcknowledgement> getDocumentAcknowledgeByClaim(Long claimKey)
	{
		Query query = entityManager
				.createNamedQuery("OMPDocAcknowledgement.findByClaimKey");
		query.setParameter("claimkey", claimKey);
		List<OMPDocAcknowledgement> reimbursementDetails = query.getResultList();
		return reimbursementDetails ;
	}
    
	@SuppressWarnings("unchecked")
	public OMPReimbursement getReimbursementByKey(Long rodKey) {
		Query query = entityManager.createNamedQuery("OMPReimbursement.findByKey")
				.setParameter("primaryKey", rodKey);
		List<OMPReimbursement> rodList = query.getResultList();

		if (rodList != null && !rodList.isEmpty()) {
			for (OMPReimbursement reimbursement : rodList) {
				entityManager.refresh(reimbursement);
			}
			return rodList.get(0);
		}
		return null;
	}
	
	  private TmpEmployee getEmployeeName(String initiatorId)
		{
		  TmpEmployee fvrInitiatorDetail;
			Query findByTransactionKey = entityManager.createNamedQuery(
					"TmpEmployee.getEmpByLoginId").setParameter("loginId", initiatorId.toLowerCase());
			try{
				fvrInitiatorDetail =(TmpEmployee) findByTransactionKey.getSingleResult();
				return fvrInitiatorDetail;
			}
			catch(Exception e)
			{
				return null;
			}
								
		}
	  
//	  public Preauth getPreauthById(Long preauthKey) {
//			Query query = entityManager.createNamedQuery("Preauth.findByKey");
//			query.setParameter("preauthKey", preauthKey);
//			@SuppressWarnings("unchecked")
//			List<Preauth> singleResult = (List<Preauth>) query.getResultList();
//			if(singleResult != null && ! singleResult.isEmpty()) {
//				entityManager.refresh(singleResult.get(0));
//				return singleResult.get(0);
//			}
//			
//			return null;
//			
//			
//		}
	
	  public List<OMPDocAcknowledgement> getDocumentAcknowledgeByackkey(Long claimKey)
		{
			Query query = entityManager
					.createNamedQuery("OMPDocAcknowledgement.findByKey");
			query.setParameter("ackDocKey", claimKey);
			List<OMPDocAcknowledgement> reimbursementDetails = query.getResultList();
			return reimbursementDetails ;
		}
}
