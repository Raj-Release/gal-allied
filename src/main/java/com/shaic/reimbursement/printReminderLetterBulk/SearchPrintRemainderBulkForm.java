/**
 * 
 */
package com.shaic.reimbursement.printReminderLetterBulk;

import java.util.ArrayList;
import java.util.Calendar;
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
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.ui.TabSheet;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 *
 */
public class SearchPrintRemainderBulkForm extends SearchComponent<SearchPrintRemainderBulkFormDTO> {
	
	@Inject
	private SearchPrintRemainderBulkTable searchTable;
	
	private List<PrintBulkReminderResultDto> prevBatchList = new ArrayList<PrintBulkReminderResultDto>();
	
	/*@Inject
	private PrintBulkReminderListExpoTable searchresultExportTable;*/
	
	private ComboBox batchTypeCmb;
	private ComboBox cpuCodeCmb;
	private ComboBox claimTypeCmb; 
	private ComboBox categoryCmb;
	private ComboBox reminderTypeCmb;
	private PopupDateField fromDate;
	private PopupDateField toDate;
	
	protected TabSheet pendingLetterTab;
	
	protected TabSheet completedLetterTab;
	
	private VerticalLayout vLayout = new VerticalLayout();
	
	protected VerticalLayout printLetterVerticalLayout;
	
//	private OptionGroup searchOption;
	
	private VerticalLayout mainVerticalLayout;
	
	private Button clearButton;
	
	private FormLayout batchIdIntimationFrmLayout = new FormLayout();
	
	private FormLayout toDateFrmLayout = new FormLayout();
	
	//private TextField intimationTxt;
	
	private TextField batchIdTxt;
		
	private Button searchBatchOrIntimationBtn;
	
	private Button clearBatchOrIntimationButton;
	
	private Map<String,Object> searchfields = new HashMap<String, Object>();
	
	private BeanItemContainer<SelectValue> batchTypeContainer;
	private BeanItemContainer<SelectValue> claimTypeContainer;
	private BeanItemContainer<SelectValue> cpuCondeContainer;
	private BeanItemContainer<SelectValue> reminderTypeContainer;
	private BeanItemContainer<SelectValue> panCardReminderTypeContainer;
	private BeanItemContainer<SelectValue> categoryContainer;
	//private BeanItemContainer<SelectValue> docReceivedContainer;
	
	@PostConstruct
	public void init() {
		initBinder();
	}
	
