package com.shaic.reimbursement.rod.allowReconsideration.search;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.reimbursement.rod.createrod.search.SearchCreateRODTableDTO;

public interface SearchAllowReconsideration extends Searchable {
	
	public void list(Page<SearchAllowReconsiderationTableDTO> search);
	
	public void setallowedRejectionReconsider(SearchAllowReconsiderationTableDTO reconsiderAllowDto);
	
	public void buildSuccessLayout();
}
