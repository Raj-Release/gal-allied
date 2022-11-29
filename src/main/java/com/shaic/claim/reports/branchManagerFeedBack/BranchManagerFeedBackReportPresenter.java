package com.shaic.claim.reports.branchManagerFeedBack;

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
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
/**
 * Part of CR R1238
 * @author Lakshminarayana
 *
 */
@ViewInterface(BranchManagerFeedBackReportView.class)
public class BranchManagerFeedBackReportPresenter extends
AbstractMVPPresenter<BranchManagerFeedBackReportView> {

	public static final String SEARCH_BM_FB = "Search Branch Manager Feedback Report";
	public static final String RESET_BM_FB_REPORT = "Reset Branch Manager Feedback Report";
	public static final String GET_DRILLED_DETAILS = "Get Branch Details splitup";
	public static final String GET_BRANCH_DETAILS = "Get BM Branch By Zone Code";
	
	@EJB
	private BranchManagerFeedBackReportService branchManagerFeedbackReportService;
	
	@EJB
	private DBCalculationService dbCalService;

	@EJB
	private MasterService masterService;
		
	
	@Override
	public void viewEntered() {
				
	}
	
	 protected void showBranchManagerFeedbackReportSearch(@Observes @CDIEvent(SEARCH_BM_FB) final ParameterDTO parameters)
	 {
		 
		 BranchManagerFeedBackReportDto searchDto = (BranchManagerFeedBackReportDto)parameters.getPrimaryParameter();
		 String userName = (String) VaadinSession.getCurrent().getAttribute(
					BPMClientContext.USERID);
		 searchDto.setUsername(userName);		 
		 
		 searchDto = branchManagerFeedbackReportService.searchBranchManagerFeedBackReport(searchDto);
		 List<BranchManagerFeedBackReportDto> bmFeedbackReportDtoList = searchDto.getSearchResultList();
	    	
		 view.showBmFeedbackReport(bmFeedbackReportDtoList);
	 }
	 
	 protected void showBranchManagerFeedbackBranchReport(@Observes @CDIEvent(GET_DRILLED_DETAILS) final ParameterDTO parameters)
	 {
		 
		 BranchManagerFeedBackReportDto searchDto = (BranchManagerFeedBackReportDto)parameters.getPrimaryParameter();

//		 searchDto = branchManagerFeedbackReportService.searchBranchManagerFeedBackReport(searchDto);
//		 List<BranchManagerFeedBackReportDto> bmFeedbackReportDtoList = searchDto.getSearchResultList();
//	    	
//		 view.showBranchDetailsResultTable(bmFeedbackReportDtoList);
	 }
	 
	 protected void showBranchBasedOnZoneCode(@Observes @CDIEvent(GET_BRANCH_DETAILS) final ParameterDTO parameters)
	 {
		 
		 Long zoneCode = (Long)parameters.getPrimaryParameter();

		 BeanItemContainer<SelectValue> branchContainer = dbCalService.getBranchDetailsContainerForBranchManagerFeedback(zoneCode);
		 	
		 view.loadBranchDropDown(branchContainer);
	 }
	 
	 protected void resetExecutiveStatusSummary(@Observes @CDIEvent(RESET_BM_FB_REPORT) final ParameterDTO parameters)
	 {	
		 view.resetSearchView();
	 }

}
