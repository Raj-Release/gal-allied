package com.shaic.claim.preauth.wizard.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.claimhistory.view.PedHistoryDTO;
import com.shaic.claim.pedquery.viewPedDetails.ViewPEDEndoresementDetailsDTO;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.OptionGroup;

public class OldPedEndorsementDTO extends AbstractSearchDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long preauthKey;
	
	private String processorRemarks;
	
	private int sno;
	
	private OptionGroup select;
	
	private long claimKey;
	
	private long intimationKey;
	
	private long policyKey;
	
	private Button viewDetails;
	
	private SelectValue pedSuggestion;
	
	private SelectValue nameOfPed;
	
//	private SelectValue approvalPedSuggestion;
	
	private String viewFile;
	
	private String pedSuggestionName;
	
	@Size(min =1, message="Please Enter Ped Name")
	private String pedName;
	
	private Date repudiationLetterDate;
	
	private String remarks;
	
	private String approvalRemarks;
	
	private String escalateRemarks;
	
	private String uploadFile;
	
	private String rejectionRemarks;
	
	private String requestorId;
	
	private String pedInitiateId;
	
	private String requestedDate;
	
	private String requestStatus;
	
	private String reasonforReferring;
	
	private Long statusKey;
	
	private Boolean isWatchList = false;
	
	
	private String watchListFlag;
	
	@NotNull(message="Please Enter specialist Remarks")
	@Size(min =1, message="Please Enter Specialist Remarks")
	private String specialistRemarks;
	
	@NotNull(message = "Please Enter Reply Remarks.")
	@Size(min =1, message="Please Enter Reply Remarks")
	private String replyRemarks;
	
	private String officeCode;
	
	private String queryRemarks;
	
	private SelectValue specialistType;
	
	private String fileName;
	
	private String tokenName;
	
	private Boolean isEditPED = false;
	
	private Long currentPED;
	
	
	private List<NewInitiatePedEndorsementDTO> newInitiatePedEndorsementDto;
	
	private List<ViewPEDEndoresementDetailsDTO> viewPEDEndoresementDetailsDTO;
	
	private Boolean isReviewer;
	
	private String intimationNo;
	
	private String pedHistoryJson;
	
	private String pedEndorsementJson;
	
	private String pedAdditionalJson;
	
	private String pedAmmendedJson;
	
	private String pedReviewedJson;
	
	private String pedProcessedJson;
	
	private String pedApprovedJson;
	
	private String repLetterDate;
	
	private String pedEffectiveFromDate;
	
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
	
	public int getSno() {
		return sno;
	}

	public void setSno(int sno) {
		this.sno = sno;
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

	

	public String getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(String requestedDate) {
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
	public Button getViewDetails() {
		return viewDetails;
	}
	public void setViewDetails(Button viewDetails) {
		this.viewDetails = viewDetails;
	}
	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public String getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(String uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getViewFile() {
		return viewFile;
	}

	public void setViewFile(String viewFile) {
		this.viewFile = viewFile;
	}

	public List<NewInitiatePedEndorsementDTO> getNewInitiatePedEndorsementDto() {
		return newInitiatePedEndorsementDto;
	}

	public void setNewInitiatePedEndorsementDto(
			List<NewInitiatePedEndorsementDTO> newInitiatePedEndorsementDto) {
		this.newInitiatePedEndorsementDto = newInitiatePedEndorsementDto;
	}
//	public SelectValue getApprovalPedSuggestion() {
//		return approvalPedSuggestion;
//	}
//
//	public void setApprovalPedSuggestion(SelectValue approvalPedSuggestion) {
//		this.approvalPedSuggestion = approvalPedSuggestion;
//	}

	public OptionGroup getSelect() {
		return select;
	}

	public void setSelect(OptionGroup select) {
		this.select = select;
	}

	public SelectValue getNameOfPed() {
		return nameOfPed;
	}

	public void setNameOfPed(SelectValue nameOfPed) {
		this.nameOfPed = nameOfPed;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getTokenName() {
		return tokenName;
	}

	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
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

	public Long getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(Long statusKey) {
		this.statusKey = statusKey;
	}

	public Boolean getIsWatchList() {
		return isWatchList;
	}

	public void setIsWatchList(Boolean isWatchList) {
		this.isWatchList = isWatchList;
	}

	public String getWatchListFlag() {
		return watchListFlag;
	}

	public void setWatchListFlag(String watchListFlag) {
		this.watchListFlag = watchListFlag;
	}

	public String getEscalateRemarks() {
		return escalateRemarks;
	}

	public void setEscalateRemarks(String escalateRemarks) {
		this.escalateRemarks = escalateRemarks;
	}

	public Boolean getIsReviewer() {
		return isReviewer;
	}

	public void setIsReviewer(Boolean isReviewer) {
		this.isReviewer = isReviewer;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getPedHistoryJson() {
		return pedHistoryJson;
	}

	public void setPedHistoryJson(String pedHistoryJson) {
		this.pedHistoryJson = pedHistoryJson;
	}

	public String getPedEndorsementJson() {
		return pedEndorsementJson;
	}

	public void setPedEndorsementJson(String pedEndorsementJson) {
		this.pedEndorsementJson = pedEndorsementJson;
	}

	public String getPedAdditionalJson() {
		return pedAdditionalJson;
	}

	public void setPedAdditionalJson(String pedAdditionalJson) {
		this.pedAdditionalJson = pedAdditionalJson;
	}

	public String getPedAmmendedJson() {
		return pedAmmendedJson;
	}

	public void setPedAmmendedJson(String pedAmmendedJson) {
		this.pedAmmendedJson = pedAmmendedJson;
	}

	public String getPedReviewedJson() {
		return pedReviewedJson;
	}

	public void setPedReviewedJson(String pedReviewedJson) {
		this.pedReviewedJson = pedReviewedJson;
	}

	public String getPedProcessedJson() {
		return pedProcessedJson;
	}

	public void setPedProcessedJson(String pedProcessedJson) {
		this.pedProcessedJson = pedProcessedJson;
	}

	public String getPedApprovedJson() {
		return pedApprovedJson;
	}

	public void setPedApprovedJson(String pedApprovedJson) {
		this.pedApprovedJson = pedApprovedJson;
	}

	public List<ViewPEDEndoresementDetailsDTO> getViewPEDEndoresementDetailsDTO() {
		return viewPEDEndoresementDetailsDTO;
	}

	public void setViewPEDEndoresementDetailsDTO(
			List<ViewPEDEndoresementDetailsDTO> viewPEDEndoresementDetailsDTO) {
		this.viewPEDEndoresementDetailsDTO = viewPEDEndoresementDetailsDTO;
	}

	public String getRepLetterDate() {
		return repLetterDate;
	}

	public void setRepLetterDate(String repLetterDate) {
		this.repLetterDate = repLetterDate;
	}

	public String getPedEffectiveFromDate() {
		return pedEffectiveFromDate;
	}

	public void setPedEffectiveFromDate(String pedEffectiveFromDate) {
		this.pedEffectiveFromDate = pedEffectiveFromDate;
	}

}
	
