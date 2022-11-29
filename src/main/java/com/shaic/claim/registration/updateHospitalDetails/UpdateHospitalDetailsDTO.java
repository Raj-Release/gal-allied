package com.shaic.claim.registration.updateHospitalDetails;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.TmpHospital;

public class UpdateHospitalDetailsDTO {
	
	
    private Long key;
	
	private Long activeStatus;
	
	private Intimation intimation;
	
	@NotNull(message="Please Select Mode of Intimation")
	private SelectValue modeOfIntimation;
	
	
	@NotNull(message="Please Select IntimatedBy")
	private SelectValue intimatedBy;
	
	private String hospitalCode;
	
	private String hospitalTypeValue;
	
	private String hospitalCodeIrda;
	
	private Timestamp activeStatusDate;
	
	@NotNull(message="Please Select Hospital Type")
	private SelectValue hospitalType;
	
	private String address;
	
	private String createdBy;
	
	private SelectValue localityId;
	
	private Timestamp createdDate;
	
	private String emailId;
	
	private String faxNumber;
	
	private Long hospitalId;
	
	private String hospitalName;
	
	private Long hospitalTypeId;
	
	private Long intimatedById;
	
	private Long cityId;
	
	private String mobileNumber;
	
	private String contactNo;
	
	private Long modeOfIntimationId;
	
	private String modifiedBy;
	
	private Timestamp modifiedDate;
	
	private String officeCode;
	
	private String phoneNumber;
	
	private String pincode;
	
	private String remarks;
	
	private Long stateId;
	
	@NotNull(message="Please Select City")
	private SelectValue city;
	
	@NotNull(message="Please Select state")
	private SelectValue state;
	
	private Hospitals hospital;
	
	@NotNull(message="Please Select Area")
	private SelectValue area;
	
	private TmpHospital hospitals;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public SelectValue getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(SelectValue hospitalType) {
		this.hospitalType = hospitalType;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getHospitalCodeIrda() {
		return hospitalCodeIrda;
	}

	public void setHospitalCodeIrda(String hospitalCodeIrda) {
		this.hospitalCodeIrda = hospitalCodeIrda;
	}

	public Timestamp getActiveStatusDate() {
		return activeStatusDate;
	}

	public void setActiveStatusDate(Timestamp activeStatusDate) {
		this.activeStatusDate = activeStatusDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public SelectValue getLocalityId() {
		return localityId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public void setLocalityId(SelectValue localityId) {
		this.localityId = localityId;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public Long getHospitalTypeId() {
		return hospitalTypeId;
	}

	public void setHospitalTypeId(Long hospitalTypeId) {
		this.hospitalTypeId = hospitalTypeId;
	}

	public Long getIntimatedById() {
		return intimatedById;
	}

	public void setIntimatedById(Long intimatedById) {
		this.intimatedById = intimatedById;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Long getModeOfIntimationId() {
		return modeOfIntimationId;
	}

	public void setModeOfIntimationId(Long modeOfIntimationId) {
		this.modeOfIntimationId = modeOfIntimationId;
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

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public Long getStateId() {
		return stateId;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

	public Hospitals getHospital() {
		return hospital;
	}

	public void setHospital(Hospitals hospital) {
		this.hospital = hospital;
	}

	public TmpHospital getHospitals() {
		return hospitals;
	}

	public void setHospitals(TmpHospital hospitals) {
		this.hospitals = hospitals;
	}

	public SelectValue getModeOfIntimation() {
		return modeOfIntimation;
	}

	public void setModeOfIntimation(SelectValue modeOfIntimation) {
		this.modeOfIntimation = modeOfIntimation;
	}

	public SelectValue getIntimatedBy() {
		return intimatedBy;
	}

	public void setIntimatedBy(SelectValue intimatedBy) {
		this.intimatedBy = intimatedBy;
	}

	public SelectValue getCity() {
		return city;
	}

	public void setCity(SelectValue city) {
		this.city = city;
	}

	public SelectValue getState() {
		return state;
	}

	public void setState(SelectValue state) {
		this.state = state;
	}

	
	public String getHospitalTypeValue() {
		return hospitalTypeValue;
	}

	public SelectValue getArea() {
		return area;
	}

	public void setArea(SelectValue area) {
		this.area = area;
	}

	public void setHospitalTypeValue(String hospitalTypeValue) {
		this.hospitalTypeValue = hospitalTypeValue;
	}

	public Intimation getIntimation() {
		return intimation;
	}

	public void setIntimation(Intimation intimation) {
		this.intimation = intimation;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	
	

}
