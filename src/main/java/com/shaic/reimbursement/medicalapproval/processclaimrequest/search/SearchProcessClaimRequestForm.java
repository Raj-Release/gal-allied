/**
 * 
 */
package com.shaic.reimbursement.medicalapproval.processclaimrequest.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;
import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.ClaimService;
import com.shaic.domain.MasUser;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
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
public class SearchProcessClaimRequestForm extends SearchComponent<SearchProcessClaimRequestFormDTO> {
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private PreauthService preauthService;
	
	
	private TextField txtIntimationNo;
	private TextField txtPolicyNo;
	private ComboBox cbxhospitalType;
	private ComboBox cbxType;
	private ComboBox cbxIntimationSource;
	private ComboBox cbxNetworkHospType;
	private ComboBox cbxTreatmentType;
	private ComboBox cbxSpeciality;
	private ComboBox cmbCpuCode;
	private ComboBox cmbProductName;
	private ComboBox cmbPriority;
//	private ComboBox cmbPriorityNew;
	private ComboBoxMultiselect priority;
	private CheckBox chkAll;
	private CheckBox chkCRM;
	private CheckBox chkVIP;
	private ComboBox cmbSource;
	private TextField txtClaimedAmountFrom;
	private TextField txtClaimedAmountTo;
	private ComboBox cmbRequestBy;
	private ComboBox cmbClaimType;
	private ComboBox cmbType;
	private String screenName;
	
	private BeanItemContainer<SelectValue> networkHospitalType;
	private BeanItemContainer<SelectValue> Speicalityvalue ;
	
	private List<String> selectedPriority;
	
	private SearchProcessClaimRequestFormDTO dto =  new SearchProcessClaimRequestFormDTO();
	
	Panel mainPanel = null;
	
	@EJB
	private ClaimService claimService;
	
	@PostConstruct
	public void init() {
		
	}
	
	public void initView(SearchProcessClaimRequestFormDTO dto, Boolean shouldDoSearch) {
		initBinder(dto);
		
		mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setContent(mainVerticalLayout());
		mainPanel.setHeight("255px");
		setCompositionRoot(mainPanel);
		cbxhospitalListener();
	}
	
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
	//	btnSearch.setStyleName("hover");
		mainVerticalLayout = new VerticalLayout();
		
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		
		txtPolicyNo = binder.buildAndBind("Policy Number","policyNo",TextField.class);
		
		cbxhospitalType = binder.buildAndBind("Hospital Type","hospitalType",ComboBox.class);
		
		cbxType = binder.buildAndBind("Type","type",ComboBox.class);
//		cbxType.setEnabled(false);
		
		cbxIntimationSource = binder.buildAndBind("Intimation Source","intimationSource",ComboBox.class);
		
		cbxNetworkHospType = binder.buildAndBind("Network Hosp Type","networkHospType",ComboBox.class);
		
		cbxTreatmentType = binder.buildAndBind("Treatment Type","treatementType",ComboBox.class);
		
		cbxSpeciality = binder.buildAndBind("Speciality","speciality",ComboBox.class);
		
		cmbCpuCode = binder.buildAndBind("CPU Code","cpuCode",ComboBox.class);
		
		cmbProductName = binder.buildAndBind("Product Name/Code","productName",ComboBox.class);
		
		cmbPriority = binder.buildAndBind("Priority(IRDA)","priority",ComboBox.class);
		
//		cmbPriorityNew = binder.buildAndBind("Priority","priorityNew",ComboBox.class);
		
		cmbSource = binder.buildAndBind("Source","source",ComboBox.class);
		
		cmbClaimType = binder.buildAndBind("Claim Type","claimType",ComboBox.class);
		
		cmbRequestBy = binder.buildAndBind("Requested User","requestedBy",ComboBox.class);
		
		txtClaimedAmountFrom = binder.buildAndBind("Claimed Amount From", "claimedAmountFrom", TextField.class);
		txtClaimedAmountFrom.setNullRepresentation("");
		
		cmbType = binder.buildAndBind("Type","pendingStatusType",ComboBox.class);
		cmbType.setId(SHAConstants.COMBOBOX_NOT_RESET);
		
