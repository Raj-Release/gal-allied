package com.shaic.claim.reimbursement.rrc.services;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.common.APIService;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(InitiateRRCRequestWizardView.class)
public class InitiateRRCRequestWizardPresenter extends AbstractMVPPresenter<InitiateRRCRequestWizardView>{

	

	
	public static final String VALIDATE_SAVE_INITIATE_USER_RRC_REQUEST = "validate_initiate_rrc_user_rrc_request";
	
	public static final String SAVE_INITIATE__RRC_REQUEST_DROP_DOWN_VALUES = "initiate_rrc_request_drop_down_values";
	
	public static final String SAVE_INITIATE_RRC_REQUEST_VALUES = "Initiate_rrc";
	
	public static final String SHOW_INITIATE_RRC_PAGE = "show_initiate_rrc_page";

	public static final String SAVE_INITIATE_RRC_REQUEST_SUB_CATEGORY_VALUES = "save_initiate_rrc_request_sub_category_values";

	public static final String SAVE_INITIATE_RRC_REQUEST_SOURCE_VALUES = "save_initiate_rrc_request_source_values";

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
	
	
	
	public void showInitiateRRCPage(@Observes @CDIEvent(SHOW_INITIATE_RRC_PAGE) final ParameterDTO parameters){
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		
		
	}
		
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(SAVE_INITIATE__RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
		

	public void saveRRCRequestValues(@Observes @CDIEvent(SAVE_INITIATE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();

		String rrcRequestNo = null;
		if(preauthDTO.getClaimDTO().getClaimType().equals(SHAConstants.CLAIMREQUEST_REIMBURSEMENT))
		{
			rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_REIMBURSEMENT);
		}
		else
		{
			rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_CASHLESS);
		}
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(SAVE_INITIATE_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(SAVE_INITIATE_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}
}
