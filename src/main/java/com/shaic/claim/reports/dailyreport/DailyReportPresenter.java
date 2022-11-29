package com.shaic.claim.reports.dailyreport;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.ims.bpm.claim.DBCalculationService;
@ViewInterface(DailyReportView.class)
public class DailyReportPresenter extends AbstractMVPPresenter<DailyReportView > {
	
	private static final long serialVersionUID = 1L;
	public static final String DAILY_REPORT = "doSearchForDailyReport";
	@EJB
	private DailyReportService searchService;
	
	@EJB
	private DBCalculationService dbCalService;

	
	public static final String GENERATE_REPORT = "generate_daily_report";
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(DAILY_REPORT) final ParameterDTO parameters) {
		
		DailyReportFormDTO searchFormDTO = (DailyReportFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord,dbCalService));
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
