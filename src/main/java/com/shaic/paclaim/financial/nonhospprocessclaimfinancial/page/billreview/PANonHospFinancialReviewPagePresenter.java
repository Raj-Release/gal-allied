package com.shaic.paclaim.financial.nonhospprocessclaimfinancial.page.billreview;

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
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.enhacement.table.PreviousPreAuthService;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.reimbursement.billing.benefits.wizard.service.ProcessClaimRequestBenefitsService;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization.VerficationAccountDetailsService;
import com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization.VerificationAccountDeatilsTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.BankMaster;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.HospitalService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.PEDValidationService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Procedure;
import com.shaic.domain.reimbursement.BillItemMapping;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.domain.service.PreMedicalService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.OptionalCoversDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(PANonHospFinancialReviewPageWizard.class)
public class PANonHospFinancialReviewPagePresenter extends AbstractMVPPresenter<PANonHospFinancialReviewPageWizard>{
	private static final long serialVersionUID = 7488192193097057694L;
	
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private PreMedicalService preMedicalService;
	
	
	@EJB
	private ProcessClaimRequestBenefitsService benefitsService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService ackDocReceivedService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private PreviousPreAuthService previousPreauthService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private PEDValidationService pedValidationService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	
	public static final String BILLING_HOSPITAL_BENEFITS = "pa_financial_nonHosp_hospital_cash_benefits";
	public static final String BILLING_PATIENT_CARE_BENEFITS = "pa_financial_nonHosp_patiet_care_benefits";
	public static final String BILLING_REVIEW_SET_UP_REFERENCE = "pa_financial_nonHosp_review_set_up_reference";


	public static final String BILLING_TREATMENT_TYPE_CHANGED = "pa_financial_nonHosp_treatment_type_changed";


	public static final String BILLING_COORDINATOR = "pa_financial_nonHosp_coordinator";
	public static final String CLAIM_HOSPITAL_BENEFITS = "pa_claim_billig_hospital_benefits";
	public static final String CLAIM_PATIENT_CARE_BENEFITS = "pa_claim_billig_patient_care_benefits";


	public static final String BILLING_PATIENT_STATUS_CHANGED = "pa_financial_nonHosp_patient_status_changed";


	public static final String BILL_ENTRY_TABLE_CATEGORY_DROPDOWN_VALUE = "pa_financial_nonHosp_bill_entry_category_values";
	
	public static final String SAVE_PATIENT_CARE_TABLE_VALUES = "pa_Save Patient Care Table Values for Claims Billing";
	
	public static final String SAVE_HOSPITAL_CASH_TABLE_VALUES = "pa_Save Hospital Cash Table Values for Claims Billing";


	public static final String BILL_ENTRY_COMPLETION_STATUS = "pa_financial_nonHosp_bill_entry_completion_status";
	
	public static final String BILLING_DETAILS_UPDATE = "pa_financial_nonHosp_update_details";
	
	public static final String BILL_REVIEW_IRDA_LEVEL2_VALUES = "pa_financial_nonHosp_irda_level2_values";
	
	public static final String BILL_REVIEW_IRDA_LEVEL3_VALUES = "pa_financial_nonHosp_irda_level3_values";
	
	public static final String BILLING_BILL_REVIEW_IRDA_LEVEL2_VALUES = "pa_financial_nonHosp_bill_review_irda_level2_values";
	
	public static final String BILLING_BILL_REVIEW_IRDA_LEVEL3_VALUES = "pa_financial_nonHosp_bill_review_irda_level3_values";


	public static final String GET_MAPPING_DATA = "pa_financial_nonHosp_get_mapping_data";
	
	public static final String COMPARE_WITH_PREVIOUS_ROD = "pa_financial_nonHosp_billing_compare_with_previous_rod";
	
	public static final String BILLING_REFERCOORDINATOR_EVENT = "pa_financial_nonHosp_refer_to_coordinator_event_for_first_page";
	
	public static final String BILLING_CANCEL_ROD_EVENT="pa_financial_nonHosp_cancel_rod_event_for_first_page";
	
	public static final String BILLING_REFER_TO_MEDICAL_APPROVER_EVENT = "pa_financial_nonHosp_refer_to_medical_approver_event_for_first_page";
	
