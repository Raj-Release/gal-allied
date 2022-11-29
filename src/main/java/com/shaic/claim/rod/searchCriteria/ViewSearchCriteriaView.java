package com.shaic.claim.rod.searchCriteria;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;


public interface ViewSearchCriteriaView extends  Searchable {
	
	public void list(Page<ViewSearchCriteriaTableDTO> tableRows);

		

}
