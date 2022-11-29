package com.shaic.claim.misc.updatesublimit;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface SearchUpdateSublimitView extends Searchable{
	
	public void list(Page<SearchUpdateSublimitTableDTO> searchDTO);
}
