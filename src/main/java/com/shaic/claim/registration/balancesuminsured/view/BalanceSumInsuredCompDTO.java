package com.shaic.claim.registration.balancesuminsured.view;

import java.io.Serializable;

public class BalanceSumInsuredCompDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5344969108892496230L;
	
	private String insuredName;
	private Double originalSumInsured;
	private Double cumulativeBonus;
	private Double rechargedSumInsured;
	private Double restoredSumInsured;
	private Double limitOfCoverage;
	public String getInsuredName() {
		return insuredName;
	}
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
	public Double getOriginalSumInsured() {
		return originalSumInsured;
	}
	public void setOriginalSumInsured(Double originalSumInsured) {
		this.originalSumInsured = originalSumInsured;
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
	
}
