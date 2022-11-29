package com.shaic.claim.status.view;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewStatusTable extends GBaseTable<ViewStatusDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {
			"referenceNo", "diagnosis", "procedure", "reqAmt", "status",
			"approvedAmt", "approvalRemarks" };
*/
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<ViewStatusDTO>(
				ViewStatusDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {
			"referenceNo", "diagnosis", "procedure", "reqAmt", "status",
			"approvedAmt", "approvalRemarks" };

		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size() + 5);
	}

	@Override
	public void tableSelectHandler(ViewStatusDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {
		return "view-status-";
	}

}
