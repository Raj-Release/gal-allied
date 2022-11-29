package com.shaic.paclaim.manageclaim.reopenclaim.searchClaimLevel;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;


/**
 * 
 *
 */
public interface PASearchReOpenClaimView extends Searchable  {
	public void resultList(Page<PASearchReOpenClaimTableDTO> tableRows);
}
