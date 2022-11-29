package com.shaic.claim.stoploss.view;

import java.io.Serializable;

public class ViewStopLossDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String policyNo;
	private String premium;
	private String stopLossPer;
	private String stopLossAmt;
	private String claimPaid;
	private String claimOutstanding;
	private String totalIncuredClaimAmt;
	private String currentClaimsAmt;

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getPremium() {
		return premium;
	}

	public void setPremium(String premium) {
		this.premium = premium;
	}

	public String getStopLossPer() {
		return stopLossPer;
	}

	public void setStopLossPer(String stopLossPer) {
		this.stopLossPer = stopLossPer;
	}

	public String getStopLossAmt() {
		return stopLossAmt;
	}

	public void setStopLossAmt(String stopLossAmt) {
		this.stopLossAmt = stopLossAmt;
	}

	public String getClaimPaid() {
		return claimPaid;
	}

	public void setClaimPaid(String claimPaid) {
		this.claimPaid = claimPaid;
	}

	public String getClaimOutstanding() {
		return claimOutstanding;
	}

	public void setClaimOutstanding(String claimOutstanding) {
		this.claimOutstanding = claimOutstanding;
	}

	public String getTotalIncuredClaimAmt() {
		return totalIncuredClaimAmt;
	}

	public void setTotalIncuredClaimAmt(String totalIncuredClaimAmt) {
		this.totalIncuredClaimAmt = totalIncuredClaimAmt;
	}

	public String getCurrentClaimsAmt() {
		return currentClaimsAmt;
	}

	public void setCurrentClaimsAmt(String currentClaimsAmt) {
		this.currentClaimsAmt = currentClaimsAmt;
	}

}