	@SuppressWarnings("serial")
	public void getContent(){
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Print Reminder Letter (Bulk)");
		mainVerticalLayout = new VerticalLayout();
		
		batchTypeCmb = binder.buildAndBind("Batch Type","searchOption",ComboBox.class);
		
		batchTypeCmb.setContainerDataSource(batchTypeContainer);
		batchTypeCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		batchTypeCmb.setItemCaptionPropertyId("value");
		
		FormLayout batchTypeFrmLayout = new FormLayout(batchTypeCmb);
		batchTypeFrmLayout.setSpacing(true);
		batchTypeFrmLayout.setMargin(new MarginInfo(true, true, false, true));
		mainVerticalLayout.addComponent(batchTypeFrmLayout);
		
		fromDate = binder.buildAndBind("From Date","fromDate",PopupDateField.class); 
		String dateValue = "dd/MM/yyyy";
		fromDate.setDateFormat(dateValue);
		
		toDate = binder.buildAndBind("To Date","toDate",PopupDateField.class);
		toDate.setDateFormat(dateValue);
		
		batchIdTxt = new TextField("Batch Id");
		
		claimTypeCmb = binder.buildAndBind("Claim Type","claimType",ComboBox.class);
		
		claimTypeCmb.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				SelectValue clmTypeSelect =  (SelectValue)event.getProperty().getValue();
				
				ArrayList<SelectValue> categorySelectContainerList = new ArrayList<SelectValue>();
				categorySelectContainerList.addAll(categoryContainer.getItemIds());
				SelectValue panSelect = null;
				for (SelectValue selectComp : categorySelectContainerList) {
					if(SHAConstants.PAN_CARD.equalsIgnoreCase(selectComp.getValue())){
						panSelect = selectComp;
						break;
					}
				}
				if(panSelect == null){
					panSelect = new SelectValue(null,SHAConstants.PAN_CARD);
				}
				
				if(claimTypeCmb.getValue() != null && !(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(clmTypeSelect.getId())){		
					if(categorySelectContainerList.contains(panSelect)){
						categorySelectContainerList.remove(panSelect);						
					}
					if(categoryCmb.getValue() != null){
						if(SHAConstants.PAN_CARD.equalsIgnoreCase(((SelectValue)categoryCmb.getValue()).getValue())){
							categoryCmb.setValue(null);	
						}
						
					}
				}
				else{
					if(!categorySelectContainerList.contains(panSelect)){						
						categorySelectContainerList.add(panSelect);						
					}					
				}
				
				categoryContainer.removeAllItems();
				categoryContainer.addAll(categorySelectContainerList);
			}
		});			
				
		cpuCodeCmb = binder.buildAndBind("CPU Code","cpuCode",ComboBox.class);
		categoryCmb = binder.buildAndBind("Category","category",ComboBox.class);
		reminderTypeCmb = binder.buildAndBind("Reminder Type","reminderType",ComboBox.class);
		
		printLetterVerticalLayout = new VerticalLayout();
		printLetterVerticalLayout.setMargin(new MarginInfo(false, true, false, true));
		printLetterVerticalLayout.setSpacing(true);
		
		mainVerticalLayout.addComponent(printLetterVerticalLayout);
		batchTypeCmb.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(batchTypeCmb.getValue() != null){
					clearSearchComponents();
					printLetterVerticalLayout.removeAllComponents();
					searchTable.removeRow();
					repaintSearchOptoin();
					SelectValue searchoption = (SelectValue)batchTypeCmb.getValue();
					if(searchoption != null && searchoption.getValue() != null && (SHAConstants.LETTERS_PRINT_PENDING).equals(searchoption.getValue())){
						printLetterVerticalLayout = buildGenerateLetterLayout();				
						
					}
					else if( searchoption != null && searchoption.getValue() != null && (SHAConstants.LETTERS_PRINT_COMPLETED).equals(searchoption.getValue())){
						printLetterVerticalLayout = buildPrintReminderLetterLayout();			
					}
					printLetterVerticalLayout.addComponent(vLayout);
				}
				
			}
		});
		
		batchTypeCmb.setValue(batchTypeContainer.getIdByIndex(0));
		searchTable.init("", false, false);		
		
		mainVerticalLayout.setSizeFull();
		//Vaadin8-setImmediate() mainVerticalLayout.setImmediate(true);
		
		mainPanel.setContent(mainVerticalLayout);
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout buildGenerateLetterLayout(){
		printLetterVerticalLayout.removeAllComponents();
		btnSearch.setCaption("Search");
		btnSearch.setDisableOnClick(true);
		
		FormLayout formLayoutLeft = new FormLayout(fromDate,claimTypeCmb);    
		formLayoutLeft.setSpacing(false);
		
		FormLayout formLayoutMiddle = new FormLayout(toDate,categoryCmb,reminderTypeCmb);	
		formLayoutMiddle.setSpacing(false);
		
		FormLayout formLayoutRight = new FormLayout(cpuCodeCmb,reminderTypeCmb);	
		formLayoutMiddle.setSpacing(false);
		
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutMiddle,formLayoutRight);
		fieldLayout.setSpacing(true);
		
		clearButton = new Button("Reset");
		clearButton.addStyleName(ValoTheme.BUTTON_DANGER);
		
		HorizontalLayout buttonhLayout = new HorizontalLayout(btnSearch,clearButton);
		
		buttonhLayout.setSpacing(true);
		
		VerticalLayout btnLayout = new VerticalLayout();
		btnLayout.setWidth("70%");
		btnLayout.addComponent(buttonhLayout);
		btnLayout.setComponentAlignment(buttonhLayout,  Alignment.MIDDLE_CENTER);
		
		printLetterVerticalLayout.addComponent(fieldLayout);
		printLetterVerticalLayout.setComponentAlignment(fieldLayout, Alignment.BOTTOM_LEFT);
		printLetterVerticalLayout.addComponent(btnLayout);
