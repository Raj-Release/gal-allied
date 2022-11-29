package com.shaic.claim.OMPBulkUploadRejection;

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

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name = "IMS_CLS_OMP_REJ_BULK_UPLOAD")
@NamedQueries({
	@NamedQuery(name = "OmpRejectionBulkUpload.findAll", query = "SELECT i FROM OmpRejectionBulkUpload i"),
	@NamedQuery(name="OmpRejectionBulkUpload.findByRodNumber", query = "SELECT r FROM OmpRejectionBulkUpload r where r.rodNumber = :rodNumber and r.uploadType = :uploadType"),
	@NamedQuery(name="OmpRejectionBulkUpload.findByPodNumber", query = "SELECT r FROM OmpRejectionBulkUpload r where r.podNumber = :podNumber and r.uploadType = :uploadType"),
	@NamedQuery(name="OmpRejectionBulkUpload.findByPodAndRodNumber", query = "SELECT r FROM OmpRejectionBulkUpload r where r.podNumber = :podNumber and r.rodNumber = :rodNumber and r.uploadType = :uploadType"),

	@NamedQuery(name="OmpRejectionBulkUpload.findByRodNumberWithDate", query = "SELECT r FROM OmpRejectionBulkUpload r where r.rodNumber = :rodNumber and (r.dateOfDispatch BETWEEN :fromDate AND :toDate) and r.uploadType = :uploadType"),
	@NamedQuery(name="OmpRejectionBulkUpload.findByPodNumberWithDate", query = "SELECT r FROM OmpRejectionBulkUpload r where r.podNumber = :podNumber and (r.dateOfDispatch BETWEEN :fromDate AND :toDate) and r.uploadType = :uploadType"),
	@NamedQuery(name="OmpRejectionBulkUpload.findByPodAndRodNumberWithDate", query = "SELECT r FROM OmpRejectionBulkUpload r where r.podNumber = :podNumber and r.rodNumber = :rodNumber and (r.dateOfDispatch BETWEEN :fromDate AND :toDate) and r.uploadType = :uploadType"),
	@NamedQuery(name="OmpRejectionBulkUpload.findByDate", query = "SELECT r FROM OmpRejectionBulkUpload r where (r.dateOfDispatch BETWEEN :fromDate AND :toDate) and r.uploadType = :uploadType" ),

	@NamedQuery(name="OmpRejectionBulkUpload.findByUploadType", query = "SELECT r FROM OmpRejectionBulkUpload r where r.uploadType = :uploadType"),

})

public class OmpRejectionBulkUpload extends AbstractEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_CLS_OMP_REJ_BULK_UPLOAD_GENERATOR", sequenceName = "SEQ_OMP_REJECTION_BULK_UPLOAD", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_OMP_REJ_BULK_UPLOAD_GENERATOR" ) 
	@Column(name="OMP_REJECTION_BULK_UPLOAD_KEY", updatable=false)
	private Long key;

	@Column(name = "ROD_NUMBER")
	private String rodNumber;
	
	@Column(name = "DATE_OF_DISPATCH")
	private Date dateOfDispatch;

	@Column(name = "POD_NUMBER")
	private String podNumber;

	@Column(name = "MODE_OF_DISPATCH")
	private String modeOfDispatch;

	@Column(name = "REMARKS")
	private String remarks;

	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name= "ACTIVE_STATUS")
	private int activeStatus;
	
	@Column(name = "UPLOAD_TYPE")
	private String uploadType;

	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	public String getRodNumber() {
		return rodNumber;
	}
	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
	}
	public Date getDateOfDispatch() {
		return dateOfDispatch;
	}
	public void setDateOfDispatch(Date dateOfDispatch) {
		this.dateOfDispatch = dateOfDispatch;
	}
	public String getPodNumber() {
		return podNumber;
	}
	public void setPodNumber(String podNumber) {
		this.podNumber = podNumber;
	}
	public String getModeOfDispatch() {
		return modeOfDispatch;
	}
	public void setModeOfDispatch(String modeOfDispatch) {
		this.modeOfDispatch = modeOfDispatch;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Timestamp getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public int getActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
	}
	public String getUploadType() {
		return uploadType;
	}
	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}
}
