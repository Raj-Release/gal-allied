package com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.premedicalprocessing;

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
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewBillSummaryPage;
import com.shaic.domain.FieldVisitRequestService;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PostHospitalisation;
import com.shaic.domain.PreHospitalisation;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementQueryService;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(MedicalApprovalPremedicalProcessingPageInterface.class)
public class MedicalApprovalPremedicalProcessingPagePresenter extends AbstractMVPPresenter<MedicalApprovalPremedicalProcessingPageInterface> {
	private static final long serialVersionUID = 3892478510257767753L;

	@EJB
	private MasterService masterService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService ackDocRecvdService;

	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private ReimbursementQueryService reimbursementQueryService;
	
	@EJB
	private CreateRODService createRodService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private InvestigationService investigationService;
	
	@javax.inject.Inject
	private ViewBillSummaryPage viewBillSummaryPage;
	
	@EJB
	private CreateRODService rodService;
	
	@EJB
	private FieldVisitRequestService fvrService;
	
	Map<String, Object> referenceData = new HashMap<String, Object>();
	
	public static final String GET_EXCLUSION_DETAILS = "medical_approval_zonal_review_get_exclusion_details";
	public static final String MEDICAL_APPROVAL_PRE_MEDICAL_PROCESSING_SETUP_REFERNCE = "medical_approval_prrmedical_processing_setup_reference";
	public static final String ZONAL_REVIEW_SUGGEST_QUERY_EVENT = "medical_approval_zonal_review_suggest_query_event";
	public static final String ZONAL_REVIEW_INITIATE_INV_EVENT = "medical_approval_zonal_review_initiate_invstg_event";
	public static final String ZONAL_REVIEW_SUGGEST_REJECTION_EVENT = "medical_approval_zonal_review_suggest_reject_event";
	public static final String ZONAL_REVIEW_REF_BILLENTRY_EVENT = "medical_approval_zonal_review_ref_billentry_event";
	public static final String ZONAL_REVIEW_SUGGEST_APPROVAL_EVENT = "medical_approval_zonal_review_suggest_approval_event";
	public static final String ZONAL_REVIEW_CANCEL_ROD_EVENT="medical_approval_zonal_review_cancel_rod_event";
	public static final String ZONAL_REVIEW_SAVE_BILL_ENTRY = "medical_approval_zonal_review_save_bill_entry";
	public static final String ZONAL_REVIEW_SUM_INSURED_CALCULATION = "medical_approval_zonal_review_row_by_row_calculation";

	public static final String BILL_ENTRY_TABLE_CATEGORY_DROPDOWN_VALUE = "medical_approval_zonal_review_bill_entry";

	public static final String BILL_ENTRY_COMPLETION_STATUS = "medical_approval_bill_entry_completion_status";
	
	public static final String ZONAL_SAVE_BILL_ENTRY_VALUES = "zonal_save_bill_entry_values";
	
	public static final String ZONAL_LOAD_BILL_DETAILS_VALUES = "zonal_load_bill_details_values";
	
	public static final String REFERENCE_DATA_CLEAR = "Reference data clear for zonal premedical processing";

	public static final String ZONAL_REVIEW_INITIATE_FVR_EVENT = "medical_approval__zonal_review_initiate_fvr_event";
	
	public static final String ZONAL_REVIEW_AUTO_SKIP_FVR = "zonal_review_auto_skip_fvr";
	
	
	public void getExclusionDetails(@Observes @CDIEvent(GET_EXCLUSION_DETAILS) final ParameterDTO parameters)
	{
		Long impactDiagnosisKey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<ExclusionDetails> icdCodeContainer = masterService.getExclusionDetailsByImpactKey(impactDiagnosisKey);
		
		view.setExclusionDetails(icdCodeContainer);
	}
	
