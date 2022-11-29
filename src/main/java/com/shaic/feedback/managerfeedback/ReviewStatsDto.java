package com.shaic.feedback.managerfeedback;

public class ReviewStatsDto {

	private String reviewStatus;
	private Long merReview;
	private Long claimsRetailReview;
	private Long claimsGmcReview;
	
	public Long getClaimsRetailReview() {
		return claimsRetailReview;
	}
	public void setClaimsRetailReview(Long claimsRetailReview) {
		this.claimsRetailReview = claimsRetailReview;
	}
	public Long getMerReview() {
		return merReview;
	}
	public void setMerReview(Long merReview) {
		this.merReview = merReview;
	}
	public Long getClaimsGmcReview() {
		return claimsGmcReview;
	}
	public void setClaimsGmcReview(Long claimsGmcReview) {
		this.claimsGmcReview = claimsGmcReview;
	}
	public String getReviewStatus() {
		return reviewStatus;
	}
	public void setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
	}	
}
