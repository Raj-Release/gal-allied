package com.shaic.claim.adviseonped.search;

import com.shaic.arch.GMVPView;
import com.shaic.arch.table.Page;

public interface SearchAdviseOnPEDView extends GMVPView {
	public void list(Page<SearchAdviseOnPEDTableDTO> tableRows);	

}
