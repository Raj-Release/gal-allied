package com.shaic.restservices.bancs.claimprovision.policydetail;

public class PreviousPolicyDetails {
	private String policyNumber;
	private String productCode;
	private String productName;
	private String policyInceptionDate;
	private String policyExpiryDate;
	private Integer sumInsured;
	private Integer totalPremium;
	
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getPolicyInceptionDate() {
		return policyInceptionDate;
	}
	public void setPolicyInceptionDate(String policyInceptionDate) {
		this.policyInceptionDate = policyInceptionDate;
	}
	public String getPolicyExpiryDate() {
		return policyExpiryDate;
	}
	public void setPolicyExpiryDate(String policyExpiryDate) {
		this.policyExpiryDate = policyExpiryDate;
	}
	public Integer getSumInsured() {
		return sumInsured;
	}
	public void setSumInsured(Integer sumInsured) {
		this.sumInsured = sumInsured;
	}
	public Integer getTotalPremium() {
		return totalPremium;
	}
	public void setTotalPremium(Integer totalPremium) {
		this.totalPremium = totalPremium;
	}
	
}
