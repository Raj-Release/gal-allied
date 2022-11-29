package com.shaic.claim.claimhistory.view;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class ViewClaimHistoryDTO extends AbstractTableDTO   implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String typeofClaim ;
	
	private String dateAndTime;
	
	private Date createdDate;
	
	private String userID;
	
	private String userName;
	
	private String referenceNo;
	
	private String claimStage;
	
	private String status;
	
	private Long statusID;
	
	private String userRemark;
	
	private Long reimbursementKey;
	
	private Long cashlessKey;
	
	private Long historyKey;
	
	private String docrecdfrom;
    
	private String rodtype;

	private String classification;

	private String subclassification;
	
	private String billClassif;
	
	private String portalStatusVal;
	
	private String websiteStatusVal;

	private String remarks;
	
	private String qryRplRemarks;
	private String qryRplDate;
	private String rplyUser;
	
	private String claimsReply;
	
	private Long ackKey;
		
	public String getTypeofClaim() {
		return typeofClaim;
	}

	public void setTypeofClaim(String typeofClaim) {
		this.typeofClaim = typeofClaim;
	}

	public String getDateAndTime() {
		return dateAndTime;
	}

	public void setDateAndTime(String dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getClaimStage() {
		return claimStage;
	}

	public void setClaimStage(String claimStage) {
		this.claimStage = claimStage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserRemark() {
		return userRemark;
	}

	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public Long getReimbursementKey() {
		return reimbursementKey;
	}

	public void setReimbursementKey(Long reimbursementKey) {
		this.reimbursementKey = reimbursementKey;
	}

	public Long getCashlessKey() {
		return cashlessKey;
	}

	public void setCashlessKey(Long cashlessKey) {
		this.cashlessKey = cashlessKey;
	}

	public Long getHistoryKey() {
		return historyKey;
	}

	public void setHistoryKey(Long historyKey) {
		this.historyKey = historyKey;
	}

	public Long getStatusID() {
		return statusID;
	}

	public void setStatusID(Long statusID) {
		this.statusID = statusID;
	}

	public String getDocrecdfrom() {
		return docrecdfrom;
	}

	public void setDocrecdfrom(String docrecdfrom) {
		this.docrecdfrom = docrecdfrom;
	}

	public String getRodtype() {
		return rodtype;
	}

	public void setRodtype(String rodtype) {
		this.rodtype = rodtype;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getSubclassification() {
		return subclassification;
	}

	public void setSubclassification(String subclassification) {
		this.subclassification = subclassification;
	}

	public String getBillClassif() {
		return billClassif;
	}

	public void setBillClassif(String billClassif) {
		this.billClassif = billClassif;
	}

	@Override
	public String toString() {
		return "ViewClaimHistoryDTO [typeofClaim=" + typeofClaim
				+ ", dateAndTime=" + dateAndTime + ", createdDate="
				+ createdDate + ", userID=" + userID + ", userName=" + userName
				+ ", referenceNo=" + referenceNo + ", claimStage=" + claimStage
				+ ", status=" + status + ", statusID=" + statusID
				+ ", userRemark=" + userRemark + ", reimbursementKey="
				+ reimbursementKey + ", cashlessKey=" + cashlessKey
				+ ", historyKey=" + historyKey + ", docrecdfrom=" + docrecdfrom
				+ ", rodtype=" + rodtype + ", classification=" + classification
				+ ", subclassification=" + subclassification + ", billClassif="
				+ billClassif + "]";
	}

	public String getPortalStatusVal() {
		return portalStatusVal;
	}

	public void setPortalStatusVal(String portalStatusVal) {
		this.portalStatusVal = portalStatusVal;
	}

	public String getWebsiteStatusVal() {
		return websiteStatusVal;
	}

	public void setWebsiteStatusVal(String websiteStatusVal) {
		this.websiteStatusVal = websiteStatusVal;
	}
	
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getQryRplRemarks() {
		return qryRplRemarks;
	}

	public void setQryRplRemarks(String qryRplRemarks) {
		this.qryRplRemarks = qryRplRemarks;
	}

	public String getQryRplDate() {
		return qryRplDate;
	}

	public void setQryRplDate(String qryRplDate) {
		this.qryRplDate = qryRplDate;
	}

	public String getRplyUser() {
		return rplyUser;
	}

	public void setRplyUser(String rplyUser) {
		this.rplyUser = rplyUser;
	}

	public String getClaimsReply() {
		return claimsReply;
	}

	public void setClaimsReply(String claimsReply) {
		this.claimsReply = claimsReply;
	}

	public Long getAckKey() {
		return ackKey;
	}

	public void setAckKey(Long ackKey) {
		this.ackKey = ackKey;
	}
	
}