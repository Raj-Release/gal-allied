package com.shaic.claim.policy.search.ui;

import org.codehaus.jackson.annotate.JsonProperty;

public class InsertHospitalToPMS {
	
	/*@JsonProperty("{")
	private String startToken;*/
	
	//private String bedCount;
	//@JsonProperty("hospBedCount")
	private String hospBedCount;
	
	//@JsonProperty("hospName")
	private String hospName;
	
	//@JsonProperty("hospitalAddress")
	private String hospitalAddress;
	
	//@JsonProperty("hospitalPincode")
	private String hospitalPincode;
	
	//@JsonProperty("hospitalCity")
	private String hospitalCity;
	
	//@JsonProperty("hospitalState")
	private String hospitalState;
	
	//@JsonProperty("hospitalIrdaCode")
	private String hospitalIrdaCode;
	
	//@JsonProperty("hospitalTelephoneStdno")
	private String hospitalTelephoneStdno;
	
	//@JsonProperty("hospitalTelephoneNo")
	private String hospitalTelephoneNo;
	
	//@JsonProperty("hospitalFaxStdno")
	private String hospitalFaxStdno;
	
	//@JsonProperty("hospitalFaxNo")
	private String hospitalFaxNo;
	
	//@JsonProperty("hospitalIntimationNo")
	private String hospitalIntimationNo;
	
	//@JsonProperty("hospitalIntimationId")
	private String hospitalIntimationId;
	
	//@JsonProperty("hospitalFlagYn")
	private String hospitalFlagYn;
	
	//@JsonProperty("createdBy")
	private String createdBy;
	
	//@JsonProperty("createdOn")
	private String createdOn;
	//@JsonProperty("}")
	//private String endToken;


	public InsertHospitalToPMS() {
		this.hospBedCount = "";
		this.hospName = "";
		this.hospitalAddress = "";
		this.hospitalPincode = "";
		this.hospitalCity = "";
		this.hospitalState = "";
		this.hospitalIrdaCode = "";
		this.hospitalTelephoneStdno = "";
		this.hospitalTelephoneNo = "";
		this.hospitalFaxStdno = "";
		this.hospitalFaxNo = "";
		this.hospitalIntimationNo = "";
		this.hospitalIntimationId = "";
		this.hospitalFlagYn = "";
		this.createdBy = "";
		this.createdOn = "";
	}


	/*public String getStartToken() {
		return startToken;
	}


	public void setStartToken(String startToken) {
		this.startToken = startToken;
	}*/


	public String getHospBedCount() {
		return hospBedCount;
	}


	public void setHospBedCount(String hospBedCount) {
		this.hospBedCount = hospBedCount;
	}


	public String getHospName() {
		return hospName;
	}


	public void setHospName(String hospName) {
		this.hospName = hospName;
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


	public String getHospitalIrdaCode() {
		return hospitalIrdaCode;
	}


	public void setHospitalIrdaCode(String hospitalIrdaCode) {
		this.hospitalIrdaCode = hospitalIrdaCode;
	}


	public String getHospitalTelephoneStdno() {
		return hospitalTelephoneStdno;
	}


	public void setHospitalTelephoneStdno(String hospitalTelephoneStdno) {
		this.hospitalTelephoneStdno = hospitalTelephoneStdno;
	}


	public String getHospitalTelephoneNo() {
		return hospitalTelephoneNo;
	}


	public void setHospitalTelephoneNo(String hospitalTelephoneNo) {
		this.hospitalTelephoneNo = hospitalTelephoneNo;
	}


	public String getHospitalFaxStdno() {
		return hospitalFaxStdno;
	}


	public void setHospitalFaxStdno(String hospitalFaxStdno) {
		this.hospitalFaxStdno = hospitalFaxStdno;
	}


	public String getHospitalFaxNo() {
		return hospitalFaxNo;
	}


	public void setHospitalFaxNo(String hospitalFaxNo) {
		this.hospitalFaxNo = hospitalFaxNo;
	}


	public String getHospitalIntimationNo() {
		return hospitalIntimationNo;
	}


	public void setHospitalIntimationNo(String hospitalIntimationNo) {
		this.hospitalIntimationNo = hospitalIntimationNo;
	}


	public String getHospitalIntimationId() {
		return hospitalIntimationId;
	}


	public void setHospitalIntimationId(String hospitalIntimationId) {
		this.hospitalIntimationId = hospitalIntimationId;
	}


	public String getHospitalFlagYn() {
		return hospitalFlagYn;
	}


	public void setHospitalFlagYn(String hospitalFlagYn) {
		this.hospitalFlagYn = hospitalFlagYn;
	}


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public String getCreatedOn() {
		return createdOn;
	}


	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}


	/*public String getEndToken() {
		return endToken;
	}


	public void setEndToken(String endToken) {
		this.endToken = endToken;
	}*/

}
