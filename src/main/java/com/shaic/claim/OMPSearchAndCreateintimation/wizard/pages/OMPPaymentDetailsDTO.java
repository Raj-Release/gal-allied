package com.shaic.claim.OMPSearchAndCreateintimation.wizard.pages;

import java.io.Serializable;

public class OMPPaymentDetailsDTO implements Serializable{
	private static final long serialVersionUID = -6397463345867570777L;
	
	private int sno;
	private String rodNo;
	private String ackClassification;
	private String ackCategory;
	private String rodClaimType;
	private Long settledAmount;
	private String userName;
	private String paymentMode;
	private String paymentDate;
	private String paymentStatus;
	
	public int getSno() {
		return sno;
	}
	public void setSno(int sno) {
		this.sno = sno;
	}
	public String getRodNo() {
		return rodNo;
	}
	public void setRodNo(String rodNo) {
		this.rodNo = rodNo;
	}
	public String getAckClassification() {
		return ackClassification;
	}
	public void setAckClassification(String ackClassification) {
		this.ackClassification = ackClassification;
	}
	public String getAckCategory() {
		return ackCategory;
	}
	public void setAckCategory(String ackCategory) {
		this.ackCategory = ackCategory;
	}
	public String getRodClaimType() {
		return rodClaimType;
	}
	public void setRodClaimType(String rodClaimType) {
		this.rodClaimType = rodClaimType;
	}
	public Long getSettledAmount() {
		return settledAmount;
	}
	public void setSettledAmount(Long settledAmount) {
		this.settledAmount = settledAmount;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
}
