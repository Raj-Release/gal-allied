package com.shaic.claim.OMPTpaservicechargesmaker.search;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SearchComponent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Alignment;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.v7.ui.TextField;

import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class OMPTpaServiceChargesMakerUI extends SearchComponent<OMPTpaServiceChargesMakerFormDto>{
	
	
	private ComboBox cmbtypeTpa;
	private ComboBox cmbpaymentperiod;
	private TextField txtInvoiceAmount;
	private TextField txtInvoiceNumber;
	private TextField txtInvoiceDate;
	private TextField txtQuarterlyOMP;
	private TextField txtApplicableQuarterly;
	private TextField txtApplicableQuarterlyPremium;
	private TextField txtSuggestedAmount;
	private TextField txtConversionRate;
	private TextField txtSuggestedAmountINR ;
	private TextField txtRemarks;
	private TabSheet mainTabSheet;
	protected VerticalLayout invoiceEntryVerticalLayout;
	protected VerticalLayout uploadDocumentsVerticalLayout;
	private VerticalLayout vLayout;
	
	
	@Inject
	private OMPTpaServiceChargesMakerDetailTable searchTable;
	
	@Inject
	private OMPTpaServiceChargesMakerUploadDetailTable searchUploadTable;
	
	@PostConstruct
	public void init() {
		initBinder();
	}
	
	public void getContent(){
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("OMP TPA SERVICE CHARGES PAYMENT- MAKER");
		
		searchTable.init("", false, false);
		
//		generateLetterTab = new TabSheet();
		invoiceEntryVerticalLayout = buildInvoiceEntryLayout();
//		generateLetterTab = buildGenerateLetterLayout();
		
//		printLetterTab = new TabSheet();
		uploadDocumentsVerticalLayout = buildUploadDocumentLayout();
//		printLetterTab = buildPrintReminderLetterLayout();
		
		mainTabSheet = new TabSheet();
		mainTabSheet.addTab(invoiceEntryVerticalLayout);
		mainTabSheet.addTab(uploadDocumentsVerticalLayout);
		mainTabSheet.setSelectedTab(invoiceEntryVerticalLayout);
		
//		mainTabSheet.addTab(generateLetterTab,SHAConstants.GENERATE_REMINDER_TAB_BULK_REMINDER);
//		mainTabSheet.addTab(printLetterTab,SHAConstants.PRINT_TAB_BULK_REMINDER);
		 
		mainTabSheet.getTab(invoiceEntryVerticalLayout).setClosable(false);
		mainTabSheet.getTab(uploadDocumentsVerticalLayout).setClosable(false);
		
		mainTabSheet.setStyleName(ValoTheme.TABSHEET_FRAMED);
		mainTabSheet.setSizeFull();
		//Vaadin8-setImmediate() mainTabSheet.setImmediate(true);
		mainTabSheet.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {
			
			 Component selected = mainTabSheet.getSelectedTab();
	        public void selectedTabChange(SelectedTabChangeEvent event) {
	        	
	        		            
	        	TabSheet mainTab = (TabSheet)event.getTabSheet();
	        	
	        	String tabName = mainTab.getCaption();	        			
	        	
	        	Layout selectedTab = (Layout) mainTab.getSelectedTab(); 
	        	
	        	String tabSheet = selectedTab.getCaption();
	        	
  	if(tabSheet != null && ! tabSheet.isEmpty()){
	        		if((tabSheet.toLowerCase()).equalsIgnoreCase("Upload Documents")){
	        		//	if(batchIdTxt != null) {
	        		//		batchIdTxt.setValue("");	
	        		//	}	        			
	        	//	if(intimationTxt != null) {
	        	//			intimationTxt.setValue("");
	        	//		}*/
	        		fireViewEvent(OMPTpaServiceChargesMakerPresenter.SUBMIT_SEARCH,null);
//	        			repaintBatchTable(prevBatchList);
	     //   		}
	        	//else{
	        	//		resetFields();
	        		}
	        	}	      	
	        }
	    });
		
		mainPanel.setContent(mainTabSheet);
		setCompositionRoot(mainPanel);
	}

	public VerticalLayout buildInvoiceEntryLayout(){
//		public TabSheet buildGenerateLetterLayout(){
	//		btnSearch.setCaption("Generate");
	//		btnSearch.setDisableOnClick(true);
			invoiceEntryVerticalLayout = new VerticalLayout();
			invoiceEntryVerticalLayout.setCaption(" Invoice Entry  ");
			invoiceEntryVerticalLayout.setSizeFull();
			
//			txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
			cmbtypeTpa = binder.buildAndBind("Type of TPA","tpaType",ComboBox.class);
			//cmbtypeTpa.setEnabled(false);
			cmbpaymentperiod = binder.buildAndBind("Payment Period","paymentPeriod",ComboBox.class);
			txtInvoiceAmount = binder.buildAndBind("Invoice Amount($)","invoiceAmount",TextField.class);
			txtInvoiceAmount.setValue("1,4848");
			//txtInvoiceAmount.setEnabled(false);
			txtInvoiceAmount.setReadOnly(true);
			txtInvoiceNumber = binder.buildAndBind("Invoice Number","invoiceNumber",TextField.class);
			txtInvoiceNumber.setReadOnly(true);
			txtInvoiceDate = binder.buildAndBind("Invoice Date","invoiceDate",TextField.class);
			txtInvoiceDate.setReadOnly(true);
			txtQuarterlyOMP = binder.buildAndBind("Quarterly OMP Premium(INR)","quarterlyPremium",TextField.class);
			txtQuarterlyOMP.setReadOnly(true);
			txtApplicableQuarterly = binder.buildAndBind("Applicable Quarterly %","applicableQuarterly",TextField.class);
			txtApplicableQuarterly.setReadOnly(true);
			txtApplicableQuarterlyPremium = binder.buildAndBind("Applicable Quarterly Premium(INR)","applicableQpremium",TextField.class);
			txtApplicableQuarterlyPremium.setReadOnly(true);
			txtSuggestedAmount = binder.buildAndBind("Suggested Amount ($)","suggestedAmount",TextField.class);
			txtSuggestedAmount.setReadOnly(true);
			txtConversionRate = binder.buildAndBind("Conversion Rate","conversionRate",TextField.class);
			txtConversionRate.setReadOnly(true);
			txtSuggestedAmountINR = binder.buildAndBind("Suggested Amount (INR)","suggestedAmountInr",TextField.class);
			txtSuggestedAmountINR.setReadOnly(true);
			txtRemarks = binder.buildAndBind("Remarks","remarks",TextField.class);
			txtRemarks.setReadOnly(true);
			/*
			fromDate = new PopupDateField("From Date");
			String dateValue = "dd/MM/yyyy";
			fromDate.setDateFormat(dateValue);
//			FormLayout fromDtFrmLayout = new FormLayout();
//			fromDtFrmLayout.addComponent(fromDate);
			
			toDate = new PopupDateField("To Date");
			toDate.setDateFormat(dateValue);
//			FormLayout toDatefrmLayout = new FormLayout();
//			toDatefrmLayout.addComponent(toDate);
			
//			HorizontalLayout dateFromLayout = new HorizontalLayout();
//			dateFromLayout.addComponents(fromDtFrmLayout,toDatefrmLayout);
			
//			txtClaimNo = binder.buildAndBind("Claim No","claimNo",TextField.class);
 * */

			FormLayout formLayoutLeft = new FormLayout(cmbtypeTpa,cmbpaymentperiod,txtInvoiceAmount,txtInvoiceNumber,txtInvoiceDate,txtQuarterlyOMP);    
			
					
			FormLayout formLayoutReight = new FormLayout(txtApplicableQuarterly,txtApplicableQuarterlyPremium,txtSuggestedAmount,txtConversionRate,txtSuggestedAmountINR,txtRemarks);	
		
			HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutReight);
			fieldLayout.setSpacing(true);
		/*	
			clearButton = new Button("Cancel");
			
			HorizontalLayout buttonhLayout = new HorizontalLayout(btnSearch,clearButton);
			
			buttonhLayout.setSpacing(true);
			
			VerticalLayout btnLayout = new VerticalLayout();
			btnLayout.addComponent(buttonhLayout);
			btnLayout.setComponentAlignment(buttonhLayout,  Alignment.MIDDLE_CENTER);
			
			*/
