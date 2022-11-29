package com.shaic.claim.policy.search.ui;


import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PremBonusDetails {
	
	
	@JsonProperty("{")
	private String startToken;
	
	@JsonProperty("ChequeStatus")
	private String chequeStatus;
	
	@JsonProperty("CumulativeBonus")
	private String cumulativeBonus;
	
	@JsonProperty("PolicyNo")
	private String policyNo;
	
	@JsonProperty("ProductCode")
	private String productCode;
	
	@JsonProperty("InsuredBonus")
	private List<PremInsuredBonusDetails> insuredBonus;
	
	@JsonProperty("}")
	private String endToken;

	public String getStartToken() {
		return startToken;
	}

	public void setStartToken(String startToken) {
		this.startToken = startToken;
	}

	public String getChequeStatus() {
		return chequeStatus;
//		return "D";
	}

	public void setChequeStatus(String chequeStatus) {
		this.chequeStatus = chequeStatus;
	}

	public String getCumulativeBonus() {
		return cumulativeBonus;
	}

	public void setCumulativeBonus(String cumulativeBonus) {
		this.cumulativeBonus = cumulativeBonus;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public List<PremInsuredBonusDetails> getInsuredBonus() {
		return insuredBonus;
	}

	public void setInsuredBonus(List<PremInsuredBonusDetails> insuredBonus) {
		this.insuredBonus = insuredBonus;
	}

	public String getEndToken() {
		return endToken;
	}

	public void setEndToken(String endToken) {
		this.endToken = endToken;
	}
	

}
