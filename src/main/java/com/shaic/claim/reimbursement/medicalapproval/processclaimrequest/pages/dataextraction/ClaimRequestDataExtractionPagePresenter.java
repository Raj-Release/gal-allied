package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.reimbursement.billing.benefits.wizard.service.ProcessClaimRequestBenefitsService;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.HospitalService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PEDValidationService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.HospitalPackage;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Procedure;
import com.shaic.domain.preauth.ProcedureMaster;
import com.shaic.domain.reimbursement.ReimbursementCalCulationDetails;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.domain.service.PreMedicalService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;
@ViewInterface(ClaimRequestDataExtractionPageInterface.class)
public class ClaimRequestDataExtractionPagePresenter extends AbstractMVPPresenter<ClaimRequestDataExtractionPageInterface>{
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
	private AcknowledgementDocumentsReceivedService ackDocReceivedService;

	@EJB
	private ProcessClaimRequestBenefitsService benefitsService;
	
	@EJB
	private ReimbursementService reimbursementService;

	public static final String MEDICAL_APPROVAL_SETUP_REFERNCE = "medical_approval_claim_request_setup_reference";
	
	public static final String CHECK_CRITICAL_ILLNESS = "medical_approval_claim_request_check_critical_illness";
	
	public static final String MEDICAL_APPROVAL_TREATMENT_TYPE_CHANGED = "medical_approval_claim_request_treatment_type_changed";
	
	public static final String MEDICAL_APPROVAL_PATIENT_STATUS_CHANGED = "medical_approval_claim_request_patient_status_changed";
	
	public static final String MEDICAL_APPROVAL_HOSPITALISATION_DUE_TO = "medical_approval_claim_request_hospitalisation_due_to";
	
	public static final String SAVE_OTHER_BENEFITS_TABLE_VALUES_MA = "save_other_benefits_table_values_ma";
	
	public static final String MEDICAL_APPROVAL_GENERATE_OTHER_CLAIMS = "medical_approval_claim_request_generate_other_claims";
	
	public static final String CLAIM_REQUEST_COORDINATOR = "Claim Request coordinator events";
	
	public static final String SUBLIMIT_CHANGED_BY_SECTION = "Set sublimit values by selecting section for claim request";
	
	public static final String GET_ICD_BLOCK = "medical_approval_claim_request_get_icd_block";
	public static final String GET_ICD_CODE = "medical_approval_claim_request_get_icd_code";
	
	public static final String GET_PROCESS_CLAIM_PROCEDURE_VALUES ="medical_approval_claim_request_procedure_values";
	
	public static final String GET_SEC_COVER = "claim_request_get_sec_cover";
	
	public static final String GET_SUB_COVER = "claim_request_get_sub_cover";
	
	public static final String GET_PACKAGE_RATE = "medical_approval_claim_request_get_package_rate";
	public static final String MEDICAL_APPROVAL_REPORTED_TO_POLICE = "medical_approval_claim_request_reported_to_police";
	public static final String COMPARE_WITH_PREVIOUS_ROD = "claim_request_compare_with_previous_rod";

	public static final String GENERATE_DOMICILLARY_FIELDS = "claim_request_generate_domicillary_fields";
	
	public static final String SET_DB_DETAILS_TO_REFERENCE_DATA = "claim_request_set_db_details_to_reference_data";
	
	public static final String REFERENCE_DATA_CLEAR = "reference data cleared for claim request Queue";
	
	public static final String GET_RTA_BALANCE_SI = "Get_RTA_Balance_SI";
	
	public static final String GET_ASSISTED_REPRODUCTION_TREATMENT_COVER_KEY_MA = "GET_ASSISTED_REPRODUCTION_TREATMENT_COVER_KEY_MA";
	public static final String CLAIM_REQUEST_APPROVE_EVENT = "claim_request_apporve_event_data_extraction_page";
	
	public static final String CLAIM_CANCEL_ROD_EVENT      =  "claim_request_cancel_rod_data_extraction_page";  
	
	public static final String CLAIM_REQUEST_QUERY_BUTTON_EVENT = "claim_request_query_button_event_data_extraction_page";

	public static final String CLAIM_REQUEST_REJECTION_EVENT = "claim_request_rejection_button_event_data_extraction_page";

	public static final String CLAIM_REQUEST_ESCALATE_EVENT = "claim_request_escalte_button_event_data_extraction_page";

	public static final String CLAIM_REQUEST_ESCALATE_REPLY_EVENT = "claim_request_escalte_reply_event_data_extraction_page";

	public static final String CLAIM_REQUEST_REFERCOORDINATOR_EVENT = "claim_request_refer_to_coordinator_event_data_extraction_page";

	public static final String CLAIM_REQUEST_SPECIALIST_EVENT = "claim_request_specialist_event_data_extraction_page";

	public static final String CLAIM_REQUEST_SENT_TO_REPLY_EVENT = "claim_request_sent_to_reply_event_data_extraction_page";

	Map<String, Object> referenceData = new HashMap<String, Object>();
	public static final String GET_EDIT_BILL_CLASSIFICATION = "view Edit Bill Calassification For MA";
	
	public static final String MA_PATIENT_CARE_BENEFITS = "ma_patient_care_benefits";
	
