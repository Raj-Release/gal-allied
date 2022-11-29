package com.shaic.claim.reports.dispatchDetailsReport;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class DispatchDetailsReportForm extends SearchComponent<DispatchDetailsReportFormDTO> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TextField txtIntimationNo;
	
	private ComboBox cmbUpdateType;
	
	private TextField rodNumber;
	
	private TextField awsNumber;
	
	private PopupDateField fromDate;
	
	private PopupDateField toDate;
	
	private TextField batchNumber;
	
	private BeanItemContainer<SelectValue> updateType;
	
	@EJB
	private MasterService masterService;
	
	private Button resetButton;
	
	private DispatchDetailsReportFormDTO bean;

	@PostConstruct
	public void init() {
		initBinder();
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Dispatch Details Report");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}

	private void initBinder() {
		this.binder = new BeanFieldGroup<DispatchDetailsReportFormDTO>(DispatchDetailsReportFormDTO.class);
		this.binder.setItemDataSource(new DispatchDetailsReportFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
	}

	private Component mainVerticalLayout() {

		 bean = new DispatchDetailsReportFormDTO();
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 mainVerticalLayout = new VerticalLayout();
		 mainVerticalLayout.setWidth("100.0%");
		 mainVerticalLayout.setMargin(false);		 
		 absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("200px");
		
		cmbUpdateType = binder.buildAndBind("Type", "updateType",ComboBox.class);
		cmbUpdateType.setWidth("190px");
		cmbUpdateType.setTabIndex(8);
		cmbUpdateType.setHeight("-1px");

		txtIntimationNo = binder.buildAndBind("Intimation Number", "intimationNo", TextField.class);
		txtIntimationNo.setWidth("190px");
		txtIntimationNo.setTabIndex(8);
		txtIntimationNo.setHeight("-1px");
		txtIntimationNo.setMaxLength(30);
		CSValidator intimationNumValidator = new CSValidator();
		intimationNumValidator.extend(txtIntimationNo);
		intimationNumValidator.setPreventInvalidTyping(true);
		intimationNumValidator.setRegExp("^[a-zA-Z 0-9/]*$");
		
		rodNumber = binder.buildAndBind("ROD Number", "rodNumber", TextField.class);
		rodNumber.setWidth("190px");
		rodNumber.setTabIndex(8);
		rodNumber.setHeight("-1px");
		rodNumber.setMaxLength(30);
		CSValidator rodNumberValidator = new CSValidator();
		rodNumberValidator.extend(rodNumber);
		rodNumberValidator.setPreventInvalidTyping(true);
		rodNumberValidator.setRegExp("^[a-zA-Z 0-9/]*$");
		
		awsNumber = binder.buildAndBind("AWB Number", "awsNumber", TextField.class);
		awsNumber.setWidth("190px");
		awsNumber.setTabIndex(8);
		awsNumber.setHeight("-1px");
		awsNumber.setMaxLength(30);
		CSValidator awsNumberValidator = new CSValidator();
		awsNumberValidator.extend(awsNumber);
		awsNumberValidator.setPreventInvalidTyping(true);
		awsNumberValidator.setRegExp("^[a-zA-Z 0-9]*$");		
				
		fromDate = (PopupDateField) binder.buildAndBind("From Date","fromDate", PopupDateField.class);
		fromDate.setWidth("190px");
		
		toDate = (PopupDateField)binder.buildAndBind("To Date","toDate",PopupDateField.class);
		toDate.setWidth("190px");
		
		batchNumber = binder.buildAndBind("Batch Number", "batchNumber", TextField.class);
		batchNumber.setWidth("190px");
		batchNumber.setTabIndex(8);
		batchNumber.setHeight("-1px");
		batchNumber.setMaxLength(30);
		CSValidator batchNumberValidator = new CSValidator();
		batchNumberValidator.extend(batchNumber);
		batchNumberValidator.setPreventInvalidTyping(true);
		batchNumberValidator.setRegExp("^[0-9]*$");
		
		FormLayout formLayoutOne = new FormLayout(cmbUpdateType,awsNumber);
		formLayoutOne.setSpacing(true);
		formLayoutOne.setMargin(true);
		
		FormLayout formLayoutTwo = new FormLayout(txtIntimationNo,fromDate);
		formLayoutTwo.setSpacing(true);
		formLayoutTwo.setMargin(true);
		
		FormLayout formLayoutThree = new FormLayout(rodNumber,toDate);
		formLayoutThree.setSpacing(true);
		formLayoutThree.setMargin(true);
		
		FormLayout formLayoutfour = new FormLayout(batchNumber);
		formLayoutfour.setSpacing(true);
		formLayoutfour.setMargin(true);		
		
		resetButton = new Button();
		resetButton.setCaption("Reset");
		resetButton.setWidth("-1px");
		resetButton.setHeight("-1px");
		resetButton.setTabIndex(13);
		resetButton.setStyleName(ValoTheme.BUTTON_DANGER);	
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutOne,formLayoutTwo,formLayoutThree,formLayoutfour);
		fieldLayout.setMargin(true);
		fieldLayout.setSpacing(true);
		absoluteLayout_3.addComponent(fieldLayout);
		absoluteLayout_3.addComponent(btnGenerateReport, "top:135.0px;left:397.0px;");
		absoluteLayout_3.addComponent(resetButton, "top:135.0px;left:527.0px;");
		mainVerticalLayout.addComponent(absoluteLayout_3);
		
		setDropDownValues();
		addListenerForCmb();
		addListener();
		return mainVerticalLayout;
	
	}

	private void setDropDownValues() {
		updateType = masterService.getSelectValueContainer(ReferenceTable.DISPATCH_BULK_UPLOAD_TYPE);
		cmbUpdateType.setContainerDataSource(updateType);
		cmbUpdateType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbUpdateType.setItemCaptionPropertyId("value");
		if(updateType !=null){
			SelectValue type = updateType.getItemIds().get(0);
			cmbUpdateType.setValue(type);
		}
	}
	
	public void resetDispatchSearchFields(){
		cmbUpdateType.clear();
		txtIntimationNo.clear();
		rodNumber.clear();
		awsNumber.clear();
		fromDate.clear();
		toDate.clear();
		batchNumber.clear();
	}
	
	@SuppressWarnings("deprecation")
	public void addListenerForCmb()
	{

		cmbUpdateType.addValueChangeListener(new ValueChangeListener() {		
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				SelectValue selValue = (SelectValue)cmbUpdateType.getValue();
				if(null != selValue && null != selValue.getId())
				{
					fireViewEvent(DispatchDetailsReportPresenter.UPDATE_TYPE_CHANGES,selValue.getId());
				}
			}
		});
		
		resetButton.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {

				fireViewEvent(DispatchDetailsReportPresenter.RESET_BUTTON_CLICK_UPDATE, null);
			}
		});
	}

}