//		printLetterVerticalLayout.setMargin(true);
		printLetterVerticalLayout.setSpacing(true);
		addListener();		
		
		clearButton.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				resetFields();
			}
		});
		
		return printLetterVerticalLayout;
		
	}
	
	public void clearSearchComponents(){
		fromDate.setValue(null);
		claimTypeCmb.setValue(null);
		toDate.setValue(null);
		categoryCmb.setValue(null);
		reminderTypeCmb.setValue(null);
		cpuCodeCmb.setValue(null);
		reminderTypeCmb.setValue(null);
		batchIdTxt.setValue("");
		searchTable.removeRow();
	}
	
	public VerticalLayout buildPrintReminderLetterLayout(){
		printLetterVerticalLayout.removeAllComponents();
		
		HorizontalLayout searchLayout = new HorizontalLayout();
		
		batchIdIntimationFrmLayout = new FormLayout();
		buildBatchIdLayout();
		buildToDateLayout();		
		searchLayout.addComponents(batchIdIntimationFrmLayout,toDateFrmLayout);
		searchLayout.setSpacing(true);
		
		printLetterVerticalLayout.addComponent(searchLayout);
		searchBatchOrIntimationBtn = new Button();		
		searchBatchOrIntimationBtn.setCaption("Search");
		searchBatchOrIntimationBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		searchBatchOrIntimationBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				SearchPrintRemainderBulkFormDTO searchDTO = getSearchFilters();

				if(searchDTO != null){
					fireViewEvent(SearchPrintRemainderBulkPresenter.SEARCH_BULK_PRINT_BATCH_ID_BUTTON_CLICK, searchDTO);
				}
				
//				if(searchfields != null && !searchfields.isEmpty()){
//					
//					fireViewEvent(SearchPrintRemainderBulkPresenter.SEARCH_BULK_PRINT_BATCH_ID_BUTTON_CLICK, searchfields);
//				}
//				else{
//					showErrorMsg("Please Enter value for BatchId / From Date and To Date for Search");
//				}
			}
		});
				
		clearBatchOrIntimationButton = new Button();
		clearBatchOrIntimationButton.setCaption("Reset");	
		clearBatchOrIntimationButton.addStyleName(ValoTheme.BUTTON_DANGER);
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
		btnLayout.setWidth("40%");
		btnLayout.addComponent(btnHlayout);
		btnLayout.setComponentAlignment(btnHlayout,Alignment.MIDDLE_CENTER);		
		printLetterVerticalLayout.addComponent(btnLayout);
		printLetterVerticalLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_LEFT);
				
		return printLetterVerticalLayout;		
	}
	
	public void resetBulkReminderPrintScreen(){
		
		if (batchIdTxt != null){
			batchIdTxt.setValue("");
		}
		if(fromDate != null){
			fromDate.setValue(null);			
		}
		if(toDate != null){
			toDate.setValue(null);
		}
		searchTable.removeRow();
		vLayout.removeAllComponents();
	}
	
	public void buildBatchIdLayout(){
		batchIdTxt.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				searchfields.clear();
				String value = (String)event.getProperty().getValue();
				if(value != null && !value.isEmpty()){
					searchfields.put(SHAConstants.SEARCH_REMINDER_BATCH, value);
				}
				Date fromDt = fromDate.getValue();
				Date toDte = toDate.getValue();
				searchfields.put("fromDate",fromDt);
				searchfields.put("toDate",toDte);				
			}
		});
		batchIdIntimationFrmLayout.addComponent(fromDate);
		
		batchIdIntimationFrmLayout.addComponent(batchIdTxt);
		binder.getItemDataSource().getBean().setBatchId(batchIdTxt.getValue());
		
	}
	
	public void buildToDateLayout(){
	
		toDateFrmLayout.addComponent(toDate);
		
		Date toDt = toDate.getValue();
		searchfields.put("toDate",toDt);
	}
	
	public void clearSearchComponentLayout(){
		batchIdIntimationFrmLayout.removeAllComponents();
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchPrintRemainderBulkFormDTO>(SearchPrintRemainderBulkFormDTO.class);
		this.binder.setItemDataSource(new SearchPrintRemainderBulkFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		batchTypeContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue pendingSelect = new SelectValue(null,SHAConstants.LETTERS_PRINT_PENDING);
		batchTypeContainer.addBean(pendingSelect);
		SelectValue completedSelect = new SelectValue(null,SHAConstants.LETTERS_PRINT_COMPLETED);
		batchTypeContainer.addBean(completedSelect);
	}

	public void setDropDownValues(Map<String,Object> parameter) {
		
		claimTypeContainer = parameter.containsKey("claimTypeContainer") && parameter.get("claimTypeContainer") != null ? (BeanItemContainer<SelectValue>)parameter.get("claimTypeContainer") : new BeanItemContainer<SelectValue>(SelectValue.class);
		cpuCondeContainer = parameter.containsKey("cpuCodeContainer") && parameter.get("cpuCodeContainer") != null ? (BeanItemContainer<SelectValue>)parameter.get("cpuCodeContainer") : new BeanItemContainer<SelectValue>(SelectValue.class);
		reminderTypeContainer = parameter.containsKey("reminderTypeContainer") && parameter.get("reminderTypeContainer") != null ? (BeanItemContainer<SelectValue>)parameter.get("reminderTypeContainer") : new BeanItemContainer<SelectValue>(SelectValue.class);
		categoryContainer = parameter.containsKey("categoryContainer") && parameter.get("categoryContainer") != null ? (BeanItemContainer<SelectValue>)parameter.get("categoryContainer") : new BeanItemContainer<SelectValue>(SelectValue.class);
//		panCardReminderTypeContainer = parameter.containsKey("panCardeReminderTypeContainer") && parameter.get("panCardeReminderTypeContainer") != null ? (BeanItemContainer<SelectValue>)parameter.get("panCardeReminderTypeContainer") : new BeanItemContainer<SelectValue>(SelectValue.class);
		
		panCardReminderTypeContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue panReqLetter = new SelectValue(1l,SHAConstants.PAN_CARD_REQUEST);
		
		panCardReminderTypeContainer.addBean(panReqLetter);
		SelectValue firstPANRem = new SelectValue(2l,("PAN Card - " + SHAConstants.FIRST_REMINDER));
		panCardReminderTypeContainer.addBean(firstPANRem);
		SelectValue secondPANRem = new SelectValue(3l,"PAN Card - " + SHAConstants.SECOND_REMINDER);
		panCardReminderTypeContainer.addBean(secondPANRem);
				
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
		
		categoryCmb.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				if(categoryCmb.getValue() != null){
					SelectValue panselect = (SelectValue)categoryCmb.getValue();
					if(SHAConstants.PAN_CARD.equalsIgnoreCase(panselect.getValue())){
						reminderTypeCmb.setContainerDataSource(panCardReminderTypeContainer);
//						reminderTypeCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//						reminderTypeCmb.setItemCaptionPropertyId("value");	
					}
					else{
						reminderTypeCmb.setContainerDataSource(reminderTypeContainer);
//						reminderTypeCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//						reminderTypeCmb.setItemCaptionPropertyId("value");
					}
				}
				else{
					reminderTypeCmb.setContainerDataSource(reminderTypeContainer);
//					reminderTypeCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//					reminderTypeCmb.setItemCaptionPropertyId("value");
				}				
			}
		});
		
		
				
