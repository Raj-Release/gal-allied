package com.shaic.restservices.bancs.tiggerEndorsement;

import java.util.Date;

public class TriggerEndorsementResponse {
	private Integer erroCode;
	
	private String errorMessage;
	
	private Date endorsementEffevtiveDate;
	
	private String endorsementStatus;
	
	public Integer getErroCode() {
		return erroCode;
	}
	public void setErroCode(Integer erroCode) {
		this.erroCode = erroCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public Date getEndorsementEffevtiveDate() {
		return endorsementEffevtiveDate;
	}
	public void setEndorsementEffevtiveDate(Date endorsementEffevtiveDate) {
		this.endorsementEffevtiveDate = endorsementEffevtiveDate;
	}
	public String getEndorsementStatus() {
		return endorsementStatus;
	}
	public void setEndorsementStatus(String endorsementStatus) {
		this.endorsementStatus = endorsementStatus;
	}
	
	

}
