package com.shaic.claim.fieldvisit.search;

import com.shaic.arch.GMVPView;
import com.shaic.arch.table.Page;

public interface SearchFieldVisitView extends GMVPView {
	
	public void list(Page<SearchFieldVisitTableDTO> tableRows);
	
	/*void setView(Class<? extends MVPView> viewClass, boolean selectInNavigationTree,ParameterDTO parameter);*/
	
	

}
