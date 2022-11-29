package com.shaic.claim.registration.manual;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.arch.AbstractMVPNavigationPresenter;
import com.shaic.domain.menu.RegistrationBean;
import com.shaic.ims.bpm.claim.BPMClientContext;

@SuppressWarnings("serial")
@ViewInterface(ManualRegisterView.class)
public class ManualRegistrationPresenter extends AbstractMVPNavigationPresenter<ManualRegisterView>{

	
	@Inject
    private RegistrationBean registrationBean;

	private BPMClientContext clientTest;
	
    // CDI MVP includes a built-in CDI event qualifier @CDIEvent which
    // uses a String (method identifier) as it's member
    public static final String SEARCH_CLAIMREGISTER_TABLE_SUBMIT = "create_claimRegistarction_presenter_search_submit";
	
	@Override
	public void viewEntered() {
		
	}
	
//	 protected void showPolicyDetailsTable(
//	            @Observes @CDIEvent(SEARCH_CLAIMREGISTER_TABLE_SUBMIT) final ParameterDTO parameters) {
//		 	clientTest = new BPMClientTest();
//		 	List<HumanTask> tasks = clientTest.getHumanTask();
//	        view.showClaimRegistrationTable(tasks);
//	    }

}
