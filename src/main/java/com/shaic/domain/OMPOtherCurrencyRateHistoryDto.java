package com.shaic.domain;

import java.util.Date;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class OMPOtherCurrencyRateHistoryDto extends AbstractSearchDTO{
	
	private String serialNumber;
	
	private String otherurrencyCode;
	private String currencyName;
	private String country;
	private String currencyRate;
	private Date date;
	private String userName;
	private String userId;
	private String processingStage;
/*	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}*/
	public String getOtherurrencyCode() {
		return otherurrencyCode;
	}
	public void setOtherurrencyCode(String otherurrencyCode) {
		this.otherurrencyCode = otherurrencyCode;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCurrencyRate() {
		return currencyRate;
	}
	public void setCurrencyRate(String currencyRate) {
		this.currencyRate = currencyRate;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getProcessingStage() {
		return processingStage;
	}
	public void setProcessingStage(String processingStage) {
		this.processingStage = processingStage;
	}
	
}

