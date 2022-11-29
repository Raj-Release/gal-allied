package com.shaic.claim.search;

import com.shaic.arch.GMVPView;
import com.shaic.arch.table.Page;

public interface SearchClaimView extends GMVPView {
	public void list(Page<SearchClaimTableDTO> tableRows);	

}
