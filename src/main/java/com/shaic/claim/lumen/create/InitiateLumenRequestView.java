package com.shaic.claim.lumen.create;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface InitiateLumenRequestView extends Searchable{
	public void renderTable(Page<LumenSearchResultTableDTO> tableRows);
	public void renderPolicyTable(Page<LumenPolicySearchResultTableDTO> policySearchResult);
}
