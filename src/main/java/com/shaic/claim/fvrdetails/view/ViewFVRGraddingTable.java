package com.shaic.claim.fvrdetails.view;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewFVRGraddingTable extends GBaseTable<ViewFVRDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Object NATURAL_COL_ORDER[] = new Object[] { "sno", "category",
			"applicability", "status" };

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<ViewFVRDTO>(
				ViewFVRDTO.class));
		table.setPageLength(8);
		table.setVisibleColumns(NATURAL_COL_ORDER);
	}

	@Override
	public void tableSelectHandler(ViewFVRDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {
		return "fvr-grading-";
	}

}
