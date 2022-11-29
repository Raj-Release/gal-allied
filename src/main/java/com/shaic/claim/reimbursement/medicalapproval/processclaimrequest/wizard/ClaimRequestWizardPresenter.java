package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard;

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
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.common.APIService;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.UpdateOtherClaimDetailDTO;
import com.shaic.claim.reimbursement.dto.EmployeeMasterDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestMedicalDecisionButtons;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.scoring.ppcoding.PPCodingDTO;
import com.shaic.claim.scoring.ppcoding.PPCodingService;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.ClaimService;
import com.shaic.domain.FieldVisitRequestService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.RawInvsDetails;
import com.shaic.domain.RawInvsHeaderDetails;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.zybnet.autocomplete.server.AutocompleteField;

@ViewInterface(ClaimRequestWizard.class)
public class ClaimRequestWizardPresenter extends
AbstractMVPPresenter<ClaimRequestWizard> {
	private static final long serialVersionUID = 331574457704311776L;

	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private PolicyService policyService;
	
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
	private PreauthService preauthService;
	
	@EJB
	private InvestigationService investigationService;
	
	@EJB
	private FieldVisitRequestService fvrService;
	
	@EJB
	private PPCodingService codingService;
	
	public static final String SUBMIT_MEDICAL_APPROVAL_CLAIM_REQUEST = "submit_medical_approval_claim_request";
	
	public static final String SAVE_CLAIM_REQUEST_RRC_REQUEST_VALUES = "save_claim_request_rrc_request_values";
	public static final String CLAIM_REQUEST_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "claim_request_load_rrc_request_drop_down_values";
	public static final String CLAIM_LOAD_EMPLOYEE_DETAILS = "claim_load_employee_details";
	public static final String VALIDATE_CLAIM_REQUEST_USER_RRC_REQUEST = "validate_claim_request_user_rrc_request";	
	public static final String CLAIM_LUMEN_REQUEST = "claim_lumen_request";
	public static final String CLAIM_REQUEST_FIELD_VISIT_EVENT = "claim_request_parallel_FVR_event";
	public static final String CLAIM_REQUEST_INITIATE_INV_EVENT = "claim_request_parallel_initiate_invstigation_event";
	public static final String CLAIM_REQUEST_QUERY_BUTTON_EVENT = "claim_request_parallel_query_button_event";
	public static final String SUBMIT_PARALLEL_QUERY = "Submit Parallel Query";
	public static final String INVS_CANCEL_REQUEST_VALIDATION = "Cancel_request_validation";
	
	protected static final String UPDATE_PREVIOUS_CLAIM_DETAILS = "update_previous_claim_details for claim request";
	
	public static final String DEFINED_LIMIT_ALERT = "defined limit alert msg for super surplus MA";
	
	public static final String CANCEL_QUERY_VALIDATION = "Cancel_Query_validation";
	public static final String UPLOAD_QUERY_LETTER_VALIDATION = "Upload_Query_Letter_validation";
	
	public static final String CONFIRMATION_SEND_TO_WAIT_FOR_INPUT = "Confirmation_send_to_wait_for_input";
	
	public static final String PARALLEL_HOSPITALISATION_DUE_TO = "parallel_hospitalisation_due_to";
	
	public static final String PARALLEL_ILLNESS = "parallel_illness";
	
	public static final String MA_HOSP_SCORING_EVENT = "claim_request_hosp_scoring_event";
	
	public static final String CHECK_PATIENT_STATUS_MA_QUERY_EVENT = "Check Patient Status for MA Query";

	public static final String CLAIM_REQUEST_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "claim_request_load_rrc_request_sub_category_values";

	public static final String CLAIM_REQUEST_LOAD_RRC_REQUEST_SOURCE_VALUES = "claim_request_load_rrc_request_source_values";
	
	public static final String CLAIM_REQUEST_IMPLANT_APPLICABLE_CHANGED = "claim_request_implant_applicable_changed";

	public static final String CLAIM_REQUEST_GENERATE_ADMISSION_TYPE = "claim_request_generate_admission_type";

	public static final String CLAIM_REQUEST_TYPE_OF_ADMISSION_CHANGED = "claim_request_type_of_admission_changed";
	
	public static final String CLAIM_REQUEST_PP_GENERATE_EVENT = "claim_request_pp_generate_event";
	
    public static final String PROCESS_CLAIM_REQUEST_SAVE_AUTO_ALLOCATION_CANCEL_REMARKS = "claim_request_save_auto_allocation_cancel_remarks";
	
	public static final String PROCESS_CLAIM_REQUEST_HOLD_SUBMIT = "submit_hold_claim_request_event";
	
	@SuppressWarnings("static-access")
	public void submitWizard(
			@Observes @CDIEvent(SUBMIT_MEDICAL_APPROVAL_CLAIM_REQUEST) final ParameterDTO parameters) throws Exception {
		PreauthDTO reimbursementDTO = (PreauthDTO) parameters.getPrimaryParameter();
		
		Boolean isAlreadyAcquired = Boolean.FALSE;
		StringBuffer aquiredUser = new StringBuffer();
		Map<String, Object> wrkFlowMap = (Map<String, Object>) reimbursementDTO.getDbOutArray();
		if (wrkFlowMap != null){
			DBCalculationService db = new DBCalculationService();
			Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
			String aciquireByUserId = db.getLockUser(wrkFlowKey);
			if((reimbursementDTO.getStrUserName() != null && aciquireByUserId != null && ! reimbursementDTO.getStrUserName().equalsIgnoreCase(aciquireByUserId)) || aciquireByUserId == null){
				isAlreadyAcquired = Boolean.TRUE;
				aquiredUser.append(aciquireByUserId);
			}
		
		}
		
		if( !isAlreadyAcquired){
			
			Boolean isValidUser = true;
			if(null != reimbursementDTO.getStatusKey() && reimbursementDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)){
				if(reimbursementDTO.getPreauthMedicalDecisionDetails() != null && reimbursementDTO.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null){
					Long claimedAmount = reimbursementDTO.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().longValue();
					claimedAmount = claimedAmount > 0l ? claimedAmount : 0l;
					String limitAmountValidation = dbCalculationService.getLimitAmountValidation(reimbursementDTO.getStrUserName(),claimedAmount, SHAConstants.REIMBURSEMENT_CHAR);
					if(limitAmountValidation != null && limitAmountValidation.equalsIgnoreCase("N")){
						isValidUser = false;
					}
				}
				
			}
			
			if(isValidUser){
		
			// If hospitalization ROD is not submitted then we should not allow other ROD to submit.
			Reimbursement currentReimbursement = createRODService.getReimbursementObjectByKey(reimbursementDTO.getKey());
			Boolean isFirstRODApproved = createRODService.getStatusOfFirstROD(reimbursementDTO.getClaimDTO().getKey(), currentReimbursement);
			isFirstRODApproved = true;
			
//			if(reimbursementDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationRequest() != null &&
//					("Y").equalsIgnoreCase(reimbursementDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationRequest()) &&
//										   reimbursementService.isClaimPaymentAvailable(reimbursementDTO.getRodNumber())){
//				if((ReferenceTable.CLAIM_REQUEST_REJECT_STATUS).equals(reimbursementDTO.getStatusKey())){
//					reimbursementDTO.setStatusKey(ReferenceTable.PAYMENT_SETTLED);
//				}
//			}
			
			if(isFirstRODApproved) {
				// Rollback procedure to clear the accumulator value.................
				
				Reimbursement hospitalizationRod = reimbursementService.getHospitalizationRod(reimbursementDTO.getClaimKey(), reimbursementDTO.getKey());
				Boolean shouldInvokeAcc = true;
				
				if((reimbursementDTO.getIsHospitalizationRepeat() != null && !reimbursementDTO.getIsHospitalizationRepeat()) && (reimbursementDTO.getHospitalizaionFlag() != null && !reimbursementDTO.getHospitalizaionFlag()) && (reimbursementDTO.getPartialHospitalizaionFlag() != null && !reimbursementDTO.getPartialHospitalizaionFlag())) {
					shouldInvokeAcc = false;
				}
				if(shouldInvokeAcc) {
					if(null != reimbursementDTO.getStatusKey() && (reimbursementDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) && (reimbursementDTO.getClaimDTO().getClaimType() != null && reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY))) {
						if(reimbursementDTO.getClaimDTO().getClaimType() != null && reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
							if(hospitalizationRod == null){
								dbCalculationService.reimbursementRollBackProc(reimbursementDTO.getPreviousPreauthKey().equals(0l) ? reimbursementDTO.getKey() : reimbursementDTO.getPreviousPreauthKey() , reimbursementDTO.getPreviousPreauthKey().equals(0l) ? "R" : "C");
							} else {
								//IMSSUPPOR-32047
								if(reimbursementDTO.getIsQueryReceived() && reimbursementDTO.getPartialHospitalizaionFlag() != null && reimbursementDTO.getPartialHospitalizaionFlag()){
									dbCalculationService.reimbursementRollBackProc(reimbursementDTO.getKey(), "R");
								}else if(!reimbursementDTO.getIsHospitalizationRepeat()) {
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
					}
					
					if(null != reimbursementDTO.getStatusKey() && ((reimbursementDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_ESCALATION_REPLY_STATUS) || reimbursementDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS) 
							|| reimbursementDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS))) /*|| reimbursementDTO.getIsQueryReceived()*/) {
						//dbCalculationService.reimbursementRollBackProc(reimbursementDTO.getKey(), "R");
						shouldInvokeAcc = false;
					}
				}
				
				if(ReferenceTable.getFHORevisedKeys().containsKey(reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					policyService.updatePolicyBonusDetails(reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyNumber());
				}
				reimbursementDTO.setStageKey(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
				
				Reimbursement reimbursement = reimbursementService.submitClaimRequest(reimbursementDTO);
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
				
				RawInvsHeaderDetails headerObj =  claimService.getRawHeaderByIntimation(reimbursementDTO.getNewIntimationDTO().getIntimationId());
				if(null != headerObj){
					List<RawInvsDetails> existingList = claimService.getRawDetailsByRecordType(headerObj.getKey());
					for (RawInvsDetails rawInvsDetails : existingList) {
						preauthService.updateEsclateIfCompleted(rawInvsDetails);
					}
				}
				
				if(shouldInvokeAcc && null != reimbursementDTO.getStatusKey() && 
						((reimbursementDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_ESCALATION_REPLY_STATUS) || reimbursementDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS) || reimbursementDTO.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS)))) {
					dbCalculationService.invokeReimbursementAccumulatorProcedure(reimbursement.getKey());
				}
				if(reimbursement != null && (null != reimbursement.getStatus().getKey() && reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) || reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS)) {
					if(reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS) && shouldInvokeAcc) {
						dbCalculationService.invokeReimbursementAccumulatorProcedure(reimbursement.getKey());
					}
					
					dbCalculationService.updateProvisionAmount(reimbursement.getKey(), reimbursement.getClaim().getKey());
					
					String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
					if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
						//try {
							/*Hospitals hospitalDetailsByKey = hospitalService.getHospitalDetailsByKey(reimbursement.getClaim().getIntimation().getHospital());
							Claim claim = claimService.getClaimByClaimKey(reimbursement.getClaim().getKey());
							String provisionAmtInput = SHAUtils.getProvisionAmtInput(claim, hospitalDetailsByKey.getName(), String.valueOf(claim.getCurrentProvisionAmount().longValue()));
							apiService.updateProvisionAmountToPremia(provisionAmtInput);
							String bedCount = reimbursementDTO.getPreauthDataExtractionDetails().getUpdateHospitalDetails().getInpatientBeds();
							if(bedCount != null) {
								PremiaService.getInstance().updateBedsCount(bedCount, hospitalDetailsByKey.getHospitalCode());
							}*/
						/*} catch(Exception e) {
							
						}*/
					}
					
				}
				
				
					view.buildSuccessLayout();
				}else {
					view.buildFailureLayout();
				}
			} else {
				view.validationForLimit();
			}
		} else{
			view.alertForAlreadyAcquired(aquiredUser.toString());
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
		if(isValid){
			reimbursementService.loadRRCRequestValues(preauthDTO,0d,SHAConstants.CLAIM_REQUEST);
		}
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	
	public void showUpdateClaimDetailTable(
			@Observes @CDIEvent(UPDATE_PREVIOUS_CLAIM_DETAILS) final ParameterDTO parameters) {
		
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		
		List<UpdateOtherClaimDetailDTO> updateOtherClaimDetails = new ArrayList<UpdateOtherClaimDetailDTO>();
		
		if(preauthDTO.getUpdateOtherClaimDetailDTO().isEmpty()){

			//updateOtherClaimDetails = preauthService.getUpdateOtherClaimDetails(preauthDTO.getNewIntimationDTO().getPolicy().getKey());
			updateOtherClaimDetails = preauthService.getUpdateOtherClaimDetailsDTOForReimbursement(preauthDTO.getKey());
			if(updateOtherClaimDetails != null && updateOtherClaimDetails.isEmpty()){
				updateOtherClaimDetails = preauthService.getUpdateOtherClaimDetails(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),preauthDTO);
			}
		}else{
			updateOtherClaimDetails = preauthDTO.getUpdateOtherClaimDetailDTO();
		}
	    
	    view.setUpdateOtherClaimsDetails(updateOtherClaimDetails);
	}

	public void preauthLumenRequest(@Observes @CDIEvent(CLAIM_LUMEN_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		view.buildInitiateLumenRequest(preauthDTO.getNewIntimationDTO().getIntimationId());
	}



	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void showAlertMessageForDefinedLimit(@Observes @CDIEvent(DEFINED_LIMIT_ALERT) final ParameterDTO parameters)
	{
		String message = (String) parameters.getPrimaryParameter();
		Object rejectionCategoryDropdownValues = masterService.getSelectValueContainer(ReferenceTable.REIMB_REJECTION_CATEGORY);
		view.confirmMessageForDefinedLimt(rejectionCategoryDropdownValues);
	}
		
	public void generateFieldVisitLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_FIELD_VISIT_EVENT) final ParameterDTO parameters) {

		PreauthDTO bean = (PreauthDTO)parameters.getPrimaryParameter();
		
		/*FieldVisitRequest fvrobjByRodKey = fvrService.getPendingFieldVisitByClaimKey(bean.getClaimDTO().getKey());
		
		if((fvrobjByRodKey  == null || (fvrobjByRodKey != null && fvrobjByRodKey.getStatus() != null && ReferenceTable.INITITATE_FVR.equals(fvrobjByRodKey.getStatus().getKey())))){
		
			if(fvrobjByRodKey  != null ){
				dbCalculationService.invokeProcedureAutoSkipFVR(fvrobjByRodKey.getFvrId());
				fvrService.autoSkipFirstFVRParallel(fvrobjByRodKey,bean.getNewIntimationDTO().getIntimationId(),bean.getStrUserName());
			}
			
		}*/
		
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

	public void generateFieldsForInvestigation(
			@Observes @CDIEvent(CLAIM_REQUEST_INITIATE_INV_EVENT) final ParameterDTO parameters) {
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		/*Boolean investigationAvailable = investigationService.getInvestigationByClaim(preauthDTO.getClaimKey());
		preauthDTO.setIsInvestigation(investigationAvailable);
		if(! preauthDTO.getIsInvestigation())
		{*/
		view.generateFieldsOnInvtClick(preauthDTO.isDirectToAssignInv());
		/*}
		else
		{
			view.alertMessageForInvestigation();
		}*/
	}
	
	public void generateQueryLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_QUERY_BUTTON_EVENT) final ParameterDTO parameters) {
		view.generateQueryLayout();
	}
	
	public void submitParallelQuery(
			@Observes @CDIEvent(SUBMIT_PARALLEL_QUERY) final ParameterDTO parameters) {
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		reimbursementService.submitParallelQuery(preauthDTO);
	}
	
	
	public void validateCancelRequest(
			@Observes @CDIEvent(INVS_CANCEL_REQUEST_VALIDATION) final ParameterDTO parameters) {
		InvesAndQueryAndFvrParallelFlowTableDTO invesFvrQueryDTO = (InvesAndQueryAndFvrParallelFlowTableDTO) parameters.getPrimaryParameter();
		view.validateCancelRequest(invesFvrQueryDTO);
	}
	
	public void validateCancelQueryRequest(
			@Observes @CDIEvent(CANCEL_QUERY_VALIDATION) final ParameterDTO parameters) {
		view.validateCancelQueryRequest();
	}

	public void validateUploadQueryLetter(
			@Observes @CDIEvent(UPLOAD_QUERY_LETTER_VALIDATION) final ParameterDTO parameters) {
		view.validateUploadQueryLetter();
	}
	
	/*public void validateForEnableParallel(
			@Observes @CDIEvent(PARALLEL_HOSPITALISATION_DUE_TO) final ParameterDTO parameters) {
		SelectValue selectedValue = (SelectValue) parameters.getPrimaryParameter();
		
		view.validateForEnableParallel(selectedValue);
	}*/
	
	public void validateForEnableParallel(
			@Observes @CDIEvent(PARALLEL_HOSPITALISATION_DUE_TO) final ParameterDTO parameters) {
		SelectValue selectedValue = (SelectValue) parameters.getPrimaryParameter();
		SelectValue selectedValue1=(SelectValue) parameters.getSecondaryParameter(0,SelectValue.class);
		
		view.validateForEnableParallel(selectedValue,selectedValue1);
	}
	
	public void validateEnableIllnessParallel(
			@Observes @CDIEvent(PARALLEL_ILLNESS) final ParameterDTO parameters) {
		SelectValue selectedValue = (SelectValue) parameters.getPrimaryParameter();
		SelectValue selectedValue1=(SelectValue) parameters.getSecondaryParameter(0,SelectValue.class);
		
		view.validateIllnessEnableParallel(selectedValue,selectedValue1);
	}
	
	public void setMAButtonLayout(
			@Observes @CDIEvent(MA_HOSP_SCORING_EVENT) final ParameterDTO parameters) {
		ClaimRequestMedicalDecisionButtons claimRequestMedicalDecisionButtons = (ClaimRequestMedicalDecisionButtons)parameters.getPrimaryParameter();
		view.generateButtonLayoutBasedOnScoring(claimRequestMedicalDecisionButtons);
		
	}
	
	public void checkPatienetStatusForMAQuery(
			@Observes @CDIEvent(CHECK_PATIENT_STATUS_MA_QUERY_EVENT) final ParameterDTO parameters) throws Exception {
		view.checkPatientStatusForMAQuery();
	}		
	
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(CLAIM_REQUEST_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(CLAIM_REQUEST_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}
	
	public void generateFieldsBasedOnImplantApplicable(@Observes @CDIEvent(CLAIM_REQUEST_IMPLANT_APPLICABLE_CHANGED) final ParameterDTO parameters)
	{
		Boolean isCked = (Boolean) parameters.getPrimaryParameter();
		view.generateFieldsBasedOnImplantApplicable(isCked);
	}
	
	public void generatePPCoadingField(@Observes @CDIEvent(CLAIM_REQUEST_PP_GENERATE_EVENT) final ParameterDTO parameters)
	{
		Boolean ischecked = (Boolean) parameters.getPrimaryParameter();
		Long intimationKey = (Long) parameters.getSecondaryParameter(0, Long.class);
		String hospitalType = (String) parameters.getSecondaryParameter(1, String.class);
		List<PPCodingDTO> codingDTOs =null;
		Map<String,Boolean> selectedPPCoding = codingService.getPPCodingValues(intimationKey, hospitalType);
		if(!ischecked){
			codingDTOs = codingService.populatePPCoding(hospitalType);
		}
		view.generatePPCoadingField(ischecked,codingDTOs,selectedPPCoding);
	}
	public void generateadmissiontypeFields(@Observes @CDIEvent(CLAIM_REQUEST_GENERATE_ADMISSION_TYPE) final ParameterDTO parameters)
	{
		Boolean displayadmissiontype = (Boolean) parameters.getPrimaryParameter();
		view.generateadmissiontypeFields(displayadmissiontype);
	}
	
	public void generateFieldsBasedOnTypeOfAdmissionStatus(@Observes @CDIEvent(CLAIM_REQUEST_TYPE_OF_ADMISSION_CHANGED) final ParameterDTO parameters)
	{
		view.genertateFieldsBasedOnTypeOfAdmisstion();
	}
	
	public void saveCancelRemarks(@Observes @CDIEvent(PROCESS_CLAIM_REQUEST_SAVE_AUTO_ALLOCATION_CANCEL_REMARKS) final ParameterDTO parameters) {
		PreauthDTO preauthdto = (PreauthDTO) parameters.getPrimaryParameter();
		Reimbursement reimbursment =null;
		if(null != preauthdto && null != preauthdto.getKey()){
			reimbursment = reimbursementService.getReimbursementByKey(preauthdto.getKey());
		}
		reimbursementService.updateCancelRemarksForMA(reimbursment,preauthdto.getAutoAllocCancelRemarks(),preauthdto);

	}
	
	public void holdSubmitWizard(
			@Observes @CDIEvent(PROCESS_CLAIM_REQUEST_HOLD_SUBMIT) final ParameterDTO parameters) throws Exception {
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();

		Boolean isAlreadyAcquired = Boolean.FALSE;
		StringBuffer aquiredUser = new StringBuffer();
		Map<String, Object> wrkFlowMap = (Map<String, Object>) preauthDTO.getDbOutArray();
		if (wrkFlowMap != null){
			DBCalculationService db = new DBCalculationService();
			Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
			String aciquireByUserId = db.getLockUser(wrkFlowKey);
			if((preauthDTO.getStrUserName() != null && aciquireByUserId != null && ! preauthDTO.getStrUserName().equalsIgnoreCase(aciquireByUserId)) || aciquireByUserId == null){
				isAlreadyAcquired = Boolean.TRUE;
				aquiredUser.append(aciquireByUserId);
			}

		}

		if(! isAlreadyAcquired){

			DBCalculationService dbCalService = new DBCalculationService();
			if(preauthDTO.getDbOutArray() != null){

				Map<String, Object> holdWrkFlowMap = (Map<String, Object>) preauthDTO.getDbOutArray();
				holdWrkFlowMap.put(SHAConstants.USER_ID,preauthDTO.getStrUserName());
				holdWrkFlowMap.put(SHAConstants.STAGE_SOURCE, SHAConstants.MA_STAGE_SOURCE);
				if(preauthDTO.getStrUserName() != null){
					TmpEmployee employeeByName = preauthService.getEmployeeByName(preauthDTO.getStrUserName());
					if(employeeByName != null){
						holdWrkFlowMap.put(SHAConstants.REFERENCE_USER_ID,employeeByName.getEmpId());
					}
				}
				holdWrkFlowMap.put(SHAConstants.PAYLOAD_PED_REFERRED_BY,preauthDTO.getStrUserName());
				holdWrkFlowMap.put(SHAConstants.PAYLOAD_PED_TYPE,SHAConstants.HOLD_FLAG);
//				holdWrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.PREAUTH_HOLD_OUTCOME);
				holdWrkFlowMap.put(SHAConstants.PAYLOAD_PED_REQUESTOR_ROLE,preauthDTO.getPreauthMedicalDecisionDetails().getHoldRemarks());
				Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(holdWrkFlowMap);

				dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
			}
			Reimbursement currentReimbursement = createRODService.getReimbursementObjectByKey(preauthDTO.getKey());
			if(currentReimbursement != null){
				preauthService.updateStageInformationForMAautoAllocation(currentReimbursement,preauthDTO);
			}

			if(preauthDTO.getDbOutArray() != null){
				Long wkFlowKey = (Long)wrkFlowMap.get(SHAConstants.WK_KEY);	
				preauthService.updateHoldRemarksForAutoAllocation(wkFlowKey, preauthDTO.getPreauthMedicalDecisionDetails().getHoldRemarks());
			}
			view.buildSuccessLayout();
		}else{
			view.alertForAlreadyAcquired(aquiredUser.toString());
		}
	}
}
