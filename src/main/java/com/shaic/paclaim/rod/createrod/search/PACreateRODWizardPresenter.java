package com.shaic.paclaim.rod.createrod.search;

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
import com.shaic.domain.BankMaster;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.ui.Panel;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(PACreateRODWizardView.class)
public class PACreateRODWizardPresenter extends AbstractMVPPresenter<PACreateRODWizardView>{

	
public static final String SUBMIT_ROD = "submit_ROD_PA";
	
	public static final String SHOW_PAYMENT_LETTER_POPUP = "show_payment_letter_popup_PA";
	
	public static final String UPDATE_ROD = "Update_Rod_PA";
	
	@EJB
	private CreateRODService rodService;
	
	@EJB
	private DBCalculationService dbcalculationService;
	

	
	
	public static final String SUBMIT_ACKNOWLEDGE_DOC_RECEIVED = "submit_acknowledge_doc_received_PA";
	
	public static final String VALIDATE_CREATE_ROD_USER_RRC_REQUEST = "validate_create_rod_user_rrc_request_PA";
	
	public static final String CREATE_ROD_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "create_rod_load_rrc_request_drop_down_values_PA";
	
	public static final String CREATE_ROD_SAVE_RRC_REQUEST_VALUES = "create_rod_save_rrc_request_values_PA";

	public static final String PA_CREATE_ROD_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "pa_create_rod_load_rrc_request_sub_category_values";

	public static final String PA_CREATE_ROD_LOAD_RRC_REQUEST_SOURCE_VALUES = "pa_create_rod_load_rrc_request_source_values";

	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private MasterService masterService;
	
	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_CREATE_ROD_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	

	public void saveRRCRequestValues(@Observes @CDIEvent(CREATE_ROD_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_REIMBURSEMENT);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(CREATE_ROD_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	/**
	 * Added for RRC Ends
	 * */

	@EJB
	private APIService apiService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private HospitalService hospitalService;

	
	@SuppressWarnings("static-access")
	public void submitWizard(
			@Observes @CDIEvent(SUBMIT_ROD) final ParameterDTO parameters) {
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		String message = null;
		Boolean isBankTransfer = true;
		/**
		 * If ifsc code is not present in bank master ,
		 * then rod creation is restricted. This validation was added
		 * since there was a sync issue in ifsc code present in bank master
		 * and ifsc code present in hospital master. Hence as per sathish sir
		 * advise, this validation is added.
		 * */
		if(rodDTO.getDocumentDetails().getPaymentModeFlag().equals(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER)
				&& rodDTO.getDocumentDetails().getAccidentOrDeath() != null 
				&& rodDTO.getDocumentDetails().getAccidentOrDeath())
		{
			BankMaster bankMaster = rodService.validateIFSCCode( rodDTO.getDocumentDetails().getIfscCode());
			if(null != bankMaster)
			{
				rodDTO.getDocumentDetails().setBankId(bankMaster.getKey());
			}
			else
			{
				isBankTransfer = false;
			}
		}
		if(isBankTransfer)
		{
			if(!rodService.validatePARODForCashlessClaim(rodDTO))
			{
			/**
			 * The above validation needs to be modified for PA Claim with respect to
			 * amount claimed value -- Vijay to be intimated on this.
			 * */
			//if(true)
			//{
				Reimbursement reimbursement = rodService.submitPAROD(rodDTO);
				if(reimbursement != null){
					

					if(null != reimbursement.getDocAcknowLedgement())
					{
							reimbursement = rodService.updatePAProvisionAndClaimStatus(reimbursement.getDocAcknowLedgement(),reimbursement.getDocAcknowLedgement().getClaim(),rodDTO,reimbursement,SHAConstants.CREATE_ROD_PA);
					}
				
					dbcalculationService.updateProvisionAmountForPANonHealth(reimbursement.getKey(), reimbursement.getClaim().getKey());
					String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
					if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
						try {
							Hospitals hospitalDetailsByKey = hospitalService.getHospitalDetailsByKey(reimbursement.getClaim().getIntimation().getHospital());
							Claim claimByClaimKey = claimService.getClaimByClaimKey(reimbursement.getClaim().getKey());
							String provisionAmtInput = SHAUtils.getProvisionAmtInput(reimbursement.getClaim(), hospitalDetailsByKey.getName(), String.valueOf(claimByClaimKey.getCurrentProvisionAmount().longValue()));
							apiService.updateProvisionAmountToPremia(provisionAmtInput);
						} catch(Exception e) {
								e.printStackTrace();
						}
					}
				
					if(null != reimbursement && null != reimbursement.getClaim() && null != reimbursement.getClaim().getClaimType() && null != reimbursement.getClaim().getClaimType().getKey() 
							&& reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.DOC_RECEIVED_TYPE_HOSPITAL)
							&& ("Y").equalsIgnoreCase(reimbursement.getDocAcknowLedgement().getHospitalisationFlag()) && null != rodDTO.getDocToken())
					//if(null != rodDTO.getDocToken())
					{
						view.buildSuccessLayout(rodDTO.getDocToken());
					}
					else
					{
						view.buildSuccessLayout();
					}
			}
				else 
				{
					message = "Amount Claimed is less than preauth approved amount. Please downsize the preauth amount to proceed further.";
					view.buildFailureLayout(message);
				}
			}else
			{
				message = "Amount Claimed is less than preauth approved amount. Please downsize the preauth amount to proceed further.";
				view.buildFailureLayout(message);
			}
		}
		else
		{
			message = "IFSC code entered in payment details section is not present in Bank master table. Please update bank master table "
					+ "to create ROD";
			view.buildFailureLayout(message);
		}
		
	}
	
	
	private void showPaymentLetterPopup(@Observes @CDIEvent(SHOW_PAYMENT_LETTER_POPUP) final ParameterDTO parameters)
	{
		String docToken = (String) parameters.getPrimaryParameter();
		Panel panel = rodService.getPaymentLetterFromDMS(docToken);
		view.showDocViewPopup(panel);
		
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void updateWizard(
			@Observes @CDIEvent(UPDATE_ROD) final ParameterDTO parameters) {
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		
		rodService.updateRodForPA(rodDTO);
		view.buildSuccessLayoutForUpdateRod();
	}
	
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(PA_CREATE_ROD_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(PA_CREATE_ROD_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}

}
