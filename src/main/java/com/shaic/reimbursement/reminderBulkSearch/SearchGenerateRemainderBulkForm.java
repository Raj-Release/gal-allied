/**
 * 
 */
package com.shaic.reimbursement.reminderBulkSearch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 *
 */
public class SearchGenerateRemainderBulkForm extends SearchComponent<SearchGenerateRemainderBulkFormDTO> {
	
	@Inject
	private SearchGenerateRemainderBulkTable searchTable;
	
	private List<BulkReminderResultDto> prevBatchList = new ArrayList<BulkReminderResultDto>();
	
	/*@Inject
	private BulkReminderListExpoTable searchresultExportTable;*/
	
//	private TextField txtIntimationNo;
//	private TextField txtClaimNo;
	private ComboBox cpuCodeCmb;
	private ComboBox claimTypeCmb; 
	private ComboBox categoryCmb;
	private ComboBox reminderTypeCmb;
//	private TextField reminderDate;
	//private ComboBox docReceivedFromCmb;
	private PopupDateField fromDate;
	private PopupDateField toDate;
	
	protected VerticalLayout generateLetterVerticalLayout;
	
	protected TabSheet generateLetterTab;
	
	protected TabSheet printLetterTab;
	
//	private VerticalLayout generateResultLayout;
	
	private VerticalLayout vLayout;
	
	protected VerticalLayout printLetterVerticalLayout;
	
	private OptionGroup searchOption;
	
	private TabSheet mainTabSheet;
	
	private Button clearButton;
	
	private FormLayout batchIdIntimationFrmLayout = new FormLayout();
	
	//private FormLayout intimationNoFrmLayout = new FormLayout();
	
	private TextField intimationTxt;
	
	private TextField batchIdTxt;
		
	private Button searchBatchOrIntimationBtn;
	
	private Button clearBatchOrIntimationButton;
	
	private Map<String,String> searchfields = new HashMap<String, String>();
	
	private BeanItemContainer<SelectValue> claimTypeContainer;
	private BeanItemContainer<SelectValue> cpuCondeContainer;
	private BeanItemContainer<SelectValue> reminderTypeContainer;
	private BeanItemContainer<SelectValue> categoryContainer;
	//private BeanItemContainer<SelectValue> docReceivedContainer;
	
	@PostConstruct
	public void init() {
		initBinder();
	}
	
	public void getContent(){
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Generate Reminder Letter (Bulk)");
		
		searchTable.init("", false, false);
		
//		generateLetterTab = new TabSheet();
		generateLetterVerticalLayout = buildGenerateLetterLayout();
//		generateLetterTab = buildGenerateLetterLayout();
		
//		printLetterTab = new TabSheet();
		printLetterVerticalLayout = buildPrintReminderLetterLayout();
//		printLetterTab = buildPrintReminderLetterLayout();
		
		mainTabSheet = new TabSheet();
		mainTabSheet.addTab(generateLetterVerticalLayout);
		mainTabSheet.addTab(printLetterVerticalLayout);
		mainTabSheet.setSelectedTab(generateLetterVerticalLayout);
		
//		mainTabSheet.addTab(generateLetterTab,SHAConstants.GENERATE_REMINDER_TAB_BULK_REMINDER);
//		mainTabSheet.addTab(printLetterTab,SHAConstants.PRINT_TAB_BULK_REMINDER);
		 
		mainTabSheet.getTab(generateLetterVerticalLayout).setClosable(false);
		mainTabSheet.getTab(printLetterVerticalLayout).setClosable(false);
		
		mainTabSheet.setStyleName(ValoTheme.TABSHEET_FRAMED);
		mainTabSheet.setSizeFull();
		//Vaadin8-setImmediate() mainTabSheet.setImmediate(true);
		mainTabSheet.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {
			
			//Component selected = mainTabSheet.getSelectedTab();
	        public void selectedTabChange(SelectedTabChangeEvent event) {
	        	
	        		            
	        	TabSheet mainTab = (TabSheet)event.getTabSheet();
	        	
	        	//String tabName = mainTab.getCaption();	        			
	        	
	        	Layout selectedTab = (Layout) mainTab.getSelectedTab(); 
	        	
	        	String tabSheet = selectedTab.getCaption();
	        	
	        	if(tabSheet != null && ! tabSheet.isEmpty()){
	        		if((tabSheet.toLowerCase()).equalsIgnoreCase(SHAConstants.PRINT_TAB_BULK_REMINDER)){
	        			if(batchIdTxt != null) {
	        				batchIdTxt.setValue("");	
	        			}	        			
	        			if(intimationTxt != null) {
	        				intimationTxt.setValue("");
	        			}
	        			fireViewEvent(SearchGenerateRemainderBulkPresenter.GET_PREV_BATCH_LIST,null);
//	        			repaintBatchTable(prevBatchList);
	        		}
	        		else{
	        			resetFields();
	        		}
	        	}	        	
	        }
	    });
		
