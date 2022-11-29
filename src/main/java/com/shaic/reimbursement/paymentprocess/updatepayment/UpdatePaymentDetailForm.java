 /**
 * 
 */
package com.shaic.reimbursement.paymentprocess.updatepayment;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.reimbursement.paymentprocess.createbatch.search.PendingLotBatchCasesTable;
import com.shaic.reimbursement.paymentprocess.createbatch.search.PendingLotBatchReportDto;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.narenj
 *
 */
public class UpdatePaymentDetailForm extends SearchComponent<UpdatePaymentDetailFormDTO> {
	
	@Inject
	private UpdatePaymentDetailListenerTable searchAcknowledgementDocumentReceiverTable;
	
	@Inject
	private PendingLotBatchCasesTable pendingCasesTable;
	
	ExcelExport excelExport;
	
	private ComboBox cbxType;
	
	private ComboBox cbxPaymentMode;
	private ComboBox cbxCPUCode;
	private ComboBox cbxClaimType;
	private ComboBox cbxClaimant;
	
	private TextField txtIntimationNo;
	private TextField txtLOTNo;
	private TextField txtRODNo;
	private TextField txtClaimNo;	
	private TextField txtDummyField;
	//private ComboBox cbxZone;
	//private ComboBox cmbBatch;
	private ComboBox cmbProduct;
	private TextField batchNo;
	private DateField fromDate;
	private DateField toDate;
	private HorizontalLayout fieldLayout;
	private HorizontalLayout layoutForType;
	
	private BeanItemContainer<SelectValue> nonKeralaCpuCode;
	
	private BeanItemContainer<SelectValue> cpuCode;
	
	private BeanItemContainer<SelectValue> type;	
	private BeanItemContainer<SelectValue> claimant;
	private BeanItemContainer<SelectValue> claimType;
	private BeanItemContainer<SelectValue> paymentMode;	
	private BeanItemContainer<SelectValue> batchType;
	private BeanItemContainer<SpecialSelectValue> product;
	BeanItemContainer<SelectValue> zoneType;
	
	//private Button searchPendingBtn;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Update Payment Detail");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout mainVerticalLayout(){
		
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		
		mainVerticalLayout = new VerticalLayout();	
		
		fieldLayout = new HorizontalLayout();
		
		//cmbBatch = binder.buildAndBind("Batch","batchType",ComboBox.class);
		//FormLayout formLayout = new FormLayout(cmbBatch);
		//formLayout.setSpacing(true);
		//formLayout.setMargin(false);
		
		//searchPendingBtn = new Button("Pending Cases");
		
		/*searchPendingBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {

				//TODO Procedure Call  For pending cases   to be fired
				fireViewEvent(UpdatePaymentDetailPresenter.SEARCH_PENDING_CASES_BUTTON_CLICK, null);
			}
		});*/
		
		//layoutForType = new HorizontalLayout(formLayout,searchPendingBtn);
		//layoutForType.setComponentAlignment(searchPendingBtn, Alignment.TOP_CENTER);
		//layoutForType.setHeight("15px");
		//layoutForType.setWidth("100%");
		//layoutForType.setMargin(false);
		buildCreateBatchLayout();
		
		addListenerForCmb();
		 
		HorizontalLayout buttonlayout = new HorizontalLayout(btnSearch,btnReset);
		buttonlayout.setWidth("70%");
		//buttonlayout.setHeight("100px");
		buttonlayout.setComponentAlignment(btnSearch,  Alignment.MIDDLE_RIGHT);
		//buttonlayout.setComponentAlignment(btnReset,  Alignment.MIDDLE_RIGHT);
		buttonlayout.setSpacing(true);
		//buttonlayout.setHeight("5px");
		
		
		//mainVerticalLayout.addComponent(layoutForType);
		
