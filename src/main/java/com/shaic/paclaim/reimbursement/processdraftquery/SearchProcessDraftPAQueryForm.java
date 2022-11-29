/**
 * 
 */
package com.shaic.paclaim.reimbursement.processdraftquery;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.reimbursement.queryrejection.processdraftquery.search.SearchProcessDraftQueryFormDTO;
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

/**
 * 
 *
 */
public class SearchProcessDraftPAQueryForm extends SearchComponent<SearchProcessDraftQueryFormDTO> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private SearchProcessDraftPAQueryTable searchTable;
	
	private TextField txtIntimationNo;
	private TextField txtClaimNo;
	private ComboBox cbxCPUCode;
	
	private ComboBox cmbType;
	private ComboBox cmbPriority;
	private ComboBox cmbSource;
	private ComboBox cmbQueryType;
	
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Process Draft Query Letter");
		mainPanel.setContent(mainVerticalLayout());
		VerticalLayout wholeLayout = new VerticalLayout(mainPanel);
		wholeLayout.setSizeFull();
		setSizeFull();
		setCompositionRoot(wholeLayout);
	}
	
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		mainVerticalLayout.setSizeFull();
		
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		cbxCPUCode = binder.buildAndBind("CPU Code","cpuCode",ComboBox.class);
		txtClaimNo = binder.buildAndBind("Claim No","claimNo",TextField.class);
		cmbPriority = binder.buildAndBind("Priority","priority",ComboBox.class);
		cmbType = binder.buildAndBind("Type","type",ComboBox.class);
		cmbSource = binder.buildAndBind("Source","source",ComboBox.class);
		cmbQueryType = binder.buildAndBind("QueryType","queryType",ComboBox.class);
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo,cbxCPUCode,cmbQueryType);
		FormLayout formLayoutReight = new FormLayout(txtClaimNo,cmbPriority);	
	
		FormLayout form3Layout = new FormLayout(cmbType,cmbSource);
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutReight,form3Layout);
		fieldLayout.setWidth("890px");
		fieldLayout.setHeight("500px");

		fieldLayout.setSizeFull();
		fieldLayout.setSpacing(true);
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("190px");
		 absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(btnSearch, "top:130.0px;left:220.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:130.0px;left:329.0px;");
		
		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		mainVerticalLayout.setWidth("900px");
		addListener();
		
		return mainVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchProcessDraftQueryFormDTO>(SearchProcessDraftQueryFormDTO.class);
		this.binder.setItemDataSource(new SearchProcessDraftQueryFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public void setCPUCode(BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage) {
		
		cbxCPUCode.setContainerDataSource(parameter);
		cbxCPUCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxCPUCode.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> selectValueForType = SHAUtils.getSelectValueForType();
		
		cmbType.setContainerDataSource(selectValueForType);
		cmbType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbType.setItemCaptionPropertyId("value");
		
		cmbPriority.setContainerDataSource(selectValueForPriority);
		cmbPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPriority.setItemCaptionPropertyId("value");
		
		cmbSource.setContainerDataSource(statusByStage);
		cmbSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSource.setItemCaptionPropertyId("value");
		
		
		BeanItemContainer<SelectValue> queryTypecontainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		queryTypecontainer.addBean(new SelectValue(null,SHAConstants.NORMAL_QUERY_TYPE));
		queryTypecontainer.addBean(new SelectValue(null,SHAConstants.PAYMENT_QUERY_TYPE));
		
		cmbQueryType.setContainerDataSource(queryTypecontainer);
		cmbQueryType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbQueryType.setItemCaptionPropertyId("value");
		
	}	
}