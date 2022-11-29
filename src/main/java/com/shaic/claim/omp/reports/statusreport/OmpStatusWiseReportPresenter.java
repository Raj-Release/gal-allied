package com.shaic.claim.omp.reports.statusreport;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.shaic.claim.omp.reports.outstandingreport.OmpStatusReportDto;
import com.shaic.domain.ClaimService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
@ViewInterface(OmpStatusWiseReportView.class)
public class OmpStatusWiseReportPresenter extends
AbstractMVPPresenter<OmpStatusWiseReportView> {

	public static final String SEARCH_OMP_STATUS_WISE_CLICK = "Search Omp Status Wise Click";
	public static final String RESET_OMP_STATUS_REPORT = "Reset Omp Status Report";

	
	@EJB
	private OmpStatuswiseReportService ompStatuswiseReportService;

	@EJB
	private MasterService masterService;
		
	
	@Override
	public void viewEntered() {
				
	}
	
	 protected void showLumenStatusReportSearch(@Observes @CDIEvent(SEARCH_OMP_STATUS_WISE_CLICK) final ParameterDTO parameters)
	 {
		 
		 OmpStatusReportDto searchDto = (OmpStatusReportDto)parameters.getPrimaryParameter();
		 String userName = (String) VaadinSession.getCurrent().getAttribute(
					BPMClientContext.USERID);
		 searchDto.setUsername(userName);		 
		 searchDto = ompStatuswiseReportService.searchOmpStsReport(searchDto);
		 List<OmpStatusReportDto> ompStatusWiseReportDtoList = searchDto.getSearchResultList();
	    	
		 view.showOmpStatusWiseReport(ompStatusWiseReportDtoList);
	 }
	
	 protected void resetExecutiveStatusSummary(@Observes @CDIEvent(RESET_OMP_STATUS_REPORT) final ParameterDTO parameters)
	 {	
		 view.resetSearchView();
	 }

}
