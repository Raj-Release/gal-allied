package com.shaic.reimbursement.rod.cancelAcknowledgment.search;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.reimbursement.rod.createrod.search.SearchCreateRODTableDTO;

public interface SearchCancelAcknowledgementView extends Searchable {

	public void list(Page<SearchCreateRODTableDTO> search);

}
