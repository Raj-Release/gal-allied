package com.shaic.claim.reimbursement.billing.pages.billreview;

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
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.enhacement.table.PreviousPreAuthService;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.premedical.dto.OtherBenefitsTableDto;
import com.shaic.claim.reimbursement.billing.benefits.wizard.service.ProcessClaimRequestBenefitsService;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.BankMaster;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Intimation;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.PEDValidationService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Procedure;
import com.shaic.domain.reimbursement.BillItemMapping;
import com.shaic.domain.reimbursement.ReimbursementCalCulationDetails;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.domain.service.PreMedicalService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(BillingReviewPageWizard.class)
public class BillingReviewPagePresenter extends AbstractMVPPresenter<BillingReviewPageWizard>{
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
	
	public static final String BILLING_HOSPITAL_BENEFITS = "billing_hospital_cash_benefits";
	public static final String BILLING_PATIENT_CARE_BENEFITS = "billing_patiet_care_benefits";
	public static final String BILLING_REVIEW_SET_UP_REFERENCE = "billing_review_set_up_reference";


	public static final String BILLING_TREATMENT_TYPE_CHANGED = "billing_treatment_type_changed";
	public static final String BILLING_VENTILATOR_SUPPORT = "billing_ventilator_support";

	public static final String BILLING_COORDINATOR = "billing_coordinator";
	public static final String CLAIM_HOSPITAL_BENEFITS = "claim_billig_hospital_benefits";
	public static final String CLAIM_PATIENT_CARE_BENEFITS = "claim_billig_patient_care_benefits";


	public static final String BILLING_PATIENT_STATUS_CHANGED = "billing_patient_status_changed";


	public static final String BILL_ENTRY_TABLE_CATEGORY_DROPDOWN_VALUE = "billing_bill_entry_category_values";
	
	public static final String SAVE_PATIENT_CARE_TABLE_VALUES = "Save Patient Care Table Values for Claims Billing";
	
	public static final String SAVE_HOSPITAL_CASH_TABLE_VALUES = "Save Hospital Cash Table Values for Claims Billing";
	
	public static final String SAVE_OTHER_BENEFITS_TABLE_VALUES = "Save other benefits value";


	public static final String GET_SEC_COVER = "billing_get_sec_cover";
	
	public static final String GET_SUB_COVER = "billing_get_sub_cover";
	
	public static final String BILL_ENTRY_COMPLETION_STATUS = "billing_bill_entry_completion_status";
	
	public static final String BILLING_DETAILS_UPDATE = "billing_update_details";
	
	public static final String BILL_REVIEW_IRDA_LEVEL2_VALUES = "bill_review_irda_level2_values";
	
	public static final String BILL_REVIEW_IRDA_LEVEL3_VALUES = "bill_review_irda_level3_values";
	
	public static final String BILLING_BILL_REVIEW_IRDA_LEVEL2_VALUES = "billing_bill_review_irda_level2_values";
	
	public static final String BILLING_BILL_REVIEW_IRDA_LEVEL3_VALUES = "billing_bill_review_irda_level3_values";


	public static final String GET_MAPPING_DATA = "billing_get_mapping_data";
	
	public static final String COMPARE_WITH_PREVIOUS_ROD = "process_Claim_billing_compare_with_previous_rod";
	
	public static final String BILLING_REFERCOORDINATOR_EVENT = "billing_refer_to_coordinator_event_for_first_page";
	
	public static final String BILLING_CANCEL_ROD_EVENT="billing_cancel_rod_event_for_first_page";
	
	public static final String BILLING_REFER_TO_MEDICAL_APPROVER_EVENT = "billing_refer_to_medical_approver_event_for_first_page";
	
	public static final String BILLING_SAVE_BILL_ENTRY_VALUES = "billing_save_bill_entry_values";
	
	public static final String BILLING_LOAD_BILL_DETAILS_VALUES = "billing_load_bill_details_values";

	public static final String BILLING_REFER_TO_BILL_ENTRY = "billing_refer_to_bill_entry";

	public static final String BILLING_UPDATE_PRODUCT_BASED_AMT = "billing_update_product_based_amt";
	
	public static final String CHECK_CRITICAL_ILLNESS = "billing_check_critical_illness";
	
	public static final String REFERENCE_DATA_CLEAR = "reference date clear in Billing review page";
	
	public static final String GET_RTA_BALANCE_SI = "Get_RTA_Balance_SI";
	
	public static final String GET_BENEFIT_DETAILS = "Get benefit details for Billing";

