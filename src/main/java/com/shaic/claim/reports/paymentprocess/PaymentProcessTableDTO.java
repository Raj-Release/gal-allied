package com.shaic.claim.reports.paymentprocess;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class PaymentProcessTableDTO extends AbstractTableDTO  implements Serializable{
	private String intimationNo;
	private String claimNumber;
	private String cpuLotNo;
	private String amount;
	private String chequeNo;
	private String chequeDate;
	
	
	
	PaymentProcessTableDTO(String intimationNo,String claimNumber, String cpuLotNo,String amount,String chequeNo,String chequeDate)
	{
		this.intimationNo = intimationNo;
		this.claimNumber = claimNumber;
		this.cpuLotNo = cpuLotNo;
		this.amount = amount;
		this.chequeNo = chequeNo;
		this.chequeDate = chequeDate;
	}
	

	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getClaimNumber() {
		return claimNumber;
	}
	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}
	public String getCpuLotNo() {
		return cpuLotNo;
	}
	public void setCpuLotNo(String cpuLotNo) {
		this.cpuLotNo = cpuLotNo;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getChequeNo() {
		return chequeNo;
	}
	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}
	public String getChequeDate() {
		return chequeDate;
	}
	public void setChequeDate(String chequeDate) {
		this.chequeDate = chequeDate;
	}
	


}
