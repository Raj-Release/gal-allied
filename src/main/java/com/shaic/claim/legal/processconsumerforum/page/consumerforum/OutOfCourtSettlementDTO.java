package com.shaic.claim.legal.processconsumerforum.page.consumerforum;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class OutOfCourtSettlementDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7026640940383563008L;

	//@NotNull(message="Please Select Out Of Court Settlement")
	private Boolean outOfCourtSettlement;

	@NotNull(message="Please Select Limitation Date")
	private Date limitOfPeriod;
	
	@NotNull(message="Please Enter Reason")
	@Size(min = 1 , message = "Please Enter Reason")
	private String reason;
	
	@NotNull(message="Please Select Settlement Date")
	private Date settlementDate;
	
	@NotNull(message="Please Select Offered Amount")
	private Double offeredAmt;
	
	@NotNull(message="Please Select Amount Saved")
	private Double amtSaved;
	
	@NotNull(message="Please Select Settled Amount")
	private Double settledAmount;
	
	private Boolean consentLetterSent;

	public Boolean getOutOfCourtSettlement() {
		return outOfCourtSettlement;
	}

	public void setOutOfCourtSettlement(Boolean outOfCourtSettlement) {
		this.outOfCourtSettlement = outOfCourtSettlement;
	}

	public Date getLimitOfPeriod() {
		return limitOfPeriod;
	}

	public void setLimitOfPeriod(Date limitOfPeriod) {
		this.limitOfPeriod = limitOfPeriod;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}

	public Double getOfferedAmt() {
		return offeredAmt;
	}

	public void setOfferedAmt(Double offeredAmt) {
		this.offeredAmt = offeredAmt;
	}

	public Double getAmtSaved() {
		return amtSaved;
	}

	public void setAmtSaved(Double amtSaved) {
		this.amtSaved = amtSaved;
	}

	public Boolean getConsentLetterSent() {
		return consentLetterSent;
	}

	public void setConsentLetterSent(Boolean consentLetterSent) {
		this.consentLetterSent = consentLetterSent;
	}

	public Double getSettledAmount() {
		return settledAmount;
	}

	public void setSettledAmount(Double settledAmount) {
		this.settledAmount = settledAmount;
	}


}
