package com.shaic.claim.reports.fvrassignmentreport;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface FVRAssignmentReportView extends Searchable{
	public void list(Page<FVRAssignmentReportTableDTO> tableRows);

	public void init(BeanItemContainer<SelectValue> cpuCode,BeanItemContainer<SelectValue> reportType,
			BeanItemContainer<SelectValue> claimType,BeanItemContainer<SelectValue> fvrCpuCode);
	
	public void generateReport();

}
