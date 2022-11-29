package com.shaic.claim.reports.paymentprocess;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.reimbursement.paymentprocesscpu.PaymentProcessCpuTable;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class PaymentProcessForm extends SearchComponent<PaymentProcessFormDTO>{
	
	private TextField txtIntimationNO;
	private TextField txtintimationSeqNo;
	private TextField txtClaimNo;
	private DateField dateField;
	private DateField toDateField;
	private ComboBox cmbYear;
	private ComboBox cmbCpu;
	private ComboBox cmbCpuLotNo;
	private ComboBox cmbStatus;
	private ComboBox cmbBranch;
	private TextField dummy;
	private TextField dummy1;
	
	private BeanItemContainer<SelectValue> year;
	private BeanItemContainer<SelectValue> cpuLotNo;
	private BeanItemContainer<SelectValue> status;
	private BeanItemContainer<SelectValue> branch;
	private BeanItemContainer<SelectValue> cpu;
	
	@Inject
	PaymentProcessCpuTable paymentProcessCpuTable;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Payment Process Report");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	
	public VerticalLayout mainVerticalLayout(){
		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 mainVerticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("100.0%");
		 mainVerticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("230px");
		
		btnSearch.setCaption(SearchComponent.GET_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		
		
		txtIntimationNO = binder.buildAndBind("Intimation No","intimationNo",TextField.class);
		txtClaimNo = binder.buildAndBind("Claim Number","claimNumber",TextField.class);
		dateField = binder.buildAndBind("From Date","fromDate",DateField.class);
		toDateField = binder.buildAndBind("To Date","toDate",DateField.class);
		cmbYear = binder.buildAndBind("Year","year",ComboBox.class);
		cmbCpu = binder.buildAndBind("CPU","cpu",ComboBox.class);
		cmbCpuLotNo = binder.buildAndBind("CPC Lot No","cpuLotNo",ComboBox.class);
		cmbStatus = binder.buildAndBind("Status","status",ComboBox.class);
		cmbBranch = binder.buildAndBind("Branch","branch",ComboBox.class);
		dummy =  new TextField();
		dummy.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		dummy.setEnabled(false);		
		dummy1 = new TextField();
		dummy1.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		dummy1.setEnabled(false);
		
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNO,txtClaimNo,dateField,cmbStatus,cmbCpuLotNo);
		formLayoutLeft.setSpacing(true);
		FormLayout formLayoutRight = new FormLayout(dummy,dummy1,toDateField);
		formLayoutRight.setSpacing(true);
		
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight);
		fieldLayout.setMargin(true);
		fieldLayout.setSpacing(true);
		absoluteLayout_3.addComponent(fieldLayout);
		
		absoluteLayout_3
		.addComponent(btnSearch, "top:190.0px;left:250.0px;");
		mainVerticalLayout.addComponent(absoluteLayout_3);
		
		addListener();
		
	
		return mainVerticalLayout;
		
	}
	
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<PaymentProcessFormDTO>(PaymentProcessFormDTO.class);
		this.binder.setItemDataSource(new PaymentProcessFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	
		public void setDropDownValues(BeanItemContainer<SelectValue> cpu,BeanItemContainer<SelectValue> year,
				BeanItemContainer<SelectValue> cpuLotNo, BeanItemContainer<SelectValue> status,BeanItemContainer<SelectValue> branch) 
		{
			cmbCpu.setContainerDataSource(cpu);
			cmbCpu.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbCpu.setItemCaptionPropertyId("value");
			
			
			cmbYear.setContainerDataSource(year);
			cmbYear.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbYear.setItemCaptionPropertyId("value");
			
			cmbCpuLotNo.setContainerDataSource(cpuLotNo);
			cmbCpuLotNo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbCpuLotNo.setItemCaptionPropertyId("value");
			
			cmbStatus.setContainerDataSource(status);
			cmbStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbStatus.setItemCaptionPropertyId("value");
			
			cmbBranch.setContainerDataSource(branch);
			cmbBranch.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbBranch.setItemCaptionPropertyId("value");
					
		}	
		
	
	

}
