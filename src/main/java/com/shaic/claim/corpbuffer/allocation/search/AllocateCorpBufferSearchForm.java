package com.shaic.claim.corpbuffer.allocation.search;

import javax.annotation.PostConstruct;

import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class AllocateCorpBufferSearchForm extends SearchComponent<AllocateCorpBufferFormDTO> {

	private static final long serialVersionUID = 1L;
	
	private AllocateCorpBufferFormDTO dto =  new AllocateCorpBufferFormDTO();
	
	private TextField txtIntimationNo;
	
	private TextField txtPolicyNo;
	
	@PostConstruct
	public void init() {
		
	}
	
	public void initView(AllocateCorpBufferFormDTO dto) {
		initBinder(dto);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		setSizeFull();
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Allocate Corporate Buffer");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	private void initBinder(AllocateCorpBufferFormDTO dto) {		
		if (dto != null) {
			this.dto = dto;
		} else {
			this.dto = new AllocateCorpBufferFormDTO();
		}
		
		this.binder = new BeanFieldGroup<AllocateCorpBufferFormDTO>(AllocateCorpBufferFormDTO.class);
		this.binder.setItemDataSource(this.dto);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		//Intimation No
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		//Vaadin8-setImmediate() txtIntimationNo.setImmediate(false);
		txtIntimationNo.setWidth("160px");
		txtIntimationNo.setTabIndex(8);
		txtIntimationNo.setHeight("-1px");
		txtIntimationNo.setMaxLength(30);
		CSValidator intimationNumValidator = new CSValidator();
		intimationNumValidator.extend(txtIntimationNo);
		intimationNumValidator.setPreventInvalidTyping(true);
		intimationNumValidator.setRegExp("^[a-zA-Z 0-9/]*$");
		
		//Policy No
		txtPolicyNo = binder.buildAndBind("Policy Number", "policyNo", TextField.class);
		//Vaadin8-setImmediate() txtPolicyNo.setImmediate(false);
		txtPolicyNo.setWidth("160px");
		txtPolicyNo.setTabIndex(8);
		txtPolicyNo.setHeight("-1px");
		txtPolicyNo.setMaxLength(30);
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo);
		FormLayout formLayoutRight = new FormLayout(txtPolicyNo);	
		
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft, formLayoutRight);	
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");		
		AbsoluteLayout absoluteLayout =  new AbsoluteLayout();
		absoluteLayout.addComponent(fieldLayout);		
		absoluteLayout.addComponent(btnSearch, "top:80.0px;left:220.0px;");
		absoluteLayout.addComponent(btnReset, "top:80.0px;left:329.0px;");
		
		mainVerticalLayout.addComponent(absoluteLayout);
		//Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		mainVerticalLayout.setWidth("700px");
		mainVerticalLayout.setMargin(false);		 
		//Vaadin8-setImmediate() absoluteLayout.setImmediate(false);
		absoluteLayout.setWidth("100.0%");
		absoluteLayout.setHeight("243px");
		addListener();
		return mainVerticalLayout;
	}

}
