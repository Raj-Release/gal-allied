/**
 * 
 */
package com.shaic.paclaim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction;

/**
 * @author ntv.vijayar
 *
 */


import java.util.ArrayList;
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
import com.shaic.claim.enhacement.table.PreviousPreAuthService;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Investigation;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PEDValidationService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementQueryService;
import com.shaic.domain.preauth.HospitalPackage;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Procedure;
import com.shaic.domain.preauth.ProcedureMaster;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.domain.service.PreMedicalService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.paclaim.reimbursement.service.PAReimbursementService;
import com.shaic.paclaim.rod.wizard.dto.PABenefitsDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.TextField;

@ViewInterface(PAClaimRequestDataExtractionPageInterface.class)
public class PAClaimRequestDataExtractionPagePresenter extends AbstractMVPPresenter<PAClaimRequestDataExtractionPageInterface>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3134981837447411741L;

	@EJB
	private MasterService masterService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private PreMedicalService preMedicalService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private PEDValidationService pedValidationService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private ReimbursementQueryService reimbursementQueryService;
	
	@EJB
	private PreviousPreAuthService previousPreauthService;

	@EJB
	private InvestigationService investigationService;
	
	@EJB
	private CreateRODService rodService;
	
	@EJB
	private PAReimbursementService paReimbService;
	

	public static final String MEDICAL_APPROVAL_SETUP_REFERNCE = "pa_medical_approval_claim_request_setup_reference";
	
	public static final String CHECK_CRITICAL_ILLNESS = "pa_medical_approval_claim_request_check_critical_illness";
	
	public static final String MEDICAL_APPROVAL_TREATMENT_TYPE_CHANGED = "pa_medical_approval_claim_request_treatment_type_changed";
	
	public static final String MEDICAL_APPROVAL_PATIENT_STATUS_CHANGED = "pa_medical_approval_claim_request_patient_status_changed";
	
	public static final String MEDICAL_APPROVAL_HOSPITALISATION_DUE_TO = "pa_medical_approval_claim_request_hospitalisation_due_to";
	
	public static final String MEDICAL_APPROVAL_GENERATE_OTHER_CLAIMS = "pa_medical_approval_claim_request_generate_other_claims";
	
	public static final String CLAIM_REQUEST_COORDINATOR = "PA_Claim Request coordinator events";
	
	public static final String SUBLIMIT_CHANGED_BY_SECTION = "PA_Set sublimit values by selecting section for claim request";
	
	public static final String GET_ICD_BLOCK = "pa_medical_approval_claim_request_get_icd_block";
	public static final String GET_ICD_CODE = "pa_medical_approval_claim_request_get_icd_code";
	
	public static final String GET_PACKAGE_RATE = "pa_medical_approval_claim_request_get_package_rate";
	public static final String MEDICAL_APPROVAL_REPORTED_TO_POLICE = "pa_medical_approval_claim_request_reported_to_police";
	public static final String COMPARE_WITH_PREVIOUS_ROD = "pa_claim_request_compare_with_previous_rod";

	public static final String GENERATE_DOMICILLARY_FIELDS = "pa_claim_request_generate_domicillary_fields";
	
	
	public static final String CLAIM_REQUEST_APPROVE_EVENT = "pa_claim_request_apporve_event";
	
	public static final String CLAIM_CANCEL_ROD_EVENT      =  "pa_claim_request_cancel_rod";            

	public static final String CLAIM_REQUEST_QUERY_BUTTON_EVENT = "pa_claim_request_query_button_event";
	
	public static final String CLAIM_REQUEST_REJECTION_EVENT = "pa_claim_request_rejection_button_event";
	
	public static final String CLAIM_REQUEST_ESCALATE_EVENT = "pa_claim_request_escalte_button_event";
	
	public static final String CLAIM_REQUEST_ESCALATE_REPLY_EVENT = "pa_claim_request_escalte_reply_event";
	
	public static final String CLAIM_REQUEST_REFERCOORDINATOR_EVENT = "pa_claim_request_refer_to_coordinator_event";
	
	public static final String CLAIM_REQUEST_FIELD_VISIT_EVENT = "pa_claim_request_field_visit_event";
	
	public static final String CLAIM_REQUEST_INVESTIGATION_EVENT = "pa_claim_request_investigation_event";
	
	public static final String CLAIM_REQUEST_SPECIALIST_EVENT = "pa_claim_request_specialist_event";

	public static final String CLAIM_REQUEST_SENT_TO_REPLY_EVENT = "pa_claim_request_sent_to_reply_event";
	
	public static final String CLAIM_REQUSET_SUM_INSURED_CALCULATION = "pa_claim_request_sum_insured_calculation";
	
	public static final String CHECK_INVESTIGATION_INITIATED = "pa_claim_request_check_investigation_initiated";
	
	public static final String PA_BENEFITS_TABLE_VALUES = "pa_benefits_table_values";

	public static final String PA_BENEFITS_TABLE_DATA = "pa_benefits_table_data";
	
	public static final String PA_LOAD_BILL_DETAILS_VALUES = "pa_load_bill_details_values";
	
	public static final String PA_SAVE_BILL_DETAILS_VALUES = "pa_load_bill_details_values";
	
	public static final String PA_BILL_ENTRY_TABLE_CATEGORY_DROPDOWN_VALUE = "pa_claim_request_bill_entry_category_dropdown_values";
	
	public static final String PA_VALIDATE_BENEFITS = "pa_validate_benefits";
	
	public static final String PA_VALIDATE_COVERS = "pa_validate_covers";
	
	public static final String PA_VALIDATE_OPTIONAL_COVERS = "pa_validate_optional_covers";
	
	
