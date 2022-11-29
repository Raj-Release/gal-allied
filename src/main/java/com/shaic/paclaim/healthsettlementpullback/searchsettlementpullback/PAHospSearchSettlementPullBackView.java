package com.shaic.paclaim.healthsettlementpullback.searchsettlementpullback;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.paclaim.healthsettlementpullback.dto.PAHospSearchSettlementPullBackDTO;

public interface PAHospSearchSettlementPullBackView extends Searchable {

	public void list(Page<PAHospSearchSettlementPullBackDTO> search);

}