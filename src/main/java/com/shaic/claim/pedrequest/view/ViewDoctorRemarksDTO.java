package com.shaic.claim.pedrequest.view;

import java.util.Date;

public class ViewDoctorRemarksDTO {
	
	
	public String strNoteDate;
	
	public Date noteDate;
	
	public String transaction;
	
	public String transactionType;
	
	public String remarks;
	
	public String userId;

	public String getStrNoteDate() {
		return strNoteDate;
	}

	public void setStrNoteDate(String strNoteDate) {
		this.strNoteDate = strNoteDate;
	}

	public Date getNoteDate() {
		return noteDate;
	}

	public void setNoteDate(Date noteDate) {
		this.noteDate = noteDate;
	}

	public String getTransaction() {
		return transaction;
	}

	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	
	

}
