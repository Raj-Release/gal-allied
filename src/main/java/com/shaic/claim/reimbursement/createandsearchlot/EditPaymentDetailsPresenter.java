package com.shaic.claim.reimbursement.createandsearchlot;

import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;


@ViewInterface(EditPaymentDetails.class)
public class EditPaymentDetailsPresenter extends AbstractMVPPresenter<EditPaymentDetails > {
	
	public static final String LOT_SETUP_IFSC_DETAILS = "Lot_Setup_ifsc_details";
	
	public static final String CLOSE_EDIT_POPUP = "close_edit_popup";
	
	public void setupIFSCDetails(
			@Observes @CDIEvent(LOT_SETUP_IFSC_DETAILS) final ParameterDTO parameters) {
		ViewSearchCriteriaTableDTO viewSearchCriteriaDTO = (ViewSearchCriteriaTableDTO) parameters.getPrimaryParameter();
		EditPaymentDetailsView view = (EditPaymentDetailsView) parameters.getSecondaryParameter(0, EditPaymentDetailsView.class);
		//com.vaadin.ui.Window popup = (com.vaadin.ui.Window) parameters.getSecondaryParameter(1,com.vaadin.ui.Window.class);
		view.setUpIFSCDetails(viewSearchCriteriaDTO,view);
	}
	
	@SuppressWarnings({ "deprecation" })
	public void closePaymentPopup(@Observes @CDIEvent(CLOSE_EDIT_POPUP) final ParameterDTO parameters) {
		com.vaadin.ui.Window popup = (com.vaadin.ui.Window) parameters.getPrimaryParameter();
		popup.close();
	}
	
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
