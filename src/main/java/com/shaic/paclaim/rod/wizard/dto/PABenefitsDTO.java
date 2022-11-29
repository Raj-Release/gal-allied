/**
 * 
 */
package com.shaic.paclaim.rod.wizard.dto;

import com.shaic.arch.fields.dto.SelectValue;

/**
 * @author ntv.vijayar
 *
 */
public class PABenefitsDTO {
	
	private String benefitCover;
	
	private SelectValue benefitCoverValue;
	
	private String percentage;
	
	private Double sumInsured;
	
	private Double eligibleAmount;
	
	/**
	 * Added for TTDBenefit value
	 * */
	private Double eligibleAmountPerWeek;
	
	private Long noOfWeeks;
	
	private Long benefitsId;
	
	private Double totalEligibleAmount;
	
	private Long key;

	

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	public Double getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(Double sumInsured) {
		this.sumInsured = sumInsured;
	}

	
	public Double getEligibleAmountPerWeek() {
		return eligibleAmountPerWeek;
	}

	public void setEligibleAmountPerWeek(Double eligibleAmountPerWeek) {
		this.eligibleAmountPerWeek = eligibleAmountPerWeek;
	}

	public Long getNoOfWeeks() {
		return noOfWeeks;
	}

	public void setNoOfWeeks(Long noOfWeeks) {
		this.noOfWeeks = noOfWeeks;
	}

	public Long getBenefitsId() {
		return benefitsId;
	}

	public void setBenefitsId(Long benefitsId) {
		this.benefitsId = benefitsId;
	}

	

	public SelectValue getBenefitCoverValue() {
		return benefitCoverValue;
	}

	public void setBenefitCoverValue(SelectValue benefitCoverValue) {
		this.benefitCoverValue = benefitCoverValue;
	}

	public void setBenefitCover(String benefitCover) {
		this.benefitCover = benefitCover;
	}

	public Double getTotalEligibleAmount() {
		return totalEligibleAmount;
	}

	public void setTotalEligibleAmount(Double totalEligibleAmount) {
		this.totalEligibleAmount = totalEligibleAmount;
	}

	public String getBenefitCover() {
		return benefitCover;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Double getEligibleAmount() {
		return eligibleAmount;
	}

	public void setEligibleAmount(Double eligibleAmount) {
		this.eligibleAmount = eligibleAmount;
	}
	
	

}
