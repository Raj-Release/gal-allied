package com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search;

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
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(PAAcknowledgeDocumentWizardView.class)
public class PAAcknowledgeDocumentWizardPresenter extends AbstractMVPPresenter<PAAcknowledgeDocumentWizardView> {

public static final String SUBMIT_ACKNOWLEDGE_DOC_RECEIVED = "submit_acknowledge_doc_received_PA";
	
	public static final String VALIDATE_ACK_DOC_REC_USER_RRC_REQUEST = "validate_ack_doc_rec_user_rrc_request_PA";
	
	public static final String ACK_DOC_REC_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "ack_doc_rec_load_rrc_request_drop_down_values_PA";
	
	public static final String ACK_DOC_REC_SAVE_RRC_REQUEST_VALUES = "ack_doc_rec_save_rrc_request_values_PA";

	public static final String PA_ACK_DOC_REC_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "pa_ack_doc_rec_load_rrc_request_sub_category_values";

	public static final String PA_ACK_DOC_REC_LOAD_RRC_REQUEST_SOURCE_VALUES = "pa_ack_doc_rec_load_rrc_request_source_values";

	
	@EJB
	private AcknowledgementDocumentsReceivedService ackDocReceivedService;
	

	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private APIService apiService;
	
	@EJB
	private ClaimService claimService;
	
	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_ACK_DOC_REC_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	

	public void saveRRCRequestValues(@Observes @CDIEvent(ACK_DOC_REC_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_REIMBURSEMENT);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(ACK_DOC_REC_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	/**
	 * Added for RRC Ends
	 * */
	
	
	@SuppressWarnings("static-access")
	public void submitWizard(
			@Observes @CDIEvent(SUBMIT_ACKNOWLEDGE_DOC_RECEIVED) final ParameterDTO parameters) {
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		rodDTO = ackDocReceivedService.submitPAAckDocReceived(rodDTO);
		Hospitals hospitalDetailsByKey = hospitalService.getHospitalDetailsByKey(rodDTO.getClaimDTO().getNewIntimationDto().getHospitalDto().getKey());
		Claim claim = claimService.getClaimByClaimKey(rodDTO.getClaimDTO().getKey());
		if((ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(rodDTO.getClaimDTO().getClaimTypeValue()) && 
				(( null != rodDTO.getDocumentDetails().getHospitalizationFlag() && ("Y").equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationFlag())) 
				  ))
		{
			try{
			//String provisionAmtInput = SHAUtils.getProvisionAmtInput(claim, hospitalDetailsByKey.getName(), String.valueOf(claim.getCurrentProvisionAmount().longValue()));
				if(null != rodDTO.getPaProvisionAmtToPremia())
				{
					String provisionAmtInput = SHAUtils.getProvisionAmtInput(claim, hospitalDetailsByKey.getName(), String.valueOf(rodDTO.getPaProvisionAmtToPremia()));
					apiService.updateProvisionAmountToPremia(provisionAmtInput);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		PremiaService.getInstance().getPolicyLock(claim, hospitalDetailsByKey.getHospitalCode());
		
		view.buildSuccessLayout();
	}

	
	

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(PA_ACK_DOC_REC_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(PA_ACK_DOC_REC_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}

}
