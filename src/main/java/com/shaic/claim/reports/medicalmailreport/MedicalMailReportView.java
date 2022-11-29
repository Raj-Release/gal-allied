package com.shaic.claim.reports.medicalmailreport;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface MedicalMailReportView extends Searchable{
	public void list(Page<MedicalMailReportTableDTO> tableRows);
	public void generateReport();


}
