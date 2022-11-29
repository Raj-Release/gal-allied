/**
 * 
 */
package com.shaic.claim.rod.wizard.dto;

import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

/**
 * @author ntv.vijayar
 *
 */
public class PreviousAccountDetailsDTO extends  AbstractTableDTO {
	
	private String source;
	
	private String policyClaimNo;
	
	private Date receiptDate;
	
	private String bankAccountNo;
	
	private String bankName;
	
	private String bankCity;
	
	private String bankBranch;
	
	private String accountType;
	
	private String ifsccode;
	
	private Boolean chkSelect;
	
	private String panNo;
	
	private String emailId;
	
	private String docReceivedFrom;
	
	private Integer serialNo;
	
	private String receiptDateValue;	
	
	private String rodNumber;
	
	private String intimationNumber;
	

	public Integer getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPolicyClaimNo() {
		return policyClaimNo;
	}

	public void setPolicyClaimNo(String policyClaimNo) {
		this.policyClaimNo = policyClaimNo;
	}

	public Date getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

	public String getBankAccountNo() {
		return bankAccountNo;
	}

	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCity() {
		return bankCity;
	}

	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}

	public String getBankBranch() {
		return bankBranch;
	}

	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getIfsccode() {
		return ifsccode;
	}

	public void setIfsccode(String ifsccode) {
		this.ifsccode = ifsccode;
	}

	public Boolean getChkSelect() {
		return chkSelect;
	}

	public void setChkSelect(Boolean chkSelect) {
		this.chkSelect = chkSelect;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getDocReceivedFrom() {
		return docReceivedFrom;
	}

	public void setDocReceivedFrom(String docReceivedFrom) {
		this.docReceivedFrom = docReceivedFrom;
	}

	public String getReceiptDateValue() {
		return receiptDateValue;
	}

	public void setReceiptDateValue(String receiptDateValue) {
		this.receiptDateValue = receiptDateValue;
	}

	public String getRodNumber() {
		return rodNumber;
	}

	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
	}

	public String getIntimationNumber() {
		return intimationNumber;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

}
