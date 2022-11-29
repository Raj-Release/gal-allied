package com.shaic.paclaim.billing.processclaimbilling.page.billreview;

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
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
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
import com.shaic.paclaim.billing.processclaimbilling.search.PASearchProcessClaimBillingService;
import com.shaic.paclaim.reimbursement.service.PAReimbursementService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;

@ViewInterface(PABillingReviewPageWizard.class)
public class PABillingReviewPagePresenter extends AbstractMVPPresenter<PABillingReviewPageWizard>{
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
	
	
	public static final String BILLING_HOSPITAL_BENEFITS = "pa_billing_hospital_cash_benefits";
	public static final String BILLING_PATIENT_CARE_BENEFITS = "pa_billing_patiet_care_benefits";
	public static final String BILLING_REVIEW_SET_UP_REFERENCE = "pa_billing_review_set_up_reference";


	public static final String BILLING_TREATMENT_TYPE_CHANGED = "pa_billing_treatment_type_changed";


	public static final String BILLING_COORDINATOR = "pa_billing_coordinator";
	public static final String CLAIM_HOSPITAL_BENEFITS = "pa_claim_billig_hospital_benefits";
	public static final String CLAIM_PATIENT_CARE_BENEFITS = "pa_claim_billig_patient_care_benefits";


	public static final String BILLING_PATIENT_STATUS_CHANGED = "pa_billing_patient_status_changed";


	public static final String BILL_ENTRY_TABLE_CATEGORY_DROPDOWN_VALUE = "pa_billing_bill_entry_category_values";
	
	public static final String SAVE_PATIENT_CARE_TABLE_VALUES = "pa_Save Patient Care Table Values for Claims Billing";
	
	public static final String SAVE_HOSPITAL_CASH_TABLE_VALUES = "pa_Save Hospital Cash Table Values for Claims Billing";


	public static final String BILL_ENTRY_COMPLETION_STATUS = "pa_billing_bill_entry_completion_status";
	
	public static final String BILLING_DETAILS_UPDATE = "pa_billing_update_details";
	
	public static final String BILL_REVIEW_IRDA_LEVEL2_VALUES = "pa_bill_review_irda_level2_values";
	
	public static final String BILL_REVIEW_IRDA_LEVEL3_VALUES = "pa_bill_review_irda_level3_values";
	
	public static final String BILLING_BILL_REVIEW_IRDA_LEVEL2_VALUES = "pa_billing_bill_review_irda_level2_values";
	
	public static final String BILLING_BILL_REVIEW_IRDA_LEVEL3_VALUES = "pa_billing_bill_review_irda_level3_values";


	public static final String GET_MAPPING_DATA = "pa_billing_get_mapping_data";
	
	public static final String COMPARE_WITH_PREVIOUS_ROD = "pa_process_Claim_billing_compare_with_previous_rod";
	
	public static final String BILLING_REFERCOORDINATOR_EVENT = "pa_billing_refer_to_coordinator_event_for_first_page";
	
	public static final String BILLING_CANCEL_ROD_EVENT="pa_billing_cancel_rod_event_for_first_page";
	
	public static final String BILLING_REFER_TO_MEDICAL_APPROVER_EVENT = "pa_billing_refer_to_medical_approver_event_for_first_page";
	
	public static final String BILLING_SAVE_BILL_ENTRY_VALUES = "pa_billing_save_bill_entry_values";
	
	public static final String BILLING_LOAD_BILL_DETAILS_VALUES = "pa_billing_load_bill_details_values";

	public static final String BILLING_REFER_TO_BILL_ENTRY = "pa_billing_refer_to_bill_entry";

	public static final String BILLING_UPDATE_PRODUCT_BASED_AMT = "pa_billing_update_product_based_amt";
	
	public static final String BILLING_SEND_TO_CLAIM_APPROVAL_EVENT = "pa_billing_send_to_claim_approval_event";
	
	public static final String BILLING_SEND_TO_FA_EVENT = "pa_billing_send_to_FA_event";


	public static final String PA_POPULATE_ADD_ON_COVERS = "pa_billing_populate_add_on_cover";

	public static final String PA_PRESENT_POPULATE_ADD_ON_COVERS = "pa_billing_present_populate_add_on_cover";
	
//	public static final String SETUP_NOMINEE_IFSC_DETAILS_BILLING = "setup Nominee IFSC For PA Billing Screen"; 
	
	public static final String PA_POPULATE_OPTIONAL_COVERS = "pa_populate_optional_covers";
	
	@EJB
	private CreateRODService rodService;

	@EJB
	private PAReimbursementService paReimbursementService;
	
	@EJB
	private PASearchProcessClaimBillingService paClaimBillingService;
	
	private Long primaryCoverID = null;

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
//		referenceData.put("fileType", masterService
//				.getSelectValueContainer(ReferenceTable.PA_ROD_UPLOAD_DOC_TABLE_FILE_TYPE));
		referenceData.put("fileType", masterService
				.getSelectValueContainer(ReferenceTable.PA_ROD_UPLOAD_DOC_TABLE_FILE_TYPE));
		
		
		
