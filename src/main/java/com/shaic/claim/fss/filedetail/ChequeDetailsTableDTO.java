package com.shaic.claim.fss.filedetail;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.shaic.arch.table.AbstractTableDTO;

public class ChequeDetailsTableDTO extends AbstractTableDTO {

	private Long key;
	
	@NotNull(message = "Please Enter Cheque Number")
	private String chequeNo;
	
	private Date chequeDate;
	
	private String bankName;
	
	private String bankBranch;

	public String getChequeNo() {
		return chequeNo;
	}

	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}

	public Date getChequeDate() {
		return chequeDate;
	}

	public void setChequeDate(Date chequeDate) {
		this.chequeDate = chequeDate;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankBranch() {
		return bankBranch;
	}

	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}
	
}
