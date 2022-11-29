package com.shaic.claim.aadhar.search;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface SearchUpdateAadharView extends Searchable{
	
	public void init();
	
	public void list(Page<SearchUpdateAadharTableDTO> tableRows);
	
	public void showErrorPopUp(String emsg);
}
