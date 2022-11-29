package com.shaic.restservices.bancs.claimprovision;

import java.util.List;

import com.shaic.claim.rod.wizard.forms.BankDetailsTableDTO;

public class PartyBankDetails {
	private String partyCode;
	private List<BankDetailsTableDTO> bankDetails;
	
	public String getPartyCode() {
		return partyCode;
	}
	public void setPartyCode(String partyCode) {
		this.partyCode = partyCode;
	}
	public List<BankDetailsTableDTO> getBankDetails() {
		return bankDetails;
	}
	public void setBankDetails(List<BankDetailsTableDTO> bankDetails) {
		this.bankDetails = bankDetails;
	}
	@Override
	public String toString() {
		return "PartyBankDetails [partyCode=" + partyCode + ", bankDetails="
				+ bankDetails + "]";
	}
	
}