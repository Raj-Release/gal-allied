package com.shaic.claim.OMPReopenClaimClaimLevel.SearchBased.search;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface OMPReopenClaimClaimLevelSearchBasedView extends Searchable{
	
	public void list(Page<OMPReopenClaimClaimLevelSearchBasedFormDto> tableRows);
	public void init(BeanItemContainer<SelectValue> parameter,
			BeanItemContainer<SelectValue> selectValueForPriority,
		BeanItemContainer<SelectValue> statusByStage,BeanItemContainer<SelectValue> selectValueForUploadedDocument);


}
