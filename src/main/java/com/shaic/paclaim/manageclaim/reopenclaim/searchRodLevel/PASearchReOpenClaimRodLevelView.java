package com.shaic.paclaim.manageclaim.reopenclaim.searchRodLevel;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;


/**
 * @author ntv.narenj
 *
 */
public interface PASearchReOpenClaimRodLevelView extends Searchable  {
	public void list(Page<PASearchReOpenClaimRodLevelTableDTO> tableRows);
}
