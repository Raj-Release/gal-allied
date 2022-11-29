package com.shaic.restservices.bancs.consumerDetails;

import java.util.List;

public class Queries {
	
	private int serialNo;
	private String queryType;
	private String queryCode;
	private String queryDescription;
	private String queryDetails;
	private String dateAndTime;
	private String raisedByRole;
	private String raisedByUser;
	private String queryReply;
	private String RepliedByRole;
	private String RepliedByUser;
	private String ReplyDateAndTime;
	private String queryStatus;
	private List<MultiSetDetail> multiSetDetail;
	
	public List<MultiSetDetail> getMultiSetDetail() {
		return multiSetDetail;
	}
	public void setMultiSetDetail(List<MultiSetDetail> multiSetDetail) {
		this.multiSetDetail = multiSetDetail;
	}
	public int getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}
	public String getQueryType() {
		return queryType;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
	public String getQueryCode() {
		return queryCode;
	}
	public void setQueryCode(String queryCode) {
		this.queryCode = queryCode;
	}
	public String getQueryDescription() {
		return queryDescription;
	}
	public void setQueryDescription(String queryDescription) {
		this.queryDescription = queryDescription;
	}
	public String getQueryDetails() {
		return queryDetails;
	}
	public void setQueryDetails(String queryDetails) {
		this.queryDetails = queryDetails;
	}
	public String getDateAndTime() {
		return dateAndTime;
	}
	public void setDateAndTime(String dateAndTime) {
		this.dateAndTime = dateAndTime;
	}
	public String getRaisedByRole() {
		return raisedByRole;
	}
	public void setRaisedByRole(String raisedByRole) {
		this.raisedByRole = raisedByRole;
	}
	public String getRaisedByUser() {
		return raisedByUser;
	}
	public void setRaisedByUser(String raisedByUser) {
		this.raisedByUser = raisedByUser;
	}
	public String getQueryReply() {
		return queryReply;
	}
	public void setQueryReply(String queryReply) {
		this.queryReply = queryReply;
	}
	public String getRepliedByRole() {
		return RepliedByRole;
	}
	public void setRepliedByRole(String repliedByRole) {
		RepliedByRole = repliedByRole;
	}
	public String getRepliedByUser() {
		return RepliedByUser;
	}
	public void setRepliedByUser(String repliedByUser) {
		RepliedByUser = repliedByUser;
	}
	public String getReplyDateAndTime() {
		return ReplyDateAndTime;
	}
	public void setReplyDateAndTime(String replyDateAndTime) {
		ReplyDateAndTime = replyDateAndTime;
	}
	public String getQueryStatus() {
		return queryStatus;
	}
	public void setQueryStatus(String queryStatus) {
		this.queryStatus = queryStatus;
	}
	
	

}
