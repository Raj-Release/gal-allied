package com.shaic.claim.pincodemapping;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;


public interface SearchPinCodeView extends Searchable {
	
	void buildSuccessLayout(Integer count);
	
	public void getResultList(Page<SearchPinCodeTableDTO> tableRows);
	
	public void buildSubmitLayout();
	
}
