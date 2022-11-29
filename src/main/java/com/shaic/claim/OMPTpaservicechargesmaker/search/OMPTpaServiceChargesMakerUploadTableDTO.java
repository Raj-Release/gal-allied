package com.shaic.claim.OMPTpaservicechargesmaker.search;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class OMPTpaServiceChargesMakerUploadTableDTO extends AbstractTableDTO  implements Serializable{
	
	private String serialNo;
	private String typeofTpa;
	private String conversionRate;
	private String suggestedAmount;
	private String document;
	private String purpose;
	private String status;
	
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getTypeofTpa() {
		return typeofTpa;
	}
	public void setTypeofTpa(String typeofTpa) {
		this.typeofTpa = typeofTpa;
	}
	public String getConversionRate() {
		return conversionRate;
	}
	public void setConversionRate(String conversionRate) {
		this.conversionRate = conversionRate;
	}
	public String getSuggestedAmount() {
		return suggestedAmount;
	}
	public void setSuggestedAmount(String suggestedAmount) {
		this.suggestedAmount = suggestedAmount;
	}
	public String getDocument() {
		return document;
	}
	public void setDocument(String document) {
		this.document = document;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
