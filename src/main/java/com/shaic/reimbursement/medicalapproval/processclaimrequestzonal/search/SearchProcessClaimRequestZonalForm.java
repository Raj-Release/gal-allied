/**
 * 
 */
package com.shaic.reimbursement.medicalapproval.processclaimrequestzonal.search;

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
import com.shaic.claim.preauth.search.SearchPreauthPresenter;
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
public class SearchProcessClaimRequestZonalForm extends SearchComponent<SearchProcessClaimRequestZonalFormDTO> {
	
	private static final long serialVersionUID = -4088910426204201267L;

	@Inject
	private SearchProcessClaimRequestZonalTable searchTable;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private PreauthService preauthService;
	
	private TextField txtIntimationNo;
	private TextField txtPolicyNo;
	private ComboBox cbxhospitalType;
	private ComboBox cbxType;
	private ComboBox cmbCpuCode;
	private ComboBox cmbProductName;
	private ComboBox cbxIntimationSource;
	private ComboBox cbxNetworkHospType;
	private ComboBox cmbPriority;
	private ComboBox cmbSource;
	
	private BeanItemContainer<SelectValue> networkHospitalType;

//	private ComboBox cmbPriorityNew;
	
	private ComboBoxMultiselect priority;
	
	private CheckBox chkAll;
	
	private CheckBox chkCRM;
	
	private CheckBox chkVIP;
	
	private CheckBox chkATOS;
	
	private List<String> selectedPriority;
	
