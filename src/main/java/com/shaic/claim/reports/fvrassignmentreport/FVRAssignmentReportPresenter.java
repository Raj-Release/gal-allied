package com.shaic.claim.reports.fvrassignmentreport;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.UsertoCPUMappingService;
import com.shaic.ims.bpm.claim.DBCalculationService;
@ViewInterface(FVRAssignmentReportView.class)
public class FVRAssignmentReportPresenter extends AbstractMVPPresenter<FVRAssignmentReportView >{
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK_FVR_REQUEST = "doSearchForFVR";
	
	@EJB
	private UsertoCPUMappingService userCPUMapService;
	
	@EJB
	private FVRAssignmentReportService searchService;
	
	@EJB
	private DBCalculationService dbCalService;
	
	public static final String SHOW_FVR_REQUEST_VIEW = "show_FVR_request_view";
	
	public static final String GENERATE_REPORT = "generate_fvr_report";

	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK_FVR_REQUEST) final ParameterDTO parameters) {
		
		FVRAssignmentReportFormDTO searchFormDTO = (FVRAssignmentReportFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
//		view.list(searchService.search(searchFormDTO,userName,passWord,userCPUMapService));
		
		view.list(searchService.search(searchFormDTO,userName,passWord,userCPUMapService,dbCalService));
	}
	
	@SuppressWarnings({ "deprecation" })
	public void generateReport(@Observes @CDIEvent(GENERATE_REPORT) final ParameterDTO parameters) {
		view.generateReport();
		
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	

}
