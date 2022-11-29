package com.shaic.claim.settlementpullback.searchbatchpendingpullback;

import javax.annotation.PostConstruct;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class SearchLotPullBackForm extends SearchComponent<SearchLotPullBackFormDTO>{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TextField txtIntimationNo;
	private TextField txtPolicyNo;
	
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("UNDO LOT");
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
		absoluteLayout_3.setHeight("150px");
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		txtPolicyNo = binder.buildAndBind("Policy Number","policyNo",TextField.class);
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo);
		FormLayout formLayoutReight = new FormLayout(txtPolicyNo);	
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft);
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");
		fieldLayout.setSpacing(true);
		
		absoluteLayout_3.addComponent(fieldLayout);
		
		absoluteLayout_3
		.addComponent(btnSearch, "top:90.0px;left:250.0px;");
		
		absoluteLayout_3.addComponent(btnReset, "top:90.0px;left:359.0px;");
		mainVerticalLayout.addComponent(absoluteLayout_3);
		
		addListener();
		
		return mainVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchLotPullBackFormDTO>(SearchLotPullBackFormDTO.class);
		this.binder.setItemDataSource(new SearchLotPullBackFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

		
	public Boolean validatePage(){
		Boolean isValid = true;
	
		if(((txtIntimationNo.getValue() == null || txtIntimationNo.getValue().equals("")))){
			isValid = false;
		}
		return isValid;
	}

}
