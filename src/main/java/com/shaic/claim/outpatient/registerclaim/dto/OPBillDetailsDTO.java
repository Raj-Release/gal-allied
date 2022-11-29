package com.shaic.claim.outpatient.registerclaim.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shaic.arch.table.AbstractTableDTO;

public class OPBillDetailsDTO extends AbstractTableDTO implements Serializable{
	private static final long serialVersionUID = 805207001992276413L;
	
	private Long key;
	
	private String details;
	
	private Long masterId;
	
	@NotNull(message = "Please Enter Bill Date")
	private Date billDate;
	
	private String billDateStr;
	
	@NotNull(message = "Please Enter Bill Number")
	@Size(min = 1, message = "Please Enter Bill Number")
	private String billNumber;
	
	@NotNull(message = "Please Enter Claimed Amount")
	@Size(min = 1, message = "Please Enter Claimed Amount")
	private String claimedAmount;
	
	@NotNull(message = "Please Enter Deductibles or Non Payable amount")
	@Size(min = 1, message = "Please Enter Deductibles or Non Payable amount")
	private String nonPayableAmt;
	
	private String payableAmt;
	
	private String nonPayableReason;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Long getMasterId() {
		return masterId;
	}

	public void setMasterId(Long masterId) {
		this.masterId = masterId;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public String getBillDateStr() {
		return billDateStr;
	}

	public void setBillDateStr(String billDateStr) {
		this.billDateStr = billDateStr;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public String getClaimedAmount() {
		return claimedAmount;
	}

	public void setClaimedAmount(String claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

	public String getNonPayableAmt() {
		return nonPayableAmt;
	}

	public void setNonPayableAmt(String nonPayableAmt) {
		this.nonPayableAmt = nonPayableAmt;
	}

	public String getPayableAmt() {
		return payableAmt;
	}

	public void setPayableAmt(String payableAmt) {
		this.payableAmt = payableAmt;
	}

	public String getNonPayableReason() {
		return nonPayableReason;
	}

	public void setNonPayableReason(String nonPayableReason) {
		this.nonPayableReason = nonPayableReason;
	}
	
	
	
}
