package com.shaic.restservices.bancs.receivepedquery;

import java.util.List;

public class UpdatePEDQueryResponseDetails {
	
	String ServiceTransactionId;
	String businessChannel;
	String userCode;
	String roleCode;
	String policyNumber;
	String memberCode;
	String claimNumber;
	Long WorkItemID;
	List<UpdatePEDQueryComplexQueryDetails> query;
	
	
	public String getServiceTransactionId() {
		return ServiceTransactionId;
	}
	public void setServiceTransactionId(String serviceTransactionId) {
		ServiceTransactionId = serviceTransactionId;
	}
	public String getBusinessChannel() {
		return businessChannel;
	}
	public void setBusinessChannel(String businessChannel) {
		this.businessChannel = businessChannel;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
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
	public Long getWorkItemID() {
		return WorkItemID;
	}
	public void setWorkItemID(Long workItemID) {
		WorkItemID = workItemID;
	}
	public List<UpdatePEDQueryComplexQueryDetails> getQuery() {
		return query;
	}
	public void setQuery(List<UpdatePEDQueryComplexQueryDetails> query) {
		this.query = query;
	}
	
	

}
