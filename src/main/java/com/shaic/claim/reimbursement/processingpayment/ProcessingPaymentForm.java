package com.shaic.claim.reimbursement.processingpayment;

import javax.annotation.PostConstruct;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Alignment;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextField;

import com.vaadin.v7.ui.VerticalLayout;

public class ProcessingPaymentForm extends SearchComponent<ProcessingPaymentFormDTO>{
	
	private PopupDateField dateField;
	private PopupDateField toDateField;
	private TextField txtLotNo;
	private ComboBox cmbZone;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Hospital Intimation Status Report");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	
	public VerticalLayout mainVerticalLayout(){
		
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		dateField = binder.buildAndBind("From Date","fromDate",PopupDateField.class);
		dateField.setTextFieldEnabled(false);
		toDateField = binder.buildAndBind("To Date","toDate",PopupDateField.class);
		toDateField.setTextFieldEnabled(false);
		
		txtLotNo = binder.buildAndBind("Lot No","lotNo",TextField.class);
		cmbZone = binder.buildAndBind("cmbZone","zoneType",ComboBox.class);
		
		
		
		FormLayout formLayoutLeft = new FormLayout(txtLotNo,dateField);
		formLayoutLeft.setSpacing(true);
		FormLayout formLayoutRight = new FormLayout(cmbZone,toDateField);
		formLayoutRight.setSpacing(true);
		
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight);
		HorizontalLayout buttonlayout = new HorizontalLayout(btnSearch,btnReset);
		buttonlayout.setWidth("45%");
		buttonlayout.setComponentAlignment(btnSearch,  Alignment.MIDDLE_RIGHT);
		buttonlayout.setSpacing(true);
		
		mainVerticalLayout.addComponent(fieldLayout);
		fieldLayout.setHeight("45px");
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
	
	
	
	public String validate()
	{
		String err = "";
		
		if(dateField.getValue()!=null && toDateField.getValue()!=null)
		{
		 if(toDateField.getValue().before(dateField.getValue()))
		 {
			return err= "Enter Valid To Date";
		}
		}
		if(dateField.getValue()==null && toDateField.getValue()==null)
		{
			return err = "Date field is Mandatory";
	
		}
		return null;
		
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<ProcessingPaymentFormDTO>(ProcessingPaymentFormDTO.class);
		this.binder.setItemDataSource(new ProcessingPaymentFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
		
	
}
