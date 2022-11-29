package com.shaic.claim.policy.search.ui;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RenewedPolicyDetails {

	@JsonProperty("ErrDesc")
	private String errorDesc;
	
	@JsonProperty("FromDt")
	private String fromDate;
	
	@JsonProperty("GrossPremium")
	private String grossPremium;
	
	@JsonProperty("Insurer")
	private String insurerName;
	
	@JsonProperty("PolSysId")
	private String policySysId;
	
	@JsonProperty("PolicyNo")
	private String policyNumber;
	
	@JsonProperty("ProdCode")
	private String productCode;
	
	@JsonProperty("ProdName")
	private String productName;
	
	@JsonProperty("SumInsured")
	private String sumInsured;
	
	@JsonProperty("ToDt")
	private String toDate;
	
	@JsonProperty("UWYear")
	private String underWriteYear;

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getGrossPremium() {
		return grossPremium;
	}

	public void setGrossPremium(String grossPremium) {
		this.grossPremium = grossPremium;
	}

	public String getInsurerName() {
		return insurerName;
	}

	public void setInsurerName(String insurerName) {
		this.insurerName = insurerName;
	}

	public String getPolicySysId() {
		return policySysId;
	}

	public void setPolicySysId(String policySysId) {
		this.policySysId = policySysId;
	}

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

	public String getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(String sumInsured) {
		this.sumInsured = sumInsured;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getUnderWriteYear() {
		return underWriteYear;
	}

	public void setUnderWriteYear(String underWriteYear) {
		this.underWriteYear = underWriteYear;
	}
	
}
