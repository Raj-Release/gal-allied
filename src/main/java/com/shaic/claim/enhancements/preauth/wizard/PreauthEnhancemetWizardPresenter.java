package com.shaic.claim.enhancements.preauth.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

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
import com.shaic.claim.enhacement.table.PreviousPreAuthService;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.fvrdetails.view.ViewFVRService;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.FvrGradingDetailsDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.UpdateOtherClaimDetailDTO;
import com.shaic.claim.preauth.wizard.view.ViewInvestigationDetails;
import com.shaic.claim.premedical.dto.OtherBenefitsTableDto;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.NewFVRGradingDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.FVRGradingDetail;
import com.shaic.domain.FVRGradingMaster;
import com.shaic.domain.FieldVisitRequestService;
import com.shaic.domain.FvrTriggerPoint;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.PEDValidationService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.RawInvsDetails;
import com.shaic.domain.RawInvsHeaderDetails;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.State;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.ViewTmpIntimation;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.HospitalPackage;
import com.shaic.domain.preauth.MasterRemarks;
import com.shaic.domain.preauth.NegotiationDetails;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.ProcedureMaster;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.domain.service.PreMedicalService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.starfax.simulation.PremiaPullService;
import com.vaadin.v7.data.util.BeanItemContainer;
@ViewInterface(PreauthEnhancementWizard.class)
public class PreauthEnhancemetWizardPresenter extends AbstractMVPPresenter<PreauthEnhancementWizard> {

	private static final long serialVersionUID = 5008035088244826928L;

	private final Logger log = LoggerFactory.getLogger(PreauthWizardPresenter.class);

	@EJB
	private MasterService masterService;

	@EJB
	private PreauthService preauthService;

	@EJB
	private CreateRODService createRodService;

	@EJB
	private PreMedicalService preMedicalService;

	@EJB
	private PreviousPreAuthService previousPreauthService;

	@EJB
	private ClaimService claimService;

	@EJB
	private DBCalculationService dbCalculationService;

	@EJB
	private HospitalService hospitalService;

	@EJB
	private PEDValidationService pedValidationService;

	@EJB
	private ViewFVRService viewFVRService;

	@EJB
	private FieldVisitRequestService fvrService;

	@Inject
	private ViewInvestigationDetails viewInvestigationDetails;

	@EJB
	private ReimbursementService reimbursementService;

	@EJB
	private PolicyService policyService;


	@EJB
	private IntimationService intimationService;

	@EJB
	private PremiaPullService premiaPullService;

	public static final String PREAUTH_STEP_CHANGE_EVENT = "preauth_enhancement_step_change_event";
	public static final String PREAUTH_SAVE_EVENT = "preauth_enhancement_save_event";
	public static final String PREAUTH_SUBMITTED_EVENT = "preatuh_enhancement_submit_event";
	public static final String SETUP_REFERENCE_DATA = "preauth_enhancement_set_edit_data";
	public static final String PREAUTH_TREATMENT_TYPE_CHANGED = "preauth_enhancement_treatment_type_changed";
	public static final String PREAUTH_PATIENT_STATUS_CHANGED = "preauth_enhancement_patient_status_changed";
	public static final String PREAUTH_RELAPSE_OF_ILLNESS_CHANGED = "preauth_enhancement_relapse_of_illness_changed";

	public static final String PREAUTH_QUERY_EVENT ="preauth_enhancement_query_click_event";
	public static final String PREAUTH_SUGGEST_REJECTION_EVENT ="preauth_enhancement_rejection_click_event";
	public static final String PREAUTH_SEND_FOR_PROCESSING_EVENT ="preauth_enhancement_send_for_processing_click_event";

	public static final String PREAUTH_REFERCOORDINATOR_EVENT = "preauth_enhancement_refer_coordinator_event";

	public static final String PREAUTH_PREVIOUS_CLAIM_DETAILS = "preauth_enhancement_previous_claim_details";

	public static final String SUBLIMIT_CHANGED_BY_SECTION = "Set sublimit values by selecting section for preauth enhancement";

	public static final String MASTER_SEARCH_STATE = "preauth_enhancement_master_search_stae";

	public static final String CHECK_CRITICAL_ILLNESS = "preauth_enhancement_check_critical_illness";

	public static final String GET_ICD_BLOCK = "preauth_enhancement_get_icd_block";

	public static final String GET_ICD_CODE = "preauth_enhancement_get_icd_code";

	public static final String GET_PROCESS_ENHN_PROCEDURE_VALUES = "preauth_enhancement_procedure_values";

	public static final String GET_SEC_COVER = "pre_auth_enhancement_get_sec_cover";

	public static final String GET_SUB_COVER = "pre_auth_enhancement_get_sub_cover";

	public static final String PREAUTH_QUERY_BUTTON_EVENT = "preauth_enhancement_query_button_event";

	public static final String PREAUTH_REJECTION_EVENT = "preauth_enhancement_rejection_event";

	public static final String PREAUTH_APPROVE_EVENT = "preauth_enhancement_approve_event";

	public static final String PREAUTH_UPDATE_PAN_CARD_EVENT = "preauth_enhancement_update_pancard_event";

	//	public static final String PREAUTH_DENIAL_EVENT = "preauth_enhancement_denial_event";

	public static final String PREAUTH_ESCALATE_EVENT = "preauth_enhancement_escalate_event";

	public static final String FIELD_VISIT_RADIO_CHANGED = "preauth_enhancement_field_visit_radio_changed";

	public static final String SPECIALIST_OPINION_RADIO_CHANGED = "preauth_enhancement_specialist_radio_changed";

	public static final String PREAUTH_SENT_TO_CPU_SELECTED = "preauth_enhancement_sent_to_cpu_selected";

	public static final String VIEW_CLAIMED_AMOUNT_DETAILS = "enhancement_view_claimed_amount_details";

	public static final String VIEW_BALANCE_SUM_INSURED_ENH_DETAILS = "view_Balance_Sum_Insured_end_details";

	public static final String NO_OF_DAYS_RULE = "enhancement_no_of_days_rule";

	public static final String GET_EXCLUSION_DETAILS = "preauth_enhancement_get_exclusion_details";

	public static final String PREAUTH_GET_PACKAGE_RATE = "preauth_enhancement_get_package_rate";

	public static final String SUM_INSURED_CALCULATION = "enhancement_calculate_sum_insured_values_from_DB";

	public static final String CHECK_INVESTIGATION_INITIATED = "enhancement_rule_for_investigation_initiated";

	public static final String BALANCE_SUM_INSURED = "enhancement_preauth_balance_sum_insured";

	public static final String SET_DB_DETAILS_TO_REFERENCE_DATA = "enhancement_preauth_set_claim_and_sublimit_db_details";

	public static final String GET_DIAGNOSIS_NAME = "enhancement_preauth_get_diagnosis_name";

	public static final String PREAUTH_WITHDRAW_EVENT = "preauth_enhancement_withdraw";

	public static final String PREAUTH_WITHDRAW_AND_REJECT_EVENT = "preauth_enhancement_withdraw_and_reject";

	public static final String PREAUTH_DOWNSIZE_EVENT = "preauth_enhancement_downsize";

	public static final String PREAUTH_ENHANCEMENT_COORDINATOR_REQUEST_EVENT = "preauth_enhancement_coordinator_request";

	public static final String GET_PREAUTH_REQUESTED_AMOUT = "get_preauth_requested_amount";

	public static final String GET_HOSPITALIZATION_DETAILS = "preauth_enhancement_get_hospitalization_details";

	public static final String HIDE_FVR_DETAILS = "hide_fvr_details";

	public static final String SHOW_FVR_DETAILS = "show_fvr_details";

	public static final String VALIDATE_PREAUTH_ENHANCEMENT_USER_RRC_REQUEST = "preauth_enhancement_user_rrc_request";

	public static final String PREAUTH_ENHANCEMENT_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "preauth_enhancement_load_rrc_request_drop_down_values";

	public static final String PREAUTH_ENHANCEMENTSAVE_RRC_REQUEST_VALUES = "preauth_enhancement_save_rrc_request_values";

	public static final String PREAUTH_ENHANCEMENT_HOSPITALISATION_DUE_TO = "pre_auth_enhancement_hospitalization_due_to";

	public static final String PREAUTH_ENHANCEMENT_REPORTED_TO_POLICE = "PREAUTH_ENHANCEMENT_REPORTED_TO_POLICE";

	public static final String RECHARGE_SI_FOR_PRODUCT = "recharging SI for product in Preauth Enhancement Screen";

	public static final String ENHANCEMENT_GENERATE_REMARKS = "Enhancement generate Remarks";

	//	public static final String ENH_SUB_REJECT_CATEG_LAYOUT = "Enhancement Reject Subcategory Layout";

	public static final String GENERATE_WITHDRAW_REJECT_REMARKS = "Enhancement Reject & Withdraw Remarks";

	public static final String ENHANCEMENT_DOWNSIZE_LAYOUT = "Enhancment Downsize Layout";

	public static final String ENHANCEMENT_PREVIOUS_CLAIM_FOR_POLICY = "enhancement_previous_claim_details for policy";

	protected static final String REFER_VB_64_COMPLAINCE = "refer 64 vb complaince enhancement";

	public static final String UPDATE_PREVIOUS_CLAIM_DETAILS = "update_previous_claim_details for enhancement";

	public static final String REFERENCE_DATA_CLEAR = "reference data clear for preauth enhancement";

	public static final String ENHANCEMENT_SHOW_PTCA_CABG = "show_ptca_cabg_enhancement";

	public static final String ENHANCEMENT_SHOW_PTCA_CABG_DELETE = "show_ptca_cabg_enhancement_delete";
	public static final String CHECK_PAN_CARD_DETAILS = "preauth enhancement check pan card details";
	public static final String ENHANCEMENT_REFER_CPU_USER = "enhancement_refer_cpu_user";

	public static final String VIEW_PREAUTH_ENH_OTHER_BENEFITS = "view_preauth_enh_other_benefits";

	public static final String ENH_OTHER_BENEFITS = "enh_other_benefits";

	public static final String RTA_RECHARGE_SI_FOR_PRODUCT = "recharging RTA SI for product in Process claim enhancement";

	public static final String GET_ASSISTED_REPRODUCTION_TREATMENT_COVER_KEY_PE = "GET_ASSISTED_REPRODUCTION_TREATMENT_COVER_KEY_PE";

	public static final String PREAUTH_ENHANCEMENT_LUMEN_REQUEST = "preauth_enhancement_lumen_request";

	public static final String CHECK_ENH_FVR_NOT_REQ_REMARKS_VALIDATION = "Enh. FVR Not Req. Remarks Validation";

	public static final String PREAUTH_PREVIOUS_CLAIM_DETAILS_1= "preauth_enhancement_previous_claim_details_1";

	public static final String ENHANCEMENT_FIELD_VISIT_GRADING = "enhancement_field_visit_grading";

	public static final String ENHANCEMENT_HOLD_EVENT = "enhancement_hold_functionality";

	public static final String INVS_REVIEW_REMARKS_LIST = "Enhancement_Invs_Review_Remarks_List";
	public static final String SAVE_AUTO_ALLOCATION_CANCEL_REMARKS = "save_enhancement_auto_allocation_cancel_remarks";
	public static final String NEGOTIATION_OPINION_RADIO_CHANGED = "preauth_enhancement_negotiation_radio_changed";
	public static final String NEGOTIATION_CANCEL_OR_UPDATE = "negotiation cancel";
	public static final String NEGOTIATION_PENDING = "negotiation pending";

	public static final String ENHN_HOSP_SCORING_EVENT = "enhancement_hosp_scoring_event";

	public static final String GET_ENHANCEMENT_QUERY_REMARKS = "Get Enhancement Query Remarks";

	public static final String ENHN_TOPUP_POLICY_EVENT = "enhancement_topup_policy_event";

	public static final String ENHN_TOPUP_POLICY_INTIMATION_EVENT = "enhn_topup_policy_intimation_event";

	public static final String ENH_GET_REJ_SUBCATEG_REMARKS = "Enhancement Get Reject Subcategory Remarks";

	public static final String ENH_SUB_REJECT_CATEG_LAYOUT = "Enhancement Reject SubCategory Layout";

	public static final String ENHN_APPROVED_DATE = "previous enhancement approved date";

	public static final String ENHN_HOLD_SUBMITTED_EVENT = "enhancement_hold_submit_event";

	public static final String SET_PRE_AUTH_ENHANCEMENT_CATAGORY_VALUE = "SET_PRE_AUTH_ENHANCEMENT_CATAGORY_VALUE";	

	public static final String PREAUTH_ENHANCEMENT_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "preauth_enhancement_load_rrc_request_sub_category_values";

	public static final String PREAUTH_ENHANCEMENT_LOAD_RRC_REQUEST_SOURCE_VALUES = "preauth_enhancement_load_rrc_request_source_values";

	public static final String PREAUTH_ENHANCEMENT_LOAD_NEGOTIATED_SAVED_AMOUNT = "preauth_enhancement_load_negotiated_saved_amount";

	protected Map<String, Object> referenceData = new HashMap<String, Object>();

	//	public static final String ENH_GET_REJ_SUBCATEG_REMARKS = "Enhancemenet get Reject Subcategory Remarks";

	public static final String PREAUTH_ENHANCEMENT_APPROVE_BSI_ALERT = "preauth_enhancement_approve_bsi_alert";

	public static final String PREAUTH_ENHANCEMENT_IMPLANT_APPLICABLE_CHANGED = "preauth_enhancement_implant_applicable_changed";
	
	public static final String PREAUTH_ENHANCEMENT_ICAC_SUBMIT_EVENT = "preauth_enhancement_icac_sumbit_event";

	public static final String PREAUTH_ENHANCEMENT_PCC_SUB_CAT_TWO_GENERATE = "preauth_enhancement_pcc_sub_cat_two_generate";

