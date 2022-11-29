package com.shaic.claim.reports.autoallocationaancelreport;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

@SuppressWarnings("serial")
public class AutoAllocationCancelReportTable extends GBaseTable<AutoAllocationCancelDetailReportDTO>{

	private static final Object[] REVISED_COLUMN_HEADER = new Object[] {		
		"intimationNumber","cancelRemarks","cancelledBy","cancelledDate"};
	

	@Override
	public void removeRow() {
		table.removeAllItems();		
		
	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<AutoAllocationCancelDetailReportDTO>(AutoAllocationCancelDetailReportDTO.class));
		table.setVisibleColumns(REVISED_COLUMN_HEADER);
		table.setColumnCollapsingAllowed(false);
		table.setHeight("450px");
		
	}

	@Override
	public void tableSelectHandler(AutoAllocationCancelDetailReportDTO t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "search-cancel-report-";	
		}

}
