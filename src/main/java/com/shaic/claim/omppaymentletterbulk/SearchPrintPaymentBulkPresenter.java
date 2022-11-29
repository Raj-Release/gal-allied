package com.shaic.claim.omppaymentletterbulk;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.arch.SHAFileUtils;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.printReminderLetterBulk.PrintBulkReminderResultDto;
import com.shaic.reimbursement.printReminderLetterBulk.SearchPrintRemainderBulkFormDTO;
import com.shaic.reimbursement.printReminderLetterBulk.SearchPrintRemainderBulkService;
import com.vaadin.server.VaadinSession;


	
@ViewInterface(SearchPrintPaymentBulkView.class)
public class SearchPrintPaymentBulkPresenter extends AbstractMVPPresenter<SearchPrintPaymentBulkView>{
	
	
	public static final String PRINT_PAYMENT_BULK_LETTER = "Print Bulk Payment Letter";
	public static final String PAYMENT_BULK_PRINT_BUTTON_CLICK = "Search Payment Bulk ";
	
	@EJB
	private SearchPrintPaymentBulkService searchService;
	
	@EJB
	private IntimationService intimationService;

	@EJB
	private DBCalculationService dbCalService;
	
	@EJB
	private MasterService masterService;

	/*protected void submitReminderLetter(
			@Observes @CDIEvent(SUBMIT_BULK_PRINT_LETTER) final ParameterDTO parameters) {
		
		PrintBulkReminderResultDto reminderLetterDto = (PrintBulkReminderResultDto )parameters.getPrimaryParameter();
//		TODO
		*//**
		 * 		Needs to be done when print button is clicked in table.
		 *//*
//		reminderLetterDto.setPrintCount(reminderLetterDto.getPrintCount() != null ? (reminderLetterDto.getPrintCount() + 1 ) : 1);
//		reminderLetterDto.setPrint("Y");
		String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		reminderLetterDto.setUserName(userId);
		
		
		
		searchService.submitBulkLetter(reminderLetterDto);
		
		
		
		
		
	}*/
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearchOMP(@Observes @CDIEvent(PAYMENT_BULK_PRINT_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchPrintPaymentBulkFormDTO searchFormDTO = (SearchPrintPaymentBulkFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		searchFormDTO.setUsername(userName);
		searchFormDTO.setPassword(passWord);
		List<PrintBulkPaymentResultDto>  searchResutListDto = searchService.search(searchFormDTO);
					
		view.loadBulkPaymentSearchTable(searchResutListDto);
		
	}
	
	protected void generateAndUploadPaymentLetter(
			@Observes @CDIEvent(PRINT_PAYMENT_BULK_LETTER) final ParameterDTO parameters) {

		PrintBulkPaymentResultDto bulkPaymentDTO = (PrintBulkPaymentResultDto) parameters.getPrimaryParameter();
		List<File> filelistForMerge = new ArrayList<File>();
		if (bulkPaymentDTO != null) {
			Path tempDir = SHAFileUtils.createTempDirectory("OMPPaymentLetter");

			if ( bulkPaymentDTO.getDocTokenList() != null ||  !(bulkPaymentDTO.getDocTokenList().isEmpty())){

				for (Long docToken : bulkPaymentDTO.getDocTokenList()) {

					String fileUrl = searchService.getDocumentURLByToken(docToken);

					String[] fileNameSplit = fileUrl.split("\\.");
					String fileName = fileNameSplit.length > 0 ? fileNameSplit[0]
							: "";

					String fileviewUrl = SHAFileUtils.viewFileByToken(String
							.valueOf(docToken));

					if (fileviewUrl != null && !fileName.isEmpty()) {
						File tmpFile = SHAFileUtils.downloadFileForCombinedView(fileName,fileviewUrl, tempDir);
						
						filelistForMerge.add(tmpFile);

						
						/*if (tmpFile != null) {
							String mergedFileUrl = tmpFile.getAbsolutePath();

							if (mergedFileUrl != null && !mergedFileUrl.isEmpty()) {
								view.generateBulkPdfReminderLetter(mergedFileUrl, bulkPaymentDTO);
							}
						} else {
							view.showErrorMsg("File Not Available for the Batch No : ");
						}*/
					}
				}
					File mergedDoc = SHAFileUtils.mergeDocuments(filelistForMerge,tempDir,"OMPPaymentLetter");
					
					if(mergedDoc != null){
						String mergedFileUrl = mergedDoc.getAbsolutePath();
						if (mergedFileUrl != null && !mergedFileUrl.isEmpty()) {
							view.generateBulkPdfPaymentLetter(mergedFileUrl, bulkPaymentDTO);
						}
					}
					else {
						view.showErrorMsg("File Not Available for the Batch No : ");
					}

				}
		}
	}
	}

