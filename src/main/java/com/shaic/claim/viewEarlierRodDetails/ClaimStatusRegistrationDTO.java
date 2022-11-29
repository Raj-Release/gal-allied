package com.shaic.claim.viewEarlierRodDetails;

public class ClaimStatusRegistrationDTO {
	
	private String claimNo;
	
	private String registrationStatus;
	
	private String currency;
	
	private String provisionAmt;
	
	private String claimType;
	
	private String registrationRemarks;

	private String incidence;
	
	public String getIncidence() {
		return incidence;
	}

	public void setIncidence(String incidence) {
		this.incidence = incidence;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getRegistrationStatus() {
		return registrationStatus;
	}

	public void setRegistrationStatus(String registrationStatus) {
		this.registrationStatus = registrationStatus;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getProvisionAmt() {
		return provisionAmt;
	}

	public void setProvisionAmt(String provisionAmt) {
		this.provisionAmt = provisionAmt;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getRegistrationRemarks() {
		return registrationRemarks;
	}

	public void setRegistrationRemarks(String registrationRemarks) {
		this.registrationRemarks = registrationRemarks;
	}

}
