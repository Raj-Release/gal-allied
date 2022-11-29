package com.shaic.domain;

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
import javax.persistence.Transient;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.preauth.Stage;

/**
 * The persistent class for the IMS_CLS_REIMBURSEMENT_QUERY database table.
 * 
 */

/**
 * 
 * @author Yosuva
 *
 */

@Entity
@Table(name="IMS_CLS_REIMBURSEMENT_QUERY")
@NamedQueries({
@NamedQuery(name="ReimbursementQuery.findAll", query="SELECT q FROM ReimbursementQuery q"),
@NamedQuery(name ="ReimbursementQuery.findByKey",query="SELECT q FROM ReimbursementQuery q WHERE q.key = :primaryKey"),
@NamedQuery(name="ReimbursementQuery.findByIntimationKey", query="SELECT q FROM ReimbursementQuery q WHERE q.reimbursement is not null and q.reimbursement.claim is not null and q.reimbursement.claim.intimation is not null and q.reimbursement.claim.intimation.key = :intimationKey"),
@NamedQuery(name="ReimbursementQuery.findByIntimationNumber", query="SELECT q FROM ReimbursementQuery q WHERE q.reimbursement is not null and q.reimbursement.claim is not null and q.reimbursement.claim.intimation is not null and q.reimbursement.claim.intimation.intimationId = :intimationNumber"),
@NamedQuery(name="ReimbursementQuery.findByPolicyNumber", query="SELECT q FROM ReimbursementQuery q WHERE q.reimbursement is not null and q.reimbursement.claim is not null and q.reimbursement.claim.intimation is not null and q.reimbursement.claim.intimation.policy is not null and q.reimbursement.claim.intimation.policy.policyNumber = :policyNumber"),
@NamedQuery(name="ReimbursementQuery.findByClaimKey", query="SELECT q FROM ReimbursementQuery q WHERE q.key = :claimKey"),
@NamedQuery(name="ReimbursementQuery.findByReimbursement", query="SELECT q FROM ReimbursementQuery q WHERE q.reimbursement.key = :reimbursementKey order by q.key desc"),
@NamedQuery(name="ReimbursementQuery.findByReimbursementKeyForPayment", query="SELECT q FROM ReimbursementQuery q WHERE q.reimbursement.key = :reimbursementKey and q.queryType = 'Y' order by q.key desc"),
@NamedQuery(name="ReimbursementQuery.findByReimbursementForQuery", query="SELECT q FROM ReimbursementQuery q WHERE q.reimbursement.key = :reimbursementKey and (q.queryReply is null or q.queryReply = 'N')"),
@NamedQuery(name="ReimbursementQuery.findByReimbursementForQueryReceived", query="SELECT q FROM ReimbursementQuery q WHERE q.reimbursement.key = :reimbursementKey and (q.queryReply is null or q.queryReply = 'Y') order by q.key desc"),
@NamedQuery(name="ReimbursementQuery.findByReceivedQueryForCancelAck", query="SELECT q FROM ReimbursementQuery q WHERE q.reimbursement.key = :reimbursementKey and q.queryReply = 'Y' order by q.key desc"),
@NamedQuery(name="ReimbursementQuery.findLatestQueryByKey",query="SELECT q FROM ReimbursementQuery q WHERE q.key = :primaryKey order by q.key desc"),
@NamedQuery(name="ReimbursementQuery.findLatestDocAckKey",query="SELECT q FROM ReimbursementQuery q WHERE q.docAcknowledgement is not null and q.docAcknowledgement.key = :docAckKey order by q.key desc"),
@NamedQuery(name="ReimbursementQuery.findByReimbursementForQueryPA", query="SELECT q FROM ReimbursementQuery q WHERE q.reimbursement.key = :reimbursementKey and (q.queryReply is null or q.queryReply = 'N') and q.queryType = 'N'"),
@NamedQuery(name="ReimbursementQuery.findByReimbursementForPaymentQueryPA", query="SELECT q FROM ReimbursementQuery q WHERE q.reimbursement.key = :reimbursementKey and (q.queryReply is null or q.queryReply = 'Y') and q.queryType = 'Y'"),
@NamedQuery(name="ReimbursementQuery.findByRodKeyAndStage",query="SELECT q FROM ReimbursementQuery q where q.reimbursement.key =:reimbursementKey and q.stage.key not in (:stageList)"),
@NamedQuery(name="ReimbursementQuery.findByRodKeyAndStatus",query="SELECT q FROM ReimbursementQuery q where q.reimbursement.key =:reimbursementKey and q.status.key in (:statusList)"),
@NamedQuery(name="ReimbursementQuery.findByReimbKeyAndStatus",query="SELECT q FROM ReimbursementQuery q where q.reimbursement.key =:reimbursementKey and q.status.key not in (:statusList) and q.stage.key in (:stageList)"),
})
public class ReimbursementQuery extends AbstractEntity{

	@Id
	@SequenceGenerator(name="IMS_CLS_SEQ_REIMBURSEMENT_QUERY_KEY_GENERATOR", sequenceName = "SEQ_REIMBURSEMENT_QUERY_KEY" , allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_SEQ_REIMBURSEMENT_QUERY_KEY_GENERATOR" )
	@Column(name="REIMBURSEMENT_QUERY_KEY")
	private Long key;

	@OneToOne

	@JoinColumn(name="REIMBURSEMENT_KEY", updatable = false)
	private Reimbursement reimbursement;    
	
	@Column(name="QUERY_REMARKS")
	private String queryRemarks; 
	
	@Column(name="QUERY_LETTER_REMARKS")
	private String queryLetterRemarks; 
	
	@Column(name="REDRAFT_REMARKS")
	private String redraftRemarks; 
	
	@Column(name="REJECTION_REMARKS")
	private String rejectionRemarks;      
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DRAFTED_DATE")
	private Date draftedDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REDRAFT_DATE")
	private Date reDraftDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="APPROVED_REJECTION_DATE")
	private Date approvedRejectionDate;
	
