package com.shaic.claim.reports.preauthFormDocReport;

import java.util.List;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.ClaimsReportService;
import com.shaic.domain.UsertoCPUMappingService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.server.VaadinSession;
@ViewInterface(SearchPreauthFormDocReportView.class)
public class SearchPreauthFormDocReportPresenter extends
AbstractMVPPresenter<SearchPreauthFormDocReportView> {

	public static final String SEARCH_BTN_CLICK = "Search Report Btn Click";
	
//	public static final String GET_CPU_LOV_LIST = "get CPU LOV List";
	
	@EJB
	private ClaimsReportService claimsReportService;
	
	@EJB
	private DBCalculationService dbCalService;
	
	@EJB
	private UsertoCPUMappingService  cpuService; 
	
	
	
	@Override
	public void viewEntered() {
				
	}
	
//	protected void getUseCPUContainer(@Observes @CDIEvent(GET_CPU_LOV_LIST) final ParameterDTO parameters){
//		String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
//		view.setCPUContainerToForm(cpuService.getRevisedUserCpuContainer(userId));
//	}
	
	 protected void showClaimPolicySearch(@Observes @CDIEvent(SEARCH_BTN_CLICK) final ParameterDTO parameters)
	    {
		   WeakHashMap<String,Object> searchFilter = (WeakHashMap<String,Object>) parameters.getPrimaryParameter();
		   
		   String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		   
		   searchFilter.put(BPMClientContext.USERID, userId);
		   
		   List<NewIntimationDto> peauthFormDocReportDto = (List<NewIntimationDto>) claimsReportService.getPreauthFormDocReport(searchFilter,dbCalService);
		    
		   view.searchPreauthFormDocReport(peauthFormDocReportDto);
	    }
	

}
