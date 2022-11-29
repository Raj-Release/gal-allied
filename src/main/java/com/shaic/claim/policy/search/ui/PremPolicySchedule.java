package com.shaic.claim.policy.search.ui;

import org.codehaus.jackson.annotate.JsonProperty;

public class PremPolicySchedule {

	@JsonProperty("Result")
	private String resultUrl;

	public String getResultUrl() {
		return resultUrl;
	}

	public void setResultUrl(String resultUrl) {
		this.resultUrl = resultUrl;
	}
	
	
}
