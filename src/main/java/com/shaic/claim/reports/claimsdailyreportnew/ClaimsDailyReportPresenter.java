package com.shaic.claim.reports.claimsdailyreportnew;

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
import com.vaadin.server.VaadinSession;
@ViewInterface(ClaimsDailyReportView.class)
public class ClaimsDailyReportPresenter extends
AbstractMVPPresenter<ClaimsDailyReportView> {

	public static final String SEARCH_CLMS_DAILY_NEW = "Search Claims Daily Report";
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private IntimationService intimationservice;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private ClaimsReportService claimsReportService;
	
	@EJB
	private UsertoCPUMappingService usertoCPUMapService;
	
	
	
	@Override
	public void viewEntered() {
				
	}
	
	 protected void showClaimPolicySearch(@Observes @CDIEvent(SEARCH_CLMS_DAILY_NEW) final ParameterDTO parameters)
	    {
		   Map<String,Object> searchFilter = (Map<String,Object>) parameters.getPrimaryParameter();
		   
		   String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		   
		   searchFilter.put(BPMClientContext.USERID, userId);
		   
//		   List<ClaimsDailyReportDto> claimReportDto = (List<ClaimsDailyReportDto>) claimsReportService.getClaimsDailyReport(searchFilter,usertoCPUMapService);
		   List<ClaimsDailyReportDto> claimReportDto = (List<ClaimsDailyReportDto>) claimsReportService.getClaimsDailyReport_From_Claim_Table(searchFilter,usertoCPUMapService);
		    
		   view.searchClaimsDailyReport(claimReportDto);
	    }
	

}
