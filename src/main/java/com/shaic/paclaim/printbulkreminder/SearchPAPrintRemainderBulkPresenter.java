/**
 * 
 */
package com.shaic.paclaim.printbulkreminder;

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
import com.shaic.reimbursement.printReminderLetterBulk.PrintBulkReminderResultDto;
import com.shaic.reimbursement.printReminderLetterBulk.SearchPrintRemainderBulkFormDTO;
import com.shaic.reimbursement.printReminderLetterBulk.SearchPrintReminderBulkTableDTO;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.v7.ui.themes.Reindeer;



/**
 * 
 *
 */

@ViewInterface(SearchPAPrintRemainderBulkView.class)
public class SearchPAPrintRemainderBulkPresenter extends AbstractMVPPresenter<SearchPAPrintRemainderBulkView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_PA_BULK_PRINT_BUTTON_CLICK = "doPASearchbulkprintTable";
	public static final String CLEAR_PA_BULK_PRINT_SEARCH_FORM = "Clear PA Bulk print Search Form";
	public static final String RESET_PA_SEARCH_BULK_PRINT_FORM = "Reset PA Bulk print Search Form";
	public static final String PRINT_PA_BULK_LETTER = "Print PA Bulk Reminder Letter";
	public static final String COMPLETED_PA_BUTTON_CLICK = "Print PA Completed Link Click";
	public static final String SUBMIT_PA_BULK_PRINT_LETTER = "Submit PA Bulk Print Letter task to BPM";
	public static final String SEARCH_PA_BULK_PRINT_BATCH_ID_BUTTON_CLICK = "SearchPABulkPrintByBatchIdIntimation";
	public static final String GET_PA_BULK_PRINT_PREV_BATCH_LIST = "doPAPrevBatchbulkPrintSearch";	
	public static final String EXPORT_PA_BULK_PRINT_REMINDER_LIST = "Export PA bulk print reminder list";
	
	
	
	@EJB
	private SearchPAPrintRemainderBulkService searchService;
	
	@EJB
	private IntimationService intimationService;

	@EJB
	private DBCalculationService dbCalService;
	
	@EJB
	private MasterService masterService;

	protected void submitReminderLetter(
			@Observes @CDIEvent(SUBMIT_PA_BULK_PRINT_LETTER) final ParameterDTO parameters) {
		
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
	
	protected void getPrevBatchReminderDetails(
			@Observes @CDIEvent(GET_PA_BULK_PRINT_PREV_BATCH_LIST) final ParameterDTO parameters) {
		List<PrintBulkReminderResultDto> prevRemindBatchList = searchService.searchPrevBatch();
		view.loadBulkReminderSearchTable(prevRemindBatchList);
	}
	
	
	protected void exportReminderDetailsToExcel(
			@Observes @CDIEvent(EXPORT_PA_BULK_PRINT_REMINDER_LIST) final ParameterDTO parameters) {	

		PrintBulkReminderResultDto bulkReminderDto = (PrintBulkReminderResultDto)parameters.getPrimaryParameter();
		 List<SearchPrintReminderBulkTableDTO> exportTableList = searchService.populateExportExcelList(bulkReminderDto);
		 bulkReminderDto.setResultListDto(exportTableList);
		view.exportToExcelReminderList(bulkReminderDto);
	}
	
	protected void generateAndUploadReminderLetter(
			@Observes @CDIEvent(PRINT_PA_BULK_LETTER) final ParameterDTO parameters) {
		
		PrintBulkReminderResultDto bulkReminderDto = (PrintBulkReminderResultDto)parameters.getPrimaryParameter();
		if(bulkReminderDto != null){ 
			Path tempDir = SHAFileUtils.createTempDirectory(bulkReminderDto.getBatchid());
			
				if(bulkReminderDto.getDocToken() != null){	
					
					String fileUrl = masterService.getDocumentURLByToken(bulkReminderDto.getDocToken());
					
					String[] fileNameSplit = fileUrl.split("\\.");
					String fileName = fileNameSplit.length > 0 ? fileNameSplit[0] :"";
					
					String fileviewUrl = SHAFileUtils.viewFileByToken(String.valueOf(bulkReminderDto.getDocToken()));
				
					if(fileviewUrl != null && !fileName.isEmpty()){
						File tmpFile = SHAFileUtils.downloadFileForCombinedView(fileName,fileviewUrl,tempDir);
					
						String mergedFileUrl = tmpFile.getAbsolutePath(); 
				
						if(mergedFileUrl != null && !mergedFileUrl.isEmpty()){
							view.generateBulkPdfReminderLetter(mergedFileUrl,bulkReminderDto);
						}
					}
				}	
		}
	}
	
	protected void completedReminderLetterLinkClick(
			@Observes @CDIEvent(COMPLETED_PA_BUTTON_CLICK) final ParameterDTO parameters) {
		
		PrintBulkReminderResultDto reminderLetterDto = (PrintBulkReminderResultDto )parameters.getPrimaryParameter();
		/**
		 * 		Needs to be done when print button is clicked in table.
		 */
		String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		reminderLetterDto.setUserName(userId);
		ConfirmDialog dialog = new ConfirmDialog();
		
		StringBuffer content = new StringBuffer();
		content.append(" Batch Id : ").append(reminderLetterDto.getBatchid());
		content.append(" ,   No. of Records : ").append(reminderLetterDto.getTotalNoofRecords());
		
		dialog = ConfirmDialog.show(UI.getCurrent(), "Are you sure The Printing is Completed For the Batch :",
				content.toString(),
				"No", "Yes", new ConfirmDialog.Listener() {
			         
					private static final long serialVersionUID = 1L;
                      
					public void onClose(ConfirmDialog dialog) {
			
						if (dialog.isCanceled() && !dialog.isConfirmed()) {
							
							PrintBulkReminderResultDto reminderDto = (PrintBulkReminderResultDto) dialog.getData();
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
		
	}
	
	protected void clearSearchReminderLetterForm(
			@Observes @CDIEvent(CLEAR_PA_BULK_PRINT_SEARCH_FORM) final ParameterDTO parameters) {
		
		view.clearReminderLetterSearch();
	}
	
	protected void resetSearchReminderLetterForm(
			@Observes @CDIEvent(RESET_PA_SEARCH_BULK_PRINT_FORM) final ParameterDTO parameters) {
		
		view.resetReminderLetterSearch();
	}
	
	@SuppressWarnings({ "deprecation" })
	public void searchByBatchIdIntimationNo(@Observes @CDIEvent(SEARCH_PA_BULK_PRINT_BATCH_ID_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchPrintRemainderBulkFormDTO searchFormDTO = (SearchPrintRemainderBulkFormDTO) parameters.getPrimaryParameter();
		
		List<PrintBulkReminderResultDto> bulkReminderResultDto = searchService.search(searchFormDTO);
		
		view.loadBulkReminderSearchTable(bulkReminderResultDto);
				
	}	
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_PA_BULK_PRINT_BUTTON_CLICK) final ParameterDTO parameters) {
		
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
