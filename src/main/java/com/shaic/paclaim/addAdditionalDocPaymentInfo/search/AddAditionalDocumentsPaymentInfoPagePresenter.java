package com.shaic.paclaim.addAdditionalDocPaymentInfo.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.reimbursement.rrc.services.SearchRRCRequestTable;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.Insured;
import com.shaic.domain.MasterService;
import com.shaic.domain.NomineeDetails;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.RODBillDetails;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(AddAditionalDocumentsPaymentInfoView.class)
public class AddAditionalDocumentsPaymentInfoPagePresenter extends
		AbstractMVPPresenter<AddAditionalDocumentsPaymentInfoView> {
	
	
	public static final String PAYMENT_INFO_SETUP_DROPDOWN_VALUES = "pa_payment_info_setup_dropdown_values";
	
	public static final String SUBMIT_PAYMENT_INFO_DOCUMENTS = "submit_pa_payment_info_documents";
	
	public static final String DELETE_PAYMENT_INFO_DOCUMENTS = "delete_pa_payment_info_documents";
	
	public static final String EDIT_PAYMENT_INFO_DOCUMENTS = "edit_pa_payment_info_documents";
	
	public static final String UPDATE_BEAN = "PA_PAYMENT_INFO_UPDATE BEAN";
	
	public static final String SUBMIT_DETAILS = "submit_pa_add_additional_documents_payment_info";
	
	public static final String SETUP_PAYMENT_INFO_DOCUMENTS_PAYABLE_DETAILS  = "setup_payable_details_pa_addl_docs_payment_info";
	
	public static final String SETUP_PAYMENT_INFO_DOCUMENTS_IFSC_DETAILS  = "setup_ifsc_details_pa_addl_docs_payment_info";
	
	public static final String PAYMENT_INFO_POPULATE_PREVIOUS_ACCT_DETAILS = "pa_payment_info_financial_populate_previous_acct_details";
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private CreateRODService rodService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private DBCalculationService dbCalculationService;

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void setUpDropDownValues(
			@Observes @CDIEvent(PAYMENT_INFO_SETUP_DROPDOWN_VALUES) final ParameterDTO parameters) {
	//	ReceiptOfDocumentsDTO bean = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		ReceiptOfDocumentsDTO bean  = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		Map<String, Object> referenceDataMap = new HashMap<String, Object>();
		BeanItemContainer<SelectValue> beanContainer = masterService.getSelectValueContainer(ReferenceTable.ROD_UPLOAD_DOC_TABLE_FILE_TYPE);
		
		referenceDataMap.put("fileType", beanContainer);
		
			view.setUpDropDownValues(referenceDataMap);
		
		}
	
	
	public void submitUploadedDocuments(
			@Observes @CDIEvent(SUBMIT_PAYMENT_INFO_DOCUMENTS) final ParameterDTO parameters) {
		
		//ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO)parameters.getPrimaryParameter();
		//List<UploadDocumentDTO> uploadDocLst = (List<UploadDocumentDTO>) parameters.getPrimaryParameter();
		UploadDocumentDTO uploadDoc = (UploadDocumentDTO) parameters.getPrimaryParameter();
		//view.loadUploadedDocsTableValues(uploadDocLst);
		view.loadUploadedDocsTableValues(uploadDoc);
		
	}
	
	public void deleteUploadedDocumentDetails(
			@Observes @CDIEvent(DELETE_PAYMENT_INFO_DOCUMENTS) final ParameterDTO parameters) {
		
		UploadDocumentDTO uploadDTO = (UploadDocumentDTO)parameters.getPrimaryParameter();
		view.deleteUploadDocumentDetails(uploadDTO);
		
		//List<DocumentDetailsDTO> documentDetailsDTO = rodService.getDocumentDetailsDTO(claimKey);
		
	//view.setDocumentDetailsDTOForValidation(documentDetailsDTO);
	}
	
	
	public void editUploadedDocumentDetails(
			@Observes @CDIEvent(EDIT_PAYMENT_INFO_DOCUMENTS) final ParameterDTO parameters) {
		
		UploadDocumentDTO uploadDTO = (UploadDocumentDTO)parameters.getPrimaryParameter();
		view.editUploadDocumentDetails(uploadDTO);
		
	}
	
	public void updateBeanDetails(
			@Observes @CDIEvent(UPDATE_BEAN) final ParameterDTO parameters) {
		ReceiptOfDocumentsDTO bean = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		Boolean isOptionSelected = (Boolean) parameters.getSecondaryParameter(0, Boolean.class);
		List<UploadDocumentDTO> rodSummaryDetails = rodService
				.getNeftRODSummaryDetails(bean.getDocumentDetails().getRodKey());
		
		if(null != rodSummaryDetails && !rodSummaryDetails.isEmpty())
		{
			List<BillEntryDetailsDTO> dtoList = new ArrayList<BillEntryDetailsDTO>();
		//	List<UploadDocumentDTO> uploadDocsList = rodDTO.getUploadDocsList();
			for (UploadDocumentDTO uploadDocDTO : rodSummaryDetails) {
				//sss
				uploadDocDTO.setIsBillSaved(true);
				uploadDocDTO.setReimbursementKey(bean.getDocumentDetails().getRodKey());
				uploadDocDTO.setRodKey(bean.getDocumentDetails().getRodKey());
				
				List<RODBillDetails> billEntryDetails = rodService
						.getBillEntryDetails(uploadDocDTO.getDocSummaryKey());
				if (billEntryDetails != null && !billEntryDetails.isEmpty()) {
					for (RODBillDetails billEntryDetailsDO : billEntryDetails) {
						
						 
						 
						dtoList.add(getBillDetailsDTOForBillEntry(billEntryDetailsDO));
						
						// >>>>>>> 7656df822ab29676fae4ca1d8cd4ec48ac917fe0
					}
					uploadDocDTO.setBillEntryDetailList(dtoList);
				}
			}
		//	rodDTO.getUploadDocumentsDTO().setBillEntryDetailList(dtoList);
			bean.setUploadDocsList(rodSummaryDetails);
			
		}
		
		Map<String, Object> referenceData  = getValuesForNameDropDown(bean);
		
		
		view.updateBean(bean, referenceData);
	}
	
	private BillEntryDetailsDTO getBillDetailsDTOForBillEntry(RODBillDetails billDetails) {
		// >>>>>>> 7656df822ab29676fae4ca1d8cd4ec48ac917fe0
		BillEntryDetailsDTO dto = new BillEntryDetailsDTO();
		
		dto.setItemName(billDetails.getItemName());
		dto.setKey(billDetails.getKey());
		SelectValue classificationValue = new SelectValue();
		classificationValue.setId(billDetails.getBillClassification().getKey());
		classificationValue.setValue(billDetails.getBillClassification()
				.getValue());
		dto.setClassification(classificationValue);
		dto.setItemNo(billDetails.getItemNumber());
		/*dto.setNoOfDays(billDetails.getNoOfDaysBills() != null ? billDetails
				.getNoOfDaysBills().doubleValue() : 0d);*/
		dto.setNoOfDays(billDetails.getNoOfDaysBills() != null ? billDetails
				.getNoOfDaysBills().doubleValue() : null);
		dto.setPerDayAmt(billDetails.getPerDayAmountBills());
		dto.setBillValue(billDetails.getClaimedAmountBills());
		dto.setItemValue(billDetails.getClaimedAmountBills());
		SelectValue billCategoryvalue = new SelectValue();
		billCategoryvalue.setId(billDetails.getBillCategory().getKey());
		billCategoryvalue.setValue(billDetails.getBillCategory().getValue());
		dto.setCategory(billCategoryvalue);
		dto.setDocumentSummaryKey(billDetails.getRodDocumentSummaryKey().getKey());
		dto.setKey(billDetails.getKey());
		
		Reimbursement objReimbursement = billDetails.getRodDocumentSummaryKey().getReimbursement();
		if(null != objReimbursement)
		{
			dto.setIntimationNo(objReimbursement.getClaim().getIntimation().getIntimationId());
			dto.setDateOfAdmission(SHAUtils.formatDate(objReimbursement.getDateOfAdmission()));
			dto.setDateOfDischarge(SHAUtils.formatDate(objReimbursement.getDateOfDischarge()));
			dto.setInsuredPatientName(objReimbursement.getClaim().getIntimation().getInsuredPatientName());
		}
		
		return dto;
	}
	
	private Map<String, Object> getValuesForNameDropDown(ReceiptOfDocumentsDTO rodDTO)
	{
		
		Map<String, Object> referenceDataMap = new HashMap<String, Object>();
		
		Policy policy = policyService.getPolicy(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyNumber());
		if(null != policy)
		{
		String proposerName =  policy.getProposerFirstName();
		List<Insured> insuredList = policy.getInsured();
		
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		List<SelectValue> payeeValueList = new ArrayList<SelectValue>();
		
		SelectValue selectValue= null;
		for (int i = 0; i < insuredList.size(); i++) {
			
			Insured insured = insuredList.get(i);
			selectValue = new SelectValue();
			//SelectValue payeeValue = new SelectValue();
			selectValue.setId(Long.valueOf(String.valueOf(i)));
			selectValue.setValue(insured.getInsuredName());
			
			//payeeValue.setId(Long.valueOf(String.valueOf(i)));
			//payeeValue.setValue(insured.getInsuredName());
			
			selectValueList.add(selectValue);
			//payeeValueList.add(payeeValue);
		}
		payeeValueList.addAll(selectValueList);
		
		for (int i = 0; i < insuredList.size(); i++) {
			Insured insured = insuredList.get(i);
			List<NomineeDetails> nomineeDetails = policyService.getNomineeDetails(insured.getKey());
			for (NomineeDetails nomineeDetails2 : nomineeDetails) {
				selectValue = new SelectValue();
				selectValue.setId(nomineeDetails2.getKey());
				selectValue.setValue(nomineeDetails2.getNomineeName());
				payeeValueList.add(selectValue);
				selectValue = null;
			}
			
		}
		
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValueList);
		
		
		SelectValue payeeSelValue = new SelectValue();
		int iSize = payeeValueList.size() +1;
		payeeSelValue.setId(Long.valueOf(String.valueOf(iSize)));
		payeeSelValue.setValue(proposerName);
		
		payeeValueList.add(payeeSelValue);
		
		if(rodDTO.getDocumentDetails().getHospitalPayableAt() != null){
			SelectValue hospitalName = new SelectValue();
			hospitalName.setId(Long.valueOf(payeeValueList.size()+1));
			hospitalName.setValue(rodDTO.getDocumentDetails().getHospitalPayableAt());
			payeeValueList.add(hospitalName);
		}
		else if(rodDTO.getDocumentDetails().getHospitalName() != null){
			SelectValue hospitalName = new SelectValue();
			hospitalName.setId(Long.valueOf(payeeValueList.size()+1));
			hospitalName.setValue(rodDTO.getDocumentDetails().getHospitalName());
			payeeValueList.add(hospitalName);
		}
		
		BeanItemContainer<SelectValue> insuredPatientNameValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		insuredPatientNameValueContainer.addAll(payeeValueList);
		
		BeanItemContainer<SelectValue> payeeNameValueContainer = dbCalculationService.getPayeeName(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getKey(),
				rodDTO.getClaimDTO().getNewIntimationDto().getKey());
		
		if(ReferenceTable.getGMCProductList().containsKey(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){	
			
			referenceDataMap.put("payeeNameList", payeeNameValueContainer);
		}
		else
		{
			referenceDataMap.put("payeeNameList", insuredPatientNameValueContainer);
		}
		}
		
		return referenceDataMap;
	}
	
	public void submitWizard(
			@Observes @CDIEvent(SUBMIT_DETAILS) final ParameterDTO parameters) {
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		rodService.submitAddAdditionalPaymentValues(rodDTO);
		view.buildSuccessLayout();
	}
	
	public void setupPayableDetails(
			@Observes @CDIEvent(SETUP_PAYMENT_INFO_DOCUMENTS_PAYABLE_DETAILS) final ParameterDTO parameters) {
		String viewSearchCriteriaDTO = (String) parameters.getPrimaryParameter();
		view.setUpPayableDetails(viewSearchCriteriaDTO);
	}
	
	public void setupIFSCDetails(
			@Observes @CDIEvent(SETUP_PAYMENT_INFO_DOCUMENTS_IFSC_DETAILS) final ParameterDTO parameters) {
		ViewSearchCriteriaTableDTO viewSearchCriteriaDTO = (ViewSearchCriteriaTableDTO) parameters.getPrimaryParameter();
		view.setUpIFSCDetails(viewSearchCriteriaDTO);
	}
	
	public void populatePreviousPaymentDetails(@Observes @CDIEvent(PAYMENT_INFO_POPULATE_PREVIOUS_ACCT_DETAILS) final ParameterDTO parameters)
	{
		PreviousAccountDetailsDTO tableDTO = (PreviousAccountDetailsDTO) parameters.getPrimaryParameter();
		view.populatePreviousPaymentDetails(tableDTO);
		
	}

}
