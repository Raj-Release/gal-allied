package com.shaic.paclaim.reimbursement.processdraftrejection;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.reimbursement.queryrejection.processdraftrejection.search.SearchProcessDraftRejectionTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;


/**
 * 
 *
 */
public interface SearchProcessDraftPARejectionView extends Searchable  {
	
	public void list(Page<SearchProcessDraftRejectionTableDTO> tableRows);
	
	public void init(BeanItemContainer<SelectValue> parameter,
			BeanItemContainer<SelectValue> selectValueForPriority,
			BeanItemContainer<SelectValue> statusByStage);
}
