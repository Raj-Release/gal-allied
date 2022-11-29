package com.shaic.reimbursement.investigation.assigninvestigation.search;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;


/**
 * @author ntv.narenj
 *
 */
public interface SearchAssignInvestigationView extends Searchable  {
	public void list(Page<SearchAssignInvestigationTableDTO> tableRows);
	
	public void init(BeanItemContainer<SelectValue> cpuCode,
			BeanItemContainer<SelectValue> claimTypeContainer,
			BeanItemContainer<SelectValue> selectValueForPriority,
			BeanItemContainer<SelectValue> statusByStage,BeanItemContainer<SelectValue> statusByInvestigationState);
}
