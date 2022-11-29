package com.shaic.claim.pedrequest.approve;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.pedrequest.view.ViewPEDTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.preauth.PreExistingDisease;
import com.vaadin.ui.Button;

public class PEDRequestDetailsApproveDTO extends AbstractSearchDTO implements Serializable{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long preauthKey;
	
	private String processorRemarks;
	
	private long claimKey;
	
	private long intimationKey;
	
	private long policyKey;
	
	private Button viewDetails;
	
	private PreauthDTO preAuthDto = new PreauthDTO();
	
	@NotNull(message="Please Enter Escalate Remarks")
	@Size(min = 1, message = "Please Enter Escalate Remarks")
	private String escalateRemarks;
	
	private List<InsuredPedDetails> insuredPedDetails = new ArrayList<InsuredPedDetails>();
	
	@NotNull(message="Please Select Ped Suggestion")
	private SelectValue pedSuggestion;
	
	@NotNull(message="Please Select Add to WatchList")
	private SelectValue watchList;
	
	private SelectValue approvalPedSuggestion;
	
	private String pedSuggestionName;
	
	/*@NotNull(message="Please Enter PED Name")
	@Size(min = 1, message = "Please Enter PED Name")*/
	private String pedApprovalName;
	
	@NotNull(message="Please Select Repudiation Letter Date")
	private Date repudiationLetterDate;
	
	private String tokenName;
	
	private String fileName;
	
	@NotNull(message="Please Enter PED Remarks")
	@Size(min = 1, message = "Please Enter PED Remarks")
	private String remarks;
	
	@NotNull(message="Please Enter Remarks")
	@Size(min = 1, message = "Please Enter Remarks")
	private String approvalRemarks;
	
	@NotNull(message="Please Enter WatchList Remarks")
	@Size(min = 1, message = "Please Enter WatchList Remarks")
	private String watchlistRemarks;
	
	private String uploadFile;
	
	@NotNull(message="Please Enter Rejection Remarks")
	@Size(min = 1, message = "Please Enter Rejection Remarks")
	private String rejectionRemarks;
	
	private String requestorId;
	
	private String pedInitiateId;
	
	private Date requestedDate;
	
	private String requestStatus;
	
	@NotNull(message="Please Enter Reason for Referring")
	private String reasonforReferring;
	
	private String specialistRemarks;
	
	private String replyRemarks;
	
	private String officeCode;
	
	@NotNull(message="Please Enter Query Remarks")
	private String queryRemarks;
	
	@NotNull(message="Please Select Specialist type")
	private SelectValue specialistType;
	
	private Boolean isEditPED = false;
	
	private Boolean isWatchList = false;
	
	private Boolean isAlreadyWatchList = false;
	
	private List<ViewPEDTableDTO> pedInitiateDetails = new ArrayList<ViewPEDTableDTO>();
	
	private List<ViewPEDTableDTO> deletedDiagnosis = new ArrayList<ViewPEDTableDTO>();
	
	private Boolean isPolicyValidate;
	
	private List<PreExistingDisease> approvedPedDetails = new ArrayList<PreExistingDisease>();
	
	public PreauthDTO getPreAuthDto() {
		return preAuthDto;
	}

	public void setPreAuthDto(PreauthDTO preAuthDto) {
		this.preAuthDto = preAuthDto;
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
	
	public SelectValue getWatchList() {
		return watchList;
	}

	public void setWatchList(SelectValue watchList) {
		this.watchList = watchList;
	}

	public String getPedSuggestionName() {
		return pedSuggestionName;
	}

	public void setPedSuggestionName(String pedSuggestionName) {
		this.pedSuggestionName = pedSuggestionName;
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
	
	public String getWatchlistRemarks() {
		return watchlistRemarks;
	}

	public void setWatchlistRemarks(String watchlistRemarks) {
		this.watchlistRemarks = watchlistRemarks;
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

	public String getProcessorRemarks() {
		return processorRemarks;
	}

	public void setProcessorRemarks(String processorRemarks) {
		this.processorRemarks = processorRemarks;
	}

	public String getQueryRemarks() {
		return queryRemarks;
	}

	public void setQueryRemarks(String queryRemarks) {
		this.queryRemarks = queryRemarks;
	}

	public SelectValue getSpecialistType() {
		return specialistType;
	}

	public void setSpecialistType(SelectValue specialistType) {
		this.specialistType = specialistType;
	}

	public String getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(String uploadFile) {
		this.uploadFile = uploadFile;
	}

	public SelectValue getApprovalPedSuggestion() {
		return approvalPedSuggestion;
	}

	public void setApprovalPedSuggestion(SelectValue approvalPedSuggestion) {
		this.approvalPedSuggestion = approvalPedSuggestion;
	}

	public String getPedApprovalName() {
		return pedApprovalName;
	}

	public void setPedApprovalName(String pedApprovalName) {
		this.pedApprovalName = pedApprovalName;
	}

	public String getTokenName() {
		return tokenName;
	}

	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<ViewPEDTableDTO> getPedInitiateDetails() {
		return pedInitiateDetails;
	}

	public void setPedInitiateDetails(List<ViewPEDTableDTO> pedInitiateDetails) {
		this.pedInitiateDetails = pedInitiateDetails;
	}

	public Boolean getIsEditPED() {
		return isEditPED;
	}

	public void setIsEditPED(Boolean isEditPED) {
		this.isEditPED = isEditPED;
	}

	public List<ViewPEDTableDTO> getDeletedDiagnosis() {
		return deletedDiagnosis;
	}

	public void setDeletedDiagnosis(List<ViewPEDTableDTO> deletedDiagnosis) {
		this.deletedDiagnosis = deletedDiagnosis;
	}

	public Boolean getIsWatchList() {
		return isWatchList;
	}

	public void setIsWatchList(Boolean isWatchList) {
		this.isWatchList = isWatchList;
	}

	public Boolean getIsAlreadyWatchList() {
		return isAlreadyWatchList;
	}

	public void setIsAlreadyWatchList(Boolean isAlreadyWatchList) {
		this.isAlreadyWatchList = isAlreadyWatchList;
	}

	public List<InsuredPedDetails> getInsuredPedDetails() {
		return insuredPedDetails;
	}

	public void setInsuredPedDetails(List<InsuredPedDetails> insuredPedDetails) {
		this.insuredPedDetails = insuredPedDetails;
	}

	public String getEscalateRemarks() {
		return escalateRemarks;
	}

	public void setEscalateRemarks(String escalateRemarks) {
		this.escalateRemarks = escalateRemarks;
	}

	public Boolean getIsPolicyValidate() {
		return isPolicyValidate;
	}

	public void setIsPolicyValidate(Boolean isPolicyValidate) {
		this.isPolicyValidate = isPolicyValidate;
	}

	public List<PreExistingDisease> getApprovedPedDetails() {
		return approvedPedDetails;
	}

	public void setApprovedPedDetails(List<PreExistingDisease> approvedPedDetails) {
		this.approvedPedDetails = approvedPedDetails;
	}

	

}
