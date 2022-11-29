package com.shaic.domain;

import java.sql.Timestamp;

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

import com.shaic.arch.fields.dto.AbstractEntity;

/**
 * The persistent class for the IMS_TMP_UPDATE_HOSPITAL_DTLS_T database table.
 * 
 */
@Entity
@Table(name="IMS_TMP_UPDATE_HOSPITAL_DTLS")
@NamedQueries({
	@NamedQuery(name="UpdateHospital.findAll", query="SELECT i FROM UpdateHospital i"),
	@NamedQuery(name="UpdateHospital.findByReimbursementKey", query="SELECT r FROM UpdateHospital r WHERE r.reimbursement is not null and r.reimbursement.key = :reimbursementKey"),
	@NamedQuery(name="UpdateHospital.findByKey",query="SELECT r FROM UpdateHospital r WHERE r.key = :primaryKey")
})
public class UpdateHospital extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_TMP_UPDATE_HOSPITAL_DTLS_KEY_GENERATOR", sequenceName = "SEQ_UPDATE_HOSPITAL_DTLS_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_TMP_UPDATE_HOSPITAL_DTLS_KEY_GENERATOR" ) 
	@Column(name="HOSPITAL_UPDATE_KEY")
	private Long key;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

//	@Column(name="ACTIVE_STATUS_DATE")
//	private Timestamp activeStatusDate;

	private String address;

	@Column(name="CITY_ID")
	private Long cityId;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="EMAIL_ID")
	private String emailId;

	@Column(name="FAX_NUMBER")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String faxNumber;

	@Column(name="HOSPITAL_ID")
	private Long hospitalId;

	@Column(name="HOSPITAL_NAME")
	private String hospitalName;

	@Column(name="HOSPITAL_TYPE_ID")
	private Long hospitalTypeId;

	@Column(name="INTIMATED_BY_ID")
	private Long intimatedById;

	@Column(name="LOCALITY_ID")
	private Long localityId;

	@Column(name="MOBILE_NUMBER")
	private String mobileNumber;

	@Column(name="MODE_OF_INTIMATION_ID")
	private Long modeOfIntimationId;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;

	@Column(name="OFFICE_CODE")
	private String officeCode;

	@Column(name="PHONE_NUMBER")
	private String phoneNumber;

	private Long pincode;

	private String remarks;

	@Column(name="STATE_ID")
	private Long stateId;
	
	@Column(name="REGISTRATION_NUMBER")
	private String registrationNumber;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="TRANSACTION_KEY", nullable=false)
	private Reimbursement reimbursement;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name="OT_FACILITIES_AVAILABLE_FLAG", length=1)
	private String otFacilityFlag;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name="ICU_FACILITIES_AVAILABLE_FLAG", length=1)
	private String icuFacilityFlag;
	
	@Column(name = "NUMBER_OF_INPATIENT_BEDS")
	private Integer inpatientBeds;
	
	public UpdateHospital() {
	}

	public Long getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}
	

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getCityId() {
		return this.cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getEmailId() {
		return this.emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}


	public Long getHospitalId() {
		return this.hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getHospitalName() {
		return this.hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public Long getHospitalTypeId() {
		return this.hospitalTypeId;
	}

	public void setHospitalTypeId(Long hospitalTypeId) {
		this.hospitalTypeId = hospitalTypeId;
	}

	public Long getIntimatedById() {
		return this.intimatedById;
	}

	public void setIntimatedById(Long intimatedById) {
		this.intimatedById = intimatedById;
	}

	public Long getKey() {
		return this.key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getLocalityId() {
		return this.localityId;
	}

	public Reimbursement getReimbursement() {
		return reimbursement;
	}

	public void setReimbursement(Reimbursement reimbursement) {
		this.reimbursement = reimbursement;
	}

	public void setLocalityId(Long localityId) {
		this.localityId = localityId;
	}


	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getOtFacilityFlag() {
		return otFacilityFlag;
	}

	public void setOtFacilityFlag(String otFacilityFlag) {
		this.otFacilityFlag = otFacilityFlag;
	}

	public String getIcuFacilityFlag() {
		return icuFacilityFlag;
	}

	public void setIcuFacilityFlag(String icuFacilityFlag) {
		this.icuFacilityFlag = icuFacilityFlag;
	}

	public Integer getInpatientBeds() {
		return inpatientBeds;
	}

	public void setInpatientBeds(Integer inpatientBeds) {
		this.inpatientBeds = inpatientBeds;
	}

	public Long getModeOfIntimationId() {
		return this.modeOfIntimationId;
	}

	public void setModeOfIntimationId(Long modeOfIntimationId) {
		this.modeOfIntimationId = modeOfIntimationId;
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

	public String getOfficeCode() {
		return this.officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}
	
	public Long getPincode() {
		return this.pincode;
	}

	public void setPincode(Long pincode) {
		this.pincode = pincode;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getStateId() {
		return this.stateId;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

}