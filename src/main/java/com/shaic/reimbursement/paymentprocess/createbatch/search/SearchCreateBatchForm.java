 /**
 * 
 */
package com.shaic.reimbursement.paymentprocess.createbatch.search;

import java.util.ArrayList;
import java.util.Collection;
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
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotFormDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.narenj
 *
 */
public class SearchCreateBatchForm extends SearchComponent<SearchCreateBatchFormDTO> {
	
//	@Inject
//	private SearchCreateBatchListenerTable searchAcknowledgementDocumentReceiverTable;
	
	@Inject
	private PendingLotBatchCasesTable pendingCasesTable;
	
	@EJB
	private PreauthService preauthService;
	
	ExcelExport excelExport;
	
	private ComboBox cbxType;
	
	private ComboBox cbxPaymentMode;
	//private ComboBox cbxCPUCode;
	private ComboBoxMultiselect cmbCpuCodeMulti;
	private ComboBox cbxClaimType;
	private ComboBox cbxVerificationType;
	private ComboBox cbxClaimant;
	
	private ComboBox cbxPaymentCpuCode;
	private ComboBoxMultiselect cmbPaymentCpuCodemulti;
	
	private TextField txtIntimationNo;
	private TextField txtLOTNo;
	private TextField txtRODNo;
	private TextField txtClaimNo;	
	private TextField txtDummyField;
	private ComboBox cbxZone;
	private ComboBox cmbBatch;
	private ComboBox cmbProduct;
	private TextField batchNo;
	private DateField fromDate;
	private DateField toDate;
	private ComboBox cbxPenalIntDays;
	private HorizontalLayout fieldLayout;
	private HorizontalLayout layoutForType;
	
	private BeanItemContainer<SelectValue> nonKeralaCpuCode;
	
	private BeanItemContainer<SelectValue> cpuCode;
	
	private BeanItemContainer<SelectValue> PaymentCpuCode;
	
	private BeanItemContainer<SelectValue> type;	
	private BeanItemContainer<SelectValue> claimant;
	private BeanItemContainer<SelectValue> claimType;
	private BeanItemContainer<SelectValue> paymentMode;	
	private BeanItemContainer<SelectValue> batchType;
	private BeanItemContainer<SpecialSelectValue> product;
	private BeanItemContainer<SelectValue> penalDueDays;
	BeanItemContainer<SelectValue> zoneType;
	private BeanItemContainer<SelectValue> verificationType;
	
	private Button searchPendingBtn;
	private SearchCreateBatchViewImpl viewImpl;
	private TextField txtQuickIntimationNo;
	private TabSheet searchTab = null;
	private TabSheet normalSearchTab = null;
	
	private TabSheet  quickSearchTab;
	
	private String menustring;
	
	Panel mainPanel;
	
	private ComboBoxMultiselect priority;

	private CheckBox chkAll;

	private CheckBox chkCRM;

	private CheckBox chkVIP;

	private BeanItemContainer<SelectValue> selectValueContainer;

	public SearchCreateBatchViewImpl getViewImpl() {
		return viewImpl;
	}

	public void setViewImpl(SearchCreateBatchViewImpl viewImpl) {
		this.viewImpl = viewImpl;
	}
	
	@Inject
	MasterService masterService;

//	@PostConstruct
	public void init(String menuString) {
		
		this.menustring = menuString;
		initBinder();
		
		mainPanel = new Panel();
		if(menustring != null &&  menustring.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1)) {
			mainPanel.setCaption("Payment Verification Level I");
		}else if(menustring != null &&  menustring.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2)) {
			mainPanel.setCaption("Payment Verification Level II");
		}else if(menustring != null &&  menustring.equalsIgnoreCase(SHAConstants.CREATE_BATCH)){
		mainPanel.setCaption("Create Batch");
		}
		
		
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		
		searchTab = new TabSheet();
		//Vaadin8-setImmediate() searchTab.setImmediate(true);
		searchTab.setSizeFull();
		searchTab.setStyleName(ValoTheme.TABSHEET_FRAMED);
		
//		normalSearchTab = mainVerticalLayout();

		normalSearchTab = new TabSheet();
		normalSearchTab.hideTabs(true);
		//Vaadin8-setImmediate() createLotTab.setImmediate(true);
		normalSearchTab.setWidth("100%");
		normalSearchTab.setHeight("100%");
		normalSearchTab.setSizeFull();
		normalSearchTab.setCaption(SHAConstants.NORMAL_SEARCH);
		
		mainVerticalLayout = new VerticalLayout();
		mainVerticalLayout = mainVerticalLayout();
		
		normalSearchTab.addComponent(mainVerticalLayout);		
		normalSearchTab.setCaption(SHAConstants.NORMAL_SEARCH);
		searchTab.addTab(normalSearchTab,SHAConstants.NORMAL_SEARCH, null);
		searchTab.setHeight("100.0%");
	
		quickSearchTab = new TabSheet();
		quickSearchTab.hideTabs(true);
		//Vaadin8-setImmediate() createLotTab.setImmediate(true);
		quickSearchTab.setWidth("100%");
		quickSearchTab.setHeight("100%");
		quickSearchTab.setSizeFull();		
		quickSearchTab.setCaption(SHAConstants.QUICK_SEARCH);


