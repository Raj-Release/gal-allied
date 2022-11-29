/**
 * 
 */
package com.shaic.claim.rod.wizard.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.HospitalService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasAcknowledgementDoctor;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.Preauth;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.CheckBox;

/**
 * @author ntv.vijayar
 *
 */
@ViewInterface(DocumentDetailsView.class)
public class DocumentDetailsPresenter  extends AbstractMVPPresenter<DocumentDetailsView>
{

	public static final String SETUP_DROPDOWN_VALUES = "setup_dropdown_values";
	
	public static final String SETUP_DOCUMENT_CHECKLIST_TABLE_VALUES = "setup_doc_checklist_tbl_values";
	
	public static final String DISABLE_TABLE_VALUES = "disable_table_values";
	
	public static final String SET_DOC_REC_STATUS_FOR_QUERY_REPLY = "set_doc_rec_status_for_query_reply";
	public static final String RESET_DOC_REC_STATUS_FOR_QUERY_REPLY = "reset_doc_rec_status_for_query_reply";
	
//	public static final String VALIDATE_BILL_CLASSIFICATION_DETAILS = "validate_bill_classification_details";
	
	public static final String VALIDATE_HOSPITALIZATION_REPEAT = "validate_hospitalization_repeat";
	
	public static final String VALIDATE_LUMPSUM_AMOUNT_CLASSIFICATION = "validate_lumpsum_amount_classification";
	
	private final Logger log = LoggerFactory.getLogger(DocumentDetailsPresenter.class);
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService ackDocReceivedService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private CreateRODService rodService;

	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void setUpDropDownValues(
			@Observes @CDIEvent(SETUP_DROPDOWN_VALUES) final ParameterDTO parameters) {
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		Map<String, Object> referenceDataMap = new WeakHashMap<String, Object>();
		referenceDataMap.put("commonValues", masterService
				.getSelectValueContainer(ReferenceTable.COMMON_VALUES));
		if(rodDTO.getStrUserName() != null){
			MasAcknowledgementDoctor ackDoctor = ackDocReceivedService.getSearchUser(rodDTO.getStrUserName());
			if(ackDoctor != null){
				referenceDataMap.put("modeOfReceipt", masterService
						.getSelectValueContainerForEmail(ReferenceTable.ACK_DOC_MODE_OF_RECEIPT));
			} else {
				referenceDataMap.put("modeOfReceipt", masterService
						.getSelectValueContainer(ReferenceTable.ACK_DOC_MODE_OF_RECEIPT));
			}
		}
		referenceDataMap.put("hospmodeOfReceipt", masterService
				.getSelectValueContainer(ReferenceTable.ACK_DOC_MODE_OF_RECEIPT));
		referenceDataMap.put("docReceivedFrom", masterService
				.getSelectValueContainer(ReferenceTable.ACK_DOC_RECEIVED_FROM));
		referenceDataMap.put("receivedStatus", masterService
				.getSelectValueContainer(ReferenceTable.ACK_DOC_RECEIVED_STATUS));
		referenceDataMap.put("reasonForReconsiderationRequest", masterService
				.getSelectValueContainer(ReferenceTable.REASON_FOR_RECONSIDERATION));
		referenceDataMap.put("particulars", masterService.getDocumentCheckListValuesContainer(SHAConstants.MASTER_TYPE_REIMBURSEMENT));
		//.getDocumentCheckListValuesByType(SHAConstants.MASTER_TYPE_REIMBURSEMENT));
		if(rodDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
			
			BeanItemContainer<SelectValue> partToContainer = new BeanItemContainer<SelectValue>(
					SelectValue.class);
			BeanItemContainer<SelectValue> partToContainerCash = new BeanItemContainer<SelectValue>(
					SelectValue.class);
			 partToContainer = masterService.getDocumentCheckListValuesContainer(SHAConstants.MASTER_TYPE_REIMBURSEMENT);
			 List<SelectValue> values = partToContainer.getItemIds();
			 for (SelectValue selectValue : values) {
				 if(selectValue.getId() != null && !selectValue.getId().equals(40l)){
					 partToContainerCash.addBean(selectValue);
				 }
			}
			 referenceDataMap.put("particulars", partToContainerCash);	
		}
		
		referenceDataMap.put("billClaissificationDetails",validateBillClassification(rodDTO.getClaimDTO().getKey()));
		referenceDataMap.put("isFinalEnhancement",getCashlessClaimStatus(rodDTO.getClaimDTO().getKey()));
		
		view.setUpDropDownValues(referenceDataMap);
		
		}
	
