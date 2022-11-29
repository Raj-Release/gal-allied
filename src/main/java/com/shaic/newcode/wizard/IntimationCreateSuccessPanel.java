package com.shaic.newcode.wizard;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.PolicyService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class IntimationCreateSuccessPanel extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5850662205320529789L;
	
	@EJB
	private PolicyService policyService;
	
	VerticalLayout layout;

	@PostConstruct
	public void init() {
		layout = new VerticalLayout();
		layout.setSizeFull();
		setCompositionRoot(layout);
	}
	
	public void createSuccessLayout(Intimation intimation) {
		layout.addComponent(buildSuccessVerticalLayout(intimation));
		layout.setSizeFull();
	}
	
	private VerticalLayout buildSuccessVerticalLayout(Intimation intimation) {
		VerticalLayout SuccessverticalLayout = new VerticalLayout();
		//Vaadin8-setImmediate() SuccessverticalLayout.setImmediate(false);
		SuccessverticalLayout.setWidth("100.0%");
		SuccessverticalLayout.setHeight("530px");
		SuccessverticalLayout.setMargin(false);
		
		Label successLabel = new Label();
		//Vaadin8-setImmediate() successLabel.setImmediate(false);
		successLabel.setWidth("-1px");
		successLabel.setHeight("-1px");
		
		SuccessverticalLayout.addComponent(successLabel);
		SuccessverticalLayout.setComponentAlignment(successLabel, new Alignment(24));
		
		String success="Claim Intimation No " + intimation.getIntimationId() + " has been successfully submitted !!!!!";
		successLabel.setValue(success);
		// horizontalLayout_1
		HorizontalLayout successhorizontalLayout = buildSuccessHorizontalLayout_1();
		SuccessverticalLayout.addComponent(successhorizontalLayout);
		SuccessverticalLayout.setComponentAlignment(successhorizontalLayout,
				new Alignment(10));
		
		return SuccessverticalLayout;
	}
	
	private HorizontalLayout buildSuccessHorizontalLayout_1() {
		// common part: create layout
		HorizontalLayout successhorizontalLayout = new HorizontalLayout();
		//Vaadin8-setImmediate() successhorizontalLayout.setImmediate(false);
		successhorizontalLayout.setWidth("-1px");
		successhorizontalLayout.setHeight("-1px");
		successhorizontalLayout.setMargin(true);
		successhorizontalLayout.setSpacing(true);
		
		Button intimationHomenativeButton = new Button();
		intimationHomenativeButton.setCaption("Intimations Home");
		//Vaadin8-setImmediate() intimationHomenativeButton.setImmediate(false);
		intimationHomenativeButton.setWidth("-1px");
		intimationHomenativeButton.setHeight("-1px");
		intimationHomenativeButton.addClickListener(new Button.ClickListener() {
	        public void buttonClick(ClickEvent event) {
	        	Page.getCurrent().setUriFragment("!" + MenuItemBean.SEARCH_POLICY);
	        } 
    	});
		successhorizontalLayout.addComponent(intimationHomenativeButton);
		
		Button intimationViewnativeButton = new Button();
		intimationViewnativeButton.setCaption("View Intimation");
		//Vaadin8-setImmediate() intimationViewnativeButton.setImmediate(false);
		intimationViewnativeButton.setWidth("-1px");
		intimationViewnativeButton.setHeight("-1px");
//		intimationViewnativeButton.setData(createdIntimationObj);
		successhorizontalLayout.addComponent(intimationViewnativeButton);
		intimationViewnativeButton.addClickListener(new Button.ClickListener() {
	        public void buttonClick(ClickEvent event) {
	        	Intimation intimation = (Intimation) event.getButton().getData();
	        	if(intimation != null) {
	        		Hospitals hospital = policyService.getVWHospitalByKey(intimation.getHospital());
//	        		IntimationsDto intimationToIntimationDetailsDTO = new DtoConverter().intimationToIntimationDTO(createdIntimationObj,hospital);
		        	
		        	if(intimation.getStatus() != null && intimation.getStatus().getProcessValue().equalsIgnoreCase("SUBMITTED")) {
//		        		ViewIntimation intimationDetails = new ViewIntimation(intimationToIntimationDetailsDTO);
//		        		UI.getCurrent().addWindow(intimationDetails);
		        	} 
	        	}
	        	
	        } 
    	});
		
		return successhorizontalLayout;
	}
}
