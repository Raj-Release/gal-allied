package com.shaic.paclaim.manageclaim.reopenclaim.pageClaimLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.FieldVisitRequestService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.CloseClaim;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.starfax.simulation.StarFaxFVRServiceRevised;


@ViewInterface(PAReopenClaimView.class)
public class PAReopenClaimPresenter extends AbstractMVPPresenter<PAReopenClaimView>{
	
	public static final String SUBMIT_REOPEN_CLAIM = "submit reopen claim PA";
	
	@EJB
	public CreateRODService createRodService;
	
	@EJB
	public ClaimService claimService;
	
	@EJB
	public ReimbursementService reimbursementService;
	
	@EJB
	public DBCalculationService dbCalculationService;
	
	@EJB
	public StarFaxFVRServiceRevised starfaxFvrServiceRevised;
	
	@EJB
	private FieldVisitRequestService fieldVisitService;
	
	@EJB
	public PreauthService preauthService;
	
	
	protected void submitCloseClaim(
			@Observes @CDIEvent(SUBMIT_REOPEN_CLAIM) final ParameterDTO parameters){
		
		PAReopenClaimPageDTO bean = (PAReopenClaimPageDTO)parameters.getPrimaryParameter();
		
		
		Claim claim = claimService.getClaimByClaimKey(bean.getClaimKey());
		
		Map<Long, Double> provisionMap = new HashMap<Long, Double>();
		
		List<ViewDocumentDetailsDTO> rodDocumentDetailsList = bean.getRodDocumentDetailsList();
		for (ViewDocumentDetailsDTO viewDocumentDetailsDTO : rodDocumentDetailsList) {
			if(viewDocumentDetailsDTO.getReimbursementKey() != null){
				provisionMap.put(viewDocumentDetailsDTO.getReimbursementKey(), viewDocumentDetailsDTO.getApprovedAmount());
			}
		}      
		
		List<Map<String, Object>> dbTaskForCloseClaim = preauthService.getDBTaskForCloseClaim(bean.getIntimationNumber(), SHAConstants.CLOSE_CLAIM_CURRENT_Q);
		
		List<Reimbursement> reimbursementList = new ArrayList<Reimbursement>();
		
		for (Map<String, Object> map : dbTaskForCloseClaim) {
			
			//Long reimbursementKey = (Long)map.get(SHAConstants.PAYLOAD_ROD_KEY);
			
			Long reimbursementKey=null;
			Long claimkey=(Long)map.get(SHAConstants.DB_CLAIM_KEY);
			
			if(claimkey!=null){
				List<Reimbursement> reimbursementByKey=createRodService.getReimbursementByClaimKey(claimkey);
				if(reimbursementByKey!=null && !reimbursementByKey.isEmpty()&&reimbursementByKey.size()==1
						&&reimbursementByKey.get(0).getStatus()!=null&&ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursementByKey.get(0).getStatus().getKey())){
				 reimbursementKey = reimbursementByKey.get(0).getKey();
					
				}
			}else{
				reimbursementKey = (Long)map.get(SHAConstants.PAYLOAD_ROD_KEY);
			}
			
			if(reimbursementKey != null && ! reimbursementKey.equals(0l)){
			
				
				Reimbursement reimbursementByKey = createRodService.getReimbursementByKey(reimbursementKey);
				
				if(reimbursementByKey != null){
				
				CloseClaim alreadyRodClosed = createRodService.getAlreadyRodClosed(reimbursementByKey.getKey());
				
	            if(alreadyRodClosed == null || (alreadyRodClosed != null && ! ReferenceTable.getCloseClaimKeys().containsKey(alreadyRodClosed.getStatus().getKey()))){
					reimbursementList.add(reimbursementByKey);

	            }
				}

			}
		}
		
//		createRodService.updateReimbursementReopenClaim(bean.getUserName(), reimbursementList);
		
