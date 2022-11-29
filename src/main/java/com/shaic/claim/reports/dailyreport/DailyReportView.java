package com.shaic.claim.reports.dailyreport;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface DailyReportView extends Searchable{
	public void list(Page<DailyReportTableDTO> tableRows);
	public void generateReport();


}
