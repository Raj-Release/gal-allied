package com.shaic.claim.registration.balancesuminsured.view;

import java.util.Iterator;

import javax.ejb.EJB;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.domain.Claim;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class ViewUnnamedRiskDetailsUI extends ViewComponent{

	
	private TextField organisationName;
	
	 private TextField sumInsured;
		
	 private TextField parentName;
		
	 private TextField dateOfBirth;
		
	 private TextField riskName;
		
	 private TextField age;
	 
	 private TextField sectionOrclass;
		
	 private TextField category;
		
	 private TextField riskDOB;
		
	 private TextField riskAge;
	 
	 @EJB
		private DBCalculationService dbCalculationService;
	 
	 @EJB
		private IntimationService intimationService;
	 
	 public void init(Long intimationKey){
		 
		Intimation intimation = intimationService.getIntimationByKey(intimationKey);
	 	Claim claim = intimationService
					.getClaimforIntimation(intimation.getKey());
			
		 organisationName = new TextField("Organization Name");
		 if(null != organisationName && null != intimation.getPolicy().getProposerFirstName()){
			 organisationName.setValue(intimation.getPolicy().getProposerFirstName());
		 }
		 
		 Double insuredSumInsured = dbCalculationService
					.getGPAInsuredSumInsured(intimation.getInsured().getInsuredId().toString(),
							intimation.getPolicy().getKey());
		 
		 sumInsured = new TextField("Sum Insured");
		 if(null != sumInsured && null != insuredSumInsured){
			 sumInsured.setValue(String.valueOf(insuredSumInsured));
		 }		 
		 
		 parentName = new TextField("Parent Name");
		 if(null != parentName && null != claim.getGpaParentName()){
			 parentName.setValue(claim.getGpaParentName());
		 }
		 
		 dateOfBirth = new TextField("Parent(DOB)"); 
		 //dateOfBirth.setDateFormat("dd/MM/yyyy");
		 if(null != dateOfBirth && null != claim.getGpaParentDOB()){
			 dateOfBirth.setValue(String.valueOf(claim.getGpaParentDOB()));
		 }
		 
		 age = new TextField("Parent Age");
		 if(null != age && null != claim.getGpaParentAge()){
			 age.setValue(String.valueOf(claim.getGpaParentAge()));
		 }
		 
		 sectionOrclass = new TextField("Section/Class");
		 if(null != sectionOrclass && null != claim.getGpaSection()){
			 sectionOrclass.setValue(claim.getGpaSection());
		 }
		 
		 category = new TextField("Category");
		 if(null != category && null !=claim.getGpaCategory()){
			 category.setValue(claim.getGpaCategory());
		 }
		 
		 riskName = new TextField("Risk Name");
		 if(null != riskName && null !=claim.getGpaRiskName()){
			 riskName.setValue(claim.getGpaRiskName());
		 }
		 
		 riskDOB = new TextField("Risk(DOB)");
		// dateOfBirth.setDateFormat("dd/MM/yyyy");
		 if(null != riskDOB && null !=claim.getGpaRiskDOB()){
			 riskDOB.setValue(String.valueOf(claim.getGpaRiskDOB()));
		 }
		 
		 riskAge = new TextField("Risk Age");
		 if(null != riskAge && null != claim.getGpaRiskAge()){
			 riskAge.setValue(String.valueOf(claim.getGpaRiskAge()));
		 }
		 
		 FormLayout gpaLayout1 = new FormLayout(organisationName,sumInsured,sectionOrclass,category,parentName,dateOfBirth,age);
		 gpaLayout1.setSpacing(true);
		 gpaLayout1.setMargin(true);
		 FormLayout gpaLayout2 = new FormLayout(riskName,riskDOB,riskAge);
		 gpaLayout2.setSpacing(true);
		 gpaLayout2.setMargin(true);
		 
		 HorizontalLayout mainHor = new HorizontalLayout(gpaLayout1,gpaLayout2);
			mainHor.setSpacing(true);
			
			Panel mainPanel = new Panel(mainHor);
			mainPanel.setCaption("");
			
			gpaLayout1.addStyleName("layoutDesign");
			gpaLayout2.addStyleName("layoutDesign");
			
	        setReadOnly(gpaLayout1,true);
	        setReadOnly(gpaLayout2,true);

			setCompositionRoot(mainPanel);

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
					else if(c instanceof DateField){
						
						DateField field = (DateField) c;
						field.setWidth("250px");
					//	field.setNullRepresentation("");
						field.setReadOnly(readOnly);
						field.addStyleName(ValoTheme.DATEFIELD_BORDERLESS);
					}
					
				}
			}
		}
	 
}
