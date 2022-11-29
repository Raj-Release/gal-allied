package com.shaic.reimbursement.rod.allowReconsideration.search;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class SearchAllowReconsiderationTableDTO extends AbstractTableDTO implements Serializable{
	
	private String intimationNo;
	private String claimType;
	private String rodNo;
	private String policyNo;
	private String cpuCodeandName;
	private Date rejectionDate;
	private String rejectionReason;
	private String rejectionRemarks;
	private Boolean isRejected;
	private String unCheckRemarks;
	private String allowReconsiderFlag;
	private String cpuId;
	private String description;
	private Long rodKey;
	private Long statusKey;
	
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
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getCpuCodeandName() {
		return cpuCodeandName;
	}
	public void setCpuCodeandName(String cpuCodeandName) {
		this.cpuCodeandName = cpuCodeandName;
	}
	public Date getRejectionDate() {
		return rejectionDate;
	}
	public void setRejectionDate(Date rejectionDate) {
		this.rejectionDate = rejectionDate;
	}
	public String getRejectionReason() {
		return rejectionReason;
	}
	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}
	public String getRejectionRemarks() {
		return rejectionRemarks;
	}
	public void setRejectionRemarks(String rejectionRemarks) {
		this.rejectionRemarks = rejectionRemarks;
	}
	public Boolean getIsRejected() {
		return isRejected;
	}
	public void setIsRejected(Boolean isRejected) {
		this.isRejected = isRejected;
	}
	public String getUnCheckRemarks() {
		return unCheckRemarks;
	}
	public void setUnCheckRemarks(String unCheckRemarks) {
		this.unCheckRemarks = unCheckRemarks;
	}
	public String getAllowReconsiderFlag() {
		return allowReconsiderFlag;
	}
	public void setAllowReconsiderFlag(String allowReconsiderFlag) {
		this.allowReconsiderFlag = allowReconsiderFlag;
	}
	public String getCpuId() {
		return cpuId;
	}
	public void setCpuId(String cpuId) {
		this.cpuId = cpuId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getRodKey() {
		return rodKey;
	}
	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}
	public Long getStatusKey() {
		return statusKey;
	}
	public void setStatusKey(Long statusKey) {
		this.statusKey = statusKey;
	}
}
