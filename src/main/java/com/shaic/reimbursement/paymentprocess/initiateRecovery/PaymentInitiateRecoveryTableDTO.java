package com.shaic.reimbursement.paymentprocess.initiateRecovery;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;

public class PaymentInitiateRecoveryTableDTO extends AbstractTableDTO implements Serializable{
	
	private String rodNumber;
	private String billClassification;
	private String payeeName;
	
	private String intimationNo;
	private SelectValue documentReceivedFrom;
	private String hospitalCode;
	private String amountSettled;
	private String tdsDeducted;
	private String amountRecoverable;
	private SelectValue paymentCancelType;
	private SelectValue  reasonForRecovery;
	private SelectValue natureofRecovery;
	private SelectValue paymentTypeVal;
	
	private Long key;
	private String intimationNumber;
	private String policyNumber;
	private String recordCount;
	private String productCode;
	private Long cpuCode;
	private Long paymentCpuCode;
	private String emailId;
	private String paymentType;
	private String claimType;
	private String priorityFlag;//priority Flag//BANCS_PRIORITY_FLAG
	private String payeeRelationship;
	private String nomineeName;
	private String nomineeRelationship;
	private String legalHeirName;
	private String legalHeirRelationship;
	private String accType;
	private String ifscCode;
	private String accountNumber;
	private String bankName;
	private String branchName;
	private String nameAsperBankAC;//Name as per Bank AC//PAYEE_NAME
	private String panNumber;
	private String micrCode;
	private String virtualPaymentAddr;
	private String proposerName;
	private String gmcEmployeeName;
	private String payableAt; // confirm//DD_PAYABLE_AT
	private String bancsUprId;
	private String utrNumber;
	//private String bankName;//Bank Name
	private Date chequeDDDate;
	//Seach Payable City

	private String reconisderFlag;
	private Double approvedAmount;
	private Date lastAckDate;
	private Date faApprovalDate;
	private Long delayDays;
	private Long iRDATAT;//IRDA TAT
	private Double interestRate;//
	private Double interestAmount;//
	//No of Days Interest Payable
    //Penal Interest Amount

	private Double totalAmount;
	private String intrestRemarks;
	private String remarks;
	
	private Long totalRecords;
	private Boolean isRecordExceed = false;
	private Integer serialNo;
	private String sendToPayment;	
	private String reprocessType;	
//	private Button payableAtButton;	
	private String paymenetCancelRemarks ;	
	private String claimNumber;	
	private Long rodKey;	
	private Long paymentKey;	
	private Date createdDate;
	private String createdBy;
	private String modifiedBy;
	private Date modifiedDate;
	private String paymentCancelBancsRead;
	private Long paymenetCancelTypeKey;
	private Date glxReadDate;
	private Date bancsReadDate;
	private String paymentCancelGlxRead;
	private Long bnksKey;
	
