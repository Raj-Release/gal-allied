package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name="IMS_HP_UPLOAD_DOC_DTLS")
@NamedQueries({
	@NamedQuery(name="HospitalUploadDocumentDetails.findAll", query="SELECT o FROM HospitalUploadDocumentDetails o")
})
public class HospitalUploadDocumentDetails extends AbstractEntity implements Serializable{
	
	private static final long serialVersionUID = -7003073221795381756L;

	@Id
	@SequenceGenerator(name="IMS_HP_UPLOAD_DOC_DTLS_GENERATOR", sequenceName = "SEQ_HP_UPD_DOC_DTLS_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_HP_UPLOAD_DOC_DTLS_GENERATOR") 
	@Column(name="HP_UPD_DOC_DTLS_KEY")
	private Long key;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="HP_UPD_DOC_KEY", nullable=false)
	private HospitalUploadDetails hospitalUploadDetails;
	
	@Column(name="DOC_ID")
	private Long docId;
	
	@Column(name="INT_ID")
	private Long intimationKey;
	
	@Column(name="INT_NUMBER")
	private String intimationNo;
	
	@Column(name="FILE_TYPEID")
	private Long fileTypeId;
	
	@Column(name="FILETYPE_NAME")
	private String fileTypeName;
	
	/*@Column(name="FILE_CONTENT")
	private String fileContent;*/
	
	@Column(name="UPLOAD_SOURCE")
	private String uploadSource;
	
	@Column(name="FILE_SERVER")
	private String fileServer;
	
	@Column(name="FILE_KEY")
	private Long fileKey;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE", updatable=false)
	private Date createdDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name = "ACTIVE_STATUS")
	private Long activeStatus;
	
	@Column(name = "PROCESSED_YN")
	private String processedFlag;
	
	@Column(name = "FILE_NAME")
	private String fileName;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public HospitalUploadDetails getHospitalUploadDetails() {
		return hospitalUploadDetails;
	}

	public void setHospitalUploadDetails(HospitalUploadDetails hospitalUploadDetails) {
		this.hospitalUploadDetails = hospitalUploadDetails;
	}

	public Long getDocId() {
		return docId;
	}

	public void setDocId(Long docId) {
		this.docId = docId;
	}

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public Long getFileTypeId() {
		return fileTypeId;
	}

	public void setFileTypeId(Long fileTypeId) {
		this.fileTypeId = fileTypeId;
	}

	public String getFileTypeName() {
		return fileTypeName;
	}

	public void setFileTypeName(String fileTypeName) {
		this.fileTypeName = fileTypeName;
	}

	/*public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}*/

	public String getUploadSource() {
		return uploadSource;
	}

	public void setUploadSource(String uploadSource) {
		this.uploadSource = uploadSource;
	}

	public String getFileServer() {
		return fileServer;
	}

	public void setFileServer(String fileServer) {
		this.fileServer = fileServer;
	}

	public Long getFileKey() {
		return fileKey;
	}

	public void setFileKey(Long fileKey) {
		this.fileKey = fileKey;
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

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getProcessedFlag() {
		return processedFlag;
	}

	public void setProcessedFlag(String processedFlag) {
		this.processedFlag = processedFlag;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
