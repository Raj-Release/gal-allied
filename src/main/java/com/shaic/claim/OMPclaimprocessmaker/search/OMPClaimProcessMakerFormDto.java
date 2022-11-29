package com.shaic.claim.OMPclaimprocessmaker.search;

import java.io.Serializable;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class OMPClaimProcessMakerFormDto extends AbstractSearchDTO implements Serializable{
	
	
	
	private String intimationNo;
	private String claimno;
	
	public String getIntimationNo(){
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo){
		this.intimationNo = intimationNo;
	}
	
	public String getClaimno(){
		return claimno;
	}
	public void setClaimno(String claimno){
		this.claimno = claimno;
	}
}