	public void loadBillDetailsValues(
			@Observes @CDIEvent(ZONAL_LOAD_BILL_DETAILS_VALUES) final ParameterDTO parameters) {
		UploadDocumentDTO uploadDTO = (UploadDocumentDTO) parameters.getPrimaryParameter();
		List<BillEntryDetailsDTO> dtoList = rodService.getBillEntryDetailsList(uploadDTO);
		uploadDTO.setBillEntryDetailList(dtoList);
		view.setUploadDTOForBillEntry(uploadDTO);
	/*	Long billClassificationKey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getMasBillCategoryValues(billClassificationKey);*/
		//view.setUpCategoryValues(selectValueContainer);
		//view.enableOrDisableBtn(uploadDTO);
		}
	
	
	public void setUpReference(
			@Observes @CDIEvent(MEDICAL_APPROVAL_PRE_MEDICAL_PROCESSING_SETUP_REFERNCE) final ParameterDTO parameters) {
		
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		
		referenceData.put("commonValues", masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceData.put("exclusionDetails",masterService.getSelectValueContainer(ReferenceTable.EXCLUSION_DETAILS));
		referenceData.put("procedureStatus",masterService.getSelectValueContainer(ReferenceTable.PROCEDURE_STATUS));
		referenceData.put("pedExclusionImpactOnDiagnosis",masterService.getSelectValueContainer(ReferenceTable.IMPACT_ON_DIAGNOSI));
		referenceData.put("medicalCategory", masterService.getSelectValueContainer(ReferenceTable.MEDICAL_CATEGORY));
		referenceData.put("cancellationReason", masterService.getSelectValueContainer(ReferenceTable.CANCELLATION_REASON));
		referenceData.put("copay", getCopayValues(preauthDTO));
		referenceData.put("fileType", masterService
				.getSelectValueContainer(ReferenceTable.ROD_UPLOAD_DOC_TABLE_FILE_TYPE));
		//referenceData.put("billClassification", masterService.getMasBillClassificationValues());
		
		referenceData.put("billClassification", setClassificationValues(preauthDTO));
		referenceData.put("rejCateg", masterService.getReimbRejCategoryByValue()); 
//				masterService.getSelectValueContainer(ReferenceTable.REJECTION_CATEGORY));
		referenceData.put("setlRejCateg", masterService
				.getSelectValueContainer(ReferenceTable.SETTLED_REJECTION_CATEGORY));		
		
		referenceData.put("allocateTo", masterService.getSelectValueContainer(ReferenceTable.ALLOCATION_TO));
		
		referenceData.put("priority", masterService.getSelectValueContainer(ReferenceTable.FVR_PRIORITY));
		referenceData.put("coPayType", masterService.getSelectValueContainer(ReferenceTable.JIO_COPAY_TYPE_VALUE));		
		referenceData.put("reasonForNotPaying", masterService.getSelectValueContainer(ReferenceTable.PED_NON_PAYABLE_REASON));
		
		view.setupReferences(referenceData);
	}
	
	public BeanItemContainer<SelectValue> setClassificationValues(PreauthDTO preauthDTO)
	{
		BeanItemContainer<SelectValue> beanContainer = masterService.getMasBillClassificationValues();
		
		List<SelectValue> selectValueList = beanContainer.getItemIds();
		
		//List<SelectValue> finalClassificationList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> classificationValueContainer = null;
		if(null != selectValueList && !selectValueList.isEmpty())
		{
			List<SelectValue> finalClassificationList = new ArrayList<SelectValue>();
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
				
				else if((ReferenceTable.OTHER_BENEFIT).equals(selectValue.getId()) && (null != preauthDTO.getOtherBenefitsFlag() && preauthDTO.getOtherBenefitsFlag()))
				{
					finalClassificationList.add(selectValue);
				}
			}
			classificationValueContainer.addAll(finalClassificationList);
		}
		return classificationValueContainer;
	}
	
