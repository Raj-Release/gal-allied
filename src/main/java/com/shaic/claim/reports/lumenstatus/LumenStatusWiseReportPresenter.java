package com.shaic.claim.reports.lumenstatus;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.shaic.domain.ClaimService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
@ViewInterface(LumenStatusWiseReportView.class)
public class LumenStatusWiseReportPresenter extends
AbstractMVPPresenter<LumenStatusWiseReportView> {

	public static final String SEARCH_LUMEN_STATUS_WISE = "Search Lumen Status Wise Report";
	public static final String RESET_LUMEN_STATUS_REPORT = "Reset Lumen Status Report";

	
	@EJB
	private LumenStatuswiseReportService lumenStatuswiseReportService;

	@EJB
	private MasterService masterService;
		
	
	@Override
	public void viewEntered() {
				
	}
	
	 protected void showLumenStatusReportSearch(@Observes @CDIEvent(SEARCH_LUMEN_STATUS_WISE) final ParameterDTO parameters)
	 {
		 
		 SearchLumenStatusWiseDto searchDto = (SearchLumenStatusWiseDto)parameters.getPrimaryParameter();
		 String userName = (String) VaadinSession.getCurrent().getAttribute(
					BPMClientContext.USERID);
		 searchDto.setUsername(userName);		 
		 searchDto = lumenStatuswiseReportService.searchbyLumenStatus(searchDto);
		 List<SearchLumenStatusWiseDto> lumenStatusWiseReportDtoList = searchDto.getSearchResultList();
	    	
		 view.showLumenStatusWiseReport(lumenStatusWiseReportDtoList);
	 }
	
	 protected void resetExecutiveStatusSummary(@Observes @CDIEvent(RESET_LUMEN_STATUS_REPORT) final ParameterDTO parameters)
	 {	
		 view.resetSearchView();
	 }

}
