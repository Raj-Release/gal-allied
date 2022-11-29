/**
 * 
 */
package com.shaic.reimbursement.printReminderLetterBulk;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAFileUtils;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.v7.ui.themes.Reindeer;



/**
 * 
 *
 */

@ViewInterface(SearchPrintRemainderBulkView.class)
public class SearchPrintRemainderBulkPresenter extends AbstractMVPPresenter<SearchPrintRemainderBulkView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BULK_PRINT_BUTTON_CLICK = "doSearchbulkprintTable";
	public static final String CLEAR_BULK_PRINT_SEARCH_FORM = "Clear Bulk print Search Form";
	public static final String RESET_SEARCH_BULK_PRINT_FORM = "Reset Bulk print Search Form";
	public static final String PRINT_BULK_LETTER = "Print Bulk Reminder Letter";
	public static final String COMPLETED_BUTTON_CLICK = "Print Completed Link Click";
	public static final String SUBMIT_BULK_PRINT_LETTER = "Submit Bulk Print Letter task to BPM";
	public static final String SEARCH_BULK_PRINT_BATCH_ID_BUTTON_CLICK = "SearchBulkPrintByBatchIdIntimation";
	public static final String GET_BULK_PRINT_PREV_BATCH_LIST = "doPrevBatchbulkPrintSearch";	
	public static final String EXPORT_BULK_PRINT_REMINDER_LIST = "Export bulk print reminder list";
	
	
	
	@EJB
	private SearchPrintRemainderBulkService searchService;
	
	@EJB
	private IntimationService intimationService;

	@EJB
	private DBCalculationService dbCalService;
	
	@EJB
	private MasterService masterService;

	protected void submitReminderLetter(
			@Observes @CDIEvent(SUBMIT_BULK_PRINT_LETTER) final ParameterDTO parameters) {
		
		PrintBulkReminderResultDto reminderLetterDto = (PrintBulkReminderResultDto )parameters.getPrimaryParameter();
//		TODO
		/**
		 * 		Needs to be done when print button is clicked in table.
		 */
//		reminderLetterDto.setPrintCount(reminderLetterDto.getPrintCount() != null ? (reminderLetterDto.getPrintCount() + 1 ) : 1);
//		reminderLetterDto.setPrint("Y");
		String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		reminderLetterDto.setUserName(userId);
		
		
		
		searchService.submitBulkLetter(reminderLetterDto);
		
		
		
		
		
	}
		
	protected void generateAndUploadReminderLetters(List<PrintBulkReminderResultDto> bulkReminderResultDto) {
		
		view.loadBulkReminderSearchTable(bulkReminderResultDto);	
	}
	
	/*private String generatePdfFile(SearchPrintReminderBulkTableDTO reminderLetterDto){
		ReportDto reportDto = new ReportDto();
		DocumentGenerator docGenarator = new DocumentGenerator();
		String fileUrl = "";
		
		if(reminderLetterDto != null && reminderLetterDto.getQueryKey() != null){
			ReimbursementQueryDto queryDto = reminderLetterDto.getReimbQueryDto();
			List<ReimbursementQueryDto> queryDtoList = new ArrayList<ReimbursementQueryDto>();
			queryDtoList.add(queryDto);	
			reportDto.setBeanList(queryDtoList);
			reportDto.setClaimId(queryDto.getClaimId());
			
			fileUrl = docGenarator.generatePdfDocument("ReimburseQueryReminderLetter", reportDto);
		}
		
		else if(reminderLetterDto != null && reminderLetterDto.getPreauthKey() == null && reminderLetterDto.getQueryKey() == null){
			ClaimDto claimDto = reminderLetterDto.getClaimDto();
			List<ClaimDto> claimDtoList = new ArrayList<ClaimDto>();
			claimDtoList.add(claimDto);
			reportDto.setBeanList(claimDtoList);
			reportDto.setClaimId(claimDto.getClaimId());
			
			fileUrl = docGenarator.generatePdfDocument("ReimburseClaimReminderLetter", reportDto);
			
		}
		else if(reminderLetterDto != null && reminderLetterDto.getPreauthKey() != null){
			ClaimDto claimDto = reminderLetterDto.getClaimDto();
			List<ClaimDto> claimDtoList = new ArrayList<ClaimDto>();
			claimDtoList.add(claimDto);
			reportDto.setBeanList(claimDtoList);
			reportDto.setClaimId(claimDto.getClaimId());
			
			fileUrl = docGenarator.generatePdfDocument("CashlessReminderLetter", reportDto);
			
		}
		
		return fileUrl;
	}*/
	
	protected void getPrevBatchReminderDetails(
			@Observes @CDIEvent(GET_BULK_PRINT_PREV_BATCH_LIST) final ParameterDTO parameters) {
		List<PrintBulkReminderResultDto> prevRemindBatchList = searchService.searchPrevBatch();
		view.loadBulkReminderSearchTable(prevRemindBatchList);
	}
	
	
	protected void exportReminderDetailsToExcel(
			@Observes @CDIEvent(EXPORT_BULK_PRINT_REMINDER_LIST) final ParameterDTO parameters) {	

		PrintBulkReminderResultDto bulkReminderDto = (PrintBulkReminderResultDto)parameters.getPrimaryParameter();
		 List<SearchPrintReminderBulkTableDTO> exportTableList = searchService.populateExportExcelList(bulkReminderDto);
		 bulkReminderDto.setResultListDto(exportTableList);
		view.exportToExcelReminderList(bulkReminderDto);
	}
	
	protected void generateAndUploadReminderLetter(
			@Observes @CDIEvent(PRINT_BULK_LETTER) final ParameterDTO parameters) {
		
		PrintBulkReminderResultDto bulkReminderDto = (PrintBulkReminderResultDto)parameters.getPrimaryParameter();
		if(bulkReminderDto != null){ 
			//List<SearchPrintReminderBulkTableDTO> tableDtoList = bulkReminderDto.getResultListDto();
			Path tempDir = SHAFileUtils.createTempDirectory(bulkReminderDto.getBatchid());
			//List<File> filelistForMerge = new ArrayList<File>();
			
				if(bulkReminderDto.getDocToken() != null){	
					
					String fileUrl = masterService.getDocumentURLByToken(bulkReminderDto.getDocToken());
					
					String[] fileNameSplit = fileUrl.split("\\.");
					String fileName = fileNameSplit.length > 0 ? fileNameSplit[0] :"";
					
					String fileviewUrl = SHAFileUtils.viewFileByToken(String.valueOf(bulkReminderDto.getDocToken()));
				
					if(fileviewUrl != null && !fileName.isEmpty()){
						File tmpFile = SHAFileUtils.downloadFileForCombinedView(fileName,fileviewUrl,tempDir);
					if(tmpFile != null){
						String mergedFileUrl = tmpFile.getAbsolutePath(); 
				
						if(mergedFileUrl != null && !mergedFileUrl.isEmpty()){
							view.generateBulkPdfReminderLetter(mergedFileUrl,bulkReminderDto);
						}
					}
					else{
						view.showErrorMsg("File Not Available for the Batch No : "+bulkReminderDto.getBatchid());
					}
					}
				}	
		}
	}
	
	protected void completedReminderLetterLinkClick(
			@Observes @CDIEvent(COMPLETED_BUTTON_CLICK) final ParameterDTO parameters) {
		
		PrintBulkReminderResultDto reminderLetterDto = (PrintBulkReminderResultDto )parameters.getPrimaryParameter();
		/**
		 * 		Needs to be done when print button is clicked in table.
		 */
		String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		reminderLetterDto.setUserName(userId);
		
//		Window wind = new Window("Are you sure The Printing is Completed !");
//		wind.setHeight("-1");
//		wind.setWidth("-1");
//		wind.setClosable(true);
//		wind.setModal(true);
		ConfirmDialog dialog = new ConfirmDialog();
		
		StringBuffer content = new StringBuffer();
		content.append(" Batch Id : ").append(reminderLetterDto.getBatchid());
		content.append(" ,   No. of Records : ").append(reminderLetterDto.getTotalNoofRecords());
		
//		VerticalLayout vlayout = new VerticalLayout();
//		
//		FormLayout frmLayout = new FormLayout();
//		
//		TextField batchId = new TextField("Batch Id");
//		batchId.setValue(reminderLetterDto.getBatchid());
//		TextField noofRecords = new TextField("No. of Records");
//		noofRecords.setValue(String.valueOf(reminderLetterDto.getTotalNoofRecords()));
//		noofRecords.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//		noofRecords.setEnabled(false);	
//		frmLayout.addComponent(batchId);
//		frmLayout.addComponent(noofRecords);
//		
//		vlayout.addComponent(frmLayout);
//		dialog.setContent(vlayout);
//		dialog.setContentMode(ContentMode.PREFORMATTED);
		
		
		dialog = ConfirmDialog.show(UI.getCurrent(), "Are you sure The Printing is Completed For the Batch :",
				content.toString(),
				"No", "Yes", new ConfirmDialog.Listener() {
			         
					private static final long serialVersionUID = 1L;
                      
					public void onClose(ConfirmDialog dialog) {
			
						if (dialog.isCanceled() && !dialog.isConfirmed()) {
							
							PrintBulkReminderResultDto reminderDto = (PrintBulkReminderResultDto) dialog.getData();
//							VerticalLayout vlayout = new VerticalLayout();
//							
//							FormLayout frmLayout = new FormLayout();
//							
//							TextField batchId = new TextField("Batch Id");
//							batchId.setValue(reminderDto.getBatchid());
//							TextField noofRecords = new TextField("No. of Records");
//							noofRecords.setValue(String.valueOf(reminderDto.getTotalNoofRecords()));
//							noofRecords.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//							noofRecords.setEnabled(false);	
//							frmLayout.addComponent(batchId);
//							frmLayout.addComponent(noofRecords);
//							
//							vlayout.addComponent(frmLayout);
//							dialog.setContent(vlayout);
							searchService.submitBulkLetter(reminderDto);
						}
							Collection<Window> windows = UI.getCurrent().getWindows();
							for (Window window : windows) {
								window.close();
						    }
					}
		});
		dialog.setWidth("30%");
		dialog.setHeight("14%");
		dialog.setData(reminderLetterDto);
		dialog.setStyleName(Reindeer.WINDOW_BLACK);
		
//		UI.getCurrent().addWindow(dialog);
//		view.submitReminderLetterBulkReminderResultDto(reminderLetterDto);
	}
	
	protected void clearSearchReminderLetterForm(
			@Observes @CDIEvent(CLEAR_BULK_PRINT_SEARCH_FORM) final ParameterDTO parameters) {
		
		view.clearReminderLetterSearch();
	}
	
	protected void resetSearchReminderLetterForm(
			@Observes @CDIEvent(RESET_SEARCH_BULK_PRINT_FORM) final ParameterDTO parameters) {
		
		view.resetReminderLetterSearch();
	}
	
	@SuppressWarnings({ "deprecation" })
	public void searchByBatchIdIntimationNo(@Observes @CDIEvent(SEARCH_BULK_PRINT_BATCH_ID_BUTTON_CLICK) final ParameterDTO parameters) {
		
//		Map<String,String> searchfields = (HashMap<String, String>)parameters.getPrimaryParameter();
//		List<PrintBulkReminderResultDto> bulkReminderResultDto = searchService.searchBatchByIdOrIntimation(searchfields);
		
		SearchPrintRemainderBulkFormDTO searchFormDTO = (SearchPrintRemainderBulkFormDTO) parameters.getPrimaryParameter();
		
		List<PrintBulkReminderResultDto> bulkReminderResultDto = searchService.search(searchFormDTO);
		
		view.loadBulkReminderSearchTable(bulkReminderResultDto);
				
	}	
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BULK_PRINT_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchPrintRemainderBulkFormDTO searchFormDTO = (SearchPrintRemainderBulkFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		searchFormDTO.setUsername(userName);
		searchFormDTO.setPassword(passWord);
		List<PrintBulkReminderResultDto>  searchResutListDto = searchService.search(searchFormDTO);
					
		view.loadBulkReminderSearchTable(searchResutListDto);
		
	}
	
	@Override
	public void viewEntered() {
		
		
	}

}
