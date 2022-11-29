package com.shaic.claim.reports.cpuwisePreauthReport;

import com.shaic.arch.GMVPView;

public interface CPUwisePreauthReportView extends GMVPView {
	
	public void showSearchPreauthCPUWise();
	public void showCPUWisePreauthDetails(CPUWisePreauthResultDto cpuWisePreauthResultDto);

}
