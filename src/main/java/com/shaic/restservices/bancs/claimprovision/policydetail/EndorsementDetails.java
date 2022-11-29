package com.shaic.restservices.bancs.claimprovision.policydetail;

public class EndorsementDetails {
	
	private String typeOfEndorsement;
	private String endorsementEffectiveDate;
	private String endorsementNumber;
	private Integer endorsementPremium;
	private Integer endorsementSumInsured;
	private Integer endorsementRevisedSumInsured;
	private String endorsementApprovedDate;
	private String remarks;
	
	public String getTypeOfEndorsement() {
		return typeOfEndorsement;
	}
	public void setTypeOfEndorsement(String typeOfEndorsement) {
		this.typeOfEndorsement = typeOfEndorsement;
	}
	public String getEndorsementEffectiveDate() {
		return endorsementEffectiveDate;
	}
	public void setEndorsementEffectiveDate(String endorsementEffectiveDate) {
		this.endorsementEffectiveDate = endorsementEffectiveDate;
	}
	public String getEndorsementNumber() {
		return endorsementNumber;
	}
	public void setEndorsementNumber(String endorsementNumber) {
		this.endorsementNumber = endorsementNumber;
	}
	public Integer getEndorsementPremium() {
		return endorsementPremium;
	}
	public void setEndorsementPremium(Integer endorsementPremium) {
		this.endorsementPremium = endorsementPremium;
	}
	public Integer getEndorsementSumInsured() {
		return endorsementSumInsured;
	}
	public void setEndorsementSumInsured(Integer endorsementSumInsured) {
		this.endorsementSumInsured = endorsementSumInsured;
	}
	public Integer getEndorsementRevisedSumInsured() {
		return endorsementRevisedSumInsured;
	}
	public void setEndorsementRevisedSumInsured(Integer endorsementRevisedSumInsured) {
		this.endorsementRevisedSumInsured = endorsementRevisedSumInsured;
	}
	public String getEndorsementApprovedDate() {
		return endorsementApprovedDate;
	}
	public void setEndorsementApprovedDate(String endorsementApprovedDate) {
		this.endorsementApprovedDate = endorsementApprovedDate;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
