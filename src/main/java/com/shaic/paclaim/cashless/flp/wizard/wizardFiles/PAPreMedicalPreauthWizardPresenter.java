package com.shaic.paclaim.cashless.flp.wizard.wizardFiles;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
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
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.paclaim.cashless.flp.wizard.pages.PAPreMedicalPreauthMedicalProcessingPage;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;

@ViewInterface(PAPreMedicalPreauthWizard.class)
public class PAPreMedicalPreauthWizardPresenter extends
		AbstractMVPPresenter<PAPreMedicalPreauthWizard> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8755523946698577719L;

	@EJB
	private ClaimService claimService;

	@EJB
	private MasterService masterService;

	@EJB
	private PreauthService preauthService;
	
	@EJB
	private PEDValidationService pedValidationService;

	@EJB
	private PreMedicalService preMedicalService;
	
	@EJB
	private HospitalService hospitalService;

	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private PolicyService policyService;

	@Inject
	private PAPreMedicalPreauthMedicalProcessingPage preMedicalPreauthMedicalProcessingPage;
	
	
	@EJB
	private ReimbursementService reimbursementService;

	public static final String PREMEDICAL_STEP_CHANGE_EVENT = "PA_premedical_step_change_event";
	public static final String PREMEDICAL_SAVE_EVENT = "PA_premedical_save_event";
	public static final String PREMEDICAL_SUBMITTED_EVENT = "PA_premedical_submit_event";
	public static final String SETUP_REFERENCE_DATA = "PA_premedical_set_edit_data";
	public static final String PREMEDICAL_TREATMENT_TYPE_CHANGED = "PA_premedical_treatment_type_changed";
	public static final String PREMEDICAL_PATIENT_STATUS_CHANGED = "PA_premedical_patient_status_changed";
	public static final String PREMEDICAL_RELAPSE_OF_ILLNESS_CHANGED = "PA_premedical_relapse_of_illness_changed";

	public static final String PREMEDICAL_QUERY_EVENT = "PA_premedical_query_click_event";
	public static final String PREMEDICAL_SUGGEST_REJECTION_EVENT = "PA_premedical_rejection_click_event";
	public static final String PREMEDICAL_SEND_FOR_PROCESSING_EVENT = "PA_premedical_send_for_processing_click_event";

	public static final String PREAUTH_REFERCOORDINATOR_EVENT = "PA_premedical_refer_coordinator_event";

	public static final String PREMEDICAL_PREVIOUS_CLAIM_DETAILS = "PA_premedical_previous_claim_details";

	public static final String PREMEDICAL_QUERY_BUTTON_EVENT = "PA_premedical_query_button_event";

	public static final String MASTER_SEARCH_STATE = "PA_master_search_stae";

	public static final String CHECK_CRITICAL_ILLNESS = "PA_check_critical_illness";

	public static final String GET_ICD_BLOCK = "PA_get_icd_block";

	public static final String GET_ICD_CODE = "PA_get_icd_code";

	public static final String GET_EXCLUSION_DETAILS = "PA_get_exclusion_details";

	public static final String NO_OF_DAYS_RULE = "PA_no_of_days_rule";

	public static final String PREMEDICAL_GET_PACKAGE_RATE = "PA_premedical_get_package_rate_by_procedure_key_and_hospital_key";

	public static final String SET_DB_DETAILS_TO_REFERENCE_DATA = "PA_premedical_set_claim_and_sublimit_db_details";
	
	public static final String SUBLIMIT_CHANGED_BY_SECTION = "PA_Set sublimit values by selecting section";
	
	public static final String PREMEDICAL_COORDINATOR_REQUEST_EVENT = "PA_premedical_coordinator_request_event";
	
	public static final String GET_HOSPITALIZATION_DETAILS = "PA_premedical_get_hospitalization_details";
	
	public static final String ADD_CUSTOM_DIAGNOSIS = "PA_add_custom_diagnosis";
	
	public static final String GET_SEC_COVER = "PA_pre_medical_pre_auth_get_sec_cover";
	
	public static final String GET_SUB_COVER = "PA_pre_medical_pre_auth_get_sub_cover";

	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	
	public static final String VALIDATE_PREMEDICAL_PREAUTH_USER_RRC_REQUEST = "PA_validate_premedical_preauth_user_rrc_request";
	
	public static final String PRE_MEDICAL_PRE_AUTH_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "PA_pre_medical_pre_auth_load_rrc_request_drop_down_values";
	
	public static final String PRE_MEDICAL_PRE_AUTH_SAVE_RRC_REQUEST_VALUES = "PA_pre_medical_pre_auth_save_rrc_request_values";

	public static final String PREMEDICAL_HOSPITALISATION_DUE_TO = "PA_pre_medical_hospitalization_due_to";

	public static final String PRE_MEDICAL_REPORTED_TO_POLICE = "PA_premedical_preauth_reported_to_police";

	public static final String PA_PRE_MEDICAL_PRE_AUTH_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "pa_pre_medical_pre_auth_load_rrc_request_sub_category_values";

	public static final String PA_PRE_MEDICAL_PRE_AUTH_LOAD_RRC_REQUEST_SOURCE_VALUES = "pa_pre_medical_pre_auth_load_rrc_request_source_values";

	
	public void addCustomDiagnosis(
			@Observes @CDIEvent(ADD_CUSTOM_DIAGNOSIS) final ParameterDTO parameters  ) {
//				String strDiag = (String)parameters.getPrimaryParameter();
				ComboBox cmbBox = (ComboBox) parameters.getPrimaryParameter();
				if(!((cmbBox.getValue() instanceof Long) || (cmbBox.getValue() instanceof SelectValue)))
				{
				String strDiag =  (String)cmbBox.getValue();
				if(null != strDiag && !("").equalsIgnoreCase(strDiag))
				{
					Boolean diagBoolean = masterService.getByDiagnosisNames(strDiag);
					if(!diagBoolean)
					{
						//Long key = preMedicalService.saveMasterDiagnosis(strDiag);
					//	SelectValue diagSelectValue = new SelectValue();
						SelectValue diagSelectValue = preMedicalService.saveMasterDiagnosis(strDiag);
						view.setCustomDiagValueToContainer(diagSelectValue,cmbBox);
					}
				}
				}
		}
	
	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_PREMEDICAL_PREAUTH_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	

	public void saveRRCRequestValues(@Observes @CDIEvent(PRE_MEDICAL_PRE_AUTH_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_CASHLESS);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(PRE_MEDICAL_PRE_AUTH_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	/**
	 * Added for RRC Ends
	 * */
	

	public void coordinatorRequestEvent(
		@Observes @CDIEvent(PREMEDICAL_COORDINATOR_REQUEST_EVENT) final ParameterDTO parameters) {
			view.intiateCoordinatorRequest();
	}
	
	public void activeStepChanged(
			@Observes @CDIEvent(PREMEDICAL_STEP_CHANGE_EVENT) final ParameterDTO parameters) {
		view.setWizardPageReferenceData(referenceData);
	}

	public void saveWizard(
			@Observes @CDIEvent(PREMEDICAL_SAVE_EVENT) final ParameterDTO parameters) {

	}

	@SuppressWarnings("static-access")
	public void submitWizard(
			@Observes @CDIEvent(PREMEDICAL_SUBMITTED_EVENT) final ParameterDTO parameters) {
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		if(!preauthDTO.getIsRepremedical()) {
			Preauth submitPreMedical = preMedicalService.submitPreMedical(preauthDTO, true);
			preMedicalService.setDBWorkFlowObject(submitPreMedical,preauthDTO,true);
		} else {
			Preauth updatePreMedical = preMedicalService.updatePreMedical(preauthDTO, true);
			preMedicalService.setDBWorkFlowObject(updatePreMedical,preauthDTO,true);
		}
		

		view.buildSuccessLayout();
	}

	public void checkCriticalIllness(
			@Observes @CDIEvent(CHECK_CRITICAL_ILLNESS) final ParameterDTO parameters) {
		Boolean checkValue = (Boolean) parameters.getPrimaryParameter();
		view.editSpecifyVisibility(checkValue);
	}

	public void setUpReferenceObjectForEdit(
			@Observes @CDIEvent(SETUP_REFERENCE_DATA) final ParameterDTO parameters) {
		PreauthDTO bean = (PreauthDTO) parameters.getPrimaryParameter();
		setReferece(bean);
		if(bean != null) {
			
			referenceData.put("sectionDetails", masterService
					.getSectionList(bean.getNewIntimationDTO().getPolicy().getProduct().getKey(),bean.getNewIntimationDTO().getPolicy()));
		}
		
		view.setWizardPageReferenceData(referenceData);
	}

	public void setReferece(PreauthDTO bean) {
		referenceData.put("treatmentType", masterService
				.getSelectValueContainer(ReferenceTable.TREATMENT_MANAGEMENT));
		referenceData.put("roomCategory", masterService
				.getSelectValueContainer(ReferenceTable.ROOM_CATEGORY));
		referenceData.put("natureOfTreatment", masterService
				.getSelectValueContainer(ReferenceTable.NATURE_OF_TREATMENT));
		referenceData.put("diagnosisName", masterService.getDiagnosisList());
		referenceData.put("sumInsured", masterService
				.getSelectValueContainer(ReferenceTable.SUM_INSURED));
		referenceData.put("commonValues", masterService
				.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData
				.put("coordinatorTypeRequest",
						masterService
								.getSelectValueContainer(ReferenceTable.COORDINATOR_REQUEST_TYPE));
		referenceData.put("patientStatus", masterService
				.getSelectValueContainer(ReferenceTable.PATIENT_STATUS));
		referenceData.put("icdChapter",
				masterService.getSelectValuesForICDChapter());
		// referenceData.put("icdBlock",
		// masterService.getSelectValuesForICDBlock());
		// referenceData.put("icdCode",
		// masterService.getSelectValuesForICDCode());
		referenceData.put("procedureName",
				preauthService.getProcedureListNames());
		referenceData.put("procedureCode", preauthService.getProcedureListNames());
		referenceData.put("medicalSpeciality", preauthService.getSpecialityType("M"));
		referenceData.put("surgicalSpeciality", preauthService.getSpecialityType("S"));
		referenceData.put("sublimitApplicable", masterService
				.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("considerForPayment", masterService
				.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("considerForDayCare", masterService
				.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("commonValues", masterService
				.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("pedExclusionImpactOnDiagnosis", masterService
				.getSelectValueContainer(ReferenceTable.IMPACT_ON_DIAGNOSI));
		referenceData.put("exclusionDetails", masterService
				.getSelectValueContainer(ReferenceTable.EXCLUSION_DETAILS));
		referenceData.put("procedureStatus", masterService
				.getSelectValueContainer(ReferenceTable.PROCEDURE_STATUS));
		//referenceData.put("pedList", masterService.getInsuredPedDetails());
		referenceData.put("rejectionCategory", masterService.getRejectionCategoryByValue());
//											   masterService.getSelectValueContainer(ReferenceTable.REJECTION_CATEGORY));
		referenceData.put("sendForProcessingCategory", masterService.getRejectionCategoryByValue());
//														masterService.getSelectValueContainer(ReferenceTable.REJECTION_CATEGORY));
		referenceData.put("terminateCover", masterService
				.getSelectValueContainer(ReferenceTable.TERMINATE_COVER));
		referenceData.put("criticalIllness",
				preauthService.getCriticalIllenssMasterValues(bean));
		referenceData.put("illness",
				masterService.getSelectValueContainer(ReferenceTable.ILLNESS));
		referenceData.put("sumInsuredRestriction", masterService
				.getSelectValueContainer(ReferenceTable.SUM_INSURED));
		referenceData.put("medicalCategory", masterService
				.getSelectValueContainer(ReferenceTable.MEDICAL_CATEGORY));
		
		referenceData.put("hospitalisationDueTo", masterService.getSelectValueContainer(ReferenceTable.HOSPITALIZATION_DUE_TO));
		referenceData.put("causeOfInjury", masterService.getSelectValueContainer(ReferenceTable.CAUSE_OF_INJURY));
		
		referenceData.put("typeOfDelivery", masterService
				.getSelectValueContainer(ReferenceTable.DELIVERY_TYPE));
		
		referenceData.put("natureOfLoss", masterService
				.getNatureCauseLossSelectValueContainer(SHAConstants.PA_LOB, ReferenceTable.NATURE_OF_LOSS));
		referenceData.put("causeOfLoss", masterService
				.getNatureCauseLossSelectValueContainer(SHAConstants.PA_LOB, ReferenceTable.CAUSE_OF_LOSS));
		referenceData.put("catastrophicLoss", masterService.getCatastrophicLossList());
		
		// referenceData.put("relapsedClaims", relapseClaimContainer);

		// CustomLazyContainer customLazyContainer = new CustomLazyContainer(3,
		// "value", masterService, "diagnosis");
		// customLazyContainer.addContainerProperty("value", SelectValue.class,
		// null);
		// referenceData.put("diagnosis", customLazyContainer);
		
		
		
	}

	public void generateFieldsBasedOnTreatement(
			@Observes @CDIEvent(PREMEDICAL_TREATMENT_TYPE_CHANGED) final ParameterDTO parameters) {
		view.generateFieldsBasedOnTreatment();
	}

	public void generateFieldsBasedOnPatientStatus(
			@Observes @CDIEvent(PREMEDICAL_PATIENT_STATUS_CHANGED) final ParameterDTO parameters) {
		view.genertateFieldsBasedOnPatientStaus();
	}

	public void generateFieldsBasedOnRelapseIllness(
			@Observes @CDIEvent(PREMEDICAL_RELAPSE_OF_ILLNESS_CHANGED) final ParameterDTO parameters) {
		PreauthDTO preauthDto = (PreauthDTO) parameters.getPrimaryParameter();
		SelectValue relpaseofIllness = parameters.getSecondaryParameters() != null ? (SelectValue) parameters
				.getSecondaryParameters()[0] : null;

		if (relpaseofIllness != null
				&& relpaseofIllness.getId().equals(ReferenceTable.COMMONMASTER_YES)) {
			BeanItemContainer<SelectValue> relapseClaimContainer = claimService
					.getRelpseClaimsForPolicy(preauthDto.getPolicyDto()
							.getPolicyNumber(), preauthDto.getClaimNumber());
			referenceData.put("relapsedClaims", relapseClaimContainer);
		}
		view.genertateFieldsBasedOnRelapseOfIllness(referenceData);
	}

	public void generatePreAuthReferCoOrdinatorLayout(
			@Observes @CDIEvent(PREAUTH_REFERCOORDINATOR_EVENT) final ParameterDTO parameters) {
		view.generateReferCoOrdinatorLayout();
	}

	public void generateFieldsForQuery(
			@Observes @CDIEvent(PREMEDICAL_QUERY_EVENT) final ParameterDTO parameters) {
		view.generateFieldsOnQueryClick();
	}

	public void generateFieldsForSuggestRejection(
			@Observes @CDIEvent(PREMEDICAL_SUGGEST_REJECTION_EVENT) final ParameterDTO parameters) {
		view.generateFieldsOnSuggestRejectionClick();
	}

	public void generateFieldsForSendForProcessing(
			@Observes @CDIEvent(PREMEDICAL_SEND_FOR_PROCESSING_EVENT) final ParameterDTO parameters) {
		view.generateFieldsOnSendForProcessingClick();
	}

	public void searchState(
			@Observes @CDIEvent(MASTER_SEARCH_STATE) final ParameterDTO parameters) {
		String q = (String) parameters.getPrimaryParameter();
		List<State> stateSearch = masterService.stateSearch(q);
		view.searchState(stateSearch);
	}

	public void getIcdBlock(
			@Observes @CDIEvent(GET_ICD_BLOCK) final ParameterDTO parameters) {
		Long chapterKey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue> icdBlockContainer = masterService
				.searchIcdBlockByChapterKey(chapterKey);

		view.setIcdBlock(icdBlockContainer);
	}

	public void getIcdCode(
			@Observes @CDIEvent(GET_ICD_CODE) final ParameterDTO parameters) {
		Long blockKey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue> icdCodeContainer = masterService
				.searchIcdCodeByBlockKey(blockKey);

		view.setIcdCode(icdCodeContainer);
	}

	public void getExclusionDetails(
			@Observes @CDIEvent(GET_EXCLUSION_DETAILS) final ParameterDTO parameters) {
		Long impactDiagnosisKey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<ExclusionDetails> icdCodeContainer = masterService
				.getExclusionDetailsByImpactKey(impactDiagnosisKey);

		view.setExclusionDetails(icdCodeContainer);
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
	
	public void getPreviousClaimDetails(
			@Observes @CDIEvent(PREMEDICAL_PREVIOUS_CLAIM_DETAILS) final ParameterDTO parameters) {
		PreauthDTO preauthDto = (PreauthDTO) parameters.getPrimaryParameter();
		if (preauthDto != null
				&& preauthDto.getNewIntimationDTO() != null
				&& preauthDto.getNewIntimationDTO().getInsuredPatient() != null
				&& preauthDto.getNewIntimationDTO().getInsuredPatient()
						.getInsuredId() != null) {
			Long policyNumberOrInsuredId = preauthDto.getNewIntimationDTO()
					.getInsuredPatient().getInsuredId();
			/*
			 * String policyNumberOrInsuredId = (String) parameters
			 * .getPrimaryParameter();
			 */
//			List<ViewTmpClaim> claimList = claimService
//					.getTmpClaimsByInsuredId(policyNumberOrInsuredId);
			
			
			
			System.out.println("--the current claim id---"
					+ preauthDto.getClaimDTO().getClaimId());
			
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
			
			/*List<PreviousClaimsTableDTO> previousClaimDTOList = preauthService
					.getPreviousClaims(claimList,
							preauthDto.getClaimDTO()
						.getClaimId());  */ 
			
			DBCalculationService dbCalculationService = new DBCalculationService();
			List<PreviousClaimsTableDTO> previousClaimDTOList = dbCalculationService.getPreviousClaims(preauthDto.getClaimDTO().getKey(), preauthDto.getNewIntimationDTO().getPolicy().getKey(), 
					preauthDto.getNewIntimationDTO().getInsuredPatient().getKey(), SHAConstants.INSURED_WISE_SEARCH_TYPE);
			
			for(PreviousClaimsTableDTO previousClaimsTableDTO : previousClaimDTOList){
				if(previousClaimsTableDTO.getAdmissionDate()!=null && !previousClaimsTableDTO.getAdmissionDate().equals("")){
//					Date tempDate = SHAUtils.formatTimestamp(previousClaimsTableDTO.getAdmissionDate());
//					previousClaimsTableDTO.setAdmissionDate((SHAUtils.formatDate(tempDate)));
				}												
			}
			// List<PreviousClaimsTableDTO> previousClaimDTOList = new
			// PreviousClaimMapper()
			// .getPreviousClaimDTOList(claimList);
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

	public void getPackageRate(
			@Observes @CDIEvent(PREMEDICAL_GET_PACKAGE_RATE) final ParameterDTO parameters) {
		
		Long procedureKey = (Long) parameters.getPrimaryParameter();
//		String ProcedureCode = (String) parameters.getSecondaryParameters()[0];
		String hosptialCode = (String) parameters.getSecondaryParameters()[0];
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getSecondaryParameters()[1];
		
		ProcedureMaster procedureMaster = masterService
				.getProcedureByKey(procedureKey);
		
		String procedureCode = procedureMaster.getProcedureCode();
		
		Long roomCategoryId = 0l;
		
		SelectValue roomCategory = preauthDTO.getNewIntimationDTO().getRoomCategory();
		if(roomCategory != null){
			roomCategoryId = roomCategory.getId();
		}

		System.out.println("--the procedure key and hospital key----"
				+ procedureCode + "===the hospital key=====" + hosptialCode);

		List<HospitalPackage> packageMaster = masterService
				.getPackageRateByProcedureAndHospitalKey(procedureCode,
						hosptialCode,roomCategoryId);
		

		Map<String, String> mappedValues = new HashMap<String, String>();
		mappedValues.put("packageRate",
				packageMaster != null ? !packageMaster.isEmpty() ? packageMaster.get(0).getRate()
						.toString() : null : null);
		mappedValues.put("dayCareProcedure", procedureMaster.getDayCareFlag()
				.equalsIgnoreCase("y") ? "Yes" : "No");
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

	public void setDBDetailsToReferenceData(
			@Observes @CDIEvent(SET_DB_DETAILS_TO_REFERENCE_DATA) final ParameterDTO parameters) {
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
		if (sumInsured.equals(0)) {
			sumInsured = policyDTO.getTotalSumInsured();
		}
//		referenceData.put(
//				"policyAgeing",
//				dbCalculationService.getPolicyAgeing(
//						policyDTO.getAdmissionDate(),
//						policyDTO.getPolicyNumber()));
		
		referenceData.put("policyAgeing",preauthDTO.getNewIntimationDTO().getPolicyYear() != null ? preauthDTO.getNewIntimationDTO().getPolicyYear() : "");
		/*referenceData.put("sublimitDBDetails", dbCalculationService
				.getClaimedAmountDetails(policyDTO.getProduct().getKey(),
						sumInsured, Integer.valueOf(insuredAge)));*/
		
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
		
		 sumInsured = dbCalculationService.getInsuredSumInsured(preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), preauthDTO.getPolicyDto().getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
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
	
		 sumInsured = dbCalculationService.getInsuredSumInsured(preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), preauthDTO.getPolicyDto().getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
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
	
	public void generateFieldsBasedOnHospitalisationDueTo(@Observes @CDIEvent(PREMEDICAL_HOSPITALISATION_DUE_TO) final ParameterDTO parameters)
	{
		SelectValue selectedValue = (SelectValue) parameters.getPrimaryParameter();
		view.genertateFieldsBasedOnHospitalisionDueTo(selectedValue);
	}
	
	public void generateFieldsBasedOnReportedPolice(@Observes @CDIEvent(PRE_MEDICAL_REPORTED_TO_POLICE) final ParameterDTO parameters)
	{
		Boolean selectedValue = (Boolean) parameters.getPrimaryParameter();
		view.genertateFieldsOnReportedPolice(selectedValue);
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}
	
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(PA_PRE_MEDICAL_PRE_AUTH_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(PA_PRE_MEDICAL_PRE_AUTH_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}

}
