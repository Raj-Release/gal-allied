package com.shaic.claim.OMPTpaservicechargesmaker.search;

import java.io.Serializable;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class OMPTpaServiceChargesMakerFormDto extends AbstractSearchDTO implements Serializable {
	
	
	private String tpaType;
	private String paymentPeriod;
	private String invoiceAmount;
	private String invoiceNumber;
	private String invoiceDate;
	private String quarterlyPremium;
	private String applicableQuarterly;
	private String applicableQpremium;
	private String suggestedAmount;
	private String conversionRate;
	private String suggestedAmountInr;
	
	public String getTpaType() {
		return tpaType;
	}
	public void setTpaType(String tpaType) {
		this.tpaType = tpaType;
	}
	public String getPaymentPeriod() {
		return paymentPeriod;
	}
	public void setPaymentPeriod(String paymentPeriod) {
		this.paymentPeriod = paymentPeriod;
	}
	public String getInvoiceAmount() {
		return invoiceAmount;
	}
	public void setInvoiceAmount(String invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getQuarterlyPremium() {
		return quarterlyPremium;
	}
	public void setQuarterlyPremium(String quarterlyPremium) {
		this.quarterlyPremium = quarterlyPremium;
	}
	public String getApplicableQuarterly() {
		return applicableQuarterly;
	}
	public void setApplicableQuarterly(String applicableQuarterly) {
		this.applicableQuarterly = applicableQuarterly;
	}
	public String getApplicableQpremium() {
		return applicableQpremium;
	}
	public void setApplicableQpremium(String applicableQpremium) {
		this.applicableQpremium = applicableQpremium;
	}
	public String getSuggestedAmount() {
		return suggestedAmount;
	}
	public void setSuggestedAmount(String suggestedAmount) {
		this.suggestedAmount = suggestedAmount;
	}
	public String getConversionRate() {
		return conversionRate;
	}
	public void setConversionRate(String conversionRate) {
		this.conversionRate = conversionRate;
	}
	public String getSuggestedAmountInr() {
		return suggestedAmountInr;
	}
	public void setSuggestedAmountInr(String suggestedAmountInr) {
		this.suggestedAmountInr = suggestedAmountInr;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	private String remarks;
	
	

}
