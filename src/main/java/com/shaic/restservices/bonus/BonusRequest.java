package com.shaic.restservices.bonus;

public class BonusRequest {
	
	private String policyNumber;
	private String healthId;
	private String productCode;
	
	public String getPolicyNumber() {
		return policyNumber;
	}
	public String getHealthId() {
		return healthId;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public void setHealthId(String healthId) {
		this.healthId = healthId;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
}
