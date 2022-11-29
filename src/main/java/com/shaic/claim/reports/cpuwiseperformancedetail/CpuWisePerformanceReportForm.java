package com.shaic.claim.reports.cpuwiseperformancedetail;

import javax.annotation.PostConstruct;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.SearchComponent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;

public class CpuWisePerformanceReportForm extends SearchComponent<CpuWisePerformanceReportFormDTO>{
	private DateField dateField;
	private DateField toDateField;
	private Button xmlReport;
	private CpuWisePerformanceReportFormDTO bean;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Cpu Wise Performance Report");
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
		 absoluteLayout_3.setHeight("149px");
		 
		xmlReport = new Button("Export To Excel");
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		
		
		
		
		dateField = new DateField("From Date");
		dateField.setDateFormat("dd/MM/yyyy");
		toDateField = new DateField("To Date");
		toDateField.setDateFormat("dd/MM/yyyy");
		dateField.setValidationVisible(false);
		toDateField.setValidationVisible(false);
		
		
		FormLayout formLayoutLeft = new FormLayout(dateField);
		formLayoutLeft.setSpacing(true);
		//formLayoutLeft.setWidth("180px");
		FormLayout formLayoutRight = new FormLayout(toDateField);
		formLayoutRight.setSpacing(true);
		
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight);
		fieldLayout.setMargin(true);
		fieldLayout.setSpacing(true);
		absoluteLayout_3.addComponent(fieldLayout);
		absoluteLayout_3
		.addComponent(btnSearch, "top:80.0px;left:190.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:80.0px;left:299.0px;");
		absoluteLayout_3.addComponent(xmlReport, "top:80.0px;left:408.0px;");
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
					fireViewEvent(CpuWisePerformanceReportPresenter.GENERATE_REPORT, null,null);
					//getTableDataForReport();
				
			}
		});
	}
	
	
	public CpuWisePerformanceReportFormDTO  validate()
	{
		String err = "";
		bean = new CpuWisePerformanceReportFormDTO();
		
		
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
		if(dateField.getValue() == null && toDateField.getValue() == null)
		{
			err= "Date Field is manditory";
			showErrorMessage(err);	
			 return null;
		}
		else
		{		
			 bean.setFromDate(dateField.getValue());
			 bean.setToDate(toDateField.getValue());
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
	private void showErrorMessage(String eMsg) {
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Errors");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
		btnSearch.setEnabled(true);
	}
	
	
	
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<CpuWisePerformanceReportFormDTO>(CpuWisePerformanceReportFormDTO.class);
		this.binder.setItemDataSource(new CpuWisePerformanceReportFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
		


}
