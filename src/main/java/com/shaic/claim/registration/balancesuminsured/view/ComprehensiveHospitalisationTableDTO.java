package com.shaic.claim.registration.balancesuminsured.view;

public class ComprehensiveHospitalisationTableDTO {

	
	private String sectionI;
	private String cover;
	private String subCover;
	private Double sumInsured;
	private Double claimPaid;
	private Double claimOutStanding;
	private Double balance;
	private Double provisionCurrentClaim;
	private Double balanceSI;
	private String optionalcoverName;
	private Double policyLevelLimit;
	private String termPeriod;
	
	public Double getBalanceSI() {
		return balanceSI;
	}
	public void setBalanceSI(Double balanceSI) {
		this.balanceSI = balanceSI;
	}
	public String getSectionI() {
		return sectionI;
	}
	public void setSectionI(String sectionI) {
		this.sectionI = sectionI;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getSubCover() {
		return subCover;
	}
	public void setSubCover(String subCover) {
		this.subCover = subCover;
	}
	public Double getSumInsured() {
		return sumInsured;
	}
	public void setSumInsured(Double sumInsured) {
		this.sumInsured = sumInsured;
	}
	public Double getClaimPaid() {
		return claimPaid;
	}
	public void setClaimPaid(Double claimPaid) {
		this.claimPaid = claimPaid;
	}
	public Double getClaimOutStanding() {
		return claimOutStanding;
	}
	public void setClaimOutStanding(Double claimOutStanding) {
		this.claimOutStanding = claimOutStanding;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public Double getProvisionCurrentClaim() {
		return provisionCurrentClaim;
	}
	public void setProvisionCurrentClaim(Double provisionCurrentClaim) {
		this.provisionCurrentClaim = provisionCurrentClaim;
	}
	public String getOptionalcoverName() {
		return optionalcoverName;
	}
	public void setOptionalcoverName(String optionalcoverName) {
		this.optionalcoverName = optionalcoverName;
	}
	public Double getPolicyLevelLimit() {
		return policyLevelLimit;
	}
	public void setPolicyLevelLimit(Double policyLevelLimit) {
		this.policyLevelLimit = policyLevelLimit;
	}
	public String getTermPeriod() {
		return termPeriod;
	}
	public void setTermPeriod(String termPeriod) {
		this.termPeriod = termPeriod;
	}
}