	public SearchProcessClaimRequestZonalFormDTO getSearchDTO()
	{
		try {
			this.binder.commit();
			SearchProcessClaimRequestZonalFormDTO bean = this.binder.getItemDataSource().getBean();
			if(selectedPriority !=null && !selectedPriority.isEmpty()){
				bean.setSelectedPriority(selectedPriority);
			}
			return  bean;
		} catch (CommitException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Process Claim Request (Zonal Medical Review)");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
		cbxhospitalListener();
	}
	
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		
		txtPolicyNo = binder.buildAndBind("Policy Number","policyNo",TextField.class);
		
		cbxhospitalType = binder.buildAndBind("Hospital Type","hospitalType",ComboBox.class);
		//Vaadin8-setImmediate() cbxhospitalType.setImmediate(true);
		
         cmbCpuCode = binder.buildAndBind("CPU Code","cpuCode",ComboBox.class);
		
		cmbProductName = binder.buildAndBind("Product Name/Code","productName",ComboBox.class);

		cbxType = binder.buildAndBind("Type","type",ComboBox.class);
		cmbSource = binder.buildAndBind("Source","source",ComboBox.class);
		
		cbxIntimationSource = binder.buildAndBind("Intimation Source","intimationSource",ComboBox.class);
		
		cbxNetworkHospType = binder.buildAndBind("Network Hosp Type","networkHospType",ComboBox.class);
		cmbPriority = binder.buildAndBind("Priority (IRDA)","priority",ComboBox.class);
		
//		cmbPriorityNew = binder.buildAndBind("Priority","priorityNew",ComboBox.class);
		
		/*priority = new Label();
		priority.setCaption("Priority");*/
		
		chkAll = binder.buildAndBind("All","priorityAll",CheckBox.class);
		//chkAll.setValue(Boolean.TRUE);
		
		chkCRM = binder.buildAndBind("CRM","crm",CheckBox.class);
		
		chkVIP = binder.buildAndBind("VIP","vip",CheckBox.class);
		
		chkATOS = binder.buildAndBind("ATOS","atos",CheckBox.class);
		
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
		cbxhospitalListener();
		
		/*FormLayout formLayoutLeft = new FormLayout(txtIntimationNo,txtPolicyNo,cbxhospitalType,cmbProductName,cmbPriority,cmbPriorityNew);
		FormLayout formLayoutReight = new FormLayout(cbxType,cbxIntimationSource,cmbCpuCode,cbxNetworkHospType,cmbSource);*/
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo,txtPolicyNo,cmbCpuCode,priority);
		FormLayout formLayoutMiddleLeft = new FormLayout(cbxType,cbxIntimationSource,cmbProductName);
		FormLayout formLayoutMiddleRight = new FormLayout(cbxhospitalType,cbxNetworkHospType,cmbSource);	
		FormLayout formLayoutRight = new FormLayout(cmbPriority);	
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutMiddleLeft,formLayoutMiddleRight,formLayoutRight);

		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 absoluteLayout_3.addComponent(fieldLayout);
		absoluteLayout_3.addComponent(formLayoutChk,"top:120.0px;left:14.0px;");		
		absoluteLayout_3.addComponent(btnSearch, "top:160.0px;left:260.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:160.0px;left:370.0px;");
		
		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("1400px");
		// mainVerticalLayout.setHeight("500px");
		 mainVerticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 
		 absoluteLayout_3.setHeight("222px");
		 addListener();
		
		return mainVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchProcessClaimRequestZonalFormDTO>(SearchProcessClaimRequestZonalFormDTO.class);
		this.binder.setItemDataSource(new SearchProcessClaimRequestZonalFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public void setCBXValue(BeanItemContainer<SelectValue> intimationSource,
			BeanItemContainer<SelectValue> hospitalType,
			BeanItemContainer<SelectValue> networkHospitalType, BeanItemContainer<SelectValue> typeContainer, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage) {
		cbxIntimationSource.setContainerDataSource(intimationSource);
		cbxIntimationSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxIntimationSource.setItemCaptionPropertyId("value");
		
		
		cbxhospitalType.setContainerDataSource(hospitalType);
		cbxhospitalType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxhospitalType.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> selectValueForType = SHAUtils.getSelectValueForType();
		
		cbxType.setContainerDataSource(selectValueForType);
		cbxType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxType.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SpecialSelectValue> productName = masterService.getContainerForProduct();
		BeanItemContainer<SelectValue> cpuCode = masterService.getTmpCpuCodes();
		
		cmbCpuCode.setContainerDataSource(cpuCode);
		cmbCpuCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCpuCode.setItemCaptionPropertyId("value");
		
		cmbProductName.setContainerDataSource(productName);
		cmbProductName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbProductName.setItemCaptionPropertyId("specialValue");
		
		selectValueForPriority = SHAUtils.getSelectValueForPriorityIRDA();
		
		cmbPriority.setContainerDataSource(selectValueForPriority);
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
		
//		cbxNetworkHospType.setContainerDataSource(networkHospitalType);
//		cbxNetworkHospType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//		cbxNetworkHospType.setItemCaptionPropertyId("value");
		
		this.networkHospitalType = networkHospitalType;
		
	}	
	
	private void cbxhospitalListener(){
		cbxhospitalType.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
			
				
				
				if(cbxhospitalType.getValue() != null){
					System.out.println("ggggggggggggggggggggggg"+cbxhospitalType.getValue());
				if(  ReferenceTable.HOSPITAL_NETWORK.equals(cbxhospitalType.getValue().toString())){
					cbxNetworkHospType.setContainerDataSource(networkHospitalType);
					cbxNetworkHospType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					cbxNetworkHospType.setItemCaptionPropertyId("value");
				}else{
					cbxNetworkHospType.setContainerDataSource(null);
				}
			}else{
					cbxNetworkHospType.setContainerDataSource(null);
				}
			}
			});
		
		btnReset.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				refresh();
				chkAll.setValue(false);
				chkCRM.setValue(false);
				chkVIP.setValue(false);
				chkATOS.setValue(false);
				priority.setValue(null);
				setselectedPriority("CRM",false);
				setselectedPriority("VIP",false);
				setselectedPriority("ATOS",false);
				
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
					chkATOS.setValue(false);

					if(docList != null)
					{
						setselectedPriority("CRM",false);
						setselectedPriority("VIP",false);
						setselectedPriority("COVID",false);
						setselectedPriority("ATOS",false);


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

		/*SpecialSelectValue selectValue2 = new SpecialSelectValue();
					selectValue2.setId(4l);
					selectValue2.setValue("COVID");*/

		SpecialSelectValue selectValue5 = new SpecialSelectValue();
		selectValue5.setId(5l);
		selectValue5.setValue("Corporate - High Priority");

		container.addBean(selectValue1);
		container.addBean(selectValue3);
		container.addBean(selectValue4);
		//container.addBean(selectValue2);
		container.addBean(selectValue5);
		container.sort(new Object[] {"value"}, new boolean[] {true});

		return container;
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