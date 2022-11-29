package com.shaic.claim.OMPProcessOmpClaimProcessor.pages;

import java.io.Serializable;

import com.shaic.domain.Reimbursement;

public class OMPCalulationDetailsDTO implements Serializable {

	private static final long serialVersionUID = -6998183575903158319L;
	
	private Integer netPayableAmt = 0 ;
	private Integer claimRestrictionAmt = 0;
	private Integer preauthAppAmt = 0;
	private Integer preauthAppAmtBeforePremium = 0;
	private Integer installmentAmount = 0;
	private Integer payableToHospitalAmt = 0;
	private Integer payableToInsAmt = 0;
	private Integer tdsAmt = 0;
	private Integer payableToHospitalAftTDSAmt = 0;
	private Integer balancePremiumAmt = 0;
	private Integer payableToInsuredAftPremiumAmt = 0;
	private Integer amountAlreadyPaid = 0;
	private Integer balanceToBePaid = 0;
	private Reimbursement reimbursement;
	private Integer hospitalDiscount = 0;
	private Integer afterHospitalDiscount = 0;

	public Integer getNetPayableAmt() {
		return netPayableAmt;
	}
	public void setNetPayableAmt(Integer netPayableAmt) {
		this.netPayableAmt = netPayableAmt;
	}
	public Integer getClaimRestrictionAmt() {
		return claimRestrictionAmt;
	}
	public void setClaimRestrictionAmt(Integer claimRestrictionAmt) {
		this.claimRestrictionAmt = claimRestrictionAmt;
	}
	public Integer getPreauthAppAmt() {
		return preauthAppAmt;
	}
	public void setPreauthAppAmt(Integer preauthAppAmt) {
		this.preauthAppAmt = preauthAppAmt;
	}
	public Integer getPayableToHospitalAmt() {
		return payableToHospitalAmt;
	}
	public void setPayableToHospitalAmt(Integer payableToHospitalAmt) {
		this.payableToHospitalAmt = payableToHospitalAmt;
	}
	public Integer getPayableToInsAmt() {
		return payableToInsAmt;
	}
	public void setPayableToInsAmt(Integer payableToInsAmt) {
		this.payableToInsAmt = payableToInsAmt;
	}
	public Integer getTdsAmt() {
		return tdsAmt;
	}
	public void setTdsAmt(Integer tdsAmt) {
		this.tdsAmt = tdsAmt;
	}
	public Integer getPayableToHospitalAftTDSAmt() {
		return payableToHospitalAftTDSAmt;
	}
	public void setPayableToHospitalAftTDSAmt(Integer payableToHospitalAftTDSAmt) {
		this.payableToHospitalAftTDSAmt = payableToHospitalAftTDSAmt;
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
	public Reimbursement getReimbursement() {
		return reimbursement;
	}
	public void setReimbursement(Reimbursement reimbursement) {
		this.reimbursement = reimbursement;
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
	public Integer getHospitalDiscount() {
		return hospitalDiscount;
	}
	public void setHospitalDiscount(Integer hospitalDiscount) {
		this.hospitalDiscount = hospitalDiscount;
	}
	public Integer getAfterHospitalDiscount() {
		return afterHospitalDiscount;
	}
	public void setAfterHospitalDiscount(Integer afterHospitalDiscount) {
		this.afterHospitalDiscount = afterHospitalDiscount;
	}
	public Integer getPreauthAppAmtBeforePremium() {
		return preauthAppAmtBeforePremium;
	}
	public void setPreauthAppAmtBeforePremium(Integer preauthAppAmtBeforePremium) {
		this.preauthAppAmtBeforePremium = preauthAppAmtBeforePremium;
	}
	public Integer getInstallmentAmount() {
		return installmentAmount;
	}
	public void setInstallmentAmount(Integer installmentAmount) {
		this.installmentAmount = installmentAmount;
	}
	

}
