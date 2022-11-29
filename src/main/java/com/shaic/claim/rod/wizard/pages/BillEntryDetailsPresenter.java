package com.shaic.claim.rod.wizard.pages;

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
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.UpdateOtherClaimDetailDTO;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.Intimation;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author ntv.vijayar
 *
 */
@ViewInterface(BillEntryDetailsView.class)
public class BillEntryDetailsPresenter  extends AbstractMVPPresenter<BillEntryDetailsView>
{

	public static final String BILL_ENTRY_DETAILS_PAGE_UPLOADED_DOC_SETUP_DROPDOWN_VALUES = "bill_entry_details_page_uploaded_doc_setup_dropdown_values";
	
	public static final String BILL_ENTRY_TABLE_CLASSIFICATION_DROPDOWN_VALUE = "bill_entry_table_classification_dropdown_values";
	
	public static final String BILL_ENTRY_TABLE_CATEGORY_DROPDOWN_VALUE = "bill_entry_table_category_dropdown_values";
	
	public static final String BILL_ENTRY_COMPLETION_STATUS = "bill_entry_completion_status";
	
	public static final String BILL_ENTRY_BILLING_HOSPITAL_BENEFITS = "bill_entry_billing_hospital_cash_benefits";
	
	
	public static final String BILL_ENTRY_BILLING_PATIENT_CARE_BENEFITS = "bill_entry_billing_patiet_care_benefits";
	
	public static final String BILL_ENTRY_ADD_ON_BENEFITS_HOSPITAL_CASH = "add_on_benefits_hospital_cash";
	
	public static final String BILL_ENTRY_ADD_ON_BENEFITS_PATIENT_CARE= "add_on_benefits_patient_care";
	
	public static final String SELECT_BILL_ENTRY_BTN_ENABLE_DISABLE = "select_bill_entry_btn_enable_disable";
	
	
	public static final String SAVE_BILL_ENTRY_VALUES = "save_bill_entry_values";
	
	public static final String LOAD_BILL_DETAILS_VALUES = "load_bill_details_values";
	
	public static final String UPDATE_PREVIOUS_CLAIM_DETAILS = "update_previous_claim_details for bill entry";

	public static final String BILL_ENTRY_HOSPITAL_CASH_PRODUCT_BENEFITS = "bill Entry Hospital Cash Product Benefits";
	
	/*public static final String SUBMIT_UPLOADED_DOCUMENTS = "submit_uploaded_documents";
	
	public static final String DELETE_UPLOADED_DOCUMENTS = "delete_uploaded_documents";*/

	
	//public static final String SETUP_DOCUMENT_CHECKLIST_TABLE_VALUES = "setup_doc_checklist_tbl_values";
	
	
	
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private CreateRODService rodService;
	
	@EJB
	private PreauthService preauthService;
	
	/*@EJB
	private AcknowledgementDocumentsReceivedService ackDocReceivedService;*/

	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	
	public void setUpDropDownValues(
			@Observes @CDIEvent(BILL_ENTRY_DETAILS_PAGE_UPLOADED_DOC_SETUP_DROPDOWN_VALUES) final ParameterDTO parameters) {
		
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO)parameters.getPrimaryParameter();
		
		Map<String, Object> referenceDataMap = new HashMap<String, Object>();
		referenceDataMap.put("fileType", masterService
				.getSelectValueContainer(ReferenceTable.ROD_UPLOAD_DOC_TABLE_FILE_TYPE));
		
		
		
		//referenceDataMap.put("billClassification", masterService.getMasBillClassificationValues());
		referenceDataMap.put("billClassification", setClassificationValues(rodDTO));
		
