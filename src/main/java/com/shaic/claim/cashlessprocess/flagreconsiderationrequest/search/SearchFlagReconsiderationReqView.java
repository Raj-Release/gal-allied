package com.shaic.claim.cashlessprocess.flagreconsiderationrequest.search;

import com.shaic.arch.GMVPView;
import com.shaic.arch.table.Page;

public interface SearchFlagReconsiderationReqView extends GMVPView {
	
	public void list(Page<SearchFlagReconsiderationReqTableDTO> tableRows);
	
	//public void resetView();
	
	/*void setView(Class<? extends MVPView> viewClass, boolean selectInNavigationTree,ParameterDTO parameter);*/

}
