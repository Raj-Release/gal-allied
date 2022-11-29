package com.shaic.claim.pedrequest.view;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;
import com.vaadin.ui.Button;

public class ViewPEDQueryDTO extends AbstractSearchDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SelectValue pedSuggestion;

	private String nameofPED;

	private String remarks;

	private Date repudiationLetterDate;
	
	private String requestorId;
	
	private Date requestedDate;
	
	private String requestStatus;
	
	private Button viewDetails;

	public SelectValue getPedSuggestion() {
		return pedSuggestion;
	}

	public void setPedSuggestion(SelectValue pedSuggestion) {
		this.pedSuggestion = pedSuggestion;
	}

	public String getNameofPED() {
		return nameofPED;
	}

	public void setNameofPED(String nameofPED) {
		this.nameofPED = nameofPED;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getRepudiationLetterDate() {
		return repudiationLetterDate;
	}

	public void setRepudiationLetterDate(Date repudiationLetterDate) {
		this.repudiationLetterDate = repudiationLetterDate;
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

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

	public Button getViewDetails() {
		return viewDetails;
	}

	public void setViewDetails(Button viewDetails) {
		this.viewDetails = viewDetails;
	}	

}
