package com.shaic.claim.reimbursement.createandsearchlot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;
import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class CreateAndSearchLotForm extends SearchComponent<CreateAndSearchLotFormDTO>{
	
	@EJB
	private PreauthService preauthService;
	
	private DateField fromDate;
	private DateField toDate;
	private ComboBox cmbType;
	//private ComboBox cmbCpuCode;
	private ComboBoxMultiselect cmbCpuCodeMulti;
	private ComboBox cmbClaimant;
	private ComboBox cmbClaimType;
	private ComboBox cmbPaymentStatus;
	private ComboBox cmbProduct;
	private TextField txtlotNo;
	private TextField txtClaimNo;
	private TextField txtRodNo;
	private TextField txtIntimationNo;
	private TextField txtBatchNo;
	private HorizontalLayout fieldLayout;
	private HorizontalLayout layoutForType;
	private ComboBox cbxVerificationType;

	
	private BeanItemContainer<SelectValue> type;
	private BeanItemContainer<SelectValue> cpuCode ;
	private BeanItemContainer<SelectValue> nonKeralaCpuCode;
	private BeanItemContainer<SelectValue> claimant;
	private BeanItemContainer<SelectValue> claimType; 
	private BeanItemContainer<SelectValue> paymentStatus;
	private BeanItemContainer<SpecialSelectValue> product;
	private BeanItemContainer<SelectValue> docVerified;
	private BeanItemContainer<SelectValue> verificationType;

	private CreateAndSearchLotViewImpl viewImpl;
	private TextField txtQuickIntimationNo;
	private TabSheet searchTab = null;
	private TabSheet normalSearchTab = null;
	
	private ComboBoxMultiselect priority;
	
	private CheckBox chkAll;
	
	private CheckBox chkCRM;
	
	private CheckBox chkVIP;
	
	private List<String> selectedPriority;
		
	public CreateAndSearchLotViewImpl getViewImpl() {
		return viewImpl;
	}


	public void setViewImpl(CreateAndSearchLotViewImpl viewImpl) {
		this.viewImpl = viewImpl;
	}


	@PostConstruct
	public void init() {
         initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Create Search Lot");
		
		searchTab = new TabSheet();
		//Vaadin8-setImmediate() searchTab.setImmediate(true);
		searchTab.setSizeFull();
		searchTab.setStyleName(ValoTheme.TABSHEET_FRAMED);
		
		normalSearchTab = mainVerticalLayout();
		normalSearchTab.setCaption(SHAConstants.NORMAL_SEARCH);
		searchTab.setHeight("100.0%");
		searchTab.addTab(normalSearchTab,SHAConstants.NORMAL_SEARCH, null);

		TabSheet  quickSearchTab = buildQuickSearchLayout();
		quickSearchTab.setCaption(SHAConstants.QUICK_SEARCH);
		searchTab.addTab(quickSearchTab,SHAConstants.QUICK_SEARCH, null);
		
		mainPanel.setContent(searchTab);
		tabListener(searchTab);
		searchTab.setSelectedTab(normalSearchTab);
		setCompositionRoot(mainPanel);
	}
		
	
	public TabSheet mainVerticalLayout(){
		
		TabSheet createLotTab = new TabSheet();
		createLotTab.hideTabs(true);
		//Vaadin8-setImmediate() createLotTab.setImmediate(true);
		createLotTab.setWidth("100%");
		createLotTab.setHeight("100%");
		createLotTab.setSizeFull();
		//Vaadin8-setImmediate() createLotTab.setImmediate(true);
		

		
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();	
		
		fieldLayout = new HorizontalLayout();
		
	//	dummyField = new TextField();
		
		
		
		//mainVerticalLayout.addComponent(new FormLayout(cmbType));
		cmbType = binder.buildAndBind("Type","type",ComboBox.class);
		FormLayout formLayout = new FormLayout(cmbType);
//		formLayout.setSpacing(true);
		formLayout.setMargin(false);
		layoutForType = new HorizontalLayout(formLayout);
		unbindFields();
		buildCreateLotLayout();
		
		//buildSearchLotLayout();
	
		addListenerForCmb();
		
		AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		//Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		absoluteLayout_3.setWidth("100.0%");
		absoluteLayout_3.setHeight("170px");
		
		VerticalLayout vlayout = new VerticalLayout();
		vlayout.addComponents(layoutForType,fieldLayout);
		
		absoluteLayout_3.addComponents(vlayout);
		
		absoluteLayout_3.addComponent(btnSearch, "top:130.0px;left:280.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:130.0px;left:359.0px;");
		mainVerticalLayout.addComponent(absoluteLayout_3);
		mainVerticalLayout.setMargin(true);
		mainVerticalLayout.setSpacing(false);
		addListener();
		
		createLotTab.addComponent(mainVerticalLayout);
		
		addListener();
		
		return createLotTab;
	}
	
	
	private void buildCreateLotLayout()
	{
		
		fromDate = binder.buildAndBind("Start Date","fromDate",DateField.class);
		toDate = binder.buildAndBind("End Date","toDate",DateField.class);
		
		fromDate.setData(true);
		toDate.setData(false);
		
		fromDate.setValue(new Date());
		toDate.setValue(new Date());
	//	toDate.setEnabled(false);
		
		//cmbCpuCode = binder.buildAndBind("CPU Code","cpuCode",ComboBox.class);
		//cmbCpuCode.setPageLength(21);
		
		//cmbCpuCodeMulti = binder.buildAndBind("CPU Code Multi","cpuCodeMulti",ComboBoxMultiselect.class.getFields()); //new ComboBoxMultiselect("Multi Cpu");
		cmbCpuCodeMulti = new ComboBoxMultiselect("CPU Code");
		cmbCpuCodeMulti.setShowSelectedOnTop(true);
		cmbCpuCodeMulti.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				BeanItem<CreateAndSearchLotFormDTO> dtoBeanObject = binder.getItemDataSource();
				CreateAndSearchLotFormDTO dtoObject = dtoBeanObject.getBean();
				dtoObject.setCpuCodeMulti(null);
				dtoObject.setCpuCodeMulti(event.getProperty().getValue());
			}
		});

		cmbClaimant = binder.buildAndBind("Claimant","claimant",ComboBox.class);
		
		cmbClaimType = binder.buildAndBind("Claim Type","claimType",ComboBox.class);
		cbxVerificationType = binder.buildAndBind("Account Verification Status","verificationType",ComboBox.class);
		cmbPaymentStatus = binder.buildAndBind("Payment Status","paymentStatus",ComboBox.class);
		cmbPaymentStatus.setData(true);
		cmbProduct = binder.buildAndBind("Product Name/Code","product",ComboBox.class);
		//cmbDocVerified = binder.buildAndBind("Document Verified","docVerified",ComboBox.class);
		txtIntimationNo = binder.buildAndBind("Intimation No","intimationNo",TextField.class);
		
		  /*cmbClaimant.addValueChangeListener(new ValueChangeListener() {
				@Override
				public void valueChange(Property.ValueChangeEvent event) {

					SelectValue selValue = (SelectValue)cmbClaimant.getValue();
					if(null != selValue && null != selValue.getValue())
					{
						
						if(null != selValue && null != selValue.getValue() && (SHAConstants.DOC_RECEIVED_FROM_INSURED).equalsIgnoreCase(selValue.getValue())){
							cbxVerificationType.setEnabled(true);
							cbxVerificationType.setValue(null);
						}else if(null != selValue && null != selValue.getValue() && (SHAConstants.DOC_RECEIVED_FROM_HOSPITAL).equalsIgnoreCase(selValue.getValue())){
							cbxVerificationType.setEnabled(false);
							for(SelectValue value:  verificationType.getItemIds()){
								if(value.getValue().equalsIgnoreCase(ReferenceTable.VERIFICATION_NOT_REQUIRED)){
									cbxVerificationType.setValue(value);
									break;
								}
								
							}
						}
					}else {
						if(cbxVerificationType != null){
							cbxVerificationType.setEnabled(true);
						}
					}

				}
			});*/
		
		chkAll = new CheckBox();
		
		chkCRM = new CheckBox();
		
		chkVIP =  new CheckBox();
		
		/*chkAll = binder.buildAndBind("All","priorityAll",CheckBox.class);
		chkAll.setValue(Boolean.TRUE);
		
		chkCRM = binder.buildAndBind("CRM","crm",CheckBox.class);
		
		chkVIP = binder.buildAndBind("VIP","vip",CheckBox.class);*/
		
		BeanItemContainer<SpecialSelectValue> container = getSelectValueForPriority();

		priority = new ComboBoxMultiselect("Priority");
		priority.setShowSelectedOnTop(true);
		//priority.setComparator(SHAUtils.getComparator());
		priority.setContainerDataSource(container);
		priority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		priority.setItemCaptionPropertyId("value");	
		priority.setData(container);
		
		FormLayout formLayoutLeft = new FormLayout(fromDate,cmbCpuCodeMulti,cmbClaimant);
		formLayoutLeft.setSpacing(true);
		formLayoutLeft.setMargin(false);
		FormLayout formLayoutRight = new FormLayout(toDate,cmbClaimType,cmbPaymentStatus);
		formLayoutRight.setSpacing(true);
		formLayoutRight.setMargin(false);		
		/*FormLayout formLayout3 = new FormLayout(cmbProduct);
		formLayout3.setSpacing(true);
		formLayout3.setMargin(false);*/
		
		FormLayout formLayout4 = new FormLayout(txtIntimationNo/*,cbxVerificationType*/,priority);
		formLayout4.setSpacing(true);
		formLayout4.setMargin(false);
		
		//fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight);
		fieldLayout.addComponent(formLayoutLeft);
		fieldLayout.addComponent(formLayoutRight);
	//	fieldLayout.addComponent(formLayout3);
		fieldLayout.addComponent(formLayout4);
		
		fieldLayout.setSpacing(true);
		//
	}
	
	
	private void buildSearchLotLayout()
	{
		if(fromDate != null){
			fromDate.setValue(null);
		}
		if(toDate != null){
			toDate.setValue(null);
		}
		if(toDate != null){
			toDate.setValue(null);
		}
		
		txtlotNo = binder.buildAndBind("LOT No","lotNo",TextField.class);
		txtlotNo.setMaxLength(25);
		CSValidator lotNovalidator = new CSValidator();
		lotNovalidator.extend(txtlotNo);
		lotNovalidator.setRegExp("^[a-z A-Z 0-9 /.]*$");
		lotNovalidator.setPreventInvalidTyping(true);
		
		txtClaimNo = binder.buildAndBind("Claim No","claimNo",TextField.class);
		CSValidator claimNovalidator = new CSValidator();
		claimNovalidator.extend(txtClaimNo);
		claimNovalidator.setRegExp("^[a-z A-Z 0-9 /.]*$");
		claimNovalidator.setPreventInvalidTyping(true);
		
		txtRodNo = binder.buildAndBind("ROD No","rodNo",TextField.class);
		CSValidator rodNovalidator = new CSValidator();
		rodNovalidator.extend(txtRodNo);
		rodNovalidator.setRegExp("^[a-z A-Z 0-9 /.]*$");
		rodNovalidator.setPreventInvalidTyping(true);
		
		txtIntimationNo = binder.buildAndBind("Intimation No","intimationNo",TextField.class);
		CSValidator intimationNovalidator = new CSValidator();
		intimationNovalidator.extend(txtIntimationNo);
		intimationNovalidator.setRegExp("^[a-z A-Z 0-9 /.]*$");
		intimationNovalidator.setPreventInvalidTyping(true);
		
		txtBatchNo =  binder.buildAndBind("Batch No","batchNo",TextField.class);
		CSValidator batchNovalidator = new CSValidator();
		batchNovalidator.extend(txtIntimationNo);
		batchNovalidator.setRegExp("^[a-z A-Z 0-9 /.]*$");
		batchNovalidator.setPreventInvalidTyping(true);
		
		cmbProduct = binder.buildAndBind("Product Name/Code","product",ComboBox.class);
		
		//cmbDocVerified = binder.buildAndBind("Document Verified","docVerified",ComboBox.class);
		
		FormLayout formLayoutLeft1 = new FormLayout(txtlotNo,txtRodNo,txtBatchNo);
		formLayoutLeft1.setSpacing(true);
		formLayoutLeft1.setMargin(false);
		FormLayout formLayoutRight1 = new FormLayout(txtIntimationNo,txtClaimNo,cmbProduct);
		formLayoutRight1.setSpacing(true);
		formLayoutRight1.setMargin(false);
		
		//fieldLayout = new HorizontalLayout(formLayoutLeft1,formLayoutRight1);
		fieldLayout.addComponent(formLayoutLeft1);
		fieldLayout.addComponent(formLayoutRight1);
		fieldLayout.setSpacing(true);
		//fieldLayout1.setVisible(false);
	}


	  private void initBinder()
	{
		this.binder = new BeanFieldGroup<CreateAndSearchLotFormDTO>(CreateAndSearchLotFormDTO.class);
		/*CreateAndSearchLotFormDTO createAndSearchLotFormDTO = new CreateAndSearchLotFormDTO();
		createAndSearchLotFormDTO.setFromDate(new Date());
		createAndSearchLotFormDTO.setToDate(new Date());*/
		this.binder.setItemDataSource(new CreateAndSearchLotFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	  
		
	  protected void addListener(){
			btnSearch.removeClickListener(this);
			btnReset.removeClickListener(this);
			btnSearch.addClickListener(this);
	    	btnReset.addClickListener(this);
	    	
	    	btnQuickSearchLot.removeClickListener(this);
	    	btnQuickReset.removeClickListener(this);
	    	btnQuickSearchLot.addClickListener(this);
	    	btnQuickReset.addClickListener(this);
		}
		
	  
	  public void addListenerForCmb()
	  {
		  
		  
		  btnSearch.addClickListener(new ClickListener() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					viewImpl.resetData();
				}
			});
		  
		  cmbType.addValueChangeListener(new ValueChangeListener() {
				
				
				@Override
				public void valueChange(Property.ValueChangeEvent event) {

					SelectValue selValue = (SelectValue)cmbType.getValue();
					if(null != selValue && null != selValue.getValue())
					{
						
						if (fieldLayout != null
								&& fieldLayout.getComponentCount() > 0) {
							fieldLayout.removeAllComponents();
						}
						
						if(null != selValue && null != selValue.getValue() && (SHAConstants.CREATE_LOT).equalsIgnoreCase(selValue.getValue())){
							unbindFields();
							buildCreateLotLayout();
							
						}else if(null != selValue && null != selValue.getValue() && (SHAConstants.SEARCH_LOT).equalsIgnoreCase(selValue.getValue())){
							unbindFields();
							buildSearchLotLayout();
						}
						setUpDropDownValues();
						fireViewEvent(CreateAndSearchLotPresenter.BUILD_RESULT_TABLE_LAYOUT_BASED_ON_TYPE, selValue.getValue(), null);
					}

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
	  }
           
	  public String validate(CreateAndSearchLotFormDTO searchDTO)
		{
			String err = "";
			
			if(null != searchDTO && null != searchDTO.getSearchTabType() && (SHAConstants.NORMAL_SEARCH.equalsIgnoreCase(searchDTO.getSearchTabType()))){
			
				if(cmbType.getValue() == null)
				{
					return err = "Select LOT Type";
				}
			
			if(null != cmbCpuCodeMulti && (cmbCpuCodeMulti.getValue() == null || cmbCpuCodeMulti.isEmpty()))
			{
				if(null != cmbType && null != cmbType.getValue())
				{
					SelectValue selValue = (SelectValue)cmbType.getValue();
					if(null != selValue && null != selValue.getValue() && (SHAConstants.CREATE_LOT).equalsIgnoreCase(selValue.getValue()))
					{
						return err = "Select Cpu Code";
					}
				}
				
			}
			
			/*if((null != cmbClaimant && null == cmbClaimant.getValue()))
			{
				SelectValue selValue = (SelectValue)cmbType.getValue();
				if(null != selValue && null != selValue.getValue() && (SHAConstants.CREATE_LOT).equalsIgnoreCase(selValue.getValue()))				{
				return err = "Please Select Claimant";
				}
			}
			
			if((null != cbxVerificationType && null == cbxVerificationType.getValue()))
			{
				SelectValue selValue = (SelectValue)cmbType.getValue();
				if(null != selValue && null != selValue.getValue() && (SHAConstants.CREATE_LOT).equalsIgnoreCase(selValue.getValue()))				{
				return err = "Select Account Verification Status to proceed";
				}
			}*/
			
			
			if(fromDate.getValue() == null && toDate.getValue() == null)
			{
				return err = "Select any one of the Date field for search";
			}
			else if(fromDate.getValue()!=null && toDate.getValue()!=null)
			{
				if(! fromDate.getValue().before(toDate.getValue()) && !((fromDate.getValue().compareTo(toDate.getValue()) == 0)))
				{
					return err= "Enter Valid To Date";
				}
			}
		}
			else if(null != searchDTO && null != searchDTO.getSearchTabType() && (SHAConstants.QUICK_SEARCH.equalsIgnoreCase(searchDTO.getSearchTabType())))
			{
				if(txtQuickIntimationNo.getValue() == null || txtQuickIntimationNo.getValue().equalsIgnoreCase(""))
				{
					return err = "Please Enter Intimation No";
				}
			}
			
			return null;
			
		}
		
			
	
	public void setDropDownValues(BeanItemContainer<SelectValue> type, BeanItemContainer<SelectValue> cpuCode ,BeanItemContainer<SelectValue> claimant,
			BeanItemContainer<SelectValue> claimType, BeanItemContainer<SelectValue> paymentStatus,BeanItemContainer<SpecialSelectValue> product,BeanItemContainer<SelectValue> docVerified,BeanItemContainer<SelectValue> verificationType) 
	{
		
		this.type = type;
		this.cpuCode = cpuCode;
		this.claimant = claimant;
		this.claimType = claimType;
		this.paymentStatus = paymentStatus;
		this.product = product;
		this.docVerified = docVerified;
		this.verificationType = verificationType;
		
		
		
		type = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		List<SelectValue> selValueList = new ArrayList<SelectValue>();
		SelectValue sel1 = new SelectValue();
		sel1.setId(1l);
		sel1.setValue(SHAConstants.CREATE_LOT);
		selValueList.add(sel1);

		SelectValue sel2 = new SelectValue();
		sel2.setId(2l);
		sel2.setValue(SHAConstants.SEARCH_LOT);
		selValueList.add(sel2);
		
		type.addAll(selValueList);
		
		fromDate.setValue(new Date());
		toDate.setValue(new Date());
		
		cmbType.setContainerDataSource(type);
		cmbType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbType.setItemCaptionPropertyId("value");
		
		
		setUpDropDownValues();
		
		searchTab.setSelectedTab(normalSearchTab);	
				
	}	
	
	private void setUpDropDownValues()
	{
		
		fromDate.setValue(new Date());
		toDate.setValue(new Date());

		/*cmbCpuCode.setContainerDataSource(cpuCode);
		cmbCpuCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCpuCode.setItemCaptionPropertyId("value");	
		List<SelectValue> filterList = new ArrayList<SelectValue>();
		List<SelectValue> cpuCode1 = (List<SelectValue>) cpuCode.getItemIds();
		 if(cpuCode1 != null && !cpuCode1.isEmpty()) {
			 for (SelectValue selectValue : cpuCode1) {
				 
				 if(! ReferenceTable.getRemovableCpuCodeList().containsKey(selectValue.getId())){
					 filterList.add(selectValue);
				 }
			}
			 
		   }
		    cmbCpuCode.removeAllItems();
		 
		    cpuCode.addAll(filterList);
			cmbCpuCode.setContainerDataSource(cpuCode);
			cmbCpuCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbCpuCode.setItemCaptionPropertyId("value");*/
		
		cmbCpuCodeMulti.setContainerDataSource(cpuCode);
		cmbCpuCodeMulti.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCpuCodeMulti.setItemCaptionPropertyId("value");	
		
		List<SelectValue> filterList = new ArrayList<SelectValue>();
		List<SelectValue> cpuCode1 = (List<SelectValue>) cpuCode.getItemIds();
		 if(cpuCode1 != null && !cpuCode1.isEmpty()) {
			 for (SelectValue selectValue : cpuCode1) {
				 
				 if(! ReferenceTable.getRemovableCpuCodeList().containsKey(selectValue.getId())){
					 filterList.add(selectValue);
				 }
			}
			 
		   }
		 cmbCpuCodeMulti.removeAllItems();
		 cpuCode.addAll(filterList);
			
		cmbCpuCodeMulti.setContainerDataSource(cpuCode);
		cmbCpuCodeMulti.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCpuCodeMulti.setItemCaptionPropertyId("value");	


		cmbClaimant.setContainerDataSource(claimant);
		cmbClaimant.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbClaimant.setItemCaptionPropertyId("value");
		
	    cmbClaimType.setContainerDataSource(claimType);
	    cmbClaimType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	    cmbClaimType.setItemCaptionPropertyId("value");
	    
	    cmbPaymentStatus.setContainerDataSource(paymentStatus);
	    cmbPaymentStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	    cmbPaymentStatus.setItemCaptionPropertyId("value");
	    Collection<?> itemIds = cmbPaymentStatus.getContainerDataSource().getItemIds();
	    if(itemIds != null && !itemIds.isEmpty()) {
	    	cmbPaymentStatus.setValue(itemIds.toArray()[1]);
	    	cmbPaymentStatus.setNullSelectionAllowed(true);
		}
	    
	    cmbProduct.setContainerDataSource(product);
	    cmbProduct.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	    cmbProduct.setItemCaptionPropertyId("specialValue");
	    
	    //cmbDocVerified.setContainerDataSource(docVerified);
	    //cmbDocVerified.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	    //cmbDocVerified.setItemCaptionPropertyId("value");
	    cbxVerificationType.setContainerDataSource(verificationType);
	    cbxVerificationType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	    cbxVerificationType.setItemCaptionPropertyId("value");
	}
	
	
	private void unbindFields() {
		
		List<Field<?>> field = getListOfFeilds();
		if(null != field && !field.isEmpty())
		{
			for (Field<?> field2 : field) {
				if (field2 != null ) {
					Object propertyId = this.binder.getPropertyId(field2);
					//if (field2!= null && field2.isAttached() && propertyId != null) {
					if (field2!= null  && propertyId != null) {
						this.binder.unbind(field2);
					}
				}
			}
		}
	}
	
	public void resetFlds()
	{
		List<Field<?>>  fieldList = getListOfFeilds();
		if(null != fieldList && !fieldList.isEmpty())
		{
			for (Field<?> field : fieldList) {
				if (field != null ) {
					if(field instanceof DateField){
						if(((DateField) field).getData() != null){
							continue;
						}
					}else if(field instanceof ComboBox){
						if(((ComboBox) field).getData() != null){
							continue;
						}
					}else if(field instanceof ComboBoxMultiselect){
						if(((ComboBoxMultiselect) field).getData() != null){
							continue;
						}
					}
					field.setValue(null);
				}
			}
		}
	}
	
	private List<Field<?>> getListOfSearchLotFeilds()
	{
			List<Field<?>>  fieldList = new ArrayList<Field<?>>();
			
			return fieldList;
	}
	
	private List<Field<?>> getListOfFeilds()
	{
			List<Field<?>>  fieldList = new ArrayList<Field<?>>();
			fieldList.add(fromDate);
			fieldList.add(toDate);
			//fieldList.add(cmbCpuCode);
			fieldList.add(cmbCpuCodeMulti);
			fieldList.add(cmbClaimType);
			fieldList.add(cmbClaimant);
			fieldList.add(cmbPaymentStatus);
			fieldList.add(cmbProduct);
			fieldList.add(cbxVerificationType);
			//fieldList.add(cmbDocVerified);
			if(null != txtlotNo)
				fieldList.add(txtlotNo);
			if(null != txtIntimationNo)
				fieldList.add(txtIntimationNo);
			if(null != txtRodNo )
				fieldList.add(txtRodNo);
			if(null != txtClaimNo )
				fieldList.add(txtClaimNo);
			if(null != txtBatchNo)
				fieldList.add(txtBatchNo);
			
			return fieldList;
	}


	private TabSheet buildQuickSearchLayout()
	{
		unbindFields();
		TabSheet createLotTab = new TabSheet();
		createLotTab.hideTabs(true);
		//Vaadin8-setImmediate() createLotTab.setImmediate(true);
		createLotTab.setWidth("100%");
		createLotTab.setHeight("100%");
		createLotTab.setSizeFull();
		//Vaadin8-setImmediate() createLotTab.setImmediate(true);
		
		quickVerticallayout = new VerticalLayout();

		txtQuickIntimationNo = binder.buildAndBind("Intimation No","quickIntimationNo",TextField.class);
		CSValidator intimationNovalidator = new CSValidator();
		intimationNovalidator.extend(txtQuickIntimationNo);
		intimationNovalidator.setRegExp("^[a-z A-Z 0-9 /.]*$");
		intimationNovalidator.setPreventInvalidTyping(true);
		
		HorizontalLayout buttonlayout = new HorizontalLayout(btnQuickSearchLot,btnQuickReset);
		buttonlayout.setWidth("70%");
		buttonlayout.setComponentAlignment(btnQuickSearchLot,  Alignment.MIDDLE_RIGHT);
		buttonlayout.setSpacing(true);
		
		FormLayout formLayout= new FormLayout(txtQuickIntimationNo);
		formLayout.setSpacing(false);
		formLayout.setMargin(false);
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.addComponents(formLayout);
		hLayout.setMargin(false);
		
		quickVerticallayout.addComponents(hLayout,buttonlayout);
		quickVerticallayout.setComponentAlignment(buttonlayout, Alignment.MIDDLE_LEFT);
		quickVerticallayout.setSpacing(true);
		quickVerticallayout.setMargin(true);
		addListener();
		createLotTab.addComponent(quickVerticallayout);
		
		return createLotTab;
	}
	
	
	public void tabListener(TabSheet searchTab){
		
		searchTab.addSelectedTabChangeListener(new SelectedTabChangeListener() {
			
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				if(event.getTabSheet().getSelectedTab().getCaption() != null) {
					if(SHAConstants.NORMAL_SEARCH.equalsIgnoreCase(event.getTabSheet().getSelectedTab().getCaption())) {
						refresh();
						viewImpl.setSplitPosition(SHAConstants.NORMAL_SEARCH);
						btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
						btnSearch.setDisableOnClick(true);
					} else if(SHAConstants.QUICK_SEARCH.equalsIgnoreCase(event.getTabSheet().getSelectedTab().getCaption())) {
						refresh();
						viewImpl.setSplitPosition(SHAConstants.QUICK_SEARCH);
						btnSearch.setCaption(SearchComponent.QUICK_SEARCH_LOT_TASK_CAPTION);
						btnSearch.setDisableOnClick(true);
						
					}
				}
				
			}
		});
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

		SpecialSelectValue selectValue5 = new SpecialSelectValue();
		selectValue5.setId(5l);
		selectValue5.setValue("Corporate - High Priority");

		container.addBean(selectValue1);
		container.addBean(selectValue3);
		container.addBean(selectValue4);
		container.addBean(selectValue5);
		container.sort(new Object[] {"value"}, new boolean[] {true});

		return container;
	}
	
	public CreateAndSearchLotFormDTO getSearchDTO()
	{
		try {
			this.binder.commit();
			CreateAndSearchLotFormDTO bean = this.binder.getItemDataSource().getBean();
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
