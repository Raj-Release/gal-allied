package com.shaic.claim.reports.shadowProvision;

import java.util.List;

import com.shaic.arch.GMVPView;

public interface SearchShowdowView extends GMVPView {


	void init();

	void exportToExcelList(
			List<SearchShadowProvisionDTO> errorLogDetailsForShadow);

}
