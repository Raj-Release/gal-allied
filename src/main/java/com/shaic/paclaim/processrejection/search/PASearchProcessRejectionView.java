package com.shaic.paclaim.processrejection.search;

import com.shaic.arch.GMVPView;
import com.shaic.arch.table.Page;
import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;

public interface PASearchProcessRejectionView extends GMVPView {
	public void list(Page<SearchProcessRejectionTableDTO> tableRows);	

}
