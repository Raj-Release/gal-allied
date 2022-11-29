package com.shaic.domain;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.preauth.Stage;

/**
 * @author karthikeyan.r
 * The persistent class for the IMS_CLS_LUMEN_REQUEST database table.
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name="IMS_CLS_LUMEN_REQUEST")
@NamedQueries({
	@NamedQuery(name="LumenRequest.findAll", query="SELECT m FROM LumenRequest m"),
	@NamedQuery(name="LumenRequest.findByKey", query="SELECT m FROM LumenRequest m where m.key = :lumenReqKey"),
	@NamedQuery(name="LumenRequest.findByClaimKey", query="SELECT m FROM LumenRequest m where m.claim.key = :claimKey")
})
public class LumenRequest extends AbstractEntity implements Serializable  {

	@Id
	@SequenceGenerator(name="IMS_CLS_LUMEN_REQUEST_GENERATOR", sequenceName = "SEQ_LUMEN_REQUEST_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_LUMEN_REQUEST_GENERATOR" ) 
	@Column(name="LUMEN_REQUEST_KEY")
	private Long key;
	
	@Column(name="LUMEN_REF_NUMBER")
	private String lumenRefNumber;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CLAIM_KEY", nullable=true)
	private Claim claim;
	
	@Column(name = "TRANSACTION_KEY")
	private Long transactionKey;	
	
	@Column(name="TRANSACTION_FLAG")
	private String transactionFlag;
	
	@Column(name="LUMEN_REMARKS")
	private String lumenRemarks;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LUMEN_INITIATED_DATE")
	private Date lumenInitiatedDate;
	
	@Column(name="PROCESSED_BY")
	private String processedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PROCESSED_DATE")
	private Date processedDate;

	@Column(name = "REVIEWED_BY")
	private String reviewedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REVIEWED_DATE")
	private Date reviewedDate;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REQUESTED_STAGE_ID", nullable= true)
	private Stage requestedStageId;
	
	@Column(name = "LUMEN_TYPE_ID")
	private Long lumenType;
	
	@Column(name = "HOSPITAL_ERROR_TYPE_ID")
	private Long hospitalErrorType;
	
	@Column(name = "ACTIVE_STATUS")
	private Long activeStatus;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STAGE_ID", nullable= true)
	private Stage stage;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_ID", nullable= true)
	private Status status;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	//new columns
	
	@Column(name = "INTIMATION_NUMBER")
	private String intimationNumber;
	
	@Column(name = "POLICY_NUMBER")
	private String policyNumber;
	
	@Column(name = "REMARKS")
	private String remarks;
	
	@Column(name = "LEVEL1_APPROVAL_REMARKS")
	private String level1ApprovalRemarks;
	
	@Column(name = "LEVEL1_REJECT_REMARKS")
	private String level1RejectRemarks;
	
	@Column(name = "LEVEL1_REPLY_REMARKS")
	private String level1ReplyRemarks;
	
	@Column(name = "LEVEL1_CLOSE_REMARKS")
	private String level1CloseRemarks;
	
	@Column(name = "COORD_APPR_REMARKS")
	private String coordApprovalRemarks;
	
	@Column(name = "COORD_REPLY_REMARKS")
	private String coordReplyRemarks;
	
	@Column(name = "GENERATE_LETTER")
	private String generateLetter;
	
	@Column(name = "LEVEL2_APPROVAL_REMARKS")
	private String level2ApprovalRemarks;
	
	@Column(name = "LEVEL2_REJECT_REMARKS")
	private String level2RejectRemarks;
	
	@Column(name = "LEVEL2_REPLY_REMARKS")
	private String level2ReplyRemarks;
	
	@Column(name = "LEVEL2_CLOSE_REMARKS")
	private String level2CloseRemarks;
	
	@Column(name = "SEND_TO")
	private String sendTo;
	
	@Column(name = "INSURED_NAME")
	private String insuredName;
	

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getLumenRefNumber() {
		return lumenRefNumber;
	}

	public void setLumenRefNumber(String lumenRefNumber) {
		this.lumenRefNumber = lumenRefNumber;
	}

	public Claim getClaim() {
		return claim;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}

	public Long getTransactionKey() {
		return transactionKey;
	}

	public void setTransactionKey(Long transactionKey) {
		this.transactionKey = transactionKey;
	}

	public String getTransactionFlag() {
		return transactionFlag;
	}

	public void setTransactionFlag(String transactionFlag) {
		this.transactionFlag = transactionFlag;
	}

	public String getLumenRemarks() {
		return lumenRemarks;
	}

	public void setLumenRemarks(String lumenRemarks) {
		this.lumenRemarks = lumenRemarks;
	}

	public Date getLumenInitiatedDate() {
		return lumenInitiatedDate;
	}

	public void setLumenInitiatedDate(Date lumenInitiatedDate) {
		this.lumenInitiatedDate = lumenInitiatedDate;
	}

	public String getProcessedBy() {
		return processedBy;
	}

	public void setProcessedBy(String processedBy) {
		this.processedBy = processedBy;
	}

	public Date getProcessedDate() {
		return processedDate;
	}

	public void setProcessedDate(Date processedDate) {
		this.processedDate = processedDate;
	}

	public String getReviewedBy() {
		return reviewedBy;
	}

	public void setReviewedBy(String reviewedBy) {
		this.reviewedBy = reviewedBy;
	}

	public Date getReviewedDate() {
		return reviewedDate;
	}

	public void setReviewedDate(Date reviewedDate) {
		this.reviewedDate = reviewedDate;
	}

	public Stage getRequestedStageId() {
		return requestedStageId;
	}

	public void setRequestedStageId(Stage requestedStageId) {
		this.requestedStageId = requestedStageId;
	}

	public Long getLumenType() {
		return lumenType;
	}

	public void setLumenType(Long lumenType) {
		this.lumenType = lumenType;
	}

	public Long getHospitalErrorType() {
		return hospitalErrorType;
	}

	public void setHospitalErrorType(Long hospitalErrorType) {
		this.hospitalErrorType = hospitalErrorType;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getIntimationNumber() {
		return intimationNumber;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getLevel1ApprovalRemarks() {
		return level1ApprovalRemarks;
	}

	public void setLevel1ApprovalRemarks(String level1ApprovalRemarks) {
		this.level1ApprovalRemarks = level1ApprovalRemarks;
	}

	public String getLevel1RejectRemarks() {
		return level1RejectRemarks;
	}

	public void setLevel1RejectRemarks(String level1RejectRemarks) {
		this.level1RejectRemarks = level1RejectRemarks;
	}

	public String getLevel1ReplyRemarks() {
		return level1ReplyRemarks;
	}

	public void setLevel1ReplyRemarks(String level1ReplyRemarks) {
		this.level1ReplyRemarks = level1ReplyRemarks;
	}

	public String getLevel1CloseRemarks() {
		return level1CloseRemarks;
	}

	public void setLevel1CloseRemarks(String level1CloseRemarks) {
		this.level1CloseRemarks = level1CloseRemarks;
	}

	public String getCoordApprovalRemarks() {
		return coordApprovalRemarks;
	}

	public void setCoordApprovalRemarks(String coordApprovalRemarks) {
		this.coordApprovalRemarks = coordApprovalRemarks;
	}

	public String getCoordReplyRemarks() {
		return coordReplyRemarks;
	}

	public void setCoordReplyRemarks(String coordReplyRemarks) {
		this.coordReplyRemarks = coordReplyRemarks;
	}

	public String getGenerateLetter() {
		return generateLetter;
	}

	public void setGenerateLetter(String generateLetter) {
		this.generateLetter = generateLetter;
	}

	public String getLevel2ApprovalRemarks() {
		return level2ApprovalRemarks;
	}

	public void setLevel2ApprovalRemarks(String level2ApprovalRemarks) {
		this.level2ApprovalRemarks = level2ApprovalRemarks;
	}

	public String getLevel2RejectRemarks() {
		return level2RejectRemarks;
	}

	public void setLevel2RejectRemarks(String level2RejectRemarks) {
		this.level2RejectRemarks = level2RejectRemarks;
	}

	public String getLevel2ReplyRemarks() {
		return level2ReplyRemarks;
	}

	public void setLevel2ReplyRemarks(String level2ReplyRemarks) {
		this.level2ReplyRemarks = level2ReplyRemarks;
	}

	public String getLevel2CloseRemarks() {
		return level2CloseRemarks;
	}

	public void setLevel2CloseRemarks(String level2CloseRemarks) {
		this.level2CloseRemarks = level2CloseRemarks;
	}

	public String getSendTo() {
		return sendTo;
	}

	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
	
}