	private BeanItemContainer<SelectValue> getCopayValues(PreauthDTO dto) {
		 BeanItemContainer<SelectValue> coPayContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		 	List<String> coPayPercentage = new ArrayList<String>();
		    for (Double string : dto.getProductCopay()) {
		    	coPayPercentage.add(String.valueOf(string));
			}
		    
		   // Long i = 0l;
		    SelectValue value = null;
		    for (String string : coPayPercentage) {
		    	//SelectValue value = new SelectValue();
		    	value = new SelectValue();
		    	String[] copayWithPercentage = string.split("\\.");
				String copay = copayWithPercentage[0].trim();
		    	value.setId(Long.valueOf(copay));
		    	value.setValue(string);
		    	coPayContainer.addBean(value);
			}
		    
		    return coPayContainer;
	}
	
	public void generateFieldsForInvestigation(
			@Observes @CDIEvent(ZONAL_REVIEW_INITIATE_INV_EVENT) final ParameterDTO parameters) {/*
			@Observes @CDIEvent(ZONAL_REVIEW_INITIATE_INV_EVENT) final ParameterDTO parameters) {
		
>>>>>>> octrelease_26_10_2017
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
//		Boolean investigationAvailable = investigationService.getInvestigationByClaim(preauthDTO.getClaimKey());
		
		Boolean investigationAvailable = false;
		Reimbursement reimbObj = ackDocRecvdService.getReimbursementByRodNo(preauthDTO.getRodNumber());
		if(reimbObj != null){
			investigationAvailable = investigationService.getInvestigationAvailableForTransactionKey(reimbObj.getKey());
		}
		
		preauthDTO.setIsInvestigation(investigationAvailable);
		if(! preauthDTO.getIsInvestigation())
		{
		view.generateFieldsOnInvtClick();
		}
		else
		{
			view.alertMessageForInvestigation();
		}
	*/}
	
	public void generateFieldsForQuery(
			@Observes @CDIEvent(ZONAL_REVIEW_SUGGEST_QUERY_EVENT) final ParameterDTO parameters) {
		view.generateFieldsOnQueryClick();
	}

	public void generateFieldsForSuggestRejection(
			@Observes @CDIEvent(ZONAL_REVIEW_SUGGEST_REJECTION_EVENT) final ParameterDTO parameters) {
		view.generateFieldsOnSuggestRejectionClick();
	}
	
	public void generateFieldsForRefToBillEntry(
			@Observes @CDIEvent(ZONAL_REVIEW_REF_BILLENTRY_EVENT) final ParameterDTO parameters) {
		view.generateFieldsForRefToBillEntryClick();
	}	

	public void generateFieldsForSuggestApproval(
			@Observes @CDIEvent(ZONAL_REVIEW_SUGGEST_APPROVAL_EVENT) final ParameterDTO parameters) {
		view.generateFieldsOnSuggestApprovalClick();
	}
	public void generateFieldsForCancelROD(
			@Observes @CDIEvent(ZONAL_REVIEW_CANCEL_ROD_EVENT) final ParameterDTO parameters) {
		view.generateFieldsOnCancelRODClick();
	}
	
	public void setBillEntryStatus(
			@Observes @CDIEvent(BILL_ENTRY_COMPLETION_STATUS) final ParameterDTO parameters) {
			//Boolean status = (Boolean) parameters.getPrimaryParameter();
			UploadDocumentDTO	uploadDTO = (UploadDocumentDTO) parameters.getPrimaryParameter();
			rodService.saveBillEntryValues(uploadDTO);
			view.setBillEntryFinalStatus(uploadDTO);
		
	}
	
	public void saveBillValues(
			@Observes @CDIEvent(ZONAL_SAVE_BILL_ENTRY_VALUES) final ParameterDTO parameters) {
			//Boolean status = (Boolean) parameters.getPrimaryParameter();
			UploadDocumentDTO	uploadDTO = (UploadDocumentDTO) parameters.getPrimaryParameter();
			rodService.saveBillEntryValues(uploadDTO);
			//view.setBillEntryFinalStatus(uploadDTO);
		
	}
	
