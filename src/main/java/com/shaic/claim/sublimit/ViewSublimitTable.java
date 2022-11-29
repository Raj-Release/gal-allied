package com.shaic.claim.sublimit;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewSublimitTable extends GBaseTable<ViewSublimitDTO> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","name", "amount","amntUtilisedIncludingCurrentClaim","availableBalanceIncludingCurrentClaim",
		"amntUtilisedExcludingCurrentClaim","availableBalanceExcludingCurrentClaim"}; 

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		setSizeFull();
        table.setContainerDataSource(new BeanItemContainer<ViewSublimitDTO>(ViewSublimitDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);		
	}

	@Override
	public void tableSelectHandler(ViewSublimitDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "view-sub-limit-";
	}

}
