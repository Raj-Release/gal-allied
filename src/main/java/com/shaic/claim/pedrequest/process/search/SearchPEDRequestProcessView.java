package com.shaic.claim.pedrequest.process.search;

import com.shaic.arch.GMVPView;
import com.shaic.arch.table.Page;

public interface SearchPEDRequestProcessView extends GMVPView {
	public void list(Page<SearchPEDRequestProcessTableDTO> tableRows);
	//public void resetResultTableValues();
}
