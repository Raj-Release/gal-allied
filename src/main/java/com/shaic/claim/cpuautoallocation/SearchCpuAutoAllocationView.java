package com.shaic.claim.cpuautoallocation;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;


public interface SearchCpuAutoAllocationView extends Searchable {
	
	void buildSuccessLayout(Integer count);
	
	public void getResultList(Page<SearchCpuAutoAllocationTableDTO> tableRows);
	
	public void setCpuDetails(SearchCpuAutoAllocationTableDTO viewSearchCriteriaDTO);
	
	public void buildSubmitLayout();
	
}
