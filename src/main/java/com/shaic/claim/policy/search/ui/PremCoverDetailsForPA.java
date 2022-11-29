package com.shaic.claim.policy.search.ui;

import org.codehaus.jackson.annotate.JsonProperty;

public class PremCoverDetailsForPA {
	
	@JsonProperty("CoverCode")
	private String coverCode;
	
	@JsonProperty("CoverDesc")
	private String coverDescription;
	
	@JsonProperty("SumInsured")
	private String sumInsured;

	public String getCoverCode() {
		return coverCode;
	}

	public void setCoverCode(String coverCode) {
		this.coverCode = coverCode;
	}

	public String getCoverDescription() {
		return coverDescription;
	}

	public void setCoverDescription(String coverDescription) {
		this.coverDescription = coverDescription;
	}

	public String getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(String sumInsured) {
		this.sumInsured = sumInsured;
	}

}

