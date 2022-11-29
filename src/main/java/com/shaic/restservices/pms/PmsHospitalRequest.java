package com.shaic.restservices.pms;

import org.codehaus.jackson.annotate.JsonProperty;

public class PmsHospitalRequest {
	
	@JsonProperty("{")
	private String startToken;
	
	//private String bedCount;
	@JsonProperty("HospitalName")
	private String hospitalName;
	
	@JsonProperty("HospitalAddress")
	private String hospitalAddress;
	
	@JsonProperty("HospitalPinCode")
	private String hospitalPincode;
	
	@JsonProperty("HospitalCity")
	private String hospitalCity;
	
	@JsonProperty("HospitalState")
	private String hospitalState;
	
	//private String hospitalIrdaCode;
	@JsonProperty("HospitalTelephone")
	private String hospitalTelephone;
	
	@JsonProperty("HospitalFaxNo")
	private String hospitalFaxNo;
	
	@JsonProperty("IntimationNo")
	private String intimationNo;
	
	@JsonProperty("IntimationId")
	private String intimationId;
	
	@JsonProperty("HospitalFlag")
	private String hospitalFlag;
	
	@JsonProperty("HospitalCreatedBy")
	private String hospitalCreatedBy;
	
	@JsonProperty("HospitalCreatedOn")
	private String hospitalCreatedOn;
	
	@JsonProperty("HospitalProviderCode")
	private String hospitalProviderCode;
	
	@JsonProperty("CreatedUserName")
	private String createdUserName;
	
	@JsonProperty("}")
	private String endToken;
	
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
	public String getHospitalPincode() {
		return hospitalPincode;
	}
	public void setHospitalPincode(String hospitalPincode) {
		this.hospitalPincode = hospitalPincode;
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
	public String getHospitalTelephone() {
		return hospitalTelephone;
	}
	public void setHospitalTelephone(String hospitalTelephone) {
		this.hospitalTelephone = hospitalTelephone;
	}
	public String getHospitalFaxNo() {
		return hospitalFaxNo;
	}
	public void setHospitalFaxNo(String hospitalFaxNo) {
		this.hospitalFaxNo = hospitalFaxNo;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getIntimationId() {
		return intimationId;
	}
	public void setIntimationId(String intimationId) {
		this.intimationId = intimationId;
	}
	public String getHospitalFlag() {
		return hospitalFlag;
	}
	public void setHospitalFlag(String hospitalFlag) {
		this.hospitalFlag = hospitalFlag;
	}
	public String getHospitalCreatedBy() {
		return hospitalCreatedBy;
	}
	public void setHospitalCreatedBy(String hospitalCreatedBy) {
		this.hospitalCreatedBy = hospitalCreatedBy;
	}
	public String getHospitalCreatedOn() {
		return hospitalCreatedOn;
	}
	public void setHospitalCreatedOn(String hospitalCreatedOn) {
		this.hospitalCreatedOn = hospitalCreatedOn;
	}
	public String getHospitalProviderCode() {
		return hospitalProviderCode;
	}
	public void setHospitalProviderCode(String hospitalProviderCode) {
		this.hospitalProviderCode = hospitalProviderCode;
	}
	public String getCreatedUserName() {
		return createdUserName;
	}
	public void setCreatedUserName(String createdUserName) {
		this.createdUserName = createdUserName;
	}
	@Override
	public String toString() {
		return "PmsHospitalRequest [hospitalName=" + hospitalName
				+ ", hospitalAddress=" + hospitalAddress + ", hospitalPincode="
				+ hospitalPincode + ", hospitalCity=" + hospitalCity
				+ ", hospitalState=" + hospitalState + ", hospitalTelephone="
				+ hospitalTelephone + ", hospitalFaxNo=" + hospitalFaxNo
				+ ", intimationNo=" + intimationNo + ", intimationId="
				+ intimationId + ", hospitalFlag=" + hospitalFlag
				+ ", hospitalCreatedBy=" + hospitalCreatedBy
				+ ", hospitalCreatedOn=" + hospitalCreatedOn
				+ ", hospitalProviderCode=" + hospitalProviderCode
				+ ", createdUserName=" + createdUserName + "]";
	}

}
