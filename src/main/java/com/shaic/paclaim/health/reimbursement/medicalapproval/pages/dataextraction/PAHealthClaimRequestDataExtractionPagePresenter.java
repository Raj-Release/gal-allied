
package com.shaic.paclaim.health.reimbursement.medicalapproval.pages.dataextraction;

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
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
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
@ViewInterface(PAHealthClaimRequestDataExtractionPageInterface.class)
public class PAHealthClaimRequestDataExtractionPagePresenter extends AbstractMVPPresenter<PAHealthClaimRequestDataExtractionPageInterface>{
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
	private AcknowledgementDocumentsReceivedService ackDocReceivedService;

	public static final String MEDICAL_APPROVAL_SETUP_REFERNCE = "pa_health_medical_approval_claim_request_setup_reference";
	
	public static final String CHECK_CRITICAL_ILLNESS = "pa_health_medical_approval_claim_request_check_critical_illness";
	
	public static final String MEDICAL_APPROVAL_TREATMENT_TYPE_CHANGED = "pa_health_medical_approval_claim_request_treatment_type_changed";
	
	public static final String MEDICAL_APPROVAL_PATIENT_STATUS_CHANGED = "pa_health_medical_approval_claim_request_patient_status_changed";
	
	public static final String MEDICAL_APPROVAL_HOSPITALISATION_DUE_TO = "pa_health_medical_approval_claim_request_hospitalisation_due_to";
	
	public static final String MEDICAL_APPROVAL_GENERATE_OTHER_CLAIMS = "pa_health_medical_approval_claim_request_generate_other_claims";
	
	public static final String CLAIM_REQUEST_COORDINATOR = "pa_health_Claim Request coordinator events";
	
	public static final String SUBLIMIT_CHANGED_BY_SECTION = "pa_health_Set sublimit values by selecting section for claim request";
	
	public static final String GET_ICD_BLOCK = "pa_health_medical_approval_claim_request_get_icd_block";
	public static final String GET_ICD_CODE = "pa_health_medical_approval_claim_request_get_icd_code";
	
	public static final String GET_SEC_COVER = "pa_health_claim_request_get_sec_cover";
	
	public static final String GET_SUB_COVER = "pa_health_claim_request_get_sub_cover";
	
	public static final String GET_PACKAGE_RATE = "pa_health_medical_approval_claim_request_get_package_rate";
	public static final String MEDICAL_APPROVAL_REPORTED_TO_POLICE = "pa_health_medical_approval_claim_request_reported_to_police";
	public static final String COMPARE_WITH_PREVIOUS_ROD = "pa_health_claim_request_compare_with_previous_rod";

	public static final String GENERATE_DOMICILLARY_FIELDS = "pa_health_claim_request_generate_domicillary_fields";
	
	public static final String SET_DB_DETAILS_TO_REFERENCE_DATA = "pa_health_claim_request_set_db_details_to_reference_data";
	
	Map<String, Object> referenceData = new HashMap<String, Object>();
	
	public static final String CLAIM_CANCEL_ROD_EVENT_FOR_FIRST_PAGE      =  "pa_health_claim_request_cancel_rod_FIRST_PAGE";
	public static final String CLAIM_REQUEST_ESCALATE_EVENT_FOR_FIRST_PAGE = "pa_health_claim_request_escalte_button_event_FIRST_PAGE";
	public static final String CLAIM_REQUEST_ESCALATE_REPLY_EVENT_FOR_FIRST_PAGE = "pa_health_claim_request_escalte_reply_event_FIRST_PAGE";
	public static final String CLAIM_REQUEST_REFERCOORDINATOR_EVENT_FOR_FIRST_PAGE = "pa_health_claim_request_refer_to_coordinator_event_FIRST_PAGE";
	public static final String CLAIM_REQUEST_SPECIALIST_EVENT_FOR_FIRST_PAGE = "pa_health_claim_request_specialist_event_FIRST_PAGE";
	public static final String CLAIM_REQUEST_SENT_TO_REPLY_EVENT_FOR_FIRST_PAGE = "pa_health_claim_request_sent_to_reply_event_FIRST_PAGE";
	
	
	public void setDBDetailsReferenceData(
			@Observes @CDIEvent(SET_DB_DETAILS_TO_REFERENCE_DATA) final ParameterDTO parameters) {
		PreauthDTO dto = (PreauthDTO) parameters.getPrimaryParameter();
		PolicyDto policyDTO = dto.getPolicyDto();
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
			if(dto.getPolicyDto().getTotalSumInsured() != null){
			sumInsured = dto.getPolicyDto().getTotalSumInsured();
			}
		}
		