	public static final String BILLING_SAVE_BILL_ENTRY_VALUES = "pa_financial_nonHosp_save_bill_entry_values";
	
	public static final String FA_LOAD_BILL_DETAILS_VALUES = "pa_fa_load_bill_details_values";
	
	public static final String FINANCIAL_REFER_TO_BILL_ENTRY = "pa_financial_refer_to_bill_entry";
	
	public static final String BILLING_LOAD_BILL_DETAILS_VALUES = "pa_financial_nonHosp_load_bill_details_values";

	public static final String BILLING_REFER_TO_BILL_ENTRY = "pa_financial_nonHosp_refer_to_bill_entry";

	public static final String BILLING_REFER_TO_BILLING = "pa_financial_nonHosp_refer_to_billing";
	
	public static final String BILLING_UPDATE_PRODUCT_BASED_AMT = "pa_financial_nonHosp_update_product_based_amt";
	
	public static final String PA_CLAIM_APR_QUERY_BUTTON_EVENT = "pa_financial_nonHosp_query_button_event";
	
	public static final String PA_CLAIM_APR_PAYMENT_QUERY_BUTTON_EVENT = "pa_financial_nonHosp_payment_query_button_event";

	public static final String PA_CLAIM_APR_CANCEL_ROD_EVENT =  "pa_financial_nonHosp_cancel_rod";


	public static final String PA_CLAIM_APR_REQUEST_APPROVE_EVENT = "pa_financial_nonHosp_apporve_event";


	public static final String PA_CLAIM_APR_REQUEST_REJECTION_EVENT = "pa_financial_nonHosp_rejection_button_event";


	//public static final String CLAIM_REQUEST_REFERCOORDINATOR_EVENT = "pa_claim_request_refer_to_coordinator_event";


	public static final String PA_CLAIM_APR_REQUEST_SENT_TO_REPLY_EVENT = "pa_financial_nonHosp_sent_to_reply_event";
	
	public static final String FA_SETUP_IFSC_DETAILS = "ifsc code Search in non hosp Screen";
	
	public static final String SETUP_NOMINEE_IFSC_DETAILS_PA_FA = "ifsc code Search in Nominee Table For Non hosp FA";
	
	public static final String ADD_BANK_IFSC_DETAILS_PA_NON_HOSP_FA = "ifsc code Search in Add Banck Screen Non hosp FA";


	public static final String PA_CLAIM_FINANCIAL_INVESTIGATION_EVENT = "pa_claimFinancialNonHosp_investigation_event";

	public static final String PA_FINANCIAL_REFER_TO_CLAIM_APPROVAL = "pa_claimAprNonHosp_claim_approval_event";


	protected static final String PA_CLAIM_FINANCIAL_VISIT_EVENT = "pa_claimAprNonHosp_fvr_event";
	
	public static final String CHECK_LEGAL_HEIR_DOC_AVAILABLE_FOR_PA_NON_HOSP = "Check Legal Heir Document Available for PA Non-Hospitalisation Death";
	public static final String PA_NH_VERIFIED_ACCOUNT_DETAIL_SAVE ="PA Non hosp Save Account Verificatin Details";
	
//	public static final String PA_NH_VERIFICATION_ACCOUNT_DETAILS ="PA Non hosp Account Verificatin Details";
	public static final String PA_NH_VERIFICATION_ACCOUNT_DETAILS ="PA Non hosp Account Verificatin Details";
	
	public static final String PA_FINANCIAL_NONHOSP_TDS_PAN_VALIDATION = "pa_financial_nonhosp_tds_pan_validation";

	public static final String PA_POPULATE_OPTIONAL_COVERS_FINANCIAL_APPROVAL_NON_HOSP = "pa_populate_optional_covers_financial_non_hosp";

	@EJB
	private VerficationAccountDetailsService verficationAccountDetailsService;
	
