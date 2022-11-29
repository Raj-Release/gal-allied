package com.shaic.claim.policy.search.ui;

import org.codehaus.jackson.annotate.JsonProperty;

public class TempRestDTO {
	

	@JsonProperty("{")
	private String startToken;
	
	@JsonProperty("PolicyNo")
	 private String policyNo ;

	@JsonProperty("PolicyEndNo")
	  private String policyEndNo;

	@JsonProperty("IntimationNo")
	  private String intimationNo;

	@JsonProperty("ClaimNo")
	  private String claimNo;

	@JsonProperty("HealthIDCard")
	  private String healthIDCard;

	@JsonProperty("RiskSysID")
	  private String riskSysID; 
	
	@JsonProperty("ReasonForAdmission")
	  private String reasonForAdmission;

	@JsonProperty("AdmissionDate")
	  private String admissionDate;

	@JsonProperty("HospitalCode")
	  private String hospitalCode;

	@JsonProperty("}")
	private String endToken;

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getPolicyEndNo() {
		return policyEndNo;
	}

	public void setPolicyEndNo(String policyEndNo) {
		this.policyEndNo = policyEndNo;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getHealthIDCard() {
		return healthIDCard;
	}

	public void setHealthIDCard(String healthIDCard) {
		this.healthIDCard = healthIDCard;
	}

	public String getRiskSysID() {
		return riskSysID;
	}

	public void setRiskSysID(String riskSysID) {
		this.riskSysID = riskSysID;
	}

	public String getReasonForAdmission() {
		return reasonForAdmission;
	}

	public void setReasonForAdmission(String reasonForAdmission) {
		this.reasonForAdmission = reasonForAdmission;
	}

	public String getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(String admissionDate) {
		this.admissionDate = admissionDate;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getStartToken() {
		return startToken;
	}

	public void setStartToken(String startToken) {
		this.startToken = startToken;
	}

	public String getEndToken() {
		return endToken;
	}

	public void setEndToken(String endToken) {
		this.endToken = endToken;
	}
	

	  
}
