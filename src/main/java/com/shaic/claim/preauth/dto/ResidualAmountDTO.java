package com.shaic.claim.preauth.dto;

import com.shaic.arch.fields.dto.SelectValue;

public class ResidualAmountDTO {

	public Double getAmountConsideredAmount() {
		return amountConsideredAmount;
	}

	public void setAmountConsideredAmount(Double amountConsideredAmount) {
		this.amountConsideredAmount = amountConsideredAmount;
	}

	public Double getMinimumAmount() {
		return minimumAmount;
	}

	public void setMinimumAmount(Double minimumAmount) {
		this.minimumAmount = minimumAmount;
	}

	public Double getCopayPercentage() {
		return copayPercentage;
	}

	public void setCopayPercentage(Double copayPercentage) {
		this.copayPercentage = copayPercentage;
	}

	public Double getCopayAmount() {
		return copayAmount;
	}

	public void setCopayAmount(Double copayAmount) {
		this.copayAmount = copayAmount;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	private Long key;
	
	private Long preauthKey;
	
	private Double approvedAmount = 0d;
	
	private String remarks;
	
	private Double amountConsideredAmount = 0d;
	
	private Double minimumAmount = 0d;
	
	private Double copayPercentage = 0d;
	
	private Double copayAmount = 0d;
	
	private Double netAmount = 0d;
	
	private Double netApprovedAmount = 0d;
	
	private String considerForPayment;
	
	private SelectValue coPayTypeId;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getPreauthKey() {
		return preauthKey;
	}

	public void setPreauthKey(Long preauthKey) {
		this.preauthKey = preauthKey;
	}

	public Double getApprovedAmount() {
		return approvedAmount;
	}

	public void setApprovedAmount(Double approvedAmount) {
		this.approvedAmount = approvedAmount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Double getNetApprovedAmount() {
		return netApprovedAmount;
	}

	public void setNetApprovedAmount(Double netApprovedAmount) {
		this.netApprovedAmount = netApprovedAmount;
	}

	public String getConsiderForPayment() {
		return considerForPayment;
	}

	public void setConsiderForPayment(String considerForPayment) {
		this.considerForPayment = considerForPayment;
	}

	public SelectValue getCoPayTypeId() {
		return coPayTypeId;
	}

	public void setCoPayTypeId(SelectValue coPayTypeId) {
		this.coPayTypeId = coPayTypeId;
	}
}
