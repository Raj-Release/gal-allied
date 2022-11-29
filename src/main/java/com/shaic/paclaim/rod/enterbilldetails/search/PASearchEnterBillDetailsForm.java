package com.shaic.paclaim.rod.enterbilldetails.search;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.reimbursement.rod.enterbilldetails.search.SearchEnterBillDetailFormDTO;
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

public class PASearchEnterBillDetailsForm extends SearchComponent<SearchEnterBillDetailFormDTO>{

	@Inject
	private PASearchEnterBillDetailsTable searchTable;
	
	private TextField txtIntimationNo;
	private TextField txtPolicyNo;
	private ComboBox cbxCPUCode;
	
	private ComboBox cmbAccidentOrDeath;;
	private ComboBox cmbType;
	private ComboBox cmbPriority;
	
	private ComboBox cmbSource;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Enter Bill Details (Bill Entry)");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		cbxCPUCode = binder.buildAndBind("CPU Code","cpuCode",ComboBox.class);
		txtPolicyNo = binder.buildAndBind("Policy Number","policyNo",TextField.class);
		cmbPriority = binder.buildAndBind("Priority","priority",ComboBox.class);
		cmbType = binder.buildAndBind("Type","type",ComboBox.class);
		cmbSource = binder.buildAndBind("Source","source",ComboBox.class);
		cmbAccidentOrDeath = binder.buildAndBind("Accident/Death","accidentOrdeath",ComboBox.class);
		
		SelectValue selectAccident = new SelectValue();
		selectAccident.setId(null);
		selectAccident.setValue(SHAConstants.ACCIDENT);
		
		SelectValue selectDeath = new SelectValue();
		selectDeath.setId(null);
		selectDeath.setValue(SHAConstants.DEATH);
		

		List<SelectValue> selectVallueList = new ArrayList<SelectValue>();		
		selectVallueList.add(selectAccident);
		selectVallueList.add(selectDeath);
		
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		selectValueContainer.addAll(selectVallueList);
		cmbAccidentOrDeath.setContainerDataSource(selectValueContainer);
		cmbAccidentOrDeath.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbAccidentOrDeath.setItemCaptionPropertyId("value");
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo,cbxCPUCode);
		FormLayout formLayoutMiddle = new FormLayout(txtPolicyNo,cmbType);
		FormLayout formLayoutReight = new FormLayout(cmbSource,cmbAccidentOrDeath);	
		FormLayout formLayoutLast = new FormLayout(cmbPriority);
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutMiddle,formLayoutReight,formLayoutLast);

		//fieldLayout.setMargin(true);
		fieldLayout.setSpacing(true);
	//	fieldLayout.setWidth("100%");		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(btnSearch, "top:110.0px;left:220.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:110.0px;left:329.0px;");
		
		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("1200px");
		// mainVerticalLayout.setSizeFull();
		 mainVerticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 
		 absoluteLayout_3.setHeight("145px");
		
		addListener();
		
		return mainVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchEnterBillDetailFormDTO>(SearchEnterBillDetailFormDTO.class);
		this.binder.setItemDataSource(new SearchEnterBillDetailFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	public void setCPUCode(BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage) {
		
		cbxCPUCode.setContainerDataSource(parameter);
		cbxCPUCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxCPUCode.setItemCaptionPropertyId("value");
		
		cmbPriority.setContainerDataSource(selectValueForPriority);
		cmbPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPriority.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> selectValueForType = SHAUtils.getSelectValueForType();
		
		cmbType.setContainerDataSource(selectValueForType);
		cmbType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbType.setItemCaptionPropertyId("value");
		
		cmbSource.setContainerDataSource(statusByStage);
		cmbSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSource.setItemCaptionPropertyId("value");
	}	
}
