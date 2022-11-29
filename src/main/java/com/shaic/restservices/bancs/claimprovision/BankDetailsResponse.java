package com.shaic.restservices.bancs.claimprovision;

import java.util.List;

public class BankDetailsResponse {
	
	private Integer errorCode;
	private String errorDescription;
	private String policyNumber;
	private List<PartyBankDetails> partyBankDetails;
	
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
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public List<PartyBankDetails> getPartyBankDetails() {
		return partyBankDetails;
	}
	public void setPartyBankDetails(List<PartyBankDetails> partyBankDetails) {
		this.partyBankDetails = partyBankDetails;
	}
	@Override
	public String toString() {
		return "BankDetailsResponse [errorCode=" + errorCode
				+ ", errorDescription=" + errorDescription + ", policyNumber="
				+ policyNumber + ", partyBankDetails=" + partyBankDetails + "]";
	}
	
}
