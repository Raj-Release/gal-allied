package com.shaic.claim.rod.wizard.forms;

import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;

public class AddBanksDetailsTableDTO {
	
	private String payeeName;
	private String payeeRelationship;
	private String reasonforNameChange;
	private String ifscCode;
	private String accountType;
	private String nameOfBank;
	private String nameAsPerBankAC;
	private String branchName;
	private String accountNumber;
	private String micrCode;
	private String panNo;
	private String virtualPaymentAddr;
	private SelectValue preference;
	private Date  effectiveFromDate;
	private Date  effectiveToDate;
	private ViewSearchCriteriaTableDTO dto;
	private int sequenceNumber;
	
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public String getPayeeRelationship() {
		return payeeRelationship;
	}
	public void setPayeeRelationship(String payeeRelationship) {
		this.payeeRelationship = payeeRelationship;
	}
	public String getReasonforNameChange() {
		return reasonforNameChange;
	}
	public void setReasonforNameChange(String reasonforNameChange) {
		this.reasonforNameChange = reasonforNameChange;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getNameOfBank() {
		return nameOfBank;
	}
	public void setNameOfBank(String nameOfBank) {
		this.nameOfBank = nameOfBank;
	}
	public String getNameAsPerBankAC() {
		return nameAsPerBankAC;
	}
	public void setNameAsPerBankAC(String nameAsPerBankAC) {
		this.nameAsPerBankAC = nameAsPerBankAC;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getMicrCode() {
		return micrCode;
	}
	public void setMicrCode(String micrCode) {
		this.micrCode = micrCode;
	}
	public String getPanNo() {
		return panNo;
	}
	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}
	public String getVirtualPaymentAddr() {
		return virtualPaymentAddr;
	}
	public void setVirtualPaymentAddr(String virtualPaymentAddr) {
		this.virtualPaymentAddr = virtualPaymentAddr;
	}
	public ViewSearchCriteriaTableDTO getDto() {
		return dto;
	}
	public void setDto(ViewSearchCriteriaTableDTO dto) {
		this.dto = dto;
	}
	public SelectValue getPreference() {
		return preference;
	}
	public void setPreference(SelectValue preference) {
		this.preference = preference;
	}
	
	public Date getEffectiveFromDate() {
		return effectiveFromDate;
	}
	public void setEffectiveFromDate(Date effectiveFromDate) {
		this.effectiveFromDate = effectiveFromDate;
	}
	public Date getEffectiveToDate() {
		return effectiveToDate;
	}
	public void setEffectiveToDate(Date effectiveToDate) {
		this.effectiveToDate = effectiveToDate;
	}
	public int getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	
	

}
