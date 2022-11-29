package com.shaic.paclaim.financial.nonhospprocessclaimfinancial.search;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsTableDTO;


/**
 * 
 */
public interface PASearchProcessClaimFinancialsNonHospView extends Searchable  {
	public void list(Page<SearchProcessClaimFinancialsTableDTO> tableRows);
}
