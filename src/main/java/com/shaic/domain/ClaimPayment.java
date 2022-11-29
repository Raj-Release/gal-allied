package com.shaic.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.preauth.Stage;

/**
 * Entity implementation class for Entity: ClaimPayment
 *
 */
@Entity
@Table(name="IMS_CLS_CLAIM_PAYMENT")
@NamedQueries({
	@NamedQuery(name ="ClaimPayment.findByKey",query="SELECT r FROM ClaimPayment r WHERE r.key = :primaryKey"),
	@NamedQuery(name ="ClaimPayment.findByKeyPayType",query="SELECT r FROM ClaimPayment r WHERE r.key = :primaryKey and r.paymentType = :paymode and (r.deletedFlag is null or r.deletedFlag = 'N')"),
	@NamedQuery(name ="ClaimPayment.findByintimationNumber",query="SELECT r FROM ClaimPayment r WHERE r.intimationNumber = :intimationNumber order by r.key desc"),
	@NamedQuery(name ="ClaimPayment.findByRodNo",query="SELECT r FROM ClaimPayment r WHERE r.rodNumber = :rodNumber and (r.deletedFlag is null or r.deletedFlag = 'N') order by r.rodNumber desc"),
	@NamedQuery(name ="ClaimPayment.findByRodNoWithSettled",query="SELECT r FROM ClaimPayment r WHERE r.rodNumber = :rodNumber and (r.deletedFlag is null or r.deletedFlag = 'N') and r.statusId.key = :statusId order by r.rodNumber desc"),
	@NamedQuery(name ="ClaimPayment.findLatestByRodNo",query="SELECT r FROM ClaimPayment r WHERE r.rodNumber = :rodNumber and (r.deletedFlag is null or r.deletedFlag = 'N') order by r.key desc"),
	@NamedQuery(name ="ClaimPayment.findByRodNoOrderByKey",query="SELECT r FROM ClaimPayment r WHERE r.rodNumber = :rodNumber and (r.deletedFlag is null or r.deletedFlag = 'N') order by r.key desc"),
	@NamedQuery(name ="ClaimPayment.findByClaimNumber",query="SELECT p FROM ClaimPayment p WHERE p.claimNumber like :claimNumber and (p.deletedFlag is null or p.deletedFlag = 'N') order by p.key desc"),
	@NamedQuery(name ="ClaimPayment.findByClaimKey",query="SELECT r FROM ClaimPayment r WHERE r.lotNumber  is not null and (r.deletedFlag is null or r.deletedFlag = 'N')"),
	@NamedQuery(name ="ClaimPayment.findByLotNumber",query="SELECT r FROM ClaimPayment r WHERE (r.lotNumber  is not null and Lower(r.lotNumber) like :lotNumber) and (r.deletedFlag is null or r.deletedFlag = 'N')"),
	@NamedQuery(name ="ClaimPayment.findByRodNoAndStatus",query="SELECT r FROM ClaimPayment r WHERE r.rodNumber = :rodNumber and r.statusId.key = :statusId and (r.deletedFlag is null or r.deletedFlag = 'N')  order by r.rodNumber desc"),
	@NamedQuery(name ="ClaimPayment.findByBatchNo",query="SELECT r FROM ClaimPayment r WHERE r.batchNumber = :batchNumber and (r.deletedFlag is null or r.deletedFlag = 'N') order by r.key asc"),
	@NamedQuery(name ="ClaimPayment.findSettledClaimByRodNo",query="SELECT r FROM ClaimPayment r WHERE r.rodNumber = :rodNumber and (r.deletedFlag is null or r.deletedFlag = 'N') order by r.key asc"),
	@NamedQuery(name ="ClaimPayment.findByClaimNumberAndStatus",query="SELECT p FROM ClaimPayment p WHERE p.claimNumber like :claimNumber and  p.statusId.key = :statusId and p.paymentType = :paymentType and (p.deletedFlag is null or p.deletedFlag = 'N') order by p.key desc"),
	@NamedQuery(name ="ClaimPayment.findByUniqueAndPremiaWS",query="SELECT r FROM ClaimPayment r WHERE r.installmentDeductionFlag is not null and r.installmentDeductionFlag = 'Y' and (r.premiaWSFlag is null or r.premiaWSFlag = 'N') and r.statusId.key = 158"),
	@NamedQuery(name ="ClaimPayment.findByRodKey",query="SELECT r FROM ClaimPayment r WHERE r.rodkey = :rodKey and (r.deletedFlag is null or r.deletedFlag = 'N')"),
	@NamedQuery(name ="ClaimPayment.findByRodKeyOrderByKey",query="SELECT r FROM ClaimPayment r WHERE r.rodkey = :rodKey and (r.deletedFlag is null or r.deletedFlag = 'N') order by r.key desc"),
	@NamedQuery(name ="ClaimPayment.findByintimationNumberForUPR",query="SELECT r FROM ClaimPayment r WHERE r.intimationNumber = :intimationNumber and r.statusId.key in (158,160,225,226) and (r.deletedFlag is null or r.deletedFlag = 'N') order by r.key desc"),
	@NamedQuery(name ="ClaimPayment.findByUprForIntimationUPR",query="SELECT r FROM ClaimPayment r WHERE r.chequeDDNumber = :uprId and r.statusId.key in (158,160,225,226) and (r.deletedFlag is null or r.deletedFlag = 'N') order by r.key desc"),
	@NamedQuery(name ="ClaimPayment.findByintimationNumberWithUPR",query="SELECT r FROM ClaimPayment r WHERE r.intimationNumber = :intimationNumber and r.chequeDDNumber = :uprId and r.statusId.key in (158,160,225,226) and (r.deletedFlag is null or r.deletedFlag = 'N') order by r.key desc")
})

