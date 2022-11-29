package com.shaic.claim.reports.hospitalintimationstatus;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface HospitalIntimationStatusReportView extends Searchable{
	public void list(Page<HospitalIntimationReportStatusTableDTO> tableRows);
	public void generateReport();

}