		CSValidator validator = new CSValidator();
		validator.extend(txtClaimedAmountFrom);
		validator.setRegExp("^[0-9.]*$");
		validator.setPreventInvalidTyping(true);
		
		txtClaimedAmountTo = binder.buildAndBind("Claimed Amount To", "claimedAmountTo", TextField.class);
		txtClaimedAmountTo.setNullRepresentation("");
		
		CSValidator validator1 = new CSValidator();
		validator1.extend(txtClaimedAmountTo);
		validator1.setRegExp("^[0-9.]*$");
		validator1.setPreventInvalidTyping(true);
		
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
		
		FormLayout formLayout1 = new FormLayout(txtIntimationNo,cmbCpuCode,cbxIntimationSource,cbxhospitalType,priority);
		formLayout1.setMargin(false);
		FormLayout formLayout2 = new FormLayout(cmbSource,cmbRequestBy,cmbClaimType,cmbProductName);	
		formLayout2.setMargin(false);
		FormLayout formLayout3 = new FormLayout(txtPolicyNo,cbxNetworkHospType,cbxTreatmentType,cbxSpeciality);
		formLayout3.setMargin(false);
		FormLayout formLayout4 = new FormLayout(txtClaimedAmountFrom,txtClaimedAmountTo,cmbPriority,cbxType/*,cmbPriorityNew*/);
		formLayout4.setMargin(false);
		
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayout1,formLayout2,formLayout3,formLayout4);	
		
		cbxhospitalListener();	

		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 absoluteLayout_3.addComponent(fieldLayout);	
		absoluteLayout_3.addComponent(formLayoutChk,"top:125.0px;left:14.0px;");	
		absoluteLayout_3.addComponent(btnSearch, "top:160.0px;left:260.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:160.0px;left:370.0px;");
		
		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("1500px");
		// mainVerticalLayout.setHeight("500px");
		 mainVerticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 
		 absoluteLayout_3.setHeight("220px");
		
		addListener();
		
