package com.shaic.reimbursement.manageclaim.closeclaim.searchBasedClaimlevel;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;


/**
 * 
 *
 */
public interface SearchCloseClaimView extends Searchable  {
	public void getResultList(Page<SearchCloseClaimTableDTO> tableRows);
}
