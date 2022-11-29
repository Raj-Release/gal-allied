package com.shaic.claim.policy.search.ui.opsearch;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.arch.utils.Props;
import com.vaadin.annotations.Theme;
import com.vaadin.v7.data.Property.ValueChangeEvent;
@Theme(Props.THEME_NAME)
public class OPExpiredPolicyClaimViewImpl  extends AbstractMVPView implements OPExpiredPolicyClaimView{

	private static final long serialVersionUID = 1L;

	@Inject
	private Instance<OPExpiredPolicyClaimUI> expiredPolicyIntimationComponent;


	private OPExpiredPolicyClaimUI expiredPolicyIntimationComponentObj;


	@PostConstruct
	public void initView() {
		showSearchPolicy();
	}

	public void searchSubmit() {
		if(expiredPolicyIntimationComponentObj.validateFields()){
			expiredPolicyIntimationComponentObj.showTable();
		}
	}


	@Override
	public void resetAlltheValues() {
		expiredPolicyIntimationComponentObj.resetAlltheValues();
	}

	@Override
	public void showSearchPolicy() {
		addStyleName("view");
		setSizeFull();
		expiredPolicyIntimationComponentObj = expiredPolicyIntimationComponent.get();
		setCompositionRoot(expiredPolicyIntimationComponentObj);
		setVisible(true);
	}

	@Override
	public void resetView() {

		if (expiredPolicyIntimationComponentObj != null ){
			expiredPolicyIntimationComponentObj.init();
		}
	}
}

