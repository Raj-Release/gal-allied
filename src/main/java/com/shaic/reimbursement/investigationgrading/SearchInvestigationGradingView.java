package com.shaic.reimbursement.investigationgrading;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.reimbursement.investigation.assigninvestigation.search.SearchAssignInvestigationTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface SearchInvestigationGradingView extends Searchable{


	public void list(Page<SearchAssignInvestigationTableDTO> tableRows);
	
	public void init(BeanItemContainer<SelectValue> cpuCode,
			BeanItemContainer<SelectValue> claimTypeContainer,
			BeanItemContainer<SelectValue> selectValueForPriority,
			BeanItemContainer<SelectValue> statusByStage);

}
