/**
 * 
 */
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
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
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
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.CheckBox;

/**
 * @author ntv.vijayar
 *
 */
@ViewInterface(BillEntryDocumentDetailsView.class)
public class BillEntryDocumentDetailsPresenter  extends AbstractMVPPresenter<BillEntryDocumentDetailsView>
{

	public static final String BILL_ENTRY_SETUP_DROPDOWN_VALUES = " bill_entry_create_rod_setup_dropdown_values";
	
	public static final String BILL_ENTRY_SETUP_DOCUMENT_CHECKLIST_TABLE_VALUES = "bill_entry_setup_doc_checklist_tbl_values";
	
	public static final String BILL_ENTRY_SELECT_RECONSIDER_TABLE_VALUES = "bill_entry_select_reconsider_table_values";
	
	public static final String BILL_ENTRY_VALIDATION_LIST_FOR_ROD_CREATION = "bill_entry_validation_list_for_rod_creation";
	
	public static final String BILL_ENTRY_SET_DOC_REC_STATUS_FOR_QUERY_REPLY = "bill_entry_doc_rec_status_for_query_reply";
	
	public static final String VALIDATE_BILL_REVIEW_WITH_BILL_CLASSIFICATION = "validate_bill_review_with_bill_classification";
	
	public static final String BILL_ENTRY_VALIDATE_LUMPSUM_AMOUNT_CLASSIFICATION = "bill_entry_validate_lumpsum_amount_classification";
	
