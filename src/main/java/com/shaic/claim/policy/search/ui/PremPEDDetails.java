package com.shaic.claim.policy.search.ui;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PremPEDDetails {
	
	@JsonProperty("PEDCode")
	private String pedCode;
	
	@JsonProperty("PEDDescription")
	private String pedDescription;
	
	@JsonProperty("Code")
	private String gmcPedCode;
	
	@JsonProperty("Description")
	private String gmcPedDescription;
	
	@JsonProperty("PEDEffectiveFromDate")
	private String pedEffectiveFromDate;
	
	@JsonProperty("PEDEffFmDt")
	private String pedEffectiveFmDate;
	
	@JsonProperty("PEDEffToDt")
	private String pedEffectiveToDate;
	
	@JsonProperty("PEDType")
	private String pedType;

	public String getPedCode() {
		return pedCode;
	}

	public void setPedCode(String pedCode) {
		this.pedCode = pedCode;
	}

	public String getPedDescription() {
		return pedDescription;
	}

	public void setPedDescription(String pedDescription) {
		this.pedDescription = pedDescription;
	}

	public String getGmcPedCode() {
		return gmcPedCode;
	}

	public void setGmcPedCode(String gmcPedCode) {
		this.gmcPedCode = gmcPedCode;
	}

	public String getGmcPedDescription() {
		return gmcPedDescription;
	}

	public void setGmcPedDescription(String gmcPedDescription) {
		this.gmcPedDescription = gmcPedDescription;
	}

	public String getPedEffectiveFromDate() {
		return pedEffectiveFromDate;
	}

	public void setPedEffectiveFromDate(String pedEffectiveFromDate) {
		this.pedEffectiveFromDate = pedEffectiveFromDate;
	}

	public String getPedEffectiveFmDate() {
		return pedEffectiveFmDate;
	}

	public void setPedEffectiveFmDate(String pedEffectiveFmDate) {
		this.pedEffectiveFmDate = pedEffectiveFmDate;
	}

	public String getPedEffectiveToDate() {
		return pedEffectiveToDate;
	}

	public void setPedEffectiveToDate(String pedEffectiveToDate) {
		this.pedEffectiveToDate = pedEffectiveToDate;
	}

	public String getPedType() {
		return pedType;
	}

	public void setPedType(String pedType) {
		this.pedType = pedType;
	}


	
}
