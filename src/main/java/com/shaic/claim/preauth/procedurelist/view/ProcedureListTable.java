package com.shaic.claim.preauth.procedurelist.view;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ProcedureListTable extends GBaseTable<ProcedureListTableDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber",
			"procedureName", "procedureCode", "packageRate", "approvedRate",
			"reference" };*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<ProcedureListTableDTO>(
				ProcedureListTableDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber",
			"procedureName", "procedureCode", "packageRate", "approvedRate",
			"reference" };
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size() + 5);
	}

	@Override
	public void tableSelectHandler(ProcedureListTableDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {
		return "procedure-";
	}

}
