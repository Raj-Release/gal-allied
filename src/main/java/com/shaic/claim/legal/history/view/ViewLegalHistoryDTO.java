package com.shaic.claim.legal.history.view;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class ViewLegalHistoryDTO extends AbstractTableDTO   implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String typeofClaim ;
	
	private String dateAndTime;
	
	private Date createdDate;
	
	private String userID;
	
	private String userName;
	
	private String referenceNo;
	
	private String claimStage;
	
	private String status;
	
	private Long statusID;
	
	private String userRemark;
	
	private Long reimbursementKey;
	
	private Long cashlessKey;
	
	private Long historyKey;
	

	public String getTypeofClaim() {
		return typeofClaim;
	}

	public void setTypeofClaim(String typeofClaim) {
		this.typeofClaim = typeofClaim;
	}

	public String getDateAndTime() {
		return dateAndTime;
	}

	public void setDateAndTime(String dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getClaimStage() {
		return claimStage;
	}

	public void setClaimStage(String claimStage) {
		this.claimStage = claimStage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserRemark() {
		return userRemark;
	}

	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public Long getReimbursementKey() {
		return reimbursementKey;
	}

	public void setReimbursementKey(Long reimbursementKey) {
		this.reimbursementKey = reimbursementKey;
	}

	public Long getCashlessKey() {
		return cashlessKey;
	}

	public void setCashlessKey(Long cashlessKey) {
		this.cashlessKey = cashlessKey;
	}

	public Long getHistoryKey() {
		return historyKey;
	}

	public void setHistoryKey(Long historyKey) {
		this.historyKey = historyKey;
	}

	public Long getStatusID() {
		return statusID;
	}

	public void setStatusID(Long statusID) {
		this.statusID = statusID;
	}
	
	

}
