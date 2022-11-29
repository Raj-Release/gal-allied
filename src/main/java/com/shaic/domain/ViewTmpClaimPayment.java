package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.preauth.Stage;



@Entity
@Table(name="VW_CLAIM_PAYMENT_TMP")
@NamedQueries({
	@NamedQuery(name ="ViewTmpClaimPayment.findByKey",query="SELECT r FROM ViewTmpClaimPayment r WHERE r.key = :primaryKey"),
	@NamedQuery(name ="ViewTmpClaimPayment.findByRodNo",query="SELECT r FROM ViewTmpClaimPayment r WHERE r.rodNumber = :rodNumber order by r.rodNumber desc"),
	@NamedQuery(name ="ViewTmpClaimPayment.findByClaimNumber",query="SELECT p FROM ViewTmpClaimPayment p WHERE p.claimNumber like :claimNumber order by p.key desc"),
	@NamedQuery(name ="ViewTmpClaimPayment.findByClaimKey",query="SELECT r FROM ViewTmpClaimPayment r WHERE r.lotNumber is not null")
})

public class ViewTmpClaimPayment extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "PAYMENT_KEY")
	private Long key;
	
	@Column(name = "BATCH_NUMBER")
	private String batchNumber;
	
	@Column(name = "LOT_NUMBER")
	private String lotNumber;
	
	@Column(name = "ROD_NUMBER")
	private String rodNumber;
	
	@Column(name = "PIO_CODE")
	private String pioCode;
	
	/*@Column(name = "PAYMENT_CPU_CODE")
	private Long paymentCpuCode;*/
	
	@Column(name = "PRODUCT_CODE")
	private String productCode;
	
	@Column(name = "CLAIM_TYPE")
	private String claimType;
	
	@Column(name = "INTIMATION_NUMBER")
	private String intimationNumber;
	
	@Column(name = "CLAIM_NUMBER")
	private String claimNumber;
	
	@Column(name = "POLICY_NUMBER")
	private String policyNumber;
	
	@Column(name = "POLICY_SYS_ID")
	private Long policySysId;
	
	@Column(name = "RISK_ID")
	private Long riskId;
	
	@Column(name = "PAYMENT_TYPE")
	private String paymentType;
	
	@Column(name = "APPROVED_AMOUNT")
	private Double approvedAmount;	
	
	/*@OneToOne
	@JoinColumn(name="CPU_CODE", nullable=true)*/
	@Column(name = "CPU_CODE")
	private Long cpuCode;
	//private TmpCPUCode cpuCode;
	
	@Column(name = "IFSC_CODE")
	private String ifscCode;
	
	@Column(name = "LETTER_FLAG")
	private String letterPrintingMode;
	
	@Column(name = "ACCOUNT_NUMBER")
	private String accountNumber;
	
	@Column(name = "BRANCH_NAME")
	private String branchName;
	
	@Column(name = "PAYEE_NAME")
	private String payeeName;
	
	@Column(name = "PAYABLE_AT")
	private String payableAt;
	
	@Column(name = "PAN_NUMBER")
	private String panNumber;
	
	@Column(name = "HOSPITAL_CODE")
	private String hospitalCode;
	
	@Column(name = "EMAIL_ID")
	private String emailId;
	
	@Column(name = "PROPOSER_CODE")
	private String proposerCode;
	
	@Column(name = "PROPOSER_NAME")
	private String proposerName;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name= "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "NOTIFICATION_FLAG")
	private String notificationFlag;
	@OneToOne
	@JoinColumn(name="PAYMENT_STATUS_ID",nullable=false)
	private MastersValue paymentStatus;
	
	@OneToOne
	@JoinColumn(name="LOT_STATUS",nullable=true)
	private Status lotStatus;
	
	@OneToOne
	@JoinColumn(name="BATCH_STATUS",nullable=true)
	private Status batchStatus;
	
	@Column(name = "BANK_CODE")
	private String bankCode;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CHEQUE_DD_DATE")
	private Date chequeDDDate;
	
	@Column(name = "CHEQUE_DD_NUMBER")
	private String chequeDDNumber;
	
	@Column(name = "BANK_NAME")
	private String bankName;
	
	@Column(name = "TDS_AMOUNT")
	private Double tdsAmount;
	
	@Column(name = "TDS_PERCENTAGE")
	private Long tdsPercentage;
	
	@Column(name = "NET_AMOUNT")
	private Double netAmount;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FA_APPROVAL_DATE")
	private Date faApprovalDate;
	
	/*@Column(name = "CANCEL_CHEQUE_NUMBER")
	private String cancelChequeNumber;
	
	@Column(name = "CANCEL_TYPE")
	private String cancelType;*/
	
	@Column(name = "REISSUE_REJECTION_FLAG")
	private String reissueRejectionFlag;
	
	/*@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CANCEL_DATE")
	private Date cancelDate;
	
	@Column(name = "CANCEL_USER_ID")
	private String cancelUserId;*/
	
	@Column(name = "BATCH_HOLD_FLAG")
	private String batchHoldFlag;
	
	
	@OneToOne
	@JoinColumn(name="STAGE_ID",nullable=false)
	private Stage stageId;
	
	@OneToOne
	@JoinColumn(name="STATUS_ID",nullable=false)
	private Status statusId;
	
	
	@Column(name = "DOCUMENT_RECEIVED_FROM")
	private String documentReceivedFrom;
	
	
	@Column(name = "REASON_FOR_CHANGE")
	private String reasonForChange;
	
	@Column(name = "LEGAL_HEIR_NAME")
	private String legalHeirName;
	
	@Column(name = "RECORD_FLAG")
	private String recordFlag;
	

