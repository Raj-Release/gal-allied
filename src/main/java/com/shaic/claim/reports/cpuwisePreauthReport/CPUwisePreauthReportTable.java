package com.shaic.claim.reports.cpuwisePreauthReport;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class CPUwisePreauthReportTable extends GBaseTable<CPUwisePreauthReportDto> {
	
	
	private static final Object[] COLUMN_HEADER = new Object[] {		
		"sno","intimationNo","policyNumber","productName","preauthStatus","preauthReqAmount","preauthAmount",
		"preauthDate","diagnosis","patientName","patientAge","hospitalName","anhHospital","treatmentType","doctor","remarks" };
	
	@Override
	public void removeRow() {
		
		
	}

	@Override
	public void initTable() {
			setSizeFull();
			table.setContainerDataSource(new BeanItemContainer<CPUwisePreauthReportDto>(CPUwisePreauthReportDto.class));
			table.setVisibleColumns(COLUMN_HEADER);
			table.setColumnCollapsingAllowed(false);
			table.setColumnWidth("remarks", 180);
	}

	@Override
	public void tableSelectHandler(CPUwisePreauthReportDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "searchCPUwisePreauthReport-";
	}

}