	public void getValuesForMedicalDecisionTable(
			@Observes @CDIEvent(ZONAL_REVIEW_SUM_INSURED_CALCULATION) final ParameterDTO parameters) {
		 	Map<String, Object> values = (Map<String, Object>) parameters.getPrimaryParameter();
		 	DiagnosisProcedureTableDTO dto = (DiagnosisProcedureTableDTO) parameters.getSecondaryParameters()[0];
		 	PreauthDTO preauthDto = (PreauthDTO) parameters.getSecondaryParameter(1, PreauthDTO.class);
			String diagnosis = null;
			if(values.containsKey("diagnosisId")) {
				diagnosis = masterService.getDiagnosis(Long.valueOf((String) values.get("diagnosisId")));
			}
			
			if (dto.getDiagnosisDetailsDTO() != null) {
				dto.getDiagnosisDetailsDTO()
						.setDiagnosis(diagnosis);
			}	
			
			Reimbursement hospitalizationRod = null;
			
			Boolean queryRaisedFromMA = reimbursementQueryService.isQueryRaisedFromMA(preauthDto.getKey());
			if(queryRaisedFromMA){
				preauthDto.setIsQueryReceived(false);
			}
			
			if(! preauthDto.getIsQueryReceived()){

				if(preauthDto.getClaimDTO().getClaimType() != null && preauthDto.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
					
					//hospitalizationRod = reimbursementService.getHospitalizationRod(preauthDto.getClaimKey(), preauthDto.getKey());
					hospitalizationRod = reimbursementService.getHospitalizationRodForMA(preauthDto.getClaimKey(), preauthDto.getKey(),preauthDto.getRodNumber());
					
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
					
					//hospitalizationRod = reimbursementService.getHospitalizationRod(preauthDto.getClaimKey(), preauthDto.getKey());
					hospitalizationRod = reimbursementService.getHospitalizationRodForMA(preauthDto.getClaimKey(), preauthDto.getKey(),preauthDto.getRodNumber());
					if(hospitalizationRod != null){
						values.put("preauthKey", hospitalizationRod.getKey());
					}else{
						values.put("preauthKey",0l);
					}
				}
			}else{
				values.put("preauthKey", preauthDto.getKey());
			}
			
			if((preauthDto.getClaimDTO().getClaimType() != null && preauthDto.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)) || (preauthDto.getIsHospitalizationRepeat() != null && preauthDto.getIsHospitalizationRepeat())) {
				values.put("preauthKey", 0l);
				//if(! (preauthDto.getIsHospitalizationRepeat() != null && preauthDto.getIsHospitalizationRepeat())){
					if(hospitalizationRod != null){
						values.put("preauthKey", hospitalizationRod.getKey());
					}else{
						values.put("preauthKey",0l);
					}
				//}
			}
			
			if(preauthDto.getIsHospitalizationRepeat() != null && preauthDto.getIsHospitalizationRepeat()){
				values.put("preauthKey", preauthDto.getKey());
			}
			
			Map<String, Object> medicalDecisionTableValues = null;
			
			values.put("productKey", preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey());
			
