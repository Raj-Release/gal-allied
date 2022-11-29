package com.shaic.claim.reimbursement.talktalktalk;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;

import org.vaadin.csvalidation.CSValidator;

import com.alert.util.ButtonOption;
import com.alert.util.MessageBox;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class InitiateTalkTalkTalkForm extends SearchComponent<InitiateTalkTalkTalkFormDTO>{

	
	private static final long serialVersionUID = -4088910426204201267L;
	private TextField txtIntimationNo;
	
	private ComboBox yearComboBox;

	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Initiate TALK TALK TALK");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	@SuppressWarnings("deprecation")
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();

		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		//	txtIntimationNo.setMaxLength(25);
		CSValidator validator = new CSValidator();
		validator.extend(txtIntimationNo);
		validator.setRegExp("^[a-z A-Z 0-9 /.]*$");
		validator.setPreventInvalidTyping(true);
		txtIntimationNo.setRequired(true);

		yearComboBox = binder.buildAndBind("Year", "intimationyear", ComboBox.class);
		yearComboBox.setRequired(true);
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo);
		FormLayout formLayoutRight = new FormLayout(yearComboBox);

		BeanItemContainer<SelectValue> policyYearValues = getPolicyYearValues();
		yearComboBox.setContainerDataSource(policyYearValues);
		yearComboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		yearComboBox.setItemCaptionPropertyId("value");
		yearComboBox.setId("intimationyear");
		List<SelectValue> itemIds2 = policyYearValues.getItemIds();
		if(itemIds2 != null && ! itemIds2.isEmpty()){
			SelectValue selectValue = itemIds2.get(0);
			yearComboBox.setValue(selectValue);
		}

		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight);
		fieldLayout.setMargin(true);
		fieldLayout.setSpacing(true);
		fieldLayout.setWidth("100%");		
		AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(btnSearch, "top:67.0px;left:335.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:67.0px;left:410.0px;");


		mainVerticalLayout.addComponent(absoluteLayout_3);
		mainVerticalLayout.setMargin(false);		 
		absoluteLayout_3.setWidth("100.0%");
		absoluteLayout_3.setHeight("155px");
		addListener();

		return mainVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<InitiateTalkTalkTalkFormDTO>(InitiateTalkTalkTalkFormDTO.class);
		this.binder.setItemDataSource(new InitiateTalkTalkTalkFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public BeanItemContainer<SelectValue> getPolicyYearValues(){
		
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		Calendar instance = Calendar.getInstance();
		//ADDED FOR FY APR - MAR
		int month = instance.get(Calendar.MONTH);
		if(month >= 3){
			instance.add(Calendar.YEAR, 1);
		}
		/*instance.add(Calendar.YEAR, 1);*/
		int intYear = instance.get(Calendar.YEAR);
		Long year = Long.valueOf(intYear);
		for(Long i= year;i>=year-13;i--){
			SelectValue selectValue = new SelectValue();
			Long j = i-1;
			selectValue.setId(j);
			selectValue.setValue(""+i.intValue());
			selectValueList.add(selectValue);
		}
		container.addAll(selectValueList);
		
		return container;
	}
	
	/*public String validate()
	{
		//String err = "";
		
		if((txtIntimationNo.getValue()==null || txtIntimationNo.getValue().equals("")))
		{
		
			return "A";
		}		
		
				
		return null;
		
	}
	*/
	public Boolean validate() {

		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();

		if ( txtIntimationNo!= null
				&& txtIntimationNo.getValue() == null
				|| (txtIntimationNo.getValue() != null && txtIntimationNo.getValue().length() == 0)) {
			hasError = true;
			eMsg.append("Please Enter Intimation Number </br>");
		}
		if (yearComboBox != null && yearComboBox.getValue() == null) {
			hasError = true;
			eMsg.append("Please Select Year </br>");
		}	
		if (hasError) {
			MessageBox.createError()
			.withCaptionCust("Errors").withHtmlMessage(eMsg.toString())
			.withOkButton(ButtonOption.caption("OK")).open();

			hasError = true;
		}
		return !hasError;
	}
	
public void setDropDownValues() {
		
		BeanItemContainer<SelectValue> policyYearValues = getPolicyYearValues();
		yearComboBox.setContainerDataSource(policyYearValues);
		yearComboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		yearComboBox.setItemCaptionPropertyId("value");
		yearComboBox.setId("intimationyear");
		List<SelectValue> itemIds2 = policyYearValues.getItemIds();
		if(itemIds2 != null && ! itemIds2.isEmpty()){
			SelectValue selectValue = itemIds2.get(0);
			yearComboBox.setValue(selectValue);
		}
	}
	
}

