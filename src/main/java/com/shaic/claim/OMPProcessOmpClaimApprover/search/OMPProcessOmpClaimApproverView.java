package com.shaic.claim.OMPProcessOmpClaimApprover.search;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface OMPProcessOmpClaimApproverView extends Searchable{
	
	
	public void list(Page<OMPProcessOmpClaimApproverTableDTO> tableRows);
	public void init(BeanItemContainer<SelectValue> classification);

}
