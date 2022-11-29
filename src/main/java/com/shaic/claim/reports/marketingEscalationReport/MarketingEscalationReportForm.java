package com.shaic.claim.reports.marketingEscalationReport;

import java.util.Date;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.domain.MasterService;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;

public class MarketingEscalationReportForm extends SearchComponent<MarketingEscalationReportFormDTO> {
	
	
	private DateField dateField;
		
	private DateField toDateField;
	
	private ComboBox cbxProductNameCode;
	
	private BeanItemContainer<SpecialSelectValue> product;
	
	@Inject
	MasterService masterService;
	
	private Button xmlReport;
	
	private MarketingEscalationReportFormDTO bean;

	@PostConstruct
	public void init() {
		initBinder();
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Marketing Escalations Report");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}

	private void initBinder() {
		this.binder = new BeanFieldGroup<MarketingEscalationReportFormDTO>(MarketingEscalationReportFormDTO.class);
		this.binder.setItemDataSource(new MarketingEscalationReportFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
	}

	private Component mainVerticalLayout() {

		 bean = new MarketingEscalationReportFormDTO();
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 mainVerticalLayout = new VerticalLayout();
		 mainVerticalLayout.setWidth("100.0%");
		 mainVerticalLayout.setMargin(false);		 
		 absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("113px");
		 
		 
		xmlReport = new Button("Export To Excel");
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();

		dateField = binder.buildAndBind("From Date","fromDate",DateField.class);	
		dateField.setDateFormat("dd/MM/yyyy");
		toDateField = binder.buildAndBind("To Date","toDate",DateField.class);
		toDateField.setDateFormat("dd/MM/yyyy");
		
		cbxProductNameCode =  binder.buildAndBind("Product Name/Code","productNameCode",ComboBox.class);
		
				
		dateField.setValidationVisible(false);
		toDateField.setValidationVisible(false);
		
		FormLayout formLayoutLeft = new FormLayout(dateField);
		formLayoutLeft.setSpacing(true);
		formLayoutLeft.setMargin(true);
		FormLayout formLayoutRight = new FormLayout(toDateField);
		formLayoutRight.setSpacing(true);
		formLayoutRight.setMargin(true);
		
		FormLayout formLayoutProduct = new FormLayout(cbxProductNameCode);
		formLayoutRight.setSpacing(true);
		formLayoutRight.setMargin(true);
		
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight,formLayoutProduct);
		fieldLayout.setMargin(true);
		fieldLayout.setSpacing(true);
		absoluteLayout_3.addComponent(fieldLayout);
		absoluteLayout_3.addComponent(btnGenerateReport, "top:80.0px;left:220.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:80.0px;left:350.0px;");
		mainVerticalLayout.addComponent(absoluteLayout_3);
		
		setDropDownValues();
		addListener();
		return mainVerticalLayout;
	
	}

	private void setDropDownValues() {
		//BeanItemContainer<SelectValue> productNameCode = masterService.getTmpCpuCodeList();
		//BeanItemContainer<SelectValue> product = masterService.getProductNameCodeList();
		    product=masterService.getProductNameCodeList();
			cbxProductNameCode.setContainerDataSource(product);
			cbxProductNameCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cbxProductNameCode.setItemCaptionPropertyId("value");
		
	}

	@SuppressWarnings("deprecation")
	public MarketingEscalationReportFormDTO validate() {

		String err = "";
		Long DiffDays=SHAUtils.getDaysBetweenDate(dateField.getValue(),toDateField.getValue());
		Date currentSystemDate = new Date();
		
		if(dateField.getValue()!=null && toDateField.getValue()!=null)
		{
			if(!SHAUtils.validateDate(dateField.getValue()) && !SHAUtils.validateDate(dateField.getValue()))
				{				
						
					 if(toDateField.getValue().before(dateField.getValue()))
					 {
						err= "Enter Valid To Date";
						showErrorMessage(err);			
						 btnSearch.setEnabled(true);
						 btnSearch.setDisableOnClick(true);
						 return null;
						
					}	
					 else
					 {
						 bean.setFromDate(dateField.getValue());
						 bean.setToDate(toDateField.getValue());
						
						
					 }		 
				}
						
		}
		if(dateField.getValue() == null || toDateField.getValue() == null)
		{
			err= "Both Date Fields are Mandatory";
			showErrorMessage(err);	
			return null;
		}else if(DiffDays > 15){
			err= "From & To date difference should be 15 days";
			showErrorMessage(err);
			dateField.clear();
			toDateField.clear();
			return null;
		}else if(dateField.getValue().after(toDateField.getValue())){
			err= "From Date should not be greater than To date";
			showErrorMessage(err);
			dateField.clear();
			toDateField.clear();
			return null;
		}else if(dateField.getValue().after(currentSystemDate) || toDateField.getValue().after(currentSystemDate) ){
			err= "From Date or To Date should not be greater than current date";
			showErrorMessage(err);
			dateField.clear();
			toDateField.clear();
			return null;
		}
		else
		{
			 bean.setFromDate(dateField.getValue());
			 bean.setToDate(toDateField.getValue());
			 bean.setProductNameCode((SpecialSelectValue) cbxProductNameCode.getValue());
		}
		
		if(dateField.getValue()!=null && toDateField.getValue()== null)
		{
			 bean.setFromDate(dateField.getValue());
		}
		
		if(toDateField.getValue()!=null && dateField.getValue()==null)
		{
			 bean.setToDate(toDateField.getValue());
		}
		
		
		
		return bean;
	
	}

	private void showErrorMessage(String err) {
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(err, buttonsNamewithType);
		
	}

}
