package com.shaic.claim.reimbursement.commonBillingFA;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsTableDTO;


/**
 * @author ntv.narenj
 *
 */
public interface SearchProcessClaimCommonBillingAndFinancialsView extends Searchable  {
	public void list(Page<SearchProcessClaimFinancialsTableDTO> tableRows);
}
