package com.shaic.feedback.managerfeedback;

import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;

public class BranchManagerFeedbackhomePageDto {
	
	private SelectValue homeBranch;
	
	private List<FeedbackStatsDto> FeedbackStatsList;
	
	private List<ReviewStatsDto> ReviewStatsList;

	public List<FeedbackStatsDto> getFeedbackStatsList() {
		return FeedbackStatsList;
	}

	public void setFeedbackStatsList(List<FeedbackStatsDto> feedbackStatsList) {
		FeedbackStatsList = feedbackStatsList;
	}

	public List<ReviewStatsDto> getReviewStatsList() {
		return ReviewStatsList;
	}

	public void setReviewStatsList(List<ReviewStatsDto> reviewStatsList) {
		ReviewStatsList = reviewStatsList;
	}

	public SelectValue getHomeBranch() {
		return homeBranch;
	}

	public void setHomeBranch(SelectValue homeBranch) {
		this.homeBranch = homeBranch;
	}	
	
}
