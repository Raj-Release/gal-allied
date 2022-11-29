package com.shaic.claim.pedrequest.approve.search;

import com.shaic.arch.GMVPView;
import com.shaic.arch.table.Page;

public interface SearchPEDRequestApproveView extends GMVPView {
	public void list(Page<SearchPEDRequestApproveTableDTO> tableRows);	

}
