package com.shaic.restservices.bancs.sendped;


public class PEDSendDetails {
	
	String serviceTransactionId;
	String workItemId;
	String policyNumber;
	String endorsementStatus;
	String endosementEffectiveDate;
	String gaeApprEndYN;
	String gaeApprUID;
	String gaeApprName;
	String gaeApprDate;
	String gaeApprRemarks;
	
	public String getServiceTransactionId() {
		return serviceTransactionId;
	}
	public void setServiceTransactionId(String serviceTransactionId) {
		this.serviceTransactionId = serviceTransactionId;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public String getEndorsementStatus() {
		return endorsementStatus;
	}
	public void setEndorsementStatus(String endorsementStatus) {
		this.endorsementStatus = endorsementStatus;
	}
	public String getEndosementEffectiveDate() {
		return endosementEffectiveDate;
	}
	public void setEndosementEffectiveDate(String endosementEffectiveDate) {
		this.endosementEffectiveDate = endosementEffectiveDate;
	}
	public String getGaeApprEndYN() {
		return gaeApprEndYN;
	}
	public void setGaeApprEndYN(String gaeApprEndYN) {
		this.gaeApprEndYN = gaeApprEndYN;
	}
	public String getGaeApprUID() {
		return gaeApprUID;
	}
	public void setGaeApprUID(String gaeApprUID) {
		this.gaeApprUID = gaeApprUID;
	}
	public String getGaeApprName() {
		return gaeApprName;
	}
	public void setGaeApprName(String gaeApprName) {
		this.gaeApprName = gaeApprName;
	}
	public String getGaeApprDate() {
		return gaeApprDate;
	}
	public void setGaeApprDate(String gaeApprDate) {
		this.gaeApprDate = gaeApprDate;
	}
	public String getGaeApprRemarks() {
		return gaeApprRemarks;
	}
	public void setGaeApprRemarks(String gaeApprRemarks) {
		this.gaeApprRemarks = gaeApprRemarks;
	}
	public String getWorkItemId() {
		return workItemId;
	}
	public void setWorkItemId(String workItemId) {
		this.workItemId = workItemId;
	}
}