	public static final String VALIDATE_HOSPITALIZATION_REPEAT = "bill_entry_validate_hospitalization_repeat";

	
	
	
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
		referenceDataMap.put("reasonForReconsiderationRequest", masterService
				.getSelectValueContainer(ReferenceTable.REASON_FOR_RECONSIDERATION));
		referenceDataMap.put("particulars", masterService.getDocumentCheckListValuesContainer(SHAConstants.MASTER_TYPE_REIMBURSEMENT));
		referenceDataMap.put("diagnosisHospitalCashContainer", masterService.getDiagnosisHospitalCash());
		referenceDataMap.put("hospitalCashDueTo", masterService.getSelectValueContainer(ReferenceTable.HOSPITAL_CASH_DUE_TO));
		//COMMENTED ON 21-MAY-2019 AS IT IS NOWHERE USED.(KOTAK SLOWNESS)
		//getValuesForNameDropDown(rodDTO);
		getDocumentDetailsDTOList(rodDTO.getClaimDTO().getKey());
		validateBillClassification(rodDTO.getClaimDTO().getKey());
		view.setUpDropDownValues(referenceDataMap);
		
		}
	
	public void validateBillClassification(Long claimKey)
	{
		/*Reimbursement reimbursement = ackDocReceivedService.getLatestReimbursementDetails(claimKey);
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
		}*/
		//List<Reimbursement> reimbursementList = ackDocReceivedService.getReimbursementDetailsForBillClassificationValidation(claimKey);
		List<Reimbursement> reimbursementList = ackDocReceivedService.getReimbursementDetailsForBillClassificationValidationWithoutCancelAck(claimKey);
		List<DocumentDetailsDTO> docDTOList = new ArrayList<DocumentDetailsDTO>();
		if(null != reimbursementList && !reimbursementList.isEmpty())
		{	DocumentDetailsDTO docDTO =null;
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
				if(null != reimbursement.getStatus())
				docDTO.setStatusId(reimbursement.getStatus().getKey());
				docDTOList.add(docDTO);
			}
		}
		//return docDTOList;
		referenceDataMap.put("billClaissificationDetails",docDTOList);	
		
		//return docDTO;
		//referenceDataMap.put("billClaissificationDetails",docDTO);		
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
	
	//COMMENTED ON 21-MAY-2019 AS IT IS NOWHERE USED.(KOTAK SLOWNESS)
	/*private void getValuesForNameDropDown(ReceiptOfDocumentsDTO rodDTO)
	{
		Policy policy = policyService.getPolicy(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyNumber());
		if(null != policy)
		{
		String proposerName =  policy.getProposerFirstName();
		List<Insured> insuredList = policy.getInsured();
		
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		List<SelectValue> payeeValueList = new ArrayList<SelectValue>();
		SelectValue selectValue = null;
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
		
	}*/
	
	
	public void saveReconsiderRequestTableValue(
			@Observes @CDIEvent(BILL_ENTRY_SELECT_RECONSIDER_TABLE_VALUES) final ParameterDTO parameters) {
		ReconsiderRODRequestTableDTO dto = (ReconsiderRODRequestTableDTO) parameters.getPrimaryParameter();
		//List<UploadDocumentDTO> uploadDocsDTO = rodService.getRODSummaryDetails(dto.getRodKey());
		List<UploadDocumentDTO> uploadDocsDTO = rodService.getRODSummaryDetailsForReconsideration(dto.getRodKey());
		
	//	List<DocumentCheckListDTO> documentCheckListDTO = ackDocReceivedService.getDocumentCheckListValues(masterService);
		view.saveReconsiderRequestTableItems(dto,uploadDocsDTO);
		
		}
	
	public void getValidationListForRodCreation(
			@Observes @CDIEvent(BILL_ENTRY_VALIDATION_LIST_FOR_ROD_CREATION) final ParameterDTO parameters) {
		Long claimKey = (Long)parameters.getPrimaryParameter();
		List<DocumentDetailsDTO> documentDetailsDTO = rodService.getDocumentDetailsDTO(claimKey);
		
		view.setDocumentDetailsDTOForValidation(documentDetailsDTO);
	}
	
	
	public void validateBillReviewWithBillClassification(
			@Observes @CDIEvent(VALIDATE_BILL_REVIEW_WITH_BILL_CLASSIFICATION) final ParameterDTO parameters) {
		
		Long rodKey = (Long)parameters.getPrimaryParameter();
		Long categoryKey = (Long)parameters.getSecondaryParameter(0, Long.class);
		String chkBox = (String)parameters.getSecondaryParameter(1,String.class);
		List<Long> categoryList = rodService.getListOfBillCategory(rodKey, categoryKey);
		view.validateBillClassificationAgainstBillEntry(categoryList,categoryKey,chkBox);
		
	}
	
	public void setDocStatusIfReplyReceivedForQuery(
			@Observes @CDIEvent(BILL_ENTRY_SET_DOC_REC_STATUS_FOR_QUERY_REPLY) final ParameterDTO parameters) {
		//ReconsiderRODRequestTableDTO dto = (ReconsiderRODRequestTableDTO) parameters.getPrimaryParameter();
	//	List<DocumentCheckListDTO> documentCheckListDTO = ackDocReceivedService.getDocumentCheckListValues(masterService);
		//Long docAckKey = (Long) parameters.getPrimaryParameter();
		RODQueryDetailsDTO rodQueryDetails = (RODQueryDetailsDTO) parameters.getPrimaryParameter();
		DocAcknowledgement docAck = ackDocReceivedService.getDocAcknowledgment(rodQueryDetails.getAcknowledgementKey());
		rodQueryDetails.setHospitalizationClaimedAmt(docAck.getHospitalizationClaimedAmount());
		rodQueryDetails.setPostHospitalizationClaimedAmt(docAck.getPostHospitalizationClaimedAmount());
		rodQueryDetails.setPreHospitalizationClaimedAmt(docAck.getPreHospitalizationClaimedAmount());
		
		rodQueryDetails.setHospitalizationFlag(docAck.getHospitalisationFlag());
		rodQueryDetails.setPreHospitalizationFlag(docAck.getPreHospitalisationFlag());
		rodQueryDetails.setPostHospitalizationFlag(docAck.getPostHospitalisationFlag());
		rodQueryDetails.setPartialHospitalizationFlag(docAck.getPartialHospitalisationFlag());
		rodQueryDetails.setAddOnBenefitsLumpsumFlag(docAck.getLumpsumAmountFlag());
		rodQueryDetails.setAddOnBenefitsPatientCareFlag(docAck.getPatientCareFlag());
		rodQueryDetails.setAddOnBeneftisHospitalCashFlag(docAck.getHospitalCashFlag());
		rodQueryDetails.setHospitalizationRepeatFlag(docAck.getHospitalizationRepeatFlag());
		rodQueryDetails.setOtherBenefitsFlag(docAck.getOtherBenefitsFlag());
		rodQueryDetails.setEmergencyMedicalEvaluationFlag(docAck.getEmergencyMedicalEvaluation());
		rodQueryDetails.setCompassionateTravelFlag(docAck.getCompassionateTravel());
		rodQueryDetails.setRepatriationOfMortalRemainsFlag(docAck.getRepatriationOfMortalRemain());
		rodQueryDetails.setPreferredNetworkHospitalFlag(docAck.getPreferredNetworkHospita());
		rodQueryDetails.setSharedAccomodationFlag(docAck.getSharedAccomodation());		
		
		
		view.setDocStatusIfReplyReceivedForQuery(rodQueryDetails);
		}
	
	
	public void validateLumpsumAmountFlag(
			@Observes @CDIEvent(BILL_ENTRY_VALIDATE_LUMPSUM_AMOUNT_CLASSIFICATION) final ParameterDTO parameters) 
	{
		
		Long claimKey = (Long) parameters.getPrimaryParameter();
		String classificationType = (String) parameters.getSecondaryParameter(0, String.class);
		CheckBox chkBox = (CheckBox) parameters.getSecondaryParameter(1, CheckBox.class);
		Long rodKey = (Long) parameters.getSecondaryParameter(2, Long.class);
		Boolean isValid = rodService.isLumpsumExists(claimKey,rodKey,classificationType);
		view.validateLumpSumAmount(isValid,classificationType,chkBox);
		
	}
	
	public void validateHospitalizationRepeat(
			@Observes @CDIEvent(VALIDATE_HOSPITALIZATION_REPEAT) final ParameterDTO parameters) 
	{
		Long claimKey = (Long) parameters.getPrimaryParameter();
		Boolean isValid = ackDocReceivedService.getStatusOfFirstRODForAckValidation(claimKey);
		view.validateHospitalizationRepeat(isValid);
		
	}
	

}


