package com.shaic.claim.reimbursement.opscreen;

import javax.annotation.PostConstruct;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Alignment;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;

public class OpForm extends SearchComponent<OpFormDTO>{
	
	private DateField dateField;
	private DateField toDateField;
	private ComboBox  cmbZoneType;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Agent/Broker Report");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	
	public VerticalLayout mainVerticalLayout(){
		
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		dateField = binder.buildAndBind("Start Date","fromDate",DateField.class);
		toDateField = binder.buildAndBind("To Date","toDate",DateField.class);
		cmbZoneType = binder.buildAndBind("Zone","zoneType" ,ComboBox.class);
		
		FormLayout formLayoutLeft = new FormLayout(dateField , cmbZoneType);
		formLayoutLeft.setSpacing(true);
		FormLayout formLayoutRight = new FormLayout(toDateField);
		formLayoutRight.setSpacing(true);
		
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight);
		fieldLayout.setHeight("45px");
		HorizontalLayout buttonlayout = new HorizontalLayout(btnSearch,btnReset);
		buttonlayout.setWidth("45%");
		buttonlayout.setComponentAlignment(btnSearch,  Alignment.MIDDLE_RIGHT);
		buttonlayout.setSpacing(true);
		
		mainVerticalLayout.addComponent(fieldLayout);
		mainVerticalLayout.setComponentAlignment(fieldLayout, Alignment.BOTTOM_LEFT);
		mainVerticalLayout.addComponent(buttonlayout);
		mainVerticalLayout.setComponentAlignment(buttonlayout, Alignment.MIDDLE_LEFT);
		mainVerticalLayout.setWidth("100%");
		mainVerticalLayout.setHeight("145px");
		mainVerticalLayout.setMargin(true);
		mainVerticalLayout.setSpacing(true);
		
		addListener();
		return mainVerticalLayout;
	}
	
	
	
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<OpFormDTO>(OpFormDTO.class);
		this.binder.setItemDataSource(new OpFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	
}



