package com.shaic.reimbursement.processi_investigationi_initiated.search;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author ntv.narenj
 *
 */
public interface SearchProcessInvestigationInitiatedView extends Searchable {
	public void list(Page<SearchProcessInvestigationInitiatedTableDTO> tableRows);
	
	public void init(BeanItemContainer<SelectValue> cpuCode,
			BeanItemContainer<SelectValue> claimTypeContainer,
			BeanItemContainer<SelectValue> selectValueForPriority,
			BeanItemContainer<SelectValue> statusByStage);
}
