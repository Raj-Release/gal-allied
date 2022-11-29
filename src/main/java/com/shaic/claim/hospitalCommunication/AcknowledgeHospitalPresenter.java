package com.shaic.claim.hospitalCommunication;

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
import com.shaic.claim.registration.ackhoscomm.search.SearchAcknowledgeHospitalCommunicationTableDTO;
import com.shaic.domain.HospitalService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.newcode.wizard.dto.HospitalAcknowledgeDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(HospitalView.class)
public class AcknowledgeHospitalPresenter extends AbstractMVPPresenter<HospitalView> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SUBMIT_EVENT = "submit_event";
	
	public static final String SET_DATA="set data into common carousel in Hospital Acknowledgement";
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private MasterService masterService;
	
	
	@EJB
	private ReimbursementService reimbursementService;
	
	public static final String VALIDATE_ACKNOWLEDGE_HOSPITAL_USER_RRC_REQUEST = "validate_acknowledge_hospital_user_rrc_request";
	
	public static final String ACKNOWLEDGE_HOSPITAL_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "acknowledge_hospital_load_rrc_request_drop_down_values";
	
	public static final String ACKNOWLEDGE_HOSPITAL_SAVE_RRC_REQUEST_VALUES = "acknowledge_hospital_save_rrc_request_values";

	public static final String ACKNOWLEDGE_HOSPITAL_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "acknowledge_hospital_load_rrc_request_sub_category_values";

	public static final String ACKNOWLEDGE_HOSPITAL_LOAD_RRC_REQUEST_SOURCE_VALUES = "acknowledge_hospital_load_rrc_request_source_values";
	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_ACKNOWLEDGE_HOSPITAL_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	

	public void saveRRCRequestValues(@Observes @CDIEvent(ACKNOWLEDGE_HOSPITAL_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_CASHLESS);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(ACKNOWLEDGE_HOSPITAL_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	/**
	 * Added for RRC Ends
	 * */
	
	public void saveHospital(@Observes @CDIEvent(SUBMIT_EVENT) final ParameterDTO parameters)
	{
		
	    HospitalAcknowledgeDTO hospitalDto=(HospitalAcknowledgeDTO)parameters.getPrimaryParameter();
	    
	    SearchAcknowledgeHospitalCommunicationTableDTO searchFormDto=(SearchAcknowledgeHospitalCommunicationTableDTO)parameters.getSecondaryParameter(0, SearchAcknowledgeHospitalCommunicationTableDTO.class);
	    
	    Boolean result=hospitalService.saveHospitalDetails(hospitalDto,searchFormDto);
	    
	    if(result){
	    	view.result();
	    }
	    
				
	}
	
	/*public void setDataReference(@Observes @CDIEvent(SUBMIT_EVENT) final ParameterDTO parameters)
	{
		
	}*/
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(ACKNOWLEDGE_HOSPITAL_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(ACKNOWLEDGE_HOSPITAL_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}
	

}
