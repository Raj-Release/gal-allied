package com.shaic.claim.registration;

import com.shaic.arch.table.GBaseTable;
import com.shaic.domain.SublimitFunObject;
import com.vaadin.v7.data.util.BeanItemContainer;

public class SublimtListTable extends GBaseTable<SublimitFunObject>{

	public static final Object[] COLUMN_HEADER = new Object[] {"serialNumber",
		"name",
		"description",
		"amount",
	};
	
	
	@Override
	public void removeRow() {
		
		
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<SublimitFunObject>(SublimitFunObject.class));
		table.setVisibleColumns(COLUMN_HEADER);	
		table.setHeight("155px");
	}

	@Override
	public void tableSelectHandler(SublimitFunObject t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "claim-registration-";
	}	

}
