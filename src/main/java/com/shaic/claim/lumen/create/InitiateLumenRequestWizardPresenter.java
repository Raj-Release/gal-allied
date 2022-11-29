package com.shaic.claim.lumen.create;

import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@SuppressWarnings("serial")
@ViewInterface(InitiateLumenRequestWizard.class)
public class InitiateLumenRequestWizardPresenter extends AbstractMVPPresenter<InitiateLumenRequestWizard>{
	
	public static final String CANCEL_REQUEST = "cancel_lumen_request_submit";
	public static final String SUBMIT_REQUEST = "lumen_request_submit";
	
	@Override
	public void viewEntered() {
		System.out.println("InitiateLumenRequestWizardPresenter called....");
		
	}
	public void cancelLumenRequest(@Observes @CDIEvent(CANCEL_REQUEST) final ParameterDTO parameters) {
		view.cancelLumenRequest();
	}
	
	public void submitLumenRequest(@Observes @CDIEvent(SUBMIT_REQUEST) final ParameterDTO parameters) {
		view.submitLumenRequest();
	}
}
