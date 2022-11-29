package com.shaic.claim.lumen.searchlevelone;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.lumen.create.LumenRequestDTO;

public interface SearchLumenLevelOneRequestView extends Searchable{
	public void renderTable(Page<LumenRequestDTO> tableRows);
}
