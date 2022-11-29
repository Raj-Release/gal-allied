package com.shaic.newcode.wizard.dto;

import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.ui.Button;

public class PedRequestProcessDTO {
	
	
private long key;
	
	private long preauthKey;
	
	private long claimKey;
	
	private long intimationKey;
	
	private long policyKey;
	
	private Button viewDetails;
	
	private SelectValue pedSuggestion;
	
	private String pedSuggestionName;
	
	private String pedName;
	
	private Date repudiationLetterDate;
	
	private String remarks;
	
	private String approvalRemarks;
	
	private String rejectionRemarks;
	
	private String requestorId;
	
	private String pedInitiateId;
	
	private Date requestedDate;
	
	private String requestStatus;
	
	private String reasonforReferring;
	
	private String specialistRemarks;
	
	private String replyRemarks;
	
	private String officeCode;

	public long getKey() {
		return key;
	}

	public void setKey(long key) {
		this.key = key;
	}

	public long getPreauthKey() {
		return preauthKey;
	}

	public void setPreauthKey(long preauthKey) {
		this.preauthKey = preauthKey;
	}

	public long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(long claimKey) {
		this.claimKey = claimKey;
	}

	public long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public long getPolicyKey() {
		return policyKey;
	}

	public void setPolicyKey(long policyKey) {
		this.policyKey = policyKey;
	}

	public Button getViewDetails() {
		return viewDetails;
	}

	public void setViewDetails(Button viewDetails) {
		this.viewDetails = viewDetails;
	}

	public SelectValue getPedSuggestion() {
		return pedSuggestion;
	}

	public void setPedSuggestion(SelectValue pedSuggestion) {
		this.pedSuggestion = pedSuggestion;
	}

	public String getPedSuggestionName() {
		return pedSuggestionName;
	}

	public void setPedSuggestionName(String pedSuggestionName) {
		this.pedSuggestionName = pedSuggestionName;
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

	public String getApprovalRemarks() {
		return approvalRemarks;
	}

	public void setApprovalRemarks(String approvalRemarks) {
		this.approvalRemarks = approvalRemarks;
	}

	public String getRejectionRemarks() {
		return rejectionRemarks;
	}

	public void setRejectionRemarks(String rejectionRemarks) {
		this.rejectionRemarks = rejectionRemarks;
	}

	public String getRequestorId() {
		return requestorId;
	}

	public void setRequestorId(String requestorId) {
		this.requestorId = requestorId;
	}

	public String getPedInitiateId() {
		return pedInitiateId;
	}

	public void setPedInitiateId(String pedInitiateId) {
		this.pedInitiateId = pedInitiateId;
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

	public String getReasonforReferring() {
		return reasonforReferring;
	}

	public void setReasonforReferring(String reasonforReferring) {
		this.reasonforReferring = reasonforReferring;
	}

	public String getSpecialistRemarks() {
		return specialistRemarks;
	}

	public void setSpecialistRemarks(String specialistRemarks) {
		this.specialistRemarks = specialistRemarks;
	}

	public String getReplyRemarks() {
		return replyRemarks;
	}

	public void setReplyRemarks(String replyRemarks) {
		this.replyRemarks = replyRemarks;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

}
