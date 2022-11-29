package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="GLX_BANCS_PAYMENT_CANCEL")
@NamedQueries({
@NamedQuery(name="BancsPaymentCancel.findAll", query="SELECT c FROM BancsPaymentCancel c"),
@NamedQuery(name ="BancsPaymentCancel.findByKey",query="SELECT c FROM BancsPaymentCancel c WHERE c.key = :primaryKey"),
@NamedQuery(name ="BancsPaymentCancel.findByPaymentKey",query="SELECT c FROM BancsPaymentCancel c WHERE c.paymentKey = :paymentKey"),
@NamedQuery(name ="BancsPaymentCancel.findByPaymentKeyPaymentType",query="SELECT c FROM BancsPaymentCancel c WHERE c.paymentKey = :paymentKey and c.paymentType = :paymentType")
})
public class BancsPaymentCancel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3516055208934934676L;

	@Id
	@SequenceGenerator(name="GLX_BANCS_PAYMENT_CANCEL_KEY_GENERATOR", sequenceName = "SEQ_GLX_BANCS_CAN_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GLX_BANCS_PAYMENT_CANCEL_KEY_GENERATOR" )
	@Column(name = "GLX_BANCS_CAN_KEY")
	private Long key;
	
	@Column(name = "INTIMATION_NUMBER")
    private String intimationNumber;
	
	@Column(name = "CLAIM_NUMBER")
    private String claimNumber;
	
	@Column(name = "ROD_NUMBER")
    private String rodNumber;
	
	@Column(name = "POLICY_NUMBER")
    private String policyNumber;
	
	@Column(name = "PAYMENT_KEY")
    private Long paymentKey;
	
	@Column(name = "ROD_KEY")
    private Long rodKey;
	
	@Column(name = "BANCS_UPR_ID")
    private String bancsUprId;
	
	@Column(name = "CLAIM_TYPE")
    private String claimType;
	
	@Column(name = "PAYMENT_TYPE")
    private String paymentType;
	
	@Column(name = "PAYMENT_CANCEL_TYPE_KEY")
    private Long paymenetCancelTypeKey;
	
	@Column(name = "PAYMENT_CANCEL_GLX_READ")
    private String paymentCancelGlxRead;
	
	@Column(name = "PAYMENT_CANCEL_BANCS_READ")
    private String paymentCancelBancsRead;
	
	@Column(name = "CREATED_BY")
    private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
    private Date createdDate;
	
	@Column(name = "MODIFIED_BY")
    private String modifedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
    private Date modifiedDate;
	
	@Column(name ="PAYMENT_CANCEL_REMARKS")
	private String paymenetCancelRemarks;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "GLX_READ_DATE")
    private Date glxReadDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BANCS_READ_DATE")
    private Date bancsReadDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="TRANSACTION_DATE")
	private Date transactionDate;
	
	@Column(name="APPROVED_AMOUNT")
	private Double approvedAmount;
	
	@Column(name="DD_PAYABLE_AT")
	private String paybleAt;
	
	@Column(name="DD_PAYABLE_NAME")
	private String payableName;
	
	@Column(name="INSTRUMENT_NUMBER")
	private String instrumentNumber;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="INSTRUMEN_DATE")
	private Date instrumentDate;
	
	@Column(name="REMITTANCE_BANK_NAME")
	private String remittanceBankName;
	
	@Column(name="REMITTANCE_BANK_BRANCH")
	private String remittancebankBranch;
	
	@Column(name="REMITTANC_ACCOUNT")
	private String remittanceAccount;
	
	@Column(name="PARTY_CODE")
	private String partyCode;
	
	@Column(name="LOB")
	private String lob;
	
	@Column(name="CPU")
	private String cpu;
	
	@Column(name="PAYMENTCPUCODE")
	private String paymentCpuCode;
	
	@Column(name="AMOUNTCHANGEDFLAG")
	private String amountChangedFlag;
	
	@Column(name="REASON_FOR_PAYMENT_RECOVERY")
	private Long recoveryReasonId;
	
	@Column(name="NATURE_OF_PAYMENT_RECOVERY")
	private Long natureRecoveryId;
	
	@Column(name="PREVIOUS_PAYMENT_MODE")
	private String previousPaymentMode;
	

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

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}

	public String getRodNumber() {
		return rodNumber;
	}

	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public Long getPaymentKey() {
		return paymentKey;
	}

	public void setPaymentKey(Long paymentKey) {
		this.paymentKey = paymentKey;
	}

	public Long getRodKey() {
		return rodKey;
	}

	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	public String getBancsUprId() {
		return bancsUprId;
	}

	public void setBancsUprId(String bancsUprId) {
		this.bancsUprId = bancsUprId;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Long getPaymenetCancelTypeKey() {
		return paymenetCancelTypeKey;
	}

	public void setPaymenetCancelTypeKey(Long paymenetCancelTypeKey) {
		this.paymenetCancelTypeKey = paymenetCancelTypeKey;
	}

	public String getPaymentCancelGlxRead() {
		return paymentCancelGlxRead;
	}

	public void setPaymentCancelGlxRead(String paymentCancelGlxRead) {
		this.paymentCancelGlxRead = paymentCancelGlxRead;
	}

	public String getPaymentCancelBancsRead() {
		return paymentCancelBancsRead;
	}

	public void setPaymentCancelBancsRead(String paymentCancelBancsRead) {
		this.paymentCancelBancsRead = paymentCancelBancsRead;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifedBy() {
		return modifedBy;
	}

	public void setModifedBy(String modifedBy) {
		this.modifedBy = modifedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getPaymenetCancelRemarks() {
		return paymenetCancelRemarks;
	}

	public void setPaymenetCancelRemarks(String paymenetCancelRemarks) {
		this.paymenetCancelRemarks = paymenetCancelRemarks;
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

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Double getApprovedAmount() {
		return approvedAmount;
	}

	public void setApprovedAmount(Double approvedAmount) {
		this.approvedAmount = approvedAmount;
	}

	public String getPaybleAt() {
		return paybleAt;
	}

	public void setPaybleAt(String paybleAt) {
		this.paybleAt = paybleAt;
	}

	public String getPayableName() {
		return payableName;
	}

	public void setPayableName(String payableName) {
		this.payableName = payableName;
	}

	public String getInstrumentNumber() {
		return instrumentNumber;
	}

	public void setInstrumentNumber(String instrumentNumber) {
		this.instrumentNumber = instrumentNumber;
	}

	public Date getInstrumentDate() {
		return instrumentDate;
	}

	public void setInstrumentDate(Date instrumentDate) {
		this.instrumentDate = instrumentDate;
	}

	public String getRemittanceBankName() {
		return remittanceBankName;
	}

	public void setRemittanceBankName(String remittanceBankName) {
		this.remittanceBankName = remittanceBankName;
	}

	public String getRemittancebankBranch() {
		return remittancebankBranch;
	}

	public void setRemittancebankBranch(String remittancebankBranch) {
		this.remittancebankBranch = remittancebankBranch;
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
		return lob;
	}

	public void setLob(String lob) {
		this.lob = lob;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getPaymentCpuCode() {
		return paymentCpuCode;
	}

	public void setPaymentCpuCode(String paymentCpuCode) {
		this.paymentCpuCode = paymentCpuCode;
	}

	public String getAmountChangedFlag() {
		return amountChangedFlag;
	}

	public void setAmountChangedFlag(String amountChangedFlag) {
		this.amountChangedFlag = amountChangedFlag;
	}

	public Long getRecoveryReasonId() {
		return recoveryReasonId;
	}

	public void setRecoveryReasonId(Long recoveryReasonId) {
		this.recoveryReasonId = recoveryReasonId;
	}

	public Long getNatureRecoveryId() {
		return natureRecoveryId;
	}

	public void setNatureRecoveryId(Long natureRecoveryId) {
		this.natureRecoveryId = natureRecoveryId;
	}

	public String getPreviousPaymentMode() {
		return previousPaymentMode;
	}

	public void setPreviousPaymentMode(String previousPaymentMode) {
		this.previousPaymentMode = previousPaymentMode;
	}
	
	
}
