package com.shaic.claim.OMPProcessOmpClaimProcessor.pages;

import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;

public class OMPClaimProcessorCalculationSheetDTO {

	private Double amttotal =0d;
	
	private Double deductiblePerPolicy =0d;
	
	private SelectValue coPayPercentage;
	
	private Double coPayApprovedAmt =0d;
	
	private Double negotiateAgreedAmt =0d;
	
	private Double balanceSumInured =0d;
	
	private Double totalAmtPayable =0d;
	
	private Double alreadyPaidAmt =0d;
	
	private Double payableAmt =0d;
	
	private Double inrAmt =0d;
	
	private Date dateOfRecovery;
	
	private Double amtRecoveredINR =0d;
	
	private Double amtRecoveredDollar =0d;
	
	private String remarks;
	
	private Double coPayAmount =0d;

	private String deductiblerecoveredfrominsured;
	
	public Double getDeductiblePerPolicy() {
		return deductiblePerPolicy;
	}

	public void setDeductiblePerPolicy(Double deductiblePerPolicy) {
		this.deductiblePerPolicy = deductiblePerPolicy;
	}

	public SelectValue getCoPayPercentage() {
		return coPayPercentage;
	}

	public void setCoPayPercentage(SelectValue coPayPercentage) {
		this.coPayPercentage = coPayPercentage;
	}

	public Double getCoPayApprovedAmt() {
		return coPayApprovedAmt;
	}

	public void setCoPayApprovedAmt(Double coPayApprovedAmt) {
		this.coPayApprovedAmt = coPayApprovedAmt;
	}

	public Double getNegotiateAgreedAmt() {
		return negotiateAgreedAmt;
	}

	public void setNegotiateAgreedAmt(Double negotiateAgreedAmt) {
		this.negotiateAgreedAmt = negotiateAgreedAmt;
	}

	public Double getBalanceSumInured() {
		return balanceSumInured;
	}

	public void setBalanceSumInured(Double balanceSumInured) {
		this.balanceSumInured = balanceSumInured;
	}

	public Double getTotalAmtPayable() {
		return totalAmtPayable;
	}

	public void setTotalAmtPayable(Double totalAmtPayable) {
		this.totalAmtPayable = totalAmtPayable;
	}

	public Double getAlreadyPaidAmt() {
		return alreadyPaidAmt;
	}

	public void setAlreadyPaidAmt(Double alreadyPaidAmt) {
		this.alreadyPaidAmt = alreadyPaidAmt;
	}

	public Double getPayableAmt() {
		return payableAmt;
	}

	public void setPayableAmt(Double payableAmt) {
		this.payableAmt = payableAmt;
	}

	public Double getInrAmt() {
		return inrAmt;
	}

	public void setInrAmt(Double inrAmt) {
		this.inrAmt = inrAmt;
	}
	
	public Date getDateOfRecovery() {
		return dateOfRecovery;
	}

	public void setDateOfRecovery(Date dateOfRecovery) {
		this.dateOfRecovery = dateOfRecovery;
	}

	public Double getAmtRecoveredINR() {
		return amtRecoveredINR;
	}

	public void setAmtRecoveredINR(Double amtRecoveredINR) {
		this.amtRecoveredINR = amtRecoveredINR;
	}

	public Double getAmtRecoveredDollar() {
		return amtRecoveredDollar;
	}

	public void setAmtRecoveredDollar(Double amtRecoveredDollar) {
		this.amtRecoveredDollar = amtRecoveredDollar;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Double getAmttotal() {
		return amttotal;
	}

	public void setAmttotal(Double amttotal) {
		this.amttotal = amttotal;
	}

	public Double getCoPayAmount() {
		return coPayAmount;
	}

	public void setCoPayAmount(Double coPayAmount) {
		this.coPayAmount = coPayAmount;
	}

	public String getDeductiblerecoveredfrominsured() {
		return deductiblerecoveredfrominsured;
	}

	public void setDeductiblerecoveredfrominsured(
			String deductiblerecoveredfrominsured) {
		this.deductiblerecoveredfrominsured = deductiblerecoveredfrominsured;
	}
	
}
