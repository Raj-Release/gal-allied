package com.shaic.claim.reimbursement.opscreen;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface OpView extends Searchable{
	
	void list(Page<OpTableDTO> tableRows);

	void init(BeanItemContainer<SelectValue> zonetype);

	void buildQueryLayout();

	void buildApproveLayout();

	void buildRejectLayout();
	
	

}
