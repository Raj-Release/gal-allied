package com.shaic.claim.reports.executivesummaryreqort;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;

public class ExecutiveStatusSummaryReportTable extends GBaseTable<ExecutiveStatusSummaryReportDto>{
	
	private static final Object[] COLUMN_HEADER = new Object[] {		
			"sno","executiveId","executiveName","stageName","statusName","stageCount"};
	
	private static final Object[] CALL_CENTRE_COLUMN_HEADER = new Object[] {		
		"executiveId","executiveName","stageName","Created","Modified","stageCount"};
	
	@Override
	public void removeRow() {
		table.removeAllItems();		
		
	}

	@Override
	public void initTable() {
			setSizeFull();
			table.setContainerDataSource(new BeanItemContainer<ExecutiveStatusSummaryReportDto>(ExecutiveStatusSummaryReportDto.class));
			table.setVisibleColumns(COLUMN_HEADER);
			table.setColumnCollapsingAllowed(false);
			table.setHeight("250px");
	}

	@Override
	public void tableSelectHandler(ExecutiveStatusSummaryReportDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "exec-summary-report-";
	}

	public void setCallcenterTableHeader(){
		
		table.removeGeneratedColumn("Created");
		table.addGeneratedColumn("Created",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(Table source,
							final Object itemId, Object columnId) {
						return ((ExecutiveStatusSummaryReportDto)itemId).getStageCount();
					}
		});
	
	table.removeGeneratedColumn("Modified");
	table.addGeneratedColumn("Modified",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table source,
						final Object itemId, Object columnId) {
					return "";
				}
	});
	table.setVisibleColumns(CALL_CENTRE_COLUMN_HEADER);
}

}