		mainPanel.setContent(mainTabSheet);
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout buildGenerateLetterLayout(){
//	public TabSheet buildGenerateLetterLayout(){
		btnSearch.setCaption("Generate");
		btnSearch.setDisableOnClick(true);
		generateLetterVerticalLayout = new VerticalLayout();
		generateLetterVerticalLayout.setCaption(SHAConstants.GENERATE_REMINDER_TAB_BULK_REMINDER);
		generateLetterVerticalLayout.setSizeFull();
		
//		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		claimTypeCmb = binder.buildAndBind("Claim Type","claimType",ComboBox.class);
		
		cpuCodeCmb = binder.buildAndBind("CPU Code","cpuCode",ComboBox.class);
		
		fromDate = new PopupDateField("From Date");
		String dateValue = "dd/MM/yyyy";
		fromDate.setDateFormat(dateValue);
//		FormLayout fromDtFrmLayout = new FormLayout();
//		fromDtFrmLayout.addComponent(fromDate);
		
		toDate = new PopupDateField("To Date");
		toDate.setDateFormat(dateValue);
//		FormLayout toDatefrmLayout = new FormLayout();
//		toDatefrmLayout.addComponent(toDate);
		
//		HorizontalLayout dateFromLayout = new HorizontalLayout();
//		dateFromLayout.addComponents(fromDtFrmLayout,toDatefrmLayout);
		
//		txtClaimNo = binder.buildAndBind("Claim No","claimNo",TextField.class);
		FormLayout formLayoutLeft = new FormLayout(claimTypeCmb,cpuCodeCmb,fromDate);    
		
		categoryCmb = binder.buildAndBind("Category","category",ComboBox.class);
		reminderTypeCmb = binder.buildAndBind("Reminder Type","reminderType",ComboBox.class);
		
		FormLayout formLayoutReight = new FormLayout(categoryCmb,reminderTypeCmb,toDate);	
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutReight);
		fieldLayout.setSpacing(true);
		
		clearButton = new Button("Cancel");
		
		HorizontalLayout buttonhLayout = new HorizontalLayout(btnSearch,clearButton);
		
		buttonhLayout.setSpacing(true);
		
		VerticalLayout btnLayout = new VerticalLayout();
		btnLayout.addComponent(buttonhLayout);
		btnLayout.setComponentAlignment(buttonhLayout,  Alignment.MIDDLE_CENTER);
		
		
//		AbsoluteLayout btnsAbsLayout = new AbsoluteLayout();
//		btnsAbsLayout.addComponent(buttonhLayout, "top:200.0px;left:200.0px;");
		
		generateLetterVerticalLayout.addComponent(fieldLayout);
		generateLetterVerticalLayout.setComponentAlignment(fieldLayout, Alignment.BOTTOM_LEFT);
//		generateLetterVerticalLayout.addComponent(dateFromLayout);
		generateLetterVerticalLayout.addComponent(btnLayout);
//		generateLetterVerticalLayout.setComponentAlignment(btnLayout, Alignment.MIDDLE_LEFT);
//		mainVerticalLayout.setWidth("100%");	
//		generateLetterVerticalLayout.setHeight("200px");
		generateLetterVerticalLayout.setMargin(true);
		generateLetterVerticalLayout.setSpacing(true);
		addListener();
		
		
		