	public static final String PREAUTH_ENHANCEMENT_PCC_SUB_CAT = "preauth_enhancement_pcc_sub_cat";

	public void fvrRadioOptChanged(@Observes @CDIEvent(CHECK_ENH_FVR_NOT_REQ_REMARKS_VALIDATION) final ParameterDTO parameters) {

		PreauthDTO preauthdto = (PreauthDTO) parameters.getPrimaryParameter();
		boolean fvrpending = preauthService.getFVRStatusIdByClaimKey(preauthdto.getClaimDTO().getKey());
		preauthdto.getPreauthMedicalDecisionDetails().setIsFvrIntiatedMA(fvrpending);
	}

	public void disableFVRIntiation(
			@Observes @CDIEvent(HIDE_FVR_DETAILS) final ParameterDTO parameters) {
		view.disableFVRDetails();

	}

	public void enableFVRIntiation(
			@Observes @CDIEvent(SHOW_FVR_DETAILS) final ParameterDTO parameters) {
		view.enableFVRDetails();

	}

	public void coordinatorRequestEvent(
			@Observes @CDIEvent(PREAUTH_ENHANCEMENT_COORDINATOR_REQUEST_EVENT) final ParameterDTO parameters) {
		view.intiateCoordinatorRequest();
	}

	public void activeStepChanged(@Observes @CDIEvent(PREAUTH_STEP_CHANGE_EVENT) final ParameterDTO parameters) {
		view.setWizardPageReferenceData(referenceData);
	}

	public void saveWizard(@Observes @CDIEvent(PREAUTH_SAVE_EVENT) final ParameterDTO parameters) 
	{

	}

