package com.shaic.claim.pcc.views;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.pcc.dto.PCCQueryDetailsTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class QueryDetailsTable extends GBaseTable<PCCQueryDetailsTableDTO> {
	

		
	@Override
	public void removeRow() {
		if(table!=null){
			table.clear();
		}
	}

	@Override
	public void initTable() {
		table.removeAllItems();
		table.setContainerDataSource(new BeanItemContainer<PCCQueryDetailsTableDTO>(PCCQueryDetailsTableDTO.class));
		Object[] VISIBLE_COLUMNS = new Object[] {"queryRemarks","queryRaiseRole","queryRaiseBy","queryRaiseDate"};

		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
	}

	@Override
	public void tableSelectHandler(PCCQueryDetailsTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "query-details-";
	}
}
