package com.shaic.claim.reimbursement.dto;

import java.io.Serializable;

public class FAInternalRemarksTableDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private int serialNo;
	private String claimType;
	private String refNoRodNo;
	private String createdDate;
	private String userId;
	private String userName;
	private String internalRemarks;
	private String remarks;
	
	public int getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}
	
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	
	public String getRefNoRodNo() {
		return refNoRodNo;
	}
	public void setRefNoRodNo(String refNoRodNo) {
		this.refNoRodNo = refNoRodNo;
	}
	
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
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
	
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getInternalRemarks() {
		return internalRemarks;
	}
	public void setInternalRemarks(String internalRemarks) {
		this.internalRemarks = internalRemarks;
	}
}
