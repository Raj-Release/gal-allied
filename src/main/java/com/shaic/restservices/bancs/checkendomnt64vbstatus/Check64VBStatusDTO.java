package com.shaic.restservices.bancs.checkendomnt64vbstatus;

import org.codehaus.jackson.annotate.JsonProperty;

public class Check64VBStatusDTO {

	
	@JsonProperty("{")
	private String startToken;
	
	@JsonProperty("serviceTransactionId")
	private String serviceTransactionId;
	
	@JsonProperty("businessChannel")
	private String businessChannel;
	
	@JsonProperty("userCode")
	private String userCode;
	
	@JsonProperty("roleCode")
	private String roleCode;
	
	@JsonProperty("policyNumber")
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
