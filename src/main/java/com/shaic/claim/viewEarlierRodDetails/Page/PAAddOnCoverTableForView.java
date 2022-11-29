package com.shaic.claim.viewEarlierRodDetails.Page;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class PAAddOnCoverTableForView extends GBaseTable<PAcoverTableViewDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] { "sNo",
		"cover", "claimedAmt" };*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<PAcoverTableViewDTO>(
				PAcoverTableViewDTO.class));
		 Object[] NATURAL_COL_ORDER = new Object[] { "sNo",
			"cover", "claimedAmt" };
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("200px");

	}

	@Override
	public void tableSelectHandler(PAcoverTableViewDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-addon-covers-";
	}

}
