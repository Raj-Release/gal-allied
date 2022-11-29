package com.shaic.claim.viewEarlierRodDetails.Table;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.viewEarlierRodDetails.dto.FieldVisitDetailsTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class FieldVisitDetailsTable extends GBaseTable<FieldVisitDetailsTableDTO> {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"sno","representiveName","remarks","fvrAssignedDate","fvrReceivedDate","status"};*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		table.removeAllItems();
	}

	@Override
	public void initTable() {
		table.removeAllItems();
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<FieldVisitDetailsTableDTO>(
				FieldVisitDetailsTableDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {"sno","representiveName","remarks","fvrAssignedDate","fvrReceivedDate","status"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size() + 4);
		table.setHeight("200px");

	}

	@Override
	public String textBundlePrefixString() {
		
		return "field-visit-table-";
	}

	@Override
	public void tableSelectHandler(FieldVisitDetailsTableDTO t) {
		// TODO Auto-generated method stub
		
	}

}
