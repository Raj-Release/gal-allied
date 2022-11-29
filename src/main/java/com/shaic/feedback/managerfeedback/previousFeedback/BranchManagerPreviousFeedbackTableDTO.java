package com.shaic.feedback.managerfeedback.previousFeedback;

import java.util.Date;
import java.util.List;

import com.shaic.newcode.wizard.dto.SearchTableDTO;

public class BranchManagerPreviousFeedbackTableDTO  extends SearchTableDTO{
	List<BranchManagerPreviousFeedbackTableDTO> policyList;
	List<BranchManagerPreviousFeedbackTableDTO> proposalList;
	List<BranchManagerPreviousFeedbackTableDTO> claimList;
	List<BranchManagerPreviousFeedbackTableDTO> viewFeedbackTableList;
	private Long key;
	private Long detailKey;
	private String policyNumber;
	private String claimNumber;
	private String proposalNumber;
	private Long feedbackStatusId;
	private String intimationNo;
	private String policyRemarks;
	private String proposalRemarks;
	private String claimRemarks;
	private String feedbackReplyRemarks;
	private String branchDetails;
	private Long feedbackKey;
	private String branchManagerName;
	private String branchName;
	private Integer mobile;
	private String reportedDate;
	private String feedbackValue;
	private String feedbackType;
	private String feedbackStatus;
	private String feedbackRemarksOverall;
	private String corpTeamReply;
	private String claimsDeptReply;
	private Long feedbackStatusKey;
	private String technicalTeamReply;
	private Date feedbackRepliedDate;
	private Integer mobileNo;
	private String previousFeedBackRemarks;
	private String feedBackDate;
	private Long feedBackTypeId;	
	private Boolean feedbackOption;
	private String feedBackRemarks;
	private String corporateMedicalUnderwritingReply;
	private String claimsDepartmentReply;
	private String repliedDate;
	private String feedBack;
	private Boolean branchManagerReview;
	private String feedBackReviewFlag;
	private Long feedBackReviewRatingId;
	private Long ratingfor;
	private Long feedBackDtlsKey;
	private String documentNumber;
	private Long fbStatusId;
	private String fbCategory;
	
