package com.shaic.claim.OMPViewDetails.view;

import java.util.Iterator;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.domain.OMPIntimation;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class OMPViewBalanceSIUI  extends ViewComponent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TextField txtNameofInsured;

	private TextField txtProductName;
	
	private TextField txtFromDate;
	
	@Inject
	 private Instance<OMPViewBalanceSIDetailTable> ompBalanceSiDetailsInstance;
	    
	 private OMPViewBalanceSIDetailTable ompBalanceSiDetailsObj;
	
	public void init(OMPIntimation intimation){
		
		
		txtNameofInsured = new TextField("Name of the Insured");
		
		txtProductName = new TextField("Product Name");
		
		FormLayout leftForm = new FormLayout(txtNameofInsured);
		
		FormLayout rightForm = new FormLayout(txtProductName);
		
		ompBalanceSiDetailsObj = ompBalanceSiDetailsInstance.get();
		ompBalanceSiDetailsObj.init("", false, false);
//		    List<OMPClaimProcessorDTO> claimHistoryForCashless = intimationService.getIntimationByNumber(intimation.getIntimationId());
//		    ompClaimRateDetailsObj.setTableList(claimHistoryForCashless);
		ompBalanceSiDetailsObj.setCaption("");
		
		VerticalLayout mainVLayout = new VerticalLayout(leftForm,rightForm,ompBalanceSiDetailsObj);
		
		Panel mainPanel = new Panel(mainVLayout);
		mainPanel.setCaption("");
		mainPanel.addStyleName("layoutDesign");
	    setReadOnly(leftForm,true);
        setReadOnly(rightForm,true);
        
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
