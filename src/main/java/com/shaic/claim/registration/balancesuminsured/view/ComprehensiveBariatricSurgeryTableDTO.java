package com.shaic.claim.registration.balancesuminsured.view;

public class ComprehensiveBariatricSurgeryTableDTO {
	
	private String sectionVI;
	private String cover;
	private String subCover;
	private Double limit;
	private Double claimPaid;
	private Double claimOutstanding;
	private Double balance;
	private Double provisionCurrentClaim;
	private Double balanceSI;
	
	public Double getBalanceSI() {
		return balanceSI;
	}
	public void setBalanceSI(Double balanceSI) {
		this.balanceSI = balanceSI;
	}
	public String getSectionVI() {
		return sectionVI;
	}
	public void setSectionVI(String sectionVI) {
		this.sectionVI = sectionVI;
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
	public Double getLimit() {
		return limit;
	}
	public void setLimit(Double limit) {
		this.limit = limit;
	}
	public Double getClaimPaid() {
		return claimPaid;
	}
	public void setClaimPaid(Double claimPaid) {
		this.claimPaid = claimPaid;
	}
	public Double getClaimOutstanding() {
		return claimOutstanding;
	}
	public void setClaimOutstanding(Double claimOutstanding) {
		this.claimOutstanding = claimOutstanding;
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
	
	

}
