package com.shaic.claim.pedquery.search;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;

public class SearchPEDQueryTableDTO extends AbstractTableDTO {//AbstractSearchDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int sno;
	
	private Long key;

	private String intimationNo;

	private String claimNo;

	private String policyNo;

	private String insuredName;

	private String cpuCode;

	private String hospitalName;

	private String address;

	private String city;

	private String pedSuggestion;

	private String pedStatus;
	
	
	private RRCDTO rrcDTO;
	
	
//	private String doorApartmentNo ;
	//private String plotGateNo;
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
//	private String address;

	public int getSno() {
		return sno;
	}

	public void setSno(int sno) {
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

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
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