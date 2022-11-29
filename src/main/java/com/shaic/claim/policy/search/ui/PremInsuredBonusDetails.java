package com.shaic.claim.policy.search.ui;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)

public class PremInsuredBonusDetails {
	
	@JsonProperty("CumulativeBonus")
	private String cumulativeBonus;
	
	@JsonProperty("RiskSysID")
	private String insuredNumber;



	public String getCumulativeBonus() {
		return cumulativeBonus;
	}

	public void setCumulativeBonus(String cumulativeBonus) {
		this.cumulativeBonus = cumulativeBonus;
	}

	public String getInsuredNumber() {
		return insuredNumber;
	}

	public void setInsuredNumber(String insuredNumber) {
		this.insuredNumber = insuredNumber;
	}
	
}