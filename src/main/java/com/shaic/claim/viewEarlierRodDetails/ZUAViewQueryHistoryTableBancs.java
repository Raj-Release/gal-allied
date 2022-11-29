package com.shaic.claim.viewEarlierRodDetails;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ZUAViewQueryHistoryTableBancs extends GBaseTable<ZUAViewQueryHistoryTableDTO>{
	
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNo","type","queryCode","queryDescription","queryDetails","dateandTime","raisedByRole","raisedByUser","queryreply","repliedByRole",
		"repliedByUser","repliedDate","querStatus"
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
		return "bancszuaqueryalert-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}


}
