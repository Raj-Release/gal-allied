package com.shaic.paclaim.addAdditinalDocument.search;

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
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Insured;
import com.shaic.domain.MasterService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.RODBillDetails;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(PAAddadditionalDocumentDetailsView.class)
public class PAAddAdditionalDocumentDetailsPresenter extends AbstractMVPPresenter<PAAddadditionalDocumentDetailsView>{

	
	private static final long serialVersionUID = 1L;

	public static final String BILL_ENTRY_SETUP_DROPDOWN_VALUES = "Add Documents bill_entry_create_rod_setup_dropdown_values_PA";
	
	public static final String BILL_ENTRY_SETUP_DOCUMENT_CHECKLIST_TABLE_VALUES = "Add Documents bill_entry_setup_doc_checklist_tbl_values_PA";
	
	public static final String BILL_ENTRY_SELECT_RECONSIDER_TABLE_VALUES = "Add Documents bill_entry_select_reconsider_table_values_PA";
	
	public static final String BILL_ENTRY_VALIDATION_LIST_FOR_ROD_CREATION = "Add Documents bill_entry_validation_list_for_rod_creation_PA";
	
	public static final String UPDATE_BEAN = "UPDATE BEAN_PA";
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService ackDocReceivedService;
	
	@EJB
	private CreateRODService rodService;
	
	Map<String, Object> referenceDataMap = new HashMap<String, Object>();
	
