package com.shaic.claim.reimbursement.rawanalysis;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface SearchProcessRawRequestView extends Searchable{
	
	public void list(Page<SearchProcessRawRequestTableDto> list);
}
