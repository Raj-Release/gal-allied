package com.shaic.claim.reimbursement.FinancialApprovalAutoAllocation;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.table.Page;
import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsTableDTO;
import com.shaic.reimbursement.manageclaim.HoldMonitorScreen.SearchHoldMonitorScreenTableDTO;

public interface SearchProcessFinancialApprovalAutoAllocationView extends GMVPView{
	public void list(Page<SearchProcessClaimFinancialsTableDTO> tableRows);
	public void init();
	void manuallyDoSearch();
	void manuallyDoSearchForCompletedCases();
	void setHoldTableList(List<SearchHoldMonitorScreenTableDTO> holdClaimsList);
}
