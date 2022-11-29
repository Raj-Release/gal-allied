package com.shaic.claim.settlementpullback.searchbatchpendingpullback;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface SearchLotPullBackView extends Searchable{
	public void list(Page<SearchLotPullBackTableDTO> search);
}
