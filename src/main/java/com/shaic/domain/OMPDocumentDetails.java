package com.shaic.domain;

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

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name="IMS_CLS_OMP_DOCUMENT_DETAILS")
@NamedQueries({
@NamedQuery(name="OMPDocumentDetails.findAll", query="SELECT p FROM OMPDocumentDetails p"),
@NamedQuery(name="OMPDocumentDetails.findByKey" , query="SELECT p FROM OMPDocumentDetails p where p.documentKey = :key"),
@NamedQuery(name="OMPDocumentDetails.findByDocToken" , query="SELECT p FROM OMPDocumentDetails p where p.documentToken = :documentToken and (p.deletedFlag is null or p.deletedFlag = 'N')"),
//@NamedQuery(name="DocumentDetails.findByIntimationNo", query="SELECT p FROM DocumentDetails p where p.intimationNumber = :intimationNumber order by p.documentType asc")
@NamedQuery(name="OMPDocumentDetails.findByRodNo" , query="SELECT p FROM OMPDocumentDetails p where p.reimbursementNumber like :reimbursementNumber and (p.deletedFlag is null or p.deletedFlag = 'N') order by p.createdDate asc"),
@NamedQuery(name="OMPDocumentDetails.findByIntimationNo", query="SELECT p FROM OMPDocumentDetails p where p.intimationNumber = :intimationNumber order by p.docSubmittedDate asc"),
@NamedQuery(name="OMPDocumentDetails.findByIntimationNoOrderByCreatedDate", query="SELECT p FROM OMPDocumentDetails p where p.intimationNumber = :intimationNumber and (p.deletedFlag is null or p.deletedFlag = 'N') order by p.createdDate asc"),
@NamedQuery(name="OMPDocumentDetails.findByRejectionLetter", query="SELECT p FROM OMPDocumentDetails p where p.intimationNumber = :intNo and p.claimNumber = :claimNo and p.reimbursementNumber = :rodNo and p.documentType = :docType"),
@NamedQuery(name="OMPDocumentDetails.findByDocType", query="SELECT p FROM OMPDocumentDetails p where  p.documentType = :documentType and p.createdDate between :fromDate and :toDate"),
@NamedQuery(name="OMPDocumentDetails.findByAckNo" , query="SELECT p FROM OMPDocumentDetails p where p.acknowledgementNumber like :acknowledgeNumber and (p.deletedFlag is null or p.deletedFlag = 'N') order by p.createdDate asc"),
@NamedQuery(name="OMPDocumentDetails.findByAckNoDesc" , query="SELECT p FROM OMPDocumentDetails p where p.acknowledgementNumber like :acknowledgeNumber and (p.deletedFlag is null or p.deletedFlag = 'N') order by p.createdDate desc")
})
public class OMPDocumentDetails extends AbstractEntity{

	
	@Id
	@SequenceGenerator(name="IMS_OMP_DOCUMENT_DETAILS_KEY_GENERATOR", sequenceName = "SEQ_OMP_DOCUMENT_DETAILS_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_OMP_DOCUMENT_DETAILS_KEY_GENERATOR") 
	@Column(name="DOCUMENT_DETAILS_KEY")
	private Long documentKey;
	
	@Column(name = "INTIMATION_NUMBER")
	private String intimationNumber;
	
	@Column(name = "CLAIM_NUMBER")
	private String claimNumber;
	
	@Column(name = "REIMBURSEMENT_NUMBER")
	private String reimbursementNumber;
	
	@Column(name = "CASHLESS_NUMBER")
	private String cashlessNumber;
	
	@Column(name = "FILE_NAME")
	private String fileName;
	
	@Column(name = "DOCUMENT_TYPE")
	private String documentType;
	
	@Column(name = "DOCUMENT_URL")
	private String documentUrl;
	
	@Column(name = "DOCUMENT_TOKEN")
	private Long documentToken;
	
	@Column(name = "DOCUMENT_SOURCE")
	private String documentSource;
	
	@Column(name = "SF_APPLICATION_ID")
	private Long sfApplicationId;
	
