package com.shaic.claim.viewEarlierRodDetails.Table;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.viewEarlierRodDetails.dto.ViewSectionDetailsTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;


public class ViewSectionDetailsTable extends GBaseTable<ViewSectionDetailsTableDTO>{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {
		"section", "cover", "subCover" };*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		table.removeAllItems();
	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<ViewSectionDetailsTableDTO>(
				ViewSectionDetailsTableDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {
			"section", "cover", "subCover" };
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("90px");
		table.setWidth("100%");

	}


	@Override
	public void tableSelectHandler(ViewSectionDetailsTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-section-";
	}

}
