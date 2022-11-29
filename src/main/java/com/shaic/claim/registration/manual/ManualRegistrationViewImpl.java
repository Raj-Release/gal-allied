package com.shaic.claim.registration.manual;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;



@SuppressWarnings("serial")
public class ManualRegistrationViewImpl extends AbstractMVPView implements ManualRegisterView {

	
	 @Inject
	 private Instance<ManualRegisterUI> claimRegister;
	 
	 
	@PostConstruct
	protected void initView() {
		 	addStyleName("view");
	        setSizeFull();
	        claimRegister.get().init();
	        setCompositionRoot(claimRegister.get());
	}
		
	public void showClaimRegistrationView() {
		
	}

	/*@Override
	public void showClaimRegistrationTable(List<HumanTask> tasks) {
		claimRegister.get().showTable(tasks);
	}*/
	
}
