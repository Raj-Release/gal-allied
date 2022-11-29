package com.shaic.claim.reports.hospitalwisereport;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class HospitalWiseReportTable extends GBaseTable<HospitalWiseReportTableDTO>{
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber","intimationDateValue","hospitalCode","hospitalName","intimationNo","fvrAllocatedDt","fieldVisitor",
		"policyNo","officeName","agentCode","agentName","smCode","smName",
		"productName","patientName","age","insuredCity","dateOfAdmissionValue","durationOfStay","diagnosis",
		"claimedAmt","paidAmt","outstandingAmount","sex","relationWithInsured","dateOfDischargeValue","claimType","cashlessAuthorizedAmount",
		"rodDateValue","chequeDateValue","chequeNo","chequeAmount","icdCodes"};
		
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<HospitalWiseReportTableDTO>(HospitalWiseReportTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setColumnWidth("icdCodes", 120);
	}
	@Override
	public void tableSelectHandler(HospitalWiseReportTableDTO t) {
	//	fireViewEvent(MenuPresenter.SHOW_PROCESS_RRC_REQUEST_DETAILS, t);
	}
	
	@Override
	public String textBundlePrefixString() {
		return "hospitalwisereport-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=5){
			table.setPageLength(5);
		}
		
	}

}
