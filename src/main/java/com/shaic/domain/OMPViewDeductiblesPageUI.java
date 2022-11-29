package com.shaic.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorDTO;
import com.shaic.claim.omp.ratechange.OMPViewDeductiblesDetailTable;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class OMPViewDeductiblesPageUI extends ViewComponent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TextField txtProductName;

	private TextField txtPlan;
	
	private TextField txtProductCode;
	
	private TextField txtSumInsured;
	
	 @Inject
	 private Instance<OMPViewDeductiblesDetailTable> ompDeductibesDetailsInstance;
	    
	 private OMPViewDeductiblesDetailTable ompDeductibesDetailsObj;
	 
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private MasterService masterService;
	
	public void init(OMPClaim claim){
		
		
		txtProductName = new TextField("Product Name");
		if(claim.getIntimation().getPolicy()!= null && claim.getIntimation().getPolicy().getProduct()!= null && claim.getIntimation().getPolicy().getProduct().getValue()!= null){
			txtProductName.setValue(claim.getIntimation().getPolicy().getProduct().getValue());
		}
		txtPlan = new TextField("Plan");
		if(claim.getIntimation().getInsured()!= null && claim.getIntimation().getInsured().getPlan()!= null ){
			txtPlan.setValue(claim.getIntimation().getInsured().getPlan());
		}
		txtProductCode = new TextField("Product Code");
		if(claim.getIntimation().getPolicy()!= null && claim.getIntimation().getPolicy().getProduct()!= null && claim.getIntimation().getPolicy().getProduct().getCode()!= null){
			txtProductCode.setValue(claim.getIntimation().getPolicy().getProduct().getCode());
		}
		txtSumInsured = new TextField("Sum Insured");
		if(claim.getIntimation().getPolicy()!= null && claim.getIntimation().getPolicy().getTotalSumInsured()!= null ){
			txtSumInsured.setValue(String.valueOf(claim.getIntimation().getPolicy().getTotalSumInsured()));
		}
		
		FormLayout firstForm = new FormLayout(txtProductName,txtPlan);
		firstForm.setSpacing(true);
		FormLayout secondForm = new FormLayout(txtProductCode,txtSumInsured);
		secondForm.setSpacing(true);
		
		ompDeductibesDetailsObj = ompDeductibesDetailsInstance.get();
		ompDeductibesDetailsObj.init("", false, false);
		List<OMPClaimProcessorDTO> deductibleList = new ArrayList<OMPClaimProcessorDTO>();
		if(claim.getIntimation().getPolicy()!= null &&claim.getIntimation().getPolicy().getProduct()!= null && claim.getIntimation().getPolicy().getProduct().getKey()!= null &&
				claim.getIntimation().getPolicy()!= null && claim.getIntimation().getPolicy().getTotalSumInsured()!= null){
			deductibleList = dbCalculationService.getdeductiblevalues(claim.getIntimation().getPolicy().getProduct().getKey(),claim.getIntimation().getInsured().getPlan(),claim.getIntimation().getPolicy().getTotalSumInsured());
			for(OMPClaimProcessorDTO listDto: deductibleList){
				if(listDto!= null){
					SelectValue eventCode = new SelectValue();
					eventCode.setId(0l);
					eventCode.setValue(listDto.getDedutEventCode());
					if(listDto.getDedutEventCode()!= null){
						MastersEvents events = masterService.getEventType(listDto.getDedutEventCode());
						eventCode.setId(events.getKey());
						listDto.setEventCode(eventCode);
						listDto.setEventdescription(events.getEventDescription());
					}
						/*if(claim.getEvent().getEventCode()!= null && claim.getEvent().getEventCode().equalsIgnoreCase("OMP-CVR-011") || 								
								claim.getEvent().getEventCode().equalsIgnoreCase("CFT-CVR-009") ||  claim.getEvent().getEventCode().equalsIgnoreCase("OMP-CVR-012") ||		
										claim.getEvent().getEventCode().equalsIgnoreCase("CFT-CVR-010") ||  claim.getEvent().getEventCode().equalsIgnoreCase("OMP-CVR-015") ||
										claim.getEvent().getEventCode().equalsIgnoreCase("CFT-CVR-012")){
						listDto.setDescription("Hours");				

						
					listDto.setDescription("Hours");
					}else{
						listDto.setDescription("Fixed Value");
					}*/
				}
			}
		}
		if(deductibleList!= null){
			ompDeductibesDetailsObj.setTableList(deductibleList);
		}
		ompDeductibesDetailsObj.setCaption("");
		
		VerticalLayout mainVLayout = new VerticalLayout(firstForm,secondForm,ompDeductibesDetailsObj);
		
		Panel mainPanel = new Panel(mainVLayout);
		mainPanel.setCaption("");
		mainPanel.addStyleName("layoutDesign");
		

        setReadOnly(firstForm,true);
        setReadOnly(secondForm,true);
        
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
				
			}
		}
	}

}
