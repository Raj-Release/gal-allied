package com.shaic.claim.pedrequest.initiateped;

import java.io.Serializable;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class SearchPEDInitiateFormDTO extends AbstractSearchDTO implements Serializable{

	private static final long serialVersionUID = -2649459376620334696L;

	private String intimationNo;
	
	private String policyNo;
	
	

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	
	
	
}
