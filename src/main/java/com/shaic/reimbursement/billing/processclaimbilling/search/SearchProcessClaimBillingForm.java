/**
 * 
 */
package com.shaic.reimbursement.billing.processclaimbilling.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

/**
 * @author ntv.narenj
 *
 */
public class SearchProcessClaimBillingForm extends SearchComponent<SearchProcessClaimBillingFormDTO> {
	
	@Inject
	private SearchProcessClaimBillingTable searchTable;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private PreauthService preauthService;
	
	private TextField txtIntimationNo;
	private TextField txtPolicyNo;
	private ComboBox cbxCPUCode;
	private ComboBox cbxType;
	private ComboBox cbxProductNameCode;
	private ComboBox cmbPriority;
	private ComboBox cmbClaimType;
	
//	private ComboBox cmbPriorityNew;
	
	private ComboBoxMultiselect priority;
	
	private CheckBox chkAll;
	
	private CheckBox chkCRM;
	
	private CheckBox chkVIP;
	
	private ComboBox cmbSource;
	
	private List<String> selectedPriority;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Process Claim Billing");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
		cbxhospitalListener();
	}
	
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		cbxCPUCode = binder.buildAndBind("CPU Code","cpuCode",ComboBox.class);
		txtPolicyNo = binder.buildAndBind("Policy Number","policyNo",TextField.class);
		cbxType = binder.buildAndBind("Type","type",ComboBox.class);
		cbxProductNameCode = binder.buildAndBind("Product Name/Code","productNameCode",ComboBox.class);
		cmbPriority = binder.buildAndBind("Priority(IRDA)","priority",ComboBox.class);
		
