package com.shaic.claim.reports.paymentbatchreport;

import javax.annotation.PostConstruct;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class PaymentBatchReportForm extends SearchComponent<PaymentBatchReportFormDTO>{
	
	private DateField dateField;
	private DateField toDateField;
	private TextField lotNoFrom;
	private TextField lotNoTo;
	private Button xmlReport;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Payment Batch Report");
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
		 absoluteLayout_3.setHeight("200px");
		 
		xmlReport = new Button("Export To Excel");
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		dateField = binder.buildAndBind("From Date","fromDate",DateField.class);
		toDateField = binder.buildAndBind("To Date","toDate",DateField.class);
		lotNoFrom = binder.buildAndBind("Lot No.From","lotNoFrom",TextField.class);
		lotNoTo = binder.buildAndBind("Lot No.To","lotNoTo",TextField.class);
		FormLayout formLayoutLeft = new FormLayout(lotNoFrom,dateField);
		formLayoutLeft.setSpacing(true);
		FormLayout formLayoutRight = new FormLayout(lotNoTo,toDateField);
		formLayoutRight.setSpacing(true);
		
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight);
		fieldLayout.setMargin(true);
		fieldLayout.setSpacing(true);
		absoluteLayout_3.addComponent(fieldLayout);
		absoluteLayout_3
		.addComponent(btnSearch, "top:120.0px;left:190.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:120.0px;left:299.0px;");
		absoluteLayout_3.addComponent(xmlReport, "top:120.0px;left:408.0px;");
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
					fireViewEvent(PaymentBatchReportPresenter.GENERATE_REPORT, null,null);
					//getTableDataForReport();
				
			}
		});
	}
	
	
	public String validate()
	{
		String err = "";
		
		if(dateField.getValue() !=null && toDateField.getValue() !=null)
		{
			 if(toDateField.getValue().before(dateField.getValue()))
			 {
				return err= "Enter Valid To Date";
			}
		}
		else if(dateField.getValue() == null && toDateField.getValue() ==null 
				&& (lotNoFrom.getValue() == null || lotNoFrom.getValue().equals(""))  && (lotNoTo.getValue() == null) || lotNoTo.getValue().equals(""))
		{
			
				return err= " Please Enter any field to search" ;
			
		}
		
		
		return null;
		
	}
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<PaymentBatchReportFormDTO>(PaymentBatchReportFormDTO.class);
		this.binder.setItemDataSource(new PaymentBatchReportFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

}
