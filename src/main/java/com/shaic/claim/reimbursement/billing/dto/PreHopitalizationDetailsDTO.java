package com.shaic.claim.reimbursement.billing.dto;

import java.io.Serializable;

public class PreHopitalizationDetailsDTO implements Serializable{

	private static final long serialVersionUID = -6998183575903158319L;
	
	private Integer amountConsidered = 0;
	private Integer copayAmt = 0;
	private Integer payableAmt = 0;
	private Integer netPayable = 0;
	private Integer claimRestrictionAmt = 0;
	private Integer payableToInsAmt = 0;
	private Integer balancePremiumAmt = 0;
	private Integer payableToInsuredAftPremiumAmt = 0;
	private Integer availableSIAftHosp = 0;
	private Integer restrictedSIAftHosp = 0;
	private Double copayValue = 0d;
	private Boolean isSIRestrictionAvail = false;
	private Integer otherInsurerAmt = 0;
	private Integer amountConsideredForPayment = 0;
	private Integer revisedEligibleAmount = 0;
	private Integer previousRodPreviousPrehospAmt = 0;
	private Integer amountAlreadyPaid = 0;
	private Integer balanceToBePaid = 0;
	private Integer avaliableSublimitBalanceForBariatric = 0;
	private Integer section3AvailableSIAfterHospROD = 0;
	
	public Integer getAmountConsidered() {
		return amountConsidered;
	}
	public void setAmountConsidered(Integer amountConsidered) {
		this.amountConsidered = amountConsidered;
	}
	public Integer getCopayAmt() {
		return copayAmt;
	}
	public void setCopayAmt(Integer copayAmt) {
		this.copayAmt = copayAmt;
	}
	public Integer getPayableAmt() {
		return payableAmt;
	}
	public void setPayableAmt(Integer payableAmt) {
		this.payableAmt = payableAmt;
	}
	public Integer getNetPayable() {
		return netPayable;
	}
	public void setNetPayable(Integer netPayable) {
		this.netPayable = netPayable;
	}
	public Integer getClaimRestrictionAmt() {
		return claimRestrictionAmt;
	}
	public void setClaimRestrictionAmt(Integer claimRestrictionAmt) {
		this.claimRestrictionAmt = claimRestrictionAmt;
	}
	public Integer getPayableToInsAmt() {
		return payableToInsAmt;
	}
	public void setPayableToInsAmt(Integer payableToInsAmt) {
		this.payableToInsAmt = payableToInsAmt;
	}
	public Integer getBalancePremiumAmt() {
		return balancePremiumAmt;
	}
	public void setBalancePremiumAmt(Integer balancePremiumAmt) {
		this.balancePremiumAmt = balancePremiumAmt;
	}
	public Integer getPayableToInsuredAftPremiumAmt() {
		return payableToInsuredAftPremiumAmt;
	}
	public void setPayableToInsuredAftPremiumAmt(
			Integer payableToInsuredAftPremiumAmt) {
		this.payableToInsuredAftPremiumAmt = payableToInsuredAftPremiumAmt;
	}
	public Integer getAvailableSIAftHosp() {
		return availableSIAftHosp;
	}
	public Integer getRestrictedSIAftHosp() {
		return restrictedSIAftHosp;
	}
	public void setAvailableSIAftHosp(Integer availableSIAftHosp) {
		this.availableSIAftHosp = availableSIAftHosp;
	}
	public void setRestrictedSIAftHosp(Integer restrictedSIAftHosp) {
		this.restrictedSIAftHosp = restrictedSIAftHosp;
	}
	public Double getCopayValue() {
		return copayValue;
	}
	public void setCopayValue(Double copayValue) {
		this.copayValue = copayValue;
	}
	public Boolean getIsSIRestrictionAvail() {
		return isSIRestrictionAvail;
	}
	public void setIsSIRestrictionAvail(Boolean isSIRestrictionAvail) {
		this.isSIRestrictionAvail = isSIRestrictionAvail;
	}
	public Integer getOtherInsurerAmt() {
		return otherInsurerAmt;
	}
	public void setOtherInsurerAmt(Integer otherInsurerAmt) {
		this.otherInsurerAmt = otherInsurerAmt;
	}
	public Integer getAmountConsideredForPayment() {
		return amountConsideredForPayment;
	}
	public void setAmountConsideredForPayment(Integer amountConsideredForPayment) {
		this.amountConsideredForPayment = amountConsideredForPayment;
	}
	public Integer getRevisedEligibleAmount() {
		return revisedEligibleAmount;
	}
	public void setRevisedEligibleAmount(Integer revisedEligibleAmount) {
		this.revisedEligibleAmount = revisedEligibleAmount;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Integer getPreviousRodPreviousPrehospAmt() {
		return previousRodPreviousPrehospAmt;
	}
	public void setPreviousRodPreviousPrehospAmt(
			Integer previousRodPreviousPrehospAmt) {
		this.previousRodPreviousPrehospAmt = previousRodPreviousPrehospAmt;
	}
	public Integer getAmountAlreadyPaid() {
		return amountAlreadyPaid;
	}
	public void setAmountAlreadyPaid(Integer amountAlreadyPaid) {
		this.amountAlreadyPaid = amountAlreadyPaid;
	}
	public Integer getBalanceToBePaid() {
		return balanceToBePaid;
	}
	public void setBalanceToBePaid(Integer balanceToBePaid) {
		this.balanceToBePaid = balanceToBePaid;
	}
	public Integer getAvaliableSublimitBalanceForBariatric() {
		return avaliableSublimitBalanceForBariatric;
	}
	public void setAvaliableSublimitBalanceForBariatric(
			Integer avaliableSublimitBalanceForBariatric) {
		this.avaliableSublimitBalanceForBariatric = avaliableSublimitBalanceForBariatric;
	}
	public Integer getSection3AvailableSIAfterHospROD() {
		return section3AvailableSIAfterHospROD;
	}
	public void setSection3AvailableSIAfterHospROD(
			Integer section3AvailableSIAfterHospROD) {
		this.section3AvailableSIAfterHospROD = section3AvailableSIAfterHospROD;
	}
	
}
