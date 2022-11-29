package com.shaic.feedback.managerfeedback;

public class FeedbackStatsDto {

	private String feedbackArea;
	private Long reported;
	private Long responded;
	private Long pending;
	private Long feedbackAreaKey;
	public String getFeedbackArea() {
		return feedbackArea;
	}
	public void setFeedbackArea(String feedbackArea) {
		this.feedbackArea = feedbackArea;
	}
	public Long getReported() {
		return reported;
	}
	public void setReported(Long reported) {
		this.reported = reported;
	}
	public Long getResponded() {
		return responded;
	}
	public void setResponded(Long responded) {
		this.responded = responded;
	}
	public Long getPending() {
		return pending;
	}
	public void setPending(Long pending) {
		this.pending = pending;
	}
	public Long getFeedbackAreaKey() {
		return feedbackAreaKey;
	}
	public void setFeedbackAreaKey(Long feedbackAreaKey) {
		this.feedbackAreaKey = feedbackAreaKey;
	}
}