           if(dto.getPreauthDataExtractionDetails().getSection() != null){
			
			referenceData.put("sublimitDBDetails", dbCalculationService
						.getClaimedAmountDetailsForSection(policyDTO.getProduct().getKey(),
								sumInsured, 0l, insuredAge,dto.getPreauthDataExtractionDetails().getSection().getId(),
								dto.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? dto.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0", (dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : dto.getClaimDTO().getClaimSubCoverCode() ),dto.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
				
			}else{
				referenceData.put("sublimitDBDetails", dbCalculationService
						.getClaimedAmountDetailsForSection(policyDTO.getProduct().getKey(),
								sumInsured, 0l, insuredAge,0l,dto.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? dto.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0", (dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : dto.getClaimDTO().getClaimSubCoverCode() ),dto.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
			}
	}
	
	public void setUpReference(
			@Observes @CDIEvent(MEDICAL_APPROVAL_SETUP_REFERNCE) final ParameterDTO parameters) {
		
		PreauthDTO dto = (PreauthDTO) parameters.getPrimaryParameter();
		
		PolicyDto policyDTO = dto.getPolicyDto();
		
		Long productKey = 0l;
		if(null != policyDTO.getProduct())
			productKey = policyDTO.getProduct().getKey();
		
		referenceData.put("sectionDetails", masterService
				.getSectionList(dto.getNewIntimationDTO().getPolicy().getProduct().getKey(),dto.getNewIntimationDTO().getPolicy()));
		referenceData.put("treatmentType", masterService.getSelectValueContainer(ReferenceTable.TREATMENT_MANAGEMENT));
		referenceData.put("roomCategory", masterService.getSelectValueContainer(ReferenceTable.ROOM_CATEGORY));
		referenceData.put("natureOfTreatment", masterService.getSelectValueContainerForPackagePdt(ReferenceTable.NATURE_OF_TREATMENT,productKey));
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
		referenceData.put("surgicalSpeciality", preauthService.getSpecialityType("S"));
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
			if(dto.getPolicyDto().getTotalSumInsured() != null){
				sumInsured = dto.getPolicyDto().getTotalSumInsured();
			}
		}
		
		 if(dto.getPreauthDataExtractionDetails().getSection() != null){
				
				referenceData.put("sublimitDBDetails", dbCalculationService
							.getClaimedAmountDetailsForSection(policyDTO.getProduct().getKey(),
									sumInsured, 0l, insuredAge,dto.getPreauthDataExtractionDetails().getSection().getId(),
									dto.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? dto.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0", (dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : dto.getClaimDTO().getClaimSubCoverCode() ),dto.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
					
				}else{
					referenceData.put("sublimitDBDetails", dbCalculationService
							.getClaimedAmountDetailsForSection(policyDTO.getProduct().getKey(),
									sumInsured, 0l, insuredAge,0l,dto.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? dto.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0", (dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : dto.getClaimDTO().getClaimSubCoverCode() ),dto.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
				}
		
//		referenceData.put("sublimitDBDetails", dbCalculationService.getClaimedAmountDetails(dto.getPolicyDto().getProduct().getKey(), sumInsured, insuredAge));
		referenceData.put("insuredPedList", masterService.getInusredPEDList(dto.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString()));
		
		NewIntimationDto intimationDTO = dto.getNewIntimationDTO();
		Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(intimationDTO.getInsuredPatient().getInsuredId().toString(), intimationDTO.getPolicy().getKey(),intimationDTO.getInsuredPatient().getLopFlag());
		
		Reimbursement reimbursementByKey = ackDocReceivedService.getReimbursementByKey(dto.getKey());
		Long benefitKey = null != reimbursementByKey.getBenefitsId() &&  null != reimbursementByKey.getBenefitsId().getKey() ? reimbursementByKey.getBenefitsId().getKey() : ReferenceTable.DEATH_BENEFIT_MASTER_VALUE;
		
		Double balanceSI = dbCalculationService.getBalanceSIForPAHealth(intimationDTO.getInsuredPatient().getKey(),dto.getClaimKey(),intimationDTO.getPolicy().getProduct().getKey(),benefitKey).get(SHAConstants.TOTAL_BALANCE_SI);
		List<Double> copayValue = dbCalculationService.getProductCoPay(intimationDTO.getPolicy().getProduct().getKey() ,intimationDTO.getInsuredPatient().getKey(), intimationDTO.getInsuredPatient().getInsuredId(),intimationDTO);
		dto.setBalanceSI(SHAUtils.getHospOrPartialAppAmt(dto, reimbursementService, balanceSI));		
		
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
		referenceData.put("policyAgeing", dbCalculationService.getPolicyAgeing(intimationDTO.getAdmissionDate(), intimationDTO.getPolicy().getPolicyNumber()));
		
		referenceData.put("commonValues", masterService
				.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("reasonForReconsiderationRequest", masterService
				.getSelectValueContainer(ReferenceTable.REASON_FOR_RECONSIDERATION));
		referenceData.put("section", masterService
				.getSelectValueContainer(ReferenceTable.SECTION));
		referenceData.put("typeOfDelivery", masterService
				.getSelectValueContainer(ReferenceTable.DELIVERY_TYPE));
		referenceData.put("natureOfLoss", masterService
				.getNatureCauseLossSelectValueContainer(SHAConstants.PA_LOB, ReferenceTable.NATURE_OF_LOSS));
		referenceData.put("causeOfLoss", masterService
				.getNatureCauseLossSelectValueContainer(SHAConstants.PA_LOB, ReferenceTable.CAUSE_OF_LOSS));
		referenceData.put("catastrophicLoss", masterService.getCatastrophicLossList());
		
		referenceData.put("cancellationReason", masterService.getSelectValueContainer(ReferenceTable.CANCELLATION_REASON));
		
		
		
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
								preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0", (preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : preauthDTO.getClaimDTO().getClaimSubCoverCode() ),preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
				
			}else{
				referenceData.put("sublimitDBDetails", dbCalculationService
						.getClaimedAmountDetailsForSection(policyDTO.getProduct().getKey(),
								sumInsured, 0l, insuredAge,0l,preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0", (preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : preauthDTO.getClaimDTO().getClaimSubCoverCode() ),preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
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
					if(procedureDTO.getNewProcedureFlag() != null && procedure.getNewProcedureFlag() != null && procedure.getNewProcedureFlag().equals(procedureDTO.getNewProcedureFlag()) && procedureDTO.getProcedureName() != null && procedure.getProcedureID() != null && null != procedureDTO.getProcedureName().getId() && procedureDTO.getProcedureName().getId().equals(procedure.getProcedureID())) {
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
					if(procedureDTO.getNewProcedureFlag() != null && procedure.getNewProcedureFlag() != null && procedure.getNewProcedureFlag().equals(procedureDTO.getNewProcedureFlag()) && procedureDTO.getProcedureName() != null && procedure.getProcedureID() != null && null != procedureDTO.getProcedureName().getId() && procedureDTO.getProcedureName().getId().equals(procedure.getProcedureID())) {
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
	


	public void generateReferCoOrdinatorLayout(
				@Observes @CDIEvent(CLAIM_REQUEST_REFERCOORDINATOR_EVENT_FOR_FIRST_PAGE) final ParameterDTO parameters) {
			view.generateReferCoOrdinatorLayout(masterService
					.getSelectValueContainer(ReferenceTable.COORDINATOR_REQUEST_TYPE));
		}

		
	public void generateEscalateLayout(
				@Observes @CDIEvent(CLAIM_REQUEST_ESCALATE_EVENT_FOR_FIRST_PAGE) final ParameterDTO parameters) {
			view.generateEscalateLayout(masterService
					.getSelectValueContainer(ReferenceTable.ESCALATE_TO_ROD));
		}

		
	public void generateEscalateReplyLayout(
				@Observes @CDIEvent(CLAIM_REQUEST_ESCALATE_REPLY_EVENT_FOR_FIRST_PAGE) final ParameterDTO parameters) {
			view.generateEscalateReplyLayout();
		}

		
	public void generateCancelRODLayout(
				@Observes @CDIEvent(CLAIM_CANCEL_ROD_EVENT_FOR_FIRST_PAGE) final ParameterDTO parameters) {
			view.generateCancelRodLayout();
		}

		
	public void generateSpecialistLayout(
				@Observes @CDIEvent(CLAIM_REQUEST_SPECIALIST_EVENT_FOR_FIRST_PAGE) final ParameterDTO parameters) {
			view.genertateSpecialistLayout(masterService.getMasterValueByReference((ReferenceTable.SPECIALIST_TYPE)));
		}

		
	public void generateSentToReplyLayout(
				@Observes @CDIEvent(CLAIM_REQUEST_SENT_TO_REPLY_EVENT_FOR_FIRST_PAGE) final ParameterDTO parameters) {
			view.genertateSentToReplyLayout();
		}
}
