package com.shaic.paclaim.reimbursement.processdraftquery;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.reimbursement.queryrejection.processdraftquery.search.SearchProcessDraftQueryTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;


/**
 * 
 *
 */
public interface SearchProcessDraftPAQueryView extends Searchable  {
	public void list(Page<SearchProcessDraftQueryTableDTO> tableRows);
	public void init(BeanItemContainer<SelectValue> parameter,
			BeanItemContainer<SelectValue> selectValueForPriority,
			BeanItemContainer<SelectValue> statusByStage);
}
