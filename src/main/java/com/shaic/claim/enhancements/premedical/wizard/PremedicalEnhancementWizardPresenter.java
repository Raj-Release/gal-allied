package com.shaic.claim.enhancements.premedical.wizard;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.UpdateOtherClaimDetailDTO;
import com.shaic.claim.premedical.dto.OtherBenefitsTableDto;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.rod.wizard.dto.SectionDetailsTableDTO;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PEDValidationService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.State;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.ViewTmpIntimation;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.preauth.HospitalPackage;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.ProcedureMaster;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.domain.service.PreMedicalService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(PremedicalEnhancementWizard.class)
public class PremedicalEnhancementWizardPresenter extends AbstractMVPPresenter<PremedicalEnhancementWizard> {
	private static final long serialVersionUID = 7646463359899061842L;

	@EJB
	private ClaimService claimService;

	@EJB
	private MasterService masterService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private PreMedicalService preMedicalService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private PEDValidationService pedValidationService;
	
	@EJB
	private PolicyService policyService;

	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private IntimationService intimationService;
	
	public static final String PREMEDICAL_STEP_CHANGE_EVENT = "enhancement_step_change_event";
	public static final String PREMEDICAL_SAVE_EVENT = "enhancement_save_event";
	public static final String PREMEDICAL_SUBMITTED_EVENT = "enhancement_submit_event";
	public static final String SETUP_REFERENCE_DATA = "enhancement_set_edit_data";
	public static final String PREMEDICAL_TREATMENT_TYPE_CHANGED = "enhancement_treatment_type_changed";
	public static final String PREMEDICAL_PATIENT_STATUS_CHANGED = "enhancement_patient_status_changed";
	public static final String PREMEDICAL_RELAPSE_OF_ILLNESS_CHANGED = "enhancement_relapse_of_illness_changed";
	
	public static final String PRE_MEDICAL_ENH_OTHER_BENEFITS = "pre_medical_enh_other_benefits";
	
	public static final String PREMEDICAL_QUERY_EVENT ="enhancement_query_click_event";
	public static final String PREMEDICAL_SUGGEST_REJECTION_EVENT ="enhancement_rejection_click_event";
	public static final String PREMEDICAL_SEND_FOR_PROCESSING_EVENT ="enhancement_send_for_processing_click_event";

	public static final String PREAUTH_REFERCOORDINATOR_EVENT = "enhancement_refer_coordinator_event";
	
	public static final String SUBLIMIT_CHANGED_BY_SECTION = "Set sublimit values by selecting section for premedical enhancement";
	
	public static final String PREMEDICAL_PREVIOUS_CLAIM_DETAILS = "enhancement_previous_claim_details";

	public static final String PREMEDICAL_QUERY_BUTTON_EVENT ="enhancement_query_button_event";
	
	public static final String MASTER_SEARCH_STATE = "enhancement_master_search_stae";
	
	public static final String CHECK_CRITICAL_ILLNESS = "enhancement_check_critical_illness";
	
	public static final String GET_ICD_BLOCK = "enhancement_get_icd_block";
	
	public static final String GET_ICD_CODE = "enhancement_get_icd_code";
	
	public static final String GET_PRE_ENHN_PROCEDURE_VALUES = "pre_enhn_procedure_values";
	
	public static final String GET_SEC_COVER = "pre_medical_enhancement_get_sec_cover";
	
	public static final String GET_SUB_COVER = "pre_medical_enhancement_get_sub_cover";
	
	public static final String GET_EXCLUSION_DETAILS = "enhancement_get_exclusion_details";

	public static final String NO_OF_DAYS_RULE = "no_of_days_rule";

	public static final String PREMEDICAL_GET_PACKAGE_RATE = "enhancement_get_package_rate_by_procedure_key_and_hospital_key";

	public static final String SET_DB_DETAILS_TO_REFERENCE_DATA = "enhancement_set_claim_and_sublimit_db_details";

	public static final String INTERIM_OR_FINAL_ENHANCEMENT = "enhancement_interim_or_final_";
	
	public static final String PREMEDICAL_ENHANCEMENT_COORDINATOR_REQUEST_EVENT = "premedical_enhancement_coordinator_request";
	
	public static final String GET_HOSPITALIZATION_DETAILS = "premedical_enhancement_get_hospitalization_details";
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	public static final String VALIDATE_PREMEDICAL_ENHANCEMENT_USER_RRC_REQUEST = "validate_premedical_enhancement_user_rrc_request";
	