	private String payableName;
	private String instrumentNo;
	private String remittanceBankName;
	private String remittanceBankBranch;
	private String remittanceAccount;
	private String partyCode;
	private String Lob;
	private Date instrumentDate;
	private String previousPaymentMode;
	
	
	public String getRodNumber() {
		return rodNumber;
	}
	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
	}
	public String getBillClassification() {
		return billClassification;
	}
	public void setBillClassification(String billClassification) {
		this.billClassification = billClassification;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public SelectValue getDocumentReceivedFrom() {
		return documentReceivedFrom;
	}
	public void setDocumentReceivedFrom(SelectValue documentReceivedFrom) {
		this.documentReceivedFrom = documentReceivedFrom;
	}
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	public String getAmountSettled() {
		return amountSettled;
	}
	public void setAmountSettled(String amountSettled) {
		this.amountSettled = amountSettled;
	}
	public String getTdsDeducted() {
		return tdsDeducted;
	}
	public void setTdsDeducted(String tdsDeducted) {
		this.tdsDeducted = tdsDeducted;
	}
	public String getAmountRecoverable() {
		return amountRecoverable;
	}
	public void setAmountRecoverable(String amountRecoverable) {
		this.amountRecoverable = amountRecoverable;
	}
	public SelectValue getPaymentCancelType() {
		return paymentCancelType;
	}
	public void setPaymentCancelType(SelectValue paymentCancelType) {
		this.paymentCancelType = paymentCancelType;
	}
	public SelectValue getReasonForRecovery() {
		return reasonForRecovery;
	}
	public void setReasonForRecovery(SelectValue reasonForRecovery) {
		this.reasonForRecovery = reasonForRecovery;
	}
	public SelectValue getNatureofRecovery() {
		return natureofRecovery;
	}
	public void setNatureofRecovery(SelectValue natureofRecovery) {
		this.natureofRecovery = natureofRecovery;
	}
	public SelectValue getPaymentTypeVal() {
		return paymentTypeVal;
	}
	public void setPaymentTypeVal(SelectValue paymentTypeVal) {
		this.paymentTypeVal = paymentTypeVal;
	}
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	public String getIntimationNumber() {
		return intimationNumber;
	}
	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public String getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(String recordCount) {
		this.recordCount = recordCount;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public Long getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(Long cpuCode) {
		this.cpuCode = cpuCode;
	}
	public Long getPaymentCpuCode() {
		return paymentCpuCode;
	}
	public void setPaymentCpuCode(Long paymentCpuCode) {
		this.paymentCpuCode = paymentCpuCode;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getPriorityFlag() {
		return priorityFlag;
	}
	public void setPriorityFlag(String priorityFlag) {
		this.priorityFlag = priorityFlag;
	}
	public String getPayeeRelationship() {
		return payeeRelationship;
	}
	public void setPayeeRelationship(String payeeRelationship) {
		this.payeeRelationship = payeeRelationship;
	}
	public String getNomineeName() {
		return nomineeName;
	}
	public void setNomineeName(String nomineeName) {
		this.nomineeName = nomineeName;
	}
	public String getNomineeRelationship() {
		return nomineeRelationship;
	}
	public void setNomineeRelationship(String nomineeRelationship) {
		this.nomineeRelationship = nomineeRelationship;
	}
	public String getLegalHeirName() {
		return legalHeirName;
	}
	public void setLegalHeirName(String legalHeirName) {
		this.legalHeirName = legalHeirName;
	}
	public String getLegalHeirRelationship() {
		return legalHeirRelationship;
	}
	public void setLegalHeirRelationship(String legalHeirRelationship) {
		this.legalHeirRelationship = legalHeirRelationship;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getNameAsperBankAC() {
		return nameAsperBankAC;
	}
	public void setNameAsperBankAC(String nameAsperBankAC) {
		this.nameAsperBankAC = nameAsperBankAC;
	}
	public String getPanNumber() {
		return panNumber;
	}
	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}
	public String getMicrCode() {
		return micrCode;
	}
	public void setMicrCode(String micrCode) {
		this.micrCode = micrCode;
	}
	public String getVirtualPaymentAddr() {
		return virtualPaymentAddr;
	}
	public void setVirtualPaymentAddr(String virtualPaymentAddr) {
		this.virtualPaymentAddr = virtualPaymentAddr;
	}
	public String getProposerName() {
		return proposerName;
	}
	public void setProposerName(String proposerName) {
		this.proposerName = proposerName;
	}
	public String getGmcEmployeeName() {
		return gmcEmployeeName;
	}
	public void setGmcEmployeeName(String gmcEmployeeName) {
		this.gmcEmployeeName = gmcEmployeeName;
	}
	public String getPayableAt() {
		return payableAt;
	}
	public void setPayableAt(String payableAt) {
		this.payableAt = payableAt;
	}
	public String getBancsUprId() {
		return bancsUprId;
	}
	public void setBancsUprId(String bancsUprId) {
		this.bancsUprId = bancsUprId;
	}
	public String getUtrNumber() {
		return utrNumber;
	}
	public void setUtrNumber(String utrNumber) {
		this.utrNumber = utrNumber;
	}
	public Date getChequeDDDate() {
		return chequeDDDate;
	}
	public void setChequeDDDate(Date chequeDDDate) {
		this.chequeDDDate = chequeDDDate;
	}
	public String getReconisderFlag() {
		return reconisderFlag;
	}
	public void setReconisderFlag(String reconisderFlag) {
		this.reconisderFlag = reconisderFlag;
	}
	public Double getApprovedAmount() {
		return approvedAmount;
	}
	public void setApprovedAmount(Double approvedAmount) {
		this.approvedAmount = approvedAmount;
	}
	public Date getLastAckDate() {
		return lastAckDate;
	}
	public void setLastAckDate(Date lastAckDate) {
		this.lastAckDate = lastAckDate;
	}
	public Date getFaApprovalDate() {
		return faApprovalDate;
	}
	public void setFaApprovalDate(Date faApprovalDate) {
		this.faApprovalDate = faApprovalDate;
	}
	public Long getDelayDays() {
		return delayDays;
	}
	public void setDelayDays(Long delayDays) {
		this.delayDays = delayDays;
	}
	public Long getiRDATAT() {
		return iRDATAT;
	}
	public void setiRDATAT(Long iRDATAT) {
		this.iRDATAT = iRDATAT;
	}
	public Double getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(Double interestRate) {
		this.interestRate = interestRate;
	}
	public Double getInterestAmount() {
		return interestAmount;
	}
	public void setInterestAmount(Double interestAmount) {
		this.interestAmount = interestAmount;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getIntrestRemarks() {
		return intrestRemarks;
	}
	public void setIntrestRemarks(String intrestRemarks) {
		this.intrestRemarks = intrestRemarks;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Long getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(Long totalRecords) {
		this.totalRecords = totalRecords;
	}
	public Boolean getIsRecordExceed() {
		return isRecordExceed;
	}
	public void setIsRecordExceed(Boolean isRecordExceed) {
		this.isRecordExceed = isRecordExceed;
	}
	public Integer getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}
	public String getSendToPayment() {
		return sendToPayment;
	}
	public void setSendToPayment(String sendToPayment) {
		this.sendToPayment = sendToPayment;
	}
	public String getReprocessType() {
		return reprocessType;
	}
	public void setReprocessType(String reprocessType) {
		this.reprocessType = reprocessType;
	}
	public String getPaymenetCancelRemarks() {
		return paymenetCancelRemarks;
	}
	public void setPaymenetCancelRemarks(String paymenetCancelRemarks) {
		this.paymenetCancelRemarks = paymenetCancelRemarks;
	}
	public String getClaimNumber() {
		return claimNumber;
	}
	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}
	public Long getRodKey() {
		return rodKey;
	}
	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}
	public Long getPaymentKey() {
		return paymentKey;
	}
	public void setPaymentKey(Long paymentKey) {
		this.paymentKey = paymentKey;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getPaymentCancelBancsRead() {
		return paymentCancelBancsRead;
	}
	public void setPaymentCancelBancsRead(String paymentCancelBancsRead) {
		this.paymentCancelBancsRead = paymentCancelBancsRead;
	}
	public Long getPaymenetCancelTypeKey() {
		return paymenetCancelTypeKey;
	}
	public void setPaymenetCancelTypeKey(Long paymenetCancelTypeKey) {
		this.paymenetCancelTypeKey = paymenetCancelTypeKey;
	}
	public Date getGlxReadDate() {
		return glxReadDate;
	}
	public void setGlxReadDate(Date glxReadDate) {
		this.glxReadDate = glxReadDate;
	}
	public Date getBancsReadDate() {
		return bancsReadDate;
	}
	public void setBancsReadDate(Date bancsReadDate) {
		this.bancsReadDate = bancsReadDate;
	}
	public String getPaymentCancelGlxRead() {
		return paymentCancelGlxRead;
	}
	public void setPaymentCancelGlxRead(String paymentCancelGlxRead) {
		this.paymentCancelGlxRead = paymentCancelGlxRead;
	}
	public Long getBnksKey() {
		return bnksKey;
	}
	public void setBnksKey(Long bnksKey) {
		this.bnksKey = bnksKey;
	}
	public String getPayableName() {
		return payableName;
	}
	public void setPayableName(String payableName) {
		this.payableName = payableName;
	}
	public String getInstrumentNo() {
		return instrumentNo;
	}
	public void setInstrumentNo(String instrumentNo) {
		this.instrumentNo = instrumentNo;
	}
	public String getRemittanceBankName() {
		return remittanceBankName;
	}
	public void setRemittanceBankName(String remittanceBankName) {
		this.remittanceBankName = remittanceBankName;
	}
	public String getRemittanceBankBranch() {
		return remittanceBankBranch;
	}
	public void setRemittanceBankBranch(String remittanceBankBranch) {
		this.remittanceBankBranch = remittanceBankBranch;
	}
	public String getRemittanceAccount() {
		return remittanceAccount;
	}
	public void setRemittanceAccount(String remittanceAccount) {
		this.remittanceAccount = remittanceAccount;
	}
	public String getPartyCode() {
		return partyCode;
	}
	public void setPartyCode(String partyCode) {
		this.partyCode = partyCode;
	}
	public String getLob() {
		return Lob;
	}
	public void setLob(String lob) {
		Lob = lob;
	}
	public Date getInstrumentDate() {
		return instrumentDate;
	}
	public void setInstrumentDate(Date instrumentDate) {
		this.instrumentDate = instrumentDate;
	}
	public String getPreviousPaymentMode() {
		return previousPaymentMode;
	}
	public void setPreviousPaymentMode(String previousPaymentMode) {
		this.previousPaymentMode = previousPaymentMode;
	}
	
	

}
