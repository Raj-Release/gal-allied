package com.shaic.reimburement.specialapprover.processclaim.search;

import javax.annotation.PostConstruct;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

/**
 * @author ntv.narenj
 *
 */
public class SearchProcessClaimForm extends SearchComponent<SearchProcessClaimFormDTO>{
	
	
	
	private TextField txtIntimationNo;
	private TextField txtPolicyNo;
	private ComboBox cbxCPUCode;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Process Claim (Reffered for Special Approval)");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout mainVerticalLayout(){
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 VerticalLayout verticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() verticalLayout.setImmediate(false);
		 verticalLayout.setWidth("100.0%");
		 verticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("139px");
		 
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		
		
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		cbxCPUCode = binder.buildAndBind("CPU Code","cpuCode",ComboBox.class);
		txtPolicyNo = binder.buildAndBind("Policy Number","policyNo",TextField.class);
		cbxCPUCode.setNullSelectionAllowed(false);
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo,cbxCPUCode);
		formLayoutLeft.setMargin(true);
		formLayoutLeft.setSpacing(true);
		FormLayout formLayoutReight = new FormLayout(txtPolicyNo);	
		formLayoutReight.setMargin(true);
		formLayoutReight.setSpacing(true);
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutReight);
//<<<<<<< HEAD
		absoluteLayout_3.addComponent(fieldLayout);
		absoluteLayout_3
		.addComponent(btnSearch, "top:100.0px;left:250.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:100.0px;left:359.0px;");
		
		verticalLayout.addComponent(absoluteLayout_3);
//=======
//		fieldLayout.setMargin(true);
//		fieldLayout.setWidth("65%");
//		fieldLayout.setSpacing(true);
//		fieldLayout.setHeight("140px");
//		HorizontalLayout buttonlayout = new HorizontalLayout(btnSearch,btnReset);
//		buttonlayout.setWidth("70%");
//		buttonlayout.setComponentAlignment(btnSearch,  Alignment.MIDDLE_RIGHT);
//		buttonlayout.setSpacing(true);
//		buttonlayout.setMargin(false);
//		
//		mainVerticalLayout.addComponent(fieldLayout);
//		mainVerticalLayout.setComponentAlignment(fieldLayout, Alignment.BOTTOM_LEFT);
//		mainVerticalLayout.addComponent(buttonlayout);
//		mainVerticalLayout.setComponentAlignment(buttonlayout, Alignment.MIDDLE_LEFT);
//		mainVerticalLayout.setWidth("100%");
//		mainVerticalLayout.setHeight("190px");
//	//	mainVerticalLayout.setMargin(true);
//		mainVerticalLayout.setSpacing(true);
//		addListener();
//>>>>>>> 87d0467994a0210209ef6df4cdbb2ceb0b50f3ed
		
		addListener();
		mainVerticalLayout = new VerticalLayout(verticalLayout);
		return mainVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchProcessClaimFormDTO>(SearchProcessClaimFormDTO.class);
		this.binder.setItemDataSource(new SearchProcessClaimFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}	
	
	public void setCPUCode(BeanItemContainer<SelectValue> parameter){
		cbxCPUCode.setContainerDataSource(parameter);
		cbxCPUCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxCPUCode.setItemCaptionPropertyId("value");
	}
}
