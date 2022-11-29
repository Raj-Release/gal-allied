package com.shaic.branchmanagerfeedback;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Status;

@Entity
@Table(name="IMS_CLS_BFB_FEEDBACK")
@NamedQueries({
@NamedQuery(name="BranchManagerFeedbackTable.findAll", query="SELECT m FROM BranchManagerFeedbackTable m"),
@NamedQuery(name="BranchManagerFeedbackTable.findByFilter", query="SELECT m FROM BranchManagerFeedbackTable m where m.feedbackType.key=:feedbackTypeKey and m.feedbackStatus.key=:feedbackStatusKey and m.feedbackBranchCode=:branchCode"),
@NamedQuery(name="BranchManagerFeedbackTable.findByKey",query="SELECT m FROM BranchManagerFeedbackTable m where m.key=:feedbackKey")
		})

public class BranchManagerFeedbackTable extends AbstractEntity implements Serializable{
	@Id
	@SequenceGenerator(name="IMS_CLS_BFB_FEEDBACK_KEY_GENERATOR", sequenceName = "SEQ_FEEDBACK_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_BFB_FEEDBACK_KEY_GENERATOR" )
	@Column(name="FEEDBACK_KEY",updatable=false)
	private Long key;
	
	@Column(name="FEEDBACK_RAISED_DATE")
	private Date feedbackRaisedDate;
	
	@Column(name="FEEDBACK_ZONE_CODE")
	private String feedbackZoneCode;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="FEEBACK_TYPE_ID", nullable=true)
	private MastersValue feedbackType;
	
	@Column(name="FEEDBACK_RAISED_REMARKS")
	private String feedbackRaisedRemarks;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="FEEDBACK_RATING_ID", nullable=true)
	private MastersValue feedbackRating;
	
	@Column(name="FEEDBACK_BRANCH_CODE")
	private String feedbackBranchCode;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STATUS_ID", nullable=true)
	private MastersValue feedbackStatus;
	
	@Column(name="FEEDBACK_REPLY_REMARKS")
	private String feedbackReplyRemarks;
	
	@Column(name="FEEDBACK_REPLY_BY")
	private String feedbackRepliedBy;
	
	@Column(name="FEEDBACK_REPLY_DATE")
	private Date feedbackRepliedDate;
	
	@Column(name="ACTIVE_FLAG")
	private Integer activeFlag;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name="FEEDBACK_RAISED_BY")
	private String feedBackRasiedBy;

	public String getFeedbackZoneCode() {
		return feedbackZoneCode;
	}

	public void setFeedbackZoneCode(String feedbackZoneCode) {
		this.feedbackZoneCode = feedbackZoneCode;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Date getFeedbackRaisedDate() {
		return feedbackRaisedDate;
	}

	public void setFeedbackRaisedDate(Date feedbackRaisedDate) {
		this.feedbackRaisedDate = feedbackRaisedDate;
	}


	public String getFeedbackRaisedRemarks() {
		return feedbackRaisedRemarks;
	}

	public void setFeedbackRaisedRemarks(String feedbackRaisedRemarks) {
		this.feedbackRaisedRemarks = feedbackRaisedRemarks;
	}

	public MastersValue getFeedbackRating() {
		return feedbackRating;
	}

	public void setFeedbackRating(MastersValue feedbackRating) {
		this.feedbackRating = feedbackRating;
	}


	public MastersValue getFeedbackType() {
		return feedbackType;
	}

	public void setFeedbackType(MastersValue feedbackType) {
		this.feedbackType = feedbackType;
	}


	public String getFeedbackBranchCode() {
		return feedbackBranchCode;
	}

	public void setFeedbackBranchCode(String feedbackBranchCode) {
		this.feedbackBranchCode = feedbackBranchCode;
	}
	
	public String getFeedbackReplyRemarks() {
		return feedbackReplyRemarks;
	}

	public void setFeedbackReplyRemarks(String feedbackReplyRemarks) {
		this.feedbackReplyRemarks = feedbackReplyRemarks;
	}

	public String getFeedbackRepliedBy() {
		return feedbackRepliedBy;
	}

	public void setFeedbackRepliedBy(String feedbackRepliedBy) {
		this.feedbackRepliedBy = feedbackRepliedBy;
	}

	public Date getFeedbackRepliedDate() {
		return feedbackRepliedDate;
	}

	public void setFeedbackRepliedDate(Date feedbackRepliedDate) {
		this.feedbackRepliedDate = feedbackRepliedDate;
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

	public MastersValue getFeedbackStatus() {
		return feedbackStatus;
	}

	public void setFeedbackStatus(MastersValue feedbackStatus) {
		this.feedbackStatus = feedbackStatus;
	}

	public String getFeedBackRasiedBy() {
		return feedBackRasiedBy;
	}

	public void setFeedBackRasiedBy(String feedBackRasiedBy) {
		this.feedBackRasiedBy = feedBackRasiedBy;
	}
}