//	public ClaimPayment() {
//		super();
//	}







	public Long getKey() {
		return key;
	}







	public void setKey(Long key) {
		this.key = key;
	}







	public String getBatchNumber() {
		return batchNumber;
	}







	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}







	public String getLotNumber() {
		return lotNumber;
	}







	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}







	public String getRodNumber() {
		return rodNumber;
	}







	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
	}







	public String getPioCode() {
		return pioCode;
	}







	public void setPioCode(String pioCode) {
		this.pioCode = pioCode;
	}







	/*public Long getPaymentCpuCode() {
		return paymentCpuCode;
	}







	public void setPaymentCpuCode(Long paymentCpuCode) {
		this.paymentCpuCode = paymentCpuCode;
	}
*/






	public String getProductCode() {
		return productCode;
	}







	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}







	public String getClaimType() {
		return claimType;
	}







	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}







	public String getIntimationNumber() {
		return intimationNumber;
	}







	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}







	public String getClaimNumber() {
		return claimNumber;
	}







	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}







	public String getPolicyNumber() {
		return policyNumber;
	}







	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}







	public Long getPolicySysId() {
		return policySysId;
	}







	public void setPolicySysId(Long policySysId) {
		this.policySysId = policySysId;
	}







	public Long getRiskId() {
		return riskId;
	}







	public void setRiskId(Long riskId) {
		this.riskId = riskId;
	}







	public String getPaymentType() {
		return paymentType;
	}







	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}







	public Double getApprovedAmount() {
		return approvedAmount;
	}







	public void setApprovedAmount(Double approvedAmount) {
		this.approvedAmount = approvedAmount;
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







	public String getBranchName() {
		return branchName;
	}







	public void setBranchName(String branchName) {
		this.branchName = branchName;
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







	public String getPanNumber() {
		return panNumber;
	}







	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}







	public String getHospitalCode() {
		return hospitalCode;
	}







	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}







	public String getEmailId() {
		return emailId;
	}







	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}







	public String getProposerCode() {
		return proposerCode;
	}







	public void setProposerCode(String proposerCode) {
		this.proposerCode = proposerCode;
	}







	public String getProposerName() {
		return proposerName;
	}







	public void setProposerName(String proposerName) {
		this.proposerName = proposerName;
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







	public String getNotificationFlag() {
		return notificationFlag;
	}







	public void setNotificationFlag(String notificationFlag) {
		this.notificationFlag = notificationFlag;
	}











	











	public String getBankCode() {
		return bankCode;
	}







	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}







	public Date getChequeDDDate() {
		return chequeDDDate;
	}







	public void setChequeDDDate(Date chequeDDDate) {
		this.chequeDDDate = chequeDDDate;
	}







	public String getChequeDDNumber() {
		return chequeDDNumber;
	}







	public void setChequeDDNumber(String chequeDDNumber) {
		this.chequeDDNumber = chequeDDNumber;
	}







	public String getBankName() {
		return bankName;
	}







	public void setBankName(String bankName) {
		this.bankName = bankName;
	}







	public Double getTdsAmount() {
		return tdsAmount;
	}







	public void setTdsAmount(Double tdsAmount) {
		this.tdsAmount = tdsAmount;
	}







	public Long getTdsPercentage() {
		return tdsPercentage;
	}







	public void setTdsPercentage(Long tdsPercentage) {
		this.tdsPercentage = tdsPercentage;
	}







	public Double getNetAmount() {
		return netAmount;
	}







	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}







	public Date getFaApprovalDate() {
		return faApprovalDate;
	}







	public void setFaApprovalDate(Date faApprovalDate) {
		this.faApprovalDate = faApprovalDate;
	}







	/*public String getCancelChequeNumber() {
		return cancelChequeNumber;
	}







	public void setCancelChequeNumber(String cancelChequeNumber) {
		this.cancelChequeNumber = cancelChequeNumber;
	}







	public String getCancelType() {
		return cancelType;
	}







	public void setCancelType(String cancelType) {
		this.cancelType = cancelType;
	}

*/





	public String getReissueRejectionFlag() {
		return reissueRejectionFlag;
	}







	public void setReissueRejectionFlag(String reissueRejectionFlag) {
		this.reissueRejectionFlag = reissueRejectionFlag;
	}







	/*public Date getCancelDate() {
		return cancelDate;
	}







	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}







	public String getCancelUserId() {
		return cancelUserId;
	}
*/






	/*public void setCancelUserId(String cancelUserId) {
		this.cancelUserId = cancelUserId;
	}

*/





	public static long getSerialversionuid() {
		return serialVersionUID;
	}







	public Status getLotStatus() {
		return lotStatus;
	}







	public void setLotStatus(Status lotStatus) {
		this.lotStatus = lotStatus;
	}













	public Status getBatchStatus() {
		return batchStatus;
	}







	public void setBatchStatus(Status batchStatus) {
		this.batchStatus = batchStatus;
	}







	public MastersValue getPaymentStatus() {
		return paymentStatus;
	}







	public void setPaymentStatus(MastersValue paymentStatus) {
		this.paymentStatus = paymentStatus;
	}







	public String getBatchHoldFlag() {
		return batchHoldFlag;
	}







	public void setBatchHoldFlag(String batchHoldFlag) {
		this.batchHoldFlag = batchHoldFlag;
	}







	public Stage getStageId() {
		return stageId;
	}







	public void setStageId(Stage stageId) {
		this.stageId = stageId;
	}







	public Status getStatusId() {
		return statusId;
	}







	public void setStatusId(Status statusId) {
		this.statusId = statusId;
	}







	public Long getCpuCode() {
		return cpuCode;
	}







	public void setCpuCode(Long cpuCode) {
		this.cpuCode = cpuCode;
	}







	






	public String getLetterPrintingMode() {
		return letterPrintingMode;
	}







	public void setLetterPrintingMode(String letterPrintingMode) {
		this.letterPrintingMode = letterPrintingMode;
	}







	public String getDocumentReceivedFrom() {
		return documentReceivedFrom;
	}







	public void setDocumentReceivedFrom(String documentReceivedFrom) {
		this.documentReceivedFrom = documentReceivedFrom;
	}







	public String getReasonForChange() {
		return reasonForChange;
	}

	public void setReasonForChange(String reasonForChange) {
		this.reasonForChange = reasonForChange;
	}


	public String getLegalHeirName() {
		return legalHeirName;
	}


	public void setLegalHeirName(String legalHeirName) {
		this.legalHeirName = legalHeirName;
	}







	public String getRecordFlag() {
		return recordFlag;
	}







	public void setRecordFlag(String recordFlag) {
		this.recordFlag = recordFlag;
	}
   
}
