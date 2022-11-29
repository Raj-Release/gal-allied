package com.shaic.claim.leagalbilling;

import java.util.Date;

import javax.persistence.Column;

public class LegalBillingDTO {
	
	private Long key;
	
	private Long awardAmount;
	
	private Long cost;
	
	private Long compensation;

	private Double interestRate;
	
	private Date interestfromDate;
	
	private Date interesttoDate;
	
	private Long totalnoofdays;
	
	private Long interestCurrentClaim;
	
	private Long interestOtherClaim;
	
	private Long totalInterestAmount;
	
	private Boolean panDetails;
	
	private Boolean interestApplicable;
	
	private String panNo;

	private String awardAmountRemark;

	private String costRemark;

	private String compensationRemark;
	
	private String intrCurrentClaimRemark;
	
	private String intrOtherClaimRemark;
	
	private String intrPayRemark;
	
	private String tdsRemark;
	
	private String intrPayTDSRemark;
	
	private String totalPayRemark;
	
	private Long intimationKey;
	
	private Long claimKey;
	
	private Long rodKey;
	
	private Long activeStatus;
	
	private String createdBy;
	
	private Date createdDate;
	
	private Long tdsAmount;
	
	private Double tdsPercentge;
	
	private Long tdsOtherClaim;
	
	private Long totalApprovedAmount;

	public Long getAwardAmount() {
		return awardAmount;
	}

	public void setAwardAmount(Long awardAmount) {
		this.awardAmount = awardAmount;
	}

	public Long getCost() {
		return cost;
	}

	public void setCost(Long cost) {
		this.cost = cost;
	}

	public Long getCompensation() {
		return compensation;
	}

	public void setCompensation(Long compensation) {
		this.compensation = compensation;
	}

	public Double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(Double interestRate) {
		this.interestRate = interestRate;
	}

	public Date getInterestfromDate() {
		return interestfromDate;
	}

	public void setInterestfromDate(Date interestfromDate) {
		this.interestfromDate = interestfromDate;
	}

	public Date getInteresttoDate() {
		return interesttoDate;
	}

	public void setInteresttoDate(Date interesttoDate) {
		this.interesttoDate = interesttoDate;
	}

	public Long getTotalnoofdays() {
		return totalnoofdays;
	}

	public void setTotalnoofdays(Long totalnoofdays) {
		this.totalnoofdays = totalnoofdays;
	}

	public Long getInterestCurrentClaim() {
		return interestCurrentClaim;
	}

	public void setInterestCurrentClaim(Long interestCurrentClaim) {
		this.interestCurrentClaim = interestCurrentClaim;
	}

	public Long getInterestOtherClaim() {
		return interestOtherClaim;
	}

	public void setInterestOtherClaim(Long interestOtherClaim) {
		this.interestOtherClaim = interestOtherClaim;
	}

	public Long getTotalInterestAmount() {
		return totalInterestAmount;
	}

	public void setTotalInterestAmount(Long totalInterestAmount) {
		this.totalInterestAmount = totalInterestAmount;
	}

	public Boolean getPanDetails() {
		return panDetails;
	}

	public void setPanDetails(Boolean panDetails) {
		this.panDetails = panDetails;
	}

	public Boolean getInterestApplicable() {
		return interestApplicable;
	}

	public void setInterestApplicable(Boolean interestApplicable) {
		this.interestApplicable = interestApplicable;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	public String getAwardAmountRemark() {
		return awardAmountRemark;
	}

	public void setAwardAmountRemark(String awardAmountRemark) {
		this.awardAmountRemark = awardAmountRemark;
	}

	public String getCostRemark() {
		return costRemark;
	}

	public void setCostRemark(String costRemark) {
		this.costRemark = costRemark;
	}

	public String getCompensationRemark() {
		return compensationRemark;
	}

	public void setCompensationRemark(String compensationRemark) {
		this.compensationRemark = compensationRemark;
	}

	public String getIntrCurrentClaimRemark() {
		return intrCurrentClaimRemark;
	}

	public void setIntrCurrentClaimRemark(String intrCurrentClaimRemark) {
		this.intrCurrentClaimRemark = intrCurrentClaimRemark;
	}

	public String getIntrOtherClaimRemark() {
		return intrOtherClaimRemark;
	}

	public void setIntrOtherClaimRemark(String intrOtherClaimRemark) {
		this.intrOtherClaimRemark = intrOtherClaimRemark;
	}

	public String getIntrPayRemark() {
		return intrPayRemark;
	}

	public void setIntrPayRemark(String intrPayRemark) {
		this.intrPayRemark = intrPayRemark;
	}

	public String getTdsRemark() {
		return tdsRemark;
	}

	public void setTdsRemark(String tdsRemark) {
		this.tdsRemark = tdsRemark;
	}

	public String getIntrPayTDSRemark() {
		return intrPayTDSRemark;
	}

	public void setIntrPayTDSRemark(String intrPayTDSRemark) {
		this.intrPayTDSRemark = intrPayTDSRemark;
	}

	public String getTotalPayRemark() {
		return totalPayRemark;
	}

	public void setTotalPayRemark(String totalPayRemark) {
		this.totalPayRemark = totalPayRemark;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public Long getRodKey() {
		return rodKey;
	}

	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getTdsAmount() {
		return tdsAmount;
	}

	public void setTdsAmount(Long tdsAmount) {
		this.tdsAmount = tdsAmount;
	}

	public Double getTdsPercentge() {
		return tdsPercentge;
	}

	public void setTdsPercentge(Double tdsPercentge) {
		this.tdsPercentge = tdsPercentge;
	}

	public Long getTdsOtherClaim() {
		return tdsOtherClaim;
	}

	public void setTdsOtherClaim(Long tdsOtherClaim) {
		this.tdsOtherClaim = tdsOtherClaim;
	}

	public Long getTotalApprovedAmount() {
		return totalApprovedAmount;
	}

	public void setTotalApprovedAmount(Long totalApprovedAmount) {
		this.totalApprovedAmount = totalApprovedAmount;
	}		
		
		
}
