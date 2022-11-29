package com.shaic.claim.reports.branchManagerFeedbackReportingPattern;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.shaic.claim.reports.branchManagerFeedBack.BranchManagerFeedBackReportDto;
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
@ViewInterface(BranchManagerFeedBackReportingPatternView.class)
public class BranchManagerFeedBackReportingPatternPresenter extends
AbstractMVPPresenter<BranchManagerFeedBackReportingPatternView> {

	public static final String SEARCH_BM_FB_PATTERN = "Search Branch Manager Feedback Reporting Pattern";
	public static final String RESET_BM_FB_REPORT_PATTERN = "Reset Branch Manager Feedback Reporting Pattern";
	public static final String GET_BM_FB_PATTERN_BRANCH_DETAILS = "Get BM Branch By Zone Code for Reporting Pattern";
	
	@EJB
	private BranchManagerFeedBackReportingPatternService branchManagerFeedbackReportService;

	@EJB
	private DBCalculationService dbCalService;
	
	@EJB
	private MasterService masterService;
		
	
	@Override
	public void viewEntered() {
				
	}
	
	 protected void showBranchManagerFeedbackReportSearch(@Observes @CDIEvent(SEARCH_BM_FB_PATTERN) final ParameterDTO parameters)
	 {
		 
		 BranchManagerFeedBackReportingPatternDto searchDto = (BranchManagerFeedBackReportingPatternDto)parameters.getPrimaryParameter();
		 String userName = (String) VaadinSession.getCurrent().getAttribute(
					BPMClientContext.USERID);
		 searchDto.setUsername(userName);		 
		 searchDto = branchManagerFeedbackReportService.searchFAapprovedSettlemntPending(searchDto);
		 List<BranchManagerFeedBackReportingPatternDto> bmFeedbackReportDtoList = searchDto.getSearchResultList();	    	
		 view.showBmFeedbackReportingPattern(bmFeedbackReportDtoList);
	 }
	
	 protected void resetExecutiveStatusSummary(@Observes @CDIEvent(RESET_BM_FB_REPORT_PATTERN) final ParameterDTO parameters)
	 {	
		 view.resetSearchView();
	 }

	 protected void showBranchBasedOnZoneCode(@Observes @CDIEvent(GET_BM_FB_PATTERN_BRANCH_DETAILS) final ParameterDTO parameters)
	 {
		 
		 Long zoneCode = (Long)parameters.getPrimaryParameter();

		 BeanItemContainer<SelectValue> branchContainer = dbCalService.getBranchDetailsContainerForBranchManagerFeedback(zoneCode);
		 	
		 view.loadBranchDropDown(branchContainer);
	 }
}