public class ClaimPayment extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="IMS_CLAIM_PAYMENT_KEY_GENERATOR", sequenceName = "SEQ_CLAIM_PAYMENT_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLAIM_PAYMENT_KEY_GENERATOR" )
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
	
	@Column(name = "PAYMENT_CPU_CODE")
	private Long paymentCpuCode;
	
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

	@Column(name = "ACCOUNT_TYPE")
	private String accountType;
	
	@Column(name = "ACCOUNT_PREFERENCE")
	private String accountPreference;
	
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
	@Column(name = "PAYMENT_DATE")
	private Date paymentDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CHEQUE_DD_DATE")
	private Date chequeDDDate;	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BATCH_CREATED_DATE")
	private Date batchCreatedDate;
	
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
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="DELETED_FLAG")
	private String deletedFlag;

	@Column(name = "BEN_APPR_AMT")
	private Double benefitsApprovedAmt;
	
	@Column(name="DELAY_DAYS  ")
	private Long delayDays;
	
	@Column(name="INT_RATE  ")
	private Double interestRate;
	
	@Column(name="INT_AMT")
	private Double interestAmount;
	
	@Column(name="INT_REMARKS")
	private String intrestRemarks;
	
	@Column(name="TOTAL_AMOUNT")
	private Double totalAmount;
		
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_ACKNOW_DT")
	private Date lastAckDate;
	
	@Column(name="ALLOWED_DELAY_DAYS")
	//private Double allowedDelayDays;
	private Long allowedDelayDays;
	
	@Column(name = "RECONSIDER_FLAG")
	private String reconisderFlag;

	@Column(name = "PREMIUM_AMOUNT")
	private Double premiumAmt;
	
	@Column(name = "TOT_APPROVED_AMOUNT")
	private Double totalApprovedAmount;
	
	@Column(name = "INSTAL_DED_FLAG")
	private String installmentDeductionFlag;
	
	@Column(name = "INT_FLAG_PREMIUM")
	private String premiaWSFlag;
	
	@Column(name = "LUMPSUM_FLAG")
	private String lumpsumFlag;
	
	@Column(name="REMARKS")
	private String remarks;
	
	@Column(name="BATCH_PROCESS_FLAG")
	private String batchProcessFlag;
	
	@Column(name="REC_STATUS")
	private String recordStatusFlag;	
	
	@Column(name = "ZONE_MAIL_ID")
	private String zonalMailId;
	
	@Column(name = "INSURED_NAME")
	private String insuredName;
	
	@OneToOne
	@JoinColumn(name="HOSPITAL_KEY", nullable=true)
	private MasHospitals hospital;
	
	@Transient
	private Long hospitalKey;
	
	@Transient
	private String hospitalName;
	
	@Column(name = "PAYMODE_CHANGE_REASON")
	private String payModeChangeReason;	
	
	@Column(name = "EMPLOYEE_NAME ")
	private String gmcEmployeeName;	
	
	@Column(name = "PRODUCT_NAME ")
	private String productName;	
	
	@Column(name = "PROCESS_CLM_TYPE ")
	private String processClaimType;
	
	@Column(name = "CORPORATE_BUFFER_LIMIT")
	private Double corporateBufferLimit;
	
	@Column(name = "BEN_APR_AMT ")
	private Double paBenefitApproveAmount;
		
	@Column(name = "ADD_APPROVED_AMOUNT")
	private Double addOnCoversApprovedAmount;
	
	@Column(name = "OPT_APPROVED_AMOUNT")
	private Double optionalApprovedAmount;
	
	@Column(name="ROD_KEY")
	private Long rodkey;
	
	@Column(name = "SAVE_FLAG")
	private String saveFlag;
	
	@Column(name = "BENEFITS_ID")
	private Long benefitsId;
	
	@Column(name="UN_NAMED_KEY")
	private Long unNamedKey;
	
	@Column(name = "BANCS_PRIORITY_FLAG")
	private String bancsPriorityFlag;
	
	@Column(name = "PAYEE_RELATIONSHIP")
	private String payeeRelationship;
	
	@Column(name = "NOMINEE_NAME")
	private String nomineeName;
	
	@Column(name = "MICR_CODE")
	private String micrCode;
	
	@Column(name = "VIRTUAL_PAYMENT_ADDRESS")
	private String virtualPaymentAddr;
	
	@Column(name = "LEGAL_HEIR_RELATIONSHIP")
	private String legalHeirRelationship;
	
	@Column(name = "BANCS_UPR_ID")
	private String uprId;
	
