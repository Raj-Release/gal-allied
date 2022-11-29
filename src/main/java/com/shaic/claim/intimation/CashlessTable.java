package com.shaic.claim.intimation;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class CashlessTable extends GBaseTable<CashLessTableDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Object[] NATURAL_COL_ORDER = new Object[] {
			"cashLessType", "responseDate", "replyStatus", "authApprovedAmt",
			"reason", "doctorNote" };

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<CashLessTableDTO>(
				CashLessTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size()+5);
	}

	@Override
	public void tableSelectHandler(CashLessTableDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {
		return "cash-less-table-";
	}

}
