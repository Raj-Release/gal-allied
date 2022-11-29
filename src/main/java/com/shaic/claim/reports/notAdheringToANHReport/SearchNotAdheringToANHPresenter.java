package com.shaic.claim.reports.notAdheringToANHReport;

import java.util.List;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import com.shaic.domain.ClaimsReportService;
import com.shaic.domain.UsertoCPUMappingService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.server.VaadinSession;
@ViewInterface(SearchNotAdheringToANHReportView.class)
public class SearchNotAdheringToANHPresenter extends AbstractMVPPresenter<SearchNotAdheringToANHReportView> {
	
	public static final String SEARCH_BTNN_CLICK = "Search Report Btnn Click";
	
	@EJB
	private ClaimsReportService claimsReportService;
	
	@EJB
	private DBCalculationService dbCalService;
	
	@EJB
	private UsertoCPUMappingService  cpuService; 

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub	
	}
	
	 protected void showClaimPolicySearch(@Observes @CDIEvent(SEARCH_BTNN_CLICK) final ParameterDTO parameters)
	    {
		   WeakHashMap<String,Object> searchFilter = (WeakHashMap<String,Object>) parameters.getPrimaryParameter();
		   
		   String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		   
		   searchFilter.put(BPMClientContext.USERID, userId);
		   
		   List<NewIntimationNotAdheringToANHDto> notAdheringToANHDto = (List<NewIntimationNotAdheringToANHDto>) claimsReportService.getNotAdheringToANHReport(searchFilter,dbCalService);
		    
		   view.searchNotAdheringToANHReport(notAdheringToANHDto);
	    }
	

}
