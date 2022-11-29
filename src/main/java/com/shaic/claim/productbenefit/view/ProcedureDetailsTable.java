package com.shaic.claim.productbenefit.view;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ProcedureDetailsTable extends GBaseTable<ProcedureDetailsTableDTO>{
	
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"procedureDetails"};

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<ProcedureDetailsTableDTO>(
				ProcedureDetailsTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
	}

	@Override
	public String textBundlePrefixString() {
		return "procedure-details-";
	}

	@Override
	public void tableSelectHandler(ProcedureDetailsTableDTO t) {
		// TODO Auto-generated method stub
		
	}
	

}
