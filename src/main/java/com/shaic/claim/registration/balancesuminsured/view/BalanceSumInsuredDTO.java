package com.shaic.claim.registration.balancesuminsured.view;

import java.io.Serializable;

public class BalanceSumInsuredDTO implements Serializable {

	private static final long serialVersionUID = -3364891006344735182L;

	private Double previousClaimPaid;

	private Double claimOutStanding;
	
	private Double cumulativeBonus;
	
	private Double rechargedSumInsured;
	
	private Double restoredSumInsured;
	
	private Double limitOfCoverage;
	
	private Double provisionAmout;
	
	private Double outstandingAmout;
	
	private Double paidAmout;
	
	private Double rechargeAmount;

	public Double getPreviousClaimPaid() {
		return previousClaimPaid;
	}

	public void setPreviousClaimPaid(Double previousClaimPaid) {
		this.previousClaimPaid = previousClaimPaid;
	}

	public Double getClaimOutStanding() {
		return claimOutStanding;
	}

	public void setClaimOutStanding(Double claimOutStanding) {
		this.claimOutStanding = claimOutStanding;
	}

	public Double getCumulativeBonus() {
		return cumulativeBonus;
	}

	public void setCumulativeBonus(Double cumulativeBonus) {
		this.cumulativeBonus = cumulativeBonus;
	}

	public Double getRechargedSumInsured() {
		return rechargedSumInsured;
	}

	public void setRechargedSumInsured(Double rechargedSumInsured) {
		this.rechargedSumInsured = rechargedSumInsured;
	}

	public Double getRestoredSumInsured() {
		return restoredSumInsured;
	}

	public void setRestoredSumInsured(Double restoredSumInsured) {
		this.restoredSumInsured = restoredSumInsured;
	}

	public Double getLimitOfCoverage() {
		return limitOfCoverage;
	}

	public void setLimitOfCoverage(Double limitOfCoverage) {
		this.limitOfCoverage = limitOfCoverage;
	}

	public Double getProvisionAmout() {
		return provisionAmout;
	}

	public void setProvisionAmout(Double provisionAmout) {
		this.provisionAmout = provisionAmout;
	}

	public Double getOutstandingAmout() {
		return outstandingAmout;
	}

	public void setOutstandingAmout(Double outstandingAmout) {
		this.outstandingAmout = outstandingAmout;
	}

	public Double getPaidAmout() {
		return paidAmout;
	}

	public void setPaidAmout(Double paidAmout) {
		this.paidAmout = paidAmout;
	}

	public Double getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(Double rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	
}
