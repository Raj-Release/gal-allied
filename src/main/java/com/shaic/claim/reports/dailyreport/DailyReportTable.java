package com.shaic.claim.reports.dailyreport;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;

public class DailyReportTable extends GBaseTable<DailyReportTableDTO>{
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber","intimationNo","intimationDateValue","policyNumber","productType","insuredName","mainMemberName","callerContactNo",
		"hospitalCode","patientName","patientAge","admissionReason","hospitalType","hospitalDateValue","hospitalName","cpuCode","hospitalState",
		"hospitalCity","fieldDoctorNameAllocated","contactNoOftheDoctor","dateAndTimeOfAllocationValue","registrationStatus","totalAuthAmount",
		"authDateValue","cashlessStatus","cashlessReason"};
	
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<DailyReportTableDTO>(DailyReportTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setColumnCollapsingAllowed(false);
		table.setHeight("480px");
	}
	@Override
	public void tableSelectHandler(DailyReportTableDTO t) {
		fireViewEvent(MenuPresenter.MEDICAL_MAIL_REPORT, t);
	}
	
	@Override
	public String textBundlePrefixString() {
		return "dailyreport-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=10){
			table.setPageLength(10);
		}
	}
}
