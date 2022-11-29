package com.shaic.claim.policy.search.ui;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PremGmcAilmentLimit {
	
	@JsonProperty("Ailment")
	private String ailment;
	
	@JsonProperty("Limit_Per_Claim")
	private String limitPerClaim;
	
	@JsonProperty("Limit_Per_Eye")
	private String limitPerEye;
	
	@JsonProperty("Limit_Per_Family")
	private String limitPerFamily;
	
	@JsonProperty("Limit_Per_Policy")
	private String limitPerPolicy;
	
	@JsonProperty("Limit_Per_Risk")
	private String limitPerRisk;
	
	@JsonProperty("SI_From")
	private String sumInsuredFrom;
	
	@JsonProperty("SI_To")
	private String sumInsuredTo;

	public String getAilment() {
		return ailment;
	}

	public void setAilment(String ailment) {
		this.ailment = ailment;
	}

	public String getLimitPerClaim() {
		return limitPerClaim;
	}

	public void setLimitPerClaim(String limitPerClaim) {
		this.limitPerClaim = limitPerClaim;
	}

	public String getLimitPerEye() {
		return limitPerEye;
	}

	public void setLimitPerEye(String limitPerEye) {
		this.limitPerEye = limitPerEye;
	}

	public String getLimitPerFamily() {
		return limitPerFamily;
	}

	public void setLimitPerFamily(String limitPerFamily) {
		this.limitPerFamily = limitPerFamily;
	}

	public String getLimitPerPolicy() {
		return limitPerPolicy;
	}

	public void setLimitPerPolicy(String limitPerPolicy) {
		this.limitPerPolicy = limitPerPolicy;
	}

	public String getLimitPerRisk() {
		return limitPerRisk;
	}

	public void setLimitPerRisk(String limitPerRisk) {
		this.limitPerRisk = limitPerRisk;
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

}
