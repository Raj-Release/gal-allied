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
@Entity
@Table(name="IMS_CLS_BFB_FEEDBACK_DETAILS")
@NamedQueries({
	@NamedQuery(name="BranchMangerFeedbackDetailsTable.findByKey",query="select o from BranchMangerFeedbackDetailsTable o where o.key=:primaryKey"),
	@NamedQuery(name="BranchMangerFeedbackDetailsTable.findByFeedbackKey",query="select o from BranchMangerFeedbackDetailsTable o where o.feedbackKey=:feedbackKey")
})
public class BranchMangerFeedbackDetailsTable extends AbstractEntity implements Serializable{

	@Id
	@SequenceGenerator(name="IMS_CLS_BFB_FEEDBACK_DETAILS_KEY_GENERATOR", sequenceName = "SEQ_FEEDBACK_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_BFB_FEEDBACK_DETAILS_KEY_GENERATOR" )
	@Column(name="FEEDBACK_DTLS_KEY ",updatable=false)
	private Long key;
	
	@Column(name="FEEDBACK_KEY",updatable=false)
	private Long feedbackKey;
	
	@Column(name="DOCUMENT_NUMBER")
	private String documentNo;

	
	@Column(name="DOCUMENT_RAISED_REMARKS")
	private String documentRaisedRemarks;
	
	@Column(name="DOCUMENT_REPLY_RAMARKS")
	private String documentReplyRemarks;
	
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
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="FEEDBACK_RATING_ID", nullable=true)
	private MastersValue feedBackRatingId;
	
	@Column(name="FB_REVIEWER_FLAG")
	private String feedBackReviewFlag;
	
	@Column(name="FB_REVIEWER_RATING_ID")
	private Long feedBackReviewerRatingId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STATUS_ID", nullable=true)
	private MastersValue feedbackStatus;

	@Column(name="REVIEW_REPLY_DATE")
	private Date reviewReplyDate;
	
	@Column(name="BM_TYPE_CATEGORY")
	private Long feedbackCategory;
	 
	public Long getFeedbackKey() {
		return feedbackKey;
	}

	public void setFeedbackKey(Long feedbackKey) {
		this.feedbackKey = feedbackKey;
	}

	public String getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
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

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getFeedBackReviewFlag() {
		return feedBackReviewFlag;
	}

	public void setFeedBackReviewFlag(String feedBackReviewFlag) {
		this.feedBackReviewFlag = feedBackReviewFlag;
	}

	public Long getFeedBackReviewerRatingId() {
		return feedBackReviewerRatingId;
	}

	public void setFeedBackReviewerRatingId(Long feedBackReviewerRatingId) {
		this.feedBackReviewerRatingId = feedBackReviewerRatingId;
	}

	public MastersValue getFeedbackStatus() {
		return feedbackStatus;
	}

	public void setFeedbackStatus(MastersValue feedbackStatus) {
		this.feedbackStatus = feedbackStatus;
	}

	public MastersValue getFeedBackRatingId() {
		return feedBackRatingId;
	}

	public void setFeedBackRatingId(MastersValue feedBackRatingId) {
		this.feedBackRatingId = feedBackRatingId;
	}

	public Date getReviewReplyDate() {
		return reviewReplyDate;
	}

	public void setReviewReplyDate(Date reviewReplyDate) {
		this.reviewReplyDate = reviewReplyDate;
	}

	public Long getFeedbackCategory() {
		return feedbackCategory;
	}

	public void setFeedbackCategory(Long feedbackCategory) {
		this.feedbackCategory = feedbackCategory;
	}
}
