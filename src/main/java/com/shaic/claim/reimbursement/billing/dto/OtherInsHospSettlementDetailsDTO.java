package com.shaic.claim.reimbursement.billing.dto;

import java.io.Serializable;

public class OtherInsHospSettlementDetailsDTO implements Serializable {

	private static final long serialVersionUID = -6998183575903158319L;
	
	private Integer totalClaimedAmt = 0 ;
	private Integer nonMedicalAmt = 0;
	private Integer netClaimedAmt = 0;
	private Integer tpaSettledAmt = 0;
	private Integer balanceAmt = 0;
	private Integer hospPayableAmt = 0;
	private Integer payableToIns = 0;
	private Integer amountAlreadyPaid = 0;
	private Integer payableAmt = 0;
	
	public Integer getTotalClaimedAmt() {
		return totalClaimedAmt;
	}
	public void setTotalClaimedAmt(Integer totalClaimedAmt) {
		this.totalClaimedAmt = totalClaimedAmt;
	}
	public Integer getNonMedicalAmt() {
		return nonMedicalAmt;
	}
	public void setNonMedicalAmt(Integer nonMedicalAmt) {
		this.nonMedicalAmt = nonMedicalAmt;
	}
	public Integer getNetClaimedAmt() {
		return netClaimedAmt;
	}
	public void setNetClaimedAmt(Integer netClaimedAmt) {
		this.netClaimedAmt = netClaimedAmt;
	}
	public Integer getTpaSettledAmt() {
		return tpaSettledAmt;
	}
	public void setTpaSettledAmt(Integer tpaSettledAmt) {
		this.tpaSettledAmt = tpaSettledAmt;
	}
	public Integer getBalanceAmt() {
		return balanceAmt;
	}
	public void setBalanceAmt(Integer balanceAmt) {
		this.balanceAmt = balanceAmt;
	}
	public Integer getHospPayableAmt() {
		return hospPayableAmt;
	}
	public void setHospPayableAmt(Integer hospPayableAmt) {
		this.hospPayableAmt = hospPayableAmt;
	}
	public Integer getPayableToIns() {
		return payableToIns;
	}
	public void setPayableToIns(Integer payableToIns) {
		this.payableToIns = payableToIns;
	}
	public Integer getAmountAlreadyPaid() {
		return amountAlreadyPaid;
	}
	public void setAmountAlreadyPaid(Integer amountAlreadyPaid) {
		this.amountAlreadyPaid = amountAlreadyPaid;
	}
	public Integer getPayableAmt() {
		return payableAmt;
	}
	public void setPayableAmt(Integer payableAmt) {
		this.payableAmt = payableAmt;
	}


}
