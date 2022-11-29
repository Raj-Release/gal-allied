package com.shaic.claim.policy.search.ui;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PremGMCContinuityBenefits {
	
	@JsonProperty("Year")
	private String year;
	
	@JsonProperty("InsurerName")
	private String insuredName;
	
	@JsonProperty("PolicyNo")
	private String policyNo;
	
	@JsonProperty("PolFMDt")
	private String polFromDate;
	
	@JsonProperty("PolToDt")
	private String polToDate;
	
	@JsonProperty("InceptionDt")
	private String inceptionDate;
	
	@JsonProperty("Waiver_30Days")
	private String waiver30Days;
	
	@JsonProperty("Exclusion_1stYear")
	private String exclusion1Yr;
	
	@JsonProperty("Exclusion_2ndYear")
	private String exclusion2Yr;
	
	@JsonProperty("PED_Waiver")
	private String pedWaiver;

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getPolFromDate() {
		return polFromDate;
	}

	public void setPolFromDate(String polFromDate) {
		this.polFromDate = polFromDate;
	}

	public String getPolToDate() {
		return polToDate;
	}

	public void setPolToDate(String polToDate) {
		this.polToDate = polToDate;
	}

	public String getInceptionDate() {
		return inceptionDate;
	}

	public void setInceptionDate(String inceptionDate) {
		this.inceptionDate = inceptionDate;
	}

	public String getWaiver30Days() {
		return waiver30Days;
	}

	public void setWaiver30Days(String waiver30Days) {
		this.waiver30Days = waiver30Days;
	}

	public String getExclusion1Yr() {
		return exclusion1Yr;
	}

	public void setExclusion1Yr(String exclusion1Yr) {
		this.exclusion1Yr = exclusion1Yr;
	}

	public String getExclusion2Yr() {
		return exclusion2Yr;
	}

	public void setExclusion2Yr(String exclusion2Yr) {
		this.exclusion2Yr = exclusion2Yr;
	}

	public String getPedWaiver() {
		return pedWaiver;
	}

	public void setPedWaiver(String pedWaiver) {
		this.pedWaiver = pedWaiver;
	}

	
	
	

}
