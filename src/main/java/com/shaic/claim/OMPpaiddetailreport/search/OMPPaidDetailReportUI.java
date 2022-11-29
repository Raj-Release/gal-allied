package com.shaic.claim.OMPpaiddetailreport.search;

import javax.annotation.PostConstruct;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

@UIScoped
@SuppressWarnings("serial")
public class OMPPaidDetailReportUI extends SearchComponent<OMPPaidDetailReportFormDto>{
	
	OMPPaidDetailReportFormDto searchDto;
	OMPPaidDetailReportDetailTable searchTable;
	
	private TextField txtPolicyNo;
	private TextField txtIntimationNo;
	private PopupDateField fromDate;
	private PopupDateField toDate;
	
	@PostConstruct
	public void init() {
initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Paid Detail Report");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		txtPolicyNo = binder.buildAndBind("Policy No","policyNo",TextField.class);
		fromDate = binder.buildAndBind("From Date","fromdate",PopupDateField.class);
		toDate = binder.buildAndBind("To Date","todate",PopupDateField.class);
		
		FormLayout formLayoutLeft = new FormLayout(fromDate,txtPolicyNo);
		FormLayout formLayoutReight = new FormLayout(toDate,txtIntimationNo);	
	    
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutReight);
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(btnSearch, "top:160.0px;left:220.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:160.0px;left:329.0px;");
	//	VerticalLayout verticalLayout = new VerticalLayout();
		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("650px");
		 mainVerticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("224px");
		 
			addListener();
			return mainVerticalLayout;

		}

private void initBinder()

{
	this.binder = new BeanFieldGroup<OMPPaidDetailReportFormDto>(OMPPaidDetailReportFormDto.class);
	this.binder.setItemDataSource(new OMPPaidDetailReportFormDto());
	this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
}
}
