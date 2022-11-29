package com.shaic.claim.reports.helpdeskstatusreport;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.UsertoCPUMappingService;

@ViewInterface(HelpDeskStatusReportView.class)

public class HelpDeskStatusReportPresenter extends AbstractMVPPresenter<HelpDeskStatusReportView > {
	private static final long serialVersionUID = 1L;

	public static final String HELP_DESK_STATUS_REPORT = "doSearchForHelpDesk";
	
	@EJB
	private HelpDeskStatusReportService searchService;
	
	@EJB
	private UsertoCPUMappingService usertoCPUMapService;
	
	public static final String SHOW_HELPDESK_REQUEST_VIEW = "show_HelpDesk_request_view";
	
	public static final String GENERATE_REPORT = "generate_helpdesk_report";
	
	public static final String RESET_FUNCTION = "reset_search_form";

	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(HELP_DESK_STATUS_REPORT) final ParameterDTO parameters) {
		
		HelpDeskStatusReportFormDTO searchFormDTO = (HelpDeskStatusReportFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord,usertoCPUMapService));
	}
	

	@SuppressWarnings({ "deprecation" })
	public void generateReport(@Observes @CDIEvent(GENERATE_REPORT) final ParameterDTO parameters) {
		view.generateReport();
		
	}
	
	@SuppressWarnings({ "deprecation" })
	public void resetSearchValue(@Observes @CDIEvent(RESET_FUNCTION) final ParameterDTO parameters) {
		view.resetSearchValue();
		
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
