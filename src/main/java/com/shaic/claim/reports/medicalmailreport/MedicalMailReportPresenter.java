package com.shaic.claim.reports.medicalmailreport;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@ViewInterface(MedicalMailReportView.class)
public class MedicalMailReportPresenter extends AbstractMVPPresenter<MedicalMailReportView > {
	
	private static final long serialVersionUID = 1L;
	public static final String MEDICAL_MAIL_REPORT = "doSearchForMedicalMail";
	@EJB
	private MedicalMailReportService searchService;

	
	public static final String GENERATE_REPORT = "generate_medical_mail_report";
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(MEDICAL_MAIL_REPORT) final ParameterDTO parameters) {
		
		MedicalMailReportFormDTO searchFormDTO = (MedicalMailReportFormDTO) parameters.getPrimaryParameter();
		
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
