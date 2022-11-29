package com.shaic.claim.reports.autoallocationaancelreport;

import java.util.Date;

public class AutoAllocationCancelDetailReportDTO {
	private String intimationNumber;
	private Long cancelKey;
	private Long intimationKey;
	private Long claimKey;
	private Long transactionKey;
	private String transactionType;
	private String cancelRemarks;
	private String cancelledBy;
	private Date cancelledDate;
	private Long stageId;
	
	
	public String getIntimationNumber() {
		return intimationNumber;
	}
	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}
	public Long getCancelKey() {
		return cancelKey;
	}
	public void setCancelKey(Long cancelKey) {
		this.cancelKey = cancelKey;
	}
	public Long getIntimationKey() {
		return intimationKey;
	}
	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}
	public Long getClaimKey() {
		return claimKey;
	}
	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}
	public Long getTransactionKey() {
		return transactionKey;
	}
	public void setTransactionKey(Long transactionKey) {
		this.transactionKey = transactionKey;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getCancelRemarks() {
		return cancelRemarks;
	}
	public void setCancelRemarks(String cancelRemarks) {
		this.cancelRemarks = cancelRemarks;
	}
	public String getCancelledBy() {
		return cancelledBy;
	}
	public void setCancelledBy(String cancelledBy) {
		this.cancelledBy = cancelledBy;
	}
	public Date getCancelledDate() {
		return cancelledDate;
	}
	public void setCancelledDate(Date cancelledDate) {
		this.cancelledDate = cancelledDate;
	}
	public Long getStageId() {
		return stageId;
	}
	public void setStageId(Long stageId) {
		this.stageId = stageId;
	}
	
}
