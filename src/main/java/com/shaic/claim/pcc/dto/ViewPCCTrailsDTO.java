package com.shaic.claim.pcc.dto;

import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class ViewPCCTrailsDTO extends AbstractTableDTO{

	private String userID ;

	private String userName;

	private Date dateAndTime;

	private String pccRemarksType;

	private String claimStage;

	private String status;
	
	private String userRemark;
	
	private String statusRemark;
	
	private String raiseRole;

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getDateAndTime() {
		return dateAndTime;
	}

	public void setDateAndTime(Date dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	public String getPccRemarksType() {
		return pccRemarksType;
	}

	public void setPccRemarksType(String pccRemarksType) {
		this.pccRemarksType = pccRemarksType;
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

	public String getStatusRemark() {
		return statusRemark;
	}

	public void setStatusRemark(String statusRemark) {
		this.statusRemark = statusRemark;
	}

	public String getRaiseRole() {
		return raiseRole;
	}

	public void setRaiseRole(String raiseRole) {
		this.raiseRole = raiseRole;
	}

}