	public void setUpDocumentCheckListValues(
			@Observes @CDIEvent(SETUP_DOCUMENT_CHECKLIST_TABLE_VALUES) final ParameterDTO parameters) {
		List<DocumentCheckListDTO> documentCheckListDTO = ackDocReceivedService.getDocumentCheckListValues(masterService);
		view.setDocumentCheckListTableValues(documentCheckListDTO);
		
		}
	
	
	public void setDocStatusIfReplyReceivedForQuery(
			@Observes @CDIEvent(SET_DOC_REC_STATUS_FOR_QUERY_REPLY) final ParameterDTO parameters) {
		//ReconsiderRODRequestTableDTO dto = (ReconsiderRODRequestTableDTO) parameters.getPrimaryParameter();
	//	List<DocumentCheckListDTO> documentCheckListDTO = ackDocReceivedService.getDocumentCheckListValues(masterService);
		//Long docAckKey = (Long) parameters.getPrimaryParameter();
		RODQueryDetailsDTO rodQueryDetails = (RODQueryDetailsDTO) parameters.getPrimaryParameter();
		DocAcknowledgement docAck = ackDocReceivedService.getDocAcknowledgment(rodQueryDetails.getAcknowledgementKey());
		if(docAck.getInsuredContactNumber() != null){
			rodQueryDetails.setAcknowledgementContactNumber(docAck.getInsuredContactNumber());
		}
		rodQueryDetails.setHospitalizationClaimedAmt(docAck.getHospitalizationClaimedAmount());
		rodQueryDetails.setPostHospitalizationClaimedAmt(docAck.getPostHospitalizationClaimedAmount());
		rodQueryDetails.setPreHospitalizationClaimedAmt(docAck.getPreHospitalizationClaimedAmount());
		rodQueryDetails.setOtherBenefitClaimedAmnt(docAck.getOtherBenefitsClaimedAmount());
		
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
		
		//added for hospital cash -- IMSSUPPOR-36652
		rodQueryDetails.setHospitalCashFlag(docAck.getProdHospBenefitFlag());	
		
		
		view.setDocStatusIfReplyReceivedForQuery(rodQueryDetails);
		}
	

	/**
	 * Added for removing hospitalization validation for pre and post hospitalization 
	 * classification values.
	 * */
	
