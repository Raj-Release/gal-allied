/**
 * 
 */
package com.shaic.paclaim.reminder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.reimbursement.queryrejection.generateremainder.search.SearchGenerateRemainderFormDTO;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
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
public class SearchGeneratePARemainderForm extends SearchComponent<SearchGenerateRemainderFormDTO> {
	
	@Inject
	private SearchGeneratePARemainderTable searchTable;
	
//	private TextField txtIntimationNo;
//	private TextField txtClaimNo;
	private ComboBox cpuCodeCmb;
	private ComboBox claimTypeCmb; 
	private ComboBox categoryCmb;
	private ComboBox reminderTypeCmb;
	private TextField reminderDate;
	
	
	
	private BeanItemContainer<SelectValue> claimTypeContainer;
	private BeanItemContainer<SelectValue> cpuCondeContainer;
	private BeanItemContainer<SelectValue> reminderTypeContainer;
	private BeanItemContainer<SelectValue> categoryContainer;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Generate Reminder letter (Claim wise)");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
//		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		claimTypeCmb = binder.buildAndBind("Claim Type","claimType",ComboBox.class);
		cpuCodeCmb = binder.buildAndBind("CPU Code","cpuCode",ComboBox.class);
		reminderDate = new TextField("Date");
		String dateValue = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
		reminderDate.setInputPrompt(dateValue);
		reminderDate.setEnabled(false);
		//Vaadin8-setImmediate() reminderDate.setImmediate(true);
		
		
//		txtClaimNo = binder.buildAndBind("Claim No","claimNo",TextField.class);
		FormLayout formLayoutLeft = new FormLayout(claimTypeCmb,cpuCodeCmb,reminderDate);    
		
		categoryCmb = binder.buildAndBind("Category","category",ComboBox.class);
		reminderTypeCmb = binder.buildAndBind("Reminder Type","reminderType",ComboBox.class);
		
		FormLayout formLayoutReight = new FormLayout(categoryCmb,reminderTypeCmb);	
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutReight);
		HorizontalLayout buttonlayout = new HorizontalLayout(btnSearch);
		buttonlayout.setWidth("45%");
		buttonlayout.setComponentAlignment(btnSearch,  Alignment.MIDDLE_RIGHT);
		buttonlayout.setSpacing(true);
		
		mainVerticalLayout.addComponent(fieldLayout);
		mainVerticalLayout.setComponentAlignment(fieldLayout, Alignment.BOTTOM_LEFT);
		mainVerticalLayout.addComponent(buttonlayout);
		mainVerticalLayout.setComponentAlignment(buttonlayout, Alignment.MIDDLE_LEFT);
//		mainVerticalLayout.setWidth("100%");	
		mainVerticalLayout.setHeight("200px");
		mainVerticalLayout.setMargin(true);
		mainVerticalLayout.setSpacing(true);
		addListener();
		
		return mainVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchGenerateRemainderFormDTO>(SearchGenerateRemainderFormDTO.class);
		this.binder.setItemDataSource(new SearchGenerateRemainderFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public void setDropDownValues(Map<String,Object> parameter) {
		claimTypeContainer = parameter.containsKey("claimTypeContainer") && parameter.get("claimTypeContainer") != null ? (BeanItemContainer<SelectValue>)parameter.get("claimTypeContainer") : new BeanItemContainer<SelectValue>(SelectValue.class);
		cpuCondeContainer = parameter.containsKey("cpuCodeContainer") && parameter.get("cpuCodeContainer") != null ? (BeanItemContainer<SelectValue>)parameter.get("cpuCodeContainer") : new BeanItemContainer<SelectValue>(SelectValue.class);
		reminderTypeContainer = parameter.containsKey("reminderTypeContainer") && parameter.get("reminderTypeContainer") != null ? (BeanItemContainer<SelectValue>)parameter.get("reminderTypeContainer") : new BeanItemContainer<SelectValue>(SelectValue.class);
		categoryContainer = parameter.containsKey("categoryContainer") && parameter.get("categoryContainer") != null ? (BeanItemContainer<SelectValue>)parameter.get("categoryContainer") : new BeanItemContainer<SelectValue>(SelectValue.class);
		
		claimTypeCmb.setContainerDataSource(claimTypeContainer);
		claimTypeCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		claimTypeCmb.setItemCaptionPropertyId("value");
		
		cpuCodeCmb.setContainerDataSource(cpuCondeContainer);
		cpuCodeCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cpuCodeCmb.setItemCaptionPropertyId("value");
		
		categoryCmb.setContainerDataSource(categoryContainer);
		categoryCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		categoryCmb.setItemCaptionPropertyId("value");
		
		reminderTypeCmb.setContainerDataSource(reminderTypeContainer);
		reminderTypeCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		reminderTypeCmb.setItemCaptionPropertyId("value");
		
	}	
	public void resetFields(){
		
 		claimTypeCmb.setValue(null);
		cpuCodeCmb.setValue(null);
		categoryCmb.setValue(null);
		reminderTypeCmb.setValue(null);
		
	}
	public SearchGenerateRemainderFormDTO getSearchFilters(){
		try {
			this.binder.commit();
			SearchGenerateRemainderFormDTO bean = binder.getItemDataSource().getBean();
			return bean;
		} catch (CommitException e) {
			e.printStackTrace();
		}
		return null;
	}
}