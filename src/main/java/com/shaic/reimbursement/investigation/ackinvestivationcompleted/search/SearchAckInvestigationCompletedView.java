package com.shaic.reimbursement.investigation.ackinvestivationcompleted.search;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;


/**
 * @author ntv.narenj
 *
 */
public interface SearchAckInvestigationCompletedView extends Searchable  {
	public void list(Page<SearchAckInvestigationCompletedTableDTO> tableRows);
	
	public void init(BeanItemContainer<SelectValue> parameter,
			BeanItemContainer<SelectValue> claimTypeContainer,
			BeanItemContainer<SelectValue> selectValueForPriority,
			BeanItemContainer<SelectValue> statusByStage);
}
