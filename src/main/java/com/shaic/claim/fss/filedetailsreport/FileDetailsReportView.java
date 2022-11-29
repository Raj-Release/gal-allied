package com.shaic.claim.fss.filedetailsreport;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface FileDetailsReportView extends Searchable{
	public void list(Page<FileDetailsReportTableDTO> tableRows);

	public void init();
	
	public void generateReport();
	
	public void resetSearchFileReportView();

}
