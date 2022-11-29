package com.shaic.claim.OMPProcessOmpClaimProcessor.search;

import javax.annotation.PostConstruct;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.cdi.UIScoped;
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

@UIScoped
@SuppressWarnings("serial")
public class OMPProcessOmpClaimProcessorUI extends SearchComponent<OMPProcessOmpClaimProcessorFormDto>{
	
	private OMPProcessOmpClaimProcessorFormDto searchDto;
	private OMPProcessOmpClaimProcessorDetailTable searchTable;
	
	private TextField intimationNumber;
	private TextField policyNumber;
	private ComboBox cmdClassification;
	
	@PostConstruct
	public void init() {
		
		initBinder();
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Process OMP Claim -Processor");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}

	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		intimationNumber = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		policyNumber = binder.buildAndBind("Policy No","policyno",TextField.class);
		cmdClassification = binder.buildAndBind("Classification","classification",ComboBox.class);
		
		FormLayout formLayoutLeft = new FormLayout(intimationNumber,cmdClassification);
		FormLayout formLayoutReight = new FormLayout(policyNumber);	
	    
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutReight);
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(btnSearch, "top:160.0px;left:220.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:160.0px;left:329.0px;");
	//	VerticalLayout verticalLayout = new VerticalLayout();
		
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
		this.binder = new BeanFieldGroup<OMPProcessOmpClaimProcessorFormDto>(OMPProcessOmpClaimProcessorFormDto.class);
		this.binder.setItemDataSource(new OMPProcessOmpClaimProcessorFormDto());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public void setDropDownValues(BeanItemContainer<SelectValue> classification) {
		cmdClassification.setContainerDataSource(classification);
		cmdClassification.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmdClassification.setItemCaptionPropertyId("value");
		
	}
}
