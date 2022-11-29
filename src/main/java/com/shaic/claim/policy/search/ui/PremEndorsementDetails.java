package com.shaic.claim.policy.search.ui;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PremEndorsementDetails {
	
	@JsonProperty("EndCode")
	private String endCode;
	
	@JsonProperty("EndEffFmDt")
	private String endEffFmDt;
	
	@JsonProperty("EndEffToDt")
	private String endEffToDt;
	
	
	@JsonProperty("EndNo")
	private String endNo;
	
	@JsonProperty("EndPremium")
	private String endPremium;

	@JsonProperty("EndRevisedSumInsured")
	private String endRevisedSumInsured;
	
		
	@JsonProperty("EndSumInsured")
	private String endSumInsured;
	
	@JsonProperty("EndText")
	private String endText;

	
	@JsonProperty("EndType")
	private String endType;


	public String getEndCode() {
		return endCode;
	}


	public void setEndCode(String endCode) {
		this.endCode = endCode;
	}


	public String getEndEffFmDt() {
		return endEffFmDt;
	}


	public void setEndEffFmDt(String endEffFmDt) {
		this.endEffFmDt = endEffFmDt;
	}


	public String getEndEffToDt() {
		return endEffToDt;
	}


	public void setEndEffToDt(String endEffToDt) {
		this.endEffToDt = endEffToDt;
	}


	public String getEndNo() {
		return endNo;
	}


	public void setEndNo(String endNo) {
		this.endNo = endNo;
	}


	public String getEndPremium() {
		return endPremium;
	}


	public void setEndPremium(String endPremium) {
		this.endPremium = endPremium;
	}


	public String getEndRevisedSumInsured() {
		return endRevisedSumInsured;
	}


	public void setEndRevisedSumInsured(String endRevisedSumInsured) {
		this.endRevisedSumInsured = endRevisedSumInsured;
	}


	public String getEndSumInsured() {
		return endSumInsured;
	}


	public void setEndSumInsured(String endSumInsured) {
		this.endSumInsured = endSumInsured;
	}


	public String getEndText() {
		return endText;
	}


	public void setEndText(String endText) {
		this.endText = endText;
	}


	public String getEndType() {
		return endType;
	}


	public void setEndType(String endType) {
		this.endType = endType;
	}

}
