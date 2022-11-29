package com.shaic.claim.bulkconvertreimb.search;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAFileUtils;
import com.shaic.domain.MasterService;
import com.shaic.ims.bpm.claim.DBCalculationService;

@ViewInterface(SearchBulkConvertReimbView.class)
public class SearchBulkConvertReimbPresenter extends
		AbstractMVPPresenter<SearchBulkConvertReimbView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private SearchBulkConvertReimbService searchConvertClaimService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private DBCalculationService dbCalService;
	
	@Inject
	private SearchBulkConvertReimbTable exportTable;
	
	public static final String GET_PREV_CONVERTED_BATCH_LIST = "GetPrevConvertedBatchList";
	
	public static final String SUBMIT_CONVERT_BUTTON_CLICK = "SubmitConvertButtonClick";
	
	public static final String SEARCH_BATCH_IINTIMATION_BUTTON_CLICK = "SearchBatchIntimationClick";
	
	public static final String GET_CONVERT_TASK_BUTTON_CLICK = "GetConvertClaimTaskBtnClick";

	public static final String EXPORT_BULK_CONVERT_LIST = "ExportBulkConvertList";
	
	public static final String RESET_SEARCH_BULK_CONVERT_FORM = "Reset Searh Bulk Convert";
	
	public static final String SHOW_BULK_COVERING_LETTER = "ShowBulkCoveringLetter";
	
	public static final String SUBMIT_BULK_COVERING_LETTER = "SubmitBulkCoveringLetter";
		
	public void searchClick(
			@Observes @CDIEvent(GET_CONVERT_TASK_BUTTON_CLICK) final ParameterDTO parameters) {
		SearchBulkConvertFormDto searchFormDTO = (SearchBulkConvertFormDto) parameters
				.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		List<SearchBulkConvertReimbTableDto> resultConversionList = searchConvertClaimService.search(searchFormDTO,userName,passWord);
		
		view.list(resultConversionList);
	}
	
	public void convertClaimClick(
			@Observes @CDIEvent(SUBMIT_CONVERT_BUTTON_CLICK) final ParameterDTO parameters) {
		
		List<SearchBulkConvertReimbTableDto> convertDtoList = (List<SearchBulkConvertReimbTableDto>)parameters.getPrimaryParameter();
		int selecteditems = 0;
		if(convertDtoList != null && !convertDtoList.isEmpty()){
			for (SearchBulkConvertReimbTableDto searchBulkConvertReimbTableDto : convertDtoList) {
				
				if(searchBulkConvertReimbTableDto.getSelected()){
					selecteditems++;
				}
			}
			if(selecteditems > 0){
				SearchBatchConvertedTableDto bulkConvertedClaimDto = searchConvertClaimService.submitConvertClaimTask(convertDtoList, dbCalService);
				view.showSuccesslayout(bulkConvertedClaimDto);
			}
		}
		else{
			
			view.loadSearchResultLayout("No Claim Intimation available for Conversion,<br>Please Select at least one Claim intimation for Conversion.");
		}		
	}
	
	public void printBulkLetterClick(
			@Observes @CDIEvent(SHOW_BULK_COVERING_LETTER) final ParameterDTO parameters) 	{
		
		SearchBatchConvertedTableDto bulkConvertClaimDto = (SearchBatchConvertedTableDto)parameters.getPrimaryParameter();
		
		List<SearchBulkConvertReimbTableDto> LetterDtoList = bulkConvertClaimDto.getExportList();
		Path tempDir = SHAFileUtils.createTempDirectory(bulkConvertClaimDto.getCrNo());
		List<File> filelistForMerge = new ArrayList<File>();
		
		for(SearchBulkConvertReimbTableDto  convertClaimLetterDto :LetterDtoList){
			if(convertClaimLetterDto.getDocToken() != null){	
				String fileUrl = masterService.getDocumentURLByToken(Long.valueOf(convertClaimLetterDto.getDocToken()));
				
				String[] fileNameSplit = fileUrl.split(".");
				String fileName = fileNameSplit.length > 0 ? fileNameSplit[0] :"";
				
				String fileviewUrl = SHAFileUtils.viewFileByToken(String.valueOf(convertClaimLetterDto.getDocToken()));
			
				File tmpFile = SHAFileUtils.downloadFileForCombinedView(fileName,fileviewUrl,tempDir);
				filelistForMerge.add(tmpFile);
			}
		}	
		
		File mergedDoc = SHAFileUtils.mergeDocuments(filelistForMerge,tempDir,bulkConvertClaimDto.getCrNo());
	
		String mergedFileUrl = mergedDoc.getAbsolutePath(); 
		
		view.generateBulkPdfCoveringLetter(mergedFileUrl,bulkConvertClaimDto); 		
	}
	
	public void getPrevConvertedBatch(
			@Observes @CDIEvent(GET_PREV_CONVERTED_BATCH_LIST) final ParameterDTO parameters) {
	
		List<SearchBatchConvertedTableDto> prevConvertedList = searchConvertClaimService.getPreviousBatchList();
		view.repaintConvertedBatchTable(prevConvertedList);
	}
	
	public void searchBatchCoveringLetter(
			@Observes @CDIEvent(SEARCH_BATCH_IINTIMATION_BUTTON_CLICK) final ParameterDTO parameters) {
		
		Map<String,String> searchfields = (HashMap<String, String>)parameters.getPrimaryParameter();
		List<SearchBatchConvertedTableDto> convertedList = searchConvertClaimService.searchByCRNoIntimationNo(searchfields); 
		view.repaintConvertedBatchTable(convertedList);		
	}

	@Override
	public void viewEntered() {

	}
	
	protected void submitBulkCoveringLetter(
			@Observes @CDIEvent(SUBMIT_BULK_COVERING_LETTER) final ParameterDTO parameters) {
		
		
//		List<SearchBatchConvertedTableDto> bulkTableListDto = (List<SearchBatchConvertedTableDto>)parameters.getPrimaryParameter();
//		searchConvertClaimService.submitBulkLetter(bulkTableListDto);
		
		
		SearchBatchConvertedTableDto bulkTableDto = (SearchBatchConvertedTableDto)parameters.getPrimaryParameter();
		searchConvertClaimService.submitBulkLetter(bulkTableDto);
		view.repaintPrintStatusOfBatchTable();
		
//		List<SearchBatchConvertedTableDto> bulkTableListDto = new ArrayList<SearchBatchConvertedTableDto>();		
//		view.repaintConvertedBatchTable(bulkTableListDto);
	}
	
	protected void exportConvertClaimList(
			@Observes @CDIEvent(EXPORT_BULK_CONVERT_LIST) final ParameterDTO parameters) {
		
		SearchBatchConvertedTableDto bulkCoveringLetterDto = (SearchBatchConvertedTableDto)parameters.getPrimaryParameter();
		List<SearchBulkConvertReimbTableDto> convertedList = bulkCoveringLetterDto.getExportList();
				
		if( convertedList != null && ! convertedList.isEmpty() )
		view.exportConvertedListToExcel(convertedList);		
	}

}
