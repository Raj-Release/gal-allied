package com.shaic.paclaim.rod.createrod.search;

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
import com.shaic.claim.policy.updateHospital.ui.UpdateHospitalService;
import com.shaic.claim.registration.updateHospitalDetails.UpdateHospitalDetailsDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.forms.ChangeHospitalMapper;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.BankMaster;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.NomineeDetails;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyNominee;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.UpdateHospital;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.AddOnCoversTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(PACreateRODDocumentDetailsView.class)
public class PACreateRODDocumentDetailsPresenter  extends AbstractMVPPresenter<PACreateRODDocumentDetailsView> {

	
public static final String CREATE_ROD_SETUP_DROPDOWN_VALUES = "create_rod_setup_dropdown_values_PA";
	
	public static final String SETUP_DOCUMENT_CHECKLIST_TABLE_VALUES = "setup_doc_checklist_tbl_values_PA";
	
	public static final String SELECT_RECONSIDER_TABLE_VALUES = "select_reconsider_table_values_PA";
	
	public static final String HOSPITAL_DETAILS = "change Hospital Details_PA";
	public static final String VALIDATION_LIST_FOR_ROD_CREATION = "validation_list_for_rod_creation_PA";
	
	public static final String HOSPITAL_PAYMENT_DETAILS = "hospital_payment_details_PA";
	
	public static final String SETUP_IFSC_DETAILS  = "setup_ifsc_details_PA";
	
	
	public static final String ROD_SET_DOC_REC_STATUS_FOR_QUERY_REPLY = "rod_set_doc_rec_status_for_query_reply_PA";
	
	public static final String COMPARE_WITH_PREVIOUS_ROD = "create_rod_compare_with_previous_rod_PA";
	
	public static final String CREATE_ROD_POPULATE_PREVIOUS_ACCT_DETAILS = "create_rod_populate_previous_acct_details_PA";
	
	public static final String VALIDATE_BENEFIT_REPEAT = "validate_benefit_repeat_ROD";
	
	public static final String VALIDATE_ADD_ON_COVERS_REPEAT = "ROd_validate_covers_repeate";
	
	public static final String VALIDATE_OPTIONAL_COVERS_REPEAT = "Rod_validate_optional_cover";
	
	//public static final String VALIDATE_BENEFIT_REPEAT_BILL_ENTRY = "Valiate_benefit_repeate_bill_entry";
	
	public static final String RESET_PARTICULARS_VALUES = "reset particulars values_ROD";
	
	public static final String RESET_PARTICULARS_VALUES_FOR_ADD_ON_COVER = "reset particular value for add on cover in create ROD";

	public static final String SETUP_NOMINEE_IFSC_DETAILS_PA_CROD = "Setup Nominee IFSC Details PA Create ROD";
	
	public static final String ADD_BANK_IFSC_DETAILS_PA_CROD = "setup Add Bank IFSC Details PA CROD";
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService ackDocReceivedService;
	
	@EJB
	private UpdateHospitalService updateHospitalService;
	
	@EJB
	private CreateRODService rodService;
	
	Map<String, Object> referenceDataMap = new HashMap<String, Object>();
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private HospitalService hospitalService;

