package com.shaic.paclaim.cashless.downsize.search;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class PASearchDownsizeCashLessProcessTableDTO extends AbstractTableDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String intimationNo;
	
	private String claimNo;
	
	private String lob;
	
	private String insuredPatientName;
	
	private String diagnosis;
	
	private String hospitalName;
	
	private String hospitalAddress;
	
	private String claimStatus;
	
	//private String preAuthKey;
	private Long preAuthKey;
	private String hospitalCity;
/*	private String doorApartmentNumber;
	private String plotGateNumber;
	private String buildingName;
	private String streetName;
	private String locality;
	private String district;
	private String state;
	private String country;
	private String pinCode;*/

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getLob() {
		return lob;
	}

	public void setLob(String lob) {
		this.lob = lob;
	}

	public String getInsuredPatientName() {
		return insuredPatientName;
	}

	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
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

	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	public String getHospitalCity() {
		return hospitalCity;
	}

	public void setHospitalCity(String hospitalCity) {
		this.hospitalCity = hospitalCity;
	}

/*	public String getDoorApartmentNumber() {
		return doorApartmentNumber;
	}

	public void setDoorApartmentNumber(String doorApartmentNumber) {
		this.doorApartmentNumber = doorApartmentNumber;
	}

	public String getPlotGateNumber() {
		return plotGateNumber;
	}

	public void setPlotGateNumber(String plotGateNumber) {
		this.plotGateNumber = plotGateNumber;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}*/

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getPreAuthKey() {
		return preAuthKey;
	}

	public void setPreAuthKey(Long preAuthKey) {
		this.preAuthKey = preAuthKey;
	}

	/*public String getPreAuthKey() {
		return preAuthKey;
	}

	public void setPreAuthKey(String preAuthKey) {
		this.preAuthKey = preAuthKey;
	}
	*/
}
