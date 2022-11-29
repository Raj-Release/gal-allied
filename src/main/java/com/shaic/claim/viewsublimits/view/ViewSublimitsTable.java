package com.shaic.claim.viewsublimits.view;

import com.shaic.arch.table.GBaseTable;
import com.shaic.domain.SublimitFunObject;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewSublimitsTable extends GBaseTable<SublimitFunObject>{
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","name", "amount","amntUtilisedIncludingCurrentClaim","availableBalanceIncludingCurrentClaim",
		"amntUtilisedExcludingCurrentClaim","availableBalanceExcludingCurrentClaim"}; 	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<SublimitFunObject>(SublimitFunObject.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		
		table.setSelectable(false);
		
	}

	@Override
	public void tableSelectHandler(SublimitFunObject t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-sublimits-table-";
	}

}
