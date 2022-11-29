package com.shaic.reimbursement.manageclaim.reopenclaim.sarchClaimLevel;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;


/**
 * 
 *
 */
public interface SearchReOpenClaimView extends Searchable  {
	public void resultList(Page<SearchReOpenClaimTableDTO> tableRows);
}
