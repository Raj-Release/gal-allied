package com.shaic.restservices.bancs.claimprovision;

public class BankDetails {
	
/*	private Integer slNo;
	private String preference;
	private String nameAsPerbankAC;
	private String nameOfThebank;
	private String branchName;
	private String accountType;
	private String accountNumber;
	private String micrCode;
	private String ifscCode;
	private String virtualPaymentAddress;
	private String effectiveFromDate;
	private String effectiveToDate;*/
	
	
	private String sequenceNo;
	private String accountType;
	private String accountNumber;
	private String endDate;
	private String ifscCode;
	private String micrCode;
	private String nameAsperBankAccount;
	private String preference;
	private String startDate;
	
	public String getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public String getMicrCode() {
		return micrCode;
	}
	public void setMicrCode(String micrCode) {
		this.micrCode = micrCode;
	}
	public String getNameAsperBankAccount() {
		return nameAsperBankAccount;
	}
	public void setNameAsperBankAccount(String nameAsperBankAccount) {
		this.nameAsperBankAccount = nameAsperBankAccount;
	}
	public String getPreference() {
		return preference;
	}
	public void setPreference(String preference) {
		this.preference = preference;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	@Override
	public String toString() {
		return "BankDetails [sequenceNo=" + sequenceNo + ", accountType="
				+ accountType + ", accountNumber=" + accountNumber
				+ ", endDate=" + endDate + ", ifscCode=" + ifscCode
				+ ", micrCode=" + micrCode + ", nameAsperBankAccount="
				+ nameAsperBankAccount + ", preference=" + preference
				+ ", startDate=" + startDate + "]";
	}
	
}
