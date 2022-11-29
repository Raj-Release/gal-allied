package com.shaic.claim.ompviewroddetails;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class OMPClaimStatusRegistrationDto extends AbstractSearchDTO{
	
	
	private String claimNo;
	
	private String registrationStatus;
	
	private String provisionAmount;
	
	private SelectValue claimType;
	
	private String remarks;
	
	
	public String getClaimNo(){
		return claimNo;
	}
	public void setClaimNo(String claimNo){
		this.claimNo = claimNo;
	}
	public String getRegistrationStatus(){
		return registrationStatus;
	}
	public void setRegistrationStatus(String registrationStatus){
		this.registrationStatus = registrationStatus;
	}
	public String getProvisionAmount(){
		return provisionAmount;
	}
	public void setProvisionAmount(String provisionAmount){
		this.provisionAmount = provisionAmount;
	}
//	public String getClaimType(){
//		return claimType;
//	}
//	public void setClaimType(String claimType){
//		this.claimType = claimType ;
//	}
	public SelectValue getClaimType() {
		return claimType;
	}

	public void setClaimType(SelectValue claimType) {
		this.claimType = claimType;
	}
	public String getRemarks(){
		return remarks;
	}
	public void setRemarks(String remarks){
		this.remarks = remarks;
	}

}
