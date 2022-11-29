package com.shaic.claim.intimation.search;

import com.shaic.arch.table.Searchable;
import com.vaadin.ui.Button.ClickEvent;

public interface SearchIntimationView extends Searchable {

	public void showIntimationDetailsView(ClickEvent event);
//	public void showSearchIntimationTable(Map<String,Object> filters);
	public void showSearchIntimationTable(SearchIntimationFormDto searchDto);
	public void resetSearchIntimationView();
	public void hideSearchFields(String valChanged);
}


