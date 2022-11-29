package com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class VerificationAccountDeatilsTableDTO extends AbstractTableDTO  implements Serializable{
	
	private String claimNumber;
	private String rodNumber;
	private String policyNumber;
	private String insuredName;
	private String payeeName;
	private String accountNumber;
	private String ifscCode;
	private String paidAmount;
	private boolean verifiedCheck;
	private long rodKey;

	
	public long getRodKey() {
		return rodKey;
	}
	public void setRodKey(long rodKey) {
		this.rodKey = rodKey;
	}
	public boolean getVerifiedCheck() {
		return verifiedCheck;
	}
	public void setVerifiedCheck(boolean verifiedCheck) {
		this.verifiedCheck = verifiedCheck;
	}
	public String getClaimNumber() {
		return claimNumber;
	}
	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}
	public String getRodNumber() {
		return rodNumber;
	}
	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public String getInsuredName() {
		return insuredName;
	}
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public String getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(String paidAmount) {
		this.paidAmount = paidAmount;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	
}
