package com.shaic.arch.table;

import com.shaic.arch.GMVPView;

public interface Searchable extends GMVPView
{
	public void doSearch();
	public void resetSearchResultTableValues();
}
