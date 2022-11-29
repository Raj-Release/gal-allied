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
import com.shaic.arch.table.AuditDetails;
import com.shaic.domain.preauth.Stage;

	/**
	 * The persistent class for the IMS_CLS_REIMBURSEMENT_QUERY database table.
	 * 
	 */


	@Entity
	@Table(name="IMS_CLS_CLAIMS_AUDIT_QUERY")
	@NamedQueries({
	@NamedQuery(name ="ClaimAuditQuery.findAudQryByKey",query="SELECT q FROM ClaimAuditQuery q WHERE q.key = :qryKey and q.activeStatus is not null and q.activeStatus = 1"),
	@NamedQuery(name="ClaimAuditQuery.findByAuditKey", query="SELECT q FROM ClaimAuditQuery q WHERE q.audit is not null and q.audit.key = :auditKey and q.activeStatus is not null and q.activeStatus = 1"),
	@NamedQuery(name="ClaimAuditQuery.findByAuditKeyAndStatus", query="SELECT q FROM ClaimAuditQuery q WHERE q.audit is not null and q.audit.key = :auditKey and Lower(q.status) = :status and q.activeStatus is not null and q.activeStatus = 1"),
	@NamedQuery(name="ClaimAuditQuery.findByAuditKeyAndStatusTeamWise", query="SELECT q FROM ClaimAuditQuery q WHERE q.audit is not null and q.audit.key = :auditKey and Lower(q.teamName) = :teamName and Lower(q.status) = :status and q.activeStatus is not null and q.activeStatus = 1"),
	@NamedQuery(name = "ClaimAuditQuery.findFaQryByRemediationStatusAndCpu", query = "SELECT i FROM ClaimAuditQuery i where i.status = :remediationStatus and i.audit.claim.originalCpuCode in (:cpuCodeList) and i.teamName = :team and i.activeStatus is not null and i.activeStatus = 1")

	/*
	@NamedQuery(name="ClaimAuditQuery.findAll", query="SELECT q FROM ReimbursementQuery q"),
	
	@NamedQuery(name="ClaimAuditQuery.findByIntimationNumber", query="SELECT q FROM ReimbursementQuery q WHERE q.reimbursement is not null and q.reimbursement.claim is not null and q.reimbursement.claim.intimation is not null and q.reimbursement.claim.intimation.intimationId = :intimationNumber"),
	@NamedQuery(name="ClaimAuditQuery.findByPolicyNumber", query="SELECT q FROM ReimbursementQuery q WHERE q.reimbursement is not null and q.reimbursement.claim is not null and q.reimbursement.claim.intimation is not null and q.reimbursement.claim.intimation.policy is not null and q.reimbursement.claim.intimation.policy.policyNumber = :policyNumber"),
	@NamedQuery(name="ClaimAuditQuery.findByClaimKey", query="SELECT q FROM ReimbursementQuery q WHERE q.key = :claimKey"),
	@NamedQuery(name="ClaimAuditQuery.findByReimbursement", query="SELECT q FROM ReimbursementQuery q WHERE q.reimbursement.key = :reimbursementKey order by q.key desc"),
	@NamedQuery(name="ClaimAuditQuery.findByReimbursementKeyForPayment", query="SELECT q FROM ReimbursementQuery q WHERE q.reimbursement.key = :reimbursementKey and q.queryType = 'Y' order by q.key desc"),
	@NamedQuery(name="ClaimAuditQuery.findByReimbursementForQuery", query="SELECT q FROM ReimbursementQuery q WHERE q.reimbursement.key = :reimbursementKey and (q.queryReply is null or q.queryReply = 'N')"),
	@NamedQuery(name="ClaimAuditQuery.findByReimbursementForQueryReceived", query="SELECT q FROM ReimbursementQuery q WHERE q.reimbursement.key = :reimbursementKey and (q.queryReply is null or q.queryReply = 'Y') order by q.key desc"),
	@NamedQuery(name="ClaimAuditQuery.findByReceivedQueryForCancelAck", query="SELECT q FROM ReimbursementQuery q WHERE q.reimbursement.key = :reimbursementKey and q.queryReply = 'Y' order by q.key desc"),
	@NamedQuery(name="ClaimAuditQuery.findLatestQueryByKey",query="SELECT q FROM ReimbursementQuery q WHERE q.key = :primaryKey order by q.key desc"),
	@NamedQuery(name="ClaimAuditQuery.findLatestDocAckKey",query="SELECT q FROM ReimbursementQuery q WHERE q.docAcknowledgement is not null and q.docAcknowledgement.key = :docAckKey order by q.key desc"),
	@NamedQuery(name="ClaimAuditQuery.findByReimbursementForQueryPA", query="SELECT q FROM ReimbursementQuery q WHERE q.reimbursement.key = :reimbursementKey and (q.queryReply is null or q.queryReply = 'N') and q.queryType = 'N'"),
	@NamedQuery(name="ClaimAuditQuery.findByReimbursementForPaymentQueryPA", query="SELECT q FROM ReimbursementQuery q WHERE q.reimbursement.key = :reimbursementKey and (q.queryReply is null or q.queryReply = 'Y') and q.queryType = 'Y'"),
	@NamedQuery(name="ClaimAuditQuery.findByRodKeyAndStage",query="SELECT q FROM ReimbursementQuery q where q.reimbursement.key =:reimbursementKey and q.stage.key not in (:stageList)"),
	@NamedQuery(name="ClaimAuditQuery.findByRodKeyAndStatus",query="SELECT q FROM ReimbursementQuery q where q.reimbursement.key =:reimbursementKey and q.status.key in (:statusList)"),
	@NamedQuery(name="ClaimAuditQuery.findByReimbKeyAndStatus",query="SELECT q FROM ReimbursementQuery q where q.reimbursement.key =:reimbursementKey and q.status.key not in (:statusList) and q.stage.key in (:stageList)"),
	*/
	})
	public class ClaimAuditQuery extends AbstractEntity{
		
		@Id
		@SequenceGenerator(name="IMS_CLS_SEQ_AUDIT_QUERY_KEY_GENERATOR", sequenceName = "SEQ_AUDIT_QUERY_KEY" , allocationSize = 1)
		@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_SEQ_AUDIT_QUERY_KEY_GENERATOR" )
		@Column(name="AUDIT_QUERY_KEY")
		private Long key;

		@OneToOne
		@JoinColumn(name="AUDIT_KEY", updatable = false)
		private AuditDetails audit;    
		
		@Column(name="TEAM")
		private String teamName;
				
		@Column(name="REMARKS")
		private String queryRemarks; 
		
		@Temporal(TemporalType.TIMESTAMP)
		@Column(name="RAISED_DATE")
		private Date qryRaiseDate;
		
		@Column(name = "FINALISED_STATUS")
		private String  finalisedStatus;
		
		@Column(name = "FINALISED_REMARKS")
		private String finalisedRemarks;
		
		@Temporal(TemporalType.TIMESTAMP)
		@Column(name="FINALISED_DATE")
		private Date finalisedDate;

		@Column(name="REPLY_REMARKS")
		private String queryReplyRemarks; 
		
		@Temporal(TemporalType.TIMESTAMP)
		@Column(name="REPLY_DATE")
		private Date replyDate;
		
		@Column(name="REPLY_BY")
		private String replyby; 
		
		@Column(name="REPLY_ROLE")
		private String replyRole;      
		
		/*@Column(name = "OUTCOME")
		private String queryOutcome;
		
		@Column(name = "OUTCOME_REMARKS")
		private String queryOutcomeRemarks;*/
		
		@Column(name="STATUS")
		private String status;    
		
		@Column(name="CREATED_BY")
		private String createdBy;  
		
		@Temporal(TemporalType.TIMESTAMP)
		@Column(name="CREATED_DATE")
		private Date createdDate;

		@Column(name="MODIFIED_BY")
		private String modifiedBy;  
		
		@Temporal(TemporalType.TIMESTAMP)
		@Column(name="MODIFIED_DATE")
		private Date modifiedDate;
		
		@Column(name = "ACTIVE_STATUS")
		private Long activeStatus;
		
		@Column(name="FINALISED_BY")
		private String finalisedBy; 
		
		@Column(name="CLAIMS_REPLY")
		private String claimsReply; 
		
		public Long getKey() {
			return key;
		}

		public void setKey(Long key) {
			this.key = key;
		}
		public AuditDetails getAudit() {
			return audit;
		}
		public void setAudit(AuditDetails audit) {
			this.audit = audit;
		}
		public String getTeamName() {
			return teamName;
		}
		public void setTeamName(String teamName) {
			this.teamName = teamName;
		}
		public String getQueryRemarks() {
			return queryRemarks;
		}
		public void setQueryRemarks(String queryRemarks) {
			this.queryRemarks = queryRemarks;
		}
		public Date getQryRaiseDate() {
			return qryRaiseDate;
		}
		public void setQryRaiseDate(Date qryRaiseDate) {
			this.qryRaiseDate = qryRaiseDate;
		}
		public String getFinalisedStatus() {
			return finalisedStatus;
		}
		public void setFinalisedStatus(String finalisedStatus) {
			this.finalisedStatus = finalisedStatus;
		}
		public String getFinalisedRemarks() {
			return finalisedRemarks;
		}
		public void setFinalisedRemarks(String finalisedRemarks) {
			this.finalisedRemarks = finalisedRemarks;
		}
		public Date getFinalisedDate() {
			return finalisedDate;
		}
		public void setFinalisedDate(Date finalisedDate) {
			this.finalisedDate = finalisedDate;
		}
		public String getQueryReplyRemarks() {
			return queryReplyRemarks;
		}
		public void setQueryReplyRemarks(String queryReplyRemarks) {
			this.queryReplyRemarks = queryReplyRemarks;
		}
		public Date getReplyDate() {
			return replyDate;
		}
		public void setReplyDate(Date replyDate) {
			this.replyDate = replyDate;
		}
		public String getReplyby() {
			return replyby;
		}
		public void setReplyby(String replyby) {
			this.replyby = replyby;
		}
		public String getReplyRole() {
			return replyRole;
		}
		public void setReplyRole(String replyRole) {
			this.replyRole = replyRole;
		}
		/*public String getQueryOutcome() {
			return queryOutcome;
		}
		public void setQueryOutcome(String queryOutcome) {
			this.queryOutcome = queryOutcome;
		}
		public String getQueryOutcomeRemarks() {
			return queryOutcomeRemarks;
		}
		public void setQueryOutcomeRemarks(String queryOutcomeRemarks) {
			this.queryOutcomeRemarks = queryOutcomeRemarks;
		}*/
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
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
		public Long getActiveStatus() {
			return activeStatus;
		}
		public void setActiveStatus(Long activeStatus) {
			this.activeStatus = activeStatus;
		}

		public String getFinalisedBy() {
			return finalisedBy;
		}

		public void setFinalisedBy(String finalisedBy) {
			this.finalisedBy = finalisedBy;
		}

		public String getClaimsReply() {
			return claimsReply;
		}

		public void setClaimsReply(String claimsReply) {
			this.claimsReply = claimsReply;
		}
		
		 

}
