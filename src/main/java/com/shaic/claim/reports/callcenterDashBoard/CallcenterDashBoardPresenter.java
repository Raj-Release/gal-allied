package com.shaic.claim.reports.callcenterDashBoard;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.table.Pageable;
import com.shaic.claim.intimation.search.SearchIntimationFormDto;
import com.shaic.domain.ClaimService;
import com.shaic.domain.ClaimsReportService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
@ViewInterface(CallcenterDashBoardReportView.class)
public class CallcenterDashBoardPresenter extends
AbstractMVPPresenter<CallcenterDashBoardReportView> {

	public static final String SEARCH_CLM_CALLCENTER_DASH_BOARD = "Search Callcenter Dash Board ";
	
	public static final String DISABLE_FIELDS = "Disable Filters";
	
	public static final String RESET_SEARCH_CALLCENTER_DASHBOARD_VIEW = "Reset Search view of call center Dashboard";
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private ClaimsReportService claimRepoService;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	
	
	@Override
	public void viewEntered() {
				
	}
	
	 protected void showClaimPolicySearch(@Observes @CDIEvent(SEARCH_CLM_CALLCENTER_DASH_BOARD) final ParameterDTO parameters)
	    {
		   Map<String,Object> searchFilter = (Map<String,Object>) parameters.getPrimaryParameter();
		   
		   
		   SearchIntimationFormDto searchDto = new SearchIntimationFormDto();
		   
		   searchDto.setFilters(searchFilter);
		   Pageable aPage = null;
		   searchDto.setPageable(aPage);
		   
		   Map<Long,Long> statusId = ReferenceTable.getCallCenterStatusValues();
		   Long statusKey = searchFilter.containsKey("refstatusKey") ? (Long) searchFilter.get("refstatusKey")
					: null;
		   Long status = statusId.get(statusKey);
		   
		   Date toDate = searchFilter.containsKey("toDate") ? (Date) searchFilter.get("toDate")
					: null;
		   Date fromDate  = searchFilter.containsKey("fromDate") ? (Date) searchFilter.get("fromDate")
					: null;
		   
		   String intimationNumber = searchFilter.containsKey("intimationNumber") ? 
				StringUtils.trim(searchFilter.get("intimationNumber")
							.toString()) : null;
		   
		   String policyNo = searchFilter.containsKey("policyNumber") ?  StringUtils.trim(searchFilter.get("policyNumber")
							.toString()) : null;
		   
		   String claimNo = searchFilter.containsKey("claimNumber") ?
					StringUtils.trim(searchFilter.get("claimNumber")
							.toString()) : null;
		   
		   String hospitalName = searchFilter.containsKey("hospitalName") ?
					 StringUtils.trim(searchFilter.get("hospitalName")
							.toString()) : null;
							
		   DBCalculationService dbCalculationService = new DBCalculationService();
		   List<CallcenterDashBoardReportDto> callcenterDashBoardReportListDto = dbCalculationService.getCallCenterDashBoard(status, toDate, fromDate, policyNo, intimationNumber, claimNo, hospitalName,null);
//		   List<CallcenterDashBoardReportDto> callcenterDashBoardReportListDto = claimRepoService.getCallcenterDashBoardSearchResult(searchDto);
		   
		     view.searchCallCenterDashBoard(callcenterDashBoardReportListDto);
		    
	    }
	
	 protected void hideSearchfields(@Observes @CDIEvent(DISABLE_FIELDS) final ParameterDTO parameters) {
			view.hideSearchFields((String)parameters.getPrimaryParameter());
		}
	 
	 protected void resetDashBoardView(@Observes @CDIEvent(RESET_SEARCH_CALLCENTER_DASHBOARD_VIEW) final ParameterDTO parameters) {
			view.resetSearchIntimationView();
		}

}
