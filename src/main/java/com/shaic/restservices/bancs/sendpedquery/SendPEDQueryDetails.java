package com.shaic.restservices.bancs.sendpedquery;

import java.util.List;

public class SendPEDQueryDetails {
	
	String serviceTransactionId;
	String policyNumber;
	String memberCode;
	String claimNumber;
	String workItemID;
	List<SendPEDQueryComplexTypeDetails> queries;
	
	
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
	public String getMemberCode() {
		return memberCode;
	}
	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}
	public String getClaimNumber() {
		return claimNumber;
	}
	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}
	public String getWorkItemID() {
		return workItemID;
	}
	public void setWorkItemID(String workItemID) {
		this.workItemID = workItemID;
	}
	public List<SendPEDQueryComplexTypeDetails> getQueries() {
		return queries;
	}
	public void setQueries(List<SendPEDQueryComplexTypeDetails> queries) {
		this.queries = queries;
	}
	
}
