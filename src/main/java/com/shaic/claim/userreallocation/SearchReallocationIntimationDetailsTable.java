package com.shaic.claim.userreallocation;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class SearchReallocationIntimationDetailsTable extends GBaseTable<AutoAllocationDetailsTableDTO> {
	
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"sNumber","intimationNo","doctorId","doctorName"};

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		table.removeAllItems();
	}

	@Override
	public void initTable() {
		// TODO Auto-generated method stub
		table.setContainerDataSource(new BeanItemContainer<AutoAllocationDetailsTableDTO>(AutoAllocationDetailsTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
	}

	@Override
	public void tableSelectHandler(AutoAllocationDetailsTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "search-intimation-re-allocation-";
	}
	
	public void removeTableItems(){
		this.table.removeAllItems();
	}
	
}
