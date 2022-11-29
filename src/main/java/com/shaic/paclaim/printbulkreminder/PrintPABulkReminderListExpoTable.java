package com.shaic.paclaim.printbulkreminder;

import com.shaic.arch.table.GBaseTable;
import com.shaic.reimbursement.printReminderLetterBulk.SearchPrintReminderBulkTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

@SuppressWarnings("serial")
public class PrintPABulkReminderListExpoTable extends GBaseTable<SearchPrintReminderBulkTableDTO>{
	
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"sno","batchid","reminderType","category","claimIntimationNo","claimType","cpuCode","letterDate"}; 
	
	@Override
	public void removeRow() {
		table.removeAllItems();
	}

	@Override
	public void initTable() {
	
		table.setContainerDataSource(new BeanItemContainer<SearchPrintReminderBulkTableDTO>(SearchPrintReminderBulkTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size());
		table.setWidth("100%");
		table.setEditable(false);
	}

	@Override
	public void tableSelectHandler(SearchPrintReminderBulkTableDTO t) {
		
	}

	@Override
	public String textBundlePrefixString() {
	
		return "search-bulk-remainder-letter-";
	}

}
