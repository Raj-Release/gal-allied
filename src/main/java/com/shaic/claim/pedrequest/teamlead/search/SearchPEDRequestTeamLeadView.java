package com.shaic.claim.pedrequest.teamlead.search;

import com.shaic.arch.GMVPView;
import com.shaic.arch.table.Page;
import com.shaic.claim.pedrequest.approve.search.SearchPEDRequestApproveTableDTO;

public interface SearchPEDRequestTeamLeadView extends GMVPView {
	public void list(Page<SearchPEDRequestApproveTableDTO> tableRows);	

}