	@SuppressWarnings("unused")
	public void submitWizard(
			@Observes @CDIEvent(PREAUTH_SUBMITTED_EVENT) final ParameterDTO parameters) throws Exception {
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
		if(!isAlreadyAcquired){
			Boolean isValidUser = true;
			if(preauthDTO.getStatusKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)){
				if(preauthDTO.getFinalTotalApprovedAmount() != null){
					String limitAmountValidation = dbCalculationService.getLimitAmountValidation(preauthDTO.getStrUserName(),preauthDTO.getFinalTotalApprovedAmount().longValue(), SHAConstants.CASHLESS_CHAR);
					if(limitAmountValidation != null && limitAmountValidation.equalsIgnoreCase("N")){
						isValidUser = false;
					}
				}

			}
			if(isValidUser){
				if(preauthDTO.getStatusKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS) 
						|| preauthDTO.getStatusKey().equals(ReferenceTable.ENHANCEMENT_DOWNSIZE_STATUS)
						|| preauthDTO.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)
						|| preauthDTO.getStatusKey().equals(ReferenceTable.ENHANCEMENT_DENIAL_OF_CASHLESS_STATUS) || preauthDTO.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS) || preauthDTO.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)
						|| preauthDTO.getStatusKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT)
						|| preauthDTO.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS)
						|| preauthDTO.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS)){

					dbCalculationService.reimbursementRollBackProc(preauthDTO.getPreviousPreauthKey(),"C");

				}

				/*if(ReferenceTable.getFHORevisedKeys().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					if(preauthDTO.getPreauthMedicalDecisionDetails().getEnhBenefitApprovedAmount() != null && preauthDTO.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null){
						Double totalOtherBenefitsApprovedAmt = preauthDTO.getPreauthMedicalDecisionDetails().getEnhBenefitApprovedAmount();
						Double initialTotalApprovedAmt = preauthDTO.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt();
						initialTotalApprovedAmt += totalOtherBenefitsApprovedAmt;
						preauthDTO.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(initialTotalApprovedAmt);
					}

					if(preauthDTO.getStatusKey().equals(ReferenceTable.ENHANCEMENT_DOWNSIZE_STATUS) || preauthDTO.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS)){
						if(preauthDTO.getPreauthMedicalDecisionDetails().getEnhBenefitApprovedAmount() != null && preauthDTO.getPreauthMedicalDecisionDetails().getDownsizedAmt() != null){
							Double totalOtherBenefitsApprovedAmt = preauthDTO.getPreauthMedicalDecisionDetails().getEnhBenefitApprovedAmount();
							Double initialTotalApprovedAmt = preauthDTO.getPreauthMedicalDecisionDetails().getDownsizedAmt();
							initialTotalApprovedAmt += totalOtherBenefitsApprovedAmt;
							preauthDTO.getPreauthMedicalDecisionDetails().setDownsizedAmt(initialTotalApprovedAmt);
						}
					}
				}*/

				Preauth preauth = preauthService.submitPreAuth(preauthDTO, true);
				preauthService.updateInvsReviewDetails(preauthDTO.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList());
				/*if(null != preauthDTO.getPreauthHoldStatusKey() && preauthDTO.getPreauthHoldStatusKey().equals(ReferenceTable.PREAUTH_HOLD_STATUS_KEY)){
					if(preauthDTO.getDbOutArray() != null){						
						Long wkFlowKey = (Long)wrkFlowMap.get(SHAConstants.WK_KEY);	
						preauthService.updateHoldRemarksForAutoAllocation(wkFlowKey, preauthDTO.getPreauthMedicalDecisionDetails().getHoldRemarks());
					}
				}*/
				RawInvsHeaderDetails headerObj =  claimService.getRawHeaderByIntimation(preauthDTO.getNewIntimationDTO().getIntimationId());
				if(null != headerObj){
					List<RawInvsDetails> existingList = claimService.getRawDetailsByRecordType(headerObj.getKey());
					for (RawInvsDetails rawInvsDetails : existingList) {
						preauthService.updateEsclateIfCompleted(rawInvsDetails);
					}
				}
				dbCalculationService.stopReminderProcessProcedure(preauth.getClaim().getIntimation().getIntimationId(),SHAConstants.OTHERS);

				if(ReferenceTable.getFHORevisedKeys().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){

					policyService.updatePolicyBonusDetails(preauthDTO.getNewIntimationDTO().getPolicy().getPolicyNumber());

					preauthDTO.setStatusKey(preauth.getStatus().getKey());
					preauthDTO.setStageKey(preauth.getStage().getKey());
					preauthDTO.setKey(preauth.getKey());
					preMedicalService.saveBenefitAmountDetails(preauthDTO);
				}
				if(null != preauthDTO && null != preauthDTO.getPreauthMedicalDecisionDetails().getNegotiationDecisionTaken() &&
						preauthDTO.getPreauthMedicalDecisionDetails().getNegotiationDecisionTaken() && 
						(null != preauthDTO.getPreauthMedicalDecisionDetails().getIsValidNegotiation() && 
						!preauthDTO.getPreauthMedicalDecisionDetails().getIsValidNegotiation()))
				{
					preauthService.insertNegotiationDetails(preauth,preauthDTO);
				}
				else if(preauthDTO.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)
						|| preauthDTO.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)
						|| preauthDTO.getStatusKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT)){

					preauthService.cancellNegotiation(preauthDTO);
				}

				if(preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS))) {

					dbCalculationService.invokeAccumulatorProcedure(preauth.getKey());
					Hospitals hospitalByKey = hospitalService.getHospitalByKey(preauth.getClaim().getIntimation().getHospital());
					//			createRodService.stopCashlessReminderLetter(preauth.getClaim().getKey(), preauthDTO.getStrUserName(), preauthDTO.getStrPassword());
					//			createRodService.stopCashlessReminderProcess(preauth.getClaim().getKey(), preauthDTO.getStrUserName(), preauthDTO.getStrPassword());

					//			preauthService.callReminderTaskForDB(preauth.getClaim(), hospitalByKey);



					Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(preauth.getClaim(), hospitalByKey);

					Object[] inputArray = (Object[])arrayListForDBCall[0];

					inputArray[SHAConstants.INDEX_REMINDER_CATEGORY] = SHAConstants.PREAUTH_BILLS_NOT_RECEIVED;
					inputArray[SHAConstants.INDEX_CASHLESS_KEY] = preauth.getKey();
					inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.OUTCOME_ENHANCEMENT_INITIATE_REMINDER_PROCESS;

					preauthService.callReminderTaskForDB(inputArray);
				}
				Double totalApprAmt = preauthDTO.getPreauthDataExtractionDetails().getTotalApprAmt();
				Policy policy = preauthDTO.getNewIntimationDTO().getPolicy();
				Policy policyByKey = policyService.getPolicyByKey(policy.getKey());
				if(policy!=null && policyByKey.getProposedPanNumber()==null && preauth.getTotalApprovalAmount() != null && preauth.getTotalApprovalAmount()>=SHAConstants.ONE_LAKH && totalApprAmt<SHAConstants.ONE_LAKH){
					if(preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS))) {

						Hospitals hospitalByKey = hospitalService.getHospitalByKey(preauth.getClaim().getIntimation().getHospital());
						//			createRodService.stopCashlessReminderLetter(preauth.getClaim().getKey(), preauthDTO.getStrUserName(), preauthDTO.getStrPassword());
						//			createRodService.stopCashlessReminderProcess(preauth.getClaim().getKey(), preauthDTO.getStrUserName(), preauthDTO.getStrPassword());
						Map<String, Object> mapValues = new WeakHashMap<String, Object>();
						mapValues.put(SHAConstants.PAYLOAD_REMINDER_CATEGORY, SHAConstants.PAN_CARD);


						mapValues.put(SHAConstants.INTIMATION_NO, preauth.getClaim().getIntimation().getIntimationId());	
						//			preauthService.callReminderTaskForDB(preauth.getClaim(), hospitalByKey);
						Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);

						DBCalculationService dbCalculationService = new DBCalculationService();
						List<Map<String, Object>> taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);	
						if (null != taskProcedure && taskProcedure.isEmpty()) {
							dbCalculationService = new DBCalculationService();
							Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(preauth.getClaim(), hospitalByKey);

							Object[] inputArray = (Object[])arrayListForDBCall[0];

							inputArray[SHAConstants.INDEX_REMINDER_CATEGORY] = SHAConstants.PAN_CARD;
							inputArray[SHAConstants.INDEX_CASHLESS_KEY] = preauth.getKey();
							inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.OUTCOME_ENHANCEMENT_INITIATE_REMINDER_PROCESS;

							preauthService.callReminderTaskForDB(inputArray);
						}

					}

				}
				//			     R1135    -     13-04-2018
				else if(preauth.getStatus() != null && 
						(preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS) 
								|| preauthDTO.getStatusKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT) 
								|| preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS) 
								|| preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_DOWNSIZE_STATUS))){

					dbCalculationService.stopReminderProcessProcedure(preauth.getClaim().getIntimation().getIntimationId(),SHAConstants.PAN_CARD);

				}

				if(preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS) )) {
					//			preauthService.callBPMRemainderTask(preauthDTO,preauth, preauthDTO.getStrUserName(), preauthDTO.getStrPassword());
				}
				if(preauth.getStatus() != null && preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)) {
					if(preauthDTO.getPreauthMedicalDecisionDetails().getWithdrawReason() != null 
							&& preauthDTO.getPreauthMedicalDecisionDetails().getWithdrawReason().getId().equals(ReferenceTable.PATIENT_NOT_ADMITTED_FOR_WITHDRAW)) {
						String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
						if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
							PremiaService.getInstance().UnLockPolicy(preauth.getClaim().getIntimation().getIntimationId());
						}
					}

					//			createRodService.stopCashlessReminderLetter(preauth.getClaim().getKey(), preauthDTO.getStrUserName(), preauthDTO.getStrPassword());
					//			createRodService.stopCashlessReminderProcess(preauth.getClaim().getKey(), preauthDTO.getStrUserName(), preauthDTO.getStrPassword());
				}

				String strPremiaFlag = BPMClientContext.PREMIA_FLAG;

				if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
					if(preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS) 
							|| preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS) 
							|| preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_DOWNSIZE_STATUS) 
							|| preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS) 
							|| preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS) 
							|| preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS) 
							|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS) 
							|| preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS) 
							|| preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_REFER_TO_COORDINATOR_STATUS) 
							|| preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS)
							|| preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT)
							|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS)) {
						Claim claimByKey = preauthService.searchByClaimKey(preauth.getClaim().getKey());
						Hospitals hospitalObject = preauthService.getHospitalObject(claimByKey.getIntimation().getHospital());
						String provisioningamt = String.valueOf(preauth.getTotalApprovalAmount().longValue());

						if(preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS) ||
								preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS)
								|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT) || preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS)) {
							provisioningamt = claimByKey.getCurrentProvisionAmount() != null ? String.valueOf(claimByKey.getCurrentProvisionAmount().longValue()) : (claimByKey.getProvisionAmount() != null ? String.valueOf(claimByKey.getProvisionAmount().longValue()) : "0");
						}

						//CR R1313   For Retail - Provision should become Zero for the Withdraw reason ReferenceTable.PATIENT_NOT_ADMITTED_FOR_WITHDRAW
						if(!ReferenceTable.getGMCProductList().containsKey(claimByKey.getIntimation().getPolicy().getProduct().getKey())){
							if(preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS) 
									&& preauthDTO.getPreauthMedicalDecisionDetails().getWithdrawReason() != null
									&& preauthDTO.getPreauthMedicalDecisionDetails().getWithdrawReason().getId().equals(ReferenceTable.PATIENT_NOT_ADMITTED_FOR_WITHDRAW_RETAIL)){
								provisioningamt = String.valueOf("0");
								claimService.updateClaimProvisionAmount(claimByKey);
							}
						}

						String provisionAmtInput = SHAUtils.getProvisionAmtInput(preauth.getClaim(), hospitalObject.getName(), provisioningamt);
						PremiaService.getInstance().updateProvisionAmount(provisionAmtInput);
					}
				}



				//		preauthService.setBPMOutcome(preauthDTO, preauth, true);

				//preauthService.setDBOutcome(preauthDTO, preauth, true);
				view.buildSuccessLayout();	
				//		preauthService.submitPreAuth(preauthDTO, true);
				//		view.buildSuccessLayout();	
			}else{
				//view.validationForLimitAmount();
			}
		}else{
			view.buildFailureLayout(aquiredUser.toString());
		}
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
		if(parameters.getPrimaryParameter() != null) {

			referenceData.put("sectionDetails", masterService
					.getSectionList(bean.getNewIntimationDTO().getPolicy().getProduct().getKey(),bean.getNewIntimationDTO().getPolicy()));
		}

		referenceData.put("coPayType", masterService.getSelectValueContainer(ReferenceTable.JIO_COPAY_TYPE_VALUE));
		view.setWizardPageReferenceData(referenceData);
	}

	public void setReferece(PreauthDTO bean)
	{
		Long productKey = bean.getNewIntimationDTO().getPolicy().getProduct().getKey();

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
		BeanItemContainer<SelectValue> procedureListNames = preauthService.getProcedureListNames();
		referenceData.put("procedureName", procedureListNames);
		referenceData.put("procedureCode", procedureListNames);
		referenceData.put("medicalSpeciality", preauthService.getSpecialityType("M"));
		referenceData.put("surgicalSpeciality", preauthService.getSpecialityType("S"));
		referenceData.put("nextLOVSpeciality", masterService.getNextLOVSpecialityType());
		referenceData.put("sublimitApplicable", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("considerForPayment", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("considerForDayCare", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("commonValues", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("pedExclusionImpactOnDiagnosis",masterService.getSelectValueContainer(ReferenceTable.IMPACT_ON_DIAGNOSI));
		referenceData.put("exclusionDetails",masterService.getSelectValueContainer(ReferenceTable.EXCLUSION_DETAILS));

		//CR R20181300
		referenceData.put("pedImpactOnDiagnosis", masterService.getSelectValueContainer(ReferenceTable.PED_IMPACT_ON_DIAGNOSI));
		referenceData.put("reasonForNotPaying", masterService.getSelectValueContainer(ReferenceTable.PED_NON_PAYABLE_REASON)); //EXCLUSION_DETAILS
		//CR R20181300

		referenceData.put("procedureStatus",masterService.getSelectValueContainer(ReferenceTable.PROCEDURE_STATUS));
		//		referenceData.put("pedList", masterService.getInsuredPedDetails());
		referenceData.put("fvrNotRequiredRemarks", masterService.getSelectValueContainer(ReferenceTable.FVR_NOT_REQUIRED_REMARKS));
		referenceData.put("rejectionCategory", masterService.getRevisedRejectionCategoryByValue(productKey));     //masterService.getRejectionCategoryByValue();
		referenceData.put("sendForProcessingCategory", masterService.getRevisedRejectionCategoryByValue(productKey));    //masterService.getRejectionCategoryByValue();

		//R1313  NEW REJ_CATEGORY LOV
		if(!ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			referenceData.put("rejectionCategory", masterService.getEnhRejectionCategoryByValue());
			referenceData.put("sendForProcessingCategory", masterService.getEnhRejectionCategoryByValue());
		}

		referenceData.put("terminateCover", masterService.getSelectValueContainer(ReferenceTable.TERMINATE_COVER));
		referenceData.put("criticalIllness", preauthService.getCriticalIllenssMasterValues(bean));

		if(ReferenceTable.getHealthGainProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			DBCalculationService dbCalculationService = new DBCalculationService();
			/*BeanItemContainer<SelectValue> siRestricationValueForHealthGain = dbCalculationService.getSiRestricationValueForHealthGain(bean.getNewIntimationDTO().getPolicy().getKey(),
					bean.getNewIntimationDTO().getInsuredPatient().getKey(), masterService);
			referenceData.put("sumInsuredRestriction",  siRestricationValueForHealthGain);*/
			referenceData.put("sumInsuredRestriction",dbCalculationService.getSiRestricationValue(bean.getNewIntimationDTO().getInsuredPatient().getKey()));
		}else{
			//referenceData.put("sumInsuredRestriction",  masterService.getSelectValueContainer(ReferenceTable.SUM_INSURED));
			referenceData.put("sumInsuredRestriction",dbCalculationService.getSiRestricationValue(bean.getNewIntimationDTO().getInsuredPatient().getKey()));
		}
		referenceData.put("status", masterService
				.getSelectValueContainer(ReferenceTable.COMMON_VALUES));

		referenceData.put("hospitalisationDueTo", masterService.getSelectValueContainer(ReferenceTable.HOSPITALIZATION_DUE_TO));
		referenceData.put("causeOfInjury", masterService.getSelectValueContainer(ReferenceTable.CAUSE_OF_INJURY));
		referenceData.put("section", masterService
				.getSelectValueContainer(ReferenceTable.SECTION));
		referenceData.put("typeOfDelivery", masterService
				.getSelectValueContainer(ReferenceTable.DELIVERY_TYPE));
		// Preauth Only..
		referenceData.put("investigatorName", masterService.getInvestigation());
		referenceData.put("stent", masterService.getSelectValueContainer(ReferenceTable.STENT));

		referenceData.put("natureOfLoss", masterService
				.getNatureCauseLossSelectValueContainer(SHAConstants.HEALTH_LOB, ReferenceTable.NATURE_OF_LOSS));
		referenceData.put("causeOfLoss", masterService
				.getNatureCauseLossSelectValueContainer(SHAConstants.PA_LOB, ReferenceTable.CAUSE_OF_LOSS));
		referenceData.put("catastrophicLoss", masterService.getCatastrophicLossList());
		// Enhancement Only
		referenceData.put("behaviourHosCombValue", masterService.getBehvrHospSelectValueContainer(ReferenceTable.BEHAVIOUR_OF_HOSP));

	}

	public void generateFieldsBasedOnOtherBenefits(@Observes @CDIEvent(ENH_OTHER_BENEFITS) final ParameterDTO parameters)
	{
		PreauthDTO beanDto = (PreauthDTO) parameters.getPrimaryParameter();
		Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(
				beanDto.getNewIntimationDTO().getInsuredPatient()
				.getInsuredId().toString(), beanDto.getPolicyDto()
				.getKey(),beanDto.getNewIntimationDTO().getInsuredPatient().getLopFlag());

		if(beanDto.getPreauthDataExtractionDetails().getOtherBenefitsList() == null || 
				(beanDto.getPreauthDataExtractionDetails().getOtherBenefitsList() != null && beanDto.getPreauthDataExtractionDetails().getOtherBenefitsList().isEmpty())){
			List<OtherBenefitsTableDto> benefitsDtoList =  new ArrayList<OtherBenefitsTableDto>();
			if(SHAConstants.YES_FLAG.equalsIgnoreCase(beanDto.getPreauthDataExtractionDetails().getOtherBenfitFlag())){
				benefitsDtoList =  dbCalculationService.getOtherBebefitsList(beanDto.getNewIntimationDTO().getPolicy().getProduct().getKey(), insuredSumInsured.longValue(),ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
				beanDto.getPreauthDataExtractionDetails().setOtherBenefitsList(benefitsDtoList);
			}
		}		
		view.genertateFieldsBasedOnOtherBenefits(beanDto);
	}



	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_PREAUTH_ENHANCEMENT_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		RRCDTO rrcDTO = null;
		if(preauthDTO.getRrcDTO() !=null){
			rrcDTO = preauthDTO.getRrcDTO();
		}else{
			rrcDTO = new RRCDTO();
		}
		reimbursementService.setRequestStageIdForRRC(rrcDTO,SHAConstants.PROCESS_ENHANCEMENT);
		rrcDTO.setProcessingStage(SHAConstants.PROCESS_ENHANCEMENT);
		preauthDTO.setRrcDTO(rrcDTO);
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(
				preauthDTO.getNewIntimationDTO().getInsuredPatient()
				.getInsuredId().toString(), preauthDTO.getPolicyDto()
				.getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		if(isValid)
		{
			reimbursementService.loadRRCRequestValues(preauthDTO,insuredSumInsured,SHAConstants.PROCESS_ENHANCEMENT);
		}

		view.buildValidationUserRRCRequestLayout(isValid);

	}


	public void saveRRCRequestValues(@Observes @CDIEvent(PREAUTH_ENHANCEMENTSAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_CASHLESS);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);

	}

	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(PREAUTH_ENHANCEMENT_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
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
		/*BeanItemContainer<SelectValue> icdCodeContainer = masterService.getIcdCodes();

		view.setIcdCode(icdCodeContainer);*/
	}

	public void getProcedureValues(
			@Observes @CDIEvent(GET_PROCESS_ENHN_PROCEDURE_VALUES) final ParameterDTO parameters) {
		Long specialistkey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue>  procedures = preauthService.getProcedureforSpeciality(specialistkey);
		view.setPreauthEnhnProcedureValues(procedures);
	}


	public void generatePreAuthApproveLayout(@Observes @CDIEvent(PREAUTH_APPROVE_EVENT) final ParameterDTO parameters)
	{
		view.generatePreauthApproveLayout();
	}

	public void generatePreauthDownSizeLayout(@Observes @CDIEvent(PREAUTH_DOWNSIZE_EVENT)final ParameterDTO parameters){
		Long prodKey = (Long) parameters.getPrimaryParameter();

		BeanItemContainer<SelectValue> selectValueContainer = masterService.getConversionReasonByValue(ReferenceTable.DOWNSIZE_REASON);

		if(!ReferenceTable.getGMCProductList().containsKey(prodKey)){
			selectValueContainer = masterService.getSortedMasterBsedOnMasterTypeCode(ReferenceTable.DOWNSIZE_REASON_RETAIL);
		}

		view.generateDownSizeTableLayout(selectValueContainer);
	}

	public void generatePreAuthQueryLayout(@Observes @CDIEvent(PREAUTH_QUERY_BUTTON_EVENT) final ParameterDTO parameters)
	{
		view.generateQueryLayout();
	}

	public void generatePreAuthRejectLayout(@Observes @CDIEvent(PREAUTH_REJECTION_EVENT) final ParameterDTO parameters)
	{

		//		Long productKey = (Long) parameters.getPrimaryParameter();
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Long productKey = preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey();

		BeanItemContainer<SelectValue> enhRejContainer = masterService.getRevisedRejectionCategoryByValue(productKey);
		//		view.generateRejectionLayout(masterService.getRevisedRejectionCategoryByValue(productKey));    //masterService.getRejectionCategoryByValue()

		//R1313  NEW REJ_CATEGORY LOV
		if(!ReferenceTable.getGMCProductList().containsKey(productKey)){
			enhRejContainer = masterService.getEnhRejectionCategoryByValue();
		}
		
		if(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)){
			if(preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM)){
				enhRejContainer = masterService.getRevisedRejectionCategoryByValueForCoron(productKey ,preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan());
			}else if(preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_INDEMNITY)){
				enhRejContainer= masterService.getRevisedRejectionCategoryByValueForCoron(productKey,preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan()); 
			}
		}

		view.generateRejectionLayout(enhRejContainer);

	}

	/*public void generatePreAuthDenialLayout(@Observes @CDIEvent(PREAUTH_DENIAL_EVENT) final ParameterDTO parameters)
	{
		view.generateDenialLayout(masterService.getSelectValueContainer(ReferenceTable.DENIAL_REASON));
	}*/

	public void generatePreAuthWithdrawLayout(@Observes @CDIEvent(PREAUTH_WITHDRAW_EVENT) final ParameterDTO parameters)
	{	
		// CR R1313  ----- START
		Long productKey = (Long) parameters.getPrimaryParameter();

		BeanItemContainer<SelectValue> selectValueContainer = masterService.getSelectValueContainer(ReferenceTable.WITHDRAWAL_REASON);

		if(!ReferenceTable.getGMCProductList().containsKey(productKey)){
			selectValueContainer = masterService
					.getSortedMasterBsedOnMasterTypeCode(ReferenceTable.WITHDRAWAL_REASON_RETAIL);
		}

		view.generateWithdrawLayout(selectValueContainer);   		// CR R1313  ----- END

		//		view.generateWithdrawLayout(masterService.getSelectValueContainer(ReferenceTable.WITHDRAWAL_REASON));  // CR R1313
	}

	public void generatePreAuthWithdrawAndRejectLayout(@Observes @CDIEvent(PREAUTH_WITHDRAW_AND_REJECT_EVENT) final ParameterDTO parameters)
	{
		PreauthDTO preauthdto = (PreauthDTO) parameters.getPrimaryParameter();
		Long productKey = preauthdto.getNewIntimationDTO().getPolicy().getProduct().getKey();
		if(ReferenceTable.getGMCProductList().containsKey(productKey) 
				&& !((productKey.equals(ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY) 
						|| productKey.equals(ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY_FOR_LUMPSUM)
						|| productKey.equals(ReferenceTable.STAR_GRP_COVID_PROD_KEY_INDI) 
						|| productKey.equals(ReferenceTable.STAR_GRP_COVID_PROD_KEY_LUMSUM)))) {				
			view.generateWithdrawAndRejectLayout(masterService.getSelectValueContainer(ReferenceTable.WITHDRAWAL_REASON), masterService.getRevisedRejectionCategoryByValue(productKey));  //masterService.getRejectionCategoryByValue();
		}else if(productKey.equals(ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY) || productKey.equals(ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY_FOR_LUMPSUM)
				|| productKey.equals(ReferenceTable.STAR_GRP_COVID_PROD_KEY_INDI) 
				|| productKey.equals(ReferenceTable.STAR_GRP_COVID_PROD_KEY_LUMSUM)){
			if(preauthdto.getPolicyDto().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM)){
				
				view.generateWithdrawAndRejectLayout(masterService.getSelectValueContainer(ReferenceTable.WITHDRAWAL_REASON), masterService.getRevisedRejectionCategoryByValueForCoron(preauthdto.getPolicyDto().getProduct().getKey() ,preauthdto.getPolicyDto().getPolicyPlan())); 

			}else if(preauthdto.getPolicyDto().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_INDEMNITY)){
				
				view.generateWithdrawAndRejectLayout(masterService.getSelectValueContainer(ReferenceTable.WITHDRAWAL_REASON), masterService.getRevisedRejectionCategoryByValueForCoron(preauthdto.getPolicyDto().getProduct().getKey(),preauthdto.getPolicyDto().getPolicyPlan())); 

			}
		}
		else {
			// Commented Below code due to CR2019105 
			//			BeanItemContainer<SelectValue> withdrawRejectContainer = masterService.getSortedMasterBsedOnMasterTypeCode(ReferenceTable.REJECT_WITHDRAWAL_REASON_RETAIL);
			BeanItemContainer<SelectValue> withdrawRejectContainer = masterService.getRevisedRejectWithdrawCategoryByValue(productKey);
			view.generateWithdrawAndRejectLayout(withdrawRejectContainer);
		}

	}

	public void generatePreAuthEscalateLayout(@Observes @CDIEvent(PREAUTH_ESCALATE_EVENT) final ParameterDTO parameters)
	{
		view.generateEscalateLayout(masterService.getSelectValueContainer(ReferenceTable.ESCALATE_TO));
	}

	public void generateFieldsBasedOnFieldVisit(@Observes @CDIEvent(FIELD_VISIT_RADIO_CHANGED) final ParameterDTO parameters)
	{
		Boolean isChecked = (Boolean) parameters.getPrimaryParameter();
		PreauthDTO preauthdto = (PreauthDTO)parameters.getSecondaryParameters()[0];

		boolean fvrpending = preauthService.getFVRStatusIdByClaimKey(preauthdto.getClaimDTO().getKey());

		if(fvrpending){
			FieldVisitRequest fvrobj = preauthService.getPendingFVRByClaimKey(preauthdto.getClaimDTO().getKey());
			view.genertateFieldsBasedOnAssignFieldVisit(isChecked,fvrobj.getRepresentativeName(),fvrobj.getRepresentativeContactNumber());
		}

		if(!fvrpending){		
			FieldVisitRequest fvrobjByRodKey = fvrService.getPendingFieldVisitByClaimKey(preauthdto.getClaimDTO().getKey());

			if((fvrobjByRodKey  == null || (fvrobjByRodKey != null && fvrobjByRodKey.getStatus() != null && ReferenceTable.INITITATE_FVR.equals(fvrobjByRodKey.getStatus().getKey())))){

				//				if(fvrobjByRodKey  != null ){
				//					dbCalculationService.invokeProcedureAutoSkipFVR(fvrobjByRodKey.getFvrId());
				//					fvrService.autoSkipFirstFVR(fvrobjByRodKey);
				//				}

				List<ViewFVRDTO> fvrDTOList = null;
				Long intimationKey =  preauthdto.getIntimationKey();
				fvrDTOList = viewFVRService.searchFVR(intimationKey);
				view.genertateFieldsBasedOnFieldVisit(isChecked, masterService.getSelectValueContainer(ReferenceTable.ALLOCATION_TO)
						,/*masterService.getSelectValueContainer(ReferenceTable.ASSIGN_TO)*/null
						,masterService.getSelectValueContainer(ReferenceTable.FVR_PRIORITY), fvrDTOList);
			}
		}

	}

	public void generateFieldsBasedOnSpecialist(@Observes @CDIEvent(SPECIALIST_OPINION_RADIO_CHANGED) final ParameterDTO parameters)
	{
		Map<String, Object> map = new WeakHashMap<String, Object>();
		map.put("specialistConsulted", masterService.getSelectValueContainerForSpecialist());
		map.put("specialistType", masterService.getSelectValueContainer(ReferenceTable.SPECIALIST_TYPE));
		Boolean isChecked = (Boolean) parameters.getPrimaryParameter();
		view.genertateFieldsBasedOnSpecialistChecked(isChecked, map);
	}

	public void getPreviousClaimDetails1(
			@Observes @CDIEvent(PREAUTH_PREVIOUS_CLAIM_DETAILS_1) final ParameterDTO parameters) {
		PreauthDTO preauthDto = (PreauthDTO) parameters.getPrimaryParameter();
		if (preauthDto != null
				&& preauthDto.getNewIntimationDTO() != null
				&& preauthDto.getNewIntimationDTO().getInsuredPatient() != null
				&& preauthDto.getNewIntimationDTO().getInsuredPatient()
				.getInsuredId() != null) {
			/*Long policyNumberOrInsuredId = preauthDto.getNewIntimationDTO()
					.getInsuredPatient().getInsuredId();*/

			//			List<ViewTmpClaim> claimList = claimService
			//					.getTmpClaimsByInsuredId(policyNumberOrInsuredId);
			System.out.println("--the current claim id---"
					+ preauthDto.getClaimDTO().getClaimId());

			//			List<PreviousClaimsTableDTO> previousClaimDTOList = SHAUtils
			//					.getPreviousClaims(claimList, preauthDto.getClaimDTO()
			//							.getClaimId(), pedValidationService, masterService);



			//								.getClaimId());                                     //used from preauthservice

			/*List<ViewTmpClaim> claimList = new ArrayList<ViewTmpClaim>();

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
				}*/


			/* List<PreviousClaimsTableDTO> previousClaimDTOList = preauthService
				          .getPreviousClaims(claimList,
				        		  preauthDto.getClaimDTO().getClaimId());*/

			DBCalculationService dbCalculationService = new DBCalculationService();
			List<PreviousClaimsTableDTO> previousClaimDTOList = dbCalculationService.getPreviousClaims(preauthDto.getClaimDTO().getKey(), preauthDto.getNewIntimationDTO().getPolicy().getKey(), 
					preauthDto.getNewIntimationDTO().getInsuredPatient().getKey(), SHAConstants.INSURED_WISE_SEARCH_TYPE);

			for(PreviousClaimsTableDTO previousClaimsTableDTO : previousClaimDTOList){
				if(previousClaimsTableDTO.getAdmissionDate()!=null && !previousClaimsTableDTO.getAdmissionDate().equals("")){
					//					Date tempDate = SHAUtils.formatTimestamp(previousClaimsTableDTO.getAdmissionDate());
					//					previousClaimsTableDTO.setAdmissionDate((SHAUtils.formatDate(tempDate)));
				}												
			}

			/*
			 * String policyNumberOrInsuredId = (String)
			 * parameters.getPrimaryParameter(); List<Claim> claimList =
			 * claimService.getClaimsByInsuredId(policyNumberOrInsuredId);
			 * List<PreviousClaimsTableDTO> previousClaimDTOList = new
			 * PreviousClaimMapper().getPreviousClaimDTOList(claimList);
			 */
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
			@Observes @CDIEvent(VIEW_BALANCE_SUM_INSURED_ENH_DETAILS) final ParameterDTO parameters) {
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

		Map<String, String>  mappedValues = new WeakHashMap<String, String>();
		mappedValues.put("packageRate",
				packageMaster != null ? !packageMaster.isEmpty() ? packageMaster.get(0).getRate()
						.toString() : null : null);
		mappedValues.put("dayCareProcedure", procedureMaster.getDayCareFlag().equalsIgnoreCase("y") ? "Yes" : "No" );
		mappedValues.put("procedureCode", procedureMaster.getProcedureCode());
		view.setPackageRate(mappedValues);
	}

	public void getSumInsuredInfoFromDB(@Observes @CDIEvent(SUM_INSURED_CALCULATION) final ParameterDTO parameters) {

		Map<String, Object> values = (Map<String, Object>) parameters.getPrimaryParameter();
		PreauthDTO bean = (PreauthDTO)parameters.getSecondaryParameter(0, PreauthDTO.class);
		String diagnosis = null;
		if(values.containsKey("diagnosisId")) {
			diagnosis = masterService.getDiagnosis(Long.valueOf((String) values.get("diagnosisId")));
		}

		Map<String, Object> medicalDecisionTableValue = null;

		values.put("productKey", bean.getNewIntimationDTO().getPolicy().getProduct().getKey());

		if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			medicalDecisionTableValue = dbCalculationService.getMedicalDecisionTableValueForGMC(values,bean.getNewIntimationDTO());
		}else{
			medicalDecisionTableValue = dbCalculationService.getMedicalDecisionTableValue(values,bean.getNewIntimationDTO());
		}

		if(values.containsKey(SHAConstants.IS_NON_ALLOPATHIC) && (Boolean)values.get(SHAConstants.IS_NON_ALLOPATHIC)) {
			Map<String, Double> nonAllopathicAmount = dbCalculationService.getNonAllopathicAmount((Long)values.get(SHAConstants.POLICY_KEY), (Long)values.get(SHAConstants.INSURED_KEY)
					,(Long)values.get("preauthKey"),"C", (Long)values.get(SHAConstants.CLAIM_KEY));
			medicalDecisionTableValue.put(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT, nonAllopathicAmount.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT));
			medicalDecisionTableValue.put(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT, nonAllopathicAmount.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT));

			//added for jira IMSSUPPOR-27044
			if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MEDI_CLASSIC_HELATH_GROUP_PRODUCT_KEY)){
				medicalDecisionTableValue.put(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT, bean.getBalanceSI());
				medicalDecisionTableValue.put(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT, 0d);
			}

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
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getSecondaryParameters()[0];

		/** The below Code is Changed as per CR- R1169**/
		//Investigation checkInitiateInvestigation = preauthService.checkInitiateInvestigation(claimKey);
		Boolean checkInitiateInvestigation = preauthService.checkInvestigationPending(claimKey);
		view.setInvestigationRule(checkInitiateInvestigation);
		Boolean isValidUser = true;
		//if(preauthDTO.getStatusKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS)){

		String amount =preauthDTO.getAmountConsideredForClaimAlert();
		int amountCons = SHAUtils.getIntegerFromStringWithComma(amount);
		if(amount != null && !amount.isEmpty()){
			Long amountConsidered = Long.valueOf(amountCons);

			String limitAmountValidation = dbCalculationService.getLimitAmountValidation(preauthDTO.getStrUserName(),amountConsidered, SHAConstants.CASHLESS_CHAR);
			if(limitAmountValidation != null && limitAmountValidation.equalsIgnoreCase("N")){
				isValidUser = false;
			}
		}

		//}

		if(!isValidUser){
			preauthDTO.setAllowedToNxt(false);
			view.validationForLimitAmount();
		}else{
			preauthDTO.setAllowedToNxt(true);
		}


	}

	public void setBalanceSumInsured(@Observes @CDIEvent(BALANCE_SUM_INSURED) final ParameterDTO parameters) 
	{
		/*	PolicyDto policyDTO = (PolicyDto) parameters.getPrimaryParameter();
		//TODO : need to change the method call
//		Double balanceSI = dbCalculationService.getBalanceSI(policyDTO.getKey(), policyDTO.getTotalSumInsured());
		Double balanceSI = new Double("9999");
		view.setBalanceSumInsured(balanceSI);*/

		/**
		 * Since BalanceSI procedure requires insured key as one parameter,
		 * now intimation dto is passed instead of policy DTO and insured key is
		 * obtained from this new intimation dto.
		 * */
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		NewIntimationDto newIntimationDTO = preauthDTO.getNewIntimationDTO();
		//Integer insuredSumInsured = dbCalculationService.getInsuredSumInsured(intimationDTO.getPolicy().getKey(), intimationDTO.getInsuredPatient().getInsuredId().toString());
		Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(newIntimationDTO.getInsuredPatient().getInsuredId().toString(), newIntimationDTO.getPolicy().getKey(),newIntimationDTO.getInsuredPatient().getLopFlag());


		//Double balanceSI = dbCalculationService.getBalanceSI(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(), intimationDTO.getPolicy().getTotalSumInsured());
		//Double balanceSI = dbCalculationService.getBalanceSI(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(), Double.valueOf(insuredSumInsured.toString()));

		Double balanceSI = 0d;
		List<Double> copayValue = new ArrayList<Double>();
		if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			balanceSI = dbCalculationService.getBalanceSIForGMC(newIntimationDTO.getPolicy().getKey() ,newIntimationDTO.getInsuredPatient().getKey(),preauthDTO.getClaimKey());
			copayValue = dbCalculationService.getProductCoPayForGMC(newIntimationDTO.getPolicy().getKey(),newIntimationDTO.getInsuredPatient().getKey());
		}else{
			if(ReferenceTable.getFHORevisedKeys().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())
					|| /*ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())*/
					(preauthDTO.getNewIntimationDTO().getPolicy().getProduct() != null 
					&& (((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
							SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
							||  SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode())
							|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
					&& ("G").equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getInsuredPatient().getPolicyPlan())))
					|| (preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode() != null 
						&& (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
								SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode())))){
				if(preauthDTO.getPreauthDataExtractionDetails().getHospitalisationDueTo() != null && preauthDTO.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId() != null && 
						preauthDTO.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId().equals(ReferenceTable.INJURY_MASTER_ID) && preauthDTO.getPreauthDataExtractionDetails().getCauseOfInjury() != null && ReferenceTable.CAUSE_OF_INJURY_ACCIDENT_KEY.equals(preauthDTO.getPreauthDataExtractionDetails().getCauseOfInjury().getId())){
					balanceSI = dbCalculationService.getRTABalanceSI(newIntimationDTO.getPolicy().getKey() ,newIntimationDTO.getInsuredPatient().getKey(),preauthDTO.getClaimKey(), 0l).get(SHAConstants.TOTAL_BALANCE_SI);
				}
				else{
					balanceSI = dbCalculationService.getBalanceSI(newIntimationDTO.getPolicy().getKey() ,newIntimationDTO.getInsuredPatient().getKey(),preauthDTO.getClaimKey(), insuredSumInsured,newIntimationDTO.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);	
				}
			}
			else if(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				String subCover = "";
				if(null != preauthDTO.getPreauthDataExtractionDetails() && null != preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO() && null != preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover()){			
					subCover = preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue();
				}
				balanceSI = dbCalculationService.getBalanceSIForStarCancerGold(preauthDTO.getNewIntimationDTO().getPolicy().getKey() ,preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getClaimKey(), insuredSumInsured,preauthDTO.getNewIntimationDTO().getKey(),subCover).get(SHAConstants.TOTAL_BALANCE_SI);	
			}
			else{
				balanceSI = dbCalculationService.getBalanceSI(newIntimationDTO.getPolicy().getKey() ,newIntimationDTO.getInsuredPatient().getKey(),preauthDTO.getClaimKey(), insuredSumInsured,newIntimationDTO.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
			}	

			copayValue = dbCalculationService.getProductCoPay(newIntimationDTO.getPolicy().getProduct().getKey() ,newIntimationDTO.getInsuredPatient().getKey(), newIntimationDTO.getInsuredPatient().getInsuredId(),newIntimationDTO);
		}


		view.setBalanceSumInsured(balanceSI, copayValue);
	}

	/*private String getInsuredAge(Date insuredDob) {
		Calendar dob = Calendar.getInstance();
		 String insuredAge = "0";
		if (insuredDob != null) {
			dob.setTime(insuredDob);
			Calendar today = Calendar.getInstance();
			int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
			insuredAge = String.valueOf(age);
		}
		return insuredAge;
	}*/


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
		Double sumInsured = dbCalculationService.getInsuredSumInsured(preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), preauthDTO.getPolicyDto().getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		/*if (policyDTO.getInsuredSumInsured() == 0) {
			sumInsured = policyDTO.getTotalSumInsured();
		}*/
		if (sumInsured == 0) {
			sumInsured = policyDTO.getTotalSumInsured();
		}
		referenceData.put("previousPreauth", previousPreauthService.search(policyDTO.getClaimKey(), false));

		referenceData.put("policyAgeing", preauthDTO.getNewIntimationDTO().getPolicyYear() != null ? preauthDTO.getNewIntimationDTO().getPolicyYear() : "");
		//		preauthDTO.getPreauthDataExtractionDetails().getSection()

		String policyPlan = preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0";

		/*if(ReferenceTable.MEDI_CLASSIC_BASIC_PRODUCT_KEY.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()) || ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())) {*/
		if(preauthDTO.getNewIntimationDTO().getPolicy().getProduct() != null 
				&& (((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
						SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode())) ||
						SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode())
						|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
				|| ((SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()) || 
						SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
					&& preauthDTO.getNewIntimationDTO().getPolicy().getProductType().getKey().equals(ReferenceTable.INDIVIDUAL_POLICY)))) {
			policyPlan = preauthDTO.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() != null ? preauthDTO.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() : "0";
		}

		if(preauthDTO.getPreauthDataExtractionDetails().getSection() != null){

			if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()) ||
					ReferenceTable.STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS_KEY.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
						preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getSectionCode());
				referenceData.put("sublimitDBDetails", dbCalculationService
						.getClaimedAmountDetailsForSectionForGMC(policyDTO.getKey(),
								sumInsured, insuredAge,preauthDTO.getPreauthDataExtractionDetails().getSection().getId(),
								preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0", (preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : preauthDTO.getClaimDTO().getClaimSubCoverCode())));
			}else{				

				referenceData.put("sublimitDBDetails", dbCalculationService
						.getClaimedAmountDetailsForSection(policyDTO.getProduct().getKey(),
								sumInsured, 0l, insuredAge,preauthDTO.getPreauthDataExtractionDetails().getSection().getId(),
								policyPlan, (preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : preauthDTO.getClaimDTO().getClaimSubCoverCode()),preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
			}

		}else{
			if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()) ||
					ReferenceTable.STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS_KEY.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
						preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getSectionCode());
				referenceData.put("sublimitDBDetails", dbCalculationService
						.getClaimedAmountDetailsForSectionForGMC(policyDTO.getKey(),
								sumInsured, insuredAge,0l,"0", (preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : preauthDTO.getClaimDTO().getClaimSubCoverCode())));
			}else{

				referenceData.put("sublimitDBDetails", dbCalculationService
						.getClaimedAmountDetailsForSection(policyDTO.getProduct().getKey(),
								sumInsured, 0l, insuredAge,0l,policyPlan, (preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : preauthDTO.getClaimDTO().getClaimSubCoverCode()),preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
			}

		}

		//		referenceData.put("sublimitDBDetails", dbCalculationService.getClaimedAmountDetails(policyDTO.getProduct().getKey(), sumInsured, insuredAge));
		Map<Integer, Object> hospitalizationDetails = new HashMap<Integer, Object>();

		if(preauthDTO.getPreauthDataExtractionDetails().getSection() != null){
			if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
						preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getSectionCode());
				hospitalizationDetails = dbCalculationService.getHospitalizationDetailsForGMC(policyDTO.getProduct().getKey(),
						sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),preauthDTO.getPreauthDataExtractionDetails().getSection().getId(),"A");
			}else{
				hospitalizationDetails = dbCalculationService.getHospitalizationDetails(policyDTO.getProduct().getKey(),
						sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),preauthDTO.getPreauthDataExtractionDetails().getSection().getId(),"A");
			}

		}else{
			if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
						preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getSectionCode());
				hospitalizationDetails = dbCalculationService.getHospitalizationDetailsForGMC(policyDTO.getProduct().getKey(),
						sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),0l,"0");
			}else{
				hospitalizationDetails = dbCalculationService.getHospitalizationDetails(policyDTO.getProduct().getKey(),
						sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),0l,policyPlan);
			}			
		}

		referenceData.put("claimDBDetails", hospitalizationDetails);
		//SHAUtils.setClearMapIntegerValue(hospitalizationDetails);
		//referenceData.put("insuredPedList", masterService.getInusredPEDList(policyDTO.getInsuredId()));
		/**
		 * Insured id will be obtained from intimation dto and not from policy dto. This is due
		 * to the change in policy related tables during policy table refractoring activity.
		 * */
		referenceData.put("insuredPedList", masterService.getInusredPEDList(preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString()));
		List<Double> copayValue = new ArrayList<Double>();
		if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			copayValue = dbCalculationService.getProductCoPayForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),preauthDTO.getNewIntimationDTO()
					.getInsuredPatient().getKey());
		}else{
			copayValue = dbCalculationService.getProductCoPay(
					preauthDTO.getNewIntimationDTO().getPolicy().getProduct()
					.getKey(), preauthDTO.getNewIntimationDTO()
					.getInsuredPatient().getKey(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(),preauthDTO.getNewIntimationDTO());
		}
		preauthDTO.setProductCopay(copayValue);
		referenceData.put("copay", getCopayValues(preauthDTO));
	}

	private BeanItemContainer<SelectValue> getCopayValues(PreauthDTO dto) {
		BeanItemContainer<SelectValue> coPayContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		List<String> coPayPercentage = new ArrayList<String>();
		for (Double string : dto.getProductCopay()) {
			coPayPercentage.add(String.valueOf(string));
		}

		Long i = 0l;
		for (String string : coPayPercentage) {
			SelectValue value = new SelectValue();
			String[] copayWithPercentage = string.split("\\.");
			String copay = copayWithPercentage[0].trim();
			value.setId(Long.valueOf(copay));
			value.setValue(string);
			coPayContainer.addBean(value);
			i++;
		}

		return coPayContainer;
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}

	public void setPreAuthRequestedAmount (@Observes @CDIEvent(GET_PREAUTH_REQUESTED_AMOUT) final ParameterDTO parameters)
	{
		PreviousPreAuthTableDTO dto = (PreviousPreAuthTableDTO)parameters.getPrimaryParameter();
		view.setPreAuthRequestedAmt(preauthService.getPreauthReqAmt(dto.getKey(), dto.getClaimKey()));
	}

	public void getHospitalizationDetails(
			@Observes @CDIEvent(GET_HOSPITALIZATION_DETAILS) final ParameterDTO parameters) {
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Hospitals hospitalById = hospitalService.getHospitalById(preauthDTO.getHospitalKey());
		PolicyDto policyDTO = preauthDTO.getPolicyDto();
		//String insuredAge = getInsuredAge(policyDTO.getInsuredDob());
		Double sumInsured = dbCalculationService.getInsuredSumInsured(preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), preauthDTO.getPolicyDto().getKey()
				,preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		if (sumInsured == 0) {
			sumInsured = policyDTO.getTotalSumInsured();
		}

		Map<Integer, Object> hospitalizationDetails = new HashMap<Integer, Object>();

		if(preauthDTO.getPreauthDataExtractionDetails().getSection() != null){
			if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
						preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getSectionCode());
				hospitalizationDetails = dbCalculationService.getHospitalizationDetailsForGMC(policyDTO.getProduct().getKey(),
						sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),preauthDTO.getPreauthDataExtractionDetails().getSection().getId(),"A");
			}else{
				hospitalizationDetails = dbCalculationService.getHospitalizationDetails(policyDTO.getProduct().getKey(),
						sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),preauthDTO.getPreauthDataExtractionDetails().getSection().getId(),"A");
			}
		}else{
			if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
						preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getSectionCode());
				hospitalizationDetails = dbCalculationService.getHospitalizationDetailsForGMC(policyDTO.getProduct().getKey(),
						sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),0l,"0");
			}else{

				String policyPlan = preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan();

				/*if(ReferenceTable.MEDI_CLASSIC_BASIC_PRODUCT_KEY.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()) || ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())) {*/

				if(preauthDTO.getNewIntimationDTO().getPolicy().getProduct() != null 
						&& (((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode())  ||
								SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()) )
								|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode())
								|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
						|| ((SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
								SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
								&& preauthDTO.getNewIntimationDTO().getPolicy().getProductType().getKey().equals(ReferenceTable.INDIVIDUAL_POLICY)))) {
					policyPlan = preauthDTO.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() != null ? preauthDTO.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() : "";
				}	
				hospitalizationDetails = dbCalculationService.getHospitalizationDetails(policyDTO.getProduct().getKey(),
						sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),0l,policyPlan);
			}


		}

		referenceData.put("claimDBDetails", hospitalizationDetails);
		//SHAUtils.setClearMapIntegerValue(hospitalizationDetails);
		view.setHospitalizationDetails(hospitalizationDetails);

	}

	public void generateFieldsBasedOnHospitalisationDueTo(@Observes @CDIEvent(PREAUTH_ENHANCEMENT_HOSPITALISATION_DUE_TO) final ParameterDTO parameters)
	{
		SelectValue selectedValue = (SelectValue) parameters.getPrimaryParameter();
		view.genertateFieldsBasedOnHospitalisionDueTo(selectedValue);
	}

	public void generateFieldsBasedOnReportedToPolice(@Observes @CDIEvent(PREAUTH_ENHANCEMENT_REPORTED_TO_POLICE) final ParameterDTO parameters)
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
		Double sumInsured = dbCalculationService.getInsuredSumInsured(preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), preauthDTO.getPolicyDto().getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		/*if (policyDTO.getInsuredSumInsured() == 0) {
			sumInsured = policyDTO.getTotalSumInsured();
		}*/
		if (sumInsured == 0) {
			sumInsured = policyDTO.getTotalSumInsured();
		}

		String policyPlan = preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0";

		/*if(ReferenceTable.MEDI_CLASSIC_BASIC_PRODUCT_KEY.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()) || ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())) {*/
		if(preauthDTO.getNewIntimationDTO().getPolicy().getProduct() != null 
				&& (((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
						SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
						|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode())
						|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
				|| ((SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
						SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
					&& preauthDTO.getNewIntimationDTO().getPolicy().getProductType().getKey().equals(ReferenceTable.INDIVIDUAL_POLICY)))) {
			policyPlan = preauthDTO.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() != null ? preauthDTO.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() : "0";
		}

		if(preauthDTO.getPreauthDataExtractionDetails().getSection() != null){

			if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
						preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getSectionCode());
				referenceData.put("sublimitDBDetails", dbCalculationService
						.getClaimedAmountDetailsForSectionForGMC(policyDTO.getKey(),
								sumInsured, insuredAge,preauthDTO.getPreauthDataExtractionDetails().getSection().getId(),
								preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0", (preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : preauthDTO.getClaimDTO().getClaimSubCoverCode())));
			}else{
				referenceData.put("sublimitDBDetails", dbCalculationService
						.getClaimedAmountDetailsForSection(policyDTO.getProduct().getKey(),
								sumInsured, 0l, insuredAge,preauthDTO.getPreauthDataExtractionDetails().getSection().getId(),
								policyPlan, (preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : preauthDTO.getClaimDTO().getClaimSubCoverCode()),preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
			}



		}else{
			if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
						preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getSectionCode());
				referenceData.put("sublimitDBDetails", dbCalculationService
						.getClaimedAmountDetailsForSectionForGMC(policyDTO.getKey(),
								sumInsured, insuredAge,0l,"0", (preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : preauthDTO.getClaimDTO().getClaimSubCoverCode())));
			}else{
				referenceData.put("sublimitDBDetails", dbCalculationService
						.getClaimedAmountDetailsForSection(policyDTO.getProduct().getKey(),
								sumInsured, 0l, insuredAge,0l,policyPlan, (preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : preauthDTO.getClaimDTO().getClaimSubCoverCode()),preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
			}

		}

		Map<Integer, Object> hospitalizationDetails = new HashMap<Integer, Object>();

		if(preauthDTO.getPreauthDataExtractionDetails().getSection() != null){
			if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
						preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getSectionCode());
				hospitalizationDetails = dbCalculationService.getHospitalizationDetailsForGMC(policyDTO.getProduct().getKey(),
						sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),preauthDTO.getPreauthDataExtractionDetails().getSection().getId(),"A");
			}else{
				hospitalizationDetails = dbCalculationService.getHospitalizationDetails(policyDTO.getProduct().getKey(),
						sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),preauthDTO.getPreauthDataExtractionDetails().getSection().getId(),"A");
			}
		}else{
			if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
						preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getSectionCode());
				hospitalizationDetails = dbCalculationService.getHospitalizationDetailsForGMC(policyDTO.getProduct().getKey(),
						sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),0l,"0");
			}else{
				hospitalizationDetails = dbCalculationService.getHospitalizationDetails(policyDTO.getProduct().getKey(),
						sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),0l,preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan());
			}


		}

		referenceData.put("claimDBDetails", hospitalizationDetails);
		//SHAUtils.setClearMapIntegerValue(hospitalizationDetails);

		//		view.setWizardPageReferenceData(referenceData);

	}


	public void rechargingSIEvent(
			@Observes @CDIEvent(RECHARGE_SI_FOR_PRODUCT) final ParameterDTO parameters) {
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();

		Long policyKey = preauthDTO.getNewIntimationDTO().getPolicy().getKey();
		Long insuredKey = preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey();

		dbCalculationService.rechargingSIbasedOnProduct(policyKey, insuredKey);
		Double balanceSI = 0d;
		if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			balanceSI = dbCalculationService.getBalanceSIForGMC(
					policyKey,insuredKey, preauthDTO.getClaimDTO().getKey());
		}else{
			balanceSI = dbCalculationService.getBalanceSI(
					policyKey,insuredKey, preauthDTO.getClaimDTO().getKey(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredSumInsured(),preauthDTO.getNewIntimationDTO().getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
		}


		//				Double balanceSI = 500000d;


		view.setBalanceSIforRechargedProcess(balanceSI);

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

	public void getRemarks(
			@Observes @CDIEvent(ENHANCEMENT_GENERATE_REMARKS) final ParameterDTO parameters) {
		Long id = (Long) parameters.getPrimaryParameter();
		String decision = (String) parameters.getSecondaryParameters()[0];

		MasterRemarks remarks = masterService.getRemarks(id);
		view.setRemarks(remarks,decision);
	}

	public void getRejectWithdrawRemarks(
			@Observes @CDIEvent(GENERATE_WITHDRAW_REJECT_REMARKS) final ParameterDTO parameters) {
		Long id = (Long) parameters.getPrimaryParameter();

		MasterRemarks remarks = masterService.getRemarks(id);
		view.setRemarks(remarks,SHAConstants.ENHANCEMENT_WITHDRAW_REJECT);
	}

	public void generateDownsizeReasonLayout(
			@Observes @CDIEvent(ENHANCEMENT_DOWNSIZE_LAYOUT) final ParameterDTO parameters) {

		Long id = (Long) parameters.getPrimaryParameter();
		String decision = (String) parameters.getSecondaryParameters()[0];

		MasterRemarks remarks = masterService.getRemarks(id);
		view.setRemarks(remarks,decision);
	}

	public void generateReferCPULayout(@Observes @CDIEvent(ENHANCEMENT_REFER_CPU_USER) final ParameterDTO parameters)
	{
		view.generateCPUUserLayout(masterService.getSelectValueContainer(ReferenceTable.AUTO_PROCESS_SUGGESTION));
	}


	public void generateVB64ComplainceLayout(@Observes @CDIEvent(REFER_VB_64_COMPLAINCE) final ParameterDTO parameters)
	{
		view.generate64VBComplainceLayout();
	}



	public void getPreviousClaimForPolicy(
			@Observes @CDIEvent(ENHANCEMENT_PREVIOUS_CLAIM_FOR_POLICY) final ParameterDTO parameters) {
		PreauthDTO preauthDto = (PreauthDTO) parameters.getPrimaryParameter();

		/*String claimId = preauthDto.getClaimDTO().getClaimId();

		String policyNumber = preauthDto.getPolicyDto().getPolicyNumber();
		List<ViewTmpClaim> previousclaimsList = new ArrayList<ViewTmpClaim>();



//		List<ViewTmpClaim> claimsByPolicyNumber = claimService
//				.getViewTmpClaimsByPolicyNumber(policyNumber);
		Policy byPolicyNumber = policyService.getByPolicyNumber(policyNumber);
		List<ViewTmpIntimation> intimationKeys = intimationService.getIntimationByPolicyKey(byPolicyNumber.getKey());
		List<ViewTmpClaim> claimsByPolicyNumber = claimService
							.getViewTmpClaimsByIntimationKeys(intimationKeys);
		previousclaimsList.addAll(claimsByPolicyNumber);

		previousclaimsList = getPreviousClaimForPreviousPolicy(byPolicyNumber.getRenewalPolicyNumber(), previousclaimsList);*/

		DBCalculationService dbCalculationService = new DBCalculationService();
		List<PreviousClaimsTableDTO> previousClaimDTOList = dbCalculationService.getPreviousClaims(preauthDto.getClaimDTO().getKey(), preauthDto.getNewIntimationDTO().getPolicy().getKey(), 
				preauthDto.getNewIntimationDTO().getInsuredPatient().getKey(), SHAConstants.POLICY_WISE_SEARCH_TYPE);

		//			List<PreviousClaimsTableDTO> previousClaimDTOList = SHAUtils
		//					.getPreviousClaims(claimsByPolicyNumber,
		//							claimByKey.getClaimId(), pedValidationService,
		//							masterService);

		/*List<PreviousClaimsTableDTO> previousClaimDTOList = preauthService
				.getPreviousClaims(previousclaimsList,
						claimId);*/

		view.setPreviousClaimDetailsForPolicy(previousClaimDTOList);
	}

	public void showUpdateClaimDetailTable(
			@Observes @CDIEvent(UPDATE_PREVIOUS_CLAIM_DETAILS) final ParameterDTO parameters) {

		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		List<UpdateOtherClaimDetailDTO> updateOtherClaimDetails = new ArrayList<UpdateOtherClaimDetailDTO>();

		if(preauthDTO.getUpdateOtherClaimDetailDTO().isEmpty()){
			//			updateOtherClaimDetails = preauthService.getUpdateOtherClaimDetails(preauthDTO.getNewIntimationDTO().getPolicy().getKey());
			updateOtherClaimDetails = preauthService.getUpdateOtherClaimDetailsDTO(preauthDTO.getKey());

			if(updateOtherClaimDetails != null && updateOtherClaimDetails.isEmpty()){
				updateOtherClaimDetails = preauthService.getUpdateOtherClaimDetails(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),preauthDTO);
			}

		}else{
			updateOtherClaimDetails = preauthDTO.getUpdateOtherClaimDetailDTO();
		}
		view.setUpdateOtherClaimsDetails(updateOtherClaimDetails, referenceData);

	}

	public List<ViewTmpClaim> getPreviousClaimForPreviousPolicy(String policyNumber, List<ViewTmpClaim> generatedList) {
		try {
			Policy renewalPolNo = policyService.getByPolicyNumber(policyNumber);
			if(renewalPolNo != null) {
				List<ViewTmpIntimation> intimationKeys = intimationService.getIntimationByPolicyKey(renewalPolNo.getKey());
				List<ViewTmpClaim> claimsByPolicyNumber = claimService
						.getViewTmpClaimsByIntimationKeys(intimationKeys);
				//				List<ViewTmpClaim> previousPolicyPreviousClaims = claimService.getViewTmpClaimsByPolicyNumber(renewalPolNo.getPolicyNumber());
				if(claimsByPolicyNumber != null && !claimsByPolicyNumber.isEmpty()) {
					for (ViewTmpClaim viewTmpClaim : claimsByPolicyNumber) {
						if(!generatedList.contains(viewTmpClaim)) {
							generatedList.add(viewTmpClaim);
						}
					}
				}
				if(renewalPolNo != null && renewalPolNo.getRenewalPolicyNumber() != null ) {
					getPreviousClaimForPreviousPolicy(renewalPolNo.getRenewalPolicyNumber(), generatedList);
				} else {
					return generatedList;
				}
			}
		} catch(Exception e) {

		}
		return generatedList;
	}

	public void setClearReferenceData(
			@Observes @CDIEvent(REFERENCE_DATA_CLEAR) final ParameterDTO parameters) {
		SHAUtils.setClearReferenceData(referenceData);
	}

	public void showPTCACABGEvent(
			@Observes @CDIEvent(ENHANCEMENT_SHOW_PTCA_CABG) final ParameterDTO parameters) {
		Boolean checkValue = (Boolean) parameters.getPrimaryParameter();
		view.generatePTCACABGLayout(checkValue);
	}

	public void showOtherBenefitsPopup(
			@Observes @CDIEvent(VIEW_PREAUTH_ENH_OTHER_BENEFITS) final ParameterDTO parameters) {
		view.generateOtherBenefitsPopup();
	}

	public void showPTCACABGEventAfterDelete(
			@Observes @CDIEvent(ENHANCEMENT_SHOW_PTCA_CABG_DELETE) final ParameterDTO parameters) {
		view.generatePTCACABGLayoutDelete();
	}



	public void generatePanCardLayout(@Observes @CDIEvent(PREAUTH_UPDATE_PAN_CARD_EVENT) final ParameterDTO parameters)
	{
		view.generatePanCardLayout();
	}

	public void checkPanCardDetails(@Observes @CDIEvent(CHECK_PAN_CARD_DETAILS) final ParameterDTO parameters)
	{
		Long policyKey = (Long) parameters.getPrimaryParameter();
		Policy policyByKey = policyService.getPolicyByKey(policyKey);
		if(policyByKey.getProposedPanNumber()!=null){
			view.checkPanCardDetails(Boolean.TRUE);
		}else{
			view.checkPanCardDetails(Boolean.FALSE);
		}
	}

	public void rechargingRTASIEvent(
			@Observes @CDIEvent(RTA_RECHARGE_SI_FOR_PRODUCT) final ParameterDTO parameters) {
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();

		Long policyKey = preauthDTO.getNewIntimationDTO().getPolicy().getKey();
		Long insuredKey = preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey();				

		dbCalculationService.getRTARechargeSI(policyKey , insuredKey,preauthDTO.getClaimDTO().getKey());				

		Double balanceSI = dbCalculationService.getRTABalanceSI(
				policyKey , insuredKey,preauthDTO.getClaimDTO().getKey(),preauthDTO.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
		view.setBalanceSIforRechargedProcess(balanceSI);

	}

	public void getAssistedReprodTreatment(@Observes @CDIEvent(GET_ASSISTED_REPRODUCTION_TREATMENT_COVER_KEY_PE) final ParameterDTO parameters) {
		try{		
			PreauthDTO bean = (PreauthDTO) parameters.getPrimaryParameter();
			Long assistedKey = reimbursementService.getAssistedReprodTreatment(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getId());
			view.setAssistedReprodTreatment(assistedKey);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void preauthLumenRequest(@Observes @CDIEvent(PREAUTH_ENHANCEMENT_LUMEN_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		view.buildInitiateLumenRequest(preauthDTO.getNewIntimationDTO().getIntimationId());
	}

	public void getPreviousClaimDetails(
			@Observes @CDIEvent(PREAUTH_PREVIOUS_CLAIM_DETAILS) final ParameterDTO parameters) {
		PreauthDTO preauthDto = (PreauthDTO) parameters.getPrimaryParameter();
		if (preauthDto != null
				&& preauthDto.getNewIntimationDTO() != null
				&& preauthDto.getNewIntimationDTO().getInsuredPatient() != null
				&& preauthDto.getNewIntimationDTO().getInsuredPatient()
				.getInsuredId() != null) {
			String selectionType = (String)parameters.getSecondaryParameters()[0];

			DBCalculationService dbCalculationService = new DBCalculationService();

			List<PreviousClaimsTableDTO> previousClaimDTOList = new ArrayList<PreviousClaimsTableDTO>();

			if(null != selectionType && SHAConstants.POLICY_WISE.equalsIgnoreCase(selectionType)){

				previousClaimDTOList = dbCalculationService.getPreviousClaims(preauthDto.getClaimDTO().getKey(), preauthDto.getNewIntimationDTO().getPolicy().getKey(), 
						preauthDto.getNewIntimationDTO().getInsuredPatient().getKey(), SHAConstants.POLICY_WISE_SEARCH_TYPE);
			}
			else if(SHAConstants.INSURED_WISE.equalsIgnoreCase(selectionType)){
				previousClaimDTOList = dbCalculationService.getPreviousClaims(preauthDto.getClaimDTO().getKey(), preauthDto.getNewIntimationDTO().getPolicy().getKey(), 
						preauthDto.getNewIntimationDTO().getInsuredPatient().getKey(), SHAConstants.INSURED_WISE_SEARCH_TYPE);
			}
			else{
				previousClaimDTOList = dbCalculationService.getPreviousClaims(preauthDto.getClaimDTO().getKey(), preauthDto.getNewIntimationDTO().getPolicy().getKey(), 
						preauthDto.getNewIntimationDTO().getInsuredPatient().getKey(), SHAConstants.RISK_WISE_SEARCH_TYPE);
			}				
			view.getPreviousClaimDetails(previousClaimDTOList);
		}
	}

	//IMSSUPPOR-24086
	public void generateGradingBasedOnFieldVisit(
			@Observes @CDIEvent(ENHANCEMENT_FIELD_VISIT_GRADING) final ParameterDTO parameters) {

		PreauthDTO preauthdto = (PreauthDTO) parameters.getPrimaryParameter();
		Preauth preauth = preauthService.getPreauthById(preauthdto.getKey());

		List<FieldVisitRequest> fvrByClaimKey = reimbursementService
				.getFVRByClaimKey(preauth.getClaim().getKey());

		List<FVRGradingMaster> fvrGradingSegB = reimbursementService
				.getFVRGradingBySegment(SHAConstants.FVR_GRADING_SEGMENT_B);

		List<FVRGradingMaster> fvrGradingSegC = reimbursementService
				.getFVRGradingBySegment(SHAConstants.FVR_GRADING_SEGMENT_C);

		List<FvrGradingDetailsDTO> fvrDtoList = new ArrayList<FvrGradingDetailsDTO>();
		// Map<Integer, FieldVisitRequest> valueMap = new HashMap<Integer,
		// FieldVisitRequest>();
		FvrGradingDetailsDTO fvrDto = null;
		List<NewFVRGradingDTO> FVRTableDTO = null;
		for (int i = 0; i < fvrByClaimKey.size(); i++) {
			FieldVisitRequest fieldVisitRequest = fvrByClaimKey.get(i);
			if (fieldVisitRequest.getStatus() != null
					&& (fieldVisitRequest.getStatus().getKey()
							.equals(ReferenceTable.FVR_REPLY_RECEIVED)
							|| fieldVisitRequest.getStatus().getKey()
							.equals(ReferenceTable.FVR_GRADING_STATUS) || fieldVisitRequest
							.getStatus().getKey()
							.equals(ReferenceTable.ASSIGNFVR))) {
				fvrDto = new FvrGradingDetailsDTO();
				fvrDto.setKey(fieldVisitRequest.getKey());
				fvrDto.setRepresentativeName(fieldVisitRequest
						.getRepresentativeName());
				fvrDto.setRepresentiveCode(fieldVisitRequest
						.getRepresentativeCode());

				if (fieldVisitRequest.getStatus() != null
						&& (fieldVisitRequest.getStatus().getKey()
								.equals(ReferenceTable.FVR_REPLY_RECEIVED) || fieldVisitRequest
								.getStatus().getKey()
								.equals(ReferenceTable.FVR_GRADING_STATUS))) {
					fvrDto.setIsFVRReceived(Boolean.TRUE);
				}

				FVRTableDTO = new ArrayList<NewFVRGradingDTO>();
				NewFVRGradingDTO eachFVRDTO = null;
				Boolean isSegmentABEdit = true;
				Boolean isSegmentCEdit = true;
				List<FVRGradingDetail> fvrGradingDetails = reimbursementService
						.getFVRGradingDetails(fieldVisitRequest.getKey());

				List<FvrTriggerPoint> fvrTriggerPoints = reimbursementService
						.getFVRTriggerPoints(fieldVisitRequest.getKey());

				for (FvrTriggerPoint masterFVR : fvrTriggerPoints) {
					eachFVRDTO = new NewFVRGradingDTO();
					eachFVRDTO.setFvrSeqNo(masterFVR.getKey());
					eachFVRDTO.setCategory(masterFVR.getRemarks());
					eachFVRDTO.setSegment(SHAConstants.FVR_GRADING_SEGMENT_A);

					if (fvrGradingDetails != null
							&& !fvrGradingDetails.isEmpty()) {
						for (FVRGradingDetail fvrGradingDetail : fvrGradingDetails) {
							if (fvrGradingDetail.getSeqNo() != null) {
								if (fvrGradingDetail.getSeqNo().equals(
										masterFVR.getKey())) {
									eachFVRDTO
									.setKey(fvrGradingDetail.getKey());
									if (fvrGradingDetail.getGrading() != null) {
										eachFVRDTO
										.setStatusFlagSegmentA(fvrGradingDetail
												.getGrading());
										isSegmentCEdit = false;
										isSegmentABEdit = true;
									}
									break;
								}
							}

						}
					}
					FVRTableDTO.add(eachFVRDTO);
				}

				for (FVRGradingMaster masterFVR : fvrGradingSegB) {
					eachFVRDTO = new NewFVRGradingDTO();
					eachFVRDTO.setFvrSeqNo(masterFVR.getKey());
					eachFVRDTO.setCategory(masterFVR.getGradingType());
					eachFVRDTO.setSegment(SHAConstants.FVR_GRADING_SEGMENT_B);

					if (fvrGradingDetails != null
							&& !fvrGradingDetails.isEmpty()) {
						for (FVRGradingDetail fvrGradingDetail : fvrGradingDetails) {
							if (fvrGradingDetail.getSeqNo() != null) {
								if (fvrGradingDetail.getSeqNo().equals(
										masterFVR.getKey())) {
									eachFVRDTO
									.setKey(fvrGradingDetail.getKey());
									if (fvrGradingDetail.getGrading() != null) {
										eachFVRDTO
										.setStatusFlag(fvrGradingDetail
												.getGrading());
										isSegmentCEdit = false;
										isSegmentABEdit = true;
									}
									break;
								}
							}
						}
					}
					FVRTableDTO.add(eachFVRDTO);
				}

				if (fieldVisitRequest.getStatus().getKey()
						.equals(ReferenceTable.FVR_REPLY_RECEIVED)) {

					for (FVRGradingMaster masterFVR : fvrGradingSegC) {
						eachFVRDTO = new NewFVRGradingDTO();
						eachFVRDTO.setFvrSeqNo(masterFVR.getKey());
						if (masterFVR.getKey().intValue() == 22) {
							eachFVRDTO.setIsFVRReceived(Boolean.TRUE);
							// eachFVRDTO.setStatusFlagSegmentC(SHAConstants.YES_FLAG);
						}
						eachFVRDTO.setCategory(masterFVR.getGradingType());
						eachFVRDTO
						.setSegment(SHAConstants.FVR_GRADING_SEGMENT_C);

						if (fvrGradingDetails != null
								&& !fvrGradingDetails.isEmpty()) {
							for (FVRGradingDetail fvrGradingDetail : fvrGradingDetails) {
								if (fvrGradingDetail.getSeqNo() != null) {
									if (fvrGradingDetail.getSeqNo().equals(
											masterFVR.getKey())) {
										eachFVRDTO.setKey(fvrGradingDetail
												.getKey());
										if (masterFVR.getKey().intValue() != 22
												&& fvrGradingDetail
												.getGrading() != null) {
											// Fix for populate any segment C
											// other then "FVR not received" if
											// fvr replied
											eachFVRDTO
											.setStatusFlagSegmentC(fvrGradingDetail
													.getGrading());
											isSegmentCEdit = true;
											isSegmentABEdit = false;
										}
										break;
									}
								}
							}
						}

						FVRTableDTO.add(eachFVRDTO);
					}

				} else if (fieldVisitRequest.getStatus().getKey()
						.equals(ReferenceTable.ASSIGNFVR)) {
					for (FVRGradingMaster masterFVR : fvrGradingSegC) {
						eachFVRDTO = new NewFVRGradingDTO();
						eachFVRDTO.setFvrSeqNo(masterFVR.getKey());
						eachFVRDTO.setCategory(masterFVR.getGradingType());
						eachFVRDTO
						.setSegment(SHAConstants.FVR_GRADING_SEGMENT_C);
						eachFVRDTO.setIsFVRReceived(Boolean.FALSE);

						if (fvrGradingDetails != null
								&& !fvrGradingDetails.isEmpty()) {
							for (FVRGradingDetail fvrGradingDetail : fvrGradingDetails) {
								if (fvrGradingDetail.getSeqNo() != null) {
									if (fvrGradingDetail.getSeqNo().equals(
											masterFVR.getKey())) {
										if (eachFVRDTO
												.getFvrSeqNo()
												.equals(ReferenceTable.GRADING_FVR_NOT_RECEIVED)) {
											eachFVRDTO.setKey(fvrGradingDetail
													.getKey());
										}
									}
								}
							}
						}

						// V1.4
						if (eachFVRDTO.getFvrSeqNo().equals(
								ReferenceTable.GRADING_FVR_NOT_RECEIVED)) {
							isSegmentCEdit = true;
							isSegmentABEdit = false;
							eachFVRDTO
							.setStatusFlagSegmentC(SHAConstants.YES_FLAG);
							eachFVRDTO.setIsAssignFVRNotReceived(Boolean.TRUE);
						}
						FVRTableDTO.add(eachFVRDTO);
					}
				} else {

					for (FVRGradingMaster masterFVR : fvrGradingSegC) {
						eachFVRDTO = new NewFVRGradingDTO();
						eachFVRDTO.setFvrSeqNo(masterFVR.getKey());
						eachFVRDTO.setCategory(masterFVR.getGradingType());
						if (masterFVR.getKey().intValue() == 22) {
							eachFVRDTO.setIsFVRReceived(Boolean.TRUE);
						} else {
							eachFVRDTO.setIsFVRReceived(Boolean.FALSE);
						}
						eachFVRDTO
						.setSegment(SHAConstants.FVR_GRADING_SEGMENT_C);

						if (fvrGradingDetails != null
								&& !fvrGradingDetails.isEmpty()) {
							for (FVRGradingDetail fvrGradingDetail : fvrGradingDetails) {
								if (fvrGradingDetail.getSeqNo() != null) {
									if (fvrGradingDetail.getSeqNo().equals(
											masterFVR.getKey())) {
										eachFVRDTO.setKey(fvrGradingDetail
												.getKey());
										if (fvrGradingDetail.getGrading() != null) {
											eachFVRDTO
											.setStatusFlagSegmentC(fvrGradingDetail
													.getGrading());
											isSegmentCEdit = true;
											isSegmentABEdit = false;
										}
										break;
									}
								}
							}
						}

						FVRTableDTO.add(eachFVRDTO);
					}

				}

				// V1.4
				/*
				 * if(fieldVisitRequest.getStatus().getKey().equals(ReferenceTable
				 * .ASSIGNFVR) ||
				 * fieldVisitRequest.getStatus().getKey().equals(ReferenceTable
				 * .FVR_GRADING_STATUS)){ isSegmentABEdit = Boolean.FALSE;
				 * isSegmentCEdit = Boolean.FALSE; }
				 */

				if (fieldVisitRequest.getStatus().getKey()
						.equals(ReferenceTable.ASSIGNFVR)) {
					fvrDto.setIsClearAllEnabled(Boolean.FALSE);
				}

				if (FVRTableDTO != null && !FVRTableDTO.isEmpty()) {

					for (NewFVRGradingDTO gradingDto : FVRTableDTO) {
						if (isSegmentABEdit && !isSegmentCEdit) {
							gradingDto.setIsEditAB(true);
						} else if (!isSegmentABEdit && isSegmentCEdit) {
							gradingDto.setIsEditAB(false);
						}/*
						 * else if(!isSegmentABEdit && !isSegmentCEdit){
						 * gradingDto.setIsEditABC(Boolean.FALSE); }
						 */
					}
				}

				if (isSegmentABEdit && !isSegmentCEdit) {
					fvrDto.setIsSegmentANotEdit(false);
					fvrDto.setIsSegmentBNotEdit(false);
					fvrDto.setIsSegmentCNotEdit(true);
				} else if (!isSegmentABEdit && isSegmentCEdit) {
					fvrDto.setIsSegmentANotEdit(true);
					fvrDto.setIsSegmentBNotEdit(true);
					fvrDto.setIsSegmentCNotEdit(false);
				}/*
				 * else if(!isSegmentABEdit && !isSegmentCEdit){
				 * fvrDto.setIsSegmentANotEdit(true);
				 * fvrDto.setIsSegmentBNotEdit(true);
				 * fvrDto.setIsSegmentCNotEdit(true); }
				 */

				fvrDto.setNewFvrGradingDTO(FVRTableDTO);

				if (fieldVisitRequest.getGradingRmrks() != null) {
					fvrDto.setGradingRemarks(fieldVisitRequest
							.getGradingRmrks());
				}

				if (fieldVisitRequest.getStatus().getKey()
						.equals(ReferenceTable.FVR_REPLY_RECEIVED)) {
					fvrDto.setIsFVRReplied(Boolean.TRUE);
				}

				fvrDtoList.add(fvrDto);
			}
		}
		preauthdto.getPreauthMedicalDecisionDetails().setFvrGradingDTO(
				fvrDtoList);
	}

	public void generatePreAuthHoldLayout(@Observes @CDIEvent(ENHANCEMENT_HOLD_EVENT) final ParameterDTO parameters)
	{
		view.generateHoldLayout();
	}

	public void invsReviewRemarksTableList(
			@Observes @CDIEvent(INVS_REVIEW_REMARKS_LIST) final ParameterDTO parameters) {

		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		List<AssignedInvestigatiorDetails> invsReviewRemarksList = reimbursementService.getInvsReviewRemarksDetails(preauthDTO);
		preauthDTO.getPreauthMedicalDecisionDetails().setInvsReviewRemarksTableList(invsReviewRemarksList);

	}
	public void saveCancelRemarks(@Observes @CDIEvent(SAVE_AUTO_ALLOCATION_CANCEL_REMARKS) final ParameterDTO parameters) {
		PreauthDTO preauthdto = (PreauthDTO) parameters.getPrimaryParameter();		
		if(null != preauthdto && null != preauthdto.getKey()){
			Preauth preauth = preauthService.getPreauthById(preauthdto.getKey());
			preauthService.updateCancelRemarks(preauth,preauthdto.getAutoAllocCancelRemarks(),preauthdto);
		}
	}

	public void generateFieldsBasedOnNegotiation(@Observes @CDIEvent(NEGOTIATION_OPINION_RADIO_CHANGED) final ParameterDTO parameters)
	{		
		Boolean isChecked = (Boolean) parameters.getPrimaryParameter();
		//view.genertateFieldsBasedOnNegotiation(isChecked);
	}

	public void updateNegotiation(@Observes @CDIEvent(NEGOTIATION_CANCEL_OR_UPDATE) final ParameterDTO parameters)
	{		
		NegotiationDetails negotiation = (NegotiationDetails) parameters.getPrimaryParameter();
		preauthService.updateNegotiationDetails(negotiation);
		//view.updateNegotiationRemarks(negotiation.getNegotiationRemarks());
	}

	public void checkNegotiation(@Observes @CDIEvent(NEGOTIATION_PENDING) final ParameterDTO parameters)
	{		
		PreauthDTO bean = (PreauthDTO) parameters.getPrimaryParameter();
		NegotiationDetails negotiationPending = preauthService.getNegotiationPending(bean.getClaimDTO().getKey());
		TmpCPUCode cpuObj = preauthService.getTmpCPUCode(bean.getNewIntimationDTO().getCpuId());
		MastersValue masValue = preauthService.getMasterValue(SHAConstants.NEGOTIATION_TYPE);
		if(negotiationPending != null){
			bean.setIsNegotiationPending(true);
			bean.getPreauthMedicalDecisionDetails().setNegotiatePoints(negotiationPending.getNegotiationRemarks());
			bean.getPreauthMedicalDecisionDetails().setAmtToNegotiated(String.valueOf(negotiationPending.getNegotiationAmt()));
		}
		else
		{
			bean.setIsNegotiationPending(false);
		}
		if((null != cpuObj && null !=cpuObj.getNegotiationFlag() && SHAConstants.YES_FLAG.equalsIgnoreCase(cpuObj.getNegotiationFlag())) &&
				(null != masValue && null !=masValue.getMappingCode() && SHAConstants.YES_FLAG.equalsIgnoreCase(masValue.getMappingCode())) &&
				null != bean.getPreauthDataExtractionDetails() && null !=bean.getPreauthDataExtractionDetails().getInterimOrFinalEnhancementFlag() &&
				("F").equalsIgnoreCase(bean.getPreauthDataExtractionDetails().getInterimOrFinalEnhancementFlag())){
			bean.setIsNegotiationApplicable(Boolean.TRUE);
		}
		else
		{
			bean.setIsNegotiationApplicable(Boolean.FALSE);
		}
	}

	public void setEnhancementButtonLayout(
			@Observes @CDIEvent(ENHN_HOSP_SCORING_EVENT) final ParameterDTO parameters) {
		PreauthEnhancementButtons preauthEnhancementButtons = (PreauthEnhancementButtons) parameters.getPrimaryParameter();
		view.generateButtonLayoutBasedOnScoring(preauthEnhancementButtons);

	}

	public void getQryRemarks(
			@Observes @CDIEvent(GET_ENHANCEMENT_QUERY_REMARKS) final ParameterDTO parameters) {

		Long qryTypeKey = (Long) parameters.getPrimaryParameter();
		Map<Long, Long> queryMap = ReferenceTable.getEnhQueryTypeMap();
		Long enhQryRemarksKey = queryMap.get(qryTypeKey);

		if(enhQryRemarksKey != null) {

			MasterRemarks remarks = masterService.getQueryRemarks(enhQryRemarksKey);

			view.setQueryRemarks(remarks.getRemarks());
		}
	}

	public void enhnTopUpPolicyInsert(@Observes @CDIEvent(ENHN_TOPUP_POLICY_EVENT) final ParameterDTO parameters) {

		PreauthDTO preauthdto = (PreauthDTO) parameters.getPrimaryParameter();
		//		PremPolicyDetails policyDetails = premiaPullService.fetchGmcPolicyDetailsFromPremia(preauthdto.getNewIntimationDTO().getInsuredPatient().getTopUppolicyNo(), String.valueOf(preauthdto.getNewIntimationDTO().getInsuredPatient().getTopUpInsuredId()));
		//		Boolean isIntegratedPolicy =premiaPullService.populateGMCandGPAPolicy(policyDetails, String.valueOf(preauthdto.getNewIntimationDTO().getInsuredPatient().getTopUpInsuredId()),false);
		Insured curentPolicy = preauthService.getTopUpPolicy(preauthdto.getPolicyKey());
		//		String topUpPolicyNo = curentPolicy.getTopUppolicyNo();
		//		Map<String, Object> map = new HashMap<String, Object>();
		//		map.put("topupPolicy", premiaPullService.getPolicyByPolicyNubember(preauthdto.getNewIntimationDTO().getInsuredPatient().getTopUppolicyNo()));
		//		map.put("topupInsured", premiaPullService.getInsuredByPolicyAndInsuredNameForDefault(preauthdto.getNewIntimationDTO().getInsuredPatient().getTopUppolicyNo(), preauthdto.getNewIntimationDTO().getInsuredPatient().getTopUpInsuredId()));

		List<PreauthDTO> map = dbCalculationService.getTopUpPolicyDetailsForRiskName(preauthdto.getPolicyDto().getPolicyNumber(),
				0L, preauthdto.getNewIntimationDTO().getInsuredPatient().getKey(),preauthdto.getIntimationKey());

		view.generateTopUpLayout(map);

	}


	public void enhnTopUpPolicyIntimation(@Observes @CDIEvent(ENHN_TOPUP_POLICY_INTIMATION_EVENT) final ParameterDTO parameters) {

		PreauthDTO preauthdto = (PreauthDTO) parameters.getPrimaryParameter();
		List<PreauthDTO> topupData = (List<PreauthDTO>) parameters.getSecondaryParameters()[0];
		String riskId = (String)parameters.getSecondaryParameters()[1];
		Policy topUpPolicyNo = policyService.getByPolicyNumber(topupData.get(0).getTopUPPolicyNumber());
		Insured topUpInsured = policyService.getInsuredByPolicyAndInsuredName(topupData.get(0).getTopUPPolicyNumber(),Long.valueOf(riskId));
		String intimationNo = dbCalculationService.getTopupPolicyIntimation(preauthdto.getNewIntimationDTO().getPolicy().getPolicyNumber(), preauthdto.getNewIntimationDTO().getIntimationId(), preauthdto.getNewIntimationDTO().getKey(), 
				topUpPolicyNo.getKey(), topUpInsured.getKey(), Long.valueOf(riskId));

		//Map<String, Object> topupData = (Map<String, Object>) parameters.getSecondaryParameters()[0];
		//Policy topUpPolicy = (Policy)topupData.get("topupPolicy");
		//Insured topUpInsured = (Insured)topupData.get("topupInsured");
		//String intimationNo = dbCalculationService.getTopupPolicyIntimation(preauthdto.getNewIntimationDTO().getPolicy().getPolicyNumber(), preauthdto.getNewIntimationDTO().getIntimationId(), preauthdto.getNewIntimationDTO().getKey(), topUpPolicy.getKey(), topUpInsured.getKey(), topUpInsured.getInsuredId());

		view.generateTopUpIntimationLayout(intimationNo);
	}

	public void previousPreauthApporvedDate(@Observes @CDIEvent(ENHN_APPROVED_DATE) final ParameterDTO parameters) {

		PreauthDTO preauthdto = (PreauthDTO) parameters.getPrimaryParameter();
		Preauth previousPreauthList = preauthService.getPreviousPreauthList(preauthdto.getClaimDTO().getKey());
		if(previousPreauthList != null && previousPreauthList.getModifiedDate() != null){
			preauthdto.getClaimDTO().setPreauthApprovedDate(previousPreauthList.getModifiedDate());
		}
	}

	public void getEnhSugCategoryLayout(

			@Observes @CDIEvent(ENH_SUB_REJECT_CATEG_LAYOUT) final ParameterDTO parameters) {
		Long id = (Long) parameters.getPrimaryParameter();

		BeanItemContainer<SelectValue> rejSubcategContainer = masterService.getRejSubcategContainer(id);

		view.setSubCategContainer(rejSubcategContainer);
	}

	public void getRejSubCategRemarks(
			@Observes @CDIEvent(ENH_GET_REJ_SUBCATEG_REMARKS) final ParameterDTO parameters) {
		Long rejSubCategid = (Long) parameters.getPrimaryParameter();
		String decision = (String) parameters.getSecondaryParameters()[0];

		MasterRemarks remarks = masterService.getRejSubCategRemarks(rejSubCategid);

		view.setRemarks(remarks, decision);
	}

	public void holdSubmitWizard(
			@Observes @CDIEvent(ENHN_HOLD_SUBMITTED_EVENT) final ParameterDTO parameters) throws Exception {
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
		if(!isAlreadyAcquired){
			Boolean isValidUser = true;
			if(preauthDTO.getStatusKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)){
				if(preauthDTO.getFinalTotalApprovedAmount() != null){
					String limitAmountValidation = dbCalculationService.getLimitAmountValidation(preauthDTO.getStrUserName(),preauthDTO.getFinalTotalApprovedAmount().longValue(), SHAConstants.CASHLESS_CHAR);
					if(limitAmountValidation != null && limitAmountValidation.equalsIgnoreCase("N")){
						isValidUser = false;
					}
				}

			}
			if(isValidUser){

				DBCalculationService dbCalService = new DBCalculationService();
				if(preauthDTO.getDbOutArray() != null){

					Map<String, Object> holdWrkFlowMap = (Map<String, Object>) preauthDTO.getDbOutArray();
					holdWrkFlowMap.put(SHAConstants.USER_ID,preauthDTO.getStrUserName());
					holdWrkFlowMap.put(SHAConstants.STAGE_SOURCE, SHAConstants.SOURCE_ENHANCEMENT_PROCESS);
					if(preauthDTO.getStrUserName() != null){
						TmpEmployee employeeByName = preauthService.getEmployeeByName(preauthDTO.getStrUserName());
						if(employeeByName != null){
							holdWrkFlowMap.put(SHAConstants.REFERENCE_USER_ID,employeeByName.getEmpId());
						}
					}
					holdWrkFlowMap.put(SHAConstants.PAYLOAD_PED_REFERRED_BY,preauthDTO.getStrUserName());
					holdWrkFlowMap.put(SHAConstants.PAYLOAD_PED_TYPE,SHAConstants.HOLD_FLAG);
					holdWrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.PREAUTH_HOLD_OUTCOME);
					holdWrkFlowMap.put(SHAConstants.PAYLOAD_PED_REQUESTOR_ROLE,preauthDTO.getPreauthMedicalDecisionDetails().getHoldRemarks());
					Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(holdWrkFlowMap);


					//dbCalService.initiateTaskProcedure(objArrayForSubmit);
					dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
				}
				Preauth preauth = preauthService.getPreauthById(preauthDTO.getKey());
				if (preauthDTO.getIsPreauthAutoAllocationQ() != null && preauthDTO.getIsPreauthAutoAllocationQ()) {
					preauthService.updateStageInformation(preauth,preauthDTO);
				}

				if(preauthDTO.getDbOutArray() != null){						
					Long wkFlowKey = (Long)wrkFlowMap.get(SHAConstants.WK_KEY);	
					preauthService.updateHoldRemarksForAutoAllocation(wkFlowKey, preauthDTO.getPreauthMedicalDecisionDetails().getHoldRemarks());
				}

				view.buildSuccessLayout();
			}
		}else{
			view.buildFailureLayout(aquiredUser.toString());
		}
	}

	public void setCategoryValue(@Observes @CDIEvent(SET_PRE_AUTH_ENHANCEMENT_CATAGORY_VALUE) final ParameterDTO parameters)
	{
		SelectValue value = (SelectValue) parameters.getPrimaryParameter();
		SelectValue catValue = new SelectValue();

		if (value != null && value.getValue() != null && !value.getValue().isEmpty() && 
				(value.getValue().toUpperCase().contains("FEVER")
						|| value.getValue().toUpperCase().contains("PYREXIA")
						|| value.getValue().toUpperCase().contains("MALARIA") 
						|| value.getValue().toUpperCase().contains("DENGUE") 
						|| value.getValue().toUpperCase().contains("TYPHOID") 
						|| value.getValue().toUpperCase().contains("LEPTOSPIROSIS")
						|| value.getValue().toUpperCase().contains("CHICKUNGUNYA")
						|| value.getValue().toUpperCase().contains("SWINE"))) {
			catValue.setValue("FEVER");
		}
		else {
			catValue.setValue("OTHERS");
		}

		view.addCategoryValues(catValue);
	}

	public void setUpSubCategoryValues(
			@Observes @CDIEvent(PREAUTH_ENHANCEMENT_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}

	public void setUpsourceValues(
			@Observes @CDIEvent(PREAUTH_ENHANCEMENT_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}
	
	public void setNegotiatedSavedAmount(
			@Observes @CDIEvent(PREAUTH_ENHANCEMENT_LOAD_NEGOTIATED_SAVED_AMOUNT) final ParameterDTO parameters) {
		view.setnegotiatedSavedAmount();
	}
	
	public void setAppAmountBSIAlert(@Observes @CDIEvent(PREAUTH_ENHANCEMENT_APPROVE_BSI_ALERT) final ParameterDTO parameters) {
		view.setEnhnAppAmountBSIAlert();
	}
	
	public void generateFieldsBasedOnImplantApplicable(@Observes @CDIEvent(PREAUTH_ENHANCEMENT_IMPLANT_APPLICABLE_CHANGED) final ParameterDTO parameters)
	{
		Boolean isCked = (Boolean) parameters.getPrimaryParameter();
		view.generateFieldsBasedOnImplantApplicable(isCked);
	}

	public void processICACPreAuthEnchSumbit(@Observes @CDIEvent(PREAUTH_ENHANCEMENT_ICAC_SUBMIT_EVENT) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();

		try {
			if(preauthDTO != null){
				preauthService.submitICACProcess(preauthDTO);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	
	public void generateFieldsBasedOnSubCatTWO(@Observes @CDIEvent(PREAUTH_ENHANCEMENT_PCC_SUB_CAT_TWO_GENERATE) final ParameterDTO parameters)
	{
		Long subcatKey = (Long) parameters.getPrimaryParameter();
		view.generateFieldsBasedOnSubCatTWO(preauthService.getPCCSubCategorystwo(subcatKey));
	}
	
	public void addpccSubCategory(@Observes @CDIEvent(PREAUTH_ENHANCEMENT_PCC_SUB_CAT) final ParameterDTO parameters)
	{
		Long subcatKey = (Long) parameters.getPrimaryParameter();
		view.addpccSubCategory(preauthService.getPCCSubCategorys(subcatKey));
	}
}
