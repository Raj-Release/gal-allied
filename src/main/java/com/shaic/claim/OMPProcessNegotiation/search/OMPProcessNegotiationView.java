package com.shaic.claim.OMPProcessNegotiation.search;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface OMPProcessNegotiationView extends Searchable{
	
	
	public void list(Page<OMPProcessNegotiationTableDTO> tableRows);
	public void init();

}
