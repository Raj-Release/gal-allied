package com.shaic.claim;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Stage;


/**
 * 
 * The persistent class for the IMS_CLS_OMP_CLAIM_PAYMENT table.
 * 
 */

@Entity
@Table(name="IMS_CLS_OMP_CLAIM_PAYMENT")
@NamedQueries({
	@NamedQuery(name ="OMPClaimPayment.findByKey",query="SELECT r FROM OMPClaimPayment r WHERE r.paymentKey = :primaryKey"),
	@NamedQuery(name="OMPClaimPayment.findByOMPIntimationNumber", query="SELECT r FROM OMPClaimPayment r WHERE r.intimationNumber = :intimationNumber"),
	@NamedQuery(name ="OMPClaimPayment.findByClaimNumber",query="SELECT r FROM OMPClaimPayment r WHERE r.claimNumber like :claimNumber order by r.paymentKey desc"),
	@NamedQuery(name ="OMPClaimPayment.findByRodNo",query="SELECT r FROM OMPClaimPayment r WHERE r.rodNumber = :rodNumber order by r.rodNumber desc"),
	@NamedQuery(name ="OMPClaimPayment.findByOMPPaymentByStatus",query="SELECT r FROM OMPClaimPayment r WHERE r.statusId.key = :status and r.paymentStatusId.key = :paymentStatusId and (r.letterFlag is null or r.letterFlag <> :letterFlag)")

})
public class OMPClaimPayment extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = -4069578069368534817L;

	@Id
	@SequenceGenerator(name="IMS_OMP_CLAIM_PAYMENT_KEY_GENERATOR", sequenceName = "SEQ_OMP_PAYMENT_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_OMP_CLAIM_PAYMENT_KEY_GENERATOR" )
	@Column(name = "PAYMENT_KEY")
	private Long paymentKey;
	
	
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
	
	@Column(name = "CLAIM_TYPE ")
	private String ClaimType;   
	
	@Column(name = "INTIMATION_NUMBER")
	private String intimationNumber; 
	
	@Column(name = "CLAIM_NUMBER")
	private String claimNumber;  
	
	@Column(name = "POLICY_NUMBER")
	private String policyNo;   
	
	@Column(name = "POLICY_SYS_ID")
	private Long policySysId;  
	
	@Column(name = "RISK_ID")
	private Long riskId;  
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PAYMENT_TYPE")
	private MastersValue paymentType;  
	
	@Column(name = "APPROVED_AMOUNT")
	private Double approvedAmt; 
	
	@Column(name = "CPU_CODE")
	private Long cpuCode;     
	
	@Column(name = "IFSC_CODE")
	private String ifscCode;  
	
	@Column(name = "ACCOUNT_NUMBER")
	private String accountNo;   
	
	@Column(name = "BRANCH_NAME")
	private String branchName;  

	@Column(name = "PAYEE_NAME")
	private String payeeName;  
	
	@Column(name = "PAYABLE_AT")
	private String payabelAt;  
	
	@Column(name = "PAN_NUMBER")
	private String panNo;   
	
	@Column(name = "HOSPITAL_CODE")
	private String hospitalCode;
	
	@Column(name = "EMAIL_ID")
	private String emailId;  
	
	@Column(name = "PROPOSER_CODE")
	private String proposerCode;   
	
	@Column(name = "PROPOSER_NAME")
	private String proposerName; 
	
	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_DATE")
	private Date createdDate;   
	
	@Column(name = "CREATED_BY")
	private String createdBy;   
	
	@Column(name = "NOTIFICATION_FLAG")
	private String notificationFlag;  
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PAYMENT_STATUS_ID")
	private MastersValue paymentStatusId;  
	
	@Column(name = "LOT_STATUS")
	private String lotstatus;   
	
	@Column(name = "BATCH_STATUS")
	private  String batchStatus;
	
	@Column(name = "BANK_CODE")
	private String bankCode;  
	
	@Temporal(TemporalType.DATE)
	@Column(name = "CHEQUE_DD_DATE")
	private Date chequeDdDate;   
	
	@Column(name = "CHEQUE_DD_NUMBER")
	private String chequeDdNumber;
	
	@Column(name = "BANK_NAME")
	private String bankName;  
	
	@Column(name = "TDS_AMOUNT")
	private Long tdsAmount;   
	
	@Column(name = "TDS_PERCENTAGE")
	private Long tdsPercentage;   
	
	@Column(name = "NET_AMOUNT")
	private Long netAmount;   
	
	@Temporal(TemporalType.DATE)
	@Column(name = "FA_APPROVAL_DATE")
	private Date faApprovalDate;   
	
	@Column(name = "REISSUE_REJECTION_FLAG")
	private String reissueRejectionFlag;    
	
	@Column(name = "BATCH_HOLD_FLAG")
	private String batchHoldFlag;  
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STAGE_ID")
	private Stage stageid;     
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_ID")
	private Status statusId;     
	
	@Column(name = "LETTER_FLAG")
	private String letterFlag;    
	
	@Column(name = "DOCUMENT_RECEIVED_FROM")
	private String docReceicedFrom;  
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;   
	
	@Temporal(TemporalType.DATE)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;   
	
	@Column(name = "REASON_FOR_CHANGE")
	private String reasonForChange;  
	
	@Column(name = "LEGAL_HEIR_NAME")
	private String legalHeirName; 
	
	@Temporal(TemporalType.DATE)
	@Column(name = "PAYMENT_DATE")
	
	private Date paymentdate;   
	@Column(name = "DM_CODE")
	private Long dmCode;  
	
	@Temporal(TemporalType.DATE)
	@Column(name = "BATCH_CREATED_DATE")
	private Date batchCreatedDate;  
	
	@Column(name = "REMARKS")
	private String remarks; 
	
	@Column(name = "DELETED_FLAG")
	private String deletedFlag;    
	
	@Column(name = "BEN_APPR_AMT")
	private Double benApprAmt;  
	
	@Column(name = "DELAY_DAYS")
	private Long delayDays;     
	
	@Column(name = "INT_RATE")
	private Long intRate;    
	
	@Column(name = "INT_AMT")
	private Double intAMt;  
	
	@Column(name = "INT_REMARKS")
	private String intRemarks; 
	
	@Column(name = "TOTAL_AMOUNT")
	private Double totalAmount;  
	
	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_ACKNOW_DT")
	private Date lastAcknowDt;   
	
	@Column(name = "ALLOWED_DELAY_DAYS")
	private Long allowedDelayDays;   
	
	@Column(name = "PAYMENT_BANK_CODE")
	private String paymentBankCode;   
	
	@Column(name = "PAYMENT_BANK_NAME")
	private String paymentBankName;  
	
	@Column(name = "RECONSIDER_FLAG")
	private String reconsiderFlag;   
	
	@Column(name = "LT_CREATED_DATE")
	private String ltCreatedDate; 
	
	@Temporal(TemporalType.DATE)
	@Column(name = "EMAIL_DATE")
	private Date emailDate;   
	
	@Column(name = "EMAIL_FLAG")
	private String emailFlag;   

	@Column(name = "TOT_APPROVED_AMOUNT")
	private Double totApprovedAmt;   
	
	@Column(name = "PREMIUM_AMOUNT")
	private Double premiumAmt;   
	
	@Column(name = "INT_FLAG_PREMIUM")
	private String intFlagPremium;    
	
	@Column(name = "INSTAL_DED_FLAG")
	private String instalDedFlag;    
	
	@Column(name = "BATCH_PROCESS_FLAG")
	private String batchProcessFlag;  
	
	@Column(name = "CLM_SEC_CODE")
	private String clmSecCode;   
	
	@Column(name = "CLM_CVR_CODE")
	private String clmCvrCode;   
	
	@Column(name = "CLM_SUB_CVR_CODE")
	private String clmSubCvrCode;   
	@Column(name = "LUMPSUM_FLAG")
	private String lumpsumFlag;    
	@Column(name = "REC_STATUS")
	private String recStatus;    
	@Column(name = "BENEFITS_ID")
	private Long denefitsId;     
	@Column(name = "BEN_APR_AMT")
	private Double benAprAMt;   
	@Column(name = "OPT_APPROVED_AMOUNT")
	private Double optApprovedAmt;   
	@Column(name = "ADD_APPROVED_AMOUNT")
	private Double addApprovedAmt;   
	@Column(name = "PROCESS_CLM_TYPE")
	private String processClmType;    
	@Column(name = "DEDUCTIBLE_RECOVERED_INSURED")
	private Double deductibleRecoveredInsured; 
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_RECOVERY")
	private Date dateRecovery;   
	@Column(name = "AMT_RECOVERED_INR")
	private Double amtRecoveredInr;   
	@Column(name = "AMT_RECOVERED_DOLLAR")
	private Double amtRecoveredDollar;   
	@Column(name = "REMARKS_PROCESSOR")
	private String remarksProcessor; 
	@Column(name = "NEGOTIATOR_NAME")
	private String negotiatorName;  
	@Column(name = "REASON_NEGOTIATION")
	private String reasonNegotiation; 
	@Column(name = "REASON_SUGGEST_REJECTIOR")
	private String reasonSuggestRejectior; 
	@Column(name = "REASON_SUGESST_APPROVAL")
	private String reasonSugesstApproval; 
	@Column(name = "REMARKS_APPROVER")
	private String remarksApprover; 
	@Column(name = "AMOUNT_DOLLER")                          
	private Double amountDoller;
	@Column(name = "DEDUCTIBLE_NON_PAYBLES")
	private Double deductibleNonPayBles;   
	@Column(name = "TOTAL_AMT_PAYABLE_DOLLAR")
	private Double totalAmtPaybleDollar;   
	@Column(name = "TOTAL_AMT_INR")
	private Double totAmtINr;   
	@Column(name = "APPROVED_AMT_DOLLAR")
	private Double approvedAmtDollar;   
	@Column(name = "AGREED_AMT_DOLLAR")
	private Double agreedAmtDollar;   
	@Column(name = "DIFF_AMT_DOLLLAR")
	private Double diffAmtDollar;   
	@Column(name = "EXPENSES_DOLLAR")
	private Double expensesDollar;   
	@Column(name = "NEGO_FEES_CLAIMED_DOLLAR")
	private Double negoFeesClaimedDollar;   
	@Column(name = "NEGO_FEES_CLAIMED_INR")
	private Double negoFeesClaimedInr;   
	@Column(name = "NEGO_FEE_CAPPING")
	private Double negoFeesCapping;   
	@Column(name = "HANDLING_CHARGES_DOLLAR")
	private Double handlingChargesDollar;   
	@Column(name = "TOTAL_EXPENCE_DOLLAR")
	private Double totalExpenceDollar;   

	@Column(name = "INSURED_NAME")
	private String insuredName; 
	
	@Column(name = "CONVERSION_RATE")
	private Double conversionRate; 
	
	@Column(name = "EXPENSE_TYPE")
	private String expensesType; 
	
	//CR20181327 2 new fields added in DB
	
	@Column(name = "POSTED_DATE")
	private Date postedDate; 

	@Column(name = "BANK_CHARGES")
	private Long bankCharges; 
	
	@Column(name = "DOCUMENT_TOKEN")
	private Long docToken;

	@Override
	public Long getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setKey(Long key) {
		// TODO Auto-generated method stub
		
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

	public Long getPaymentCpuCode() {
		return paymentCpuCode;
	}

	public void setPaymentCpuCode(Long paymentCpuCode) {
		this.paymentCpuCode = paymentCpuCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getClaimType() {
		return ClaimType;
	}

	public void setClaimType(String claimType) {
		ClaimType = claimType;
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

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
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

	public MastersValue getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(MastersValue paymentType) {
		this.paymentType = paymentType;
	}

	public Double getApprovedAmt() {
		return approvedAmt;
	}

	public void setApprovedAmt(Double approvedAmt) {
		this.approvedAmt = approvedAmt;
	}

	public Long getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(Long cpuCode) {
		this.cpuCode = cpuCode;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
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

	public String getPayabelAt() {
		return payabelAt;
	}

	public void setPayabelAt(String payabelAt) {
		this.payabelAt = payabelAt;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
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

	public MastersValue getPaymentStatusId() {
		return paymentStatusId;
	}

	public void setPaymentStatusId(MastersValue paymentStatusId) {
		this.paymentStatusId = paymentStatusId;
	}

	public String getLotstatus() {
		return lotstatus;
	}

	public void setLotstatus(String lotstatus) {
		this.lotstatus = lotstatus;
	}

	public String getBatchStatus() {
		return batchStatus;
	}

	public void setBatchStatus(String batchStatus) {
		this.batchStatus = batchStatus;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public Date getChequeDdDate() {
		return chequeDdDate;
	}

	public void setChequeDdDate(Date chequeDdDate) {
		this.chequeDdDate = chequeDdDate;
	}

	public String getChequeDdNumber() {
		return chequeDdNumber;
	}

	public void setChequeDdNumber(String chequeDdNumber) {
		this.chequeDdNumber = chequeDdNumber;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Long getTdsAmount() {
		return tdsAmount;
	}

	public void setTdsAmount(Long tdsAmount) {
		this.tdsAmount = tdsAmount;
	}

	public Long getTdsPercentage() {
		return tdsPercentage;
	}

	public void setTdsPercentage(Long tdsPercentage) {
		this.tdsPercentage = tdsPercentage;
	}

	public Long getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Long netAmount) {
		this.netAmount = netAmount;
	}

	public Date getFaApprovalDate() {
		return faApprovalDate;
	}

	public void setFaApprovalDate(Date faApprovalDate) {
		this.faApprovalDate = faApprovalDate;
	}

	public String getReissueRejectionFlag() {
		return reissueRejectionFlag;
	}

	public void setReissueRejectionFlag(String reissueRejectionFlag) {
		this.reissueRejectionFlag = reissueRejectionFlag;
	}

	public String getBatchHoldFlag() {
		return batchHoldFlag;
	}

	public void setBatchHoldFlag(String batchHoldFlag) {
		this.batchHoldFlag = batchHoldFlag;
	}

	public Stage getStageid() {
		return stageid;
	}

	public void setStageid(Stage stageid) {
		this.stageid = stageid;
	}

	public Status getStatusId() {
		return statusId;
	}

	public void setStatusId(Status statusId) {
		this.statusId = statusId;
	}

	public String getLetterFlag() {
		return letterFlag;
	}

	public void setLetterFlag(String letterFlag) {
		this.letterFlag = letterFlag;
	}

	public String getDocReceicedFrom() {
		return docReceicedFrom;
	}

	public void setDocReceicedFrom(String docReceicedFrom) {
		this.docReceicedFrom = docReceicedFrom;
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

	public Date getPaymentdate() {
		return paymentdate;
	}

	public void setPaymentdate(Date paymentdate) {
		this.paymentdate = paymentdate;
	}

	public Long getDmCode() {
		return dmCode;
	}

	public void setDmCode(Long dmCode) {
		this.dmCode = dmCode;
	}

	public Date getBatchCreatedDate() {
		return batchCreatedDate;
	}

	public void setBatchCreatedDate(Date batchCreatedDate) {
		this.batchCreatedDate = batchCreatedDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public Double getBenApprAmt() {
		return benApprAmt;
	}

	public void setBenApprAmt(Double benApprAmt) {
		this.benApprAmt = benApprAmt;
	}

	public Long getDelayDays() {
		return delayDays;
	}

	public void setDelayDays(Long delayDays) {
		this.delayDays = delayDays;
	}

	public Long getIntRate() {
		return intRate;
	}

	public void setIntRate(Long intRate) {
		this.intRate = intRate;
	}

	public Double getIntAMt() {
		return intAMt;
	}

	public void setIntAMt(Double intAMt) {
		this.intAMt = intAMt;
	}

	public String getIntRemarks() {
		return intRemarks;
	}

	public void setIntRemarks(String intRemarks) {
		this.intRemarks = intRemarks;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Date getLastAcknowDt() {
		return lastAcknowDt;
	}

	public void setLastAcknowDt(Date lastAcknowDt) {
		this.lastAcknowDt = lastAcknowDt;
	}

	public Long getAllowedDelayDays() {
		return allowedDelayDays;
	}

	public void setAllowedDelayDays(Long allowedDelayDays) {
		this.allowedDelayDays = allowedDelayDays;
	}

	public String getPaymentBankCode() {
		return paymentBankCode;
	}

	public void setPaymentBankCode(String paymentBankCode) {
		this.paymentBankCode = paymentBankCode;
	}

	public String getPaymentBankName() {
		return paymentBankName;
	}

	public void setPaymentBankName(String paymentBankName) {
		this.paymentBankName = paymentBankName;
	}

	public String getReconsiderFlag() {
		return reconsiderFlag;
	}

	public void setReconsiderFlag(String reconsiderFlag) {
		this.reconsiderFlag = reconsiderFlag;
	}

	public String getLtCreatedDate() {
		return ltCreatedDate;
	}

	public void setLtCreatedDate(String ltCreatedDate) {
		this.ltCreatedDate = ltCreatedDate;
	}

	public Date getEmailDate() {
		return emailDate;
	}

	public void setEmailDate(Date emailDate) {
		this.emailDate = emailDate;
	}

	public String getEmailFlag() {
		return emailFlag;
	}

	public void setEmailFlag(String emailFlag) {
		this.emailFlag = emailFlag;
	}

	public Double getTotApprovedAmt() {
		return totApprovedAmt;
	}

	public void setTotApprovedAmt(Double totApprovedAmt) {
		this.totApprovedAmt = totApprovedAmt;
	}

	public Double getPremiumAmt() {
		return premiumAmt;
	}

	public void setPremiumAmt(Double premiumAmt) {
		this.premiumAmt = premiumAmt;
	}

	public String getIntFlagPremium() {
		return intFlagPremium;
	}

	public void setIntFlagPremium(String intFlagPremium) {
		this.intFlagPremium = intFlagPremium;
	}

	public String getInstalDedFlag() {
		return instalDedFlag;
	}

	public void setInstalDedFlag(String instalDedFlag) {
		this.instalDedFlag = instalDedFlag;
	}

	public String getBatchProcessFlag() {
		return batchProcessFlag;
	}

	public void setBatchProcessFlag(String batchProcessFlag) {
		this.batchProcessFlag = batchProcessFlag;
	}

	public String getClmSecCode() {
		return clmSecCode;
	}

	public void setClmSecCode(String clmSecCode) {
		this.clmSecCode = clmSecCode;
	}

	public String getClmCvrCode() {
		return clmCvrCode;
	}

	public void setClmCvrCode(String clmCvrCode) {
		this.clmCvrCode = clmCvrCode;
	}

	public String getClmSubCvrCode() {
		return clmSubCvrCode;
	}

	public void setClmSubCvrCode(String clmSubCvrCode) {
		this.clmSubCvrCode = clmSubCvrCode;
	}

	public String getLumpsumFlag() {
		return lumpsumFlag;
	}

	public void setLumpsumFlag(String lumpsumFlag) {
		this.lumpsumFlag = lumpsumFlag;
	}

	public String getRecStatus() {
		return recStatus;
	}

	public void setRecStatus(String recStatus) {
		this.recStatus = recStatus;
	}

	public Long getDenefitsId() {
		return denefitsId;
	}

	public void setDenefitsId(Long denefitsId) {
		this.denefitsId = denefitsId;
	}

	public Double getBenAprAMt() {
		return benAprAMt;
	}

	public void setBenAprAMt(Double benAprAMt) {
		this.benAprAMt = benAprAMt;
	}

	public Double getOptApprovedAmt() {
		return optApprovedAmt;
	}

	public void setOptApprovedAmt(Double optApprovedAmt) {
		this.optApprovedAmt = optApprovedAmt;
	}

	public Double getAddApprovedAmt() {
		return addApprovedAmt;
	}

	public void setAddApprovedAmt(Double addApprovedAmt) {
		this.addApprovedAmt = addApprovedAmt;
	}

	public String getProcessClmType() {
		return processClmType;
	}

	public void setProcessClmType(String processClmType) {
		this.processClmType = processClmType;
	}

	public Double getDeductibleRecoveredInsured() {
		return deductibleRecoveredInsured;
	}

	public void setDeductibleRecoveredInsured(Double deductibleRecoveredInsured) {
		this.deductibleRecoveredInsured = deductibleRecoveredInsured;
	}

	public Date getDateRecovery() {
		return dateRecovery;
	}

	public void setDateRecovery(Date dateRecovery) {
		this.dateRecovery = dateRecovery;
	}

	public Double getAmtRecoveredInr() {
		return amtRecoveredInr;
	}

	public void setAmtRecoveredInr(Double amtRecoveredInr) {
		this.amtRecoveredInr = amtRecoveredInr;
	}

	public Double getAmtRecoveredDollar() {
		return amtRecoveredDollar;
	}

	public void setAmtRecoveredDollar(Double amtRecoveredDollar) {
		this.amtRecoveredDollar = amtRecoveredDollar;
	}

	public String getRemarksProcessor() {
		return remarksProcessor;
	}

	public void setRemarksProcessor(String remarksProcessor) {
		this.remarksProcessor = remarksProcessor;
	}

	public String getNegotiatorName() {
		return negotiatorName;
	}

	public void setNegotiatorName(String negotiatorName) {
		this.negotiatorName = negotiatorName;
	}

	public String getReasonNegotiation() {
		return reasonNegotiation;
	}

	public void setReasonNegotiation(String reasonNegotiation) {
		this.reasonNegotiation = reasonNegotiation;
	}

	public String getReasonSuggestRejectior() {
		return reasonSuggestRejectior;
	}

	public void setReasonSuggestRejectior(String reasonSuggestRejectior) {
		this.reasonSuggestRejectior = reasonSuggestRejectior;
	}

	public String getReasonSugesstApproval() {
		return reasonSugesstApproval;
	}

	public void setReasonSugesstApproval(String reasonSugesstApproval) {
		this.reasonSugesstApproval = reasonSugesstApproval;
	}

	public String getRemarksApprover() {
		return remarksApprover;
	}

	public void setRemarksApprover(String remarksApprover) {
		this.remarksApprover = remarksApprover;
	}

	public Double getAmountDoller() {
		return amountDoller;
	}

	public void setAmountDoller(Double amountDoller) {
		this.amountDoller = amountDoller;
	}

	public Double getDeductibleNonPayBles() {
		return deductibleNonPayBles;
	}

	public void setDeductibleNonPayBles(Double deductibleNonPayBles) {
		this.deductibleNonPayBles = deductibleNonPayBles;
	}

	public Double getTotalAmtPaybleDollar() {
		return totalAmtPaybleDollar;
	}

	public void setTotalAmtPaybleDollar(Double totalAmtPaybleDollar) {
		this.totalAmtPaybleDollar = totalAmtPaybleDollar;
	}

	public Double getTotAmtINr() {
		return totAmtINr;
	}

	public void setTotAmtINr(Double totAmtINr) {
		this.totAmtINr = totAmtINr;
	}

	public Double getApprovedAmtDollar() {
		return approvedAmtDollar;
	}

	public void setApprovedAmtDollar(Double approvedAmtDollar) {
		this.approvedAmtDollar = approvedAmtDollar;
	}

	public Double getAgreedAmtDollar() {
		return agreedAmtDollar;
	}

	public void setAgreedAmtDollar(Double agreedAmtDollar) {
		this.agreedAmtDollar = agreedAmtDollar;
	}

	public Double getDiffAmtDollar() {
		return diffAmtDollar;
	}

	public void setDiffAmtDollar(Double diffAmtDollar) {
		this.diffAmtDollar = diffAmtDollar;
	}

	public Double getExpensesDollar() {
		return expensesDollar;
	}

	public void setExpensesDollar(Double expensesDollar) {
		this.expensesDollar = expensesDollar;
	}

	public Double getNegoFeesClaimedDollar() {
		return negoFeesClaimedDollar;
	}

	public void setNegoFeesClaimedDollar(Double negoFeesClaimedDollar) {
		this.negoFeesClaimedDollar = negoFeesClaimedDollar;
	}

	public Double getNegoFeesClaimedInr() {
		return negoFeesClaimedInr;
	}

	public void setNegoFeesClaimedInr(Double negoFeesClaimedInr) {
		this.negoFeesClaimedInr = negoFeesClaimedInr;
	}

	public Double getNegoFeesCapping() {
		return negoFeesCapping;
	}

	public void setNegoFeesCapping(Double negoFeesCapping) {
		this.negoFeesCapping = negoFeesCapping;
	}

	public Double getHandlingChargesDollar() {
		return handlingChargesDollar;
	}

	public void setHandlingChargesDollar(Double handlingChargesDollar) {
		this.handlingChargesDollar = handlingChargesDollar;
	}

	public Double getTotalExpenceDollar() {
		return totalExpenceDollar;
	}

	public void setTotalExpenceDollar(Double totalExpenceDollar) {
		this.totalExpenceDollar = totalExpenceDollar;
	}

	public Long getPaymentKey() {
		return paymentKey;
	}

	public void setPaymentKey(Long paymentKey) {
		this.paymentKey = paymentKey;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public Double getConversionRate() {
		return conversionRate;
	}

	public void setConversionRate(Double conversionRate) {
		this.conversionRate = conversionRate;
	}

	public String getExpensesType() {
		return expensesType;
	}

	public void setExpensesType(String expensesType) {
		this.expensesType = expensesType;
	}

	public Date getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}

	public Long getBankCharges() {
		return bankCharges;
	}

	public void setBankCharges(Long bankCharges) {
		this.bankCharges = bankCharges;
	}

	public Long getDocToken() {
		return docToken;
	}

	public void setDocToken(Long docToken) {
		this.docToken = docToken;
	}

	
}