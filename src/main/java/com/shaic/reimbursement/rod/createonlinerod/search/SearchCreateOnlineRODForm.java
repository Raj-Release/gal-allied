/**
 * 
 */
package com.shaic.reimbursement.rod.createonlinerod.search;

import java.util.Iterator;

import javax.annotation.PostConstruct;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

/**
 * @author ntv.narenj
 *
 */
public class SearchCreateOnlineRODForm extends SearchComponent<SearchCreateOnlineRODFormDTO> {
	
	/*@Inject
	private SearchCreateRODTable searchTable;*/
	
	private TextField txtIntimationNo;
	private TextField txtPolicyNo;
	private ComboBox cbxCPUCode;
	private ComboBox cmbType;
	private ComboBox cmbPriority;
	private ComboBox cmbPriorityNew;
	private ComboBox cmbSource;
	private ComboBox cmbDocumentUploaded;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Online Receipt of Discharge Documents");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
		resetListener();
	}
	
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		cbxCPUCode = binder.buildAndBind("CPU Code","cpuCode",ComboBox.class);
		txtPolicyNo = binder.buildAndBind("Policy Number","policyNo",TextField.class);
		cmbPriority = binder.buildAndBind("Priority(IRDA)","priority",ComboBox.class);
		cmbPriorityNew = binder.buildAndBind("Priority","priorityNew",ComboBox.class);
		cmbType = binder.buildAndBind("Type","type",ComboBox.class);
		cmbSource = binder.buildAndBind("Source","source",ComboBox.class);
		cmbDocumentUploaded = binder.buildAndBind("Document Uploaded","isDocumentUploaded",ComboBox.class);
		
		//cmbDocumentUploaded = binder.buildAndBind("Document uploaded","",ComboBox.class)
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo,cbxCPUCode,cmbPriority,cmbPriorityNew);
		FormLayout formLayoutReight = new FormLayout(txtPolicyNo,cmbType,cmbSource,cmbDocumentUploaded);	
	    //Label dummy = new Label();
	   // dummy.setWidth("10px");
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutReight);

		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(btnSearch, "top:160.0px;left:220.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:160.0px;left:329.0px;");
		
		
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
		this.binder = new BeanFieldGroup<SearchCreateOnlineRODFormDTO>(SearchCreateOnlineRODFormDTO.class);
		this.binder.setItemDataSource(new SearchCreateOnlineRODFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public void setCPUCode(BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage, BeanItemContainer<SelectValue> selectValueForUploadedDocument) {
		cbxCPUCode.setContainerDataSource(parameter);
		cbxCPUCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxCPUCode.setItemCaptionPropertyId("value");
		
		selectValueForPriority = SHAUtils.getSelectValueForPriorityIRDA();
		cmbPriority.setContainerDataSource(selectValueForPriority);
		cmbPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPriority.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> selectValueForPriorityNew = getSelectValueForPriorityNew();
		cmbPriorityNew.setContainerDataSource(selectValueForPriorityNew);
		cmbPriorityNew.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPriorityNew.setItemCaptionPropertyId("value");
		cmbPriorityNew.setValue(selectValueForPriorityNew.getItemIds().get(0));
		
		BeanItemContainer<SelectValue> selectValueForType = SHAUtils.getSelectValueForType();
		
		cmbType.setContainerDataSource(selectValueForType);
		cmbType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbType.setItemCaptionPropertyId("value");
		
		
		
		cmbSource.setContainerDataSource(statusByStage);
		cmbSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSource.setItemCaptionPropertyId("value");
		
		cmbDocumentUploaded.setContainerDataSource(selectValueForUploadedDocument);
		cmbDocumentUploaded.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbDocumentUploaded.setItemCaptionPropertyId("value");

		
	}	
	
	public void resetListener()
	{
		btnReset.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				BeanItemContainer<SelectValue> selectValueForPriorityNew = getSelectValueForPriorityNew();
				
				cmbPriorityNew.setContainerDataSource(selectValueForPriorityNew);
				cmbPriorityNew.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				cmbPriorityNew.setItemCaptionPropertyId("value");
				cmbPriorityNew.setValue(selectValueForPriorityNew.getItemIds().get(0));
			}
		});
	}
	
	/* public void addListenerForCmb()
	  {
		  cmbDocumentUploaded.addValueChangeListener(new ValueChangeListener() {
				
				
				@Override
				public void valueChange(Property.ValueChangeEvent event) {

					SelectValue selValue = (SelectValue)cmbDocumentUploaded.getValue();
					if(null != selValue && null != selValue.getValue())
					{
						//invoke presenter
					}

				}
			});
	  }*/
	
	public static BeanItemContainer<SelectValue> getSelectValueForPriorityNew(){
		BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		SelectValue selectValue3 = new SelectValue();
		selectValue3.setId(1l);
		selectValue3.setValue(SHAConstants.ALL);
		SelectValue selectValue4 = new SelectValue();
		selectValue4.setId(2l);
		selectValue4.setValue(SHAConstants.NORMAL);
		SelectValue selectValue2 = new SelectValue();
		selectValue2.setId(3l);
		selectValue2.setValue(SHAConstants.CRM_FLAGGED);
		SelectValue selectValue5 = new SelectValue();
		selectValue5.setId(4l);
		selectValue5.setValue("Corporate – High Priority");
		SelectValue selectValue1 = new SelectValue();
		selectValue1.setId(5l);
		selectValue1.setValue("VIP");
		
		SelectValue selectValue6 = new SelectValue();
		selectValue6.setId(6l);
		selectValue6.setValue("CMD Club");
		
		SelectValue selectValue7 = new SelectValue();
		selectValue7.setId(7l);
		selectValue7.setValue("ED Club");
		
		SelectValue selectValue8 = new SelectValue();
		selectValue8.setId(8l);
		selectValue8.setValue("BM Club");
		
		SelectValue selectValue9 = new SelectValue();
		selectValue9.setId(9l);
		selectValue9.setValue("ZM Club");
		
		SelectValue selectValue10 = new SelectValue();
		selectValue10.setId(10l);
		selectValue10.setValue("CMD Elite Club");
		
		SelectValue selectValue11 = new SelectValue();
		selectValue11.setId(11l);
		selectValue11.setValue("MD Club");
		
		container.addBean(selectValue3);
		container.addBean(selectValue4);	
		container.addBean(selectValue2);
		container.addBean(selectValue5);
		container.addBean(selectValue1);
		container.addBean(selectValue6);
		container.addBean(selectValue7);
		container.addBean(selectValue8);
		container.addBean(selectValue9);
		container.addBean(selectValue10);
		container.addBean(selectValue11);
		container.sort(new Object[] {"value"}, new boolean[] {true});
		
		return container;
	}
}