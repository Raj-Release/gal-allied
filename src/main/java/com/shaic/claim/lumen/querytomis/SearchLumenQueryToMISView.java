package com.shaic.claim.lumen.querytomis;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.lumen.create.LumenRequestDTO;

public interface SearchLumenQueryToMISView extends Searchable{
	public void renderTable(Page<LumenRequestDTO> tableRows);
}
