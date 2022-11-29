package com.shaic.claim.reports.paymentpendingdashboard;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;


import com.shaic.ims.bpm.claim.DBCalculationService;

@ViewInterface(PaymentPendingDashboardView.class)
public class PaymentPendingDashboardPresenter extends AbstractMVPPresenter<PaymentPendingDashboardView> {

	private static final long serialVersionUID = 1L;
	@EJB
	private PaymentPendingDashboardService searchService;

	@EJB
	private DBCalculationService dbCalService;


	public static final String GENERATE_PAYMENT_PENDING_DASHBOARD_REPORT = "generate_payment_pending_dashboard_report";

	@SuppressWarnings({ "deprecation" })
	public void generateReport(
			@Observes @CDIEvent(GENERATE_PAYMENT_PENDING_DASHBOARD_REPORT) final ParameterDTO parameters) {
		PaymentPendingDashboardFormDTO searchFormDTO = (PaymentPendingDashboardFormDTO) parameters
				.getPrimaryParameter();

		String userName = (String) parameters.getSecondaryParameter(0,
				String.class);
		String passWord = (String) parameters.getSecondaryParameter(1,
				String.class);

		view.list(searchService.search(searchFormDTO, userName, passWord,
				dbCalService));
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}


}


