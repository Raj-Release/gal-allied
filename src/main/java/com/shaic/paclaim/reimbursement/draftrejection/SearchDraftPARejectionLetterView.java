package com.shaic.paclaim.reimbursement.draftrejection;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.reimbursement.queryrejection.draftrejection.search.SearchDraftRejectionLetterTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;


/**
 * 
 *
 */
public interface SearchDraftPARejectionLetterView extends Searchable  {
	public void list(Page<SearchDraftRejectionLetterTableDTO> tableRows);

	public void init(BeanItemContainer<SelectValue> parameter,
			BeanItemContainer<SelectValue> selectValueForPriority,
			BeanItemContainer<SelectValue> statusByStage);
}
