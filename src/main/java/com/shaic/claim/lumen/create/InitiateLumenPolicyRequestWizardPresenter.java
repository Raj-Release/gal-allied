package com.shaic.claim.lumen.create;

import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@SuppressWarnings("serial")
@ViewInterface(InitiateLumenPolicyRequestWizard.class)
public class InitiateLumenPolicyRequestWizardPresenter extends AbstractMVPPresenter<InitiateLumenPolicyRequestWizard>{
	
	public static final String POLICY_CANCEL_REQUEST = "policy_cancel_lumen_request_submit";
	public static final String POLICY_SUBMIT_REQUEST = "policy_lumen_request_submit";
	
	@Override
	public void viewEntered() {
		System.out.println("InitiateLumenRequestWizardPresenter called....");
		
	}
	public void cancelLumenRequest(@Observes @CDIEvent(POLICY_CANCEL_REQUEST) final ParameterDTO parameters) {
		view.cancelLumenRequest();
	}
	
	public void submitLumenRequest(@Observes @CDIEvent(POLICY_SUBMIT_REQUEST) final ParameterDTO parameters) {
		view.submitLumenRequest();
	}
}
