package com.shaic.claim.reports.medicalAuditClaimStatusReport;

import java.util.List;

import com.shaic.arch.GMVPView;

public interface MedicalAuditClaimStatusReportView extends GMVPView {
	
	public void showMedicalAuditClaimStatusReport();
	public void medicalAuditClaimStatusReportDetailsView(List<MedicalAuditClaimStatusReportDto> MedicalAuditClaimStatusReportDtoList);

}
