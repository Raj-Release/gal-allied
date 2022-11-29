package com.shaic.arch;

import java.util.List;

import com.shaic.claim.ClaimDto;
import com.shaic.domain.PolicyEndorsementDetails;

public class VB64ComplianceDto {
	private String reportId;
	private ClaimDto claimDto;
	private String paymentStatus;
	private List<PolicyEndorsementDetails> endorsement;
	public ClaimDto getClaimDto() {
		return claimDto;
	}
	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}
	public List<PolicyEndorsementDetails> getEndorsement() {
		return endorsement;
	}
	public void setEndorsement(List<PolicyEndorsementDetails> endorsement) {
		this.endorsement = endorsement;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}	
	
}