		clearButton.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				resetFields();
			}
		});
		
		vLayout = new VerticalLayout();
		generateLetterVerticalLayout.addComponent(vLayout);
		
		return generateLetterVerticalLayout;
		
//		generateLetterTab.addComponent(generateLetterVerticalLayout);
//		return generateLetterTab;
	}
	
	public VerticalLayout buildPrintReminderLetterLayout(){
//	public TabSheet buildPrintReminderLetterLayout(){
		
		printLetterVerticalLayout = new VerticalLayout();
		printLetterVerticalLayout.setCaption(SHAConstants.PRINT_TAB_BULK_REMINDER);
		
		printLetterVerticalLayout.setSizeFull();
		printLetterVerticalLayout.setMargin(true);
		
		searchOption = new OptionGroup();
		
		
		Collection<Boolean> searchValues = new ArrayList<Boolean>(2);
		searchValues.add(true);
		searchValues.add(false);

		searchOption.addItems(searchValues);
		searchOption.setItemCaption(true, "Batch Id");
		searchOption.setItemCaption(false, "Intimation No");
		searchOption.setStyleName("horizontal");
		//Vaadin8-setImmediate() searchOption.setImmediate(true);
		
		searchOption.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				Boolean batchId = (Boolean)event.getProperty().getValue();

				if(batchId){
					clearSearchComponentLayout();
					buildBatchIdLayout();					
				}
				else{
					clearSearchComponentLayout();
					buildIntimationNoLayout();					
				}
				
				searchBatchOrIntimationBtn.setData(searchfields);
				
			}
		}
		);
		
		
		FormLayout searchFrm = new FormLayout();
		
		searchFrm.addComponent(searchOption);
		
		printLetterVerticalLayout.addComponent(searchFrm);
		
		batchIdIntimationFrmLayout = new FormLayout();
		buildBatchIdLayout();
		buildIntimationNoLayout();		
		printLetterVerticalLayout.addComponent(batchIdIntimationFrmLayout);
		searchBatchOrIntimationBtn = new Button();		
		searchBatchOrIntimationBtn.setCaption("Search");
		
		searchBatchOrIntimationBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
//				String searchFilter = (String)event.getButton().getData();				
				if(searchfields != null && !searchfields.isEmpty()){
					fireViewEvent(SearchGenerateRemainderBulkPresenter.SEARCH_BATCH_ID_BUTTON_CLICK, searchfields);
				}
				else{
					showErrorMsg("Please Enter value for BatchId / Intimation Number for Search");
				}
			}
		});
				
		clearBatchOrIntimationButton = new Button();
		clearBatchOrIntimationButton.setCaption("Reset");		
		clearBatchOrIntimationButton.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				resetBulkReminderPrintScreen();				
			}
		});		
		
		
		HorizontalLayout btnHlayout = new HorizontalLayout();
		btnHlayout.addComponents(searchBatchOrIntimationBtn,clearBatchOrIntimationButton);
		btnHlayout.setSpacing(true);
		
		VerticalLayout btnLayout = new VerticalLayout();
		btnLayout.addComponent(btnHlayout);
		btnLayout.setComponentAlignment(btnHlayout,Alignment.MIDDLE_CENTER);		
		printLetterVerticalLayout.addComponent(btnLayout);
		printLetterVerticalLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_LEFT);
				
		printLetterVerticalLayout.addComponent(searchTable);
				
		return printLetterVerticalLayout;
		
