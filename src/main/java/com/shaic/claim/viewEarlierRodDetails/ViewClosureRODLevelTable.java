package com.shaic.claim.viewEarlierRodDetails;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewClosureRODLevelTable extends GBaseTable<ViewClosureDto> {

	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"sNo","closedDate","closedBy","reasonForClosure"};*/
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<ViewClosureDto>(
				ViewClosureDto.class));
		Object[] NATURAL_COL_ORDER = new Object[] {"sNo","closedDate","closedBy","reasonForClosure"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("180px");
		table.setWidth("100%");
}

	@Override
	public void tableSelectHandler(ViewClosureDto t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-closure-details-rod-level-";
	}

}
