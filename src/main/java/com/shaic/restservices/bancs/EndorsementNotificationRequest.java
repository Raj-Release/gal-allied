package com.shaic.restservices.bancs;


public class EndorsementNotificationRequest {
	
	private String policyId;
	private String policyNumber;
	private String policyEndorsementNumber;
	private String insuredId;
	private String productCode;
	private String endDate;
	
	public String getPolicyId() {
		return policyId;
	}
	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public String getPolicyEndorsementNumber() {
		return policyEndorsementNumber;
	}
	public void setPolicyEndorsementNumber(String policyEndorsementNumber) {
		this.policyEndorsementNumber = policyEndorsementNumber;
	}
	public String getInsuredId() {
		return insuredId;
	}
	public void setInsuredId(String insuredId) {
		this.insuredId = insuredId;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
}
