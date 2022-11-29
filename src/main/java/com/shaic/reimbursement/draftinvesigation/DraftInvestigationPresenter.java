package com.shaic.reimbursement.draftinvesigation;

import java.sql.Timestamp;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.Investigation;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(DraftInvestigationWizard.class)
public class DraftInvestigationPresenter extends
		AbstractMVPPresenter<DraftInvestigationWizard> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String DRAFT_INVESTIGATION_SUBMIT = "Draft Investigation Submit For Reimbursement";

	@Inject
	private InvestigationService investigationService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	public static final String VALIDATE_DRAFT_INVESTIGATION_USER_RRC_REQUEST = "validate_draft_investigation_user_rrc_request";
	
	public static final String PROCESS_DRAFT_INVESTIGATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "draft_investigation_load_rrc_request_drop_down_values";

	public static final String PROCESS_DRAFT_INVESTIGATION_SAVE_RRC_REQUEST_VALUES = "draft_investigation_save_rrc_request_values";

	public static final String PROCESS_DRAFT_INVESTIGATION_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "process_draft_investigation_load_rrc_request_sub_category_values";

	public static final String PROCESS_DRAFT_INVESTIGATION_LOAD_RRC_REQUEST_SOURCE_VALUES = "process_draft_investigation_load_rrc_request_source_values";

	
	
	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_DRAFT_INVESTIGATION_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	

	public void saveRRCRequestValues(@Observes @CDIEvent(PROCESS_DRAFT_INVESTIGATION_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_REIMBURSEMENT);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(PROCESS_DRAFT_INVESTIGATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
			BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
			view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	/**
	 * Added for RRC Ends
	 * */
	

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}

	public void submitWizard(
			@Observes @CDIEvent(DRAFT_INVESTIGATION_SUBMIT) final ParameterDTO parameters) {
		DraftInvestigatorDto bean = (DraftInvestigatorDto) parameters
				.getPrimaryParameter();
		Investigation investigation = investigationService
				.getByInvestigationKey(bean.getKey());
		/*TmpInvestigation tmpInvestigation = investigationService
				.getRepresentativeListByInvestigationKey(bean
						.getInvestigatorNameListSelectValue().getId());*/
		if(investigation!=null){
			/*investigation.setInvestigatorCode(tmpInvestigation
			/*investigation.setInvestigatorName(tmpInvestigation
					.getInvestigatorName());*/
			/*Date date = new Date();
			investigation.setAssignedDate(date);*/
			if(bean.getAllocationToSelectValue() != null){
				SelectValue allocationToSelectValue = bean.getAllocationToSelectValue();
				MastersValue allocationTo = new MastersValue();
				allocationTo.setKey(allocationToSelectValue.getId());
				allocationTo.setValue(allocationToSelectValue.getValue());
				investigation.setAllocationTo(allocationTo);
			}
			if (bean.getReasonForInitiatingInvestSelectValue() != null) {
				
				SelectValue reasonForIniInvtSelectValue = bean.getReasonForInitiatingInvestSelectValue();
				MastersValue reasonforIniInv = new MastersValue();
				reasonforIniInv.setKey(reasonForIniInvtSelectValue.getId());
				reasonforIniInv.setValue(reasonForIniInvtSelectValue.getValue());
				investigation.setReasonForInitiatingInv(reasonforIniInv);
			}
			if (bean.getReasonForRefering() != null) {

				investigation.setReasonForReferring(bean.getReasonForRefering());
			}
			investigation.setClaimBackgroundDetails(bean.getClaimBackgroundDetails());
			investigation.setFactsOfCase(bean.getInvestigationTriggerPoints());
			investigation.setUhidIpNo(bean.getUhidIpNo()!=null?bean.getUhidIpNo():"");
			
			if(bean.getReimbReqBy() !=null && !bean.getReimbReqBy().isEmpty()){
				if(bean.getReimbReqBy().equalsIgnoreCase(SHAConstants.FA_CURRENT_QUEUE) || bean.getReimbReqBy().equalsIgnoreCase(SHAConstants.MA_CURRENT_QUEUE)){
				investigation.setTransactionFlag(SHAConstants.REIMBURSEMENT_CHAR);
				}
			}else{
				investigation.setTransactionFlag(SHAConstants.CASHLESS_CHAR);
			}
			
			Stage stage = preauthService.getStageByKey(ReferenceTable.INVESTIGATION_STAGE);
			Status status = preauthService.getStatusByPreauth(ReferenceTable.DRAFT_INVESTIGATION);
			investigation.setStage(stage);
			investigation.setStatus(status);
			investigation.setDraftedDate(new Timestamp(System.currentTimeMillis()));
			investigation.setDraftLetterBy(bean.getUserName());
			String docToken = investigationService.generateDraftLetter(bean);
			investigation.setToken(docToken);
			Investigation investigationResult = investigationService.updateIvestigation(investigation,SHAConstants.DRAFT_INVESTIGATION,bean.getUserName());
			
			investigationService.updateReimbursementStatus(bean.getRodKey(), status, stage, bean.getUserName());
			
			
			if(bean.getTriggerPointsList() != null && !bean.getTriggerPointsList().isEmpty()){
				investigationService.submitRODInvestigationDetails(bean.getTriggerPointsList(), investigation, bean);
			}
			
			investigationService.submitDraftInvestigation(investigationResult, bean);
			
			if(investigationResult != null && ! bean.getIsCashless()){
				//investigationService.setBPMOutcomeForAssign(investigationResult, bean);
			}else if(investigationResult != null && bean.getIsCashless()){
				//investigationService.submitInvestigationBPMForPreauth(investigationResult,bean);
			}
			
//			if(result){
				view.buildSuccessLayout();
//			}
		}
		
		//System.out.println(bean.getAllocationToValue());
	}
	
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(PROCESS_DRAFT_INVESTIGATION_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(PROCESS_DRAFT_INVESTIGATION_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}

}
