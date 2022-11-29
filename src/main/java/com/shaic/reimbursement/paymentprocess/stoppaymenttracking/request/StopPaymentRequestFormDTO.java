package com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request;

import java.io.Serializable;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class StopPaymentRequestFormDTO extends AbstractSearchDTO implements Serializable{

	
	
	private String intimationNo;
	
	private String utrNumber;

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getUtrNumber() {
		return utrNumber;
	}

	public void setUtrNumber(String utrNumber) {
		this.utrNumber = utrNumber;
	}
	
	
}