//		quickSearchTab = buildQuickSearchLayout();
		
		quickVerticallayout = new VerticalLayout();
//		quickVerticallayout = buildQuickSearchLayout();
		quickSearchTab.addComponent(quickVerticallayout);		
		searchTab.addTab(quickSearchTab,SHAConstants.QUICK_SEARCH, null);
		
		mainPanel.setContent(searchTab);
		tabListener(searchTab);
		setCompositionRoot(mainPanel);
	}
	
//	public TabSheet mainVerticalLayout(){
	public VerticalLayout mainVerticalLayout(){
		
		/*TabSheet createLotTab = new TabSheet();
		createLotTab.hideTabs(true);
		//Vaadin8-setImmediate() createLotTab.setImmediate(true);
		createLotTab.setWidth("100%");
		createLotTab.setHeight("100%");
		createLotTab.setSizeFull();
		//Vaadin8-setImmediate() createLotTab.setImmediate(true);
		mainVerticalLayout = new VerticalLayout();*/	
		
		mainVerticalLayout.removeAllComponents();
		unbindFields();
		
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		
		
		fieldLayout = new HorizontalLayout();
		
		if(cmbBatch != null && binder.getField("batchType") != null) {
			binder.unbind(cmbBatch);
		}	
		
		cmbBatch = binder.buildAndBind("Batch","batchType",ComboBox.class);
		
		SelectValue selectCreateBatch = new SelectValue();
	    selectCreateBatch.setId(3l);
	    selectCreateBatch.setValue(SHAConstants.CREATE_BATCH_TYPE);

		SelectValue selectSearchBatch = new SelectValue();
		selectSearchBatch.setId(4l);
		selectSearchBatch.setValue(SHAConstants.SEARCH_BATCH_TYPE);
		
		List<SelectValue> selectVallueList = new ArrayList<SelectValue>();
		selectVallueList.add(selectCreateBatch);
		selectVallueList.add(selectSearchBatch);
		selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		selectValueContainer.addAll(selectVallueList);
		
		cmbBatch.setContainerDataSource(selectValueContainer);
	    cmbBatch.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	    cmbBatch.setItemCaptionPropertyId("value");
	    
		FormLayout formLayout = new FormLayout(cmbBatch);
		formLayout.setHeight("15%");
		formLayout.setSpacing(false);
		formLayout.setMargin(false);
		
		searchPendingBtn = new Button("Pending Cases");
		searchPendingBtn.setHeight("");
		
		searchPendingBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {

				//TODO Procedure Call  For pending cases   to be fired
				fireViewEvent(SearchCreateBatchPresenter.SEARCH_PENDING_CASES_BUTTON_CLICK, null);
			}
		});
		
		layoutForType = new HorizontalLayout(formLayout,searchPendingBtn);
		layoutForType.setComponentAlignment(searchPendingBtn, Alignment.TOP_CENTER);
		layoutForType.setHeight("90%");
		layoutForType.setWidth("100%");
		layoutForType.setMargin(false);
		layoutForType.setSpacing(false);
		buildCreateBatchLayout();
		
		addListenerForCmb();
		
		setDropDownValues();
		 
		AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		//Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		absoluteLayout_3.setWidth("100.0%");
		absoluteLayout_3.setHeight("200px");
		
		VerticalLayout vlayout = new VerticalLayout();
		vlayout.addComponents(layoutForType,fieldLayout);

		
		absoluteLayout_3.addComponents(vlayout);
		
		absoluteLayout_3.addComponent(btnSearch, "top:165.0px;left:280.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:165.0px;left:359.0px;");
		mainVerticalLayout.addComponent(absoluteLayout_3);
		mainVerticalLayout.setMargin(false);
		mainVerticalLayout.setSpacing(false);
		
		/*createLotTab.addComponent(mainVerticalLayout);
		
		return createLotTab;*/
		
		if(menustring != null &&(menustring.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) || menustring.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2))){
	    	
			cmbBatch.setValue(selectValueContainer.getIdByIndex(0));					
	    	cmbBatch.setVisible(false);
	    }
		
		if(menustring != null &&  menustring.equalsIgnoreCase(SHAConstants.CREATE_BATCH)){
			cmbBatch.setVisible(true);
		}
		
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
		
		txtClaimNo = binder.buildAndBind("Claim No","claimNo",TextField.class);
		txtClaimNo.setMaxLength(25);
		CSValidator claimNovalidator = new CSValidator();
		claimNovalidator.extend(txtClaimNo);
		claimNovalidator.setRegExp("^[a-z A-Z 0-9 /.]*$");
		claimNovalidator.setPreventInvalidTyping(true);
		
		txtLOTNo = binder.buildAndBind("LOT NO","lotNo",TextField.class);
		txtLOTNo.setMaxLength(25);
		CSValidator lotNovalidator = new CSValidator();
		lotNovalidator.extend(txtLOTNo);
		lotNovalidator.setRegExp("^[a-z A-Z 0-9 /.]*$");
		lotNovalidator.setPreventInvalidTyping(true);
		
		txtRODNo = binder.buildAndBind("ROD No","rodNO",TextField.class);
		txtRODNo.setMaxLength(30);
		CSValidator rodNovalidator = new CSValidator();
		rodNovalidator.extend(txtRODNo);
		rodNovalidator.setRegExp("^[a-z A-Z 0-9 /.]*$");
		rodNovalidator.setPreventInvalidTyping(true);
		
		
		cbxType = binder.buildAndBind("Type","type",ComboBox.class);
