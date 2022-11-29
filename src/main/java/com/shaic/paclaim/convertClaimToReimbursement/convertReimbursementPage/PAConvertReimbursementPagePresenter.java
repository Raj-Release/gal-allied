package com.shaic.paclaim.convertClaimToReimbursement.convertReimbursementPage;

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
import com.shaic.claim.registration.convertClaim.search.SearchConvertClaimTableDto;
import com.shaic.domain.ClaimService;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.newcode.wizard.dto.ConvertClaimDTO;
import com.vaadin.v7.data.util.BeanItemContainer;



@ViewInterface(PAConvertReimbursementPageView.class)
public class PAConvertReimbursementPagePresenter extends AbstractMVPPresenter<PAConvertReimbursementPageView> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7640561472049224002L;

	public static final String SUBMIT_EVENT = "submit Event for convert to reimbursement PA";
	
	public static final String CONVERSION_REIMBURSEMENT="Convert to Reimbursement for out of process PA";
	
	@EJB
	private ReimbursementService reimbursementService;
	
	public static final String VALIDATE_CONVERT_CLAIM_USER_RRC_REQUEST = "convert_claim_user_rrc_request_for_outOfProcess_PA";
	
	public static final String CONVERT_CLAIM_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "convert_claim_load_rrc_request_drop_down_values_for_outOfProcess_PA";
	
	public static final String CONVERT_CLAIM_SAVE_RRC_REQUEST_VALUES = "convert_claim_query_save_rrc_request_values_for_outOfProcess_PA";

	public static final String PA_CONVERT_CLAIM_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "pa_convert_claim_load_rrc_request_sub_category_values";

	public static final String PA_CONVERT_CLAIM_LOAD_RRC_REQUEST_SOURCE_VALUES = "pa_convert_claim_load_rrc_request_source_values";

	
	@EJB
	private  ClaimService claimService;
	
	@EJB
	private MasterService masterService;
	
	
	
	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_CONVERT_CLAIM_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	

	public void saveRRCRequestValues(@Observes @CDIEvent(CONVERT_CLAIM_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_CASHLESS);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(CONVERT_CLAIM_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	/**
	 * Added for RRC Ends
	 * */
	
	public void setUpReference(@Observes @CDIEvent(SUBMIT_EVENT) final ParameterDTO parameters)
	{
		ConvertClaimDTO bean=(ConvertClaimDTO)parameters.getPrimaryParameter();
		
		SearchConvertClaimTableDto searchFormDto=(SearchConvertClaimTableDto) parameters.getSecondaryParameter(0,SearchConvertClaimTableDto.class);

		Boolean result=claimService.submitConvertToReimbursement(bean,"submit",searchFormDto);
		
		if(result){
			view.result();
		}
	}
	public void setUpconversion(@Observes @CDIEvent(CONVERSION_REIMBURSEMENT) final ParameterDTO parameters)
	{
		
		ConvertClaimDTO bean=(ConvertClaimDTO)parameters.getPrimaryParameter();
		
		SearchConvertClaimTableDto searchFormDto=(SearchConvertClaimTableDto) parameters.getSecondaryParameter(0,SearchConvertClaimTableDto.class);
				
		Boolean result=claimService.submitPAConvertToReimbursement(bean, "Conversion",searchFormDto);
		
		if(result){
			view.result();
		}
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(PA_CONVERT_CLAIM_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(PA_CONVERT_CLAIM_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}

}
