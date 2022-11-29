package com.shaic.paclaim.health.reimbursement.medicalapproval.pages.medicaldecision;

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
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewBillSummaryPage;
import com.shaic.domain.ClaimService;
import com.shaic.domain.FieldVisitRequestService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Investigation;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PEDValidationService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementQueryService;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.domain.service.PreMedicalService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(PAHealthClaimRequestMedicalDecisionPageInterface.class)
public class PAHealthClaimRequestMedicalDecisionPagePresenter extends
		AbstractMVPPresenter<PAHealthClaimRequestMedicalDecisionPageInterface> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9106963317300947015L;

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
	private PreviousPreAuthService previousPreauthService;

	@EJB
	private InvestigationService investigationService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private ReimbursementQueryService reimbursementQueryService;
	
	@EJB
	private CreateRODService createRodService;

	@EJB
	private PEDValidationService pedValidationService;
	
	@javax.inject.Inject
	private ViewBillSummaryPage viewBillSummaryPage;
	
	@EJB
	private CreateRODService rodService;

	@EJB
	private FieldVisitRequestService fvrService;

	public static final String MEDICAL_APPROVAL_SETUP_REFERNCE = "pa_health_medical_approval_claim_request_medical_decision_setup_reference";

	public static final String VIEW_BALANCE_SUM_INSURED_DETAILS = "pa_health_medical_approval_claim_request_medical_decision_balance_sum_insured_details";

	public static final String VIEW_CLAIMED_AMOUNT_DETAILS = "pa_health_medical_approval_claim_request_medical_decision_claimed_amount_details";

	public static final String GET_PREAUTH_REQUESTED_AMOUT = "pa_health_medical_approval_claim_request_medical_decision_preauth_requeseted_amount";

	public static final String CLAIM_REQUEST_APPROVE_EVENT = "pa_health_claim_request_apporve_event";
	
	public static final String CLAIM_CANCEL_ROD_EVENT      =  "pa_health_claim_request_cancel_rod";            

	public static final String CLAIM_REQUEST_QUERY_BUTTON_EVENT = "pa_health_claim_request_query_button_event";

	public static final String CLAIM_REQUEST_REJECTION_EVENT = "pa_health_claim_request_rejection_button_event";

	public static final String CLAIM_REQUEST_ESCALATE_EVENT = "pa_health_claim_request_escalte_button_event";

	public static final String CLAIM_REQUEST_ESCALATE_REPLY_EVENT = "pa_health_claim_request_escalte_reply_event";

	public static final String CLAIM_REQUEST_SENT_TO_CPU_SELECTED = "pa_health_claim_request_sent_to_cpu_selected";

	public static final String CLAIM_REQUEST_REFERCOORDINATOR_EVENT = "pa_health_claim_request_refer_to_coordinator_event";

	public static final String CLAIM_REQUEST_FIELD_VISIT_EVENT = "pa_health_claim_request_field_visit_event";

	public static final String CLAIM_REQUEST_INVESTIGATION_EVENT = "pa_health_claim_request_investigation_event";

	public static final String CLAIM_REQUEST_SPECIALIST_EVENT = "pa_health_claim_request_specialist_event";

	public static final String CLAIM_REQUEST_SENT_TO_REPLY_EVENT = "pa_health_claim_request_sent_to_reply_event";

	public static final String CLAIM_REQUSET_SUM_INSURED_CALCULATION = "pa_health_claim_request_sum_insured_calculation";

	public static final String SUM_INSURED_CALCULATION = "pa_health_claim_request_for_getting_sum_insured_calculation";

	public static final String BILL_ENTRY_TABLE_CATEGORY_DROPDOWN_VALUE = "pa_health_claim_request_bill_entry_category_dropdown_values";

	public static final String BILL_ENTRY_COMPLETION_STATUS = "pa_health_claim_request_bill_entry_completion_status";

	public static final String CLAIM_REQUEST_SAVE_BILL_ENTRY = "pa_health_claim_request_save_bill_entry_for_amount_considered";

	public static final String CHECK_INVESTIGATION_INITIATED = "pa_health_claim_request_check_investigation_initiated";
	
	public static final String MA_SAVE_BILL_ENTRY_VALUES = "pa_health_ma_save_bill_entry_values";
	
	public static final String MA_LOAD_BILL_DETAILS_VALUES = "pa_health_ma_load_bill_details_values";
	

	public static final String PA_HEALTH_CLAIM_REQUEST_AUTO_SKIP_FVR = "pa_health_claim_request_auto_skip_fvr";

	Map<String, Object> referenceData = new HashMap<String, Object>();

	public void setUpReference(
			@Observes @CDIEvent(MEDICAL_APPROVAL_SETUP_REFERNCE) final ParameterDTO parameters) {

		PreauthDTO dto = (PreauthDTO) parameters.getPrimaryParameter();

		if (dto.getClaimKey() != null) {
			List<Investigation> investigationList = investigationService
					.getByInvestigationByClaimKey(dto.getClaimKey());
			if (investigationList != null && investigationList.size() >= 0) {
				dto.setInvestigationSize(investigationList.size());
			} else {
				dto.setInvestigationSize(0);
			}
		}
		referenceData.put("investigatorName", masterService.getInvestigation());
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
				previousPreauthService.search(dto.getClaimKey(), false));
		referenceData.put("status", masterService
				.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData
				.put("fileType",
						masterService
								.getSelectValueContainer(ReferenceTable.PA_ROD_UPLOAD_DOC_TABLE_FILE_TYPE));
		referenceData.put("cancellationReason", masterService.getSelectValueContainer(ReferenceTable.CANCELLATION_REASON));
		
		/*referenceData.put("billClassification",
				masterService.getMasBillClassificationValues());*/
		referenceData.put("billClassification", setClassificationValues(dto));

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
				
			}
			classificationValueContainer.addAll(finalClassificationList);
		}
		return classificationValueContainer;
	}
	
	public void loadBillDetailsValues(
			@Observes @CDIEvent(MA_LOAD_BILL_DETAILS_VALUES) final ParameterDTO parameters) {
		UploadDocumentDTO uploadDTO = (UploadDocumentDTO) parameters.getPrimaryParameter();
		List<BillEntryDetailsDTO> dtoList = rodService.getBillEntryDetailsList(uploadDTO);
		uploadDTO.setBillEntryDetailList(dtoList);
		view.setUploadDTOForBillEntry(uploadDTO);
	/*	Long billClassificationKey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getMasBillCategoryValues(billClassificationKey);*/
		//view.setUpCategoryValues(selectValueContainer);
		//view.enableOrDisableBtn(uploadDTO);
		}
	

	public void viewBalanceSumInsured(
			@Observes @CDIEvent(VIEW_BALANCE_SUM_INSURED_DETAILS) final ParameterDTO parameters) {
		String intimationId = (String) parameters.getPrimaryParameter();
		if (intimationId != null && !intimationId.equals("")) {
			view.viewBalanceSumInsured(intimationId);
		}

	}

	public void viewClaimAmountDetails(
			@Observes @CDIEvent(VIEW_CLAIMED_AMOUNT_DETAILS) final ParameterDTO parameters) {
		view.viewClaimAmountDetails();
	}

	public void setBillEntryStatus(
			@Observes @CDIEvent(BILL_ENTRY_COMPLETION_STATUS) final ParameterDTO parameters) {
		// Boolean status = (Boolean) parameters.getPrimaryParameter();
		UploadDocumentDTO uploadDTO = (UploadDocumentDTO) parameters
				.getPrimaryParameter();
		rodService.saveBillEntryValues(uploadDTO);
		view.setBillEntryFinalStatus(uploadDTO);

	}

	public void setPreAuthRequestedAmount(
			@Observes @CDIEvent(GET_PREAUTH_REQUESTED_AMOUT) final ParameterDTO parameters) {
		PreviousPreAuthTableDTO dto = (PreviousPreAuthTableDTO)parameters.getPrimaryParameter();
		view.setPreAuthRequestedAmt(preauthService.getPreauthReqAmt(dto.getKey(), dto.getClaimKey()));
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

	public void sentTOCPUChecked(
			@Observes @CDIEvent(CLAIM_REQUEST_SENT_TO_CPU_SELECTED) final ParameterDTO parameters) {
		Boolean isChecked = (Boolean) parameters.getPrimaryParameter();
		view.generateFieldsBasedOnSentTOCPU(isChecked);
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
	
	public void saveBillValues(
			@Observes @CDIEvent(MA_SAVE_BILL_ENTRY_VALUES) final ParameterDTO parameters) {
			//Boolean status = (Boolean) parameters.getPrimaryParameter();
			UploadDocumentDTO	uploadDTO = (UploadDocumentDTO) parameters.getPrimaryParameter();
			rodService.saveBillEntryValues(uploadDTO);
			//view.setBillEntryFinalStatus(uploadDTO);
		
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

	public void getSumInsuredInfoFromDB(
			@Observes @CDIEvent(SUM_INSURED_CALCULATION) final ParameterDTO parameters) {
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
		
		if(! preauthDto.getIsQueryReceived()){
		
			if(preauthDto.getClaimDTO().getClaimType() != null && preauthDto.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
					
					Reimbursement hospitalizationRod = reimbursementService.getHospitalizationRod(preauthDto.getClaimKey(), preauthDto.getKey());
					
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
					
					Reimbursement hospitalizationRod = reimbursementService.getHospitalizationRod(preauthDto.getClaimKey(), preauthDto.getKey());
					if(hospitalizationRod != null){
						values.put("preauthKey", hospitalizationRod.getKey());
					}else{
						values.put("preauthKey",0l);
					}
				}
		}else{
			values.put("preauthKey", preauthDto.getKey());
		}
	
	
	
	     if(preauthDto.getIsReferToMedicalApprover() || preauthDto.getIsEscalateReplyEnabled()){
	    	 values.put("preauthKey", preauthDto.getKey());
	     }
	     
	     if((preauthDto.getClaimDTO().getClaimType() != null && preauthDto.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)) || (preauthDto.getIsHospitalizationRepeat() != null && preauthDto.getIsHospitalizationRepeat())) {
				values.put("preauthKey", 0l);
			}
	
		Map<String, Object> medicalDecisionTableValues = dbCalculationService
				.getMedicalDecisionTableValue(values,preauthDto.getNewIntimationDTO());

		view.setDiagnosisSumInsuredValuesFromDB(dto, medicalDecisionTableValues);
	}

	public void setUpCategoryValues(
			@Observes @CDIEvent(BILL_ENTRY_TABLE_CATEGORY_DROPDOWN_VALUE) final ParameterDTO parameters) {
		Long billClassificationKey = (Long) parameters.getPrimaryParameter();
		SelectValue claimType = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		Long productKey = (Long) parameters.getSecondaryParameter(2, Long.class);
		String subCoverValue = (String) parameters.getSecondaryParameter(3, String.class);
		Boolean isDomicillary = (Boolean) parameters.getSecondaryParameter(4, Boolean.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService
				.getMasBillCategoryValuesForZonalAndMedical(billClassificationKey,claimType,isDomicillary);
		
		List<SelectValue> selectValueList = selectValueContainer.getItemIds();
		
		
		List<SelectValue> protaCategoryList = new ArrayList<SelectValue>();
		List<SelectValue> finalCategoryList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> categoryValueContainer = null;
		if(null != selectValueList && !selectValueList.isEmpty())
		{
			categoryValueContainer = new BeanItemContainer<SelectValue>(
						SelectValue.class);
			for (SelectValue selectValue : selectValueList) {	
				if(!ReferenceTable.getPrePostNatalMap().containsKey(selectValue.getId()))
				{
					finalCategoryList.add(selectValue);
				}
				//added for GMC prorata Calculation
				if(productKey != null && !(ReferenceTable.STAR_GMC_PRODUCT_KEY.equals(productKey)|| ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY.equals(productKey))){
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
				if(productKey != null && (ReferenceTable.STAR_GMC_PRODUCT_KEY.equals(productKey)|| ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY.equals(productKey))){
					protaCategoryList.add(selectValue);
					if(billClassificationKey.equals(8L)){
						if(selectValue.getValue().equalsIgnoreCase(SHAConstants.OTHERS)) {
							finalCategoryList.remove(selectValue);
						}
					}
				}
				
				// new added for GLX2020193
				if(productKey != null && !(ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(productKey))){
					if(billClassificationKey.equals(12L)){
						if(selectValue.getId().equals(ReferenceTable.VALUABLE_SERVICE_PROVIDER)){
							finalCategoryList.remove(selectValue);
							protaCategoryList.remove(selectValue);
						}
					}
				}
				if(productKey != null && (ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(productKey))){
					if(billClassificationKey.equals(12L)){
						if(selectValue.getId().equals(ReferenceTable.PREFERRED_NETWORK_HOSPITAL)) {
							finalCategoryList.remove(selectValue);
						}
					}
				}
			}
			categoryValueContainer.addAll(finalCategoryList);
			
			if(productKey != null && !(ReferenceTable.STAR_GMC_PRODUCT_KEY.equals(productKey)|| ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY.equals(productKey))){
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
		if((ReferenceTable.STAR_WEDDING_GIFT_INSURANCE.equals(productKey) || (ReferenceTable.STAR_GMC_PRODUCT_KEY.equals(productKey)|| ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY.equals(productKey)) || 
				(ReferenceTable.getComprehensiveProducts().containsKey(productKey)) || (ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(productKey))) && (!ReferenceTable.getMaternityMap().containsKey(subCoverValue))) 
			view.setUpCategoryValues(categoryValueContainer);
		else	
			view.setUpCategoryValues(selectValueContainer);
		}
	
	public void saveBillEntryValues(
			@Observes @CDIEvent(CLAIM_REQUEST_SAVE_BILL_ENTRY) final ParameterDTO parameters) {
		PreauthDTO bean = (PreauthDTO) parameters.getPrimaryParameter();
		reimbursementService.billEntrySave(bean);
		viewBillSummaryPage.init(bean,bean.getKey(), true);
		Map<String, String> payableAmount = viewBillSummaryPage.getPayableAmount();
		Double sumValue = 0d;
		String string = payableAmount.get(SHAConstants.HOSPITALIZATION);
		sumValue = SHAUtils.getDoubleValueFromString(string);
		
		bean =  SHAUtils.roomRentNursingMapping(bean, 8l, 9l, false,false);
		bean = SHAUtils.roomRentNursingMapping(bean, 10l, 11l, true,false);
		bean = SHAUtils.roomRentNursingMapping(bean, 85l, 84l, false,true);
		reimbursementService.saveRoomRentNursingMapping(bean, bean.getRoomRentMappingDTOList());
		dbCalculationService.getBillDetailsSummary(bean.getKey());
		
		view.setBillEntryAmountConsiderValue(sumValue);
    }
	
	public void ruleForCheckInvestigation(@Observes @CDIEvent(CHECK_INVESTIGATION_INITIATED) final ParameterDTO parameters) {
		Long claimKey = (Long) parameters.getPrimaryParameter();
		if(claimKey != null) {
			Investigation checkInitiateInvestigation = preauthService.checkInitiateInvestigation(claimKey);
//			view.setInvestigationRule(checkInitiateInvestigation);
			
			Boolean isInvPending = preauthService.checkInvestigationPending(claimKey);
//			view.setInvestigationRule(isInvPending, checkInitiateInvestigation);
		}
		
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}

	public void submitAutoSkipFVR(
			@Observes @CDIEvent(PA_HEALTH_CLAIM_REQUEST_AUTO_SKIP_FVR) final ParameterDTO parameters) {
		// Boolean status = (Boolean) parameters.getPrimaryParameter();
		PreauthDTO uploadDTO = (PreauthDTO) parameters
				.getPrimaryParameter();
		FieldVisitRequest fvrobjByRodKey = fvrService.getPendingFieldVisitByClaimKey(uploadDTO.getClaimDTO().getKey());
		
		if((fvrobjByRodKey  == null || (fvrobjByRodKey != null && fvrobjByRodKey.getStatus() != null && ReferenceTable.INITITATE_FVR.equals(fvrobjByRodKey.getStatus().getKey())))){
		
			if(fvrobjByRodKey  != null ){
				dbCalculationService.invokeProcedureAutoSkipFVR(fvrobjByRodKey.getFvrId());
				if(uploadDTO.getIsFvrInitiate()){
				fvrService.autoSkipFirstFVR(fvrobjByRodKey);
				}else{
					fvrService.autoSkipNoFirstFVR(fvrobjByRodKey);
				}
			}
			}
		

	}
}
