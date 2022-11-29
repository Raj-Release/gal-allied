/**
 * 
 */
package com.shaic.reimbursement.rod.uploadinvestication.search;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.Stage;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

/**
 * @author ntv.narenj
 *
 */
public class SearchUploadInvesticationForm extends SearchComponent<SearchUploadInvesticationFormDTO> {
	
	private TextField txtIntimationNo;
	private TextField txtPolicyNo;
	
	private ComboBox cmbType;
	private ComboBox cmbPriority;
//	private ComboBox cmbPriorityNew;
	private ComboBox cmbSource;
	private ComboBox cmbClaimType;
	
	private Label priority;
	
	private CheckBox chkAll;
	
	private CheckBox chkCRM;
	
	private CheckBox chkVIP;
	
	@EJB
	private PreauthService preauthService;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Upload Investigation Report");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
		
		resetListener();
	}
	
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		
		txtPolicyNo = binder.buildAndBind("Policy Number","policyNo",TextField.class);

		cmbPriority = binder.buildAndBind("Priority(IRDA)","priority",ComboBox.class);
//		cmbPriorityNew = binder.buildAndBind("Priority","priorityNew",ComboBox.class);
		cmbType = binder.buildAndBind("Type","type",ComboBox.class);
		cmbSource = binder.buildAndBind("Source","source",ComboBox.class);
		cmbClaimType = binder.buildAndBind("Claim Type","claimType",ComboBox.class);
		
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
				
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo,cmbSource,cmbPriority/*,cmbPriorityNew*/);
		FormLayout formLayoutReight = new FormLayout(txtPolicyNo,cmbClaimType,cmbType);	
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutReight);

		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 absoluteLayout_3.addComponent(fieldLayout);				
		absoluteLayout_3.addComponent(formLayoutChk,"top:120.0px;left:14.0px;");
		absoluteLayout_3.addComponent(btnSearch, "top:170.0px;left:370.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:170.0px;left:470.0px;");
		
		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("650px");
		 mainVerticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 
		 absoluteLayout_3.setHeight("224px");
		
		
		addListener();
		
		setDropDownValues();
		
		return mainVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchUploadInvesticationFormDTO>(SearchUploadInvesticationFormDTO.class);
		this.binder.setItemDataSource(new SearchUploadInvesticationFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}	
	
	public void setCPUCode(BeanItemContainer<SelectValue> parameter) {
		
//		cmbType.setContainerDataSource(selectValueForType);
		cmbType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbType.setItemCaptionPropertyId("value");
		
//		cmbSource.setContainerDataSource(statusByStage);
		cmbSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSource.setItemCaptionPropertyId("value");
	}
	
   public void setDropDownValues(){
		
		BeanItemContainer<SelectValue> selectValueForType = SHAUtils.getSelectValueForType();
		
		cmbType.setContainerDataSource(selectValueForType);
		cmbType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbType.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils.getSelectValueForPriority();

		selectValueForPriority = SHAUtils.getSelectValueForPriorityIRDA();
		cmbPriority.setContainerDataSource(selectValueForPriority);
		cmbPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPriority.setItemCaptionPropertyId("value");
		
		/*BeanItemContainer<SelectValue> selectValueForPriorityNew = SHAUtils.getSelectValueForPriorityNew();
		cmbPriorityNew.setContainerDataSource(selectValueForPriorityNew);
		cmbPriorityNew.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPriorityNew.setItemCaptionPropertyId("value");
		cmbPriorityNew.setValue(selectValueForPriorityNew.getItemIds().get(0));*/
		
		
		//BeanItemContainer<SelectValue> selectValueForClaimType = SHAUtils.getSelectValueForFilterType();
	
		
		
		Stage stageByKey2 = preauthService.getStageByKey(ReferenceTable.CLAIM_REQUEST_STAGE);
		Stage stageByKey3 = preauthService.getStageByKey(ReferenceTable.FINANCIAL_STAGE);
		Stage stageByKey4 = preauthService.getStageByKey(ReferenceTable.PREAUTH_STAGE);
		Stage stageByKey5 = preauthService.getStageByKey(ReferenceTable.ENHANCEMENT_STAGE);

		SelectValue selectValue1 = new SelectValue();
		selectValue1.setId(stageByKey2.getKey());
		selectValue1.setValue(stageByKey2.getStageName());
		
		SelectValue selectValue2 = new SelectValue();
		selectValue2.setId(stageByKey3.getKey());
		selectValue2.setValue(stageByKey3.getStageName());
		
		SelectValue selectValue3 = new SelectValue();
		selectValue3.setId(stageByKey4.getKey());
		selectValue3.setValue(stageByKey4.getStageName());
		
		SelectValue selectValue4 = new SelectValue();
		selectValue4.setId(stageByKey5.getKey());
		selectValue4.setValue(stageByKey5.getStageName());
		
		BeanItemContainer<SelectValue> statusByStage = new BeanItemContainer<SelectValue>(SelectValue.class);
		statusByStage.addBean(selectValue1);
		statusByStage.addBean(selectValue2);
		statusByStage.addBean(selectValue3);
		statusByStage.addBean(selectValue4);
		
		cmbSource.setContainerDataSource(statusByStage);
		cmbSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSource.setItemCaptionPropertyId("value");
	}

   public void setClaimType(BeanItemContainer<SelectValue> parameter) {
		cmbClaimType.setContainerDataSource(parameter);
		cmbClaimType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbClaimType.setItemCaptionPropertyId("value");
	
	
   }
   @SuppressWarnings("deprecation")
public void resetListener()
	{
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
   
   public void refresh()
	{
		System.out.println("---inside the refresh----");
		if(mainVerticalLayout != null) {
			SHAUtils.resetComponent(mainVerticalLayout);
			searchable.resetSearchResultTableValues();
			
			chkAll.setValue(true);
			chkCRM.setValue(false);
			chkVIP.setValue(false);
			
			/*BeanItemContainer<SelectValue> selectValueForPriorityNew = SHAUtils.getSelectValueForPriorityNew();
			
			cmbPriorityNew.setContainerDataSource(selectValueForPriorityNew);
			cmbPriorityNew.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbPriorityNew.setItemCaptionPropertyId("value");
			cmbPriorityNew.setValue(selectValueForPriorityNew.getItemIds().get(0));*/
		}
		if (quickVerticallayout != null){
			
			SHAUtils.resetComponent(quickVerticallayout);
			searchable.resetSearchResultTableValues();
			
			chkAll.setValue(true);
			chkCRM.setValue(false);
			chkVIP.setValue(false);
			
			/*BeanItemContainer<SelectValue> selectValueForPriorityNew = SHAUtils.getSelectValueForPriorityNew();
			
			cmbPriorityNew.setContainerDataSource(selectValueForPriorityNew);
			cmbPriorityNew.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbPriorityNew.setItemCaptionPropertyId("value");
			cmbPriorityNew.setValue(selectValueForPriorityNew.getItemIds().get(0));*/
		}
	}

}