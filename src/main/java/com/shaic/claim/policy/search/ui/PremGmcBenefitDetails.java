package com.shaic.claim.policy.search.ui;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PremGmcBenefitDetails {

	@JsonProperty("ConditionCode")
	private String conditionCode;
	
	@JsonProperty("ConditionDesc")
	private String conditionDesc;
	
	@JsonProperty("ConditionLongDesc")
	private String conditionLongDesc;

	public String getConditionCode() {
		return conditionCode;
	}

	public void setConditionCode(String conditionCode) {
		this.conditionCode = conditionCode;
	}

	public String getConditionDesc() {
		return conditionDesc;
	}

	public void setConditionDesc(String conditionDesc) {
		this.conditionDesc = conditionDesc;
	}

	public String getConditionLongDesc() {
		return conditionLongDesc;
	}

	public void setConditionLongDesc(String conditionLongDesc) {
		this.conditionLongDesc = conditionLongDesc;
	}

}
