package com.shaic.paclaim.manageclaim.closeclaim.pageClaimLevel;

import java.io.Serializable;
import java.util.Date;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class PACloseClaimTableDTO extends AbstractSearchDTO implements Serializable  {
	
	
    private String insuredPatientName;
    
    private Date dateOfAdmission;
    
    private String strDateOfAdmission;
    
    private Double provisionAmount;
    
    private Integer numberOfRod;
    
    private String claimStatus;

	public String getInsuredPatientName() {
		return insuredPatientName;
	}

	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}

	public Date getDateOfAdmission() {
		return dateOfAdmission;
	}

	public void setDateOfAdmission(Date dateOfAdmission) {
		this.dateOfAdmission = dateOfAdmission;
	}

	public String getStrDateOfAdmission() {
		return strDateOfAdmission;
	}

	public void setStrDateOfAdmission(String strDateOfAdmission) {
		this.strDateOfAdmission = strDateOfAdmission;
	}

	public Double getProvisionAmount() {
		return provisionAmount;
	}

	public void setProvisionAmount(Double provisionAmount) {
		this.provisionAmount = provisionAmount;
	}

	public Integer getNumberOfRod() {
		return numberOfRod;
	}

	public void setNumberOfRod(Integer numberOfRod) {
		this.numberOfRod = numberOfRod;
	}

	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}




}
