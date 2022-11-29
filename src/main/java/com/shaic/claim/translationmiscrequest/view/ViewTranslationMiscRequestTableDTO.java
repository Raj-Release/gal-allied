package com.shaic.claim.translationmiscrequest.view;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class ViewTranslationMiscRequestTableDTO extends AbstractTableDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	



	private Date requestedDate;
	private Date repliedDate;
	private String requestType;
	private String requestorRole;
	private String requestorNameID;
	private String requestorRemarks;
	private String coordinatorRepliedID;
	private String coordinatorRemarks;
	
	public Date getRequestedDate() {
		return requestedDate;
	}
	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
	}
	public Date getRepliedDate() {
		return repliedDate;
	}
	public void setRepliedDate(Date repliedDate) {
		this.repliedDate = repliedDate;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getRequestorRole() {
		return requestorRole;
	}
	public void setRequestorRole(String requestorRole) {
		this.requestorRole = requestorRole;
	}
	public String getRequestorNameID() {
		return requestorNameID;
	}
	public void setRequestorNameID(String requestorNameID) {
		this.requestorNameID = requestorNameID;
	}
	public String getRequestorRemarks() {
		return requestorRemarks;
	}
	public void setRequestorRemarks(String requestorRemarks) {
		this.requestorRemarks = requestorRemarks;
	}
	public String getCoordinatorRepliedID() {
		return coordinatorRepliedID;
	}
	public void setCoordinatorRepliedID(String coordinatorRepliedID) {
		this.coordinatorRepliedID = coordinatorRepliedID;
	}
	public String getCoordinatorRemarks() {
		return coordinatorRemarks;
	}
	public void setCoordinatorRemarks(String coordinatorRemarks) {
		this.coordinatorRemarks = coordinatorRemarks;
	}

	
}