		view.setUpDropDownValues(referenceDataMap);
		
		}
	
	
	
	
	public BeanItemContainer<SelectValue> setClassificationValues(ReceiptOfDocumentsDTO rodDTO)
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
				/*if(("Hospitalization").equalsIgnoreCase(selectValue.getValue()) && ((null != rodDTO.getDocumentDetails().getHospitalization() && rodDTO.getDocumentDetails().getHospitalization())
						|| (null != rodDTO.getDocumentDetails().getHospitalizationRepeat() && rodDTO.getDocumentDetails().getHospitalizationRepeat())  || (null != rodDTO.getDocumentDetails().getPartialHospitalization() && rodDTO.getDocumentDetails().getPartialHospitalization())
						))
				{
					finalClassificationList.add(selectValue);
				}
				else if(("Pre-Hospitalization").equalsIgnoreCase(selectValue.getValue()) && (null != rodDTO.getDocumentDetails().getPreHospitalization() && rodDTO.getDocumentDetails().getPreHospitalization()))
				{
					finalClassificationList.add(selectValue);
				}
				else if(("Post-Hospitalization").equalsIgnoreCase(selectValue.getValue()) && (null != rodDTO.getDocumentDetails().getPostHospitalization() && rodDTO.getDocumentDetails().getPostHospitalization()))
				{
					finalClassificationList.add(selectValue);
				}
				else if(("Lump Sum Amount").equalsIgnoreCase(selectValue.getValue()) && (null != rodDTO.getDocumentDetails().getLumpSumAmount() && rodDTO.getDocumentDetails().getLumpSumAmount()))
				{
					finalClassificationList.add(selectValue);
				}*/
				
				if((ReferenceTable.HOSPITALIZATION).equals(selectValue.getId()) && ((null != rodDTO.getDocumentDetails().getHospitalization() && rodDTO.getDocumentDetails().getHospitalization())
						|| (null != rodDTO.getDocumentDetails().getHospitalizationRepeat() && rodDTO.getDocumentDetails().getHospitalizationRepeat())  || (null != rodDTO.getDocumentDetails().getPartialHospitalization() && rodDTO.getDocumentDetails().getPartialHospitalization())
						))
				{
					finalClassificationList.add(selectValue);
				}
				else if((ReferenceTable.PRE_HOSPITALIZATION).equals(selectValue.getId()) && (null != rodDTO.getDocumentDetails().getPreHospitalization() && rodDTO.getDocumentDetails().getPreHospitalization()))
				{
					finalClassificationList.add(selectValue);
				}
				else if((ReferenceTable.POST_HOSPITALIZATION).equals(selectValue.getId()) && (null != rodDTO.getDocumentDetails().getPostHospitalization() && rodDTO.getDocumentDetails().getPostHospitalization()))
				{
					finalClassificationList.add(selectValue);
				}
				else if((ReferenceTable.LUMPSUM).equals(selectValue.getId()) && (null != rodDTO.getDocumentDetails().getLumpSumAmount() && rodDTO.getDocumentDetails().getLumpSumAmount()))
				{
					finalClassificationList.add(selectValue);
				}
				else if((ReferenceTable.OTHER_BENEFIT).equals(selectValue.getId()) && (null != rodDTO.getDocumentDetails().getOtherBenefits() && rodDTO.getDocumentDetails().getOtherBenefits()))
				{
					finalClassificationList.add(selectValue);
				}
				else if((ReferenceTable.HOSPITAL_CASH_BILL_ID).equals(selectValue.getId()) && (null != rodDTO.getDocumentDetails().getHospitalCash() && rodDTO.getDocumentDetails().getHospitalCash()))
				{
					finalClassificationList.add(selectValue);
				}
				
			}
			classificationValueContainer.addAll(finalClassificationList);
		}
		return classificationValueContainer;
	}
	
	public void setUpCategoryValues(
			@Observes @CDIEvent(BILL_ENTRY_TABLE_CATEGORY_DROPDOWN_VALUE) final ParameterDTO parameters) {
		Long billClassificationKey = (Long) parameters.getPrimaryParameter();
		SelectValue claimType = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getMasBillCategoryValues(billClassificationKey,claimType);
		Long productKey = (Long) parameters.getSecondaryParameter(2, Long.class);
		String subCoverValue = (String) parameters.getSecondaryParameter(3, String.class);
		
		String intimationNo = (String) parameters.getSecondaryParameter(4, String.class);
		
		Intimation intimationObj = preauthService.getIntimationByNo(intimationNo);
		
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
						else
						{
							finalCategoryList.add(selectValue);
						}
					}
					else if(ReferenceTable.STAR_SPECIAL_CARE_PRODUCT_KEY.equals(productKey) 
							/*|| ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(productKey)*/){
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
										SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode())))){
							if(billClassificationKey.equals(12L)){
								if(selectValue.getId().equals(ReferenceTable.SHARED_ACCOMODATION)){
									finalCategoryList.add(selectValue);
								}
							}else{
								finalCategoryList.add(selectValue);
							}
						}
					// new added for GLX2020193
					else if(ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(productKey) || ReferenceTable.getValuableServiceProviderForFHO().containsKey(productKey)){
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
				if(intimationObj != null && !(ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(intimationObj.getPolicy().getProduct().getKey())
						|| ReferenceTable.getValuableServiceProviderForFHO().containsKey(intimationObj.getPolicy().getProduct().getKey()))){
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
				ReferenceTable.POS_FAMILY_HEALTH_OPTIMA.equals(productKey) || (ReferenceTable.STAR_GMC_PRODUCT_KEY.equals(productKey)|| ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY.equals(productKey))
				|| (((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode())
						|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode()))
						&& ("G").equalsIgnoreCase(intimationObj.getInsured().getPolicyPlan()))) 
						|| ((SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode()) || 
								SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(intimationObj.getPolicy().getProduct().getCode()))
								&& intimationObj.getPolicy().getProductType().getKey().equals(ReferenceTable.INDIVIDUAL_POLICY))
								|| (ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(intimationObj.getPolicy().getProduct().getKey())))
			view.setUpCategoryValues(categoryValueContainer);
		else	
			view.setUpCategoryValues(selectValueContainer);
		}
	
	public void setBillEntryStatus(
			@Observes @CDIEvent(BILL_ENTRY_COMPLETION_STATUS) final ParameterDTO parameters) {
			//Boolean status = (Boolean) parameters.getPrimaryParameter();
			UploadDocumentDTO	uploadDTO = (UploadDocumentDTO) parameters.getPrimaryParameter();
			rodService.saveBillEntryValues(uploadDTO);
			view.setBillEntryFinalStatus(uploadDTO);
		
	}
	
	public void saveBillEntryValues(
			@Observes @CDIEvent(SAVE_BILL_ENTRY_VALUES) final ParameterDTO parameters) {
			//Boolean status = (Boolean) parameters.getPrimaryParameter();
			UploadDocumentDTO	uploadDTO = (UploadDocumentDTO) parameters.getPrimaryParameter();
			rodService.saveBillEntryValues(uploadDTO);
			//view.setBillEntryFinalStatus(uploadDTO);
		
	}
	
	
	public void generateFieldsBasedHospitalCashAddOnBenefits(@Observes @CDIEvent(BILL_ENTRY_BILLING_HOSPITAL_BENEFITS) final ParameterDTO parameters)
	{
		Boolean selectedValue = (Boolean) parameters.getPrimaryParameter();
		view.generateFieldsBasedOnHospitalCashBenefits(selectedValue);
	}
	
	public void generateFieldsBasedPatientCareAddOnBenefits(@Observes @CDIEvent(BILL_ENTRY_BILLING_PATIENT_CARE_BENEFITS) final ParameterDTO parameters)
	{
		Boolean selectedValue = (Boolean) parameters.getPrimaryParameter();
		view.generateFieldsBasedOnPatientCareBenefits(selectedValue);
	}
	
	public void calculateNoOfDaysForHospitalCash(@Observes @CDIEvent(BILL_ENTRY_ADD_ON_BENEFITS_HOSPITAL_CASH) final ParameterDTO parameters)
	{
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		Double sumInsured = dbCalculationService.getInsuredSumInsured(String.valueOf(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getInsuredId()), rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getKey(),
				rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getLopFlag());
		List<Object> addOnBenefitsValues = dbCalculationService.getAddOnBenefitsValues(rodDTO.getDocumentDetails().getRodKey(), 
				rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getKey(),sumInsured , rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey(),"HC");
		view.setUpHospitalCashValues(addOnBenefitsValues);
		
		
	}
	
	public void calculateNoOfDaysForPatientCare(@Observes @CDIEvent(BILL_ENTRY_ADD_ON_BENEFITS_PATIENT_CARE) final ParameterDTO parameters)
	{
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		Double sumInsured = dbCalculationService.getInsuredSumInsured(String.valueOf(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getInsuredId()), rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getKey(),
				rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getLopFlag());
		List<Object> addOnBenefitsValues = dbCalculationService.getAddOnBenefitsValues(rodDTO.getDocumentDetails().getRodKey(), 
				rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getKey(),sumInsured , rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey(),"PC");
		view.setUpPatientCareValues(addOnBenefitsValues);
		
		
	}
	
	
	public void enableOrDisableBtn(
			@Observes @CDIEvent(SELECT_BILL_ENTRY_BTN_ENABLE_DISABLE) final ParameterDTO parameters) {
		UploadDocumentDTO uploadDTO = (UploadDocumentDTO) parameters.getPrimaryParameter();
	/*	Long billClassificationKey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getMasBillCategoryValues(billClassificationKey);*/
		//view.setUpCategoryValues(selectValueContainer);
		view.enableOrDisableBtn(uploadDTO);
		}
	
	public void loadBillDetailsValues(
			@Observes @CDIEvent(LOAD_BILL_DETAILS_VALUES) final ParameterDTO parameters) {
		UploadDocumentDTO uploadDTO = (UploadDocumentDTO) parameters.getPrimaryParameter();
		List<BillEntryDetailsDTO> dtoList = rodService.getBillEntryDetailsList(uploadDTO);
		uploadDTO.setBillEntryDetailList(dtoList);
		view.setUploadDTOForBillEntry(uploadDTO);
	/*	Long billClassificationKey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getMasBillCategoryValues(billClassificationKey);*/
		//view.setUpCategoryValues(selectValueContainer);
		//view.enableOrDisableBtn(uploadDTO);
		}
	
	public void showUpdateClaimDetailTable(
			@Observes @CDIEvent(UPDATE_PREVIOUS_CLAIM_DETAILS) final ParameterDTO parameters) {
		
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		rodDTO.getPreauthDTO().setNewIntimationDTO(rodDTO.getClaimDTO().getNewIntimationDto());
		
		List<UpdateOtherClaimDetailDTO> updateOtherClaimDetails = new ArrayList<UpdateOtherClaimDetailDTO>();
		
		if(rodDTO.getPreauthDTO().getUpdateOtherClaimDetailDTO().isEmpty()){
			/*updateOtherClaimDetails = preauthService.getUpdateOtherClaimDetails(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getKey());
			List<UpdateOtherClaimDetailDTO> updateOtherClaimDetailsForExisting = preauthService.getUpdateOtherClaimDetailsDTOForReimbursement(rodDTO.getDocumentDetails().getKey());
			updateOtherClaimDetails.addAll(updateOtherClaimDetailsForExisting);*/
			updateOtherClaimDetails = preauthService.getUpdateOtherClaimDetailsDTOForReimbursement(rodDTO.getDocumentDetails().getRodKey());
			if(updateOtherClaimDetails != null && updateOtherClaimDetails.isEmpty()){
				updateOtherClaimDetails = preauthService.getUpdateOtherClaimDetails(rodDTO.getPreauthDTO().getNewIntimationDTO().getPolicy().getKey(),rodDTO.getPreauthDTO());
			}
		}else{
			updateOtherClaimDetails = rodDTO.getPreauthDTO().getUpdateOtherClaimDetailDTO();
		}
	    
	    view.setUpdateOtherClaimsDetails(updateOtherClaimDetails);

	}
	
	//added for new product
	public void generateFieldsBasedOnHospitalCashProduct(@Observes @CDIEvent(BILL_ENTRY_HOSPITAL_CASH_PRODUCT_BENEFITS) final ParameterDTO parameters)
	{
		Boolean selectedValue = (Boolean) parameters.getPrimaryParameter();
		view.generateFieldsBasedOnHospitalCashProduct(selectedValue);
	}
	
	
	/*public void setUpBillClassificationDropDownValues(
			@Observes @CDIEvent(BILL_ENTRY_TABLE_CLASSIFICATION_DROPDOWN_VALUE) final ParameterDTO parameters) {
	
		 BeanItemContainer<SelectValue> billClassificationContainer = masterService.getMasBillClassificationValues();
		view.setUpBillClassificationDropDownValues(billClassificationContainer);
		
		}*/
	
	
	/*public void submitUploadedDocuments(
			@Observes @CDIEvent(SUBMIT_UPLOADED_DOCUMENTS) final ParameterDTO parameters) {
		
		//ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO)parameters.getPrimaryParameter();
		List<UploadDocumentDTO> uploadDocLst = (List<UploadDocumentDTO>) parameters.getPrimaryParameter();
		view.loadUploadedDocsTableValues(uploadDocLst);
		
	}
	
	public void deleteUploadedDocumentDetails(
			@Observes @CDIEvent(DELETE_UPLOADED_DOCUMENTS) final ParameterDTO parameters) {
		
		UploadDocumentDTO uploadDTO = (UploadDocumentDTO)parameters.getPrimaryParameter();
		view.deleteUploadDocumentDetails(uploadDTO);*/
		
		//List<DocumentDetailsDTO> documentDetailsDTO = rodService.getDocumentDetailsDTO(claimKey);
		
	//view.setDocumentDetailsDTOForValidation(documentDetailsDTO);
	}
	
	
	
	/*public void setUpDocumentCheckListValues(
			@Observes @CDIEvent(SETUP_DOCUMENT_CHECKLIST_TABLE_VALUES) final ParameterDTO parameters) {
		List<DocumentCheckListDTO> documentCheckListDTO = ackDocReceivedService.getDocumentCheckListValues(masterService);
		view.setDocumentCheckListTableValues(documentCheckListDTO);
		
		}
*/



