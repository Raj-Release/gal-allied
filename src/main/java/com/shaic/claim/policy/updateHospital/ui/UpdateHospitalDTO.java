package com.shaic.claim.policy.updateHospital.ui;

import java.sql.Timestamp;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.domain.Hospitals;
import com.shaic.domain.TmpHospital;


public class UpdateHospitalDTO {

	private Long key;
	
	private Long activeStatus;
	
	private String hospitalCode;
	
	private String hospitalCodeIrda;
	
	private Timestamp activeStatusDate;
	
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
	
	private Long modeOfIntimationId;
	
	private String modifiedBy;
	
	private Timestamp modifiedDate;
	
	private String officeCode;
	
	private String phoneNumber;
	
	private String pincode;
	
	private Long remarks;
	
	private Long stateId;
	
	private String city;
	
	private String state;
	
	private Hospitals hospital;
	
	private TmpHospital hospitals;
	
	
	public UpdateHospitalDTO(HospitalDto hospitalDto){
		
		this.setHospitalName(hospitalDto.getName());
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

	public Long getRemarks() {
		return remarks;
	}

	public void setRemarks(Long remarks) {
		this.remarks = remarks;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getStateId() {
		return stateId;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public SelectValue getLocalityId() {
		return localityId;
	}

	public void setLocalityId(SelectValue localityId) {
		this.localityId = localityId;
	}

}