//		cbxCPUCode = binder.buildAndBind("CPU Code","cpuCode",ComboBox.class);
//		cbxCPUCode.setPageLength(21);
		
		cmbCpuCodeMulti = new ComboBoxMultiselect("CPU Code");
		cmbCpuCodeMulti.setShowSelectedOnTop(true);
		cmbCpuCodeMulti.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				BeanItem<SearchCreateBatchFormDTO> dtoBeanObject = binder.getItemDataSource();
				SearchCreateBatchFormDTO dtoObject = dtoBeanObject.getBean();
				dtoObject.setCpuCodeMulti(null);
				dtoObject.setCpuCodeMulti(event.getProperty().getValue());
			}
		});
		if(menustring != null &&(menustring.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) || menustring.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2))){
			cbxPaymentCpuCode =  binder.buildAndBind("Payment CPU Code","payCpuCode",ComboBox.class);
		}
		
		cmbPaymentCpuCodemulti = new ComboBoxMultiselect("Payment CPU Code");
		cmbPaymentCpuCodemulti.setShowSelectedOnTop(true);
		cmbPaymentCpuCodemulti.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				BeanItem<SearchCreateBatchFormDTO> dtoBeanObject = binder.getItemDataSource();
				SearchCreateBatchFormDTO dtoObject = dtoBeanObject.getBean();
				dtoObject.setPaymentCpuCodeMulti(null);
				dtoObject.setPaymentCpuCodeMulti(event.getProperty().getValue());
			}
		});
		
		cbxClaimType = binder.buildAndBind("Claim Type","cliamType",ComboBox.class);
		cbxVerificationType = binder.buildAndBind("Account Verification Status","verificationType",ComboBox.class);

		cbxClaimant = binder.buildAndBind("Claimant","claimant",ComboBox.class);
		cbxPaymentMode = binder.buildAndBind("Payment Mode","paymentMode",ComboBox.class);
		cmbProduct = binder.buildAndBind("Product Name/Code","product",ComboBox.class);
		cbxPenalIntDays = binder.buildAndBind("Penal Interest Days","penalDueDays",ComboBox.class);
		//cbxPenalIntDays.setVisible(false);
		//Vaadin8-setImmediate() cbxType.setImmediate(true);
		txtDummyField = new TextField();	
		txtDummyField.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		txtDummyField.setEnabled(false);
		
		cbxZone =binder.buildAndBind("Zone","zone",ComboBox.class);
		

		  cbxClaimant.addValueChangeListener(new ValueChangeListener() {
				@Override
				public void valueChange(Property.ValueChangeEvent event) {

					SelectValue selValue = (SelectValue)cbxClaimant.getValue();
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
					}else{
						if(cbxVerificationType != null){
							cbxVerificationType.setEnabled(true);
						}
					}

				}
			});
		
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
		
		//cmbBatch = binder.buildAndBind("Batch","batchType",ComboBox.class);
		/*cbxZone.addItem("KERELA-ZONE");
		cbxZone.addItem("NON-KERELA-ZONE");*/
		  
		
		FormLayout formLayoutLeft = new FormLayout();
		FormLayout formLayoutRight = new FormLayout();
		FormLayout formLayout3 = new FormLayout();
		FormLayout formLayout4 = new FormLayout();
		
		if(menustring != null &&(menustring.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) || menustring.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2))){

			fromDate = binder.buildAndBind("Start Date","fromDate",DateField.class);	
			fromDate.setDateFormat("dd/MM/yyyy");
			toDate = binder.buildAndBind("End Date","toDate",DateField.class);
			toDate.setDateFormat("dd/MM/yyyy");
			
			formLayoutLeft.addComponent(fromDate);
			formLayoutRight.addComponent(toDate);
			formLayout3.addComponent(new Label());
		}
		
		formLayoutLeft.addComponents(cbxType,cbxZone,cbxPaymentMode,cmbCpuCodeMulti);
		formLayoutLeft.setSpacing(false);
		formLayoutLeft.setMargin(false);
		if(menustring != null &&(menustring.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) || menustring.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2))){
			formLayoutRight.addComponents(txtIntimationNo,cbxPaymentCpuCode,cbxClaimType,cbxPenalIntDays);
		}else{
			formLayoutRight.addComponents(txtIntimationNo,cmbPaymentCpuCodemulti,cbxClaimType,cbxPenalIntDays);
		}
		
		
		formLayoutRight.setSpacing(false);
		formLayoutRight.setMargin(false);	
		formLayout3.addComponents(cbxClaimant/*,txtLOTNo,cmbProduct,cbxVerificationType*/);
		
		if(menustring != null && (menustring.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) || menustring.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2))){
			formLayout3.addComponents(cmbProduct,cbxVerificationType);
		}
		else {
			formLayout3.addComponents(txtLOTNo,cmbProduct,cbxVerificationType);
		}
		
		formLayout3.setSpacing(false);
		formLayout3.setMargin(false);	
