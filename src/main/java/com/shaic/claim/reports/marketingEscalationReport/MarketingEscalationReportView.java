package com.shaic.claim.reports.marketingEscalationReport;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface MarketingEscalationReportView extends Searchable {
	
	public void list(Page<MarketingEscalationReportTableDTO> tableRows);
	public void generateReport();

}
