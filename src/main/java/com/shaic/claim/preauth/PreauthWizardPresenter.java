package com.shaic.claim.preauth;

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
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.fvrdetails.view.ViewFVRService;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.wizard.dto.FvrGradingDetailsDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.UpdateOtherClaimDetailDTO;
import com.shaic.claim.premedical.dto.OtherBenefitsTableDto;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.NewFVRGradingDTO;
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
import com.shaic.domain.InsuredService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PEDValidationService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.RawInvsDetails;
import com.shaic.domain.RawInvsHeaderDetails;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.State;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.ViewTmpIntimation;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.HospitalPackage;
import com.shaic.domain.preauth.MasterRemarks;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.ProcedureMaster;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.domain.service.PreMedicalService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.starfax.simulation.PremiaPullService;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(PreauthWizard.class)
public class PreauthWizardPresenter  extends AbstractMVPPresenter<PreauthWizard>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5008035088244826928L;

	private final Logger log = LoggerFactory.getLogger(PreauthWizardPresenter.class);

	@EJB
	private MasterService masterService;

	@EJB
	private FieldVisitRequestService fvrService;

	@EJB
	private PreauthService preauthService;

	@EJB
	private PreMedicalService preMedicalService;

	@EJB
	private ClaimService claimService;

	@EJB
	private PolicyService policyService;

	@EJB
	private InsuredService insuredService;

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

	@EJB
	private IntimationService intimationService;

	@EJB
	private PremiaPullService premiaPullService;


	public static final String PREAUTH_STEP_CHANGE_EVENT = "preauth_step_change_event";
	public static final String PREAUTH_SAVE_EVENT = "preauth_save_event";
	public static final String PREAUTH_SUBMITTED_EVENT = "preatuh_submit_event";
	public static final String SETUP_REFERENCE_DATA = "preauth_set_edit_data";
	public static final String PREAUTH_TREATMENT_TYPE_CHANGED = "preauth_treatment_type_changed";
	public static final String PREAUTH_PATIENT_STATUS_CHANGED = "preauth_patient_status_changed";
	public static final String PREAUTH_RELAPSE_OF_ILLNESS_CHANGED = "preauth_relapse_of_illness_changed";

	public static final String PREAUTH_QUERY_EVENT ="preauth_query_click_event";
	public static final String PREAUTH_SUGGEST_REJECTION_EVENT ="preauth_rejection_click_event";
	public static final String PREAUTH_SEND_FOR_PROCESSING_EVENT ="preauth_send_for_processing_click_event";

	public static final String PREAUTH_REFERCOORDINATOR_EVENT = "preauth_refer_coordinator_event";

	public static final String PREAUTH_PREVIOUS_CLAIM_DETAILS = "preauth_previous_claim_details";

	public static final String MASTER_SEARCH_STATE = "preauth_master_search_stae";

	public static final String CHECK_CRITICAL_ILLNESS = "preauth_check_critical_illness";

	public static final String GET_ICD_BLOCK = "preauth_get_icd_block";

	public static final String GET_ICD_CODE = "preauth_get_icd_code";

	public static final String  GET_PREAUTH_PROCEDURE_VALUES = "get_preauth_procedure_values";

	public static final String PREAUTH_QUERY_BUTTON_EVENT = "preauth_query_button_event";

	public static final String PREAUTH_REJECTION_EVENT = "preauth_rejection_event";

	public static final String PREAUTH_APPROVE_EVENT = "preauth_approve_event";

	public static final String PREAUTH_DENIAL_EVENT = "preauth_denial_event";

	public static final String PREAUTH_NOT_REQUIRED_EVENT = "Cashless Not Required Btn Click event";

	public static final String PREAUTH_ESCALATE_EVENT = "preauth_escalate_event";

	public static final String FIELD_VISIT_RADIO_CHANGED = "preauth_field_visit_radio_changed";

	public static final String SPECIALIST_OPINION_RADIO_CHANGED = "preauth_specialist_radio_changed";

	public static final String PREAUTH_SENT_TO_CPU_SELECTED = "preauth_sent_to_cpu_selected";

	public static final String VIEW_CLAIMED_AMOUNT_DETAILS = "view_claimed_amount_details";

	public static final String VIEW_BALANCE_SUM_INSURED_DETAILS = "view_Balance_Sum_Insured_details";

	public static final String SUBLIMIT_CHANGED_BY_SECTION = "Set sublimit values by selecting section for preauth";

	public static final String NO_OF_DAYS_RULE = "no_of_days_rule";

	public static final String GET_EXCLUSION_DETAILS = "preauth_get_exclusion_details";

	public static final String PREAUTH_GET_PACKAGE_RATE = "preauth_get_package_rate";

	public static final String SUM_INSURED_CALCULATION = "calculate_sum_insured_values_from_DB";

	public static final String CHECK_INVESTIGATION_INITIATED = "rule_for_investigation_initiated";

	public static final String BALANCE_SUM_INSURED = "preauth_balance_sum_insured";

	public static final String SET_DB_DETAILS_TO_REFERENCE_DATA = "preauth_set_claim_and_sublimit_db_details";

	public static final String GET_DIAGNOSIS_NAME = "preauth_get_diagnosis_name";

	public static final String PREAUTH_COORDINATOR_REQUEST_EVENT = "preauth_coordinator_request";

	public static final String GET_HOSPITALIZATION_DETAILS = "preauth_get_hospitalization_details";

	public static final String VALIDATE_PREAUTH_USER_RRC_REQUEST = "preauth_user_rrc_request";

	public static final String PREAUTH_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "preauth_load_rrc_request_drop_down_values";

	public static final String PREAUTH_SAVE_RRC_REQUEST_VALUES = "preauth_save_rrc_request_values";

	public static final String PREAUTH_HOSPITALISATION_DUE_TO = "pre_auth_hospitalization_due_to";

	public static final String PREAUTH_OTHER_BENEFITS = "pre_auth_other_benefits";

	public static final String PRE_AUTH_REPORTED_TO_POLICE = "pre_auth_reported_to_police";

	public static final String RECHARGE_SI_FOR_PRODUCT = "recharging SI for product in Preauth";

	public static final String GET_SEC_COVER = "pre_auth_get_sec_cover";

	public static final String GET_SUB_COVER = "pre_auth_get_sub_cover";

	public static final String PREAUTH_GENERATE_REMARKS = "generate Remarks";

	public static final String PREAUTH_GET_REJ_SUBCATEG_REMARKS = "Preauth get Reject Subcategory Remarks";

	public static final String PREAUTH_SUB_REJECT_CATEG_LAYOUT = "Generate Sub Category Layout For Preauth";

	public static final String PREAUTH_PREVIOUS_CLAIM_FOR_POLICY = "preauth_previous_claim_policy";

	protected static final String REFER_VB_64_COMPLAINCE = "refer 64 vb complaince preauth";

	public static final String UPDATE_PREVIOUS_CLAIM_DETAILS = "update_previous_claim_details for preauth";

	public static final String REFERENCE_DATA_CLEAR = "reference data clear preauth";

	public static final String CHECK_PAN_CARD_DETAILS = "check pan card details";
	public static final String PREAUTH_REFER_CPU_USER = "preauth_refer_cpu_user";

	public static final String SHOW_PTCA_CABG = "show_ptca_cabg_preauth";

	public static final String VIEW_PREAUTH_OTHER_BENEFITS = "view_preauth_other_benefits";

	public static final String RTA_RECHARGE_SI_FOR_PRODUCT = "recharging RTA SI for product in Process claim preauth";

	public static final String GET_ASSISTED_REPRODUCTION_TREATMENT_COVER_KEY_PP = "GET_ASSISTED_REPRODUCTION_TREATMENT_COVER_KEY_PP";

	public static final String PROCESS_PREAUTH_LUMEN_REQUEST = "process_preauth_lumen_request";

	public static final String CHECK_FVR_NOT_REQ_REMARKS_VALIDATION = "FVR Not Req. Remarks Validation";

	public static final String PREAUTH_FIELD_VISIT_GRADING = "preauth_field_visit_grading";

	public static final String PREAUTH_HOLD_EVENT = "preauth_hold_functionality";

	public static final String INVS_REVIEW_REMARKS_LIST = "Preauth_Invs_Review_Remarks_List";
	public static final String SAVE_AUTO_ALLOCATION_CANCEL_REMARKS = "save_preauth_auto_allocation_cancel_remarks";

	protected Map<String, Object> referenceData = new HashMap<String, Object>();

	public static final String PREAUTH_LOAD_RAW_DROP_DOWN_VALUES = "preauth_load_raw_drop_down_values";

	public static final String PREAUTH_SUBMIT_DECISION_MISMATCH_ALERT = "preaut submit decision mismatch alert";

	public static final String SAVE_STP_REMARKS = "save_stp_remarks";

	public static final String PREAUTH_HOSP_SCORING_EVENT = "preauth_hosp_scoring_event";

	public static final String GET_PREAUTH_QRY_REMARKS = "Get Preauth Query Remarks";

	public static final String PREAUTH_TOPUP_POLICY_EVENT = "preauth_topup_policy_event";

	public static final String PREAUTH_TOPUP_POLICY_INTIMATION_EVENT = "preauth_topup_policy_intimation_event";

	public static final String PREAUTH_HOLD_SUBMIT = "preaut_hold_submit";

	public static final String SET_PRE_AUTH_CATAGORY_VALUE = "SET_PRE_AUTH_CATAGORY_VALUE";
	
	public static final String PREAUTH_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "preauth_load_rrc_request_sub_category_values";

	public static final String PREAUTH_LOAD_RRC_REQUEST_SOURCE_VALUES = "preauth_load_rrc_request_source_values";
	
	public static final String PREAUTH_LOAD_NEGOTIATED_SAVED_AMOUNT = "preauth_load_negotiated_saved_amount";
	
	public static final String PREAUTH_APPROVE_BSI_ALERT = "preauth_approve_bsi_alert";
	
	public static final String PREAUTH_DYNAMICFRMLAYOUT_REMOVE_COMP = "preauth_dynamicfrmlayout_remove_comp";
	
	public static final String PREAUTH_IMPLANT_APPLICABLE_CHANGED = "preauth_implant_applicable_changed";
	
	public static final String PREAUTH_ICAC_SUBMIT_EVENT = "preauth_icac_sumbit_event";

	public static final String PREAUTH_PCC_SUB_CAT_TWO_GENERATE = "preauth_pcc_sub_cat_two_generate";
	
	public static final String PREAUTH_PCC_SUB_CAT = "preauth_pcc_sub_cat";


	public void fvrRadioOptChanged(@Observes @CDIEvent(CHECK_FVR_NOT_REQ_REMARKS_VALIDATION) final ParameterDTO parameters) {

		PreauthDTO preauthdto = (PreauthDTO) parameters.getPrimaryParameter();
		boolean fvrpending = preauthService.getFVRStatusIdByClaimKey(preauthdto.getClaimDTO().getKey());
		preauthdto.getPreauthMedicalDecisionDetails().setIsFvrIntiatedMA(fvrpending);
	}

	public void getPreauthQueryRemarks(@Observes @CDIEvent(GET_PREAUTH_QRY_REMARKS) final ParameterDTO parameters) {

		Long qryTypeKey = (Long) parameters.getPrimaryParameter();
		Map<Long, Long> queryMap = ReferenceTable.getPreauthQueryTypeMap();
		Long preauthQryRemarksKey = queryMap.get(qryTypeKey);

		if(preauthQryRemarksKey != null) {

			MasterRemarks remarks = masterService.getQueryRemarks(preauthQryRemarksKey);

			view.setPreauthQryRemarks(remarks.getRemarks());
		}


	}

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
			@Observes @CDIEvent(PREAUTH_SUBMITTED_EVENT) final ParameterDTO parameters) throws Exception {
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		/*try
		{*/
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

				Boolean isValidUser = true;
				/*	if(preauthDTO.getStatusKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS)){
						if(preauthDTO.getPreauthMedicalDecisionDetails() != null && preauthDTO.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null){
							String limitAmountValidation = dbCalculationService.getLimitAmountValidation(preauthDTO.getStrUserName(),preauthDTO.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().longValue(), SHAConstants.CASHLESS_CHAR);
							if(limitAmountValidation != null && limitAmountValidation.equalsIgnoreCase("N")){
								isValidUser = false;
							}
						}

					}*/
				if(isValidUser){

					/*if(ReferenceTable.getFHORevisedKeys().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					if(preauthDTO.getPreauthDataExtractionDetails().getTotalOtherBenefitsApprovedAmt() != null && preauthDTO.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null){
						Double totalOtherBenefitsApprovedAmt = preauthDTO.getPreauthDataExtractionDetails().getTotalOtherBenefitsApprovedAmt();
						Double initialTotalApprovedAmt = preauthDTO.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt();
						initialTotalApprovedAmt += totalOtherBenefitsApprovedAmt;
						preauthDTO.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(initialTotalApprovedAmt);
					}
				}*/
					Preauth preauth = null;

					preauth = preauthService.submitPreAuth(preauthDTO, false);	
					preauthService.updateInvsReviewDetails(preauthDTO.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList());	

					/*if(null != preauthDTO.getPreauthHoldStatusKey() && preauthDTO.getPreauthHoldStatusKey().equals(ReferenceTable.PREAUTH_HOLD_STATUS_KEY)){
					if(preauthDTO.getDbOutArray() != null){						
						Long wkFlowKey = (Long)wrkFlowMap.get(SHAConstants.WK_KEY);	
						preauthService.updateHoldRemarksForAutoAllocation(wkFlowKey, preauthDTO.getPreauthMedicalDecisionDetails().getHoldRemarks());
					}
				}*/
					RawInvsHeaderDetails headerObj =  claimService.getRawHeaderByIntimation(preauthDTO.getNewIntimationDTO().getIntimationId());
					if(null != headerObj){
						List<RawInvsDetails> existingList = claimService.getRawDetailsByRawInvKey(headerObj.getKey());
						for (RawInvsDetails rawInvsDetails : existingList) {
							preauthService.updateEsclateIfCompleted(rawInvsDetails);
						}
					}

					if(ReferenceTable.getFHORevisedKeys().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){

						policyService.updatePolicyBonusDetails(preauthDTO.getNewIntimationDTO().getPolicy().getPolicyNumber());

						preauthDTO.setStatusKey(preauth.getStatus().getKey());
						preauthDTO.setStageKey(preauth.getStage().getKey());
						preauthDTO.setKey(preauth.getKey());
						preMedicalService.saveBenefitAmountDetails(preauthDTO);
					}

					if(preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS))) {
						dbCalculationService.invokeAccumulatorProcedure(preauth.getKey());
						Hospitals hospitalByKey = hospitalService.getHospitalByKey(preauth.getClaim().getIntimation().getHospital());
						//			preauthService.callBPMRemainderTask(preauthDTO,preauth, preauthDTO.getStrUserName(), preauthDTO.getStrPassword());
						Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(preauth.getClaim(), hospitalByKey);

						Object[] inputArray = (Object[])arrayListForDBCall[0];

						inputArray[SHAConstants.INDEX_REMINDER_CATEGORY] = SHAConstants.PREAUTH_BILLS_NOT_RECEIVED;
						inputArray[SHAConstants.INDEX_CASHLESS_KEY] = preauth.getKey();
						inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.OUTCOME_PREAUTH_INITIATE_REMINDER_PROCESS;

						dbCalculationService.stopReminderProcessProcedure(preauth.getClaim().getIntimation().getIntimationId(),SHAConstants.OTHERS);

						preauthService.callReminderTaskForDB(inputArray);
					}
					//Policy policyByKey = policyService.getPolicyByKey(preauth.getPolicy().getKey());
					Policy policy = preauthDTO.getNewIntimationDTO().getPolicy();
					if(policy!=null && policy.getProposedPanNumber()==null && preauth.getTotalApprovalAmount() != null && preauth.getTotalApprovalAmount()>=SHAConstants.ONE_LAKH){

						if(preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS))) {
							//dbCalculationService.invokeAccumulatorProcedure(preauth.getKey());
							Hospitals hospitalByKey = hospitalService.getHospitalByKey(preauth.getClaim().getIntimation().getHospital());
							//			preauthService.callBPMRemainderTask(preauthDTO,preauth, preauthDTO.getStrUserName(), preauthDTO.getStrPassword());
							Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(preauth.getClaim(), hospitalByKey);

							Object[] inputArray = (Object[])arrayListForDBCall[0];

							inputArray[SHAConstants.INDEX_REMINDER_CATEGORY] = SHAConstants.PAN_CARD;
							inputArray[SHAConstants.INDEX_CASHLESS_KEY] = preauth.getKey();
							inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.OUTCOME_PREAUTH_INITIATE_REMINDER_PROCESS;

							//dbCalculationService.stopReminderProcessProcedure(preauth.getClaim().getIntimation().getIntimationId(),SHAConstants.OTHERS);

							preauthService.callReminderTaskForDB(inputArray);
						}
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
							if(preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_DOWNSIZE_STATUS) ||
									preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS) 
									|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS) 
									|| preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_REFER_TO_COORDINATOR_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS)) {
								Claim claimByKey = preauthService.searchByClaimKey(preauth.getClaim().getKey());
								Hospitals hospitalObject = preauthService.getHospitalObject(claimByKey.getIntimation().getHospital());
								String provisioningamt = String.valueOf(preauth.getTotalApprovalAmount() != null ? preauth.getTotalApprovalAmount().longValue() : 0d);
								if(preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS) ||
										preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)
										|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS) 
										|| preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS)) {
									provisioningamt = claimByKey.getCurrentProvisionAmount() != null ? String.valueOf(claimByKey.getCurrentProvisionAmount().longValue()) : (claimByKey.getProvisionAmount() != null ? String.valueOf(claimByKey.getProvisionAmount().longValue()) : "0");
								}
								String provisionAmtInput = SHAUtils.getProvisionAmtInput(preauth.getClaim(), hospitalObject.getName(), provisioningamt);
								PremiaService.getInstance().updateProvisionAmount(provisionAmtInput);
							}
						}

						/*} catch (Exception e) {

					throw e;

				}*/


					} catch (Exception e) {

						e.printStackTrace();

					}

					//		preauthService.setBPMOutcome(preauthDTO, preauth, false);
					//preauthService.setDBOutcome(preauthDTO, preauth, false);



					view.buildSuccessLayout();
				}else{
					//view.validationForLimitAmt("");
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

			BeanItemContainer<SelectValue> sectionList = masterService
					.getSectionList(bean.getNewIntimationDTO().getPolicy().getProduct().getKey(),bean.getNewIntimationDTO().getPolicy());

			if(ReferenceTable.getSuperSurplusKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
					&& ((bean.getNewIntimationDTO().getPolicy().getPolicyPlan() == null) || (bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")))){
				List<SelectValue> itemIds = sectionList.getItemIds();
				for (SelectValue selectValue : itemIds) {
					if(selectValue.getCommonValue().equals(ReferenceTable.MATERNITY_COVER_CODE)){
						itemIds.remove(selectValue);
						break;
					}
				}
				referenceData.put("sectionDetails", sectionList);
			}else{
				referenceData.put("sectionDetails", sectionList);
			}


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

		Date startDate = new Date();
		log.info("^^^^^^^^^^^^^^^^^^^^^^^Before Diagnosis master call ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^" +startDate);

		referenceData.put("diagnosisName", masterService.getDiagnosisList());

		log.info("^^^^^^^^^^^^^^^^^^^^^^ After Diagnosis master call  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^------> "+ SHAUtils.getDurationFromTwoDate(startDate, new Date()));

		referenceData.put("sumInsured", masterService.getSelectValueContainer(ReferenceTable.SUM_INSURED));
		referenceData.put("commonValues", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("coordinatorTypeRequest", masterService.getSelectValueContainer(ReferenceTable.COORDINATOR_REQUEST_TYPE));
		referenceData.put("patientStatus", masterService.getSelectValueContainer(ReferenceTable.PATIENT_STATUS));
		referenceData.put("icdChapter", masterService.getSelectValuesForICDChapter());
		//		referenceData.put("icdBlock", masterService.getSelectValuesForICDBlock());
		//		referenceData.put("icdCode", masterService.getSelectValuesForICDCode());
		referenceData.put("illness", masterService.getSelectValueContainer(ReferenceTable.ILLNESS));

		Date startDate1 = new Date();
		log.info("^^^^^^^^^^^^^^^^^^^^^^^Before Procedure master call ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^" +startDate1);

		referenceData.put("procedureName", preauthService.getProcedureListNames());

		log.info("^^^^^^^^^^^^^^^^^^^^^^ After Procedure master call  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^------> "+ SHAUtils.getDurationFromTwoDate(startDate1, new Date()));

		referenceData.put("procedureCode", preauthService.getProcedureListNames());
		referenceData.put("medicalSpeciality", preauthService.getSpecialityType("M"));
		referenceData.put("surgicalSpeciality", preauthService.getSpecialityType("S"));	
		referenceData.put("nextLOVSpeciality", masterService.getNextLOVSpecialityType());		
		referenceData.put("sublimitApplicable", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("considerForPayment", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("considerForDayCare", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("commonValues", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("pedExclusionImpactOnDiagnosis",masterService.getSelectValueContainer(ReferenceTable.IMPACT_ON_DIAGNOSI));

		//CR R20181300
		referenceData.put("pedImpactOnDiagnosis", masterService.getSelectValueContainer(ReferenceTable.PED_IMPACT_ON_DIAGNOSI));
		referenceData.put("reasonForNotPaying", masterService.getSelectValueContainer(ReferenceTable.PED_NON_PAYABLE_REASON)); //EXCLUSION_DETAILS
		//CR R20181300

		referenceData.put("exclusionDetails",masterService.getSelectValueContainer(ReferenceTable.EXCLUSION_DETAILS));
		referenceData.put("procedureStatus",masterService.getSelectValueContainer(ReferenceTable.PROCEDURE_STATUS));
		//		referenceData.put("pedList", masterService.getInsuredPedDetails());
		referenceData.put("fvrNotRequiredRemarks", masterService.getSelectValueContainer(ReferenceTable.FVR_NOT_REQUIRED_REMARKS));
		referenceData.put("rejectionCategory", masterService.getRevisedRejectionCategoryByValue(productKey));  //masterService.getRejectionCategoryByValue();
		referenceData.put("sendForProcessingCategory", masterService.getRevisedRejectionCategoryByValue(productKey));  // masterService.getRejectionCategoryByValue();
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


		// Preauth Only..
		referenceData.put("investigatorName", masterService.getInvestigation());

		referenceData.put("hospitalisationDueTo", masterService.getSelectValueContainer(ReferenceTable.HOSPITALIZATION_DUE_TO));
		referenceData.put("causeOfInjury", masterService.getSelectValueContainer(ReferenceTable.CAUSE_OF_INJURY));
		referenceData.put("section", masterService
				.getSelectValueContainer(ReferenceTable.SECTION));
		referenceData.put("typeOfDelivery", masterService
				.getSelectValueContainer(ReferenceTable.DELIVERY_TYPE));
		referenceData.put("stent", masterService.getSelectValueContainer(ReferenceTable.STENT));

		referenceData.put("natureOfLoss", masterService
				.getNatureCauseLossSelectValueContainer(SHAConstants.HEALTH_LOB, ReferenceTable.NATURE_OF_LOSS));
		referenceData.put("causeOfLoss", masterService
				.getNatureCauseLossSelectValueContainer(SHAConstants.PA_LOB, ReferenceTable.CAUSE_OF_LOSS));
		referenceData.put("catastrophicLoss", masterService.getCatastrophicLossList());
		referenceData.put("preAuthTypeValue", masterService.getSelectValueContainer(ReferenceTable.PRE_AUTH_TYPE));
		

		referenceData.put("behaviourHosCombValue", masterService.getBehvrHospSelectValueContainer(ReferenceTable.BEHAVIOUR_OF_HOSP));
		
	/*	if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)){
			if(bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM)){
				referenceData.put("rejectionCategory", masterService.getRevisedRejectionCategoryByValueForCoron(productKey ,SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM)); 
			}else if(bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_INDEMNITY)){
				referenceData.put("rejectionCategory", masterService.getRevisedRejectionCategoryByValueForCoron(productKey,SHAConstants.POLICY_COVID_GRP_PLAN_INDEMNITY));
			}
		}*/
	}

	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_PREAUTH_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		RRCDTO rrcDTO =null;
		if(preauthDTO.getRrcDTO() !=null){
			rrcDTO = preauthDTO.getRrcDTO();
		}else{
			rrcDTO = new RRCDTO();
		}
		reimbursementService.setRequestStageIdForRRC(rrcDTO,SHAConstants.PROCESS_PREAUTH);
		preauthDTO.setRrcDTO(rrcDTO);
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(
				preauthDTO.getNewIntimationDTO().getInsuredPatient()
				.getInsuredId().toString(), preauthDTO.getPolicyDto()
				.getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		if(isValid)
		{
			reimbursementService.loadRRCRequestValues(preauthDTO, insuredSumInsured,SHAConstants.PROCESS_PREAUTH);
		}
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

	public void getProcedureValues(
			@Observes @CDIEvent(GET_PREAUTH_PROCEDURE_VALUES) final ParameterDTO parameters) {
		Long specialistkey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue>  procedures = preauthService.getProcedureforSpeciality(specialistkey);
		view.setProcedureValues(procedures);
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
		//		Long productKey = (Long) parameters.getPrimaryParameter();
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Long productKey = preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey();
		if(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)){
			if(preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM)){
				view.generateRejectionLayout(masterService.getRevisedRejectionCategoryByValueForCoron(productKey ,preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan())); 
			}else if(preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_INDEMNITY)){
				view.generateRejectionLayout(masterService.getRevisedRejectionCategoryByValueForCoron(productKey,preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan())); 
			}
		}else{
			view.generateRejectionLayout(masterService.getRevisedRejectionCategoryByValue(productKey));  
		}
		 //masterService.getRejectionCategoryByValue();
	}

	public void generatePreAuthDenialLayout(@Observes @CDIEvent(PREAUTH_DENIAL_EVENT) final ParameterDTO parameters)
	{
		BeanItemContainer<SelectValue> denialContainer = masterService.getSelectValueContainer(ReferenceTable.DENIAL_REASON);

		Long productKey = (Long) parameters.getPrimaryParameter();

		if(!ReferenceTable.getGMCProductList().containsKey(productKey)){
			denialContainer = masterService.getSortedMasterBsedOnMasterTypeCode(ReferenceTable.DENIAL_REASON_RETAIL);
		}

		view.generateDenialLayout(denialContainer);
	}

	public void generateCashlessNotReqLayout(@Observes @CDIEvent(PREAUTH_NOT_REQUIRED_EVENT) final ParameterDTO parameters)
	{
		//		BeanItemContainer<SelectValue> denialContainer = masterService.getSelectValueContainer(ReferenceTable.DENIAL_REASON);

		//		Long productKey = (Long) parameters.getPrimaryParameter();
		//		
		//		if(!ReferenceTable.getGMCProductList().containsKey(productKey)){
		//			denialContainer = masterService.getSortedMasterBsedOnMasterTypeCode(ReferenceTable.DENIAL_REASON_RETAIL);
		//		}

		view.generateCashlessNotReqLayout();
	}

	public void generatePreAuthEscalateLayout(@Observes @CDIEvent(PREAUTH_ESCALATE_EVENT) final ParameterDTO parameters)
	{
		view.generateEscalateLayout(masterService.getSelectValueContainer(ReferenceTable.ESCALATE_TO));
	}

	public void generateFieldsBasedOnFieldVisit(@Observes @CDIEvent(FIELD_VISIT_RADIO_CHANGED) final ParameterDTO parameters)
	{
		//		Boolean fvrRadioOption = (Boolean) parameters.getPrimaryParameter();
		Boolean isChecked = (Boolean) parameters.getPrimaryParameter();		
		PreauthDTO preauthdto = (PreauthDTO)parameters.getSecondaryParameters()[0];

		boolean fvrpending = preauthService.getFVRStatusIdByClaimKey(preauthdto.getClaimDTO().getKey());
		preauthdto.getPreauthMedicalDecisionDetails().setIsFvrIntiatedMA(fvrpending);

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
				view.genertateFieldsBasedOnFieldVisit(isChecked, masterService.getSelectValueContainer(ReferenceTable.ALLOCATION_TO),/*masterService.getSelectValueContainer(ReferenceTable.ASSIGN_TO)*/null
						,masterService.getSelectValueContainer(ReferenceTable.FVR_PRIORITY),fvrDTOList);
			}			
		}				
	}

	public void generateFieldsBasedOnSpecialist(@Observes @CDIEvent(SPECIALIST_OPINION_RADIO_CHANGED) final ParameterDTO parameters)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("specialistConsulted", masterService.getSelectValueContainerForSpecialist());
		map.put("specialistType", masterService.getSelectValueContainer(ReferenceTable.SPECIALIST_TYPE));
		Boolean isChecked = (Boolean) parameters.getPrimaryParameter();
		view.genertateFieldsBasedOnSpecialistChecked(isChecked, map);
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
				|| (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()) || 
						SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
					&& preauthDTO.getNewIntimationDTO().getPolicy().getProductType().getKey().equals(ReferenceTable.INDIVIDUAL_POLICY))) {
			policyPlan = preauthDTO.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() != null ? preauthDTO.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() : "0";
		}

		if(preauthDTO.getPreauthDataExtractionDetails().getSection() != null){

			referenceData.put("sublimitDBDetails", dbCalculationService
					.getClaimedAmountDetailsForSection(policyDTO.getProduct().getKey(),
							sumInsured, 0l, insuredAge,preauthDTO.getPreauthDataExtractionDetails().getSection().getId(),
							preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0", (preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : preauthDTO.getClaimDTO().getClaimSubCoverCode()),preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId()));

		}else{
			referenceData.put("sublimitDBDetails", dbCalculationService
					.getClaimedAmountDetailsForSection(policyDTO.getProduct().getKey(),
							sumInsured, 0l, insuredAge,0l,policyPlan, (preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : preauthDTO.getClaimDTO().getClaimSubCoverCode()),preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
		}

		Map<Integer, Object> hospitalizationDetails = new HashMap<Integer, Object>();

		if(preauthDTO.getPreauthDataExtractionDetails().getSection() != null){
			if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				hospitalizationDetails = dbCalculationService.getHospitalizationDetailsForGMC(policyDTO.getProduct().getKey(),
						sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),preauthDTO.getPreauthDataExtractionDetails().getSection().getId(),"A");
			}else{
				hospitalizationDetails = dbCalculationService.getHospitalizationDetails(policyDTO.getProduct().getKey(),
						sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),preauthDTO.getPreauthDataExtractionDetails().getSection().getId(),"A");
			}

		}else{
			if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				hospitalizationDetails = dbCalculationService.getHospitalizationDetailsForGMC(policyDTO.getProduct().getKey(),
						sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),0l,"0");
			}else{
				hospitalizationDetails = dbCalculationService.getHospitalizationDetails(policyDTO.getProduct().getKey(),
						sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),0l,policyPlan);
			}


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
		Double balanceSI=0d;
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

	public void getRemarks(
			@Observes @CDIEvent(PREAUTH_GENERATE_REMARKS) final ParameterDTO parameters) {
		Long id = (Long) parameters.getPrimaryParameter();
		Boolean isReject = (Boolean) parameters.getSecondaryParameters()[0];

		MasterRemarks remarks = masterService.getRemarks(id);
		view.setRemarks(remarks,isReject);
	}

	public void getRejSubCategRemarks(
			@Observes @CDIEvent(PREAUTH_GET_REJ_SUBCATEG_REMARKS) final ParameterDTO parameters) {
		Long rejSubCategid = (Long) parameters.getPrimaryParameter();
		Boolean isReject = true;

		MasterRemarks remarks = masterService.getRejSubCategRemarks(rejSubCategid);
		view.setRemarks(remarks,isReject);
	}

	public void showUpdateClaimDetailTable(
			@Observes @CDIEvent(UPDATE_PREVIOUS_CLAIM_DETAILS) final ParameterDTO parameters) {

		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		List<UpdateOtherClaimDetailDTO> updateOtherClaimDetails = new ArrayList<UpdateOtherClaimDetailDTO>();

		if(preauthDTO.getUpdateOtherClaimDetailDTO().isEmpty()){
			//						updateOtherClaimDetails = preauthService.getUpdateOtherClaimDetails(preauthDTO.getNewIntimationDTO().getPolicy().getKey());
			updateOtherClaimDetails = preauthService.getUpdateOtherClaimDetailsDTO(preauthDTO.getKey());

			if(updateOtherClaimDetails != null && updateOtherClaimDetails.isEmpty()){
				updateOtherClaimDetails = preauthService.getUpdateOtherClaimDetails(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),preauthDTO);
			}

		}else{
			updateOtherClaimDetails = preauthDTO.getUpdateOtherClaimDetailDTO();
		}
		view.setUpdateOtherClaimsDetails(updateOtherClaimDetails, referenceData);

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
			Map<String, Double> nonAllopathicAmount = dbCalculationService.getNonAllopathicAmount((Long)values.get(SHAConstants.POLICY_KEY), (Long)values.get(SHAConstants.INSURED_KEY),0l,"0", (Long)values.get(SHAConstants.CLAIM_KEY));
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
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getSecondaryParameters()[0];


		/** The below Code is Changed as per CR- R1169**/
		//Investigation checkInitiateInvestigation = preauthService.checkInitiateInvestigation(claimKey);
		Long claimKey = (Long) parameters.getPrimaryParameter();
		Boolean checkInitiateInvestigation = preauthService.checkInvestigationPending(claimKey);
		view.setInvestigationRule(checkInitiateInvestigation);
		Boolean isValidUser = true;
		if(null != preauthDTO.getStatusKey() && preauthDTO.getStatusKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS)){
			//	if(preauthDTO.getPreauthMedicalDecisionDetails() != null && preauthDTO.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null){

			String amount =preauthDTO.getAmountConsideredForClaimAlert();
			int amountCons = SHAUtils.getIntegerFromStringWithComma(amount);
			if(amount != null && !amount.isEmpty()){
				Long amountConsidered = Long.valueOf(amountCons);

				String limitAmountValidation = dbCalculationService.getLimitAmountValidation(preauthDTO.getStrUserName(),amountConsidered, SHAConstants.CASHLESS_CHAR);
				if(limitAmountValidation != null && limitAmountValidation.equalsIgnoreCase("N")){
					isValidUser = false;
				}
				//}
			}

		}

		if(!isValidUser){
			preauthDTO.setAllowedToNxt(false);
			view.validationForLimitAmt("");
		}else{
			preauthDTO.setAllowedToNxt(true);
		}


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
		Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(intimationDTO.getInsuredPatient().getInsuredId().toString(), intimationDTO.getPolicy().getKey(),intimationDTO.getInsuredPatient().getLopFlag());


		//Double balanceSI = dbCalculationService.getBalanceSI(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(), intimationDTO.getPolicy().getTotalSumInsured());
		//Double balanceSI = dbCalculationService.getBalanceSI(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(), Double.valueOf(insuredSumInsured.toString()));
		Double balanceSI = 0d;
		List<Double> copayValue = new ArrayList<Double>();
		if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			balanceSI = dbCalculationService.getBalanceSIForGMC(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),preauthDTO.getClaimKey());
			copayValue = dbCalculationService.getProductCoPayForGMC(intimationDTO.getPolicy().getKey(),intimationDTO.getInsuredPatient().getKey());
		}else{
			if(ReferenceTable.getFHORevisedKeys().containsKey(intimationDTO.getPolicy().getProduct().getKey())
					|| /*ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())*/
					(preauthDTO.getNewIntimationDTO().getPolicy().getProduct() != null 
					&& (SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode())
							|| SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode())
							|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
					&& ("G").equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getInsuredPatient().getPolicyPlan()))
					|| (preauthDTO.getNewIntimationDTO().getPolicy().getProduct() != null 
							&& (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()) || 
									SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode())))){
				if(preauthDTO.getPreauthDataExtractionDetails().getHospitalisationDueTo() != null && preauthDTO.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId() != null && 
						preauthDTO.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId().equals(ReferenceTable.INJURY_MASTER_ID) && preauthDTO.getPreauthDataExtractionDetails().getCauseOfInjury() != null && ReferenceTable.CAUSE_OF_INJURY_ACCIDENT_KEY.equals(preauthDTO.getPreauthDataExtractionDetails().getCauseOfInjury().getId())){
					balanceSI = dbCalculationService.getRTABalanceSI(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),preauthDTO.getClaimKey(), 0l).get(SHAConstants.TOTAL_BALANCE_SI);
				}
				else{
					balanceSI = dbCalculationService.getBalanceSI(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),preauthDTO.getClaimKey(), insuredSumInsured,intimationDTO.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);	
				}

			}
			else if(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(intimationDTO.getPolicy().getProduct().getKey())){
				String subCover = "";
				if(null != preauthDTO.getPreauthDataExtractionDetails() && null != preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO() && null != preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover()){			
					subCover = preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue();
				}
				balanceSI = dbCalculationService.getBalanceSIForStarCancerGold(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),preauthDTO.getClaimKey(), insuredSumInsured,intimationDTO.getKey(),subCover).get(SHAConstants.TOTAL_BALANCE_SI);	
			}
			else{
				balanceSI = dbCalculationService.getBalanceSI(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),preauthDTO.getClaimKey(), insuredSumInsured,intimationDTO.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
			}
			copayValue = dbCalculationService.getProductCoPay(intimationDTO.getPolicy().getProduct().getKey() ,intimationDTO.getInsuredPatient().getKey(), intimationDTO.getInsuredPatient().getInsuredId(),intimationDTO);




		}



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
		Double sumInsured = dbCalculationService.getInsuredSumInsured(preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), preauthDTO.getPolicyDto().getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		/*if (policyDTO.getInsuredSumInsured() == 0) {
			sumInsured = policyDTO.getTotalSumInsured();
		}*/
		if (sumInsured == 0) {
			sumInsured = policyDTO.getTotalSumInsured();
		}
		//		referenceData.put("policyAgeing", dbCalculationService.getPolicyAgeing(policyDTO.getAdmissionDate(), policyDTO.getPolicyNumber()));
		referenceData.put("policyAgeing",preauthDTO.getNewIntimationDTO().getPolicyYear() != null ? preauthDTO.getNewIntimationDTO().getPolicyYear() : "");

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
						sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),0l,preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan());
			}else{
				hospitalizationDetails = dbCalculationService.getHospitalizationDetails(policyDTO.getProduct().getKey(),
						sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),0l,policyPlan);
			}


		}

		referenceData.put("claimDBDetails", hospitalizationDetails);
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

		//Long i = 0l;
		SelectValue value = null;
		for (String string : coPayPercentage) {
			value = new SelectValue();
			String[] copayWithPercentage = string.split("\\.");
			String copay = copayWithPercentage[0].trim();
			value.setId(Long.valueOf(copay));
			value.setValue(string);
			coPayContainer.addBean(value);
			//i++;
		}

		return coPayContainer;
	}


	public void getHospitalizationDetails(
			@Observes @CDIEvent(GET_HOSPITALIZATION_DETAILS) final ParameterDTO parameters) {
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Hospitals hospitalById = hospitalService.getHospitalById(preauthDTO.getHospitalKey());
		PolicyDto policyDTO = preauthDTO.getPolicyDto();
		//String insuredAge = getInsuredAge(policyDTO.getInsuredDob());
		Double sumInsured = dbCalculationService.getInsuredSumInsured(preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), preauthDTO.getPolicyDto().getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		if (sumInsured == 0) {
			sumInsured = policyDTO.getTotalSumInsured();
		}

		Map<Integer, Object> hospitalizationDetails = new HashMap<Integer, Object>();

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
			policyPlan = preauthDTO.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() != null ? preauthDTO.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() : "";
		}

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
						sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),0l,
						policyPlan);
			}


		}

		referenceData.put("claimDBDetails", hospitalizationDetails);
		view.setHospitalizationDetails(hospitalizationDetails);

	}

	public void generateFieldsBasedOnOtherBenefits(@Observes @CDIEvent(PREAUTH_OTHER_BENEFITS) final ParameterDTO parameters)
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
		view.genertateFieldsBasedOnObterBenefits(beanDto);
	}

	public void generateFieldsBasedOnHospitalisationDueTo(@Observes @CDIEvent(PREAUTH_HOSPITALISATION_DUE_TO) final ParameterDTO parameters)
	{
		//		PreauthDTO beanDto = (PreauthDTO) parameters.getPrimaryParameter();

		view.genertateFieldsBasedOnHospitalisionDueTo();
		//		view.genertateFieldsBasedOnHospitalisionDueTo(selectedValue);
	}

	public void generateFieldsBasedOnReportedToPolice(@Observes @CDIEvent(PRE_AUTH_REPORTED_TO_POLICE) final ParameterDTO parameters)
	{
		Boolean selectedValue = (Boolean) parameters.getPrimaryParameter();
		view.genertateFieldsBasedOnReportedToPolice(selectedValue);
	}

	public void getPreviousClaimForPolicy(
			@Observes @CDIEvent(PREAUTH_PREVIOUS_CLAIM_FOR_POLICY) final ParameterDTO parameters) {
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

		previousclaimsList = getPreviousClaimForPreviousPolicy(byPolicyNumber.getRenewalPolicyNumber(), previousclaimsList);




//			List<PreviousClaimsTableDTO> previousClaimDTOList = SHAUtils
//					.getPreviousClaims(claimsByPolicyNumber,
//							claimByKey.getClaimId(), pedValidationService,
//							masterService);

		List<PreviousClaimsTableDTO> previousClaimDTOList = preauthService
				.getPreviousClaims(previousclaimsList,
						claimId);*/

		DBCalculationService dbCalculationService = new DBCalculationService();
		List<PreviousClaimsTableDTO> previousClaimDTOList = dbCalculationService.getPreviousClaims(preauthDto.getClaimDTO().getKey(), preauthDto.getNewIntimationDTO().getPolicy().getKey(), 
				preauthDto.getNewIntimationDTO().getInsuredPatient().getKey(), SHAConstants.POLICY_WISE_SEARCH_TYPE);

		view.setPreviousClaimDetailsForPolicy(previousClaimDTOList);
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

	public void generateVB64ComplainceLayout(@Observes @CDIEvent(REFER_VB_64_COMPLAINCE) final ParameterDTO parameters)
	{
		view.generate64VBComplainceLayout();
	}

	public void clearReferenceData(@Observes @CDIEvent(REFERENCE_DATA_CLEAR) final ParameterDTO parameters)
	{
		SHAUtils.setClearReferenceData(referenceData);
	}

	public void showPTCACABGEvent(
			@Observes @CDIEvent(SHOW_PTCA_CABG) final ParameterDTO parameters) {
		Boolean checkValue = (Boolean) parameters.getPrimaryParameter();
		view.generatePTCACABGLayout(checkValue);
	}


	public void showBenefits(
			@Observes @CDIEvent(VIEW_PREAUTH_OTHER_BENEFITS) final ParameterDTO parameters) {
		view.generateOtherBenefitsPopup();
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

	public void generateReferCPULayout(@Observes @CDIEvent(PREAUTH_REFER_CPU_USER) final ParameterDTO parameters)
	{
		view.generateCPUUserLayout(masterService.getSelectValueContainer(ReferenceTable.AUTO_PROCESS_SUGGESTION));
	}

	public void getAssistedReprodTreatment(@Observes @CDIEvent(GET_ASSISTED_REPRODUCTION_TREATMENT_COVER_KEY_PP) final ParameterDTO parameters) {
		try{

			PreauthDTO bean = (PreauthDTO) parameters.getPrimaryParameter();
			Long assistedKey = reimbursementService.getAssistedReprodTreatment(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getId());


			view.setAssistedReprodTreatment(assistedKey);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void preauthLumenRequest(@Observes @CDIEvent(PROCESS_PREAUTH_LUMEN_REQUEST) final ParameterDTO parameters){
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
	public void generateGradingBasedOnFieldVisit(@Observes @CDIEvent(PREAUTH_FIELD_VISIT_GRADING) final ParameterDTO parameters)
	{
		PreauthDTO preauthdto = (PreauthDTO) parameters.getPrimaryParameter();
		Preauth preauth = preauthService.getPreauthById(preauthdto.getKey());

		if(!(preauth.getStatus().getKey()
				.equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS) || preauth.getStatus().getKey()
				.equals(ReferenceTable.PREAUTH_REJECT_STATUS)))
		{
			List<FieldVisitRequest> fvrByClaimKey = reimbursementService
					.getFVRByClaimKey(preauth.getClaim().getKey());

			List<FVRGradingMaster> fvrGradingSegB = reimbursementService
					.getFVRGradingBySegment(SHAConstants.FVR_GRADING_SEGMENT_B);

			List<FVRGradingMaster> fvrGradingSegC = reimbursementService
					.getFVRGradingBySegment(SHAConstants.FVR_GRADING_SEGMENT_C);

			List<FvrGradingDetailsDTO> fvrDtoList = new ArrayList<FvrGradingDetailsDTO>();
			//Map<Integer, FieldVisitRequest> valueMap = new HashMap<Integer, FieldVisitRequest>();
			FvrGradingDetailsDTO fvrDto  = null;
			List<NewFVRGradingDTO> FVRTableDTO =  null;
			for (int i = 0; i < fvrByClaimKey.size(); i++) {
				FieldVisitRequest fieldVisitRequest = fvrByClaimKey.get(i);
				if(fieldVisitRequest.getStatus() != null && (fieldVisitRequest.getStatus().getKey().equals(ReferenceTable.FVR_REPLY_RECEIVED) || fieldVisitRequest.getStatus().getKey().equals(ReferenceTable.FVR_GRADING_STATUS) || fieldVisitRequest.getStatus().getKey().equals(ReferenceTable.ASSIGNFVR))){
					fvrDto = new FvrGradingDetailsDTO();
					fvrDto.setKey(fieldVisitRequest.getKey());
					fvrDto.setRepresentativeName(fieldVisitRequest
							.getRepresentativeName());
					fvrDto.setRepresentiveCode(fieldVisitRequest
							.getRepresentativeCode());
					if(fieldVisitRequest.getStatus() != null && (fieldVisitRequest.getStatus().getKey().equals(ReferenceTable.FVR_REPLY_RECEIVED) || fieldVisitRequest.getStatus().getKey().equals(ReferenceTable.FVR_GRADING_STATUS))){
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

						if(fvrGradingDetails != null && !fvrGradingDetails.isEmpty()){
							for (FVRGradingDetail fvrGradingDetail : fvrGradingDetails) {
								if(fvrGradingDetail.getSeqNo() != null){
									if(fvrGradingDetail.getSeqNo().equals(masterFVR.getKey())){
										eachFVRDTO.setKey(fvrGradingDetail.getKey());
										if(fvrGradingDetail.getGrading() != null){
											eachFVRDTO.setStatusFlagSegmentA(fvrGradingDetail.getGrading());
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

						if(fvrGradingDetails != null && !fvrGradingDetails.isEmpty()){
							for (FVRGradingDetail fvrGradingDetail : fvrGradingDetails) {
								if(fvrGradingDetail.getSeqNo() != null){
									if(fvrGradingDetail.getSeqNo().equals(masterFVR.getKey())){
										eachFVRDTO.setKey(fvrGradingDetail.getKey());
										if(fvrGradingDetail.getGrading() != null){
											eachFVRDTO.setStatusFlag(fvrGradingDetail.getGrading());
											isSegmentCEdit = false;
											isSegmentABEdit = true;
										}
										break;
									}}
							}
						}
						FVRTableDTO.add(eachFVRDTO);
					}

					if(fieldVisitRequest.getStatus().getKey().equals(ReferenceTable.FVR_REPLY_RECEIVED)){

						for (FVRGradingMaster masterFVR : fvrGradingSegC) {
							eachFVRDTO = new NewFVRGradingDTO();
							eachFVRDTO.setFvrSeqNo(masterFVR.getKey());
							if(masterFVR.getKey().intValue() == 22){
								eachFVRDTO.setIsFVRReceived(Boolean.TRUE);
								//eachFVRDTO.setStatusFlagSegmentC(SHAConstants.YES_FLAG);
							}
							eachFVRDTO.setCategory(masterFVR.getGradingType());
							eachFVRDTO.setSegment(SHAConstants.FVR_GRADING_SEGMENT_C);

							if(fvrGradingDetails != null && !fvrGradingDetails.isEmpty()){
								for (FVRGradingDetail fvrGradingDetail : fvrGradingDetails) {
									if(fvrGradingDetail.getSeqNo() != null){
										if(fvrGradingDetail.getSeqNo().equals(masterFVR.getKey())){
											eachFVRDTO.setKey(fvrGradingDetail.getKey());
											if(masterFVR.getKey().intValue() != 22 && fvrGradingDetail.getGrading() != null){
												//Fix for populate any segment C other then "FVR not received" if fvr replied 
												eachFVRDTO.setStatusFlagSegmentC(fvrGradingDetail.getGrading());
												isSegmentCEdit = true;
												isSegmentABEdit = false;
											}
											break;
										}}
								}
							}

							FVRTableDTO.add(eachFVRDTO);
						}

					}else if(fieldVisitRequest.getStatus().getKey().equals(ReferenceTable.ASSIGNFVR)){
						for (FVRGradingMaster masterFVR : fvrGradingSegC) {
							eachFVRDTO = new NewFVRGradingDTO();
							eachFVRDTO.setFvrSeqNo(masterFVR.getKey());
							eachFVRDTO.setCategory(masterFVR.getGradingType());
							eachFVRDTO.setSegment(SHAConstants.FVR_GRADING_SEGMENT_C);
							eachFVRDTO.setIsFVRReceived(Boolean.FALSE);

							if(fvrGradingDetails != null && !fvrGradingDetails.isEmpty()){
								for (FVRGradingDetail fvrGradingDetail : fvrGradingDetails) {
									if(fvrGradingDetail.getSeqNo() != null){
										if(fvrGradingDetail.getSeqNo().equals(masterFVR.getKey())){
											if(eachFVRDTO.getFvrSeqNo().equals(ReferenceTable.GRADING_FVR_NOT_RECEIVED)){
												eachFVRDTO.setKey(fvrGradingDetail.getKey());	
											}
										}}
								}
							}

							//V1.4
							if(eachFVRDTO.getFvrSeqNo().equals(ReferenceTable.GRADING_FVR_NOT_RECEIVED)){
								isSegmentCEdit = true;
								isSegmentABEdit = false;
								eachFVRDTO.setStatusFlagSegmentC(SHAConstants.YES_FLAG);
								eachFVRDTO.setIsAssignFVRNotReceived(Boolean.TRUE);
							}
							FVRTableDTO.add(eachFVRDTO);
						}
					}else{

						for (FVRGradingMaster masterFVR : fvrGradingSegC) {
							eachFVRDTO = new NewFVRGradingDTO();
							eachFVRDTO.setFvrSeqNo(masterFVR.getKey());
							eachFVRDTO.setCategory(masterFVR.getGradingType());
							eachFVRDTO.setSegment(SHAConstants.FVR_GRADING_SEGMENT_C);
							if(masterFVR.getKey().intValue() == 22){
								eachFVRDTO.setIsFVRReceived(Boolean.TRUE);
							}else{
								eachFVRDTO.setIsFVRReceived(Boolean.FALSE);
							}

							if(fvrGradingDetails != null && !fvrGradingDetails.isEmpty()){
								for (FVRGradingDetail fvrGradingDetail : fvrGradingDetails) {
									if(fvrGradingDetail.getSeqNo() != null){
										if(fvrGradingDetail.getSeqNo().equals(masterFVR.getKey())){
											eachFVRDTO.setKey(fvrGradingDetail.getKey());
											if(fvrGradingDetail.getGrading() != null){
												eachFVRDTO.setStatusFlagSegmentC(fvrGradingDetail.getGrading());
												isSegmentCEdit = true;
												isSegmentABEdit = false;
											}
											break;
										}}
								}
							}

							FVRTableDTO.add(eachFVRDTO);
						}



					}


					/*//V1.4
				if(fieldVisitRequest.getStatus().getKey().equals(ReferenceTable.ASSIGNFVR) || fieldVisitRequest.getStatus().getKey().equals(ReferenceTable.FVR_GRADING_STATUS)){
					isSegmentABEdit = Boolean.FALSE;
					isSegmentCEdit = Boolean.FALSE;
				}*/

					if(fieldVisitRequest.getStatus().getKey().equals(ReferenceTable.ASSIGNFVR)){
						fvrDto.setIsClearAllEnabled(Boolean.FALSE);
					}


					if(FVRTableDTO != null && !FVRTableDTO.isEmpty()){

						for (NewFVRGradingDTO gradingDto : FVRTableDTO) {
							if(isSegmentABEdit && !isSegmentCEdit){
								gradingDto.setIsEditAB(true);
							}else if(!isSegmentABEdit && isSegmentCEdit){
								gradingDto.setIsEditAB(false);
							}/*else if(!isSegmentABEdit && !isSegmentCEdit){
							gradingDto.setIsEditABC(Boolean.FALSE);
						}*/
						}
					}

					if(isSegmentABEdit && !isSegmentCEdit){
						fvrDto.setIsSegmentANotEdit(false);
						fvrDto.setIsSegmentBNotEdit(false);
						fvrDto.setIsSegmentCNotEdit(true);
					}else if(!isSegmentABEdit && isSegmentCEdit){
						fvrDto.setIsSegmentANotEdit(true);
						fvrDto.setIsSegmentBNotEdit(true);
						fvrDto.setIsSegmentCNotEdit(false);
					}/*else if(!isSegmentABEdit && !isSegmentCEdit){
					fvrDto.setIsSegmentANotEdit(true);
					fvrDto.setIsSegmentBNotEdit(true);
					fvrDto.setIsSegmentCNotEdit(true);
				}*/

					fvrDto.setNewFvrGradingDTO(FVRTableDTO);

					if (fieldVisitRequest.getGradingRmrks() != null) {
						fvrDto.setGradingRemarks(fieldVisitRequest
								.getGradingRmrks());
					}

					if(fieldVisitRequest.getStatus().getKey().equals(ReferenceTable.FVR_REPLY_RECEIVED)){
						fvrDto.setIsFVRReplied(Boolean.TRUE);
					}

					fvrDtoList.add(fvrDto);
				}
			}
			preauthdto.getPreauthMedicalDecisionDetails().setFvrGradingDTO(
					fvrDtoList);
		}				
	}

	public void generatePreAuthHoldLayout(@Observes @CDIEvent(PREAUTH_HOLD_EVENT) final ParameterDTO parameters)
	{
		view.generateHoldLayout();
	}

	public void loadRawDropDownValues(
			@Observes @CDIEvent(PREAUTH_LOAD_RAW_DROP_DOWN_VALUES) final ParameterDTO parameters) 
	{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadRRCRequestDropDownValues(mastersValueContainer);
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

	public void preauthSubmitDecisionMismatchAlert(@Observes @CDIEvent(PREAUTH_SUBMIT_DECISION_MISMATCH_ALERT) final ParameterDTO parameters) {

		PreauthDTO preauthdto = (PreauthDTO) parameters.getPrimaryParameter();
		dbCalculationService.callProcedureForSTP(preauthdto);
		/*String isDecisionMismatched = preauthService.getSTPALert(preauthdto.getKey(),preauthdto.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt(),preauthdto.getStatusKey());
		preauthdto.getPreauthMedicalDecisionDetails().setIsDecisionMismatched(isDecisionMismatched);*/
	}

	public void saveStpRemarks(@Observes @CDIEvent(SAVE_STP_REMARKS) final ParameterDTO parameters) {
		PreauthDTO preauthdto = (PreauthDTO) parameters.getPrimaryParameter();
		if(null != preauthdto && null != preauthdto.getKey()){
			Preauth preauth = preauthService.getPreauthById(preauthdto.getKey());
			preauthService.updateSTPRemarks(preauth,preauthdto);
		}
	}

	public void setPreauthButtonLayout(
			@Observes @CDIEvent(PREAUTH_HOSP_SCORING_EVENT) final ParameterDTO parameters) {
		ProcessPreAuthButtonLayout ProcessPreAuthButtonLayout = (ProcessPreAuthButtonLayout)parameters.getPrimaryParameter();
		view.generateButtonLayoutBasedOnScoring(ProcessPreAuthButtonLayout);

	}

	public void preauthTopUpPolicyInsert(@Observes @CDIEvent(PREAUTH_TOPUP_POLICY_EVENT) final ParameterDTO parameters) {

		PreauthDTO preauthdto = (PreauthDTO) parameters.getPrimaryParameter();
		/*PremPolicyDetails policyDetails = premiaPullService.fetchGmcPolicyDetailsFromPremia(preauthdto.getNewIntimationDTO().getInsuredPatient().getTopUppolicyNo(), String.valueOf(preauthdto.getNewIntimationDTO().getInsuredPatient().getTopUpInsuredId()));
		Boolean isIntegratedPolicy =premiaPullService.populateGMCandGPAPolicy(policyDetails, String.valueOf(preauthdto.getNewIntimationDTO().getInsuredPatient().getTopUpInsuredId()),false);
		 */
		Insured curentPolicy = preauthService.getTopUpPolicy(preauthdto.getPolicyKey());
		//		String topUpPolicyNo = curentPolicy.getTopUppolicyNo();



		/*SelectValue riskName = (SelectValue) dbCalculationService.getTopUpPolicyDetailsForRiskName(preauthdto.getPolicyDto().getPolicyNumber(),
				topUpPolicy.getInsuredId(),topUpPolicy.getKey(),preauthdto.getIntimationKey(),topUpPolicyNo);
		 */		
		//Map<String, Object> map = new HashMap<String, Object>();
		/*map.put("topupPolicy", premiaPullService.getPolicyByPolicyNubember(preauthdto.getNewIntimationDTO().getInsuredPatient().getTopUppolicyNo()));
		map.put("topupInsured", premiaPullService.getInsuredByPolicyAndInsuredNameForDefault(preauthdto.getNewIntimationDTO().getInsuredPatient().getTopUppolicyNo(), preauthdto.getNewIntimationDTO().getInsuredPatient().getTopUpInsuredId()));*/

		/*map.put("topupPolicy", topUpPolicy);
		map.put("topupInsured", dbCalculationService.getTopUpPolicyDetailsForRiskName(preauthdto.getPolicyDto().getPolicyNumber(),
				topUpPolicy.getInsuredId(),topUpPolicy.getKey(),preauthdto.getIntimationKey(),topUpPolicyNo));*/

		List<PreauthDTO> map = dbCalculationService.getTopUpPolicyDetailsForRiskName(preauthdto.getPolicyDto().getPolicyNumber(),
				0L, preauthdto.getNewIntimationDTO().getInsuredPatient().getKey(),preauthdto.getIntimationKey());

		view.generateTopUpLayout(map);
	}


	public void preauthTopUpPolicyIntimation(@Observes @CDIEvent(PREAUTH_TOPUP_POLICY_INTIMATION_EVENT) final ParameterDTO parameters) {

		PreauthDTO preauthdto = (PreauthDTO) parameters.getPrimaryParameter();
		//		Map<String, Object> topupData = (Map<String, Object>) parameters.getSecondaryParameters()[0];
		List<PreauthDTO> topupData = (List<PreauthDTO>) parameters.getSecondaryParameters()[0];
		String riskId = (String)parameters.getSecondaryParameters()[1];
		//		Policy topUpPolicy = (Policy)topupData.get("topupPolicy");
		//    	Insured topUpInsured = (Insured)topupData.get("topupInsured");
		Policy topUpPolicyNo = policyService.getByPolicyNumber(topupData.get(0).getTopUPPolicyNumber());
		Insured topUpInsured = policyService.getInsuredByPolicyAndInsuredName(topupData.get(0).getTopUPPolicyNumber(),Long.valueOf(riskId));
		String intimationNo = dbCalculationService.getTopupPolicyIntimation(preauthdto.getNewIntimationDTO().getPolicy().getPolicyNumber(), preauthdto.getNewIntimationDTO().getIntimationId(), preauthdto.getNewIntimationDTO().getKey(), 
				topUpPolicyNo.getKey(), topUpInsured.getKey(), Long.valueOf(riskId));

		view.generateTopUpIntimationLayout(intimationNo);
	}

	public void getSugCategoryLayout(
			@Observes @CDIEvent(PREAUTH_SUB_REJECT_CATEG_LAYOUT) final ParameterDTO parameters) {
		Long id = (Long) parameters.getPrimaryParameter();

		BeanItemContainer<SelectValue> rejSubcategContainer = masterService.getRejSubcategContainer(id);

		view.setSubCategContainer(rejSubcategContainer);
	}

	public void holdSubmitWizard(
			@Observes @CDIEvent(PREAUTH_HOLD_SUBMIT) final ParameterDTO parameters) throws Exception {
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
				holdWrkFlowMap.put(SHAConstants.STAGE_SOURCE, SHAConstants.SOURCE_PREAUTH_PROCESS);
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

				dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
			}
			Preauth preauth = preauthService.getPreauthById(preauthDTO.getKey());
			if (preauthDTO.getIsPreauthAutoAllocationQ() != null && preauthDTO.getIsPreauthAutoAllocationQ()) {
				if(preauth != null){
					preauthService.updateStageInformation(preauth,preauthDTO);
				}else{
					Claim claim = preauthService.getClaimByKey(preauthDTO.getClaimKey());
					preauthService.updateStageInformationHoldByPassFLP(claim,preauthDTO);
				}
			}

			if(preauthDTO.getDbOutArray() != null){
				Long wkFlowKey = (Long)wrkFlowMap.get(SHAConstants.WK_KEY);	
				preauthService.updateHoldRemarksForAutoAllocation(wkFlowKey, preauthDTO.getPreauthMedicalDecisionDetails().getHoldRemarks());
			}
			view.buildSuccessLayout();
		}else{
			view.buildFailureLayout(aquiredUser.toString());
		}

	}

	public void setCategoryValue(@Observes @CDIEvent(SET_PRE_AUTH_CATAGORY_VALUE) final ParameterDTO parameters)
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
			@Observes @CDIEvent(PREAUTH_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(PREAUTH_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}
	
	public void setNegotiatedSavedAmount(
			@Observes @CDIEvent(PREAUTH_LOAD_NEGOTIATED_SAVED_AMOUNT) final ParameterDTO parameters) {
		view.setnegotiatedSavedAmount();
	}
	
	public void setAppAmountBSIAlert(@Observes @CDIEvent(PREAUTH_APPROVE_BSI_ALERT) final ParameterDTO parameters) {
		view.setAppAmountBSIAlert();
	}
	
	public void removedynamicComp(@Observes @CDIEvent(PREAUTH_DYNAMICFRMLAYOUT_REMOVE_COMP) final ParameterDTO parameters) {
		view.removedynamicComp();
	}
	
	public void generateFieldsBasedOnImplantApplicable(@Observes @CDIEvent(PREAUTH_IMPLANT_APPLICABLE_CHANGED) final ParameterDTO parameters)
	{
		Boolean isCked = (Boolean) parameters.getPrimaryParameter();
		view.generateFieldsBasedOnImplantApplicable(isCked);
	}

	public void processICACPerauthSumbit(@Observes @CDIEvent(PREAUTH_ICAC_SUBMIT_EVENT) final ParameterDTO parameters){
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
	
	public void generateFieldsBasedOnSubCatTWO(@Observes @CDIEvent(PREAUTH_PCC_SUB_CAT_TWO_GENERATE) final ParameterDTO parameters)
	{
		Long subcatKey = (Long) parameters.getPrimaryParameter();
		view.generateFieldsBasedOnSubCatTWO(preauthService.getPCCSubCategorystwo(subcatKey));
	}
	
	public void addpccSubCategory(@Observes @CDIEvent(PREAUTH_PCC_SUB_CAT) final ParameterDTO parameters)
	{
		Long subcatKey = (Long) parameters.getPrimaryParameter();
		view.addpccSubCategory(preauthService.getPCCSubCategorys(subcatKey));
	}
}