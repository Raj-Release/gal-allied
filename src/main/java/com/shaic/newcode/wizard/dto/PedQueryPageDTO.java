package com.shaic.newcode.wizard.dto;

import java.util.Date;

public class PedQueryPageDTO {
	
	private String pedSuggestion;
	
	private String pedName;
	
	private Date repudiationLetterDate;
	
	private String remarks;
	
	private String requestorId;
	
	private Date requestedDate;
	
	private String requestedStatus;

	public String getPedSuggestion() {
		return pedSuggestion;
	}

	public void setPedSuggestion(String pedSuggestion) {
		this.pedSuggestion = pedSuggestion;
	}

	public String getPedName() {
		return pedName;
	}

	public void setPedName(String pedName) {
		this.pedName = pedName;
	}

	public Date getRepudiationLetterDate() {
		return repudiationLetterDate;
	}

	public void setRepudiationLetterDate(Date repudiationLetterDate) {
		this.repudiationLetterDate = repudiationLetterDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRequestorId() {
		return requestorId;
	}

	public void setRequestorId(String requestorId) {
		this.requestorId = requestorId;
	}

	public Date getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
	}

	public String getRequestedStatus() {
		return requestedStatus;
	}

	public void setRequestedStatus(String requestedStatus) {
		this.requestedStatus = requestedStatus;
	}
	
	

}