	public Boolean getCashlessClaimStatus(Long claimKey)
	{
		Preauth preauth = preauthService.getLatestPreauthByClaim(claimKey);
		if(null != preauth && ("F").equalsIgnoreCase(preauth.getEnhancementType()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void resetDocStatusIfReplyReceivedForQuery(
			@Observes @CDIEvent(RESET_DOC_REC_STATUS_FOR_QUERY_REPLY) final ParameterDTO parameters) {
		//ReconsiderRODRequestTableDTO dto = (ReconsiderRODRequestTableDTO) parameters.getPrimaryParameter();
	//	List<DocumentCheckListDTO> documentCheckListDTO = ackDocReceivedService.getDocumentCheckListValues(masterService);
		//view.setDocStatusIfReplyReceivedForQuery();
		view.resetDocStatusIfReplyReceivedForQuery();
		
		}
	
	
	
	public void disableReconsiderRequestTableValue(
			@Observes @CDIEvent(DISABLE_TABLE_VALUES) final ParameterDTO parameters) {
		ReconsiderRODRequestTableDTO dto = (ReconsiderRODRequestTableDTO) parameters.getPrimaryParameter();
	//	List<DocumentCheckListDTO> documentCheckListDTO = ackDocReceivedService.getDocumentCheckListValues(masterService);
		view.disableReconsiderRequestTableItems(dto);
		
		}
	
	public void validateHospitalizationRepeat(
			@Observes @CDIEvent(VALIDATE_HOSPITALIZATION_REPEAT) final ParameterDTO parameters) 
	{
		Long claimKey = (Long) parameters.getPrimaryParameter();
		Boolean isValid = ackDocReceivedService.getStatusOfFirstRODForAckValidation(claimKey);
		view.validateHospitalizationRepeat(isValid);
		
	}
	
	
	public List<DocumentDetailsDTO> validateBillClassification(Long claimKey)
	{
		/*List<Reimbursement> reimbursementList = ackDocReceivedService.getReimbursementDetailsForBillClassificationValidation(claimKey);
		
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
				
				docDTOList.add(docDTO);
			}
		}*/
		
		//Added for isssue 1621. Now record is been taken from acknowlegement table. After discussing with sathis sir, can implement this code.
		List<DocAcknowledgement> docAcknowledgementList = ackDocReceivedService.getAckDetailsForBillClassificationValidation(claimKey);
		if(null != docAcknowledgementList && !docAcknowledgementList.isEmpty())
			log.info("Inside document details preseneter-- docacknowledgement list is non empty--");
		List<DocumentDetailsDTO> docDTOList = new ArrayList<DocumentDetailsDTO>();
		if(null != docAcknowledgementList && !docAcknowledgementList.isEmpty())
		{	
			DocumentDetailsDTO docDTO = null;
			for (DocAcknowledgement docAck : docAcknowledgementList) {
				//DocAcknowledgement docAck= reimbursement.getDocAcknowLedgement();
				docDTO = new DocumentDetailsDTO();
				log.info("Inside for loop----"+docAck.getHospitalisationFlag());
				docDTO.setHospitalizationFlag(docAck.getHospitalisationFlag());
				docDTO.setPartialHospitalizationFlag(docAck.getPartialHospitalisationFlag());
				docDTO.setPreHospitalizationFlag(docAck.getPreHospitalisationFlag());
				docDTO.setPostHospitalizationFlag(docAck.getPostHospitalisationFlag());
				docDTO.setLumpSumAmountFlag(docAck.getLumpsumAmountFlag());
				docDTO.setAddOnBenefitsPatientCareFlag(docAck.getPatientCareFlag());
				docDTO.setAddOnBenefitsHospitalCashFlag(docAck.getHospitalCashFlag());
				if(null != docAck.getStatus())
				docDTO.setStatusId(docAck.getStatus().getKey());
				docDTOList.add(docDTO);
				
			}
		}
		log.info("---->validationList----->"+docDTOList);
		return docDTOList;
		
		
		/*//Reimbursement reimbursement = ackDocReceivedService.getLatestReimbursementDetails(claimKey);
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
		
		return docDTO;*/
		
		
	}
	
	public void validateLumpsumAmountFlag(
			@Observes @CDIEvent(VALIDATE_LUMPSUM_AMOUNT_CLASSIFICATION) final ParameterDTO parameters) 
	{
		
		Long claimKey = (Long) parameters.getPrimaryParameter();
		String classificationType = (String) parameters.getSecondaryParameter(0, String.class);
		CheckBox chkBox = (CheckBox) parameters.getSecondaryParameter(1, CheckBox.class);
		Long intimationKey = (Long) parameters.getSecondaryParameter(2, Long.class);
		Boolean isValid = rodService.isLumpSumExistsForClaim(claimKey,intimationKey,classificationType);
		view.validateLumpSumAmount(isValid,classificationType,chkBox);
		
	}


}
