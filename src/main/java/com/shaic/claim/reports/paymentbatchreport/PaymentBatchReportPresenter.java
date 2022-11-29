package com.shaic.claim.reports.paymentbatchreport;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.UsertoCPUMappingService;

@ViewInterface(PaymentBatchReportView.class)
public class PaymentBatchReportPresenter  extends AbstractMVPPresenter<PaymentBatchReportView>{
	
	public static final String SEARCH_BUTTON_CLICK_PAYMENT_BATCH = "doSearchForPaymentBatch";
	
	public static final String GENERATE_REPORT = "generate_payment_batch_report";
	
	@EJB
	private UsertoCPUMappingService userCPUMapService;
	
	@EJB
	private PaymentBatchReportService searchService;
	

	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK_PAYMENT_BATCH) final ParameterDTO parameters) {
		
		PaymentBatchReportFormDTO searchFormDTO = (PaymentBatchReportFormDTO) parameters.getPrimaryParameter();
		
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
