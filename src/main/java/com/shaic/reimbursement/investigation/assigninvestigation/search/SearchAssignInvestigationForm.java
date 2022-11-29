/**
 * 
 */
package com.shaic.reimbursement.investigation.assigninvestigation.search;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

/**
 * @author ntv.narenj
 *
 */
public class SearchAssignInvestigationForm extends SearchComponent<SearchAssignInvestigationFormDTO> {
	
	@Inject
	private SearchAssignInvestigationTable searchTable;
	
	private TextField txtIntimationNo;
	private TextField txtPolicyNo;
	private ComboBox cbxCPUCode;
	private ComboBox cmbClaimType;
	
	private ComboBox cmbType;
	private ComboBox cmbPriority;
	private ComboBox cmbSource;
//	private ComboBox cmbPriorityNew;
	
	private Label priority;
	
	private CheckBox chkAll;
	
	private CheckBox chkCRM;
	
	private CheckBox chkVIP;
	
	//CR2019058 Adding new fields for get task search
	private ComboBox cmbInvstigationState;
	private PopupDateField fromDateField;
	private PopupDateField endDateField;
	private TextField txtDummy1 = new TextField();
	private TextField txtDummy2 = new TextField();
	private TextField txtDummy3 = new TextField();
	private TextField txtDummy4 = new TextField();

	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Assign Investigation");
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
		cmbClaimType = binder.buildAndBind("Claim Type","claimType",ComboBox.class);
		
		cmbPriority = binder.buildAndBind("Priority(IRDA)","priority",ComboBox.class);
		cmbType = binder.buildAndBind("Type","type",ComboBox.class);
		cmbSource = binder.buildAndBind("Source","source",ComboBox.class);
//		cmbPriorityNew = binder.buildAndBind("Priority", "priorityNew", ComboBox.class);
		
		txtDummy1.setEnabled(false);
		txtDummy1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		txtDummy2.setEnabled(false);
		txtDummy2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		txtDummy3.setEnabled(false);
		txtDummy3.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		txtDummy4.setEnabled(false);
		txtDummy4.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		cmbInvstigationState = binder.buildAndBind("Investigation State","invstigationState",ComboBox.class);
		fromDateField = binder.buildAndBind("From Date","fromDate",PopupDateField.class);
		endDateField = binder.buildAndBind("To Date","endDate",PopupDateField.class);
		
		priority = new Label();
		priority.setCaption("Priority");
		
		chkAll = binder.buildAndBind("All","priorityAll",CheckBox.class);
		chkAll.setValue(Boolean.TRUE);
		
		chkCRM = binder.buildAndBind("CRM","crm",CheckBox.class);
		
		chkVIP = binder.buildAndBind("VIP","vip",CheckBox.class);
		
