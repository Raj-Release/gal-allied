package com.shaic.claim.cashlessprocess.flagreconsiderationrequest.showwizard;

import com.shaic.arch.GMVPView;
import com.shaic.claim.cashlessprocess.flagreconsiderationrequest.search.SearchFlagReconsiderationReqTableDTO;
import com.shaic.claim.clearcashless.dto.SearchClearCashlessDTO;

public interface ReconsiderationFlagRequestView extends GMVPView {
	public void initView(SearchFlagReconsiderationReqTableDTO searchResult);

	public void buildSuccessLayout();
	public void buildFailureLayout();
}
