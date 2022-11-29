package com.shaic.domain.fss;

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

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name="IMS_CLS_WRH_TRN_FILE_STORAGE")
@NamedQueries({
	@NamedQuery(name="FileStorage.findByStorageKey", query="SELECT m FROM FileStorage m where m.key = :key and m.activateStatus is not null and m.activateStatus = 'Y'")
})

public class FileStorage extends AbstractEntity {
	
	@Id
	@SequenceGenerator(name="IMS_CLS_WRH_TRN_FILE_STORAGE_KEY_GENERATOR", sequenceName = "SEQ_FILE_STORAGE_KEY" , allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_WRH_TRN_FILE_STORAGE_KEY_GENERATOR" )
	@Column(name="FILE_STORAGE_KEY")
	private Long key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CLIENT_KEY", nullable=true)
	private MasClient client;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STORAGE_KEY", nullable=true)
	private MasStorageLocation storage;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="RACK_KEY", nullable=true)
	private MasRack rack;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="SHELF_KEY", nullable=true)
	private MasShelf shelf;
	
	@Column(name="CLAIM_NUMBER")
	private String claimId;
	
	@Column(name="YEAR")
	private Integer year;
	
	@Column(name="PATIENT_NAME")
	private String patientName;
	
	@Column(name="ALMIRAH_NO")
	private String almirahNo;
	
	@Column(name="ADDITIONAL_SHELF_NO")
	private Long addlShelfNo;
	
	@Column(name="IN_OUT_FLAG")
	private String inOutFlag;
	
	@Column(name="CLAIM_REJECTED_FLAG")
	private String rejectFlag;
	
	@Column(name="REMARKS")
	private String remarks;

	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="ACTIVE_STATUS")
	private String activateStatus;
	
	@Column(name="BUNDLE_NUMBER")
	private String bundleNo;
	
	public MasClient getClient() {
		return client;
	}

	public void setClient(MasClient client) {
		this.client = client;
	}

	public MasStorageLocation getStorage() {
		return storage;
	}

	public void setStorage(MasStorageLocation storage) {
		this.storage = storage;
	}

	public MasRack getRack() {
		return rack;
	}

	public void setRack(MasRack rack) {
		this.rack = rack;
	}

	public MasShelf getShelf() {
		return shelf;
	}

	public void setShelf(MasShelf shelf) {
		this.shelf = shelf;
	}

	public String getClaimId() {
		return claimId;
	}

	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getAlmirahNo() {
		return almirahNo;
	}

	public void setAlmirahNo(String almirahNo) {
		this.almirahNo = almirahNo;
	}

	public String getInOutFlag() {
		return inOutFlag;
	}

	public void setInOutFlag(String inOutFlag) {
		this.inOutFlag = inOutFlag;
	}

	public String getRejectFlag() {
		return rejectFlag;
	}

	public void setRejectFlag(String rejectFlag) {
		this.rejectFlag = rejectFlag;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public String getActivateStatus() {
		return activateStatus;
	}

	public void setActivateStatus(String activateStatus) {
		this.activateStatus = activateStatus;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getAddlShelfNo() {
		return addlShelfNo;
	}

	public void setAddlShelfNo(Long addlShelfNo) {
		this.addlShelfNo = addlShelfNo;
	}

	public String getBundleNo() {
		return bundleNo;
	}

	public void setBundleNo(String bundleNo) {
		this.bundleNo = bundleNo;
	}
	
	

}