		HorizontalLayout priorityHorLayout = new HorizontalLayout(priority,chkAll,chkCRM,chkVIP);
		priorityHorLayout.setMargin(false);
		priorityHorLayout.setSpacing(true);
		FormLayout formLayoutChk = new FormLayout(priorityHorLayout);

		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo,cbxCPUCode,cmbPriority,fromDateField);
		FormLayout formLayoutMiddle = new FormLayout(txtPolicyNo,cmbClaimType,cmbType/*,cmbPriorityNew*/,endDateField);
		FormLayout formLayoutRight = new FormLayout(cmbInvstigationState,cmbSource,txtDummy1);

		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutMiddle,formLayoutRight);
		
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");		
//		fieldLayout.setSpacing(false);
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(formLayoutChk,"top:152.0px;left:14.0px;");
		absoluteLayout_3.addComponent(btnSearch, "top:185.0px;left:370.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:185.0px;left:470.0px;");
		
		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("1000px");
		 mainVerticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 
		 absoluteLayout_3.setHeight("224px");
		addListener();
		resetListener();
		
		return mainVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchAssignInvestigationFormDTO>(SearchAssignInvestigationFormDTO.class);
		this.binder.setItemDataSource(new SearchAssignInvestigationFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public void setCPUCode(BeanItemContainer<SelectValue> parameter,BeanItemContainer<SelectValue> claimTypeContainer, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage, BeanItemContainer<SelectValue> statusByInvestigationState) {
		
		cbxCPUCode.setContainerDataSource(parameter);
		cbxCPUCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxCPUCode.setItemCaptionPropertyId("value");
		
		cmbClaimType.setContainerDataSource(claimTypeContainer);
		cmbClaimType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbClaimType.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> selectValueForType = SHAUtils.getSelectValueForType();
		
		cmbType.setContainerDataSource(selectValueForType);
		cmbType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbType.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> selectValueForPriorityIRDA = SHAUtils.getSelectValueForPriorityIRDA();
		cmbPriority.setContainerDataSource(selectValueForPriorityIRDA);
		cmbPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPriority.setItemCaptionPropertyId("value");
		
		/*BeanItemContainer<SelectValue> selectValueForPriorityNew = SHAUtils.getSelectValueForPriorityNew();
		cmbPriorityNew.setContainerDataSource(selectValueForPriorityNew);
		cmbPriorityNew.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPriorityNew.setItemCaptionPropertyId("value");
		cmbPriorityNew.setValue(selectValueForPriorityNew.getItemIds().get(0));*/
		
		cmbSource.setContainerDataSource(statusByStage);
		cmbSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSource.setItemCaptionPropertyId("value");
		
		
		cmbInvstigationState.setContainerDataSource(statusByInvestigationState);
		cmbInvstigationState.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbInvstigationState.setItemCaptionPropertyId("value");

		
		fromDateField = new PopupDateField("From Date");
		//Vaadin8-setImmediate() fromDateField.setImmediate(false);
		fromDateField.setTabIndex(6);
		fromDateField.setWidth("160px");
		fromDateField.setHeight("-1px");
		fromDateField.setTextFieldEnabled(false);
		fromDateField.setDateFormat(("dd/MM/yyyy"));

		endDateField = new PopupDateField("From Date");
		//Vaadin8-setImmediate() fromDateField.setImmediate(false);
		endDateField.setTabIndex(6);
		endDateField.setWidth("160px");
		endDateField.setHeight("-1px");
		endDateField.setTextFieldEnabled(false);
		endDateField.setDateFormat(("dd/MM/yyyy"));
	}
	
	private void resetListener() {
		
		btnReset.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				chkAll.setValue(true);
				chkCRM.setValue(false);
				chkVIP.setValue(false);
				
				/*BeanItemContainer<SelectValue> selectValueForPriorityNew = SHAUtils.getSelectValueForPriorityNew();
				
				cmbPriorityNew.setContainerDataSource(selectValueForPriorityNew);
				cmbPriorityNew.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				cmbPriorityNew.setItemCaptionPropertyId("value");
				cmbPriorityNew.setValue(selectValueForPriorityNew.getItemIds().get(0));*/
			}
		});
		
		chkCRM.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
					boolean value = (Boolean) event.getProperty().getValue();

					if(value || (chkVIP != null && chkVIP.getValue() != null && chkVIP.getValue().equals(Boolean.TRUE)))
					{
						chkAll.setValue(false);
						chkAll.setEnabled(false);
					}
					else{
						chkAll.setEnabled(true);
					}	 						 
					
				}
			}
		});
		
		chkVIP.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
					boolean value = (Boolean) event.getProperty().getValue();

					if(value || (chkCRM != null && chkCRM.getValue() != null && chkCRM.getValue().equals(Boolean.TRUE)))
					{
						chkAll.setValue(false);
						chkAll.setEnabled(false);
					}
					else{
						chkAll.setEnabled(true);
					}	 						 
					
				}
			}
		});
		
	}
	
/*private void searchListener() {
		
	btnSearch.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				if((cmbInvstigationState.getValue() != null) && ((fromDateField.getValue() == null && endDateField.getValue() != null ) || (fromDateField.getValue() != null && endDateField.getValue() == null ) || (fromDateField.getValue() != null && endDateField.getValue() != null )))
				{
					
					showErrorPopup("<b>Please Select both Intimation from date and to date for Search");
				 }
				else if((cmbInvstigationState.getValue() != null) && ((fromDateField.getValue() != null  && endDateField.getValue() != null) && ((Date)fromDateField.getValue()).before((Date)endDateField.getValue())) )
				 {
					 
			    	 showErrorPopup("<b>Invalid Date Range, <br>Please Select Intimation End date greater than Intimation From date");
				 }
		 
				else if((cmbInvstigationState.getValue() == null) && ((fromDateField.getValue() != null  || endDateField.getValue() != null)))
				 {
					 
			    	 showErrorPopup("<b>Please Select Intimation State");
				 } 
				
			}
		});
		
	}*/

public void showErrorPopup(String errorMessage) {
    Label label = new Label(errorMessage, ContentMode.HTML);
    label.setStyleName("errMessage");
    VerticalLayout layout = new VerticalLayout();
    layout.setMargin(true);
    layout.addComponent(label);

    ConfirmDialog dialog = new ConfirmDialog();
    dialog.setCaption("Error");
    dialog.setClosable(true);
    dialog.setContent(layout);
    dialog.setResizable(false);
    dialog.setModal(true);
    dialog.show(getUI().getCurrent(), null, true);

    return;
}
	
}