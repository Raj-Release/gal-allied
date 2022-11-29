package com.shaic.paclaim.healthsettlementpullback;

import com.shaic.arch.GMVPView;
import com.shaic.paclaim.healthsettlementpullback.dto.PAHospSearchSettlementPullBackDTO;

public interface PAHospSettlementPullBackView extends GMVPView {
	public void initView(PAHospSearchSettlementPullBackDTO searchResult);

	public void buildSuccessLayout();
	public void buildFailureLayout();
}
