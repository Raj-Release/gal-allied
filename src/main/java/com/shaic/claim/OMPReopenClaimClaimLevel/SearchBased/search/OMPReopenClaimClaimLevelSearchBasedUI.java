package com.shaic.claim.OMPReopenClaimClaimLevel.SearchBased.search;

import javax.annotation.PostConstruct;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

@UIScoped
@SuppressWarnings("serial")
public class OMPReopenClaimClaimLevelSearchBasedUI extends SearchComponent<OMPReopenClaimClaimLevelSearchBasedFormDto>{
	
	private OMPReopenClaimClaimLevelSearchBasedFormDto searchDto;
	private OMPReopenClaimClaimLevelSearchBasedDetailTable searchTable;
	
	private TextField intimationNumber;
	private TextField policyNumber;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Re-open Claim -Claim Level (Search Based)");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}

	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		intimationNumber = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		policyNumber = binder.buildAndBind("Policy No","policyNo",TextField.class);
	//	claimNumber = binder.buildAndBind("Claim No","claimno",TextField.class);
		
		FormLayout formLayoutLeft = new FormLayout(intimationNumber);
		FormLayout formLayoutReight = new FormLayout(policyNumber);	
	    
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutReight);
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(btnSearch, "top:160.0px;left:220.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:160.0px;left:329.0px;");
//		VerticalLayout verticalLayout = new VerticalLayout();
		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("650px");
		 mainVerticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("224px");
		 
			addListener();
			return mainVerticalLayout;

		}
	private void initBinder()

	{
		this.binder = new BeanFieldGroup<OMPReopenClaimClaimLevelSearchBasedFormDto>(OMPReopenClaimClaimLevelSearchBasedFormDto.class);
		this.binder.setItemDataSource(new OMPReopenClaimClaimLevelSearchBasedFormDto());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
}
