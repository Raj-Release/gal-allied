package com.shaic.reimbursement.misc.processclaimrefund.search;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;


/**
 * @author ntv.narenj
 *
 */
public interface SearchProcessClaimRefundView extends Searchable  {
	public void list(Page<SearchProcessClaimRefundTableDTO> tableRows);
}
