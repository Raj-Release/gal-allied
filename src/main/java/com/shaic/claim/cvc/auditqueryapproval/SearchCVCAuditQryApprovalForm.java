package com.shaic.claim.cvc.auditqueryapproval;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionFormDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;


public class SearchCVCAuditQryApprovalForm extends SearchComponent<SearchCVCAuditActionFormDTO>{
	
	private SearchCVCAuditActionFormDTO searchDto;
	
	private SearchCVCAuditQryApprovalTable searchTable;
	
	@EJB
	private MasterService masterService;
	
	private TextField intimationNumber;
	
//	private ComboBox cmbStatus;
	
	private TextField txtuserId;
	
	private ComboBox cmbClmType;
	
	private ComboBox year;
	
	private BeanItemContainer<SelectValue> auditStatusContainer;
	
	private BeanItemContainer<SelectValue> auditClmStatusContainer;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Claim Audit Query Approval");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
		resetBtnListener();
	}
	
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		intimationNumber = binder.buildAndBind("Intimation No", "intimationNumber", TextField.class);
		
		/*cmbStatus = binder.buildAndBind("Status", "cmbauditStatus", ComboBox.class);
		cmbStatus.setNullSelectionAllowed(Boolean.FALSE);
		auditStatusContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		auditStatusContainer.addBean(new SelectValue(1L,SHAConstants.AUDIT_QUERY_APPROVAL_PENDING));
		
		cmbStatus.setContainerDataSource(auditStatusContainer);
		cmbStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbStatus.setItemCaptionPropertyId("value");
		cmbStatus.setValue(auditStatusContainer.getItemIds().get(0));
		cmbStatus.setEnabled(false);*/
		
		txtuserId = binder.buildAndBind("User Id", "userId", TextField.class);
		
		cmbClmType = binder.buildAndBind("Claim Type", "clmType", ComboBox.class);
		auditClmStatusContainer = masterService.getMasterValueByReference(ReferenceTable.CLAIM_TYPE);
		
		cmbClmType.setContainerDataSource(auditClmStatusContainer);
		cmbClmType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbClmType.setItemCaptionPropertyId("value");
		
		year = binder.buildAndBind("Year", "year", ComboBox.class);
		BeanItemContainer<SelectValue> policyYearValues = getPolicyYearValues();
		year.setContainerDataSource(policyYearValues);
		year.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		year.setItemCaptionPropertyId("value");
		year.setId("year");
		List<SelectValue> itemIds2 = policyYearValues.getItemIds();
		if(itemIds2 != null && ! itemIds2.isEmpty()){
			SelectValue selectValue = itemIds2.get(0);
			year.setValue(selectValue);
		}
		if(searchDto != null && searchDto.getYear() != null && searchDto.getYear().getValue() != null){
			year.setValue(searchDto.getYear());
		}
		
		FormLayout formLayoutLeft = new FormLayout(intimationNumber);
		FormLayout formLayoutMiddle = new FormLayout(year);
//		FormLayout formLayoutMiddle = new FormLayout(cmbStatus);
		FormLayout formLayoutRight = new FormLayout(txtuserId);
		FormLayout formLayoutRight1 = new FormLayout(cmbClmType);
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft, formLayoutMiddle, formLayoutRight, formLayoutRight1);
		fieldLayout.setMargin(true);
		fieldLayout.setSpacing(true);
		fieldLayout.setWidth("1200px");
		fieldLayout.setSizeFull();
		AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(btnSearch, "top:100.0px;left:220.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:100.0px;left:320.0px;");
		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		//Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		mainVerticalLayout.setWidth("1200px");
		mainVerticalLayout.setMargin(false);		 
		//Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		absoluteLayout_3.setWidth("100.0%");
		absoluteLayout_3.setHeight("150px");
		 
		addListener();
//		setRequiredAndValidation(year);
		mandatoryFields.add(year);
		return mainVerticalLayout;

	}
	private void initBinder()

	{
		this.binder = new BeanFieldGroup<SearchCVCAuditActionFormDTO>(SearchCVCAuditActionFormDTO.class);
		this.binder.setItemDataSource(new SearchCVCAuditActionFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public void setDropDownValues() {
		
		auditStatusContainer = masterService.getMasterValueByReference(SHAConstants.CVC_REMEDIATION_STATUS);
		
		/*cmbStatus.setContainerDataSource(auditStatusContainer);
		cmbStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbStatus.setItemCaptionPropertyId("value");*/
		// TODO Implements
//		cmbStatus.setValue(auditStatusContainer.getItemIds().get(2));
		
		cmbClmType.setContainerDataSource(auditClmStatusContainer);
		cmbClmType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbClmType.setItemCaptionPropertyId("value");
				
		/*String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		txtuserId.setValue(userName != null ? userName.toUpperCase() : "");*/
		
		BeanItemContainer<SelectValue> policyYearValues = getPolicyYearValues();
		year.setContainerDataSource(policyYearValues);
		year.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		year.setItemCaptionPropertyId("value");
		year.setId("year");
		List<SelectValue> itemIds2 = policyYearValues.getItemIds();
		if(itemIds2 != null && ! itemIds2.isEmpty()){
			SelectValue selectValue = itemIds2.get(0);
			year.setValue(selectValue);
		}
	}
	private void resetBtnListener(){
	btnReset.addClickListener(new Button.ClickListener() {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			
//			cmbStatus.setValue(auditStatusContainer.getItemIds().get(1));
			
			/*String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			txtuserId.setValue(userName != null ? userName.toUpperCase() : "");*/
			
			intimationNumber.setValue("");
			cmbClmType.setValue(null);
		}
	});
	}
	
	public BeanItemContainer<SelectValue> getPolicyYearValues(){
		
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		Calendar instance = Calendar.getInstance();
		//ADDED FOR FY APR - MAR
		int month = instance.get(Calendar.MONTH);
		if(month >= 3){
			instance.add(Calendar.YEAR, 1);
		}
		/*instance.add(Calendar.YEAR, 1);*/
		int intYear = instance.get(Calendar.YEAR);
		Long year = Long.valueOf(intYear);
		for(Long i= year;i>=year-13;i--){
			SelectValue selectValue = new SelectValue();
			Long j = i-1;
			selectValue.setId(j);
			selectValue.setValue(""+i.intValue());
			selectValueList.add(selectValue);
		}
		container.addAll(selectValueList);
		
		return container;
	}
	
	@SuppressWarnings("unused")
	private void setRequiredAndValidation(Component component) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		AbstractField<Field> field = (AbstractField<Field>) component;
		field.setRequired(true);
		field.setValidationVisible(false);
	}

}
