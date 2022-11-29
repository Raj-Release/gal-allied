package com.shaic.paclaim.manageclaim.closeclaim.searchRodLevel;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;


/**
 * @author ntv.narenj
 *
 */
public interface PASearchCloseClaimRODLevelView extends Searchable  {
	public void list(Page<PASearchCloseClaimTableDTORODLevel> tableRows);
}
