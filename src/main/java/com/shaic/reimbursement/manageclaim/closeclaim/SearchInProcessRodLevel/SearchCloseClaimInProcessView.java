package com.shaic.reimbursement.manageclaim.closeclaim.SearchInProcessRodLevel;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.reimbursement.manageclaim.closeclaim.searchRodLevel.SearchCloseClaimTableDTORODLevel;

public interface SearchCloseClaimInProcessView extends Searchable{
	
	public void list(Page<SearchCloseClaimTableDTORODLevel> tableRows);
	
}
