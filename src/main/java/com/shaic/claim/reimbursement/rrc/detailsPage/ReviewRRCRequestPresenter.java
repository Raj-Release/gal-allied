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
import com.shaic.claim.reimbursement.rrc.services.ProcessRRCRequestService;
import com.shaic.claim.reimbursement.rrc.services.ReviewRRCRequestService;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author ntv.vijayar
 *
 */
@ViewInterface(ReviewRRCRequestDataExtractionWizard.class)
public class ReviewRRCRequestPresenter extends AbstractMVPPresenter<ReviewRRCRequestDataExtractionWizard>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SUBMIT_RRC_REQUEST_FOR_REVIEW = "submit_rrc_request_for_review";
	
	public static final String REVIEW_RRC_SUB_CATEGORY_VALUES = "review_rrc_sub_category_values";
	
	public static final String REVIEW_RRC_SOURCE_VALUES = "review_rrc_source_values";
	
	@EJB
	private ReviewRRCRequestService reviewRRCService;

	@EJB
	private ProcessRRCRequestService processRRCService;
	
	@SuppressWarnings("static-access")
	public void submitWizard(
			@Observes @CDIEvent(SUBMIT_RRC_REQUEST_FOR_REVIEW) final ParameterDTO parameters) {
		RRCDTO rrcDTO = (RRCDTO) parameters.getPrimaryParameter();
		String strRRCrequestNo = reviewRRCService.submitReviewRRCRequest(rrcDTO);
		view.buildSuccessLayout(strRRCrequestNo);
	}
	

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(REVIEW_RRC_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = processRRCService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(REVIEW_RRC_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = processRRCService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}

}
