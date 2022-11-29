/**
 * 
 */
package com.shaic.paclaim.printbulkreminder;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.arch.validation.ValidatorUtils;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.reimbursement.printReminderLetterBulk.PrintBulkReminderResultDto;
import com.shaic.reimbursement.printReminderLetterBulk.SearchPrintRemainderBulkFormDTO;
import com.shaic.reimbursement.printReminderLetterBulk.SearchPrintReminderBulkTableDTO;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;

/**
 * 
 *
 */
public class SearchPAPrintRemainderBulkViewImpl extends AbstractMVPView implements SearchPAPrintRemainderBulkView{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private SearchPAPrintRemainderBulkForm  searchForm;
	
	private SearchPAPrintRemainderBulkTable searchResultTable;
	
	@Inject
	private ShowPAPrintReminderLetterBulkPage showReminderLetterPage;
	
	//private List<PrintBulkReminderResultDto> prevBatchList;	
	
	private VerticalLayout mainPanel;
	
	//private SearchPrintReminderBulkTableDTO reminderLetterDto;
	
	@Inject
	private PrintPABulkReminderListExpoTable searchresultExportTable;
	
	//private Button closeButton;
//	private Button excelButton;
//	private VerticalLayout btnLayout; 
//	private VerticalLayout vLayout;
//	private HorizontalLayout buttonsLayout;
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		searchForm.init();
		searchForm.getContent();
						
		mainPanel = new VerticalLayout();
		mainPanel.addComponent(searchForm);
		
//		vLayout = new VerticalLayout(searchResultTable);
//		vLayout = new VerticalLayout();
//		addFooterButtons();
		
//		mainPanel.setSecondComponent(vLayout);
//		mainPanel.setSplitPosition(42);
		setHeight("670");
		mainPanel.setHeight("650");
		setCompositionRoot(mainPanel);
//		searchResultTable.addSearchListener(this);
		searchForm.addSearchListener(this);
				
		resetView();
	}
	
	@Override
	public void resetView() {
//		searchForm.refresh();
		
		searchForm.resetFields();
		searchForm.resetBulkReminderPrintScreen();
		
	}

	@Override
	public void doSearch() {
		SearchPrintRemainderBulkFormDTO searchDTO = searchForm.getSearchFilters();
		this.searchResultTable = searchForm.getSearchTable();
		if(searchDTO != null){
			Pageable pageable = searchResultTable.getPageable();
			searchDTO.setPageable(pageable);
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
			fireViewEvent(SearchPAPrintRemainderBulkPresenter.SEARCH_PA_BULK_PRINT_BUTTON_CLICK, searchDTO,userName,passWord);	
		}
		else{
			return;
		}
	}

	@Override
	public void resetSearchResultTableValues() {
//		this.searchResultTable = searchForm.getSearchTable();
//		searchResultTable.getPageable().setPageNumber(1);
//		searchResultTable.removeRow();
//		searchResultTable.resetTable();
//		Iterator<Component> componentIter = mainPanel.getComponentIterator();
//		while(componentIter.hasNext())
//		{
//			Component comp = (Component)componentIter.next();
//			
//			
//			
//			if(comp instanceof SearchGenerateRemainderTable)
//			{
//				((SearchGenerateRemainderTable) comp).removeRow();
//			}
//		}
	
		
	}

	@Override
	public void list(Page<PrintBulkReminderResultDto> tableRows) {
		this.searchResultTable = searchForm.getSearchTable();
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchResultTable.setTableList(tableRows);
			searchResultTable.tablesize();
			searchResultTable.setHasNextPage(tableRows.isHasNext());
		}
//		else
//		{
//			
//			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
//			Button homeButton = new Button("Generate Reminder Letter Home");
//			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
//			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
//			layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
//			layout.setSpacing(true);
//			layout.setMargin(true);
//			HorizontalLayout hLayout = new HorizontalLayout(layout);
//			hLayout.setMargin(true);
//			
//			final ConfirmDialog dialog = new ConfirmDialog();
//			dialog.setCaption("");
//			dialog.setClosable(false);
//			dialog.setContent(hLayout);
//			dialog.setResizable(false);
//			dialog.setModal(true);
//			dialog.show(getUI().getCurrent(), null, true);
//			
//			homeButton.addClickListener(new ClickListener() {
//				private static final long serialVersionUID = 7396240433865727954L;
//
//				@Override
//				public void buttonClick(ClickEvent event) {
//					dialog.close();
//					fireViewEvent(MenuItemBean.GENERATE_REMINDER_LETTER_BULK, null);
//					
//				}
//			});
//		}
		
	}

	@Override
	public void init(Map<String, Object> parameter) {
		//prevBatchList = prevBatchList = parameter.containsKey("prevBatchList") && parameter.get("prevBatchList") != null ? (List<PrintBulkReminderResultDto>)parameter.get("prevBatchList") : new ArrayList<PrintBulkReminderResultDto>();
		searchForm.setDropDownValues(parameter);
		
	}
	