		referenceData.put("billClassification", setClassificationValues(dto));
		referenceData.put(SHAConstants.ALL_BILL_CLASSIFICATIONS, masterService.getMasBillClassificationValues());
		
		//referenceData.put("billClassification", masterService.getMasBillClassificationValues());
		
		Double insuredAge = dto.getNewIntimationDTO().getInsuredPatient().getInsuredAge();
		Double sumInsured = 0d;
		
		if(null != dto.getNewIntimationDTO() && null != dto.getNewIntimationDTO().getPolicy() && null != dto.getNewIntimationDTO().getPolicy().getProduct() &&
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
			if(null != productCode && ReferenceTable.getGPAProducts().containsKey(productCode)){
				referenceData.put("optionalCoverProc",dbCalculationService.getClaimCoverValues(SHAConstants.GPA_OPTIONAL_COVER, productCode , insuredKey));
			} else {
				referenceData.put("optionalCoverProc",dbCalculationService.getClaimCoverValues(SHAConstants.OPTIONAL_COVER, productCode , insuredKey));
			}
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
	 
	 public void populateAddOnCoverValue(@Observes @CDIEvent(PA_POPULATE_ADD_ON_COVERS) final ParameterDTO parameters){
		  
		 Long coverID = (Long) parameters.getPrimaryParameter();
		 
		 AddOnCoversTableDTO addOnCoversTableDTO = (AddOnCoversTableDTO) parameters.getSecondaryParameter(0, AddOnCoversTableDTO.class);
		 ComboBox cmbBox = (ComboBox) parameters.getSecondaryParameter(1, ComboBox.class);
		 if(addOnCoversTableDTO.getProductCode()!=null){
			 	AddOnCoversTableDTO addOnCoversTableDTO2 = new AddOnCoversTableDTO();	
			  long productKey =addOnCoversTableDTO.getProductCode();
			  Boolean onLoad = Boolean.FALSE;
			  AddOnCoversTableDTO setAddonDtoforCoverId = paClaimBillingService.setAddonDtoforCoverIdOnchange(addOnCoversTableDTO.getRodId(), productKey, 0d, addOnCoversTableDTO, coverID, dbCalculationService,onLoad);
			  
			  view.generateAddOnCoverValue(setAddonDtoforCoverId,cmbBox);
		  }
	 }
	 
	 
	 public void populatePresentAddOnCoverValue(@Observes @CDIEvent(PA_PRESENT_POPULATE_ADD_ON_COVERS) final ParameterDTO parameters){
		  
		 Long coverID = (Long) parameters.getPrimaryParameter();
		 
		 AddOnCoversTableDTO addOnCoversTableDTO = (AddOnCoversTableDTO) parameters.getSecondaryParameter(0, AddOnCoversTableDTO.class);
		 ComboBox cmbBox = (ComboBox) parameters.getSecondaryParameter(1, ComboBox.class);
		 if(addOnCoversTableDTO.getProductCode()!=null){
			 	AddOnCoversTableDTO addOnCoversTableDTO2 = new AddOnCoversTableDTO();	
			  long productKey =addOnCoversTableDTO.getProductCode();
			  Boolean onLoad = Boolean.FALSE;
			  List<AddOnCoversTableDTO> setAddonDtoforCoverIdList = paClaimBillingService.getAddOnCoverListByRodKey(addOnCoversTableDTO.getRodId(), productKey, null);
			  if(setAddonDtoforCoverIdList!=null && setAddonDtoforCoverIdList.size()>0){
				  AddOnCoversTableDTO setAddonDtoforCoverId = setAddonDtoforCoverIdList.get(0);
				  view.generateAddOnCoverValue(setAddonDtoforCoverId,cmbBox);
			  }
		  }
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
	 
	 
	 public void generateSendToClaimApprovalLayout(@Observes @CDIEvent(BILLING_SEND_TO_CLAIM_APPROVAL_EVENT) final ParameterDTO parameters)
	{
		view.generateSendToClaimApprovalLayout();
	}
	 
	public void generateSendToFALayout(@Observes @CDIEvent(BILLING_SEND_TO_FA_EVENT) final ParameterDTO parameters)
	{
		view.generateSendToFALayout();
	}

	/*public void getNomineeIFSCDetails(@Observes @CDIEvent(SETUP_NOMINEE_IFSC_DETAILS_BILLING) final ParameterDTO parameters)
	{
		ViewSearchCriteriaTableDTO viewSearchCriteriaDTO = (ViewSearchCriteriaTableDTO) parameters.getPrimaryParameter();
		view.setUpNomineeIFSCDetails(viewSearchCriteriaDTO);			
	}*/

	public void populateApproveAmntOPT(@Observes @CDIEvent(PA_POPULATE_OPTIONAL_COVERS) final ParameterDTO parameters)
	{
		List<OptionalCoversDTO> optListBilling = (List<OptionalCoversDTO>) parameters.getPrimaryParameter();
		view.populateApproveAmntOPT(optListBilling);
	}
}
