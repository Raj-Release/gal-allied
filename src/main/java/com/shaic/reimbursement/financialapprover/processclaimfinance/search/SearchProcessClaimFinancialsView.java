package com.shaic.reimbursement.financialapprover.processclaimfinance.search;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;


/**
 * @author ntv.narenj
 *
 */
public interface SearchProcessClaimFinancialsView extends Searchable  {
	public void list(Page<SearchProcessClaimFinancialsTableDTO> tableRows);
}
