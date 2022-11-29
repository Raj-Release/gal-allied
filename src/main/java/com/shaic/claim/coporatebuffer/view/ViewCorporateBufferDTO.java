package com.shaic.claim.coporatebuffer.view;

import java.io.Serializable;

public class ViewCorporateBufferDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String bufferedSI;
	private String utilizedAmt;
	private String currentClaimAmt;
	private String balance;

	public String getBufferedSI() {
		return bufferedSI;
	}

	public void setBufferedSI(String bufferedSI) {
		this.bufferedSI = bufferedSI;
	}

	public String getUtilizedAmt() {
		return utilizedAmt;
	}

	public void setUtilizedAmt(String utilizedAmt) {
		this.utilizedAmt = utilizedAmt;
	}

	public String getCurrentClaimAmt() {
		return currentClaimAmt;
	}

	public void setCurrentClaimAmt(String currentClaimAmt) {
		this.currentClaimAmt = currentClaimAmt;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

}
