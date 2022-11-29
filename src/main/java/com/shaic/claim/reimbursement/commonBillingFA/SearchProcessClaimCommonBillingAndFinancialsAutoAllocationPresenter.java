package com.shaic.claim.reimbursement.commonBillingFA;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.Page;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsTableDTO;
import com.shaic.reimbursement.manageclaim.HoldMonitorScreen.SearchHoldMonitorScreenTableDTO;

@ViewInterface(SearchProcessClaimCommonBillingAndFinancialsAutoAllocationView.class)
public class SearchProcessClaimCommonBillingAndFinancialsAutoAllocationPresenter  extends AbstractMVPPresenter<SearchProcessClaimCommonBillingAndFinancialsAutoAllocationView>{

	private static final long serialVersionUID = -5504472929540762973L;

	public static final String COMMON_BILLING_FA_AUTO_SEARCH_BUTTON_CLICK = "commonBillingFA_for_auto_allocation";
	
	public static final String SEARCH_FOR_COMPLETED_CASE = "search_for_Billing_completed_case for Billing q";
	public static final String SEARCH_FOR_HOLD_TABLE_LIST = "search_for_Billing_hold_table_list";
	
	@EJB
	private SearchProcessClaimCommonBillingAndFinancialsAutoAllocationService searchService;
	

	@EJB
	private DBCalculationService dbCalculationService;

	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(COMMON_BILLING_FA_AUTO_SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {


		SearchProcessClaimFinancialsTableDTO searchFormDTO = (SearchProcessClaimFinancialsTableDTO) parameters.getPrimaryParameter();

		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);

		Page<SearchProcessClaimFinancialsTableDTO> search = searchService.search(searchFormDTO,userName,passWord);

		view.list(search);
	}

	@Override
	public void viewEntered() {

	}
	
//	@SuppressWarnings({ "deprecation" })
//	public void handleSearchForAutoAllocationDetails(@Observes @CDIEvent(SEARCH_FOR_COMPLETED_CASE) final ParameterDTO parameters) {
//		
//		String userName= (String) parameters.getPrimaryParameter();
//
//		SearchPreauthTableDTO search = preAuthSearchService.getCompletedCase(userName);
//		view.setValuesForCompletedCase(search);
//	}
	
	@SuppressWarnings({ "deprecation" })
	public void getHoldTableList(@Observes @CDIEvent(SEARCH_FOR_HOLD_TABLE_LIST) final ParameterDTO parameters) {
		
		SearchProcessClaimFinancialsTableDTO searchFormDTO = new SearchProcessClaimFinancialsTableDTO();
		String userName =(String)parameters.getPrimaryParameter();
		String passWord=(String)parameters.getSecondaryParameter(0, String.class);		
		
		List<SearchHoldMonitorScreenTableDTO> holdClaims = dbCalculationService.getHoldMonitorClaimsForBillingFA(null, 
				userName, SHAConstants.BILLING_CURRENT_KEY,SHAConstants.AUTO_ALLOCATION_ALLOCATION_MENU_TYPE,null,null);

		view.setHoldTableList(holdClaims);
	}
}