//	public static final String CLAIM_REQUSET_SUM_INSURED_CALCULATION = "pa_claim_request_sum_insured_calculation";
	
	
	Map<String, Object> referenceData = new HashMap<String, Object>();
	public void setUpReference(
			@Observes @CDIEvent(MEDICAL_APPROVAL_SETUP_REFERNCE) final ParameterDTO parameters) {
		
		PreauthDTO dto = (PreauthDTO) parameters.getPrimaryParameter();
		
		PolicyDto policyDTO = dto.getPolicyDto();
		
		
		referenceData.put("treatmentType", masterService.getSelectValueContainer(ReferenceTable.TREATMENT_MANAGEMENT));
		referenceData.put("roomCategory", masterService.getSelectValueContainer(ReferenceTable.ROOM_CATEGORY));
		referenceData.put("natureOfTreatment", masterService.getSelectValueContainer(ReferenceTable.NATURE_OF_TREATMENT));
		referenceData.put("diagnosisName", masterService.getDiagnosisList());
		referenceData.put("sumInsured", masterService.getSelectValueContainer(ReferenceTable.SUM_INSURED));
		referenceData.put("commonValues", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("coordinatorTypeRequest", masterService.getSelectValueContainer(ReferenceTable.COORDINATOR_REQUEST_TYPE));
		referenceData.put("patientStatus", masterService.getSelectValueContainer(ReferenceTable.REIMBURSEMENT_PATIENT_STATUS));
		referenceData.put("icdChapter", masterService.getSelectValuesForICDChapter());
//		referenceData.put("icdBlock", masterService.getSelectValuesForICDBlock());
//		referenceData.put("icdCode", masterService.getSelectValuesForICDCode());
		referenceData.put("illness", masterService.getSelectValueContainer(ReferenceTable.ILLNESS));
		
		BeanItemContainer<SelectValue> procedureListNames = preauthService.getProcedureListNames();
		
		referenceData.put("procedureName", procedureListNames);
		referenceData.put("procedureCode", procedureListNames);
		referenceData.put("medicalSpeciality", preauthService.getSpecialityType("M"));
		referenceData.put("surgicalSpeciality",preauthService.getSpecialityType("S"));
		referenceData.put("sublimitApplicable", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("considerForPayment", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("considerForDayCare", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("commonValues", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("hospitalisationDueTo", masterService.getSelectValueContainer(ReferenceTable.HOSPITALIZATION_DUE_TO));
		referenceData.put("causeOfInjury", masterService.getSelectValueContainer(ReferenceTable.CAUSE_OF_INJURY));
		referenceData.put("sumInsuredRestriction",  masterService.getSelectValueContainer(ReferenceTable.SUM_INSURED));
		Double insuredAge = dto.getNewIntimationDTO().getInsuredPatient().getInsuredAge();
		

		Double sumInsured = 0d;
		if(null != dto.getNewIntimationDTO() && null != dto.getNewIntimationDTO().getPolicy() && 
				null != dto.getNewIntimationDTO().getPolicy().getProduct() && 
				null != dto.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
				!(ReferenceTable.getGPAProducts().containsKey(dto.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
		 sumInsured = dbCalculationService.getInsuredSumInsured(dto.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), dto.getPolicyDto().getKey(),dto.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		}
		else
		{
			sumInsured = dbCalculationService.getGPAInsuredSumInsured(dto.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), dto.getPolicyDto().getKey());
		}
		if (sumInsured == 0) {
			sumInsured = dto.getPolicyDto().getTotalSumInsured();
		}
		
		 if(dto.getPreauthDataExtractionDetails().getSection() != null){
				
				referenceData.put("sublimitDBDetails", dbCalculationService
							.getClaimedAmountDetailsForSection(policyDTO.getProduct().getKey(),
									sumInsured, 0l, insuredAge,dto.getPreauthDataExtractionDetails().getSection().getId(),
									dto.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? dto.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0",null,dto.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
					
				}else{
					referenceData.put("sublimitDBDetails", dbCalculationService
							.getClaimedAmountDetailsForSection(policyDTO.getProduct().getKey(),
									sumInsured, 0l, insuredAge,0l,dto.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? dto.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0",null,dto.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
				}
		
//		referenceData.put("sublimitDBDetails", dbCalculationService.getClaimedAmountDetails(dto.getPolicyDto().getProduct().getKey(), sumInsured, insuredAge));
		referenceData.put("insuredPedList", masterService.getInusredPEDList(dto.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString()));
		
		NewIntimationDto intimationDTO = dto.getNewIntimationDTO();
		Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(intimationDTO.getInsuredPatient().getInsuredId().toString(), intimationDTO.getPolicy().getKey(),intimationDTO.getInsuredPatient().getLopFlag());
		Double balanceSI = dbCalculationService.getBalanceSIForReimbursement(intimationDTO.getPolicy().getKey(), intimationDTO.getInsuredPatient().getKey(),dto.getClaimKey(), dto.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
		List<Double> copayValue = dbCalculationService.getProductCoPay(intimationDTO.getPolicy().getProduct().getKey() ,intimationDTO.getInsuredPatient().getKey(), intimationDTO.getInsuredPatient().getInsuredId(),intimationDTO);
		dto.setBalanceSI(SHAUtils.getHospOrPartialAppAmt(dto, reimbursementService, balanceSI));
		dto.setProductCopay(copayValue);
		referenceData.put("policyAgeing", dbCalculationService.getPolicyAgeing(intimationDTO.getAdmissionDate(), intimationDTO.getPolicy().getPolicyNumber()));
		
		referenceData.put("commonValues", masterService
				.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("reasonForReconsiderationRequest", masterService
				.getSelectValueContainer(ReferenceTable.REASON_FOR_RECONSIDERATION));
		referenceData.put("section", masterService
				.getSelectValueContainer(ReferenceTable.SECTION));
		referenceData.put("typeOfDelivery", masterService
				.getSelectValueContainer(ReferenceTable.DELIVERY_TYPE));
		
		if (dto.getClaimKey() != null) {
			List<Investigation> investigationList = investigationService
					.getByInvestigationByClaimKey(dto.getClaimKey());
			if (investigationList != null && investigationList.size() >= 0) {
				dto.setInvestigationSize(investigationList.size());
			} else {
				dto.setInvestigationSize(0);
			}
		}
		//referenceData.put("investigatorName", masterService.getInvestigation());
		referenceData.put("investigatorName", masterService.getInvestigationForPA());
		referenceData.put("commonValues", masterService
				.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("medicalVerification", masterService
				.getSelectValueContainer(ReferenceTable.MEDICAL_VERIFICATION));
		referenceData
				.put("treatmentQualityVerification",
						masterService
								.getSelectValueContainer(ReferenceTable.TREATMENT_QUALITY_VERIFICATION));
		referenceData.put("verified", masterService
				.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("previousPreauth",
				previousPreauthService.search(dto.getClaimKey(),false));
		referenceData.put("status", masterService
				.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData
				.put("fileType",
						masterService
								.getSelectValueContainer(ReferenceTable.ROD_UPLOAD_DOC_TABLE_FILE_TYPE));
		referenceData.put("cancellationReason", masterService.getSelectValueContainer(ReferenceTable.CANCELLATION_REASON));
		
		/*referenceData.put("billClassification",
				masterService.getMasBillClassificationValues());*/
		referenceData.put("billClassification", setClassificationValues(dto));
		referenceData.put("paBenefits", masterService.getSelectValueContainerForBenefits(ReferenceTable.MASTER_TYPE_CODE_BENEFITS,dto.getNewIntimationDTO().getPolicy().getProduct().getKey()));
		referenceData.put("causeOfAccident", masterService.getSelectValueContainer(ReferenceTable.MASTER_TYPE_ACCIDENT_OF_CAUSE));
		referenceData.put("natureOfLoss", masterService
				.getNatureCauseLossSelectValueContainer(SHAConstants.PA_LOB, ReferenceTable.NATURE_OF_LOSS));
		referenceData.put("causeOfLoss", masterService
				.getNatureCauseLossSelectValueContainer(SHAConstants.PA_LOB, ReferenceTable.CAUSE_OF_LOSS));
		referenceData.put("catastrophicLoss", masterService.getCatastrophicLossList());
		referenceData.put("preauthDTO",dto);
		getCoversAndOptionalCoverValues(dto);
		view.setupReferences(referenceData);
		
		
		
	}
	
	public BeanItemContainer<SelectValue> setClassificationValues(PreauthDTO preauthDTO)
	{
		BeanItemContainer<SelectValue> beanContainer = masterService.getMasBillClassificationValues();
		
		List<SelectValue> selectValueList = beanContainer.getItemIds();
		
		List<SelectValue> finalClassificationList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> classificationValueContainer = null;
		if(null != selectValueList && !selectValueList.isEmpty())
		{
			 classificationValueContainer = new BeanItemContainer<SelectValue>(
						SelectValue.class);
			for (SelectValue selectValue : selectValueList) {		
				//if(("Hospitalization").equalsIgnoreCase(selectValue.getValue()) && (null != preauthDTO.getHospitalizaionFlag() && preauthDTO.getHospitalizaionFlag()))
				if(("Hospitalization").equalsIgnoreCase(selectValue.getValue()) && ((null != preauthDTO.getHospitalizaionFlag() && preauthDTO.getHospitalizaionFlag())
						|| (null != preauthDTO.getIsHospitalizationRepeat() && preauthDTO.getIsHospitalizationRepeat()) || (null != preauthDTO.getPartialHospitalizaionFlag() &&  preauthDTO.getPartialHospitalizaionFlag())
						))
				
			/*	if(("Hospitalization").equalsIgnoreCase(selectValue.getValue()) && (null != preauthDTO.getHospitalizaionFlag() && preauthDTO.getHospitalizaionFlag())
						|| (null != preauthDTO.getIsHospitalizationRepeat() && preauthDTO.getIsHospitalizationRepeat())
						)*/
				{
					finalClassificationList.add(selectValue);
				}
				else if(("Pre-Hospitalization").equalsIgnoreCase(selectValue.getValue()) && (null != preauthDTO.getPreHospitalizaionFlag() && preauthDTO.getPreHospitalizaionFlag()))
				{
					finalClassificationList.add(selectValue);
				}
				else if(("Post-Hospitalization").equalsIgnoreCase(selectValue.getValue()) && (null != preauthDTO.getPostHospitalizaionFlag() && preauthDTO.getPostHospitalizaionFlag()))
				{
					finalClassificationList.add(selectValue);
				}
			}
			classificationValueContainer.addAll(finalClassificationList);
		}
		return classificationValueContainer;
	}
	
	
	public void setSublimitValues(
			@Observes @CDIEvent(SUBLIMIT_CHANGED_BY_SECTION) final ParameterDTO parameters) {
		
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
	
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
								preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0",null,preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
				
			}else{
				referenceData.put("sublimitDBDetails", dbCalculationService
						.getClaimedAmountDetailsForSection(policyDTO.getProduct().getKey(),
								sumInsured, 0l, insuredAge,0l,preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0",null,preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
			}
		
//		view.setWizardPageReferenceData(referenceData);
		
	}
	
	public void generateFieldsBasedOnReportedPolice(@Observes @CDIEvent(MEDICAL_APPROVAL_REPORTED_TO_POLICE) final ParameterDTO parameters)
	{
		Boolean selectedValue = (Boolean) parameters.getPrimaryParameter();
		view.generateFieldsBasedOnReportedPolice(selectedValue);
	}
	
	public void checkCriticalIllness(
			@Observes @CDIEvent(CHECK_CRITICAL_ILLNESS) final ParameterDTO parameters) {
		Boolean checkValue = (Boolean) parameters.getPrimaryParameter();
		view.editSpecifyVisibility(checkValue);
	}
	
	public void generateFieldsBasedOnTreatement(@Observes @CDIEvent(MEDICAL_APPROVAL_TREATMENT_TYPE_CHANGED) final ParameterDTO parameters)
	{
		view.generateFieldsBasedOnTreatment();
	}
	
	public void coordinatorEvents(@Observes @CDIEvent(CLAIM_REQUEST_COORDINATOR) final ParameterDTO parameters)
	{
		view.intiateCoordinatorRequest();
	}
	
	public void generateFieldsBasedOnPatientStatus(@Observes @CDIEvent(MEDICAL_APPROVAL_PATIENT_STATUS_CHANGED) final ParameterDTO parameters)
	{
		view.genertateFieldsBasedOnPatientStaus();
	}
	
	public void generateFieldsBasedOnHospitalisationDueTo(@Observes @CDIEvent(MEDICAL_APPROVAL_HOSPITALISATION_DUE_TO) final ParameterDTO parameters)
	{
		SelectValue selectedValue = (SelectValue) parameters.getPrimaryParameter();
		view.genertateFieldsBasedOnHospitalisionDueTo(selectedValue);
	}
	
	public void generateFieldsBasedOnOtherCoveredClaim(@Observes @CDIEvent(MEDICAL_APPROVAL_GENERATE_OTHER_CLAIMS) final ParameterDTO parameters)
	{
		Boolean selectedValue = (Boolean) parameters.getPrimaryParameter();
		view.generateFieldsBasedOnOtherCoveredClaim(selectedValue);
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
	
	public void getPackageRate(@Observes @CDIEvent(GET_PACKAGE_RATE) final ParameterDTO parameters)
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
	
	public void compareWithPreviousROD(@Observes @CDIEvent(COMPARE_WITH_PREVIOUS_ROD) final ParameterDTO parameters) {
		PreauthDTO bean = (PreauthDTO) parameters.getPrimaryParameter();
		Reimbursement previousLatestROD = reimbursementService.getFilteredPreviousLatestROD(bean.getClaimKey(), bean.getKey());
		String comparisonResult = "";
		if(previousLatestROD != null) {
			
			List<PedValidation> findPedValidationByPreauthKey = preauthService.findPedValidationByPreauthKey(previousLatestROD.getKey());
			List<Procedure> findProcedureByPreauthKey = preauthService.findProcedureByPreauthKey(previousLatestROD.getKey());
//			if(!previousLatestROD.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase(docAckknowledgement.getHospitalisationFlag())) {
//				
//			} else if(!previousLatestROD.getDocAcknowLedgement().getPreHospitalisationFlag().equalsIgnoreCase(docAckknowledgement.getPreHospitalisationFlag())) {
//				
//			} else if(!previousLatestROD.getDocAcknowLedgement().getPostHospitalisationFlag().equalsIgnoreCase(docAckknowledgement.getPostHospitalisationFlag())) {
//				
//			} else if(!previousLatestROD.getDocAcknowLedgement().getPartialHospitalisationFlag().equalsIgnoreCase(docAckknowledgement.getPartialHospitalisationFlag())) {
//				
//			}
			
			if(previousLatestROD.getDateOfAdmission() != null && bean.getPreauthDataExtractionDetails().getAdmissionDate() != null && !previousLatestROD.getDateOfAdmission().equals(bean.getPreauthDataExtractionDetails().getAdmissionDate())) {
				comparisonResult += "Previous DOA : " + SHAUtils.formatDate(previousLatestROD.getDateOfAdmission()) + "   Current DOA : " + SHAUtils.formatDate(bean.getPreauthDataExtractionDetails().getAdmissionDate()) + "</br>";
			}
			
			if(previousLatestROD.getDateOfDischarge() != null && bean.getPreauthDataExtractionDetails().getDischargeDate() != null && !previousLatestROD.getDateOfDischarge().equals(bean.getPreauthDataExtractionDetails().getDischargeDate())) {
				comparisonResult += "Previous DOD : " + SHAUtils.formatDate(previousLatestROD.getDateOfDischarge()) + "    Current DOD : " + SHAUtils.formatDate(bean.getPreauthDataExtractionDetails().getDischargeDate());
			}
			
			if(previousLatestROD.getTreatmentType() != null && bean.getPreauthDataExtractionDetails().getTreatmentType() != null && !previousLatestROD.getTreatmentType().getKey().equals(bean.getPreauthDataExtractionDetails().getTreatmentType().getId())) {
				comparisonResult += "Previous Treatment Type : " + (previousLatestROD.getTreatmentType().getKey().equals(ReferenceTable.SURGICAL_CODE) ? "Surgical" : "Medical") + "    Current Treatment Type : " + (bean.getPreauthDataExtractionDetails().getTreatmentType().getId().equals(ReferenceTable.SURGICAL_CODE) ? "Surgical" : "Medical");
			}
			List<ProcedureDTO> procedureExclusionCheckTableList = bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList();
			List<DiagnosisDetailsTableDTO> diagnosisTableList = bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
			String procedureVariation = "";
			String diagnosisVariation = "";
			String addedDiag = "";
			String addedProc = "";
			for (Procedure procedure : findProcedureByPreauthKey) {
				Boolean isExisting = false;
				for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
					if(procedureDTO.getNewProcedureFlag() != null && procedure.getNewProcedureFlag() != null && procedure.getNewProcedureFlag().equals(procedureDTO.getNewProcedureFlag()) && procedureDTO.getProcedureName() != null && procedure.getProcedureID() != null && procedureDTO.getProcedureName().getId().equals(procedure.getProcedureID())) {
						isExisting = true;
						break;
					} 
				}
				if(!isExisting) {
					procedureVariation += procedure.getProcedureName() + " ,";
				}
			}
			
			for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
				Boolean isExisting = false;
				Long procedureKey = 0l;
				for (Procedure procedure : findProcedureByPreauthKey) {
					if(procedureDTO.getNewProcedureFlag() != null && procedure.getNewProcedureFlag() != null && procedure.getNewProcedureFlag().equals(procedureDTO.getNewProcedureFlag()) && procedureDTO.getProcedureName() != null && procedure.getProcedureID() != null && procedureDTO.getProcedureName().getId().equals(procedure.getProcedureID())) {
						isExisting = true;
						procedureKey = procedure.getKey();
						break;
					} 
				}
				if(!isExisting && (procedureDTO.getRecTypeFlag() == null || (procedureDTO.getRecTypeFlag() != null && procedureDTO.getRecTypeFlag().equalsIgnoreCase("A")))) {
					addedProc += procedureDTO.getProcedureName().getValue() + " ,";
				}
			}
			
			for (PedValidation pedValidation : findPedValidationByPreauthKey) {
				Boolean isExisting = false;
				for (DiagnosisDetailsTableDTO diagnosisDTO : diagnosisTableList) {
					if(diagnosisDTO.getDiagnosisName() != null && diagnosisDTO.getDiagnosisName().getId().equals(pedValidation.getDiagnosisId())) {
						isExisting = true;
						break;
					} 
				}
				if(!isExisting) {
					String diagnosisByKey = preauthService.getDiagnosisByKey(pedValidation.getDiagnosisId());
					diagnosisVariation += diagnosisByKey + " ,";
				}
			}
			
			for (DiagnosisDetailsTableDTO diagnosisDTO : diagnosisTableList) {
				Boolean isExisting = false;
				Long pedValidationKey = 0l;
				for (PedValidation pedValidation : findPedValidationByPreauthKey) {
					if(diagnosisDTO.getDiagnosisName() != null && diagnosisDTO.getDiagnosisName().getId().equals(pedValidation.getDiagnosisId())) {
						isExisting = true;
						pedValidationKey = pedValidation.getKey();
						break;
					} 
				}
				if(!isExisting && (diagnosisDTO.getRecTypeFlag() == null || (diagnosisDTO.getRecTypeFlag() != null && diagnosisDTO.getRecTypeFlag().equalsIgnoreCase("A")))) {
					String diagnosisByKey = preauthService.getDiagnosisByKey(diagnosisDTO.getDiagnosisId());
					addedDiag += diagnosisByKey + " ,";
				}
			}
			
			if(!diagnosisVariation.isEmpty() && diagnosisVariation.length() > 0) {
				comparisonResult +="The following Diagnosis has been changed from Previous ROD :  " + diagnosisVariation + "</br>";
			}
			if(!procedureVariation.isEmpty() && procedureVariation.length() > 0) {
				comparisonResult += "The following Procedure has been changed from Previous ROD :  " + procedureVariation + "</br>";
			}
			
			if(!addedDiag.isEmpty() && addedDiag.length() > 0) {
				comparisonResult +="The following Diagnosis has been added from Previous ROD :  " + addedDiag + "</br>";
			}
			if(!addedProc.isEmpty() && addedProc.length() > 0) {
				comparisonResult += "The following Procedure has been added from Previous ROD :  " + addedProc + "</br>";
			}
//			if(previousLatestROD.getPayeeName() != null && bean.getPayeeName() != null && !previousLatestROD.getPayeeName().equals(bean.getPayeeName())) {
//				comparisonResult += "Previous Insured Patient Name : " + (previousLatestROD.getPayeeName()) + " Current Insured Patient Name : " + (bean.getPayeeName());
//			}
			
		}
		
		view.setCompareWithRODResult(comparisonResult);
	}
	
	public void generateFieldsBasedOnDomicillaryFields(@Observes @CDIEvent(GENERATE_DOMICILLARY_FIELDS) final ParameterDTO parameters)
	{
		Boolean selectedValue = (Boolean) parameters.getPrimaryParameter();
		view.genertateFieldsBasedOnDomicillaryFields(selectedValue);
	}
	
	public void generateApproveLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_APPROVE_EVENT) final ParameterDTO parameters) {
		view.generateApproveLayout();
	}
	
	public void generateCancelRODLayout(
			@Observes @CDIEvent(CLAIM_CANCEL_ROD_EVENT) final ParameterDTO parameters) {
		view.generateCancelRodLayout();
	}
	
	public void generateQueryLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_QUERY_BUTTON_EVENT) final ParameterDTO parameters) {
		view.generateQueryLayout();
	}
	
	public void generateRejectLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_REJECTION_EVENT) final ParameterDTO parameters) {

		view.generateRejectionLayout(masterService.getReimbRejCategoryByValue());
//										masterService.getSelectValueContainer(ReferenceTable.REJECTION_CATEGORY));
	}
	
	public void generateEscalateLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_ESCALATE_EVENT) final ParameterDTO parameters) {
		view.generateEscalateLayout(masterService
				.getSelectValueContainer(ReferenceTable.ESCALATE_TO_ROD));
	}
	
	public void generateEscalateReplyLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_ESCALATE_REPLY_EVENT) final ParameterDTO parameters) {
		view.generateEscalateReplyLayout();
	}
	
	public void generateReferCoOrdinatorLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_REFERCOORDINATOR_EVENT) final ParameterDTO parameters) {
		view.generateReferCoOrdinatorLayout(masterService
				.getSelectValueContainer(ReferenceTable.COORDINATOR_REQUEST_TYPE));
	}
	
	public void generateFieldVisitLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_FIELD_VISIT_EVENT) final ParameterDTO parameters) {
		view.genertateFieldsBasedOnFieldVisit(masterService
				.getSelectValueContainer(ReferenceTable.ALLOCATION_TO)
				,masterService.getSelectValueContainer(ReferenceTable.ASSIGN_TO),masterService.getSelectValueContainer(ReferenceTable.FVR_PRIORITY));
	}
	
	public void generateInvestigationLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_INVESTIGATION_EVENT) final ParameterDTO parameters) {
		view.genertateInvestigationLayout(masterService
				.getSelectValueContainer(ReferenceTable.ALLOCATION_TO_INVESTIGATION));
	}
	
	public void generateSpecialistLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_SPECIALIST_EVENT) final ParameterDTO parameters) {
		view.genertateSpecialistLayout(masterService.getMasterValueByReference((ReferenceTable.SPECIALIST_TYPE)));
	}
	
	public void generateSentToReplyLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_SENT_TO_REPLY_EVENT) final ParameterDTO parameters) {
		view.genertateSentToReplyLayout();
	}
	
	public void getValuesForMedicalDecisionTable(
			@Observes @CDIEvent(CLAIM_REQUSET_SUM_INSURED_CALCULATION) final ParameterDTO parameters) {
		Map<String, Object> values = (Map<String, Object>) parameters
				.getPrimaryParameter();
		DiagnosisProcedureTableDTO dto = (DiagnosisProcedureTableDTO) parameters
				.getSecondaryParameters()[0];
		PreauthDTO preauthDto = (PreauthDTO) parameters.getSecondaryParameter(1, PreauthDTO.class);
		String diagnosis = null;
		if (values.containsKey("diagnosisId")) {
			diagnosis = masterService.getDiagnosis(Long.valueOf((String) values
					.get("diagnosisId")));
		}

		if (dto.getDiagnosisDetailsDTO() != null) {
			dto.getDiagnosisDetailsDTO().setDiagnosis(diagnosis);
		}
		
		Reimbursement hospitalizationRod = reimbursementService.getHospitalizationRod(preauthDto.getClaimKey(), preauthDto.getKey());
		
		Boolean queryRaisedFromMA = reimbursementQueryService.isQueryRaisedFromMA(preauthDto.getKey());
		if(queryRaisedFromMA){
			preauthDto.setIsQueryReceived(false);
		}
		
		if(!preauthDto.getIsQueryReceived()){
		
			if(preauthDto.getClaimDTO().getClaimType() != null && preauthDto.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
				
	//			Reimbursement hospitalizationRod = reimbursementService.getHospitalizationRod(preauthDto.getClaimKey(), preauthDto.getKey());
				
				if(hospitalizationRod != null){
					
					values.put("preauthKey", hospitalizationRod.getKey());
					
				}else{
					if(preauthDto.getClaimDTO().getLatestPreauthKey() != null){
						values.put("preauthKey", preauthDto.getClaimDTO().getLatestPreauthKey());
				    }else{
				    	Preauth latestPreauth = reimbursementService.getLatestPreauthByClaim(preauthDto.getClaimDTO().getKey());
				    	if(latestPreauth != null){
				    		values.put("preauthKey", latestPreauth.getKey());
				    	}
				    }
				}
			
			}else{
				
				if(hospitalizationRod != null){
					values.put("preauthKey", hospitalizationRod.getKey());
				}else{
					values.put("preauthKey",0l);
				}
			}
		} else {
			values.put("preauthKey", preauthDto.getKey());
		}


		if((preauthDto.getClaimDTO().getClaimType() != null && preauthDto.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)) || (preauthDto.getIsHospitalizationRepeat() != null && preauthDto.getIsHospitalizationRepeat())) {
			values.put("preauthKey", 0l);
		}
		
		Map<String, Object> medicalDecisionTableValues = dbCalculationService
				.getMedicalDecisionTableValue(values,preauthDto.getNewIntimationDTO());
		
		
		if(values.containsKey(SHAConstants.IS_NON_ALLOPATHIC) && (Boolean)values.get(SHAConstants.IS_NON_ALLOPATHIC)) {
//			Map<String, Double> nonAllopathicAmount = dbCalculationService.getNonAllopathicAmount((Long)values.get(SHAConstants.POLICY_KEY), (Long)values.get(SHAConstants.INSURED_KEY)
//					,(Long)values.get("rodKey"),"R");
			Map<String, Double> nonAllopathicAmount = null;
			if(preauthDto.getClaimDTO().getClaimType() != null && preauthDto.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){

				
				Long preauthKey = (Long)values.get("preauthKey");

				
				if(hospitalizationRod != null){
					
					nonAllopathicAmount = dbCalculationService.getNonAllopathicAmount((Long)values.get(SHAConstants.POLICY_KEY), (Long)values.get(SHAConstants.INSURED_KEY)
							,preauthKey,"R", (Long)values.get(SHAConstants.CLAIM_KEY));
				}else{
					nonAllopathicAmount = dbCalculationService.getNonAllopathicAmount((Long)values.get(SHAConstants.POLICY_KEY), (Long)values.get(SHAConstants.INSURED_KEY)
							,preauthKey,"C", (Long)values.get(SHAConstants.CLAIM_KEY));
				}
				
				
				}else{
					
				nonAllopathicAmount = dbCalculationService.getNonAllopathicAmount((Long)values.get(SHAConstants.POLICY_KEY), (Long)values.get(SHAConstants.INSURED_KEY)
							,0l,"0", (Long)values.get(SHAConstants.CLAIM_KEY));
				}
			medicalDecisionTableValues.put(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT, nonAllopathicAmount.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT));
			medicalDecisionTableValues.put(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT, nonAllopathicAmount.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT));
		}

		view.getValuesForMedicalDecisionTable(dto, medicalDecisionTableValues);
	}
	
	/*public void ruleForCheckInvestigation(@Observes @CDIEvent(CHECK_INVESTIGATION_INITIATED) final ParameterDTO parameters) {
		Long claimKey = (Long) parameters.getPrimaryParameter();
		if(claimKey != null) {
			Investigation checkInitiateInvestigation = preauthService.checkInitiateInvestigation(claimKey);
			view.setInvestigationRule(checkInitiateInvestigation);
		}
		
	}*/

	
	private void getCoversAndOptionalCoverValues(PreauthDTO dto)
	{
		Long productCode = dto.getNewIntimationDTO().getPolicy().getProduct().getKey();
		Long insuredKey = dto.getNewIntimationDTO().getInsuredPatient().getKey();
		if(null != productCode && null != insuredKey)
		{
			
			referenceData.put("addOnCovers",dbCalculationService.getBebefitAdditionalCovers(SHAConstants.ADDITIONAL_COVER, productCode , insuredKey));
			referenceData.put("optionalCovers",dbCalculationService.getBebefitAdditionalCovers(SHAConstants.OPTIONAL_COVER, productCode , insuredKey));
			referenceData.put("addOnCoverProc",dbCalculationService.getClaimCoverValues(SHAConstants.ADDITIONAL_COVER, productCode , insuredKey));
			referenceData.put("optionalCoverProc",dbCalculationService.getClaimCoverValues(SHAConstants.OPTIONAL_COVER, productCode , insuredKey));
			if(null != productCode && ReferenceTable.getGPAProducts().containsKey(productCode)){
				referenceData.put("addOnCovers",dbCalculationService.getBebefitAdditionalCovers(SHAConstants.GPA_ADDITIONAL_COVER, productCode , insuredKey));
				referenceData.put("optionalCovers",dbCalculationService.getBebefitAdditionalCovers(SHAConstants.GPA_OPTIONAL_COVER, productCode , insuredKey));
			}
			//reimbursementDTO.getDocumentDetails().setOptionalCovers(dbCalculationService.getBebefitAdditionalCovers(SHAConstants.OPTIONAL_COVER, productCode , insuredKey));
		}	
	}
	
	public void loadBenefitsTable(
			@Observes @CDIEvent(PA_BENEFITS_TABLE_DATA) final ParameterDTO parameters)
			{
				Long benefitsId = (Long)parameters.getPrimaryParameter();
				Long insuredKey = (Long)parameters.getSecondaryParameter(0, Long.class);
				GComboBox cmb = (GComboBox)parameters.getSecondaryParameter(1, GComboBox.class);
				List<PABenefitsDTO> paBenefitsValueList = dbCalculationService.getBenefitCoverValues(insuredKey, benefitsId);
				
				view.loadPABenefitsValues(paBenefitsValueList,cmb);
			}
	
	public void getValuesForBenefits(
			@Observes @CDIEvent(PA_BENEFITS_TABLE_VALUES) final ParameterDTO parameters)
			{
				//Long benefitsId = (Long)parameters.getPrimaryParameter();
				PreauthDTO bean = (PreauthDTO) parameters.getPrimaryParameter();
				Long benefitsId = (Long)parameters.getSecondaryParameter(0, Long.class);
				Long reimbursementKey = bean.getKey();
				Long insuredKey = bean.getNewIntimationDTO()
						.getInsuredPatient().getKey();
				List<PABenefitsDTO> paBenefitsValueList = null;
				BeanItemContainer<SelectValue> coverContainer =null;
				if(null != bean && null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getPolicy() &&
						null != bean.getNewIntimationDTO().getPolicy().getProduct() && null != bean.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
						!(ReferenceTable.getGPAProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
					paBenefitsValueList = dbCalculationService.getBenefitCoverValues(insuredKey, benefitsId);				
					coverContainer = dbCalculationService.getBenefitCoverValueContainer(insuredKey, benefitsId);
				}
				else
				{
					paBenefitsValueList = dbCalculationService.getGPABenefitCoverValues(insuredKey, benefitsId);				
					coverContainer = dbCalculationService.getGPABenefitCoverValueContainer(insuredKey, benefitsId);
				}
				List<PABenefitsDTO> benefitsDBList = rodService.getPABenefitsDataBasedOnRodKey(reimbursementKey,insuredKey,bean);
				Map referenceDataMap = new HashMap<String, Object>();
				referenceDataMap.put("coverContainerValue", coverContainer);
				view.setPABenefitsTableValues(paBenefitsValueList,benefitsId,referenceDataMap,benefitsDBList);
			}
	
	
	public void loadBillDetailsValues(
			@Observes @CDIEvent(PA_LOAD_BILL_DETAILS_VALUES) final ParameterDTO parameters) {
		UploadDocumentDTO uploadDTO = (UploadDocumentDTO) parameters.getPrimaryParameter();
		List<BillEntryDetailsDTO> dtoList = rodService.getBillEntryDetailsList(uploadDTO);
		uploadDTO.setBillEntryDetailList(dtoList);
		view.setUploadDTOForBillEntry(uploadDTO);
	/*	Long billClassificationKey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getMasBillCategoryValues(billClassificationKey);*/
		//view.setUpCategoryValues(selectValueContainer);
		//view.enableOrDisableBtn(uploadDTO);
		}
	
	public void saveBillValues(
			@Observes @CDIEvent(PA_SAVE_BILL_DETAILS_VALUES) final ParameterDTO parameters) {
			//Boolean status = (Boolean) parameters.getPrimaryParameter();
			UploadDocumentDTO	uploadDTO = (UploadDocumentDTO) parameters.getPrimaryParameter();
			rodService.saveBillEntryValues(uploadDTO);
			//view.setBillEntryFinalStatus(uploadDTO);
		
	}
	
	public void setUpCategoryValues(
			@Observes @CDIEvent(PA_BILL_ENTRY_TABLE_CATEGORY_DROPDOWN_VALUE) final ParameterDTO parameters) {
		Long billClassificationKey = (Long) parameters.getPrimaryParameter();
		SelectValue claimType = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService
				.getMasBillCategoryValues(billClassificationKey,claimType);
		view.setUpCategoryValues(selectValueContainer);
	}
	
	public void validateBenefitsForClaim(
			@Observes @CDIEvent(PA_VALIDATE_BENEFITS) final ParameterDTO parameters) {
		PreauthDTO dto = (PreauthDTO) parameters.getPrimaryParameter();
		Long benefitsId = (Long) parameters.getSecondaryParameter(0,Long.class);
		String presenterString = (String) parameters.getSecondaryParameter(1, String.class);
		Boolean isValid = paReimbService.validateBenefitsForClaims(dto.getClaimDTO().getKey(), dto.getRodNumber(), benefitsId);
		
		/*Long insuredKey = dto.getNewIntimationDTO().getInsuredPatient().getKey();
		List<PABenefitsDTO> paBenefitsValueList = dbCalculationService.getBenefitCoverValues(insuredKey, benefitsId);
		BeanItemContainer<SelectValue> coverContainer = dbCalculationService.getBenefitCoverValueContainer(insuredKey, benefitsId);
		Map referenceDataMap = new HashMap<String, Object>();
		referenceDataMap.put("coverContainerValue", coverContainer);*/
	//	view.setPABenefitsTableValues(paBenefitsValueList,benefitsId,referenceDataMap);
		
		
		
		view.validateCoversAndBenefitsForClaims(isValid,presenterString,benefitsId,null,null,null);
	}
	

	public void validateCoversForClaim(
			@Observes @CDIEvent(PA_VALIDATE_COVERS) final ParameterDTO parameters) {
		PreauthDTO dto = (PreauthDTO) parameters.getPrimaryParameter();
		Long coverId = (Long) parameters.getSecondaryParameter(0,Long.class);
		TextField eligibleFld = (TextField) parameters.getSecondaryParameter(1, TextField.class);
		
		TextField amountClaimedFld = (TextField) parameters.getSecondaryParameter(2, TextField.class);
		String presenterString = (String) parameters.getSecondaryParameter(3, String.class);
		ComboBox categoryCombo = (ComboBox) parameters.getSecondaryParameter(4, ComboBox.class);
		
		Boolean isValid = paReimbService.validateCoversForClaims(dto.getClaimDTO().getKey(), dto.getKey(), coverId);
		view.validateCoversAndBenefitsForClaims(isValid,presenterString,coverId,eligibleFld,amountClaimedFld,categoryCombo);
	}
	
	public void validateOptionalCoversForClaim(
			@Observes @CDIEvent(PA_VALIDATE_OPTIONAL_COVERS) final ParameterDTO parameters) {
		PreauthDTO dto = (PreauthDTO) parameters.getPrimaryParameter();
		Long coverId = (Long) parameters.getSecondaryParameter(0,Long.class);
		TextField eligibleFld = (TextField) parameters.getSecondaryParameter(1, TextField.class);
		
		TextField amountClaimedFld = (TextField) parameters.getSecondaryParameter(2, TextField.class);
		String presenterString = (String) parameters.getSecondaryParameter(3, String.class);
		ComboBox categoryCombo = (ComboBox) parameters.getSecondaryParameter(4, ComboBox.class);
		
		Boolean isValid = paReimbService.validateOptionalCoversForClaim(dto.getClaimDTO().getKey(), dto.getKey(), coverId);
		view.validateCoversAndBenefitsForClaims(isValid,presenterString,coverId,eligibleFld,amountClaimedFld,categoryCombo);
	}
	
	
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
}
