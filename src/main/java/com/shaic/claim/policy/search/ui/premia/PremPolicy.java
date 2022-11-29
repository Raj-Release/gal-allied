package com.shaic.claim.policy.search.ui.premia;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.shaic.domain.Product;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PremPolicy {

	@JsonProperty("Address")
	private String address;
	
	@JsonProperty("HealthCardNo")
	private String healthCardNo;

	@JsonProperty("MobileNumber")
	private String mobileNumber;
	
	@JsonProperty("OfficeCode")
	private String officeCode;
	
	@JsonProperty("PolicyEndDate")
	private String policyEndDate;
	
	@JsonProperty("SectionCode")
	private String sectionCode;	

	@JsonProperty("DateofInception")
	private String dateOfInception;

	@JsonProperty("PolicyNo")
	private String policyNo;

	@JsonProperty("PolicyEndNoIdx")
	private String policyEndNoIdx;

	@JsonProperty("PolicyStatus")
	private String policyStatus;
	
	@JsonProperty("ProductName")
	private String productName;
	
	@JsonProperty("ProductCode")
	private String productCode;

	@JsonProperty("SumInsured")
	private String sumInsured;

	@JsonProperty("TelephoneNo")
	private String telephoneNo;
	
	@JsonProperty("InsuredName")
	private String insuredName;
	
	@JsonProperty("ProposerName")
	private String proposerName;
	
	@JsonProperty("PolicyStartDate")
	private String policyStartDate;

	private Product product;
	
	private String policySource;

	public String getTelephoneNo() {
		return telephoneNo;
	}

	public void setTelephoneNo(String telephoneNo) {
		this.telephoneNo = telephoneNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDateofInception() {
		return dateOfInception;
	}

	public void setDateofInception(String dateofInception) {
		this.dateOfInception = dateofInception;
	}

	public String getHealthCardNo() {
		return healthCardNo;
	}

	public void setHealthCardNo(String healthCardNo) {
		this.healthCardNo = healthCardNo;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public String getPolicyEndDate() {
		return policyEndDate;
	}

	public void setPolicyEndDate(String policyEndDate) {
		this.policyEndDate = policyEndDate;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getPolicyStatus() {
		return policyStatus;
	}

	public void setPolicyStatus(String policyStatus) {
		this.policyStatus = policyStatus;
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

	public String getPolicyEndNoIdx() {
		return policyEndNoIdx;
	}

	public void setPolicyEndNoIdx(String policyEndNoIdx) {
		this.policyEndNoIdx = policyEndNoIdx;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(String sectionCode) {
		this.sectionCode = sectionCode;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getPolicySource() {
		return policySource;
	}

	public void setPolicySource(String policySource) {
		this.policySource = policySource;
	}

	public String getProposerName() {
		return proposerName;
	}

	public void setProposerName(String proposerName) {
		this.proposerName = proposerName;
	}

	public String getPolicyStartDate() {
		return policyStartDate;
	}

	public void setPolicyStartDate(String policyStartDate) {
		this.policyStartDate = policyStartDate;
	}
	
	
	
}