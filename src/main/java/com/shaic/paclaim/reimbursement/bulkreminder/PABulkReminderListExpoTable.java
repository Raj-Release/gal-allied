package com.shaic.paclaim.reimbursement.bulkreminder;

import com.shaic.arch.table.GBaseTable;
import com.shaic.reimbursement.reminderBulkSearch.SearchGenerateReminderBulkTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

@SuppressWarnings("serial")
public class PABulkReminderListExpoTable extends GBaseTable<SearchGenerateReminderBulkTableDTO>{
	
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"sno","batchid","subBatchId","reminderType","category","claimIntimationNo","claimType","cpuCode","letterDate"}; 
	
	@Override
	public void removeRow() {
		table.removeAllItems();
	}

	@Override
	public void initTable() {
	
		table.setContainerDataSource(new BeanItemContainer<SearchGenerateReminderBulkTableDTO>(SearchGenerateReminderBulkTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size());
		table.setWidth("100%");
		table.setEditable(false);
	}

	@Override
	public void tableSelectHandler(SearchGenerateReminderBulkTableDTO t) {
		
	}

	@Override
	public String textBundlePrefixString() {
	
		return "search-bulk-remainder-letter-";
	}

}
