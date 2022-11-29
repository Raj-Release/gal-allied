package com.shaic.reimbursement.manageclaim.searchClaimwiseApproval;

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

public class SearchClaimWiseAllowApprovalForm extends SearchComponent<SearchClaimWiseAllowApprovalFormDto>{
	
	private TextField txtIntimationNo;
	private TextField txtClaimNo;
	private TextField txtPolicyNo;
	
	private Panel mainpanel;
	
	@PostConstruct
	public void init(){
		initBinder();
		
		mainpanel = new Panel();
		mainpanel.addStyleName("panelHeader");
		mainpanel.addStyleName("g-search-panel");
		mainpanel.setCaption("Claim Wise Allow Approval");
		mainpanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainpanel);
	}
	
	public VerticalLayout mainVerticalLayout(){

		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		txtClaimNo = binder.buildAndBind("Claim No","claimNo",TextField.class);
		txtPolicyNo = binder.buildAndBind("Policy Number","policyNo",TextField.class);
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo,txtPolicyNo);
		FormLayout formLayoutReight = new FormLayout(txtClaimNo);	
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutReight);

		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");		
		AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(btnSearch, "top:120.0px;left:220.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:120.0px;left:329.0px;");
		
		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		//Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		mainVerticalLayout.setWidth("650px");
		mainVerticalLayout.setMargin(false);		 
		//Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		absoluteLayout_3.setWidth("100.0%");
		absoluteLayout_3.setHeight("200px");
		addListener();
		
		return mainVerticalLayout;
	
	}
	
	private void initBinder(){
		this.binder = new BeanFieldGroup<SearchClaimWiseAllowApprovalFormDto>(SearchClaimWiseAllowApprovalFormDto.class);
		this.binder.setItemDataSource( new SearchClaimWiseAllowApprovalFormDto());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

}
