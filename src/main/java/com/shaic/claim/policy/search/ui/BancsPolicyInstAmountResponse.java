package com.shaic.claim.policy.search.ui;

public class BancsPolicyInstAmountResponse {


	private String errorCode;
	private String errorDescription;
	private String pendingInstallmentAmount;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public String getPendingInstallmentAmount() {
		return pendingInstallmentAmount;
	}

	public void setPendingInstallmentAmount(String pendingInstallmentAmount) {
		this.pendingInstallmentAmount = pendingInstallmentAmount;
	}

}
