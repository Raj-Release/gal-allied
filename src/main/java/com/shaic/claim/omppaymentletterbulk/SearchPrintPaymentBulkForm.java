package com.shaic.claim.omppaymentletterbulk;

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
import com.shaic.reimbursement.printReminderLetterBulk.PrintBulkReminderResultDto;
import com.shaic.reimbursement.printReminderLetterBulk.SearchPrintRemainderBulkFormDTO;
import com.shaic.reimbursement.printReminderLetterBulk.SearchPrintRemainderBulkPresenter;
import com.shaic.reimbursement.printReminderLetterBulk.SearchPrintRemainderBulkTable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;

public class SearchPrintPaymentBulkForm extends SearchComponent<SearchPrintPaymentBulkFormDTO> {
	
	@Inject
	private SearchPrintPaymentBulkTable searchTable;
	
	private List<PrintBulkPaymentResultDto> prevBatchList = new ArrayList<PrintBulkPaymentResultDto>();
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private PopupDateField fromDate;
	private PopupDateField toDate;
	
	
	
	private VerticalLayout vLayout = new VerticalLayout();
	
	protected VerticalLayout printLetterVerticalLayout;
	
	
	private VerticalLayout mainVerticalLayout;
	
	private Button clearButton;
	
	private FormLayout batchIdIntimationFrmLayout = new FormLayout();
	
	private FormLayout toDateFrmLayout = new FormLayout();

	private Map<String,Object> searchfields = new HashMap<String, Object>();
	
	private Button searchBatchOrIntimationBtn;
	
	private Button clearBatchOrIntimationButton;


	
	@PostConstruct
	public void init() {
		initBinder();
	}
	
	@SuppressWarnings("serial")
	public void getContent(){
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Print OMP Payment Letter (Bulk)");
		mainVerticalLayout = new VerticalLayout();
		
		
		
		fromDate = binder.buildAndBind("From Date","fromDate",PopupDateField.class); 
		String dateValue = "dd/MM/yyyy";
		fromDate.setDateFormat(dateValue);
		fromDate.setRequired(true);
		
		toDate = binder.buildAndBind("To Date","toDate",PopupDateField.class);
		toDate.setDateFormat(dateValue);
		toDate.setRequired(true);

		mandatoryFields.add(fromDate);
		mandatoryFields.add(toDate);
		showOrHideValidation(false);

	
		printLetterVerticalLayout = new VerticalLayout();
		printLetterVerticalLayout.setMargin(new MarginInfo(false, true, false, true));
		printLetterVerticalLayout.setSpacing(true);
		
		printLetterVerticalLayout = buildGenerateLetterLayout();	
		
		mainVerticalLayout.addComponent(printLetterVerticalLayout);
		
		searchTable.init("", false, false);		
		
		mainVerticalLayout.setSizeFull();
		
		mainPanel.setContent(mainVerticalLayout);
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout buildGenerateLetterLayout(){
		printLetterVerticalLayout.removeAllComponents();
		btnSearch.setCaption("Search");
		btnSearch.setDisableOnClick(true);
		
		FormLayout formLayoutLeft = new FormLayout(fromDate);    
		formLayoutLeft.setSpacing(false);
		
		FormLayout formLayoutMiddle = new FormLayout(toDate);	
		formLayoutMiddle.setSpacing(false);

		
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutMiddle);
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
		toDate.setValue(null);
		searchTable.removeRow();
	}
	
