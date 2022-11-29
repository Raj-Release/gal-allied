package com.shaic.domain.preauth;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.domain.MastersValue;


/**
 * The persistent class for the HMS_MAS_HOSPITAL_PACKAGE_T database table.
 * 
 */
@Entity
@Table(name="MAS_HOSPITAL_PACKAGE")
@NamedQueries({
	@NamedQuery(name="HospitalPackage.findAll", query="SELECT h FROM HospitalPackage h where h.activeStatus is not null and h.activeStatus = 1"),
	@NamedQuery(name="HospitalPackage.findByHospitalCode", query="SELECT h FROM HospitalPackage h where lower(h.hospitalCode) = lower(:hospitalCode)"),
	@NamedQuery(name="HospitalPackage.findByHospitalCodeAndProcedure", query="SELECT h FROM HospitalPackage h where h.procedureCode = :procedureCode and lower(h.hospitalCode) = lower(:hospitalCode)"),
	@NamedQuery(name="HospitalPackage.findByHospitalAndProcedure", query="SELECT h FROM HospitalPackage h where h.procedureCode = :procedureCode and lower(h.hospitalCode) = lower(:hosptialCode) and h.roomCategory.key = :roomCategoryId"),
	@NamedQuery(name="HospitalPackage.findByHospitalCodeAndProcedureCode", query="SELECT h FROM HospitalPackage h where h.procedureCode = :procedureCode and lower(h.hospitalCode) = lower(:hospitalCode)"),
	
})

public class HospitalPackage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PACKAGE_KEY")
	private Long key;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

//	@Column(name="ACTIVE_STATUS_DATE")
//	private Timestamp activeStatusDate;

	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;

	
	@Column(name="HOSPITAL_CODE", nullable=false)
	private String hospitalCode;
	
	@Column(name="PROCEDURE_CODE")
	private String procedureCode;
	

	@Column(name="MAX_DURATION_STAY",nullable=true)
	private Long maxDurationOfStay;

	@Column(name="MIN_DURATION_STAY",nullable=true)
	private Long minDurationOfStay;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;

	@Column(name = "PACKAGE_RATE")
	private Double rate;

	@Column(name = "REMARKS")
	private String remarks;

	@OneToOne
	@JoinColumn(name="ROOM_CATEGORY_ID", nullable=false)
	private MastersValue roomCategory;

//	@Column(name="VERSION")
//	private Long version;

	public HospitalPackage() {
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
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

	

	public Long getMaxDurationOfStay() {
		return maxDurationOfStay;
	}

	public void setMaxDurationOfStay(Long maxDurationOfStay) {
		this.maxDurationOfStay = maxDurationOfStay;
	}

//	public Long getMinDurationOfStay() {
//		return minDurationOfStay;
//	}
//
//	public void setMinDurationOfStay(Long minDurationOfStay) {
//		this.minDurationOfStay = minDurationOfStay;
//	}

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

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public MastersValue getRoomCategory() {
		return roomCategory;
	}

	public void setRoomCategory(MastersValue roomCategory) {
		this.roomCategory = roomCategory;
	}

//	public Long getVersion() {
//		return version;
//	}
//
//	public void setVersion(Long version) {
//		this.version = version;
//	}
	
	
	public Long getMinDurationOfStay() {
		return minDurationOfStay;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public void setMinDurationOfStay(Long minDurationOfStay) {
		this.minDurationOfStay = minDurationOfStay;
	}

	public String getProcedureCode() {
		return procedureCode;
	}

	public void setProcedureCode(String procedureCode) {
		this.procedureCode = procedureCode;
	}
}