/**
 * 
 */
package com.shaic.claims.reibursement.addaditionaldocuments;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author ntv.srikanthp
 *
 */
@ViewInterface(AddAditionalDocumentsView.class)
public class AddAditionalDocumentsPresenter extends AbstractMVPPresenter<AddAditionalDocumentsViewImpl>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SUBMIT_BILL_ENTRY_DETAILS = "submit_bill_entry_details_add_additional_documents";
	
	public static final String VALIDATE_ADD_ADDITIONAL_DOC_REC_USER_RRC_REQUEST = "validate_add_additional_doc_user_rrc_request";
	
	public static final String ADD_ADDITIONAL_DOC_REC_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "add_additional_doc_load_rrc_request_drop_down_values";
	
	public static final String ADD_ADDITIONAL_DOC_REC_SAVE_RRC_REQUEST_VALUES = "add_additional_doc_save_rrc_request_values";
	
	public static final String ADD_ADDITIONAL_DOC_REC_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "add_additional_doc_rec_load_rrc_request_sub_category_values";
	
	public static final String ADD_ADDITIONAL_DOC_REC_LOAD_RRC_REQUEST_SOURCE_VALUES = "add_additional_doc_rec_load_rrc_request_source_values";
	 
	
	@EJB
	private CreateRODServiceForAddAdditionalDocuments rodService;
		
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private MasterService masterService;


	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

	
	public void submitWizard(
			@Observes @CDIEvent(SUBMIT_BILL_ENTRY_DETAILS) final ParameterDTO parameters) {
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		rodService.submitBillEntryValues(rodDTO);
		view.buildSuccessLayout();
	}
	
	
	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_ADD_ADDITIONAL_DOC_REC_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	

	public void saveRRCRequestValues(@Observes @CDIEvent(ADD_ADDITIONAL_DOC_REC_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_REIMBURSEMENT);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(ADD_ADDITIONAL_DOC_REC_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	/**
	 * Added for RRC Ends
	 * */
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(ADD_ADDITIONAL_DOC_REC_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(ADD_ADDITIONAL_DOC_REC_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}
	
}
