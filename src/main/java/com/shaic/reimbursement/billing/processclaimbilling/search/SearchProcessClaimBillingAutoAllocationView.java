package com.shaic.reimbursement.billing.processclaimbilling.search;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.table.Page;
import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsTableDTO;
import com.shaic.reimbursement.manageclaim.HoldMonitorScreen.SearchHoldMonitorScreenTableDTO;

public interface SearchProcessClaimBillingAutoAllocationView extends GMVPView{
	public void list(Page<SearchProcessClaimBillingTableDTO> tableRows);
	public void init();
	void manuallyDoSearch();
	void manuallyDoSearchForCompletedCases();
	void setHoldTableList(List<SearchHoldMonitorScreenTableDTO> holdClaimsList);
}
