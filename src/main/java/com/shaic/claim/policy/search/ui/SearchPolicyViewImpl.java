package com.shaic.claim.policy.search.ui;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.arch.utils.Props;
import com.vaadin.annotations.Theme;
import com.vaadin.v7.data.Property.ValueChangeEvent;

@Theme(Props.THEME_NAME)
public class SearchPolicyViewImpl extends AbstractMVPView implements SearchPolicyView {

	private static final long serialVersionUID = 2645510427614044958L;
	
	@Inject
    private Instance<SearchPolicyUI> createIntimationComponent;
    
    @PostConstruct
	public void initView() {
    	showSearchPolicy();
    }
    
	public void searchSubmit() {
		createIntimationComponent.get().showTable();
	}
    
	@Override
	public void showLayoutBasedOnSelectedItem(ValueChangeEvent valueChangeEvent) {
//		createIntimationComponent.get().showLayoutBasedOnSelection(valueChangeEvent);
	}
	
	@Override
	public void resetAllValues() {
		createIntimationComponent.get().resetAllValues();
	}

	@Override
	public void showSearchPolicy() {
		addStyleName("view");
        setSizeFull();
        setCompositionRoot(createIntimationComponent.get());
        setVisible(true);
	}

	@Override
	public void resetView() {
		if (createIntimationComponent.get() != null )
			createIntimationComponent.get().init();
	}

	@Override
	public void searchPolicyWithParameter(String policyNumber,
			String healthCardNumber) {
		if (createIntimationComponent.get() != null )
		{
			createIntimationComponent.get().searchPolicyWithParameter(policyNumber, healthCardNumber);
		}
		
	}
}
