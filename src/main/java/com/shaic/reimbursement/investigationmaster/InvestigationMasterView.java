package com.shaic.reimbursement.investigationmaster;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.shaic.reimbursement.investigation.assigninvestigation.search.SearchAssignInvestigationTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface InvestigationMasterView extends Searchable{
	
	public void list(Page<InvestigationMasterTableDTO> tableRows);
	
	public void init(BeanItemContainer<SelectValue> investigatorTypeContainer,BeanItemContainer<SelectValue> investigatorNameContainer,
			BeanItemContainer<SelectValue> privateInvestigatorNameContainer);
}
