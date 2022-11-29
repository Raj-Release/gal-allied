package com.shaic.claim.OMPprocessrejection.search;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;

public interface SearchOMPProcessRejectionView extends Searchable{
	public void list(Page<SearchProcessRejectionTableDTO> tableRows);	
}
