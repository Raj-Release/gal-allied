package com.shaic.claim.fss.filedetailsreport;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.ims.bpm.claim.DBCalculationService;
@ViewInterface(FileDetailsReportView.class)
public class FileDetailsReportPresenter extends AbstractMVPPresenter<FileDetailsReportView >{
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK_FILE_REQUEST = "doSearchForFileDetail";
	
	@EJB
	private FileDetailsReportService searchService;
	
	@EJB
	private DBCalculationService dbCalService;
	
	public static final String SHOW_FVR_REQUEST_VIEW = "show_FVR_request_view";
	
	public static final String GENERATE_REPORT = "generate_file_detail_report";
	
	public static final String RESET_SEARCH_VIEW = "File Storage Reset Search Fields";

	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK_FILE_REQUEST) final ParameterDTO parameters) {
		
		FileDetailsReportFormDTO searchFormDTO = (FileDetailsReportFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
//		view.list(searchService.search(searchFormDTO,userName,passWord,userCPUMapService));
		
		view.list(searchService.search(searchFormDTO,userName,passWord,dbCalService));
	}
	
	@SuppressWarnings({ "deprecation" })
	public void generateReport(@Observes @CDIEvent(GENERATE_REPORT) final ParameterDTO parameters) {
		view.generateReport();
		
	}
	
	protected void showSearchFile(@Observes @CDIEvent(RESET_SEARCH_VIEW) final ParameterDTO parameters) {
		view.resetSearchFileReportView();
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	

}
