package com.shaic.claim.claimhistory.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.ClaimRemarksAlerts;
import com.shaic.ClaimRemarksAlerts;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.AuditTrails;
import com.shaic.claim.pcc.hrmp.HRMStageInformation;
import com.shaic.domain.Claim;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.preauth.OPStageInformation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.StageInformation;

@Stateless
public class ViewClaimHistoryService {
	
	
	@PersistenceContext
	protected EntityManager entityManager;

	public ViewClaimHistoryService() {
		super();
	}

	public List<ViewClaimHistoryDTO> getClaimHistoryForCashless(Long claimKey){
		
		Query query = entityManager.createNamedQuery("StageInformation.findByClaimKey");
		query.setParameter("claimkey", claimKey);
//		query.setParameter("preauthKey", preauthkey);
		List<StageInformation> stageInformationList = (List<StageInformation>) query.getResultList();
		
		List<ViewClaimHistoryDTO> searchClaimHistoryDTO = ViewClaimHistoryMapper.getInstance()
				.getViewClaimHistoryDTO(stageInformationList);
		
		for (ViewClaimHistoryDTO viewClaimHistoryDTO : searchClaimHistoryDTO) {
			
			if(viewClaimHistoryDTO.getCashlessKey() != null){
				Preauth preauthById = getPreauthById(viewClaimHistoryDTO.getCashlessKey());
				if(preauthById != null){
					if(preauthById.getEnhancementType() != null){
						viewClaimHistoryDTO.setReferenceNo(preauthById.getPreauthId() + "- Enhancement");
					}else{
						viewClaimHistoryDTO.setReferenceNo(preauthById.getPreauthId() + "- Preauth");
					}
				}
				viewClaimHistoryDTO.setDocrecdfrom(SHAConstants.HOSPITAL);
				viewClaimHistoryDTO.setRodtype(SHAConstants.NOT_APPLICABLE);
				viewClaimHistoryDTO.setClassification(SHAConstants.NOT_APPLICABLE);
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
	
public List<ViewClaimHistoryDTO> getOPClaimHistoryForCashless(Long claimKey){
		
		Query query = entityManager.createNamedQuery("OPStageInformation.findAllByIntimationKey");
		query.setParameter("intimationKey", claimKey);
//		query.setParameter("preauthKey", preauthkey);
		List<OPStageInformation> stageInformationList = (List<OPStageInformation>) query.getResultList();
		
		List<ViewClaimHistoryDTO> searchClaimHistoryDTO = ViewOPClaimHistoryMapper.getInstance().getViewClaimHistoryDTO(stageInformationList);
		
		for (ViewClaimHistoryDTO viewClaimHistoryDTO : searchClaimHistoryDTO) {
			
			if(viewClaimHistoryDTO.getCashlessKey() != null){
				Preauth preauthById = getPreauthById(viewClaimHistoryDTO.getCashlessKey());
				if(preauthById != null){
					if(preauthById.getEnhancementType() != null){
						viewClaimHistoryDTO.setReferenceNo(preauthById.getPreauthId() + "- Enhancement");
					}else{
						viewClaimHistoryDTO.setReferenceNo(preauthById.getPreauthId() + "- Preauth");
					}
				}
				viewClaimHistoryDTO.setDocrecdfrom(SHAConstants.HOSPITAL);
				viewClaimHistoryDTO.setRodtype(SHAConstants.NOT_APPLICABLE);
				viewClaimHistoryDTO.setClassification(SHAConstants.NOT_APPLICABLE);
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
	
public List<ViewClaimHistoryDTO> getClaimHistoryForOPHealthCheckUp(Long claimKey){
		
		Query query = entityManager.createNamedQuery("OPStageInformation.findAllByClaimKey");
		query.setParameter("claimkey", claimKey);
//		query.setParameter("preauthKey", preauthkey);
		List<OPStageInformation> stageInformationList = (List<OPStageInformation>) query.getResultList();
		
		List<ViewClaimHistoryDTO> searchClaimHistoryDTO = ViewOPClaimHistoryMapper.getInstance().getViewClaimHistoryDTO(stageInformationList);
		
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
	
    public List<ViewClaimHistoryDTO> getClaimHistoryForReimbursement(Long claimKey, Long rodKey,Long ackStageKey){
		
    	Query query = entityManager.createNamedQuery("StageInformation.findByRodKey");
		query.setParameter("claimkey", claimKey);
		query.setParameter("rodKey", rodKey);
		query.setParameter("stageKey", ackStageKey);
		
		
		List<StageInformation> stageInformationList = (List<StageInformation>) query.getResultList();
		
		//List<StageInformation> finalListInformation = new ArrayList<StageInformation>();
		
//		for (StageInformation stageInformation : stageInformationList) {
//			
//			if(stageInformation.getStage().getKey() != null && stageInformation.getStage().getKey().equals(ackStageKey)){
//				finalListInformation.add(stageInformation);
//			}
//		}
		
		List<ViewClaimHistoryDTO> searchClaimHistoryDTO = ViewClaimHistoryMapper.getInstance()
				.getViewClaimHistoryDTO(stageInformationList);
		
		List<DocAcknowledgement> documentAcknowledgeByClaim = getDocumentAcknowledgeByClaim(claimKey);
	
		
		for (ViewClaimHistoryDTO viewClaimHistoryDTO : searchClaimHistoryDTO) {
			if(viewClaimHistoryDTO.getReimbursementKey() != null){
				Reimbursement reimbursementByKey = getReimbursementByKey(viewClaimHistoryDTO.getReimbursementKey());
				if(reimbursementByKey != null){
					viewClaimHistoryDTO.setReferenceNo(reimbursementByKey.getRodNumber());
				}
				
				viewClaimHistoryDTO.setDocrecdfrom((reimbursementByKey.getDocAcknowLedgement() != null 
						&& reimbursementByKey.getDocAcknowLedgement().getDocumentReceivedFromId() != null 
						&& reimbursementByKey.getDocAcknowLedgement().getDocumentReceivedFromId().getValue() != null) ? reimbursementByKey.getDocAcknowLedgement().getDocumentReceivedFromId().getValue() : "");
				
				viewClaimHistoryDTO.setRodtype((reimbursementByKey.getReconsiderationRequest() != null 
						&& !reimbursementByKey.getReconsiderationRequest().equalsIgnoreCase(SHAConstants.YES_FLAG)) ? SHAConstants.ORIGINAL : (SHAConstants.RECONSIDERATION) + (reimbursementByKey.getVersion() != null && reimbursementByKey.getVersion().intValue() > 1 ? reimbursementByKey.getVersion().intValue() - 1 : ""));
				
				if(reimbursementByKey.getReconsiderationRequest() == null){
					viewClaimHistoryDTO.setRodtype(SHAConstants.ORIGINAL);
				}
				
				viewClaimHistoryDTO.setBillClassif((reimbursementByKey.getDocAcknowLedgement() != null 
						&& reimbursementByKey.getDocAcknowLedgement().getHospitalisationFlag() != null 
						&& reimbursementByKey.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)) ? SHAConstants.HOSPITALIZATION : SHAConstants.PARTIAL);
				
				if(reimbursementByKey.getDocAcknowLedgement() != null){
					String billClassificationValue = getBillClassificationValue(reimbursementByKey.getDocAcknowLedgement());
					if(billClassificationValue != null && reimbursementByKey.getDocAcknowLedgement().getBenifitFlag() != null){
						billClassificationValue += ","+reimbursementByKey.getDocAcknowLedgement().getBenifitFlag();
					}else if(reimbursementByKey.getDocAcknowLedgement().getBenifitFlag() != null){
						billClassificationValue = reimbursementByKey.getDocAcknowLedgement().getBenifitFlag();
					}
					viewClaimHistoryDTO.setBillClassif(billClassificationValue);
				}
				
			}else{
				if(viewClaimHistoryDTO.getStatusID() != null && viewClaimHistoryDTO.getStatusID().equals(ReferenceTable.ACKNOWLEDGE_STATUS_KEY)){
					if(documentAcknowledgeByClaim != null && !documentAcknowledgeByClaim.isEmpty()){
						
						viewClaimHistoryDTO.setDocrecdfrom((documentAcknowledgeByClaim.get(documentAcknowledgeByClaim.size()-1)  != null 
								&& documentAcknowledgeByClaim.get(documentAcknowledgeByClaim.size()-1).getDocumentReceivedFromId() != null 
								&& documentAcknowledgeByClaim.get(documentAcknowledgeByClaim.size()-1).getDocumentReceivedFromId().getValue() != null) ? documentAcknowledgeByClaim.get(documentAcknowledgeByClaim.size()-1).getDocumentReceivedFromId().getValue() : "");
						
						viewClaimHistoryDTO.setRodtype((documentAcknowledgeByClaim.get(documentAcknowledgeByClaim.size()-1) != null 
								&& documentAcknowledgeByClaim.get(documentAcknowledgeByClaim.size()-1).getReconsiderationRequest() != null 
								&& !documentAcknowledgeByClaim.get(documentAcknowledgeByClaim.size()-1).getReconsiderationRequest().equalsIgnoreCase(SHAConstants.YES_FLAG)) ? SHAConstants.ORIGINAL : SHAConstants.RECONSIDERATION);
						
						
						viewClaimHistoryDTO.setBillClassif((documentAcknowledgeByClaim.get(documentAcknowledgeByClaim.size()-1) != null 
								&& documentAcknowledgeByClaim.get(documentAcknowledgeByClaim.size()-1).getHospitalisationFlag() != null 
								&& documentAcknowledgeByClaim.get(documentAcknowledgeByClaim.size()-1).getHospitalisationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)) ? SHAConstants.HOSPITALIZATION : SHAConstants.PARTIAL);
						

						if(documentAcknowledgeByClaim.get(documentAcknowledgeByClaim.size()-1) != null){
							String billClassificationValue = getBillClassificationValue(documentAcknowledgeByClaim.get(documentAcknowledgeByClaim.size()-1));
							if(billClassificationValue != null && documentAcknowledgeByClaim.get(documentAcknowledgeByClaim.size()-1).getBenifitFlag() != null){
								billClassificationValue += ","+documentAcknowledgeByClaim.get(documentAcknowledgeByClaim.size()-1).getBenifitFlag();
							}else if(documentAcknowledgeByClaim.get(documentAcknowledgeByClaim.size()-1).getBenifitFlag() != null){
								billClassificationValue = documentAcknowledgeByClaim.get(documentAcknowledgeByClaim.size()-1).getBenifitFlag();
							}
							viewClaimHistoryDTO.setBillClassif(billClassificationValue);
						}
						
						for (DocAcknowledgement docAcknowledgement : documentAcknowledgeByClaim) {
							if(viewClaimHistoryDTO.getCreatedDate() != null && docAcknowledgement.getCreatedDate().compareTo(viewClaimHistoryDTO.getCreatedDate()) == 0){
								viewClaimHistoryDTO.setReferenceNo(docAcknowledgement.getAcknowledgeNumber() + " - Acknowledgement");
								break;
							}
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
	
	 @SuppressWarnings("unchecked")
     private TmpEmployee getEmployeeName(String initiatorId)
         {
              List<TmpEmployee> fvrInitiatorDetail;
              Query findByTransactionKey = entityManager.createNamedQuery(
                                   "TmpEmployee.getEmpByLoginId").setParameter("loginId", initiatorId.toLowerCase());
                  try{
                         fvrInitiatorDetail =(List<TmpEmployee>) findByTransactionKey.getResultList();
                         if(fvrInitiatorDetail != null && ! fvrInitiatorDetail.isEmpty()) {
                                 return fvrInitiatorDetail.get(0);
                          }
                   }
             catch(Exception e)
             {
                  return null;
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
	  
	  public List<ViewClaimHistoryDTO> ViewTrailsForCashlessAndReimbursementHistory(Intimation intimation){
			
			List<Claim> claimByIntimation = getClaimByIntimation(intimation.getKey());
			List<ViewClaimHistoryDTO> filterDTO = new ArrayList<ViewClaimHistoryDTO>();
			if(claimByIntimation != null && ! claimByIntimation.isEmpty()){
				
			Claim claim = claimByIntimation.get(0);
			
			List<ViewClaimHistoryDTO> tableList = new ArrayList<ViewClaimHistoryDTO>();
			
			List<ViewClaimHistoryDTO> claimHistoryForCashless = getClaimHistoryForCashless(claim.getKey());
			tableList.addAll(claimHistoryForCashless);
			
			List<Reimbursement> reimbursementDetails = getReimbursementDetails(claim.getKey());

			for (Reimbursement reimbursement : reimbursementDetails) {
				
				List<ViewClaimHistoryDTO> claimHistoryForReimbursement = getClaimHistoryForReimbursement(claim.getKey(), reimbursement.getKey(),ReferenceTable.ACKNOWLEDGE_STAGE_KEY);

				tableList.addAll(claimHistoryForReimbursement);
				
			}
			try{
				if(reimbursementDetails == null || reimbursementDetails.isEmpty()){
					List<ViewClaimHistoryDTO> claimHistoryForReimbursement = getClaimHistoryForReimbursement(claim.getKey(), 0l,ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
					tableList.addAll(claimHistoryForReimbursement);
				}
			}catch(Exception e){
				
			}
			
			
			List<Long> duplicateKeys = new ArrayList<Long>();
			
			for (ViewClaimHistoryDTO viewClaimHistoryDTO : tableList) {
				
				if(! duplicateKeys.contains(viewClaimHistoryDTO.getHistoryKey())){
					filterDTO.add(viewClaimHistoryDTO);
				}
				
				duplicateKeys.add(viewClaimHistoryDTO.getHistoryKey());
			}
			
			}
			
			return filterDTO;
		}
	
	  public List<Reimbursement> getReimbursementDetails(Long claimKey) {
			Query query = entityManager
					.createNamedQuery("Reimbursement.findByClaimKey");
			query.setParameter("claimKey", claimKey);
			List<Reimbursement> reimbursementDetails = query.getResultList();
			if (null != reimbursementDetails && !reimbursementDetails.isEmpty()) {
				for (Reimbursement reimbursement : reimbursementDetails) {
					entityManager.refresh(reimbursement);
				}

			}
			return reimbursementDetails;
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
	  
	  private String getBillClassificationValue(DocAcknowledgement docAck) {
			StringBuilder strBuilder = new StringBuilder();
			// StringBuilder amtBuilder = new StringBuilder();
			// Double total = 0d;
			try {
				if (("Y").equals(docAck.getHospitalisationFlag())) {
					strBuilder.append("Hospitalization");
					strBuilder.append(",");
				}
				if (("Y").equals(docAck.getPreHospitalisationFlag())) {
					strBuilder.append("Pre-Hospitalization");
					strBuilder.append(",");
				}
				if (("Y").equals(docAck.getPostHospitalisationFlag())) {
					strBuilder.append("Post-Hospitalization");
					strBuilder.append(",");
				}

				if (("Y").equals(docAck.getPartialHospitalisationFlag())) {
					strBuilder.append("Partial-Hospitalization");
					strBuilder.append(",");
				}

				if (("Y").equals(docAck.getLumpsumAmountFlag())) {
					strBuilder.append("Lumpsum Amount");
					strBuilder.append(",");

				}
				if (("Y").equals(docAck.getHospitalCashFlag())) {
					strBuilder.append("Add on Benefits (Hospital cash)");
					strBuilder.append(",");

				}
				if (("Y").equals(docAck.getPatientCareFlag())) {
					strBuilder.append("Add on Benefits (Patient Care)");
					strBuilder.append(",");
				}
				if (("Y").equals(docAck.getHospitalizationRepeatFlag())) {
					strBuilder.append("Hospitalization Repeat");
					strBuilder.append(",");
				}
				
				if (("Y").equals(docAck.getCompassionateTravel())) {
					strBuilder.append("Compassionate Travel");
					strBuilder.append(",");
				}
				
				if (("Y").equals(docAck.getRepatriationOfMortalRemain())) {
					strBuilder.append("Repatriation Of Mortal Remains");
					strBuilder.append(",");
				}
				
				if(null != docAck.getClaim()&& docAck.getClaim().getIntimation() != null && docAck.getClaim().getIntimation().getPolicy() != null &&
						(ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(docAck.getClaim().getIntimation().getPolicy().getProduct().getKey())
								|| ReferenceTable.getValuableServiceProviderForFHO().containsKey(docAck.getClaim().getIntimation().getPolicy().getProduct().getKey()))){
					if (("Y").equals(docAck.getPreferredNetworkHospita())) {
						strBuilder.append("Valuable Service Provider (Hospital)");
						strBuilder.append(",");
					}
				}
				else{ 
					if (("Y").equals(docAck.getPreferredNetworkHospita())) {
						strBuilder.append("Preferred Network Hospital");
						strBuilder.append(",");
					}
				}
				
				if (("Y").equals(docAck.getSharedAccomodation())) {
					strBuilder.append("Shared Accomodation");
					strBuilder.append(",");
				}
				
				if (("Y").equals(docAck.getEmergencyMedicalEvaluation())) {
					strBuilder.append("Emergency Medical Evacuation");
					strBuilder.append(",");
				}
				
				//added for new product076
				if (("Y").equals(docAck.getProdHospBenefitFlag())) {	
				strBuilder.append("Hospital Cash");
				}
				if (("Y").equals(docAck.getStarWomenCare())) {
					strBuilder.append("Star Mother Cover");
					strBuilder.append(",");
				}
				
				// rodQueryDTO.setClaimedAmount(total);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return strBuilder.toString();
		}
	  
	  public List<ViewClaimHistoryDTO> getClaimHistoryForClaimAudit(Long intimationKey){
			
			Query query = entityManager.createNamedQuery("AuditTrails.findByIntimationKey");
			query.setParameter("intimationKey", intimationKey);
			List<AuditTrails> auditList = (List<AuditTrails>) query.getResultList();
			if(auditList != null && ! auditList.isEmpty()){
			List<ViewClaimHistoryDTO> searchClaimHistoryDTOList = new ArrayList<ViewClaimHistoryDTO>();
			ViewClaimHistoryDTO viewClaimHistoryDTO = null;
			for (AuditTrails auditTrails : auditList) {

				viewClaimHistoryDTO = new ViewClaimHistoryDTO();
				
				Preauth preauthById = getPreauthById(auditTrails.getTransactionKey());
				if(preauthById != null){
					
						viewClaimHistoryDTO.setReferenceNo(preauthById.getPreauthId());
					if(auditTrails.getCreatedBy() != null){
						TmpEmployee employeeName = getEmployeeName(auditTrails.getCreatedBy());
						if(employeeName != null){
							viewClaimHistoryDTO.setUserName(employeeName.getEmpFirstName());
						}
					}
					if(auditTrails.getCreatedDate() != null && auditTrails.getCreatedBy() != null){
					viewClaimHistoryDTO.setDateAndTime(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(auditTrails.getCreatedDate()));
					viewClaimHistoryDTO.setUserID(auditTrails.getCreatedBy());
					}
					viewClaimHistoryDTO.setStatus(auditTrails.getRemediationStatus());
					viewClaimHistoryDTO.setRemarks(auditTrails.getRemarks());
				}
				
				Reimbursement reimbursementByKey = getReimbursementByKey(auditTrails.getTransactionKey());
				if(reimbursementByKey != null){
					
						viewClaimHistoryDTO.setReferenceNo(reimbursementByKey.getRodNumber());
					if(auditTrails.getCreatedBy() != null){
						TmpEmployee employeeName = getEmployeeName(auditTrails.getCreatedBy());
						if(employeeName != null){
							viewClaimHistoryDTO.setUserName(employeeName.getEmpFirstName());
						}
					}
					if(auditTrails.getCreatedDate() != null && auditTrails.getCreatedBy() != null){
					viewClaimHistoryDTO.setDateAndTime(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(auditTrails.getCreatedDate()));
					viewClaimHistoryDTO.setUserID(auditTrails.getCreatedBy());
					}
					viewClaimHistoryDTO.setStatus(auditTrails.getRemediationStatus());
					viewClaimHistoryDTO.setRemarks(auditTrails.getRemarks());
				}
				viewClaimHistoryDTO.setQryRplRemarks(auditTrails.getQryRplRemarks() != null ? auditTrails.getQryRplRemarks() : "");
				viewClaimHistoryDTO.setQryRplDate(auditTrails.getQryRplDate() != null ? new SimpleDateFormat("dd-MM-yyyy HH:mm").format(auditTrails.getQryRplDate()) : "");
				if(auditTrails.getRplyUser() != null){
				TmpEmployee employeeName = getEmployeeName(auditTrails.getRplyUser());
					if(employeeName != null){
						viewClaimHistoryDTO.setRplyUser(auditTrails.getRplyUser() + (employeeName.getEmpFirstName() != null ? ("-"+employeeName.getEmpFirstName()) : ""));
					}
				}
				viewClaimHistoryDTO.setClaimsReply(auditTrails.getClaimsReply() != null ? auditTrails.getClaimsReply() : "");
				
				searchClaimHistoryDTOList.add(viewClaimHistoryDTO);
			}
			
			return searchClaimHistoryDTOList;
			}
			else{
				return null;
			}
		}
	  
	  public List<ViewClaimHistoryDTO> getClaimHistoryForClaimsAlert(Intimation intimation){
			
		  
			Query query = entityManager.createNamedQuery("ClaimRemarksAlerts.findByIntimationnoWithStatus");
			query.setParameter("intitmationNo", intimation.getIntimationId());

			List<ClaimRemarksAlerts> remarksAlerts = (List<ClaimRemarksAlerts>) query.getResultList();
			List<ViewClaimHistoryDTO> searchClaimHistoryDTOs = new ArrayList<ViewClaimHistoryDTO>();
			if(remarksAlerts !=null && !remarksAlerts.isEmpty() ){
				for (ClaimRemarksAlerts claimRemarksAlerts : remarksAlerts) {
					ViewClaimHistoryDTO viewClaimHistoryDTO = new ViewClaimHistoryDTO();
					viewClaimHistoryDTO.setTypeofClaim(intimation.getClaimType().getValue());
					viewClaimHistoryDTO.setReferenceNo(claimRemarksAlerts.getIntitmationNo());
					viewClaimHistoryDTO.setCreatedDate(claimRemarksAlerts.getCreatedDate());
					String header = "Claim Alert Category -" + claimRemarksAlerts.getAlertCategory().getValue() +"\n";
					viewClaimHistoryDTO.setUserRemark(header + claimRemarksAlerts.getRemarks());
					if(claimRemarksAlerts.getCreatedBy() != null){
						viewClaimHistoryDTO.setUserID(claimRemarksAlerts.getCreatedBy());
						TmpEmployee employeeName = getEmployeeName(claimRemarksAlerts.getCreatedBy());
						if(employeeName != null){
							viewClaimHistoryDTO.setUserName(employeeName.getEmpFirstName());
						}
					}
					viewClaimHistoryDTO.setDocrecdfrom(SHAConstants.NOT_APPLICABLE);
					viewClaimHistoryDTO.setRodtype(SHAConstants.NOT_APPLICABLE);
					viewClaimHistoryDTO.setClassification(SHAConstants.NOT_APPLICABLE);
					viewClaimHistoryDTO.setBillClassif(SHAConstants.NOT_APPLICABLE);
					viewClaimHistoryDTO.setClaimStage(SHAConstants.NOT_APPLICABLE);
					viewClaimHistoryDTO.setStatus(SHAConstants.NOT_APPLICABLE);
					viewClaimHistoryDTO.setHistoryKey(claimRemarksAlerts.getKey());
					searchClaimHistoryDTOs.add(viewClaimHistoryDTO);
				}
			}
			return searchClaimHistoryDTOs;
		}
	
	  public List<ViewClaimHistoryDTO> getClaimHistoryForHRMCashless(Long claimKey){

		  Query query = entityManager.createNamedQuery("HRMStageInformation.findByClaimKey");
		  query.setParameter("claimkey", claimKey);
		  //			query.setParameter("preauthKey", preauthkey);
		  List<HRMStageInformation> stageInformationList = (List<HRMStageInformation>) query.getResultList();

		  List<ViewClaimHistoryDTO> searchClaimHistoryDTO = ViewClaimHistoryMapper.getHRMInstance()
				  .getViewHRMClaimHistoryDTO(stageInformationList);

		  for (ViewClaimHistoryDTO viewClaimHistoryDTO : searchClaimHistoryDTO) {

			  if(viewClaimHistoryDTO.getCashlessKey() != null){
				  Preauth preauthById = getPreauthById(viewClaimHistoryDTO.getCashlessKey());
				  if(preauthById != null){
					  if(preauthById.getEnhancementType() != null){
						  viewClaimHistoryDTO.setReferenceNo(preauthById.getPreauthId() + "- Enhancement");
					  }else{
						  viewClaimHistoryDTO.setReferenceNo(preauthById.getPreauthId() + "- Preauth");
					  }
				  }
				  viewClaimHistoryDTO.setDocrecdfrom(SHAConstants.HOSPITAL);
				  viewClaimHistoryDTO.setRodtype(SHAConstants.NOT_APPLICABLE);
				  viewClaimHistoryDTO.setClassification(SHAConstants.NOT_APPLICABLE);
			  }

			  if(viewClaimHistoryDTO.getUserID() != null && !viewClaimHistoryDTO.getUserID().equalsIgnoreCase(SHAConstants.GALAXY_APP)){
				  TmpEmployee employeeName = getEmployeeName(viewClaimHistoryDTO.getUserID());
				  if(employeeName != null){
					  viewClaimHistoryDTO.setUserName(employeeName.getEmpFirstName());
				  }
			  }else if(viewClaimHistoryDTO.getUserID().equalsIgnoreCase(SHAConstants.GALAXY_APP)){
				  viewClaimHistoryDTO.setUserName(SHAConstants.GALAXY_APP);
			  }
		  }

		  return searchClaimHistoryDTO;
	  }
}
