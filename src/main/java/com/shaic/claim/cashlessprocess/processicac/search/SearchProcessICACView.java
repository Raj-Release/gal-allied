package com.shaic.claim.cashlessprocess.processicac.search;

import com.shaic.arch.GMVPView;
import com.shaic.arch.table.Page;

public interface SearchProcessICACView extends GMVPView {
	
	public void list(Page<SearchProcessICACTableDTO> tableRows);
	
	//public void resetView();
	
	/*void setView(Class<? extends MVPView> viewClass, boolean selectInNavigationTree,ParameterDTO parameter);*/

}
