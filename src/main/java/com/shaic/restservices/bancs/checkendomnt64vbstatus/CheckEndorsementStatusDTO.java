package com.shaic.restservices.bancs.checkendomnt64vbstatus;

import org.codehaus.jackson.annotate.JsonProperty;

public class CheckEndorsementStatusDTO {
	
	@JsonProperty("{")
	private String startToken;
	
	@JsonProperty("ServiceTransactionId")
	private String serviceTransactionId;
	
	@JsonProperty("BusinessChannel")
	private String businessChannel;
	
	@JsonProperty("UserCode")
	private String userCode;
	
	@JsonProperty("RoleCode")
	private String roleCode;
	
	@JsonProperty("PolicyNumber")
	private String policyNo ;

	@JsonProperty("}")
	private String endToken;

	public String getStartToken() {
		return startToken;
	}

	public void setStartToken(String startToken) {
		this.startToken = startToken;
	}

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

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getEndToken() {
		return endToken;
	}

	public void setEndToken(String endToken) {
		this.endToken = endToken;
	}

}
