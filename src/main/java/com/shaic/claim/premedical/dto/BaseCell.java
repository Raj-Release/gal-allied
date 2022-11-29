package com.shaic.claim.premedical.dto;

import java.io.Serializable;

import com.shaic.arch.SHAUtils;

public class BaseCell implements Serializable{

	private static final long serialVersionUID = 3319986328134637125L;
	
	private Long benefitId;

	private Float totalBillingDays;// claimed days
	
	private Integer billingPerDayAmount;
	
	private Integer claimedBillAmount;
	
	private Integer deductibleAmount;
	
	private Integer netAmount;
	
	private Float totalDaysForPolicy; //  product days
	
	private Integer policyPerDayPayment;
	
	private Integer policyMaxAmount;
	
	private Integer paybleAmount;
	
	private Integer nonPayableAmount;
	
	private String restrictTo;
	
	private String overridePackageDeduction;
	
	private String restrictToFlag;
	
	private String overridePackageDeductionFlag;
	
	private Integer considerPerDayAmt;
	
	private String nonPayableReason;
	
	private String description;
	
	private Integer sno;

	public Long getBenefitId() {
		return benefitId;
	}

	public void setBenefitId(Long benefitId) {
		
		try{
			if(benefitId != null){
				String description = SHAUtils.baseCellMap.get(benefitId);
				this.description = description;
				Integer sno = SHAUtils.baseCellSno.get(benefitId);
				this.sno = sno;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		this.benefitId = benefitId;
	}

	public Float getTotalBillingDays() {
		return totalBillingDays;
	}

	public void setTotalBillingDays(Float totalBillingDays) {
		this.totalBillingDays = totalBillingDays;
	}

	public Integer getBillingPerDayAmount() {
		return billingPerDayAmount;
	}

	public void setBillingPerDayAmount(Integer billingPerDayAmount) {
		this.billingPerDayAmount = billingPerDayAmount;
	}

	public Integer getClaimedBillAmount() {
		return claimedBillAmount;
	}

	public void setClaimedBillAmount(Integer claimedBillAmount) {
		this.claimedBillAmount = claimedBillAmount;
	}

	public Integer getDeductibleAmount() {
		return deductibleAmount;
	}

	public void setDeductibleAmount(Integer deductibleAmount) {
		this.deductibleAmount = deductibleAmount;
	}

	public Integer getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Integer netAmount) {
		this.netAmount = netAmount;
	}

	public Float getTotalDaysForPolicy() {
		return totalDaysForPolicy;
	}

	public void setTotalDaysForPolicy(Float totalDaysForPolicy) {
		this.totalDaysForPolicy = totalDaysForPolicy;
	}

	public Integer getPolicyPerDayPayment() {
		return policyPerDayPayment;
	}

	public void setPolicyPerDayPayment(Integer policyPerDayPayment) {
		this.policyPerDayPayment = policyPerDayPayment;
	}

	public Integer getPolicyMaxAmount() {
		return policyMaxAmount;
	}

	public void setPolicyMaxAmount(Integer policyMaxAmount) {
		this.policyMaxAmount = policyMaxAmount;
	}

	public Integer getPaybleAmount() {
		return paybleAmount;
	}

	public void setPaybleAmount(Integer paybleAmount) {
		this.paybleAmount = paybleAmount;
	}

	public Integer getNonPayableAmount() {
		return nonPayableAmount;
	}

	public void setNonPayableAmount(Integer nonPayableAmount) {
		this.nonPayableAmount = nonPayableAmount;
	}

	public String getRestrictTo() {
		return restrictTo;
	}

	public void setRestrictTo(String restrictTo) {
		this.restrictTo = restrictTo;
	}

	public String getOverridePackageDeduction() {
		return overridePackageDeduction;
	}

	public void setOverridePackageDeduction(String overridePackageDeduction) {
		this.overridePackageDeduction = overridePackageDeduction;
	}

	public String getRestrictToFlag() {
		return restrictToFlag;
	}

	public void setRestrictToFlag(String restrictToFlag) {
		this.restrictToFlag = restrictToFlag;
	}

	public String getOverridePackageDeductionFlag() {
		return overridePackageDeductionFlag;
	}

	public void setOverridePackageDeductionFlag(String overridePackageDeductionFlag) {
		this.overridePackageDeductionFlag = overridePackageDeductionFlag;
	}

	public Integer getConsiderPerDayAmt() {
		return considerPerDayAmt;
	}

	public void setConsiderPerDayAmt(Integer considerPerDayAmt) {
		this.considerPerDayAmt = considerPerDayAmt;
	}

	public String getNonPayableReason() {
		return nonPayableReason;
	}

	public void setNonPayableReason(String nonPayableReason) {
		this.nonPayableReason = nonPayableReason;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSno() {
		return sno;
	}

	public void setSno(Integer sno) {
		this.sno = sno;
	}

	
	
	

	
}