//		FormLayout formLayout4 = new FormLayout(cbxPaymentCpuCode);
//		formLayout4.setSpacing(true);
//		formLayout4.setMargin(false);
		
		formLayout4.addComponents(priority);
		formLayout4.setSpacing(false);
		formLayout4.setMargin(false);
		
		fieldLayout.addComponent(formLayoutLeft);
		fieldLayout.addComponent(formLayoutRight);
		fieldLayout.addComponent(formLayout3);
		fieldLayout.addComponent(formLayout4);
//		fieldLayout.addComponent(formLayout4);
		fieldLayout.setSpacing(true);
		fieldLayout.setMargin(false);
		
		setDropDownValues();
		
		addListener();		

		//fieldLayout.setMargin(true);
		//fieldLayout.setHeight("10px");
	}
	
	public void buildSearchBatchLayout()
	{
		batchNo = binder.buildAndBind("Batch No","batchNo",TextField.class);
		/*CSValidator batchNovalidator = new CSValidator();
		batchNovalidator.extend(batchNo);
		batchNovalidator.setRegExp("^[a-z A-Z 0-9 /. -]*$");
		batchNovalidator.setPreventInvalidTyping(true);*/
	//	cbxClaimType = binder.buildAndBind("Claim Type","cliamType",ComboBox.class);
		
		fromDate = binder.buildAndBind("From Date","fromDate",DateField.class);	
		toDate = binder.buildAndBind("To Date","toDate",DateField.class);
		cbxClaimType = binder.buildAndBind("Claim Type","cliamType",ComboBox.class);
		cmbProduct = binder.buildAndBind("Product Name/Code","product",ComboBox.class);
		
		FormLayout formLayout1 = null;
		if(menustring.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) || menustring.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2)){
			FormLayout formLayout2 = new FormLayout(fromDate,toDate);
			formLayout2.setSpacing(false);
			formLayout2.setMargin(false);
			FormLayout formLayout3 = new FormLayout(cmbProduct,cbxClaimType);
			formLayout3.setSpacing(false);
			formLayout3.setMargin(false);
			fieldLayout.addComponent(formLayout2);
			fieldLayout.addComponent(formLayout3);
		}else{
			formLayout1 = new FormLayout(batchNo,cbxClaimType);
		 	formLayout1.setSpacing(false);
			formLayout1.setMargin(false);
			FormLayout formLayout2 = new FormLayout(fromDate,toDate);
			formLayout2.setSpacing(false);
			formLayout2.setMargin(false);
			FormLayout formLayout3 = new FormLayout(cmbProduct);
			formLayout3.setSpacing(false);
			formLayout3.setMargin(false); 	
			fieldLayout.addComponent(formLayout1);
			fieldLayout.addComponent(formLayout2);
			fieldLayout.addComponent(formLayout3);
			 addListenerForBatchNo();                                                                                                                     
		}
		
		//fieldLayout = new HorizontalLayout(formLayoutLeft1,formLayoutRight1);
		
		fieldLayout.setSpacing(true);
		fieldLayout.setMargin(false);
		
		addListener();
		//fieldLayout1.setVisible(false);
		
	}
	
	
	 public void addListenerForCmb()
	  {
		 
		 if(menustring == null || (menustring != null &&  menustring.equalsIgnoreCase(SHAConstants.CREATE_BATCH))){
		 
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
							
							fireViewEvent(SearchCreateBatchPresenter.REPAINT_TABLE,selValue.getValue(), null);	
						
							
							
						}else if(null != selValue && null != selValue.getValue() && (SHAConstants.SEARCH_BATCH_TYPE).equalsIgnoreCase(selValue.getValue())){
							
							
							unbindFields();							
							buildSearchBatchLayout();					
							resetFlds();

							fireViewEvent(SearchCreateBatchPresenter.REPAINT_TABLE,selValue.getValue(), null);						
					
						}
						setDropDownValues();						
						
					}
					
				}
			});
		 }
	  }
	
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
	    	btnQuickSearchBatch.removeClickListener(this);
	    	btnQuickReset.removeClickListener(this);
	    	btnQuickSearchBatch.addClickListener(this);
	    	btnQuickReset.addClickListener(this);
	    	
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
	    	if(cbxType != null) {
	    		
	    	
		    	cbxType
				.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;
	
					@Override
					public void valueChange(ValueChangeEvent event) {
						SelectValue value = (SelectValue) event.getProperty().getValue();
						if(null != value)
						{	
							SelectValue selValue = (SelectValue)cmbBatch.getValue();
							if(null != selValue  && null != selValue.getValue() && !SHAConstants.SEARCH_BATCH_TYPE.equalsIgnoreCase(selValue.getValue())){
								
								String layoutType = value.getValue();
								fireViewEvent(SearchCreateBatchPresenter.BUILD_LAYOUT_BASED_ON_TYPE, layoutType, SHAConstants.CREATE_BATCH_TYPE);
							}
						}
	
					}
				});
	    	}	
	    	
	    	if(cbxZone != null) {
		    	cbxZone
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
								cmbCpuCodeMulti.setContainerDataSource(nonKeralaCpuCode);
								cmbCpuCodeMulti.setItemCaptionMode(ItemCaptionMode.PROPERTY);
								cmbCpuCodeMulti.setItemCaptionPropertyId("value");
							}
							else {													
								List<SelectValue> itemIds = cpuCode.getItemIds();
								for (SelectValue selectValue : itemIds) {
									if(selectValue.getValue().equalsIgnoreCase("950004 - Kerala")){
										BeanItemContainer<SelectValue> cpuCode = new BeanItemContainer<SelectValue>(SelectValue.class);
										List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
										selectValuesList.add(selectValue);
										cpuCode.addAll(selectValuesList);
										cmbCpuCodeMulti.setContainerDataSource(cpuCode);
			    					}
								}
							}
						}
						 
					}
				});
	    	}
	    	
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
							for (String selValue : docList) {

								if(selValue.equalsIgnoreCase("All"))
								{	
									chkAll.setValue(true);
								}
								if(selValue.equalsIgnoreCase("CRM Flagged"))
								{	
									chkCRM.setValue(true);
								}
								if(selValue.equalsIgnoreCase("VIP"))
								{	
									chkVIP.setValue(true);
								}
								if(selValue.equalsIgnoreCase("Corporate - High Priority"))
								{	
									chkAll.setValue(true);
								}

							}
						}

					}

				}
			});
		}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchCreateBatchFormDTO>(SearchCreateBatchFormDTO.class);
		this.binder.setItemDataSource(new SearchCreateBatchFormDTO());
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
			BeanItemContainer<SpecialSelectValue> product,
			BeanItemContainer<SelectValue> penalDueDays,
			BeanItemContainer<SelectValue> verificationType){
		
		    this.type = type;
		    this.claimant = claimant;
		    this.claimType = claimType;
		    this.paymentMode = paymentMode;
		    this.batchType = batchType;
			this.nonKeralaCpuCode = nonKeralaCpuCode;
			this.cpuCode=cpuCode;
			this.product = product;
			this.penalDueDays = penalDueDays;
			this.verificationType = verificationType;
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
//		String userZoneDetails = dbCalculationService.getUserZoneDetails(userName);
		zoneType = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		List<SelectValue> selValueList = new ArrayList<SelectValue>();
		//commented by noufel since making zone type as PAN india as default
//		if(userZoneDetails != null && userZoneDetails.equalsIgnoreCase(SHAConstants.KERALA_ZONE)){
//			SelectValue sel1 = new SelectValue();
//			sel1.setId(1l);
//			sel1.setValue(SHAConstants.KERALA_ZONE);
//			selValueList.add(sel1);
//		}else{
//			SelectValue sel2 = new SelectValue();
//			sel2.setId(2l);
//			sel2.setValue(SHAConstants.NON_KERALA_ZONE);
//			selValueList.add(sel2);
//		}
//  below code added to make pan india as default zone
		SelectValue sel1 = new SelectValue();
		sel1.setId(1l);
		sel1.setValue(SHAConstants.PAN_INDIA);
		selValueList.add(sel1);
		
		List<SelectValue> filterList = new ArrayList<SelectValue>();
		List<SelectValue> cpuCode1 = (List<SelectValue>) nonKeralaCpuCode.getItemIds();
		 if(cpuCode1 != null && !cpuCode1.isEmpty()) {
			 for (SelectValue selectValue : cpuCode1) {
				 
				 if(! ReferenceTable.getRemovableCpuCodeList().containsKey(selectValue.getId())){
					 filterList.add(selectValue);
				 }
			}
			 
		   }
		 nonKeralaCpuCode.removeAllItems();
		 nonKeralaCpuCode.addAll(filterList);
		
		zoneType.addAll(selValueList);		
		this.zoneType = zoneType;
		
		
		 /*SelectValue selectCreateBatch = new SelectValue();
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
			
		    cmbBatch.setContainerDataSource(selectValueContainer);
		    cmbBatch.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		    cmbBatch.setItemCaptionPropertyId("value");
		  */
		    
		setDropDownValues();
		searchTab.setSelectedTab(normalSearchTab);
		
	}
	
	
	   public void setDropDownValues()
	   {
		cbxType.setContainerDataSource(type);
		cbxType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxType.setItemCaptionPropertyId("value");
		Collection<SelectValue> itemIds = (Collection<SelectValue>) cbxType.getContainerDataSource().getItemIds();
		    if(itemIds != null && !itemIds.isEmpty()) {
		    	cbxType.setValue(itemIds.toArray()[1]);
		    	cbxType.setNullSelectionAllowed(false);
			}
		
		cbxZone.setContainerDataSource(zoneType);
		cbxZone.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxZone.setItemCaptionPropertyId("value");
		Collection<SelectValue> zoneItemIds = (Collection<SelectValue>) cbxZone.getContainerDataSource().getItemIds();
	    if(zoneItemIds != null && !zoneItemIds.isEmpty()) {
	    	cbxZone.setValue(zoneItemIds.toArray()[0]);
	    	cbxZone.setNullSelectionAllowed(false);
	    	cbxZone.setEnabled(false);
		}
	    
	    if(cbxZone != null &&(menustring != null &&(menustring.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) || menustring.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2)))){
	    	cbxZone.setEnabled(true);	
	    }
		
	/*	cbxCPUCode.setContainerDataSource(cpuCode);
		cbxCPUCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxCPUCode.setItemCaptionPropertyId("value");
		List<SelectValue> filterList = new ArrayList<SelectValue>();
		List<SelectValue> cpuCode1 = (List<SelectValue>) cpuCode.getItemIds();
		 if(cpuCode1 != null && !cpuCode1.isEmpty()) {
			 for (SelectValue selectValue : cpuCode1) {
				 
				 if(! ReferenceTable.getRemovableCpuCodeList().containsKey(selectValue.getId())){
					 filterList.add(selectValue);
				 }
			}
			 
		   }
		
		 	cbxCPUCode.removeAllItems();
		 
		    cpuCode.addAll(filterList);
		    cbxCPUCode.setContainerDataSource(cpuCode);
		    cbxCPUCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		    cbxCPUCode.setItemCaptionPropertyId("value");*/
		    
		cmbCpuCodeMulti.setContainerDataSource(cpuCode);
		cmbCpuCodeMulti.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCpuCodeMulti.setItemCaptionPropertyId("value");	
		
		List<SelectValue> filterList = new ArrayList<SelectValue>();
		if(cpuCode !=null){
			List<SelectValue> cpuCode1 = (List<SelectValue>) cpuCode.getItemIds();
			if(cpuCode1 != null && !cpuCode1.isEmpty()) {
				
				for (SelectValue selectValue : cpuCode1) {
					 
					 if(! ReferenceTable.getRemovableCpuCodeList().containsKey(selectValue.getId())){
						 filterList.add(selectValue);
					 }
				 }
				 
				cmbCpuCodeMulti.removeAllItems();
				cpuCode.addAll(filterList);		 
			  }
		}
		 
