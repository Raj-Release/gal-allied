package com.shaic.claim.reimbursement.dto;

import java.io.Serializable;
import java.util.Date;

public class ReceiptOfDocumentAndMedicalProcess implements Serializable{

	private String acknowledgmentNo;
	
	private String reimbursementNo;
	
	private String documentReceivedFrom;
	
	private String documentReceivedDate;
	
	private String medicalResponseDate;
	
	private String modeOfRececipt;
	
	private String billClassification;
	
	private String status;
	
	private String remarks;
	
	private Double amount;
	
	private Date letterGeneratorDate;
	
	private String noOfLetterGeneratedQuery;
	
	private Date reminderDate;
	
	private Long reimbursementKey;
	
	private String typeOfClaim;
	
	private String acknowledgementToken;

	private String reconsiderationRequestFlagged;

	private String reconsiderationRequestFlagRemarks;

	public String getAcknowledgmentNo() {
		return acknowledgmentNo;
	}

	public void setAcknowledgmentNo(String acknowledgmentNo) {
		this.acknowledgmentNo = acknowledgmentNo;
	}

	public String getReimbursementNo() {
		return reimbursementNo;
	}

	public void setReimbursementNo(String reimbursementNo) {
		this.reimbursementNo = reimbursementNo;
	}

	public String getDocumentReceivedFrom() {
		return documentReceivedFrom;
	}

	public void setDocumentReceivedFrom(String documentReceivedFrom) {
		this.documentReceivedFrom = documentReceivedFrom;
	}

	public String getMedicalResponseDate() {
		return medicalResponseDate;
	}

	public void setMedicalResponseDate(String medicalResponseDate) {
		this.medicalResponseDate = medicalResponseDate;
	}

	public String getModeOfRececipt() {
		return modeOfRececipt;
	}

	public void setModeOfRececipt(String modeOfRececipt) {
		this.modeOfRececipt = modeOfRececipt;
	}

	public String getBillClassification() {
		return billClassification;
	}

	public void setBillClassification(String billClassification) {
		this.billClassification = billClassification;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getLetterGeneratorDate() {
		return letterGeneratorDate;
	}

	public void setLetterGeneratorDate(Date letterGeneratorDate) {
		this.letterGeneratorDate = letterGeneratorDate;
	}

	public String getNoOfLetterGeneratedQuery() {
		return noOfLetterGeneratedQuery;
	}

	public void setNoOfLetterGeneratedQuery(String noOfLetterGeneratedQuery) {
		this.noOfLetterGeneratedQuery = noOfLetterGeneratedQuery;
	}

	public Date getReminderDate() {
		return reminderDate;
	}

	public void setReminderDate(Date reminderDate) {
		this.reminderDate = reminderDate;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getDocumentReceivedDate() {
		return documentReceivedDate;
	}

	public void setDocumentReceivedDate(String documentReceivedDate) {
		this.documentReceivedDate = documentReceivedDate;
	}

	public Long getReimbursementKey() {
		return reimbursementKey;
	}

	public void setReimbursementKey(Long reimbursementKey) {
		this.reimbursementKey = reimbursementKey;
	}

	public String getTypeOfClaim() {
		return typeOfClaim;
	}

	public void setTypeOfClaim(String typeOfClaim) {
		this.typeOfClaim = typeOfClaim;
	}

	public String getAcknowledgementToken() {
		return acknowledgementToken;
	}

	public void setAcknowledgementToken(String acknowledgementToken) {
		this.acknowledgementToken = acknowledgementToken;
	}
	
	public String getReconsiderationRequestFlagged() {
		return reconsiderationRequestFlagged;
	}

	public void setReconsiderationRequestFlagged(
			String reconsiderationRequestFlagged) {
		this.reconsiderationRequestFlagged = reconsiderationRequestFlagged;
	}

	public String getReconsiderationRequestFlagRemarks() {
		return reconsiderationRequestFlagRemarks;
	}

	public void setReconsiderationRequestFlagRemarks(
			String reconsiderationRequestFlagRemarks) {
		this.reconsiderationRequestFlagRemarks = reconsiderationRequestFlagRemarks;
	}
	
}
