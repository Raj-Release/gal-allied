package com.shaic.claim.reports.paymentpendingdashboard;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class PaymentPendingDashboardTable extends GBaseTable<PaymentPendingDashboardTableDTO>{
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber","claimIntimationNo", 
		"payeeName", "hosCode", "claimType", "approvedAmount", "paymentFileSentOn",
		 "batchNumber","ifscCode", "accountNumber" };

@Override
public void removeRow() {
	table.removeAllItems();
	
}

@Override
public void initTable() {
	
	table.setContainerDataSource(new BeanItemContainer<PaymentPendingDashboardTableDTO>(PaymentPendingDashboardTableDTO.class));
	table.setVisibleColumns(NATURAL_COL_ORDER);
	table.setColumnCollapsingAllowed(false);
	table.setHeight("480px");
}
@Override
public void tableSelectHandler(PaymentPendingDashboardTableDTO t) {
	
}

@Override
public String textBundlePrefixString() {
	return "payment-pending-dashboard-report-";
}

protected void tablesize(){
	table.setPageLength(table.size()+1);
	int length =table.getPageLength();
	if(length>=10){
		table.setPageLength(10);
	}
}
}
