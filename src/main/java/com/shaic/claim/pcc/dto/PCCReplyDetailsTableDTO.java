package com.shaic.claim.pcc.dto;

import java.util.Date;

public class PCCReplyDetailsTableDTO {
	
	private String replyRemarks;

	private String replyRole;

	private String replyGivenBy;

	private Date repliedDate;
	
	private Long queryKey;

	public String getReplyRemarks() {
		return replyRemarks;
	}

	public void setReplyRemarks(String replyRemarks) {
		this.replyRemarks = replyRemarks;
	}

	public String getReplyRole() {
		return replyRole;
	}

	public void setReplyRole(String replyRole) {
		this.replyRole = replyRole;
	}

	public String getReplyGivenBy() {
		return replyGivenBy;
	}

	public void setReplyGivenBy(String replyGivenBy) {
		this.replyGivenBy = replyGivenBy;
	}

	public Date getRepliedDate() {
		return repliedDate;
	}

	public void setRepliedDate(Date repliedDate) {
		this.repliedDate = repliedDate;
	}

	public Long getQueryKey() {
		return queryKey;
	}

	public void setQueryKey(Long queryKey) {
		this.queryKey = queryKey;
	}
}
