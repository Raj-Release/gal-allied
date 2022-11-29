package com.shaic.claim.reports.autoallocationaancelreport;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface AutoAllocationCancelReportView extends Searchable{
	void list(Page<AutoAllocationCancelDetailReportDTO> tableRows);

	void generateReport();
}