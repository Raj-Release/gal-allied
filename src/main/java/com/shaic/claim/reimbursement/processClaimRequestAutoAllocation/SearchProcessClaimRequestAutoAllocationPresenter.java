package com.shaic.claim.reimbursement.processClaimRequestAutoAllocation;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.Page;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsTableDTO;
import com.shaic.reimbursement.manageclaim.HoldMonitorScreen.SearchHoldMonitorScreenTableDTO;
import com.shaic.reimbursement.medicalapproval.processclaimrequest.search.SearchProcessClaimRequestTableDTO;

@ViewInterface(SearchProcessClaimRequestAutoAllocationView.class)
public class SearchProcessClaimRequestAutoAllocationPresenter  extends AbstractMVPPresenter<SearchProcessClaimRequestAutoAllocationView>{


	private static final long serialVersionUID = -5504472929540762973L;

	public static final String PROCESS_CLAIM_REQUEST_AUTO_SEARCH_BUTTON_CLICK = "processClaimRequest_for_auto_allocation";
	
	public static final String SEARCH_PROCESS_CLAIM_REQUEST_FOR_COMPLETED_CASE = "search_for_ClaimRequest_completed_case for MA q";
	public static final String SEARCH_PROCESS_CLAIM_REQUEST_FOR_HOLD_TABLE_LIST = "search_for_ClaimRequest_hold_table_list";
	
	@EJB
	private SearchProcessClaimRequestAutoAllocationService searchService;
	

	@EJB
	private DBCalculationService dbCalculationService;

	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(PROCESS_CLAIM_REQUEST_AUTO_SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {


		SearchProcessClaimRequestTableDTO searchFormDTO = (SearchProcessClaimRequestTableDTO) parameters.getPrimaryParameter();

		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);

		Page<SearchProcessClaimRequestTableDTO> search = searchService.search(searchFormDTO,userName,passWord,searchFormDTO.getImsUser());

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
	public void getHoldTableList(@Observes @CDIEvent(SEARCH_PROCESS_CLAIM_REQUEST_FOR_HOLD_TABLE_LIST) final ParameterDTO parameters) {
		
		SearchProcessClaimFinancialsTableDTO searchFormDTO = new SearchProcessClaimFinancialsTableDTO();
		String userName =(String)parameters.getPrimaryParameter();
		String passWord=(String)parameters.getSecondaryParameter(0, String.class);		
		
		List<SearchHoldMonitorScreenTableDTO> holdClaims = dbCalculationService.getHoldMonitorClaimsForBillingFA(null, 
				userName, SHAConstants.MA_CURRENT_QUEUE,SHAConstants.AUTO_ALLOCATION_ALLOCATION_MENU_TYPE,null,null);

		view.setHoldTableList(holdClaims);
	}


}
