package com.shaic.claim.viewEarlierRodDetails;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class ZUAViewQueryHistoryTableDTO extends AbstractTableDTO  implements Serializable{
	
	private String queryCode;
	private String queryDescription;
	private String flowFrom;
	private String type;
	private String flowTo;
	private String remarks;
	private String serialNo;
	private String userId;
	private String apprDateAndTime;
	private String queryDetails; 
	private String dateandTime; 
	private String raisedByRole; 
	private String raisedByUser; 
	private String queryreply; 
	private String repliedByRole; 
	private String repliedByUser; 
	private String repliedDate; 
	private String querStatus;
	
	public String getQueryDetails() {
		return queryDetails;
	}
	public void setQueryDetails(String queryDetails) {
		this.queryDetails = queryDetails;
	}
	public String getDateandTime() {
		return dateandTime;
	}
	public void setDateandTime(String dateandTime) {
		this.dateandTime = dateandTime;
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
	public String getQueryreply() {
		return queryreply;
	}
	public void setQueryreply(String queryreply) {
		this.queryreply = queryreply;
	}
	public String getRepliedByRole() {
		return repliedByRole;
	}
	public void setRepliedByRole(String repliedByRole) {
		this.repliedByRole = repliedByRole;
	}
	public String getRepliedByUser() {
		return repliedByUser;
	}
	public void setRepliedByUser(String repliedByUser) {
		this.repliedByUser = repliedByUser;
	}
	public String getRepliedDate() {
		return repliedDate;
	}
	public void setRepliedDate(String repliedDate) {
		this.repliedDate = repliedDate;
	}
	public String getQuerStatus() {
		return querStatus;
	}
	public void setQuerStatus(String querStatus) {
		this.querStatus = querStatus;
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
	public String getFlowFrom() {
		return flowFrom;
	}
	public void setFlowFrom(String flowFrom) {
		this.flowFrom = flowFrom;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFlowTo() {
		return flowTo;
	}
	public void setFlowTo(String flowTo) {
		this.flowTo = flowTo;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getApprDateAndTime() {
		return apprDateAndTime;
	}
	public void setApprDateAndTime(String apprDateAndTime) {
		this.apprDateAndTime = apprDateAndTime;
	}
	
	
}
