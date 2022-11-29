package com.shaic.claim.ompviewroddetails;

import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimCalculationViewTableDTO;

public class OMPPaymentDetailsTableDTO {
	
	private static final long serialVersionUID = 1L;
	
	private String sno;
	
	private String rodNo;
	
	private String claimType;
	
	private String eventCode;
	
	private String classification;
	
	private String documentReceivedFrom;
	
	private String rodType;
	
	private String amount;
	
	private String paymentType;
	
	private String bankName;
	
	private String chequeOrTransactionno;
	
	private Date chequeOrTransactionDate;
	
	private String accountno;
	
	private String ifscCode;
	
	private String branchName;
	
	private Long serialNumber;
	
	private SelectValue paymentTo;
	
	private String payeeNameStr;
	
	private SelectValue payMode;
	
	private String payableAt;
	
	private String panNo;
	
	private String emailId;
	
	private String transectionChequeNo;
	
	private Date paymentDate;
	
	private String paymentStatus;
	
	private Long key;
	
	private Date ompProcessApprovedDate;
	
	private String remarks; 
	
	private SelectValue currency;
	
	//Additional fields for CR20181327
	
	private Date chequeDate;

	private String convRateInInr; 
	
	private String convAmtInInr;
	
	private String bankChargeInInr;

	private String settleAmt;

	private Date postedDate;
	
	
	//CR2019034
	
	OMPClaimCalculationViewTableDTO ompClaimCalculationViewTableDTO;

	 

	
	public OMPClaimCalculationViewTableDTO getOmpClaimCalculationViewTableDTO() {
		return ompClaimCalculationViewTableDTO;
	}
	public void setOmpClaimCalculationViewTableDTO(
			OMPClaimCalculationViewTableDTO ompClaimCalculationViewTableDTO) {
		this.ompClaimCalculationViewTableDTO = ompClaimCalculationViewTableDTO;
	}
	public String getSno(){
		return sno;
	}
	public void setSno(String sno){
		this.sno = sno;
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
	public String getDocumentReceivedFrom(){
		return documentReceivedFrom;
	}
	public void setDocumentReceivedFrom(String documentReceivedFrom){
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
	public String getChequeOrTransactionno(){
		return chequeOrTransactionno;
	}
	public void setChequeOrTransactionno(String chequeOrTransactionno){
		this.chequeOrTransactionno = chequeOrTransactionno;
	}
	public Date getChequeOrTransactionDate(){
		return chequeOrTransactionDate;
	}
	public void setChequeOrTransactionDate(Date chequeOrTransactionDate){
		this.chequeOrTransactionDate = chequeOrTransactionDate;
	}
	public String getAccountno(){
		return accountno;
	}
	public void setAccountno(String accountno){
		this.accountno = accountno;
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
	public Long getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(Long serialNumber) {
		this.serialNumber = serialNumber;
	}
	public SelectValue getPaymentTo() {
		return paymentTo;
	}
	public void setPaymentTo(SelectValue paymentTo) {
		this.paymentTo = paymentTo;
	}
	public String getPayeeNameStr() {
		return payeeNameStr;
	}
	public void setPayeeNameStr(String payeeNameStr) {
		this.payeeNameStr = payeeNameStr;
	}
	public SelectValue getPayMode() {
		return payMode;
	}
	public void setPayMode(SelectValue payMode) {
		this.payMode = payMode;
	}
	public String getPayableAt() {
		return payableAt;
	}
	public void setPayableAt(String payableAt) {
		this.payableAt = payableAt;
	}
	public String getPanNo() {
		return panNo;
	}
	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getTransectionChequeNo() {
		return transectionChequeNo;
	}
	public void setTransectionChequeNo(String transectionChequeNo) {
		this.transectionChequeNo = transectionChequeNo;
	}
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	public Date getOmpProcessApprovedDate() {
		return ompProcessApprovedDate;
	}
	public void setOmpProcessApprovedDate(Date ompProcessApprovedDate) {
		this.ompProcessApprovedDate = ompProcessApprovedDate;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public SelectValue getCurrency() {
		return currency;
	}
	public void setCurrency(SelectValue currency) {
		this.currency = currency;
	}
	
	public Date getChequeDate() {
		return chequeDate;
	}
	public void setChequeDate(Date chequeDate) {
		this.chequeDate = chequeDate;
	}
	public String getConvRateInInr() {
		return convRateInInr;
	}
	public void setConvRateInInr(String convRateInInr) {
		this.convRateInInr = convRateInInr;
	}
	public String getConvAmtInInr() {
		return convAmtInInr;
	}
	public void setConvAmtInInr(String convAmtInInr) {
		this.convAmtInInr = convAmtInInr;
	}
	public String getBankChargeInInr() {
		return bankChargeInInr;
	}
	public void setBankChargeInInr(String bankChargeInInr) {
		this.bankChargeInInr = bankChargeInInr;
	}
	public String getSettleAmt() {
		return settleAmt;
	}
	public void setSettleAmt(String settleAmt) {
		this.settleAmt = settleAmt;
	}
	public Date getPostedDate() {
		return postedDate;
	}
	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}
	
	
}
