package com.shaic.claim.reimbursement.paymentprocesscpu;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class PaymentProcessCpuTableDTO extends AbstractTableDTO  implements Serializable{
	
	private String intimationNo;
	private String claimNumber;
	private String cpuLotNo;
	private Double amount;
	private String chequeNo;
	private Date chequeDate;
	private String chequeDateValue;
	private String status;	
	private String ddNumber;
	private String bankName;
	private Long claimPaymentKey;
	private String createdBy;
	private Long cpuCode;
	private String letterFlag;
	private String docReceivedFrom;
	private Double tdsAmount;
	private String rodNo;
	private Date paymentDate;
	private String paymentType;
	private Double netAmount;
	private String claimType;
	private String accountNumber;
	private String ifscCode;
	private String branchName;
	private Double interestAmount;
	
	private Integer serialNo;
	
	private String benefitCover;
	
	private String rodType;
	private String payeeName;
	
	private Date faApprovalDate; 
	private String faApprovalDateValue; 
	private String financialRemarks;
	
	private String billClassification;
	private Long rodKey;
	
	private Date modifiedDate;
	
	private String portalStatusVal;
	private String websiteStatusVal;
	private String cpuCodeWithValue;
	private Date todayDate;	
	private String paymentPartyMode;
	
	private String policyNumber;
	
	private Long statusId;	
	
	private String policySource;
	
	private String bancsUprId;
	
	public String getRodType() {
		return rodType;
	}
	public void setRodType(String rodType) {
		this.rodType = rodType;
	}
	public String getBenefitCover() {
		return benefitCover;
	}
	public void setBenefitCover(String benefitCover) {
		this.benefitCover = benefitCover;
	}

	public Double getInterestAmount() {
		return interestAmount;
	}
	public void setInterestAmount(Double interestAmount) {
		this.interestAmount = interestAmount;
	}
	public Double getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getRodNo() {
		return rodNo;
	}
	public void setRodNo(String rodNo) {
		this.rodNo = rodNo;
	}
	public String getDocReceivedFrom() {
		return docReceivedFrom;
	}
	public void setDocReceivedFrom(String docReceivedFrom) {
		this.docReceivedFrom = docReceivedFrom;
	}
	public Double getTdsAmount() {
		return tdsAmount;
	}
	public void setTdsAmount(Double tdsAmount) {
		this.tdsAmount = tdsAmount;
	}
	public String getLetterFlag() {
		return letterFlag;
	}
	public void setLetterFlag(String letterFlag) {
		this.letterFlag = letterFlag;
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
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
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
		
		if(chequeDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(chequeDate);
			setChequeDateValue(dateformat);
		    this.chequeDate = chequeDate;
		}
	}
	public String getChequeDateValue() {
		return chequeDateValue;
	}
	public void setChequeDateValue(String chequeDateValue) {
		this.chequeDateValue = chequeDateValue;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getDdNumber() {
		return ddNumber;
	}
	public void setDdNumber(String ddNumber) {
		this.ddNumber = ddNumber;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public Long getClaimPaymentKey() {
		return claimPaymentKey;
	}
	public void setClaimPaymentKey(Long claimPaymentKey) {
		this.claimPaymentKey = claimPaymentKey;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Long getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(Long cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public Integer getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public String getFaApprovalDateValue() {
		return faApprovalDateValue;
	}
	public void setFaApprovalDateValue(String faApprovalDateValue) {
		this.faApprovalDateValue = faApprovalDateValue;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public Date getFaApprovalDate() {
		return faApprovalDate;
	}
	public void setFaApprovalDate(Date faApprovalDate) {
		
		if(faApprovalDate !=  null) {
		String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(faApprovalDate);
		setChequeDateValue(dateformat);
		this.faApprovalDate = faApprovalDate;
	}
	}
	public String getFinancialRemarks() {
		return financialRemarks;
	}
	public void setFinancialRemarks(String financialRemarks) {
		this.financialRemarks = financialRemarks;
	}
	public String getBillClassification() {
		return billClassification;
	}
	public void setBillClassification(String billClassification) {
		this.billClassification = billClassification;
	}
	public Long getRodKey() {
		return rodKey;
	}
	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getPortalStatusVal() {
		return portalStatusVal;
	}
	public void setPortalStatusVal(String portalStatusVal) {
		this.portalStatusVal = portalStatusVal;
	}
	public String getWebsiteStatusVal() {
		return websiteStatusVal;
	}
	public void setWebsiteStatusVal(String websiteStatusVal) {
		this.websiteStatusVal = websiteStatusVal;
	}

	public String getCpuCodeWithValue() {
		return cpuCodeWithValue;
	}
	public void setCpuCodeWithValue(String cpuCodeWithValue) {
		this.cpuCodeWithValue = cpuCodeWithValue;
	}	

	public Date getTodayDate() {
		return todayDate;
	}
	public void setTodayDate(Date todayDate) {
		this.todayDate = todayDate;
	}
	public String getPaymentPartyMode() {
		return paymentPartyMode;
	}
	public void setPaymentPartyMode(String paymentPartyMode) {
		this.paymentPartyMode = paymentPartyMode;
	}
	
	public String getPolicyNumber() {
		return policyNumber;
	}
	
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	
	public Long getStatusId() {
		return statusId;
	}
	
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
	
	public String getPolicySource() {
		return policySource;
	}
	
	public void setPolicySource(String policySource) {
		this.policySource = policySource;
	}
	
	public String getBancsUprId() {
		return bancsUprId;
	}
	
	public void setBancsUprId(String bancsUprId) {
		this.bancsUprId = bancsUprId;
	}	
    
	
}