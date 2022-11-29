package com.shaic.claim.OMPProcessOmpClaimProcessor.search;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface OMPProcessOmpClaimProcessorView extends Searchable{

	
	public void list(Page<OMPProcessOmpClaimProcessorTableDTO> page);

	public void init(BeanItemContainer<SelectValue> classification);
}
