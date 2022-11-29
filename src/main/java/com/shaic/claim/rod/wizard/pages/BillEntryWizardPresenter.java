/**
 * 
 */
package com.shaic.claim.rod.wizard.pages;

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

/**
 * @author ntv.vijayar
 *
 */
@ViewInterface(BillEntryWizardView.class)
public class BillEntryWizardPresenter extends AbstractMVPPresenter<BillEntryWizardViewImpl>
{
	
	private static final long serialVersionUID = 1800989028816886291L;

	public static final String SUBMIT_BILL_ENTRY_DETAILS = "submit_bill_entry_details";
	
	public static final String GET_SEC_COVER = "bill_entry_get_sec_cover";

	public static final String GET_SUB_COVER = "bill_entry_get_sub_cover";
	
	@EJB
	private CreateRODService rodService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	

	//public static final String SUBMIT_ACKNOWLEDGE_DOC_RECEIVED = "submit_acknowledge_doc_received";
	
	public static final String VALIDATE_BILL_ENTRY_USER_RRC_REQUEST = "validate_bill_entry_user_rrc_request";
	
	public static final String BILL_ENTRY_RRC_REQUEST_DROP_DOWN_VALUES = "bill_entry_load_rrc_request_drop_down_values";
	
	public static final String BILL_ENTRY_SAVE_RRC_REQUEST_VALUES = "bill_entry_save_rrc_request_values";

	public static final String BILL_ENTRY_RRC_REQUEST_SUB_CATEGORY_VALUES = "bill_entry_rrc_request_sub_category_values";
	
	public static final String BILL_ENTRY_RRC_REQUEST_SOURCE_VALUES = "bill_entry_rrc_request_source_values";
	 
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
	
	public void getSecCover(
			@Observes @CDIEvent(GET_SEC_COVER) final ParameterDTO parameters) {
		Long sectionKey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue> coverContainer = masterService
				.getCoverList(sectionKey);

		view.setCoverList(coverContainer);
	}
	
	public void getSubCover(
			@Observes @CDIEvent(GET_SUB_COVER) final ParameterDTO parameters) {
		Long coverKey = (Long) parameters.getPrimaryParameter();
		BeanItemContainer<SelectValue> subCoverContainer = masterService
				.getSubCoverList(coverKey);

		view.setSubCoverList(subCoverContainer);
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
		Boolean isFirstRODApproved = false;
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		if(rodDTO != null && rodDTO.getTotalClaimedAmount() != null){
		rodDTO.setCurrentProvisionAmount(rodDTO.getTotalClaimedAmount());
		}
		
		Reimbursement reimbursement = rodService.getReimbursementObjectByKey(rodDTO.getDocumentDetails().getRodKey());
		
		if(null != rodDTO && null != rodDTO.getHospitalizationFlag() && !(SHAConstants.YES_FLAG).equalsIgnoreCase(rodDTO.getHospitalizationFlag()) &&
				(rodDTO.getDocumentDetails().getHospitalizationFlag() !=null && !(SHAConstants.YES_FLAG).equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationFlag())))
		{
			isFirstRODApproved = rodService.getStatusOfFirstROD(rodDTO.getClaimDTO().getKey(),reimbursement);
		}
		else
		{
			isFirstRODApproved = true;
		}
		//added for new product
		if (rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null &&  rodDTO.getClaimDTO().getNewIntimationDto().getPolicy()
				.getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
				|| rodDTO.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
					rodDTO.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)) {
			isFirstRODApproved = true;
		}
		//Boolean isFirstRODApproved = true;
		//Boolean isFirstRODApproved = true;
		Boolean isBillEntrySubmitted = false;
		if(isFirstRODApproved){
		        isBillEntrySubmitted = rodService.submitBillEntryValues(rodDTO);
		if(rodDTO.getDocumentDetails() != null && rodDTO.getDocumentDetails().getRodKey() != null){
			
			
			Reimbursement objReimbursement = rodService.getReimbursementObjectByKey(rodDTO.getDocumentDetails().getRodKey());
			if(objReimbursement != null){
//			dbCalculationService.updateProvisionAmount(reimbursement.getKey(), reimbursement.getClaim().getKey());
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
							dbCalculationService.updateProvisionAmount(objReimbursement.getKey(), objReimbursement.getClaim().getKey());
						}
						else
						{
							String provisionAmtInput = SHAUtils.getProvisionAmtInput(objReimbursement.getClaim(), hospitalDetailsByKey.getName(), String.valueOf(objReimbursement.getCurrentProvisionAmt().longValue()));
							apiService.updateProvisionAmountToPremia(provisionAmtInput);
							dbCalculationService.updateProvisionAmount(objReimbursement.getKey(), objReimbursement.getClaim().getKey());
						}
					}

				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		
			}
			
		   }
		
	   }
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
			@Observes @CDIEvent(BILL_ENTRY_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(BILL_ENTRY_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}
	
}
