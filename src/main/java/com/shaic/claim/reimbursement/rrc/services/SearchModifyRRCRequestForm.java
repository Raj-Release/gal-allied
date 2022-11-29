/**
 * 
 */
package com.shaic.claim.reimbursement.rrc.services;

import javax.annotation.PostConstruct;

import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

/**
 * @author ntv.vijayar
 *
 */
public class SearchModifyRRCRequestForm  extends SearchComponent<SearchModifyRRCRequestFormDTO> {
	
	private static final long serialVersionUID = -4088910426204201267L;
	private TextField txtIntimationNo;
	private TextField txtRRCRequestNo;
	private ComboBox cmbCpu;
	private ComboBox cmbRRCRequestType;
	private ComboBox cmbEligiblityType;
	private DateField fromDate;
	private DateField toDate;
	private Label dummyField;
	
	
	private BeanItemContainer<SelectValue> cpuCode;
	private BeanItemContainer<SelectValue> rrcRequestType;
	
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Modify RRC Request");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
	//	txtIntimationNo.setMaxLength(25);
		CSValidator validator = new CSValidator();
		validator.extend(txtIntimationNo);
		validator.setRegExp("^[a-z A-Z 0-9 / .]*$");
		validator.setPreventInvalidTyping(true);
		
		txtRRCRequestNo = binder.buildAndBind("RRC Request No","rrcRequestNo",TextField.class);
	//	txtRRCRequestNo.setMaxLength(25);
		CSValidator rrcValidator = new CSValidator();
		rrcValidator.extend(txtRRCRequestNo);
		rrcValidator.setRegExp("^[a-z A-Z 0-9 / .]*$");
		rrcValidator.setPreventInvalidTyping(true);
		
		cmbCpu = binder.buildAndBind("CPU","cpu",ComboBox.class);
		//Vaadin8-setImmediate() cmbCpu.setImmediate(true);
		
		
		
		cmbRRCRequestType = binder.buildAndBind("RRC Request Type","rrcRequestType",ComboBox.class);
		cmbEligiblityType = binder.buildAndBind("Eligibility Type","eligibilityType",ComboBox.class);
		fromDate = binder.buildAndBind("From Date","fromDate",DateField.class);
		toDate = binder.buildAndBind("To Date","toDate",DateField.class);
		dummyField = new Label();
		dummyField.setVisible(true);
		dummyField.setReadOnly(true);
		
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo,cmbCpu,dummyField,fromDate);
		formLayoutLeft.setSpacing(true);
		FormLayout formLayoutRight = new FormLayout(txtRRCRequestNo,cmbRRCRequestType,cmbEligiblityType,toDate);
		formLayoutRight.setSpacing(true);
		
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight);

		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(btnSearch, "top:175.0px;left:220.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:175.0px;left:329.0px;");
		
		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("650px");
		 mainVerticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 
		 absoluteLayout_3.setHeight("240px");
		addListener();
		
		return mainVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchModifyRRCRequestFormDTO>(SearchModifyRRCRequestFormDTO.class);
		this.binder.setItemDataSource(new SearchModifyRRCRequestFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public void setDropDownValues(BeanItemContainer<SelectValue> cpu,
			BeanItemContainer<SelectValue> rrcRequestType, BeanItemContainer<SelectValue> rrcEligiblity) 
	{
		cmbCpu.setContainerDataSource(cpu);
		cmbCpu.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCpu.setItemCaptionPropertyId("value");
		
		
		cmbRRCRequestType.setContainerDataSource(rrcRequestType);
		cmbRRCRequestType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbRRCRequestType.setItemCaptionPropertyId("value");
		
		cmbEligiblityType.setContainerDataSource(rrcEligiblity);
		cmbEligiblityType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbEligiblityType.setItemCaptionPropertyId("value");
				
	}	
	
	/*private void cbxhospitalListener(){
		cbxhospitalType.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
			
				
				
				if(cbxhospitalType.getValue() != null){
					System.out.println("ggggggggggggggggggggggg"+cbxhospitalType.getValue());
				if(  ReferenceTable.HOSPITAL_NETWORK.equals(cbxhospitalType.getValue().toString())){
					cbxNetworkHospType.setContainerDataSource(networkHospitalType);
					cbxNetworkHospType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					cbxNetworkHospType.setItemCaptionPropertyId("value");
				}else{
					cbxNetworkHospType.setContainerDataSource(null);
				}
			}else{
					cbxNetworkHospType.setContainerDataSource(null);
				}
			}
			});
	}
	*/

}