//		 cbxPaymentCpuCode
		
		BeanItemContainer<SelectValue> selectValueContainerForCPUCode = masterService.getTmpCpuCodeList();
		if(menustring != null &&(menustring.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) || menustring.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2))){
			cbxPaymentCpuCode.setContainerDataSource(selectValueContainerForCPUCode);
			cbxPaymentCpuCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cbxPaymentCpuCode.setItemCaptionPropertyId("value");
		}else{
//			BeanItemContainer<SelectValue> selectValueContainerForCPUCode = masterService.getTmpCpuCodeList();
			cmbPaymentCpuCodemulti.setContainerDataSource(selectValueContainerForCPUCode);
			cmbPaymentCpuCodemulti.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbPaymentCpuCodemulti.setItemCaptionPropertyId("value");
		}
		
		 
			
			
		cmbCpuCodeMulti.setContainerDataSource(cpuCode);
		cmbCpuCodeMulti.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCpuCodeMulti.setItemCaptionPropertyId("value");	
	   
	    cbxClaimant.setContainerDataSource(claimant);
	    cbxClaimant.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	    cbxClaimant.setItemCaptionPropertyId("value");
		
	    cbxClaimType.setContainerDataSource(claimType);
	    cbxClaimType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	    cbxClaimType.setItemCaptionPropertyId("value");
	    
	    cbxPaymentMode.setContainerDataSource(paymentMode);
	    cbxPaymentMode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	    cbxPaymentMode.setItemCaptionPropertyId("value");
	    
	    cmbProduct.setContainerDataSource(product);
	    cmbProduct.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	    cmbProduct.setItemCaptionPropertyId("specialValue");
	    
	    cbxPenalIntDays.setContainerDataSource(penalDueDays);
	    cbxPenalIntDays.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	    cbxPenalIntDays.setItemCaptionPropertyId("value");
	  //  cbxPenalIntDays.setNullSelectionAllowed(false);
	    
	    cbxVerificationType.setContainerDataSource(verificationType);
	    cbxVerificationType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	    cbxVerificationType.setItemCaptionPropertyId("value");
	  //  cbxPenalIntDays.setNullSelectionAllowed(false);
	    
	    
	}	
	
	
	public String validate(SearchCreateBatchFormDTO searchDTO)
	{
		
		if(menustring != null && (menustring.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) || menustring.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2))) {
			cmbBatch.setValue(selectValueContainer.getIdByIndex(0));
			searchDTO.setBatchType(new SelectValue(1l, SHAConstants.CREATE_BATCH_TYPE));
		}
		
		String err = null;
		
		if(null != searchDTO && null != searchDTO.getSearchTabType() && (SHAConstants.NORMAL_SEARCH.equalsIgnoreCase(searchDTO.getSearchTabType()))){
		
			if(menustring == null || (menustring != null &&  menustring.equalsIgnoreCase(SHAConstants.CREATE_BATCH))) {
				
				if(null != cbxType && null == cbxType.getValue())
				{
					if((null != cmbBatch.getValue()) && ((cmbBatch.getValue().toString()).equalsIgnoreCase(SHAConstants.CREATE_BATCH_TYPE)))
					{			
					err = "Please select any one value from Type dropdown";
					}
				}
				if(menustring != null &&  menustring.equalsIgnoreCase(SHAConstants.CREATE_BATCH)){
					if(null != cmbBatch && null == cmbBatch.getValue() )
					{
						err = "Please select any one value from batch dropdown";
					}
				}
				if((null != cmbBatch && null != cmbBatch.getValue()) && ("").equals(cmbBatch.getValue()))
				{
					err = "Please select any one value from batch dropdown";
				}
			}
		
			if(null != cbxZone && null == cbxZone.getValue()  )
			{
				if((null != cmbBatch.getValue()) && ((cmbBatch.getValue().toString()).equalsIgnoreCase(SHAConstants.CREATE_BATCH_TYPE)))
				{
				return err = "Select zone to proceed";
				}
			}
		
			if((null != cbxClaimant && null == cbxClaimant.getValue()))
			{
				if((null != cmbBatch.getValue()) && ((cmbBatch.getValue().toString()).equalsIgnoreCase(SHAConstants.CREATE_BATCH_TYPE)))
				{
				return err = "Please Select Claimant";
				}
			}
		
			if(null != cbxVerificationType && null == cbxVerificationType.getValue()  )
			{
				if((null != cmbBatch.getValue()) && ((cmbBatch.getValue().toString()).equalsIgnoreCase(SHAConstants.CREATE_BATCH_TYPE)))
				{
				return err = "Select Account Verification Status to proceed";
				}
			}
		
		
			if((null!= cbxClaimType && null != cbxClaimType.getValue()) && ((null != fromDate && null== fromDate.getValue()) && (null != toDate && null == toDate.getValue()))
				&& ((cmbBatch.getValue().toString()).equalsIgnoreCase(SHAConstants.SEARCH_BATCH_TYPE)))
			{
					
				err = "Please select From Date and To Date";
				
			}
		}
		else if(null != searchDTO && null != searchDTO.getSearchTabType() && (SHAConstants.QUICK_SEARCH.equalsIgnoreCase(searchDTO.getSearchTabType())))
		{
			if(txtQuickIntimationNo.getValue() == null || txtQuickIntimationNo.getValue().equalsIgnoreCase(""))
			{
				return err = "Please Enter Intimation No";
			}
		}
		
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
		//fieldList.add(cbxCPUCode);
		fieldList.add(cbxPaymentCpuCode);
		fieldList.add(cmbPaymentCpuCodemulti);
		fieldList.add(cmbCpuCodeMulti);
		fieldList.add(cbxClaimType);
		fieldList.add(cbxZone); 
		fieldList.add(cbxClaimant);
		fieldList.add(cmbProduct);
		fieldList.add(cbxPenalIntDays);
		fieldList.add(cbxVerificationType);
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
		if(null != cbxPenalIntDays)
			fieldList.add(cbxPenalIntDays);
		if(null != txtQuickIntimationNo)
			fieldList.add(txtQuickIntimationNo);
		
		return fieldList;
}

