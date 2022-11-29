package com.shaic.claim.lumen.search;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.lumen.create.LumenRequestDTO;

public interface SearchLumenRequestView extends Searchable{
	public void renderTable(Page<LumenRequestDTO> tableRows);
	public void showLumenTrails(LumenRequestDTO lumenRequestDTO);
}
