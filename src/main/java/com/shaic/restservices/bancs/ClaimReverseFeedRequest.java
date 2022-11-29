package com.shaic.restservices.bancs;

import java.util.List;

public class ClaimReverseFeedRequest {

	private String claimUprId;
	private List<ClaimReverseFeedData> details;
	
	
	public String getClaimUprId() {
		return claimUprId;
	}
	public void setClaimUprId(String claimUprId) {
		this.claimUprId = claimUprId;
	}
	public List<ClaimReverseFeedData> getDetails() {
		return details;
	}
	public void setDetails(List<ClaimReverseFeedData> details) {
		this.details = details;
	}
	
	
}