		mainVerticalLayout.addComponent(fieldLayout);
	//	fieldLayout.setHeight("10px");
	//	mainVerticalLayout.addComponent(fieldLayout1);
//		mainVerticalLayout.setComponentAlignment(fieldLayout, Alignment.BOTTOM_LEFT);
		mainVerticalLayout.addComponent(buttonlayout);
		mainVerticalLayout.setComponentAlignment(buttonlayout, Alignment.MIDDLE_LEFT);	
//		mainVerticalLayout.setWidth("100%");
		mainVerticalLayout.setHeight("220px");	
		mainVerticalLayout.setMargin(false);
		mainVerticalLayout.setSpacing(false);
		
		return mainVerticalLayout;
	}
	
	public void buildCreatePendingBatchLayout(List<PendingLotBatchReportDto> pendingList){

		pendingCasesTable.init("", false, false);
		
		if(pendingList != null && !pendingList.isEmpty()){
			pendingCasesTable.setTableList(pendingList);
		}

		Button expBtn = new Button("Export To Excel");
		expBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		expBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				excelExport = new  ExcelExport(pendingCasesTable.getTable());
				excelExport.excludeCollapsedColumns();
				excelExport.setReportTitle("PENDING CASES");
				excelExport.setDisplayTotals(false);
				excelExport.export();
			}
		});
		
		//POP UP To Show The above Table and Export Botton On top of table, Ok Button to Close the POP Up
		Window popUp = new Window("");
		
		popUp.setModal(true);
		popUp.setResizable(false);
		popUp.setWidth("85%");
		popUp.setWindowMode(WindowMode.NORMAL);
		popUp.center();
		final Button okBtn = new Button("OK");
		okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		okBtn.setData(popUp);
		okBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Window popupWindow = (Window)okBtn.getData();
				popupWindow.close();				
			}
		});
		VerticalLayout pendingLayout = new VerticalLayout();
		HorizontalLayout hlayout = new HorizontalLayout();
		hlayout.setWidth("95%");
		hlayout.addComponents(new Label(""),expBtn);
		hlayout.setComponentAlignment(expBtn, Alignment.MIDDLE_CENTER);
		pendingLayout.addComponents(hlayout,pendingCasesTable,okBtn);
		pendingLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
		
		Panel pendingPanel = new Panel("Pending Cases");
		pendingPanel.setContent(pendingLayout);
		popUp.setContent(pendingPanel);
		
		UI.getCurrent().addWindow(popUp);
	}
	
	public void buildCreateBatchLayout()
	{
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		//txtIntimationNo.setMaxLength(15);
		
		CSValidator intimationNovalidator = new CSValidator();
		intimationNovalidator.extend(txtIntimationNo);
		intimationNovalidator.setRegExp("^[a-z A-Z 0-9 /.]*$");
		intimationNovalidator.setPreventInvalidTyping(true);
		
		/*txtClaimNo = binder.buildAndBind("Claim No","claimNo",TextField.class);
		txtClaimNo.setMaxLength(25);
		CSValidator claimNovalidator = new CSValidator();
		claimNovalidator.extend(txtClaimNo);
		claimNovalidator.setRegExp("^[a-z A-Z 0-9 /.]*$");
		claimNovalidator.setPreventInvalidTyping(true);*/
		
		/*txtLOTNo = binder.buildAndBind("LOT NO","lotNo",TextField.class);
		txtLOTNo.setMaxLength(25);
		CSValidator lotNovalidator = new CSValidator();
		lotNovalidator.extend(txtLOTNo);
		lotNovalidator.setRegExp("^[a-z A-Z 0-9 /.]*$");
		lotNovalidator.setPreventInvalidTyping(true);*/
		
		/*txtRODNo = binder.buildAndBind("ROD No","rodNO",TextField.class);
		txtRODNo.setMaxLength(30);
		CSValidator rodNovalidator = new CSValidator();
		rodNovalidator.extend(txtRODNo);
		rodNovalidator.setRegExp("^[a-z A-Z 0-9 /.]*$");
		rodNovalidator.setPreventInvalidTyping(true);*/
		
		
		cbxType = binder.buildAndBind("Type","type",ComboBox.class);
		cbxCPUCode = binder.buildAndBind("CPU Code","cpuCode",ComboBox.class);
		cbxCPUCode.setPageLength(21);
		cbxClaimType = binder.buildAndBind("Claim Type","cliamType",ComboBox.class);
		//cbxClaimant = binder.buildAndBind("Claimant","claimant",ComboBox.class);
		//cbxPaymentMode = binder.buildAndBind("Payment Mode","paymentMode",ComboBox.class);
		cmbProduct = binder.buildAndBind("Product Name/Code","product",ComboBox.class);
		
		//Vaadin8-setImmediate() cbxType.setImmediate(true);
		txtDummyField = new TextField();	
		txtDummyField.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		txtDummyField.setEnabled(false);
		
		//cbxZone =binder.buildAndBind("Zone","zone",ComboBox.class);
		//cmbBatch = binder.buildAndBind("Batch","batchType",ComboBox.class);
		/*cbxZone.addItem("KERELA-ZONE");
		cbxZone.addItem("NON-KERELA-ZONE");*/
		
		fromDate = binder.buildAndBind("From Date","fromDate",DateField.class);	
		toDate = binder.buildAndBind("To Date","toDate",DateField.class);
		//cbxClaimType = binder.buildAndBind("Claim Type","cliamType",ComboBox.class);
		//cmbProduct = binder.buildAndBind("Product Name/Code","product",ComboBox.class);
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo,fromDate,cbxType);
		formLayoutLeft.setSpacing(true);
		formLayoutLeft.setMargin(false);
		FormLayout formLayoutRight = new FormLayout(cbxClaimType,toDate);
		formLayoutRight.setSpacing(true);
		formLayoutRight.setMargin(false);	
		FormLayout formLayout3 = new FormLayout(cbxCPUCode,cmbProduct);
		formLayout3.setSpacing(true);
		formLayout3.setMargin(false);	
		//FormLayout formLayout4 = new FormLayout(cbxZone,cmbProduct);
		//formLayout4.setSpacing(true);
		//formLayout4.setMargin(false);
		
		fieldLayout.addComponent(formLayoutLeft);
		fieldLayout.addComponent(formLayoutRight);
		fieldLayout.addComponent(formLayout3);
		//fieldLayout.addComponent(formLayout4);
		fieldLayout.setSpacing(true);
		fieldLayout.setMargin(false);
		addListener();		

		//fieldLayout.setMargin(true);
		//fieldLayout.setHeight("10px");
	}
	
	/*public void buildSearchBatchLayout()
	{
		batchNo = binder.buildAndBind("Batch No","batchNo",TextField.class);
		CSValidator batchNovalidator = new CSValidator();
		batchNovalidator.extend(batchNo);
		batchNovalidator.setRegExp("^[a-z A-Z 0-9 /. -]*$");
		batchNovalidator.setPreventInvalidTyping(true);
	//	cbxClaimType = binder.buildAndBind("Claim Type","cliamType",ComboBox.class);
		
		fromDate = binder.buildAndBind("From Date","fromDate",DateField.class);	
		toDate = binder.buildAndBind("To Date","toDate",DateField.class);
		cbxClaimType = binder.buildAndBind("Claim Type","cliamType",ComboBox.class);
		cmbProduct = binder.buildAndBind("Product Name/Code","product",ComboBox.class);
		
		FormLayout formLayout1 = new FormLayout(batchNo,cbxClaimType);
		formLayout1.setSpacing(true);
		formLayout1.setMargin(false);
		FormLayout formLayout2 = new FormLayout(fromDate,toDate);
		formLayout2.setSpacing(true);
		formLayout2.setMargin(false);
		FormLayout formLayout3 = new FormLayout(cmbProduct);
		formLayout3.setSpacing(true);
		formLayout3.setMargin(false);
		
		//fieldLayout = new HorizontalLayout(formLayoutLeft1,formLayoutRight1);
		fieldLayout.addComponent(formLayout1);
		fieldLayout.addComponent(formLayout2);
		fieldLayout.addComponent(formLayout3);
		fieldLayout.setSpacing(true);
		fieldLayout.setMargin(false);
		addListener();
		addListenerForBatchNo();
		//fieldLayout1.setVisible(false);
		
	}*/
	
	
	 public void addListenerForCmb()
	  {/*
		 cmbBatch.addValueChangeListener(new ValueChangeListener() {
				
				
				@Override
				public void valueChange(Property.ValueChangeEvent event) {

					
					SelectValue selValue = (SelectValue)cmbBatch.getValue();
					if(null != selValue && null != selValue.getValue())
					{
						
						if (fieldLayout != null
								&& fieldLayout.getComponentCount() > 0) {
							fieldLayout.removeAllComponents();
						}
						
						
						
						if(null != selValue && null != selValue.getValue() && (SHAConstants.CREATE_BATCH_TYPE).equalsIgnoreCase(selValue.getValue())){
							
							unbindFields();							
							buildCreateBatchLayout();							
							resetFlds();
							
							fireViewEvent(UpdatePaymentDetailPresenter.REPAINT_TABLE,selValue.getValue(), null);	
						
							
							
						}else if(null != selValue && null != selValue.getValue() && (SHAConstants.SEARCH_BATCH_TYPE).equalsIgnoreCase(selValue.getValue())){
							
							
							unbindFields();							
							buildSearchBatchLayout();					
							resetFlds();

							fireViewEvent(UpdatePaymentDetailPresenter.REPAINT_TABLE,selValue.getValue(), null);						
					
						}
						setDropDownValues();
						
						
					}
					
				}
			});
	  */}
	
	 public void addListenerForBatchNo()
	  {
		 batchNo.addValueChangeListener(new ValueChangeListener() {
				
				
				@Override
				public void valueChange(Property.ValueChangeEvent event) {	
				
					String value = (String)event.getProperty().getValue();
					if(null != value && !(value.equals("")))
					{
						fromDate.setEnabled(false);
						toDate.setEnabled(false);
						cbxClaimType.setEnabled(false);
					}
					else
					{
						fromDate.setEnabled(true);
						toDate.setEnabled(true);
						cbxClaimType.setEnabled(true);
					}
					
				}
			});
	  }
	
	
	  protected void addListener(){
			btnSearch.removeClickListener(this);
			btnReset.removeClickListener(this);
			btnSearch.addClickListener(this);
	    	btnReset.addClickListener(this);
	    	
	    	cbxType
			.addValueChangeListener(new Property.ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					SelectValue value = (SelectValue) event.getProperty().getValue();
					if(null != value)
					{/*	
						String layoutType = value.getValue();
						fireViewEvent(UpdatePaymentDetailPresenter.BUILD_LAYOUT_BASED_ON_TYPE, layoutType, SHAConstants.CREATE_BATCH_TYPE);
					*/}

				}
			});
	    	
	    	/*cbxZone
			.addValueChangeListener(new Property.ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					//if(null != cbxZone && cbxZone.getValue().equa(SHAConstants.NON_KERALA_ZONE)){
					if(null != cbxZone)
					{
						SelectValue selValue = (SelectValue)cbxZone.getValue();
						if(null != selValue && null != selValue.getValue() && (SHAConstants.NON_KERALA_ZONE).equalsIgnoreCase(selValue.getValue()))
						{
						cbxCPUCode.setContainerDataSource(nonKeralaCpuCode);
						cbxCPUCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
						cbxCPUCode.setItemCaptionPropertyId("value");
						}
						else {
							List<SelectValue> itemIds = cpuCode.getItemIds();
							for (SelectValue selectValue : itemIds) {
								if(selectValue.getValue().equalsIgnoreCase("950004 - Kerala")){
									BeanItemContainer<SelectValue> cpuCode = new BeanItemContainer<SelectValue>(SelectValue.class);
									List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
									selectValuesList.add(selectValue);
									cpuCode.addAll(selectValuesList);
									cbxCPUCode.setContainerDataSource(cpuCode);
		    					}
							}
						}
					}
					 
				}
			});*/
	    	
		}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<UpdatePaymentDetailFormDTO>(UpdatePaymentDetailFormDTO.class);
		this.binder.setItemDataSource(new UpdatePaymentDetailFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public void setDropDownValues(BeanItemContainer<SelectValue> type,
			BeanItemContainer<SelectValue> cpuCode,
			BeanItemContainer<SelectValue> claimant,
			BeanItemContainer<SelectValue> claimType,
			BeanItemContainer<SelectValue> paymentMode,
			BeanItemContainer<SelectValue> nonKeralaCpuCode,
			BeanItemContainer<SelectValue> batchType,
			BeanItemContainer<SelectValue> zoneType,
			BeanItemContainer<SpecialSelectValue> product){
		
		    this.type = type;
		    this.claimant = claimant;
		    this.claimType = claimType;
		    this.paymentMode = paymentMode;
		    this.batchType = batchType;
			this.nonKeralaCpuCode = nonKeralaCpuCode;
			this.cpuCode=cpuCode;
			this.product = product;
			
			
		zoneType = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		List<SelectValue> selValueList = new ArrayList<SelectValue>();
		SelectValue sel1 = new SelectValue();
		sel1.setId(1l);
		sel1.setValue(SHAConstants.KERALA_ZONE);
		selValueList.add(sel1);

		SelectValue sel2 = new SelectValue();
		sel2.setId(2l);
		sel2.setValue(SHAConstants.NON_KERALA_ZONE);
		selValueList.add(sel2);
		
		zoneType.addAll(selValueList);		
		this.zoneType = zoneType;
		
		
		 SelectValue selectCreateBatch = new SelectValue();
		    selectCreateBatch.setId(3l);
		    selectCreateBatch.setValue(SHAConstants.CREATE_BATCH_TYPE);

			SelectValue selectSearchBatch = new SelectValue();

			selectSearchBatch.setId(4l);
			selectSearchBatch.setValue(SHAConstants.SEARCH_BATCH_TYPE);
			
			List<SelectValue> selectVallueList = new ArrayList<SelectValue>();
			selectVallueList.add(selectCreateBatch);
			selectVallueList.add(selectSearchBatch);
			BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			selectValueContainer.addAll(selectVallueList);
			
		    //cmbBatch.setContainerDataSource(selectValueContainer);
		    //cmbBatch.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		    //cmbBatch.setItemCaptionPropertyId("value");
		    
		setDropDownValues();
	}
	   public void setDropDownValues()
	   {
		
		
		cbxType.setContainerDataSource(type);
		cbxType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxType.setItemCaptionPropertyId("value");
		

		//cbxZone.setContainerDataSource(zoneType);
		//cbxZone.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		//cbxZone.setItemCaptionPropertyId("value");
		
		
		cbxCPUCode.setContainerDataSource(cpuCode);
		cbxCPUCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxCPUCode.setItemCaptionPropertyId("value");	     
	   
	   /* cbxClaimant.setContainerDataSource(claimant);
	    cbxClaimant.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	    cbxClaimant.setItemCaptionPropertyId("value");*/
		
	    cbxClaimType.setContainerDataSource(claimType);
	    cbxClaimType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	    cbxClaimType.setItemCaptionPropertyId("value");
	    
	    /*cbxPaymentMode.setContainerDataSource(paymentMode);
	    cbxPaymentMode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	    cbxPaymentMode.setItemCaptionPropertyId("value");*/
	    
	    cmbProduct.setContainerDataSource(product);
	    cmbProduct.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	    cmbProduct.setItemCaptionPropertyId("specialValue");
	    
	   
	}	
	
	
	public String validate()
	{
		String err = null;
		Boolean isError = Boolean.TRUE;
		if(null != txtIntimationNo && null != txtIntimationNo.getValue()  && txtIntimationNo.getValue().trim().length()>0)
		{
			isError = Boolean.FALSE;
		}
		if(null != cmbProduct && null != cmbProduct.getValue() )
		{
			isError = Boolean.FALSE;
		}
		if(null != cbxClaimType && null != cbxClaimType.getValue() )
		{
			isError = Boolean.FALSE;
		}
		if(null != cbxCPUCode && null != cbxCPUCode.getValue() )
		{
			isError = Boolean.FALSE;
		}
		if(null != fromDate && null != fromDate.getValue() )
		{
			isError = Boolean.FALSE;
		}
		if(null != toDate && null != toDate.getValue() )
		{
			isError = Boolean.FALSE;
		}
		if(null != cbxType && null != cbxType.getValue() )
		{
			isError = Boolean.FALSE;
		}
		if(isError){
			err = "Please enter either of one intimation no, from date, to date";
		}
		
		/*if(null != cbxType && null == cbxType.getValue())
		{
			if((null != cmbBatch.getValue()) && ((cmbBatch.getValue().toString()).equalsIgnoreCase(SHAConstants.CREATE_BATCH_TYPE)))
			{			
			err = "Please select any one value from type dropdown for batch creation";
			}
		}
		
		if(null != cmbBatch && null == cmbBatch.getValue() )
		{
			err = "Please select any one value from batch dropdown";
		}
		if((null != cmbBatch && null != cmbBatch.getValue()) && ("").equals(cmbBatch.getValue()))
		{
			err = "Please select any one value from batch dropdown";
		}
		
		if(null != cbxZone && null == cbxZone.getValue()  )
		{
			if((null != cmbBatch.getValue()) && ((cmbBatch.getValue().toString()).equalsIgnoreCase(SHAConstants.CREATE_BATCH_TYPE)))
			{
			return err = "Select zone to proceed";
			}
		}
		
		if((null!= cbxClaimType && null != cbxClaimType.getValue()) && ((null != fromDate && null== fromDate.getValue()) && (null != toDate && null == toDate.getValue()))
				&& ((cmbBatch.getValue().toString()).equalsIgnoreCase(SHAConstants.SEARCH_BATCH_TYPE)))
		{
				
			err = "Please select From Date and To Date";
			
		}
		if(fromDate.getValue()!=null && toDate.getValue()!=null)
		{
		 if(toDate.getValue().before(fromDate.getValue()))
		 {
			return err= "Enter Valid To Date";
		}
		}*/
		return err;
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
				field.setValue(null);
			}
		}
	}
}

private List<Field<?>> getListOfFeilds()
{
		List<Field<?>>  fieldList = new ArrayList<Field<?>>();
				
		
		fieldList.add(cbxPaymentMode);
		fieldList.add(cbxCPUCode);
		fieldList.add(cbxClaimType);
		//fieldList.add(cbxZone); 
		fieldList.add(cbxClaimant);
		fieldList.add(cmbProduct);
		if(null != txtLOTNo)
			fieldList.add(txtLOTNo);
		if(null != txtIntimationNo)
			fieldList.add(txtIntimationNo);
		if(null != txtRODNo )
			fieldList.add(txtRODNo);
		if(null != txtClaimNo )
			fieldList.add(txtClaimNo);
		if(null != batchNo)
			fieldList.add(batchNo);
		if(null != fromDate)
			fieldList.add(fromDate);
		if(null != toDate)
			fieldList.add(toDate);
		if(null != cbxType)
			fieldList.add(cbxType);
		return fieldList;
}

	


}