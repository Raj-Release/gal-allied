package com.shaic.domain;

import java.util.Iterator;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class OMPViewClaimantDetailsPageUI extends ViewComponent{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TextField txtProposerName;

	private TextField txtAddress;
	
	private TextField txtCountry;
	
	private TextField txtState;
	
	private TextField txtCity;
	
	private TextField txtPincode;
	
	private Policy policy;

	private PolicyService policyService;
	
	private MasterService masterService;
	
	public void setPolicyServiceAndPolicy(PolicyService a_policyService, Policy policy,
			MasterService masterService) {
		//super("View Policy Details");
		this.policyService = a_policyService;
		this.policy = policy;
		this.masterService = masterService;
	}
	
	public void init(/*OMPIntimation intimation*/Policy policy){
		Policy apolicy = policyService.getPolicy(policy.getPolicyNumber());
		
		if(apolicy!= null){
		txtProposerName = new TextField("Proposer Name");
		txtProposerName.setValue(apolicy.getProposerFirstName());
		
		txtAddress = new TextField("Address");
		txtAddress.setValue(apolicy.getProposerAddress());
		
		txtCountry = new TextField("Country");
		txtCountry.setValue("");
		
		txtState = new TextField("State");
		txtState.setValue(apolicy.getProposerState());
		
		txtCity = new TextField("City");
		txtCity.setValue(apolicy.getProposerCity());
		
		txtPincode = new TextField("Pincode");
		txtPincode.setValue(String.valueOf(apolicy.getProposerPinCode()));
		
		FormLayout mainForm = new FormLayout(txtProposerName,txtAddress,txtCountry,txtState,txtCity,txtPincode);
		mainForm.setSpacing(true);
		HorizontalLayout mainHor = new HorizontalLayout(mainForm);
		Panel mainPanel = new Panel(mainHor);
		mainPanel.setCaption("");
		mainPanel.addStyleName("layoutDesign");
		
        setReadOnly(mainForm,true);
		
        setCompositionRoot(mainPanel);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	private void setReadOnly(FormLayout a_formLayout, boolean readOnly) {
		Iterator<Component> formLayoutLeftComponent = a_formLayout
				.getComponentIterator();
		while (formLayoutLeftComponent.hasNext()) {
			Component c = formLayoutLeftComponent.next();
			if (c instanceof com.vaadin.v7.ui.AbstractField) {
				if(c instanceof TextField){
					TextField field = (TextField) c;
					field.setWidth("250px");
					field.setNullRepresentation("");
					field.setReadOnly(readOnly);
					field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				}else if(c instanceof TextArea){
					
					TextArea field = (TextArea) c;
					field.setWidth("250px");
					field.setNullRepresentation("");
					field.setReadOnly(readOnly);
					field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				}
				
			}
		}
	}

}
