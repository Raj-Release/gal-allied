package com.shaic.restservices.bancs.claimprovision.policydetail;

public class TVCDetails {
	
	private String tvcInitiatedDate;
	private String transactionId;
	private String startTime;
	private String endTime;
	private String tvcStatus;
	
	public String getTvcInitiatedDate() {
		return tvcInitiatedDate;
	}
	public void setTvcInitiatedDate(String tvcInitiatedDate) {
		this.tvcInitiatedDate = tvcInitiatedDate;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getTvcStatus() {
		return tvcStatus;
	}
	public void setTvcStatus(String tvcStatus) {
		this.tvcStatus = tvcStatus;
	}
	
}
