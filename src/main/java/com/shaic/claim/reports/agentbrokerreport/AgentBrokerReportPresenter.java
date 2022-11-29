package com.shaic.claim.reports.agentbrokerreport;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.UsertoCPUMappingService;
import com.shaic.ims.bpm.claim.DBCalculationService;
@ViewInterface(AgentBrokerReportView.class)

public class AgentBrokerReportPresenter extends AbstractMVPPresenter<AgentBrokerReportView >{
	
	private static final long serialVersionUID = 1L;
	public static final String AGENT_BROKER_REPORT = "doSearchForAgentReport";
	@EJB
	private AgentBrokerReportService searchService;
	
	@EJB
	private UsertoCPUMappingService cpuMapService;

	@EJB
	private DBCalculationService dbCalService;
	
	public static final String GENERATE_REPORT = "generate_agent_report";
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(AGENT_BROKER_REPORT) final ParameterDTO parameters) {
		
		AgentBrokerReportFormDTO searchFormDTO = (AgentBrokerReportFormDTO) parameters.getPrimaryParameter();
		
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
