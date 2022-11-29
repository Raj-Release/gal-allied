package com.shaic.claim.OMPCloseClaimRODLevel.SearchBased.search;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface OMPCloseClaimRODLevelSearchBasedView extends Searchable{
	
	public void list(Page<OMPCloseClaimRODLevelSearchBasedFormDto> tableRows);
	public void init(BeanItemContainer<SelectValue> parameter,
			BeanItemContainer<SelectValue> selectValueForPriority,
		BeanItemContainer<SelectValue> statusByStage,BeanItemContainer<SelectValue> selectValueForUploadedDocument);


}
