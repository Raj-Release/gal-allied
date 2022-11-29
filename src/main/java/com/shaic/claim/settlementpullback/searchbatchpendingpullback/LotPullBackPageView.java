package com.shaic.claim.settlementpullback.searchbatchpendingpullback;

import com.shaic.arch.GMVPView;

public interface LotPullBackPageView extends GMVPView{
	public void initView(SearchLotPullBackTableDTO searchResult);

	public void buildSuccessLayout();
	public void buildFailureLayout();
}
