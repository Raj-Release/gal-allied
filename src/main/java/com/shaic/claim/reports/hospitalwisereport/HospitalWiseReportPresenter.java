package com.shaic.claim.reports.hospitalwisereport;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.UsertoCPUMappingService;
@ViewInterface(HospitalWiseReportView.class)
public class HospitalWiseReportPresenter extends AbstractMVPPresenter<HospitalWiseReportView >{
	private static final long serialVersionUID = 1L;

	public static final String HOSPITAL_WISE_REPORT = "doSearchForHospital";
	
	@EJB
	private UsertoCPUMappingService userCPUMapService;
	
	@EJB
	private HospitalWiseReportService searchService;
	public static final String SHOW_HOSPITAL_REQUEST_VIEW = "show_HOSPITAL_request_view";
	
	public static final String GENERATE_REPORT = "generate_hospital_report";

	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(HOSPITAL_WISE_REPORT) final ParameterDTO parameters) {
		
		HospitalWiseReportFormDTO searchFormDTO = (HospitalWiseReportFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord,userCPUMapService));
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
