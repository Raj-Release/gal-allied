package com.shaic.domain.omp;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OMPCreateIntimationPolicyDetails {
	
	
	@JsonProperty("{")
	private String startToken;

	@JsonProperty("PolicyNo")
	private String policyNo;
	
	@JsonProperty("InsuredName")
	private String insuredName;
	
	@JsonProperty("ProductName")
	private String productName;
	
	@JsonProperty("SumInsured")
	private String sumInsured;
	
	@JsonProperty("PolicyEndDate")
	private String policyEndDate;
	
	@JsonProperty("DateofInception")
	private String policyFromDate;

	public String getPolicyNo() {
		return policyNo;
	}
	
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
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

	public String getPolicyEndDate() {
		return policyEndDate;
	}

	public void setPolicyEndDate(String policyEndDate) {
		this.policyEndDate = policyEndDate;
	}
	
	public String getPolicyFromDate() {
		return policyFromDate;
	}

	public void setPolicyFromDate(String policyFromDate) {
		this.policyFromDate = policyFromDate;
	}



	@JsonProperty("}")
	private String endToken;

}
