package com.shaic.restservices.bancs.claimprovision.policydetail;

public class NACHMandates {
	
	private Integer mandateSlNo;
	private String acHolderName;
	private String bankName;
	private String bankBranchName;
	private String bankAccountNumber;
	private Integer nachLimitAmount;
	private String micrCode;
	private String ifscCode;
	private String mandateStartDate;
	private String mandateEndDate;
	private String applicationNo;
	private String nachURNNo;
	private String frequency;
	
	public Integer getMandateSlNo() {
		return mandateSlNo;
	}
	public void setMandateSlNo(Integer mandateSlNo) {
		this.mandateSlNo = mandateSlNo;
	}
	public String getAcHolderName() {
		return acHolderName;
	}
	public void setAcHolderName(String acHolderName) {
		this.acHolderName = acHolderName;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankBranchName() {
		return bankBranchName;
	}
	public void setBankBranchName(String bankBranchName) {
		this.bankBranchName = bankBranchName;
	}
	public String getBankAccountNumber() {
		return bankAccountNumber;
	}
	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}
	public Integer getNachLimitAmount() {
		return nachLimitAmount;
	}
	public void setNachLimitAmount(Integer nachLimitAmount) {
		this.nachLimitAmount = nachLimitAmount;
	}
	public String getMicrCode() {
		return micrCode;
	}
	public void setMicrCode(String micrCode) {
		this.micrCode = micrCode;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public String getMandateStartDate() {
		return mandateStartDate;
	}
	public void setMandateStartDate(String mandateStartDate) {
		this.mandateStartDate = mandateStartDate;
	}
	public String getMandateEndDate() {
		return mandateEndDate;
	}
	public void setMandateEndDate(String mandateEndDate) {
		this.mandateEndDate = mandateEndDate;
	}
	public String getApplicationNo() {
		return applicationNo;
	}
	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}
	public String getNachURNNo() {
		return nachURNNo;
	}
	public void setNachURNNo(String nachURNNo) {
		this.nachURNNo = nachURNNo;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	
}
