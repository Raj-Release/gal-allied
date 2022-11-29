package com.shaic.claim.viewEarlierRodDetails;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class EsclateToRawReplyReceivedTable extends GBaseTable<EsclateToRawTableDTO>{

	
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber","category","subCategory","remarksToRaw","resolutionFromRaw","remarksFromRaw"
		};
	
	
	@Override
	public void removeRow() {	
		table.removeAllItems();
		
	}
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<EsclateToRawTableDTO>(EsclateToRawTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("200px");
		table.setWidth("1270px");
	}
	@Override
	public void tableSelectHandler(EsclateToRawTableDTO t) {
	}
	
	@Override
	public String textBundlePrefixString() {
		return "esclatetoraw-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}


}
