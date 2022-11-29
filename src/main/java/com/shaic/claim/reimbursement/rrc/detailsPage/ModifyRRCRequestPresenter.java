



/**
 * 
 */
package com.shaic.claim.reimbursement.rrc.detailsPage;

/**
 * @author ntv.vijayar
 *
 */

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.reimbursement.rrc.services.ModifyRRCRequestService;
import com.shaic.claim.reimbursement.rrc.services.ProcessRRCRequestService;
import com.vaadin.v7.data.util.BeanItemContainer;


@ViewInterface(ModifyRRCRequestDataExtractionWizard.class)
public class ModifyRRCRequestPresenter extends AbstractMVPPresenter<ModifyRRCRequestDataExtractionWizard>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SUBMIT_RRC_REQUEST_FOR_MODIFY = "submit_rrc_request_for_modify";
	
public static final String MODIFY_RRC_SUB_CATEGORY_VALUES = "modify_rrc_sub_category_values";
	
	public static final String MODIFY_RRC_SOURCE_VALUES = "modify_rrc_source_values";
	
	@EJB
	private ModifyRRCRequestService modifyRRCService;
	
	@EJB
	private ProcessRRCRequestService processRRCService;
	
	
	@SuppressWarnings("static-access")
	public void submitWizard(
			@Observes @CDIEvent(SUBMIT_RRC_REQUEST_FOR_MODIFY) final ParameterDTO parameters) {
		RRCDTO rrcDTO = (RRCDTO) parameters.getPrimaryParameter();
		String strRRCrequestNo = modifyRRCService.submitModifyRRCRequest(rrcDTO);
		view.buildSuccessLayout(strRRCrequestNo);
	}
	

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(MODIFY_RRC_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = processRRCService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(MODIFY_RRC_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = processRRCService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}
	

}
