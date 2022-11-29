package com.shaic.claim.reports.opinionvalidationreport;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.ims.bpm.claim.DBCalculationService;

/**
 * @author GokulPrasath.A
 *
 */
@ViewInterface(OpinionValidationReportView.class)
public class OpinionValidationReportPresenter extends AbstractMVPPresenter<OpinionValidationReportView >  {
	
	private static final long serialVersionUID = 1L;
	public static final String OPINION_VALIDATION_REPORT = "doSearchForOpinionValidationReport";
	@Inject
	private OpinionValidationReportService searchService;
	
	@EJB
	private DBCalculationService dbCalService;

	
	public static final String GENERATE_REPORT = "generate_opinion_validation_report";
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(OPINION_VALIDATION_REPORT) final ParameterDTO parameters) {
		
		OpinionValidationReportFormDTO searchFormDTO = (OpinionValidationReportFormDTO) parameters.getPrimaryParameter();
		
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
