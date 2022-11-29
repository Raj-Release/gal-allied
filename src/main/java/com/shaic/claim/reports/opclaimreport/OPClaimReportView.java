package com.shaic.claim.reports.opclaimreport;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface OPClaimReportView extends Searchable{
	
	public void list(Page<OPClaimReportTableDTO> tableRows);
	public void generateReport();

}
