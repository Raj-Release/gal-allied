package com.shaic.claim.reports.cpuwiseperformancedetail;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface CpuWisePerformanceReportView extends Searchable {
	public void list(Page<CpuWisePerformanceReportTableDTO> tableRows);
	public void generateReport();

}
