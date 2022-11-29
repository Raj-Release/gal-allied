package com.shaic.domain;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.viewEarlierRodDetails.ZUAViewQueryHistoryTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ZUATopViewQueryTable extends GBaseTable<ZUAViewQueryHistoryTableDTO>{
	
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"queryCode","queryDescription"
		};
	

	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<ZUAViewQueryHistoryTableDTO>(ZUAViewQueryHistoryTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("65px");
		//table.setWidth("375px");
		//table.setColumnWidth("queryCode", 120);
		//table.setColumnWidth("queryDescription", 250);
		
	}
	@Override
	public void tableSelectHandler(ZUAViewQueryHistoryTableDTO t) {
		//fireViewEvent(MenuPresenter.FVR_ASSINMENT_REPORT, t);
	}
	
	@Override
	public String textBundlePrefixString() {
		return "zuaqueryalert-";
	}
	

}
