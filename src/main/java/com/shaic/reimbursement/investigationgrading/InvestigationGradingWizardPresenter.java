package com.shaic.reimbursement.investigationgrading;

import java.util.Date;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.Investigation;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.reimbursement.assigninvesigation.AssignInvestigatorDto;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(InvestigationGradingWizardView.class)
public class InvestigationGradingWizardPresenter extends
AbstractMVPPresenter<InvestigationGradingWizardView>{



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String INVESTIGATION_GRADING_SUBMIT = "Investigation Grading Submit For Reimbursement";

	@Inject
	private InvestigationService investigationService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	public static final String VALIDATE_ASSIGN_INVESTIGATION_USER_RRC_REQUEST = "validate_investigation_grading_user_rrc_request";
	
	public static final String PROCESS_ASSIGN_INVESTIGATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "investigation_grading_load_rrc_request_drop_down_values";
	
	public static final String PROCESS_ASSIGN_INVESTIGATION_SAVE_RRC_REQUEST_VALUES = "investigation_grading_save_rrc_request_values";
	
	
	
	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_ASSIGN_INVESTIGATION_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	

	public void saveRRCRequestValues(@Observes @CDIEvent(PROCESS_ASSIGN_INVESTIGATION_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_REIMBURSEMENT);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(PROCESS_ASSIGN_INVESTIGATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
			BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
			view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	/**
	 * Added for RRC Ends
	 * */
	

	@Override
	public void viewEntered() {
		

	}

	public void submitWizard(
			@Observes @CDIEvent(INVESTIGATION_GRADING_SUBMIT) final ParameterDTO parameters) {
		AssignInvestigatorDto bean = (AssignInvestigatorDto) parameters
				.getPrimaryParameter();
		Investigation investigation = investigationService
				.getByInvestigationKey(bean.getKey());
	/*	TmpInvestigation tmpInvestigation = investigationService
				.getRepresentativeListByInvestigationKey(bean
						.getInvestigatorNameListSelectValue().getId());*/
		if(investigation!=null /*&& tmpInvestigation!=null*/){
			/*investigation.setInvestigatorCode(tmpInvestigation
					.getInvestigatorCode());
			investigation.setInvestigatorName(tmpInvestigation
					.getInvestigatorName());*/
			/*Date date = new Date();
			investigation.setAssignedDate(date);
			if(bean.getAllocationToSelectValue() != null){
				SelectValue allocationToSelectValue = bean.getAllocationToSelectValue();
				MastersValue allocationTo = new MastersValue();
				allocationTo.setKey(allocationToSelectValue.getId());
				allocationTo.setValue(allocationToSelectValue.getValue());
				investigation.setAllocationTo(allocationTo);
			}*/
			/*investigation.setInvestigationTriggerPoints(bean.getInvestigationTriggerPoints());
			investigation.setGradingRemarks(bean.getGradingRemarks());
			investigation.setGradingCategory(bean.getGradingCategorySelectValue().getValue());*/
			
			investigation.setClaimBackgroundDetails(bean.getClaimBackgroundDetails());
			
			
			Map<String, Object> wrkFlowMap = (Map<String, Object>) bean.getDbOutArray();
			Long assignedInvestigKey = wrkFlowMap.get(SHAConstants.PAYLOAD_RRC_REQUEST_KEY) != null && ! wrkFlowMap.get(SHAConstants.PAYLOAD_RRC_REQUEST_KEY).toString().equalsIgnoreCase("null") ? Long.valueOf( String.valueOf(wrkFlowMap.get(SHAConstants.PAYLOAD_RRC_REQUEST_KEY))) : null;
			
			AssignedInvestigatiorDetails assignedInvestigation = investigationService.getAssignedInvestigByKey(assignedInvestigKey);
			
			Stage stage = preauthService.getStageByKey(ReferenceTable.INVESTIGATION_STAGE);
			Status status = preauthService.getStatusByPreauth(ReferenceTable.INVESTIGATION_GRADING);
			assignedInvestigation.setStage(stage);
			assignedInvestigation.setStatus(status);
			
			if(bean.getGradingCategorySelectValue() != null){
				assignedInvestigation.setGradingCategory(bean.getGradingCategorySelectValue().getValue());
			}
			if(bean.getGradingRemarks() != null){
				assignedInvestigation.setGradingRemarks(bean.getGradingRemarks());
			}
			assignedInvestigation.setGradingDate(new Date());
			assignedInvestigation.setGradedBy(SHAUtils.getUserNameForDB(bean.getUserName()));
			
			if(assignedInvestigation != null){
			
				investigationService.updateAssignedIvestigation(assignedInvestigation, SHAConstants.INVESTIGATION_GRADING, bean.getUserName(),investigation);
			}	
						
		//	investigationService.updateReimbursementStatus(bean.getRodKey(), status, stage, bean.getUserName());
			
			/*if(investigationResult != null && ! bean.getIsCashless()){
				investigationService.setBPMOutcomeForAssign(investigationResult, bean);
			}else if(investigationResult != null && bean.getIsCashless()){
				investigationService.submitInvestigationBPMForPreauth(investigationResult,bean);
			}*/
			
			investigationService.submitInvestigationGrade(assignedInvestigation.getInvestigation(),bean);
			
			
//			if(result){
				view.buildSuccessLayout();
//			}
		}
		
		System.out.println(bean.getAllocationToValue());
	}


}
