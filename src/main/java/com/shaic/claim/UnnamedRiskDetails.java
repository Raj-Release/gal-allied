package com.shaic.claim;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.ui.Alignment;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;


public class UnnamedRiskDetails extends ViewComponent{
	
	private TextField organisationName;
	
	 private TextField sumInsured;
		
	 private TextField parentName;
		
	 private DateField dateOfBirth;
		
	 private TextField riskName;
		
	 private TextField age;
	 
	 
	 public void init(NewIntimationDto intimation){		 
		
			
			organisationName = new TextField("Organisation Name");
			organisationName.setEnabled(false);
			if(null != organisationName && null != intimation.getPolicy().getProposerFirstName()){
				organisationName.setValue(intimation.getPolicy().getProposerFirstName());			
			}
			
			dateOfBirth = new DateField("Date of Birth");
			dateOfBirth.setDateFormat("dd/MM/yyyy");
			dateOfBirth.setEnabled(false);
			if(null != dateOfBirth && intimation.getParentDOB()!= null){
				dateOfBirth.setValue(intimation.getParentDOB());
			}	
			
			riskName = new TextField("Risk Name");
			riskName.setEnabled(false);
			if(null != riskName && null != intimation.getPaPatientName()){
				riskName.setValue(intimation.getPaPatientName());
			}
			
			parentName = new TextField("Parent Name");
			parentName.setEnabled(false);
			if(null != parentName && null != intimation.getPaParentName()){
				parentName.setValue(intimation.getPaParentName());
			}
			
			sumInsured = new TextField("Sum Insured");	
			
			DBCalculationService dbCalculationService = new DBCalculationService();
			
			Double insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
					intimation.getInsuredPatient()
							.getInsuredId().toString(), intimation.getPolicy()
							.getKey());
			if(null != sumInsured && null != insuredSumInsured){
				sumInsured.setValue(String.valueOf(insuredSumInsured));
				sumInsured.setEnabled(false);;
			}
			
			age = new TextField("Age");
			if(null != age && null != intimation.getParentAge()){
				age.setValue(String.valueOf(intimation.getParentAge()));
				age.setEnabled(false);
			}
			
			FormLayout gpaLayout1 = new FormLayout(parentName,dateOfBirth,riskName,age);
			
			FormLayout gpaLayout2 = new FormLayout(organisationName,sumInsured);
			
			
			   HorizontalLayout unNamedRiskDetails = new HorizontalLayout(gpaLayout1,gpaLayout2);
			   unNamedRiskDetails.setComponentAlignment(gpaLayout1, Alignment.TOP_CENTER);
			   unNamedRiskDetails.setComponentAlignment(gpaLayout2, Alignment.MIDDLE_RIGHT);
			   unNamedRiskDetails.setWidth("100%");
			    
			    Panel mainPanel = new Panel(unNamedRiskDetails);
			    mainPanel.addStyleName("girdBorder");    
			    
//			    mainVerticalLayout = new VerticalLayout(mainPanel);
//			    mainVerticalLayout.setSpacing(true);
//			    mainVerticalLayout.setComponentAlignment(mainPanel, Alignment.TOP_CENTER);
			    
			    setCompositionRoot(mainPanel);
		 
	 }

}
