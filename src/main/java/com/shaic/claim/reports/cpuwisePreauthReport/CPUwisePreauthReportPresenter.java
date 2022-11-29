package com.shaic.claim.reports.cpuwisePreauthReport;

import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.ClaimService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.UsertoCPUMappingService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.server.VaadinSession;
@ViewInterface(CPUwisePreauthReportView.class)
public class CPUwisePreauthReportPresenter extends
AbstractMVPPresenter<CPUwisePreauthReportView> {

	public static final String SEARCH_CPU_WISE_PREAUTH = "Search CPU wise Preauth";
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private IntimationService intimationservice;
	
	@EJB
	private PreauthService preAuthService;
	
	@EJB
	private UsertoCPUMappingService userCPUMapService;	
	
	@EJB
	private DBCalculationService dbclaService;
	
	@Override
	public void viewEntered() {
				
	}
	
	 protected void showClaimPolicySearch(@Observes @CDIEvent(SEARCH_CPU_WISE_PREAUTH) final ParameterDTO parameters)
	    {
		   WeakHashMap<String,Object> searchFilter = (WeakHashMap<String,Object>) parameters.getPrimaryParameter();
		   
//		   List<CPUwisePreauthReportDto> claimReportDto = (List<CPUwisePreauthReportDto>) preAuthService.getCpuWisePreauth(searchFilter);

		   String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		   
		   searchFilter.put(BPMClientContext.USERID, userId);
		   
		   CPUWisePreauthResultDto resultDto = preAuthService.getCpuWisePreauth(searchFilter,userCPUMapService,dbclaService);
		   
		   view.showCPUWisePreauthDetails(resultDto);
	    }
	

}
