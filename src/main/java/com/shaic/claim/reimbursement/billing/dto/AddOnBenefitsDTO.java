package com.shaic.claim.reimbursement.billing.dto;

import java.io.Serializable;
import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;

public class AddOnBenefitsDTO implements Serializable {

	private static final long serialVersionUID = 8709693172219300318L;
	
	private String serialNo;
	
	private String particulars;
	
	private String dateOfAdmission;
	
	private String dateOfDischarge;
	
	private String admittedNoOfDays; 
	
	private Integer allowedNoOfDays;
	
	private Integer totalClaimedAmount = 0;
	
	private Integer entitledNoOfDays;
	
	private Integer noOfDaysPerHospitalization;
	
	private Integer entitlementPerDayAmt;
	
	private Integer utilizedNoOfDays;
	private Integer utilizedAmount;
	
	private Integer balanceAvailable;
	
	private Integer eligibleNoofDays;
	
	private Integer eligiblePayableNoOfDays;
	
	private Integer eligiblePerDayAmt;
	
	private Integer eligibleNetAmount;
	
	//private Double copayPercentage;
	
	private List<String> copayPercentage; 
	
	private SelectValue coPayPercentage;
	
	private Integer copayAmount = 0 ;
	
	private Integer netAmountAfterCopay = 0 ;
	
	private Integer limitAsPerPolicy = 0;
	
	private Integer payableAmount = 0;
	
	private List<Double> productCoPay;
	
	private String engagedFrom;
	
	private String engagedTo;
	
	private Integer phcBenefitId;
	
	private String disallowanceRemarks;
	
	private Integer totalNoOfDaysClaimed;
	
	private String disallowanceRemarksForBillAssessment;

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getParticulars() {
		return particulars;
	}

	public void setParticulars(String particulars) {
		this.particulars = particulars;
	}

	public String getDateOfAdmission() {
		return dateOfAdmission;
	}

	public void setDateOfAdmission(String dateOfAdmission) {
		this.dateOfAdmission = dateOfAdmission;
	}

	public String getDateOfDischarge() {
		return dateOfDischarge;
	}

	public void setDateOfDischarge(String dateOfDischarge) {
		this.dateOfDischarge = dateOfDischarge;
	}

	public String getAdmittedNoOfDays() {
		return admittedNoOfDays;
	}

	public void setAdmittedNoOfDays(String admittedNoOfDays) {
		this.admittedNoOfDays = admittedNoOfDays;
	}

	public Integer getAllowedNoOfDays() {
		return allowedNoOfDays;
	}

	public void setAllowedNoOfDays(Integer allowedNoOfDays) {
		this.allowedNoOfDays = allowedNoOfDays;
	}

	public Integer getTotalClaimedAmount() {
		return totalClaimedAmount;
	}

	public void setTotalClaimedAmount(Integer totalClaimedAmount) {
		this.totalClaimedAmount = totalClaimedAmount;
	}

	public Integer getEntitledNoOfDays() {
		return entitledNoOfDays;
	}

	public void setEntitledNoOfDays(Integer entitledNoOfDays) {
		this.entitledNoOfDays = entitledNoOfDays;
	}

	public Integer getNoOfDaysPerHospitalization() {
		return noOfDaysPerHospitalization;
	}

	public void setNoOfDaysPerHospitalization(Integer noOfDaysPerHospitalization) {
		this.noOfDaysPerHospitalization = noOfDaysPerHospitalization;
	}

	public Integer getEntitlementPerDayAmt() {
		return entitlementPerDayAmt;
	}

	public void setEntitlementPerDayAmt(Integer entitlementPerDayAmt) {
		this.entitlementPerDayAmt = entitlementPerDayAmt;
	}

	public Integer getUtilizedAmount() {
		return utilizedAmount;
	}

	public void setUtilizedAmount(Integer utilizedAmount) {
		this.utilizedAmount = utilizedAmount;
	}

	public Integer getBalanceAvailable() {
		return balanceAvailable;
	}

	public void setBalanceAvailable(Integer balanceAvailable) {
		this.balanceAvailable = balanceAvailable;
	}

	public Integer getEligibleNoofDays() {
		return eligibleNoofDays;
	}

	public void setEligibleNoofDays(Integer eligibleNoofDays) {
		this.eligibleNoofDays = eligibleNoofDays;
	}