	public static final String SETUP_NOMINEE_IFSC_DETAILS_BILLING = "setup Nominee IFSC For Billing Screen";
	
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
		Boolean isBasedOnPremium = false;
		if(ReferenceTable.getPremiumDeductionProductKeys().containsKey(dto.getNewIntimationDTO().getPolicy().getProduct().getKey()) && (dto.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) && (dto.getPreauthDataExtractionDetails().getDocAckknowledgement().getHospitalisationFlag() != null && dto.getPreauthDataExtractionDetails().getDocAckknowledgement().getHospitalisationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG))) {
			isBasedOnPremium = true;
		}
		referenceData.put("sectionDetails", masterService
				.getSectionList(dto.getNewIntimationDTO().getPolicy().getProduct().getKey(),dto.getNewIntimationDTO().getPolicy()));
		referenceData.put("patientStatus", masterService.getSelectValueContainer(ReferenceTable.REIMBURSEMENT_PATIENT_STATUS));
		referenceData.put("previousPreauth", previousPreauthService.search(dto.getClaimKey(), isBasedOnPremium));
		referenceData.put("status", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("fileType", masterService
				.getSelectValueContainer(ReferenceTable.ROD_UPLOAD_DOC_TABLE_FILE_TYPE));
		
		
		referenceData.put("billClassification", setClassificationValues(dto));
		referenceData.put(SHAConstants.ALL_BILL_CLASSIFICATIONS, masterService.getMasBillClassificationValues());
		
		//referenceData.put("billClassification", masterService.getMasBillClassificationValues());
		
		Double insuredAge = dto.getNewIntimationDTO().getInsuredPatient().getInsuredAge();
		Double sumInsured = dbCalculationService.getInsuredSumInsured(dto.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), dto.getPolicyDto().getKey(),dto.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		if (sumInsured == 0) {
			sumInsured = dto.getPolicyDto().getTotalSumInsured();
		}
		
		if(dto.getPreauthDataExtractionDetails().getSection() != null && dto.getPreauthDataExtractionDetails().getSection().getId() != null){
			if(ReferenceTable.getGMCProductList().containsKey(dto.getNewIntimationDTO().getPolicy().getProduct().getKey()) ||
					ReferenceTable.STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS_KEY.equals(dto.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(dto.getNewIntimationDTO().getPolicy().getKey(),
						dto.getNewIntimationDTO().getInsuredPatient().getKey(),dto.getNewIntimationDTO().getPolicy().getSectionCode());
				referenceData.put("sublimitDBDetails", dbCalculationService.getClaimedAmountDetailsForSectionForGMC(dto.getPolicyDto().getKey(), sumInsured, insuredAge,dto.getPreauthDataExtractionDetails().getSection().getId(),
						dto.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? dto.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0", (dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : dto.getClaimDTO().getClaimSubCoverCode())));
			}else{
				
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
				
				referenceData.put("sublimitDBDetails", dbCalculationService.getClaimedAmountDetailsForSection(dto.getPolicyDto().getProduct().getKey(), sumInsured, 0l, insuredAge,dto.getPreauthDataExtractionDetails().getSection().getId(),
						policyPlan, (dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : dto.getClaimDTO().getClaimSubCoverCode()),dto.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
			}
			
		}else{
			if(ReferenceTable.getGMCProductList().containsKey(dto.getNewIntimationDTO().getPolicy().getProduct().getKey()) ||
					ReferenceTable.STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS_KEY.equals(dto.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(dto.getNewIntimationDTO().getPolicy().getKey(),
						dto.getNewIntimationDTO().getInsuredPatient().getKey(),dto.getNewIntimationDTO().getPolicy().getSectionCode());
				referenceData.put("sublimitDBDetails", dbCalculationService.getClaimedAmountDetailsForSectionForGMC(dto.getPolicyDto().getKey(), sumInsured, insuredAge,0l,"0", (dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : dto.getClaimDTO().getClaimSubCoverCode())));
			}else{
				referenceData.put("sublimitDBDetails", dbCalculationService.getClaimedAmountDetailsForSection(dto.getPolicyDto().getProduct().getKey(), sumInsured,0l, insuredAge,0l,dto.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? dto.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0", (dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? dto.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : dto.getClaimDTO().getClaimSubCoverCode()),dto.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
			}
			
		}
		
		
		referenceData.put("insuredPedList", masterService.getInusredPEDList(dto.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString()));
		
		NewIntimationDto intimationDTO = dto.getNewIntimationDTO();
//		Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(intimationDTO.getInsuredPatient().getInsuredId().toString(), intimationDTO.getPolicy().getKey());
		Double balanceSI = 0d;
		List<Double> copayValue = new ArrayList<Double>();
		
		 if(ReferenceTable.getGMCProductList().containsKey(intimationDTO.getPolicy().getProduct().getKey())){
			balanceSI = dbCalculationService.getBalanceSIForGMC(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),dto.getClaimKey());
			copayValue = dbCalculationService.getProductCoPayForGMC(intimationDTO.getPolicy().getKey(),intimationDTO.getInsuredPatient().getKey());
		 }else{
			 //code added for cr modern sublimit SI by noufel
			 if(dto.getIsBalSIForSublimitCardicSelected()){
				 balanceSI = dbCalculationService.getBalanceSIForCardicCarePlatRemb(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),dto.getClaimKey(),dto.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
			 }
			 if(dto.getIsModernSublimitSelected()){
				 balanceSI = dbCalculationService.getBalanceSIForModernSublimit(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),dto.getClaimKey(),dto.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
			 }else{
				 balanceSI = dbCalculationService.getBalanceSIForReimbursement(intimationDTO.getPolicy().getKey() ,intimationDTO.getInsuredPatient().getKey(),dto.getClaimKey(),dto.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
			 }
			 copayValue = dbCalculationService.getProductCoPay(intimationDTO.getPolicy().getProduct().getKey() ,intimationDTO.getInsuredPatient().getKey(), intimationDTO.getInsuredPatient().getInsuredId(),
					 intimationDTO);
		 }
		
		
		dto.setBalanceSI(SHAUtils.getHospOrPartialAppAmt(dto, reimbursementService, balanceSI));
		
		Reimbursement reimbursementByKey = ackDocReceivedService.getReimbursementByKey(dto.getKey());
		
		if(reimbursementByKey != null && reimbursementByKey.getDocAcknowLedgement() != null){
			Boolean isSettled = reimbursementService.isSettledPaymentAvailable(reimbursementByKey.getRodNumber());
			DocAcknowledgement docAcknowLedgement = reimbursementByKey.getDocAcknowLedgement();
			Double preHospitalLAlreadyPaid = 0d;
			Double postHospitalAlreadyPaid = 0d;
			Double prepostAlreadyPaid = 0d;
			if(isSettled){
				if(reimbursementByKey.getReconsiderationRequest() != null && reimbursementByKey.getReconsiderationRequest().equalsIgnoreCase("Y") ){
					Double alreadyPaidAmt = 0d;
					List<ReimbursementCalCulationDetails> reimbursementCalculationDetails = ackDocReceivedService.getReimbursementCalculationDetails(dto.getKey());
					for (ReimbursementCalCulationDetails reimbursementCalCulationDetails2 : reimbursementCalculationDetails) {
						
						if(docAcknowLedgement.getDocumentReceivedFromId() != null && docAcknowLedgement.getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
							if(reimbursementCalCulationDetails2.getAmountAlreadyPaidAmt() !=null && reimbursementCalCulationDetails2.getAmountAlreadyPaidAmt() > 0){
								alreadyPaidAmt += reimbursementCalCulationDetails2.getAmountAlreadyPaidAmt() != null ? reimbursementCalCulationDetails2.getAmountAlreadyPaidAmt() :0;
							} else {
								alreadyPaidAmt += reimbursementCalCulationDetails2.getPayableToHospAftTDS() != null ? reimbursementCalCulationDetails2.getPayableToHospAftTDS() : 0;
						}
						}else{
							//IMSSUPPOR-27457
							if(ReferenceTable.FINANCIAL_REFER_TO_BILLING.equals(reimbursementByKey.getStatus().getKey())){
								if(reimbursementCalCulationDetails2.getAmountAlreadyPaidAmt() != null){
									if(reimbursementCalCulationDetails2.getBillClassificationId() != null && ! reimbursementCalCulationDetails2.getBillClassificationId().equals(ReferenceTable.PRE_HOSPITALIZATION)
											&& ! reimbursementCalCulationDetails2.getBillClassificationId().equals(ReferenceTable.POST_HOSPITALIZATION)){
										alreadyPaidAmt += reimbursementCalCulationDetails2.getAmountAlreadyPaidAmt();
									}
									/*if(reimbursementCalCulationDetails2.getPayableToHospital() != null){
										alreadyPaidAmt += reimbursementCalCulationDetails2.getPayableToHospital();
									}*/
								}
							}else{
							if(reimbursementCalCulationDetails2.getPayableInsuredAfterPremium() != null){
								if(reimbursementCalCulationDetails2.getBillClassificationId() != null && ! reimbursementCalCulationDetails2.getBillClassificationId().equals(ReferenceTable.PRE_HOSPITALIZATION)
										&& ! reimbursementCalCulationDetails2.getBillClassificationId().equals(ReferenceTable.POST_HOSPITALIZATION)){
									alreadyPaidAmt += reimbursementCalCulationDetails2.getPayableInsuredAfterPremium();
								}
								/*if(reimbursementCalCulationDetails2.getPayableToHospital() != null){
									alreadyPaidAmt += reimbursementCalCulationDetails2.getPayableToHospital();
								}*/
							}
							}
						}
					}
					
					dto.setBalanceSI(dto.getBalanceSI()+alreadyPaidAmt);
					
				}
			}
			
			/*if(reimbursementByKey.getDocAcknowLedgement().getPreHospitalisationFlag().toLowerCase().equalsIgnoreCase("y")) {
			preHospitalLAlreadyPaid = getPreHospitalizationRODValues(dto.getClaimKey(), dto.getKey());
			}
			if(reimbursementByKey.getDocAcknowLedgement().getPostHospitalisationFlag().toLowerCase().equalsIgnoreCase("y")) {
			postHospitalAlreadyPaid = getPostHospitalizationRODValues(dto.getClaimKey(), dto.getKey());
			}
			
			prepostAlreadyPaid = (preHospitalLAlreadyPaid != null ? preHospitalLAlreadyPaid :0) + (postHospitalAlreadyPaid != null ? postHospitalAlreadyPaid :0);
			dto.setBalanceSI(dto.getBalanceSI()+prepostAlreadyPaid);*/
			
			
		}
		
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
		
		referenceData.put("reasonForNotPaying", masterService.getSelectValueContainer(ReferenceTable.PED_NON_PAYABLE_REASON));
		
		if(dto.getPreauthDataExtractionDetails().getSection() != null && dto.getPreauthDataExtractionDetails().getSection().getId() != null){
			
			MastersValue master = masterService.getMaster(dto.getPreauthDataExtractionDetails().getSection().getId());
			if(master != null){
				SelectValue section = dto.getPreauthDataExtractionDetails().getSection();
				section.setValue(master.getValue());
			}
			
		}
		//System.out.println(String.format("Ventilator Support in vpresenter [%s]", dto.getPreauthDataExtractionDetails().getVentilatorSupport().toString()));
		referenceData.put("roomCategory", masterService.getSelectValueContainer(ReferenceTable.ROOM_CATEGORY));
		referenceData.put(SHAConstants.BILL_CLASSIFICATION_DETAILS, validateBillClassification(dto.getClaimDTO().getKey()));
		referenceData.put("natureOfLoss", masterService
				.getNatureCauseLossSelectValueContainer(SHAConstants.HEALTH_LOB, ReferenceTable.NATURE_OF_LOSS));
		referenceData.put("causeOfLoss", masterService
				.getNatureCauseLossSelectValueContainer(SHAConstants.PA_LOB, ReferenceTable.CAUSE_OF_LOSS));
		referenceData.put("catastrophicLoss", masterService.getCatastrophicLossList());
		referenceData.put("treatcareHealthcorona", masterService
				.getSelectValueContainer(ReferenceTable.HOME_CARE_TREATMENT_KAVACH));
		
		referenceData.put("patientDayCareValueContainer", masterService.getSelectValueContainer(ReferenceTable.HOSPITAL_CASH_PATIENT_DAY_CARE));
		referenceData.put("hospitalCashDueTo", masterService.getSelectValueContainer(ReferenceTable.HOSPITAL_CASH_DUE_TO));
		
		view.setupReferences(referenceData);
	}
	
	
	
	public List<DocumentDetailsDTO> validateBillClassification(Long claimKey) {
		List<Reimbursement> reimbursementList = ackDocReceivedService.getReimbursementDetailsForBillClassificationValidation(claimKey);
		List<DocumentDetailsDTO> docDTOList = new ArrayList<DocumentDetailsDTO>();
		if(null != reimbursementList && !reimbursementList.isEmpty())
		{
			DocumentDetailsDTO docDTO = null;
			for (Reimbursement reimbursement : reimbursementList) {
				DocAcknowledgement docAck= reimbursement.getDocAcknowLedgement();
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
	
	public void loadBillDetailsValues(
			@Observes @CDIEvent(BILLING_LOAD_BILL_DETAILS_VALUES) final ParameterDTO parameters) {
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

	
	public void billingDetailsUpdatedEvent(@Observes @CDIEvent(BILLING_DETAILS_UPDATE) final ParameterDTO parameters)
	{
		PreauthDTO preauthDTO =(PreauthDTO) parameters.getPrimaryParameter();
//		Reimbursement reimbursement = reimbursementService.saveUpdatedBillDetails(preauthDTO);
//		reimbursementService.billEntrySave(preauthDTO);
		Reimbursement reimbursement =reimbursementService.getReimbursementByKey(preauthDTO.getKey());
		//added by noufel fro GMC prop CR
		if(!(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_PRODUCT_KEY)
				|| preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY))){
			dbCalculationService.getBillDetailsSummary(reimbursement.getKey());
		} 
		else if((preauthDTO.getProrataDeductionFlag() != null && preauthDTO.getProrataDeductionFlag().equalsIgnoreCase("N")) &&
				(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_PRODUCT_KEY)
						|| preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY))){
			dbCalculationService.getBillDetailsSummary(reimbursement.getKey());
		}
		else if(preauthDTO.getIsCashlessPropDedSelected() && (preauthDTO.getProrataDeductionFlag() != null && preauthDTO.getProrataDeductionFlag().equalsIgnoreCase("Y")) &&
				(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_PRODUCT_KEY)
				|| preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY))){
			dbCalculationService.getBillDetailsSummary(reimbursement.getKey());
		}
		
	}
	
	public void setBillEntryStatus(
			@Observes @CDIEvent(BILL_ENTRY_COMPLETION_STATUS) final ParameterDTO parameters) {
			//Boolean status = (Boolean) parameters.getPrimaryParameter();
			UploadDocumentDTO	uploadDTO = (UploadDocumentDTO) parameters.getPrimaryParameter();
			rodService.saveBillEntryValues(uploadDTO);
			view.setBillEntryFinalStatus(uploadDTO);
		
	}
	
	public void generateFieldsBasedOnTreatement(@Observes @CDIEvent(BILLING_TREATMENT_TYPE_CHANGED) final ParameterDTO parameters)
	{
		view.generateFieldsBasedOnTreatment();
	}
	
	public void coordinatorEvents(@Observes @CDIEvent(BILLING_COORDINATOR) final ParameterDTO parameters)
	{
		view.intiateCoordinatorRequest();
	}
	
	public void generateFieldsBasedOnPatientStatus(@Observes @CDIEvent(BILLING_PATIENT_STATUS_CHANGED) final ParameterDTO parameters)
	{
		view.genertateFieldsBasedOnPatientStaus();
	}
	
	public void generateFieldsBasedHospitalCashAddOnBenefits(@Observes @CDIEvent(CLAIM_HOSPITAL_BENEFITS) final ParameterDTO parameters)
	{
		Boolean selectedValue = (Boolean) parameters.getPrimaryParameter();
		view.generateFieldsBasedOnHospitalCashBenefits(selectedValue);
	}
	
	public void generateFieldsBasedPatientAddOnBenefits(@Observes @CDIEvent(CLAIM_PATIENT_CARE_BENEFITS) final ParameterDTO parameters)
	{
		Boolean selectedValue = (Boolean) parameters.getPrimaryParameter();
		view.generateFieldsBasedOnPatientCareBenefits(selectedValue);
	}
	
	public void setUpCategoryValues(
			@Observes @CDIEvent(BILL_ENTRY_TABLE_CATEGORY_DROPDOWN_VALUE) final ParameterDTO parameters) {
		Long billClassificationKey = (Long) parameters.getPrimaryParameter();
		SelectValue claimType = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		//BeanItemContainer<SelectValue> selectValueContainer = masterService.getMasBillCategoryValues(billClassificationKey,claimType);
		Long productKey = (Long) parameters.getSecondaryParameter(2, Long.class);
		String subCoverValue = (String) parameters.getSecondaryParameter(3, String.class);
		Boolean isDomicillary = (Boolean) parameters.getSecondaryParameter(4, Boolean.class);
		
		String intimationNo = (String) parameters.getSecondaryParameter(5, String.class);
		
		Intimation intimationObj = preauthService.getIntimationByNo(intimationNo);
		
		BeanItemContainer<SelectValue> selectValueContainer = masterService
				.getMasBillCategoryValuesForZonalAndMedical(billClassificationKey,claimType,isDomicillary);
		
		
		List<SelectValue> selectValueList = selectValueContainer.getItemIds();
		
		List<SelectValue> finalCategoryList = new ArrayList<SelectValue>();
		List<SelectValue> protaCategoryList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> categoryValueContainer = null;
		if(null != selectValueList && !selectValueList.isEmpty())
		{
			categoryValueContainer = new BeanItemContainer<SelectValue>(
						SelectValue.class);
			for (SelectValue selectValue : selectValueList) {
				if(!ReferenceTable.getPrePostNatalMap().containsKey(selectValue.getId()))
				{
					if(ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(productKey)){
						if(billClassificationKey.equals(12L)){
							if(selectValue.getId().equals(ReferenceTable.COMPASSIONATE_TRAVEL) 
									|| selectValue.getId().equals(ReferenceTable.PREFERRED_NETWORK_HOSPITAL)){
								finalCategoryList.add(selectValue);
							}
						}
						else{
							finalCategoryList.add(selectValue);
						}
					}
					else if(ReferenceTable.STAR_SPECIAL_CARE_PRODUCT_KEY.equals(productKey)){
						if(billClassificationKey.equals(12L)){
							if(selectValue.getId().equals(ReferenceTable.SHARED_ACCOMODATION)){
								finalCategoryList.add(selectValue);
							}
						}else{
							finalCategoryList.add(selectValue);
						}
					}
					else if(ReferenceTable.POS_FAMILY_HEALTH_OPTIMA.equals(productKey)){
						if(billClassificationKey.equals(12L)){
							if(!selectValue.getId().equals(ReferenceTable.COMPASSIONATE_TRAVEL)){
								finalCategoryList.add(selectValue);
							}
						}else{
							finalCategoryList.add(selectValue);
						}
					}
					else if(intimationObj != null && ((((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode()) ||
							SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode()))
							|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode())
							|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode()))
								&& ("G").equalsIgnoreCase(intimationObj.getInsured().getPolicyPlan()))
								|| (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode()) ||
										SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode())))) {
							if(billClassificationKey.equals(12L)){
								if(selectValue.getId().equals(ReferenceTable.SHARED_ACCOMODATION)){
									finalCategoryList.add(selectValue);
								}
							}else{
								finalCategoryList.add(selectValue);
							}
					}// new added for GLX2020193
					else if(ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(productKey)){
						if(billClassificationKey.equals(12L)){
							if(!selectValue.getId().equals(ReferenceTable.PREFERRED_NETWORK_HOSPITAL)){
								finalCategoryList.add(selectValue);
							}
						}else{
							finalCategoryList.add(selectValue);
						}
					}
					else
					 {
						finalCategoryList.add(selectValue);
					 }
				}
				//added for GMC prorata Calculation
				if(intimationObj != null && !(ReferenceTable.STAR_GMC_PRODUCT_CODE.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode())
											|| ReferenceTable.STAR_GMC_NBFC_PRODUCT_CODE.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode()))){
					protaCategoryList.add(selectValue);
					if(billClassificationKey.equals(8L)){
						if(selectValue.getId().equals(ReferenceTable.OTHERS_WITH_PRORORTIONATE_DEDUCTION) 
								|| selectValue.getId().equals(ReferenceTable.OTHERS_WITHOUT_PRORORTIONATE_DEDUCTION)){
							finalCategoryList.remove(selectValue);

						}
						if(selectValue.getId().equals(ReferenceTable.OTHERS_WITH_PRORORTIONATE_DEDUCTION) 
								|| selectValue.getId().equals(ReferenceTable.OTHERS_WITHOUT_PRORORTIONATE_DEDUCTION)){
							protaCategoryList.remove(selectValue);

						}
					}
				}
				
				//added for GMC prorata Calculation
				if(intimationObj != null && (ReferenceTable.STAR_GMC_PRODUCT_CODE.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode())
						|| ReferenceTable.STAR_GMC_NBFC_PRODUCT_CODE.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode()))){
					protaCategoryList.add(selectValue);
					if(billClassificationKey.equals(8L)){
						if(selectValue.getValue().equalsIgnoreCase(SHAConstants.OTHERS)) {
							finalCategoryList.remove(selectValue);
						}
					}
				}
				
				// new added for GLX2020193
				if(intimationObj != null && !(ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(intimationObj.getPolicy().getProduct().getKey()))){
					if(billClassificationKey.equals(12L)){
						if(selectValue.getId().equals(ReferenceTable.VALUABLE_SERVICE_PROVIDER)){
							finalCategoryList.remove(selectValue);
							protaCategoryList.remove(selectValue);
						}
					}
				}
			}
			categoryValueContainer.addAll(finalCategoryList);
			
			if(intimationObj != null && !(ReferenceTable.STAR_GMC_PRODUCT_CODE.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode())
					|| ReferenceTable.STAR_GMC_NBFC_PRODUCT_CODE.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode()))){
				selectValueContainer.removeAllItems();
				selectValueContainer.addAll(protaCategoryList);
			}
			
		}
		/**
		 * For star wedding gift insurance , policy pre and post
		 * natal shouldn't be addded in drop down list. Hence below condition is
		 * added , based on which drop down data with or without 
		 * pre and post natal would be added. 
		 * 
		 * This change could've been done in master service. The reason
		 * for not doing there is, domicillary based check was available and
		 * that was respective to claim type. Instead of tampering that code, 
		 * the condition was handled in presenter level.
		 * 
		 * */
		if((ReferenceTable.STAR_WEDDING_GIFT_INSURANCE.equals(productKey) || (ReferenceTable.getComprehensiveProducts().containsKey(productKey))) && (!ReferenceTable.getMaternityMap().containsKey(subCoverValue)) ||
				ReferenceTable.POS_FAMILY_HEALTH_OPTIMA.equals(productKey) || (ReferenceTable.STAR_GMC_PRODUCT_CODE.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode())
						|| ReferenceTable.STAR_GMC_NBFC_PRODUCT_CODE.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode())
						|| ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(intimationObj.getPolicy().getProduct().getKey()))) 
			view.setUpCategoryValues(categoryValueContainer);
		else	
			view.setUpCategoryValues(selectValueContainer);
		}
	
	
		
	public void setUpIRDALevel2Values(
			@Observes @CDIEvent(BILLING_BILL_REVIEW_IRDA_LEVEL2_VALUES) final ParameterDTO parameters) {
		Long irdaLevel1Key = (Long) parameters.getPrimaryParameter();
		GComboBox cmb = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getIRDALevel2Values(irdaLevel1Key);
		view.setUpIrdaLevel2Values(selectValueContainer,cmb,sel);
	}
	
	public void setUpIRDALevel3Values(
			@Observes @CDIEvent(BILLING_BILL_REVIEW_IRDA_LEVEL3_VALUES) final ParameterDTO parameters) {
		Long irdaLevel2Key = (Long) parameters.getPrimaryParameter();
		GComboBox cmb = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getIRDALevel3Values(irdaLevel2Key);
		view.setUpIrdaLevel3Values(selectValueContainer,cmb,sel);
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
	
	public void saveOtherBenefitsValue(@Observes @CDIEvent(SAVE_OTHER_BENEFITS_TABLE_VALUES) final ParameterDTO parameters)
	{
		PreauthDTO preauthDTO = (PreauthDTO)parameters.getPrimaryParameter();
		DocAcknowledgement docAck = preauthDTO.getPreauthDataExtractionDetails().getDocAckknowledgement();			
		ackDocReceivedService.updateOtherBenefitsValues(preauthDTO,docAck);
		
		
	}
	 public void compareWithPreviousROD(@Observes @CDIEvent(COMPARE_WITH_PREVIOUS_ROD) final ParameterDTO parameters) {
		 try{
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
						procedureVariation += procedure.getProcedureName() + " ,";
					}
				}
				
				for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
					Boolean isExisting = false;
					Long procedureKey = 0l;
					for (Procedure procedure : findProcedureByPreauthKey) {
						if(procedureDTO.getNewProcedureFlag() != null && procedure.getNewProcedureFlag() != null 
								&& procedure.getNewProcedureFlag().equals(procedureDTO.getNewProcedureFlag()) && procedureDTO.getProcedureName() != null && procedure.getProcedureID() != null 
								&& procedureDTO.getProcedureName().getId().equals(procedure.getProcedureID())) {
							isExisting = true;
							procedureKey = procedure.getKey();
							break;
						}else if(procedure.getNewProcedureFlag() == 1){
							isExisting = true;
							break;
						} 
					}
					if(!isExisting && (procedureDTO.getRecTypeFlag() == null || (procedureDTO.getRecTypeFlag() != null && procedureDTO.getRecTypeFlag().equalsIgnoreCase("A")))) {
//						addedProc += procedureDTO.getProcedureName().getValue() + " ,";
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
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		}
	 
	 public void generateReferCoOrdinatorLayout(@Observes @CDIEvent(BILLING_REFERCOORDINATOR_EVENT) final ParameterDTO parameters)
		{
			view.generateReferCoOrdinatorLayout(masterService.getSelectValueContainer(ReferenceTable.COORDINATOR_REQUEST_TYPE));
		}
	 
	 public void generateCancelRODLayout(@Observes @CDIEvent(BILLING_CANCEL_ROD_EVENT) final ParameterDTO parameters)
		{
		 	PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		 	reimbursementService.isFvrOrInvesOrQueryInitiated(preauthDTO);
			if(null != preauthDTO.getProceedWithoutReport() && (SHAConstants.YES_FLAG.equalsIgnoreCase(preauthDTO.getProceedWithoutReport()))){
				
				view.alertMsgForCancelRod();
			}
			else
			{
				view.generateCancelRODLayout(masterService.getSelectValueContainer(ReferenceTable.CANCELLATION_REASON));
			}
		}
	 
	 public void generateReferToMedicalApproverLayout(@Observes @CDIEvent(BILLING_REFER_TO_MEDICAL_APPROVER_EVENT) final ParameterDTO parameters){
			view.generateReferToMedicalApproverLayout();
	 }

	 public void generateReferToBillEntryLayout(@Observes @CDIEvent(BILLING_REFER_TO_BILL_ENTRY) final ParameterDTO parameters){
			view.generateReferToBillEntryLayout();
	 }

	 public void setProductBasedAmt(
				@Observes @CDIEvent(BILLING_UPDATE_PRODUCT_BASED_AMT) final ParameterDTO parameters) {
		 
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
			
			
			
			Map<Integer, Object> detailsMap  = new WeakHashMap<Integer, Object>();;
			
			SelectValue sectionVal = preauthDTO.getPreauthDataExtractionDetails().getSection();
			Long section = 0l;
			String policyPlan = "0";
			if(null != sectionVal)
			{
				section = sectionVal.getId();
				policyPlan = preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan();
				
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
					
			}
			
			
			if(null != preauthDTO.getPreauthDataExtractionDetails().getSection())
			{
				if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
							preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getSectionCode());
					detailsMap = dbCalculationService
							.getHospitalizationDetailsForGMC(preauthDTO.getPolicyDto()
									.getProduct().getKey(), sumInsured,
									preauthDTO.getNewIntimationDTO().getHospitalDto()
											.getRegistedHospitals().getCityClass(),
											preauthDTO.getNewIntimationDTO()
											.getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),section,policyPlan);
				}else{
					detailsMap = dbCalculationService
							.getHospitalizationDetails(preauthDTO.getPolicyDto()
									.getProduct().getKey(), sumInsured,
									preauthDTO.getNewIntimationDTO().getHospitalDto()
											.getRegistedHospitals().getCityClass(),
											preauthDTO.getNewIntimationDTO()
											.getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),section,policyPlan);
				}
				
			
			}
			else
			{
				Long sectionCategory = 0l;
				if(null != policyDTO
					&& policyDTO.getProduct().getKey().equals(ReferenceTable.STAR_CARDIAC_CARE_POLICY) || policyDTO.getProduct().getKey().equals(ReferenceTable.DIABETES_INDIVIDUAL_POLICY) || policyDTO.getProduct().getKey().equals(ReferenceTable.DIABETES_FLOATER_POLICY))
				{
					sectionCategory = 1l;
				}
				
				if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
							preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getSectionCode());
					detailsMap = dbCalculationService
							.getHospitalizationDetailsForGMC(preauthDTO.getPolicyDto()
									.getProduct().getKey(), sumInsured,
									preauthDTO.getNewIntimationDTO().getHospitalDto()
											.getRegistedHospitals().getCityClass(),
											preauthDTO.getNewIntimationDTO()
											.getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),sectionCategory,policyPlan);
				}else{
					detailsMap = dbCalculationService
							.getHospitalizationDetails(preauthDTO.getPolicyDto()
									.getProduct().getKey(), sumInsured,
									preauthDTO.getNewIntimationDTO().getHospitalDto()
											.getRegistedHospitals().getCityClass(),
											preauthDTO.getNewIntimationDTO()
											.getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),sectionCategory,policyPlan);
				}
				
			}
			view.updateProductBasedAmtDetails(detailsMap);
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
		
		public void checkCriticalIllness(
				@Observes @CDIEvent(CHECK_CRITICAL_ILLNESS) final ParameterDTO parameters) {
			Boolean checkValue = (Boolean) parameters.getPrimaryParameter();
			view.editSpecifyVisibility(checkValue);
		}
		
		public void clearReferenceData(
				@Observes @CDIEvent(REFERENCE_DATA_CLEAR) final ParameterDTO parameters) {
			
		}
		
		public void getRTABalanceSI(@Observes @CDIEvent(GET_RTA_BALANCE_SI) final ParameterDTO parameters)
		{
			
			PreauthDTO bean = (PreauthDTO) parameters.getPrimaryParameter();
			String isRTAAvailable = dbCalculationService.getRTARechargeAvailable(bean.getClaimKey());
			
			if(!(SHAConstants.YES_FLAG.equalsIgnoreCase(isRTAAvailable)))
			{			
			
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
		}
		
		public void getBeneiftDetails(@Observes @CDIEvent(GET_BENEFIT_DETAILS) final ParameterDTO parameters)
		{
			PreauthDTO bean = (PreauthDTO) parameters.getPrimaryParameter();
			
			if((ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
					|| ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
					|| /*ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())*/
						(bean.getNewIntimationDTO().getPolicy().getProduct() != null 
						&& (((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
								SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
								|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())
								|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
						&& ("G").equalsIgnoreCase(bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan())))
					|| (bean.getNewIntimationDTO().getPolicy().getProduct() != null 
					&& (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
							SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())))){
				
				Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(
						bean.getNewIntimationDTO().getInsuredPatient()
								.getInsuredId().toString(), bean
								.getPolicyDto().getKey(),bean.getNewIntimationDTO().getInsuredPatient().getLopFlag());
				Reimbursement reimbursementByKey = reimbursementService.getReimbursementByKey(bean.getKey());
				
				 List<OtherBenefitsTableDto> benefitTableValues = reimbursementService.getBenefitTableValues(bean.getNewIntimationDTO(), insuredSumInsured, reimbursementByKey);
				 bean.getPreauthMedicalDecisionDetails().setOtherBenefitTableDtoList(benefitTableValues);
			}
			
		}
		
		protected Double getPreHospitalizationRODValues(Long claimKey, Long rodKey) {
			Reimbursement previousROD = null;
			Integer previousNumber = 1;
			Double postHospAmt = 0d;
			
			List<Reimbursement> previousRODByClaimKey = reimbursementService
					.getPreviousRODByClaimKey(claimKey);
			if (previousRODByClaimKey != null && !previousRODByClaimKey.isEmpty()) {
				for (Reimbursement reimbursement : previousRODByClaimKey) {
					
					if(reimbursement.getDocAcknowLedgement().getPreHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getPreHospitalisationFlag().toLowerCase().equalsIgnoreCase("y") && !reimbursement.getKey().equals(rodKey) && !SHAUtils.getRejectStatusMap().containsKey(reimbursement.getStatus().getKey()) && !ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey())  && !ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey())) {
						ReimbursementCalCulationDetails reimbursementCalcByRodAndClassificationKey = reimbursementService.getReimbursementCalcObjByRodAndClassificationKey(reimbursement.getKey(), ReferenceTable.PRE_HOSPITALIZATION);
						if(reimbursementCalcByRodAndClassificationKey != null) {
							if(reimbursement.getOtherInsurerApplicableFlag() != null && reimbursement.getOtherInsurerApplicableFlag().equalsIgnoreCase("Y")) {
								postHospAmt += reimbursementCalcByRodAndClassificationKey.getTpaPayableAmt();
							} else {
								postHospAmt += reimbursementCalcByRodAndClassificationKey.getPayableToInsured();
							}
							
						}
					}
				}
			}
			return postHospAmt;
		}
		
		protected Double getPostHospitalizationRODValues(Long claimKey, Long rodKey) {
			Reimbursement previousROD = null;
			Integer previousNumber = 1;
			Double postHospAmt = 0d;
			
			List<Reimbursement> previousRODByClaimKey = reimbursementService
					.getPreviousRODByClaimKey(claimKey);
			if (previousRODByClaimKey != null && !previousRODByClaimKey.isEmpty()) {
				for (Reimbursement reimbursement : previousRODByClaimKey) {
					
					if(reimbursement.getDocAcknowLedgement().getPostHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getPostHospitalisationFlag().toLowerCase().equalsIgnoreCase("y") && !reimbursement.getKey().equals(rodKey) && !SHAUtils.getRejectStatusMap().containsKey(reimbursement.getStatus().getKey()) && !ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey())  && !ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey())) {
						ReimbursementCalCulationDetails reimbursementCalcByRodAndClassificationKey = reimbursementService.getReimbursementCalcObjByRodAndClassificationKey(reimbursement.getKey(), ReferenceTable.POST_HOSPITALIZATION);
						if(reimbursementCalcByRodAndClassificationKey != null && ReferenceTable.getFinancialApprovalStatus().containsKey(reimbursement.getStatus().getKey())) {
							if(reimbursement.getOtherInsurerApplicableFlag() != null && reimbursement.getOtherInsurerApplicableFlag().equalsIgnoreCase("Y")) {
								postHospAmt += reimbursementCalcByRodAndClassificationKey.getTpaPayableAmt() != null ? reimbursementCalcByRodAndClassificationKey.getTpaPayableAmt() : 0d;
							} else {
								postHospAmt += reimbursementCalcByRodAndClassificationKey.getPayableInsuredAfterPremium();
							}
							
						}
					}
				}
			}
			return postHospAmt;
		}

		public void getNomineeIFSCDetails(@Observes @CDIEvent(SETUP_NOMINEE_IFSC_DETAILS_BILLING) final ParameterDTO parameters)
		{
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
}