	public static final String SAVE_MA_HOSPITAL_CASH_PHC_TABLE_VALUES = "save_MA_hospital_cash_phc_table_values";

	public static final String SAVE_MA_HOSPITAL_CASH_TABLE_VALUES = "save_ma_hospital_cash_table_values";
	
	public static final String  CLAIM_REQUEST_VENTILATOR_SUPPORT = "claim_request_ventilator_support";
	
		
	public void setDBDetailsReferenceData(
			@Observes @CDIEvent(SET_DB_DETAILS_TO_REFERENCE_DATA) final ParameterDTO parameters) {
		PreauthDTO dto = (PreauthDTO) parameters.getPrimaryParameter();
		PolicyDto policyDTO = dto.getPolicyDto();
		Double insuredAge = dto.getNewIntimationDTO().getInsuredPatient().getInsuredAge();
		Double sumInsured = dbCalculationService.getInsuredSumInsured(dto.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), dto.getPolicyDto().getKey(),dto.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		if (sumInsured == 0) {
			sumInsured = dto.getPolicyDto().getTotalSumInsured();
		}
		
		String policyPlan = dto.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? dto.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0";
		
		/*if(ReferenceTable.MEDI_CLASSIC_BASIC_PRODUCT_KEY.equals(dto.getNewIntimationDTO().getPolicy().getProduct().getKey()) || ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(dto.getNewIntimationDTO().getPolicy().getProduct().getKey())) {*/
		if(dto.getNewIntimationDTO().getPolicy().getProduct() != null 
				&& (((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(dto.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
						SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(dto.getNewIntimationDTO().getPolicy().getProduct().getCode()))
						|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(dto.getNewIntimationDTO().getPolicy().getProduct().getCode())
						|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(dto.getNewIntimationDTO().getPolicy().getProduct().getCode()))
				|| ((SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(dto.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
						SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(dto.getNewIntimationDTO().getPolicy().getProduct().getCode()))
						&& dto.getNewIntimationDTO().getPolicy().getProductType().getKey().equals(ReferenceTable.INDIVIDUAL_POLICY)))) {
			policyPlan = dto.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() != null ? dto.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() : "0";
		}
				
		if(dto.getPreauthDataExtractionDetails().getSection() != null && dto.getPreauthDataExtractionDetails().getSection().getId() != null){
			if(ReferenceTable.getGMCProductList().containsKey(dto.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(dto.getNewIntimationDTO().getPolicy().getKey(),
						dto.getNewIntimationDTO().getInsuredPatient().getKey(),dto.getNewIntimationDTO().getPolicy().getSectionCode());
				referenceData.put("sublimitDBDetails", dbCalculationService.getClaimedAmountDetailsForSectionForGMC(dto.getPolicyDto().getKey(), sumInsured, insuredAge,dto.getPreauthDataExtractionDetails().getSection().getId(),
						policyPlan, (dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : dto.getClaimDTO().getClaimSubCoverCode())));
			}else{
				referenceData.put("sublimitDBDetails", dbCalculationService.getClaimedAmountDetailsForSection(dto.getPolicyDto().getProduct().getKey(), sumInsured, 0l, insuredAge,dto.getPreauthDataExtractionDetails().getSection().getId(),
						policyPlan, (dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : dto.getClaimDTO().getClaimSubCoverCode()),dto.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
			}
			
		}else{
			if(ReferenceTable.getGMCProductList().containsKey(dto.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(dto.getNewIntimationDTO().getPolicy().getKey(),
						dto.getNewIntimationDTO().getInsuredPatient().getKey(),dto.getNewIntimationDTO().getPolicy().getSectionCode());
				referenceData.put("sublimitDBDetails", dbCalculationService.getClaimedAmountDetailsForSectionForGMC(dto.getPolicyDto().getKey(), sumInsured, insuredAge,0l,"0", (dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : dto.getClaimDTO().getClaimSubCoverCode())));
			}else{
				referenceData.put("sublimitDBDetails", dbCalculationService.getClaimedAmountDetailsForSection(dto.getPolicyDto().getProduct().getKey(), sumInsured, 0l, insuredAge,0l,policyPlan, (dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : dto.getClaimDTO().getClaimSubCoverCode()),dto.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
			}
			
		}
	}
	
	public void setUpReference(
			@Observes @CDIEvent(MEDICAL_APPROVAL_SETUP_REFERNCE) final ParameterDTO parameters) {
		
		PreauthDTO dto = (PreauthDTO) parameters.getPrimaryParameter();
		
		PolicyDto policyDTO = dto.getPolicyDto();
		
		referenceData.put("sectionDetails", masterService
				.getSectionList(dto.getNewIntimationDTO().getPolicy().getProduct().getKey(),dto.getNewIntimationDTO().getPolicy()));
		
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
		
		//CR R20181300
		referenceData.put("pedImpactOnDiagnosis", masterService.getSelectValueContainer(ReferenceTable.PED_IMPACT_ON_DIAGNOSI));
		referenceData.put("reasonForNotPaying", masterService.getSelectValueContainer(ReferenceTable.PED_NON_PAYABLE_REASON)); //EXCLUSION_DETAILS
		//CR R20181300
				
		referenceData.put("illness", masterService.getSelectValueContainer(ReferenceTable.ILLNESS));
		referenceData.put("criticalIllness", preauthService.getCriticalIllenssMasterValues(dto));
		BeanItemContainer<SelectValue> procedureListNames = preauthService.getProcedureListNames();
		referenceData.put("procedureName", procedureListNames);
		referenceData.put("procedureCode", procedureListNames);
		referenceData.put("medicalSpeciality", preauthService.getSpecialityType("M"));
		referenceData.put("surgicalSpeciality", preauthService.getSpecialityType("S"));
		referenceData.put("sublimitApplicable", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("considerForPayment", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("considerForDayCare", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("commonValues", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("hospitalisationDueTo", masterService.getSelectValueContainer(ReferenceTable.HOSPITALIZATION_DUE_TO));
		referenceData.put("causeOfInjury", masterService.getSelectValueContainer(ReferenceTable.CAUSE_OF_INJURY));
		referenceData.put("billClassification", setClassificationValues(dto));	
		if(ReferenceTable.getHealthGainProducts().containsKey(dto.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			DBCalculationService dbCalculationService = new DBCalculationService();
			/*BeanItemContainer<SelectValue> siRestricationValueForHealthGain = dbCalculationService.getSiRestricationValueForHealthGain(dto.getNewIntimationDTO().getPolicy().getKey(),
					dto.getNewIntimationDTO().getInsuredPatient().getKey(), masterService);
			referenceData.put("sumInsuredRestriction",  siRestricationValueForHealthGain);*/
			referenceData.put("sumInsuredRestriction",dbCalculationService.getSiRestricationValue(dto.getNewIntimationDTO().getInsuredPatient().getKey()));
		}else{
			//referenceData.put("sumInsuredRestriction",  masterService.getSelectValueContainer(ReferenceTable.SUM_INSURED));
			referenceData.put("sumInsuredRestriction",dbCalculationService.getSiRestricationValue(dto.getNewIntimationDTO().getInsuredPatient().getKey()));
		}
		
		Double insuredAge = dto.getNewIntimationDTO().getInsuredPatient().getInsuredAge();
		Double sumInsured = dbCalculationService.getInsuredSumInsured(dto.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), dto.getPolicyDto().getKey(),dto.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		if (sumInsured == 0) {
			sumInsured = dto.getPolicyDto().getTotalSumInsured();
		}
		
		String policyPlan = dto.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? dto.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0";
		
		/*if(ReferenceTable.MEDI_CLASSIC_BASIC_PRODUCT_KEY.equals(dto.getNewIntimationDTO().getPolicy().getProduct().getKey()) || ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(dto.getNewIntimationDTO().getPolicy().getProduct().getKey())) {*/
		if(dto.getNewIntimationDTO().getPolicy().getProduct() != null 
				&& (((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(dto.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
						SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(dto.getNewIntimationDTO().getPolicy().getProduct().getCode()))
						|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(dto.getNewIntimationDTO().getPolicy().getProduct().getCode())
						|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(dto.getNewIntimationDTO().getPolicy().getProduct().getCode()))
				|| ((SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(dto.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
						SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(dto.getNewIntimationDTO().getPolicy().getProduct().getCode()))
					&& dto.getNewIntimationDTO().getPolicy().getProductType().getKey().equals(ReferenceTable.INDIVIDUAL_POLICY)))) {
			policyPlan = dto.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() != null ? dto.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() : "0";
		}
		
		if(dto.getPreauthDataExtractionDetails().getSection() != null && dto.getPreauthDataExtractionDetails().getSection().getId() != null){
			if(ReferenceTable.getGMCProductList().containsKey(dto.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(dto.getNewIntimationDTO().getPolicy().getKey(),
						dto.getNewIntimationDTO().getInsuredPatient().getKey(),dto.getNewIntimationDTO().getPolicy().getSectionCode());
				referenceData.put("sublimitDBDetails", dbCalculationService.getClaimedAmountDetailsForSectionForGMC(dto.getPolicyDto().getKey(), sumInsured, insuredAge,dto.getPreauthDataExtractionDetails().getSection().getId(),
						dto.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? dto.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0", (dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : dto.getClaimDTO().getClaimSubCoverCode())));
			}else{
				referenceData.put("sublimitDBDetails", dbCalculationService.getClaimedAmountDetailsForSection(dto.getPolicyDto().getProduct().getKey(), sumInsured, 0l, insuredAge,dto.getPreauthDataExtractionDetails().getSection().getId(),
						policyPlan, (dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : dto.getClaimDTO().getClaimSubCoverCode()),dto.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
			}
			
		}else{
			if(ReferenceTable.getGMCProductList().containsKey(dto.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(dto.getNewIntimationDTO().getPolicy().getKey(),
						dto.getNewIntimationDTO().getInsuredPatient().getKey(),dto.getNewIntimationDTO().getPolicy().getSectionCode());
				referenceData.put("sublimitDBDetails", dbCalculationService.getClaimedAmountDetailsForSectionForGMC(dto.getPolicyDto().getKey(), sumInsured, insuredAge,0l,"0", (dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : dto.getClaimDTO().getClaimSubCoverCode())));
			}else{
				referenceData.put("sublimitDBDetails", dbCalculationService.getClaimedAmountDetailsForSection(dto.getPolicyDto().getProduct().getKey(), sumInsured, 0l, insuredAge,0l,policyPlan, (dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : dto.getClaimDTO().getClaimSubCoverCode()),dto.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
			}
			
		}
		
//		referenceData.put("sublimitDBDetails", dbCalculationService.getClaimedAmountDetails(dto.getPolicyDto().getProduct().getKey(), sumInsured, insuredAge));
		referenceData.put("insuredPedList", masterService.getInusredPEDList(dto.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString()));
		
		NewIntimationDto intimationDTO = dto.getNewIntimationDTO();
		//Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(intimationDTO.getInsuredPatient().getInsuredId().toString(), intimationDTO.getPolicy().getKey());
		Double balanceSI = 0d;
		List<Double> copayValue = new ArrayList<Double>();
		
		 if(ReferenceTable.getGMCProductList().containsKey(intimationDTO.getPolicy().getProduct().getKey())){
			balanceSI = dbCalculationService.getBalanceSIForGMC(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),dto.getClaimKey());
			copayValue = dbCalculationService.getProductCoPayForGMC(intimationDTO.getPolicy().getKey(),intimationDTO.getInsuredPatient().getKey());
		}else if(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(intimationDTO.getPolicy().getProduct().getKey())
				|| ReferenceTable.STAR_CANCER_PLATINUM_PRODUCT_KEY_IND.equals(intimationDTO.getPolicy().getProduct().getKey())){
			String subCover = "";
			if(null != dto.getPreauthDataExtractionDetails() && null != dto.getPreauthDataExtractionDetails().getSectionDetailsDTO() && null != dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover()){			
				subCover = dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue();
			}
			balanceSI = dbCalculationService.getBalanceSIForReimbursementStarCancerGold(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),dto.getClaimKey(), dto.getKey(),subCover).get(SHAConstants.TOTAL_BALANCE_SI);
			copayValue = dbCalculationService.getProductCoPay(intimationDTO.getPolicy().getProduct().getKey() ,intimationDTO.getInsuredPatient().getKey(), intimationDTO.getInsuredPatient().getInsuredId(),intimationDTO);
		}else{
			//code added for cr modern sublimit SI by noufel
			if(dto.getIsBalSIForSublimitCardicSelected()){
				balanceSI = dbCalculationService.getBalanceSIForCardicCarePlatRemb(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),dto.getClaimKey(),dto.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
			}
			else if(dto.getIsModernSublimitSelected()){
				balanceSI = dbCalculationService.getBalanceSIForModernSublimit(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),dto.getClaimKey(),dto.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
			}else{
				balanceSI = dbCalculationService.getBalanceSIForReimbursement(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),dto.getClaimKey(),dto.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
			}
			copayValue = dbCalculationService.getProductCoPay(intimationDTO.getPolicy().getProduct().getKey() ,intimationDTO.getInsuredPatient().getKey(), intimationDTO.getInsuredPatient().getInsuredId(),intimationDTO);
		}
		dto.setBalanceSI(SHAUtils.getHospOrPartialAppAmt(dto, reimbursementService, balanceSI));
		
		
		Reimbursement reimbursementByKey = ackDocReceivedService.getReimbursementByKey(dto.getKey());
		
		if(reimbursementByKey != null && reimbursementByKey.getDocAcknowLedgement() != null){
			
			Boolean isSettled = reimbursementService.isSettledPaymentAvailable(reimbursementByKey.getRodNumber());
			
			DocAcknowledgement docAcknowLedgement = reimbursementByKey.getDocAcknowLedgement();
			if(isSettled){
				if(reimbursementByKey.getReconsiderationRequest() != null && reimbursementByKey.getReconsiderationRequest().equalsIgnoreCase("Y") 
						&& docAcknowLedgement.getPaymentCancellationFlag() != null && docAcknowLedgement.getPaymentCancellationFlag().equalsIgnoreCase("N")){
					Double alreadyPaidAmt = 0d;
					List<ReimbursementCalCulationDetails> reimbursementCalculationDetails = ackDocReceivedService.getReimbursementCalculationDetails(dto.getKey());
					for (ReimbursementCalCulationDetails reimbursementCalCulationDetails2 : reimbursementCalculationDetails) {
						if(reimbursementCalCulationDetails2.getPayableInsuredAfterPremium() != null){
							alreadyPaidAmt += reimbursementCalCulationDetails2.getPayableInsuredAfterPremium();
						}
					}
					
					dto.setBalanceSI(dto.getBalanceSI()+alreadyPaidAmt);
					
				}
			}
			
		}
		
		dto.setProductCopay(copayValue);
//		referenceData.put("policyAgeing", dbCalculationService.getPolicyAgeing(intimationDTO.getAdmissionDate(), intimationDTO.getPolicy().getPolicyNumber()));
		referenceData.put("policyAgeing",intimationDTO.getPolicyYear() != null ? intimationDTO.getPolicyYear() : "");
		
		referenceData.put("commonValues", masterService
				.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("reasonForReconsiderationRequest", masterService
				.getSelectValueContainer(ReferenceTable.REASON_FOR_RECONSIDERATION));
		referenceData.put("section", masterService
				.getSelectValueContainer(ReferenceTable.SECTION));
		referenceData.put("typeOfDelivery", masterService
				.getSelectValueContainer(ReferenceTable.DELIVERY_TYPE));
		referenceData.put("cancellationReason", masterService.getSelectValueContainer(ReferenceTable.CANCELLATION_REASON));
		referenceData.put("natureOfLoss", masterService
				.getNatureCauseLossSelectValueContainer(SHAConstants.HEALTH_LOB, ReferenceTable.NATURE_OF_LOSS));
		referenceData.put("causeOfLoss", masterService
				.getNatureCauseLossSelectValueContainer(SHAConstants.PA_LOB, ReferenceTable.CAUSE_OF_LOSS));
		referenceData.put("catastrophicLoss", masterService.getCatastrophicLossList());
		referenceData.put("treatcareHealthcorona", masterService
				.getSelectValueContainer(ReferenceTable.HOME_CARE_TREATMENT_KAVACH));
		referenceData.put("typeofAdmission",masterService.getTypeOfAdmissionTypes());
		
		referenceData.put("patientDayCareValueContainer", masterService.getSelectValueContainer(ReferenceTable.HOSPITAL_CASH_PATIENT_DAY_CARE));

		referenceData.put("hospitalCashDueTo", masterService.getSelectValueContainer(ReferenceTable.HOSPITAL_CASH_DUE_TO));
		
		view.setupReferences(referenceData);
		
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
						||  SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode())
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
						.getClaimedAmountDetailsForSectionForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
								sumInsured, insuredAge,preauthDTO.getPreauthDataExtractionDetails().getSection().getId(),
								preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0", (preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : preauthDTO.getClaimDTO().getClaimSubCoverCode() )));
			}else{
				referenceData.put("sublimitDBDetails", dbCalculationService
						.getClaimedAmountDetailsForSection(policyDTO.getProduct().getKey(),
								sumInsured, 0l, insuredAge,preauthDTO.getPreauthDataExtractionDetails().getSection().getId(),
								policyPlan, (preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : preauthDTO.getClaimDTO().getClaimSubCoverCode() ),preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
			}
			}else{
				if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()) ||
						ReferenceTable.STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS_KEY.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
							preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getSectionCode());
					referenceData.put("sublimitDBDetails", dbCalculationService
							.getClaimedAmountDetailsForSectionForGMC(policyDTO.getKey(),
									sumInsured, insuredAge,0l,"0", (preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : preauthDTO.getClaimDTO().getClaimSubCoverCode() )));
				}else{
					referenceData.put("sublimitDBDetails", dbCalculationService
							.getClaimedAmountDetailsForSection(policyDTO.getProduct().getKey(),
									sumInsured, 0l, insuredAge,0l,policyPlan, (preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : preauthDTO.getClaimDTO().getClaimSubCoverCode() ),preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
				}
				
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
	
	public void getProcedureValues(
			@Observes @CDIEvent(GET_PROCESS_CLAIM_PROCEDURE_VALUES) final ParameterDTO parameters) {
		Long specialistkey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue>  procedures = preauthService.getProcedureforSpeciality(specialistkey);
		view.setProcessClaimProcedureValues(procedures);
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
		if(procedureMaster.getDayCareFlag() != null){
		mappedValues.put("dayCareProcedure", procedureMaster.getDayCareFlag().equalsIgnoreCase("y") ? "Yes" : "No" );
		}
		mappedValues.put("procedureCode", procedureMaster.getProcedureCode());
		view.setPackageRate(mappedValues);
	}
	
	public void compareWithPreviousROD(@Observes @CDIEvent(COMPARE_WITH_PREVIOUS_ROD) final ParameterDTO parameters) {
		
		try{
		
		PreauthDTO bean = (PreauthDTO) parameters.getPrimaryParameter();
		Reimbursement previousLatestROD = reimbursementService.getFilteredPreviousLatestROD(bean.getClaimKey(), bean.getKey());
		StringBuffer comparisonResult = new StringBuffer();
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
				comparisonResult.append("Previous DOA : ").append(SHAUtils.formatDate(previousLatestROD.getDateOfAdmission())).append("   Current DOA : ").append(SHAUtils.formatDate(bean.getPreauthDataExtractionDetails().getAdmissionDate())).append("</br>");
			}
			
			if(previousLatestROD.getDateOfDischarge() != null && bean.getPreauthDataExtractionDetails().getDischargeDate() != null && !previousLatestROD.getDateOfDischarge().equals(bean.getPreauthDataExtractionDetails().getDischargeDate())) {
				comparisonResult.append("Previous DOD : ").append(SHAUtils.formatDate(previousLatestROD.getDateOfDischarge())).append("    Current DOD : ").append(SHAUtils.formatDate(bean.getPreauthDataExtractionDetails().getDischargeDate()));
			}
			
			if(previousLatestROD.getTreatmentType() != null && bean.getPreauthDataExtractionDetails().getTreatmentType() != null && !previousLatestROD.getTreatmentType().getKey().equals(bean.getPreauthDataExtractionDetails().getTreatmentType().getId())) {
				comparisonResult.append("Previous Treatment Type : ").append((previousLatestROD.getTreatmentType().getKey().equals(ReferenceTable.SURGICAL_CODE) ? "Surgical" : "Medical")).append("    Current Treatment Type : ").append((bean.getPreauthDataExtractionDetails().getTreatmentType().getId().equals(ReferenceTable.SURGICAL_CODE) ? "Surgical" : "Medical"));
			}
			List<ProcedureDTO> procedureExclusionCheckTableList = bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList();
			List<DiagnosisDetailsTableDTO> diagnosisTableList = bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
			StringBuffer procedureVariation = new StringBuffer();
			StringBuffer diagnosisVariation = new StringBuffer();
			StringBuffer addedDiag = new StringBuffer();
			StringBuffer addedProc = new StringBuffer();
			for (Procedure procedure : findProcedureByPreauthKey) {
				Boolean isExisting = false;
				for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
					if (procedureDTO.getNewProcedureFlag() != null
							&& procedure.getNewProcedureFlag() != null
							&& procedure.getNewProcedureFlag().equals(
									procedureDTO.getNewProcedureFlag())
							&& procedureDTO.getProcedureName() != null) {
						if (procedure.getNewProcedureFlag() == 0 && procedure.getProcedureID() != null) {
							if (procedureDTO.getProcedureName().getId()
									.equals(procedure.getProcedureID())) {
								
								isExisting = true;
								break;
							}
						}else if(procedure.getNewProcedureFlag() == 1){
							isExisting = true;
							break;
						}
					}

				}
				if(!isExisting) {
					procedureVariation.append(procedure.getProcedureName()).append(" ,");
				}
			}
			
			for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
				Boolean isExisting = false;
				//Long procedureKey = 0l;
				for (Procedure procedure : findProcedureByPreauthKey) {
					if(procedureDTO.getNewProcedureFlag() != null && procedure.getNewProcedureFlag() != null && procedure.getNewProcedureFlag().equals(procedureDTO.getNewProcedureFlag()) && procedureDTO.getProcedureName() != null && procedure.getProcedureID() != null && procedureDTO.getProcedureName().getId().equals(procedure.getProcedureID())) {
						isExisting = true;
						//procedureKey = procedure.getKey();
						break;
					} 
				}
				if(!isExisting && (procedureDTO.getRecTypeFlag() == null || (procedureDTO.getRecTypeFlag() != null && procedureDTO.getRecTypeFlag().equalsIgnoreCase("A")))) {
//					addedProc += procedureDTO.getProcedureName().getValue() + " ,";
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
					diagnosisVariation.append(diagnosisByKey).append(" ,");
				}
			}
			
			for (DiagnosisDetailsTableDTO diagnosisDTO : diagnosisTableList) {
				Boolean isExisting = false;
				//Long pedValidationKey = 0l;
				for (PedValidation pedValidation : findPedValidationByPreauthKey) {
					if(diagnosisDTO.getDiagnosisName() != null && diagnosisDTO.getDiagnosisName().getId().equals(pedValidation.getDiagnosisId())) {
						isExisting = true;
						//pedValidationKey = pedValidation.getKey();
						break;
					} 
				}
				if(!isExisting && (diagnosisDTO.getRecTypeFlag() == null || (diagnosisDTO.getRecTypeFlag() != null && diagnosisDTO.getRecTypeFlag().equalsIgnoreCase("A")))) {
					String diagnosisByKey = preauthService.getDiagnosisByKey(diagnosisDTO.getDiagnosisId());
					addedDiag.append(diagnosisByKey).append(" ,");
				}
			}
			
			if(diagnosisVariation.length() > 0) {
				comparisonResult.append("The following Diagnosis has been changed from Previous ROD :  ").append(diagnosisVariation).append("</br>");
			}
			if( procedureVariation.length() > 0) {
				comparisonResult.append("The following Procedure has been changed from Previous ROD :  ").append(procedureVariation).append( "</br>");
			}
			
			if(addedDiag.length() > 0) {
				comparisonResult.append("The following Diagnosis has been added from Previous ROD :  ").append(addedDiag).append("</br>");
			}
			if(addedProc.length() > 0) {
				comparisonResult.append("The following Procedure has been added from Previous ROD :  ").append(addedProc).append("</br>");
			}
//			if(previousLatestROD.getPayeeName() != null && bean.getPayeeName() != null && !previousLatestROD.getPayeeName().equals(bean.getPayeeName())) {
//				comparisonResult += "Previous Insured Patient Name : " + (previousLatestROD.getPayeeName()) + " Current Insured Patient Name : " + (bean.getPayeeName());
//			}
			
		}
		
		view.setCompareWithRODResult(comparisonResult.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void generateFieldsBasedOnDomicillaryFields(@Observes @CDIEvent(GENERATE_DOMICILLARY_FIELDS) final ParameterDTO parameters)
	{
		Boolean selectedValue = (Boolean) parameters.getPrimaryParameter();
		view.genertateFieldsBasedOnDomicillaryFields(selectedValue);
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
	
	public void setClearReferenceData(
			@Observes @CDIEvent(REFERENCE_DATA_CLEAR) final ParameterDTO parameters) {
		SHAUtils.setClearReferenceData(referenceData);
	}
	
	public void generateApproveLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_APPROVE_EVENT) final ParameterDTO parameters) {		
		
		view.generateApproveLayout();
	}
	
	public void getRTABalanceSI(@Observes @CDIEvent(GET_RTA_BALANCE_SI) final ParameterDTO parameters)
	{
		PreauthDTO bean = (PreauthDTO) parameters.getPrimaryParameter();
		Double balanceSI = dbCalculationService.getRTABalanceSI(bean.getNewIntimationDTO().getPolicy().getKey() , bean.getNewIntimationDTO().getInsuredPatient().getKey(),bean.getClaimKey(),bean.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
		
		Reimbursement reimbursementByKey = ackDocReceivedService.getReimbursementByKey(bean.getKey());
		
		bean.setBalanceSI(SHAUtils.getHospOrPartialAppAmt(bean, reimbursementService, balanceSI));
		
		if(reimbursementByKey != null && reimbursementByKey.getDocAcknowLedgement() != null){
			Boolean isSettled = reimbursementService.isSettledPaymentAvailable(reimbursementByKey.getRodNumber());
			DocAcknowledgement docAcknowLedgement = reimbursementByKey.getDocAcknowLedgement();
			if(isSettled){
				if(reimbursementByKey.getReconsiderationRequest() != null && reimbursementByKey.getReconsiderationRequest().equalsIgnoreCase("Y") 
						&& docAcknowLedgement.getPaymentCancellationFlag() != null && docAcknowLedgement.getPaymentCancellationFlag().equalsIgnoreCase("N")){
					Double alreadyPaidAmt = 0d;
					List<ReimbursementCalCulationDetails> reimbursementCalculationDetails = ackDocReceivedService.getReimbursementCalculationDetails(bean.getKey());
					for (ReimbursementCalCulationDetails reimbursementCalCulationDetails2 : reimbursementCalculationDetails) {
						if(reimbursementCalCulationDetails2.getPayableInsuredAfterPremium() != null){
							alreadyPaidAmt += reimbursementCalCulationDetails2.getPayableInsuredAfterPremium();
						}
					}
					
					bean.setBalanceSI(bean.getBalanceSI()+alreadyPaidAmt);
					
				}
			}
			
		}
		
	}
	
	public void getAssistedReprodTreatment(@Observes @CDIEvent(GET_ASSISTED_REPRODUCTION_TREATMENT_COVER_KEY_MA) final ParameterDTO parameters) {
		
		try{
		
		PreauthDTO bean = (PreauthDTO) parameters.getPrimaryParameter();
		Long assistedKey = reimbursementService.getAssistedReprodTreatment(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getId());
		
		
		view.setAssistedReprodTreatment(assistedKey);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void getEditBillClassification(@Observes @CDIEvent(GET_EDIT_BILL_CLASSIFICATION) final ParameterDTO parameters) {
		PreauthDTO bean = (PreauthDTO) parameters.getPrimaryParameter();
		if(referenceData == null){
			referenceData = new WeakHashMap<String, Object>();	
			referenceData.put("billClassification", setClassificationValues(bean));
		}
		referenceData.put(SHAConstants.BILL_CLASSIFICATION_DETAILS, validateBillClassification(bean.getClaimDTO().getKey()));
		referenceData.put(SHAConstants.ALL_BILL_CLASSIFICATIONS, masterService.getMasBillClassificationValues());
		view.showEditBillClassification(referenceData);
		
	}
	
	public void generateFieldsBasedPatientAddOnBenefits(@Observes @CDIEvent(MA_PATIENT_CARE_BENEFITS) final ParameterDTO parameters)
	{
		Boolean selectedValue = (Boolean) parameters.getPrimaryParameter();
		view.generateFieldsBasedOnPatientCareBenefits(selectedValue);
	}
	
	public List<DocumentDetailsDTO> validateBillClassification(Long claimKey) {
		List<Reimbursement> reimbursementList = ackDocReceivedService.getReimbursementDetailsForBillClassificationValidation(claimKey);
		List<DocumentDetailsDTO> docDTOList = new ArrayList<DocumentDetailsDTO>();
		if(null != reimbursementList && !reimbursementList.isEmpty())
		{
			DocAcknowledgement docAck = null;
			DocumentDetailsDTO docDTO  = null;
			for (Reimbursement reimbursement : reimbursementList) {
				docAck= reimbursement.getDocAcknowLedgement();
				docDTO = new DocumentDetailsDTO();
				docDTO.setHospitalizationFlag(docAck.getHospitalisationFlag());
				docDTO.setPartialHospitalizationFlag(docAck.getPartialHospitalisationFlag());
				docDTO.setPreHospitalizationFlag(docAck.getPreHospitalisationFlag());
				docDTO.setPostHospitalizationFlag(docAck.getPostHospitalisationFlag());
				docDTO.setLumpSumAmountFlag(docAck.getLumpsumAmountFlag());
				docDTO.setAddOnBenefitsPatientCareFlag(docAck.getPatientCareFlag());
				docDTO.setAddOnBenefitsHospitalCashFlag(docAck.getHospitalCashFlag());
				if(null != reimbursement.getStatus()) {
					docDTO.setStatusId(reimbursement.getStatus().getKey());
				}
				
				docDTOList.add(docDTO);
			}
		}
		
		return docDTOList;
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
				/*if(("Hospitalization").equalsIgnoreCase(selectValue.getValue()) && (null != preauthDTO.getHospitalizaionFlag() && preauthDTO.getHospitalizaionFlag())
						|| (null != preauthDTO.getIsHospitalizationRepeat() && preauthDTO.getIsHospitalizationRepeat())
						)*/
				
				//	if(("Hospitalization").equalsIgnoreCase(selectValue.getValue()) && (null != preauthDTO.getHospitalizaionFlag() && preauthDTO.getHospitalizaionFlag()))
					if((ReferenceTable.HOSPITALIZATION).equals(selectValue.getId()) && ((null != preauthDTO.getHospitalizaionFlag() && preauthDTO.getHospitalizaionFlag())
							|| (null != preauthDTO.getIsHospitalizationRepeat() && preauthDTO.getIsHospitalizationRepeat()) || (null != preauthDTO.getPartialHospitalizaionFlag() &&  preauthDTO.getPartialHospitalizaionFlag())
							))
					{
						finalClassificationList.add(selectValue);
					}
					else if((ReferenceTable.PRE_HOSPITALIZATION).equals(selectValue.getId()) && (null != preauthDTO.getPreHospitalizaionFlag() && preauthDTO.getPreHospitalizaionFlag()))
					{
						finalClassificationList.add(selectValue);
					}
					else if((ReferenceTable.POST_HOSPITALIZATION).equals(selectValue.getId()) && (null != preauthDTO.getPostHospitalizaionFlag() && preauthDTO.getPostHospitalizaionFlag()))
					{
						finalClassificationList.add(selectValue);
					}
					else if((ReferenceTable.LUMPSUM).equals(selectValue.getId()) && (null != preauthDTO.getLumpSumAmountFlag() && preauthDTO.getLumpSumAmountFlag()))
					{
						finalClassificationList.add(selectValue);
					}
					
					else if((ReferenceTable.OTHER_BENEFIT).equals(selectValue.getId()) && (null != preauthDTO.getOtherBenefitsFlag() && preauthDTO.getOtherBenefitsFlag()))
					{
						finalClassificationList.add(selectValue);
					}
					else if((preauthDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
							preauthDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
					          || (preauthDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
					        		  preauthDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))) &&
							(ReferenceTable.HOSPITAL_CASH_BILL_ID).equals(selectValue.getId()) && (null != preauthDTO && null != preauthDTO.getReceiptOfDocumentsDTO() 
							&& preauthDTO.getReceiptOfDocumentsDTO().getDocumentDetails()!= null &&  preauthDTO.getReceiptOfDocumentsDTO().getDocumentDetails().getHospitalCash()))
					{
						finalClassificationList.add(selectValue);
					}
			}
			classificationValueContainer.addAll(finalClassificationList);
		}
		return classificationValueContainer;
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

		view.generateRejectionLayout(masterService
				.getSelectValueContainer(ReferenceTable.REJECTION_CATEGORY));
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
	
	public void generateSpecialistLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_SPECIALIST_EVENT) final ParameterDTO parameters) {
		view.genertateSpecialistLayout(masterService.getMasterValueByReference((ReferenceTable.SPECIALIST_TYPE)));
	}

	public void generateSentToReplyLayout(
			@Observes @CDIEvent(CLAIM_REQUEST_SENT_TO_REPLY_EVENT) final ParameterDTO parameters) {
		view.genertateSentToReplyLayout();
	}

	public void saveOtherBenefitsValue(@Observes @CDIEvent(SAVE_OTHER_BENEFITS_TABLE_VALUES_MA) final ParameterDTO parameters)
	{
		PreauthDTO preauthDTO = (PreauthDTO)parameters.getPrimaryParameter();
		DocAcknowledgement docAck = preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement();			
		ackDocReceivedService.updateOtherBenefitsValues(preauthDTO,docAck);		
	}
	
	public void saveHospitalCashPhcValue(@Observes @CDIEvent(SAVE_MA_HOSPITAL_CASH_PHC_TABLE_VALUES) final ParameterDTO parameters)
	{
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO)parameters.getPrimaryParameter();
		List<AddOnBenefitsDTO> addOnBenefitsDTO = benefitsService.saveHospitalCashPhcValues(rodDTO);
		view.setBenefitsData(addOnBenefitsDTO);
		//Boolean selectedValue = (Boolean) parameters.getPrimaryParameter();
		//view.generateFieldsBasedOnHospitalCashBenefits(selectedValue);
	}
	public void saveHospitalCashValue(@Observes @CDIEvent(SAVE_MA_HOSPITAL_CASH_TABLE_VALUES) final ParameterDTO parameters)
	{
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO)parameters.getPrimaryParameter();
		List<AddOnBenefitsDTO> addOnBenefitsDTO = benefitsService.saveHospitalCashValues(rodDTO);
		view.setBenefitsData(addOnBenefitsDTO);
		//Boolean selectedValue = (Boolean) parameters.getPrimaryParameter();
		//view.generateFieldsBasedOnHospitalCashBenefits(selectedValue);
	}
}
