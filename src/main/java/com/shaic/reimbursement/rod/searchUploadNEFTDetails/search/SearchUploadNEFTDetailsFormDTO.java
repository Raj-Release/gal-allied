package com.shaic.reimbursement.rod.searchUploadNEFTDetails.search;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class SearchUploadNEFTDetailsFormDTO  extends AbstractSearchDTO {

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