	@EJB
	private CreateRODService createRodService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void setUpDropDownValues(
			@Observes @CDIEvent(CREATE_ROD_SETUP_DROPDOWN_VALUES) final ParameterDTO parameters) {
		
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
		referenceDataMap.put("particulars", masterService.getDocumentCheckListValuesContainer(SHAConstants.MASTER_TYPE_PA));
		referenceDataMap.put("documentType", masterService
				.getSelectValueContainer(ReferenceTable.PA_DOCUMENT_TYPE));
		referenceDataMap.put(SHAConstants.DEATH_FLAGS, masterService.getRevisedParticularsByBenefit(SHAConstants.MASTER_TYPE_PA, SHAConstants.DEATH_FLAGS));
		referenceDataMap.put(SHAConstants.PPD, masterService.getRevisedParticularsByBenefit(SHAConstants.MASTER_TYPE_PA, SHAConstants.PPD));
		referenceDataMap.put(SHAConstants.PTD, masterService.getRevisedParticularsByBenefit(SHAConstants.MASTER_TYPE_PA, SHAConstants.PTD));
		referenceDataMap.put(SHAConstants.TTD, masterService.getRevisedParticularsByBenefit(SHAConstants.MASTER_TYPE_PA, SHAConstants.TTD));
		
		BeanItemContainer<SelectValue> additionalCovers = rodDTO.getDocumentDetails().getAdditionalCovers();
		if(additionalCovers != null && additionalCovers.getItemIds() != null && ! additionalCovers.getItemIds().isEmpty()){
			List<SelectValue> itemIds = additionalCovers.getItemIds();
			for (SelectValue selectValue : itemIds) {
				referenceDataMap.put(selectValue.getValue(), masterService.getRevisedParticularsByBenefit(SHAConstants.MASTER_TYPE_PA, selectValue.getValue()));
			}
		}
		
		BeanItemContainer<SelectValue> optionalCover = rodDTO.getDocumentDetails().getOptionalCovers();
		if(optionalCover != null && optionalCover.getItemIds() != null && ! optionalCover.getItemIds().isEmpty()){
			List<SelectValue> itemIds = optionalCover.getItemIds();
			for (SelectValue selectValue : itemIds) {
				referenceDataMap.put(selectValue.getValue(), masterService.getRevisedParticularsByBenefit(SHAConstants.MASTER_TYPE_PA, selectValue.getValue()));
			}
		}
		
		getValuesForNameDropDown(rodDTO);
		getDocumentDetailsDTOList(rodDTO.getClaimDTO().getKey());
		validateBillClassification(rodDTO.getClaimDTO().getKey());
		referenceDataMap.put("isFinalEnhancement",getCashlessClaimStatus(rodDTO.getClaimDTO().getKey()));
	
		view.setUpDropDownValues(referenceDataMap);
		
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

	public void populatePaymentDetails(
			@Observes @CDIEvent(HOSPITAL_PAYMENT_DETAILS) final ParameterDTO parameters) {
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		
		if(null != rodDTO.getClaimDTO() &&  (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(rodDTO.getClaimDTO().getClaimTypeValue()))
		{
			populatePaymentDetailsForCashLessClaim(rodDTO.getClaimDTO().getNewIntimationDto().getHospitalDto().getKey(),rodDTO);
		}
		else
		{
			populatePaymentDetailsForReimbursementClaim(rodDTO.getClaimDTO().getKey(), rodDTO , rodDTO.getClaimDTO().getNewIntimationDto().getHospitalDto().getKey());
			
		}
		view.setUpPaymentDetails(rodDTO);
	}
	
	private void populatePaymentDetailsForReimbursementClaim(Long claimKey,ReceiptOfDocumentsDTO rodDTO,Long HospitalKey)
	{
		DocumentDetailsDTO docDetailsDTO = createRodService.getPreviousRODDetailsForClaim(claimKey,rodDTO.getDocumentDetails());
		if(null != docDetailsDTO )
		{
			rodDTO.getDocumentDetails().setAccountNo(docDetailsDTO.getAccountNo());
			rodDTO.getDocumentDetails().setPayableAt(docDetailsDTO.getPayableAt());
			rodDTO.getDocumentDetails().setEmailId(docDetailsDTO.getEmailId());
			rodDTO.getDocumentDetails().setPanNo(docDetailsDTO.getPanNo());
			rodDTO.getDocumentDetails().setPayeeName(docDetailsDTO.getPayeeName());
			rodDTO.getDocumentDetails().setReasonForChange(docDetailsDTO.getReasonForChange());
			rodDTO.getDocumentDetails().setLegalFirstName(docDetailsDTO.getLegalFirstName());		
			rodDTO.getDocumentDetails().setBankName(docDetailsDTO.getBankName());
			rodDTO.getDocumentDetails().setCity(docDetailsDTO.getCity());
			rodDTO.getDocumentDetails().setIfscCode(docDetailsDTO.getIfscCode());

		}
		else
		{
			populatePaymentDetailsForCashLessClaim(HospitalKey,rodDTO);
		}
		
	}
	
	private void populatePaymentDetailsForCashLessClaim(Long key,ReceiptOfDocumentsDTO rodDTO)
	{
		//HardCoding key for testing reason. This needs to be removed later.
		//key = 15l;
		Hospitals hospitals = createRodService.getHospitalsDetails(key, masterService);
		if(null != hospitals)
		{
			rodDTO.getDocumentDetails().setEmailId(hospitals.getEmailId());
			rodDTO.getDocumentDetails().setPanNo(hospitals.getPanNumber());
			rodDTO.getDocumentDetails().setHospitalName(hospitals.getName());
			String strHospitalPaymentType = hospitals.getPaymentType();
			
			if(null != strHospitalPaymentType && !("").equalsIgnoreCase(strHospitalPaymentType))
			{
				if((ReferenceTable.CHEQUE_DD).equalsIgnoreCase(strHospitalPaymentType))
				{
					//In details page , if its true, the cheque/DD will be selected. Else bank transfer will selected.
					//rodDTO.getDocumentDetails().setPaymentMode(true);
					rodDTO.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
					rodDTO.getDocumentDetails().setPayableAt(hospitals.getPayableAt());
				}
				else if((ReferenceTable.BANK_TRANSFER).equalsIgnoreCase(strHospitalPaymentType))
				{
					//rodDTO.getDocumentDetails().setPaymentMode(false);
					rodDTO.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER);
					rodDTO.getDocumentDetails().setAccountNo(hospitals.getAccountNo());
					rodDTO.getDocumentDetails().setIfscCode(hospitals.getIfscCode());
					if(null != rodDTO.getDocumentDetails().getIfscCode())
					{
						BankMaster masBank = createRodService.getBankMaster(rodDTO.getDocumentDetails().getIfscCode(),masterService);
						if(masBank != null) {
							rodDTO.getDocumentDetails().setBankId(masBank.getKey());
							rodDTO.getDocumentDetails().setBankName(masBank.getBankName());
							rodDTO.getDocumentDetails().setBranch(masBank.getBranchName());
							rodDTO.getDocumentDetails().setCity(masBank.getCity());
						}	
					}	
				}
			}
		}
	}
	
	
	
	/*public void validateBillClassification(Long claimKey)
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
	}*/
	
	
	public void validateBillClassification(Long claimKey)
	{
		List<Reimbursement> reimbursementList = ackDocReceivedService.getReimbursementDetailsForBillClassificationValidationWithoutCancelAck(claimKey);
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
				if(null != reimbursement.getStatus())
				docDTO.setStatusId(reimbursement.getStatus().getKey());
				docDTOList.add(docDTO);
			}
		}
		//return docDTOList;
		referenceDataMap.put("billClaissificationDetails",docDTOList);	
	}
	
	public void setChangeHospital(@Observes @CDIEvent(HOSPITAL_DETAILS) final ParameterDTO parameters) {
		
		ReceiptOfDocumentsDTO bean =(ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		
		UpdateHospitalDetailsDTO changeHospital = null;
		
		if(bean.getChangeHospitalDto() != null){
			changeHospital = bean.getChangeHospitalDto();
		}
		else{
			
			Long intimationKey = bean.getClaimDTO().getIntimationKey();
			
			Intimation intimation = intimationService.getIntimationByKey(intimationKey);
			
			Hospitals hospitals = hospitalService.getHospitalById(intimation.getHospital());
			
			if(hospitals == null){
				
				UpdateHospital updateHospitals = updateHospitalService.searchHospitalByKey(intimation.getHospital());
//				SelectValue selectValue = null;
//				changeHospital = ChangeHospitalMapper.getUpdateHospitalsDTO(updateHospitals);
//				if(changeHospital.getStateId() != null){
//				State state = masterService.getStateByKey(changeHospital.getStateId());
//				if(state != null){
//				selectValue = new SelectValue();
//				selectValue.setId(state.getKey());
//				selectValue.setValue(state.getValue());
//				changeHospital.setState(selectValue);
//				}
//				 
//				}
//				if(changeHospital.getCityId() != null){
//					CityTownVillage city = masterService.getCityByKey(changeHospital.getCityId());
//					if(city != null){
//						selectValue = new SelectValue();
//						selectValue.setId(city.getKey());
//						selectValue.setValue(city.getValue());
//						changeHospital.setCity(selectValue);
//						}
//					
//				}
//				if(changeHospital.getArea() != null){
//					Locality area = masterService.localitySearchByKey(changeHospital.getArea().getId());
//					
//					if(area != null){
//						selectValue = new SelectValue();
//						selectValue.setId(area.getKey());
//						selectValue.setValue(area.getValue());
//						changeHospital.setArea(selectValue);
//						}
//				}
				if(updateHospitals != null){
					
					hospitals = hospitalService.getHospitalById(updateHospitals.getHospitalId());
					
					ChangeHospitalMapper changeHospitalMapper =ChangeHospitalMapper.getInstance();
					changeHospital = changeHospitalMapper.getHosptialsDTO(hospitals);
//					changeHospital.setKey(intimation.getHospital());
					
				}
				
			}else{
			  changeHospital = ChangeHospitalMapper.getHosptialsDTO(hospitals);
//			  changeHospital.setKey(intimation.getHospital());
			}
			
			changeHospital.setIntimation(intimation);
			
		}
		view.setHospitalDetails(changeHospital);
		
		
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
		SelectValue selectValue= null;
		for (int i = 0; i < insuredList.size(); i++) {
			
			Insured insured = insuredList.get(i);
			selectValue = new SelectValue();
			selectValue.setId(Long.valueOf(String.valueOf(i)));
			selectValue.setValue(insured.getInsuredName());
			
			SelectValue payeeValue = new SelectValue();
			payeeValue.setId(Long.valueOf(String.valueOf(i)));
			payeeValue.setValue(insured.getInsuredName());
			payeeValue.setSourceRiskId(insured.getSourceRiskId());
			payeeValue.setRelationshipWithProposer(insured.getRelationshipwithInsuredId() != null ? insured.getRelationshipwithInsuredId().getValue() : "");
			payeeValue.setNameAsPerBankAccount(insured.getNameOfAccountHolder() != null ? insured.getNameOfAccountHolder() : "");
			
			selectValueList.add(selectValue);
			payeeValueList.add(payeeValue);
		}
		
		if(rodDTO.getClaimDTO().getAccDeathBenefitflag() != null 
				&& SHAConstants.DEATH_FLAG.equalsIgnoreCase(rodDTO.getClaimDTO().getAccDeathBenefitflag())
				&& rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
			
			List<PolicyNominee> pNomineeDetails = intimationService.getPolicyNomineeList(policy.getKey());
			
			if(null != policy.getProduct().getKey() 
					&& (ReferenceTable.getGMCProductList().containsKey(policy.getProduct().getKey()) || ReferenceTable.getRevisedCriticareProducts().containsKey(policy.getProduct().getKey()))){
			
				pNomineeDetails = intimationService.getPolicyNomineeListForInsured(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getKey());
			}
			
			for (PolicyNominee pNominee : pNomineeDetails) {
				selectValue = new SelectValue();
				selectValue.setId(pNominee.getKey());
				selectValue.setValue(pNominee.getNomineeName());
				payeeValueList.add(selectValue);
				selectValue = null;
			}
			
		}
		
		/*for (int i = 0; i < insuredList.size(); i++) {
			Insured insured = insuredList.get(i);
			List<NomineeDetails> nomineeDetails = policyService.getNomineeDetails(insured.getKey());
			for (NomineeDetails nomineeDetails2 : nomineeDetails) {
				selectValue = new SelectValue();
				selectValue.setId(nomineeDetails2.getKey());
				selectValue.setValue(nomineeDetails2.getNomineeName());
				
				payeeValueList.add(selectValue);
			}
			
		}*/
		
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValueList);
		
		
		SelectValue payeeSelValue = new SelectValue();
		int iSize = payeeValueList.size() +1;
		payeeSelValue.setId(Long.valueOf(String.valueOf(iSize)));
		payeeSelValue.setValue(proposerName);
		payeeSelValue.setSourceRiskId(policy.getProposerCode());
		payeeSelValue.setRelationshipWithProposer(SHAConstants.RELATIONSHIP_SELF);
		
		payeeValueList.add(payeeSelValue);
		
		if(rodDTO.getDocumentDetails().getHospitalName() != null){
			SelectValue hospitalName = new SelectValue();
			hospitalName.setId(Long.valueOf(payeeValueList.size()+1));
			hospitalName.setValue(rodDTO.getDocumentDetails().getHospitalName());
			payeeValueList.add(hospitalName);
		}
		
		
		
		
		
		
		BeanItemContainer<SelectValue> payeeNameValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		payeeNameValueContainer.addAll(payeeValueList);
		
		referenceDataMap.put("payeeNameList", payeeNameValueContainer);
		referenceDataMap.put("insuredPatientList" , selectValueContainer);
		}
		
	}
	
	
	public void saveReconsiderRequestTableValue(
			@Observes @CDIEvent(SELECT_RECONSIDER_TABLE_VALUES) final ParameterDTO parameters) {
		ReconsiderRODRequestTableDTO dto = (ReconsiderRODRequestTableDTO) parameters.getPrimaryParameter();
		List<UploadDocumentDTO> uploadDocsDTO = rodService.getRODSummaryDetails(dto.getRodKey());		
		Reimbursement reimb = rodService.getReimbursementByRODKey(dto.getRodKey());
		BankMaster bankDetails = rodService.getBankMasterByKey(reimb.getBankId(), masterService);
		
		if(null != reimb){
		dto.setAccountNo(reimb.getAccountNumber());
		dto.setEmailId(reimb.getPayeeEmailId());
		dto.setPanNo(reimb.getPanNumber());
		dto.setPayableAt(reimb.getPayableAt());
		dto.setPayeeName(reimb.getPayeeName());
		dto.setReasonForChange(reimb.getReasonForChange());
		dto.setLeagalHeirName(reimb.getLegalHeirFirstName());
		}
		if(null != bankDetails){
		dto.setIfscCode(bankDetails.getIfscCode());
		dto.setBankName(bankDetails.getBankName());
		dto.setBranchName(bankDetails.getBranchName());
		dto.setCity(bankDetails.getCity());
		}
		//return uploadDocsList;
	//	List<DocumentCheckListDTO> documentCheckListDTO = ackDocReceivedService.getDocumentCheckListValues(masterService);
		view.saveReconsiderRequestTableItems(dto,uploadDocsDTO);
		
		}
	
	public void getValidationListForRodCreation(
			@Observes @CDIEvent(VALIDATION_LIST_FOR_ROD_CREATION) final ParameterDTO parameters) {
		Long claimKey = (Long)parameters.getPrimaryParameter();
		List<DocumentDetailsDTO> documentDetailsDTO = rodService.getDocumentDetailsDTO(claimKey);
		
		view.setDocumentDetailsDTOForValidation(documentDetailsDTO);
	}
	
	public void setupIFSCDetails(
			@Observes @CDIEvent(SETUP_IFSC_DETAILS) final ParameterDTO parameters) {
		ViewSearchCriteriaTableDTO viewSearchCriteriaDTO = (ViewSearchCriteriaTableDTO) parameters.getPrimaryParameter();
		
		BankMaster masBank = masterService.getBankDetails(viewSearchCriteriaDTO.getIfscCode());
		
		if(masBank != null) {
			viewSearchCriteriaDTO.setBankId(masBank.getKey());
			viewSearchCriteriaDTO.setBankName(masBank.getBankName());
			viewSearchCriteriaDTO.setBranchName(masBank.getBranchName());
			viewSearchCriteriaDTO.setCity(masBank.getCity());
		}	
		
		view.setUpIFSCDetails(viewSearchCriteriaDTO);
	}
	
	public void validateRepeat(
			@Observes @CDIEvent(VALIDATE_ADD_ON_COVERS_REPEAT) final ParameterDTO parameters) 
	{
		Long claimKey = (Long) parameters.getPrimaryParameter();	
		List<AddOnCoversTableDTO> coversList =  (List<AddOnCoversTableDTO>) parameters.getSecondaryParameters()[0];
		Long currentAckKey = (Long)parameters.getSecondaryParameters()[1];
		DocAcknowledgement docDetails = ackDocReceivedService.getDocAcknowledgmentByClaimKey(claimKey);	
		List<Reimbursement> reimbDetails = rodService.getNonCancelledReimbursementByClaimKey(claimKey);
		String isValidCoverName = null;
		/*for (Reimbursement reimbursement : reimbDetails) {				
		if(null != docDetails && ! ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS.equals(docDetails.getStatus().getKey()) && !(ReferenceTable.FINANCIAL_CANCEL_ROD.equals(reimbursement.getStatus().getKey())
				|| ReferenceTable.BILLING_CANCEL_ROD.equals(reimbursement.getStatus().getKey()) || ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS.equals(reimbursement.getStatus().getKey())
				|| ReferenceTable.ZONAL_REVIEW_CANCEL_ROD.equals(reimbursement.getStatus().getKey())))
		{
		*/
		if(null != reimbDetails && !reimbDetails.isEmpty())
		{
		if(null != docDetails && ! ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS.equals(docDetails.getStatus().getKey()))
				{
			isValidCoverName = rodService.getAlreadySelectedAdditionalCoversForRod(claimKey ,coversList,currentAckKey);
		}
		}
		//}
		
		Boolean isValid = false;
		
		if(null != isValidCoverName){
			isValid = true;
		}
	/*	List<DocAcknowledgement> docAckList = rodService.getDocAckListByClaim(claimKey);
		String isValidCoverName = "";
		Boolean isValid = true;
		if(null != docAckList && !docAckList.isEmpty()){
			for (DocAcknowledgement docAcknowledgement : docAckList) {
				
				isValidCoverName = rodService.getAlreadySelectedAdditionalCoversByAckKey(docAcknowledgement.getKey() ,coversList,currentAckKey);
				if(null != isValidCoverName && !("").equalsIgnoreCase(isValidCoverName)){
					isValid = false;
					break;
				}	
			}
		}
*/	/*	List<Reimbursement> reimbList= ackDocReceivedService.getReimbursementDetails(claimKey);
		String isValidCoverName = "";
		Boolean isValid = true;

		if(null != reimbList && !reimbList.isEmpty())
		{
			//Reimbursement reimbursement = reimbList.get(0);
			for (Reimbursement reimbursement : reimbList) {			
			isValidCoverName = rodService.getAlreadySelectedAdditionalCovers(reimbursement.getKey() ,coversList);
			if(null != isValidCoverName && !("").equalsIgnoreCase(isValidCoverName)){
				isValid = false;
			}	
			}
		}*/

		view.validateCoversRepeat(isValid,isValidCoverName);
		
	}
	
	public void validateOptionalCoverRepeat(
			@Observes @CDIEvent(VALIDATE_OPTIONAL_COVERS_REPEAT) final ParameterDTO parameters) 
	{
		Long claimKey = (Long) parameters.getPrimaryParameter();	
		List<AddOnCoversTableDTO> coversList =  (List<AddOnCoversTableDTO>) parameters.getSecondaryParameters()[0];
		Long currentAckKey = (Long)parameters.getSecondaryParameters()[1];
		DocAcknowledgement docDetails = ackDocReceivedService.getDocAcknowledgmentByClaimKey(claimKey);
		List<Reimbursement> reimbDetails = rodService.getNonCancelledReimbursementByClaimKey(claimKey);
		String isValidCoverName = null;
		/*for (Reimbursement reimbursement : reimbDetails) 
		{*/	
		/*if(null != docDetails && ! ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS.equals(docDetails.getStatus().getKey()) && !(ReferenceTable.FINANCIAL_CANCEL_ROD.equals(reimbursement.getStatus().getKey())
				|| ReferenceTable.BILLING_CANCEL_ROD.equals(reimbursement.getStatus().getKey()) || ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS.equals(reimbursement.getStatus().getKey())
				|| ReferenceTable.ZONAL_REVIEW_CANCEL_ROD.equals(reimbursement.getStatus().getKey())))
		{				
		*/	if(null != reimbDetails && !reimbDetails.isEmpty())
		{
			if(null != docDetails && ! ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS.equals(docDetails.getStatus().getKey()))
			{			
			isValidCoverName = rodService.getAlreadySelectedOptionalCoversForRod(claimKey ,coversList,currentAckKey);
		}
		}
		//}
		
		Boolean isValid = false;
		
		if(null != isValidCoverName){
			isValid = true;
		}

		//List<Reimbursement> reimbList= ackDocReceivedService.getReimbursementDetails(claimKey);
		/*List<DocAcknowledgement> docAckList = rodService.getDocAckListByClaim(claimKey);
		String isValidCoverName = "";
		Boolean isValid = true;
		if(null != docAckList && !docAckList.isEmpty()){
			for (DocAcknowledgement docAcknowledgement : docAckList) {
				
				isValidCoverName = rodService.getAlreadySelectedOptionalCoversByAckKey(docAcknowledgement.getKey() ,coversList,currentAckKey);
				if(null != isValidCoverName && !("").equalsIgnoreCase(isValidCoverName)){
					isValid = false;
					break;
				}	
			}
		}*/
		/*if(null != reimbList && !reimbList.isEmpty())
		{
			//Reimbursement reimbursement = reimbList.get(0);
			for (Reimbursement reimbursement : reimbList) {	
			isValidCoverName = rodService.getAlreadySelectedOptionalCovers(reimbursement.getKey() ,coversList);
			if(null != isValidCoverName && !("").equalsIgnoreCase(isValidCoverName)){
				isValid = false;
			}
			}			
		}*/
		view.validateCoversRepeat(isValid,isValidCoverName);
		
	}
	
	/*public void setUpDocumentCheckListValues(
			@Observes @CDIEvent(SETUP_DOCUMENT_CHECKLIST_TABLE_VALUES) final ParameterDTO parameters) {
		List<DocumentCheckListDTO> documentCheckListDTO = ackDocReceivedService.getDocumentCheckListValues(masterService);
		view.setDocumentCheckListTableValues(documentCheckListDTO);
		
		}
*/
	
	public void setDocStatusIfReplyReceivedForQuery(
			@Observes @CDIEvent(ROD_SET_DOC_REC_STATUS_FOR_QUERY_REPLY) final ParameterDTO parameters) {
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
	/*	BankMaster bankObj = null;
		if(null != rodQueryDetails.getRodNo())
		{
		Reimbursement reimObj = ackDocReceivedService.getReimbursementByRodNo(rodQueryDetails.getRodNo());		
		if(null != reimObj && null != reimObj.getBankId()){
			bankObj = ackDocReceivedService.getBankDetails(reimObj.getBankId());
		}
		
		if(null != reimObj){		
		rodQueryDetails.setPayableAt(reimObj.getPayableAt());
		rodQueryDetails.setEmailId(reimObj.getPayeeEmailId());
		rodQueryDetails.setPanNo(reimObj.getPanNumber());
		rodQueryDetails.setLegalHairName(reimObj.getLegalHeirFirstName());
		rodQueryDetails.setReasonForchange(reimObj.getReasonForChange());
		rodQueryDetails.setAccNo(reimObj.getAccountNumber());
		}
		if(null != bankObj){
		rodQueryDetails.setBankName(bankObj.getBankName());
		rodQueryDetails.setBranchName(bankObj.getBranchName());
		rodQueryDetails.setCity(bankObj.getCity());
		rodQueryDetails.setIfscCode(bankObj.getIfscCode());
		}		
		}*/
		view.setDocStatusIfReplyReceivedForQuery(rodQueryDetails);
			
		
	}
	
	
	public void compareWithPreviousROD(@Observes @CDIEvent(COMPARE_WITH_PREVIOUS_ROD) final ParameterDTO parameters) {
		ReceiptOfDocumentsDTO bean = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		Reimbursement previousLatestROD = reimbursementService.getFilteredPreviousLatestROD(bean.getClaimDTO().getKey(), null);
		String comparisonResult = "";
		if(previousLatestROD != null) {
			if(previousLatestROD.getDateOfAdmission() != null && bean.getDocumentDetails().getDateOfAdmission() != null && !previousLatestROD.getDateOfAdmission().equals(bean.getDocumentDetails().getDateOfAdmission())) {
				comparisonResult += "Previous DOA : " + SHAUtils.formatDate(previousLatestROD.getDateOfAdmission()) + "  Current DOA : " + SHAUtils.formatDate(bean.getDocumentDetails().getDateOfAdmission()) + "</br>";
			}
			
//			if(previousLatestROD.getDateOfDischarge() != null && bean.getDocumentDetails().get != null && !previousLatestROD.getDateOfDischarge().equals(bean.getPreauthDataExtractionDetails().getDischargeDate())) {
//				comparisonResult += "Previous DOD : " + SHAUtils.formatDate(previousLatestROD.getDateOfDischarge()) + " Current DOD : " + SHAUtils.formatDate(bean.getPreauthDataExtractionDetails().getDischargeDate());
//			}
			
			
//			if(previousLatestROD.getPayeeName() != null && bean.getDocumentDetails().getPayeeName() != null && !previousLatestROD.getPayeeName().equals(bean.getDocumentDetails().getPayeeName().getValue())) {
//				comparisonResult += "Previous Insured Patient Name : " + (previousLatestROD.getPayeeName()) + "  Current Insured Patient Name : " + (bean.getDocumentDetails().getPayeeName());
//			}
			
		}
		view.setComparisonResult(comparisonResult);
	}
	
	public void populatePreviousPaymentDetails(@Observes @CDIEvent(CREATE_ROD_POPULATE_PREVIOUS_ACCT_DETAILS) final ParameterDTO parameters)
	{
		PreviousAccountDetailsDTO tableDTO = (PreviousAccountDetailsDTO) parameters.getPrimaryParameter();
		view.populatePreviousPaymentDetails(tableDTO);
		
	}
	
	public void validateBenefitRepeat(
			@Observes @CDIEvent(VALIDATE_BENEFIT_REPEAT) final ParameterDTO parameters) 
	{
		Long claimKey = (Long) parameters.getPrimaryParameter();
		Long value = (Long) parameters.getSecondaryParameters()[0];
		Boolean chkBoxValue = (Boolean) parameters.getSecondaryParameters()[1];
		com.vaadin.v7.ui.CheckBox chkBox = (com.vaadin.v7.ui.CheckBox) parameters.getSecondaryParameters()[2];
		Long currentAckKey = (Long)parameters.getSecondaryParameters()[3];
		String benefitValue = (String)parameters.getSecondaryParameters()[4];
		List<DocAcknowledgement> docDetails = ackDocReceivedService.getAckDetailsForBillClassificationValidation(claimKey);		
		if(null != docDetails)
		{		
		Boolean isValid = createRodService.getAlreadySelectedBenefit(claimKey,value);
		view.validateBenefitRepeat(isValid,chkBoxValue,chkBox,benefitValue);
		}
//		view.validateBenefitRepeat(isValid,chkBoxValue);
		
	}

	/*public void validateBenefitRepeatBillentry(
			@Observes @CDIEvent(VALIDATE_BENEFIT_REPEAT_BILL_ENTRY) final ParameterDTO parameters) 
	{
		Long claimKey = (Long) parameters.getPrimaryParameter();
		Long value = (Long) parameters.getSecondaryParameters()[0];
		Boolean chkBoxValue = (Boolean) parameters.getSecondaryParameters()[1];
<<<<<<< HEAD
		com.vaadin.v7.ui.CheckBoxchkBox = (com.vaadin.ui.CheckBox) parameters.getSecondaryParameters()[2];
=======
		com.vaadin.v7.ui.CheckBox chkBox = (com.vaadin.v7.ui.CheckBox) parameters.getSecondaryParameters()[2];
>>>>>>> a1b28df37a8ac40ebb73f2c4cd51036c598b388a
		Long rodKey = (Long) parameters.getSecondaryParameters()[3];
		String benefitValue = (String)parameters.getSecondaryParameters()[4];
		List<DocAcknowledgement> docDetails = ackDocReceivedService.getAckDetailsForBillClassificationValidation(claimKey);			
		if(null != docDetails )//&& ! ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS.equals(docDetail)
		{		
		Boolean isValid = createRodService.getAlreadySelectedBenefitBillEntry(claimKey,value,rodKey);
		view.validateBenefitRepeat(isValid,chkBoxValue,chkBox,benefitValue);
		}
//		view.validateBenefitRepeat(isValid,chkBoxValue);
		
	}*/
	
	public void validateResetForParticulars(
			@Observes @CDIEvent(RESET_PARTICULARS_VALUES) final ParameterDTO parameters) 
	{
		/*String value = (String) parameters.getPrimaryParameter();
//		Boolean isValid = ackDocReceivedService.getAlreadySelectedBenefit(claimKey,value);
		BeanItemContainer<SelectValue> particularsByBenefit = masterService.getParticularsByBenefit(SHAConstants.MASTER_TYPE_PA, value);
		view.resetParticularsBasedOnBenefit(particularsByBenefit,value);*/
		
	}
	
	public void validateResetForParticularsForAddonCover(
			@Observes @CDIEvent(RESET_PARTICULARS_VALUES_FOR_ADD_ON_COVER) final ParameterDTO parameters) 
	{
		SelectValue value = (SelectValue) parameters.getPrimaryParameter();		
//		Boolean isValid = ackDocReceivedService.getAlreadySelectedBenefit(claimKey,value);
		view.resetParticularsBasedOnAddOnCover(value);
		
	}
	
	public void setupNomineeIFSCDetails(
			@Observes @CDIEvent(SETUP_NOMINEE_IFSC_DETAILS_PA_CROD) final ParameterDTO parameters) {
		ViewSearchCriteriaTableDTO viewSearchCriteriaDTO = (ViewSearchCriteriaTableDTO) parameters.getPrimaryParameter();
		
		BankMaster masBank = masterService.getBankDetails(viewSearchCriteriaDTO.getIfscCode());
		
		if(masBank != null) {
			viewSearchCriteriaDTO.setBankId(masBank.getKey());
			viewSearchCriteriaDTO.setBankName(masBank.getBankName());
			viewSearchCriteriaDTO.setBranchName(masBank.getBranchName());
			viewSearchCriteriaDTO.setCity(masBank.getCity());
		}	
		
		view.setUpIFSCDetails(viewSearchCriteriaDTO);
	}
	
	public void setupAddBankIFSCDetails(
			@Observes @CDIEvent(ADD_BANK_IFSC_DETAILS_PA_CROD) final ParameterDTO parameters) {
		ViewSearchCriteriaTableDTO viewSearchCriteriaDTO = (ViewSearchCriteriaTableDTO) parameters.getPrimaryParameter();
		
		BankMaster masBank = masterService.getBankDetails(viewSearchCriteriaDTO.getIfscCode());
		
		if(masBank != null) {
			viewSearchCriteriaDTO.setBankId(masBank.getKey());
			viewSearchCriteriaDTO.setBankName(masBank.getBankName());
			viewSearchCriteriaDTO.setBranchName(masBank.getBranchName());
			viewSearchCriteriaDTO.setCity(masBank.getCity());
		}
		
		view.setUpAddBankIFSCDetails(viewSearchCriteriaDTO);
	}
}
