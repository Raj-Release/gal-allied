package com.shaic.claim.OMPProcessNegotiation.pages;

import java.io.Serializable;
import java.util.Date;

public class OMPNegotiationDetailsDTO implements Serializable {
	
	
	
	private Integer serialNumber;
	private Date negotiationReqstDate;
	private Date negotiationCompletDate;
	private String nameOfNegotiatior;
	private Double approvedAmt =0d;
	private Double agreedAmount =0d;
	private Double diffAmt;
	private Double expenseAmt;
	private Double handlingCharges; 
	private String negotiationRemarks;
	private Long key;
	private Long rodKey;
	private Long claimKey;
	

	public Date getNegotiationReqstDate() {
		return negotiationReqstDate;
	}
	public void setNegotiationReqstDate(Date negotiationReqstDate) {
		this.negotiationReqstDate = negotiationReqstDate;
	}
	public Date getNegotiationCompletDate() {
		return negotiationCompletDate;
	}
	public void setNegotiationCompletDate(Date negotiationCompletDate) {
		this.negotiationCompletDate = negotiationCompletDate;
	}
	public String getNameOfNegotiatior() {
		return nameOfNegotiatior;
	}
	public void setNameOfNegotiatior(String nameOfNegotiatior) {
		this.nameOfNegotiatior = nameOfNegotiatior;
	}
	public Double getApprovedAmt() {
		return approvedAmt;
	}
	public void setApprovedAmt(Double approvedAmt) {
		this.approvedAmt = approvedAmt;
	}
	public Double getAgreedAmount() {
		return agreedAmount;
	}
	public void setAgreedAmount(Double agreedAmount) {
		this.agreedAmount = agreedAmount;
	}
	public String getNegotiationRemarks() {
		return negotiationRemarks;
	}
	public void setNegotiationRemarks(String negotiationRemarks) {
		this.negotiationRemarks = negotiationRemarks;
	}
	public Integer getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	public Long getRodKey() {
		return rodKey;
	}
	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}
	public Long getClaimKey() {
		return claimKey;
	}
	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}
	public Double getDiffAmt() {
		return diffAmt;
	}
	public void setDiffAmt(Double diffAmt) {
		this.diffAmt = diffAmt;
	}
	public Double getExpenseAmt() {
		return expenseAmt;
	}
	public void setExpenseAmt(Double expenseAmt) {
		this.expenseAmt = expenseAmt;
	}
	public Double getHandlingCharges() {
		return handlingCharges;
	}
	public void setHandlingCharges(Double handlingCharges) {
		this.handlingCharges = handlingCharges;
	}
	
	
}
