package com.shaic.claim.pcc.views;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.pcc.dto.PCCApproveDetailsTableDTO;
import com.shaic.claim.pcc.dto.PCCQueryDetailsTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class PCCDetailsTable extends GBaseTable<PCCApproveDetailsTableDTO> {


	private static final long serialVersionUID = 3028729594709428726L;

	@Override
	public void removeRow() {
		if(table!=null){
			table.clear();
		}
	}

	@Override
	public void initTable() {
		table.removeAllItems();
		table.setContainerDataSource(new BeanItemContainer<PCCApproveDetailsTableDTO>(PCCApproveDetailsTableDTO.class));
		Object[] VISIBLE_COLUMNS = new Object[] {"remarks","raiseRole","raiseBy","raiseDate","status"};

		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
	}

	@Override
	public void tableSelectHandler(PCCApproveDetailsTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "pcc-details-";
	}

}
