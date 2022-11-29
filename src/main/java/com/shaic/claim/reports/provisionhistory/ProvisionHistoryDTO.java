package com.shaic.claim.reports.provisionhistory;

import java.util.Date;

public class ProvisionHistoryDTO {
	
	private String intimationNo;
	private String cashlessRefNo;
	private String reimbursementRefNo;
	private double previousProvisionAmount;
	private double currentProvisionAmount;
	private double previousClaimProvisionAmount;
	private double currentClaimProvisionAmount;
	private String status;
	private Date date;
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getCashlessRefNo() {
		return cashlessRefNo;
	}
	public void setCashlessRefNo(String cashlessRefNo) {
		this.cashlessRefNo = cashlessRefNo;
	}
	public String getReimbursementRefNo() {
		return reimbursementRefNo;
	}
	public void setReimbursementRefNo(String reimbursementRefNo) {
		this.reimbursementRefNo = reimbursementRefNo;
	}
	public double getPreviousProvisionAmount() {
		return previousProvisionAmount;
	}
	public void setPreviousProvisionAmount(double previousProvisionAmount) {
		this.previousProvisionAmount = previousProvisionAmount;
	}
	public double getCurrentProvisionAmount() {
		return currentProvisionAmount;
	}
	public void setCurrentProvisionAmount(double currentProvisionAmount) {
		this.currentProvisionAmount = currentProvisionAmount;
	}
	public double getPreviousClaimProvisionAmount() {
		return previousClaimProvisionAmount;
	}
	public void setPreviousClaimProvisionAmount(double previousClaimProvisionAmount) {
		this.previousClaimProvisionAmount = previousClaimProvisionAmount;
	}
	public double getCurrentClaimProvisionAmount() {
		return currentClaimProvisionAmount;
	}
	public void setCurrentClaimProvisionAmount(double currentClaimProvisionAmount) {
		this.currentClaimProvisionAmount = currentClaimProvisionAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
     
}
