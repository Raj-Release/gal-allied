package com.shaic.claim.hospitalCommunication;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.newcode.wizard.dto.SearchTableDTO;

public class HospitalFormDTO extends SearchTableDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;
	
	private String name;
	
	private String phoneno;
	
	private String addr1;
	
	private String addr2;
	
	private String addr3;
	
	private String city;
	
	private String state;
	
	private String pincode;
	
	private String remarks;
	
	private String authorizedRepresentative;
	
	private String representName;
	
	private String hospitalType;
	
	private String roomCategory;
	
	private String denialRemarks;
	
	private long claimedAmount;
	
	private long approvedAmount;

	private SelectValue claimStatus;
	
	private String contactName;
	
	private String designation;
	
	private String hospitalRemarks;

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getHospitalRemarks() {
		return hospitalRemarks;
	}

	public void setHospitalRemarks(String hospitalRemarks) {
		this.hospitalRemarks = hospitalRemarks;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneno() {
		return phoneno;
	}

	
	
	public SelectValue getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(SelectValue claimStatus) {
		this.claimStatus = claimStatus;
	}

	public void setPhoneno(String phoneno) {
		this.phoneno = phoneno;
	}

	public String getAddr1() {
		return addr1;
	}

	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}

	public String getAddr2() {
		return addr2;
	}

	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}

	public String getAddr3() {
		return addr3;
	}

	public void setAddr3(String addr3) {
		this.addr3 = addr3;
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

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getAuthorizedRepresentative() {
		return authorizedRepresentative;
	}

	public void setAuthorizedRepresentative(String authorizedRepresentative) {
		this.authorizedRepresentative = authorizedRepresentative;
	}

	public String getRepresentName() {
		return representName;
	}

	public void setRepresentName(String representName) {
		this.representName = representName;
	}

	public String getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}

	public String getRoomCategory() {
		return roomCategory;
	}

	public void setRoomCategory(String roomCategory) {
		this.roomCategory = roomCategory;
	}

	public String getDenialRemarks() {
		return denialRemarks;
	}

	public void setDenialRemarks(String denialRemarks) {
		this.denialRemarks = denialRemarks;
	}

	public long getClaimedAmount() {
		return claimedAmount;
	}

	public void setClaimedAmount(long claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

	public long getApprovedAmount() {
		return approvedAmount;
	}

	public void setApprovedAmount(long approvedAmount) {
		this.approvedAmount = approvedAmount;
	}
	
	
	
}