//private TabSheet buildQuickSearchLayout()
private VerticalLayout buildQuickSearchLayout()
{
	unbindFields();
	
	/*TabSheet createLotTab = new TabSheet();
	createLotTab.hideTabs(true);
	//Vaadin8-setImmediate() createLotTab.setImmediate(true);
	createLotTab.setWidth("100%");
	createLotTab.setHeight("100%");
	createLotTab.setSizeFull();
	//Vaadin8-setImmediate() createLotTab.setImmediate(true);
	
	quickVerticallayout = new VerticalLayout();*/

	quickVerticallayout.removeAllComponents();
	
	txtQuickIntimationNo = binder.buildAndBind("Intimation No","quickIntimationNo",TextField.class);
	CSValidator intimationNovalidator = new CSValidator();
	intimationNovalidator.extend(txtQuickIntimationNo);
	intimationNovalidator.setRegExp("^[a-z A-Z 0-9 /.]*$");
	intimationNovalidator.setPreventInvalidTyping(true);
	
	/*Button btnSearch = new Button(QUICK_SEARCH_TASK_CAPTION);
	btnSearch.addStyleName(ValoTheme.BUTTON_FRIENDLY);
	
	Button btnReset = new Button(QUICK_SEARCH_RESET_CAPTION);
	btnReset.addStyleName(ValoTheme.BUTTON_DANGER);*/
	
	HorizontalLayout buttonlayout = new HorizontalLayout(btnQuickSearchBatch,btnQuickReset);
	buttonlayout.setWidth("70%");
	buttonlayout.setComponentAlignment(btnQuickSearchBatch,  Alignment.MIDDLE_RIGHT);
	buttonlayout.setSpacing(true);
	
	FormLayout formLayout= new FormLayout(txtQuickIntimationNo);
	formLayout.setSpacing(true);
	formLayout.setMargin(false);
	
	HorizontalLayout hLayout = new HorizontalLayout();
	hLayout.addComponents(formLayout);
	hLayout.setMargin(false);
	
	quickVerticallayout.addComponents(hLayout,buttonlayout);
	quickVerticallayout.setComponentAlignment(buttonlayout, Alignment.MIDDLE_LEFT);
	quickVerticallayout.setSpacing(true);
	quickVerticallayout.setMargin(true);
	
	addListener();
	
	/*createLotTab.addComponent(quickVerticallayout);
	return createLotTab;*/
	
	return quickVerticallayout;
}


