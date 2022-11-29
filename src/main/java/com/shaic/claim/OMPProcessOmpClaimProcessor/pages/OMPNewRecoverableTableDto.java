package com.shaic.claim.OMPProcessOmpClaimProcessor.pages;

import java.io.Serializable;
import java.util.Date;

public class OMPNewRecoverableTableDto implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer serialNumber;
	
	private Date dateofRecovery;
	
	private Double amountRecoveredInr;
	
	private String nameOfNegotiatior;
	
	private Double amountRecoveredUsd;
	
	private Double agreedAmount =0d;
	
	private String remarks;
	
	private Long key;
	
	private Long rodKey;
	
	private Long claimKey;
	
	private String sendToAccounts;

	public Integer getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getNameOfNegotiatior() {
		return nameOfNegotiatior;
	}

	public void setNameOfNegotiatior(String nameOfNegotiatior) {
		this.nameOfNegotiatior = nameOfNegotiatior;
	}

	
	public Double getAgreedAmount() {
		return agreedAmount;
	}

	public void setAgreedAmount(Double agreedAmount) {
		this.agreedAmount = agreedAmount;
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

	public Date getDateofRecovery() {
		return dateofRecovery;
	}

	public void setDateofRecovery(Date dateofRecovery) {
		this.dateofRecovery = dateofRecovery;
	}

	public Double getAmountRecoveredInr() {
		return amountRecoveredInr;
	}

	public void setAmountRecoveredInr(Double amountRecoveredInr) {
		this.amountRecoveredInr = amountRecoveredInr;
	}

	public Double getAmountRecoveredUsd() {
		return amountRecoveredUsd;
	}

	public void setAmountRecoveredUsd(Double amountRecoveredUsd) {
		this.amountRecoveredUsd = amountRecoveredUsd;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getSendToAccounts() {
		return sendToAccounts;
	}

	public void setSendToAccounts(String sendToAccounts) {
		this.sendToAccounts = sendToAccounts;
	}

	
	

}
