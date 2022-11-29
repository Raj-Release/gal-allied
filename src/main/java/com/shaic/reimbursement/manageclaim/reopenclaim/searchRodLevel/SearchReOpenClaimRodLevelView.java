package com.shaic.reimbursement.manageclaim.reopenclaim.searchRodLevel;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;


/**
 * @author ntv.narenj
 *
 */
public interface SearchReOpenClaimRodLevelView extends Searchable  {
	public void list(Page<SearchReOpenClaimRodLevelTableDTO> tableRows);
}
