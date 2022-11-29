package com.shaic.claim.OMPProcessOmpAcknowledgementDocuments.Upload;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.OMPProcessOmpClaimProcessor.search.OMPProcessOmpClaimProcessorTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface OMPProcessOmpAcknowledgementDocumentsView extends Searchable{

	
	public void list(Page<OMPProcessOmpAcknowledgementDocumentsTableDTO> page);

	//public void init(BeanItemContainer<SelectValue> classification);
}