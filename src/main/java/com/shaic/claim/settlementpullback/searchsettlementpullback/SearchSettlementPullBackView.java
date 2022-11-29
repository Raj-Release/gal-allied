package com.shaic.claim.settlementpullback.searchsettlementpullback;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.settlementpullback.dto.SearchSettlementPullBackDTO;

public interface SearchSettlementPullBackView extends Searchable {

	public void list(Page<SearchSettlementPullBackDTO> search);

}