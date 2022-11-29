package com.shaic.claim.pedrequest.process;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.pedrequest.view.ViewPEDTableDTO;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;
import com.shaic.domain.InsuredPedDetails;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.OptionGroup;


public class PEDRequestDetailsProcessDTO extends AbstractSearchDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO oldPedEndorsementDTO;
	
	private Long key;

	@NotNull(message="Please Select PED Suggestion")
	private SelectValue pedSuggestion;

	@NotNull(message="Please Enter PED Name")
	@Size(min = 1 , message = "Please Enter PED Name")
	private String nameofPED;
	
	private OptionGroup select;

	@NotNull(message="Please Enter PED Remarks")
	@Size(min = 1 , message = "Please Enter PED Remarks")
	private String remarks;

	@NotNull(message = "Please Enter Query Remarks.")
	@Size(min = 1 , message = "Please Enter Query Remarks.")
	private String queryRemarks;

	@NotNull(message="Please Enter specialistType")
	private SelectValue specialistType;
	
	@NotNull(message="Please Enter WatchList Reason")
	private SelectValue watchListReason;

	private String uploadFile;

	@NotNull(message="Please Enter Reason for Referring")
	@Size(min = 1 , message = "Please Enter Reason for Referring")
	private String reasonforReferring;

	@NotNull(message="Please Enter Review Remarks")
	@Size(min = 1 , message = "Please Enter Review Remarks")
	private String sendApproveRemarks;
	
    private String pedSuggestionName;
	
	private SelectValue pedName;
	
	@NotNull(message="Please select Repudiation Letter")
	private Date repudiationLetterDate;
	
	private String approvalRemarks;
	
	@NotNull(message="Please Enter WatchList Remarks")
	@Size(min = 1 , message = "Please Enter WatchList Remarks")
	private String watchListRemarks;
	
	private String rejectionRemarks;
	
	private String requestorId;
	
	private String pedInitiateId;
	
	private String requestedDate;
	
	private String requestStatus;
	
	private Button viewDetails;
	
	private String tokenName;
	
	private String fileName;
	
	private Boolean isEditPED = false;
	
	private Long currentPED;
	
	private Boolean isWatchList = false;
	
	private Boolean isAlreadyWatchList = false;
	
	private List<InsuredPedDetails> insuredPedDetails = new ArrayList<InsuredPedDetails>();
	
	
	private List<ViewPEDTableDTO> pedInitiateDetails = new ArrayList<ViewPEDTableDTO>();
	
	private List<ViewPEDTableDTO> deletedDiagnosis = new ArrayList<ViewPEDTableDTO>();

	public com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO getOldPedEndorsementDTO() {
		return oldPedEndorsementDTO;
	}

	public void setOldPedEndorsementDTO(
			com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO oldPedEndorsementDTO) {
		this.oldPedEndorsementDTO = oldPedEndorsementDTO;
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
	
	public SelectValue getWatchListReason() {
		return watchListReason;
	}

	public void setWatchListReason(SelectValue watchListReason) {
		this.watchListReason = watchListReason;
	}

	public String getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(String uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getReasonforReferring() {
		return reasonforReferring;
	}

	public void setReasonforReferring(String reasonforReferring) {
		this.reasonforReferring = reasonforReferring;
	}

	public String getSendApproveRemarks() {
		return sendApproveRemarks;
	}

	public void setSendApproveRemarks(String sendApproveRemarks) {
		this.sendApproveRemarks = sendApproveRemarks;
	}

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

	public String getApprovalRemarks() {
		return approvalRemarks;
	}

	public void setApprovalRemarks(String approvalRemarks) {
		this.approvalRemarks = approvalRemarks;
	}
	
	public String getWatchListRemarks() {
		return watchListRemarks;
	}

	public void setWatchListRemarks(String watchListRemarks) {
		this.watchListRemarks = watchListRemarks;
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

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Button getViewDetails() {
		return viewDetails;
	}

	public void setViewDetails(Button viewDetails) {
		this.viewDetails = viewDetails;
	}

	public SelectValue getPedName() {
		return pedName;
	}

	public void setPedName(SelectValue pedName) {
		this.pedName = pedName;
	}

	public OptionGroup getSelect() {
		return select;
	}

	public void setSelect(OptionGroup select) {
		this.select = select;
	}

	public String getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(String requestedDate) {
		this.requestedDate = requestedDate;
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

	public Long getCurrentPED() {
		return currentPED;
	}

	public void setCurrentPED(Long currentPED) {
		this.currentPED = currentPED;
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

	

}
