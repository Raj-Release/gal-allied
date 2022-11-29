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
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.policy.updateHospital.ui.UpdateHospitalService;
import com.shaic.claim.registration.updateHospitalDetails.UpdateHospitalDetailsDTO;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.forms.AddBanksDetailsTableDTO;
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
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.CheckBox;

/**
 * @author ntv.vijayar
 *
 */
@ViewInterface(CreateRODDocumentDetailsView.class)
public class CreateRODDocumentDetailsPresenter  extends AbstractMVPPresenter<CreateRODDocumentDetailsView>
{

	public static final String CREATE_ROD_SETUP_DROPDOWN_VALUES = "create_rod_setup_dropdown_values";
	
	public static final String SETUP_DOCUMENT_CHECKLIST_TABLE_VALUES = "setup_doc_checklist_tbl_values";
	
	public static final String SELECT_RECONSIDER_TABLE_VALUES = "select_reconsider_table_values";
	
	public static final String HOSPITAL_DETAILS = "change Hospital Details";
	public static final String VALIDATION_LIST_FOR_ROD_CREATION = "validation_list_for_rod_creation";
	
	public static final String HOSPITAL_PAYMENT_DETAILS = "hospital_payment_details";
	
	public static final String SETUP_IFSC_DETAILS  = "setup_ifsc_details";
	
	public static final String SETUP_NOMINEE_IFSC_DETAILS = "setup Nominee IFSC Details";

	public static final String SETUP_LEGAL_HEIR_IFSC_DETAILS = "setup Legal Heir IFSC Details";
	
	public static final String ROD_SET_DOC_REC_STATUS_FOR_QUERY_REPLY = "rod_set_doc_rec_status_for_query_reply";
	
	public static final String COMPARE_WITH_PREVIOUS_ROD = "create_rod_compare_with_previous_rod";
	
	public static final String CREATE_ROD_POPULATE_PREVIOUS_ACCT_DETAILS = "create_rod_populate_previous_acct_details";
	
	public static final String CREATE_ROD_VALIDATE_LUMPSUM_AMOUNT_CLASSIFICATION = "create_rod_validate_lumpsum_amount_classification";
	
	public static final String VALIDATE_HOSPITALIZATION_REPEAT = "create_rod_validate_hospitalization_repeat";
	
	public static final String REFERENCE_DATA_CLEAR   = "clear reference data in create rod document details page";
	
	public static final String CLAIMED_AMNT_ALERT = "Claimed Amount Alert";

	public static final String SETUP_ROD_PAYABLE_DETAILS  = "setup_payable_details_rod";
	
	public static final String ADD_BANK_IFSC_DETAILS_CROD  = "setup_ifsc_details_add_bank_create_rod";
	
	public static final String SETUP_BANK_DETAILS  = "setup_bank_details";
	
//	public static final String SETUP_ACC_PREF_DETAILS = "set up Account Preference Details";
	
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
	
	@EJB
	private DBCalculationService dbCalculationService;
	
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
		referenceDataMap.put("particulars", masterService.getDocumentCheckListValuesContainer(SHAConstants.MASTER_TYPE_REIMBURSEMENT));
		getValuesForNameDropDown(rodDTO);
		getDocumentDetailsDTOList(rodDTO.getClaimDTO().getKey());
		validateBillClassification(rodDTO.getClaimDTO().getKey());
		referenceDataMap.put("isFinalEnhancement",getCashlessClaimStatus(rodDTO.getClaimDTO().getKey()));
		
		referenceDataMap.put("patientStatusContainer", masterService.getSelectValueContainer(ReferenceTable.REIMBURSEMENT_PATIENT_STATUS));
		//added for new product hosptal cash
		referenceDataMap.put("diagnosisHospitalCashContainer", masterService.getDiagnosisHospitalCash());
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
		
		Hospitals hospitals = createRodService.getHospitalsDetails(HospitalKey, masterService);
		
		if(hospitals != null){
			rodDTO.getDocumentDetails().setHospitalName(hospitals.getName());
		}
		
