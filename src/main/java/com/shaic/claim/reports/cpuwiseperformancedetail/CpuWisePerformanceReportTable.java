package com.shaic.claim.reports.cpuwiseperformancedetail;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;

public class CpuWisePerformanceReportTable extends GBaseTable<CpuWisePerformanceReportTableDTO>{
	
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber","type","gmcNetWork","gmcProvisionAmount","gmcNonNetWork","gmcProvisionAmount1","nonGmcNetWork","nonGmcProvisionAmount",
		"nonGmcNonNetWork","nonGmcProvisionAmount1","total","provisionAmountTotal"};
	
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<CpuWisePerformanceReportTableDTO>(CpuWisePerformanceReportTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setColumnWidth("provisionAmountTotal", 250);
		table.setColumnCollapsingAllowed(false);
		table.setHeight("490px");
	}
	@Override
	public void tableSelectHandler(CpuWisePerformanceReportTableDTO t) {
		fireViewEvent(MenuPresenter.CPU_WISE_PERFORMANCE_REPORT, t);
	}
	
	@Override
	public String textBundlePrefixString() {
		return "cpuwiseperformancereport-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
	}

}
