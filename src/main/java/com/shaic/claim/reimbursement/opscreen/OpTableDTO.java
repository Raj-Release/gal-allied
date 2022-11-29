package com.shaic.claim.reimbursement.opscreen;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class OpTableDTO extends AbstractTableDTO  implements Serializable{
	
	private String claimNo;	
	private String policyNo;		
	private String product;		
	private String zone	;	
	private String paymentMode	;	
	private String ifscCode;		
	private String accountNo;	
	private String branchName;		
	private String amountPayable;		
	private String payeeName;		
	private String payableAt;		
	private String panNo;	
	private String emailID;
	private String checkBoxStatus;
	private Boolean chkSelect;
	
	
	
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getAmountPayable() {
		return amountPayable;
	}
	public void setAmountPayable(String amountPayable) {
		this.amountPayable = amountPayable;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public String getPayableAt() {
		return payableAt;
	}
	public void setPayableAt(String payableAt) {
		this.payableAt = payableAt;
	}
	public String getPanNo() {
		return panNo;
	}
	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}
	public String getEmailID() {
		return emailID;
	}
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	
	public String getCheckBoxStatus() {
		return checkBoxStatus;
	}
	public void setCheckBoxStatus(String checkBoxStatus) {
		this.checkBoxStatus = checkBoxStatus;
	}
	public Boolean getChkSelect() {
		return chkSelect;
	}
	public void setChkSelect(Boolean chkSelect) {
		this.chkSelect = chkSelect;
	}
	
	
		

	
	

}
