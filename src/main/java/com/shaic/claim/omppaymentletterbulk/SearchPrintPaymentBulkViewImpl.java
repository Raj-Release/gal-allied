package com.shaic.claim.omppaymentletterbulk;

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
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;


public class SearchPrintPaymentBulkViewImpl extends AbstractMVPView implements SearchPrintPaymentBulkView {
	
	@Inject
	private SearchPrintPaymentBulkForm  searchForm;
	
	private SearchPrintPaymentBulkTable searchResultTable;
	
	@Inject
	private ShowPrintPaymentLetterBulkPage showPaymentLetterPage;
	
	//private List<PrintBulkReminderResultDto> prevBatchList;	
	
	private VerticalLayout mainPanel;
	
	//private SearchPrintReminderBulkTableDTO reminderLetterDto;
	
	@Inject
	private PrintBulkPaymentListExpoTable searchresultExportTable;
	
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
	public void doSearch() {
		SearchPrintPaymentBulkFormDTO searchDTO = searchForm.getSearchFilters();
		this.searchResultTable = searchForm.getSearchTable();
		if(searchDTO != null){
			Pageable pageable = searchResultTable.getPageable();
			searchDTO.setPageable(pageable);
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
			fireViewEvent(SearchPrintPaymentBulkPresenter.PAYMENT_BULK_PRINT_BUTTON_CLICK, searchDTO,userName,passWord);	
		}
		else{
			return;
		}
	}

	
	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		searchForm.resetFields();
		searchForm.resetBulkPaymentPrintScreen();
	}


	@Override
	public void loadBulkPaymentSearchTable(List<PrintBulkPaymentResultDto> bulkReminderResultDto) {
		
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
	public void generateBulkPdfPaymentLetter(String fileUrl,
			PrintBulkPaymentResultDto bulkReminderDto) {
		if(!ValidatorUtils.isNull(fileUrl))
		{
			generateBulkReminderLetterPDF(fileUrl,bulkReminderDto);
			
		}
		else
		{
			//Exception while PDF Letter Generation
		}	
		
	}
	
	public void generateBulkReminderLetterPDF(String fileUrl, PrintBulkPaymentResultDto bulkReminderDto){

		
		final Window window = new Window();
		window.setResizable(true);
		window.setCaption("Payment Letter PDF");
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
		showPaymentLetterPage.initView(this, fileUrl,bulkReminderDto); 
		/*bulkReminderDto.setPrint("Y");
		bulkReminderDto.setStatus("Completed");*/
		letterLayout.addComponent(showPaymentLetterPage);
		window.setContent(letterLayout);
		UI.getCurrent().addWindow(window);
	
	}

	@Override
	public void resetSearchResultTableValues() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showErrorMsg(String msg) {

		Label successLabel = new Label(msg, ContentMode.HTML);			
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
