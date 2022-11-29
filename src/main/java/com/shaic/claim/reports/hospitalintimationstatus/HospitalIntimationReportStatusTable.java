package com.shaic.claim.reports.hospitalintimationstatus;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;

public class HospitalIntimationReportStatusTable extends GBaseTable<HospitalIntimationReportStatusTableDTO>{
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber","intimationNo","policyNo","intimationDateValue","productCode","insuredName","hospitalName","hospitalAddress",
		"telephoneNo","fax","city","state","stage"};
	
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<HospitalIntimationReportStatusTableDTO>(HospitalIntimationReportStatusTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setColumnCollapsingAllowed(false);
		table.setHeight("498px");
	}
	@Override
	public void tableSelectHandler(HospitalIntimationReportStatusTableDTO t) {
		fireViewEvent(MenuPresenter.HOSPITAL_INTIMATION_REPORT, t);
	}
	
	@Override
	public String textBundlePrefixString() {
		return "hospitalintimationreport-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=10){
			table.setPageLength(10);
		}
		
	}
	

}
