package com.shaic.restservices.bancs.claimprovision.policydetail;

public class PolicyDetailsRequest {
	
	private String businessChannel;
	private String userCode;
	private String roleCode;
	private String policyNumber;
	
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
	
	@Override
	public String toString() {
		return "PolicyDetailsRequest [businessChannel=" + businessChannel
				+ ", userCode=" + userCode + ", roleCode=" + roleCode
				+ ", policyNumber=" + policyNumber + "]";
	}
	
}
