package com.shaic.gpaclaim.unnamedriskdetails;

import java.io.Serializable;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class SearchUnnamedRiskDetailsFormDTO extends AbstractSearchDTO implements Serializable{

	private String intimationNo;

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	
	
	
}
