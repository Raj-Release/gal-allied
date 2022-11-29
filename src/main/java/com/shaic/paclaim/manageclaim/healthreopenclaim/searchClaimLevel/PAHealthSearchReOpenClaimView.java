package com.shaic.paclaim.manageclaim.healthreopenclaim.searchClaimLevel;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;


/**
 * 
 *
 */
public interface PAHealthSearchReOpenClaimView extends Searchable  {
	public void resultList(Page<PAHealthSearchReOpenClaimTableDTO> tableRows);
}
