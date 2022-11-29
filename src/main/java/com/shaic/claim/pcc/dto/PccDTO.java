package com.shaic.claim.pcc.dto;

import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.dto.UploadedDocumentsDTO;

public class PccDTO {
	
	private Long pccKey;
	
	private Long negotiatioAmount;
	
	private Long savedAmount;
	
	private String approveRemarks;
	
	private SelectValue userRoleAssigned;
	
	private SelectValue userNameAssigned;
	
	private String queryRemarks;
	
	private String disApproveRemarks;
	
	private String responseRemarks;
	
	private String replyRemarks;
	
	private Boolean isNegotiation;
	
	private String assignRemarks;
	
	private String remarksforApprove;
	
	private String remarkForDisapprove;
	
	private String remarkForResponse;
	
	private String remarksforQuery;
	
	private Boolean isApproved;
	
	private Boolean isQueryRaised;
	
    private Boolean isDisapproved;
	
	private Boolean isResponse;
	
	private String remarksAssignforZMH;
	
	private String remarksNegotioanforZMH;
	
	private Boolean isAssign;

	private String fileUploadRemarks;
	
	private String intimationNumber;
	
	private SelectValue assginedHRMCoordinator;
	
	private SelectValue proposedHRMCoordinator;
	
	private DocumentDetailsDTO documentDetails;
	
	private List<PCCUploadDocumentsDTO> uploadedFileTableDto; 	
	
	private List<PCCUploadDocumentsDTO> uploadedNDeletedFileListDto;
	
	public Long getNegotiatioAmount() {
		return negotiatioAmount;
	}

	public void setNegotiatioAmount(Long negotiatioAmount) {
		this.negotiatioAmount = negotiatioAmount;
	}

	public Long getSavedAmount() {
		return savedAmount;
	}

	public void setSavedAmount(Long savedAmount) {
		this.savedAmount = savedAmount;
	}

	public String getApproveRemarks() {
		return approveRemarks;
	}

	public void setApproveRemarks(String approveRemarks) {
		this.approveRemarks = approveRemarks;
	}

	public String getQueryRemarks() {
		return queryRemarks;
	}

	public void setQueryRemarks(String queryRemarks) {
		this.queryRemarks = queryRemarks;
	}

	public String getDisApproveRemarks() {
		return disApproveRemarks;
	}

	public void setDisApproveRemarks(String disApproveRemarks) {
		this.disApproveRemarks = disApproveRemarks;
	}

	public String getResponseRemarks() {
		return responseRemarks;
	}

	public void setResponseRemarks(String responseRemarks) {
		this.responseRemarks = responseRemarks;
	}

	public String getReplyRemarks() {
		return replyRemarks;
	}

	public void setReplyRemarks(String replyRemarks) {
		this.replyRemarks = replyRemarks;
	}

	public Boolean getIsNegotiation() {
		return isNegotiation;
	}

	public void setIsNegotiation(Boolean isNegotiation) {
		this.isNegotiation = isNegotiation;
	}

	public String getAssignRemarks() {
		return assignRemarks;
	}

	public void setAssignRemarks(String assignRemarks) {
		this.assignRemarks = assignRemarks;
	}

	public Boolean getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}

	public Boolean getIsQueryRaised() {
		return isQueryRaised;
	}

	public void setIsQueryRaised(Boolean isQueryRaised) {
		this.isQueryRaised = isQueryRaised;
	}

	public Long getPccKey() {
		return pccKey;
	}

	public void setPccKey(Long pccKey) {
		this.pccKey = pccKey;
	}

	public SelectValue getUserRoleAssigned() {
		return userRoleAssigned;
	}

	public void setUserRoleAssigned(SelectValue userRoleAssigned) {
		this.userRoleAssigned = userRoleAssigned;
	}

	public SelectValue getUserNameAssigned() {
		return userNameAssigned;
	}

	public void setUserNameAssigned(SelectValue userNameAssigned) {
		this.userNameAssigned = userNameAssigned;
	}

	public String getRemarksforApprove() {
		return remarksforApprove;
	}

	public void setRemarksforApprove(String remarksforApprove) {
		this.remarksforApprove = remarksforApprove;
	}

	public String getRemarksforQuery() {
		return remarksforQuery;
	}

	public void setRemarksforQuery(String remarksforQuery) {
		this.remarksforQuery = remarksforQuery;
	}

	public Boolean getIsDisapproved() {
		return isDisapproved;
	}

	public void setIsDisapproved(Boolean isDisapproved) {
		this.isDisapproved = isDisapproved;
	}

	public Boolean getIsResponse() {
		return isResponse;
	}

	public String getRemarksAssignforZMH() {
		return remarksAssignforZMH;
	}

	public void setRemarksAssignforZMH(String remarksAssignforZMH) {
		this.remarksAssignforZMH = remarksAssignforZMH;
	}

	public String getRemarksNegotioanforZMH() {
		return remarksNegotioanforZMH;
	}

	public void setRemarksNegotioanforZMH(String remarksNegotioanforZMH) {
		this.remarksNegotioanforZMH = remarksNegotioanforZMH;
	}

	public void setIsResponse(Boolean isResponse) {
		this.isResponse = isResponse;
	}

	public String getRemarkForDisapprove() {
		return remarkForDisapprove;
	}

	public void setRemarkForDisapprove(String remarkForDisapprove) {
		this.remarkForDisapprove = remarkForDisapprove;
	}

	public String getRemarkForResponse() {
		return remarkForResponse;
	}

	public void setRemarkForResponse(String remarkForResponse) {
		this.remarkForResponse = remarkForResponse;
	}

	public Boolean getIsAssign() {
		return isAssign;
	}

	public void setIsAssign(Boolean isAssign) {
		this.isAssign = isAssign;
	}

	public String getFileUploadRemarks() {
		return fileUploadRemarks;
	}

	public void setFileUploadRemarks(String fileUploadRemarks) {
		this.fileUploadRemarks = fileUploadRemarks;
	}
	
	public DocumentDetailsDTO getDocumentDetails() {
		return documentDetails;
	}

	public void setDocumentDetails(DocumentDetailsDTO documentDetails) {
		this.documentDetails = documentDetails;
	}

	public List<PCCUploadDocumentsDTO> getUploadedFileTableDto() {
		return uploadedFileTableDto;
	}

	public void setUploadedFileTableDto(
			List<PCCUploadDocumentsDTO> uploadedFileTableDto) {
		this.uploadedFileTableDto = uploadedFileTableDto;
	}

	public List<PCCUploadDocumentsDTO> getUploadedNDeletedFileListDto() {
		return uploadedNDeletedFileListDto;
	}

	public void setUploadedNDeletedFileListDto(
			List<PCCUploadDocumentsDTO> uploadedNDeletedFileListDto) {
		this.uploadedNDeletedFileListDto = uploadedNDeletedFileListDto;
	}

	public String getIntimationNumber() {
		return intimationNumber;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public SelectValue getAssginedHRMCoordinator() {
		return assginedHRMCoordinator;
	}

	public void setAssginedHRMCoordinator(SelectValue assginedHRMCoordinator) {
		this.assginedHRMCoordinator = assginedHRMCoordinator;
	}

	public SelectValue getProposedHRMCoordinator() {
		return proposedHRMCoordinator;
	}

	public void setProposedHRMCoordinator(SelectValue proposedHRMCoordinator) {
		this.proposedHRMCoordinator = proposedHRMCoordinator;
	}

	
	

	
}
