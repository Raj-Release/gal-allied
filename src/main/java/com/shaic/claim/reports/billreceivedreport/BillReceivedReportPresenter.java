package com.shaic.claim.reports.billreceivedreport;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@ViewInterface(BillReceivedReportView.class)
public class BillReceivedReportPresenter extends AbstractMVPPresenter<BillReceivedReportView > {
	private static final long serialVersionUID = 1L;
	public static final String BILL_RECEIVED_REPORT = "doSearchForBillReceivedReport";
	@EJB
	private BillReceivedReportService searchService;

	
	public static final String GENERATE_REPORT = "generate_billreceived_report";
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(BILL_RECEIVED_REPORT) final ParameterDTO parameters) {
		
		BillReceivedReportFormDTO searchFormDTO = (BillReceivedReportFormDTO) parameters.getPrimaryParameter();
		
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
