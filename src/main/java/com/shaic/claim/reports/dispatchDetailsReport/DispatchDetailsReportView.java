package com.shaic.claim.reports.dispatchDetailsReport;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface DispatchDetailsReportView extends Searchable {
	
	public void list(Page<DispatchDetailsReportTableDTO> tableRows);
	public void generateReport();
	public void buildUpdateTypeLayout(Long updateType);
	public void resetDispatchSearchFields();

}
