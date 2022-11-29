package com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.HospitalService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.Preauth;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(PAAcknowledgementDocumentDetailsView.class)
public class PAAcknowledgementDocumentDetailsPresenter extends AbstractMVPPresenter<PAAcknowledgementDocumentDetailsView>{

public static final String SETUP_DROPDOWN_VALUES = "setup_dropdown_values_PA";


	public static final String SET_DEFAULT_PARTICULAR_VALUES = "default values for particular for pa ack";
	
	public static final String SETUP_DOCUMENT_CHECKLIST_TABLE_VALUES = "setup_doc_checklist_tbl_values_PA";
	
	public static final String DISABLE_TABLE_VALUES = "disable_table_values_PA";
	
	public static final String SET_DOC_REC_STATUS_FOR_QUERY_REPLY = "set_doc_rec_status_for_query_reply_PA";
	public static final String RESET_DOC_REC_STATUS_FOR_QUERY_REPLY = "reset_doc_rec_status_for_query_reply_PA";
	
//	public static final String VALIDATE_BILL_CLASSIFICATION_DETAILS = "validate_bill_classification_details";
	
	public static final String VALIDATE_HOSPITALIZATION_REPEAT = "validate_hospitalization_repeat_PA";
	
	public static final String VALIDATE_BENEFIT_REPEAT = "validate_benefit_repeat";
	
	public static final String VALIDATE_ADD_ON_COVERS_REPEAT = "validate_covers_repeate";
	
	public static final String VALIDATE_OPTIONAL_COVERS_REPEAT = "validate_optional_cover";
	
	public static final String RESET_RECONSIDERATION_TABLE_VALUES = "reset_reconsideration_table_values_PA";
	
	public static final String RESET_PARTICULARS_VALUES = "reset particulars values";
	
	public static final String RESET_PARTICULARS_VALUES_FOR_ADD_ON_COVER = "reset particular value for add on cover";
	
	private final Logger log = LoggerFactory.getLogger(PAAcknowledgementDocumentDetailsPresenter.class);
	
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

	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void setUpDropDownValues(
			@Observes @CDIEvent(SETUP_DROPDOWN_VALUES) final ParameterDTO parameters) {
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		Map<String, Object> referenceDataMap = new HashMap<String, Object>();
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
		referenceDataMap.put("particulars", masterService.getDocumentCheckListValuesContainer(SHAConstants.MASTER_TYPE_PA));
		//.getDocumentCheckListValuesByType(SHAConstants.MASTER_TYPE_REIMBURSEMENT));
		referenceDataMap.put("documentType", masterService
				.getSelectValueContainer(ReferenceTable.PA_DOCUMENT_TYPE));
		referenceDataMap.put(SHAConstants.DEATH_FLAGS, masterService.getRevisedParticularsByBenefit(SHAConstants.MASTER_TYPE_PA, SHAConstants.DEATH_FLAGS));
		referenceDataMap.put(SHAConstants.PPD, masterService.getRevisedParticularsByBenefit(SHAConstants.MASTER_TYPE_PA, SHAConstants.PPD));
		referenceDataMap.put(SHAConstants.PTD, masterService.getRevisedParticularsByBenefit(SHAConstants.MASTER_TYPE_PA, SHAConstants.PTD));
		referenceDataMap.put(SHAConstants.TTD, masterService.getRevisedParticularsByBenefit(SHAConstants.MASTER_TYPE_PA, SHAConstants.TTD));
		referenceDataMap.put(SHAConstants.HOSPITALIZATION,masterService.getRevisedParticularsByBenefit(SHAConstants.MASTER_TYPE_PA, SHAConstants.HOSPITALIZATION));
		
		BeanItemContainer<SelectValue> additionalCovers = rodDTO.getDocumentDetails().getAdditionalCovers();
		if(additionalCovers != null && additionalCovers.getItemIds() != null && ! additionalCovers.getItemIds().isEmpty()){
			List<SelectValue> itemIds = additionalCovers.getItemIds();
			for (SelectValue selectValue : itemIds) {
				 BeanItemContainer<SpecialSelectValue> revisedParticularsByBenefit = masterService.getRevisedParticularsByBenefit(SHAConstants.MASTER_TYPE_PA, selectValue.getValue());
				 List<SpecialSelectValue> itemIds2 = revisedParticularsByBenefit.getItemIds();
				 for (SpecialSelectValue specialSelectValue : itemIds2) {
					 specialSelectValue.setCommonValue("Y");
				}
				referenceDataMap.put(selectValue.getValue(),revisedParticularsByBenefit);
			}
		}
		
		BeanItemContainer<SelectValue> optionalCover = rodDTO.getDocumentDetails().getOptionalCovers();
		if(optionalCover != null && optionalCover.getItemIds() != null && ! optionalCover.getItemIds().isEmpty()){
			List<SelectValue> itemIds = optionalCover.getItemIds();
			for (SelectValue selectValue : itemIds) {
				 BeanItemContainer<SpecialSelectValue> revisedParticularsByBenefit = masterService.getRevisedParticularsByBenefit(SHAConstants.MASTER_TYPE_PA, selectValue.getValue());
				 List<SpecialSelectValue> itemIds2 = revisedParticularsByBenefit.getItemIds();
				 for (SpecialSelectValue specialSelectValue : itemIds2) {
					 specialSelectValue.setCommonValue("Y");
				}
				referenceDataMap.put(selectValue.getValue(), revisedParticularsByBenefit);
			}
		}
		
		
		referenceDataMap.put("billClaissificationDetails",validateBillClassification(rodDTO.getClaimDTO().getKey()));
		referenceDataMap.put("isFinalEnhancement",getCashlessClaimStatus(rodDTO.getClaimDTO().getKey()));
		
		view.setUpDropDownValues(referenceDataMap);
		
		}
	
