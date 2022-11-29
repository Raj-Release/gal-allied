package com.shaic.claim.preauth.wizard.dto;

import java.util.List;

import com.shaic.claim.claimhistory.view.PedHistoryDTO;
import com.shaic.claim.pedquery.viewPedDetails.ViewPEDEndoresementDetailsDTO;


public class OldPedEndorsementDetailDTO {

	private String intimationNo;
	
	private String pedSuggestionName;
	
	private String pedName;
	
	private String repLetterDate;
	
	private String remarks;
	
	private String requestorId;
	
	private String requestedDate;
	
	private String requestStatus;
	
	private List<ViewPEDEndoresementDetailsDTO> viewPEDEndoresementDetailsDTO;
	
	private List<PedHistoryDTO> pedHistoryList; 
	
	private boolean hasPedQueryDetails = Boolean.FALSE;
	
	private String pedQueryStatus;
	
	private String replyRemarks;
	
	private boolean hasPedEsclateDetails = Boolean.FALSE;	
	
	private String hasPedEsclateRemarks;
	
	private String pedEsclateRemarks;
	
	private String pedDiscussedWith;
	
	private String pedSuggestion;
	
	private String pedRequestorId;
	
	private String pedRequestorDate;
	
	private boolean hasPedSpecialistDetails = Boolean.FALSE;	
	
	private String pedSpecialistStatus;
	
	private String pedSpecialistRemarks;
	
	private OldPedEndorsementDetailDTO pedAmmended;
	
	private OldPedEndorsementDetailDTO pedReviewed;
	
	private OldPedEndorsementDetailDTO pedProcessed;
	
	private OldPedEndorsementDetailDTO pedApproved;
	
	public OldPedEndorsementDetailDTO(OldPedEndorsementDTO oldPedDto) {
		this.intimationNo = oldPedDto.getIntimationNo();
		this.pedSuggestionName = oldPedDto.getPedSuggestionName();
		this.pedName = oldPedDto.getPedName();
		this.repLetterDate = oldPedDto.getRepLetterDate();
		this.remarks = oldPedDto.getRemarks();
		this.requestorId = oldPedDto.getRequestorId();
		this.requestedDate = oldPedDto.getRequestedDate();
		this.requestStatus = oldPedDto.getRequestStatus();
		this.viewPEDEndoresementDetailsDTO = oldPedDto.getViewPEDEndoresementDetailsDTO();
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
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

	public String getRepLetterDate() {
		return repLetterDate;
	}

	public void setRepLetterDate(String repLetterDate) {
		this.repLetterDate = repLetterDate;
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

	public List<ViewPEDEndoresementDetailsDTO> getViewPEDEndoresementDetailsDTO() {
		return viewPEDEndoresementDetailsDTO;
	}

	public void setViewPEDEndoresementDetailsDTO(
			List<ViewPEDEndoresementDetailsDTO> viewPEDEndoresementDetailsDTO) {
		this.viewPEDEndoresementDetailsDTO = viewPEDEndoresementDetailsDTO;
	}

	public List<PedHistoryDTO> getPedHistoryList() {
		return pedHistoryList;
	}

	public void setPedHistoryList(List<PedHistoryDTO> pedHistoryList) {
		this.pedHistoryList = pedHistoryList;
	}

	public boolean isHasPedQueryDetails() {
		return hasPedQueryDetails;
	}

	public void setHasPedQueryDetails(boolean hasPedQueryDetails) {
		this.hasPedQueryDetails = hasPedQueryDetails;
	}

	public String getPedQueryStatus() {
		return pedQueryStatus;
	}

	public void setPedQueryStatus(String pedQueryStatus) {
		this.pedQueryStatus = pedQueryStatus;
	}

	public String getReplyRemarks() {
		return replyRemarks;
	}

	public void setReplyRemarks(String replyRemarks) {
		this.replyRemarks = replyRemarks;
	}

	public boolean isHasPedEsclateDetails() {
		return hasPedEsclateDetails;
	}

	public void setHasPedEsclateDetails(boolean hasPedEsclateDetails) {
		this.hasPedEsclateDetails = hasPedEsclateDetails;
	}

	public String getHasPedEsclateRemarks() {
		return hasPedEsclateRemarks;
	}

	public void setHasPedEsclateRemarks(String hasPedEsclateRemarks) {
		this.hasPedEsclateRemarks = hasPedEsclateRemarks;
	}

	public String getPedEsclateRemarks() {
		return pedEsclateRemarks;
	}

	public void setPedEsclateRemarks(String pedEsclateRemarks) {
		this.pedEsclateRemarks = pedEsclateRemarks;
	}

	public String getPedDiscussedWith() {
		return pedDiscussedWith;
	}

	public void setPedDiscussedWith(String pedDiscussedWith) {
		this.pedDiscussedWith = pedDiscussedWith;
	}

	public String getPedRequestorId() {
		return pedRequestorId;
	}

	public void setPedRequestorId(String pedRequestorId) {
		this.pedRequestorId = pedRequestorId;
	}

	public String getPedRequestorDate() {
		return pedRequestorDate;
	}

	public void setPedRequestorDate(String pedRequestorDate) {
		this.pedRequestorDate = pedRequestorDate;
	}

	public boolean isHasPedSpecialistDetails() {
		return hasPedSpecialistDetails;
	}

	public void setHasPedSpecialistDetails(boolean hasPedSpecialistDetails) {
		this.hasPedSpecialistDetails = hasPedSpecialistDetails;
	}

	public String getPedSpecialistStatus() {
		return pedSpecialistStatus;
	}

	public void setPedSpecialistStatus(String pedSpecialistStatus) {
		this.pedSpecialistStatus = pedSpecialistStatus;
	}

	public String getPedSpecialistRemarks() {
		return pedSpecialistRemarks;
	}

	public void setPedSpecialistRemarks(String pedSpecialistRemarks) {
		this.pedSpecialistRemarks = pedSpecialistRemarks;
	}

	public OldPedEndorsementDetailDTO getPedAmmended() {
		return pedAmmended;
	}

	public void setPedAmmended(OldPedEndorsementDetailDTO pedAmmended) {
		this.pedAmmended = pedAmmended;
	}

	public OldPedEndorsementDetailDTO getPedReviewed() {
		return pedReviewed;
	}

	public void setPedReviewed(OldPedEndorsementDetailDTO pedReviewed) {
		this.pedReviewed = pedReviewed;
	}

	public OldPedEndorsementDetailDTO getPedProcessed() {
		return pedProcessed;
	}

	public void setPedProcessed(OldPedEndorsementDetailDTO pedProcessed) {
		this.pedProcessed = pedProcessed;
	}

	public OldPedEndorsementDetailDTO getPedApproved() {
		return pedApproved;
	}

	public void setPedApproved(OldPedEndorsementDetailDTO pedApproved) {
		this.pedApproved = pedApproved;
	}

	public String getPedSuggestion() {
		return pedSuggestion;
	}

	public void setPedSuggestion(String pedSuggestion) {
		this.pedSuggestion = pedSuggestion;
	}		
	
}
