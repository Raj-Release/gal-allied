package com.shaic.restservices.bancs.receivepedquery;

public class UpdatePEDQueryComplexQueryDetails {
	
	Long queryId;
	String reply;
	String repliedUserCode;
	String repliedRoleCode;
	String raisedDate;
	
	public Long getQueryId() {
		return queryId;
	}
	public void setQueryId(Long queryId) {
		this.queryId = queryId;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	public String getRepliedUserCode() {
		return repliedUserCode;
	}
	public void setRepliedUserCode(String repliedUserCode) {
		this.repliedUserCode = repliedUserCode;
	}
	public String getRepliedRoleCode() {
		return repliedRoleCode;
	}
	public void setRepliedRoleCode(String repliedRoleCode) {
		this.repliedRoleCode = repliedRoleCode;
	}
	public String getRaisedDate() {
		return raisedDate;
	}
	public void setRaisedDate(String raisedDate) {
		this.raisedDate = raisedDate;
	}
	
	

}
