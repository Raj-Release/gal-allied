package com.shaic.claim.policy.search.ui;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PremGmcRoomRentLimit {
	
	@JsonProperty("Limit_Amount")
	private String limitAmount;
	
	@JsonProperty("Limit_Per")
	private String limitPercentage;
	
	@JsonProperty("NR_Limit")
	private String nrLimit;
	
	@JsonProperty("SI_From")
	private String sumInsuredFrom;
	
	@JsonProperty("SI_To")
	private String sumInsuredTo;
	
	@JsonProperty("Room_Type")
	private String roomRentType;
	
	@JsonProperty("Proportionate")
	private String proportionateFlag;
	
	@JsonProperty("Charges")
	private String charges;
	
	

	public String getLimitAmount() {
		return limitAmount;
	}

	public void setLimitAmount(String limitAmount) {
		this.limitAmount = limitAmount;
	}

	public String getLimitPercentage() {
		return limitPercentage;
	}

	public void setLimitPercentage(String limitPercentage) {
		this.limitPercentage = limitPercentage;
	}

	public String getNrLimit() {
		return nrLimit;
	}

	public void setNrLimit(String nrLimit) {
		this.nrLimit = nrLimit;
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

	public String getRoomRentType() {
		return roomRentType;
	}

	public void setRoomRentType(String roomRentType) {
		this.roomRentType = roomRentType;
	}

	public String getProportionateFlag() {
		return proportionateFlag;
	}

	public void setProportionateFlag(String proportionateFlag) {
		this.proportionateFlag = proportionateFlag;
	}

	public String getCharges() {
		return charges;
	}

	public void setCharges(String charges) {
		this.charges = charges;
	}

}
