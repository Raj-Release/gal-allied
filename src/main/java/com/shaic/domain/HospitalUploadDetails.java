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

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name="IMS_HP_UPLOAD_DTLS")
@NamedQueries({
	@NamedQuery(name="HospitalUploadDetails.findAll", query="SELECT o FROM HospitalUploadDetails o"),
	@NamedQuery(name="HospitalUploadDetails.findByKey", query="SELECT o FROM HospitalUploadDetails o WHERE o.key = :hpKey"),
	@NamedQuery(name="HospitalUploadDetails.findByIntNo", query="SELECT o FROM HospitalUploadDetails o WHERE o.intimationNo = :intNum")
})
public class HospitalUploadDetails extends AbstractEntity implements Serializable{

	private static final long serialVersionUID = 8920580017635485317L;

	@Id
	@SequenceGenerator(name="IMS_HP_UPLOAD_DTLS_GENERATOR", sequenceName = "SEQ_HP_UPLOAD_DOC_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_HP_UPLOAD_DTLS_GENERATOR") 
	@Column(name="HP_UPD_DOC_KEY")
	private Long key;
	
	@Column(name="INT_ID")
	private Long intimationKey;
	
	@Column(name="INT_NUMBER")
	private String intimationNo;
	
	@Column(name="PROVIDER_CODE")
	private String providerCode;
	
	@Column(name="CLAIMED_AMOUNT")
	private Long claimedAmount;
	
	@Column(name="HOSPITAL_NAME")
	private String hospitalName;
	
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

	public String getProviderCode() {
		return providerCode;
	}

	public void setProviderCode(String providerCode) {
		this.providerCode = providerCode;
	}

	public Long getClaimedAmount() {
		return claimedAmount;
	}

	public void setClaimedAmount(Long claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
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
