package com.shaic.claim.reports.intimationAlternateCPUReport;

import java.util.List;

import com.shaic.arch.GMVPView;

public interface IntimationAlternateCPUwiseReportView extends GMVPView {
	
	public void showIntimationAlternateCPUReport();
	public void intimationAlternateCPUReportDetailsView(List<IntimationAlternateCPUwiseReportDto> intimationAlternateCPUDtoList);

}
