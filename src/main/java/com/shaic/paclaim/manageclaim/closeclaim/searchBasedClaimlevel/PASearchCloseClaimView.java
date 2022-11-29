package com.shaic.paclaim.manageclaim.closeclaim.searchBasedClaimlevel;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;


/**
 * 
 *
 */
public interface PASearchCloseClaimView extends Searchable  {
	public void getResultList(Page<PASearchCloseClaimTableDTO> tableRows);
}
