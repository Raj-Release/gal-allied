package com.shaic.claim.adviseonped.search;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;

public class SearchAdviseOnPEDTableDTO extends AbstractTableDTO implements Serializable {

	private static final long serialVersionUID = -4784233338391482167L;

	private Integer sno;

	private String intimationNo;

	private String claimNo;

	private String policyNo;

	private String insuredPatientName;

	private String cpuCode;

	private String hospitalName;

	private String hospitalAddress;

	private String hospitalCity;

	private String pedSuggestion;

	private String pedStatus;
	
/*	private String doorApartmentNumber;
	private String plotGateNumber;
	private String buildingName;
	private String streetName;
	private String locality;
	private String district;
	private String state;
	private String country;
	private String pinCode;*/
	
	private Long hospitalTypeId;
	
	private RRCDTO rrcDTO;
	

	public Integer getSno() {
		return sno;
	}

	public void setSno(Integer sno) {
		this.sno = sno;
	}

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

	public String getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
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

	public String getHospitalCity() {
		return hospitalCity;
	}

	public void setHospitalCity(String hospitalCity) {
		this.hospitalCity = hospitalCity;
	}

	public String getPedSuggestion() {
		return pedSuggestion;
	}

	public void setPedSuggestion(String pedSuggestion) {
		this.pedSuggestion = pedSuggestion;
	}

	public String getPedStatus() {
		return pedStatus;
	}

	public void setPedStatus(String pedStatus) {
		this.pedStatus = pedStatus;
	}

	/*public String getDoorApartmentNumber() {
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

	public Long getHospitalTypeId() {
		return hospitalTypeId;
	}

	public void setHospitalTypeId(Long hospitalTypeId) {
		this.hospitalTypeId = hospitalTypeId;
	}

	public RRCDTO getRrcDTO() {
		return rrcDTO;
	}

	public void setRrcDTO(RRCDTO rrcDTO) {
		this.rrcDTO = rrcDTO;
	}


}
