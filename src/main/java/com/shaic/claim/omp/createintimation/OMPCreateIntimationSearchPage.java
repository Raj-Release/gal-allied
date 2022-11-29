package com.shaic.claim.omp.createintimation;


import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.cmn.login.ImsUser;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@UIScoped
@SuppressWarnings("serial")
public class OMPCreateIntimationSearchPage extends SearchComponent<OMPCreateIntimationFormDTO> {
	
	//private OMPCreateIntimationFormDto searchDto;
	
	@Inject
    private OMPSearchIntimationDetailTable searchResultTable;
	
	@Inject
    private OMPCreateIntimationDetailTable createResultTable;
	
	private ComboBox cmbIntimationType;
	private TextField intimationNumber;
	private ComboBox cmbIntimationStatus;
	private TextField claimNumber;	
	private TextField policyNumber;
	private TextField passportNumber;
	private TextField insuredName;

	private Button resetBtn;
	
	///private  Button button;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Search & Create Intimation");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}

	public VerticalLayout mainVerticalLayout(){
		mainVerticalLayout = new VerticalLayout();
		cmbIntimationType = binder.buildAndBind("Type","type",ComboBox.class);
		intimationNumber = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		cmbIntimationStatus = binder.buildAndBind("Intimation Status","intimationStatus",ComboBox.class);
		claimNumber = binder.buildAndBind("Claim No", "claimNumber", TextField.class);
		policyNumber = binder.buildAndBind("Policy Number","policyNo",TextField.class);
		passportNumber = binder.buildAndBind("Passport Number", "passportno", TextField.class);
		insuredName = binder.buildAndBind("Insured Name ", "insuredName", TextField.class);
		passportNumber.setReadOnly(true);// R1276 - Making Passport field as ReadOnly as per User Comments.		
		FormLayout formLayoutLeft = new FormLayout(cmbIntimationType,cmbIntimationStatus,policyNumber,insuredName);
		FormLayout formLayoutRight = new FormLayout(intimationNumber,claimNumber,passportNumber);	
	    
		resetBtn = new Button("Reset");
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight);
		
		fieldLayout.setSpacing(true);
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");
		
		AbsoluteLayout searchIntimation_layout =  new AbsoluteLayout();
		searchIntimation_layout.addComponent(fieldLayout);
		searchIntimation_layout.addComponent(btnSearch, "top:160.0px;left:220.0px;");
		searchIntimation_layout.addComponent(resetBtn, "top:160.0px;left:329.0px;");
				
		mainVerticalLayout.addComponent(searchIntimation_layout);
		//Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		mainVerticalLayout.setWidth("650px");
		mainVerticalLayout.setMargin(false);	
		
		//Vaadin8-setImmediate() searchIntimation_layout.setImmediate(false);
		searchIntimation_layout.setWidth("100.0%");
		searchIntimation_layout.setHeight("224px");
		
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnSearch.setImmediate(true);
		btnSearch.setWidth("-1px");
		btnSearch.setTabIndex(12);
		btnSearch.setHeight("-1px");
		btnSearch.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		addListener();
		setDropDownValues();
		
		return mainVerticalLayout;
	}
	
	
	public void addListener() {
		
		btnSearch.removeClickListener(this);
		btnSearch.addClickListener(this);

//		btnSearch.addClickListener(new Button.ClickListener() {
//			private static final long serialVersionUID = 1L;
//			@Override
//			public void buttonClick(ClickEvent event) {
//				btnSearch.setEnabled(true);
//				searchable.doSearch();			
//			}
//		});
		
		resetBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				resetAlltheValues();	
			}
		});
		
		cmbIntimationType.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(cmbIntimationType.getValue() != null){
					if (StringUtils.isNotBlank(cmbIntimationType.getValue().toString())) {
						if (cmbIntimationType.getValue().toString().equals(SHAConstants.SEARCH_INTIMATION.toString())) {
							intimationNumber.setVisible(true);
							cmbIntimationStatus.setVisible(false);
							claimNumber.setVisible(true);
							insuredName.setVisible(true);
							passportNumber.setVisible(false);
							
							searchResultTable.init("", false, true);
							searchResultTable.setSubmitTableHeader();
							
						} else if (cmbIntimationType.getValue().toString().equals(SHAConstants.CREATE_INTIMATION.toString())) {
							intimationNumber.setVisible(false);
							cmbIntimationStatus.setVisible(false);
							claimNumber.setVisible(false);
							insuredName.setVisible(false);
							passportNumber.setVisible(true);
							createResultTable.init("", false, false);
							createResultTable.setSubmitTableHeader();
						} 
					}
				}
			}

		});
		
	}

	private void initBinder(){
		this.binder = new BeanFieldGroup<OMPCreateIntimationFormDTO>(OMPCreateIntimationFormDTO.class);
		this.binder.setItemDataSource(new OMPCreateIntimationFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public void setDropDownValues(){
		BeanItemContainer<SelectValue> ompTypeSelectValue = SHAUtils.getOMPSelectValueForType();
		cmbIntimationType.setContainerDataSource(ompTypeSelectValue);
		cmbIntimationType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbIntimationType.setItemCaptionPropertyId("value");
		
		
		BeanItemContainer<SelectValue> ompStatusSelectValue = SHAUtils.getOMPSelectValueForStatus();
		cmbIntimationStatus.setContainerDataSource(ompStatusSelectValue);
		cmbIntimationStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbIntimationStatus.setItemCaptionPropertyId("value");

		setDefaultValue();
	}
	public void setPassportFieldEnable(boolean enable){	
		passportNumber.setReadOnly(false);
	}
	public void resetAlltheValues(){
			intimationNumber.setValue("");
			claimNumber.setValue("");
			policyNumber.setValue("");
			passportNumber.setReadOnly(false);
			passportNumber.setValue("");
			passportNumber.setReadOnly(true);
			insuredName.setValue("");
			//setDefaultValue();
			if(searchResultTable != null){
				searchResultTable.removeRow();
			}
//			policyNumber.setValue("");
//			passportNumber.setValue("");
			if(createResultTable != null){
				createResultTable.removeRow();
			}
	}

	
	@SuppressWarnings("unchecked")
	public void setDefaultValue(){
		List<SelectValue> defaultType = (List<SelectValue>)cmbIntimationType.getContainerDataSource().getItemIds();
		cmbIntimationType.setValue(defaultType.get(0));
		List<SelectValue> defaultStatus = (List<SelectValue>) cmbIntimationStatus.getContainerDataSource().getItemIds();
		cmbIntimationStatus.setValue(defaultStatus.get(0));
	}
	
	public String getdisplayType(){
		return (cmbIntimationType.getValue() == null)?"":cmbIntimationType.getValue().toString();
	}
	
	public String doPolicyValidation(){
		//String policyNo = cmbIntimationType.getValue().toString();
		return (StringUtils.isBlank(policyNumber.getValue()))? "No PolicyNumber Entered":policyNumber.getValue();
	}
	
	public String doValidation(){
	if(StringUtils.isBlank(policyNumber.getValue()) && StringUtils.isBlank(intimationNumber.getValue()) 
			&& StringUtils.isBlank(insuredName.getValue()) && StringUtils.isBlank(claimNumber.getValue())){
		
		return ("No value");
	}
	return ("value");
		
	}
	public void refresh()
	{
		System.out.println("---inside the refresh----");
		resetAlltheValues();
	}
	
}

			

				
