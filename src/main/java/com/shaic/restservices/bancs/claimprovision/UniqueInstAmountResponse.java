package com.shaic.restservices.bancs.claimprovision;

public class UniqueInstAmountResponse {
	private Integer errorCode;
	private String errorDescription;
	private Integer pendingPremiumAmount;
	private Integer tax;
	private Integer netAmount;

	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	public Integer getPendingPremiumAmount() {
		return pendingPremiumAmount;
	}
	public void setPendingPremiumAmount(Integer pendingPremiumAmount) {
		this.pendingPremiumAmount = pendingPremiumAmount;
	}
	public Integer getTax() {
		return tax;
	}
	public void setTax(Integer tax) {
		this.tax = tax;
	}
	public Integer getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(Integer netAmount) {
		this.netAmount = netAmount;
	}

}