	@Column(name = "SF_DOCUMENT_ID")
	private Long sfDocumentId;
	
	@Column(name = "SF_FILE_SIZE")
	private Long sfFileSize;
	
	@Column(name = "SF_FILE_NAME")
	private String sfFileName;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DOC_SUBMITTED_DATE")
	private Date docSubmittedDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DOC_ACKNOWLEDGEMENT_DATE")
	private Date docAcknowledgementDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "DELETED_FLAG")
	private String deletedFlag;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DOC_RECEIVED_DATE")
	private Date docRecievedDate;
	
	@Column(name = "REMARKS")
	private String remarks;
	
	@Column(name = "RECEIVED_STATUS")
	private String recievedStatus;
	
	@Column(name = "NO_OF_DOC")
	private Long noOfDoc;
	
	@Column(name = "FILE_TYPE")
	private String fileType;
	
	@Column(name = "ACKNOWLEDGEMENT_NUMBER")
	private String acknowledgementNumber;

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

	public String getReimbursementNumber() {
		return reimbursementNumber;
	}

	public void setReimbursementNumber(String reimbursementNumber) {
		this.reimbursementNumber = reimbursementNumber;
	}

	public String getCashlessNumber() {
		return cashlessNumber;
	}

	public void setCashlessNumber(String cashlessNumber) {
		this.cashlessNumber = cashlessNumber;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentUrl() {
		return documentUrl;
	}

	public void setDocumentUrl(String documentUrl) {
		this.documentUrl = documentUrl;
	}

	public Long getDocumentToken() {
		return documentToken;
	}

	public void setDocumentToken(Long documentToken) {
		this.documentToken = documentToken;
	}

	public String getDocumentSource() {
		return documentSource;
	}

	public void setDocumentSource(String documentSource) {
		this.documentSource = documentSource;
	}

	public Long getSfApplicationId() {
		return sfApplicationId;
	}

	public void setSfApplicationId(Long sfApplicationId) {
		this.sfApplicationId = sfApplicationId;
	}

	public Long getSfDocumentId() {
		return sfDocumentId;
	}

	public void setSfDocumentId(Long sfDocumentId) {
		this.sfDocumentId = sfDocumentId;
	}

	public Long getSfFileSize() {
		return sfFileSize;
	}

	public void setSfFileSize(Long sfFileSize) {
		this.sfFileSize = sfFileSize;
	}

	

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getDocSubmittedDate() {
		return docSubmittedDate;
	}

	public void setDocSubmittedDate(Date docSubmittedDate) {
		this.docSubmittedDate = docSubmittedDate;
	}

	public Date getDocAcknowledgementDate() {
		return docAcknowledgementDate;
	}

	public void setDocAcknowledgementDate(Date docAcknowledgementDate) {
		this.docAcknowledgementDate = docAcknowledgementDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getSfFileName() {
		return sfFileName;
	}

	public void setSfFileName(String sfFileName) {
		this.sfFileName = sfFileName;
	}
	
	
	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public Date getDocRecievedDate() {
		return docRecievedDate;
	}

	public void setDocRecievedDate(Date docRecievedDate) {
		this.docRecievedDate = docRecievedDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRecievedStatus() {
		return recievedStatus;
	}

	public void setRecievedStatus(String recievedStatus) {
		this.recievedStatus = recievedStatus;
	}

	public Long getNoOfDoc() {
		return noOfDoc;
	}

	public void setNoOfDoc(Long noOfDoc) {
		this.noOfDoc = noOfDoc;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Long getDocumentKey() {
		return documentKey;
	}

	public void setDocumentKey(Long documentKey) {
		this.documentKey = documentKey;
	}

	@Override
	public Long getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setKey(Long key) {
		// TODO Auto-generated method stub
		
	}

	public String getAcknowledgementNumber() {
		return acknowledgementNumber;
	}

	public void setAcknowledgementNumber(String acknowledgementNumber) {
		this.acknowledgementNumber = acknowledgementNumber;
	}

}