	@EJB
	private DBCalculationService dbService;
	
	
	@EJB
	private CreateRODService rodService;


	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	Map<String, Object> referenceData = new HashMap<String, Object>();
	public void setUpReference(
			@Observes @CDIEvent(BILLING_REVIEW_SET_UP_REFERENCE) final ParameterDTO parameters) {
		
		PreauthDTO dto = (PreauthDTO) parameters.getPrimaryParameter();
		
		referenceData.put("patientStatus", masterService.getSelectValueContainer(ReferenceTable.REIMBURSEMENT_PATIENT_STATUS));
		referenceData.put("previousPreauth", previousPreauthService.search(dto.getClaimKey(),false));
		referenceData.put("status", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("fileType", masterService
				.getSelectValueContainer(ReferenceTable.PA_ROD_UPLOAD_DOC_TABLE_FILE_TYPE));
		
		referenceData.put("billClassification", setClassificationValues(dto));
		referenceData.put(SHAConstants.ALL_BILL_CLASSIFICATIONS, masterService.getMasBillClassificationValues());
		
		//referenceData.put("billClassification", masterService.getMasBillClassificationValues());
		
		Double insuredAge = dto.getNewIntimationDTO().getInsuredPatient().getInsuredAge();
		
		Double sumInsured = 0d;
		if(null != dto.getNewIntimationDTO() && null != dto.getNewIntimationDTO().getPolicy() && 
				null != dto.getNewIntimationDTO().getPolicy().getProduct() && 
				null != dto.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
				!(ReferenceTable.getGPAProducts().containsKey(dto.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
			
		 sumInsured = dbCalculationService.getInsuredSumInsured(dto.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), dto.getPolicyDto().getKey()
				 ,dto.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		}
		else
		{
			 sumInsured = dbCalculationService.getGPAInsuredSumInsured(dto.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), dto.getPolicyDto().getKey());
		}
		if (sumInsured == 0) {
			sumInsured = dto.getPolicyDto().getTotalSumInsured();
		}
		
		if(dto.getPreauthDataExtractionDetails().getSection() != null && dto.getPreauthDataExtractionDetails().getSection().getId() != null){
			referenceData.put("sublimitDBDetails", dbCalculationService.getClaimedAmountDetailsForSection(dto.getPolicyDto().getProduct().getKey(), sumInsured, 0l, insuredAge,dto.getPreauthDataExtractionDetails().getSection().getId(),
					dto.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? dto.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0",null,dto.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
		}else{
			
			referenceData.put("sublimitDBDetails", dbCalculationService.getClaimedAmountDetailsForSection(dto.getPolicyDto().getProduct().getKey(), sumInsured, 0l, insuredAge,0l,dto.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? dto.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0",null,dto.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
		}
		
		
		referenceData.put("insuredPedList", masterService.getInusredPEDList(dto.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString()));
		
		NewIntimationDto intimationDTO = dto.getNewIntimationDTO();
//		Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(intimationDTO.getInsuredPatient().getInsuredId().toString(), intimationDTO.getPolicy().getKey());
		Double balanceSI = dbCalculationService.getBalanceSIForReimbursement(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),dto.getClaimKey(),dto.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
		List<Double> copayValue = dbCalculationService.getProductCoPay(intimationDTO.getPolicy().getProduct().getKey() ,intimationDTO.getInsuredPatient().getKey(), intimationDTO.getInsuredPatient().getInsuredId(),intimationDTO);
		dto.setBalanceSI(SHAUtils.getHospOrPartialAppAmt(dto, reimbursementService, balanceSI));
		dto.setProductCopay(copayValue);
		referenceData.put("terminateCover", masterService.getSelectValueContainer(ReferenceTable.TERMINATE_COVER));
		
		referenceData.put("irdaLevel1", masterService.getIRDALevel1Values());
		referenceData.put("commonValues", masterService
				.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("reasonForReconsiderationRequest", masterService
				.getSelectValueContainer(ReferenceTable.REASON_FOR_RECONSIDERATION));
		
		referenceData.put("illness", masterService.getSelectValueContainer(ReferenceTable.ILLNESS));
		referenceData.put("criticalIllness", preauthService.getCriticalIllenssMasterValues(dto));
		referenceData.put("section", masterService
				.getSelectValueContainer(ReferenceTable.SECTION));
		
		if(dto.getPreauthDataExtractionDetails().getSection() != null && dto.getPreauthDataExtractionDetails().getSection().getId() != null){
			
			MastersValue master = masterService.getMaster(dto.getPreauthDataExtractionDetails().getSection().getId());
			if(master != null){
				SelectValue section = dto.getPreauthDataExtractionDetails().getSection();
				section.setValue(master.getValue());
			}
			
		}
		
		referenceData.put("roomCategory", masterService.getSelectValueContainer(ReferenceTable.ROOM_CATEGORY));
		referenceData.put(SHAConstants.BILL_CLASSIFICATION_DETAILS, validateBillClassification(dto.getClaimDTO().getKey()));
		getCoversAndOptionalCoverValues(dto);
		
		/*for bancs*/
		referenceData.put("natureOfLoss", masterService
				.getNatureCauseLossSelectValueContainer(SHAConstants.PA_LOB, ReferenceTable.NATURE_OF_LOSS));
		referenceData.put("causeOfLoss", masterService
				.getNatureCauseLossSelectValueContainer(SHAConstants.PA_LOB, ReferenceTable.CAUSE_OF_LOSS));
		referenceData.put("catastrophicLoss", masterService.getCatastrophicLossList());
		
		view.setupReferences(referenceData);
	}
	
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
			//reimbursementDTO.getDocumentDetails().setOptionalCovers(dbCalculationService.getBebefitAdditionalCovers(SHAConstants.OPTIONAL_COVER, productCode , insuredKey));
		}	
	}
	
	public List<DocumentDetailsDTO> validateBillClassification(Long claimKey) {
		List<Reimbursement> reimbursementList = ackDocReceivedService.getReimbursementDetailsForBillClassificationValidation(claimKey);
		List<DocumentDetailsDTO> docDTOList = new ArrayList<DocumentDetailsDTO>();
		if(null != reimbursementList && !reimbursementList.isEmpty())
		{
			for (Reimbursement reimbursement : reimbursementList) {
				DocAcknowledgement docAck= reimbursement.getDocAcknowLedgement();
				DocumentDetailsDTO docDTO = new DocumentDetailsDTO();
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
	
	
	public void saveBillValues(
			@Observes @CDIEvent(BILLING_SAVE_BILL_ENTRY_VALUES) final ParameterDTO parameters) {
			//Boolean status = (Boolean) parameters.getPrimaryParameter();
			UploadDocumentDTO	uploadDTO = (UploadDocumentDTO) parameters.getPrimaryParameter();
			/*String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
			uploadDTO.setUsername(userId);*/
			rodService.saveBillEntryValues(uploadDTO);
			//view.setBillEntryFinalStatus(uploadDTO);
		
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
				/*if(("Hospitalization").equalsIgnoreCase(selectValue.getValue()) && (null != preauthDTO.getHospitalizaionFlag() && preauthDTO.getHospitalizaionFlag())
						|| (null != preauthDTO.getIsHospitalizationRepeat() && preauthDTO.getIsHospitalizationRepeat())
						)*/
				if(("Hospitalization").equalsIgnoreCase(selectValue.getValue()) && ((null != preauthDTO.getHospitalizaionFlag() && preauthDTO.getHospitalizaionFlag())
						|| (null != preauthDTO.getIsHospitalizationRepeat() && preauthDTO.getIsHospitalizationRepeat()) || (null != preauthDTO.getPartialHospitalizaionFlag() &&  preauthDTO.getPartialHospitalizaionFlag())
						))
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

	
	public void billingDetailsUpdatedEvent(@Observes @CDIEvent(BILLING_DETAILS_UPDATE) final ParameterDTO parameters)
	{
		PreauthDTO preauthDTO =(PreauthDTO) parameters.getPrimaryParameter();
//		Reimbursement reimbursement = reimbursementService.saveUpdatedBillDetails(preauthDTO);
		reimbursementService.billEntrySave(preauthDTO);
		Reimbursement reimbursement =reimbursementService.getReimbursementByKey(preauthDTO.getKey());
		dbCalculationService.getBillDetailsSummary(reimbursement.getKey());
		
	}
	
	public void getMappingData(@Observes @CDIEvent(GET_MAPPING_DATA) final ParameterDTO parameters) {
		Long key = (Long) parameters.getPrimaryParameter();
		Object[] secondaryParameters = parameters.getSecondaryParameters();
		Boolean isInvokeForOneToOne = false;
		if(secondaryParameters != null && secondaryParameters.length > 0) {
			isInvokeForOneToOne = (Boolean) secondaryParameters[0];
		}
		
		List<BillItemMapping> mappingData = reimbursementService.getMappingData(key);
		view.setMappingData(mappingData, isInvokeForOneToOne);
	}
	
	public void savePatientCareValues(@Observes @CDIEvent(SAVE_PATIENT_CARE_TABLE_VALUES) final ParameterDTO parameters)
	{
		PreauthDTO rodDTO = (PreauthDTO)parameters.getPrimaryParameter();
		List<AddOnBenefitsDTO> addOnBenefitsDTO = benefitsService.updatePatientCareValues(rodDTO);
		view.setBenefitsData(addOnBenefitsDTO);
		//Boolean selectedValue = (Boolean) parameters.getPrimaryParameter();
		//view.generateFieldsBasedOnHospitalCashBenefits(selectedValue);
	}
	
	public void saveHospitalCashValue(@Observes @CDIEvent(SAVE_HOSPITAL_CASH_TABLE_VALUES) final ParameterDTO parameters)
	{
		PreauthDTO rodDTO = (PreauthDTO)parameters.getPrimaryParameter();
		List<AddOnBenefitsDTO> addOnBenefitsDTO = benefitsService.updateHospitalCashValues(rodDTO);
		view.setBenefitsData(addOnBenefitsDTO);
		//Boolean selectedValue = (Boolean) parameters.getPrimaryParameter();
		//view.generateFieldsBasedOnHospitalCashBenefits(selectedValue);
	}
	
	 public void compareWithPreviousROD(@Observes @CDIEvent(COMPARE_WITH_PREVIOUS_ROD) final ParameterDTO parameters) {
			PreauthDTO bean = (PreauthDTO) parameters.getPrimaryParameter();
			Reimbursement previousLatestROD = reimbursementService.getFilteredPreviousLatestROD(bean.getClaimKey(), bean.getKey());
			String comparisonResult = "";
			if(previousLatestROD != null) {
				
				List<PedValidation> findPedValidationByPreauthKey = preauthService.findPedValidationByPreauthKey(previousLatestROD.getKey());
				List<Procedure> findProcedureByPreauthKey = preauthService.findProcedureByPreauthKey(previousLatestROD.getKey());
//				if(!previousLatestROD.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase(docAckknowledgement.getHospitalisationFlag())) {
//					
//				} else if(!previousLatestROD.getDocAcknowLedgement().getPreHospitalisationFlag().equalsIgnoreCase(docAckknowledgement.getPreHospitalisationFlag())) {
//					
//				} else if(!previousLatestROD.getDocAcknowLedgement().getPostHospitalisationFlag().equalsIgnoreCase(docAckknowledgement.getPostHospitalisationFlag())) {
//					
//				} else if(!previousLatestROD.getDocAcknowLedgement().getPartialHospitalisationFlag().equalsIgnoreCase(docAckknowledgement.getPartialHospitalisationFlag())) {
//					
//				}
				
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
						if(procedure.getNewProcedureFlag().equals(procedureDTO.getNewProcedureFlag()) && procedureDTO.getProcedureName() != null && procedure.getProcedureID() != null && procedureDTO.getProcedureName().getId().equals(procedure.getProcedureID())) {
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
//				if(previousLatestROD.getPayeeName() != null && bean.getPayeeName() != null && !previousLatestROD.getPayeeName().equals(bean.getPayeeName())) {
//					comparisonResult += "Previous Insured Patient Name : " + (previousLatestROD.getPayeeName()) + " Current Insured Patient Name : " + (bean.getPayeeName());
//				}
				
			}
			
			view.setCompareWithRODResult(comparisonResult);
		}
	 
	 public void generateReferCoOrdinatorLayout(@Observes @CDIEvent(BILLING_REFERCOORDINATOR_EVENT) final ParameterDTO parameters)
		{
			view.generateReferCoOrdinatorLayout(masterService.getSelectValueContainer(ReferenceTable.COORDINATOR_REQUEST_TYPE));
		}
	 
	 public void generateCancelRODLayout(@Observes @CDIEvent(BILLING_CANCEL_ROD_EVENT) final ParameterDTO parameters)
		{
			view.generateCancelRODLayout(masterService.getSelectValueContainer(ReferenceTable.CANCELLATION_REASON));
		}
	 
	 public void generateReferToMedicalApproverLayout(@Observes @CDIEvent(BILLING_REFER_TO_MEDICAL_APPROVER_EVENT) final ParameterDTO parameters){
			view.generateReferToMedicalApproverLayout();
	 }

	 public void generateReferToBillEntryLayout(@Observes @CDIEvent(BILLING_REFER_TO_BILL_ENTRY) final ParameterDTO parameters){
			view.generateReferToBillEntryLayout();
	 }
	 
	 public void generateReferToBillingLayout(@Observes @CDIEvent(BILLING_REFER_TO_BILLING) final ParameterDTO parameters){
			view.generateReferToBillingLayout();
	 }
	 
	 public void generateQueryLayout(
				@Observes @CDIEvent(PA_CLAIM_APR_QUERY_BUTTON_EVENT) final ParameterDTO parameters) {
			view.generateQueryLayout();
		}

	 public void generatePaymentQueryLayout(
				@Observes @CDIEvent(PA_CLAIM_APR_PAYMENT_QUERY_BUTTON_EVENT) final ParameterDTO parameters) {
			view.generatePaymentLayout();
		}
	 
	 public void generateApproveLayout(
				@Observes @CDIEvent(PA_CLAIM_APR_REQUEST_APPROVE_EVENT) final ParameterDTO parameters) {
			view.generateApproveLayout();
		}

	 public void generateRejectLayout(
				@Observes @CDIEvent(PA_CLAIM_APR_REQUEST_REJECTION_EVENT) final ParameterDTO parameters) {

			view.generateRejectionLayout(masterService.getReimbRejCategoryByValue());
//											masterService.getSelectValueContainer(ReferenceTable.REJECTION_CATEGORY));
		}
	 
	 public void generateSentToReplyLayout(
				@Observes @CDIEvent(PA_CLAIM_APR_REQUEST_SENT_TO_REPLY_EVENT) final ParameterDTO parameters) {
			view.genertateSentToReplyLayout();
		}
	 

		public void setupIFSCDetails(
				@Observes @CDIEvent(FA_SETUP_IFSC_DETAILS) final ParameterDTO parameters) {
			ViewSearchCriteriaTableDTO viewSearchCriteriaDTO = (ViewSearchCriteriaTableDTO) parameters.getPrimaryParameter();
			view.setUpIFSCDetails(viewSearchCriteriaDTO);
		}
		
		public void generateInvestigationLayout(
				@Observes @CDIEvent(PA_CLAIM_FINANCIAL_INVESTIGATION_EVENT) final ParameterDTO parameters) {
			view.genertateInvestigationLayout(masterService
					.getSelectValueContainer(ReferenceTable.ALLOCATION_TO_INVESTIGATION));
		}
		
		public void generateClaimApprovalLayout(
				@Observes @CDIEvent(PA_FINANCIAL_REFER_TO_CLAIM_APPROVAL) final ParameterDTO parameters) {
			view.genertateClaimAprlayout();
		}
		
		public void generateFieldVisitLayout(
				@Observes @CDIEvent(PA_CLAIM_FINANCIAL_VISIT_EVENT) final ParameterDTO parameters) {
			view.genertateFieldsBasedOnFieldVisit(masterService
					.getSelectValueContainer(ReferenceTable.ALLOCATION_TO)
					,masterService.getSelectValueContainer(ReferenceTable.ASSIGN_TO),masterService.getSelectValueContainer(ReferenceTable.FVR_PRIORITY));
		}
		
		public void getLegalHeirDocAvailableByIntimation(@Observes @CDIEvent(CHECK_LEGAL_HEIR_DOC_AVAILABLE_FOR_PA_NON_HOSP) final ParameterDTO parameters)
		{
			PreauthDTO preauthDto = (PreauthDTO) parameters.getPrimaryParameter();
			boolean available = masterService.getLegalHeirDocAvailableByIntimation(ReferenceTable.HEALTH_LEGAL_HEIR_DOC_KEY, preauthDto.getNewIntimationDTO().getIntimationId());
			
			preauthDto.getClaimDTO().setLegalHeirDocAvailable(available);			
		}		

		@SuppressWarnings("static-access")
		public void saveVerifiedAccount(
				@Observes @CDIEvent(PA_NH_VERIFIED_ACCOUNT_DETAIL_SAVE) final ParameterDTO parameters) {		
			PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
			verficationAccountDetailsService.submitAccountVerificationDetails(preauthDTO.getVerificationAccountDeatilsTableDTO(),SHAConstants.FA_PA_NH_VERIFICATION);
			preauthDTO.setVerificationClicked(true);
		}
		
		/*public void setVerifictaionAccountDetails(
				@Observes @CDIEvent(PA_NH_VERIFICATION_ACCOUNT_DETAILS) final ParameterDTO parameters) {
			PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
			String documentReceivedFrom = verficationAccountDetailsService.getDocumentReceivedFrom(preauthDTO.getDocumentReceivedFromId());
			List<VerificationAccountDeatilsTableDTO> verifiedAccountDetails = dbService.getVerifiedAccountDetails(preauthDTO.getPreauthDataExtractionDetails().getAccountNo());
			preauthDTO.setVerificationAccountDeatilsTableDTO(verifiedAccountDetails);
			
		}*/
		public void setupAddBankIFSCDetails(
				@Observes @CDIEvent(ADD_BANK_IFSC_DETAILS_PA_NON_HOSP_FA) final ParameterDTO parameters) {
			ViewSearchCriteriaTableDTO viewSearchCriteriaDTO = (ViewSearchCriteriaTableDTO) parameters.getPrimaryParameter();
			
			BankMaster masBank = masterService.getBankDetails(viewSearchCriteriaDTO.getIfscCode());
			
			if(masBank != null) {
				viewSearchCriteriaDTO.setBankId(masBank.getKey());
				viewSearchCriteriaDTO.setBankName(masBank.getBankName());
				viewSearchCriteriaDTO.setBranchName(masBank.getBranchName());
				viewSearchCriteriaDTO.setCity(masBank.getCity());
			}
			
			view.setUpAddBankIFSCDetails(viewSearchCriteriaDTO);
		}
		
		public void setupNomineeIFSCDetails(
				@Observes @CDIEvent(SETUP_NOMINEE_IFSC_DETAILS_PA_FA) final ParameterDTO parameters) {
			ViewSearchCriteriaTableDTO viewSearchCriteriaDTO = (ViewSearchCriteriaTableDTO) parameters.getPrimaryParameter();
			
			BankMaster masBank = masterService.getBankDetails(viewSearchCriteriaDTO.getIfscCode());
			
			if(masBank != null) {
				viewSearchCriteriaDTO.setBankId(masBank.getKey());
				viewSearchCriteriaDTO.setBankName(masBank.getBankName());
				viewSearchCriteriaDTO.setBranchName(masBank.getBranchName());
				viewSearchCriteriaDTO.setCity(masBank.getCity());
			}
			
			view.setUpNomineeIFSCDetails(viewSearchCriteriaDTO);
		}
		
		public void setupPanDetailsMandatory(@Observes @CDIEvent(PA_FINANCIAL_NONHOSP_TDS_PAN_VALIDATION) final ParameterDTO parameters)
		{
			boolean panDetails = (boolean) parameters.getPrimaryParameter();
			view.setupPanDetailsMandatory(panDetails);
		}
		
		//R1022
		public void populateApproveAmntOPT(@Observes @CDIEvent(PA_POPULATE_OPTIONAL_COVERS_FINANCIAL_APPROVAL_NON_HOSP) final ParameterDTO parameters)
		{
			List<OptionalCoversDTO> optListFinancialpproval = (List<OptionalCoversDTO>) parameters.getPrimaryParameter();
			view.populateApproveAmntOPT(optListFinancialpproval);
		}
}
