package com.shaic.claim.OMPSearchAndCreateintimation.wizard.pages;

import java.io.Serializable;

public class OMPProposerDetailsDTO implements Serializable{
	private static final long serialVersionUID = -6397463345867570777L;
	
	private int sno;
	private String ackNo;
	private String rodNo;
	private String ackClassification;
	private String ackCategory;
	private String rodClaimType;
	private Long totalAmount;
	private Long finalAmount;
	private String approvedDate;
	private String clmStatus;
	
	public int getSno() {
		return sno;
	}
	public void setSno(int sno) {
		this.sno = sno;
	}
	public String getAckNo() {
		return ackNo;
	}
	public void setAckNo(String ackNo) {
		this.ackNo = ackNo;
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
	public Long getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Long getFinalAmount() {
		return finalAmount;
	}
	public void setFinalAmount(Long finalAmount) {
		this.finalAmount = finalAmount;
	}
	public String getApprovedDate() {
		return approvedDate;
	}
	public void setApprovedDate(String approvedDate) {
		this.approvedDate = approvedDate;
	}
	public String getClmStatus() {
		return clmStatus;
	}
	public void setClmStatus(String clmStatus) {
		this.clmStatus = clmStatus;
	}
}
