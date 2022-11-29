package com.shaic.claim.reports.provisionhistory;

import java.util.List;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.settlementpullback.dto.SearchSettlementPullBackDTO;

public interface ProvisionHistoryView extends Searchable{

	public void list(List<ProvisionHistoryDTO> search);
}
