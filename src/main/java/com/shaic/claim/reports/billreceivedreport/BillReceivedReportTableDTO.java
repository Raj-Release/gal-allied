package com.shaic.claim.reports.billreceivedreport;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class BillReceivedReportTableDTO extends AbstractTableDTO  implements Serializable{
	private String cpuCode;	
	private String typeOfClaim;
//	private Long totalBillReceived;
	private Long checkSend;
	private Long medicallyRejected;
	private Long financiallyReject;
	private Long close;
	private Long completeTotal;
	private Long financiallyPending;
	private Long billingPending;
	private Long medicallyPending;
	private Long chequeNotIssued;
	private Long later;
	private Long claimOpen;
	private Long pending;
	private Long medicallyReplyAwaiting;
	private Long reimbursementKey;
	private Long claimKey;
	private Long statusKey;
	private Long stageKey;
	private Integer totalBillReceived;
	
	
	
	public String getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}	
	
	public Long getReimbursementKey() {
		return reimbursementKey;
	}
	public void setReimbursementKey(Long reimbursementKey) {
		this.reimbursementKey = reimbursementKey;
	}
	public Long getClaimKey() {
		return claimKey;
	}
	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}
	public Long getStatusKey() {
		return statusKey;
	}
	public void setStatusKey(Long statusKey) {
		this.statusKey = statusKey;
	}
	public Long getStageKey() {
		return stageKey;
	}
	public void setStageKey(Long stageKey) {
		this.stageKey = stageKey;
	}

	public String getTypeOfClaim() {
		return typeOfClaim;
	}
	public void setTypeOfClaim(String typeOfClaim) {
		this.typeOfClaim = typeOfClaim;
	}
	/*public Long getTotalBillReceived() {
		return totalBillReceived;
	}
	public void setTotalBillReceived(Long totalBillReceived) {
		this.totalBillReceived = totalBillReceived;
	}*/
	public Long getCheckSend() {
		return checkSend;
	}
	public void setCheckSend(Long checkSend) {
		this.checkSend = checkSend;
	}
	public Long getMedicallyRejected() {
		return medicallyRejected;
	}
	public void setMedicallyRejected(Long medicallyRejected) {
		this.medicallyRejected = medicallyRejected;
	}
	public Integer getTotalBillReceived() {
		return totalBillReceived;
	}
	public void setTotalBillReceived(Integer totalBillReceived) {
		this.totalBillReceived = totalBillReceived;
	}
	public Long getFinanciallyReject() {
		return financiallyReject;
	}
	public void setFinanciallyReject(Long financiallyReject) {
		this.financiallyReject = financiallyReject;
	}
	public Long getClose() {
		return close;
	}
	public void setClose(Long close) {
		this.close = close;
	}
	public Long getCompleteTotal() {
		return completeTotal;
	}
	public void setCompleteTotal(Long completeTotal) {
		this.completeTotal = completeTotal;
	}
	public Long getFinanciallyPending() {
		return financiallyPending;
	}
	public void setFinanciallyPending(Long financiallyPending) {
		this.financiallyPending = financiallyPending;
	}
	public Long getBillingPending() {
		return billingPending;
	}
	public void setBillingPending(Long billingPending) {
		this.billingPending = billingPending;
	}
	public Long getMedicallyPending() {
		return medicallyPending;
	}
	public void setMedicallyPending(Long medicallyPending) {
		this.medicallyPending = medicallyPending;
	}
	public Long getChequeNotIssued() {
		return chequeNotIssued;
	}
	public void setChequeNotIssued(Long chequeNotIssued) {
		this.chequeNotIssued = chequeNotIssued;
	}
	public Long getLater() {
		return later;
	}
	public void setLater(Long later) {
		this.later = later;
	}
	public Long getClaimOpen() {
		return claimOpen;
	}
	public void setClaimOpen(Long claimOpen) {
		this.claimOpen = claimOpen;
	}
	public Long getPending() {
		return pending;
	}
	public void setPending(Long pending) {
		this.pending = pending;
	}
	public Long getMedicallyReplyAwaiting() {
		return medicallyReplyAwaiting;
	}
	public void setMedicallyReplyAwaiting(Long medicallyReplyAwaiting) {
		this.medicallyReplyAwaiting = medicallyReplyAwaiting;
	}
	
	
	
	
}
