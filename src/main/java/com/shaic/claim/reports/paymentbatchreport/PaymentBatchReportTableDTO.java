package com.shaic.claim.reports.paymentbatchreport;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class PaymentBatchReportTableDTO extends AbstractTableDTO  implements Serializable{

	
	private String intimationNo;
	private String claimNo;
	private String policyNo;	
	private String product;
	private String accountBatchNo;
	private String typeOfClaim;
	private String paymentType;
	private String lotNo;
	private Double approvedAmt;
	private Double serviceTax;
	private Double sumOfApprovedAndServiceTax;
	private Long tdsAmt;
	private Long netAmnt;
	private Long tdsPercentage;	
	private String payeeName;
	private String payableAt;
	private String cpuCode;
	private String ifscCode;
	private String beneficiaryAcntNo;
	private String branchName;
	private String chequeNo;
	private Date chequeDate;
	private String strChequeDate;
	private String rodNumber;
	private Long bankCode;
	private String bankName;
	private String panNo;
	private String providerCode;
	private Long refNo;
	private Date paymentReqDt;
	private String paymentReqDateValue;
	private String userID;
	private String emailID;	
	private String zonalMailId;
	private String pioCode;
	
	private String productNameValue;
	private Date lastAckDate;
	private String lastAckDateValue;
	private Date nextDayOfFaApprovedDate;
	private String nextDayOfFaApprovedDateValue;
	private Double penalInterestRate;
	private Integer noOfExceedingDays;
	private Integer exceedingIRDATatDays;
	private Double penalInterestAmnt;
	private Double penalTotalAmnt;
	private String penalRemarks;
	private Date faApprovedDate;
	
	
	public String getPioCode() {
		return pioCode;
	}
	public void setPioCode(String pioCode) {
		this.pioCode = pioCode;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getAccountBatchNo() {
		return accountBatchNo;
	}
	public void setAccountBatchNo(String accountBatchNo) {
		this.accountBatchNo = accountBatchNo;
	}
	public String getTypeOfClaim() {
		return typeOfClaim;
	}
	public void setTypeOfClaim(String typeOfClaim) {
		this.typeOfClaim = typeOfClaim;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getLotNo() {
		return lotNo;
	}
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	public Double getApprovedAmt() {
		return approvedAmt;
	}
	public void setApprovedAmt(Double approvedAmt) {
		this.approvedAmt = approvedAmt;
	}
	public Double getServiceTax() {
		return serviceTax;
	}
	public void setServiceTax(Double serviceTax) {
		this.serviceTax = serviceTax;
	}
	public Double getSumOfApprovedAndServiceTax() {
		return sumOfApprovedAndServiceTax;
	}
	public void setSumOfApprovedAndServiceTax(Double sumOfApprovedAndServiceTax) {
		this.sumOfApprovedAndServiceTax = sumOfApprovedAndServiceTax;
	}
	public Long getTdsAmt() {
		return tdsAmt;
	}
	public void setTdsAmt(Long tdsAmt) {
		this.tdsAmt = tdsAmt;
	}
	public Long getNetAmnt() {
		return netAmnt;
	}
	public void setNetAmnt(Long netAmnt) {
		this.netAmnt = netAmnt;
	}
	public Long getTdsPercentage() {
		return tdsPercentage;
	}
	public void setTdsPercentage(Long tdsPercentage) {
		this.tdsPercentage = tdsPercentage;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public String getPayableAt() {
		return payableAt;
	}
	public void setPayableAt(String payableAt) {
		this.payableAt = payableAt;
	}
	public String getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public String getBeneficiaryAcntNo() {
		return beneficiaryAcntNo;
	}
	public void setBeneficiaryAcntNo(String beneficiaryAcntNo) {
		this.beneficiaryAcntNo = beneficiaryAcntNo;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
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
		this.chequeDate = chequeDate;
	}
	public Long getBankCode() {
		return bankCode;
	}
	public void setBankCode(Long bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getPanNo() {
		return panNo;
	}
	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}
	public String getProviderCode() {
		return providerCode;
	}
	public void setProviderCode(String providerCode) {
		this.providerCode = providerCode;
	}
	public Long getRefNo() {
		return refNo;
	}
	public void setRefNo(Long refNo) {
		this.refNo = refNo;
	}
	public Date getPaymentReqDt() {
		return paymentReqDt;
	}
	public void setPaymentReqDt(Date paymentReqDt) {
		if(paymentReqDt !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(paymentReqDt);
			setPaymentReqDateValue(dateformat);
		    this.paymentReqDt = paymentReqDt;
		}
	}
	public String getPaymentReqDateValue() {
		return paymentReqDateValue;
	}
	public void setPaymentReqDateValue(String paymentReqDateValue) {
		this.paymentReqDateValue = paymentReqDateValue;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getEmailID() {
		return emailID;
	}
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	public String getZonalMailId() {
		return zonalMailId;
	}
	public void setZonalMailId(String zonalMailId) {
		this.zonalMailId = zonalMailId;
	}
	public String getProductNameValue() {
		return productNameValue;
	}
	public void setProductNameValue(String productNameValue) {
		this.productNameValue = productNameValue;
	}
	public String getStrChequeDate() {
		return strChequeDate;
	}
	public void setStrChequeDate(String strChequeDate) {
		this.strChequeDate = strChequeDate;
	}
	public String getRodNumber() {
		return rodNumber;
	}
	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
	}
	public Date getLastAckDate() {
		return lastAckDate;
	}
	public void setLastAckDate(Date lastAckDate) {
		if(lastAckDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(lastAckDate);
			setLastAckDateValue(dateformat);
		    this.lastAckDate = lastAckDate;
		}
	}
	public String getLastAckDateValue() {
		return lastAckDateValue;
	}
	public void setLastAckDateValue(String lastAckDateValue) {
		this.lastAckDateValue = lastAckDateValue;
	}
	public Date getNextDayOfFaApprovedDate() {
		return nextDayOfFaApprovedDate;
	}
	public void setNextDayOfFaApprovedDate(Date nextDayOfFaApprovedDate) {
		if(nextDayOfFaApprovedDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(nextDayOfFaApprovedDate);
			setNextDayOfFaApprovedDateValue(dateformat);
		    this.nextDayOfFaApprovedDate = nextDayOfFaApprovedDate;
		}
	}
	public String getNextDayOfFaApprovedDateValue() {
		return nextDayOfFaApprovedDateValue;
	}
	public void setNextDayOfFaApprovedDateValue(String nextDayOfFaApprovedDateValue) {
		this.nextDayOfFaApprovedDateValue = nextDayOfFaApprovedDateValue;
	}

	public Integer getNoOfExceedingDays() {
		return noOfExceedingDays;
	}
	public void setNoOfExceedingDays(Integer noOfExceedingDays) {
		this.noOfExceedingDays = noOfExceedingDays;
	}
	public Integer getExceedingIRDATatDays() {
		return exceedingIRDATatDays;
	}
	public void setExceedingIRDATatDays(Integer exceedingIRDATatDays) {
		this.exceedingIRDATatDays = exceedingIRDATatDays;
	}
	public Double getPenalInterestAmnt() {
		return penalInterestAmnt;
	}
	public void setPenalInterestAmnt(Double penalInterestAmnt) {
		this.penalInterestAmnt = penalInterestAmnt;
	}
	public Double getPenalTotalAmnt() {
		return penalTotalAmnt;
	}
	public void setPenalTotalAmnt(Double penalTotalAmnt) {
		this.penalTotalAmnt = penalTotalAmnt;
	}
	public String getPenalRemarks() {
		return penalRemarks;
	}
	public void setPenalRemarks(String penalRemarks) {
		this.penalRemarks = penalRemarks;
	}
	public Date getFaApprovedDate() {
		return faApprovedDate;
	}
	public void setFaApprovedDate(Date faApprovedDate) {
		this.faApprovedDate = faApprovedDate;
	}
	public Double getPenalInterestRate() {
		return penalInterestRate;
	}
	public void setPenalInterestRate(Double penalInterestRate) {
		this.penalInterestRate = penalInterestRate;
	}
	
	
}
