package com.shaic.claim.reports.marketingEscalationReport;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.claim.reports.opinionvalidationreport.OpinionValidationReportFormDTO;
import com.shaic.ims.bpm.claim.DBCalculationService;


@ViewInterface(MarketingEscalationReportView.class)
public class MarketingEscalationReportPresenter extends AbstractMVPPresenter<MarketingEscalationReportView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2010102309349356235L;

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	@Inject
	private MarketingEscalationReportService searchService;
	
	@EJB
	private DBCalculationService dbCalService;
	
	public static final String MARKETING_ESCALATION_REPORT = "doSearchForMarketEscalationReport";
	public static final String GENERATE_REPORT = " marketing_escalation__report";
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(MARKETING_ESCALATION_REPORT) final ParameterDTO parameters) {
		
		MarketingEscalationReportFormDTO searchFormDTO = (MarketingEscalationReportFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord,dbCalService));
	}
	
	@SuppressWarnings({ "deprecation" })
	public void generateReport(@Observes @CDIEvent(GENERATE_REPORT) final ParameterDTO parameters) {
		view.generateReport();
		
	}

}