//		cmbPriorityNew = binder.buildAndBind("Priority","priorityNew",ComboBox.class);
		
		cmbClaimType = binder.buildAndBind("Claim Type","claimType",ComboBox.class);
		
		cmbSource = binder.buildAndBind("Source","source",ComboBox.class);
		
		/*priority = new Label();
		priority.setCaption("Priority");*/
		
		chkAll = binder.buildAndBind("All","priorityAll",CheckBox.class);
		//chkAll.setValue(Boolean.TRUE);
		
		chkCRM = binder.buildAndBind("CRM","crm",CheckBox.class);
		
		chkVIP = binder.buildAndBind("VIP","vip",CheckBox.class);
		
		/*HorizontalLayout priorityHorLayout = new HorizontalLayout(priority,chkAll,chkCRM,chkVIP);
		priorityHorLayout.setMargin(false);
		priorityHorLayout.setSpacing(true);*/
		BeanItemContainer<SpecialSelectValue> container = getSelectValueForPriority();

		priority = new ComboBoxMultiselect("Priority");
		priority.setShowSelectedOnTop(true);
		//priority.setComparator(SHAUtils.getComparator());
		priority.setContainerDataSource(container);
		priority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		priority.setItemCaptionPropertyId("value");	
		priority.setData(container);
		FormLayout formLayoutChk = new FormLayout();
		
		/*FormLayout formLayoutLeft = new FormLayout(txtIntimationNo,txtPolicyNo,cbxCPUCode,cmbSource);
		FormLayout formLayoutReight = new FormLayout(cbxType, cbxProductNameCode,cmbClaimType,cmbPriority,cmbPriorityNew);*/	
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo,txtPolicyNo,cbxCPUCode,priority);
		FormLayout formLayoutMiddle = new FormLayout(cbxType,cmbClaimType,cbxProductNameCode);
		FormLayout formLayoutRight = new FormLayout(cmbPriority,cmbSource);
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutMiddle,formLayoutRight);
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(formLayoutChk,"top:120.0px;left:14.0px;");	
		absoluteLayout_3.addComponent(btnSearch, "top:175.0px;left:290.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:175.0px;left:420.0px;");
		
		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("1200px");
		// mainVerticalLayout.setHeight("500px");
		 mainVerticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 
		 absoluteLayout_3.setHeight("243px");
		
		
		addListener();
		
		return mainVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchProcessClaimBillingFormDTO>(SearchProcessClaimBillingFormDTO.class);
		this.binder.setItemDataSource(new SearchProcessClaimBillingFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public void setCBXValue(BeanItemContainer<SelectValue> cpucode,
			BeanItemContainer<SelectValue> productNameCode, BeanItemContainer<SelectValue> typeContainer, BeanItemContainer<SelectValue> claimType, BeanItemContainer<SelectValue> statusByStage) {
		
		cbxCPUCode.setContainerDataSource(cpucode);
		cbxCPUCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxCPUCode.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SpecialSelectValue> productName = masterService.getContainerForProduct();
		
		cbxProductNameCode.setContainerDataSource(productName);
		cbxProductNameCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxProductNameCode.setItemCaptionPropertyId("specialValue");
		
		cmbClaimType.setContainerDataSource(claimType);
		cmbClaimType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbClaimType.setItemCaptionPropertyId("value");
		
//		cbxProductNameCode.setContainerDataSource(typeContainer);
//		cbxProductNameCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//		cbxProductNameCode.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils.getSelectValueForPriorityIRDA();
		cmbPriority.setContainerDataSource(selectValueForPriority);
		cmbPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPriority.setItemCaptionPropertyId("value");
		
		/*BeanItemContainer<SelectValue> selectValueForPriorityNew = SHAUtils.getSelectValueForPriorityNew();
		cmbPriorityNew.setContainerDataSource(selectValueForPriorityNew);
		cmbPriorityNew.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPriorityNew.setItemCaptionPropertyId("value");
		cmbPriorityNew.setValue(selectValueForPriorityNew.getItemIds().get(0));*/
		
		BeanItemContainer<SelectValue> selectValueForType = SHAUtils.getSelectValueForType();
		
		cbxType.setContainerDataSource(selectValueForType);
		cbxType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxType.setItemCaptionPropertyId("value");
		
		cmbSource.setContainerDataSource(statusByStage);
		cmbSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSource.setItemCaptionPropertyId("value");
	}	
	
	private void cbxhospitalListener(){
		
		btnReset.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				refresh();
				chkAll.setValue(false);
				chkCRM.setValue(false);
				chkVIP.setValue(false);
				priority.setValue(null);
				setselectedPriority("CRM",false);
				setselectedPriority("VIP",false);
				setselectedPriority("ATOS",false);
				setselectedPriority("CMD",false);
				setselectedPriority("BM",false);
				setselectedPriority("ED",false);
				setselectedPriority("ZM",false);
				setselectedPriority("MD",false);
				setselectedPriority("CMD ELITE",false);
				setselectedPriority("COVID",false);
				
				/*BeanItemContainer<SelectValue> selectValueForPriorityNew = SHAUtils.getSelectValueForPriorityNew();
				
				cmbPriorityNew.setContainerDataSource(selectValueForPriorityNew);
				cmbPriorityNew.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				cmbPriorityNew.setItemCaptionPropertyId("value");
				cmbPriorityNew.setValue(selectValueForPriorityNew.getItemIds().get(0));*/
			}
		});
		
		priority.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 2697682747976915503L;

			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				/*BeanItem<PreauthMedicalDecisionDTO> dtoBeanObject = binder.getItemDataSource();
								PreauthMedicalDecisionDTO dtoObject = dtoBeanObject.getBean();
								dtoObject.setUserRoleMulti(null);
								dtoObject.setUserRoleMulti(event.getProperty().getValue());*/

				if(null != event && null != event.getProperty() && null != event.getProperty().getValue()){
					BeanItemContainer<SpecialSelectValue> listOfDoctors = (BeanItemContainer<SpecialSelectValue>) priority.getData();
					List<String> docList = preauthService.getListFromMultiSelectComponent(event.getProperty().getValue());
					BeanItemContainer<SpecialSelectValue> listOfRoleContainer = (BeanItemContainer<SpecialSelectValue>)priority.getData();
					List<String> roles = new ArrayList<String>();
					List<SpecialSelectValue> listOfRoles = listOfRoleContainer.getItemIds();
					chkAll.setValue(false);
					chkCRM.setValue(false);
					chkVIP.setValue(false);

					if(docList != null)
					{
						setselectedPriority("CRM",false);
						setselectedPriority("VIP",false);
						setselectedPriority("COVID",false);
						setselectedPriority("ATOS",false);
						setselectedPriority("CMD",false);
						setselectedPriority("BM",false);
						setselectedPriority("ED",false);
						setselectedPriority("ZM",false);
						setselectedPriority("MD",false);
						setselectedPriority("CMD ELITE",false);


						for (String selValue : docList) {

							if(selValue.equalsIgnoreCase("All"))
							{	
								chkAll.setValue(true);
							}
							if(selValue.equalsIgnoreCase("CRM Flagged"))
							{	
								chkCRM.setValue(true);
								setselectedPriority("CRM",true);
							}
							if(selValue.equalsIgnoreCase("VIP"))
							{	
								chkVIP.setValue(true);
								setselectedPriority("VIP",true);
							}
							if(selValue.equalsIgnoreCase("Corporate - High Priority"))
							{	
								//chkAll.setValue(true);
								setselectedPriority("ATOS",true);
							}
							if(selValue.equalsIgnoreCase("CMD Club"))
							{	
								setselectedPriority("CMD",true);
							}
							if(selValue.equalsIgnoreCase("ED Club"))
							{	
								setselectedPriority("ED",true);
							}
							if(selValue.equalsIgnoreCase("ZM Club"))
							{	
								setselectedPriority("ZM",true);
							}
							if(selValue.equalsIgnoreCase("BM Club"))
							{	
								setselectedPriority("BM",true);
							}
							if(selValue.equalsIgnoreCase("CMD Elite Club"))
							{	
								setselectedPriority("CMD ELITE",true);
							}
							if(selValue.equalsIgnoreCase("MD Club"))
							{	
								setselectedPriority("MD",true);
							}
							
							if(selValue.equalsIgnoreCase("COVID"))
							{	
								setselectedPriority("COVID",true);
							}
						}
					}

				}

			}
		});
		
		/*chkCRM.addValueChangeListener(new ValueChangeListener() {

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
		});*/
		
	}
	
	public static BeanItemContainer<SpecialSelectValue> getSelectValueForPriority(){
		BeanItemContainer<SpecialSelectValue> container = new BeanItemContainer<SpecialSelectValue>(SelectValue.class);

		SpecialSelectValue selectValue1 = new SpecialSelectValue();
		selectValue1.setId(1l);
		selectValue1.setValue("All");

		SpecialSelectValue selectValue3 = new SpecialSelectValue();
		selectValue3.setId(2l);
		selectValue3.setValue("CRM Flagged");

		SpecialSelectValue selectValue4 = new SpecialSelectValue();
		selectValue4.setId(3l);
		selectValue4.setValue("VIP");

		SpecialSelectValue selectValue2 = new SpecialSelectValue();
		selectValue2.setId(4l);
		selectValue2.setValue("COVID");

		SpecialSelectValue selectValue5 = new SpecialSelectValue();
		selectValue5.setId(5l);
		selectValue5.setValue("Corporate - High Priority");
		
		SpecialSelectValue selectValue6 = new SpecialSelectValue();
		selectValue6.setId(6l);
		selectValue6.setValue("CMD Club");
		
		SpecialSelectValue selectValue7 = new SpecialSelectValue();
		selectValue7.setId(7l);
		selectValue7.setValue("ED Club");
		
		SpecialSelectValue selectValue8 = new SpecialSelectValue();
		selectValue8.setId(8l);
		selectValue8.setValue("BM Club");
		
		SpecialSelectValue selectValue9 = new SpecialSelectValue();
		selectValue9.setId(9l);
		selectValue9.setValue("ZM Club");
		
		SpecialSelectValue selectValue10 = new SpecialSelectValue();
		selectValue10.setId(10l);
		selectValue10.setValue("CMD Elite Club");
		
		SpecialSelectValue selectValue11 = new SpecialSelectValue();
		selectValue11.setId(11l);
		selectValue11.setValue("MD Club");

		container.addBean(selectValue1);
		container.addBean(selectValue3);
		container.addBean(selectValue4);
		container.addBean(selectValue2);
		container.addBean(selectValue5);
		container.addBean(selectValue6);
		container.addBean(selectValue7);
		container.addBean(selectValue8);
		container.addBean(selectValue9);
		container.addBean(selectValue10);
		container.addBean(selectValue11);
		container.sort(new Object[] {"value"}, new boolean[] {true});

		return container;
	}
	
	public SearchProcessClaimBillingFormDTO getSearchDTO()
	{
		try {
			this.binder.commit();
			SearchProcessClaimBillingFormDTO bean = this.binder.getItemDataSource().getBean();
			if(selectedPriority !=null && !selectedPriority.isEmpty()){
				bean.setSelectedPriority(selectedPriority);
			}
			return  bean;
		} catch (CommitException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	private void setselectedPriority(String priority,Boolean ischk){
		if(ischk){
			if(selectedPriority !=null 
					&& !selectedPriority.contains(priority)){
				selectedPriority.add(priority);
			}else{
				selectedPriority = new ArrayList<String>();
				selectedPriority.add(priority);
			}
		}else{
			if(selectedPriority !=null 
					&& selectedPriority.contains(priority)){
				selectedPriority.remove(priority);
			}
		}
	}

}