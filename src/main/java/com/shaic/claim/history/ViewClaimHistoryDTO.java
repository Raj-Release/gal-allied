package com.shaic.claim.history;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class ViewClaimHistoryDTO extends AbstractTableDTO   implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String typeofClaim ;
	
	private String dateAndTime;
	
	private String userID;
	
	private String claimStage;
	
	private String status;
	
	private String userRemark;

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
	
	

}
