package com.shaic.claim.aadhar.search;

import java.io.Serializable;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;



public class SearchUpdateAadharDTO extends AbstractSearchDTO implements Serializable{
	
	private String intimationNo;

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

}
