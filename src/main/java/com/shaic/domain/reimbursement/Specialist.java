package com.shaic.domain.reimbursement;

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

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.Claim;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Stage;

@Entity
@Table(name="IMS_CLS_SPECIALIST")
@NamedQueries({
@NamedQuery(name="Specialist.findAll", query="SELECT i FROM Specialist i"),
@NamedQuery(name="Specialist.findKey", query="SELECT o FROM Specialist o where o.key = :primaryKey"),
@NamedQuery(name="Specialist.findByClaimKey", query="SELECT o FROM Specialist o where o.claim.key = :claimKey order by o.key desc"),
@NamedQuery(name="Specialist.findByTransactionKey", query="SELECT o FROM Specialist o where o.transactionKey = :transactionKey order by o.key desc"),
@NamedQuery(name="Specialist.findCompletedTask", query="SELECT o FROM Specialist o where o.modifiedDate >= :fromDate and o.modifiedDate <= :toDate and o.status.key in (:statusKey) and lower(o.modifiedBy) = :modifiedBy order by o.key asc")
})
public class Specialist extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_CLS_SPECIALIST_KEY_GENERATOR", sequenceName = "SEQ_SPECIALIST_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_SPECIALIST_KEY_GENERATOR" ) 
	@Column(name="SPECIALIST_KEY")
	private Long key;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

//	@Column(name="CREATED_DATE")
//	private Timestamp createdDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;

	@OneToOne
	@JoinColumn(name="CLAIM_KEY", nullable=false)
	private Claim claim;
	
	@OneToOne
	@JoinColumn(name="SPECIALIST_TYPE_ID", nullable=false)
	private MastersValue spcialistType;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;

	@Column(name="SPECIALIST_REMARKS")
	private String specialistRemarks;
	
	@Column(name="REASON_FOR_REFERRING")
	private String reasonForReferring;
	
	@Column(name = "TRANSACTION_KEY", nullable = true)
	private Long transactionKey;
	
	@Column(nullable = true, columnDefinition = "VARCHAR(1)", name="TRANSACTION_FLAG", length=1)
	private String transactionFlag;
	
//	@Lob
//	@Column(name="FILE_UPLOAD")
//	private byte[] fileUpload;
	
	@Column(name="DOCUMENT_TOKEN")
	private String fileToken;
	
	@Column(name="FILE_NAME")
	private String fileName;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REQUESTED_DATE")
	private Date requestedDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DOCUMENT_RECEIVED_DATE")
	private Date documentReceivedDate;
	
	@OneToOne
	@JoinColumn(name="STATUS_ID",nullable=false)
	private Status status;
	
	@OneToOne
	@JoinColumn(name="STAGE_ID",nullable=false)
	private Stage stage;
	
	@Column(name = "OFFICE_CODE")
	private String officeCode;
	
	public Specialist() {
		
	}


	public Long getKey() {
		return key;
	}


	public void setKey(Long key) {
		this.key = key;
	}


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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


	public String getReasonForReferring() {
		return reasonForReferring;
	}


	public void setReasonForReferring(String reasonForReferring) {
		this.reasonForReferring = reasonForReferring;
	}


	public Long getActiveStatus() {
		return activeStatus;
	}


	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}


	public Claim getClaim() {
		return claim;
	}


	public void setClaim(Claim claim) {
		this.claim = claim;
	}


	public MastersValue getSpcialistType() {
		return spcialistType;
	}


	public void setSpcialistType(MastersValue spcialistType) {
		this.spcialistType = spcialistType;
	}


	public String getSpecialistRemarks() {
		return specialistRemarks;
	}


	public void setSpecialistRemarks(String specialistRemarks) {
		this.specialistRemarks = specialistRemarks;
	}


//	public byte[] getFileUpload() {
//		return fileUpload;
//	}
//
//
//	public void setFileUpload(byte[] fileUpload) {
//		this.fileUpload = fileUpload;
//	}


	public Date getRequestedDate() {
		return requestedDate;
	}


	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
	}


	public Date getDocumentReceivedDate() {
		return documentReceivedDate;
	}


	public void setDocumentReceivedDate(Date documentReceivedDate) {
		this.documentReceivedDate = documentReceivedDate;
	}


	public Status getStatus() {
		return status;
	}


	public void setStatus(Status status) {
		this.status = status;
	}


	public Stage getStage() {
		return stage;
	}


	public void setStage(Stage stage) {
		this.stage = stage;
	}


	public String getOfficeCode() {
		return officeCode;
	}


	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}


	public String getFileToken() {
		return fileToken;
	}


	public void setFileToken(String fileToken) {
		this.fileToken = fileToken;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public Long getTransactionKey() {
		return transactionKey;
	}


	public void setTransactionKey(Long transactionKey) {
		this.transactionKey = transactionKey;
	}


	public String getTransactionFlag() {
		return transactionFlag;
	}

	public void setTransactionFlag(String transactionFlag) {
		this.transactionFlag = transactionFlag;
	}


	public Date getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}
