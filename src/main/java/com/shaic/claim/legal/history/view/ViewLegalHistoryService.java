package com.shaic.claim.legal.history.view;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.StageLegalInformation;

@Stateless
public class ViewLegalHistoryService {
	
	
	@PersistenceContext
	protected EntityManager entityManager;

	public ViewLegalHistoryService() {
		super();
	}

	public List<ViewLegalHistoryDTO> getClaimHistoryForCashless(Long claimKey){
		
		Query query = entityManager.createNamedQuery("StageLegalInformation.findByClaimKey");
		query.setParameter("claimkey", claimKey);
//		query.setParameter("preauthKey", preauthkey);
		List<StageLegalInformation> stageInformationList = (List<StageLegalInformation>) query.getResultList();
		
		List<ViewLegalHistoryDTO> searchClaimHistoryDTO = ViewLegalHistoryMapper.getInstance()
				.getViewClaimHistoryDTO(stageInformationList);
		
		for (ViewLegalHistoryDTO viewClaimHistoryDTO : searchClaimHistoryDTO) {
			
			if(viewClaimHistoryDTO.getCashlessKey() != null){
				Preauth preauthById = getPreauthById(viewClaimHistoryDTO.getCashlessKey());
				if(preauthById != null){
					if(preauthById.getEnhancementType() != null){
						viewClaimHistoryDTO.setReferenceNo(preauthById.getPreauthId() + "- Enhancement");
					}else{
						viewClaimHistoryDTO.setReferenceNo(preauthById.getPreauthId() + "- Preauth");
					}
				}
			}

			if(viewClaimHistoryDTO.getUserID() != null){
				TmpEmployee employeeName = getEmployeeName(viewClaimHistoryDTO.getUserID());
				if(employeeName != null){
					viewClaimHistoryDTO.setUserName(employeeName.getEmpFirstName());
				}
			}
		}
		
		return searchClaimHistoryDTO;
	}
	
public List<ViewLegalHistoryDTO> getClaimHistoryForOPHealthCheckUp(Long claimKey){
		
		Query query = entityManager.createNamedQuery("StageInformation.findAllByClaimKey");
		query.setParameter("claimkey", claimKey);
//		query.setParameter("preauthKey", preauthkey);
		List<StageLegalInformation> stageInformationList = (List<StageLegalInformation>) query.getResultList();
		
		List<ViewLegalHistoryDTO> searchClaimHistoryDTO = ViewLegalHistoryMapper.getInstance()
				.getViewClaimHistoryDTO(stageInformationList);
		
		for (ViewLegalHistoryDTO viewClaimHistoryDTO : searchClaimHistoryDTO) {

			if(viewClaimHistoryDTO.getUserID() != null){
				TmpEmployee employeeName = getEmployeeName(viewClaimHistoryDTO.getUserID());
				if(employeeName != null){
					viewClaimHistoryDTO.setUserName(employeeName.getEmpFirstName());
				}
			}
		}
		
		return searchClaimHistoryDTO;
	}
	
    public List<ViewLegalHistoryDTO> getClaimHistoryForReimbursement(Long claimKey, Long rodKey,Long ackStageKey){
		
    	Query query = entityManager.createNamedQuery("StageInformation.findByRodKey");
		query.setParameter("claimkey", claimKey);
		query.setParameter("rodKey", rodKey);
		query.setParameter("stageKey", ackStageKey);
		
		
		List<StageLegalInformation> stageInformationList = (List<StageLegalInformation>) query.getResultList();
		
		List<StageLegalInformation> finalListInformation = new ArrayList<StageLegalInformation>();
		
//		for (StageInformation stageInformation : stageInformationList) {
//			
//			if(stageInformation.getStage().getKey() != null && stageInformation.getStage().getKey().equals(ackStageKey)){
//				finalListInformation.add(stageInformation);
//			}
//		}
		
		List<ViewLegalHistoryDTO> searchClaimHistoryDTO = ViewLegalHistoryMapper.getInstance()
				.getViewClaimHistoryDTO(stageInformationList);
		
		List<DocAcknowledgement> documentAcknowledgeByClaim = getDocumentAcknowledgeByClaim(claimKey);
	
		
		for (ViewLegalHistoryDTO viewClaimHistoryDTO : searchClaimHistoryDTO) {
			if(viewClaimHistoryDTO.getReimbursementKey() != null){
				Reimbursement reimbursementByKey = getReimbursementByKey(viewClaimHistoryDTO.getReimbursementKey());
				if(reimbursementByKey != null){
					viewClaimHistoryDTO.setReferenceNo(reimbursementByKey.getRodNumber());
				}
			}else{
				if(viewClaimHistoryDTO.getStatusID() != null && viewClaimHistoryDTO.getStatusID().equals(ReferenceTable.ACKNOWLEDGE_STATUS_KEY)){
					
					for (DocAcknowledgement docAcknowledgement : documentAcknowledgeByClaim) {
						if(viewClaimHistoryDTO.getCreatedDate() != null && docAcknowledgement.getCreatedDate().compareTo(viewClaimHistoryDTO.getCreatedDate()) == 0){
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
				}
			}
		}
		
		return searchClaimHistoryDTO;
	}
    
	public List<DocAcknowledgement> getDocumentAcknowledgeByClaim(Long claimKey)
	{
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findByClaimKey");
		query.setParameter("claimkey", claimKey);
		List<DocAcknowledgement> reimbursementDetails = query.getResultList();
//		List<DocAcknowledgement> docAckList = new ArrayList<DocAcknowledgement>();
//		for (DocAcknowledgement docAcknowledgement : reimbursementDetails) {
//			entityManager.refresh(docAcknowledgement);
//			docAckList.add(docAcknowledgement);
//		}
		return reimbursementDetails ;
	}
    
	@SuppressWarnings("unchecked")
	public Reimbursement getReimbursementByKey(Long rodKey) {
		Query query = entityManager.createNamedQuery("Reimbursement.findByKey")
				.setParameter("primaryKey", rodKey);
		List<Reimbursement> rodList = query.getResultList();

		if (rodList != null && !rodList.isEmpty()) {
			for (Reimbursement reimbursement : rodList) {
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
	
}