//		prevBatchList = parameter.containsKey("prevBatchList") && parameter.get("prevBatchList") != null ? (List<PrintBulkReminderResultDto>)parameter.get("prevBatchList") : new ArrayList<PrintBulkReminderResultDto>();
//		searchTable.setTableList(prevBatchList);
		repaintBatchTable(prevBatchList);
		
	}	
	
	public void resetFields(){
		
 		claimTypeCmb.setValue(null);
		cpuCodeCmb.setValue(null);
		categoryCmb.setValue(null);
		reminderTypeCmb.setValue(null);
		fromDate.setValue(null);
		toDate.setValue(null);
		batchTypeCmb.setValue(batchTypeContainer.getIdByIndex(0));
		vLayout.removeAllComponents();	
		searchTable.removeRow();
		
	}
	
	public SearchPrintRemainderBulkTable getSearchTable(){
		return this.searchTable;
	}
	
	@SuppressWarnings("deprecation")
	public SearchPrintRemainderBulkFormDTO getSearchFilters(){
		SearchPrintRemainderBulkFormDTO bean = null;
		try {			
				boolean hasError = false;
				StringBuffer errorMsg = new StringBuffer();			
				
				
				if(batchTypeCmb.getValue() != null){
				
					SelectValue searchOptionVal = (SelectValue)batchTypeCmb.getValue(); 
					if(searchOptionVal.getValue() != null && SHAConstants.LETTERS_PRINT_PENDING.equalsIgnoreCase(searchOptionVal.getValue()) && categoryCmb.getValue() == null){
						hasError = true;
						errorMsg.append("Please Provide Atleast one Value for Category. <br>");
					}					
				}			
				
				if(fromDate.getValue() != null && toDate.getValue() != null){
					if((fromDate.getValue().getYear() - toDate.getValue().getYear() > 0) && ((fromDate.getValue().getDate() - toDate.getValue().getDate()) > 0 || (toDate.getValue().getDate() - fromDate.getValue().getDate()) >= 7 
								|| (fromDate.getValue().getMonth() - toDate.getValue().getMonth()) < 0)){
						errorMsg.append("The From and To date range should not Exceed 7 Days.<br>");
						hasError = true;
					}
				
					if(toDate.getValue().getYear() - fromDate.getValue().getYear()  != 0 && ((toDate.getValue().getYear() - fromDate.getValue().getYear() != 1 ) || (fromDate.getValue().getMonth() - toDate.getValue().getMonth() != 11))){
						errorMsg.append("The From and To date range should not Exceed 7 Days.<br>");
						hasError = true;
					}
						
					if((toDate.getValue().getYear() - fromDate.getValue().getYear() == 1 ) && (fromDate.getValue().getMonth() - toDate.getValue().getMonth() == 11)){
							
						Calendar lastYearDate = Calendar.getInstance();
						lastYearDate.setTime(fromDate.getValue());
							int lastYearDays = lastYearDate.getMaximum(Calendar.DAY_OF_MONTH) - fromDate.getValue().getDate()+1;
							int curYearDays = toDate.getValue().getDate();
							int totalDays = lastYearDays + curYearDays;
							if(totalDays < 0 ||  totalDays > 7){
								errorMsg.append("The From and To date range should not Exceed 7 Days.<br>");
								hasError = true;
						    }
				   }
				}
				
				if(batchTypeCmb.getValue() != null){
					SelectValue selectOpt = (SelectValue)batchTypeCmb.getValue();
					
					if(selectOpt.getValue() != null && SHAConstants.LETTERS_PRINT_COMPLETED.equalsIgnoreCase(selectOpt.getValue()) && fromDate.getValue() == null && toDate.getValue() == null){
						if(batchIdTxt.getValue() == null || batchIdTxt.getValue().isEmpty()){
							errorMsg.append("Please Enter the Batch Id / From and To Date for search.<br>");
							hasError = true;
						}
					}
				}
				
				if(!hasError && binder.isValid()){
					this.binder.commit();	
				}
				
				if(!hasError){
					bean = binder.getItemDataSource().getBean();
					bean.setBatchId(batchIdTxt.getValue());
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
	
	public void repaintSearchOptoin(){
		if(vLayout != null){
			vLayout.removeAllComponents();
		}
		else{
			vLayout = new VerticalLayout();
		}
		
		Label batchTypelbl = new Label();
		
		if(batchTypeCmb.getValue() != null && ((SelectValue)batchTypeCmb.getValue()).getValue() != null){
		
			batchTypelbl.setCaption(((SelectValue)batchTypeCmb.getValue()).getValue());
			vLayout.addComponent(batchTypelbl);
		}
	}
	
	public void repaintBatchTable(List<PrintBulkReminderResultDto> batchRemList){
		repaintSearchOptoin();	
		mainVerticalLayout.removeComponent(searchTable);
		searchTable.initTable();
		searchTable.setTableList(batchRemList);
		searchTable.generatePrintCompletedColumn();
		searchTable.generateExprotColumn();
		mainVerticalLayout.addComponent(searchTable);		
	}
	
	public void exportList(PrintBulkReminderResultDto bulkReminderDto)
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
		return printLetterVerticalLayout;
	}
	
}