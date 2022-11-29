package com.shaic.claim.reports.negotiationreport;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.claim.ViewNegotiationDetailsDTO;
import com.shaic.domain.ClaimsReportService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.server.VaadinSession;

@ViewInterface(NegotiationReportView.class)
public class NegotiationReportPresenter extends AbstractMVPPresenter<NegotiationReportView>{
	
	public static final String GET_NEGOTIATION_REPORT = "getnegotiationreport";
	
	@EJB
	private ClaimsReportService clmReportService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	protected void getNegotiationReportDetails(@Observes @CDIEvent(GET_NEGOTIATION_REPORT) final ParameterDTO parameters){
		
		Map<String,Object> searchFilter = (Map<String,Object>) parameters.getPrimaryParameter(); 
		String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		searchFilter.put(BPMClientContext.USERID, userId);
		List<ViewNegotiationDetailsDTO> negotiationDtls = (List<ViewNegotiationDetailsDTO>)clmReportService.getNegotiationReportDetails(searchFilter,dbCalculationService);
		view.negotionReportDtls(negotiationDtls);
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
