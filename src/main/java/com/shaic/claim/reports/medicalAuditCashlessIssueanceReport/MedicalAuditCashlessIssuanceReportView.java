package com.shaic.claim.reports.medicalAuditCashlessIssueanceReport;

import java.util.List;

import com.shaic.arch.GMVPView;

public interface MedicalAuditCashlessIssuanceReportView extends GMVPView {
	
	public void showMedicalAuditClaimStatusReport();
	public void medicalAuditClaimStatusReportDetailsView(List<MedicalAuditCashlessIssuanceReportDto> MedicalAuditClaimStatusReportDtoList);

}
