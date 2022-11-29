package com.shaic.claim.reimbursement.FinancialApprovalAutoAllocation;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.Page;
import com.shaic.claim.reimbursement.commonBillingFA.SearchProcessClaimCommonBillingAndFinancialsAutoAllocationService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsTableDTO;
import com.shaic.reimbursement.manageclaim.HoldMonitorScreen.SearchHoldMonitorScreenTableDTO;

@ViewInterface(SearchProcessFinancialApprovalAutoAllocationView.class)
public class SearchProcessFinancialApprovalAutoAllocationPresenter  extends AbstractMVPPresenter<SearchProcessFinancialApprovalAutoAllocationView>{


	private static final long serialVersionUID = -5504472929540762973L;

	public static final String FINANCIALAPPROVAL_AUTO_SEARCH_BUTTON_CLICK = "FinancialApproval_for_auto_allocation";
	public static final String SEARCH_FOR_FA_HOLD_TABLE_LIST = "search_for_FinancialApproval_hold_table_list";
	
	@EJB
	private SearchProcessClaimCommonBillingAndFinancialsAutoAllocationService searchService;
	

	@EJB
	private DBCalculationService dbCalculationService;

	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(FINANCIALAPPROVAL_AUTO_SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {


		SearchProcessClaimFinancialsTableDTO searchFormDTO = (SearchProcessClaimFinancialsTableDTO) parameters.getPrimaryParameter();

		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);

		Page<SearchProcessClaimFinancialsTableDTO> search = searchService.search(searchFormDTO,userName,passWord);

		view.list(search);
	}

	@Override
	public void viewEntered() {

	}
	
	@SuppressWarnings({ "deprecation" })
	public void getHoldTableList(@Observes @CDIEvent(SEARCH_FOR_FA_HOLD_TABLE_LIST) final ParameterDTO parameters) {
		
		SearchProcessClaimFinancialsTableDTO searchFormDTO = new SearchProcessClaimFinancialsTableDTO();
		String userName =(String)parameters.getPrimaryParameter();
		String passWord=(String)parameters.getSecondaryParameter(0, String.class);		
		
		List<SearchHoldMonitorScreenTableDTO> holdClaims = dbCalculationService.getHoldMonitorClaimsForBillingFA(null, 
				userName, SHAConstants.FA_CURRENT_QUEUE,SHAConstants.AUTO_ALLOCATION_ALLOCATION_MENU_TYPE,null,null);

		view.setHoldTableList(holdClaims);
	}

}
