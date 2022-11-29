package com.shaic.claim.reports.finapprovednotsettledreport;

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
 * Part of CR R1201
 * @author Lakshminarayana
 *
 */
@ViewInterface(FinApprovedPaymentPendingReportView.class)
public class FinApprovedPaymentPendingReportPresenter extends
AbstractMVPPresenter<FinApprovedPaymentPendingReportView> {

	public static final String SEARCH_FIN_APPROVED_SETTLEMENT_PENDING = "Search Fin. Approved Payment pending Report";
	public static final String RESET_FIN_APPROVED_SETTLEMENT_PENDING_REPORT = "Reset Fin. Approved Payment pending Report";

	
	@EJB
	private FinApprovedPaymentPendingReportService finApprovedPaymentPendingReportService;

	@EJB
	private MasterService masterService;
		
	
	@Override
	public void viewEntered() {
				
	}
	
	 protected void showInvStsReportSearch(@Observes @CDIEvent(SEARCH_FIN_APPROVED_SETTLEMENT_PENDING) final ParameterDTO parameters)
	 {
		 
		 FinApprovedPaymentPendingReportDto searchDto = (FinApprovedPaymentPendingReportDto)parameters.getPrimaryParameter();
		 String userName = (String) VaadinSession.getCurrent().getAttribute(
					BPMClientContext.USERID);
		 searchDto.setUsername(userName);		 
		 searchDto = finApprovedPaymentPendingReportService.searchFAapprovedSettlemntPending(searchDto);
		 List<FinApprovedPaymentPendingReportDto> invAssignStatusReportDtoList = searchDto.getSearchResultList();
	    	
		 view.showFinApprovedSettlementPendingReport(invAssignStatusReportDtoList);
	 }
	
	 protected void resetExecutiveStatusSummary(@Observes @CDIEvent(RESET_FIN_APPROVED_SETTLEMENT_PENDING_REPORT) final ParameterDTO parameters)
	 {	
		 view.resetSearchView();
	 }

}
