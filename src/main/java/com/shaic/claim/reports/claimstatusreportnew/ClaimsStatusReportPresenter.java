package com.shaic.claim.reports.claimstatusreportnew;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.ClaimService;
import com.shaic.domain.ClaimsReportService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.UsertoCPUMappingService;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.server.VaadinSession;
@ViewInterface(ClaimsStatusReportView.class)
public class ClaimsStatusReportPresenter extends
AbstractMVPPresenter<ClaimsStatusReportView> {

	public static final String SEARCH_CLMS_STATUS_NEW = "Search Claims Status Report";
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private IntimationService intimationservice;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private ClaimsReportService claimsReportService;
	
	@EJB
	private UsertoCPUMappingService userCPUMapService;
	
	@EJB
	private DBCalculationService dbCalService;
	
	@Override
	public void viewEntered() {
				
	}
	
	 protected void showClaimStatusReportSearch(@Observes @CDIEvent(SEARCH_CLMS_STATUS_NEW) final ParameterDTO parameters)
	    {
		   Map<String,Object> searchFilter = (Map<String,Object>) parameters.getPrimaryParameter();
		   
		   String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		   
		   searchFilter.put(BPMClientContext.USERID, userId);
		   		   
		   List<ClaimsStatusReportDto> claimsStatusReportListDto = (List<ClaimsStatusReportDto>) claimsReportService.getClaimsStatusReport(searchFilter,userCPUMapService,dbCalService);
		    
		   view.searchClaimsStatusReport(claimsStatusReportListDto);
	    }
	

}
