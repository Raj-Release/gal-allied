package com.shaic.restservices.bancs.triggerPartyEndorsement;

import java.util.Date;

public class TriggerPartyEndorsementResponse {
	private Integer errorCode;
	
	private String errorDescription;
	
	private String partyCode;
	
	private Date endorsementEffectiveDate;
	
	private String status;

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public String getPartyCode() {
		return partyCode;
	}

	public void setPartyCode(String partyCode) {
		this.partyCode = partyCode;
	}

	public Date getEndorsementEffectiveDate() {
		return endorsementEffectiveDate;
	}

	public void setEndorsementEffectiveDate(Date endorsementEffectiveDate) {
		this.endorsementEffectiveDate = endorsementEffectiveDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
		

}
