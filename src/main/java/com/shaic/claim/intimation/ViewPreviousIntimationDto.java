package com.shaic.claim.intimation;

public class ViewPreviousIntimationDto {
	
	private Long key;
	
	private String intimationNo;
	
	private String policyNo;
	
	private String insuredPatientName;
	
	private String callerOrIntimatorName;
	
	private String contactNo;
	
	private String status;
	
	private String admittedDate;
	
	private String createdOn;
	
	private String modifiedOn;
	
	private String saveAndSubmittedOn;
	
	private String callerAddress;
	
	private String attenderContactNo;
	
	private String attenderMobileNo;
	
	private String applicationFlag;
	
	// For CRM ListIntimation WS
	private Long hospitalKey;
	private String hospitalCode;
    
	private String hospitalName;

	private String hospitalAddress;

	private String hospitalType;
	
	private String isdummy;
	
	public String getIntimatinoNo() {
		return intimationNo;
	}

	public void setIntimatinoNo(String intimatinoNo) {
		this.intimationNo = intimatinoNo;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getInsuredPatientName() {
		return insuredPatientName;
	}

	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}

	public String getCallerOrIntimatorName() {
		return callerOrIntimatorName;
	}

	public void setCallerOrIntimatorName(String callerOrIntimatorName) {
		this.callerOrIntimatorName = callerOrIntimatorName;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAdmittedDate() {
		return admittedDate;
	}

	public void setAdmittedDate(String admittedDate) {
		this.admittedDate = admittedDate;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getSaveAndSubmittedOn() {
		return saveAndSubmittedOn;
	}

	public void setSaveAndSubmittedOn(String saveAndSubmittedOn) {
		this.saveAndSubmittedOn = saveAndSubmittedOn;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getCallerAddress() {
		return callerAddress;
	}

	public void setCallerAddress(String callerAddress) {
		this.callerAddress = callerAddress;
	}

	public String getAttenderContactNo() {
		return attenderContactNo;
	}

	public void setAttenderContactNo(String attenderContactNo) {
		this.attenderContactNo = attenderContactNo;
	}

	public String getAttenderMobileNo() {
		return attenderMobileNo;
	}

	public void setAttenderMobileNo(String attenderMobileNo) {
		this.attenderMobileNo = attenderMobileNo;
	}

	public String getApplicationFlag() {
		return applicationFlag;
	}

	public void setApplicationFlag(String applicationFlag) {
		this.applicationFlag = applicationFlag;
	}

	public Long getHospitalKey() {
		return hospitalKey;
	}

	public void setHospitalKey(Long hospitalKey) {
		this.hospitalKey = hospitalKey;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	
	public String getHospitalName() {
		return hospitalName;
	}
	
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getHospitalAddress() {
		return hospitalAddress;
	}

	public void setHospitalAddress(String hospitalAddress) {
		this.hospitalAddress = hospitalAddress;
	}

	public String getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}

	public String getIsdummy() {
		return isdummy;
	}

	public void setIsdummy(String isdummy) {
		this.isdummy = isdummy;
	}

}
