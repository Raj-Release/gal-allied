package com.shaic.claim.reports.billreceivedreport;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface BillReceivedReportView extends Searchable {
	public void list(Page<BillReceivedReportTableDTO> tableRows);
	public void generateReport();

}
