package com.shaic.claim.fss.filedetailsreport;

import java.util.ArrayList;
import java.util.List;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
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
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class FileDetailsReportForm extends SearchComponent<FileDetailsReportFormDTO>{
	
	
	
	private DateField dateField;
	private DateField toDateField;
	private TextField reportType;
	private ComboBox cmbStatusType;
	private Button xmlReport;
	private Button btnReset;
	
	private BeanItemContainer<SelectValue> statusType;
	
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("File Details Report");
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
		
		reportType = new TextField("Report Type");
		reportType.setRequired(true);
		reportType.setEnabled(false);
		
		cmbStatusType = binder.buildAndBind("Status","statusType",ComboBox.class);
		//cmbStatusType.setRequired(true);
		
		dateField = binder.buildAndBind("From Date","fromDate",DateField.class);
		dateField.setRequired(true);
		toDateField = binder.buildAndBind("To Date","toDate",DateField.class);
		toDateField.setRequired(true);
		
		FormLayout formLayoutLeft = new FormLayout(reportType,dateField);
		formLayoutLeft.setSpacing(true);
		FormLayout formLayoutRight = new FormLayout(cmbStatusType, toDateField);
		formLayoutRight.setSpacing(true);
		
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight);
		fieldLayout.setSpacing(true);
		fieldLayout.setMargin(true);
		absoluteLayout_3.addComponent(fieldLayout);
		
		btnReset = new Button();
		btnReset.setCaption("Reset");
		//Vaadin8-setImmediate() btnReset.setImmediate(true);
		btnReset.setTabIndex(13);
		btnReset.setStyleName(ValoTheme.BUTTON_DANGER);
		btnReset.addClickListener(new ClickListener() {

			public void buttonClick(ClickEvent event) {

				fireViewEvent(FileDetailsReportPresenter.RESET_SEARCH_VIEW, null);
			}
		});
		
		absoluteLayout_3
		.addComponent(btnSearch, "top:150.0px;left:250.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:150.0px;left:359.0px;");
		absoluteLayout_3.addComponent(xmlReport, "top:150.0px;left:468.0px;");
		mainVerticalLayout.addComponent(absoluteLayout_3);
		
		addListener();
		addReportListener();
		return mainVerticalLayout;
	}
	
public void resetSearchFileDetailsFields() {
	dateField.setValue(null);
	toDateField.setValue(null);
	cmbStatusType.setValue(null);
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
					fireViewEvent(FileDetailsReportPresenter.GENERATE_REPORT, null,null);
					//getTableDataForReport();
				
			}
		});
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<FileDetailsReportFormDTO>(FileDetailsReportFormDTO.class);
		this.binder.setItemDataSource(new FileDetailsReportFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public void setDropDownValues() 
	{
		reportType.setValue(SHAConstants.STORAGE_FILE_STATUS);
		
		SelectValue selectCheckIn = new SelectValue();
		selectCheckIn.setId(null);
		selectCheckIn.setValue(SHAConstants.FILE_CHECK_IN);
		
		SelectValue selectCheckOut = new SelectValue();
		selectCheckOut.setId(null);
		selectCheckOut.setValue(SHAConstants.FILE_CHECK_OUT);
		
		List<SelectValue> selectVallueList1 = new ArrayList<SelectValue>();
		selectVallueList1.add(selectCheckIn);
		selectVallueList1.add(selectCheckOut);
		
		BeanItemContainer<SelectValue> selectValueContainer1 = new BeanItemContainer<SelectValue>(SelectValue.class);
		selectValueContainer1.addAll(selectVallueList1);
		cmbStatusType.setContainerDataSource(selectValueContainer1);
		cmbStatusType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbStatusType.setItemCaptionPropertyId("value");
		
	}	
	
	
	public String validate()
	{
		String err = "";
		
		if(dateField.getValue() == null || toDateField.getValue() == null)
		{
			err = "Date Fields are Mandatory, Please provide Both From and To Date Value</br>";
		}
		
		if(dateField.getValue()!=null && toDateField.getValue()!=null)
		{
		 if(toDateField.getValue().before(dateField.getValue()))
		 {
			 err += "Enter Valid To Date</br>";
		}
		}
		
		return err;
		
	}
}

