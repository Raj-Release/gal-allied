package com.shaic.claim.leagalbilling;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="IMS_CLS_LEGAL_TAX_DEDUCTION")
@NamedQueries({
	@NamedQuery(name="LegalTaxDeduction.findbyClaimKey",query = "SELECT o FROM LegalTaxDeduction o where o.claimKey = :claimKey and o.activeStatus = 1"),
	@NamedQuery(name="LegalTaxDeduction.findbyrodKey",query = "SELECT o FROM LegalTaxDeduction o where o.rodKey = :rodKey and o.activeStatus = 1")
})
public class LegalTaxDeduction implements Serializable{

	@Id
	@SequenceGenerator(name="LEGAL_TAX_DEDUCTION_GENERATOR", sequenceName = "SEQ_LEGAL_TAX_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LEGAL_TAX_DEDUCTION_GENERATOR" )
	@Column(name="TAX_KEY")
	private Long key;
	
	@Column(name="INTIMATION_KEY")
	private Long intimationKey;
	
	@Column(name="CLAIM_KEY")
	private Long claimKey;
	
	@Column(name="ROD_KEY")
	private Long rodKey;

	@Column(name="AWARD_AMOUNT")
	private Long awardAmount;

	@Column(name="TAX_COST")
	private Long cost;

	@Column(name="COMPENSATION_AMT")
	private Long compensation;

	@Column(name="INTEREST_APPLICABLE")
	private String interestApplicable;
	
	@Column(name="INTEREST_RATE")
	private Double interestRate;

	@Column(name="INT_FROM_DT")
	private Date interestfromDate;

	@Column(name="INT_TO_DT")
	private Date interesttoDate;

	@Column(name="TOTAL_DAYS")
	private Long totalnoofdays;

	@Column(name="INT_CURRENT_CLM")
	private Long interestCurrentClaim;

	@Column(name="INT_OTHER_CLM")
	private Long interestOtherClaim;

	@Column(name="PAN_NUMBER")
	private String panNo;

	@Column(name="AWARD_AMOUNT_REMARKS")
	private String awardAmountRemark;

	@Column(name="TAX_COST_REMARKS")
	private String costRemark;

	@Column(name="COMPENSATION_REMARKS")
	private String compensationRemark;

	@Column(name="INT_CURRENT_CLM_REMARKS")
	private String intrCurrentClaimRemark;

	@Column(name="INT_OTHER_CLM_REMARKS")
	private String intrOtherClaimRemark;

	@Column(name="INT_PAYABLE_REMARKS")
	private String intrPayRemark;

	@Column(name="TDS_REMARKS")
	private String tdsRemark;

	@Column(name="INT_PAYABLE_TDS_REMARKS")
	private String intrPayTDSRemark;

	@Column(name="TOT_AMT_PAY_REMARKS")
	private String totalPayRemark;
	
	@Column(name="ACTIVE_FLAG")
	private Long activeStatus;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="MODIFIED_BY")
	private String modifyBy;
	
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="PAN_NO_FLAG")
	private String panFlag;
	
	@Column(name="TDS_AMOUNT")
	private Long tdsAmount;
	
	@Column(name="TDS_PERCENTAGE")
	private Double tdsPercentge;

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

	public String getInterestApplicable() {
		return interestApplicable;
	}

	public void setInterestApplicable(String interestApplicable) {
		this.interestApplicable = interestApplicable;
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

	public String getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getPanFlag() {
		return panFlag;
	}

	public void setPanFlag(String panFlag) {
		this.panFlag = panFlag;
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
	
}
