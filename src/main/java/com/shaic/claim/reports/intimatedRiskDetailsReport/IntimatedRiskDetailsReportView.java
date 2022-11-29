package com.shaic.claim.reports.intimatedRiskDetailsReport;

import java.util.List;

import com.shaic.arch.GMVPView;

public interface IntimatedRiskDetailsReportView extends GMVPView {
	
	public void showIntimatedRiskDetailsReport();
	public void intimatedRiskReportDetailsView(List<IntimatedRiskDetailsReportDto> intimatedRiskDetailsDtoList);

}
