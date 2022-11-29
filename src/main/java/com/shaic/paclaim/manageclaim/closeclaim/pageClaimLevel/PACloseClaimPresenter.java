package com.shaic.paclaim.manageclaim.closeclaim.pageClaimLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.Claim;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;

@ViewInterface(PACloseClaimView.class)
public class PACloseClaimPresenter extends AbstractMVPPresenter<PACloseClaimView> {
	
	
	
	public static final String UPLOAD_DOCUMENT_TABLE = "Upload Documents For Close Claim PA";
	
	public static final String SUBMIT_CLOSE_CLAIM = "Submit Close Claim PA";
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private CreateRODService createRodService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	protected void setTableValues(
			@Observes @CDIEvent(UPLOAD_DOCUMENT_TABLE) final ParameterDTO parameters){
		
		PACloseClaimPageDTO bean = (PACloseClaimPageDTO)parameters.getPrimaryParameter();
		
		PAUploadDocumentCloseClaimDTO uploadDocumentForCloseClaim = createRodService.uploadDocumentForPACloseClaim(bean);
		
		view.setUploadDocumentsDTO(uploadDocumentForCloseClaim);
		
	}
	
	protected void submitCloseClaim(
			@Observes @CDIEvent(SUBMIT_CLOSE_CLAIM) final ParameterDTO parameters){
		
		PACloseClaimPageDTO bean = (PACloseClaimPageDTO)parameters.getPrimaryParameter();
		
		 Claim claim = createRodService.getClaimByClaimKey(bean.getClaimKey());
		 
		 if(claim != null && claim.getStatus().getKey().equals(ReferenceTable.CLAIM_CLOSED_STATUS)){
			 view.alreadyClosed("Claim Already Closed");
		 }else{
		
//		CloseAllClaimTask closeClaimTask = BPMClientContext.getCloseClaimTask(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
		
//----------------------------------------------------Reimbursement Task -----------------------------------------------------------------------------
		/*CloseAllClaimTask closeClaimTask = BPMClientContext.getCloseClaimTask("claimshead", BPMClientContext.BPMN_PASSWORD);
		
		PayloadBOType payloadType = new PayloadBOType();
		payloadType = SHAUtils.getReimPayloadForDeathAcc(payloadType);
		IntimationType intimationType = new IntimationType();
		intimationType.setIntimationNumber(bean.getIntimationNumber());
		payloadType.setIntimation(intimationType);
		
		PagedTaskList tasks = closeClaimTask.getTasks("claimshead", null, payloadType);
		
		List<HumanTask> humanTasks = tasks.getHumanTasks();
		*/
			 
			 List<Reimbursement> reimbursementList = new ArrayList<Reimbursement>();
			 
			 List<Map<String, Object>> taskProcedureForCloseClaim = dbCalculationService.getTaskProcedureForCloseClaim(claim.getIntimation().getIntimationId());
			 
			 for (Map<String, Object> map : taskProcedureForCloseClaim) {
				 
				Long reimbursementKey = (Long)map.get(SHAConstants.PAYLOAD_ROD_KEY);
				
				if(reimbursementKey != null && ! reimbursementKey.equals(0l)){
				
					Reimbursement reimbursement = createRodService.getReimbursementByKey(reimbursementKey);
					reimbursementList.add(reimbursement);
					
				}
			}

		/*for (HumanTask humanTask : humanTasks) {
			
			if(humanTask.getPayload() != null && humanTask.getPayload().getRod() != null 
					&& humanTask.getPayload().getRod().getKey() != null){
				
				Long reimbursementKey = humanTask.getPayload().getRod().getKey();
				Reimbursement reimbursement = createRodService.getReimbursementByKey(reimbursementKey);
				reimbursementList.add(reimbursement);
			}
		}*/
			
			for (Reimbursement reimbursement : reimbursementList) {
				
				//IMSSUPPOR-37576 rejection rod need stop close
		    	if(!ReferenceTable.getRejectedRODKeys().containsKey(reimbursement.getStatus().getKey())){
				
				dbCalculationService.reimbursementRollBackProc(reimbursement.getKey(),"R");
				
				createRodService.updateReimbursmentForCloseClaimPANonHealth(bean.getUserName(), reimbursement);
				
				Reimbursement hospitalizationRod = reimbursementService.getHospitalizationRod(reimbursement.getClaim().getKey(), reimbursement.getKey());
				
				
				if(reimbursement.getStage().getKey().equals(ReferenceTable.BILLING_STAGE)
						|| reimbursement.getStage().getKey().equals(ReferenceTable.FINANCIAL_STAGE)){
					
					if(reimbursement.getClaim().getClaimType() != null && reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
						if(hospitalizationRod == null){
							if(reimbursement.getClaim().getLatestPreauthKey() != null){
//								dbCalculationService.invokeAccumulatorProcedure(reimbursement.getClaim().getLatestPreauthKey());
							}else{
								Preauth latestPreauth = reimbursementService.getLatestPreauthByClaim(reimbursement.getClaim().getKey());
//								dbCalculationService.invokeAccumulatorProcedure(latestPreauth.getKey());
							}
						}else{
//							dbCalculationService.invokeReimbursementAccumulatorProcedure(hospitalizationRod.getKey());
						}
					}else{
						if(hospitalizationRod != null){
//							dbCalculationService.invokeReimbursementAccumulatorProcedure(hospitalizationRod.getKey());
						}
					}
				}
				
				dbCalculationService.updateProvisionAmountForPANonHealth(reimbursement.getKey(), reimbursement.getClaim().getKey());
				
			}
		 }
			
			PremiaService.getInstance().UnLockPolicy(bean.getIntimationNumber());

		   createRodService.submitPACloseClaim(bean);
		   
		   dbCalculationService.stopReminderProcessProcedure(bean.getIntimationNumber(),SHAConstants.OTHERS);
		   
		   for (Map<String, Object> map : taskProcedureForCloseClaim) {
			   
			   	Long wrkFlowKey = (Long) map.get(SHAConstants.WK_KEY);
			   	String currentQ = (String)map.get(SHAConstants.CURRENT_Q);
			   	String outCome = currentQ+SHAConstants.CLOSE_CLAIM_CURRENT_Q;
			   	dbCalculationService.pullBackSubmitProcedure(wrkFlowKey, outCome, "claimshead");
			   	
			   }
		
		/*for (HumanTask humanTask : humanTasks) {
			
			if(humanTask.getPayloadCashless() != null && humanTask.getPayloadCashless().getPedReq() == null && humanTask.getPayloadCashless().getFieldVisit() == null){
				
				SystemActionsHumanTaskService task = BPMClientContext.getActiveAndDeactiveHumanTask("claimshead",BPMClientContext.BPMN_PASSWORD);
				
				BPMClientContext.setActiveOrDeactive(task, "claimshead", humanTask.getNumber(), SHAConstants.SUSPEND_HUMANTASK);
				
			}else if(humanTask.getPayload() != null && humanTask.getPayload().getDocReceiptACK() != null){

//				if(humanTask.getPayloadCashless() == null || (humanTask.getPayloadCashless() != null && humanTask.getPayloadCashless().getPedReq() == null
//						&& humanTask.getPayloadCashless().getFieldVisit() == null)){
				
					SystemActionsHumanTaskService task = BPMClientContext.getActiveAndDeactiveHumanTask("claimshead",BPMClientContext.BPMN_PASSWORD);
					BPMClientContext.setActiveOrDeactive(task, "claimshead", humanTask.getNumber(), SHAConstants.SUSPEND_HUMANTASK);
					
//				}
			}

		}*/
//------------------------------------------------------------------Reimbursement End Task-----------------------------------------------------------------------------------------------------

		/*com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType cashlessPayload = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType();
		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType cashlessIntimationType = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType();
		cashlessIntimationType.setIntimationNumber(bean.getIntimationNumber());
		cashlessPayload.setIntimation(cashlessIntimationType);
		
		PagedTaskList cashlessTask = closeClaimTask.getTasks("claimshead", null, cashlessPayload);
		
		List<HumanTask> cashlessHumanTask = cashlessTask.getHumanTasks();
		
		for (HumanTask humanTask : cashlessHumanTask) {
			
			if(humanTask.getPayloadCashless() != null && 
					(humanTask.getPayloadCashless().getPedReq() == null || (humanTask.getPayloadCashless().getPedReq() != null && humanTask.getPayloadCashless().getPedReq().getKey() == null))
					&& humanTask.getPayloadCashless().getFieldVisit() == null
					&& humanTask.getPayloadCashless().getInvestigation() == null){
				SystemActionsHumanTaskService task = BPMClientContext.getActiveAndDeactiveHumanTask("claimshead",BPMClientContext.BPMN_PASSWORD);
				BPMClientContext.setActiveOrDeactive(task, "claimshead", humanTask.getNumber(), SHAConstants.SUSPEND_HUMANTASK);
			}
		}*/
		
		
		view.result();
		 }
		
//		view.setUploadDocumentsDTO(uploadDocumentForCloseClaim);
		
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
