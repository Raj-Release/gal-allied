package com.shaic.reimbursement.queryrejection.processdraftrejection.search;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;


/**
 * @author ntv.narenj
 *
 */
public interface SearchProcessDraftRejectionView extends Searchable  {
	
	public void list(Page<SearchProcessDraftRejectionTableDTO> tableRows);
	
	public void init(BeanItemContainer<SelectValue> parameter,
			BeanItemContainer<SelectValue> selectValueForPriority,
			BeanItemContainer<SelectValue> statusByStage);
}
