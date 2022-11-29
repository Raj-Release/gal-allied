package com.shaic.paclaim.financial.claimapproval.hosiptalpagereview;

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
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.reimbursement.billing.benefits.wizard.service.ProcessClaimRequestBenefitsService;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
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
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(PAClaimAprHosReviewPageWizard.class)
public class PAClaimAprHosReviewPagePresenter extends AbstractMVPPresenter<PAClaimAprHosReviewPageWizard>{
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
	
	
	public static final String BILLING_HOSPITAL_BENEFITS = "pa_claimApr_hospital_cash_benefits";
	public static final String BILLING_PATIENT_CARE_BENEFITS = "pa_claimApr_patiet_care_benefits";
	public static final String BILLING_REVIEW_SET_UP_REFERENCE = "pa_claimApr_review_set_up_reference";


	public static final String BILLING_TREATMENT_TYPE_CHANGED = "pa_claimApr_treatment_type_changed";


	public static final String BILLING_COORDINATOR = "pa_claimApr_coordinator";
	public static final String CLAIM_HOSPITAL_BENEFITS = "pa_claim_billig_hospital_benefits";
	public static final String CLAIM_PATIENT_CARE_BENEFITS = "pa_claim_billig_patient_care_benefits";


	public static final String BILLING_PATIENT_STATUS_CHANGED = "pa_claimApr_patient_status_changed";


	public static final String BILL_ENTRY_TABLE_CATEGORY_DROPDOWN_VALUE = "pa_claimApr_bill_entry_category_values";
	
	public static final String SAVE_PATIENT_CARE_TABLE_VALUES = "pa_Save Patient Care Table Values for Claims Billing";
	
	public static final String SAVE_HOSPITAL_CASH_TABLE_VALUES = "pa_Save Hospital Cash Table Values for Claims Billing";


	public static final String BILL_ENTRY_COMPLETION_STATUS = "pa_claimApr_bill_entry_completion_status";
	
	public static final String BILLING_DETAILS_UPDATE = "pa_claimApr_update_details";
	
	public static final String BILL_REVIEW_IRDA_LEVEL2_VALUES = "pa_claimApr_review_irda_level2_values";
	
	public static final String BILL_REVIEW_IRDA_LEVEL3_VALUES = "pa_claimApr_review_irda_level3_values";
	
	public static final String BILLING_BILL_REVIEW_IRDA_LEVEL2_VALUES = "pa_claimApr_bill_review_irda_level2_values";
	
	public static final String BILLING_BILL_REVIEW_IRDA_LEVEL3_VALUES = "pa_claimApr_bill_review_irda_level3_values";


	public static final String GET_MAPPING_DATA = "pa_claimApr_get_mapping_data";
	
	public static final String COMPARE_WITH_PREVIOUS_ROD = "pa_process_Claim_billing_compare_with_previous_rod";
	
	public static final String BILLING_REFERCOORDINATOR_EVENT = "pa_claimApr_refer_to_coordinator_event_for_first_page";
	
	public static final String BILLING_CANCEL_ROD_EVENT="pa_claimApr_cancel_rod_event_for_first_page";
	
	public static final String BILLING_REFER_TO_MEDICAL_APPROVER_EVENT = "pa_claimApr_refer_to_medical_approver_event_for_first_page";
	
	public static final String BILLING_SAVE_BILL_ENTRY_VALUES = "pa_claimApr_save_bill_entry_values";
	
	public static final String BILLING_LOAD_BILL_DETAILS_VALUES = "pa_claimApr_load_bill_details_values";

	public static final String BILLING_REFER_TO_BILL_ENTRY = "pa_claimApr_refer_to_bill_entry";

	public static final String BILLING_UPDATE_PRODUCT_BASED_AMT = "pa_claimApr_update_product_based_amt";

	
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
				.getSelectValueContainer(ReferenceTable.ROD_UPLOAD_DOC_TABLE_FILE_TYPE));
		
		referenceData.put("billClassification", setClassificationValues(dto));
		referenceData.put(SHAConstants.ALL_BILL_CLASSIFICATIONS, masterService.getMasBillClassificationValues());
		
		//referenceData.put("billClassification", masterService.getMasBillClassificationValues());
		
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
		view.setupReferences(referenceData);
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
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getMasBillCategoryValues(billClassificationKey,claimType);
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
			
			
			
			Map<Integer, Object> detailsMap  = new HashMap<Integer, Object>();;
			
			SelectValue sectionVal = preauthDTO.getPreauthDataExtractionDetails().getSection();
			Long section = 0l;
			String policyPlan = "0";
			if(null != sectionVal)
			{
				section = sectionVal.getId();
				policyPlan = preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan();
			}
			
			
			if(null != preauthDTO.getPreauthDataExtractionDetails().getSection())
			{
				
				detailsMap = dbCalculationService
						.getHospitalizationDetails(preauthDTO.getPolicyDto()
								.getProduct().getKey(), sumInsured,
								preauthDTO.getNewIntimationDTO().getHospitalDto()
										.getRegistedHospitals().getCityClass(),
										preauthDTO.getNewIntimationDTO()
										.getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),section,policyPlan);
			
			}
			else
			{
				Long sectionCategory = 0l;
				if(null != policyDTO
					&& policyDTO.getProduct().getKey().equals(ReferenceTable.STAR_CARDIAC_CARE_POLICY) || policyDTO.getProduct().getKey().equals(ReferenceTable.DIABETES_INDIVIDUAL_POLICY) || policyDTO.getProduct().getKey().equals(ReferenceTable.DIABETES_FLOATER_POLICY))
				{
					sectionCategory = 1l;
				}
				
				detailsMap = dbCalculationService
						.getHospitalizationDetails(preauthDTO.getPolicyDto()
								.getProduct().getKey(), sumInsured,
								preauthDTO.getNewIntimationDTO().getHospitalDto()
										.getRegistedHospitals().getCityClass(),
										preauthDTO.getNewIntimationDTO()
										.getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),sectionCategory,policyPlan);
			}
			view.updateProductBasedAmtDetails(detailsMap);
	 }


}