		if(null != docDetailsDTO )
		{
			rodDTO.getDocumentDetails().setAccountNo(docDetailsDTO.getAccountNo());
			rodDTO.getDocumentDetails().setPayableAt(docDetailsDTO.getPayableAt());
			rodDTO.getDocumentDetails().setEmailId(docDetailsDTO.getEmailId());
			rodDTO.getDocumentDetails().setPanNo(docDetailsDTO.getPanNo());
			rodDTO.getDocumentDetails().setPayeeName(docDetailsDTO.getPayeeName());
			rodDTO.getDocumentDetails().setReasonForChange(docDetailsDTO.getReasonForChange());
			rodDTO.getDocumentDetails().setPayModeChangeReason(docDetailsDTO.getPayModeChangeReason());
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
			rodDTO.getDocumentDetails().setHospitalPayableAt(hospitals.getPayableAt());
			String strHospitalPaymentType = hospitals.getPaymentType();
			
			if(null != strHospitalPaymentType && !("").equalsIgnoreCase(strHospitalPaymentType))
			{
				if((ReferenceTable.PAYMENT_TYPE_CHEQUE).equalsIgnoreCase(strHospitalPaymentType))
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
					
					if(rodDTO.getDocumentDetails().getDocumentsReceivedFrom() != null 
					&& rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId().
					equals(ReferenceTable.RECEIVED_FROM_HOSPITAL))
					{
					
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
				 }else{
					 List<Reimbursement> previousRODByClaimKey = createRodService.getPreviousRODByClaimKey(rodDTO.getClaimDTO().getKey());
						if (previousRODByClaimKey != null && !previousRODByClaimKey.isEmpty()) {
							for (Reimbursement reimbursement : previousRODByClaimKey) {
								if(reimbursement.getStatus() != null && !ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey())
										&& !ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey())) { 
									if(rodDTO.getDocumentDetails().getDocumentsReceivedFrom() != null 
									&& rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId().
									equals(reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getKey()))
								{
										rodDTO.getDocumentDetails().setAccountNo(reimbursement.getAccountNumber());
										if (null != reimbursement.getBankId()) {
											BankMaster masBank = createRodService.getBankDetails(reimbursement
													.getBankId());
											
											if(masBank != null) {
												rodDTO.getDocumentDetails().setBankId(masBank.getKey());
												rodDTO.getDocumentDetails().setBankName(masBank.getBankName());
												rodDTO.getDocumentDetails().setCity(masBank.getCity());
												rodDTO.getDocumentDetails().setIfscCode(masBank.getIfscCode());
												rodDTO.getDocumentDetails().setCity(masBank.getCity());
											}	
												rodDTO.getDocumentDetails().setPayableAt(reimbursement.getPayableAt());
										}
								}
									
							}
						}
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
			DocumentDetailsDTO docDTO = null;
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
				if(null != docAck.getProdHospBenefitFlag()){
					docDTO.setHospitalCashFlag(docAck.getProdHospBenefitFlag());
				}
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
			  changeHospital = ChangeHospitalMapper.getInstance().getHosptialsDTO(hospitals);
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
		//CODE CHANGED FOR GMC SLOWNESS - 20-MAY-2019
		Policy policy = policyService.getPolicy(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyNumber());
		if(null != policy)
		{
			if(!ReferenceTable.getGMCProductList().containsKey(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
				String proposerName =  policy.getProposerFirstName();
				List<Insured> insuredList = policy.getInsured();
				
				List<SelectValue> selectValueList = new ArrayList<SelectValue>();
				List<SelectValue> payeeValueList = new ArrayList<SelectValue>();
				
				List<SelectValue> insuredValueList = new ArrayList<SelectValue>();
				
				SelectValue selectValue= null;
				for (int i = 0; i < insuredList.size(); i++) {
					
					Insured insured = insuredList.get(i);
					selectValue = new SelectValue();
					//SelectValue payeeValue = new SelectValue();
					selectValue.setId(Long.valueOf(String.valueOf(i)));
					selectValue.setValue(insured.getInsuredName());
					selectValue.setRelationshipWithProposer(insured.getRelationshipwithInsuredId() != null ? insured.getRelationshipwithInsuredId().getValue() : "");
					selectValue.setSourceRiskId(insured.getSourceRiskId());
					selectValue.setNameAsPerBankAccount(insured.getNameOfAccountHolder());
					//payeeValue.setId(Long.valueOf(String.valueOf(i)));
					//payeeValue.setValue(insured.getInsuredName());
					
					selectValueList.add(selectValue);
					//payeeValueList.add(payeeValue);
				}
				payeeValueList.addAll(selectValueList);
				insuredValueList.addAll(selectValueList);
				
				for (int i = 0; i < insuredList.size(); i++) {
					Insured insured = insuredList.get(i);
					List<NomineeDetails> nomineeDetails = policyService.getNomineeDetails(insured.getKey());
					for (NomineeDetails nomineeDetails2 : nomineeDetails) {
						selectValue = new SelectValue();
						selectValue.setId(nomineeDetails2.getKey());
						selectValue.setValue(nomineeDetails2.getNomineeName());
						selectValue.setRelationshipWithProposer(nomineeDetails2.getRelationId() != null ? nomineeDetails2.getRelationId().getValue() : "");
						payeeValueList.add(selectValue);
						selectValue = null;
					}
					
				}
				
				SelectValue payeeSelValue = new SelectValue();
				int iSize = payeeValueList.size() +1;
				payeeSelValue.setId(Long.valueOf(String.valueOf(iSize)));
				payeeSelValue.setValue(proposerName);
				payeeSelValue.setSourceRiskId(policy.getProposerCode());
				payeeSelValue.setRelationshipWithProposer(SHAConstants.RELATIONSHIP_SELF);
				
				payeeValueList.add(payeeSelValue);

				
				//IMSSUPPOR-28338		
						//added for showing the policy nominee details in payment list
								List<PolicyNominee> policyNomineeList = intimationService.getPolicyNomineeList(policy.getKey());
								if(policyNomineeList != null && ! policyNomineeList.isEmpty()){
								for (PolicyNominee policyNomineeList2 : policyNomineeList) {
									selectValue = new SelectValue();
									selectValue.setId(policyNomineeList2.getKey());
									selectValue.setValue(policyNomineeList2.getNomineeName());
									selectValue.setRelationshipWithProposer(policyNomineeList2.getRelationshipWithProposer() != null ? policyNomineeList2.getRelationshipWithProposer() : "");
									selectValue.setSourceRiskId(policyNomineeList2.getNomineeCode());
//									selectValue.setNameAsPerBankAccount(policyNomineeList2.getNa ());
									payeeValueList.add(selectValue);
									selectValue = null;
								}
								}
								//added for showing the policy nominee details in payment list

				if((rodDTO.getDeath() != null && rodDTO.getDeath()) || (rodDTO.getDeathFlag() != null && SHAConstants.YES_FLAG.equalsIgnoreCase(rodDTO.getDeathFlag()))   
						&& rodDTO.getDocumentDetails().getDocumentsReceivedFrom() != null
						&& ReferenceTable.RECEIVED_FROM_INSURED.equals(rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId())
						&& rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
						&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
				
					List<PolicyNominee> pNomineeDetails = intimationService.getPolicyNomineeList(policy.getKey());
				
				if(null != policy.getProduct().getKey() 
						&& (ReferenceTable.getGMCProductList().containsKey(policy.getProduct().getKey()) || ReferenceTable.getRevisedCriticareProducts().containsKey(policy.getProduct().getKey())
								 || policy.getProduct().getKey().equals(ReferenceTable.RAKSHAK_CORONA_PRODUCT_KEY))){
				
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
				
				BeanItemContainer<SelectValue> insuredListContainer = new BeanItemContainer<SelectValue>(
						SelectValue.class);
				insuredListContainer.addAll(insuredValueList);
				
				// IMSSUPPOR-28338
				// added for showing the policy nominee details in payment list
				//GALAXYMAIN-12655 commented as discussed with-show nominee list only for diseased cases
				/*List<PolicyNominee> policyNomineeList = intimationService
						.getPolicyNomineeList(policy.getKey());
				if (policyNomineeList != null && !policyNomineeList.isEmpty()) {
					for (PolicyNominee policyNomineeList2 : policyNomineeList) {
						selectValue = new SelectValue();
						selectValue.setId(policyNomineeList2.getKey());
						selectValue.setValue(policyNomineeList2
								.getNomineeName());
						payeeValueList.add(selectValue);
						selectValue = null;
					}
				}*/
				// added for showing the policy nominee details in payment list
				
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
				
				BeanItemContainer<SelectValue> payeeNameValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
				
				payeeNameValueContainer.addAll(payeeValueList);
				
				referenceDataMap.put("payeeNameList", payeeNameValueContainer);
				referenceDataMap.put("insuredPatientList" , insuredListContainer);
				
			}
		else
			{
				BeanItemContainer<SelectValue> payeeNameValueContainer = dbCalculationService.getPayeeName(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getKey(),
						rodDTO.getClaimDTO().getNewIntimationDto().getKey());
				
				List<Insured> insuredList = new ArrayList<Insured>();
				
				if(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getDependentRiskId() != null){
					insuredList = policyService.getInsuredListForGMC(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getDependentRiskId());	
				}else{
					insuredList = policyService.getInsuredListForGMC(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getInsuredId());	
				}
				
				List<SelectValue> selectValueList = new ArrayList<SelectValue>();
				List<SelectValue> insuredValueList = new ArrayList<SelectValue>();
				
				SelectValue selectValue= null;
				for (int i = 0; i < insuredList.size(); i++) {
					
					Insured insured = insuredList.get(i);
					selectValue = new SelectValue();
					//SelectValue payeeValue = new SelectValue();
					selectValue.setId(Long.valueOf(String.valueOf(i)));
					selectValue.setValue(insured.getInsuredName());
					selectValue.setRelationshipWithProposer(insured.getRelationshipwithInsuredId() != null ? insured.getRelationshipwithInsuredId().getValue() : "");
					//payeeValue.setId(Long.valueOf(String.valueOf(i)));
					//payeeValue.setValue(insured.getInsuredName());
					
					selectValueList.add(selectValue);
					//payeeValueList.add(payeeValue);
				}
				insuredValueList.addAll(selectValueList);
				
				BeanItemContainer<SelectValue> insuredListContainer = new BeanItemContainer<SelectValue>(
						SelectValue.class);
				insuredListContainer.addAll(insuredValueList);
				
				
				if((rodDTO.getDeath() != null && rodDTO.getDeath()) || (rodDTO.getDeathFlag() != null && SHAConstants.YES_FLAG.equalsIgnoreCase(rodDTO.getDeathFlag()))   
						&& rodDTO.getDocumentDetails().getDocumentsReceivedFrom() != null
						&& ReferenceTable.RECEIVED_FROM_INSURED.equals(rodDTO.getDocumentDetails().getDocumentsReceivedFrom().getId())
						&& rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
						&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
				
					List<PolicyNominee> pNomineeDetails = intimationService.getPolicyNomineeList(policy.getKey());
				
				if(null != policy.getProduct().getKey() 
						&& (ReferenceTable.getGMCProductList().containsKey(policy.getProduct().getKey()) || ReferenceTable.getRevisedCriticareProducts().containsKey(policy.getProduct().getKey())
								 || policy.getProduct().getKey().equals(ReferenceTable.RAKSHAK_CORONA_PRODUCT_KEY))){
				
					pNomineeDetails = intimationService.getPolicyNomineeListForInsured(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getKey());
				}
				
				for (PolicyNominee pNominee : pNomineeDetails) {
					selectValue = new SelectValue();
					selectValue.setId(pNominee.getKey());
					selectValue.setValue(pNominee.getNomineeName());
					payeeNameValueContainer.addBean(selectValue);
					selectValue = null;
				}
				
				}
				
					referenceDataMap.put("payeeNameList", payeeNameValueContainer);
					referenceDataMap.put("insuredPatientList" , insuredListContainer);
			}
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
		dto.setPatientStatus(new SelectValue());
		
		if(reimb.getPatientStatus() != null){
			dto.getPatientStatus().setId(reimb.getPatientStatus().getKey());
			dto.getPatientStatus().setValue(reimb.getPatientStatus().getValue());
		}
		dto.setLegalHeirName(reimb.getNomineeName());
		dto.setLegalHeirAddr(reimb.getNomineeAddr());
		dto.setDocumentReceivedFrom(reimb.getDocAcknowLedgement() != null 
				&& reimb.getDocAcknowLedgement().getDocumentReceivedFromId() != null ? reimb.getDocAcknowLedgement().getDocumentReceivedFromId().getValue() : "");
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
	
	public void setupNomineeIFSCDetails(
			@Observes @CDIEvent(SETUP_NOMINEE_IFSC_DETAILS) final ParameterDTO parameters) {
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
	
	public void setupLegalHeirIFSCDetails(
			@Observes @CDIEvent(SETUP_LEGAL_HEIR_IFSC_DETAILS) final ParameterDTO parameters) {
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
			@Observes @CDIEvent(ADD_BANK_IFSC_DETAILS_CROD) final ParameterDTO parameters) {
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
	
	public void setupAddBankDetails(
			@Observes @CDIEvent(SETUP_BANK_DETAILS) final ParameterDTO parameters) {
		ReceiptOfDocumentsDTO dto = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		view.setUpBankDetails(dto);
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
		StringBuffer comparisonResult = new StringBuffer();
		if(previousLatestROD != null) {
			if(previousLatestROD.getDateOfAdmission() != null && bean.getDocumentDetails().getDateOfAdmission() != null && !previousLatestROD.getDateOfAdmission().equals(bean.getDocumentDetails().getDateOfAdmission())) {
				comparisonResult.append("Previous DOA : ").append(SHAUtils.formatDate(previousLatestROD.getDateOfAdmission())).append("  Current DOA : ").append(SHAUtils.formatDate(bean.getDocumentDetails().getDateOfAdmission())).append("</br>");
			}
			
//			if(previousLatestROD.getDateOfDischarge() != null && bean.getDocumentDetails().get != null && !previousLatestROD.getDateOfDischarge().equals(bean.getPreauthDataExtractionDetails().getDischargeDate())) {
//				comparisonResult += "Previous DOD : " + SHAUtils.formatDate(previousLatestROD.getDateOfDischarge()) + " Current DOD : " + SHAUtils.formatDate(bean.getPreauthDataExtractionDetails().getDischargeDate());
//			}
			
			
//			if(previousLatestROD.getPayeeName() != null && bean.getDocumentDetails().getPayeeName() != null && !previousLatestROD.getPayeeName().equals(bean.getDocumentDetails().getPayeeName().getValue())) {
//				comparisonResult += "Previous Insured Patient Name : " + (previousLatestROD.getPayeeName()) + "  Current Insured Patient Name : " + (bean.getDocumentDetails().getPayeeName());
//			}
			
		}
		view.setComparisonResult(comparisonResult.toString());
	}
	
	public void populatePreviousPaymentDetails(@Observes @CDIEvent(CREATE_ROD_POPULATE_PREVIOUS_ACCT_DETAILS) final ParameterDTO parameters)
	{
		PreviousAccountDetailsDTO tableDTO = (PreviousAccountDetailsDTO) parameters.getPrimaryParameter();
		view.populatePreviousPaymentDetails(tableDTO);
		
	}
	
	public void validateLumpsumAmountFlag(
			@Observes @CDIEvent(CREATE_ROD_VALIDATE_LUMPSUM_AMOUNT_CLASSIFICATION) final ParameterDTO parameters) 
	{
		
		Long claimKey = (Long) parameters.getPrimaryParameter();
		String classificationType = (String) parameters.getSecondaryParameter(0, String.class);
		CheckBox chkBox = (CheckBox) parameters.getSecondaryParameter(1, CheckBox.class);
		Long intimationKey = (Long) parameters.getSecondaryParameter(2, Long.class);
		Boolean isValid = rodService.isLumpSumExistsForClaim(claimKey,intimationKey,classificationType);
		view.validateLumpSumAmount(isValid,classificationType,chkBox);
		
	}
	

	public void validateHospitalizationRepeat(
			@Observes @CDIEvent(VALIDATE_HOSPITALIZATION_REPEAT) final ParameterDTO parameters) 
	{
		Long claimKey = (Long) parameters.getPrimaryParameter();
		Boolean isValid = ackDocReceivedService.getStatusOfFirstRODForAckValidation(claimKey);
		view.validateHospitalizationRepeat(isValid);
		
	}

	public void clearReferenceData(
			@Observes @CDIEvent(REFERENCE_DATA_CLEAR) final ParameterDTO parameters) {
		SHAUtils.setClearReferenceData(referenceDataMap);
	}
	
	public void setupPayableDetails(
			@Observes @CDIEvent(SETUP_ROD_PAYABLE_DETAILS) final ParameterDTO parameters) {
		String viewSearchCriteriaDTO = (String) parameters.getPrimaryParameter();
		view.setUpPayableDetails(viewSearchCriteriaDTO);
	}
	

	/*public void validateClaimedAmount(
			@Observes @CDIEvent(CLAIMED_AMNT_ALERT) final ParameterDTO parameters) 
	{
		
		view.validateClaimedAmount();
		
	}*/

}