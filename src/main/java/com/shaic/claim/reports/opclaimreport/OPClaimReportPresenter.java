package com.shaic.claim.reports.opclaimreport;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@ViewInterface(OPClaimReportView.class)
public class OPClaimReportPresenter extends AbstractMVPPresenter<OPClaimReportView >{

	private static final long serialVersionUID = 1L;
	public static final String OP_CLAIM_REPORT = "doSearchForOPReport";
	@EJB
	private OPClaimReportService searchService;

	
	public static final String GENERATE_REPORT = "generate_op_report";
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(OP_CLAIM_REPORT) final ParameterDTO parameters) {
		
		OPClaimReportFormDTO searchFormDTO = (OPClaimReportFormDTO) parameters.getPrimaryParameter();
		
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
