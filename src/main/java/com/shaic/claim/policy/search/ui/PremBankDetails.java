package com.shaic.claim.policy.search.ui;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PremBankDetails {
	
	@JsonProperty("AccountNo")
	private String accountNumber;
	
	@JsonProperty("AccountType")
	private String accountType;
	
	@JsonProperty("BankName")
	private String bankName;
	
	@JsonProperty("BranchName")
	private String branchName;
	
	@JsonProperty("EffectiveFrom")
	private String effectiveFrom;
	
	@JsonProperty("EffectiveTo")
	private String effectiveTo;
	
	@JsonProperty("IFSCCode")
	private String ifscCode;
	
	@JsonProperty("NameAsPerBank")
	private String nameAsPerBank;
	
	@JsonProperty("Others")
	private String others;

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getEffectiveFrom() {
		return effectiveFrom;
	}

	public void setEffectiveFrom(String effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public String getEffectiveTo() {
		return effectiveTo;
	}

	public void setEffectiveTo(String effectiveTo) {
		this.effectiveTo = effectiveTo;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getNameAsPerBank() {
		return nameAsPerBank;
	}

	public void setNameAsPerBank(String nameAsPerBank) {
		this.nameAsPerBank = nameAsPerBank;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

}