public void tabListener(TabSheet searchTab){
	
	searchTab.addSelectedTabChangeListener(new SelectedTabChangeListener() {
		
		@Override
		public void selectedTabChange(SelectedTabChangeEvent event) {
			if(event.getTabSheet().getSelectedTab().getCaption() != null) {
				if(SHAConstants.NORMAL_SEARCH.equalsIgnoreCase(event.getTabSheet().getSelectedTab().getCaption())) {
					refresh();
					viewImpl.setSplitPosition(SHAConstants.NORMAL_SEARCH);
					mainVerticalLayout = mainVerticalLayout();
				} else if(SHAConstants.QUICK_SEARCH.equalsIgnoreCase(event.getTabSheet().getSelectedTab().getCaption())) {
					refresh();
					viewImpl.setSplitPosition(SHAConstants.QUICK_SEARCH);
					quickVerticallayout = buildQuickSearchLayout();
					fireViewEvent(SearchCreateBatchPresenter.BUILD_LAYOUT_BASED_ON_TYPE, "", SHAConstants.CREATE_BATCH_TYPE);
				}
			}
			
		}
	});
}
	
public ComboBox getCbxType() {
	return cbxType;
}

public void setCbxType(ComboBox cbxType) {
	this.cbxType = cbxType;
}

public ComboBox getCmbBatch() {
	return cmbBatch;
}

