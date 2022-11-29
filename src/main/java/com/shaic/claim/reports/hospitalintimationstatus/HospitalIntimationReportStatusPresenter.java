package com.shaic.claim.reports.hospitalintimationstatus;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@ViewInterface(HospitalIntimationStatusReportView.class)

public class HospitalIntimationReportStatusPresenter extends AbstractMVPPresenter<HospitalIntimationStatusReportView >{
	
	private static final long serialVersionUID = 1L;
	public static final String HOSPITAL_INTIMATION_REPORT = "doSearchForHospitalIntimation";
	@EJB
	private HospitalIntimationReportStatusService searchService;

	
	public static final String GENERATE_REPORT = "generate_hospital_intimation_report";
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(HOSPITAL_INTIMATION_REPORT) final ParameterDTO parameters) {
		
		HospitalIntimationStatusReportFormDTO searchFormDTO = (HospitalIntimationStatusReportFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
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
