package com.shaic.reimbursement.processi_investigationi_initiated;

import java.sql.Timestamp;
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
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.ClaimService;
import com.shaic.domain.FieldVisitRequestService;
import com.shaic.domain.Investigation;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.processi_investigationi_initiated.search.SearchProcessInvestigationInitiatedTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.OptionGroup;

@ViewInterface(ProcessInvestigationInitiatedView.class)
public class ProcessInvestigationInitiatedPresenter extends
		AbstractMVPPresenter<ProcessInvestigationInitiatedView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public static final String SUBMIT_CLICK = "Submit Click";
	
	public static final String SET_REFERNCE = "Set Reference";

	public static final String CLAIM_DTO = "claim dto";

	public static final String INVESTIGATION_DTO = "investigation";

	public static final String INVESTIGATION_SIZE = "investigation size";
	
	public static final String FIELD_VISIT_RADIO_CHANGED = "field visit radio for investigation";

	@EJB
	private InvestigationService investigationService;

	@EJB
	private ClaimService claimService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private FieldVisitRequestService fvrService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	public static final String VALIDATE_PROCESS_INVESTIGATION_INTIATED_USER_RRC_REQUEST = "validate_process_investigation_intiated_user_rrc_request";
	
	public static final String PROCESS_INVESTIGATION_INITIATED_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "process_investigation_load_rrc_request_drop_down_values";
	
	public static final String PROCESS_INVESTIGATION_INTIATED_SAVE_RRC_REQUEST_VALUES = "process_investigation_save_rrc_request_values";
	
	public static final String VALIDATE_PROCESS_INVESTIGATION_INTIATED_USER_LUMEN_REQUEST = "validate_process_investigation_intiated_user_lumen_request";
	
	public static final String PROCESS_INVESTIGATION_INITIATED_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "process_investigation_initiated_load_rrc_request_sub_category_values";
	
	public static final String PROCESS_INVESTIGATION_INITIATED_LOAD_RRC_REQUEST_SOURCE_VALUES = "process_investigation_initiated_load_rrc_request_source_values";
	 

	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_PROCESS_INVESTIGATION_INTIATED_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	
	public void preauthLumenRequest(@Observes @CDIEvent(VALIDATE_PROCESS_INVESTIGATION_INTIATED_USER_LUMEN_REQUEST) final ParameterDTO parameters){
		Investigation investigation = (Investigation) parameters.getPrimaryParameter();
		view.buildInitiateLumenRequest(investigation.getIntimation().getIntimationId());
	}
	

	public void saveRRCRequestValues(@Observes @CDIEvent(PROCESS_INVESTIGATION_INTIATED_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_REIMBURSEMENT);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(PROCESS_INVESTIGATION_INITIATED_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	/**
	 * Added for RRC Ends
	 * */
	
	public void generateFieldsBasedOnAssignfvr(
			@Observes @CDIEvent(SET_REFERNCE) final ParameterDTO parameters) {
		Map<String, Object> referenceObj = new HashMap<String, Object>();
		Long investigationKey = (Long) parameters.getPrimaryParameter();
		Investigation investigation = investigationService
				.getByInvestigationKey(investigationKey);

		if (investigation != null) {
			List<Investigation> investigationSize = (List<Investigation>) investigationService
					.getByInvestigation(investigation.getIntimation().getKey());
			ClaimDto claimDto = claimService.claimToClaimDTO(investigation
					.getClaim());
			referenceObj.put(INVESTIGATION_DTO, investigation);
			referenceObj.put(CLAIM_DTO, claimDto);
			referenceObj.put(INVESTIGATION_SIZE, investigationSize.size());
			view.setReference(referenceObj);
			view.setLayout();
		}
	}
	
	public void generateFieldsBasedOnFieldVisit(@Observes @CDIEvent(FIELD_VISIT_RADIO_CHANGED) final ParameterDTO parameters)
	{
		Boolean isChecked = (Boolean) parameters.getPrimaryParameter();		
		Long claimKey = (Long) parameters.getSecondaryParameters()[0];
		String intimationNo = (String) parameters.getSecondaryParameters()[1];
		String userName = (String) parameters.getSecondaryParameters()[2];
		OptionGroup intiateFvrOption =  (OptionGroup) parameters.getSecondaryParameters()[3];
		
		BeanItemContainer<SelectValue> fvrAllocContainer = null;
		BeanItemContainer<SelectValue> fvrPriorityContainer = null;
		if(!preauthService.getFVRStatusIdByClaimKey(claimKey)){
		
			if(claimKey != null){
				FieldVisitRequest fvrObj = fvrService.getPendingFieldVisitByClaimKey(claimKey);
				if(fvrObj != null){
					dbCalculationService.invokeProcedureAutoSkipFVR(fvrObj.getFvrId());
					fvrService.autoSkipFirstFVRParallel(fvrObj,intimationNo,userName);
				}
			}
			
			fvrAllocContainer = masterService.getSelectValueContainer(ReferenceTable.ALLOCATION_TO);
			fvrPriorityContainer = masterService.getSelectValueContainer(ReferenceTable.FVR_PRIORITY);
			
			view.genertateFieldsBasedOnFieldVisit(isChecked, fvrAllocContainer,/*masterService.getSelectValueContainer(ReferenceTable.ASSIGN_TO)*/null
		,fvrPriorityContainer,intiateFvrOption);
		}
		else{
//			isChecked = false;
			view.showErrorMessage("<BR>FVR request is in process, cannot initiate another request.");
		}
		
	}

	public void submitClick(
			@Observes @CDIEvent(SUBMIT_CLICK) final ParameterDTO parameters) {
		ProcessInvestigationInitiatedDto processInvestigationInitiatedDto = (ProcessInvestigationInitiatedDto) parameters
				.getPrimaryParameter();
		Investigation investigation = (Investigation) parameters.getSecondaryParameters()[0];
		
		Boolean isDisapprove = (Boolean) parameters.getSecondaryParameter(2, Boolean.class);
		
		SearchProcessInvestigationInitiatedTableDTO tableDto = (SearchProcessInvestigationInitiatedTableDTO) parameters.getSecondaryParameter(1, SearchProcessInvestigationInitiatedTableDTO.class);
		
		processInvestigationInitiatedDto.setUserName(tableDto.getUsername());
		processInvestigationInitiatedDto.setPassWord(tableDto.getPassword());		
		
		Map<String, Object> workObj = (Map<String, Object>)tableDto.getDbOutArray();
		
		String clmType = String.valueOf(workObj.get(SHAConstants.CLAIM_TYPE));
		String lobType = String.valueOf(workObj.get(SHAConstants.LOB));
		
		FieldVisitRequest fvrReqObj = null;
		
		if(isDisapprove){
		
		if(workObj.get(SHAConstants.PAYLOAD_REIMB_REQ_BY) == null || clmType.equalsIgnoreCase(SHAConstants.CASHLESS_CLAIM_TYPE)){
				
			    workObj.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_CASHLESS_INV_DISAPPROVE);			                                               
		}
		if(null != lobType && (SHAConstants.PA_LOB.equalsIgnoreCase(lobType))){
			
			if(ReferenceTable.ZONAL_REVIEW_STAGE.equals(investigation.getStage().getKey())){
				workObj.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_DISAPPROVE_ZMR_INVESTIGATION);
		}	
			
		if(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY.equals(investigation.getStage().getKey())){
			
			//For PA Parallel process.
//          workObj.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_DISAPPROVE_CORP_INVESTIGATION);
			workObj.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_PARALLEL_UPLOAD_INVS_END);
			workObj.put(SHAConstants.STAGE_SOURCE,SHAConstants.MA_STAGE_SOURCE);
			}
		}
		else{
			if(ReferenceTable.ZONAL_REVIEW_STAGE.equals(investigation.getStage().getKey())){
				workObj.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_PARALLEL_UPLOAD_INVS_END);	
			}	
			if(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY.equals(investigation.getStage().getKey())){
				workObj.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_PARALLEL_UPLOAD_INVS_END);	
			}
		}
		if(ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY.equals(investigation.getStage().getKey())){
			workObj.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_DISAPPROVE_FA_INVESTIGATION);
			workObj.put(SHAConstants.STAGE_SOURCE,SHAConstants.FA_STAGE_SOURCE);
		}
		if(ReferenceTable.CLAIM_APPROVAL_STAGE.equals(investigation.getStage().getKey()))
		{
			workObj.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_DISAPPROVE_CLAIM_APPROVAL_INVESTIGATION);
			workObj.put(SHAConstants.STAGE_SOURCE,SHAConstants.MA_STAGE_SOURCE);
		}
		
		
		if(processInvestigationInitiatedDto.getInitiateFieldVisitRequestFlag()){
			fvrReqObj = investigationService.createFVRRecord(processInvestigationInitiatedDto,investigation);
			if(fvrReqObj != null)
			workObj.put(SHAConstants.FVR_KEY, fvrReqObj.getKey());
			workObj.put(SHAConstants.PAYLOAD_FVR_CPU_CODE, fvrReqObj.getFvrCpuId());
			workObj.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_INITIATE_FVR_PROCESS_INIT_INV);
		}
		
		}
		else{
			workObj.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_INITIATE_INV_APPROVE);
		}		
		
		processInvestigationInitiatedDto.setDbOutArray(workObj);		

		Stage stage = preauthService.getStageByKey(ReferenceTable.INVESTIGATION_STAGE);
		investigation.setStage(stage);
		
		if (!isDisapprove && processInvestigationInitiatedDto.getInvestigationApprovedRemarks() != null
				&& !processInvestigationInitiatedDto
						.getInvestigationApprovedRemarks().equals("")) {
			investigation.setRemarks(processInvestigationInitiatedDto
					.getInvestigationApprovedRemarks());
			Status status = preauthService.getStatusByPreauth(ReferenceTable.INITIATE_INVESTIGATION_APPROVED);
			investigation.setStatus(status);
			
			investigation.setApprovedDate(new Timestamp(System.currentTimeMillis()));
			investigation.setApprovedBy(tableDto.getUsername());
			
			if (processInvestigationInitiatedDto.getInvAllocationTo() != null) {

				MastersValue invAllocateTo = new MastersValue();
				invAllocateTo.setKey(processInvestigationInitiatedDto
						.getInvAllocationTo().getId());
				invAllocateTo.setValue(processInvestigationInitiatedDto
						.getInvAllocationTo().getValue());
				investigation.setAllocationTo(invAllocateTo);
			}
			if (processInvestigationInitiatedDto.getReasonForInitiatingInvestSelectValue() != null) {

				MastersValue reasonforIniInv = new MastersValue();
				reasonforIniInv.setKey(processInvestigationInitiatedDto
						.getReasonForInitiatingInvestSelectValue().getId());
				reasonforIniInv.setValue(processInvestigationInitiatedDto
						.getReasonForInitiatingInvestSelectValue().getValue());
				investigation.setReasonForInitiatingInv(reasonforIniInv);
			}
			if (processInvestigationInitiatedDto.getReasonForReferring() != null) {

				investigation.setReasonForReferring(processInvestigationInitiatedDto.getReasonForReferring());
			}
			
			
		}else{
			investigation.setRemarks(processInvestigationInitiatedDto
					.getInvestigationNotRequiredRemarks());
			Status status = preauthService.getStatusByPreauth(ReferenceTable.INITIATE_INVESTIGATION_DIS_APPROVED);
			investigation.setStatus(status);
			workObj.put(SHAConstants.STAGE_SOURCE, SHAConstants.SOURCE_INVESTIGATION_DISAPPROVED);
		}
		Investigation investigationResult = investigationService.updateIvestigation(investigation,"",tableDto.getUsername());
		
		if (processInvestigationInitiatedDto.getReasonForInitiatingInvestSelectValue() != null) {

			MastersValue reasonforIniInv = new MastersValue();
			reasonforIniInv.setKey(processInvestigationInitiatedDto
					.getReasonForInitiatingInvestSelectValue().getId());
			reasonforIniInv.setValue(processInvestigationInitiatedDto
					.getReasonForInitiatingInvestSelectValue().getValue());
			investigation.setReasonForInitiatingInv(reasonforIniInv);
		}
		if (processInvestigationInitiatedDto.getReasonForReferring() != null) {

			investigation.setReasonForReferring(processInvestigationInitiatedDto.getReasonForReferring());
		}
		
		
		if(processInvestigationInitiatedDto.getInitiateFieldVisitRequestFlag() && fvrReqObj != null){
			investigationService.updateReimbursementStatus(tableDto.getRodKey(),fvrReqObj.getStatus(),investigationResult.getStage(),tableDto.getUsername());
		}else{
			investigationService.updateReimbursementStatus(tableDto.getRodKey(),investigationResult.getStatus(),investigationResult.getStage(),tableDto.getUsername());
		}
		
		if(investigationResult != null){
//			investigationService.setBPMOutcome(investigationResult, tableDto,isDisapprove);
			
			investigationService.setDBOutcome(investigationResult, tableDto,isDisapprove);
			
		}
		view.finalResult(true);
	}
	
	

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}
	
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(PROCESS_INVESTIGATION_INITIATED_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}	
	
	public void setUpsourceValues(
			@Observes @CDIEvent(PROCESS_INVESTIGATION_INITIATED_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}

}
