package com.shaic.restservices.bancs.triggerPartyEndorsement;

import java.util.Date;

public class TriggerPartyEndorseBankDetails {
	 private String operationCode;
	 private Long slNo;
	 private String preference;
	 private String nameAsPerBankAC;
	 private String nameOfTheBank;
	 private String branchName;
	 private String accountType;
	 private String accountNumber;
	 private String MICRCode;
	 private String IFSCCode;
	 private String virtualPaymentAddress;
	 private Date effectiveFromDate;
	 private Date effectiveToDate;
	 private String starDate;
	 private String endDate;


	 // Getter Methods 

	 public String getOperationCode() {
	  return operationCode;
	 }

	 public Long getSlNo() {
	  return slNo;
	 }

	 public String getPreference() {
	  return preference;
	 }

	 public String getNameAsPerBankAC() {
	  return nameAsPerBankAC;
	 }

	 public String getNameOfTheBank() {
	  return nameOfTheBank;
	 }

	 public String getBranchName() {
	  return branchName;
	 }

	 public String getAccountType() {
	  return accountType;
	 }

	 public String getAccountNumber() {
	  return accountNumber;
	 }

	 public String getMICRCode() {
	  return MICRCode;
	 }

	 public String getIFSCCode() {
	  return IFSCCode;
	 }

	 public String getVirtualPaymentAddress() {
	  return virtualPaymentAddress;
	 }

	 public Date getEffectiveFromDate() {
	  return effectiveFromDate;
	 }

	 public Date getEffectiveToDate() {
	  return effectiveToDate;
	 }

	 // Setter Methods 

	 public void setOperationCode(String operationCode) {
	  this.operationCode = operationCode;
	 }

	 public void setSlNo(Long slNo) {
	  this.slNo = slNo;
	 }

	 public void setPreference(String preference) {
	  this.preference = preference;
	 }

	 public void setNameAsPerBankAC(String nameAsPerBankAC) {
	  this.nameAsPerBankAC = nameAsPerBankAC;
	 }

	 public void setNameOfTheBank(String nameOfTheBank) {
	  this.nameOfTheBank = nameOfTheBank;
	 }

	 public void setBranchName(String branchName) {
	  this.branchName = branchName;
	 }

	 public void setAccountType(String accountType) {
	  this.accountType = accountType;
	 }

	 public void setAccountNumber(String accountNumber) {
	  this.accountNumber = accountNumber;
	 }

	 public void setMICRCode(String MICRCode) {
	  this.MICRCode = MICRCode;
	 }

	 public void setIFSCCode(String IFSCCode) {
	  this.IFSCCode = IFSCCode;
	 }

	 public void setVirtualPaymentAddress(String virtualPaymentAddress) {
	  this.virtualPaymentAddress = virtualPaymentAddress;
	 }

	 public void setEffectiveFromDate(Date effectiveFromDate) {
	  this.effectiveFromDate = effectiveFromDate;
	 }

	 public void setEffectiveToDate(Date effectiveToDate) {
	  this.effectiveToDate = effectiveToDate;
	 }

	public String getStarDate() {
		return starDate;
	}

	public void setStarDate(String starDate) {
		this.starDate = starDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
