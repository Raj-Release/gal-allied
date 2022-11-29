package com.shaic.claim.fvrdetailedview;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author GokulPrasath.A
 *
 */
public class FvrNotRequiredDetailsTable extends GBaseTable<FvrDetailedViewDTO> {

	private static final long serialVersionUID = 1L;

	private static final Object[] NATURAL_COL_ORDER = new Object[] {
			"serialNumber", "fvrRemarksNotRequiredBy", "fvrNotRequiredUpdatedDateAndTime", "fvrNotRequiredRemarks", "claimStage" };

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<FvrDetailedViewDTO>(
				FvrDetailedViewDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size()+5);
	}

	@Override
	public void tableSelectHandler(FvrDetailedViewDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {
		return "fvr-not-required-details-";
	}

}
