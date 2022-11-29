package com.shaic.claim.policy.search.ui;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PremGpaBenefitDetails {
	
	@JsonProperty("AmountPerUnit")
	private String amountPerUnit;
	
	@JsonProperty("BenefitBasedOn")
	private String benefitBasedOn;
	
	@JsonProperty("BenefitCode")
	private String benefitCode;
	
	@JsonProperty("BenefitDescription")
	private String benefitDesc;
	
	@JsonProperty("BenefitLongDescription")
	private String benefitLongDescription;
	
	@JsonProperty("ClaimMaxAmount")
	private String claimMaximumAmount;
	
	@JsonProperty("ClaimPercentage")
	private String claimPercentage;
	
	@JsonProperty("FlatAmountPerClaim")
	private String flatAmountPerClaim;
	
	@JsonProperty("FlatAmountPerPolicyPeriod")
	private String flatAmountPerPolicyPeriod;
	
	@JsonProperty("NoOfUnits")
	private String noOfUnits;
	
	@JsonProperty("Remarks")
	private String remarks;
	
	
	@JsonProperty("SIMaxAmount")
	private String siMaximumAmount;
	
	@JsonProperty("SIPercentage")
	private String siPercentage;
	
	@JsonProperty("SrNo")
	private String serialNumber;
	
	@JsonProperty("SubBenefitDescription")
	private String subBenefitDescription;
	
	@JsonProperty("WithInSI")
	private String withInSI;

	public String getAmountPerUnit() {
		return amountPerUnit;
	}

	public void setAmountPerUnit(String amountPerUnit) {
		this.amountPerUnit = amountPerUnit;
	}

	public String getBenefitBasedOn() {
		return benefitBasedOn;
	}

	public void setBenefitBasedOn(String benefitBasedOn) {
		this.benefitBasedOn = benefitBasedOn;
	}

	public String getBenefitCode() {
		return benefitCode;
	}

	public void setBenefitCode(String benefitCode) {
		this.benefitCode = benefitCode;
	}

	public String getBenefitDesc() {
		return benefitDesc;
	}

	public void setBenefitDesc(String benefitDesc) {
		this.benefitDesc = benefitDesc;
	}

	public String getBenefitLongDescription() {
		return benefitLongDescription;
	}

	public void setBenefitLongDescription(String benefitLongDescription) {
		this.benefitLongDescription = benefitLongDescription;
	}

	public String getClaimMaximumAmount() {
		return claimMaximumAmount;
	}

	public void setClaimMaximumAmount(String claimMaximumAmount) {
		this.claimMaximumAmount = claimMaximumAmount;
	}

	public String getClaimPercentage() {
		return claimPercentage;
	}

	public void setClaimPercentage(String claimPercentage) {
		this.claimPercentage = claimPercentage;
	}

	public String getFlatAmountPerClaim() {
		return flatAmountPerClaim;
	}

	public void setFlatAmountPerClaim(String flatAmountPerClaim) {
		this.flatAmountPerClaim = flatAmountPerClaim;
	}

	public String getFlatAmountPerPolicyPeriod() {
		return flatAmountPerPolicyPeriod;
	}

	public void setFlatAmountPerPolicyPeriod(String flatAmountPerPolicyPeriod) {
		this.flatAmountPerPolicyPeriod = flatAmountPerPolicyPeriod;
	}

	public String getNoOfUnits() {
		return noOfUnits;
	}

	public void setNoOfUnits(String noOfUnits) {
		this.noOfUnits = noOfUnits;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getSiMaximumAmount() {
		return siMaximumAmount;
	}

	public void setSiMaximumAmount(String siMaximumAmount) {
		this.siMaximumAmount = siMaximumAmount;
	}

	public String getSiPercentage() {
		return siPercentage;
	}

	public void setSiPercentage(String siPercentage) {
		this.siPercentage = siPercentage;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getSubBenefitDescription() {
		return subBenefitDescription;
	}

	public void setSubBenefitDescription(String subBenefitDescription) {
		this.subBenefitDescription = subBenefitDescription;
	}

	public String getWithInSI() {
		return withInSI;
	}

	public void setWithInSI(String withInSI) {
		this.withInSI = withInSI;
	}
	

}