			if(ReferenceTable.getGMCProductList().containsKey(preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				medicalDecisionTableValues = dbCalculationService.getMedicalDecisionTableValueForGMC(values,preauthDto.getNewIntimationDTO());
			}else{
				medicalDecisionTableValues = dbCalculationService.getMedicalDecisionTableValue(values,preauthDto.getNewIntimationDTO());
			}
			
			
			if(values.containsKey(SHAConstants.IS_NON_ALLOPATHIC) && (Boolean)values.get(SHAConstants.IS_NON_ALLOPATHIC)) {
				
					Map<String, Double> nonAllopathicAmount = null;
					if(preauthDto.getClaimDTO().getClaimType() != null && preauthDto.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
	//				nonAllopathicAmount = dbCalculationService.getNonAllopathicAmount((Long)values.get(SHAConstants.POLICY_KEY), (Long)values.get(SHAConstants.INSURED_KEY)
	//						,preauthDto.getPreauthKey(),"C");
	//				if(preauthDto.getClaimDTO().getLatestPreauthKey() != null){
	//					nonAllopathicAmount = dbCalculationService.getNonAllopathicAmount((Long)values.get(SHAConstants.POLICY_KEY), (Long)values.get(SHAConstants.INSURED_KEY)
	//							,preauthDto.getClaimDTO().getLatestPreauthKey(),"C");
	//				}
						
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
								,(Long)values.get("preauthKey"),"R", (Long)values.get(SHAConstants.CLAIM_KEY));
					}
					medicalDecisionTableValues.put(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT, nonAllopathicAmount.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT) != null ? nonAllopathicAmount.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT) :0d);
					medicalDecisionTableValues.put(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT, nonAllopathicAmount.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT) != null ? nonAllopathicAmount.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT) : 0d);
					
					//added for jira IMSSUPPOR-27044
					if(preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MEDI_CLASSIC_HELATH_GROUP_PRODUCT_KEY)){
						medicalDecisionTableValues.put(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT, preauthDto.getBalanceSI());
						medicalDecisionTableValues.put(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT, 0d);
					}
			}
			
			
			view.getValuesForMedicalDecisionTable(dto, medicalDecisionTableValues);
	}
	
	public void setUpCategoryValues(
			@Observes @CDIEvent(BILL_ENTRY_TABLE_CATEGORY_DROPDOWN_VALUE) final ParameterDTO parameters) {
		Long billClassificationKey = (Long) parameters.getPrimaryParameter();
		SelectValue claimType = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		Long productKey = (Long) parameters.getSecondaryParameter(2, Long.class);
		String subCoverValue = (String) parameters.getSecondaryParameter(3, String.class);
		Boolean isDomicillary = (Boolean) parameters.getSecondaryParameter(4, Boolean.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getMasBillCategoryValuesForZonalAndMedical(billClassificationKey,claimType,isDomicillary);
		
		
		List<SelectValue> selectValueList = selectValueContainer.getItemIds();
		List<SelectValue> finalCategoryList = new ArrayList<SelectValue>();
		//List<SelectValue> finalCategoryList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> categoryValueContainer = null;
		if(null != selectValueList && !selectValueList.isEmpty())
		{
			List<SelectValue> protaCategoryList = new ArrayList<SelectValue>();
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
							else
							{
								finalCategoryList.add(selectValue);
							}
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
					else{
						finalCategoryList.add(selectValue);
					}
				}
				//added for GMC prorata Calculation
				if(productKey != null && !(ReferenceTable.STAR_GMC_PRODUCT_KEY.equals(productKey) || ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY.equals(productKey))){
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
		if((ReferenceTable.STAR_WEDDING_GIFT_INSURANCE.equals(productKey) || (ReferenceTable.getComprehensiveProducts().containsKey(productKey))) && (!ReferenceTable.getMaternityMap().containsKey(subCoverValue)) ||
				ReferenceTable.POS_FAMILY_HEALTH_OPTIMA.equals(productKey) || (ReferenceTable.STAR_GMC_PRODUCT_KEY.equals(productKey)|| ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY.equals(productKey))) 
			view.setUpCategoryValues(categoryValueContainer);
		else	
			view.setUpCategoryValues(selectValueContainer);
		}
		//view.setUpCategoryValues(selectValueContainer);
	
	public void saveBillEntryValues(
			@Observes @CDIEvent(ZONAL_REVIEW_SAVE_BILL_ENTRY) final ParameterDTO parameters) {
		PreauthDTO bean = (PreauthDTO) parameters.getPrimaryParameter();
		reimbursementService.billEntrySave(bean);
		
		bean =  SHAUtils.roomRentNursingMapping(bean, 8l, 9l, false,false);
		bean = SHAUtils.roomRentNursingMapping(bean, 10l, 11l, true,false);
		bean =  SHAUtils.roomRentNursingMapping(bean, 85l, 84l, false,true);
		reimbursementService.saveRoomRentNursingMapping(bean, bean.getRoomRentMappingDTOList());
		
		viewBillSummaryPage.init(bean,bean.getKey(), true);
		Map<String, String> payableAmount = viewBillSummaryPage.getPayableAmount();
		Double sumValue = 0d;
		
		dbCalculationService.getBillDetailsSummary(bean.getKey());
		
		
		String string = payableAmount.get(SHAConstants.HOSPITALIZATION);
		
		sumValue = SHAUtils.getDoubleValueFromString(string);
		
		setPreAndPostHopitalizationAmount(bean);
		
		
		view.setBillEntryAmountConsiderValue(sumValue);
    }
	
	
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void setReferenceDataClear(
			@Observes @CDIEvent(REFERENCE_DATA_CLEAR) final ParameterDTO parameters) {
		SHAUtils.setClearReferenceData(referenceData);
		
	}
	
	private PreauthDTO setPreAndPostHopitalizationAmount(PreauthDTO preauthDTO) {
		List<PreHospitalisation> preHospitalisationValues = createRodService
				.getPreHospitalisationList(preauthDTO.getKey());
		List<PostHospitalisation> postHospitalisationValues = createRodService
				.getPostHospitalisationList(preauthDTO.getKey());
		Integer postHospitalizationAmt = 0;
		Integer preHospitalizationAmt = 0;
		for (PostHospitalisation postHospitalisation : postHospitalisationValues) {
			postHospitalizationAmt += postHospitalisation
					.getClaimedAmountBills() != null ? postHospitalisation
					.getClaimedAmountBills().intValue() : 0;
		}

		for (PreHospitalisation preHospitalisation : preHospitalisationValues) {
			preHospitalizationAmt += preHospitalisation.getClaimedAmountBills() != null ? preHospitalisation
					.getClaimedAmountBills().intValue() : 0;
		}
		preauthDTO.getPreauthMedicalDecisionDetails().setBillingRemarks("");
		preauthDTO.setPreHospitalisationValue(String
				.valueOf(preHospitalizationAmt));
		preauthDTO.setPostHospitalisationValue(String
				.valueOf(postHospitalizationAmt));

		return preauthDTO;
	}
	
	public void generateFieldsForInitiateFVR(
			@Observes @CDIEvent(ZONAL_REVIEW_INITIATE_FVR_EVENT) final ParameterDTO parameters) {
		view.generateFieldsOnInitiateFvrClick();
	}
	
	public void submitAutoSkipFVR(
			@Observes @CDIEvent(ZONAL_REVIEW_AUTO_SKIP_FVR) final ParameterDTO parameters) {
		// Boolean status = (Boolean) parameters.getPrimaryParameter();
		PreauthDTO uploadDTO = (PreauthDTO) parameters
				.getPrimaryParameter();
		FieldVisitRequest fvrobjByRodKey = fvrService.getPendingFieldVisitByClaimKey(uploadDTO.getClaimDTO().getKey());
		
		if((fvrobjByRodKey  == null || (fvrobjByRodKey != null && fvrobjByRodKey.getStatus() != null && ReferenceTable.INITITATE_FVR.equals(fvrobjByRodKey.getStatus().getKey())))){
		
			if(fvrobjByRodKey  != null ){
				dbCalculationService.invokeProcedureAutoSkipFVR(fvrobjByRodKey.getFvrId());
				fvrService.autoSkipFirstFVR(fvrobjByRodKey);
			}
			}
		

	}
}
