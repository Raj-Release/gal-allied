package com.shaic.restservices.bancs.triggerPolicyEndorsement;

import java.util.Date;

public class TriggerPolicyEndorseNachMandates {
    
    private String operationName;
    
    private Long mandateSiNo;
    
    private String acHolderName;

    private String bankName;
    
    private String bankBranchName;
    
    private String bankAccountNumber;
    
    private String accountType;
    
    private Long nachLimitAmount;
	
    private String micrCode;

    private String ifscCode;

    private Date mandateStartDate;
    
    private Date mandateEndDate;
    
    private String applicationNo;
    
    private String nachUrnNo;

    private String frequency;

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public Long getMandateSiNo() {
		return mandateSiNo;
	}

	public void setMandateSiNo(Long mandateSiNo) {
		this.mandateSiNo = mandateSiNo;
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

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public Long getNachLimitAmount() {
		return nachLimitAmount;
	}

	public void setNachLimitAmount(Long nachLimitAmount) {
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

	public Date getMandateStartDate() {
		return mandateStartDate;
	}

	public void setMandateStartDate(Date mandateStartDate) {
		this.mandateStartDate = mandateStartDate;
	}

	public Date getMandateEndDate() {
		return mandateEndDate;
	}

	public void setMandateEndDate(Date mandateEndDate) {
		this.mandateEndDate = mandateEndDate;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getNachUrnNo() {
		return nachUrnNo;
	}

	public void setNachUrnNo(String nachUrnNo) {
		this.nachUrnNo = nachUrnNo;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
    
    
    
    



}
