package com.shaic.claim.viewEarlierRodDetails;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ZUAViewQueryHistoryTable extends GBaseTable<ZUAViewQueryHistoryTableDTO>{
	
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber","flowFrom","type","flowTo","remarks","serialNo","apprDateAndTime",
		"userId",
		};
	
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<ZUAViewQueryHistoryTableDTO>(ZUAViewQueryHistoryTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("350px");
		table.setColumnWidth("remarks", 120);
	}
	@Override
	public void tableSelectHandler(ZUAViewQueryHistoryTableDTO t) {
		//fireViewEvent(MenuPresenter.FVR_ASSINMENT_REPORT, t);
	}
	
	@Override
	public String textBundlePrefixString() {
		return "zuaqueryalert-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}


}
