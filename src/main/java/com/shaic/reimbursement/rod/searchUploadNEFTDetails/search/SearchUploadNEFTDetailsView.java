package com.shaic.reimbursement.rod.searchUploadNEFTDetails.search;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface SearchUploadNEFTDetailsView extends Searchable{

	public void list(Page<SearchUploadNEFTDetailsTableDTO> tableRows);
	public void validation();
}
