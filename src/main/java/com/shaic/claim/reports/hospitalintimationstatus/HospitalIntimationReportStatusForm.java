package com.shaic.claim.reports.hospitalintimationstatus;

import javax.annotation.PostConstruct;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.VerticalLayout;

public class HospitalIntimationReportStatusForm extends SearchComponent<HospitalIntimationStatusReportFormDTO>{
	
	private PopupDateField dateField;
	private PopupDateField toDateField;
	private Button xmlReport;
	
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
		mainVerticalLayout = new VerticalLayout();
		
		dateField = binder.buildAndBind("From Date","fromDate",PopupDateField.class);
		dateField.setTextFieldEnabled(false);
		toDateField = binder.buildAndBind("To Date","toDate",PopupDateField.class);
		toDateField.setTextFieldEnabled(false);
		
		FormLayout formLayoutLeft = new FormLayout(dateField);
		formLayoutLeft.setSpacing(true);
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
					fireViewEvent(HospitalIntimationReportStatusPresenter.GENERATE_REPORT, null,null);
					//getTableDataForReport();
				
			}
		});
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
		this.binder = new BeanFieldGroup<HospitalIntimationStatusReportFormDTO>(HospitalIntimationStatusReportFormDTO.class);
		this.binder.setItemDataSource(new HospitalIntimationStatusReportFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
		

}
