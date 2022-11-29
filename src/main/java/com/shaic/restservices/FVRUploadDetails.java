package com.shaic.restservices;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name="IMS_CLS_HP_FVR_DOCUMENT")
public class FVRUploadDetails  extends AbstractEntity  implements Serializable {
	private static final long serialVersionUID = 1571868821786643417L;

	@Id
	@SequenceGenerator(name="IMS_CLS_HP_FVR_DOCUMENT_GENERATOR", sequenceName = "SEQ_HP_FVR_DOCUMENT_ID", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_HP_FVR_DOCUMENT_GENERATOR" ) 
	@Column(name="HP_FVR_DOC_KEY")
	private Long key;
	
	@Column(name="INT_ID")
	private Long intimationKey;
	
	@Column(name="INT_NUMBER")
	private String intimationNo;

	@Column(name="FVR_NO")
	private String fvrNo;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FVR_REPLIED_DATE")
	private Date fvrRepliedDate;
	
	@Column(name="FILE_TYPEID")
	private String fileTypeId;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="HP_DATE", updatable=false)
	private Date uploadDate;
	
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
	
	@Column(name="STARFAX_BATCH_STATUS")
	private String sfBatchStatus;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
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


	public String getFvrNo() {
		return fvrNo;
	}

	public void setFvrNo(String fvrNo) {
		this.fvrNo = fvrNo;
	}

	public Date getFvrRepliedDate() {
		return fvrRepliedDate;
	}

	public void setFvrRepliedDate(Date fvrRepliedDate) {
		this.fvrRepliedDate = fvrRepliedDate;
	}

	

	public String getFileTypeId() {
		return fileTypeId;
	}

	public void setFileTypeId(String fileTypeId) {
		this.fileTypeId = fileTypeId;
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

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getSfBatchStatus() {
		return sfBatchStatus;
	}

	public void setSfBatchStatus(String sfBatchStatus) {
		this.sfBatchStatus = sfBatchStatus;
	}
	
}
