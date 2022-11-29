package com.shaic.claim.reports.tatreport;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class TATReportTableDTO extends AbstractTableDTO  implements Serializable{

	private String intimationNo;
	private String claimType;
	private String rodNo;
	private String cpu;
	private String ackDate;
	private String latAckDate;
	private String rodDate;
	private String zonalApprovalDate;
	private String maApproveDate;
	private String billApproveDate;
	private String faApprovedDate;
	private String settledDate;
	private Integer rodDelay;
	private Integer zonalDelay;
	private Integer medicalDelay;
	private Integer billingDelay;
	private Integer faDelay;
	private Integer totalDelay;
	private Integer delayExcludingSunday;
	private String status;
	private String userQueue;
	private Integer currentDelay;
	
	
	/**
	 * Newly incorporated as per revised TAT Report Requirement Changes
	 */
	private String cpuCode;
	private String pendingWith;
	private Integer pendingDays;
	private Double claimedAmt;
	private String reconsider;
	private String lastStatusDate;
	private String investigationDate;
	private String rejectionDate;
	private String officeCodeValue;
	private String zone;
	
	private String docRecvdFrom;
	private String hospName;
	private String hospCity;
	private Double claimedBillCompletedAmt;
	private Double FAApprovedAmt;
	private Double ZMRAmt;
	
	private String userId;
	private String userName;

	
	public String getUserQueue() {
		return userQueue;
	}
	public void setUserQueue(String userQueue) {
		this.userQueue = userQueue;
	}
	public Integer getCurrentDelay() {
		return currentDelay;
	}
	public void setCurrentDelay(Integer currentDelay) {
		this.currentDelay = currentDelay;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getRodNo() {
		return rodNo;
	}
	public void setRodNo(String rodNo) {
		this.rodNo = rodNo;
	}
	public String getCpu() {
		return cpu;
	}
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}
	public String getAckDate() {
		return ackDate;
	}
	public void setAckDate(String ackDate) {
		this.ackDate = ackDate;
	}
	public String getLatAckDate() {
		return latAckDate;
	}
	public void setLatAckDate(String latAckDate) {
		this.latAckDate = latAckDate;
	}
	public String getRodDate() {
		return rodDate;
	}
	public void setRodDate(String rodDate) {
		this.rodDate = rodDate;
	}
	public String getZonalApprovalDate() {
		return zonalApprovalDate;
	}
	public void setZonalApprovalDate(String zonalApprovalDate) {
		this.zonalApprovalDate = zonalApprovalDate;
	}
	public String getMaApproveDate() {
		return maApproveDate;
	}
	public void setMaApproveDate(String maApproveDate) {
		this.maApproveDate = maApproveDate;
	}
	public String getBillApproveDate() {
		return billApproveDate;
	}
	public void setBillApproveDate(String billApproveDate) {
		this.billApproveDate = billApproveDate;
	}
	public String getFaApprovedDate() {
		return faApprovedDate;
	}
	public void setFaApprovedDate(String faApprovedDate) {
		this.faApprovedDate = faApprovedDate;
	}
	public String getSettledDate() {
		return settledDate;
	}
	public void setSettledDate(String settledDate) {
		this.settledDate = settledDate;
	}
	public Integer getRodDelay() {
		return rodDelay;
	}
	public void setRodDelay(Integer rodDelay) {
		this.rodDelay = rodDelay;
	}
	public Integer getZonalDelay() {
		return zonalDelay;
	}
	public void setZonalDelay(Integer zonalDelay) {
		this.zonalDelay = zonalDelay;
	}
	public Integer getMedicalDelay() {
		return medicalDelay;
	}
	public void setMedicalDelay(Integer medicalDelay) {
		this.medicalDelay = medicalDelay;
	}
	public Integer getBillingDelay() {
		return billingDelay;
	}
	public void setBillingDelay(Integer billingDelay) {
		this.billingDelay = billingDelay;
	}
	public Integer getFaDelay() {
		return faDelay;
	}
	public void setFaDelay(Integer faDelay) {
		this.faDelay = faDelay;
	}
	public Integer getTotalDelay() {
		return totalDelay;
	}
	public void setTotalDelay(Integer totalDelay) {
		this.totalDelay = totalDelay;
	}
	public Integer getDelayExcludingSunday() {
		return delayExcludingSunday;
	}
	public void setDelayExcludingSunday(Integer delayExcludingSunday) {
		this.delayExcludingSunday = delayExcludingSunday;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getPendingWith() {
		return pendingWith;
	}
	public void setPendingWith(String pendingWith) {
		this.pendingWith = pendingWith;
	}
	public Integer getPendingDays() {
		return pendingDays;
	}
	public void setPendingDays(Integer pendingDays) {
		this.pendingDays = pendingDays;
	}
	public Double getClaimedAmt() {
		return claimedAmt;
	}
	public void setClaimedAmt(Double claimedAmt) {
		this.claimedAmt = claimedAmt;
	}
	public String getReconsider() {
		return reconsider;
	}
	public void setReconsider(String reconsider) {
		this.reconsider = reconsider;
	}
	public String getLastStatusDate() {
		return lastStatusDate;
	}
	public void setLastStatusDate(String lastStatusDate) {
		this.lastStatusDate = lastStatusDate;
	}
	public String getInvestigationDate() {
		return investigationDate;
	}
	public void setInvestigationDate(String investigationDate) {
		this.investigationDate = investigationDate;
	}
	public String getRejectionDate() {
		return rejectionDate;
	}
	public void setRejectionDate(String rejectionDate) {
		this.rejectionDate = rejectionDate;
	}
	public String getOfficeCodeValue() {
		return officeCodeValue;
	}
	public void setOfficeCodeValue(String officeCodeValue) {
		this.officeCodeValue = officeCodeValue;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getDocRecvdFrom() {
		return docRecvdFrom;
	}
	public void setDocRecvdFrom(String docRecvdFrom) {
		this.docRecvdFrom = docRecvdFrom;
	}
	public String getHospName() {
		return hospName;
	}
	public void setHospName(String hospName) {
		this.hospName = hospName;
	}
	public String getHospCity() {
		return hospCity;
	}
	public void setHospCity(String hospCity) {
		this.hospCity = hospCity;
	}
	public Double getClaimedBillCompletedAmt() {
		return claimedBillCompletedAmt;
	}
	public void setClaimedBillCompletedAmt(Double claimedBillCompletedAmt) {
		this.claimedBillCompletedAmt = claimedBillCompletedAmt;
	}
	public Double getFAApprovedAmt() {
		return FAApprovedAmt;
	}
	public void setFAApprovedAmt(Double fAApprovedAmt) {
		this.FAApprovedAmt = fAApprovedAmt;
	}
	public Double getZMRAmt() {
		return ZMRAmt;
	}
	public void setZMRAmt(Double zMRAmt) {
		ZMRAmt = zMRAmt;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}		
}
