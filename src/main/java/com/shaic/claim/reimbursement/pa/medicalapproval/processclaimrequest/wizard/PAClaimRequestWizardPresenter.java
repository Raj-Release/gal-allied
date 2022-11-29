/**
 * 
 */
package com.shaic.claim.reimbursement.pa.medicalapproval.processclaimrequest.wizard;

/**
 * @author ntv.vijayar
 *
 */


import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.common.APIService;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.EmployeeMasterDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.FieldVisitRequestService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.paclaim.reimbursement.service.PAReimbursementService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.zybnet.autocomplete.server.AutocompleteField;

@ViewInterface(PAClaimRequestWizard.class)
public class PAClaimRequestWizardPresenter extends
AbstractMVPPresenter<PAClaimRequestWizard> {
	private static final long serialVersionUID = 331574457704311776L;

	//@EJB
	//private PAReimbursementService reimbursementService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private APIService apiService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private CreateRODService createRODService;
	
	@EJB
	private PAReimbursementService reimbursementService;
	

	@EJB
	private PreauthService preauthService;
	

	@EJB
	private ReimbursementService reimbursementServiceHealth;
	

	@EJB
	private FieldVisitRequestService fvrService;

	public static final String SUBMIT_MEDICAL_APPROVAL_CLAIM_REQUEST = "pa_submit_medical_approval_claim_request";
	
	public static final String SAVE_CLAIM_REQUEST_RRC_REQUEST_VALUES = "pa_save_claim_request_rrc_request_values";
	public static final String CLAIM_REQUEST_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "pa_claim_request_load_rrc_request_drop_down_values";
	public static final String CLAIM_LOAD_EMPLOYEE_DETAILS = "pa_claim_load_employee_details";
	public static final String VALIDATE_CLAIM_REQUEST_USER_RRC_REQUEST = "pa_validate_claim_request_user_rrc_request";
	public static final String PA_CLAIM_REQUEST_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "pa_claim_request_load_rrc_request_sub_category_values";

	public static final String PA_CLAIM_REQUEST_LOAD_RRC_REQUEST_SOURCE_VALUES = "pa_claim_request_load_rrc_request_source_values";
	
	public static final String PA_CLAIM_REQUEST_INITIATE_INV_EVENT = "pa_claim_request_parallel_initiate_invstigation_event";

	public static final String PA_CLAIM_REQUEST_QUERY_BUTTON_EVENT = "pa_claim_request_parallel_query_button_event";
	
	public static final String PA_SUBMIT_PARALLEL_QUERY = "pa_submit_parallel_query";
	
	public static final String PA_CLAIM_REQUEST_FIELD_VISIT_EVENT = "pa_claim_request_parallel_FVR_event";

	public static final String PA_CLAIM_REQUEST_AUTO_SKIP_FVR = "pa_claim_request_auto_skip_fvr";
	


	@SuppressWarnings("static-access")
	public void submitWizard(
			@Observes @CDIEvent(SUBMIT_MEDICAL_APPROVAL_CLAIM_REQUEST) final ParameterDTO parameters) {
		PreauthDTO reimbursementDTO = (PreauthDTO) parameters.getPrimaryParameter();
		
		
		// If hospitalization ROD is not submitted then we should not allow other ROD to submit.
		Reimbursement currentReimbursement = createRODService.getReimbursementObjectByKey(reimbursementDTO.getKey());
		//Boolean isFirstRODApproved = createRODService.getStatusOfFirstROD(reimbursementDTO.getClaimDTO().getKey(), currentReimbursement);
		Boolean isFirstRODApproved = true;
		if(isFirstRODApproved) {
			// Rollback procedure to clear the accumulator value.................
			
			//Reimbursement hospitalizationRod = reimbursementService.getHospitalizationRod(reimbursementDTO.getClaimKey(), reimbursementDTO.getKey());
			Boolean shouldInvokeAcc = true;
			/*
			if((reimbursementDTO.getIsHospitalizationRepeat() != null && !reimbursementDTO.getIsHospitalizationRepeat()) && (reimbursementDTO.getHospitalizaionFlag() != null && !reimbursementDTO.getHospitalizaionFlag()) && (reimbursementDTO.getPartialHospitalizaionFlag() != null && !reimbursementDTO.getPartialHospitalizaionFlag())) {
				shouldInvokeAcc = false;
			}*/
			
		/*	if((reimbursementDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) && (reimbursementDTO.getClaimDTO().getClaimType() != null && reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY))) {
				if(reimbursementDTO.getClaimDTO().getClaimType() != null && reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
					if(hospitalizationRod == null){
						dbCalculationService.reimbursementRollBackProc(reimbursementDTO.getPreviousPreauthKey().equals(0l) ? reimbursementDTO.getKey() : reimbursementDTO.getPreviousPreauthKey() , reimbursementDTO.getPreviousPreauthKey().equals(0l) ? "R" : "C");
					} else {
						if(!reimbursementDTO.getIsHospitalizationRepeat()) {
							dbCalculationService.reimbursementRollBackProc(hospitalizationRod.getKey(), "R");
						}
						
					}
					
				}else{
					if(hospitalizationRod != null) {
						if(!reimbursementDTO.getIsHospitalizationRepeat()) {
							dbCalculationService.reimbursementRollBackProc(hospitalizationRod.getKey(), "R");
						}
						
					}
				}
			}*/

			reimbursementDTO.setStageKey(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
			if((reimbursementDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_ESCALATION_REPLY_STATUS) || reimbursementDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS) 
					|| reimbursementDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS)) || reimbursementDTO.getIsQueryReceived()) {
				dbCalculationService.reimbursementRollBackProc(reimbursementDTO.getKey(), "R");
			}
			
			Reimbursement reimbursement = reimbursementService.submitClaimRequest(reimbursementDTO);
			if(shouldInvokeAcc && (reimbursementDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_ESCALATION_REPLY_STATUS) || reimbursementDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS) || reimbursementDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS))) {
				dbCalculationService.invokeReimbursementAccumulatorProcedure(reimbursement.getKey());
			}
			if(reimbursement != null && (reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) || reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS)) {
				if(reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS) && shouldInvokeAcc) {
					dbCalculationService.invokeReimbursementAccumulatorProcedure(reimbursement.getKey());
				}
				
				dbCalculationService.updateProvisionAmountForPANonHealth(reimbursement.getKey(), reimbursement.getClaim().getKey());
				
				String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
				if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
					try {
						Hospitals hospitalDetailsByKey = hospitalService.getHospitalDetailsByKey(reimbursement.getClaim().getIntimation().getHospital());
						Claim claim = claimService.getClaimByClaimKey(reimbursement.getClaim().getKey());
						String provisionAmtInput = SHAUtils.getProvisionAmtInput(claim, hospitalDetailsByKey.getName(), String.valueOf(claim.getCurrentProvisionAmount().longValue()));
						apiService.updateProvisionAmountToPremia(provisionAmtInput);
						String bedCount = reimbursementDTO.getPreauthDataExtractionDetails().getUpdateHospitalDetails().getInpatientBeds();
						if(bedCount != null) {
							PremiaService.getInstance().updateBedsCount(bedCount, hospitalDetailsByKey.getHospitalCode());
						}
					} catch(Exception e) {
						
					}
				}
				
			}
			
			if(reimbursementDTO != null && reimbursementDTO.getPreauthMedicalDecisionDetails() != null && reimbursementDTO.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList() != null && !reimbursementDTO.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList().isEmpty()){
				System.out.println("reimbursementDTO.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList().size() : " +reimbursementDTO.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList().size());

				for (AssignedInvestigatiorDetails assignedInvestigatiorDetails : reimbursementDTO.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList()) {
					if(assignedInvestigatiorDetails.getRvoFindingsKey() != null){

						if(assignedInvestigatiorDetails.getRvoFindingsKey().getId() != null ){
							MastersValue masterValue = masterService.getMaster(assignedInvestigatiorDetails.getRvoFindingsKey().getId());
							if(masterValue != null){
								assignedInvestigatiorDetails.setRvoFindings(masterValue);
							}
						}

					}
					if(assignedInvestigatiorDetails.getRvoReasonKey() != null){
						if(assignedInvestigatiorDetails.getRvoReasonKey().getId() != null ){
							MastersValue masterValue = masterService.getMaster(assignedInvestigatiorDetails.getRvoReasonKey().getId());
							if(masterValue != null){
								assignedInvestigatiorDetails.setRvoReason(masterValue);
							}
						}
					}
					if(assignedInvestigatiorDetails.getReviewRemarkskey() != null){
						if(assignedInvestigatiorDetails.getReviewRemarkskey().getId() != null ){
							MastersValue masterValue = masterService.getMaster(assignedInvestigatiorDetails.getReviewRemarkskey().getId());
							if(masterValue != null && masterValue.getValue() != null && !masterValue.getValue().isEmpty()){
								assignedInvestigatiorDetails.setReviewRemarks(masterValue.getValue());
							}
						}
					}
					
					if(assignedInvestigatiorDetails.getRvoFindingsKey() != null && assignedInvestigatiorDetails.getRvoFindingsKey().getId() != null &&
							assignedInvestigatiorDetails.getRvoFindingsKey().getId() != null 
							&& (assignedInvestigatiorDetails.getRvoFindingsKey().getId().equals(ReferenceTable.RVO_FINDINGS_ACCEPTED_CLAIM_APPROVED_KEY) ||
									assignedInvestigatiorDetails.getRvoFindingsKey().getId().equals(ReferenceTable.RVO_FINDINGS_ACCEPTED_CLAIM_REJECTED_KEY))){
						assignedInvestigatiorDetails.setRvoReason(null);
					}

				}
			}
			preauthService.updateInvsReviewDetails(reimbursementDTO.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList());
			
			
			view.buildSuccessLayout();
		} else {
			view.buildFailureLayout();
		}
		

	}
	
	public void saveRRCRequestValues(@Observes @CDIEvent(SAVE_CLAIM_REQUEST_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_REIMBURSEMENT);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(CLAIM_REQUEST_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	
	public void loadEmployeeDetails(@Observes @CDIEvent(CLAIM_LOAD_EMPLOYEE_DETAILS) final ParameterDTO parameters){
		AutocompleteField<EmployeeMasterDTO> field = (AutocompleteField<EmployeeMasterDTO>) parameters.getPrimaryParameter();
		String query = parameters.getSecondaryParameter(0, String.class);
		List<EmployeeMasterDTO> employeeList = reimbursementService.getListOfEmployeeDetails(query);
		view.loadEmployeeMasterData(field, employeeList);
		
	}
	
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_CLAIM_REQUEST_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}


	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(PA_CLAIM_REQUEST_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(PA_CLAIM_REQUEST_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}
	
	public void generateFieldsForInvestigation(
			@Observes @CDIEvent(PA_CLAIM_REQUEST_INITIATE_INV_EVENT) final ParameterDTO parameters) {
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		view.generateFieldsOnInvtClick(preauthDTO.isDirectToAssignInv());
		
	}
	
	
	public void generateQueryLayout(
			@Observes @CDIEvent(PA_CLAIM_REQUEST_QUERY_BUTTON_EVENT) final ParameterDTO parameters) {
		view.generateQueryLayout();
	}
	

	public void submitParallelQuery(
			@Observes @CDIEvent(PA_SUBMIT_PARALLEL_QUERY) final ParameterDTO parameters) {
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		reimbursementService.submitParallelQuery(preauthDTO);
	}
	
	public void generateFieldVisitLayout(
			@Observes @CDIEvent(PA_CLAIM_REQUEST_FIELD_VISIT_EVENT) final ParameterDTO parameters) {

		PreauthDTO bean = (PreauthDTO)parameters.getPrimaryParameter();
	
		
		boolean fvrpending = preauthService.getFVRStatusIdByClaimKey(bean.getClaimDTO().getKey());
		
		boolean isAssigned = false;
		FieldVisitRequest fvrobj = null;
		
		if(fvrpending){
			fvrobj = preauthService.getPendingFVRByClaimKey(bean.getClaimDTO().getKey());
		}
		String repName = "";
		String repContactNo = "";
		if(fvrobj != null){
			isAssigned = true;
			repName = fvrobj.getRepresentativeName();
			repContactNo = fvrobj.getRepresentativeContactNumber();
		}
		
		if((ReferenceTable.STAR_CRITICARE_OTHER_BANKS_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
				||!ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
						&& bean.getClaimDTO().getClaimType() != null
						&& ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY.equals(bean.getClaimDTO().getClaimType().getId())) {
			String icdCodeValue = null;
			if(bean.getPreauthDataExtractionDetails().getDiagnosisTableList() != null 
					&& !bean.getPreauthDataExtractionDetails().getDiagnosisTableList().isEmpty() && (bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(0).getDiagnosisName() != null || bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(0).getIcdCode() != null)) {
				String icdCode = bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(0).getIcdCode() != null ? bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(0).getIcdCode().getValue() : "";
			
				String split[] = icdCode.split("-");
			
				if(split.length>0){
					icdCodeValue = split[split.length-1];  // bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(0).getIcdCode().getCommonValue();
				}
				bean.setMulticlaimAvailFlag(dbCalculationService.getClaimRestrictionAvailable(bean.getNewIntimationDTO().getPolicy().getKey(),
						bean.getNewIntimationDTO().getInsuredPatient().getKey(),bean.getNewIntimationDTO().getKey(),
						icdCodeValue,
						bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(0).getDiagnosisName().getValue()));
	
			}
		}	
		
		view.genertateFieldsBasedOnFieldVisit(masterService
				.getSelectValueContainer(ReferenceTable.ALLOCATION_TO)
				,/*masterService.getSelectValueContainer(ReferenceTable.ASSIGN_TO)*/null,masterService.getSelectValueContainer(ReferenceTable.FVR_PRIORITY),
				isAssigned, repName, repContactNo);
		
	}
	
	public void submitAutoSkipFVR(
			@Observes @CDIEvent(PA_CLAIM_REQUEST_AUTO_SKIP_FVR) final ParameterDTO parameters) {
		// Boolean status = (Boolean) parameters.getPrimaryParameter();
		PreauthDTO uploadDTO = (PreauthDTO) parameters
				.getPrimaryParameter();
		FieldVisitRequest fvrobjByRodKey = fvrService.getPendingFieldVisitByClaimKey(uploadDTO.getClaimDTO().getKey());
		
		if((fvrobjByRodKey  == null || (fvrobjByRodKey != null && fvrobjByRodKey.getStatus() != null && ReferenceTable.INITITATE_FVR.equals(fvrobjByRodKey.getStatus().getKey())))){
		
			if(fvrobjByRodKey  != null ){
				dbCalculationService.invokeProcedureAutoSkipFVR(fvrobjByRodKey.getFvrId());
				if(uploadDTO.getIsFvrInitiate()){
				fvrService.autoSkipFirstFVR(fvrobjByRodKey);
				}else{
					fvrService.autoSkipNoFirstFVR(fvrobjByRodKey);
				}
			}
			}
		

	}
}
