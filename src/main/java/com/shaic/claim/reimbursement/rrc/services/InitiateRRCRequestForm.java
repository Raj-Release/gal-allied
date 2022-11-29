package com.shaic.claim.reimbursement.rrc.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;

import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class InitiateRRCRequestForm extends SearchComponent<InitiateRRCRequestFormDTO>{

	
	private static final long serialVersionUID = -4088910426204201267L;
	private TextField txtIntimationNo;
	private TextField txtPolicyNo;

	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Initiate RRC Request");
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
		validator.setRegExp("^[a-z A-Z 0-9 /.]*$");
		validator.setPreventInvalidTyping(true);
		
		txtPolicyNo = binder.buildAndBind("Policy No", "policyNo", TextField.class);
		//txtPolicyNo.setMaxLength(25);
		CSValidator policyValidator = new CSValidator();
		policyValidator.extend(txtPolicyNo);
		policyValidator.setRegExp("^[a-z A-Z 0-9 /.]*$");
		policyValidator.setPreventInvalidTyping(true);
		
		
		
		
		FormLayout formLayoutLeft = new FormLayout(txtPolicyNo);
		formLayoutLeft.setSpacing(true);
		FormLayout formLayoutRight = new FormLayout(txtIntimationNo);
		formLayoutRight.setSpacing(true);
		
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight);
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(btnSearch, "top:170.0px;left:220.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:170.0px;left:329.0px;");
		
		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("650px");
		 mainVerticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 
		 absoluteLayout_3.setHeight("230px");
		
		addListener();
		
		return mainVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<InitiateRRCRequestFormDTO>(InitiateRRCRequestFormDTO.class);
		this.binder.setItemDataSource(new InitiateRRCRequestFormDTO());
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
	public String validate()
	{
		//String err = "";
		
		if((txtIntimationNo.getValue()==null || txtIntimationNo.getValue().equals("")) && (txtPolicyNo.getValue() == null || txtPolicyNo.getValue().equals("")))
		{
		
			return "Any one of the field is Mandatory";
		}		
		
				
		return null;
		
	}
	
	
}
