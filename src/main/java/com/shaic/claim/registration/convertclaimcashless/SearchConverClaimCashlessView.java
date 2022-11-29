package com.shaic.claim.registration.convertclaimcashless;

import com.shaic.arch.GMVPView;
import com.shaic.arch.table.Page;

public interface SearchConverClaimCashlessView extends GMVPView {
	public void list(Page<SearchConverClaimCashlessTableDTO> tableRows);
	//public void resetResultTableValues();
}
