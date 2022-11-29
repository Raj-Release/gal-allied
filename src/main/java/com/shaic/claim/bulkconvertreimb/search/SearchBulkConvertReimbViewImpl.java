package com.shaic.claim.bulkconvertreimb.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Searchable;
import com.shaic.arch.validation.ValidatorUtils;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class SearchBulkConvertReimbViewImpl  extends AbstractMVPView implements SearchBulkConvertReimbView, Searchable {	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject 
	private ShowBulkCoveringLetterPage showBulkCoveringLetterPage;
	
	@Inject
	private SearchBulkConvertReimbPage searchForm;
	
	@Inject
	private SearchBulkConvertReimbTable searchResultTable;
	
	private VerticalLayout mainPanel = new VerticalLayout();
	
	@PostConstruct
	protected void init() {
		
		addStyleName("view");
		setSizeFull();
		searchForm.addSearchListener(this);
		searchForm.init();
		
		searchResultTable.addStyleName((ValoTheme.TABLE_COMPACT));

		mainPanel.addComponent(searchForm);
		
		mainPanel.setWidth("100.0%");

		setHeight("100.0%");
		setHeight("600px");
		setCompositionRoot(mainPanel);		
		
		resetView();
		
	}
	
	@Override
	public void resetView() {
		System.out.println("---tinside the reset view");
		
		searchForm.refresh(); 
	}
	
	@Override
	public void list(List<SearchBulkConvertReimbTableDto> tableRows) {
		if(null != tableRows && !tableRows.isEmpty())
		{	
			searchResultTable.setTableList(tableRows);
			searchForm.repaintSearchConvertTable(tableRows);
		}
		else
		{
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Convert Claim to Reimbursement (Bulk) Home");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setContent(hLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					fireViewEvent(MenuItemBean.CONVERT_CLAIM_BULK, null);
					
				}
			});
			
		}
		
	}

	@Override
	public void doSearch() {
		SearchBulkConvertFormDto searchDTO = searchForm.getSearchDTO();
		
		if(searchDTO != null){
		
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
			
			fireViewEvent(SearchBulkConvertReimbPresenter.GET_CONVERT_TASK_BUTTON_CLICK, searchDTO,userName,passWord);
		}
		else{
			searchForm.showErrorMsg("Please Select atleaset one CPU Code for Search.");
		}
		
	}

	@Override
	public void init(BeanItemContainer<SelectValue> cpuParam,BeanItemContainer<SelectValue> typeValueParam) {
		// TODO Auto-generated method stub
		searchForm.setDropDownValues(cpuParam,typeValueParam);
		
	}
	@Override
	public void resetSearchResultTableValues() {
		
		
		
	}

	@Override
	public void setPrevBatchConvertList(
			List<SearchBatchConvertedTableDto> prevBatchList) {
		searchForm.setPreviousBatchList(prevBatchList);
	}

	@Override
	public void repaintConvertedBatchTable(
			List<SearchBatchConvertedTableDto> prevBatchList) {
		searchForm.repaintBulkConvertedTable(prevBatchList);
	}

	@Override
	public void generateBulkPdfCoveringLetter(String fileUrl,SearchBatchConvertedTableDto bulkConvertDto)
	{
		if(!ValidatorUtils.isNull(fileUrl))
		{
			generateBulkCoveringLetterPDF(fileUrl,bulkConvertDto);
			
		}
		else
		{
			//Exception while PDF Letter Generation
		}	
		
	}
	public void generateBulkCoveringLetterPDF(String fileUrl, SearchBatchConvertedTableDto bulkConvertClaimDto){

		
		final Window window = new Window();
		// ((VerticalLayout) window.getContent()).setSizeFull();
		window.setResizable(true);
		window.setCaption("Covering Letter PDF");
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
		showBulkCoveringLetterPage.initView(this, fileUrl,bulkConvertClaimDto); 
		bulkConvertClaimDto.setPrintTaken("Y");
		bulkConvertClaimDto.setStatus("Completed");
		letterLayout.addComponent(showBulkCoveringLetterPage);
		window.setContent(letterLayout);
		UI.getCurrent().addWindow(window);
	}
	
	@Override
	public void submitBulkCoveringLetter(SearchBatchConvertedTableDto bulkConvertClaimDto){
		Collection<Window> windows = UI.getCurrent().getWindows();
		for (Window window : windows) {
			window.close();
		}
//		List<SearchBatchConvertedTableDto> bulkTableList = searchForm.getBulkTableList();
//		fireViewEvent(SearchBulkConvertReimbPresenter.SUBMIT_BULK_COVERING_LETTER, bulkTableList);
		fireViewEvent(SearchBulkConvertReimbPresenter.SUBMIT_BULK_COVERING_LETTER,bulkConvertClaimDto);
	}
	
	@Override
	public void loadSearchResultLayout(String resultMsg){
		Label successLabel = new Label(resultMsg, ContentMode.HTML);			
		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(true);
		dialog.setContent(successLabel);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}
	
	@Override
	public void showSuccesslayout(SearchBatchConvertedTableDto bulkConvertClaimDto){
		
		List<SearchBulkConvertReimbTableDto> convertedList =  bulkConvertClaimDto.getExportList();
		int count = 0;
		StringBuffer result = new StringBuffer();
		
		if(convertedList != null && ! convertedList.isEmpty()){
			for (SearchBulkConvertReimbTableDto searchBulkConvertReimbTableDto : convertedList) {
				if(searchBulkConvertReimbTableDto.getSelected()){
					count++;
				}				
			} 
			result.append("Intimations Converted Successfully !!!			<br>")
					.append("Letter will be Generated Shortly			 <br>  ")
					.append("CR No: " + bulkConvertClaimDto.getCrNo())
					.append("			<br>").append("Total Records : ").append(count)
					.append("		<br>");			
		}
		else{
			result.append("Error has Occured while Intimation Conversion !!!			<br>");
		}		 
		
		loadSearchResultLayout(result.toString());
	}

	@Override
	public void exportConvertedListToExcel(
			List<SearchBulkConvertReimbTableDto> finalconvertedList) {
		searchResultTable.setTableList(finalconvertedList);
		searchResultTable.setExportExcelTableHeader();
		ExcelExport export = new ExcelExport(searchResultTable.getTable());
		export.setReportTitle("Bulk Convert Claim To Reimbursement List");
		export.setDisplayTotals(false);
		export.export();		
	}
	
	@Override
	public List<SearchBatchConvertedTableDto> getBulkTableList(){
		return searchForm.getBulkTableList();
	}

	@Override
	public void repaintPrintStatusOfBatchTable() {
		List<SearchBatchConvertedTableDto> currList = searchForm.getBulkTableList();
		List<SearchBatchConvertedTableDto> newList = new  ArrayList<SearchBatchConvertedTableDto>();
		newList.addAll(currList);
		searchForm.repaintBulkConvertedTable(currList);
	}
}
