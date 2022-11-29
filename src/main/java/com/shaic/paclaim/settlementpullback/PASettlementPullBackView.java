package com.shaic.paclaim.settlementpullback;

import com.shaic.arch.GMVPView;
import com.shaic.paclaim.settlementpullback.dto.PASearchSettlementPullBackDTO;

public interface PASettlementPullBackView extends GMVPView {
	public void initView(PASearchSettlementPullBackDTO searchResult);

	public void buildSuccessLayout();
	public void buildFailureLayout();
}