	public static final String PRE_MEDICAL_ENHANCEMENT_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "pre_medical_enhancement_load_rrc_request_drop_down_values";
	
	public static final String PRE_MEDICAL_ENHANCEMENT_SAVE_RRC_REQUEST_VALUES = "pre_medical_enhancement_save_rrc_request_values";

	public static final String PREMEDICAL_ENHANCEMENT_HOSPITALISATION_DUE_TO = "pre_medical_enhancement_hospitalization_due_to";

	public static final String PRE_MEDICAL_ENHANCEMENT_REPORTED_TO_POLICE = "PRE_MEDICAL_ENHANCEMENT_REPORTED_TO_POLICE";
	
	 public static final String PREMEDICAL_ENHANCEMENT_PREVIOUS_CLAIM_FOR_POLICY = "premedical_enhancement_previous_claim_details for policy";
	 
	 public static final String UPDATE_PREVIOUS_CLAIM_DETAILS = "update_previous_claim_details for premedical ehancement";
	 
	 public static final String REFERENCE_DATA_CLEAR = "reference data clear first level enhancement";
	 
	 public static final String GET_ASSISTED_REPRODUCTION_TREATMENT_COVER_KEY_FE = "GET_ASSISTED_REPRODUCTION_TREATMENT_COVER_KEY_FE";

	public static final String PREMEDICAL_ENHANCEMENT_LUMEN_REQUEST = "premedical_enhancement_lumen_request";
	
	public static final String SET_PRE_MEDICAL_ENHANCEMENT_CATAGORY_VALUE = "SET_PRE_MEDICAL_ENHANCEMENT_CATAGORY_VALUE";

	public static final String PRE_MEDICAL_ENHANCEMENT_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "pre_medical_enhancement_load_rrc_request_sub_category_values";
	
	public static final String PRE_MEDICAL_ENHANCEMENT_LOAD_RRC_REQUEST_SOURCE_VALUES = "pre_medical_enhancement_load_rrc_request_source_values";
	
	public static final String PRE_MEDICAL_ENHANCEMENT_HOLD_SUBMIT = "pre_medical_enhancement_hold_submit";

	public static final String PRE_MEDICAL_ENHANCEMENT_SEND_FOR_HOLD_EVENT = "pre_medical_enhancement_send_for_hold_event";

	public static final String PREMEDICAL_ENHANCEMENT_SAVE_AUTO_ALLOCATION_CANCEL_REMARKS = "premedical_enhancement_save_auto_allocation_cancel_remarks";

	public void coordinatorRequestEvent(
			@Observes @CDIEvent(PREMEDICAL_ENHANCEMENT_COORDINATOR_REQUEST_EVENT) final ParameterDTO parameters) {
				view.intiateCoordinatorRequest();
		}
	
	public void activeStepChanged(@Observes @CDIEvent(PREMEDICAL_STEP_CHANGE_EVENT) final ParameterDTO parameters) 
	{
		view.setWizardPageReferenceData(referenceData);
	}
	
