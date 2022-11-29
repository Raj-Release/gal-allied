package com.shaic.claim.pedrequest.approve.bancspedQuery;

import com.shaic.arch.GMVPView;
import com.shaic.arch.table.Page;

public interface BancsSearchPEDRequestApproveView extends GMVPView {
	public void list(Page<BancsSearchPEDRequestApproveTableDTO> tableRows);	

}
