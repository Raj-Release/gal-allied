package com.shaic.gpaclaim.unnamedriskdetails;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface SearchUnnamedRiskDetailsView extends Searchable{
	public void list(Page<SearchUnnamedRiskDetailsTableDTO> tableRows);
}
