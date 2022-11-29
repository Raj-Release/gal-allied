package com.shaic.branchmanagerfeedback;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class TechnicalDepartmentFeedbackDTO extends AbstractSearchDTO implements Serializable  {
	
	private SelectValue feedbackValue;
	private SelectValue zoneValue;
	private SelectValue branchValue;
	private SelectValue feedbackStatusValue;
	private SelectValue feedbackTypeValue;
	private java.sql.Date fromDate;
	private java.sql.Date toDate;

	
	public SelectValue getFeedbackValue() {
		return feedbackValue;
	}
	public void setFeedbackValue(SelectValue feedbackValue) {
		this.feedbackValue = feedbackValue;
	}
	public SelectValue getZoneValue() {
		return zoneValue;
	}
	public void setZoneValue(SelectValue zoneValue) {
		this.zoneValue = zoneValue;
	}
	public SelectValue getBranchValue() {
		return branchValue;
	}
	public void setBranchValue(SelectValue branchValue) {
		this.branchValue = branchValue;
	}
	public SelectValue getFeedbackStatusValue() {
		return feedbackStatusValue;
	}
	public void setFeedbackStatusValue(SelectValue feedbackStatusValue) {
		this.feedbackStatusValue = feedbackStatusValue;
	}
	public SelectValue getFeedbackTypeValue() {
		return feedbackTypeValue;
	}
	public void setFeedbackTypeValue(SelectValue feedbackTypeValue) {
		this.feedbackTypeValue = feedbackTypeValue;
	}
	public java.sql.Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(java.sql.Date fromDate) {
		this.fromDate = fromDate;
	}
	public java.sql.Date getToDate() {
		return toDate;
	}
	public void setToDate(java.sql.Date toDate) {
		this.toDate = toDate;
	}

	
}
