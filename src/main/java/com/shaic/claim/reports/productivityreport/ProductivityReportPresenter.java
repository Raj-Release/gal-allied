package com.shaic.claim.reports.productivityreport;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.ims.bpm.claim.DBCalculationService;
@ViewInterface(ProductivityReportView.class)
public class ProductivityReportPresenter extends AbstractMVPPresenter<ProductivityReportView > {
	
	private static final long serialVersionUID = 1L;
	@EJB
	private ProductivityReportService searchService;
	
	@EJB
	private DBCalculationService dbCalService;

	
	public static final String GENERATE_REPORT = "generate_productivity_report";
	
	@SuppressWarnings({ "deprecation" })
	public void generateReport(
			@Observes @CDIEvent(GENERATE_REPORT) final ParameterDTO parameters) {
		ProductivityReportFormDTO searchFormDTO = (ProductivityReportFormDTO) parameters
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
