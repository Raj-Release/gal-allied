package com.shaic.branchmanagerfeedback;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class PolicyFeedbcakDTO extends AbstractTableDTO  implements Serializable{
	
	private String branchDetails;
	private Date reportedDate;
	private String feedbackType;
	private String feedbackStatus;
	private String feedbackremarks;
	private String technicalTeamReply;
	private Long feedbackStatusKey;
	
	public Long getFeedbackStatusKey() {
		return feedbackStatusKey;
	}
	public void setFeedbackStatusKey(Long feedbackStatusKey) {
		this.feedbackStatusKey = feedbackStatusKey;
	}
	public String getBranchDetails() {
		return branchDetails;
	}
	public void setBranchDetails(String branchDetails) {
		this.branchDetails = branchDetails;
	}
	public Date getReportedDate() {
		return reportedDate;
	}
	public void setReportedDate(Date reportedDate) {
		this.reportedDate = reportedDate;
	}
	public String getFeedbackType() {
		return feedbackType;
	}
	public void setFeedbackType(String feedbackType) {
		this.feedbackType = feedbackType;
	}
	public String getFeedbackStatus() {
		return feedbackStatus;
	}
	public void setFeedbackStatus(String feedbackStatus) {
		this.feedbackStatus = feedbackStatus;
	}
	public String getFeedbackremarks() {
		return feedbackremarks;
	}
	public void setFeedbackremarks(String feedbackremarks) {
		this.feedbackremarks = feedbackremarks;
	}
	public String getTechnicalTeamReply() {
		return technicalTeamReply;
	}
	public void setTechnicalTeamReply(String technicalTeamReply) {
		this.technicalTeamReply = technicalTeamReply;
	}
	
	
}