//		setManaualFlag();
		
		return mainVerticalLayout;
	}
	
	private void initBinder(SearchProcessClaimRequestFormDTO dto)
	{
		if(dto != null) {
			this.dto = dto;
		} else {
			this.dto = new SearchProcessClaimRequestFormDTO();
		}
		
		this.binder = new BeanFieldGroup<SearchProcessClaimRequestFormDTO>(SearchProcessClaimRequestFormDTO.class);
		this.binder.setItemDataSource(this.dto);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public void setCBXValue(BeanItemContainer<SelectValue> intimationSource,
			BeanItemContainer<SelectValue> hospitalType,
			BeanItemContainer<SelectValue> networkHospitalType,
			BeanItemContainer<SelectValue> treatementType, BeanItemContainer<SelectValue> typeContainer, 
			BeanItemContainer<SelectValue> productName1, BeanItemContainer<SelectValue> cpuCode, BeanItemContainer<SelectValue> statusByStage,
			BeanItemContainer<SelectValue> claimType,String screenName) {
		
		if(null != screenName && (SHAConstants.MEDICAL_PENDING_SCREEN.equalsIgnoreCase(screenName))){
			
			mainPanel.setCaption("Process Claim Request");
		}
		else if(null != screenName && (SHAConstants.WAIT_FOR_INPUT_SCREEN.equalsIgnoreCase(screenName)))
		{
			mainPanel.setCaption("Wait For Input");
		}

		cbxIntimationSource.setContainerDataSource(intimationSource);
		cbxIntimationSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxIntimationSource.setItemCaptionPropertyId("value");
		if(this.dto != null && this.dto.getIntimationSource() != null) {
			cbxIntimationSource.setValue(this.dto.getIntimationSource());
		}
		cbxhospitalType.setContainerDataSource(hospitalType);
		cbxhospitalType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxhospitalType.setItemCaptionPropertyId("value");
		if(this.dto != null && this.dto.getHospitalType() != null) {
			cbxhospitalType.setValue(this.dto.getHospitalType());
		}
		cbxTreatmentType.setContainerDataSource(treatementType);
		cbxTreatmentType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxTreatmentType.setItemCaptionPropertyId("value");
		if(this.dto != null && this.dto.getTreatementType() != null) {
			cbxTreatmentType.setValue(this.dto.getTreatementType());
		}
		BeanItemContainer<SelectValue> selectValueForType = SHAUtils.getSelectValueForType();
		
		BeanItemContainer<SpecialSelectValue> productName = masterService.getContainerForProduct();
		
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		
		BeanItemContainer<SelectValue> employeeLoginNameContainer = masterService.getEmployeeLoginContainer(userName);
		
		cmbClaimType.setContainerDataSource(claimType);
		cmbClaimType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbClaimType.setItemCaptionPropertyId("value");
		if(this.dto != null && this.dto.getClaimType() != null) {
			cmbClaimType.setValue(this.dto.getClaimType());
		}
		
		cbxType.setContainerDataSource(selectValueForType);
		cbxType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxType.setItemCaptionPropertyId("value");
		if(this.dto != null && this.dto.getType() != null) {
			cbxType.setValue(this.dto.getType());
		}
		
		cmbCpuCode.setContainerDataSource(cpuCode);
		cmbCpuCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCpuCode.setItemCaptionPropertyId("value");
		if(this.dto != null && this.dto.getCpuCode() != null) {
			cmbCpuCode.setValue(this.dto.getCpuCode());
		}
		
		cmbProductName.setContainerDataSource(productName);
		cmbProductName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbProductName.setItemCaptionPropertyId("specialValue");
		if(this.dto != null && this.dto.getProductName() != null) {
			cmbProductName.setValue(this.dto.getProductName());
		}
		
		BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils.getSelectValueForPriorityIRDA();
		cmbPriority.setContainerDataSource(selectValueForPriority);
		cmbPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPriority.setItemCaptionPropertyId("value");
		if(this.dto != null && this.dto.getPriority() != null) {
			cmbPriority.setValue(this.dto.getPriority());
		}
		
		/*BeanItemContainer<SelectValue> selectValueForPriorityNew = SHAUtils.getSelectValueForPriorityNew();
		
		cmbPriorityNew.setContainerDataSource(selectValueForPriorityNew);
		cmbPriorityNew.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPriorityNew.setItemCaptionPropertyId("value");
		cmbPriorityNew.setValue(selectValueForPriorityNew.getItemIds().get(0));*/
		
		cmbSource.setContainerDataSource(statusByStage);
		cmbSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSource.setItemCaptionPropertyId("value");
		if(this.dto != null && this.dto.getSource() != null) {
			cmbSource.setValue(this.dto.getSource());
		}
		
		cmbRequestBy.setContainerDataSource(employeeLoginNameContainer);
		cmbRequestBy.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbRequestBy.setItemCaptionPropertyId("value");
		if(this.dto != null && this.dto.getRequestedBy() != null) {
			cmbRequestBy.setValue(this.dto.getRequestedBy());
		}
		
		SelectValue recordType = new SelectValue();
		
		if(null != screenName && (SHAConstants.WAIT_FOR_INPUT_SCREEN.equalsIgnoreCase(screenName))){
			
			recordType.setId(51l);
			recordType.setValue(SHAConstants.PARALLEL_WAITING_FOR_INPUT);
		}
		else if(null != screenName && (SHAConstants.MEDICAL_PENDING_SCREEN.equalsIgnoreCase(screenName))){

			recordType.setId(49l);
			recordType.setValue(SHAConstants.PARALLEL_MEDICAL_PENDING);
		}
		

		List<SelectValue> selectVallueList = new ArrayList<SelectValue>();
		selectVallueList.add(recordType);
		
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		selectValueContainer.addAll(selectVallueList);
		cmbType.setContainerDataSource(selectValueContainer);
		cmbType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbType.setItemCaptionPropertyId("value");
		cmbType.setValue(recordType);
		cmbType.setId(SHAConstants.COMBOBOX_NOT_RESET);
		
		employeeLoginNameContainer.sort(new Object[] {"value"}, new boolean[] {true});
		
		statusByStage.sort(new Object[] {"value"}, new boolean[] {true});

		this.networkHospitalType = networkHospitalType;
		
		this.screenName = screenName;
		setManaualFlag();
	}	
	private void cbxhospitalListener(){
		cbxhospitalType.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
			
				
				
				if(cbxhospitalType.getValue() != null){
					if(ReferenceTable.HOSPITAL_NETWORK.equals(cbxhospitalType.getValue().toString())){
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
		
		cbxTreatmentType.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
			
				if(cbxTreatmentType.getValue() != null ){
					if(ReferenceTable.MEDICAL.equals(cbxTreatmentType.getValue().toString())){
					
						fireViewEvent(SearchProcessClaimRequestPresenter.SPECIALITY, ReferenceTable.MEDICAL_CODE );
						
						cbxSpeciality.setContainerDataSource(Speicalityvalue);
						cbxSpeciality.setItemCaptionMode(ItemCaptionMode.PROPERTY);
						cbxSpeciality.setItemCaptionPropertyId("value");
					}else if(ReferenceTable.SURGICAL.equals(cbxTreatmentType.getValue().toString())){
					
						fireViewEvent(SearchProcessClaimRequestPresenter.SPECIALITY,ReferenceTable.SURGICAL_CODE );
						
						cbxSpeciality.setContainerDataSource(Speicalityvalue);
						cbxSpeciality.setItemCaptionMode(ItemCaptionMode.PROPERTY);
						cbxSpeciality.setItemCaptionPropertyId("value");
					}
					
				}else{
					cbxSpeciality.setContainerDataSource(null);
				}
			}
		});
		
	cmbSource.addValueChangeListener(new ValueChangeListener() {
		private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(Property.ValueChangeEvent event) {
			
				if(cmbSource.getValue() != null ) {
					SelectValue value = (SelectValue) cmbSource.getValue();
					if(value != null && ReferenceTable.getReplyReceivedStatus().containsKey(value.getId())) {
						if(cmbRequestBy != null && !cmbRequestBy.getItemIds().isEmpty()) {
							cmbRequestBy.setValue(cmbRequestBy.getItemIds().toArray()[0]);
						}
					} else {
						cmbRequestBy.setValue(null);
					}
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
			priority.setValue(null);
			setselectedPriority("CRM",false);
			setselectedPriority("VIP",false);
			setselectedPriority("ATOS",false);
			setselectedPriority("CMD",false);
			setselectedPriority("BM",false);
			setselectedPriority("ZM",false);
			setselectedPriority("ED",false);
			setselectedPriority("MD",false);
			setselectedPriority("CMD ELITE",false);
			
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
	
/*	chkCRM.addValueChangeListener(new ValueChangeListener() {

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

	public void setSpecialityCBX(
			BeanItemContainer<SelectValue> specialityValueByReference) {
		Speicalityvalue = specialityValueByReference;
		
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
	
	public SearchProcessClaimRequestFormDTO getSearchDTO()
	{
		try {
			this.binder.commit();
			SearchProcessClaimRequestFormDTO bean = this.binder.getItemDataSource().getBean();
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
	
	//GLX2021004
	 public void setManaualFlag() {
		 if(null != screenName && (SHAConstants.MEDICAL_PENDING_SCREEN.equalsIgnoreCase(screenName))){
				ImsUser imsUser1 = null;
				if(VaadinSession.getCurrent().getAttribute(BPMClientContext.USER_OBJECT) != null) {
					imsUser1 = (ImsUser) VaadinSession.getCurrent().getAttribute(BPMClientContext.USER_OBJECT);
				}
				MasUser masUser = claimService.getUserName(imsUser1.getUserName());
				if(masUser != null && masUser.getManualPickFlagMA() != null && masUser.getManualPickFlagMA().equalsIgnoreCase("Y")){
					btnSearch.setEnabled(true);
				}else if(masUser != null && masUser.getManualPickFlagMA() != null && masUser.getManualPickFlagMA().equalsIgnoreCase("N")){
					btnSearch.setEnabled(false);
				}
				else{
					btnSearch.setEnabled(false);
				}
		 }else {
			 btnSearch.setEnabled(true);
		 }
	}
}