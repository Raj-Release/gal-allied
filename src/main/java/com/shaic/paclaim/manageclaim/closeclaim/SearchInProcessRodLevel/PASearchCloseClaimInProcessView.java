package com.shaic.paclaim.manageclaim.closeclaim.SearchInProcessRodLevel;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.paclaim.manageclaim.closeclaim.searchRodLevel.PASearchCloseClaimTableDTORODLevel;

public interface PASearchCloseClaimInProcessView extends Searchable{
	
	public void list(Page<PASearchCloseClaimTableDTORODLevel> tableRows);
	
}