	public Long getDetailKey() {
		return detailKey;
	}
	public void setDetailKey(Long detailKey) {
		this.detailKey = detailKey;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public String getClaimNumber() {
		return claimNumber;
	}
	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}
	public String getProposalNumber() {
		return proposalNumber;
	}
	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}
	public String getProposalRemarks() {
		return proposalRemarks;
	}
	public void setProposalRemarks(String proposalRemarks) {
		this.proposalRemarks = proposalRemarks;
	}
	public String getClaimRemarks() {
		return claimRemarks;
	}
	public void setClaimRemarks(String claimRemarks) {
		this.claimRemarks = claimRemarks;
	}
	
	public Long getFeedbackStatusId() {
		return feedbackStatusId;
	}
	public void setFeedbackStatusId(Long feedbackStatusId) {
		this.feedbackStatusId = feedbackStatusId;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getPolicyRemarks() {
		return policyRemarks;
	}
	public void setPolicyRemarks(String policyRemarks) {
		this.policyRemarks = policyRemarks;
	}
	public Integer getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(Integer mobileNo) {
		this.mobileNo = mobileNo;
	}
	public Date getFeedbackRepliedDate() {
		return feedbackRepliedDate;
	}
	public void setFeedbackRepliedDate(Date feedbackRepliedDate) {
		this.feedbackRepliedDate = feedbackRepliedDate;
	}
	public String getFeedbackReplyRemarks() {
		return feedbackReplyRemarks;
	}
	public void setFeedbackReplyRemarks(String feedbackReplyRemarks) {
		this.feedbackReplyRemarks = feedbackReplyRemarks;
	}
	public Long getFeedbackKey() {
		return feedbackKey;
	}
	public void setFeedbackKey(Long feedbackKey) {
		this.feedbackKey = feedbackKey;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public Integer getMobile() {
		return mobile;
	}
	public void setMobile(Integer mobile) {
		this.mobile = mobile;
	}
	public String getBranchManagerName() {
		return branchManagerName;
	}
	public void setBranchManagerName(String branchManagerName) {
		this.branchManagerName = branchManagerName;
	}
	public String getTechnicalTeamReply() {
		return technicalTeamReply;
	}
	public void setTechnicalTeamReply(String technicalTeamReply) {
		this.technicalTeamReply = technicalTeamReply;
	}
	public String getFeedbackStatus() {
		return feedbackStatus;
	}
	public void setFeedbackStatus(String feedbackStatus) {
		this.feedbackStatus = feedbackStatus;
	}
	public Long getFeedbackStatusKey() {
		return feedbackStatusKey;
	}
	public void setFeedbackStatusKey(Long feedbackStatusKey) {
		this.feedbackStatusKey = feedbackStatusKey;
	}
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	public String getBranchDetails() {
		return branchDetails;
	}
	public void setBranchDetails(String branchDetails) {
		this.branchDetails = branchDetails;
	}

	public String getFeedbackValue() {
		return feedbackValue;
	}
	public String getReportedDate() {
		return reportedDate;
	}
	public void setReportedDate(String reportedDate) {
		this.reportedDate = reportedDate;
	}
	public void setFeedbackValue(String feedbackValue) {
		this.feedbackValue = feedbackValue;
	}
	public String getFeedbackType() {
		return feedbackType;
	}
	public void setFeedbackType(String feedbackType) {
		this.feedbackType = feedbackType;
	}
	public String getFeedbackRemarksOverall() {
		return feedbackRemarksOverall;
	}
	public void setFeedbackRemarksOverall(String feedbackRemarksOverall) {
		this.feedbackRemarksOverall = feedbackRemarksOverall;
	}
	public String getCorpTeamReply() {
		return corpTeamReply;
	}
	public void setCorpTeamReply(String corpTeamReply) {
		this.corpTeamReply = corpTeamReply;
	}
	public String getClaimsDeptReply() {
		return claimsDeptReply;
	}
	public void setClaimsDeptReply(String claimsDeptReply) {
		this.claimsDeptReply = claimsDeptReply;
	}
	public List<BranchManagerPreviousFeedbackTableDTO> getPolicyList() {
		return policyList;
	}
	public void setPolicyList(List<BranchManagerPreviousFeedbackTableDTO> policyList) {
		this.policyList = policyList;
	}
	public List<BranchManagerPreviousFeedbackTableDTO> getProposalList() {
		return proposalList;
	}
	public void setProposalList(
			List<BranchManagerPreviousFeedbackTableDTO> proposalList) {
		this.proposalList = proposalList;
	}
	public List<BranchManagerPreviousFeedbackTableDTO> getClaimList() {
		return claimList;
	}
	public void setClaimList(List<BranchManagerPreviousFeedbackTableDTO> claimList) {
		this.claimList = claimList;
	}
	public String getPreviousFeedBackRemarks() {
		return previousFeedBackRemarks;
	}
	public void setPreviousFeedBackRemarks(String previousFeedBackRemarks) {
		this.previousFeedBackRemarks = previousFeedBackRemarks;
	}
	public String getFeedBackDate() {
		return feedBackDate;
	}
	public void setFeedBackDate(String feedBackDate) {
		this.feedBackDate = feedBackDate;
	}
	public Long getFeedBackTypeId() {
		return feedBackTypeId;
	}
	public void setFeedBackTypeId(Long feedBackTypeId) {
		this.feedBackTypeId = feedBackTypeId;
	}
	public Boolean getFeedbackOption() {
		return feedbackOption;
	}
	public void setFeedbackOption(Boolean feedbackOption) {
		this.feedbackOption = feedbackOption;
	}
	public List<BranchManagerPreviousFeedbackTableDTO> getViewFeedbackTableList() {
		return viewFeedbackTableList;
	}
	public void setViewFeedbackTableList(
			List<BranchManagerPreviousFeedbackTableDTO> viewFeedbackTableList) {
		this.viewFeedbackTableList = viewFeedbackTableList;
	}
	public String getFeedBackRemarks() {
		return feedBackRemarks;
	}
	public void setFeedBackRemarks(String feedBackRemarks) {
		this.feedBackRemarks = feedBackRemarks;
	}
	public String getCorporateMedicalUnderwritingReply() {
		return corporateMedicalUnderwritingReply;
	}
	public void setCorporateMedicalUnderwritingReply(
			String corporateMedicalUnderwritingReply) {
		this.corporateMedicalUnderwritingReply = corporateMedicalUnderwritingReply;
	}
	public String getClaimsDepartmentReply() {
		return claimsDepartmentReply;
	}
	public void setClaimsDepartmentReply(String claimsDepartmentReply) {
		this.claimsDepartmentReply = claimsDepartmentReply;
	}
	public String getRepliedDate() {
		return repliedDate;
	}
	public void setRepliedDate(String repliedDate) {
		this.repliedDate = repliedDate;
	}
	public String getFeedBack() {
		return feedBack;
	}
	public void setFeedBack(String feedBack) {
		this.feedBack = feedBack;
	}
	public Boolean getBranchManagerReview() {
		return branchManagerReview;
	}
	public void setBranchManagerReview(Boolean branchManagerReview) {
		this.branchManagerReview = branchManagerReview;
	}
	public String getFeedBackReviewFlag() {
		return feedBackReviewFlag;
	}
	public void setFeedBackReviewFlag(String feedBackReviewFlag) {
		this.feedBackReviewFlag = feedBackReviewFlag;
	}
	public Long getFeedBackReviewRatingId() {
		return feedBackReviewRatingId;
	}
	public void setFeedBackReviewRatingId(Long feedBackReviewRatingId) {
		this.feedBackReviewRatingId = feedBackReviewRatingId;
	}
	public Long getRatingfor() {
		return ratingfor;
	}
	public void setRatingfor(Long ratingfor) {
		this.ratingfor = ratingfor;
	}
	public Long getFeedBackDtlsKey() {
		return feedBackDtlsKey;
	}
	public void setFeedBackDtlsKey(Long feedBackDtlsKey) {
		this.feedBackDtlsKey = feedBackDtlsKey;
	}
	public String getDocumentNumber() {
		return documentNumber;
	}
	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}
	public Long getFbStatusId() {
		return fbStatusId;
	}
	public void setFbStatusId(Long fbStatusId) {
		this.fbStatusId = fbStatusId;
	}
	public String getFbCategory() {
		return fbCategory;
	}
	public void setFbCategory(String fbCategory) {
		this.fbCategory = fbCategory;
	}
}