//	public void populateBatchReminderList(List<BulkReminderResultDto> resultListDto){
//		resultListDto.addAll(prevBatchList);
////		searchResultTable.setTableList(resultListDto);		
//		searchForm.repaintBatchTable(prevBatchList);
//	}
	
	public void generateBulkPdfReminderLetter(String fileUrl,PrintBulkReminderResultDto bulkReminderDto)
	{
		if(!ValidatorUtils.isNull(fileUrl))
		{
			generateBulkReminderLetterPDF(fileUrl,bulkReminderDto);
			
		}
		else
		{
			//Exception while PDF Letter Generation
		}	
		
	}
	
	public void generateBulkReminderLetterPDF(String fileUrl, PrintBulkReminderResultDto bulkReminderDto){

		
		final Window window = new Window();
		// ((VerticalLayout) window.getContent()).setSizeFull();
		window.setResizable(true);
		window.setCaption("Reminder Letter PDF");
		window.setWidth("800");
		window.setHeight("600");
		window.setModal(true);
		window.setClosable(true);
		window.center();
		window.addCloseListener(new Window.CloseListener() {
			@Override
			public void windowClose(CloseEvent e) {
				window.close();
			}
		});
		VerticalLayout letterLayout = new VerticalLayout();
		letterLayout.setSizeFull();
		showReminderLetterPage.initView(this, fileUrl,bulkReminderDto); 
		bulkReminderDto.setPrint("Y");
		bulkReminderDto.setStatus("Completed");
		letterLayout.addComponent(showReminderLetterPage);
		window.setContent(letterLayout);
		UI.getCurrent().addWindow(window);
		
//		if( ! prevBatchList.contains(bulkReminderDto)){
//			prevBatchList.add(bulkReminderDto);	
//		}
		
//		loadBulkReminderSearchTable(prevBatchList);
		
	}
		
	@Override
	public void submitReminderLetterBulkReminderResultDto(PrintBulkReminderResultDto bulkReminderDto){
		Collection<Window> windows = UI.getCurrent().getWindows();
		for (Window window : windows) {
			window.close();
		}
//		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
//		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
//		reminderLetterDto.setUsername(userName);
//		reminderLetterDto.setPassword(passWord);
//		fireViewEvent(SearchPrintRemainderBulkPresenter.SUBMIT_BULK_PRINT_LETTER, bulkReminderDto);
	}
	
	
	@Override
	public void clearReminderLetterSearch() {
		searchForm.resetFields();
//		clearSearchForm();
		resetSearchResultTableValues();
	}

//	public void addFooterButtons(){
//		
//		buttonsLayout = new HorizontalLayout();
//		
//		closeButton = new Button("Close");
//		closeButton.addClickListener(new Button.ClickListener() {
//			
//			@Override
//			public void buttonClick(ClickEvent event) {
//				
//				fireViewEvent(SearchGenerateRemainderBulkPresenter.CLEAR_BULK_SEARCH_FORM,null);
//				fireViewEvent(MenuItemBean.GENERATE_REMINDER_LETTER_CLAIM_WISE,null);				
//			}
//		});
//		
//	}
	
//	public void clearSearchForm(){
//		
//		if(btnLayout != null){
//			btnLayout.removeAllComponents();
//			vLayout.removeComponent(btnLayout);			
//		}
//	}
	
	
	@Override
	public void resetReminderLetterSearch() {

		searchForm.resetFields();
//		clearSearchForm();
		resetSearchResultTableValues();
	}

	@Override
	public void loadResultLayout(PrintBulkReminderResultDto bulkReminderResultDto) {
		searchForm.enableButtons();
		
	}

	@Override
	public void loadBulkReminderSearchTable(
			List<PrintBulkReminderResultDto> bulkReminderResultDto) {
		
		if(bulkReminderResultDto != null && ! bulkReminderResultDto.isEmpty()){
			searchForm.repaintBatchTable(bulkReminderResultDto);
		}
		else{
			
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			VerticalLayout layout = new VerticalLayout(successLabel);
			layout.setSpacing(true);
			layout.setMargin(true);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.setWidth("10%");
			dialog.show(getUI().getCurrent(), null, true);
		}
	}

	@Override
	public void exportToExcelReminderList(PrintBulkReminderResultDto bulkReminderDto) {
		
//		searchForm.exportList(bulkReminderDto);
		
		List<SearchPrintReminderBulkTableDTO> eportList = bulkReminderDto.getResultListDto();
		searchresultExportTable.init("", false, false);
		searchForm.getgenerateLaout().addComponent(searchresultExportTable);
		searchresultExportTable.setVisible(false);
		if(eportList != null && !eportList.isEmpty()){
			
			searchresultExportTable.setTableList(eportList);
			ExcelExport excelExport = new ExcelExport(searchresultExportTable.getTable());
			excelExport.setReportTitle("Bulk Reminder List");
			excelExport.setDisplayTotals(false);
			excelExport.export();
		}
		
	}

}
