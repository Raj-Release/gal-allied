package com.shaic.reimbursement.manageclaim.closeclaim.searchRodLevel;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;


/**
 * @author ntv.narenj
 *
 */
public interface SearchCloseClaimRODLevelView extends Searchable  {
	public void list(Page<SearchCloseClaimTableDTORODLevel> tableRows);
}
