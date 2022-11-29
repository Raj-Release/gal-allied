package com.shaic.restservices.bancs.claimprovision;

public class BankDetailsRequest {

	private String serviceTransactionId;
	private String businessChannel;
	private String userCode;
	private String roleCode;
	private String partyCode;
	private String policyNumber;
	
	public String getServiceTransactionId() {
		return serviceTransactionId;
	}
	public void setServiceTransactionId(String serviceTransactionId) {
		this.serviceTransactionId = serviceTransactionId;
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
	public String getPartyCode() {
		return partyCode;
	}
	public void setPartyCode(String partyCode) {
		this.partyCode = partyCode;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	
}