	public void defaultParticularValues(
			@Observes @CDIEvent(SET_DEFAULT_PARTICULAR_VALUES) final ParameterDTO parameters) {
		
		BeanItemContainer<SelectValue> documentCheckListValuesContainer = masterService.getDocumentCheckListValuesContainer(SHAConstants.MASTER_TYPE_PA);
		
		view.setDefaultParticularValues(documentCheckListValuesContainer);
		
	}
	
	
	public void setUpDocumentCheckListValues(
			@Observes @CDIEvent(SETUP_DOCUMENT_CHECKLIST_TABLE_VALUES) final ParameterDTO parameters) {
		List<DocumentCheckListDTO> documentCheckListDTO = ackDocReceivedService.getPADocumentCheckListValues(masterService);		
		List<DocumentCheckListDTO> revisedDocumentCheckListDTO = new ArrayList<DocumentCheckListDTO>();
	/*	
		if(null != documentCheckListDTO && !documentCheckListDTO.isEmpty())
		{
		for (int i = 0;i<6;i++) {
			
			revisedDocumentCheckListDTO.add(documentCheckListDTO.get(i));
		}
		}*/
		
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
		
		rodQueryDetails.setHospitalizationFlag(docAck.getHospitalisationFlag());
		rodQueryDetails.setPreHospitalizationFlag(docAck.getPreHospitalisationFlag());
		rodQueryDetails.setPostHospitalizationFlag(docAck.getPostHospitalisationFlag());
		rodQueryDetails.setPartialHospitalizationFlag(docAck.getPartialHospitalisationFlag());
		rodQueryDetails.setAddOnBenefitsLumpsumFlag(docAck.getLumpsumAmountFlag());
		rodQueryDetails.setAddOnBenefitsPatientCareFlag(docAck.getPatientCareFlag());
		rodQueryDetails.setAddOnBeneftisHospitalCashFlag(docAck.getHospitalCashFlag());
		rodQueryDetails.setHospitalizationRepeatFlag(docAck.getHospitalizationRepeatFlag());
	
		
		
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
	
	public void resetReconsiderRequestTableValue(
			@Observes @CDIEvent(RESET_RECONSIDERATION_TABLE_VALUES) final ParameterDTO parameters) {
		ReconsiderRODRequestTableDTO dto = (ReconsiderRODRequestTableDTO) parameters.getPrimaryParameter();
	//	List<DocumentCheckListDTO> documentCheckListDTO = ackDocReceivedService.getDocumentCheckListValues(masterService);
		view.resetReconsiderRequestTableItems(dto);
		
		}

	
	public void validateHospitalizationRepeat(
			@Observes @CDIEvent(VALIDATE_HOSPITALIZATION_REPEAT) final ParameterDTO parameters) 
	{
		Long claimKey = (Long) parameters.getPrimaryParameter();
		Boolean isValid = ackDocReceivedService.getStatusOfFirstRODForAckValidation(claimKey);
		view.validateHospitalizationRepeat(isValid);
		
	}
	
	public void validateBenefitRepeat(
			@Observes @CDIEvent(VALIDATE_BENEFIT_REPEAT) final ParameterDTO parameters) 
	{
		Long claimKey = (Long) parameters.getPrimaryParameter();
		String value = (String) parameters.getSecondaryParameters()[0];
		Boolean chkBoxValue = (Boolean) parameters.getSecondaryParameters()[1];
		Boolean isValid = ackDocReceivedService.getAlreadySelectedBenefit(claimKey,value);
		view.validateBenefitRepeat(isValid,chkBoxValue,value);
		
	}
	
	public void validateResetForParticulars(
			@Observes @CDIEvent(RESET_PARTICULARS_VALUES) final ParameterDTO parameters) 
	{
		String value = (String) parameters.getPrimaryParameter();		
//		Boolean isValid = ackDocReceivedService.getAlreadySelectedBenefit(claimKey,value);
		//BeanItemContainer<SelectValue> particularsByBenefit = masterService.getParticularsByBenefit(SHAConstants.MASTER_TYPE_PA, value);
		view.resetParticularsBasedOnBenefit(null,value);
		
	}
	
	public void validateResetForParticularsForAddonCover(
			@Observes @CDIEvent(RESET_PARTICULARS_VALUES_FOR_ADD_ON_COVER) final ParameterDTO parameters) 
	{
		SelectValue value = (SelectValue) parameters.getPrimaryParameter();	
		Boolean deleteValue = (Boolean) parameters.getSecondaryParameters()[0];
//		Boolean isValid = ackDocReceivedService.getAlreadySelectedBenefit(claimKey,value);
		view.resetParticularsBasedOnAddOnCover(value,deleteValue);
		
	}
	
	
	
	public void validateRepeat(
			@Observes @CDIEvent(VALIDATE_ADD_ON_COVERS_REPEAT) final ParameterDTO parameters) 
	{
		Long claimKey = (Long) parameters.getPrimaryParameter();	
		List<AddOnCoversTableDTO> coversList =  (List<AddOnCoversTableDTO>) parameters.getSecondaryParameters()[0];
		//Long ackKey = (Long) parameters.getSecondaryParameters()[1];
		DocAcknowledgement docDetails = ackDocReceivedService.getDocAcknowledgmentByClaimKey(claimKey);	
		String isValidCoverName = null;
		if(null != docDetails && ! ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS.equals(docDetails.getStatus().getKey()))
		{		
			isValidCoverName = ackDocReceivedService.getAlreadySelectedAdditionalCovers(claimKey ,coversList);
		}
	
		Boolean isValid = false;
		
		if(null != isValidCoverName){
			isValid = true;
		}
		view.validateCoversRepeat(isValid,isValidCoverName);
		
		
		
		
	}
	
	public void validateOptionalCoverRepeat(
			@Observes @CDIEvent(VALIDATE_OPTIONAL_COVERS_REPEAT) final ParameterDTO parameters) 
	{
		Long claimKey = (Long) parameters.getPrimaryParameter();	
		List<AddOnCoversTableDTO> coversList =  (List<AddOnCoversTableDTO>) parameters.getSecondaryParameters()[0];		
		DocAcknowledgement docDetails = ackDocReceivedService.getDocAcknowledgmentByClaimKey(claimKey);	
		String isValidCoverName = null;
		if(null != docDetails && ! ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS.equals(docDetails.getStatus().getKey()))
		{		
			isValidCoverName = ackDocReceivedService.getAlreadySelectedOptionalCovers(claimKey ,coversList);
		 
		}
	
		Boolean isValid = false;
		
		if(null != isValidCoverName){
			isValid = true;
		}
		
		
		view.validateCoversRepeat(isValid,isValidCoverName);
		
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
			for (DocAcknowledgement docAck : docAcknowledgementList) {
				//DocAcknowledgement docAck= reimbursement.getDocAcknowLedgement();
				DocumentDetailsDTO docDTO = new DocumentDetailsDTO();
				log.info("Inside for loop----"+docAck.getHospitalisationFlag());
				docDTO.setHospitalizationFlag(docAck.getHospitalisationFlag());
				docDTO.setPartialHospitalizationFlag(docAck.getPartialHospitalisationFlag());
				docDTO.setPreHospitalizationFlag(docAck.getPreHospitalisationFlag());
				docDTO.setPostHospitalizationFlag(docAck.getPostHospitalisationFlag());
				docDTO.setLumpSumAmountFlag(docAck.getLumpsumAmountFlag());
				docDTO.setAddOnBenefitsPatientCareFlag(docAck.getPatientCareFlag());
				docDTO.setAddOnBenefitsHospitalCashFlag(docAck.getHospitalCashFlag());
				docDTO.setBenifitFlag(docAck.getBenifitFlag());
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


}
