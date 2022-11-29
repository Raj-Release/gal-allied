package com.shaic.claim.edithospitalInfo.search;

import com.shaic.arch.GMVPView;
import com.shaic.arch.table.Page;

public interface SearchEditHospitalDetailsView extends GMVPView {

	public void list(Page<SearchEditHospitalDetailsTableDTO> tableRows);

}

