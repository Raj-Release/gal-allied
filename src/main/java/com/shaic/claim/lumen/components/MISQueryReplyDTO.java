package com.shaic.claim.lumen.components;

import java.io.Serializable;
import java.util.Date;

import com.shaic.domain.LumenRequest;

@SuppressWarnings("serial")
public class MISQueryReplyDTO implements Serializable{

	private String queryRaisedBy;
	private String queryRaisedRole;
	private Date queryRaisedDate;
	private String replyReceivedFrom;
	private Date repliedDate;
	
	private LumenRequest lumenRequest;
	
	private Long queryKey;
	
	private String misReplyRemarks;
	
	public String getQueryRaisedBy() {
		return queryRaisedBy;
	}
	public void setQueryRaisedBy(String queryRaisedBy) {
		this.queryRaisedBy = queryRaisedBy;
	}
	public String getQueryRaisedRole() {
		return queryRaisedRole;
	}
	public void setQueryRaisedRole(String queryRaisedRole) {
		this.queryRaisedRole = queryRaisedRole;
	}
	public Date getQueryRaisedDate() {
		return queryRaisedDate;
	}
	public void setQueryRaisedDate(Date queryRaisedDate) {
		this.queryRaisedDate = queryRaisedDate;
	}
	public String getReplyReceivedFrom() {
		return replyReceivedFrom;
	}
	public void setReplyReceivedFrom(String replyReceivedFrom) {
		this.replyReceivedFrom = replyReceivedFrom;
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
	public LumenRequest getLumenRequest() {
		return lumenRequest;
	}
	public void setLumenRequest(LumenRequest lumenRequest) {
		this.lumenRequest = lumenRequest;
	}
	public String getMisReplyRemarks() {
		return misReplyRemarks;
	}
	public void setMisReplyRemarks(String misReplyRemarks) {
		this.misReplyRemarks = misReplyRemarks;
	}
}
