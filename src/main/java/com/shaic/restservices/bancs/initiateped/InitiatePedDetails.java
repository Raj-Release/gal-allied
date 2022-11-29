package com.shaic.restservices.bancs.initiateped;

import java.util.List;

public class InitiatePedDetails {
	
	String ServiceTransactionId;
	String businessChannel;
	String userCode;
	String roleCode;
	String operationCode;
	String policyNumber;
	String memberCode;
	String claimNumber;
	String remarks;
	List<ComplexPedDetails> pedDetails;
	
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
	public String getOperationCode() {
		return operationCode;
	}
	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
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
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public List<ComplexPedDetails> getPedDetails() {
		return pedDetails;
	}
	public void setPedDetails(List<ComplexPedDetails> pedDetails) {
		this.pedDetails = pedDetails;
	}

}
