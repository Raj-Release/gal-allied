package com.shaic.claim.settlementpullback;

import com.shaic.arch.GMVPView;
import com.shaic.claim.settlementpullback.dto.SearchSettlementPullBackDTO;

public interface SettlementPullBackView extends GMVPView {
	public void initView(SearchSettlementPullBackDTO searchResult);

	public void buildSuccessLayout();
	public void buildFailureLayout();
}