	@Column(name="CREATED_BY")
	private String createdBy;  
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DOCUMENT_RECEIVED_DATE")
	private Date queryReplyDate;
	
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;  
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	
	@Column(name = "QUERY_REPLY")
	private String queryReply;
	
	@OneToOne
	@JoinColumn(name="DOC_ACKNOWLEDGEMENT_KEY")
	private DocAcknowledgement docAcknowledgement; 
	
	@OneToOne
	@JoinColumn(name="STAGE_ID", nullable=false)
	private Stage stage;    
			
	@OneToOne
	@JoinColumn(name="STATUS_ID",nullable=false)
	private Status status;    
	
	@Column(name = "DOCUMENT_TOKEN")
	private Long documentToken;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REMINDER_DATE_1")
	private Date reminderDate1;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REMINDER_DATE_2")
	private Date reminderDate2;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REMINDER_DATE_3")
	private Date reminderDate3;
	
	@Column(name = "REMINDER_COUNT")
	private Long reminderCount;

	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "PAYMENT_QUERY_FLAG", length = 1)
	private String  queryType;
	
	
	@Column(name = "PARLL_CANCEL_REQ")
	private String queryCancelRequest;
	
	
	@Column(name = "PARLL_CANCEL_REMARKS")
	private String queryCancelRemarks;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "QUERY_TYPE_ID", nullable = true)
	private MastersValue qryTyp;
	
	@Column(name = "REL_INST_FLG")
	private String  relInstaFlg;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Reimbursement getReimbursement() {
		return reimbursement;
	}

	public void setReimbursement(Reimbursement reimbursement) {
		this.reimbursement = reimbursement;
	}

	public String getQueryRemarks() {
		return queryRemarks;
	}

	public void setQueryRemarks(String queryRemarks) {
		this.queryRemarks = queryRemarks;
	}

	public String getQueryLetterRemarks() {
		return queryLetterRemarks;
	}

	public void setQueryLetterRemarks(String queryLetterRemarks) {
		this.queryLetterRemarks = queryLetterRemarks;
	}

	public String getRedraftRemarks() {
		return redraftRemarks;
	}

	public void setRedraftRemarks(String redraftRemarks) {
		this.redraftRemarks = redraftRemarks;
	}

	public String getRejectionRemarks() {
		return rejectionRemarks;
	}

	public void setRejectionRemarks(String rejectionRemarks) {
		this.rejectionRemarks = rejectionRemarks;
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

	public Date getDraftedDate() {
		return draftedDate;
	}

	public void setDraftedDate(Date draftedDate) {
		this.draftedDate = draftedDate;
	}

	public Date getReDraftDate() {
		return reDraftDate;
	}

	public void setReDraftDate(Date reDraftDate) {
		this.reDraftDate = reDraftDate;
	}

	public Date getApprovedRejectionDate() {
		return approvedRejectionDate;
	}

	public void setApprovedRejectionDate(Date approvedRejectionDate) {
		this.approvedRejectionDate = approvedRejectionDate;
	}

	public String getQueryReply() {
		return queryReply;
	}

	public void setQueryReply(String queryReply) {
		this.queryReply = queryReply;
	}

	public DocAcknowledgement getDocAcknowledgement() {
		return docAcknowledgement;
	}

	public void setDocAcknowledgement(DocAcknowledgement docAcknowledgement) {
		this.docAcknowledgement = docAcknowledgement;
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

	public Long getDocumentToken() {
		return documentToken;
	}

	public void setDocumentToken(Long documentToken) {
		this.documentToken = documentToken;
	}

	public Date getQueryReplyDate() {
		return queryReplyDate;
	}

	public void setQueryReplyDate(Date queryReplyDate) {
		this.queryReplyDate = queryReplyDate;
	}
	
	public Date getReminderDate1() {
		return reminderDate1;
	}

	public void setReminderDate1(Date reminderDate1) {
		this.reminderDate1 = reminderDate1;
	}

	public Date getReminderDate2() {
		return reminderDate2;
	}

	public void setReminderDate2(Date reminderDate2) {
		this.reminderDate2 = reminderDate2;
	}

	public Date getReminderDate3() {
		return reminderDate3;
	}

	public void setReminderDate3(Date reminderDate3) {
		this.reminderDate3 = reminderDate3;
	}

	public Long getReminderCount() {
		return reminderCount;
	}

	public void setReminderCount(Long reminderCount) {
		this.reminderCount = reminderCount;
	}	

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getQueryCancelRequest() {
		return queryCancelRequest;
	}

	public void setQueryCancelRequest(String queryCancelRequest) {
		this.queryCancelRequest = queryCancelRequest;
	}

	public String getQueryCancelRemarks() {
		return queryCancelRemarks;
	}

	public void setQueryCancelRemarks(String queryCancelRemarks) {
		this.queryCancelRemarks = queryCancelRemarks;
	}

	public MastersValue getQryTyp() {
		return qryTyp;
	}

	public void setQryTyp(MastersValue qryTyp) {
		this.qryTyp = qryTyp;
	}

	public String getRelInstaFlg() {
		return relInstaFlg;
	}

	public void setRelInstaFlg(String relInstaFlg) {
		this.relInstaFlg = relInstaFlg;
	}
	
	

}

