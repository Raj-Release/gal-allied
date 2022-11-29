package com.shaic.claim.policy.search.ui;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PremGmcCopayLimit {
	
	@JsonProperty("Age_Type")
	private String ageType;
	
	@JsonProperty("Claim_Type")
	private String claimType;
	
	@JsonProperty("CoPay_Per")
	private String copayPercentage;
	
	@JsonProperty("End_No_Idx")
	private String endNoIdx;
	
	@JsonProperty("Rel_Type")
	private String relationType;
	
	@JsonProperty("SI_From")
	private String sumInsuredFrom;
	
	@JsonProperty("SI_To")
	private String sumInsuredTo;
	
	@JsonProperty("Age_From")
	private String ageFrom;
	
	@JsonProperty("Age_To")
	private String ageTo;

	public String getAgeType() {
		return ageType;
	}

	public void setAgeType(String ageType) {
		this.ageType = ageType;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getCopayPercentage() {
		return copayPercentage;
	}

	public void setCopayPercentage(String copayPercentage) {
		this.copayPercentage = copayPercentage;
	}

	public String getEndNoIdx() {
		return endNoIdx;
	}

	public void setEndNoIdx(String endNoIdx) {
		this.endNoIdx = endNoIdx;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public String getSumInsuredFrom() {
		return sumInsuredFrom;
	}

	public void setSumInsuredFrom(String sumInsuredFrom) {
		this.sumInsuredFrom = sumInsuredFrom;
	}

	public String getSumInsuredTo() {
		return sumInsuredTo;
	}

	public void setSumInsuredTo(String sumInsuredTo) {
		this.sumInsuredTo = sumInsuredTo;
	}

	public String getAgeFrom() {
		return ageFrom;
	}

	public void setAgeFrom(String ageFrom) {
		this.ageFrom = ageFrom;
	}

	public String getAgeTo() {
		return ageTo;
	}

	public void setAgeTo(String ageTo) {
		this.ageTo = ageTo;
	}
	
	
}
