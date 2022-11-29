package com.shaic.claim.reports.ackwithoutrodreport;

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
@ViewInterface(AckWithoutRodReportView.class)
public class AckWithoutRodReportPresenter extends
AbstractMVPPresenter<AckWithoutRodReportView> {

	public static final String SEARCH_ACK_WITHOUT_ROD = "Search Ack without ROD Report";
	public static final String RESET_ACK_WITHOUT_ROD_REPORT = "Reset Ack without Rod Report";

	
	@EJB
	private AckWithoutRodReportService ackWithoutRodReportService;

	@EJB
	private MasterService masterService;
		
	
	@Override
	public void viewEntered() {
				
	}
	
	 protected void showLumenStatusReportSearch(@Observes @CDIEvent(SEARCH_ACK_WITHOUT_ROD) final ParameterDTO parameters)
	 {
		 
		 AckWithoutRodTableDto searchDto = (AckWithoutRodTableDto)parameters.getPrimaryParameter();
		 String userName = (String) VaadinSession.getCurrent().getAttribute(
					BPMClientContext.USERID);
		 searchDto.setUsername(userName);		 
		 List<AckWithoutRodTableDto> ackwithoutRodDtoList = ackWithoutRodReportService.searchAckWithoutRod(searchDto);
	    	
		 view.showAckWithoutRodReport(ackwithoutRodDtoList);
	 }
	
	 protected void resetExecutiveStatusSummary(@Observes @CDIEvent(RESET_ACK_WITHOUT_ROD_REPORT) final ParameterDTO parameters)
	 {	
		 view.resetSearchView();
	 }

}
