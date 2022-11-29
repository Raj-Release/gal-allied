package com.shaic.reimbursement.manageclaim.searchClaimwiseApproval;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface SearchClaimWiseAllowApprovalView extends Searchable{
	public void list(Page<SearchClaimWiseAllowApprovalDto> tableRows);

}
