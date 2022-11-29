package com.shaic.claim.pcc.reviewer;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.pcc.dto.SearchProcessPCCRequestTableDTO;

public interface SearchPccReviewerView extends Searchable {
	
	public void list(Page<SearchProcessPCCRequestTableDTO> list);

}
