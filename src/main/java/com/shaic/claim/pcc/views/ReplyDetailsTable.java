package com.shaic.claim.pcc.views;

import java.util.Date;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.pcc.dto.PCCReplyDetailsTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ReplyDetailsTable extends GBaseTable<PCCReplyDetailsTableDTO> {

	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		if(table!=null){
			table.clear();
		}
	}

	@Override
	public void initTable() {
		
		table.removeAllItems();
		table.setContainerDataSource(new BeanItemContainer<PCCReplyDetailsTableDTO>(PCCReplyDetailsTableDTO.class));
		Object[] VISIBLE_COLUMNS = new Object[] {"replyRemarks","replyRole","replyGivenBy","repliedDate"};

		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
	}

	@Override
	public void tableSelectHandler(PCCReplyDetailsTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "reply-details-";
	}
}