public void setCmbBatch(ComboBox cmbBatch) {
	this.cmbBatch = cmbBatch;
}

public void resetQuickSearchLayout(){
	if(txtQuickIntimationNo != null)
	txtQuickIntimationNo.setValue(null);
}

public void resetIntimation(){
	if(txtIntimationNo != null)
		txtIntimationNo.setValue(null);
}

public void setMenuString(String menu) {
	if(menu != null && menu.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1)) {
		this.menustring = SHAConstants.PAYMENT_LVL1;
	}else if(menu != null && menu.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2)) {
		this.menustring = SHAConstants.PAYMENT_LVL2;
	}else if(menu != null && menu.equalsIgnoreCase(SHAConstants.CREATE_BATCH)) {
		this.menustring = SHAConstants.CREATE_BATCH;
	}
	if(menustring != null &&  menustring.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1)) {
		mainPanel.setCaption("Payment Verification Level I");
	}else if(menustring != null &&  menustring.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2)) {
		mainPanel.setCaption("Payment Verification Level II");
	}else if(menustring != null &&  menustring.equalsIgnoreCase(SHAConstants.CREATE_BATCH)){
	mainPanel.setCaption("Create Batch");
	}
	if(menustring != null &&(menustring.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) || menustring.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2))){
    	cmbBatch.setValue(((BeanItemContainer<SelectValue>)cmbBatch.getContainerDataSource()).getIdByIndex(0));
    	cmbBatch.setVisible(false);
    }
	if(menustring != null &&  menustring.equalsIgnoreCase(SHAConstants.CREATE_BATCH)){
		cmbBatch.setVisible(true);
	}
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
}