	public VerticalLayout buildPrintReminderLetterLayout(){
		printLetterVerticalLayout.removeAllComponents();
		
		HorizontalLayout searchLayout = new HorizontalLayout();
		
		batchIdIntimationFrmLayout = new FormLayout();
		buildToDateLayout();		
		searchLayout.addComponents(batchIdIntimationFrmLayout,toDateFrmLayout);
		searchLayout.setSpacing(true);
		
		printLetterVerticalLayout.addComponent(searchLayout);
		searchBatchOrIntimationBtn = new Button();		
		searchBatchOrIntimationBtn.setCaption("Search");
		searchBatchOrIntimationBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
				
		clearBatchOrIntimationButton = new Button();
		clearBatchOrIntimationButton.setCaption("Reset");	
		clearBatchOrIntimationButton.addStyleName(ValoTheme.BUTTON_DANGER);
		clearBatchOrIntimationButton.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				resetBulkPaymentPrintScreen();				
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
	
	public void resetBulkPaymentPrintScreen(){
		
		
		if(fromDate != null){
			fromDate.setValue(null);			
		}
		if(toDate != null){
			toDate.setValue(null);
		}
		searchTable.removeRow();
		vLayout.removeAllComponents();
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
		this.binder = new BeanFieldGroup<SearchPrintPaymentBulkFormDTO>(SearchPrintPaymentBulkFormDTO.class);
		this.binder.setItemDataSource(new SearchPrintPaymentBulkFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
	}

	
	public void resetFields(){
		
 		
		fromDate.setValue(null);
		toDate.setValue(null);
		vLayout.removeAllComponents();	
		searchTable.removeRow();
		
	}
	
	public SearchPrintPaymentBulkTable getSearchTable(){
		return this.searchTable;
	}
	
	@SuppressWarnings("deprecation")
	public SearchPrintPaymentBulkFormDTO getSearchFilters(){
		SearchPrintPaymentBulkFormDTO bean = null;
		try {			
				boolean hasError = false;
				StringBuffer errorMsg = new StringBuffer();		
				
			/*	if(fromDate.getValue() != null && toDate.getValue() != null){
					
					 //Comparing dates
			        

			        //Convert long to String
			        String dayDifference = Long.toString(differenceDates);
			        
			        if(differenceDates < 0 && Math.abs(differenceDates) >= 7 ){
			        	errorMsg.append("The From and To date range should not Exceed 7 Days.<br>");
						hasError = true;
			        }else if(differenceDates > 0){
			        	errorMsg.append(" To date must be after From date<br>");
						hasError = true;
			        }
			        
				}else{
					errorMsg.append("The From and To date Fields are Mandatory.<br>");
					hasError = true;
				}*/
				
			if (fromDate.getValue() == null && toDate.getValue() == null) {
				errorMsg.append("The From and To date Fields are Mandatory.<br>");
				hasError = true;
			} else if (fromDate.getValue() != null && toDate.getValue() == null) {
				errorMsg.append("Date Fields are Mandatory, Please provide To Date Value<br>");
				hasError = true;
			} else if (fromDate.getValue() == null && toDate.getValue() != null) {
				errorMsg.append("Date Fields are Mandatory, Please provide From Date Value<br>");
				hasError = true;
			} else {
				if (fromDate.getValue() != null && toDate.getValue() != null) {
					long difference = fromDate.getValue().getTime() - toDate.getValue().getTime();
					long differenceDates = difference / (24 * 60 * 60 * 1000);
					if (differenceDates < 0 && Math.abs(differenceDates) >= 7) {
						errorMsg.append("The From and To date range should not Exceed 7 Days.<br>");
						hasError = true;
					} else if (toDate.getValue().before(fromDate.getValue())) {
						errorMsg.append("Enter Valid To Date<br>");
						hasError = true;
					}
				}
			}
				
				
				if(!hasError && binder.isValid()){
					this.binder.commit();	
				}
				
				if(!hasError){
					bean = binder.getItemDataSource().getBean();
					//bean.setBatchId(batchIdTxt.getValue());
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
		dialog.setCaption("Info");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		return;
	}	
	
	
	
	public void repaintBatchTable(List<PrintBulkPaymentResultDto> batchRemList){
		mainVerticalLayout.removeComponent(searchTable);
		searchTable.initTable();
		searchTable.setTableList(batchRemList);
		//searchTable.generatePrintCompletedColumn();
		//searchTable.generateExprotColumn();
		mainVerticalLayout.addComponent(searchTable);		
	}
	
	
	
	public VerticalLayout getgenerateLaout(){
		return printLetterVerticalLayout;
	}
	
	@SuppressWarnings("unused")
	private void setRequired(Boolean isRequired) {

		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				field.setRequired(isRequired);
			}
		}
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	
}