	public void getOtherBenefitDetails(@Observes @CDIEvent(PRE_MEDICAL_ENH_OTHER_BENEFITS) final ParameterDTO parameters) 
	{
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		
		Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(
				preauthDTO.getNewIntimationDTO().getInsuredPatient()
						.getInsuredId().toString(), preauthDTO.getPolicyDto()
						.getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		
		if(preauthDTO.getPreauthDataExtractionDetails().getOtherBenefitsList() == null || 
				(preauthDTO.getPreauthDataExtractionDetails().getOtherBenefitsList() != null && preauthDTO.getPreauthDataExtractionDetails().getOtherBenefitsList().isEmpty())){
			List<OtherBenefitsTableDto> benefitsDtoList =  new ArrayList<OtherBenefitsTableDto>();		
			if(SHAConstants.YES_FLAG.equalsIgnoreCase(preauthDTO.getPreauthDataExtractionDetails().getOtherBenfitFlag()) && preauthDTO.getKey() != null){
				benefitsDtoList =  preMedicalService.getBenefitAmountDetailsByCashlessKey(preauthDTO.getKey());
			}
			if(benefitsDtoList.isEmpty())
			{
				benefitsDtoList =  dbCalculationService.getOtherBebefitsList(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey(), insuredSumInsured.longValue(),ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);			
			}
			preauthDTO.getPreauthDataExtractionDetails().setOtherBenefitsList(benefitsDtoList);
		}		
		view.genertateFieldsBasedOnObterBenefits(preauthDTO);
	}
	
	public void saveWizard(@Observes @CDIEvent(PREMEDICAL_SAVE_EVENT) final ParameterDTO parameters) 
	{
		
	}
	
	@SuppressWarnings("static-access")
	public void submitWizard(
			@Observes @CDIEvent(PREMEDICAL_SUBMITTED_EVENT) final ParameterDTO parameters) {
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();

		if(!preauthDTO.getIsRepremedical()) {
			Preauth submitPreMedical = preMedicalService.submitPreMedical(preauthDTO, false);
			preMedicalService.setDBWorkFlowObject(submitPreMedical,preauthDTO,false);
		} else {
			Preauth updatePreMedical = preMedicalService.updatePreMedical(preauthDTO, false);
			preMedicalService.setDBWorkFlowObject(updatePreMedical,preauthDTO,false);
		}
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
		if(parameters.getPrimaryParameter() != null) {
			
			/*referenceData.put("sectionDetails", masterService
					.getSectionList(bean.getNewIntimationDTO().getPolicy().getProduct().getKey(),bean.getNewIntimationDTO().getPolicy()));*/
			
			//Pre Prod issue Mail - 05-MAR-2019
			BeanItemContainer<SelectValue> sectionList = masterService
					.getSectionList(bean.getNewIntimationDTO().getPolicy().getProduct().getKey(),bean.getNewIntimationDTO().getPolicy());
			
			if(ReferenceTable.getSuperSurplusKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
					&& ((bean.getNewIntimationDTO().getPolicy().getPolicyPlan() == null) || (bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")))){
				List<SelectValue> itemIds = sectionList.getItemIds();
				List<SelectValue> filterItem = new ArrayList<SelectValue>();
				for (SelectValue selectValue : itemIds) {
					if(! selectValue.getCommonValue().equals(ReferenceTable.DELVIERY_AND_NEW_BORN_SECTION_CODE)){
						filterItem.add(selectValue);
					}
				}
				sectionList.removeAllItems();
				sectionList.addAll(filterItem);
				referenceData.put("sectionDetails", sectionList);
			}else{
				referenceData.put("sectionDetails", sectionList);
			}
			
		}
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
		BeanItemContainer<SelectValue> procedureListNames = preauthService.getProcedureListNames();
		referenceData.put("procedureName", procedureListNames);
		referenceData.put("procedureCode", procedureListNames);
		referenceData.put("medicalSpeciality", preauthService.getSpecialityType("M"));
		referenceData.put("surgicalSpeciality", preauthService.getSpecialityType("S"));
		referenceData.put("nextLOVSpeciality", masterService.getNextLOVSpecialityType());
		referenceData.put("sublimitApplicable", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("considerForPayment", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		
		//CR R20181300
		referenceData.put("pedImpactOnDiagnosis", masterService.getSelectValueContainer(ReferenceTable.PED_IMPACT_ON_DIAGNOSI));
		referenceData.put("reasonForNotPaying", masterService.getSelectValueContainer(ReferenceTable.PED_NON_PAYABLE_REASON)); //EXCLUSION_DETAILS
		//CR R20181300
		
		referenceData.put("considerForDayCare", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("commonValues", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("pedExclusionImpactOnDiagnosis",masterService.getSelectValueContainer(ReferenceTable.IMPACT_ON_DIAGNOSI));
		referenceData.put("exclusionDetails",masterService.getSelectValueContainer(ReferenceTable.EXCLUSION_DETAILS));
		referenceData.put("procedureStatus",masterService.getSelectValueContainer(ReferenceTable.PROCEDURE_STATUS));
//		referenceData.put("pedList", masterService.getInsuredPedDetails());
		
		referenceData.put("rejectionCategory", masterService.getRevisedRejectionCategoryByValue(productKey));   //masterService.getRejectionCategoryByValue();
		referenceData.put("sendForProcessingCategory", masterService.getRevisedRejectionCategoryByValue(productKey));  //masterService.getRejectionCategoryByValue();
		
		//R1313  NEW REJ_CATEGORY LOV
		if(!ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			referenceData.put("rejectionCategory", masterService.getEnhRejectionCategoryByValue());
			referenceData.put("sendForProcessingCategory", masterService.getEnhRejectionCategoryByValue());
		}	
		
		referenceData.put("terminateCover", masterService.getSelectValueContainer(ReferenceTable.TERMINATE_COVER));
		referenceData.put("criticalIllness", preauthService.getCriticalIllenssMasterValues(bean));
		referenceData.put("illness", masterService.getSelectValueContainer(ReferenceTable.ILLNESS));
		
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
		referenceData.put("medicalCategory",  getMedicalAsCategory());
		
		referenceData.put("hospitalisationDueTo", masterService.getSelectValueContainer(ReferenceTable.HOSPITALIZATION_DUE_TO));
		referenceData.put("causeOfInjury", masterService.getSelectValueContainer(ReferenceTable.CAUSE_OF_INJURY));
		referenceData.put("section", masterService
				.getSelectValueContainer(ReferenceTable.SECTION));
		referenceData.put("typeOfDelivery", masterService
				.getSelectValueContainer(ReferenceTable.DELIVERY_TYPE));
		referenceData.put("natureOfLoss", masterService
				.getNatureCauseLossSelectValueContainer(SHAConstants.HEALTH_LOB, ReferenceTable.NATURE_OF_LOSS));
		referenceData.put("causeOfLoss", masterService
				.getNatureCauseLossSelectValueContainer(SHAConstants.PA_LOB, ReferenceTable.CAUSE_OF_LOSS));
		referenceData.put("catastrophicLoss", masterService.getCatastrophicLossList());
		
//		referenceData.put("relapsedClaims", relapseClaimContainer);
			
//		CustomLazyContainer customLazyContainer = new CustomLazyContainer(3, "value", masterService, "diagnosis");
//		customLazyContainer.addContainerProperty("value", SelectValue.class, null);
//		referenceData.put("diagnosis", customLazyContainer);		
	}
	
	public BeanItemContainer<SelectValue> getMedicalAsCategory() {
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);

		List<SelectValue> selectValueList = new ArrayList<SelectValue>();

		SelectValue selectValue = new SelectValue();
		selectValue.setId(ReferenceTable.PRE_MEDICAL_PRE_AUTH_MEDICAL_CATEGORY);
		selectValue.setValue(ReferenceTable.MEDICAL);
		selectValueList.add(selectValue);

		selectValueContainer.addAll(selectValueList);

		return selectValueContainer;
	}
	
	
	public void generateFieldsBasedOnTreatement(@Observes @CDIEvent(PREMEDICAL_TREATMENT_TYPE_CHANGED) final ParameterDTO parameters)
	{
		view.generateFieldsBasedOnTreatment();
	}
	
	public void generateFieldsBasedOnPatientStatus(@Observes @CDIEvent(PREMEDICAL_PATIENT_STATUS_CHANGED) final ParameterDTO parameters)
	{
		view.genertateFieldsBasedOnPatientStaus();
	}
	
	public void generateFieldsBasedOnRelapseIllness(@Observes @CDIEvent(PREMEDICAL_RELAPSE_OF_ILLNESS_CHANGED) final ParameterDTO parameters)
	{
		PreauthDTO preauthDto = (PreauthDTO) parameters.getPrimaryParameter();
		SelectValue relpaseofIllness = parameters.getSecondaryParameters() != null ? (SelectValue) parameters.getSecondaryParameters()[0] : null;
	
		if(relpaseofIllness != null && relpaseofIllness.getId() == ReferenceTable.COMMONMASTER_YES){
			BeanItemContainer<SelectValue> relapseClaimContainer = claimService.getRelpseClaimsForPolicy(preauthDto.getPolicyDto().getPolicyNumber(), preauthDto.getClaimNumber());
			referenceData.put("relapsedClaims", relapseClaimContainer);
		}
		view.genertateFieldsBasedOnRelapseOfIllness(referenceData);
	}
	
	public void generatePreAuthReferCoOrdinatorLayout(@Observes @CDIEvent(PREAUTH_REFERCOORDINATOR_EVENT) final ParameterDTO parameters)
	{
		view.generateReferCoOrdinatorLayout();
	}
	
	public void generateFieldsForQuery(@Observes @CDIEvent(PREMEDICAL_QUERY_EVENT) final ParameterDTO parameters)
	{
		view.generateFieldsOnQueryClick();
	}

	
	public void generateFieldsForSuggestRejection(@Observes @CDIEvent(PREMEDICAL_SUGGEST_REJECTION_EVENT) final ParameterDTO parameters)
	{
		view.generateFieldsOnSuggestRejectionClick();
	}

	
	public void generateFieldsForSendForProcessing(@Observes @CDIEvent(PREMEDICAL_SEND_FOR_PROCESSING_EVENT) final ParameterDTO parameters)
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
		SelectValue icdCodeContainer = masterService.getIcdDescriptionKey(blockKey);
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addBean(icdCodeContainer);
		
		view.setIcdCode(selectValueContainer);
	}
	
	
	public void getEnhancementProcedureValues(@Observes @CDIEvent(GET_PRE_ENHN_PROCEDURE_VALUES) final ParameterDTO parameters)
	{
		Long specialistkey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue>  procedures = preauthService.getProcedureforSpeciality(specialistkey);
		view.setPreEnhnProcedureValues(procedures);
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
	
	public void getExclusionDetails(@Observes @CDIEvent(GET_EXCLUSION_DETAILS) final ParameterDTO parameters)
	{
		Long impactDiagnosisKey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<ExclusionDetails> icdCodeContainer = masterService.getExclusionDetailsByImpactKey(impactDiagnosisKey);
		
		view.setExclusionDetails(icdCodeContainer);
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
	
	public void getPackageRate(@Observes @CDIEvent(PREMEDICAL_GET_PACKAGE_RATE) final ParameterDTO parameters)
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
		
		String policyPlan = preauthDTO.getNewIntimationDTO().getInsuredPatient().getPlan() != null ? preauthDTO.getNewIntimationDTO().getInsuredPatient().getPlan() : "";
		
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
		
//		referenceData.put("sublimitDBDetails", dbCalculationService.getClaimedAmountDetails(policyDTO.getProduct().getKey(),sumInsured , insuredAge));
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

		//referenceData.put("insuredPedList", masterService.getInusredPEDList(policyDTO.getInsuredId()));
				/**
				 * Insured id will be obtained from intimation dto and not from policy dto. This is due
				 * to the change in policy related tables during policy table refractoring activity.
				 * */
				referenceData.put("insuredPedList", masterService.getInusredPEDList(preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString()));
				List<Double> copayValue = new ArrayList<Double>();
//				Below Code Added for CR2019007 for copay value
				if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
					copayValue = dbCalculationService.getProductCoPayForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),preauthDTO.getNewIntimationDTO()
							.getInsuredPatient().getKey());
				} else {
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
		    SelectValue value = null;
		    for (String string : coPayPercentage) {
		    	value = new SelectValue();
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
	public void getHospitalizationDetails(
			@Observes @CDIEvent(GET_HOSPITALIZATION_DETAILS) final ParameterDTO parameters) {
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Hospitals hospitalById = hospitalService.getHospitalById(preauthDTO.getHospitalKey());
		PolicyDto policyDTO = preauthDTO.getPolicyDto();
		//String insuredAge = getInsuredAge(policyDTO.getInsuredDob());
//		NewIntimationDto newIntimationDTO = preauthDTO.getNewIntimationDTO() != null ? preauthDTO.getNewIntimationDTO() : preauthDTO.getClaimDTO().getNewIntimationDto();
		Double sumInsured = dbCalculationService.getInsuredSumInsured(preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), preauthDTO.getPolicyDto().getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
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
			}else {
				
				 String policyPlan = preauthDTO.getNewIntimationDTO().getInsuredPatient().getPlan() != null ? preauthDTO.getNewIntimationDTO().getInsuredPatient().getPlan() : "";
					
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
			        
				hospitalizationDetails = dbCalculationService.getHospitalizationDetails(policyDTO.getProduct().getKey(),
						sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),0l,policyPlan);
			}		
			
		}
		
		referenceData.put("claimDBDetails", hospitalizationDetails);
		view.setHospitalizationDetails(hospitalizationDetails);
		
	}
	
	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_PREMEDICAL_ENHANCEMENT_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		RRCDTO rrcDTO = preauthDTO.getRrcDTO();
		reimbursementService.setRequestStageIdForRRC(rrcDTO,SHAConstants.PRE_MEDICAL_PROCESSING_ENHANCEMENT);
		preauthDTO.setRrcDTO(rrcDTO);
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(
				preauthDTO.getNewIntimationDTO().getInsuredPatient()
						.getInsuredId().toString(), preauthDTO.getPolicyDto()
						.getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		if(isValid)
		{
			reimbursementService.loadRRCRequestValues(preauthDTO,insuredSumInsured,SHAConstants.PRE_MEDICAL_PROCESSING_ENHANCEMENT);
		}
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	

	public void saveRRCRequestValues(@Observes @CDIEvent(PRE_MEDICAL_ENHANCEMENT_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_CASHLESS);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(PRE_MEDICAL_ENHANCEMENT_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	/**
	 * Added for RRC Ends
	 * */

	public void generateFieldsBasedOnHospitalisationDueTo(@Observes @CDIEvent(PREMEDICAL_ENHANCEMENT_HOSPITALISATION_DUE_TO) final ParameterDTO parameters)
	{
		PreauthDTO beanDto = (PreauthDTO) parameters.getPrimaryParameter();
		SelectValue selectedValue = beanDto.getPreauthDataExtractionDetails().getHospitalisationDueTo(); 
		
		if(beanDto != null && beanDto.getNewIntimationDTO().getPolicy() != null && beanDto.getNewIntimationDTO().getPolicy().getProduct().getKey() != null &&
//				ReferenceTable.FHO_PRODUCT_REVISED.equals(beanDto.getNewIntimationDTO().getPolicy().getProduct().getKey()) && selectedValue != null && 
				
				//Needs to uncomment aboveCode  for testing purpose,  below code needs to be removed only for testing purpose
				ReferenceTable.getFHORevisedKeys().containsKey(beanDto.getNewIntimationDTO().getPolicy().getProduct().getKey()) && selectedValue != null &&
				
				selectedValue.getValue() != null && !selectedValue.getValue().isEmpty() && 
				(ReferenceTable.ASSISTED_REPRODUCTION_TREATMENT).equals(selectedValue.getId())){
						
			List<SectionDetailsTableDTO> sectionDetailsList = masterService.getSectionDetailsDtoList(ReferenceTable.ASSISTED_REPRODUCTION_TREATMENT_COVER_KEY);
			beanDto.getPreauthDataExtractionDetails().setSectionDetailsListDto(sectionDetailsList);
			}			
//		view.genertateFieldsBasedOnHospitalisionDueTo(selectedValue);
		view.genertateFieldsBasedOnHospitalisionDueTo(beanDto);
	}
	
	public void generateFieldsBasedOnReportedToPolice(@Observes @CDIEvent(PRE_MEDICAL_ENHANCEMENT_REPORTED_TO_POLICE) final ParameterDTO parameters)
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
		
		String policyPlan = preauthDTO.getNewIntimationDTO().getInsuredPatient().getPlan() != null ? preauthDTO.getNewIntimationDTO().getInsuredPatient().getPlan() : "";
		
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
							sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),0l,policyPlan);
				}
				
				
			}
			
			referenceData.put("claimDBDetails", hospitalizationDetails);
		
//		view.setWizardPageReferenceData(referenceData);
		
	}
	
	public void getPreviousClaimForPolicy(
			@Observes @CDIEvent(PREMEDICAL_ENHANCEMENT_PREVIOUS_CLAIM_FOR_POLICY) final ParameterDTO parameters) {
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
	
	
	public void showUpdateClaimDetailTable(
			@Observes @CDIEvent(UPDATE_PREVIOUS_CLAIM_DETAILS) final ParameterDTO parameters) {
		
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		
		List<UpdateOtherClaimDetailDTO> updateOtherClaimDetails = new ArrayList<UpdateOtherClaimDetailDTO>();
		if(preauthDTO.getUpdateOtherClaimDetailDTO().isEmpty()){
			
			updateOtherClaimDetails = preauthService.getUpdateOtherClaimDetailsDTO(preauthDTO.getKey());
			
			if(updateOtherClaimDetails != null && updateOtherClaimDetails.isEmpty()){
				updateOtherClaimDetails = preauthService.getUpdateOtherClaimDetails(preauthDTO.getPreviousPreauthKey(),preauthDTO);
			}
//			updateOtherClaimDetails = preauthService.getUpdateOtherClaimDetails(preauthDTO.getNewIntimationDTO().getPolicy().getKey());
		}else{
			updateOtherClaimDetails = preauthDTO.getUpdateOtherClaimDetailDTO();
		}
	    
	    view.setUpdateOtherClaimsDetails(updateOtherClaimDetails, referenceData);
	}
	
	public void clearReferenceData(
			@Observes @CDIEvent(REFERENCE_DATA_CLEAR) final ParameterDTO parameters) {
		SHAUtils.setClearReferenceData(referenceData);
	}
	
	public void getAssistedReprodTreatment(@Observes @CDIEvent(GET_ASSISTED_REPRODUCTION_TREATMENT_COVER_KEY_FE) final ParameterDTO parameters) {
		try{
		PreauthDTO bean = (PreauthDTO) parameters.getPrimaryParameter();
		Long assistedKey = reimbursementService.getAssistedReprodTreatment(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getId());

		view.setAssistedReprodTreatment(assistedKey);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void preauthLumenRequest(@Observes @CDIEvent(PREMEDICAL_ENHANCEMENT_LUMEN_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		view.buildInitiateLumenRequest(preauthDTO.getNewIntimationDTO().getIntimationId());
	}
	
	public void getPreviousClaimDetails(
			@Observes @CDIEvent(PREMEDICAL_PREVIOUS_CLAIM_DETAILS) final ParameterDTO parameters) {
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
	
	public void setCategoryValue(@Observes @CDIEvent(SET_PRE_MEDICAL_ENHANCEMENT_CATAGORY_VALUE) final ParameterDTO parameters)
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
			@Observes @CDIEvent(PRE_MEDICAL_ENHANCEMENT_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(PRE_MEDICAL_ENHANCEMENT_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}
	
	public void holdSubmitWizard(
			@Observes @CDIEvent(PRE_MEDICAL_ENHANCEMENT_HOLD_SUBMIT) final ParameterDTO parameters) throws Exception {
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
				holdWrkFlowMap.put(SHAConstants.STAGE_SOURCE, SHAConstants.SOURCE_FLE_PROCESS);
				if(preauthDTO.getStrUserName() != null){
					TmpEmployee employeeByName = preauthService.getEmployeeByName(preauthDTO.getStrUserName());
					if(employeeByName != null){
						holdWrkFlowMap.put(SHAConstants.REFERENCE_USER_ID,employeeByName.getEmpId());
					}
				}
				holdWrkFlowMap.put(SHAConstants.PAYLOAD_PED_REFERRED_BY,preauthDTO.getStrUserName());
				holdWrkFlowMap.put(SHAConstants.PAYLOAD_PED_TYPE,SHAConstants.HOLD_FLAG);
				holdWrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.FLE_HOLD_OUTCOME);
				holdWrkFlowMap.put(SHAConstants.PAYLOAD_PED_REQUESTOR_ROLE,preauthDTO.getPreMedicalPreauthMedicalDecisionDetails().getHoldRemarks());
				Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(holdWrkFlowMap);

				dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
			}
			Preauth preauth = preauthService.getPreauthById(preauthDTO.getKey());
			if (preauthDTO.getIsPreauthAutoAllocationQ() != null && preauthDTO.getIsPreauthAutoAllocationQ()) {
				preMedicalService.updateStageInformation(preauth,preauthDTO);
			}

			if(preauthDTO.getDbOutArray() != null){
				Long wkFlowKey = (Long)wrkFlowMap.get(SHAConstants.WK_KEY);	
				preauthService.updateHoldRemarksForAutoAllocation(wkFlowKey, preauthDTO.getPreMedicalPreauthMedicalDecisionDetails().getHoldRemarks());
			}
			view.buildSuccessLayout();
		}else{
			view.buildFailureLayout(aquiredUser.toString());
		}

	}
	
	public void generateFieldsOnHoldClick(
			@Observes @CDIEvent(PRE_MEDICAL_ENHANCEMENT_SEND_FOR_HOLD_EVENT) final ParameterDTO parameters) {
		view.generateFieldsOnHoldClick();
	}
	
	public void saveCancelRemarks(@Observes @CDIEvent(PREMEDICAL_ENHANCEMENT_SAVE_AUTO_ALLOCATION_CANCEL_REMARKS) final ParameterDTO parameters) {
		PreauthDTO preauthdto = (PreauthDTO) parameters.getPrimaryParameter();
		Preauth preauth =null;
		if(null != preauthdto && null != preauthdto.getKey()){
			preauth = preauthService.getPreauthById(preauthdto.getKey());
		}
		preMedicalService.updateCancelRemarks(preauth,preauthdto.getAutoAllocCancelRemarks(),preauthdto);
	}
}
