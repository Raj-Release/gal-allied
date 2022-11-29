package com.shaic.paclaim.manageclaim.healthreopenclaim.searchRodLevel;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;


/**
 * @author ntv.narenj
 *
 */
public interface PAHealthSearchReOpenClaimRodLevelView extends Searchable  {
	public void list(Page<PAHealthSearchReOpenClaimRodLevelTableDTO> tableRows);
}
