package com.shaic.claim.omp.newregistration;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.shaic.claim.omp.createintimation.OMPSearchIntimationDetailTable;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class OMPNewRegistationSearchForm extends SearchComponent<OMPNewRegistrationSearchDTO>{

	private static final long serialVersionUID = -8168273641091242997L;

	@Inject
    private OMPNewRegistrationSearchTable searchResultTable;
	
	private TextField intimationNumber;
	private TextField policyNumber;
	private DateField intimationDateField;

	private Button resetBtn;
	
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Claim Registration");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}

	public VerticalLayout mainVerticalLayout(){
		mainVerticalLayout = new VerticalLayout();
		intimationNumber = binder.buildAndBind("Intimation No", "intimationNoForm", TextField.class);
		policyNumber = binder.buildAndBind("Policy Number","policyNoForm",TextField.class);
		intimationDateField = binder.buildAndBind("Intimation Date","intimationDateForm",DateField.class);
		FormLayout formLayoutLeft = new FormLayout(intimationNumber, policyNumber);
		FormLayout formLayoutRight = new FormLayout(intimationDateField);
	    
		resetBtn = new Button("Reset");
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight);
		
		fieldLayout.setSpacing(true);
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");
		
		AbsoluteLayout searchIntimation_layout =  new AbsoluteLayout();
		searchIntimation_layout.addComponent(fieldLayout);
		searchIntimation_layout.addComponent(btnSearch, "top:160.0px;left:220.0px;");
		searchIntimation_layout.addComponent(resetBtn, "top:160.0px;left:329.0px;");
				
		mainVerticalLayout.addComponent(searchIntimation_layout);
		//Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		mainVerticalLayout.setWidth("665px");
		mainVerticalLayout.setMargin(false);	
		
		//Vaadin8-setImmediate() searchIntimation_layout.setImmediate(false);
		searchIntimation_layout.setWidth("100.0%");
		searchIntimation_layout.setHeight("224px");
		
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnSearch.setImmediate(true);
		btnSearch.setWidth("-1px");
		btnSearch.setTabIndex(12);
		btnSearch.setHeight("-1px");
		btnSearch.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		addListener();
		
		return mainVerticalLayout;
	}
	
	
	public void addListener() {
		
		btnSearch.removeClickListener(this);
		btnSearch.addClickListener(this);

//		btnSearch.addClickListener(new Button.ClickListener() {
//			private static final long serialVersionUID = 1L;
//			@Override
//			public void buttonClick(ClickEvent event) {
//				btnSearch.setEnabled(true);
//				searchable.doSearch();			
//			}
//		});
		
		resetBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				resetAlltheValues();	
			}
		});
		
		
	}

	private void initBinder(){
		this.binder = new BeanFieldGroup<OMPNewRegistrationSearchDTO>(OMPNewRegistrationSearchDTO.class);
		this.binder.setItemDataSource(new OMPNewRegistrationSearchDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public void resetAlltheValues(){
			intimationNumber.setValue("");
			intimationDateField.setValue(null);
			policyNumber.setValue("");
			if(searchResultTable != null){
				searchResultTable.removeRow();
			}
	}

	
	public String doPolicyValidation(){
		return (StringUtils.isBlank(policyNumber.getValue()))? "No PolicyNumber Entered":policyNumber.getValue();
	}
	
	public String doValidation(){
		if(StringUtils.isBlank(policyNumber.getValue()) && StringUtils.isBlank(intimationNumber.getValue())){
			return ("No value");
		}
		return ("value");
	}
	
	public void refresh(){
		System.out.println("---inside the refresh----");
		resetAlltheValues();
	}
}
