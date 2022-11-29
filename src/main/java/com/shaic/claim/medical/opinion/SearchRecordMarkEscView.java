package com.shaic.claim.medical.opinion;

import com.shaic.arch.GMVPView;
import com.shaic.arch.table.Page;
import com.shaic.claim.pedrequest.initiateped.SearchPEDInitiateTableDTO;

public interface SearchRecordMarkEscView extends GMVPView {
	public void list(Page<SearchRecordMarkEscTableDTO> tableRows);
	
}
