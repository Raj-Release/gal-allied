package com.shaic.claim.pcc.wizard;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.pcc.dto.SearchProcessPCCRequestTableDTO;

public interface SearchPccView extends Searchable {
	public void list(Page<SearchProcessPCCRequestTableDTO> list);

}
