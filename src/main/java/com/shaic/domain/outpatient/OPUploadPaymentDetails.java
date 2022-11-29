package com.shaic.domain.outpatient;

import java.sql.Timestamp;
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

@Entity
@Table(name = "IMS_CLS_OP_UPLOAD_DOC_DTLS")
@NamedQueries({
	@NamedQuery(name ="OPUploadPaymentDetails.findByKey",query="SELECT r FROM OPUploadPaymentDetails r WHERE r.key = :key"),
})
public class OPUploadPaymentDetails {
	
	@Id
	@SequenceGenerator(name="IMS_CLS_OP_UPLOAD_DOC_DTLS_KEY_GENERATOR", sequenceName = "SEQ_OP_UPLOAD_DOCUMENT_DTLS", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="IMS_CLS_OP_UPLOAD_DOC_DTLS_KEY_GENERATOR" ) 
	@Column(name="OP_UPLOAD_DOCUMENT_KEY", updatable=false)
	private Long key;
		
	@Column(name = "BATCH_NUMBER")
	private String batchNumber;
	
	@Column(name = "INTIMATION_NUMBER")
	private String intimationNumber;
	
	@Column(name = "CLAIM_NUMBER")
	private String claimNumber;
	
	@Column(name="PAYMENT_TYPE")
	private String paymentType;
	
	@Column(name="BILL_AMOUNT")
	private Double billAmount;
	
	@Column(name="PAYABLE_AMOUNT")
	private Double payableAmount;
	
	@Column(name="TOT_BILL_AMOUNT")
	private Double totBillAmount;
	
	@Column(name="DEDUCTION_AMOUNT")
	private Double deductionAmount;
	
	@Column(name="TOT_PAYABLE_AMOUNT")
	private Double totPayableAmount;
	
	@Column(name="REMARKS")
	private String remarks;
	
	@Column(name="PAYEE_NAME")
	private String payeeName;
	
	@Column(name="CHEQUE_NUMBER")
	private String chequeNo;
	
	@Column(name="CHEQUE_DATE")
	private Date checqueDate;
	
	@Column(name="UPLOAD_STATUS")
	private String uploadStatus;
	
	@Column(name="UPLOAD_REMARKS")
	private String uploadRemarks;
	
	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATD_DATE")
	private Timestamp createdDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;

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

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Double getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}

	public Double getPayableAmount() {
		return payableAmount;
	}

	public void setPayableAmount(Double payableAmount) {
		this.payableAmount = payableAmount;
	}

	public Double getTotBillAmount() {
		return totBillAmount;
	}

	public void setTotBillAmount(Double totBillAmount) {
		this.totBillAmount = totBillAmount;
	}

	public Double getDeductionAmount() {
		return deductionAmount;
	}

	public void setDeductionAmount(Double deductionAmount) {
		this.deductionAmount = deductionAmount;
	}

	public Double getTotPayableAmount() {
		return totPayableAmount;
	}

	public void setTotPayableAmount(Double totPayableAmount) {
		this.totPayableAmount = totPayableAmount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public String getChequeNo() {
		return chequeNo;
	}

	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}

	public Date getChecqueDate() {
		return checqueDate;
	}

	public void setChecqueDate(Date checqueDate) {
		this.checqueDate = checqueDate;
	}

	public String getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	public String getUploadRemarks() {
		return uploadRemarks;
	}

	public void setUploadRemarks(String uploadRemarks) {
		this.uploadRemarks = uploadRemarks;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
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

}
