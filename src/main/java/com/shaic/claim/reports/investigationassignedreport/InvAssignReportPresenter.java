package com.shaic.claim.reports.investigationassignedreport;

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
/**
 * Part of CR R0768
 * @author Lakshminarayana
 *
 */
@ViewInterface(InvAssignReportView.class)
public class InvAssignReportPresenter extends
AbstractMVPPresenter<InvAssignReportView> {

	public static final String SEARCH_INV_ASSIGN_STATUS_WISE = "Search Inv. Assign Status Report";
	public static final String RESET_INV_ASSIGN_STATUS_REPORT = "Reset Inv. Assign Status Report";

	
	@EJB
	private InvAssignReportService invAssignStatusReportService;

	@EJB
	private MasterService masterService;
		
	
	@Override
	public void viewEntered() {
				
	}
	
	 protected void showInvStsReportSearch(@Observes @CDIEvent(SEARCH_INV_ASSIGN_STATUS_WISE) final ParameterDTO parameters)
	 {
		 
		 InvAssignStatusReportDto searchDto = (InvAssignStatusReportDto)parameters.getPrimaryParameter();
		 String userName = (String) VaadinSession.getCurrent().getAttribute(
					BPMClientContext.USERID);
		 searchDto.setUsername(userName);		 
		 searchDto = invAssignStatusReportService.searchByInvAssignStatus(searchDto);
		 List<InvAssignStatusReportDto> invAssignStatusReportDtoList = searchDto.getSearchResultList();
	    	
		 view.showInvAssignStatusReport(invAssignStatusReportDtoList,searchDto.getStatusSelect().getId());
	 }
	
	 protected void resetExecutiveStatusSummary(@Observes @CDIEvent(RESET_INV_ASSIGN_STATUS_REPORT) final ParameterDTO parameters)
	 {	
		 view.resetSearchView();
	 }

}
