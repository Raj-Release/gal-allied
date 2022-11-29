package com.shaic.restservices.bancs.initiateped;

public class PEDInitiateResponse {
	
	String errorCode;
	String errorDescription;
	String status;
	String workItemID;
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getWorkItemID() {
		return workItemID;
	}
	public void setWorkItemID(String workItemID) {
		this.workItemID = workItemID;
	}
	
	

}
