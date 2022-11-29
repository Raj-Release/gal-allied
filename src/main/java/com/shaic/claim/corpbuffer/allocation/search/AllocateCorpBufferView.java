package com.shaic.claim.corpbuffer.allocation.search;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface AllocateCorpBufferView extends Searchable  {
	
	public void init();
	
	public void list(Page<AllocateCorpBufferTableDTO> tableRows);
	
}
