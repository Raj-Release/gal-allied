package com.shaic.claim.reports.fvrassignmentreport;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;

public class FVRAssignmentReportForm extends SearchComponent<FVRAssignmentReportFormDTO>{
	
	
	
	private DateField dateField;
	private DateField toDateField;
	private ComboBox cmbCpuCode;
	private ComboBox cmbReportType;
	private Button xmlReport;
	private ComboBox cmbClaimType;
	private ComboBox cmbFvrCpuCode;
	
	private BeanItemContainer<SelectValue> cpuCode;
	private BeanItemContainer<SelectValue> reportType;
	
	
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("FVR Assignment Report");
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
		 absoluteLayout_3.setHeight("206px");
		 
		xmlReport = new Button("Export To Excel");
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		
		
		cmbCpuCode = binder.buildAndBind("CPU Code","cpuCode",ComboBox.class);
		//Vaadin8-setImmediate() cmbCpuCode.setImmediate(true);
		
		
		
		
		cmbReportType = binder.buildAndBind("Report Type","reportType",ComboBox.class);
		cmbReportType.setRequired(true);
		dateField = binder.buildAndBind("From Date","fromDate",DateField.class);
		dateField.setRequired(true);
		toDateField = binder.buildAndBind("To Date","toDate",DateField.class);
		toDateField.setRequired(true);
		cmbClaimType = binder.buildAndBind("Claim Type","claimType",ComboBox.class);
		cmbFvrCpuCode = binder.buildAndBind("FVR CPU Code","fvrCpuCode",ComboBox.class);
		
		FormLayout formLayoutLeft = new FormLayout(dateField,cmbCpuCode,cmbReportType);
		formLayoutLeft.setSpacing(true);
		FormLayout formLayoutRight = new FormLayout(toDateField,cmbFvrCpuCode,cmbClaimType);
		formLayoutRight.setSpacing(true);
		
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight);
		fieldLayout.setSpacing(true);
		fieldLayout.setMargin(true);
		absoluteLayout_3.addComponent(fieldLayout);
		
		absoluteLayout_3
		.addComponent(btnSearch, "top:150.0px;left:250.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:150.0px;left:359.0px;");
		absoluteLayout_3.addComponent(xmlReport, "top:150.0px;left:468.0px;");
		mainVerticalLayout.addComponent(absoluteLayout_3);
		
		addListener();
		addReportListener();
		return mainVerticalLayout;
	}
	
	private void addReportListener()
	{
		xmlReport.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;


				@Override
				public void buttonClick(ClickEvent event) {
					fireViewEvent(FVRAssignmentReportPresenter.GENERATE_REPORT, null,null);
					//getTableDataForReport();
				
			}
		});
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<FVRAssignmentReportFormDTO>(FVRAssignmentReportFormDTO.class);
		this.binder.setItemDataSource(new FVRAssignmentReportFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public void setDropDownValues(BeanItemContainer<SelectValue> cpuCode, BeanItemContainer<SelectValue> reportType,BeanItemContainer<SelectValue> claimType,
			BeanItemContainer<SelectValue> fvrCpuCode) 
	{
		//SelectValue selectAllCpu = new SelectValue();
		//selectAllCpu.setId(0l);
		//selectAllCpu.setValue("ALL CPU");
		//cpuCode.addBean(selectAllCpu);
		//cpuCode.addItem("selectAllCpu");
		
		cmbCpuCode.setContainerDataSource(cpuCode);
		cmbCpuCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCpuCode.setItemCaptionPropertyId("value");
	     
	     cmbFvrCpuCode.setContainerDataSource(fvrCpuCode);
	     cmbFvrCpuCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	     cmbFvrCpuCode.setItemCaptionPropertyId("value");
	     
	     cmbClaimType.setContainerDataSource(claimType);
	     cmbClaimType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	     cmbClaimType.setItemCaptionPropertyId("value");
	     

		SelectValue selectFvrAssigned = new SelectValue();
		selectFvrAssigned.setId(51l);
		selectFvrAssigned.setValue("FVR Assigned");

		SelectValue selectFvrNotAssigned = new SelectValue();

		selectFvrNotAssigned.setId(49l);
		selectFvrNotAssigned.setValue("FVR Assignment Pending");
		
		SelectValue selectFvrNotRequired = new SelectValue();

		selectFvrNotRequired.setId(52l);
		selectFvrNotRequired.setValue("FVR Not Required");
		

		List<SelectValue> selectVallueList = new ArrayList<SelectValue>();
		selectVallueList.add(selectFvrAssigned);
		selectVallueList.add(selectFvrNotAssigned);
		selectVallueList.add(selectFvrNotRequired);
		
		
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		selectValueContainer.addAll(selectVallueList);
		cmbReportType.setContainerDataSource(selectValueContainer);
		cmbReportType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbReportType.setItemCaptionPropertyId("value");
		
		
	}	
	
	
	public String validate()
	{
		StringBuffer err = new StringBuffer();
		
		if(dateField.getValue() == null || toDateField.getValue() == null)
		{
			err.append("Date Fields are Mandatory, Please provide Both From and To Date Value<br>");
		}
		
		if(dateField.getValue()!=null && toDateField.getValue()!=null)
		{
		 if(toDateField.getValue().before(dateField.getValue()))
		 {
			 err.append("Enter Valid To Date<br>");
		}
		}
		
		/*else
		{
		if(cmbCpuCode.getValue()==null && cmbReportType.getValue() == null && cmbClaimType.getValue() == null)
		{
		
			return err = "Any one of the field is Mandatory";
		}		
		}*/
		
		if(cmbReportType.getValue() == null)
		{
			err.append("Please select Report Type<br>");
		}		
		
		return err.toString();
		
	}
}

