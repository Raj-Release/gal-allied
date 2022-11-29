package com.shaic.claim.omp.reports.outstandingreport;

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
@ViewInterface(OmpOutstandingReportView.class)
public class OmpOutstandingReportPresenter extends
AbstractMVPPresenter<OmpOutstandingReportView> {

	public static final String SEARCH_OMP_OUTSTANDING = "Search Omp Outstanding btn click";
	public static final String RESET_OMP_OUTSTANDING_REPORT = "Reset Lumen Status Report";

	
	@EJB
	private OmpOutstandingReportService ompStatuswiseReportService;

	@EJB
	private MasterService masterService;
		
	
	@Override
	public void viewEntered() {
				
	}
	
	 protected void showLumenStatusReportSearch(@Observes @CDIEvent(SEARCH_OMP_OUTSTANDING) final ParameterDTO parameters)
	 {
		 
		 OmpStatusReportDto searchDto = (OmpStatusReportDto)parameters.getPrimaryParameter();
		 String userName = (String) VaadinSession.getCurrent().getAttribute(
					BPMClientContext.USERID);
		 searchDto.setUsername(userName);		 
		 searchDto = ompStatuswiseReportService.searchOmpOutStanding(searchDto);
		 List<OmpStatusReportDto> lumenStatusWiseReportDtoList = searchDto.getSearchResultList();
	    	
		 view.showOmpOutstandingReport(lumenStatusWiseReportDtoList);
	 }
	
	 protected void resetExecutiveStatusSummary(@Observes @CDIEvent(RESET_OMP_OUTSTANDING_REPORT) final ParameterDTO parameters)
	 {	
		 view.resetSearchView();
	 }

}
