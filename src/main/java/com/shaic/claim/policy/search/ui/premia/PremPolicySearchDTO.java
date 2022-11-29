package com.shaic.claim.policy.search.ui.premia;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

public class PremPolicySearchDTO implements Serializable {

	private static final long serialVersionUID = 4842363748683760054L;
	
	@JsonProperty("HealthCardNo")
	private String healthCardNo; 

	public String getHealthCardNo() {
		return healthCardNo;
	}

	public void setHealthCardNo(String healthCardNo) {
		this.healthCardNo = healthCardNo;
	}

	public String getInsuredDOB() {
		return insuredDOB;
	}

	public void setInsuredDOB(String insuredDOB) {
		this.insuredDOB = insuredDOB;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProposerDOB() {
		return proposerDOB;
	}

	public void setProposerDOB(String proposerDOB) {
		this.proposerDOB = proposerDOB;
	}

	public String getProposerName() {
		return proposerName;
	}

	public void setProposerName(String proposerName) {
		this.proposerName = proposerName;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	@JsonProperty("InsuredDOB")
	private String insuredDOB;
			
	@JsonProperty("InsuredName")
	private String insuredName;
	
	@JsonProperty("MobileNo")
	private String mobileNo;
	
	@JsonProperty("OfficeCode")
	private String officeCode;
	
	@JsonProperty("PolicyNo")
	private String policyNo;
	
	@JsonProperty("ProductName")
	private String productName;
	
	@JsonProperty("ProposerDOB")
	private String proposerDOB;
	
	@JsonProperty("ProposerName")
	private String proposerName;
	
	@JsonProperty("ReceiptNo")
	private String receiptNo;
	
	@JsonProperty("RiskId")
	private String riskSysId;
	

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getRiskSysId() {
		return riskSysId;
	}

	public void setRiskSysId(String riskSysId) {
		this.riskSysId = riskSysId;
	}
}