	@EJB
	private PolicyService policyService;

	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void setUpDropDownValues(
			@Observes @CDIEvent(BILL_ENTRY_SETUP_DROPDOWN_VALUES) final ParameterDTO parameters) {
		
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		
		
		referenceDataMap.put("commonValues", masterService
				.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		referenceDataMap.put("modeOfReceipt", masterService
				.getSelectValueContainer(ReferenceTable.ACK_DOC_MODE_OF_RECEIPT));
		referenceDataMap.put("docReceivedFrom", masterService
				.getSelectValueContainer(ReferenceTable.ACK_DOC_RECEIVED_FROM));
		referenceDataMap.put("receivedStatus", masterService
				.getSelectValueContainer(ReferenceTable.ACK_DOC_RECEIVED_STATUS));
		referenceDataMap.put("particulars", masterService.getDocumentCheckListValuesContainer(SHAConstants.MASTER_TYPE_PA));
		getValuesForNameDropDown(rodDTO);
		getDocumentDetailsDTOList(rodDTO.getClaimDTO().getKey());
		validateBillClassification(rodDTO.getClaimDTO().getKey());
		view.setUpDropDownValues(referenceDataMap);
		
		}
	
	public void validateBillClassification(Long claimKey)
	{
		Reimbursement reimbursement = ackDocReceivedService.getLatestReimbursementDetails(claimKey);
		DocumentDetailsDTO docDTO = null;
		if(null != reimbursement)
		{
			DocAcknowledgement docAck= reimbursement.getDocAcknowLedgement();
			docDTO = new DocumentDetailsDTO();
			docDTO.setHospitalizationFlag(docAck.getHospitalisationFlag());
			docDTO.setPartialHospitalizationFlag(docAck.getPartialHospitalisationFlag());
			docDTO.setPreHospitalizationFlag(docAck.getPreHospitalisationFlag());
			docDTO.setPostHospitalizationFlag(docAck.getPostHospitalisationFlag());
			docDTO.setLumpSumAmountFlag(docAck.getLumpsumAmountFlag());
			docDTO.setAddOnBenefitsPatientCareFlag(docAck.getPatientCareFlag());
			docDTO.setAddOnBenefitsHospitalCashFlag(docAck.getHospitalCashFlag());
		}
		//return docDTO;
		referenceDataMap.put("billClaissificationDetails",docDTO);		
	}
	
	public void updateBeanDetails(
			@Observes @CDIEvent(UPDATE_BEAN) final ParameterDTO parameters) {
		ReceiptOfDocumentsDTO bean = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		List<UploadDocumentDTO> rodSummaryDetails = rodService
				.getRODSummaryDetails(bean.getDocumentDetails().getRodKey());
		
		if(null != rodSummaryDetails && !rodSummaryDetails.isEmpty())
		{
			List<BillEntryDetailsDTO> dtoList = new ArrayList<BillEntryDetailsDTO>();
		//	List<UploadDocumentDTO> uploadDocsList = rodDTO.getUploadDocsList();
			for (UploadDocumentDTO uploadDocDTO : rodSummaryDetails) {
				//sss
				uploadDocDTO.setIsBillSaved(true);
				
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
		view.updateBean(bean);
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
	
	private void getDocumentDetailsDTOList(Long claimKey)
	{
		List<DocumentDetailsDTO> validationList = rodService.getDocumentDetailsDTO(claimKey);
		/*List<DocumentDetailsDTO> validationList = null;
		if(null != documentDetailsDTO && !documentDetailsDTO.isEmpty())
		{
			validationList = new ArrayList<DocumentDetailsDTO>();
			for (DocumentDetailsDTO documentDetailsDTO2 : documentDetailsDTO) {
				if(null != documentDetailsDTO2)
				{
					validationList.add(documentDetailsDTO2);
				}
			}
		}*/
		referenceDataMap.put("validationDocList", validationList);
	}
	
	
	private void getValuesForNameDropDown(ReceiptOfDocumentsDTO rodDTO)
	{
		Policy policy = policyService.getPolicy(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyNumber());
		if(null != policy)
		{
		String proposerName =  policy.getProposerFirstName();
		List<Insured> insuredList = policy.getInsured();
		
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		List<SelectValue> payeeValueList = new ArrayList<SelectValue>();
		for (int i = 0; i < insuredList.size(); i++) {
			
			Insured insured = insuredList.get(i);
			SelectValue selectValue = new SelectValue();
			SelectValue payeeValue = new SelectValue();
			selectValue.setId(Long.valueOf(String.valueOf(i)));
			selectValue.setValue(insured.getInsuredName());
			
			payeeValue.setId(Long.valueOf(String.valueOf(i)));
			payeeValue.setValue(insured.getInsuredName());
			
			selectValueList.add(selectValue);
			payeeValueList.add(payeeValue);
		}
		
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValueList);
		
		
		SelectValue payeeSelValue = new SelectValue();
		payeeSelValue.setId(Long.valueOf(String.valueOf(selectValueList.size() -1)));
		payeeSelValue.setValue(proposerName);
		payeeValueList.add(payeeSelValue);
		
		BeanItemContainer<SelectValue> payeeNameValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		payeeNameValueContainer.addAll(payeeValueList);
		
		referenceDataMap.put("payeeNameList", selectValueContainer);
		referenceDataMap.put("insuredPatientList" , payeeNameValueContainer);
		}
		
	}
	
	
	public void saveReconsiderRequestTableValue(
			@Observes @CDIEvent(BILL_ENTRY_SELECT_RECONSIDER_TABLE_VALUES) final ParameterDTO parameters) {
		ReconsiderRODRequestTableDTO dto = (ReconsiderRODRequestTableDTO) parameters.getPrimaryParameter();
		List<UploadDocumentDTO> uploadDocsDTO = rodService.getRODSummaryDetails(dto.getRodKey());
		
	//	List<DocumentCheckListDTO> documentCheckListDTO = ackDocReceivedService.getDocumentCheckListValues(masterService);
		view.saveReconsiderRequestTableItems(dto,uploadDocsDTO);
		
		}
	
	public void getValidationListForRodCreation(
			@Observes @CDIEvent(BILL_ENTRY_VALIDATION_LIST_FOR_ROD_CREATION) final ParameterDTO parameters) {
		Long claimKey = (Long)parameters.getPrimaryParameter();
		List<DocumentDetailsDTO> documentDetailsDTO = rodService.getDocumentDetailsDTO(claimKey);
		
		view.setDocumentDetailsDTOForValidation(documentDetailsDTO);
	}
	
}
