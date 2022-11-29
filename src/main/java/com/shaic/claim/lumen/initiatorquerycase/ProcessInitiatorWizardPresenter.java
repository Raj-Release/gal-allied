package com.shaic.claim.lumen.initiatorquerycase;

import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@SuppressWarnings("serial")
@ViewInterface(ProcessInitiatorWizard.class)
public class ProcessInitiatorWizardPresenter extends AbstractMVPPresenter<ProcessInitiatorWizard>{
	
	public static final String CANCEL_INITIATOR_REQUEST = "cancel_initiator_submit";
	public static final String SUBMIT_INITIATOR_REQUEST = "initiator_submit";
	
	@Override
	public void viewEntered() {
		System.out.println("ProcessInitiatorWizardPresenter called....");
		
	}
	public void cancelLumenRequest(@Observes @CDIEvent(CANCEL_INITIATOR_REQUEST) final ParameterDTO parameters) {
		view.cancelLumenRequest();
	}
	
	public void submitLumenRequest(@Observes @CDIEvent(SUBMIT_INITIATOR_REQUEST) final ParameterDTO parameters) {
		view.submitLumenRequest();
	}
}
