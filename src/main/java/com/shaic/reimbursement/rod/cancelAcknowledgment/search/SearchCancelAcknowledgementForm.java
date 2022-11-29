package com.shaic.reimbursement.rod.cancelAcknowledgment.search;

import javax.annotation.PostConstruct;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.shaic.reimbursement.rod.createrod.search.SearchCreateRODFormDTO;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class SearchCancelAcknowledgementForm extends SearchComponent<SearchCreateRODFormDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TextField txtIntimationNo;
	private TextField txtPolicyNo;
	private TextField txtAcknowledgementNumber;
	
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Cancel Acknowledgement");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		txtAcknowledgementNumber = binder.buildAndBind("Acknowledgment Number","acknowledgementNo",TextField.class);
		txtPolicyNo = binder.buildAndBind("Policy Number","policyNo",TextField.class);
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo,txtAcknowledgementNumber);
		FormLayout formLayoutReight = new FormLayout(txtPolicyNo);	
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutReight);
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
	//	mainVerticalLayout.setMargin(true);
		mainVerticalLayout.setSpacing(true);
		
		addListener();
		
		return mainVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchCreateRODFormDTO>(SearchCreateRODFormDTO.class);
		this.binder.setItemDataSource(new SearchCreateRODFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	

}
