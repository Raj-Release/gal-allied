package com.shaic.restservices.bancs;


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
@Table(name="IMS_CLSB_PAY_REVERSE_FEED")
@NamedQueries({
	@NamedQuery(name="ClaimReverseFeedTable.findAll", query="SELECT o FROM ClaimReverseFeedTable o")
})

public class ClaimReverseFeedTable{

	/**
	 * 
	 */
	@Id
	@SequenceGenerator(name="IMS_CLSB_PAY_REVERSE_FEED_KEY_GENERATOR", sequenceName = "SEQ_B_PAY_REVERSE_FEED"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLSB_PAY_REVERSE_FEED_KEY_GENERATOR" ) 
	
	@Column(name = "PAY_REVERSE_KEY")
	private Long key;
	
	@Column(name = "FIELD_INDICATOR")
	private String prfFieldIndicator;

	@Column(name = "BANK_ACCOUNT_NUMBER")
	private String prfBankAccountNumber;
	
	@Column(name = "TOTAL_AMOUNT_PAID")
	private Double prfTotalAmountPaid;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "VOUCHER_DATE")
	private Date prfVoucherDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CHEQUE_DATE")
	private Date prfChequeDate;
	
	@Column(name = "CHEQUE_NUMBER")
	private String prfChequeNumber;

	@Column(name = "PAYMENT_METHOD")
	private String prfPaymentMethod;

	@Column(name = "NARRATION")
	private String prfNarration;
	
	@Column(name = "PAY_VOUCHER_NO")
	private String prfPaymentVoucherNumber;

	@Column(name = "INVOICE_NUMBER")
	private String prfInvoiceNumber;

	@Column(name = "INVOICE_PAID_AMT")
	private Double prfInvoicePaidAmount;
	
	@Column(name = "CHECK_ID")
	private Double checkId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_UPDATE_DATE")
	private Date lastUpdateDate;

	@Column(name = "ENTRY_SOURCE")
	private String entrySource;
	
	@Column(name = "ACTIVE_STATUS")
	private Long activeStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifieDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name = "UPD_FLAG")
	private String updFlag;
	
	@Column(name = "CLAIM_UPR_ID")
	private String claimUprId;
	
	@Column(name = "BANK_REMARKS")
	private String bankRemarks;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getPrfFieldIndicator() {
	return prfFieldIndicator;
	}

	public void setPrfFieldIndicator(String prfFieldIndicator) {
		this.prfFieldIndicator = prfFieldIndicator;
	}

	public String getPrfBankAccountNumber() {
		return prfBankAccountNumber;
	}

	public void setPrfBankAccountNumber(String prfBankAccountNumber) {
		this.prfBankAccountNumber = prfBankAccountNumber;
	}

	public Double getPrfTotalAmountPaid() {
		return prfTotalAmountPaid;
	}

	public void setPrfTotalAmountPaid(Double prfTotalAmountPaid) {
		this.prfTotalAmountPaid = prfTotalAmountPaid;
	}

	public Date getPrfVoucherDate() {
		return prfVoucherDate;
	}

	public void setPrfVoucherDate(Date prfVoucherDate) {
		this.prfVoucherDate = prfVoucherDate;
	}

	public Date getPrfChequeDate() {
		return prfChequeDate;
	}

	public void setPrfChequeDate(Date prfChequeDate) {
		this.prfChequeDate = prfChequeDate;
	}

	public String getPrfChequeNumber() {
		return prfChequeNumber;
	}

	public void setPrfChequeNumber(String prfChequeNumber) {
		this.prfChequeNumber = prfChequeNumber;
	}

	public String getPrfPaymentMethod() {
		return prfPaymentMethod;
	}

	public void setPrfPaymentMethod(String prfPaymentMethod) {
		this.prfPaymentMethod = prfPaymentMethod;
	}

	public String getPrfNarration() {
		return prfNarration;
	}

	public void setPrfNarration(String prfNarration) {
		this.prfNarration = prfNarration;
	}

	public String getPrfPaymentVoucherNumber() {
		return prfPaymentVoucherNumber;
	}

	public void setPrfPaymentVoucherNumber(String prfPaymentVoucherNumber) {
		this.prfPaymentVoucherNumber = prfPaymentVoucherNumber;
	}

	public String getPrfInvoiceNumber() {
		return prfInvoiceNumber;
	}

	public void setPrfInvoiceNumber(String prfInvoiceNumber) {
		this.prfInvoiceNumber = prfInvoiceNumber;
	}

	public Double getPrfInvoicePaidAmount() {
		return prfInvoicePaidAmount;
	}

	public void setPrfInvoicePaidAmount(Double prfInvoicePaidAmount) {
		this.prfInvoicePaidAmount = prfInvoicePaidAmount;
	}

	public Double getCheckId() {
		return checkId;
	}

	public void setCheckId(Double checkId) {
		this.checkId = checkId;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getEntrySource() {
		return entrySource;
	}

	public void setEntrySource(String entrySource) {
		this.entrySource = entrySource;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
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

	public Date getModifieDate() {
		return modifieDate;
	}

	public void setModifieDate(Date modifieDate) {
		this.modifieDate = modifieDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getUpdFlag() {
		return updFlag;
	}

	public void setUpdFlag(String updFlag) {
		this.updFlag = updFlag;
	}

	public String getClaimUprId() {
		return claimUprId;
	}

	public void setClaimUprId(String claimUprId) {
		this.claimUprId = claimUprId;
	}

	public String getBankRemarks() {
		return bankRemarks;
	}

	public void setBankRemarks(String bankRemarks) {
		this.bankRemarks = bankRemarks;
	}
	
}
