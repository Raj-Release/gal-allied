package com.shaic.claim.reimbursement.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ZonalReviewUpdateHospitalDetailsDTO implements Serializable{
	private static final long serialVersionUID = -1574151313688684925L;

	private Long key; 
	
	private String hospitalName;
	
	private String hospitalPhoneNo;
	
	private String hopitalRegNumber;
	
	private String panNumber;
	
	@NotNull(message = "Please Enter No of Inpatient Beds")
	@Size(min = 1, message = "Please Enter No of Inpatient Beds")
	private String inpatientBeds;
	
	private Boolean otFacility;
	
	private Boolean icuFacility;
	
	private String otFacilityFlag;
	
	private String icuFacilityFlag;
	
	private String hospitalCode;
	
	private String hospitalAddress1;
	
	private String hospitalAddress2;
	
	private String hospitalAddress3;
	
	private String hospitalCity;
	
	private Long hospitalCityId;
	
	private String hospitalState;
	
	private Long hospitalStateId;
	
	private String hospitalPincode;
	
	private Long hospitalTypeId;
	
	private Long hospitalId;

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getHospitalPhoneNo() {
		return hospitalPhoneNo;
	}

	public void setHospitalPhoneNo(String hospitalPhoneNo) {
		this.hospitalPhoneNo = hospitalPhoneNo;
	}

	public String getHopitalRegNumber() {
		return hopitalRegNumber;
	}

	public void setHopitalRegNumber(String hopitalRegNumber) {
		this.hopitalRegNumber = hopitalRegNumber;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getInpatientBeds() {
		return inpatientBeds;
	}

	public void setInpatientBeds(String inpatientBeds) {
		this.inpatientBeds = inpatientBeds;
	}

	public Boolean getOtFacility() {
		return otFacility;
	}

	public void setOtFacility(Boolean otFacility) {
		this.otFacility = otFacility;
		this.otFacilityFlag = (this.otFacility != null && this.otFacility)  ? "Y" : "N";
	}

	public Boolean getIcuFacility() {
		return icuFacility;
	}

	public void setIcuFacility(Boolean icuFacility) {
		this.icuFacility = icuFacility;
		this.icuFacilityFlag = (this.icuFacility != null && this.icuFacility)  ? "Y" : "N";
	}

	public String getOtFacilityFlag() {
		return otFacilityFlag;
	}

	public void setOtFacilityFlag(String otFacilityFlag) {
		this.otFacilityFlag = otFacilityFlag;
		if(this.otFacilityFlag != null) {
			this.otFacility = this.otFacilityFlag.toLowerCase().equalsIgnoreCase("y") ? true : false;
		}
	}

	public String getIcuFacilityFlag() {
		return icuFacilityFlag;
	}

	public void setIcuFacilityFlag(String icuFacilityFlag) {
		this.icuFacilityFlag = icuFacilityFlag;
		if(this.icuFacilityFlag != null) {
			this.icuFacility = this.icuFacilityFlag.toLowerCase().equalsIgnoreCase("y") ? true : false;
		}
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getHospitalAddress1() {
		return hospitalAddress1;
	}

	public void setHospitalAddress1(String hospitalAddress1) {
		this.hospitalAddress1 = hospitalAddress1;
	}

	public String getHospitalAddress2() {
		return hospitalAddress2;
	}

	public void setHospitalAddress2(String hospitalAddress2) {
		this.hospitalAddress2 = hospitalAddress2;
	}

	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getHospitalAddress3() {
		return hospitalAddress3;
	}

	public void setHospitalAddress3(String hospitalAddress3) {
		this.hospitalAddress3 = hospitalAddress3;
	}

	public String getHospitalCity() {
		return hospitalCity;
	}

	public void setHospitalCity(String hospitalCity) {
		this.hospitalCity = hospitalCity;
	}

	public String getHospitalState() {
		return hospitalState;
	}

	public void setHospitalState(String hospitalState) {
		this.hospitalState = hospitalState;
	}

	public String getHospitalPincode() {
		return hospitalPincode;
	}

	public void setHospitalPincode(String hospitalPincode) {
		this.hospitalPincode = hospitalPincode;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getHospitalCityId() {
		return hospitalCityId;
	}

	public void setHospitalCityId(Long hospitalCityId) {
		this.hospitalCityId = hospitalCityId;
	}

	public Long getHospitalStateId() {
		return hospitalStateId;
	}

	public void setHospitalStateId(Long hospitalStateId) {
		this.hospitalStateId = hospitalStateId;
	}

	public Long getHospitalTypeId() {
		return hospitalTypeId;
	}

	public void setHospitalTypeId(Long hospitalTypeId) {
		this.hospitalTypeId = hospitalTypeId;
	}
}
