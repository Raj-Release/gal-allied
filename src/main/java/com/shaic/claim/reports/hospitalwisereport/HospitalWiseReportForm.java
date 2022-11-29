package com.shaic.claim.reports.hospitalwisereport;

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
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class HospitalWiseReportForm extends	SearchComponent<HospitalWiseReportFormDTO> {
	private DateField dateField;
	private DateField toDateField;
	private ComboBox cmbdateType;
	private TextField txtHospitalCode;
	private TextField dummyField;
	private Button xmlReport;

	private BeanItemContainer<SelectValue> dateType;

	@PostConstruct
	public void init() {
		initBinder();

		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Hospital Wise Report");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}

	public VerticalLayout mainVerticalLayout() {
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

		cmbdateType = binder.buildAndBind("Date Type", "dateType",
				ComboBox.class);
		//Vaadin8-setImmediate() cmbdateType.setImmediate(true);
		
		setDropDownValues();

		dateField = binder.buildAndBind("Intimation Date:From", "fromDate",DateField.class);
		toDateField = binder.buildAndBind("To Date", "toDate", DateField.class);
		dummyField = new TextField("");
		dummyField.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		dummyField.setEnabled(false);
		txtHospitalCode = binder.buildAndBind("Hospital Code", "hospitalCode",
				TextField.class);

		FormLayout formLayoutLeft = new FormLayout(cmbdateType, dateField,
				txtHospitalCode);
		formLayoutLeft.setSpacing(true);
		
		FormLayout formLayoutRight = new FormLayout(dummyField,toDateField);
		formLayoutRight.setSpacing(true);

		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,
				formLayoutRight);
		fieldLayout.setSpacing(true);
//		fieldLayout.setWidth("100%");
		fieldLayout.setMargin(true);
		absoluteLayout_3.addComponent(fieldLayout);
		
		absoluteLayout_3
		.addComponent(btnSearch, "top:150.0px;left:190.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:150.0px;left:299.0px;");
		absoluteLayout_3.addComponent(xmlReport, "top:150.0px;left:408.0px;");
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
					fireViewEvent(HospitalWiseReportPresenter.GENERATE_REPORT, null,null);
					//getTableDataForReport();
				
			}
		});
	}

	private void initBinder() {
		this.binder = new BeanFieldGroup<HospitalWiseReportFormDTO>(
				HospitalWiseReportFormDTO.class);
		this.binder.setItemDataSource(new HospitalWiseReportFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	
	  public void setDropDownValues() {
		  
		    SelectValue intimationDate = new SelectValue();
		    intimationDate.setId(1l);
		    intimationDate.setValue("Intimation Date");

			SelectValue admissionDate = new SelectValue();
			admissionDate.setId(2l);
			admissionDate.setValue("Admission Date");
		

			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			selectValueList.add(intimationDate);
			selectValueList.add(admissionDate);
			
		  BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		  selectValueContainer.addAll(selectValueList);
		  
		  cmbdateType.setContainerDataSource(selectValueContainer);
		  cmbdateType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		  cmbdateType.setItemCaptionPropertyId("value");
	  }
	  
	public String validate() 
	{
		 String err = "";
		 
		 if((dateField.getValue() == null && toDateField.getValue() == null) && txtHospitalCode.getValue() == "" && cmbdateType.getValue() == null)
			{
				return err = "Please Provide value for Mandatory field - Date Type.";
			}
			else
			{	
				if(cmbdateType.getValue() == null){
					return err = "Date Type is Mandatory. Please Select Date Type Value";
				}
				
				
				if(dateField.getValue() != null && toDateField.getValue() !=null)
				{
			      if(toDateField.getValue().before(dateField.getValue())) 
							 {
				      err= "Enter Valid To Date </br>";
			           }
			      
			}	
			}
		return err;
	}

	
}
