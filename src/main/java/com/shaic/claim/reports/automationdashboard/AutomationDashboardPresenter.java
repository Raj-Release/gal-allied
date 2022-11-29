package com.shaic.claim.reports.automationdashboard;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.ims.bpm.claim.DBCalculationService;

@ViewInterface(AutomationDashboardView.class)
public class AutomationDashboardPresenter extends AbstractMVPPresenter<AutomationDashboardView > {

	private static final long serialVersionUID = 1L;
	@EJB
	private AutomationDashboardService searchService;

	@EJB
	private DBCalculationService dbCalService;


	public static final String GENERATE_AUTOMATION_DASHBOARD_REPORT = "generate_automation_dashboard_report";

	@SuppressWarnings({ "deprecation" })
	public void generateReport(
			@Observes @CDIEvent(GENERATE_AUTOMATION_DASHBOARD_REPORT) final ParameterDTO parameters) {
		AutomationDashboardFormDTO searchFormDTO = (AutomationDashboardFormDTO) parameters
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


