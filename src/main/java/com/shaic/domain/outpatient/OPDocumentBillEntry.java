package com.shaic.domain.outpatient;

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

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name = "IMS_CLS_OP_DOCUMENT_BILL_ENTRY")
@NamedQueries({
	@NamedQuery(name ="OPDocumentBillEntry.findByHealthCheckupKey",query="SELECT r FROM OPDocumentBillEntry r WHERE r.opHealthCheckup is not null and r.opHealthCheckup.key = :healthCheckupKey"),
})
public class OPDocumentBillEntry extends AbstractEntity{

	private static final long serialVersionUID = -797939768188532132L;
	@Id
	@SequenceGenerator(name="IMS_CLS_OP_DOCUMENT_BILL_ENTRY_KEY_GENERATOR", sequenceName = "SEQ_OP_DOC_BILL_ENTRY_KEY", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="IMS_CLS_OP_DOCUMENT_BILL_ENTRY_KEY_GENERATOR" ) 
	@Column(name="OP_DOC_BILL_ENTRY_KEY", updatable=false)
	private Long key;
	
	@OneToOne
	@JoinColumn(name="OP_HEALTH_CHECKUP_KEY", nullable=false)
	private OPHealthCheckup opHealthCheckup;
	
	@Column(name = "BILL_TYPE_ID")
	private Long billTypeId;
	
	@Column(name = "BILL_NUMBER")
	private String billNumber;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BILL_DATE")
	private Date billDate;
	
	@Column(name = "CLAIMED_AMOUNT")
	private Long claimedAmount;
	
	@Column(name = "NON_PAYABLE_AMOUNT")
	private Long nonPayableAmount;
	
	@Column(name = "NON_PAYABLE_REASON")
	private String nonPayableReason;
	
	@Column(name = "REMARKS")
	private String billEntryRemarks;
	
	@Column(name = "RECEIVED_STATUS")
	private String billReceivedStatus;
	
	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="FILE_TYPE_ID")
	private String fileTypeId;
	
	@Column(name="DOCUMENT_TOKEN")
	private String documentToken;
	
	@Column(name="DUDUCTIBLE_AMOUNT")
	private Long deductibleAmt;
	
	@Column(name="PAYABLE_AMOUNT")
	private Long payableAmt;
	
	@Column(name="FILE_NAME")
	private String fileName;
	
	
	@Override
	public Long getKey() {
		return this.key;
	}

	@Override
	public void setKey(Long key) {
		this.key = key;
	}

	public Long getBillTypeId() {
		return billTypeId;
	}

	public void setBillTypeId(Long billTypeId) {
		this.billTypeId = billTypeId;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public Long getClaimedAmount() {
		return claimedAmount;
	}

	public void setClaimedAmount(Long claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

	public Long getNonPayableAmount() {
		return nonPayableAmount;
	}

	public void setNonPayableAmount(Long nonPayableAmount) {
		this.nonPayableAmount = nonPayableAmount;
	}

	public String getNonPayableReason() {
		return nonPayableReason;
	}

	public void setNonPayableReason(String nonPayableReason) {
		this.nonPayableReason = nonPayableReason;
	}

	public OPHealthCheckup getOpHealthCheckup() {
		return opHealthCheckup;
	}

	public void setOpHealthCheckup(OPHealthCheckup opHealthCheckup) {
		this.opHealthCheckup = opHealthCheckup;
	}

	public String getBillEntryRemarks() {
		return billEntryRemarks;
	}

	public void setBillEntryRemarks(String billEntryRemarks) {
		this.billEntryRemarks = billEntryRemarks;
	}

	public String getBillReceivedStatus() {
		return billReceivedStatus;
	}

	public void setBillReceivedStatus(String billReceivedStatus) {
		this.billReceivedStatus = billReceivedStatus;
	}
	
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
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

	public String getFileTypeId() {
		return fileTypeId;
	}

	public void setFileTypeId(String fileTypeId) {
		this.fileTypeId = fileTypeId;
	}

	public String getDocumentToken() {
		return documentToken;
	}

	public void setDocumentToken(String documentToken) {
		this.documentToken = documentToken;
	}

	public Long getDeductibleAmt() {
		return deductibleAmt;
	}

	public void setDeductibleAmt(Long deductibleAmt) {
		this.deductibleAmt = deductibleAmt;
	}

	public Long getPayableAmt() {
		return payableAmt;
	}

	public void setPayableAmt(Long payableAmt) {
		this.payableAmt = payableAmt;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