//		printLetterTab.addComponent(printLetterVerticalLayout);
//		return printLetterTab;
	}
	
	public void resetBulkReminderPrintScreen(){
		
		if(intimationTxt != null){
			intimationTxt.setValue("");
		}
		if (batchIdTxt != null){
			batchIdTxt.setValue("");
		}
		searchTable.removeRow();
	}
	
	public void buildBatchIdLayout(){
		if(batchIdTxt != null)
			batchIdIntimationFrmLayout.removeComponent(batchIdTxt);
		batchIdTxt = new TextField("Batch Id");
		batchIdTxt.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				searchfields.clear();
				String value = (String)event.getProperty().getValue();
				if(value != null && !value.isEmpty()){
					searchfields.put(SHAConstants.SEARCH_REMINDER_BATCH, value);	
				}
								
			}
		});
		
		batchIdIntimationFrmLayout.addComponent(batchIdTxt);		
		
	}
	
	public void buildIntimationNoLayout(){
		if(intimationTxt != null)
			batchIdIntimationFrmLayout.removeComponent(intimationTxt);
		intimationTxt = new TextField("Intimation No");
		intimationTxt.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				searchfields.clear();
				String value = (String)event.getProperty().getValue();
				if(value != null && !value.isEmpty()){
					searchfields.put(SHAConstants.SEARCH_REMINDER_INTIMATION,value);
				}
			}
		});
	
		batchIdIntimationFrmLayout.addComponent(intimationTxt);
	}
	
	public void clearSearchComponentLayout(){
		batchIdIntimationFrmLayout.removeAllComponents();
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchGenerateRemainderBulkFormDTO>(SearchGenerateRemainderBulkFormDTO.class);
		this.binder.setItemDataSource(new SearchGenerateRemainderBulkFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public void setDropDownValues(Map<String,Object> parameter) {
		claimTypeContainer = parameter.containsKey("claimTypeContainer") && parameter.get("claimTypeContainer") != null ? (BeanItemContainer<SelectValue>)parameter.get("claimTypeContainer") : new BeanItemContainer<SelectValue>(SelectValue.class);
		cpuCondeContainer = parameter.containsKey("cpuCodeContainer") && parameter.get("cpuCodeContainer") != null ? (BeanItemContainer<SelectValue>)parameter.get("cpuCodeContainer") : new BeanItemContainer<SelectValue>(SelectValue.class);
		reminderTypeContainer = parameter.containsKey("reminderTypeContainer") && parameter.get("reminderTypeContainer") != null ? (BeanItemContainer<SelectValue>)parameter.get("reminderTypeContainer") : new BeanItemContainer<SelectValue>(SelectValue.class);
		categoryContainer = parameter.containsKey("categoryContainer") && parameter.get("categoryContainer") != null ? (BeanItemContainer<SelectValue>)parameter.get("categoryContainer") : new BeanItemContainer<SelectValue>(SelectValue.class);
		
		claimTypeCmb.setContainerDataSource(claimTypeContainer);
		claimTypeCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		claimTypeCmb.setItemCaptionPropertyId("value");
		
		cpuCodeCmb.setContainerDataSource(cpuCondeContainer);
		cpuCodeCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cpuCodeCmb.setItemCaptionPropertyId("value");
		
		categoryCmb.setContainerDataSource(categoryContainer);
		categoryCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		categoryCmb.setItemCaptionPropertyId("value");
		
		reminderTypeCmb.setContainerDataSource(reminderTypeContainer);
		reminderTypeCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		reminderTypeCmb.setItemCaptionPropertyId("value");
		
		prevBatchList = parameter.containsKey("prevBatchList") && parameter.get("prevBatchList") != null ? (List<BulkReminderResultDto>)parameter.get("prevBatchList") : new ArrayList<BulkReminderResultDto>();
		searchTable.setTableList(prevBatchList);
		repaintBatchTable(prevBatchList);
		
	}	
	
	public void resetFields(){
		
 		claimTypeCmb.setValue(null);
		cpuCodeCmb.setValue(null);
		categoryCmb.setValue(null);
		reminderTypeCmb.setValue(null);
		fromDate = null;
		toDate.setValue(new Date());
		vLayout.removeAllComponents();	
		if(intimationTxt != null ){
			intimationTxt.setValue("");			
		}
		if(batchIdTxt != null){
			batchIdTxt.setValue("");
		}
		
	}
	
	public SearchGenerateRemainderBulkTable getSearchTable(){
		return this.searchTable;
	}
	
	public SearchGenerateRemainderBulkFormDTO getSearchFilters(){
		SearchGenerateRemainderBulkFormDTO bean = null;
		try {			
				boolean hasError = false;
				StringBuffer errorMsg = new StringBuffer();			
				
				if(binder.isValid()){
					this.binder.commit();	
				}
				else if(claimTypeCmb.getValue() == null && categoryCmb.getValue() == null && cpuCodeCmb.getValue() == null && reminderTypeCmb.getValue() == null) {
					errorMsg.append("Please Provide Atleast one Value for Search Condtion<br>");
					hasError = true;
				}
				
				
//				if(claimTypeCmb.getValue() == null){
//					errorMsg += "Please Provide Atleast one Value for Claim Type<br>";
//					hasError = true;
//				}
//				if(categoryCmb.getValue() == null){
//					errorMsg += "Please Provide Atleast one Value for Category<br>";
//					hasError = true;
//				}
//				if(cpuCodeCmb.getValue() == null){
//					errorMsg += "Please Provide Atleast one Value for CPU Code<br>";
//					hasError = true;
//				}
//				if(reminderTypeCmb.getValue() == null){
//					errorMsg += "Please Provide Atleast one Value for Reminder Type<br>";
//					hasError = true;
//				}				
				
				if(!hasError){
					bean = binder.getItemDataSource().getBean();
					bean.setLobId(ReferenceTable.HEALTH_LOB_KEY);
				}
				else{
					showErrorMsg(errorMsg.toString());
					bean = null;
				}
				
		} catch (CommitException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public void showErrorMsg(String errorMessage) {
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
	
	public void paintSearchResultLayout(BulkReminderResultDto bulkReminderResultDto){
		
		if(vLayout != null){
			generateLetterVerticalLayout.removeComponent(vLayout);
			vLayout.removeAllComponents();
		}
		else{
			vLayout = new VerticalLayout();
		}
		FormLayout resultLayout = new FormLayout();
		
		Label batchlbl = new Label();
		
		if(bulkReminderResultDto.getBatchid() != null && !bulkReminderResultDto.getBatchid().isEmpty()){
		
		batchlbl.setValue("Letter Will be Generated Shortly !!!");
		resultLayout.addComponent(batchlbl);

		Label batchLble = new Label();
		batchLble.setValue("Batch Id:");
		Label batchValLbl = new Label();
		batchValLbl.setValue(bulkReminderResultDto.getBatchid());
		HorizontalLayout hlayout = new HorizontalLayout();
		hlayout.addComponents(batchLble,batchValLbl);
		resultLayout.addComponent(hlayout);
		
		Label totalNoofRecLbl = new Label();
		totalNoofRecLbl.setValue("Total No.of Records :");
		
		Label totalNoofRecValLbl = new Label();
		totalNoofRecValLbl.setValue(String.valueOf(bulkReminderResultDto.getTotalNoofRecords()));
		
		HorizontalLayout hlayout1 = new HorizontalLayout();
		hlayout1.addComponents(totalNoofRecLbl,totalNoofRecValLbl);
		resultLayout.addComponent(hlayout1);
		}
		else{
			batchlbl.setValue("No Records Found");
			resultLayout.addComponent(batchlbl);
		}
		
		vLayout.addComponent(resultLayout);
		
		generateLetterVerticalLayout.addComponent(vLayout);
		
	}
	
	public void repaintBatchTable(List<BulkReminderResultDto> batchRemList){
		printLetterVerticalLayout.removeComponent(searchTable);
		searchTable.initTable();
		searchTable.setTableList(batchRemList);		
		printLetterVerticalLayout.addComponent(searchTable);
		
	}
	
	public void exportList(BulkReminderResultDto bulkReminderDto)
	{
//		List<SearchGenerateReminderBulkTableDTO> eportList = bulkReminderDto.getResultListDto();
//		searchresultExportTable.init("", false, false);
//		if(eportList != null && !eportList.isEmpty()){
//			
//			searchresultExportTable.setTableList(eportList);
//			ExcelExport excelExport = new ExcelExport(searchresultExportTable.getTable());
//			excelExport.setReportTitle("Bulk Reminder List");
//			excelExport.setDisplayTotals(false);
//			excelExport.export();
//		}
	}	
	
	public VerticalLayout getgenerateLaout(){
		return generateLetterVerticalLayout;
	}
	
}