package com.shaic.claim.preauth.dto;

import java.io.Serializable;

public class SpecificProductDeductibleTableDTO implements Serializable {
	private static final long serialVersionUID = -5205600137880465698L;
	
	private String claims;
	
	private Integer amountConsidered;
	
	private Integer originalSI;
	
	private Integer amountToBeConsidered;
	
	private Integer deductible;
	
	private Integer payableAmount;
	
	private Integer balanceSI;
	
	private Integer eligibleAmountPayable;
	
	private Integer amountPayable;

	public String getClaims() {
		return claims;
	}

	public void setClaims(String claims) {
		this.claims = claims;
	}

	public Integer getAmountConsidered() {
		return amountConsidered;
	}

	public void setAmountConsidered(Integer amountConsidered) {
		this.amountConsidered = amountConsidered;
	}

	public Integer getOriginalSI() {
		return originalSI;
	}

	public void setOriginalSI(Integer originalSI) {
		this.originalSI = originalSI;
	}

	public Integer getAmountToBeConsidered() {
		return amountToBeConsidered;
	}

	public void setAmountToBeConsidered(Integer amountToBeConsidered) {
		this.amountToBeConsidered = amountToBeConsidered;
	}

	public Integer getDeductible() {
		return deductible;
	}

	public void setDeductible(Integer deductible) {
		this.deductible = deductible;
	}

	public Integer getPayableAmount() {
		return payableAmount;
	}

	public void setPayableAmount(Integer payableAmount) {
		this.payableAmount = payableAmount;
	}

	public Integer getBalanceSI() {
		return balanceSI;
	}

	public void setBalanceSI(Integer balanceSI) {
		this.balanceSI = balanceSI;
	}

	public Integer getAmountPayable() {
		return amountPayable;
	}

	public void setAmountPayable(Integer amountPayable) {
		this.amountPayable = amountPayable;
	}

	public Integer getEligibleAmountPayable() {
		return eligibleAmountPayable;
	}

	public void setEligibleAmountPayable(Integer eligibleAmountPayable) {
		this.eligibleAmountPayable = eligibleAmountPayable;
	}
	

}