		for (Reimbursement reimbursement : reimbursementList) {
			//IMSSUPPOR-37576 rejection rod need stop close
	    	if(!ReferenceTable.getRejectedRODKeys().containsKey(reimbursement.getStatus().getKey())){
			
			Reimbursement hospitalizationRod = reimbursementService.getHospitalizationRod(reimbursement.getClaim().getKey(), reimbursement.getKey());
			
			if(reimbursement.getClaim() != null && reimbursement.getClaim().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
				
				if(hospitalizationRod != null){
//					dbCalculationService.reimbursementRollBackProc(hospitalizationRod.getKey(),"R");
				}else{
					Preauth latestPreauth = reimbursementService.getLatestPreauthByClaim(reimbursement.getClaim().getKey());
//					dbCalculationService.reimbursementRollBackProc(latestPreauth.getKey(), "C");
				}
			}else{
				if(hospitalizationRod != null){
//					dbCalculationService.reimbursementRollBackProc(hospitalizationRod.getKey(),"R");
				}
			}
			
			Double provisionAmount = provisionMap.get(reimbursement.getKey());	
			reimbursement.setCurrentProvisionAmt(provisionAmount);
			
			createRodService.updateReimbursementReopenClaim(bean.getUserName(), reimbursement);
			
			 if(reimbursement.getStage().getKey().equals(ReferenceTable.CLAIM_REQUEST_STAGE)
					 ||reimbursement.getStage().getKey().equals(ReferenceTable.BILLING_STAGE)
					 ||reimbursement.getStage().getKey().equals(ReferenceTable.FINANCIAL_STAGE)){
		    		
				 dbCalculationService.invokeReimbursementAccumulatorProcedure(reimbursement.getKey());
		    	}
			
			dbCalculationService.updateProvisionAmountForPANonHealth(reimbursement.getKey(), reimbursement.getClaim().getKey());
			
		}
	}
		
		createRodService.submitReopenPAClaim(bean);
		
		for (Map<String, Object> map : dbTaskForCloseClaim) {
			
			Long wrkFlowKey = (Long) map.get(SHAConstants.WK_KEY);
			String IntimationNo = (String)map.get(SHAConstants.INTIMATION_NO);
		   	
		   	dbCalculationService.submitProcedureReopenClaim(wrkFlowKey, IntimationNo,"claimshead");
			
//			String previousQ = (String) map.get(SHAConstants)
			
			
		}
        view.result();
	}
	
	public void releaseHumanTask(Integer taskNumber){
		
		

 		if(taskNumber != null){
 			//BPMClientContext.setActiveOrDeactiveClaim("claimshead",BPMClientContext.BPMN_PASSWORD, taskNumber, SHAConstants.SYS_RELEASE);
 		}
	}
	
	
	public void submitFvrReplyTask(String intimationNumber){/*
		
      List<HumanTask> reimbursementFVRTask = starfaxFvrServiceRevised.getReimbursementFVRTask(intimationNumber);
      for (HumanTask humanTask : reimbursementFVRTask) {
		if(humanTask.getPayload() != null && humanTask.getPayload().getFieldVisit() != null && 
				humanTask.getPayload().getFieldVisit().getKey() != null){
			
			FieldVisitRequest fieldVisitByKey = fieldVisitService.getFieldVisitByKey(humanTask.getPayload().getFieldVisit().getKey());
			
			if(fieldVisitByKey != null && fieldVisitByKey.getTransactionFlag() != null && fieldVisitByKey.getTransactionFlag().equalsIgnoreCase("R")){
				
				if(fieldVisitByKey.getStatus().getKey().equals(ReferenceTable.FVR_REPLY_RECEIVED)){

					SubmitFVRReplyTask submitFVRReplyTask = BPMClientContext.getReimbSubmitFVRReplyDocTask(BPMClientContext.BPMN_TASK_USER, "Star@123");
					humanTask.setOutcome("APPROVE");
					
					com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType payload = humanTask.getPayload();
					if(payload != null && payload.getClassification() != null){
						com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.classification.ClassificationType classification = payload.getClassification();
							classification.setSource(fieldVisitByKey.getStatus() .getProcessValue());
							payload.setClassification(classification);
							humanTask.setPayload(payload);
                            try {
								submitFVRReplyTask.execute(BPMClientContext.BPMN_TASK_USER, humanTask);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					}
	
				}
				
			}
		}
	}
		
	*/}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
