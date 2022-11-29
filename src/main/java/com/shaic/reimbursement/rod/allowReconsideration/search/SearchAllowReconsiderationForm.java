package com.shaic.reimbursement.rod.allowReconsideration.search;

import javax.annotation.PostConstruct;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class SearchAllowReconsiderationForm extends SearchComponent<SearchAllowReconsiderationDTO>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TextField txtIntimationNo;
	private TextField txtRodNumber;
	
	@PostConstruct
	public void init(){
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Allow Reconsideration");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout mainVerticalLayout() {
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		txtRodNumber = binder.buildAndBind("ROD Number","rodNo",TextField.class);
		
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo);
		FormLayout formLayoutRight = new FormLayout(txtRodNumber);
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight);
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");
		fieldLayout.setSpacing(true);
		fieldLayout.setHeight("140px");
		HorizontalLayout buttonlayout = new HorizontalLayout(btnSearch,btnReset);
		buttonlayout.setWidth("100%");
		buttonlayout.setComponentAlignment(btnSearch,  Alignment.MIDDLE_RIGHT);
		buttonlayout.setSpacing(true);
		buttonlayout.setMargin(false);
		mainVerticalLayout.addComponent(fieldLayout);
		mainVerticalLayout.setComponentAlignment(fieldLayout, Alignment.BOTTOM_LEFT);
		mainVerticalLayout.addComponent(buttonlayout);
		mainVerticalLayout.setComponentAlignment(buttonlayout, Alignment.MIDDLE_LEFT);
		mainVerticalLayout.setWidth("100%");
		mainVerticalLayout.setHeight("190px");
		mainVerticalLayout.setSpacing(true);
		
		addListener();
		
		return mainVerticalLayout;
	}
	
	private void initBinder() {
		this.binder = new BeanFieldGroup<SearchAllowReconsiderationDTO>(SearchAllowReconsiderationDTO.class);
		this.binder.setItemDataSource(new SearchAllowReconsiderationDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
	}

}
