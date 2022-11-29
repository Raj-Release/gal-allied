package com.shaic.claim.OMPProcessNegotiation.search;

import java.io.Serializable;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class OMPProcessNegotiationFormDto extends AbstractSearchDTO implements Serializable{
	
	private String intimationNo;
//	private SelectValue type;
	private String claimno;
	
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getClaimno() {
		return claimno;
	}
	public void setClaimno(String claimno) {
		this.claimno = claimno;
	}

}
