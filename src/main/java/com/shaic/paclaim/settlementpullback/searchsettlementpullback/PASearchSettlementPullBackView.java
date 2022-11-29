package com.shaic.paclaim.settlementpullback.searchsettlementpullback;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.paclaim.settlementpullback.dto.PASearchSettlementPullBackDTO;

public interface PASearchSettlementPullBackView extends Searchable {

	public void list(Page<PASearchSettlementPullBackDTO> search);

}