package com.shaic.claim.reassignfieldvisit.search;

import com.shaic.arch.GMVPView;
import com.shaic.arch.table.Page;

public interface SearchReAssignFieldVisitView extends GMVPView{
	
	public void list(Page<SearchReAssignFieldVisitTableDTO> tableRows);

}
