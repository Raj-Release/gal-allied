package com.shaic.claim.reports.paymentpendingdashboard;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class PaymentPendingDashboardTableDTO extends AbstractTableDTO  implements Serializable{
	
	private int serialNumber;
	private String claimIntimationNo;
	private String payeeName;
	private String hosCode;
	private String claimType;
	private Double approvedAmount;
	private String paymentFileSentOn;
	private String batchNumber;
	private String ifscCode;
	private String accountNumber;
	public int getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getClaimIntimationNo() {
		return claimIntimationNo;
	}
	public void setClaimIntimationNo(String claimIntimationNo) {
		this.claimIntimationNo = claimIntimationNo;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public String getHosCode() {
		return hosCode;
	}
	public void setHosCode(String hosCode) {
		this.hosCode = hosCode;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public Double getApprovedAmount() {
		return approvedAmount;
	}
	public void setApprovedAmount(Double approvedAmount) {
		this.approvedAmount = approvedAmount;
	}
	public String getPaymentFileSentOn() {
		return paymentFileSentOn;
	}
	public void setPaymentFileSentOn(String paymentFileSentOn) {
		this.paymentFileSentOn = paymentFileSentOn;
	}
	public String getBatchNumber() {
		return batchNumber;
	}
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	
}