	public Integer getEligiblePayableNoOfDays() {
		return eligiblePayableNoOfDays;
	}

	public void setEligiblePayableNoOfDays(Integer eligiblePayableNoOfDays) {
		this.eligiblePayableNoOfDays = eligiblePayableNoOfDays;
	}

	public Integer getEligiblePerDayAmt() {
		return eligiblePerDayAmt;
	}

	public void setEligiblePerDayAmt(Integer eligiblePerDayAmt) {
		this.eligiblePerDayAmt = eligiblePerDayAmt;
	}

	public Integer getEligibleNetAmount() {
		return eligibleNetAmount;
	}

	public void setEligibleNetAmount(Integer eligibleNetAmount) {
		this.eligibleNetAmount = eligibleNetAmount;
	}

/*	public Double getCopayPercentage() {
		return copayPercentage;
	}

	public void setCopayPercentage(Double copayPercentage) {
		this.copayPercentage = copayPercentage;
	}*/

	public Integer getCopayAmount() {
		return copayAmount;
	}

	public void setCopayAmount(Integer copayAmount) {
		this.copayAmount = copayAmount;
	}

	public Integer getNetAmountAfterCopay() {
		return netAmountAfterCopay;
	}

	public void setNetAmountAfterCopay(Integer netAmountAfterCopay) {
		this.netAmountAfterCopay = netAmountAfterCopay;
	}

	public Integer getLimitAsPerPolicy() {
		return limitAsPerPolicy;
	}

	public void setLimitAsPerPolicy(Integer limitAsPerPolicy) {
		this.limitAsPerPolicy = limitAsPerPolicy;
	}

	public Integer getPayableAmount() {
		return payableAmount;
	}

	public void setPayableAmount(Integer payableAmount) {
		this.payableAmount = payableAmount;
	}

	public Integer getUtilizedNoOfDays() {
		return utilizedNoOfDays;
	}

	public void setUtilizedNoOfDays(Integer utilizedNoOfDays) {
		this.utilizedNoOfDays = utilizedNoOfDays;
	}

	public List<String> getCopayPercentage() {
		return copayPercentage;
	}

	public void setCopayPercentage(List<String> copayPercentage) {
		this.copayPercentage = copayPercentage;
	}

	public List<Double> getProductCoPay() {
		return productCoPay;
	}

	public void setProductCoPay(List<Double> productCoPay) {
		this.productCoPay = productCoPay;
	}

	public SelectValue getCoPayPercentage() {
		return coPayPercentage;
	}

	public void setCoPayPercentage(SelectValue coPayPercentage) {
		this.coPayPercentage = coPayPercentage;
	}

	public String getEngagedFrom() {
		return engagedFrom;
	}

	public void setEngagedFrom(String engagedFrom) {
		this.engagedFrom = engagedFrom;
	}

	public String getEngagedTo() {
		return engagedTo;
	}

	public void setEngagedTo(String engagedTo) {
		this.engagedTo = engagedTo;
	}

	public Integer getPhcBenefitId() {
		return phcBenefitId;
	}

	public void setPhcBenefitId(Integer phcBenefitId) {
		this.phcBenefitId = phcBenefitId;
	}

	public String getDisallowanceRemarks() {
		return disallowanceRemarks;
	}

	public void setDisallowanceRemarks(String disallowanceRemarks) {
		this.disallowanceRemarks = disallowanceRemarks;
	}

	public Integer getTotalNoOfDaysClaimed() {
		return totalNoOfDaysClaimed;
	}

	public void setTotalNoOfDaysClaimed(Integer totalNoOfDaysClaimed) {
		this.totalNoOfDaysClaimed = totalNoOfDaysClaimed;
	}

	public String getDisallowanceRemarksForBillAssessment() {
		return disallowanceRemarksForBillAssessment;
	}

	public void setDisallowanceRemarksForBillAssessment(
			String disallowanceRemarksForBillAssessment) {
		this.disallowanceRemarksForBillAssessment = disallowanceRemarksForBillAssessment;
	}

	/*public Date getEngagedFrom() {
		return engagedFrom;
	}

	public void setEngagedFrom(Date engagedFrom) {
		this.engagedFrom = engagedFrom;
	}

	public Date getEngagedTo() {
		return engagedTo;
	}

	public void setEngagedTo(Date engagedTo) {
		this.engagedTo = engagedTo;
	}
*/
	

}
