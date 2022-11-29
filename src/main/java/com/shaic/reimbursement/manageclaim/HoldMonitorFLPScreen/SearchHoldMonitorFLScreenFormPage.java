package com.shaic.reimbursement.manageclaim.HoldMonitorFLPScreen;

import javax.annotation.PostConstruct;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.reimbursement.manageclaim.HoldMonitorScreen.SearchHoldMonitorScreenFormDTO;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;

public class SearchHoldMonitorFLScreenFormPage extends SearchComponent<SearchHoldMonitorScreenFormDTO>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private TextField txtintimationNo;
	private ComboBox user;
	private ComboBox cmbtype;
	private ComboBox cmbCpuCode;
	
	private Panel mainPanel;
	
	@PostConstruct
	public void init(){
		initBinder();
		
		mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Hold Monitor Screen - First Level Processing");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout mainVerticalLayout(){

		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		txtintimationNo = binder.buildAndBind("Intimation No", "intimationNumber", TextField.class);
		user = binder.buildAndBind("User","user",ComboBox.class);
		cmbtype = binder.buildAndBind("Type","type",ComboBox.class);
		cmbCpuCode = binder.buildAndBind("CPU Code","cpuCode",ComboBox.class);
		
		FormLayout firstLayout = new FormLayout(txtintimationNo,user);
		FormLayout secondLayout = new FormLayout(cmbtype,cmbCpuCode);	
		/*FormLayout thirdLayout = new FormLayout(cmbtype);
		FormLayout fourthLayout = new FormLayout(cmbCpuCode);*/
		
//		HorizontalLayout horizonatalLayout  = new HorizontalLayout(txtintimationNo,cmbtype,user);
		HorizontalLayout fieldLayout = new HorizontalLayout(firstLayout,secondLayout);
		//Vaadin8-setImmediate() fieldLayout.setImmediate(false);
		fieldLayout.setSpacing(false);
		
//		HorizontalLayout fieldLayout = new HorizontalLayout(horizonatalLayout);

		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");		
		AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(btnSearch, "top:100.0px;left:320.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:100.0px;left:429.0px;");
		
		
		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		//Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		mainVerticalLayout.setWidth("1000px");
		mainVerticalLayout.setMargin(false);		 
		//Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		absoluteLayout_3.setWidth("100.0%");
		absoluteLayout_3.setHeight("174px");
		addListener();
		
		return mainVerticalLayout;
	
	}
	
	private void initBinder(){
		this.binder = new BeanFieldGroup<SearchHoldMonitorScreenFormDTO>(SearchHoldMonitorScreenFormDTO.class);
		this.binder.setItemDataSource(new SearchHoldMonitorScreenFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
	}
	
	public void setComboBoxValue(BeanItemContainer<SelectValue> type){
		cmbtype.setContainerDataSource(type);
		cmbtype.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbtype.setItemCaptionPropertyId("value");
	}
	
	public void setUserComboValues(BeanItemContainer<SelectValue> userList){
		user.setContainerDataSource(userList);
		user.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		user.setItemCaptionPropertyId("value");
	}
	
	public void setCpuCodeComboBoxValue(BeanItemContainer<SelectValue> cpuCode){
		cmbCpuCode.setContainerDataSource(cpuCode);
		cmbCpuCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCpuCode.setItemCaptionPropertyId("value");
	}
	
}
