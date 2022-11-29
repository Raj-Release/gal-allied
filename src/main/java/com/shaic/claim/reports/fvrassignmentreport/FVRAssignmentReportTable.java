package com.shaic.claim.reports.fvrassignmentreport;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;

public class FVRAssignmentReportTable extends GBaseTable<FVRAssignmentReportTableDTO>{
	
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber","intimationNo","dateofAdmissionValue","admissionType","patientName","hospitalName","hospitalType","location","productName",
		"claimType","representativeType","pointToFocus","initiatorId","initiatorName","representativeId","representativeName","fvrAssigneeName",
		"fvrDateValue","fvrTime","fvrAssignedDateValue","fvrAssignedTime","fvrReceivedDateValue","fvrReceivedTime","tat","cpuCode","fvrCpuCode","fvrExecutiveComments",
		"fvrNotRequiredComments","hospitalCode"};
	
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<FVRAssignmentReportTableDTO>(FVRAssignmentReportTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("350px");
		table.setColumnWidth("hospitalCode", 120);
	}
	@Override
	public void tableSelectHandler(FVRAssignmentReportTableDTO t) {
		fireViewEvent(MenuPresenter.FVR_ASSINMENT_REPORT, t);
	}
	
	@Override
	public String textBundlePrefixString() {
		return "fvrassignmentreport-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}

}
