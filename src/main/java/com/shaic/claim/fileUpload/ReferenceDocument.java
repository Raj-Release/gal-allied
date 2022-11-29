package com.shaic.claim.fileUpload;

import java.sql.Timestamp;
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
import javax.persistence.Transient;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.MastersValue;



@Entity
@Table(name="IMS_CLS_REFERENCE_DOCUMENT")
@NamedQueries({
@NamedQuery(name="ReferenceDocument.findAll", query="SELECT p FROM ReferenceDocument p"),
@NamedQuery(name="ReferenceDocument.findByKey", query="SELECT p FROM ReferenceDocument p where p.key = :docKey and p.deletedFlag is not null and p.deletedFlag = 'N'"),
@NamedQuery(name="ReferenceDocument.findByTransactionKey", query="SELECT p FROM ReferenceDocument p where p.transactionKey = :transactionKey and (p.deletedFlag is not null and p.deletedFlag = 'N')"),
@NamedQuery(name="ReferenceDocument.findByToken", query="SELECT p FROM ReferenceDocument p where p.documentToken=:documentToken")
//@NamedQuery(name="DocumentDetails.findByIntimationNo", query="SELECT p FROM DocumentDetails p where p.intimationNumber = :intimationNumber order by p.documentType asc")
})

public class ReferenceDocument extends AbstractEntity{
	
	@Id
	@SequenceGenerator(name="IMS_DOCUMENT_KEY_GENERATOR", sequenceName = "SEQ_REF_DOCUMENT_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_DOCUMENT_KEY_GENERATOR") 
	@Column(name="DOCUMENT_KEY")
	private Long key;
	
	@Column(name = "TRANSACTION_KEY", nullable = false)
	private Long transactionKey;
	
	@Column(name = "TRANSACTION_TYPE")
	private String transactionType;
	
	@Column(name = "DOCUMENT_TOKEN")
	private String documentToken;
	
	@Column(name = "FILE_NAME")
	private String fileName;
	
	@Column(name = "DELETED_FLAG")
	private String deletedFlag;
	
	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="FILE_TYPE_ID",  nullable=false)
	private MastersValue fileType;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPLOAD_LETTER_DATE")
	private Date letterDate;
	
	@Column(name = "NO_PAGES")
	private Integer noOfPages;
	
	@Transient
	private String fileUploadRemarks;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getTransactionKey() {
		return transactionKey;
	}

	public void setTransactionKey(Long transactionKey) {
		this.transactionKey = transactionKey;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getDocumentToken() {
		return documentToken;
	}

	public void setDocumentToken(String documentToken) {
		this.documentToken = documentToken;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
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

	public MastersValue getFileType() {
		return fileType;
	}

	public void setFileType(MastersValue fileType) {
		this.fileType = fileType;
	}

	public Integer getNoOfPages() {
		return noOfPages;
	}

	public void setNoOfPages(Integer noOfPages) {
		this.noOfPages = noOfPages;
	}

	public Date getLetterDate() {
		return letterDate;
	}

	public void setLetterDate(Date letterDate) {
		this.letterDate = letterDate;
	}

	public String getFileUploadRemarks() {
		return fileUploadRemarks;
	}

	public void setFileUploadRemarks(String fileUploadRemarks) {
		this.fileUploadRemarks = fileUploadRemarks;
	}
	

}
