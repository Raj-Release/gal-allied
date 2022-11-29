package com.shaic.feedback.managerfeedback;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.vaadin.ui.Button;

public class ManagerFeedBackPolicyTableDto extends AbstractTableDTO {
	private int serialNumber;
	private String remarks;
	private String policyNumber;
	private String proposalNO;
	private String intimationNO;
	private String processType;
	private String deltedFlag;
	private Long feedbackFor;
	private SelectValue feedBackRating;

	
	
	private Long ratingfor;
	private String documentRaisedRemarks;
	private String documentReplyRemarks;

	private Integer activeFlag;
	private Date createdDate;
	private String createdBy;
	private Date modifiedDate;
	private String modifiedBy;
	private Button deleteButton;
	private String branch;
	private String zone;
	private Button btnFeedbackIcon;
	
	private String clickType = "";
	
	private List<ManagerFeedBackPolicyTableDto> tableDtoList;
	private SelectValue feedBackType;
	private SelectValue fbCategory;
	
	
	public Long getRatingfor() {
		return ratingfor;
	}
	public void setRatingfor(Long ratingfor) {
		this.ratingfor = ratingfor;
	}
	public String getDocumentRaisedRemarks() {
		return documentRaisedRemarks;
	}
	public void setDocumentRaisedRemarks(String documentRaisedRemarks) {
		this.documentRaisedRemarks = documentRaisedRemarks;
	}
	public String getDocumentReplyRemarks() {
		return documentReplyRemarks;
	}
	public void setDocumentReplyRemarks(String documentReplyRemarks) {
		this.documentReplyRemarks = documentReplyRemarks;
	}
	public Integer getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	private String overAllRmrks;
	
	
	public String getProposalNO() {
		return proposalNO;
	}
	public void setProposalNO(String proposalNO) {
		this.proposalNO = proposalNO;
	}
	public String getIntimationNO() {
		return intimationNO;
	}
	public void setIntimationNO(String intimationNO) {
		this.intimationNO = intimationNO;
	}
	public String getProcessType() {
		return processType;
	}
	public void setProcessType(String processType) {
		this.processType = processType;
	}
	public String getDeltedFlag() {
		return deltedFlag;
	}
	public void setDeltedFlag(String deltedFlag) {
		this.deltedFlag = deltedFlag;
	}
	public int getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public Long getFeedbackFor() {
		return feedbackFor;
	}
	public void setFeedbackFor(Long feedbackFor) {
		this.feedbackFor = feedbackFor;
	}
	public String getOverAllRmrks() {
		return overAllRmrks;
	}
	public void setOverAllRmrks(String overAllRmrks) {
		this.overAllRmrks = overAllRmrks;
	}
	public List<ManagerFeedBackPolicyTableDto> getTableDtoList() {
		return tableDtoList;
	}
	public void setTableDtoList(List<ManagerFeedBackPolicyTableDto> tableDtoList) {
		this.tableDtoList = tableDtoList;
	}
	public String getClickType() {
		return clickType;
	}
	public void setClickType(String clickType) {
		this.clickType = clickType;
	}
	public Button getDeleteButton() {
		return deleteButton;
	}
	public void setDeleteButton(Button deleteButton) {
		this.deleteButton = deleteButton;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public SelectValue getFeedBackType() {
		return feedBackType;
	}
	public void setFeedBackType(SelectValue feedBackType) {
		this.feedBackType = feedBackType;
	}
	public SelectValue getFeedBackRating() {
		return feedBackRating;
	}
	public void setFeedBackRating(SelectValue feedBackRating) {
		this.feedBackRating = feedBackRating;
	}
	public Button getBtnFeedbackIcon() {
		return btnFeedbackIcon;
	}
	public void setBtnFeedbackIcon(Button btnFeedbackIcon) {
		this.btnFeedbackIcon = btnFeedbackIcon;
	}
	public SelectValue getFbCategory() {
		return fbCategory;
	}
	public void setFbCategory(SelectValue fbCategory) {
		this.fbCategory = fbCategory;
	}
}
