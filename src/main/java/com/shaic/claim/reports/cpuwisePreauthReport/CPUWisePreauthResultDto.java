package com.shaic.claim.reports.cpuwisePreauthReport;

import java.util.List;

public class CPUWisePreauthResultDto {
	
	Long estimation;
	Long preauthApproved;
	Long enhancementApproved;
	Long queryRaised;
	Long docGiven;
	Long rejected;
	Long withDrawn;
	List<CPUwisePreauthReportDto> cpuwisePreauthProcessedList;
	
	public Long getEstimation() {
		return estimation;
	}
	public void setEstimation(Long estimation) {
		this.estimation = estimation;
	}
	public Long getPreauthApproved() {
		return preauthApproved;
	}
	public void setPreauthApproved(Long preauthApproved) {
		this.preauthApproved = preauthApproved;
	}
	public Long getEnhancementApproved() {
		return enhancementApproved;
	}
	public void setEnhancementApproved(Long enhancementApproved) {
		this.enhancementApproved = enhancementApproved;
	}
	public Long getQueryRaised() {
		return queryRaised;
	}
	public void setQueryRaised(Long queryRaised) {
		this.queryRaised = queryRaised;
	}
	public Long getDocGiven() {
		return docGiven;
	}
	public void setDocGiven(Long docGiven) {
		this.docGiven = docGiven;
	}
	public Long getRejected() {
		return rejected;
	}
	public void setRejected(Long rejected) {
		this.rejected = rejected;
	}
	public Long getWithDrawn() {
		return withDrawn;
	}
	public void setWithDrawn(Long withDrawn) {
		this.withDrawn = withDrawn;
	}
	public List<CPUwisePreauthReportDto> getCpuwisePreauthProcessedList() {
		return cpuwisePreauthProcessedList;
	}
	public void setCpuwisePreauthProcessedList(
			List<CPUwisePreauthReportDto> cpuwisePreauthProcessedList) {
		this.cpuwisePreauthProcessedList = cpuwisePreauthProcessedList;
	}
	
	

}
