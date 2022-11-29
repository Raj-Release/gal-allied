package com.shaic.branchmanagerfeedback;

import java.io.Serializable;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class TechnicalTeamReplyOnFeedbackDTO  extends AbstractSearchDTO implements Serializable  {

	private String branchManager;
	private String mobile;
	private String branchName;
	private Boolean feedbackFor;
	private String feedbackRemarks;
	private String techTeamReply;
	private String feedbackType;
	public String getBranchManager() {
		return branchManager;
	}
	public void setBranchManager(String branchManager) {
		this.branchManager = branchManager;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public Boolean getFeedbackFor() {
		return feedbackFor;
	}
	public void setFeedbackFor(Boolean feedbackFor) {
		this.feedbackFor = feedbackFor;
	}
	public String getFeedbackRemarks() {
		return feedbackRemarks;
	}
	public void setFeedbackRemarks(String feedbackRemarks) {
		this.feedbackRemarks = feedbackRemarks;
	}
	public String getTechTeamReply() {
		return techTeamReply;
	}
	public void setTechTeamReply(String techTeamReply) {
		this.techTeamReply = techTeamReply;
	}
	public String getFeedbackType() {
		return feedbackType;
	}
	public void setFeedbackType(String feedbackType) {
		this.feedbackType = feedbackType;
	}
	
}
