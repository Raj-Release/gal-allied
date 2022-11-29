package com.shaic.claim.pcc.zonalCoordinator;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.pcc.dto.SearchProcessPCCRequestTableDTO;

public interface SearchPccZonalCoordinatorView extends Searchable {
	public void list(Page<SearchProcessPCCRequestTableDTO> list);
}
