package com.shaic.claim.reimbursement.processClaimRequestAutoAllocation;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.table.Page;
import com.shaic.reimbursement.manageclaim.HoldMonitorScreen.SearchHoldMonitorScreenTableDTO;
import com.shaic.reimbursement.medicalapproval.processclaimrequest.search.SearchProcessClaimRequestTableDTO;

public interface SearchProcessClaimRequestAutoAllocationView extends GMVPView{
	public void list(Page<SearchProcessClaimRequestTableDTO> tableRows);
	public void init();
	void manuallyDoSearch();
	void manuallyDoSearchForCompletedCases();
	void setHoldTableList(List<SearchHoldMonitorScreenTableDTO> holdClaimsList);

}
