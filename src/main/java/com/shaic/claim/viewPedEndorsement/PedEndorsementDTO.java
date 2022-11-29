package com.shaic.claim.viewPedEndorsement;

import java.io.Serializable;

public class PedEndorsementDTO implements Serializable {

	private static final long serialVersionUID = 4468862283186880744L;

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
