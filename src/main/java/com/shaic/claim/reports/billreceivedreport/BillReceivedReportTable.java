package com.shaic.claim.reports.billreceivedreport;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;

public class BillReceivedReportTable extends GBaseTable<BillReceivedReportTableDTO>{
	
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber","cpuCode","typeOfClaim","totalBillReceived","checkSend","medicallyRejected","financiallyReject","close","completeTotal",
		"financiallyPending","billingPending","medicallyPending","chequeNotIssued","claimOpen","pending","medicallyReplyAwaiting"};
	
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<BillReceivedReportTableDTO>(BillReceivedReportTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setColumnCollapsingAllowed(false);
		table.setHeight("480px");
	}
	@Override
	public void tableSelectHandler(BillReceivedReportTableDTO t) {
		fireViewEvent(MenuPresenter.BILL_RECEIVED_REPORT, t);
	}
	
	@Override
	public String textBundlePrefixString() {
		return "billreceivedreport-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
	}

}
