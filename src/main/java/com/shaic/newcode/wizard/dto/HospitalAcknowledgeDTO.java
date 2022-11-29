package com.shaic.newcode.wizard.dto;

import javax.validation.constraints.NotNull;

import com.shaic.domain.Claim;
import com.shaic.domain.Intimation;
import com.shaic.domain.Policy;
import com.shaic.domain.preauth.Preauth;

public class HospitalAcknowledgeDTO {
	
	private Long key;

	
	@NotNull(message="Please Enter Remarks")
	private String remarks;
	
	private Long hospitalId;

	private Long approvedAmount;
	
	private Double claimedAmount;

	private String denialRemarks;

	private String hopitalCode;
	
	private Claim claim;
	
	private Policy policy;
	
	private Preauth preauth;
	
	private Intimation intimation;
	
	private String intimationNumber;

	@NotNull(message="Please Enter Hospital Contact Name")
	private String hospitalName;

	private String address1;

	private String address2;

	private String address3;

	private String city;

	private String state;

	private String pinCode;

	private String hospitalPhno;

	private String authorizedRepresentative;

	private String nameofRepresentative;

	private String hospitalCategory;

	private String hospitalRemarks;

	private String roomCategory;

	@NotNull(message="Please Enter Hospital Name")
	private String hospitalContactName;

	private String designation;

	private String claimStatus;

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDenialRemarks() {
		return denialRemarks;
	}

	public void setDenialRemarks(String denialRemarks) {
		this.denialRemarks = denialRemarks;
	}

	public String getHopitalCode() {
		return hopitalCode;
	}

	public void setHopitalCode(String hopitalCode) {
		this.hopitalCode = hopitalCode;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
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

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}


	public String getAuthorizedRepresentative() {
		return authorizedRepresentative;
	}

	public void setAuthorizedRepresentative(String authorizedRepresentative) {
		this.authorizedRepresentative = authorizedRepresentative;
	}

	public String getNameofRepresentative() {
		return nameofRepresentative;
	}

	public void setNameofRepresentative(String nameofRepresentative) {
		this.nameofRepresentative = nameofRepresentative;
	}

	public String getHospitalCategory() {
		return hospitalCategory;
	}

	public void setHospitalCategory(String hospitalCategory) {
		this.hospitalCategory = hospitalCategory;
	}

	public String getHospitalRemarks() {
		return hospitalRemarks;
	}

	public void setHospitalRemarks(String hospitalRemarks) {
		this.hospitalRemarks = hospitalRemarks;
	}

	public String getRoomCategory() {
		return roomCategory;
	}

	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	public void setRoomCategory(String roomCategory) {
		this.roomCategory = roomCategory;
	}

	public String getHospitalContactName() {
		return hospitalContactName;
	}

	public void setHospitalContactName(String hospitalContactName) {
		this.hospitalContactName = hospitalContactName;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getHospitalPhno() {
		return hospitalPhno;
	}

	public void setHospitalPhno(String hospitalPhno) {
		this.hospitalPhno = hospitalPhno;
	}

	public Double getClaimedAmount() {
		return claimedAmount;
	}

	public void setClaimedAmount(Double claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

	public Long getApprovedAmount() {
		return approvedAmount;
	}

	public void setApprovedAmount(Long approvedAmount) {
		this.approvedAmount = approvedAmount;
	}

	public String getIntimationNumber() {
		return intimationNumber;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public Claim getClaim() {
		return claim;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	public Intimation getIntimation() {
		return intimation;
	}

	public void setIntimation(Intimation intimation) {
		this.intimation = intimation;
	}

	public Preauth getPreauth() {
		return preauth;
	}

	public void setPreauth(Preauth preauth) {
		this.preauth = preauth;
	}
}
