package com.shaic.claim.claimhistory.view;

import java.util.Date;

public class PedHistoryDTO {
	
	private String status;
	
	private String strDateAndTime;
	
	private Date dateAndTime;
	
	private String userName;
	
	private String remarks;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getStrDateAndTime() {
		return strDateAndTime;
	}

	public void setStrDateAndTime(String strDateAndTime) {
		this.strDateAndTime = strDateAndTime;
	}

	public Date getDateAndTime() {
		return dateAndTime;
	}

	public void setDateAndTime(Date dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

}
