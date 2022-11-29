package com.shaic.claim.registration.balancesuminsured.view;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class OPProcessingDetailsTableDTO extends AbstractTableDTO implements
		Serializable {

	private static final long serialVersionUID = 1L;
	private String type;
	private String claimedAmount;
	private String nonPayable;
	private String payableAmt;
	private String status;
	private String typeofPayment;
	private String transactionNo;
	private String transactionDate;
	private String remarks;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getClaimedAmount() {
		return claimedAmount;
	}

	public void setClaimedAmount(String claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

	public String getNonPayable() {
		return nonPayable;
	}

	public void setNonPayable(String nonPayable) {
		this.nonPayable = nonPayable;
	}

	public String getPayableAmt() {
		return payableAmt;
	}

	public void setPayableAmt(String payableAmt) {
		this.payableAmt = payableAmt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTypeofPayment() {
		return typeofPayment;
	}

	public void setTypeofPayment(String typeofPayment) {
		this.typeofPayment = typeofPayment;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
