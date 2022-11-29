package com.shaic.claim.OMPclaimprocesschecker.search;

import javax.annotation.PostConstruct;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class OMPClaimProcessCheckerUI extends SearchComponent<OMPClaimProcessCheckerFormDto>{
	
	OMPClaimProcessCheckerFormDto	searchDto;
	
	
	private TextField policyNumber;
	private TextField intimationNumber;
	private TextField claimNumber;
	private TextField passportNumber;
	private ComboBox cmbtype;
	
	@PostConstruct
	public void init() {
initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Claim Process -Checker");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		intimationNumber = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		policyNumber = binder.buildAndBind("Policy No","policyNo",TextField.class);
		claimNumber = binder.buildAndBind("Claim No","claimno",TextField.class);
		passportNumber = binder.buildAndBind("Pass Port No","passportno",TextField.class);
		cmbtype = binder.buildAndBind("Type","claimtype",ComboBox.class);
	//	intimationDateField = binder.buildAndBind("Intimation Date","intimationDate",PopupDateField.class);
	//	lossDateField = binder.buildAndBind("Loss Date","lossnDate",PopupDateField.class);
		
		FormLayout formLayoutLeft = new FormLayout(intimationNumber,policyNumber,passportNumber);
		FormLayout formLayoutReight = new FormLayout(claimNumber,cmbtype);	
	    Label dummy = new Label();
	   // dummy.setWidth("10px");
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutReight);
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(btnSearch, "top:160.0px;left:220.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:160.0px;left:329.0px;");
		
		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("650px");
		 mainVerticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 
		 absoluteLayout_3.setHeight("224px");
		
		addListener();
	setDropDownValues();
		return mainVerticalLayout;
	}
	
private void initBinder()
	
	{
		this.binder = new BeanFieldGroup<OMPClaimProcessCheckerFormDto>(OMPClaimProcessCheckerFormDto.class);
		this.binder.setItemDataSource(new OMPClaimProcessCheckerFormDto());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
public void setDropDownValues(){
//	BeanItemContainer<SpecialSelectValue> productNameValue = masterService.getContainerForProduct();
//	cmbtype.setContainerDataSource(productNameValue);
//	cmbtype.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//	cmbtype.setItemCaptionPropertyId("specialValue");
}
public void setType(BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage, BeanItemContainer<SelectValue> selectValueForUploadedDocument) {
/*	eventCodeType.setContainerDataSource(parameter);
	eventCodeType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	eventCodeType.setItemCaptionPropertyId("value");
	
	cmbProductCode.setContainerDataSource(selectValueForPriority);
	cmbProductCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	cmbProductCode.setItemCaptionPropertyId("value");
	
	BeanItemContainer<SelectValue> selectValueForType = SHAUtils.getSelectValueForType();
	
	*/
	
	
}

}
