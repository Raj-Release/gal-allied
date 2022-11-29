package com.shaic.claim.settlementpullback.searchbatchpendingpullback;

import java.io.Serializable;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class SearchLotPullBackFormDTO extends AbstractSearchDTO implements Serializable{

	private static final long serialVersionUID = 3128204490195650666L;
	
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
