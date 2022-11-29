package com.shaic.claim.lumen.createinpopup;

import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@SuppressWarnings("serial")
@ViewInterface(PopupInitiateLumenRequestWizard.class)
public class PopupInitiateLumenRequestWizardPresenter extends AbstractMVPPresenter<PopupInitiateLumenRequestWizard>{
	
	public static final String POPUP_CANCEL_REQUEST = "popup_cancel_lumen_request_submit";
	public static final String POPUP_SUBMIT_REQUEST = "popup_lumen_request_submit";
	
	@Override
	public void viewEntered() {
		System.out.println("Popup InitiateLumenRequestWizardPresenter called....");
		
	}
	public void cancelLumenRequest(@Observes @CDIEvent(POPUP_CANCEL_REQUEST) final ParameterDTO parameters) {
		view.cancelLumenRequest();
	}
	
	public void submitLumenRequest(@Observes @CDIEvent(POPUP_SUBMIT_REQUEST) final ParameterDTO parameters) {
		view.submitLumenRequest();
	}
}
