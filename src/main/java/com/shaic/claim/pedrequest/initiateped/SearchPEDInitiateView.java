package com.shaic.claim.pedrequest.initiateped;

import com.shaic.arch.GMVPView;
import com.shaic.arch.table.Page;

public interface SearchPEDInitiateView extends GMVPView {
	public void list(Page<SearchPEDInitiateTableDTO> tableRows);
	//public void resetResultTableValues();
}
