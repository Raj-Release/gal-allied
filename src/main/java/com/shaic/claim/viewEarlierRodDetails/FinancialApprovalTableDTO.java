package com.shaic.claim.viewEarlierRodDetails;

import com.shaic.arch.table.AbstractTableDTO;

public class FinancialApprovalTableDTO extends AbstractTableDTO{
	
	private Integer sno;
	
	private String approvalType;
	
	private String documentReceivedFrom;
	
	private String faDate;
	
	private String amount;
	
	private String status;
	
	private String paymentType;
	
	private String transactionNo;
	
	private String  transactionDate;
	
	private String financialRemarks;
	
	private Long rodKey;
	
	private String rodNumber;
	
	private String rodType;
	
	private String typeOfClaim;
	
	private String billClassification;
	
	public String getRodType() {
		return rodType;
	}

	public void setRodType(String rodType) {
		this.rodType = rodType;
	}

	public String getRodNumber() {
		return rodNumber;
	}

	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
	}

	public String getBenefitCover() {
		return benefitCover;
	}

	public void setBenefitCover(String benefitCover) {
		this.benefitCover = benefitCover;
	}

	private String benefitCover;

	public String getApprovalType() {
		return approvalType;
	}

	public void setApprovalType(String approvalType) {
		this.approvalType = approvalType;
	}


	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
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

	public String getFinancialRemarks() {
		return financialRemarks;
	}

	public void setFinancialRemarks(String financialRemarks) {
		this.financialRemarks = financialRemarks;
	}

	public Integer getSno() {
		return sno;
	}

	public void setSno(Integer sno) {
		this.sno = sno;
	}

	

	public Long getRodKey() {
		return rodKey;
	}

	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	public String getFaDate() {
		return faDate;
	}

	public void setFaDate(String faDate) {
		this.faDate = faDate;
	}

	public String getDocumentReceivedFrom() {
		return documentReceivedFrom;
	}

	public void setDocumentReceivedFrom(String documentReceivedFrom) {
		this.documentReceivedFrom = documentReceivedFrom;
	}

	public String getTypeOfClaim() {
		return typeOfClaim;
	}

	public void setTypeOfClaim(String typeOfClaim) {
		this.typeOfClaim = typeOfClaim;
	}

	public String getBillClassification() {
		return billClassification;
	}

	public void setBillClassification(String billClassification) {
		this.billClassification = billClassification;
	}
	
	

}
