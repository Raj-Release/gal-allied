package com.shaic.claim.ompviewroddetails;

import java.util.Date;

public class OMPViewReconsiderationRodTableDTO {
	
	private static final long serialVersionUID = 1l;
	
	private String siNo;
	
	private String rodNo;
	
	private String claimType;
	
	private String eventCode;
	
	private String classification;
	
	private String documentReceivedFrom;
	
	private String rodType;
	
	private String amount;
	
	private String paymentType;
	
	private String bankName;
	
	private String chequeOrTransactionNo;

	private Date  chequeOrTransactionDate;
	
	private String accountNo;
	
	private String ifscCode;
	
	private String branchName;
	
	public String getSiNo(){
		return siNo;
	}
	public void setSiNo(String siNo){
		this.siNo = siNo;
	}
	public String getRodNo(){
		return rodNo;
	}
	public void setRodNo(String rodNo){
		this.rodNo = rodNo;
	}
	public String getClaimType(){
		return claimType;
	}
	public void setClaimType(String claimType){
		this.claimType = claimType;
	}
	public String getEventCode(){
		return eventCode;
	}
	public void setEventCode(String eventCode){
		this.eventCode = eventCode;
	}
	public String getClassification(){
		return classification;
	}
	public void setClassification(String classification){
		this.classification = classification;
	}
	public String getDocumentReceivedFrom() {
		return documentReceivedFrom;
	}
	public void setDocumentReceivedFrom(String documentReceivedFrom) {
		this.documentReceivedFrom = documentReceivedFrom;
	}
	public String getRodType(){
		return rodType;
	}
	public void setRodType(String rodType){
		this.rodType = rodType;
	}
	public String getAmount(){
		return amount;
	}
	public void setAmount(String amount){
		this.amount = amount;
	}
	public String getPaymentType(){
		return paymentType;
	}
	public void setPaymentType(String paymentType){
		this.paymentType = paymentType;
	}
	public String getBankName(){
		return bankName;
	}
	public void setBankName(String bankName){
		this.bankName = bankName;
	}
	public String getChequeOrTransactionNo(){
		return chequeOrTransactionNo;
	}
	public void setChequeOrTransactionNo(String chequeOrTransactionNo){
		this.chequeOrTransactionNo = chequeOrTransactionNo;
	}
	public Date getChequeOrTransactionDate(){
		return chequeOrTransactionDate;
	}
	public void setChequeOrTransactionDate(Date chequeOrTransactionDate){
		this.chequeOrTransactionDate = chequeOrTransactionDate;
	}
	public String getAccountNo(){	
		return accountNo;
	}
	public void setAccountNo(String accountNo){
		this.accountNo = accountNo;
	}
	public String getIfscCode(){
		return ifscCode;
	}
	public void setIfscCode(String ifscCode){
		this.ifscCode = ifscCode;
	}
	public String getBranchName(){
		return branchName;
	}
	public void setBranchName(String branchName){
		this.branchName = branchName;
	}

}
