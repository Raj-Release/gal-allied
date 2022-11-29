package com.shaic.claim.outpatient.createbatchop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;
import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class CreateBatchOpForm extends SearchComponent<CreateBatchOpDTO>{
	
	private DateField fromDate;
	private DateField toDate;
	private ComboBox cmbType;
	//private ComboBox cmbCpuCode;
	private ComboBoxMultiselect cmbCpuCodeMulti;
	private ComboBox cmbClaimant;
	private ComboBox cmbClaimType;
	private ComboBox cmbPaymentStatus;
	private ComboBox cmbPaymentMode;
	private ComboBox cmbProduct;
	private TextField txtlotNo;
	private TextField txtClaimNo;
	private TextField txtRodNo;
	private TextField txtIntimationNo;
	private TextField txtBatchNo;
	private HorizontalLayout fieldLayout;
	private HorizontalLayout layoutForType;
	private ComboBox cmbPIOCode;
	
	private BeanItemContainer<SelectValue> type;
	private BeanItemContainer<SelectValue> cpuCode ;
	private BeanItemContainer<SelectValue> nonKeralaCpuCode;
	private BeanItemContainer<SelectValue> claimant;
	private BeanItemContainer<SelectValue> paymentStatus;
	private BeanItemContainer<SelectValue> claimType; 
	private BeanItemContainer<SpecialSelectValue> product;
	private BeanItemContainer<SelectValue> docVerified;
	private BeanItemContainer<SelectValue> paymentMode;
	private BeanItemContainer<SelectValue> pioCode;
//	private OptionGroup searchOption;
	
	private CreateBatchOpViewImpl viewImpl;
	private TextField txtQuickIntimationNo;
//	private TabSheet searchTab = null;
//	private TabSheet normalSearchTab = null;
		
	public CreateBatchOpViewImpl getViewImpl() {
		return viewImpl;
	}


	public void setViewImpl(CreateBatchOpViewImpl viewImpl) {
		this.viewImpl = viewImpl;
	}


	@PostConstruct
	public void init() {
         initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Create/Search Batch");
		VerticalLayout mainVerticalLayout2 = mainVerticalLayout();
		mainPanel.setContent(mainVerticalLayout2);
		setCompositionRoot(mainPanel);

	}
		
	
	public VerticalLayout mainVerticalLayout(){
				 
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		//btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();	
		
		fieldLayout = new HorizontalLayout();		
	    /*searchOption = new OptionGroup();
	    searchOption.addItem("Create Batch");
	    searchOption.addItem("Search Batch");*/

	   /* Collection<Boolean> searchValues = new ArrayList<Boolean>(2);
		searchValues.add(true);
		searchValues.add(false);

		searchOption.addItems(searchValues);
		searchOption.setItemCaption(true, "Create Batch");
		searchOption.setItemCaption(false, "Search Batch");*/
		/*searchOption.setStyleName("horizontal");*/
		
		cmbType = new ComboBox("Type");
				
		FormLayout formLayout = new FormLayout(cmbType);
		formLayout.setMargin(false);
		layoutForType = new HorizontalLayout(formLayout);
		buildCreateLotLayout();
		addListenerForCmb();
		
		AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		absoluteLayout_3.setWidth("100.0%");
		absoluteLayout_3.setHeight("170px");
		
		VerticalLayout vlayout = new VerticalLayout();
		vlayout.addComponents(layoutForType,fieldLayout);
		vlayout.setSpacing(true);
		
		absoluteLayout_3.addComponents(vlayout);
		
		absoluteLayout_3.addComponent(btnSearch, "top:130.0px;left:280.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:130.0px;left:359.0px;");
		mainVerticalLayout.addComponent(absoluteLayout_3);
		mainVerticalLayout.setMargin(true);
		mainVerticalLayout.setSpacing(false);
		addListener();		
		//addListener();
		
	return mainVerticalLayout;
	}
	
	
	private void buildCreateLotLayout()
	{
		
		fromDate = binder.buildAndBind("Start Date","fromDate",DateField.class);
		toDate = binder.buildAndBind("End Date","toDate",DateField.class);
	
		/*fromDate.setValue(new Date());
		toDate.setValue(new Date());*/
		
		cmbPaymentStatus = binder.buildAndBind("Payment Status","paymentStatus",ComboBox.class);
		cmbPaymentStatus.setData(true);
		cmbPaymentMode = binder.buildAndBind("Payment Mode","paymentMode",ComboBox.class);
		cmbPaymentMode.setData(true);
		txtIntimationNo = binder.buildAndBind("Intimation No","intimationNo",TextField.class);
		txtClaimNo = binder.buildAndBind("Claim No","claimNo",TextField.class);
		cmbPIOCode = binder.buildAndBind("PIO Code","pioCode",ComboBox.class);
		
		FormLayout formLayoutLeft = new FormLayout(fromDate,txtIntimationNo,cmbPaymentMode);
		formLayoutLeft.setSpacing(true);
		formLayoutLeft.setMargin(false);
		FormLayout formLayoutRight = new FormLayout(toDate,txtClaimNo,cmbPaymentStatus);
		formLayoutRight.setSpacing(true);
		formLayoutRight.setMargin(false);
		FormLayout formLayoutRight2 = new FormLayout(cmbPIOCode);
		formLayoutRight2.setSpacing(true);
		formLayoutRight2.setMargin(false);
	
		
		fieldLayout.addComponent(formLayoutLeft);
		fieldLayout.addComponent(formLayoutRight);	
		fieldLayout.addComponent(formLayoutRight2);	
		fieldLayout.setSpacing(true);
		
	}
	
	
	private void buildSearchLotLayout()
	{
		fromDate = binder.buildAndBind("Start Date","fromDate",DateField.class);
		toDate = binder.buildAndBind("End Date","toDate",DateField.class);
		
		txtClaimNo = binder.buildAndBind("Claim No","claimNo",TextField.class);
		CSValidator claimNovalidator = new CSValidator();
		claimNovalidator.extend(txtClaimNo);
		claimNovalidator.setRegExp("^[a-z A-Z 0-9 /.]*$");
		claimNovalidator.setPreventInvalidTyping(true);
		

		
		txtBatchNo =  binder.buildAndBind("Batch No","batchNo",TextField.class);
		CSValidator batchNovalidator = new CSValidator();
		batchNovalidator.extend(txtIntimationNo);
		batchNovalidator.setRegExp("^[a-z A-Z 0-9 /.]*$");
		batchNovalidator.setPreventInvalidTyping(true);
		
		
		
		FormLayout formLayoutLeft1 = new FormLayout(fromDate,txtBatchNo);
		formLayoutLeft1.setSpacing(true);
		formLayoutLeft1.setMargin(false);
		FormLayout formLayoutRight1 = new FormLayout(toDate,txtClaimNo);
		formLayoutRight1.setSpacing(true);
		formLayoutRight1.setMargin(false);
		
		fieldLayout.addComponent(formLayoutLeft1);
		fieldLayout.addComponent(formLayoutRight1);
		fieldLayout.setSpacing(true);
	}


	  private void initBinder()
	{
		this.binder = new BeanFieldGroup<CreateBatchOpDTO>(CreateBatchOpDTO.class);
		this.binder.setItemDataSource(new CreateBatchOpDTO());
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
		
	  
	  @SuppressWarnings("deprecation")
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
					
					//String batchId = (String)event.getProperty().getValue();
					 
					
					/*if (fieldLayout != null
							&& fieldLayout.getComponentCount() > 0) {
						fieldLayout.removeAllComponents();
					}*/
					
					if(cmbType.getValue() != null && cmbType.getValue().equals("Create Batch")){						
						/*unbindFields();
						fieldLayout.removeAllComponents();*/
//						buildCreateLotLayout();				
					}
					else{
						/*unbindFields();
						fieldLayout.removeAllComponents();
						buildCreateLotLayout();		*/		
					}
					//setUpDropDownValues();
//					fireViewEvent(CreateBatchOpPresenter.BUILD_RESULT_TABLE_LAYOUT_BASED_ON_SEARCH, cmbType.getValue(), null);
				}
			});
	  }
           
	  public String validate(CreateBatchOpDTO searchDTO)
		{
		  	String err = null;
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
			
			/*if(txtQuickIntimationNo.getValue() == null || txtQuickIntimationNo.getValue().equalsIgnoreCase(""))
			{
				return err = "Please Enter Intimation No";
			}*/
			
		  /*
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
			
		*/
			return null;
}
		
			
	
	public void setDropDownValues(BeanItemContainer<SelectValue> type, BeanItemContainer<SelectValue> cpuCode ,BeanItemContainer<SelectValue> claimant,
			BeanItemContainer<SelectValue> claimType, 
			BeanItemContainer<SelectValue> paymentStatus,
			BeanItemContainer<SpecialSelectValue> product,
			BeanItemContainer<SelectValue> docVerified,
			BeanItemContainer<SelectValue> paymentMode,
			BeanItemContainer<SelectValue> pioCode) 
	{
		this.paymentStatus = paymentStatus;
		this.type = type;
		this.cpuCode = cpuCode;
		this.claimant = claimant;
		this.claimType = claimType;
		this.paymentMode = paymentMode;
		this.pioCode = pioCode;
		
		this.product = product;
		this.docVerified = docVerified;
		
		
		
		type = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		List<SelectValue> selValueList = new ArrayList<SelectValue>();
		SelectValue sel1 = new SelectValue();
		sel1.setId(1l);
		sel1.setValue(SHAConstants.CREATE_BATCH_OP);
		selValueList.add(sel1);

		/*SelectValue sel2 = new SelectValue();
		sel2.setId(2l);
		sel2.setValue(SHAConstants.SEARCH_BATCH_OP);
		selValueList.add(sel2);*/
		
		type.addAll(selValueList);
		
		/*fromDate.setValue(new Date());
		toDate.setValue(new Date());*/
		
		cmbType.setContainerDataSource(type);
		cmbType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbType.setItemCaptionPropertyId("value");
		cmbType.setValue(selValueList.toArray()[0]);
		cmbType.setNullSelectionAllowed(false);
		cmbType.setEnabled(false);
		cmbType.setId(SHAConstants.COMBOBOX_NOT_RESET);
		
		
		setUpDropDownValues();
		
		//searchTab.setSelectedTab(normalSearchTab);	
				
	}	
	
	private void setUpDropDownValues()
	{
		
		/*fromDate.setValue(new Date());
		toDate.setValue(new Date());*/   
		
		cmbPaymentStatus.setContainerDataSource(paymentStatus);
	    cmbPaymentStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	    cmbPaymentStatus.setItemCaptionPropertyId("value");
	    cmbPaymentStatus.setNullSelectionAllowed(true);
	    /*Collection<?> itemIds = cmbPaymentStatus.getContainerDataSource().getItemIds();
	    if(itemIds != null && !itemIds.isEmpty()) {
	    	cmbPaymentStatus.setValue(itemIds.toArray()[1]);
	    	cmbPaymentStatus.setNullSelectionAllowed(true);
		}*/
	    
	    cmbPaymentMode.setContainerDataSource(paymentMode);	
	    cmbPaymentMode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	    cmbPaymentMode.setItemCaptionPropertyId("value");
	    cmbPaymentMode.setNullSelectionAllowed(true);
//	    Collection<?> paymentIds = cmbPaymentMode.getContainerDataSource().getItemIds();
//	    if(paymentIds != null && !paymentIds.isEmpty()) {
//	    	cmbPaymentMode.setValue(paymentIds.toArray()[1]);
//	    	cmbPaymentMode.setValue("");
//	    	cmbPaymentMode.setNullSelectionAllowed(false);
//		}
	    
	    cmbPIOCode.setContainerDataSource(pioCode);
		cmbPIOCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPIOCode.setItemCaptionPropertyId("value");
	    
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
			fieldList.add(cmbPaymentMode);
			fieldList.add(cmbProduct);
			fieldList.add(cmbPIOCode);
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

}
