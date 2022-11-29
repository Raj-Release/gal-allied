package com.shaic.claim.OMPViewDetails.view;

import java.util.Iterator;
import java.util.List;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAUtils;
import com.shaic.domain.MasterService;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class OMPViewRiskDetailsUI extends ViewComponent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TextField txtPolicyNumber;

	private TextField txtRiskName;
	
	private TextField txtFromDate;
	
	private TextField txtToDate;
	
	private TextField txtSectionCode;
	
	private TextField txtCoverCode;
	
	private TextField txtSuminsured;
	
	private TextField txtRelationShip;
	
	private TextField txtSex;
	
	private TextField txtAge;
	
	private TextField txtDOb;
	
	private TextField txtPlaceOfVisit;
	
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
	
	
	public void init(Policy plicy,OMPIntimation intimation){
		
		txtPolicyNumber = new TextField("Policy Number");
		txtPolicyNumber.setValue(plicy.getPolicyNumber());
		
		txtFromDate = new TextField("Policy From Date");
		String fromDate = SHAUtils.formatDate(plicy.getPolicyFromDate());
		txtFromDate.setValue(fromDate);
		
		txtToDate = new TextField("Policy To Date");
		String toDate = SHAUtils.formatDate(plicy.getPolicyToDate());
		txtToDate.setValue(toDate);
		
		txtSuminsured = new TextField("Sum Insured($)");
		
		for(int index = 0; index < plicy.getInsured().size(); index++){
//			System.out.println("Intimationkey+++++++++++++++++++++"+tableDTO.get(index).getKey());
//			tableDTO.get(index).setRodKey(rodKey);
//			if (tableDTO.get(index).getKey() != null) {
		
		txtSuminsured.setValue(plicy!=null ? plicy.getTotalSumInsured().toString():"");
		
		txtRelationShip = new TextField("Relationship");
		txtRelationShip.setValue(plicy.getInsured().get(index).getRelationshipwithInsuredId() != null ? plicy.getInsured().get(index).getRelationshipwithInsuredId().getValue() : "");
		txtRelationShip.setValue("Self");
		txtSectionCode = new TextField("Section Code");
		txtSectionCode.setValue(plicy.getSectionCode());
		txtAge = new TextField("Age");
		txtAge.setValue(plicy.getInsured().get(index).getInsuredAge() != null ? plicy.getInsured().get(index).getInsuredAge().toString() : "");
		
		txtCoverCode = new TextField("Cover Code");
		if(intimation!=null&&intimation.getEvent()!=null&&intimation.getEvent().getEventCode()!=null){
			txtCoverCode.setValue(intimation.getEvent().getEventCode());
		}
		txtRiskName = new TextField("Risk Name");
		txtRiskName.setValue(plicy.getInsured().get(index).getInsuredName());
		txtSex = new TextField("Sex");
		txtSex.setValue(plicy.getInsured().get(index).getInsuredGender() != null && plicy.getInsured().get(index).getInsuredGender().getValue() !=null ? plicy.getInsured().get(index).getInsuredGender().getValue() : "");
		txtDOb = new TextField("DOB");
		String insureddob = SHAUtils.formatDate(plicy.getInsured().get(index).getInsuredDateOfBirth());
		txtDOb.setValue(insureddob);
		
		txtPlaceOfVisit = new TextField("Place of Visit");
		txtPlaceOfVisit.setValue(plicy.getInsured().get(index).getPlaceOfvisit());
		
		FormLayout firstForm = new FormLayout(txtPolicyNumber,txtFromDate,txtToDate,txtSectionCode,txtCoverCode,txtRiskName);
		firstForm.setSpacing(true);
		
		FormLayout secondForm = new FormLayout(txtSuminsured,txtRelationShip,txtSex,txtAge,txtDOb,txtPlaceOfVisit);
		secondForm.setSpacing(true);
		

		HorizontalLayout mainHor = new HorizontalLayout(firstForm,secondForm);
		mainHor.setSpacing(true);
		
		Panel mainPanel = new Panel(mainHor);
		mainPanel.setCaption("");
		
		firstForm.addStyleName("layoutDesign");
		secondForm.addStyleName("layoutDesign");
		
        setReadOnly(firstForm,true);
        setReadOnly(secondForm,true);

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
