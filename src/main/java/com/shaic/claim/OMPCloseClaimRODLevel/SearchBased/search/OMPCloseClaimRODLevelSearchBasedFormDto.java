package com.shaic.claim.OMPCloseClaimRODLevel.SearchBased.search;

import java.io.Serializable;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class OMPCloseClaimRODLevelSearchBasedFormDto extends AbstractSearchDTO implements Serializable{
	
	private String intimationNo;
	private String policyNo;
	private String claimno;
	
	public String getIntimationNo(){
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo){
		this.intimationNo = intimationNo;
	}
	public String getPolicyNo(){
		return policyNo;
	}
	public void setPolicyNo(String policyNo){
		this.policyNo = policyNo;
	}
	public String getClaimno(){
		return claimno;
	}
	public void setClaimno(String claimno){
		this.claimno = claimno;
	}

}
