package com.shaic.claim.pincodemapping;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ZoneClassHistoryTable extends GBaseTable<ZoneClassHistoryDto> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber", "createdBy", "createdDate", "remarks" };*/
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<ZoneClassHistoryDto>(
				ZoneClassHistoryDto.class));
		Object[] NATURAL_COL_ORDER = new Object[] {
			"serialNumber", "createdBy", "createdDate", "remarks" };
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("350px");
		table.setWidth("100%");

	}

	@Override
	public void tableSelectHandler(ZoneClassHistoryDto t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-zone-class-history-";
	}

}
