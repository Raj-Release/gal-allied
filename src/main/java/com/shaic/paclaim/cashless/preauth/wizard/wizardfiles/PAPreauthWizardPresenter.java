package com.shaic.paclaim.cashless.preauth.wizard.wizardfiles;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.fvrdetails.view.ViewFVRService;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Investigation;
import com.shaic.domain.MasterService;
import com.shaic.domain.PEDValidationService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.State;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.preauth.HospitalPackage;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.ProcedureMaster;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.domain.service.PreMedicalService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(PAPreauthWizard.class)
public class PAPreauthWizardPresenter  extends AbstractMVPPresenter<PAPreauthWizard>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5008035088244826928L;
	
	private final Logger log = LoggerFactory.getLogger(PreauthWizardPresenter.class);
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private PreMedicalService preMedicalService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private PEDValidationService pedValidationService;
	
	@EJB
	private ViewFVRService viewFVRService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
		
	public static final String PREAUTH_STEP_CHANGE_EVENT = "PA_preauth_step_change_event";
	public static final String PREAUTH_SAVE_EVENT = "PA_preauth_save_event";
	public static final String PREAUTH_SUBMITTED_EVENT = "PA_preatuh_submit_event";
	public static final String SETUP_REFERENCE_DATA = "PA_preauth_set_edit_data";
	public static final String PREAUTH_TREATMENT_TYPE_CHANGED = "PA_preauth_treatment_type_changed";
	public static final String PREAUTH_PATIENT_STATUS_CHANGED = "PA_preauth_patient_status_changed";
	public static final String PREAUTH_RELAPSE_OF_ILLNESS_CHANGED = "PA_preauth_relapse_of_illness_changed";
	
	public static final String PREAUTH_QUERY_EVENT ="PA_preauth_query_click_event";
	public static final String PREAUTH_SUGGEST_REJECTION_EVENT ="PA_preauth_rejection_click_event";
	public static final String PREAUTH_SEND_FOR_PROCESSING_EVENT ="PA_preauth_send_for_processing_click_event";

	public static final String PREAUTH_REFERCOORDINATOR_EVENT = "PA_preauth_refer_coordinator_event";
	
	public static final String PREAUTH_PREVIOUS_CLAIM_DETAILS = "PA_preauth_previous_claim_details";

	public static final String MASTER_SEARCH_STATE = "PA_preauth_master_search_stae";
	
	public static final String CHECK_CRITICAL_ILLNESS = "PA_preauth_check_critical_illness";
	
	public static final String GET_ICD_BLOCK = "PA_preauth_get_icd_block";
	
	public static final String GET_ICD_CODE = "PA_preauth_get_icd_code";
	
	public static final String PREAUTH_QUERY_BUTTON_EVENT = "PA_preauth_query_button_event";

	public static final String PREAUTH_REJECTION_EVENT = "PA_preauth_rejection_event";

	public static final String PREAUTH_APPROVE_EVENT = "PA_preauth_approve_event";

	public static final String PREAUTH_DENIAL_EVENT = "PA_preauth_denial_event";

	public static final String PREAUTH_ESCALATE_EVENT = "PA_preauth_escalate_event";

	public static final String FIELD_VISIT_RADIO_CHANGED = "PA_preauth_field_visit_radio_changed";

	public static final String SPECIALIST_OPINION_RADIO_CHANGED = "PA_preauth_specialist_radio_changed";

	public static final String PREAUTH_SENT_TO_CPU_SELECTED = "PA_preauth_sent_to_cpu_selected";

	public static final String VIEW_CLAIMED_AMOUNT_DETAILS = "PA_view_claimed_amount_details";
	
	public static final String VIEW_BALANCE_SUM_INSURED_DETAILS = "PA_view_Balance_Sum_Insured_details";
	
	public static final String SUBLIMIT_CHANGED_BY_SECTION = "PA_Set sublimit values by selecting section for preauth";
	
	public static final String NO_OF_DAYS_RULE = "PA_no_of_days_rule";

	public static final String GET_EXCLUSION_DETAILS = "PA_preauth_get_exclusion_details";

	public static final String PREAUTH_GET_PACKAGE_RATE = "PA_preauth_get_package_rate";

	public static final String SUM_INSURED_CALCULATION = "PA_calculate_sum_insured_values_from_DB";

	public static final String CHECK_INVESTIGATION_INITIATED = "PA_rule_for_investigation_initiated";
	
	public static final String BALANCE_SUM_INSURED = "PA_preauth_balance_sum_insured";
	
	public static final String SET_DB_DETAILS_TO_REFERENCE_DATA = "PA_preauth_set_claim_and_sublimit_db_details";

	public static final String GET_DIAGNOSIS_NAME = "PA_preauth_get_diagnosis_name";
	
	public static final String PREAUTH_COORDINATOR_REQUEST_EVENT = "PA_preauth_coordinator_request";
	
	public static final String GET_HOSPITALIZATION_DETAILS = "PA_preauth_get_hospitalization_details";
	
	public static final String VALIDATE_PREAUTH_USER_RRC_REQUEST = "PA_preauth_user_rrc_request";
	
	public static final String PREAUTH_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "PA_preauth_load_rrc_request_drop_down_values";
	
	public static final String PREAUTH_SAVE_RRC_REQUEST_VALUES = "PA_preauth_save_rrc_request_values";
	
	public static final String PREAUTH_HOSPITALISATION_DUE_TO = "PA_pre_auth_hospitalization_due_to";

	public static final String PRE_AUTH_REPORTED_TO_POLICE = "PA_pre_auth_reported_to_police";
	
	public static final String RECHARGE_SI_FOR_PRODUCT = "PA_recharging SI for product in Preauth";
	
	public static final String GET_SEC_COVER = "PA_pre_auth_get_sec_cover";
	
	public static final String GET_SUB_COVER = "PA_pre_auth_get_sub_cover";

	public static final String PA_PREAUTH_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "pa_preauth_load_rrc_request_sub_category_values";

	public static final String PA_PREAUTH_LOAD_RRC_REQUEST_SOURCE_VALUES = "pa_preauth_load_rrc_request_source_values";

	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	
	public void coordinatorRequestEvent(
			@Observes @CDIEvent(PREAUTH_COORDINATOR_REQUEST_EVENT) final ParameterDTO parameters) {
				view.intiateCoordinatorRequest();
		}
	
	public void activeStepChanged(@Observes @CDIEvent(PREAUTH_STEP_CHANGE_EVENT) final ParameterDTO parameters) {
		view.setWizardPageReferenceData(referenceData);
	}
	
	public void saveWizard(@Observes @CDIEvent(PREAUTH_SAVE_EVENT) final ParameterDTO parameters) 
	{
		
	}
	
	public void submitWizard(
			@Observes @CDIEvent(PREAUTH_SUBMITTED_EVENT) final ParameterDTO parameters)throws Exception {
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Preauth preauth = preauthService.submitPreAuth(preauthDTO, false);
		if(preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS))) {
			dbCalculationService.invokeAccumulatorProcedure(preauth.getKey());
//			preauthService.callBPMRemainderTask(preauthDTO,preauth, preauthDTO.getStrUserName(), preauthDTO.getStrPassword());
			
			Hospitals hospitalByKey = hospitalService.getHospitalByKey(preauth.getClaim().getIntimation().getHospital());
			
			Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(preauth.getClaim(), hospitalByKey);
			
			Object[] inputArray = (Object[])arrayListForDBCall[0];
			
			inputArray[SHAConstants.INDEX_REMINDER_CATEGORY] = SHAConstants.PREAUTH_BILLS_NOT_RECEIVED;
			inputArray[SHAConstants.INDEX_CASHLESS_KEY] = preauth.getKey();
			inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.OUTCOME_PREAUTH_INITIATE_REMINDER_PROCESS;
			
			dbCalculationService.stopReminderProcessProcedure(preauth.getClaim().getIntimation().getIntimationId(),SHAConstants.OTHERS);
			
			preauthService.callReminderTaskForDB(inputArray);
			
		}
		
		if(preauth.getStatus() != null && preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS)) {
			String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
			if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
				PremiaService.getInstance().UnLockPolicy(preauth.getClaim().getIntimation().getIntimationId());
			}
		}
		
		
		String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
		try {
			if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
				if(preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_DOWNSIZE_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_REFER_TO_COORDINATOR_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS)) {
					Claim claimByKey = preauthService.searchByClaimKey(preauth.getClaim().getKey());
					Hospitals hospitalObject = preauthService.getHospitalObject(claimByKey.getIntimation().getHospital());
					String provisioningamt = String.valueOf(preauth.getTotalApprovalAmount().longValue());
					if(preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS) ||
							preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)
							|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS)) {
						provisioningamt = claimByKey.getCurrentProvisionAmount() != null ? String.valueOf(claimByKey.getCurrentProvisionAmount().longValue()) : (claimByKey.getProvisionAmount() != null ? String.valueOf(claimByKey.getProvisionAmount().longValue()) : "0");
					}
					String provisionAmtInput = SHAUtils.getProvisionAmtInput(preauth.getClaim(), hospitalObject.getName(), provisioningamt);
					PremiaService.getInstance().updateProvisionAmount(provisionAmtInput);
				}
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());

		}
		
		preauthService.setBPMOutcome(preauthDTO, preauth, false);
		view.buildSuccessLayout();	
	}
	
	public void checkCriticalIllness(
			@Observes @CDIEvent(CHECK_CRITICAL_ILLNESS) final ParameterDTO parameters) {
		Boolean checkValue = (Boolean) parameters.getPrimaryParameter();
		view.editSpecifyVisibility(checkValue);
	}
	
	public void setUpReferenceObjectForEdit(@Observes @CDIEvent(SETUP_REFERENCE_DATA) final ParameterDTO parameters)
	{
		PreauthDTO bean = (PreauthDTO) parameters.getPrimaryParameter();
		setReferece(bean);
		
		if(bean != null) {
			
			referenceData.put("sectionDetails", masterService
					.getSectionList(bean.getNewIntimationDTO().getPolicy().getProduct().getKey(),bean.getNewIntimationDTO().getPolicy()));
		}
		referenceData.put("coPayType", masterService.getSelectValueContainer(ReferenceTable.JIO_COPAY_TYPE_VALUE));
		view.setWizardPageReferenceData(referenceData);
	}
	
	public void setReferece(PreauthDTO bean)
	{
		referenceData.put("treatmentType", masterService.getSelectValueContainer(ReferenceTable.TREATMENT_MANAGEMENT));
		referenceData.put("roomCategory", masterService.getSelectValueContainer(ReferenceTable.ROOM_CATEGORY));
		referenceData.put("natureOfTreatment", masterService.getSelectValueContainer(ReferenceTable.NATURE_OF_TREATMENT));
		referenceData.put("diagnosisName", masterService.getDiagnosisList());
		referenceData.put("sumInsured", masterService.getSelectValueContainer(ReferenceTable.SUM_INSURED));
		referenceData.put("commonValues", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("coordinatorTypeRequest", masterService.getSelectValueContainer(ReferenceTable.COORDINATOR_REQUEST_TYPE));
		referenceData.put("patientStatus", masterService.getSelectValueContainer(ReferenceTable.PATIENT_STATUS));
		referenceData.put("icdChapter", masterService.getSelectValuesForICDChapter());
//		referenceData.put("icdBlock", masterService.getSelectValuesForICDBlock());
//		referenceData.put("icdCode", masterService.getSelectValuesForICDCode());
		referenceData.put("illness", masterService.getSelectValueContainer(ReferenceTable.ILLNESS));
		referenceData.put("procedureName", preauthService.getProcedureListNames());
		referenceData.put("procedureCode", preauthService.getProcedureListNames());
		referenceData.put("medicalSpeciality", preauthService.getSpecialityType("M"));
		referenceData.put("surgicalSpeciality", preauthService.getSpecialityType("S"));	
		referenceData.put("sublimitApplicable", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("considerForPayment", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("considerForDayCare", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("commonValues", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("pedExclusionImpactOnDiagnosis",masterService.getSelectValueContainer(ReferenceTable.IMPACT_ON_DIAGNOSI));
		referenceData.put("exclusionDetails",masterService.getSelectValueContainer(ReferenceTable.EXCLUSION_DETAILS));
		referenceData.put("procedureStatus",masterService.getSelectValueContainer(ReferenceTable.PROCEDURE_STATUS));
		//referenceData.put("pedList", masterService.getInsuredPedDetails());
		referenceData.put("fvrNotRequiredRemarks", masterService.getSelectValueContainer(ReferenceTable.FVR_NOT_REQUIRED_REMARKS));
		referenceData.put("rejectionCategory", masterService.getRejectionCategoryByValue());
//												masterService.getSelectValueContainer(ReferenceTable.REJECTION_CATEGORY));
		referenceData.put("sendForProcessingCategory", masterService.getRejectionCategoryByValue()); 
//														masterService.getSelectValueContainer(ReferenceTable.REJECTION_CATEGORY));
		referenceData.put("terminateCover", masterService.getSelectValueContainer(ReferenceTable.TERMINATE_COVER));
		referenceData.put("criticalIllness", preauthService.getCriticalIllenssMasterValues(bean));
		referenceData.put("sumInsuredRestriction",  masterService.getSelectValueContainer(ReferenceTable.SUM_INSURED));
		
		// Preauth Only..
		referenceData.put("investigatorName", masterService.getInvestigation());
		
		referenceData.put("hospitalisationDueTo", masterService.getSelectValueContainer(ReferenceTable.HOSPITALIZATION_DUE_TO));
		referenceData.put("causeOfInjury", masterService.getSelectValueContainer(ReferenceTable.CAUSE_OF_INJURY));
		referenceData.put("section", masterService
				.getSelectValueContainer(ReferenceTable.SECTION));
		referenceData.put("typeOfDelivery", masterService
				.getSelectValueContainer(ReferenceTable.DELIVERY_TYPE));
		referenceData.put("natureOfLoss", masterService
				.getNatureCauseLossSelectValueContainer(SHAConstants.PA_LOB, ReferenceTable.NATURE_OF_LOSS));
		referenceData.put("causeOfLoss", masterService
				.getNatureCauseLossSelectValueContainer(SHAConstants.PA_LOB, ReferenceTable.CAUSE_OF_LOSS));
		referenceData.put("catastrophicLoss", masterService.getCatastrophicLossList());

		referenceData.put("behaviourHosCombValue", masterService.getBehvrHospSelectValueContainer(ReferenceTable.BEHAVIOUR_OF_HOSP));
		
	}
	
	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_PREAUTH_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	

	public void saveRRCRequestValues(@Observes @CDIEvent(PREAUTH_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_CASHLESS);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(PREAUTH_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	/**
	 * Added for RRC Ends
	 * */
	
	public void generateFieldsBasedOnTreatement(@Observes @CDIEvent(PREAUTH_TREATMENT_TYPE_CHANGED) final ParameterDTO parameters)
	{
		view.generateFieldsBasedOnTreatment();
	}
	
	public void generateFieldsBasedOnPatientStatus(@Observes @CDIEvent(PREAUTH_PATIENT_STATUS_CHANGED) final ParameterDTO parameters)
	{
		view.genertateFieldsBasedOnPatientStaus();
	}
	
	public void generateFieldsBasedOnRelapseIllness(@Observes @CDIEvent(PREAUTH_RELAPSE_OF_ILLNESS_CHANGED) final ParameterDTO parameters)
	{
		PreauthDTO preauthDto = (PreauthDTO) parameters.getPrimaryParameter();
		SelectValue relpaseofIllness = parameters.getSecondaryParameters() != null ? (SelectValue) parameters.getSecondaryParameters()[0] : null;
	
		if(relpaseofIllness != null && relpaseofIllness.getId() == ReferenceTable.COMMONMASTER_YES){
			BeanItemContainer<SelectValue> relapseClaimContainer = claimService.getRelpseClaimsForPolicy(preauthDto.getPolicyDto().getPolicyNumber() , preauthDto.getClaimNumber());
			referenceData.put("relapsedClaims", relapseClaimContainer);
		}
		view.genertateFieldsBasedOnRelapseOfIllness(referenceData);
	}
	
	public void generatePreAuthReferCoOrdinatorLayout(@Observes @CDIEvent(PREAUTH_REFERCOORDINATOR_EVENT) final ParameterDTO parameters)
	{
		view.generateReferCoOrdinatorLayout();
	}
	
	public void generateFieldsForQuery(@Observes @CDIEvent(PREAUTH_QUERY_EVENT) final ParameterDTO parameters)
	{
		view.generateFieldsOnQueryClick();
	}

	
	public void generateFieldsForSuggestRejection(@Observes @CDIEvent(PREAUTH_SUGGEST_REJECTION_EVENT) final ParameterDTO parameters)
	{
		view.generateFieldsOnSuggestRejectionClick();
	}

	
	public void generateFieldsForSendForProcessing(@Observes @CDIEvent(PREAUTH_SEND_FOR_PROCESSING_EVENT) final ParameterDTO parameters)
	{
		view.generateFieldsOnSendForProcessingClick();
	}
	
	public void searchState(@Observes @CDIEvent(MASTER_SEARCH_STATE) final ParameterDTO parameters)
	{
		String q = (String) parameters.getPrimaryParameter();
		List<State> stateSearch = masterService.stateSearch(q);
		view.searchState(stateSearch);
	}
	
	public void getIcdBlock(@Observes @CDIEvent(GET_ICD_BLOCK) final ParameterDTO parameters)
	{
		Long chapterKey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue> icdBlockContainer = masterService.searchIcdBlockByChapterKey(chapterKey);
		
		view.setIcdBlock(icdBlockContainer);
	}
	
	public void getIcdCode(@Observes @CDIEvent(GET_ICD_CODE) final ParameterDTO parameters)
	{
		Long blockKey = (Long) parameters.getPrimaryParameter();
		/*R20181279 - Commented Below Line
		BeanItemContainer<SelectValue> icdCodeContainer = masterService.searchIcdCodeByBlockKey(blockKey);*/
		/*BeanItemContainer<SelectValue> icdCodeContainer = masterService
				.getIcdCodes();
		view.setIcdCode(icdCodeContainer);*/
	}

	public void generatePreAuthApproveLayout(@Observes @CDIEvent(PREAUTH_APPROVE_EVENT) final ParameterDTO parameters)
	{
		view.generatePreauthApproveLayout();
	}
	
	public void generatePreAuthQueryLayout(@Observes @CDIEvent(PREAUTH_QUERY_BUTTON_EVENT) final ParameterDTO parameters)
	{
		view.generateQueryLayout();
	}
	
	public void generatePreAuthRejectLayout(@Observes @CDIEvent(PREAUTH_REJECTION_EVENT) final ParameterDTO parameters)
	{
		
		view.generateRejectionLayout(masterService.getRejectionCategoryByValue());
//										masterService.getSelectValueContainer(ReferenceTable.REJECTION_CATEGORY));
	}
	
	public void generatePreAuthDenialLayout(@Observes @CDIEvent(PREAUTH_DENIAL_EVENT) final ParameterDTO parameters)
	{
		view.generateDenialLayout(masterService.getSelectValueContainer(ReferenceTable.DENIAL_REASON));
	}
	
	public void generatePreAuthEscalateLayout(@Observes @CDIEvent(PREAUTH_ESCALATE_EVENT) final ParameterDTO parameters)
	{
		view.generateEscalateLayout(masterService.getSelectValueContainer(ReferenceTable.ESCALATE_TO));
	}

	public void generateFieldsBasedOnFieldVisit(@Observes @CDIEvent(FIELD_VISIT_RADIO_CHANGED) final ParameterDTO parameters)
	{
		Boolean isChecked = (Boolean) parameters.getPrimaryParameter();		
		view.genertateFieldsBasedOnFieldVisit(isChecked, masterService.getSelectValueContainer(ReferenceTable.ALLOCATION_TO),masterService.getSelectValueContainer(ReferenceTable.ASSIGN_TO)
				,masterService.getSelectValueContainer(ReferenceTable.FVR_PRIORITY));
	}
	
	public void generateFieldsBasedOnSpecialist(@Observes @CDIEvent(SPECIALIST_OPINION_RADIO_CHANGED) final ParameterDTO parameters)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("specialistConsulted", masterService.getSelectValueContainerForSpecialist());
		map.put("specialistType", masterService.getSelectValueContainer(ReferenceTable.SPECIALIST_TYPE));
		Boolean isChecked = (Boolean) parameters.getPrimaryParameter();
		view.genertateFieldsBasedOnSpecialistChecked(isChecked, map);
	}
	
	public void getPreviousClaimDetails(
			@Observes @CDIEvent(PREAUTH_PREVIOUS_CLAIM_DETAILS) final ParameterDTO parameters) {

		PreauthDTO preauthDto = (PreauthDTO) parameters.getPrimaryParameter();
		if (preauthDto != null
				&& preauthDto.getNewIntimationDTO() != null
				&& preauthDto.getNewIntimationDTO().getInsuredPatient() != null
				&& preauthDto.getNewIntimationDTO().getInsuredPatient()
						.getInsuredId() != null) {
			Long policyNumberOrInsuredId = preauthDto.getNewIntimationDTO()
					.getInsuredPatient().getInsuredId();

//			List<ViewTmpClaim> claimList = claimService
//					.getTmpClaimsByInsuredId(policyNumberOrInsuredId);
		/*	System.out.println("--the current claim id---"
					+ preauthDto.getClaimDTO().getClaimId());
			
			List<ViewTmpClaim> claimList = new ArrayList<ViewTmpClaim>();

			try{

				List<ViewTmpClaim> claimsByPolicyNumber = claimService
						.getViewTmpClaimsByPolicyNumber(preauthDto.getNewIntimationDTO().getPolicy().getPolicyNumber());
				
				for (ViewTmpClaim viewTmpClaim : claimsByPolicyNumber) {
					if(preauthDto.getNewIntimationDTO().getInsuredPatient().getHealthCardNumber().equalsIgnoreCase(viewTmpClaim.getIntimation().getInsured().getHealthCardNumber())){
						claimList.add(viewTmpClaim);
					}
				}
				
				claimList = getPreviousClaimInsuedWiseForPreviousPolicy(preauthDto.getNewIntimationDTO().getPolicy().getRenewalPolicyNumber(), 
						claimList,preauthDto.getNewIntimationDTO().getInsuredPatient().getHealthCardNumber());
				
			}catch(Exception e){
				e.printStackTrace();
			}

			List<PreviousClaimsTableDTO> previousClaimDTOList = preauthService
					.getPreviousClaims(claimList,
							preauthDto.getClaimDTO()
						.getClaimId());*/
			
			DBCalculationService dbCalculationService = new DBCalculationService();
			List<PreviousClaimsTableDTO> previousClaimDTOList = dbCalculationService.getPreviousClaims(preauthDto.getClaimDTO().getKey(), preauthDto.getNewIntimationDTO().getPolicy().getKey(), 
					preauthDto.getNewIntimationDTO().getInsuredPatient().getKey(), SHAConstants.INSURED_WISE_SEARCH_TYPE);
			
			

			for(PreviousClaimsTableDTO previousClaimsTableDTO : previousClaimDTOList){
				if(previousClaimsTableDTO.getAdmissionDate()!=null && !previousClaimsTableDTO.getAdmissionDate().equals("")){
//					Date tempDate = SHAUtils.formatTimestamp(previousClaimsTableDTO.getAdmissionDate());
//					previousClaimsTableDTO.setAdmissionDate((SHAUtils.formatDate(tempDate)));
				}												
			}

			view.getPreviousClaimDetails(previousClaimDTOList);
		}
	}
	
	public List<ViewTmpClaim> getPreviousClaimInsuedWiseForPreviousPolicy(String policyNumber, List<ViewTmpClaim> generatedList,String healthCardNumber) {
		
		try {
			Policy renewalPolNo = policyService.getByPolicyNumber(policyNumber);
			if(renewalPolNo != null) {
				List<ViewTmpClaim> previousPolicyPreviousClaims = claimService.getViewTmpClaimsByPolicyNumber(renewalPolNo.getPolicyNumber());
				if(previousPolicyPreviousClaims != null && !previousPolicyPreviousClaims.isEmpty()) {
					for (ViewTmpClaim viewTmpClaim : previousPolicyPreviousClaims) {
						if(viewTmpClaim.getIntimation().getInsured().getHealthCardNumber().equalsIgnoreCase(healthCardNumber)){
							if(!generatedList.contains(viewTmpClaim)) {
								generatedList.add(viewTmpClaim);
							}
						}
					}
				}
				if(renewalPolNo != null && renewalPolNo.getRenewalPolicyNumber() != null ) {
					getPreviousClaimInsuedWiseForPreviousPolicy(renewalPolNo.getRenewalPolicyNumber(), generatedList,healthCardNumber);
				} else {
					return generatedList;
				}
			}
		} catch(Exception e) {
		
	}
	return generatedList;
}
	
	public void sentTOCPUChecked(
			@Observes @CDIEvent(PREAUTH_SENT_TO_CPU_SELECTED) final ParameterDTO parameters) {
		Boolean isChecked = (Boolean) parameters.getPrimaryParameter();
		view.generateFieldsBasedOnSentTOCPU(isChecked);
	}
	
	public void viewClaimAmountDetails(
			@Observes @CDIEvent(VIEW_CLAIMED_AMOUNT_DETAILS) final ParameterDTO parameters) {
		view.viewClaimAmountDetails();
	}
	
	public void viewBalanceSumInsured(
			@Observes @CDIEvent(VIEW_BALANCE_SUM_INSURED_DETAILS) final ParameterDTO parameters) {
		String intimationId = (String) parameters.getPrimaryParameter();
		if(intimationId!=null && !intimationId.equals("")){
			view.viewBalanceSumInsured(intimationId);
		}
		
	}
	
	public void getExclusionDetails(@Observes @CDIEvent(GET_EXCLUSION_DETAILS) final ParameterDTO parameters)
	{
		Long impactDiagnosisKey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<ExclusionDetails> icdCodeContainer = masterService.getExclusionDetailsByImpactKey(impactDiagnosisKey);
		
		view.setExclusionDetails(icdCodeContainer);
	}
	
	public void getPackageRate(@Observes @CDIEvent(PREAUTH_GET_PACKAGE_RATE) final ParameterDTO parameters)
	{
		Long procedureKey = (Long) parameters.getPrimaryParameter();
//		String ProcedureCode = (String) parameters.getSecondaryParameters()[0];
		String hosptialCode = (String) parameters.getSecondaryParameters()[0];
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getSecondaryParameters()[1];
		
		ProcedureMaster procedureMaster = masterService.getProcedureByKey(procedureKey);
		
		String procedureCode = procedureMaster.getProcedureCode();
		
		Long roomCategoryId = 0l;
		
		SelectValue roomCategory = preauthDTO.getNewIntimationDTO().getRoomCategory();
		if(roomCategory != null){
			roomCategoryId = roomCategory.getId();
		}
		
		List<HospitalPackage> packageMaster = masterService.getPackageRateByProcedureAndHospitalKey(procedureCode, hosptialCode,roomCategoryId);
		
		
		Map<String, String>  mappedValues = new HashMap<String, String>();
		mappedValues.put("packageRate",
				packageMaster != null ? !packageMaster.isEmpty() ? packageMaster.get(0).getRate()
						.toString() : null : null);
		mappedValues.put("dayCareProcedure", procedureMaster.getDayCareFlag().equalsIgnoreCase("y") ? "Yes" : "No" );
		mappedValues.put("procedureCode", procedureMaster.getProcedureCode());
		view.setPackageRate(mappedValues);
	}
	
	public void getSumInsuredInfoFromDB(@Observes @CDIEvent(SUM_INSURED_CALCULATION) final ParameterDTO parameters) {
		 Map<String, Object> values = (Map<String, Object>) parameters.getPrimaryParameter();
		String diagnosis = null;
		 PreauthDTO preauthDTO = (PreauthDTO) parameters.getSecondaryParameter(0, PreauthDTO.class);
		if(values.containsKey("diagnosisId")) {
			diagnosis = masterService.getDiagnosis(Long.valueOf((String) values.get("diagnosisId")));
		}
		
		Map<String, Object> medicalDecisionTableValue = dbCalculationService.getMedicalDecisionTableValue(values,preauthDTO.getNewIntimationDTO());
		if(values.containsKey(SHAConstants.IS_NON_ALLOPATHIC) && (Boolean)values.get(SHAConstants.IS_NON_ALLOPATHIC)) {
			Map<String, Double> nonAllopathicAmount = dbCalculationService.getNonAllopathicAmount((Long)values.get(SHAConstants.POLICY_KEY), (Long)values.get(SHAConstants.INSURED_KEY),0l,"0", (Long)values.get(SHAConstants.CLAIM_KEY));
			medicalDecisionTableValue.put(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT, nonAllopathicAmount.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT));
			medicalDecisionTableValue.put(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT, nonAllopathicAmount.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT));
		}
		
		view.setDiagnosisSumInsuredValuesFromDB(medicalDecisionTableValue, diagnosis);
	}
	
	public void getDiagnosisName(@Observes @CDIEvent(GET_DIAGNOSIS_NAME) final ParameterDTO parameters) {
		Long diagnosisId = (Long) parameters.getPrimaryParameter();
		String diagnosis = masterService.getDiagnosis(diagnosisId);
		view.setDiagnosisName(diagnosis);
	}
	
	public void ruleForCheckInvestigation(@Observes @CDIEvent(CHECK_INVESTIGATION_INITIATED) final ParameterDTO parameters) {
		Long claimKey = (Long) parameters.getPrimaryParameter();
		Investigation checkInitiateInvestigation = preauthService.checkInitiateInvestigation(claimKey);
		view.setInvestigationRule(checkInitiateInvestigation);
	}
	
	public void setBalanceSumInsured(@Observes @CDIEvent(BALANCE_SUM_INSURED) final ParameterDTO parameters) 
	{
		/**
		 * Since BalanceSI procedure requires insured key as one parameter,
		 * now intimation dto is passed instead of policy DTO and insured key is
		 * obtained from this new intimation dto.
		 * */
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		NewIntimationDto intimationDTO = preauthDTO.getNewIntimationDTO();
	//	PolicyDto policyDTO = (PolicyDto) parameters.getPrimaryParameter();
		//Double balanceSI = dbCalculationService.getBalanceSI(policyDTO.getKey(), policyDTO.getTotalSumInsured());
		
		//Integer insuredSumInsured = dbCalculationService.getInsuredSumInsured(intimationDTO.getPolicy().getKey(), intimationDTO.getInsuredPatient().getInsuredId().toString());
		Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(intimationDTO.getInsuredPatient().getInsuredId().toString(), intimationDTO.getPolicy().getKey()
				,intimationDTO.getInsuredPatient().getLopFlag());

		
		//Double balanceSI = dbCalculationService.getBalanceSI(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(), intimationDTO.getPolicy().getTotalSumInsured());
		//Double balanceSI = dbCalculationService.getBalanceSI(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(), Double.valueOf(insuredSumInsured.toString()));
		Double balanceSI = dbCalculationService.getBalanceSIForPAHealth(intimationDTO.getInsuredPatient().getKey(),preauthDTO.getClaimKey(),preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey(),ReferenceTable.HOSPITALIZATION_BENEFITS).get(SHAConstants.TOTAL_BALANCE_SI);
		List<Double> copayValue = dbCalculationService.getProductCoPay(intimationDTO.getPolicy().getProduct().getKey() ,intimationDTO.getInsuredPatient().getKey(), intimationDTO.getInsuredPatient().getInsuredId(),intimationDTO);
		//Double balanceSI = new Double("9999");
		view.setBalanceSumInsured(balanceSI, copayValue);
	}
	
	private String getInsuredAge(Date insuredDob) {
		Calendar dob = Calendar.getInstance();
		 String insuredAge = "0";
		if (insuredDob != null) {
			dob.setTime(insuredDob);
			Calendar today = Calendar.getInstance();
			int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
			insuredAge = String.valueOf(age);
		}
		return insuredAge;
	}

	
	public void setDBDetailsToReferenceData(@Observes @CDIEvent(SET_DB_DETAILS_TO_REFERENCE_DATA) final ParameterDTO parameters) {
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Hospitals hospitalById = hospitalService.getHospitalById(preauthDTO.getHospitalKey());
		PolicyDto policyDTO = preauthDTO.getPolicyDto();
		//String insuredAge = getInsuredAge(policyDTO.getInsuredDob());
		Double insuredAge = preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredAge();

		/**
		 * InsuredSumInsured calculation is now enabled via procedure. Hence, the total sum insured present
		 * in DB will not be used unless or until the procedure returns 0. Hence commenting above code and
		 * adding below line.
		 * */
	//	Integer sumInsured = dbCalculationService.getInsuredSumInsured(preauthDTO.getPolicyDto().getKey(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString());
		Double sumInsured = 0d;
		if(null != preauthDTO.getNewIntimationDTO() && null != preauthDTO.getNewIntimationDTO().getPolicy() && 
				null != preauthDTO.getNewIntimationDTO().getPolicy().getProduct() && 
				null != preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
				!(ReferenceTable.getGPAProducts().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
		
		 sumInsured = dbCalculationService.getInsuredSumInsured(preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), preauthDTO.getPolicyDto().getKey()
				 ,preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		}
		else
		{
			sumInsured = dbCalculationService.getGPAInsuredSumInsured(preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), preauthDTO.getPolicyDto().getKey());
		}
		/*if (policyDTO.getInsuredSumInsured() == 0) {
			sumInsured = policyDTO.getTotalSumInsured();
		}*/
		if (sumInsured == 0) {
			sumInsured = policyDTO.getTotalSumInsured();
		}
//		referenceData.put("policyAgeing", dbCalculationService.getPolicyAgeing(policyDTO.getAdmissionDate(), policyDTO.getPolicyNumber()));
		referenceData.put("policyAgeing",preauthDTO.getNewIntimationDTO().getPolicyYear() != null ? preauthDTO.getNewIntimationDTO().getPolicyYear() : "");
		
		
		if(preauthDTO.getPreauthDataExtractionDetails().getSection() != null){
			
			referenceData.put("sublimitDBDetails", dbCalculationService
						.getClaimedAmountDetailsForSection(policyDTO.getProduct().getKey(),
								sumInsured, 0l, insuredAge,preauthDTO.getPreauthDataExtractionDetails().getSection().getId(),
								preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0", (preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : preauthDTO.getClaimDTO().getClaimSubCoverCode()),preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
				
			}else{
				referenceData.put("sublimitDBDetails", dbCalculationService
						.getClaimedAmountDetailsForSection(policyDTO.getProduct().getKey(),
								sumInsured, 0l, insuredAge,0l,preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0", (preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : preauthDTO.getClaimDTO().getClaimSubCoverCode()),preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
			}
		
		
//		referenceData.put("sublimitDBDetails", dbCalculationService.getClaimedAmountDetails(policyDTO.getProduct().getKey(), sumInsured, insuredAge));
		Map<Integer, Object> hospitalizationDetails = new HashMap<Integer, Object>();
		
		if(preauthDTO.getPreauthDataExtractionDetails().getSection() != null){
			
			hospitalizationDetails = dbCalculationService.getHospitalizationDetails(policyDTO.getProduct().getKey(),
					sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),preauthDTO.getPreauthDataExtractionDetails().getSection().getId(),"A");
		}else{
			
			hospitalizationDetails = dbCalculationService.getHospitalizationDetails(policyDTO.getProduct().getKey(),
					sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),0l,"0");
			
		}
		
		referenceData.put("claimDBDetails", hospitalizationDetails);
		//referenceData.put("insuredPedList", masterService.getInusredPEDList(policyDTO.getInsuredId()));
				/**
				 * Insured id will be obtained from intimation dto and not from policy dto. This is due
				 * to the change in policy related tables during policy table refractoring activity.
				 * */
		referenceData.put("insuredPedList", masterService.getInusredPEDList(preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString()));
		List<Double> copayValue = dbCalculationService.getProductCoPay(
				preauthDTO.getNewIntimationDTO().getPolicy().getProduct()
						.getKey(), preauthDTO.getNewIntimationDTO()
						.getInsuredPatient().getKey(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(),preauthDTO.getNewIntimationDTO());
		preauthDTO.setProductCopay(copayValue);
		referenceData.put("copay", getCopayValues(preauthDTO));
	}
	
	private BeanItemContainer<SelectValue> getCopayValues(PreauthDTO dto) {
		 BeanItemContainer<SelectValue> coPayContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		 	List<String> coPayPercentage = new ArrayList<String>();
		    for (Double string : dto.getProductCopay()) {
		    	coPayPercentage.add(String.valueOf(string.intValue()));
			}
		    
		    Long i = 0l;
		    for (String string : coPayPercentage) {
		    	SelectValue value = new SelectValue();
		    	value.setId(Long.valueOf(string));
		    	value.setValue(string);
		    	coPayContainer.addBean(value);
		    	i++;
			}
		    
		    return coPayContainer;
	}
	
	
	public void getHospitalizationDetails(
			@Observes @CDIEvent(GET_HOSPITALIZATION_DETAILS) final ParameterDTO parameters) {
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Hospitals hospitalById = hospitalService.getHospitalById(preauthDTO.getHospitalKey());
		PolicyDto policyDTO = preauthDTO.getPolicyDto();
		//String insuredAge = getInsuredAge(policyDTO.getInsuredDob());
		Double sumInsured = 0d;
		if(null != preauthDTO.getNewIntimationDTO() && null != preauthDTO.getNewIntimationDTO().getPolicy() && 
				null != preauthDTO.getNewIntimationDTO().getPolicy().getProduct() && 
				null != preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
				!(ReferenceTable.getGPAProducts().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
		 sumInsured = dbCalculationService.getInsuredSumInsured(preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), preauthDTO.getPolicyDto().getKey()
				 ,preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		}
		else
		{
			 sumInsured = dbCalculationService.getGPAInsuredSumInsured(preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), preauthDTO.getPolicyDto().getKey());
		}
		if (sumInsured == 0) {
			sumInsured = policyDTO.getTotalSumInsured();
		}
		
        Map<Integer, Object> hospitalizationDetails = new HashMap<Integer, Object>();
		
		if(preauthDTO.getPreauthDataExtractionDetails().getSection() != null){
			
			hospitalizationDetails = dbCalculationService.getHospitalizationDetails(policyDTO.getProduct().getKey(),
					sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),preauthDTO.getPreauthDataExtractionDetails().getSection().getId(),"A");
		}else{
			
			hospitalizationDetails = dbCalculationService.getHospitalizationDetails(policyDTO.getProduct().getKey(),
					sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),0l,"0");
			
		}
		
		referenceData.put("claimDBDetails", hospitalizationDetails);
		view.setHospitalizationDetails(hospitalizationDetails);
		
	}
	
	public void generateFieldsBasedOnHospitalisationDueTo(@Observes @CDIEvent(PREAUTH_HOSPITALISATION_DUE_TO) final ParameterDTO parameters)
	{
		SelectValue selectedValue = (SelectValue) parameters.getPrimaryParameter();
		view.genertateFieldsBasedOnHospitalisionDueTo(selectedValue);
	}
	
	public void generateFieldsBasedOnReportedToPolice(@Observes @CDIEvent(PRE_AUTH_REPORTED_TO_POLICE) final ParameterDTO parameters)
	{
		Boolean selectedValue = (Boolean) parameters.getPrimaryParameter();
		view.genertateFieldsBasedOnReportedToPolice(selectedValue);
	}
	
	public void setSublimitValues(
			@Observes @CDIEvent(SUBLIMIT_CHANGED_BY_SECTION) final ParameterDTO parameters) {
		
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		
		Hospitals hospitalById = hospitalService.getHospitalById(preauthDTO.getHospitalKey());
		PolicyDto policyDTO = preauthDTO.getPolicyDto();
	//	String insuredAge = getInsuredAge(policyDTO.getInsuredDob());
		Double insuredAge = preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredAge();
		System.out.println("----age insured000"+insuredAge);
		//Double sumInsured = policyDTO.getInsuredSumInsured();
		/**
		 * InsuredSumInsured calculation is now enabled via procedure. Hence, the total sum insured present
		 * in DB will not be used unless or until the procedure returns 0. Hence commenting above code and
		 * adding below line.
		 * */
	//	Integer sumInsured = dbCalculationService.getInsuredSumInsured(preauthDTO.getPolicyDto().getKey(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString());
		Double sumInsured = 0d;
		if(null != preauthDTO.getNewIntimationDTO() && null != preauthDTO.getNewIntimationDTO().getPolicy() && 
				null != preauthDTO.getNewIntimationDTO().getPolicy().getProduct() && 
				null != preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
				!(ReferenceTable.getGPAProducts().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
		
		 sumInsured = dbCalculationService.getInsuredSumInsured(preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), preauthDTO.getPolicyDto().getKey()
				 ,preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		}
		else
		{
			sumInsured = dbCalculationService.getGPAInsuredSumInsured(preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), preauthDTO.getPolicyDto().getKey());
		}
		
		/*if (policyDTO.getInsuredSumInsured() == 0) {
			sumInsured = policyDTO.getTotalSumInsured();
		}*/
		if (sumInsured == 0) {
			sumInsured = policyDTO.getTotalSumInsured();
		}
		
		if(preauthDTO.getPreauthDataExtractionDetails().getSection() != null){
			
			referenceData.put("sublimitDBDetails", dbCalculationService
						.getClaimedAmountDetailsForSection(policyDTO.getProduct().getKey(),
								sumInsured, 0l, insuredAge,preauthDTO.getPreauthDataExtractionDetails().getSection().getId(),
								preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0", (preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : preauthDTO.getClaimDTO().getClaimSubCoverCode()),preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
				
			}else{
				referenceData.put("sublimitDBDetails", dbCalculationService
						.getClaimedAmountDetailsForSection(policyDTO.getProduct().getKey(),
								sumInsured, 0l, insuredAge,0l,preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0", (preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : preauthDTO.getClaimDTO().getClaimSubCoverCode()),preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
			}
		
		 Map<Integer, Object> hospitalizationDetails = new HashMap<Integer, Object>();
			
			if(preauthDTO.getPreauthDataExtractionDetails().getSection() != null){
				
				hospitalizationDetails = dbCalculationService.getHospitalizationDetails(policyDTO.getProduct().getKey(),
						sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),preauthDTO.getPreauthDataExtractionDetails().getSection().getId(),"A");
			}else{
				
				hospitalizationDetails = dbCalculationService.getHospitalizationDetails(policyDTO.getProduct().getKey(),
						sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),0l,"0");
				
			}
			
			referenceData.put("claimDBDetails", hospitalizationDetails);
		
//		view.setWizardPageReferenceData(referenceData);
		
	}
	
	public void rechargingSIEvent(
			@Observes @CDIEvent(RECHARGE_SI_FOR_PRODUCT) final ParameterDTO parameters) {
				PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
				
				Long policyKey = preauthDTO.getNewIntimationDTO().getPolicy().getKey();
				Long insuredKey = preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey();
				
				dbCalculationService.rechargingSIbasedOnProduct(policyKey, insuredKey);
				
				Double balanceSI = dbCalculationService.getBalanceSIForPAHealth(
						insuredKey, preauthDTO.getClaimDTO().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey(),ReferenceTable.HOSPITALIZATION_BENEFITS).get(SHAConstants.TOTAL_BALANCE_SI);
				
//				Double balanceSI = 500000d;
				
				
				view.setBalanceSIforRechargedProcess(balanceSI);
				
		}
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void getSecCover(
			@Observes @CDIEvent(GET_SEC_COVER) final ParameterDTO parameters) {
		Long sectionKey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue> coverContainer = masterService
				.getCoverList(sectionKey);

		view.setCoverList(coverContainer);
	}
	
	public void getSubCover(
			@Observes @CDIEvent(GET_SUB_COVER) final ParameterDTO parameters) {
		Long coverKey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue> subCoverContainer = masterService
				.getSubCoverList(coverKey);

		view.setSubCoverList(subCoverContainer);
	}
	
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(PA_PREAUTH_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(PA_PREAUTH_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}


}
