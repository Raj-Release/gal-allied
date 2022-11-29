package com.shaic.claim.reports.dispatchDetailsReport;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.ims.bpm.claim.DBCalculationService;


@ViewInterface(DispatchDetailsReportView.class)
public class DispatchDetailsReportPresenter extends AbstractMVPPresenter<DispatchDetailsReportView> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1876474527212066064L;

	@EJB
	private DispatchDetailsReportService searchService;
	
	@EJB
	private DBCalculationService dbCalService;
	
	public static final String DISPATCH_DETAILS_REPORT_SEARCH = "dispatch_details_report_search";
	
	public static final String GENERATE_DISPATCH_DETAILS_REPORT = " generate_dispatch_details_report";
	
	public static final String UPDATE_TYPE_CHANGES = " update_type_changes_report";
	
	public static final String RESET_BUTTON_CLICK_UPDATE = " reset_button_click_update_report";
	
	public void handleSearch(@Observes @CDIEvent(DISPATCH_DETAILS_REPORT_SEARCH) final ParameterDTO parameters) {
		
		DispatchDetailsReportFormDTO searchFormDTO = (DispatchDetailsReportFormDTO) parameters.getPrimaryParameter();		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	
	public void generateReport(@Observes @CDIEvent(GENERATE_DISPATCH_DETAILS_REPORT) final ParameterDTO parameters) {
		view.generateReport();		
	}
	
	public void buildTabelTypeChanges(@Observes @CDIEvent(UPDATE_TYPE_CHANGES) final ParameterDTO parameters) {
		Long updateType = (Long) parameters.getPrimaryParameter();
		view.buildUpdateTypeLayout(updateType);
	}
	
	public void resetSearch(@Observes @CDIEvent(RESET_BUTTON_CLICK_UPDATE) final ParameterDTO parameters) {
		view.resetDispatchSearchFields();
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
