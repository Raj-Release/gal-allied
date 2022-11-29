package com.shaic.claim.registration.balancesuminsured.view;

public class PABalanceSumInsuredTableDTO {
	
	private String benefits;
	private String coverDesc;
	private Double orginalSI;
	private Double cumulativeBonus;
	private Double totalSI;
	private Double claimPaid;
	private Double claimOutStanding;
	private Double balanceSI;
	private Double currentClaim;
	private Double availableSI;
	
	public String getCoverDesc() {
		return coverDesc;
	}
	public void setCoverDesc(String coverDesc) {
		this.coverDesc = coverDesc;
	}
	
	public String getBenefits() {
		return benefits;
	}
	public void setBenefits(String benefits) {
		this.benefits = benefits;
	}
	public Double getOrginalSI() {
		return orginalSI;
	}
	public void setOrginalSI(Double orginalSI) {
		this.orginalSI = orginalSI;
	}
	public Double getCumulativeBonus() {
		return cumulativeBonus;
	}
	public void setCumulativeBonus(Double cumulativeBonus) {
		this.cumulativeBonus = cumulativeBonus;
	}
	public Double getTotalSI() {
		return totalSI;
	}
	public void setTotalSI(Double totalSI) {
		this.totalSI = totalSI;
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
	public Double getBalanceSI() {
		return balanceSI;
	}
	public void setBalanceSI(Double balanceSI) {
		this.balanceSI = balanceSI;
	}
	public Double getCurrentClaim() {
		return currentClaim;
	}
	public void setCurrentClaim(Double currentClaim) {
		this.currentClaim = currentClaim;
	}
	public Double getAvailableSI() {
		return availableSI;
	}
	public void setAvailableSI(Double availableSI) {
		this.availableSI = availableSI;
	}
	
}
