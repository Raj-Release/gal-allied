package com.shaic.claim.policy.search.ui;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PremDeliveryExpLimit {
	
	@JsonProperty("Del_Type")
	private String deliveryType;
	
	@JsonProperty("End_No_Idx")
	private String endNoIdx;
	
	@JsonProperty("Limit_Amount")
	private String limitAmount;
	
	@JsonProperty("SI_From")
	private String sumInsuredFrom;
	
	@JsonProperty("SI_To")
	private String sumInsuredTo;

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public String getEndNoIdx() {
		return endNoIdx;
	}

	public void setEndNoIdx(String endNoIdx) {
		this.endNoIdx = endNoIdx;
	}

	public String getLimitAmount() {
		return limitAmount;
	}

	public void setLimitAmount(String limitAmount) {
		this.limitAmount = limitAmount;
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