/*	@Column(name = "ACCOUNT_TYPE")
	private String accType;*/
	
	@Column(name="NOMINEE_KEY")
	private Long NomineeKey;

	@Column(name="NOMINEE_RELATIONSHIP")
	private String nomineeRelationship;
	
	@Column(name="LEGAL_HEIR_KEY")
	private Long legalHeirKey;
	
	@Column(name="SOURCE_RISK_ID")
	private String sourceRiskId;
	
	@Column(name="RECORD_COUNT")
	private String selectCount;
	
	@Column(name = "VERIFIED_STATUS_FLAG")
	private String verifiedStatusFlag;
	
	@Column(name = "VERIFIED_USER_ID")
	private String verifiedUserId;
	
	@Column(name = "VERIFIED_STATUS")
	private String verifiedStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "VERIFIED_DATE")
	private Date verifiedDate;	
	
	@Column(name = "VNR_FLAG")
	private String vnrFlag;
	
	@Column(name="DEATH_DATE")
	private Date deathDate;
	
	@Column(name="CLAIM_LEGAL_COST")
	private Long legalCost;
	
	@Column(name="CLAIM_COMP_AMT")
	private Long compensationAmt;
	
	@Column(name="CLAIM_INT_RATE")
	private Double tdsPercentge;
	
	@Column(name="CLAIM_NO_OF_DAYS")
	private Long claimTotalnoofdays;
	
	@Column(name="CLAIM_TOT_INTEREST_AMT")
	private Long totalIntAmt;
	
	@Column(name="CLAIM_TDS_AMT")
	private Long tdsAmt;
	
	@Column(name="CLAIM_TOT_AMT")
	private Long claimTotAmt;
	
	@Column(name="ADDRESS_1")
	private String addressOne;
	
	@Column(name="PINCODE")
	private String pinCode;
	
	public String getBancsPriorityFlag() {
		return bancsPriorityFlag;
	}

	public void setBancsPriorityFlag(String bancsPriorityFlag) {
		this.bancsPriorityFlag = bancsPriorityFlag;
	}

	public String getPayeeRelationship() {
		return payeeRelationship;
	}

	public void setPayeeRelationship(String payeeRelationship) {
		this.payeeRelationship = payeeRelationship;
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
	
	public String getRecordStatusFlag() {
		return recordStatusFlag;
	}

	public void setRecordStatusFlag(String recordStatusFlag) {
		this.recordStatusFlag = recordStatusFlag;
	}

	public String getBatchProcessFlag() {
		return batchProcessFlag;
	}

	public void setBatchProcessFlag(String batchProcessFlag) {
		this.batchProcessFlag = batchProcessFlag;
	}


	public String getLumpsumFlag() {
		return lumpsumFlag;
	}

	public void setLumpsumFlag(String lumpsumFlag) {
		this.lumpsumFlag = lumpsumFlag;
	}

	public Date getLastAckDate() {
		return lastAckDate;
	}


	public void setLastAckDate(Date lastAckDate) {
		this.lastAckDate = lastAckDate;
	}


	public ClaimPayment() {
		super();
	}


	public Date getBatchCreatedDate() {
		return batchCreatedDate;
	}

	public void setBatchCreatedDate(Date batchCreatedDate) {
		this.batchCreatedDate = batchCreatedDate;
	}

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

	public Double getBenefitsApprovedAmt() {
		return benefitsApprovedAmt;
	}

	public void setBenefitsApprovedAmt(Double benefitsApprovedAmt) {
		this.benefitsApprovedAmt = benefitsApprovedAmt;
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

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
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

	public String getIntrestRemarks() {
		return intrestRemarks;
	}

	public void setIntrestRemarks(String intrestRemarks) {
		this.intrestRemarks = intrestRemarks;
	}
   
	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}


	public Double getTotalAmount() {
		return totalAmount;
	}


	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}


	public Long getDelayDays() {
		return delayDays;
	}


	public void setDelayDays(Long delayDays) {
		this.delayDays = delayDays;
	}


	public Long getAllowedDelayDays() {
		return allowedDelayDays;
	}


	public void setAllowedDelayDays(Long allowedDelayDays) {
		this.allowedDelayDays = allowedDelayDays;
	}


	public String getReconisderFlag() {
		return reconisderFlag;
	}


	public void setReconisderFlag(String reconisderFlag) {
		this.reconisderFlag = reconisderFlag;
	}

	public Double getPremiumAmt() {
		return premiumAmt;
	}

	public void setPremiumAmt(Double premiumAmt) {
		this.premiumAmt = premiumAmt;
	}

	public Double getTotalApprovedAmount() {
		return totalApprovedAmount;
	}

	public void setTotalApprovedAmount(Double totalApprovedAmount) {
		this.totalApprovedAmount = totalApprovedAmount;
	}

	
	public String getPremiaWSFlag() {
		return premiaWSFlag;
	}

	public void setPremiaWSFlag(String premiaWSFlag) {
		this.premiaWSFlag = premiaWSFlag;
	}

	public String getInstallmentDeductionFlag() {
		return installmentDeductionFlag;
	}

	public void setInstallmentDeductionFlag(String installmentDeductionFlag) {
		this.installmentDeductionFlag = installmentDeductionFlag;
	}
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getZonalMailId() {
		return zonalMailId;
	}

	public void setZonalMailId(String zonalMailId) {
		this.zonalMailId = zonalMailId;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getKey() == null) {
            return obj == this;
        } else {
            return getKey().equals(((ClaimPayment) obj).getKey());
        }
    }

    @Override
    public int hashCode() {
        if (key != null) {
            return key.hashCode();
        } else {
            return super.hashCode();
        }
    }

	public MasHospitals getHospital() {
		return hospital;
	}

	public void setHospital(MasHospitals hospital) {
		this.hospital = hospital;
		this.hospitalName = this.hospital != null ? this.hospital.getName() : "";
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName =  this.hospital != null ? this.hospital.getName() : "";
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getPayModeChangeReason() {
		return payModeChangeReason;
	}

	public void setPayModeChangeReason(String payModeChangeReason) {
		this.payModeChangeReason = payModeChangeReason;
	}

	public String getGmcEmployeeName() {
		return gmcEmployeeName;
	}

	public void setGmcEmployeeName(String gmcEmployeeName) {
		this.gmcEmployeeName = gmcEmployeeName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProcessClaimType() {
		return processClaimType;
	}

	public void setProcessClaimType(String processClaimType) {
		this.processClaimType = processClaimType;
	}

	public Double getCorporateBufferLimit() {
		return corporateBufferLimit;
	}

	public void setCorporateBufferLimit(Double corporateBufferLimit) {
		this.corporateBufferLimit = corporateBufferLimit;
	}

	public Double getPaBenefitApproveAmount() {
		return paBenefitApproveAmount;
	}

	public void setPaBenefitApproveAmount(Double paBenefitApproveAmount) {
		this.paBenefitApproveAmount = paBenefitApproveAmount;
	}
		
	/*public Reimbursement getRodkey() {
		return rodkey;
	}

	public void setRodkey(Reimbursement rodkey) {
		this.rodkey = rodkey;
	}*/

	public Long getPaymentCpuCode() {
		return paymentCpuCode;
	}

	public void setPaymentCpuCode(Long paymentCpuCode) {
		this.paymentCpuCode = paymentCpuCode;
	}

	public Double getAddOnCoversApprovedAmount() {
		return addOnCoversApprovedAmount;
	}

	public void setAddOnCoversApprovedAmount(Double addOnCoversApprovedAmount) {
		this.addOnCoversApprovedAmount = addOnCoversApprovedAmount;
	}

	public Double getOptionalApprovedAmount() {
		return optionalApprovedAmount;
	}

	public void setOptionalApprovedAmount(Double optionalApprovedAmount) {
		this.optionalApprovedAmount = optionalApprovedAmount;
	}

	public String getSaveFlag() {
		return saveFlag;
	}

	public void setSaveFlag(String saveFlag) {
		this.saveFlag = saveFlag;
	}

	public Long getRodkey() {
		return rodkey;
	}

	public void setRodkey(Long rodkey) {
		this.rodkey = rodkey;
	}

	public Long getBenefitsId() {
		return benefitsId;
	}

	public void setBenefitsId(Long benefitsId) {
		this.benefitsId = benefitsId;
	}

	public Long getHospitalKey() {
		return hospitalKey;
	}

	public void setHospitalKey(Long hospitalKey) {
		this.hospitalKey = hospitalKey;
	}

	public Long getUnNamedKey() {
		return unNamedKey;
	}

	public void setUnNamedKey(Long unNamedKey) {
		this.unNamedKey = unNamedKey;
	}

	public Long getNomineeKey() {
		return NomineeKey;
	}

	public void setNomineeKey(Long nomineeKey) {
		NomineeKey = nomineeKey;
	}	

	public String getNomineeName() {
		return nomineeName;
	}

	public void setNomineeName(String nomineeName) {
		this.nomineeName = nomineeName;
	}

	public Long getLegalHeirKey() {
		return legalHeirKey;
	}

	public void setLegalHeirKey(Long legalHeirKey) {
		this.legalHeirKey = legalHeirKey;
	}

	public String getNomineeRelationship() {
		return nomineeRelationship;
	}

	public void setNomineeRelationship(String nomineeRelationship) {
		this.nomineeRelationship = nomineeRelationship;
	}

	public String getLegalHeirRelationship() {
		return legalHeirRelationship;
	}

	public void setLegalHeirRelationship(String legalHeirRelationship) {
		this.legalHeirRelationship = legalHeirRelationship;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getSourceRiskId() {
		return sourceRiskId;
	}

	public void setSourceRiskId(String sourceRiskId) {
		this.sourceRiskId = sourceRiskId;
	}

	public String getAccountPreference() {
		return accountPreference;
	}

	public void setAccountPreference(String accountPreference) {
		this.accountPreference = accountPreference;
	}

	public String getSelectCount() {
		return selectCount;
	}

	public void setSelectCount(String selectCount) {
		this.selectCount = selectCount;
	}
	
	public String getVerifiedStatusFlag() {
		return verifiedStatusFlag;
	}

	public void setVerifiedStatusFlag(String verifiedStatusFlag) {
		this.verifiedStatusFlag = verifiedStatusFlag;
	}

	public String getVerifiedUserId() {
		return verifiedUserId;
	}

	public void setVerifiedUserId(String verifiedUserId) {
		this.verifiedUserId = verifiedUserId;
	}

	public String getVerifiedStatus() {
		return verifiedStatus;
	}

	public void setVerifiedStatus(String verifiedStatus) {
		this.verifiedStatus = verifiedStatus;
	}

	public Date getVerifiedDate() {
		return verifiedDate;
	}

	public void setVerifiedDate(Date verifiedDate) {
		this.verifiedDate = verifiedDate;
	}

	public String getVnrFlag() {
		return vnrFlag;
	}

	public void setVnrFlag(String vnrFlag) {
		this.vnrFlag = vnrFlag;
	}

	public Date getDeathDate() {
		return deathDate;
	}

	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}

	public String getUprId() {
		return uprId;
	}

	public void setUprId(String uprId) {
		this.uprId = uprId;
	}		

	public Long getLegalCost() {
		return legalCost;
	}

	public void setLegalCost(Long legalCost) {
		this.legalCost = legalCost;
	}

	public Long getCompensationAmt() {
		return compensationAmt;
	}

	public void setCompensationAmt(Long compensationAmt) {
		this.compensationAmt = compensationAmt;
	}

	public Double getTdsPercentge() {
		return tdsPercentge;
	}

	public void setTdsPercentge(Double tdsPercentge) {
		this.tdsPercentge = tdsPercentge;
	}

	public Long getClaimTotalnoofdays() {
		return claimTotalnoofdays;
	}

	public void setClaimTotalnoofdays(Long claimTotalnoofdays) {
		this.claimTotalnoofdays = claimTotalnoofdays;
	}

	public Long getTotalIntAmt() {
		return totalIntAmt;
	}

	public void setTotalIntAmt(Long totalIntAmt) {
		this.totalIntAmt = totalIntAmt;
	}

	public Long getTdsAmt() {
		return tdsAmt;
	}

	public void setTdsAmt(Long tdsAmt) {
		this.tdsAmt = tdsAmt;
	}

	public Long getClaimTotAmt() {
		return claimTotAmt;
	}

	public void setClaimTotAmt(Long claimTotAmt) {
		this.claimTotAmt = claimTotAmt;
	}

	public String getAddressOne() {
		return addressOne;
	}

	public void setAddressOne(String addressOne) {
		this.addressOne = addressOne;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	
	
}
