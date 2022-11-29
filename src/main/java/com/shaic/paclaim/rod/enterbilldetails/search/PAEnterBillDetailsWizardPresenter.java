package com.shaic.paclaim.rod.enterbilldetails.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.common.APIService;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(PAEnterBillDetailsWizardView.class)
public class PAEnterBillDetailsWizardPresenter extends AbstractMVPPresenter<PAEnterBillDetailsWizardViewImpl>
{

	private static final long serialVersionUID = 1800989028816886291L;

	public static final String SUBMIT_BILL_ENTRY_DETAILS = "submit_bill_entry_details_PA";
	
	@EJB
	private CreateRODService rodService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	

	//public static final String SUBMIT_ACKNOWLEDGE_DOC_RECEIVED = "submit_acknowledge_doc_received";
	
	public static final String VALIDATE_BILL_ENTRY_USER_RRC_REQUEST = "validate_bill_entry_user_rrc_request_PA";
	
	public static final String BILL_ENTRY_RRC_REQUEST_DROP_DOWN_VALUES = "bill_entry_load_rrc_request_drop_down_values_PA";
	
	public static final String BILL_ENTRY_SAVE_RRC_REQUEST_VALUES = "bill_entry_save_rrc_request_values_PA";

	public static final String PA_BILL_ENTRY_RRC_REQUEST_SUB_CATEGORY_VALUES = "pa_bill_entry_rrc_request_sub_category_values";

	public static final String PA_BILL_ENTRY_RRC_REQUEST_SOURCE_VALUES = "pa_bill_entry_rrc_request_source_values";


	@EJB
	private APIService apiService;
	
	@EJB
	private HospitalService hospitalService;


	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private MasterService masterService;
	
	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_BILL_ENTRY_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	

	public void saveRRCRequestValues(@Observes @CDIEvent(BILL_ENTRY_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_REIMBURSEMENT);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(BILL_ENTRY_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	/**
	 * Added for RRC Ends
	 * */
	
	@SuppressWarnings("static-access")
	public void submitWizard(
			@Observes @CDIEvent(SUBMIT_BILL_ENTRY_DETAILS) final ParameterDTO parameters) {
		StringBuffer billClassification = null;
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		if(rodDTO != null && rodDTO.getTotalClaimedAmount() != null){
		rodDTO.setCurrentProvisionAmount(rodDTO.getTotalClaimedAmount());
		}
		//Need to uncomment the below code.temporarly 
		
	//	Boolean isFirstRODApproved = rodService.getStatusOfFirstROD(rodDTO.getClaimDTO().getKey(),reimbursement);
		
		Boolean isBillEntrySubmitted = false;
		//if(isFirstRODApproved){
		        isBillEntrySubmitted = rodService.submitPABillEntryValues(rodDTO,SHAConstants.PA_LOB);
		        
		if(rodDTO.getDocumentDetails() != null && rodDTO.getDocumentDetails().getRodKey() != null){
			
			Reimbursement objReimbursement = rodService.getReimbursementObjectByKey(rodDTO.getDocumentDetails().getRodKey());
			if(objReimbursement != null){
				if(null != objReimbursement.getDocAcknowLedgement())
				{
					objReimbursement = rodService.updatePAProvisionAndClaimStatus(objReimbursement.getDocAcknowLedgement(),objReimbursement.getDocAcknowLedgement().getClaim(),rodDTO,objReimbursement,SHAConstants.BILL_ENTRY_PA);
				}
				
			dbCalculationService.updateProvisionAmount(objReimbursement.getKey(), objReimbursement.getClaim().getKey());
			String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
			if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
				try {
					
					/*if(((null != rodDTO.getDocumentDetails().getHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationFlag())) && (null != rodDTO.getDocumentDetails().getPreHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPreHospitalizationFlag()))
							&& (null != rodDTO.getDocumentDetails().getPostHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPostHospitalizationFlag())) && (null != rodDTO.getDocumentDetails().getPartialHospitalizationFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getPartialHospitalizationFlag()))	
							 && (null != rodDTO.getDocumentDetails().getLumpSumAmountFlag() && ("N").equalsIgnoreCase(rodDTO.getDocumentDetails().getLumpSumAmountFlag())) && ((null != rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCashFlag() && ("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCashFlag()))
							 || (null != rodDTO.getDocumentDetails().getAddOnBenefitsPatientCareFlag() && ("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getAddOnBenefitsPatientCareFlag()))))*/
					if((null != rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCashFlag() && ("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCashFlag()))
							 || (null != rodDTO.getDocumentDetails().getAddOnBenefitsPatientCareFlag() && ("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getAddOnBenefitsPatientCareFlag()))
						) {
						Hospitals hospitalDetailsByKey = hospitalService.getHospitalDetailsByKey(objReimbursement.getClaim().getIntimation().getHospital());
					//	String provisionAmtInput = SHAUtils.getProvisionAmtInput(objReimbursement.getClaim(), hospitalDetailsByKey.getName(), String.valueOf(objReimbursement.getCurrentProvisionAmt().longValue()));
						if(null != rodDTO.getCurrentProvisionAmount())
						{
							String provisionAmtInput = SHAUtils.getProvisionAmtInput(objReimbursement.getClaim(), hospitalDetailsByKey.getName(), String.valueOf(rodDTO.getCurrentProvisionAmount().longValue()));
							apiService.updateProvisionAmountToPremia(provisionAmtInput);
							dbCalculationService.updateProvisionAmountForPANonHealth(objReimbursement.getKey(), objReimbursement.getClaim().getKey());
						}
						else
						{
							String provisionAmtInput = SHAUtils.getProvisionAmtInput(objReimbursement.getClaim(), hospitalDetailsByKey.getName(), String.valueOf(objReimbursement.getCurrentProvisionAmt().longValue()));
							apiService.updateProvisionAmountToPremia(provisionAmtInput);
							dbCalculationService.updateProvisionAmountForPANonHealth(objReimbursement.getKey(), objReimbursement.getClaim().getKey());
						}
					}

				} catch(Exception e) {
					
				}
			}
		
			}
			
		   }
		
	  // }
		if(isBillEntrySubmitted)
		{
			view.buildSuccessLayout();
		}
		else if((SHAConstants.YES_FLAG).equalsIgnoreCase(rodDTO.getDocumentDetails().getPaymentCancellationNeededFlag()))
		{
			view.buildFailureLayout("Payment cancellation is pending. Please intiate payment cancellation request in premia to proceed to further");
		}
		else
		{
			billClassification = new StringBuffer();
			if(("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationFlag()))
			{
				billClassification.append("Hospitalization").append(" ");
			}
			if(("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getPartialHospitalizationFlag()))
			{
				billClassification.append("PartialHospitalization").append(" ");
			}
			if(("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getPreHospitalizationFlag()))
			{
				billClassification.append("PreHospitalization").append(" ");
			}
			if(("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getPostHospitalizationFlag()))
			{
				billClassification.append("PostHospitalization").append(" ");
			}
			if(("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getLumpSumAmountFlag()))
			{
				billClassification.append("LumpSumAmount").append(" ");
			}
			if(("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationRepeatFlag()))
			{
				billClassification.append("HospitalizationRepeat").append(" ");
			}
			if(("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCashFlag()))
			{
				billClassification.append("HospitalCash").append(" ");
			}
			if(("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getAddOnBenefitsPatientCareFlag()))
			{
				billClassification.append("PatientCare").append(" ");
			}
			view.buildFailureLayout(billClassification.toString());
		}
	}
	
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(PA_BILL_ENTRY_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(PA_BILL_ENTRY_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}
	
}
