/**
 * 
 */
package com.shaic.claim.reimbursement.rrc.detailsPage;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.reimbursement.rrc.services.ProcessRRCRequestService;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author ntv.vijayar
 *
 */
@ViewInterface(ProcessRRCRequestDataExtractionWizard.class)
public class ProcessRRCRequestPresenter extends AbstractMVPPresenter<ProcessRRCRequestDataExtractionWizard>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SUBMIT_RRC_REQUEST_FOR_PROCESS = "submit_rrc_request_for_process";
	
	public static final String PROCESS_RRC_SUB_CATEGORY_VALUES = "process_rrc_sub_category_values";
	
	public static final String PROCESS_RRC_SOURCE_VALUES = "process_rrc_source_values";
	
	@EJB
	private ProcessRRCRequestService rrcRequestService;

	
	
	@SuppressWarnings("static-access")
	public void submitWizard(
			@Observes @CDIEvent(SUBMIT_RRC_REQUEST_FOR_PROCESS) final ParameterDTO parameters) {
		RRCDTO rrcDTO = (RRCDTO) parameters.getPrimaryParameter();
		//String strRRCrequestNo = rrcRequestService.submitProcessRRCRequest(rrcDTO);
		rrcDTO = rrcRequestService.submitProcessRRCRequest(rrcDTO);
		String strRRCrequestNo = rrcDTO.getRrcRequestNo();
		String successMessage = "";
		if(SHAConstants.RRC_STATUS_PROCESS.equalsIgnoreCase(rrcDTO.getRrcStatus()))
		{
			//String successMessage = "";
			if(null != strRRCrequestNo)
			{
				 successMessage = "RRC RequestNo" + " " + strRRCrequestNo + " Successfully submitted for processing !!!";
			}
			else {
				 successMessage = "Failure occured while submitting RRC Request.Please contact administrator.!!!";
			}
			view.buildSuccessLayout(successMessage);
		}
		else if(SHAConstants.RRC_STATUS_ON_HOLD.equalsIgnoreCase(rrcDTO.getRrcStatus()))
		{
			
			if(null != strRRCrequestNo)
			{
				 successMessage = "RRC RequestNo" + " " + strRRCrequestNo + " is kept on hold !!!";
				 if(null != rrcDTO.getIsHoldRepeated() && rrcDTO.getIsHoldRepeated())
					{
					 successMessage = "RRC RequestNo" + " " + strRRCrequestNo + " is already kept on hold !!!";
					}
			}
			else {
				 successMessage = "Failure occured while submitting RRC Request.Please contact administrator.!!!";
			}
			view.buildSuccessLayout(successMessage);
		}
	}
	

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(PROCESS_RRC_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = rrcRequestService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(PROCESS_RRC_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = rrcRequestService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}
	

}
