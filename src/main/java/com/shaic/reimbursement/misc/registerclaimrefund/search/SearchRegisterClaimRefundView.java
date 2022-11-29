package com.shaic.reimbursement.misc.registerclaimrefund.search;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;


/**
 * @author ntv.narenj
 *
 */
public interface SearchRegisterClaimRefundView extends Searchable  {
	public void list(Page<SearchRegisterClaimRefundTableDTO> tableRows);
}
