package com.shaic.claim.reports.productivityreport;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface ProductivityReportView extends Searchable{
	
	public void list(Page<ProductivityReportTableDTO> tableRows);
	public void setupDroDownValues();

}
