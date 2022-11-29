package com.shaic.domain.preauth;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * The persistent class for the HMS_MAS_HOSPITAL_PACKAGE_T database table.
 * 
 */
@Entity
@Table(name="HMS_MAS_HOSPITAL_PACKAGE")
@NamedQuery(name="HmsHospitalPackage.findAll", query="SELECT h FROM HmsHospitalPackage h")
public class HmsHospitalPackage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="KEY")
	private Long key;

	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

	@Column(name="ACTIVE_STATUS_DATE")
	private Timestamp activeStatusDate;

//	@Column(name="CREATED_BY")
//	private String createdBy;

//	@Column(name="CREATED_DATE")
//	private Timestamp createdDate;

	@Column(name="FK_HOSPITAL_KEY")
	private Long fkHospitalKey;

	@OneToOne
	@JoinColumn(name="FK_PROCEDURE_KEY", nullable=false)
	private ProcedureMaster procedureMaster;
	
	@Column(name="MAX_DURATION_OF_STAY")
	private Long maxDurationOfStay;

	@Column(name="MIN_DURATION_OF_STAY")
	private Long minDurationOfStay;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;

	private Long rate;

	private String remarks;

	@Column(name="ROOM_CATEGORY_ID")
	private Long roomCategoryId;

	@Column(name="VERSION")
	private Long version;

	public HmsHospitalPackage() {
	}

	public Long getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Timestamp getActiveStatusDate() {
		return this.activeStatusDate;
	}

	public void setActiveStatusDate(Timestamp activeStatusDate) {
		this.activeStatusDate = activeStatusDate;
	}

//	public String getCreatedBy() {
//		return this.createdBy;
//	}

//	public void setCreatedBy(String createdBy) {
//		this.createdBy = createdBy;
//	}

//	public Timestamp getCreatedDate() {
//		return this.createdDate;
//	}

//	public void setCreatedDate(Timestamp createdDate) {
//		this.createdDate = createdDate;
//	}

	public Long getFkHospitalKey() {
		return this.fkHospitalKey;
	}

	public void setFkHospitalKey(Long fkHospitalKey) {
		this.fkHospitalKey = fkHospitalKey;
	}

	public ProcedureMaster getFkProcedureKey() {
		return this.procedureMaster;
	}

	public void setFkProcedureKey(ProcedureMaster procedureMaster) {
		this.procedureMaster = procedureMaster;
	}

	public Long getKey() {
		return this.key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getMaxDurationOfStay() {
		return this.maxDurationOfStay;
	}

	public void setMaxDurationOfStay(Long maxDurationOfStay) {
		this.maxDurationOfStay = maxDurationOfStay;
	}

	public Long getMinDurationOfStay() {
		return this.minDurationOfStay;
	}

	public void setMinDurationOfStay(Long minDurationOfStay) {
		this.minDurationOfStay = minDurationOfStay;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Long getRate() {
		return this.rate;
	}

	public void setRate(Long rate) {
		this.rate = rate;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getRoomCategoryId() {
		return this.roomCategoryId;
	}

	public void setRoomCategoryId(Long roomCategoryId) {
		this.roomCategoryId = roomCategoryId;
	}

	public Long getVersion() {
		return this.version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}