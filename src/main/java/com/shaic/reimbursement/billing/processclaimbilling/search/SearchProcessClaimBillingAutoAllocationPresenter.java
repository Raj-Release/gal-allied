package com.shaic.reimbursement.billing.processclaimbilling.search;

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

@ViewInterface(SearchProcessClaimBillingAutoAllocationView.class)
public class SearchProcessClaimBillingAutoAllocationPresenter  extends AbstractMVPPresenter<SearchProcessClaimBillingAutoAllocationView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6080696849738464832L;
	public static final String CLAIM_BILLING_AUTO_SEARCH_BUTTON_CLICK = "claim_billing_auto_search_button_click";
	public static final String SEARCH_FOR_COMPLETED_CASE_BILLING = "search_for_completed_case_billing";
	public static final String SEARCH_FOR_CLAIM_BILLING_HOLD_TABLE_LIST = "search_for_claim_billing_hold_table_list";
	
	@EJB
	private SearchProcessClaimBillingAutoAllocationService searchService;

	@EJB
	private DBCalculationService dbCalculationService;

	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(CLAIM_BILLING_AUTO_SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {


		SearchProcessClaimBillingFormDTO searchFormDTO = (SearchProcessClaimBillingFormDTO) parameters.getPrimaryParameter();

		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		Page<SearchProcessClaimBillingTableDTO> search = searchService.search(searchFormDTO,userName,passWord);
		view.list(search);
	}

	@Override
	public void viewEntered() {

	}
	
	@SuppressWarnings({ "deprecation" })
	public void getHoldTableList(@Observes @CDIEvent(SEARCH_FOR_CLAIM_BILLING_HOLD_TABLE_LIST) final ParameterDTO parameters) {
		
		String userName =(String)parameters.getPrimaryParameter();
		String passWord=(String)parameters.getSecondaryParameter(0, String.class);		
		
		List<SearchHoldMonitorScreenTableDTO> holdClaims = dbCalculationService.getHoldMonitorClaimsForBillingFA(null, 
				userName, SHAConstants.BILLING_CURRENT_KEY,SHAConstants.AUTO_ALLOCATION_ALLOCATION_MENU_TYPE,null,SHAConstants.PROCESS_CLAIM_BILLING_AUTO_ALLOCATION);

		view.setHoldTableList(holdClaims);
	}
}
