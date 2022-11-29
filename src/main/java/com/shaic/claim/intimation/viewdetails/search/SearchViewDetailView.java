package com.shaic.claim.intimation.viewdetails.search;

import com.shaic.arch.table.Searchable;
import com.shaic.claim.intimation.search.SearchIntimationFormDto;
import com.vaadin.ui.Button.ClickEvent;

public interface SearchViewDetailView extends Searchable {

	public void showIntimationDetailsView(ClickEvent event);
//	public void showSearchIntimationTable(Map<String,Object> filters);
	public void showSearchViewDetailIntimationTable(SearchIntimationFormDto searchDto);
	public void resetSearchIntimationView();
	public void hideSearchFields(String valChanged);
	
}
