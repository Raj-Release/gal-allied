package com.shaic.paclaim.health.reimbursement.financial.search;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;


/**
 *
 *
 */
public interface PAHealthSearchProcessClaimFinancialsView extends Searchable  {
	public void list(Page<PAHealthSearchProcessClaimFinancialsTableDTO> tableRows);
}
