package com.shaic.claim.pedquery;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.pedrequest.view.ViewPEDTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class PEDQueryDTO extends AbstractSearchDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	private com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO oldPedEndorsementDTO;



	private Long key;
	
	private List<ViewPEDTableDTO> viewPedTableDTO;
	
	private List<ViewPEDTableDTO> deletedDTO;
	
	private Boolean isEditPED = false;
	
	
	@NotNull(message="Please Enter PED Suggestion")
	private SelectValue pedSuggestion;
	
	@NotNull(message="Please Choose Insured Name")
	private SelectValue insuredName;

	@NotNull(message="Please Enter Ped Name")
	private SelectValue nameofPED;
	
/*	@NotNull(message="Please Enter Ped Name")
	@Size(min = 1 , message = "Please Enter Ped name")*/
	private String pedName;

	@NotNull(message="Please Enter Remarks")
	@Size(min = 1 , message = "Please Enter Remarks")
	private String remarks;

	@NotNull(message="Please Enter Repudiation Letter Date")
	private Date repudiationLetterDate;
	
	private String queryRemarks;
	
	private SelectValue specialistType;
	
	private String uploadFile;
	
	private String reasonforReferring;
	
	private String rejectionRemarks;
	
	private String approveRemarks;
	
	private String processorRemarks;
	
	private String viewFile;
	
	private String specialistRemarks;	
	
	private Long stageKey;
	
	@NotNull(message="Please Enter Review Remarks")
	@Size(min = 1 , message = "Please Enter Review Remarks")
	private String reviewRemarks;
	
	private Boolean isWatchList = false;
	
	BeanItemContainer<SelectValue> selectIcdChapterContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
	
	BeanItemContainer<SelectValue> selectPedCodeContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
	
	BeanItemContainer<SelectValue> selectSourceContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
	
	BeanItemContainer<SelectValue> pedSuggestionContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
	
	BeanItemContainer<SelectValue> insuredDetailsContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
	
	private ClaimDto claimDto;
	
	private PolicyDto policyDto;
	
	private Boolean isPolicyValidate;
	
	private Boolean isAddWatchList;
	
	private String addWatchListFlag;
	
	private Boolean isWatchListReviewer;
	
	private String watchlistRemarks;
	
	private Boolean isDiscussed;
	
	private String discussedFlag;
	
	@NotNull(message="Please Enter Discussed Remarks")
	@Size(min = 1 , message = "Please Enter Discussed Remarks")
	private String discussRemarks;
	
	@NotNull(message="Please Enter Discussed with")
	private SelectValue discussWith;
	
	BeanItemContainer<SelectValue> pedTlContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
	
	private PreauthDTO preauthDTO;
	
	@NotNull(message="Please Enter PED Effective from date")
	private Date pedEffFromDate;
	
	private String policyInsuredAgeingFlag;
	
	private Date docRecievedDate;

	public String getViewFile() {
		return viewFile;
	}

	public void setViewFile(String viewFile) {
		this.viewFile = viewFile;
	}

	public String getSpecialistRemarks() {
		return specialistRemarks;
	}

	public void setSpecialistRemarks(String specialistRemarks) {
		this.specialistRemarks = specialistRemarks;
	}

	public com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO getOldPedEndorsementDTO() {
		return oldPedEndorsementDTO;
	}

	public void setOldPedEndorsementDTO(
			com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO oldPedEndorsementDTO) {
		this.oldPedEndorsementDTO = oldPedEndorsementDTO;
	}

	public SelectValue getPedSuggestion() {
		return pedSuggestion;
	}

	public void setPedSuggestion(SelectValue pedSuggestion) {
		this.pedSuggestion = pedSuggestion;
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

	public String getReasonforReferring() {
		return reasonforReferring;
	}

	public void setReasonforReferring(String reasonforReferring) {
		this.reasonforReferring = reasonforReferring;
	}


	public String getRejectionRemarks() {
		return rejectionRemarks;
	}

	public void setRejectionRemarks(String rejectionRemarks) {
		this.rejectionRemarks = rejectionRemarks;
	}

	public String getApproveRemarks() {
		return approveRemarks;
	}

	public void setApproveRemarks(String approveRemarks) {
		this.approveRemarks = approveRemarks;
	}

	public String getProcessorRemarks() {
		return processorRemarks;
	}

	public void setProcessorRemarks(String processorRemarks) {
		this.processorRemarks = processorRemarks;
	}

	public List<ViewPEDTableDTO> getViewPedTableDTO() {
		return viewPedTableDTO;
	}

	public void setViewPedTableDTO(List<ViewPEDTableDTO> viewPedTableDTO) {
		this.viewPedTableDTO = viewPedTableDTO;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public SelectValue getNameofPED() {
		return nameofPED;
	}

	public void setNameofPED(SelectValue nameofPED) {
		this.nameofPED = nameofPED;
	}

	public String getPedName() {
		return pedName;
	}

	public void setPedName(String pedName) {
		this.pedName = pedName;
	}

	public Long getStageKey() {
		return stageKey;
	}

	public void setStageKey(Long stageKey) {
		this.stageKey = stageKey;
	}

	public Boolean getIsEditPED() {
		return isEditPED;
	}

	public void setIsEditPED(Boolean isEditPED) {
		this.isEditPED = isEditPED;
	}

	public List<ViewPEDTableDTO> getDeletedDTO() {
		return deletedDTO;
	}

	public void setDeletedDTO(List<ViewPEDTableDTO> deletedDTO) {
		this.deletedDTO = deletedDTO;
	}

	public Boolean getIsWatchList() {
		return isWatchList;
	}

	public void setIsWatchList(Boolean isWatchList) {
		this.isWatchList = isWatchList;
	}

	public String getReviewRemarks() {
		return reviewRemarks;
	}

	public void setReviewRemarks(String reviewRemarks) {
		this.reviewRemarks = reviewRemarks;
	}

	public BeanItemContainer<SelectValue> getSelectIcdChapterContainer() {
		return selectIcdChapterContainer;
	}

	public void setSelectIcdChapterContainer(
			BeanItemContainer<SelectValue> selectIcdChapterContainer) {
		this.selectIcdChapterContainer = selectIcdChapterContainer;
	}

	public BeanItemContainer<SelectValue> getSelectPedCodeContainer() {
		return selectPedCodeContainer;
	}

	public void setSelectPedCodeContainer(
			BeanItemContainer<SelectValue> selectPedCodeContainer) {
		this.selectPedCodeContainer = selectPedCodeContainer;
	}

	public BeanItemContainer<SelectValue> getSelectSourceContainer() {
		return selectSourceContainer;
	}

	public void setSelectSourceContainer(
			BeanItemContainer<SelectValue> selectSourceContainer) {
		this.selectSourceContainer = selectSourceContainer;
	}

	public BeanItemContainer<SelectValue> getPedSuggestionContainer() {
		return pedSuggestionContainer;
	}

	public void setPedSuggestionContainer(
			BeanItemContainer<SelectValue> pedSuggestionContainer) {
		this.pedSuggestionContainer = pedSuggestionContainer;
	}

	public ClaimDto getClaimDto() {
		return claimDto;
	}

	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}

	public BeanItemContainer<SelectValue> getInsuredDetailsContainer() {
		return insuredDetailsContainer;
	}

	public void setInsuredDetailsContainer(
			BeanItemContainer<SelectValue> insuredDetailsContainer) {
		this.insuredDetailsContainer = insuredDetailsContainer;
	}

	public SelectValue getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(SelectValue insuredName) {
		this.insuredName = insuredName;
	}

	public PolicyDto getPolicyDto() {
		return policyDto;
	}

	public void setPolicyDto(PolicyDto policyDto) {
		this.policyDto = policyDto;
	}

	public Boolean getIsPolicyValidate() {
		return isPolicyValidate;
	}

	public void setIsPolicyValidate(Boolean isPolicyValidate) {
		this.isPolicyValidate = isPolicyValidate;
	}

	public Boolean getIsAddWatchList() {
		return isAddWatchList;
	}

	public void setIsAddWatchList(Boolean isAddWatchList) {
		this.isAddWatchList = isAddWatchList;
		this.addWatchListFlag = this.isAddWatchList != null && isAddWatchList ? "Y" : "N" ;
	}

	public String getAddWatchListFlag() {
		return addWatchListFlag;
	}

	public void setAddWatchListFlag(String addWatchListFlag) {
		this.addWatchListFlag = addWatchListFlag;
		if(this.addWatchListFlag != null && this.addWatchListFlag.equalsIgnoreCase("Y")) {
			this.isAddWatchList = true;
		}
	}

	public Boolean getIsWatchListReviewer() {
		return isWatchListReviewer;
	}

	public void setIsWatchListReviewer(Boolean isWatchListReviewer) {
		this.isWatchListReviewer = isWatchListReviewer;
	}

	public String getWatchlistRemarks() {
		return watchlistRemarks;
	}

	public void setWatchlistRemarks(String watchlistRemarks) {
		this.watchlistRemarks = watchlistRemarks;
	}

	public Boolean getIsDiscussed() {
		return isDiscussed;
	}

	public void setIsDiscussed(Boolean isDiscussed) {
		this.isDiscussed = isDiscussed;
		this.discussedFlag = this.isDiscussed != null && isDiscussed ? "Y" : "N" ;
	}

	public String getDiscussedFlag() {
		return discussedFlag;
	}

	public void setDiscussedFlag(String discussedFlag) {
		this.discussedFlag = discussedFlag;
		if(this.discussedFlag != null && this.discussedFlag.equalsIgnoreCase("Y")) {
			this.isDiscussed = true;
		}
	
	}

	public String getDiscussRemarks() {
		return discussRemarks;
	}

	public void setDiscussRemarks(String discussRemarks) {
		this.discussRemarks = discussRemarks;
	}

	public SelectValue getDiscussWith() {
		return discussWith;
	}

	public void setDiscussWith(SelectValue discussWith) {
		this.discussWith = discussWith;
	}

	public BeanItemContainer<SelectValue> getPedTlContainer() {
		return pedTlContainer;
	}

	public void setPedTlContainer(BeanItemContainer<SelectValue> pedTlContainer) {
		this.pedTlContainer = pedTlContainer;
	}

	public PreauthDTO getPreauthDTO() {
		return preauthDTO;
	}

	public void setPreauthDTO(PreauthDTO preauthDTO) {
		this.preauthDTO = preauthDTO;
	}

	public Date getPedEffFromDate() {
		return pedEffFromDate;
	}

	public void setPedEffFromDate(Date pedEffFromDate) {
		this.pedEffFromDate = pedEffFromDate;
	}

	public String getPolicyInsuredAgeingFlag() {
		return policyInsuredAgeingFlag;
	}

	public void setPolicyInsuredAgeingFlag(String policyInsuredAgeingFlag) {
		this.policyInsuredAgeingFlag = policyInsuredAgeingFlag;
	}

	public Date getDocRecievedDate() {
		return docRecievedDate;
	}

	public void setDocRecievedDate(Date docRecievedDate) {
		this.docRecievedDate = docRecievedDate;
	}


}
