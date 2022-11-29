package com.shaic.claim.reports.automationdashboard;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class AutomationDashboardTable extends GBaseTable<AutomationDashboardTableDTO>{
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber","intimationNo", 
		"lotNumber", "batchNumber", "hospitalCode", "hospitalName", "approvedAmount",
		 "status","pmsNeftVerifedStatus", "lotCreatedDate", "batchCreatedDate","batchAutomated" };

@Override
public void removeRow() {
	table.removeAllItems();
	
}

@Override
public void initTable() {
	
	table.setContainerDataSource(new BeanItemContainer<AutomationDashboardTableDTO>(AutomationDashboardTableDTO.class));
	table.setVisibleColumns(NATURAL_COL_ORDER);
	table.setColumnCollapsingAllowed(false);
	table.setHeight("480px");
}
@Override
public void tableSelectHandler(AutomationDashboardTableDTO t) {
	
}

@Override
public String textBundlePrefixString() {
	return "automation-dashboard-report-";
}

protected void tablesize(){
	table.setPageLength(table.size()+1);
	int length =table.getPageLength();
	if(length>=10){
		table.setPageLength(10);
	}
}
}
