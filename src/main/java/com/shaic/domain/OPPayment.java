//package com.shaic.domain;
//
//import java.io.Serializable;
//import java.util.Date;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.OneToOne;
//import javax.persistence.SequenceGenerator;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//
//import com.shaic.arch.fields.dto.AbstractEntity;
//import com.shaic.domain.preauth.Stage;
//
///**
// * Entity implementation class for Entity: OPPayment
// *
// */
//@Entity
//@Table(name="IMS_CLS_OP_PAYMENT")
//
//public class OPPayment extends AbstractEntity implements Serializable{
//	
//private static final long serialVersionUID = 1L;
//	
//	@Id
//	@SequenceGenerator(name="IMS_CLAIM_OPPAYMENT_GENARATOR", sequenceName = "SEQ_CLAIM_PAYMENT_KEY", allocationSize = 1)
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLAIM_OPPAYMENT_GENARATOR" )
//	@Column(name = "OP_PAYMENT_KEY")
//	private Long key;
//	
//	@Column(name = "LOT_NUMBER")
//	private String lotNumber;
//	
//	@Column(name = "ROD_NUMBER")
//	private String rodNumber;
//	
//	@Column(name = "INTIMATION_NUMBER")
//	private String intimationNumber;
//	
//	@Column(name = "CLAIM_NUMBER")
//	private String claimNumber;
//	
//	@Column(name = "RISK_ID")
//	private Long riskId;
//	
//	@Column(name = "POLICY_NUMBER")
//	private String policyNumber;
//	
//	@Column(name = "CLAIM_TYPE")
//	private String claimType;
//	
//	@Column(name = "LEGAL_HEIR_NAME")
//	private String legalHairName;
//	
//	@Column(name = "PAYMENT_CPU_CODE")
//	private Long paymentCpuCode;	
//	
//	@Column(name = "CPU_ZONE")
//	private String cpuZone;
//	
//	@Column(name = "PRODUCT_CODE")
//	private String productCode;
//	
//	@Column(name = "PAYMENT_TYPE")
//	private String paymentType;
//	
//	@Column(name = "APPROVED_AMOUNT")
//	private Double approvedAmount;
//	
//	@Column(name = "CPU_CODE")
//	private Long cpuCode;
//	
//	@Column(name = "IFSC_CODE")
//	private String ifscCode;
//	
//	@Column(name = "ACCOUNT_NUMBER")
//	private String accountNumber;
//	
//	@Column(name = "BRANCH_NAME")
//	private String branchName;
//	
//	@Column(name = "PAYEE_NAME")
//	private String payeeName;
//	
//	@Column(name = "PAYABLE_AT")
//	private String payableAt;
//	
//	@Column(name = "PAN_NUMBER")
//	private String panNumber;
//	
//	@Column(name = "HOSPITAL_CODE")
//	private String hospitalCode;
//	
//	@Column(name = "EMAIL_ID")
//	private String emailId;
//	
//	@Column(name = "BANK_CODE")
//	private String bankCode;
//	
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "CHEQUE_DD_DATE")
//	private Date chequeDDDate;
//	
//	@Column(name = "CHEQUE_DD_NUMBER")
//	private String chequeDDNumber;
//	
//	@Column(name = "BANK_NAME")
//	private String bankName;
//	
//	@Column(name = "NET_AMOUNT")
//	private Double netAmount;
//	
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "FA_APPROVAL_DATE")
//	private Date faApprovalDate;
//	
//	@Column(name = "REASON_FOR_CHANGE")
//	private String reasonForChange;
//	
//	@OneToOne
//	@JoinColumn(name="STAGE_ID",nullable=false)
//	private Stage stageId;
//	
//	@OneToOne
//	@JoinColumn(name="STATUS_ID",nullable=false)
//	private Status statusId;
//	
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "CREATED_DATE")
//	private Date createdDate;
//	
//	@Column(name = "CREATED_BY")
//	private String createdBy;
//	
//	@Column(name = "MODIFIED_BY")
//	private String modifiedBy;
//	
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "MODIFIED_DATE")
//	private Date modifiedDate;
//
//	public Long getKey() {
//		return key;
//	}
//
//	public void setKey(Long key) {
//		this.key = key;
//	}
//
//	public String getLotNumber() {
//		return lotNumber;
//	}
//
//	public void setLotNumber(String lotNumber) {
//		this.lotNumber = lotNumber;
//	}
//
//	public String getRodNumber() {
//		return rodNumber;
//	}
//
//	public void setRodNumber(String rodNumber) {
//		this.rodNumber = rodNumber;
//	}
//
//	public String getIntimationNumber() {
//		return intimationNumber;
//	}
//
//	public void setIntimationNumber(String intimationNumber) {
//		this.intimationNumber = intimationNumber;
//	}
//
//	public String getClaimNumber() {
//		return claimNumber;
//	}
//
//	public void setClaimNumber(String claimNumber) {
//		this.claimNumber = claimNumber;
//	}
//
//	public Long getRiskId() {
//		return riskId;
//	}
//
//	public void setRiskId(Long riskId) {
//		this.riskId = riskId;
//	}
//
//	public String getPolicyNumber() {
//		return policyNumber;
//	}
//
//	public void setPolicyNumber(String policyNumber) {
//		this.policyNumber = policyNumber;
//	}
//
//	public String getClaimType() {
//		return claimType;
//	}
//
//	public void setClaimType(String claimType) {
//		this.claimType = claimType;
//	}
//
//	public String getLegalHairName() {
//		return legalHairName;
//	}
//
//	public void setLegalHairName(String legalHairName) {
//		this.legalHairName = legalHairName;
//	}
//
//	public Long getPaymentCpuCode() {
//		return paymentCpuCode;
//	}
//
//	public void setPaymentCpuCode(Long paymentCpuCode) {
//		this.paymentCpuCode = paymentCpuCode;
//	}
//
//	public String getCpuZone() {
//		return cpuZone;
//	}
//
//	public void setCpuZone(String cpuZone) {
//		this.cpuZone = cpuZone;
//	}
//
//	public String getProductCode() {
//		return productCode;
//	}
//
//	public void setProductCode(String productCode) {
//		this.productCode = productCode;
//	}
//
//	public String getPaymentType() {
//		return paymentType;
//	}
//
//	public void setPaymentType(String paymentType) {
//		this.paymentType = paymentType;
//	}
//
//	public Double getApprovedAmount() {
//		return approvedAmount;
//	}
//
//	public void setApprovedAmount(Double approvedAmount) {
//		this.approvedAmount = approvedAmount;
//	}
//
//	public Long getCpuCode() {
//		return cpuCode;
//	}
//
//	public void setCpuCode(Long cpuCode) {
//		this.cpuCode = cpuCode;
//	}
//
//	public String getIfscCode() {
//		return ifscCode;
//	}
//
//	public void setIfscCode(String ifscCode) {
//		this.ifscCode = ifscCode;
//	}
//
//	public String getAccountNumber() {
//		return accountNumber;
//	}
//
//	public void setAccountNumber(String accountNumber) {
//		this.accountNumber = accountNumber;
//	}
//
//	public String getBranchName() {
//		return branchName;
//	}
//
//	public void setBranchName(String branchName) {
//		this.branchName = branchName;
//	}
//
//	public String getPayeeName() {
//		return payeeName;
//	}
//
//	public void setPayeeName(String payeeName) {
//		this.payeeName = payeeName;
//	}
//
//	public String getPayableAt() {
//		return payableAt;
//	}
//
//	public void setPayableAt(String payableAt) {
//		this.payableAt = payableAt;
//	}
//
//	public String getPanNumber() {
//		return panNumber;
//	}
//
//	public void setPanNumber(String panNumber) {
//		this.panNumber = panNumber;
//	}
//
//	public String getHospitalCode() {
//		return hospitalCode;
//	}
//
//	public void setHospitalCode(String hospitalCode) {
//		this.hospitalCode = hospitalCode;
//	}
//
//	public String getEmailId() {
//		return emailId;
//	}
//
//	public void setEmailId(String emailId) {
//		this.emailId = emailId;
//	}
//
//	public String getBankCode() {
//		return bankCode;
//	}
//
//	public void setBankCode(String bankCode) {
//		this.bankCode = bankCode;
//	}
//
//	public Date getChequeDDDate() {
//		return chequeDDDate;
//	}
//
//	public void setChequeDDDate(Date chequeDDDate) {
//		this.chequeDDDate = chequeDDDate;
//	}
//
//	public String getChequeDDNumber() {
//		return chequeDDNumber;
//	}
//
//	public void setChequeDDNumber(String chequeDDNumber) {
//		this.chequeDDNumber = chequeDDNumber;
//	}
//
//	public String getBankName() {
//		return bankName;
//	}
//
//	public void setBankName(String bankName) {
//		this.bankName = bankName;
//	}
//
//	public Double getNetAmount() {
//		return netAmount;
//	}
//
//	public void setNetAmount(Double netAmount) {
//		this.netAmount = netAmount;
//	}
//
//	public Date getFaApprovalDate() {
//		return faApprovalDate;
//	}
//
//	public void setFaApprovalDate(Date faApprovalDate) {
//		this.faApprovalDate = faApprovalDate;
//	}
//
//	public String getReasonForChange() {
//		return reasonForChange;
//	}
//
//	public void setReasonForChange(String reasonForChange) {
//		this.reasonForChange = reasonForChange;
//	}
//
//	public Stage getStageId() {
//		return stageId;
//	}
//
//	public void setStageId(Stage stageId) {
//		this.stageId = stageId;
//	}
//
//	public Status getStatusId() {
//		return statusId;
//	}
//
//	public void setStatusId(Status statusId) {
//		this.statusId = statusId;
//	}
//
//	public Date getCreatedDate() {
//		return createdDate;
//	}
//
//	public void setCreatedDate(Date createdDate) {
//		this.createdDate = createdDate;
//	}
//
//	public String getCreatedBy() {
//		return createdBy;
//	}
//
//	public void setCreatedBy(String createdBy) {
//		this.createdBy = createdBy;
//	}
//
//	public String getModifiedBy() {
//		return modifiedBy;
//	}
//
//	public void setModifiedBy(String modifiedBy) {
//		this.modifiedBy = modifiedBy;
//	}
//
//	public Date getModifiedDate() {
//		return modifiedDate;
//	}
//
//	public void setModifiedDate(Date modifiedDate) {
//		this.modifiedDate = modifiedDate;
//	}
//
//	public static long getSerialversionuid() {
//		return serialVersionUID;
//	}
//	
//	
//	
//	
//	
//	
//	
//	
//
//}
