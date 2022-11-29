package com.shaic.claim.policy.search.ui;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PremGmcCorpBufferLimit {

	
	@JsonProperty("Family_SI_YN")
	private String familySiType;
	
	@JsonProperty("Limit_Amount")
	private String limitAmount;
	
	@JsonProperty("Limit_Appl")
	private String limitApplicable;
	
	@JsonProperty("NO_SI_YN")
	private String noSiType;
	
	@JsonProperty("SI_From")
	private String sumInsuredFrom;
	
	@JsonProperty("SI_To")
	private String sumInsuredTo;
	
	@JsonProperty("Tot_No_SI")
	private String totalNoSI;

	public String getFamilySiType() {
		return familySiType;
	}

	public void setFamilySiType(String familySiType) {
		this.familySiType = familySiType;
	}

	public String getLimitAmount() {
		return limitAmount;
	}

	public void setLimitAmount(String limitAmount) {
		this.limitAmount = limitAmount;
	}

	public String getLimitApplicable() {
		return limitApplicable;
	}

	public void setLimitApplicable(String limitApplicable) {
		this.limitApplicable = limitApplicable;
	}

	public String getNoSiType() {
		return noSiType;
	}

	public void setNoSiType(String noSiType) {
		this.noSiType = noSiType;
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

	public String getTotalNoSI() {
		return totalNoSI;
	}

	public void setTotalNoSI(String totalNoSI) {
		this.totalNoSI = totalNoSI;
	}
	
	

}
