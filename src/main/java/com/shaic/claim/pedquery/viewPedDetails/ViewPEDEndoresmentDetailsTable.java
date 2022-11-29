package com.shaic.claim.pedquery.viewPedDetails;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewPEDEndoresmentDetailsTable extends
		GBaseTable<ViewPEDEndoresementDetailsDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*public static final Object[] NATURAL_COL_ORDER = new Object[] { "pedCode",
			"description", "icdChapter", "icdBlock", "icdCode", "source",
			"othersSpecify", "doctorRemarks" };
*/
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<ViewPEDEndoresementDetailsDTO>(
				ViewPEDEndoresementDetailsDTO.class));
		 Object[] NATURAL_COL_ORDER = new Object[] { "pedCode",
			"description", "icdChapter", "icdBlock", "icdCode", "source",
			"othersSpecify", "doctorRemarks" };

		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size());
	}

	@Override
	public void tableSelectHandler(ViewPEDEndoresementDetailsDTO t) {
		// TODO Auto-generated method stub
	}

	@Override
	public String textBundlePrefixString() {
		return "view-ped-endorsement-details-";
	}

}
