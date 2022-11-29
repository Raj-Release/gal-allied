package com.shaic.claim.medical.opinion;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface OpinionValidationView extends Searchable  {
	
	public void init(BeanItemContainer<SelectValue> cpuCode) ;
	public void list(Page<OpinionValidationTableDTO> tableRows);
	public void completedCaseList(Page<OpinionValidationTableDTO> tabOpinionValidationTableDTOList);
	
}
