package com.shaic.claim.reimbursement.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class OtherClaimDetailsDTO implements Serializable {
	private static final long serialVersionUID = -8585074426831907868L;

	private Long key;
	
	@NotNull(message = "Please Enter Previous Insurance Company Name.")
	@Size(min = 1 , message = "Please Enter Previous Insurance Company Name.")
	private String otherClaimCompanyName;
	
	private Date otherClaimCommencementDate;
	
	private String otherClaimPolicyNumber;
	
	private String otherClaimSumInsured;
	
	private Boolean last4YearsHospitalisation;
	
	private String last4YearsHospitalisationFlag;

	public String getOtherClaimCompanyName() {
		return otherClaimCompanyName;
	}

	public void setOtherClaimCompanyName(String otherClaimCompanyName) {
		this.otherClaimCompanyName = otherClaimCompanyName;
	}

	public Date getOtherClaimCommencementDate() {
		return otherClaimCommencementDate;
	}

	public void setOtherClaimCommencementDate(Date otherClaimCommencementDate) {
		this.otherClaimCommencementDate = otherClaimCommencementDate;
	}

	public String getOtherClaimPolicyNumber() {
		return otherClaimPolicyNumber;
	}

	public void setOtherClaimPolicyNumber(String otherClaimPolicyNumber) {
		this.otherClaimPolicyNumber = otherClaimPolicyNumber;
	}

	public String getOtherClaimSumInsured() {
		return otherClaimSumInsured;
	}

	public void setOtherClaimSumInsured(String otherClaimSumInsured) {
		this.otherClaimSumInsured = otherClaimSumInsured;
	}

	public Boolean getLast4YearsHospitalisation() {
		return last4YearsHospitalisation;
	}

	public void setLast4YearsHospitalisation(Boolean last4YearsHospitalisation) {
		this.last4YearsHospitalisation = last4YearsHospitalisation;
		this.setLast4YearsHospitalisationFlag((this.last4YearsHospitalisation != null && this.last4YearsHospitalisation) ? "Y" : "N" );
	}

	public String getLast4YearsHospitalisationFlag() {
		return last4YearsHospitalisationFlag;
	}

	public void setLast4YearsHospitalisationFlag(
			String last4YearsHospitalisationFlag) {
		this.last4YearsHospitalisationFlag = last4YearsHospitalisationFlag;
		if(this.last4YearsHospitalisationFlag != null) {
			this.last4YearsHospitalisation = this.last4YearsHospitalisationFlag.toLowerCase().equalsIgnoreCase("y") ? true : false;
		}
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}
}
