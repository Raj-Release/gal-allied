package com.shaic.claim.policy.search.ui.opsearch;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.arch.utils.Props;
import com.vaadin.annotations.Theme;
import com.vaadin.v7.data.Property.ValueChangeEvent;
@Theme(Props.THEME_NAME)
public class OPRegisterClaimPolicyViewImpl  extends AbstractMVPView implements OPRegisterClaimPolicyView{

	private static final long serialVersionUID = 1L;

	@Inject
	private Instance<OPRegisterClaimPolicyUI> createIntimationComponent;


	private OPRegisterClaimPolicyUI createIntimationComponentObj;


	@PostConstruct
	public void initView() {
		showSearchPolicy();
		//resetView();
	}

	public void searchSubmit() {
		if(createIntimationComponentObj.validateFields()){
			createIntimationComponentObj.showTable();
		}
	}

	@Override
	public void showLayoutBasedOnSelectedItem(ValueChangeEvent valueChangeEvent) {
		createIntimationComponentObj.showLayoutBasedOnSelection(valueChangeEvent);
	}

	@Override
	public void resetAlltheValues() {
		createIntimationComponentObj.resetAlltheValues();
	}

	@Override
	public void showSearchPolicy() {
		addStyleName("view");
		setSizeFull();
		createIntimationComponentObj = createIntimationComponent.get();
		setCompositionRoot(createIntimationComponentObj);
		setVisible(true);
	}

	@Override
	public void resetView() {

		if (createIntimationComponentObj != null ){
			createIntimationComponentObj.init();
		}
		//	createIntimationComponent.get().refresh();
	}
}
