/**
 * 
 */
package com.shaic.reimbursement.reminderBulkSearch;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAFileUtils;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.paclaim.printbulkreminder.SearchPAPrintRemainderBulkService;
import com.vaadin.server.VaadinSession;



/**
 * 
 *
 */

@ViewInterface(SearchGenerateRemainderBulkView.class)
public class SearchGenerateRemainderBulkPresenter extends AbstractMVPPresenter<SearchGenerateRemainderBulkView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BULK_BUTTON_CLICK = "doSearchbulkTable";
	public static final String CLEAR_BULK_SEARCH_FORM = "Clear Bulk Search Form";
	public static final String RESET_SEARCH_BULK_FORM = "Reset Bulk Search Form";
	public static final String GENERATE_BULK_LETTER = "Generate Bulk Letter";
	public static final String SUBMIT_BULK_LETTER = "Submit Bulk Letter task to BPM";
	public static final String SEARCH_BATCH_ID_BUTTON_CLICK = "SearchByBatchIdIntimation";
	public static final String GET_PREV_BATCH_LIST = "doPrevBatchSearch";	
	public static final String EXPORT_BULK_REMINDER_LIST = "Export bulk reminder list";
	
	
	
	@EJB
	private SearchGenerateRemainderBulkService searchService;
	
	@EJB
	 private SearchPAPrintRemainderBulkService paBulkRemidnerService;
	
	@EJB
	private IntimationService intimationService;

	@EJB
	private DBCalculationService dbCalService;
	
	@EJB
	private MasterService masterService;

	protected void submitReminderLetter(
			@Observes @CDIEvent(SUBMIT_BULK_LETTER) final ParameterDTO parameters) {
		
		BulkReminderResultDto reminderLetterDto = (BulkReminderResultDto )parameters.getPrimaryParameter();
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
		
	protected void generateAndUploadReminderLetters(BulkReminderResultDto bulkReminderResultDto) {
		
		List<SearchGenerateReminderBulkTableDTO> resultListDto = bulkReminderResultDto.getResultListDto();
		
//		SearchGenerateReminderBulkTableDTO reminderLetterDto = (SearchGenerateReminderBulkTableDTO )parameters.getPrimaryParameter();
		
		if(bulkReminderResultDto.getBatchid() != null && !bulkReminderResultDto.getBatchid().isEmpty() && resultListDto != null && !resultListDto.isEmpty()){
			
			for(SearchGenerateReminderBulkTableDTO reminderLetterDto : resultListDto){
				reminderLetterDto.setBatchid(bulkReminderResultDto.getBatchid());
				if(reminderLetterDto.getIntimationKey() != null){
					Intimation intimationObj = intimationService.getIntimationByKey(reminderLetterDto.getIntimationKey());
				if(intimationObj != null){	
					NewIntimationDto intimationDto = intimationService.getIntimationDto(intimationObj);
					if(reminderLetterDto.getClaimDto() != null){
						reminderLetterDto.getClaimDto().setNewIntimationDto(intimationDto);	
					}
					if(reminderLetterDto.getReimbQueryDto() != null && reminderLetterDto.getReimbQueryDto().getReimbursementDto() != null && reminderLetterDto.getReimbQueryDto().getReimbursementDto() != null && reminderLetterDto.getReimbQueryDto().getReimbursementDto().getClaimDto() != null)
					reminderLetterDto.getReimbQueryDto().getReimbursementDto().getClaimDto().setNewIntimationDto(intimationDto);
				}
				}
				String fileUrl = generatePdfFile(reminderLetterDto);
				reminderLetterDto.setFileUrl(fileUrl);	
				reminderLetterDto = searchService.submitReminderLetter(reminderLetterDto);
				
				String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
				
				bulkReminderResultDto.setUserName(userId);
				searchService.submitTransacReminderDetails(reminderLetterDto);
			}
		}
		
		else{
				// No Intimation Available for the Claim
		}
		
		view.loadResultLayout(bulkReminderResultDto);	
	}
	
	private String generatePdfFile(SearchGenerateReminderBulkTableDTO reminderLetterDto){
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
	}
	
	protected void getPrevBatchReminderDetails(
			@Observes @CDIEvent(GET_PREV_BATCH_LIST) final ParameterDTO parameters) {
		List<BulkReminderResultDto> prevRemindBatchList = searchService.searchPrevBatch();
		view.loadBulkReminderSearchTable(prevRemindBatchList);
	}
	
	
	protected void exportReminderDetailsToExcel(
			@Observes @CDIEvent(EXPORT_BULK_REMINDER_LIST) final ParameterDTO parameters) {	

		BulkReminderResultDto bulkReminderDto = (BulkReminderResultDto)parameters.getPrimaryParameter();
		view.exportToExcelReminderList(bulkReminderDto);
	}
	
	protected void generateAndUploadReminderLetter(
			@Observes @CDIEvent(GENERATE_BULK_LETTER) final ParameterDTO parameters) {
		
		BulkReminderResultDto bulkReminderDto = (BulkReminderResultDto)parameters.getPrimaryParameter();
		
			List<SearchGenerateReminderBulkTableDTO> tableDtoList = bulkReminderDto.getResultListDto();
			Path tempDir = SHAFileUtils.createTempDirectory(bulkReminderDto.getBatchid());
			List<File> filelistForMerge = new ArrayList<File>();
			
			for(SearchGenerateReminderBulkTableDTO  reminderLetterDto :tableDtoList){
				if(reminderLetterDto.getDocToken() != null){	
					String fileUrl = masterService.getDocumentURLByToken(reminderLetterDto.getDocToken());
					
					String[] fileNameSplit = fileUrl.split(".");
					String fileName = fileNameSplit.length > 0 ? fileNameSplit[0] :"";
					
					String fileviewUrl = SHAFileUtils.viewFileByToken(String.valueOf(reminderLetterDto.getDocToken()));
				
					File tmpFile = SHAFileUtils.downloadFileForCombinedView(fileName,fileviewUrl,tempDir);
					filelistForMerge.add(tmpFile);
				}
			}	
			
			File mergedDoc = SHAFileUtils.mergeDocuments(filelistForMerge,tempDir,bulkReminderDto.getBatchid());
		
			String mergedFileUrl = mergedDoc.getAbsolutePath(); 
			
			view.generateBulkPdfReminderLetter(mergedFileUrl,bulkReminderDto); 
		
	}
	
	protected void clearSearchReminderLetterForm(
			@Observes @CDIEvent(CLEAR_BULK_SEARCH_FORM) final ParameterDTO parameters) {
		
		view.clearReminderLetterSearch();
	}
	
	protected void resetSearchReminderLetterForm(
			@Observes @CDIEvent(RESET_SEARCH_BULK_FORM) final ParameterDTO parameters) {
		
		view.resetReminderLetterSearch();
	}
	
	@SuppressWarnings({ "deprecation" })
	public void searchByBatchIdIntimationNo(@Observes @CDIEvent(SEARCH_BATCH_ID_BUTTON_CLICK) final ParameterDTO parameters) {
		
		Map<String,String> searchfields = (HashMap<String, String>)parameters.getPrimaryParameter();

		List<BulkReminderResultDto> bulkReminderResultDto = searchService.searchBatchByIdOrIntimation(searchfields);
		view.loadBulkReminderSearchTable(bulkReminderResultDto);
		
	}	
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BULK_BUTTON_CLICK) final ParameterDTO parameters) {
		
//		SearchGenerateRemainderBulkFormDTO searchFormDTO = (SearchGenerateRemainderBulkFormDTO) parameters.getPrimaryParameter();
//		
//		String userName=(String)parameters.getSecondaryParameter(0, String.class);
//		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
//		
//		searchFormDTO.setUsername(userName);
//		searchFormDTO.setPassword(passWord);
//		BulkReminderResultDto  searchResutDto = searchService.search(searchFormDTO,userName,passWord,dbCalService);
//
//		if(searchResutDto != null && searchResutDto.getBatchid() != null && !searchResutDto.getBatchid().isEmpty()){
//			
//			generateAndUploadReminderLetters(searchResutDto);
//		}
//		else{
//			view.loadResultLayout(searchResutDto);
//		}
		
		/**
		 *  The above Code was commented for testing purpose and needs to be uncommented 
		 *  below search was auto generation of Bulk reminder letter as part of new       CR - R0492
		 * 
		 */
		
//		searchService.searchNGenerateBulkReminderLetter();
//		searchService.searchNGeneratePANCardReminderLetter();		
//		paBulkRemidnerService.searchNGeneratePAReminderLetterBulk();
		
	}
	
	@Override
	public void viewEntered() {
		
		
	}

}
//=======
///**
// * 
// */
//package com.shaic.reimbursement.reminderBulkSearch;
//
//import java.io.File;
//import java.nio.file.Path;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.ejb.EJB;
//import javax.enterprise.event.Observes;
//
//import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
//import org.vaadin.addon.cdimvp.CDIEvent;
//import org.vaadin.addon.cdimvp.ParameterDTO;
//import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
//
//import com.shaic.arch.SHAFileUtils;
//import com.shaic.arch.SHAUtils;
//import com.shaic.claim.ClaimDto;
//import com.shaic.claim.ReportDto;
//import com.shaic.claim.intimation.create.dto.DocumentGenerator;
//import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;
//import com.shaic.domain.Intimation;
//import com.shaic.domain.IntimationService;
//import com.shaic.domain.MasterService;
//import com.shaic.ims.bpm.claim.BPMClientContext;
//import com.shaic.ims.bpm.claim.DBCalculationService;
//import com.shaic.newcode.wizard.dto.NewIntimationDto;
//import com.shaic.reimbursement.queryrejection.draftquery.search.SearchDraftQueryLetterTableDTO;
//import com.shaic.reimbursement.queryrejection.draftrejection.search.SearchDraftRejectionLetterFormDTO;
//import com.vaadin.addon.tableexport.ExcelExport;
//import com.vaadin.server.VaadinSession;
//
//
//
///**
// * 
// *
// */
//
//@ViewInterface(SearchGenerateRemainderBulkView.class)
//public class SearchGenerateRemainderBulkPresenter extends AbstractMVPPresenter<SearchGenerateRemainderBulkView>{
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//
//	public static final String SEARCH_BULK_BUTTON_CLICK = "doSearchbulkTable";
//	public static final String CLEAR_BULK_SEARCH_FORM = "Clear Bulk Search Form";
//	public static final String RESET_SEARCH_BULK_FORM = "Reset Bulk Search Form";
//	public static final String GENERATE_BULK_LETTER = "Generate Bulk Letter";
//	public static final String SUBMIT_BULK_LETTER = "Submit Bulk Letter task to BPM";
//	public static final String SEARCH_BATCH_ID_BUTTON_CLICK = "SearchByBatchIdIntimation";
//	public static final String GET_PREV_BATCH_LIST = "doPrevBatchSearch";	
//	public static final String EXPORT_BULK_REMINDER_LIST = "Export bulk reminder list";
//	
//	
//	
//	@EJB
//	private SearchGenerateRemainderBulkService searchService;
//	
//	@EJB
//	private IntimationService intimationService;
//
//	@EJB
//	private DBCalculationService dbCalService;
//	
//	@EJB
//	private MasterService masterService;
//
//	protected void submitReminderLetter(
//			@Observes @CDIEvent(SUBMIT_BULK_LETTER) final ParameterDTO parameters) {
//		
//		BulkReminderResultDto reminderLetterDto = (BulkReminderResultDto )parameters.getPrimaryParameter();
//		/**
//		 * 		Needs to be done when print button is clicked in table.
//		 */
////		reminderLetterDto.setPrintCount(reminderLetterDto.getPrintCount() != null ? (reminderLetterDto.getPrintCount() + 1 ) : 1);
////		reminderLetterDto.setPrint("Y");
//		String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
//		
//		reminderLetterDto.setUserName(userId);
//		searchService.submitBulkLetter(reminderLetterDto);
//	}
//		
//	protected void generateAndUploadReminderLetters(BulkReminderResultDto bulkReminderResultDto) {
//		
//		List<SearchGenerateReminderBulkTableDTO> resultListDto = bulkReminderResultDto.getResultListDto();
//		
////		SearchGenerateReminderBulkTableDTO reminderLetterDto = (SearchGenerateReminderBulkTableDTO )parameters.getPrimaryParameter();
//		
//		if(bulkReminderResultDto.getBatchid() != null && !bulkReminderResultDto.getBatchid().isEmpty() && resultListDto != null && !resultListDto.isEmpty()){
//			
//			for(SearchGenerateReminderBulkTableDTO reminderLetterDto : resultListDto){
//				reminderLetterDto.setBatchid(bulkReminderResultDto.getBatchid());
//				if(reminderLetterDto.getIntimationKey() != null){
//					Intimation intimationObj = intimationService.getIntimationByKey(reminderLetterDto.getIntimationKey());
//				if(intimationObj != null){	
//					NewIntimationDto intimationDto = intimationService.getIntimationDto(intimationObj);
//					if(reminderLetterDto.getClaimDto() != null){
//						reminderLetterDto.getClaimDto().setNewIntimationDto(intimationDto);	
//					}
//					if(reminderLetterDto.getReimbQueryDto() != null && reminderLetterDto.getReimbQueryDto().getReimbursementDto() != null && reminderLetterDto.getReimbQueryDto().getReimbursementDto() != null && reminderLetterDto.getReimbQueryDto().getReimbursementDto().getClaimDto() != null)
//					reminderLetterDto.getReimbQueryDto().getReimbursementDto().getClaimDto().setNewIntimationDto(intimationDto);
//				}
//				}
//				String fileUrl = generatePdfFile(reminderLetterDto);
//				reminderLetterDto.setFileUrl(fileUrl);	
//				reminderLetterDto = searchService.submitReminderLetter(reminderLetterDto);
//				
//				String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
//				
//				bulkReminderResultDto.setUserName(userId);
//				searchService.submitReminderDetails(reminderLetterDto);
//			}
//		}
//		
//		else{
//				// No Intimation Available for the Claim
//		}
//		
//		view.loadResultLayout(bulkReminderResultDto);	
//	}
//	
//	private String generatePdfFile(SearchGenerateReminderBulkTableDTO reminderLetterDto){
//		ReportDto reportDto = new ReportDto();
//		DocumentGenerator docGenarator = new DocumentGenerator();
//		String fileUrl = "";
//		
//		if(reminderLetterDto != null && reminderLetterDto.getQueryKey() != null){
//			ReimbursementQueryDto queryDto = reminderLetterDto.getReimbQueryDto();
//			List<ReimbursementQueryDto> queryDtoList = new ArrayList<ReimbursementQueryDto>();
//			queryDtoList.add(queryDto);	
//			reportDto.setBeanList(queryDtoList);
//			reportDto.setClaimId(queryDto.getClaimId());
//			
//			fileUrl = docGenarator.generatePdfDocument("ReimburseQueryReminderLetter", reportDto);
//		}
//		
//		else if(reminderLetterDto != null && reminderLetterDto.getPreauthKey() == null && reminderLetterDto.getQueryKey() == null){
//			ClaimDto claimDto = reminderLetterDto.getClaimDto();
//			List<ClaimDto> claimDtoList = new ArrayList<ClaimDto>();
//			claimDtoList.add(claimDto);
//			reportDto.setBeanList(claimDtoList);
//			reportDto.setClaimId(claimDto.getClaimId());
//			
//			fileUrl = docGenarator.generatePdfDocument("ReimburseClaimReminderLetter", reportDto);
//			
//		}
//		else if(reminderLetterDto != null && reminderLetterDto.getPreauthKey() != null){
//			ClaimDto claimDto = reminderLetterDto.getClaimDto();
//			List<ClaimDto> claimDtoList = new ArrayList<ClaimDto>();
//			claimDtoList.add(claimDto);
//			reportDto.setBeanList(claimDtoList);
//			reportDto.setClaimId(claimDto.getClaimId());
//			
//			fileUrl = docGenarator.generatePdfDocument("CashlessReminderLetter", reportDto);
//			
//		}
//		
//		return fileUrl;
//	}
//	
//	protected void getPrevBatchReminderDetails(
//			@Observes @CDIEvent(GET_PREV_BATCH_LIST) final ParameterDTO parameters) {
//		List<BulkReminderResultDto> prevRemindBatchList = searchService.searchPrevBatch();
//		view.loadBulkReminderSearchTable(prevRemindBatchList);
//	}
//	
//	
//	protected void exportReminderDetailsToExcel(
//			@Observes @CDIEvent(EXPORT_BULK_REMINDER_LIST) final ParameterDTO parameters) {	
//
//		BulkReminderResultDto bulkReminderDto = (BulkReminderResultDto)parameters.getPrimaryParameter();
//		view.exportToExcelReminderList(bulkReminderDto);
//	}
//	
//	protected void generateAndUploadReminderLetter(
//			@Observes @CDIEvent(GENERATE_BULK_LETTER) final ParameterDTO parameters) {
//		
//		BulkReminderResultDto bulkReminderDto = (BulkReminderResultDto)parameters.getPrimaryParameter();
//		
//			List<SearchGenerateReminderBulkTableDTO> tableDtoList = bulkReminderDto.getResultListDto();
//			Path tempDir = SHAFileUtils.createTempDirectory(bulkReminderDto.getBatchid());
//			List<File> filelistForMerge = new ArrayList<File>();
//			
//			for(SearchGenerateReminderBulkTableDTO  reminderLetterDto :tableDtoList){
//				if(reminderLetterDto.getDocToken() != null){	
//					String fileUrl = masterService.getDocumentURLByToken(reminderLetterDto.getDocToken());
//					
//					String[] fileNameSplit = fileUrl.split(".");
//					String fileName = fileNameSplit.length > 0 ? fileNameSplit[0] :"";
//					
//					String fileviewUrl = SHAFileUtils.viewFileByToken(String.valueOf(reminderLetterDto.getDocToken()));
//				
//					File tmpFile = SHAFileUtils.downloadFileForCombinedView(fileName,fileviewUrl,tempDir);
//					filelistForMerge.add(tmpFile);
//				}
//			}	
//			
//			File mergedDoc = SHAFileUtils.mergeDocuments(filelistForMerge,tempDir,bulkReminderDto.getBatchid());
//		
//			String mergedFileUrl = mergedDoc.getAbsolutePath(); 
//			
//			view.generateBulkPdfReminderLetter(mergedFileUrl,bulkReminderDto); 
//		
//	}
//	
//	protected void clearSearchReminderLetterForm(
//			@Observes @CDIEvent(CLEAR_BULK_SEARCH_FORM) final ParameterDTO parameters) {
//		
//		view.clearReminderLetterSearch();
//	}
//	
//	protected void resetSearchReminderLetterForm(
//			@Observes @CDIEvent(RESET_SEARCH_BULK_FORM) final ParameterDTO parameters) {
//		
//		view.resetReminderLetterSearch();
//	}
//	
//	@SuppressWarnings({ "deprecation" })
//	public void searchByBatchIdIntimationNo(@Observes @CDIEvent(SEARCH_BATCH_ID_BUTTON_CLICK) final ParameterDTO parameters) {
//		
//		Map<String,String> searchfields = (HashMap<String, String>)parameters.getPrimaryParameter();
//
//		List<BulkReminderResultDto> bulkReminderResultDto = searchService.searchBatchByIdOrIntimation(searchfields);
//		view.loadBulkReminderSearchTable(bulkReminderResultDto);
//		
//	}	
//	
//	@SuppressWarnings({ "deprecation" })
//	public void handleSearch(@Observes @CDIEvent(SEARCH_BULK_BUTTON_CLICK) final ParameterDTO parameters) {
//		
//		SearchGenerateRemainderBulkFormDTO searchFormDTO = (SearchGenerateRemainderBulkFormDTO) parameters.getPrimaryParameter();
//		
//		String userName=(String)parameters.getSecondaryParameter(0, String.class);
//		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
//		
//		searchFormDTO.setUsername(userName);
//		searchFormDTO.setPassword(passWord);
//		BulkReminderResultDto  searchResutDto = searchService.search(searchFormDTO,userName,passWord,dbCalService);
//
//		if(searchResutDto != null && searchResutDto.getBatchid() != null && !searchResutDto.getBatchid().isEmpty()){
//			
//			generateAndUploadReminderLetters(searchResutDto);
//		}
//		else{
//			view.loadResultLayout(searchResutDto);
//		}
//		
//		
//	}
//	
//	@Override
//	public void viewEntered() {
//		
//		
//	}
//
//}
//>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
