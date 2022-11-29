package com.shaic.claim.preauth.wizard.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shaic.arch.fields.dto.SelectValue;

public class CoordinatorDTO {

	private Long key;
	
	private Long policyKey;
	
	private Long intimationKey;
	
	private Long preauthKey;
	
	private String requestType;
	
	private String requesterRemarks;
	
	private String viewFile;
	
	private String coordinatorRemarks;
	
	private Date coordinatorReplyDate;
	
	private Boolean refertoCoordinator;
	
	private String refertoCoordinatorFlag;
	
	@NotNull(message = "Please Choose Type of Coordinator Request.")
	private SelectValue typeofCoordinatorRequest;
	
	@NotNull(message = "Please Enter Reason For Referring.")
	@Size(min = 1, message = "Please Enter Reason For Referring.")
	private String reasonForRefering;

	
	public String getRequesterRemarks() {
		return requesterRemarks;
	}

	public void setRequesterRemarks(String requesterRemarks) {
		this.requesterRemarks = requesterRemarks;
	}

	public String getViewFile() {
		return viewFile;
	}

	public void setViewFile(String viewFile) {
		this.viewFile = viewFile;
	}

	public String getCoordinatorRemarks() {
		return coordinatorRemarks;
	}

	public void setCoordinatorRemarks(String coordinatorRemarks) {
		this.coordinatorRemarks = coordinatorRemarks;
	}
	
	public SelectValue getTypeofCoordinatorRequest() {
		return typeofCoordinatorRequest;
	}

	public void setTypeofCoordinatorRequest(SelectValue typeofCoordinatorRequest) {
		this.typeofCoordinatorRequest = typeofCoordinatorRequest;
		
		this.requestType= typeofCoordinatorRequest !=null ? typeofCoordinatorRequest.getValue() : null;
	}

	public String getReasonForRefering() {
		return reasonForRefering;
	}

	public void setReasonForRefering(String reasonForReferring) {
		this.reasonForRefering = reasonForReferring;
	}

	public Boolean getRefertoCoordinator() {
		return refertoCoordinator;
	}

	public void setRefertoCoordinator(Boolean refertoCoordinator) {
		this.refertoCoordinator = refertoCoordinator;
	
		refertoCoordinatorFlag = this.refertoCoordinator != null && this.refertoCoordinator ? "Y" : "N";
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getPolicyKey() {
		return policyKey;
	}

	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public Long getPreauthKey() {
		return preauthKey;
	}

	public void setPreauthKey(Long preauthKey) {
		this.preauthKey = preauthKey;
	}

	public String getRequestType() {
		return requestType;
	}
	
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public Date getCoordinatorReplyDate() {
		return coordinatorReplyDate;
	}

	public void setCoordinatorReplyDate(Date coordinatorReplyDate) {
		this.coordinatorReplyDate = coordinatorReplyDate;
	}

	public String getRefertoCoordinatorFlag() {
		return refertoCoordinatorFlag;
	}

	public void setRefertoCoordinatorFlag(String refertoCoordinatorFlag) {
		this.refertoCoordinatorFlag = refertoCoordinatorFlag;
	}
	
}
