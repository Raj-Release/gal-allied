package com.shaic.claim.reports.plannedAdmissionReport;

import java.util.List;

import com.shaic.arch.GMVPView;

public interface PlannedAdmissionReportView extends GMVPView {
	
	public void showSearchPlannedAdmissionReport();
	public void plannedAdmissionReportDetailsView(List<PlannedAdmissionReportDto> plannedIntimationDtoList);

}
