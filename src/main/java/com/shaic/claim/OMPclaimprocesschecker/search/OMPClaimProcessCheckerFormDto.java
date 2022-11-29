package com.shaic.claim.OMPclaimprocesschecker.search;

import java.io.Serializable;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class OMPClaimProcessCheckerFormDto extends AbstractSearchDTO implements Serializable{
	
	private String intimationNo;
	private String policyNo;
	private String claimno;
	private String passportno;
	private String claimtype;
	
	
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
	public String getclaimno(){
		return claimno;
	}
	public void setclaimno(String claimno){
		this.claimno = claimno;
	}
	
	public String getPassportno(){
		return passportno;
	}
	public void setPassportno(String passportno){
		this.passportno = passportno;
	}
	
	public String getClaimtype(){
		return claimtype;
	}
	public void setClaimtype(String claimtype){
		this.claimtype = claimtype;
	}
	
	
}