//			AbsoluteLayout btnsAbsLayout = new AbsoluteLayout();
//			btnsAbsLayout.addComponent(buttonhLayout, "top:200.0px;left:200.0px;");
			
			invoiceEntryVerticalLayout.addComponent(fieldLayout);
			invoiceEntryVerticalLayout.setComponentAlignment(fieldLayout, Alignment.BOTTOM_LEFT);
//			generateLetterVerticalLayout.addComponent(dateFromLayout);
//			InvoiceEntryVerticalLayout.addComponent(btnLayout);
//			generateLetterVerticalLayout.setComponentAlignment(btnLayout, Alignment.MIDDLE_LEFT);
//			mainVerticalLayout.setWidth("100%");	
//			generateLetterVerticalLayout.setHeight("200px");
			invoiceEntryVerticalLayout.setMargin(true);
			invoiceEntryVerticalLayout.setSpacing(true);
			addListener();
			
			
		/*	
			clearButton.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					resetFields();
				}
			});*/
			
			vLayout = new VerticalLayout();
		invoiceEntryVerticalLayout.addComponent(vLayout);
			
			return invoiceEntryVerticalLayout;
			
//			generateLetterTab.addComponent(generateLetterVerticalLayout);
//			return generateLetterTab;
		}
	public VerticalLayout buildUploadDocumentLayout(){
//		public TabSheet buildPrintReminderLetterLayout(){
			
		uploadDocumentsVerticalLayout = new VerticalLayout();
		uploadDocumentsVerticalLayout.setCaption(SHAConstants.UPLOADED_DOCUMENTS);
			
		uploadDocumentsVerticalLayout.setSizeFull();
		uploadDocumentsVerticalLayout.setMargin(true);
		
		//Panel main = new Panel();
	//	uploadDocumentsVerticalLayout.addComponent(searchTable);
		//searchTable.init(false,false);
		
	//	VerticalSplitPanel vLayout = new VerticalSplitPanel();
	//	vLayout.setFirstComponent(searchTable);
	//	vLayout.setSplitPosition(46);
	   // setHeight("550px");
		//vLayout.setSpacing(true);
		//vLayout.setSecondComponent(searchUploadTable);
		//	vLayout.setFirstComponent(searchTable);
		//	uploadDocumentsVerticalLayout.setSplitPosition(46);
		//	vLayout.setSecondComponent(searchUploadTable);
		//searchUploadTable.init("upload Documents", true, true);
		uploadDocumentsVerticalLayout.addComponent(searchTable);
		//vLayout.setFirstComponent(buildseconcomponent());
		//vLayout.setSplitPosition(46);
		//setHeight("550px");
		//setCompositionRoot(vLayout);
	//	searchTable.addSearchListener(this);
	//	vLayout.setSpacing(true);
	//	uploadDocumentsVerticalLayout.setSecondComponent(searchUploadTable);
	//	uploadDocumentsVerticalLayout.addComponent(vLayout);
	//	return vLayout;
	//	private void buildseconcomponent()
	//	{
	//	};
		
		return uploadDocumentsVerticalLayout;
	}

	private void initBinder()

	{
		this.binder = new BeanFieldGroup<OMPTpaServiceChargesMakerFormDto>(OMPTpaServiceChargesMakerFormDto.class);
		this.binder.setItemDataSource(new OMPTpaServiceChargesMakerFormDto());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public VerticalLayout getgenerateLaout(){
		return invoiceEntryVerticalLayout;
	}
}
