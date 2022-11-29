package com.shaic.arch.table;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
import com.shaic.domain.Claim;
import com.shaic.domain.Intimation;

@Entity
@Table(name = "IMS_CLS_CVC_AUDIT_DTLS")
@NamedQueries({
@NamedQuery(name = "AuditDetails.findAll", query = "SELECT i FROM AuditDetails i"),
@NamedQuery(name = "AuditDetails.findByAuditKey", query="SELECT i FROM AuditDetails i where i.key = :key"),
@NamedQuery(name = "AuditDetails.findByKey", query="SELECT i FROM AuditDetails i where i.intimationKey = :intimationKey"),
@NamedQuery(name = "AuditDetails.findByKeyWithPending", query="SELECT i FROM AuditDetails i where i.intimationKey = :intimationKey and i.remediationStatus = :remediationStatus"),
@NamedQuery(name = "AuditDetails.findByRemediationStatus", query = "SELECT i FROM AuditDetails i where i.remediationStatus = :remediationStatus order by i.createdDate asc"),
@NamedQuery(name = "AuditDetails.findByRemediationStatusAndCpu", query = "SELECT i FROM AuditDetails i where i.remediationStatus = :remediationStatus and i.claim.originalCpuCode in (:cpuCodeList) and i.claimType = :clmType"),
@NamedQuery(name = "AuditDetails.findFaQryByRemediationStatusAndCpu", query = "SELECT i FROM AuditDetails i where i.remediationStatus = :remediationStatus and i.claim.originalCpuCode in (:cpuCodeList)"),
@NamedQuery(name = "AuditDetails.findByIntimationKey", query="SELECT i FROM AuditDetails i WHERE i.intimationKey is not null and i.intimationKey = :intimationKey and i.lockedBy is not null")

})
public class AuditDetails extends AbstractEntity{
	@Id
	@SequenceGenerator(name="IMS_AUDIT_KEY_GENERATOR", sequenceName = "SEQ_CVC_AUD_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_AUDIT_KEY_GENERATOR" ) 
	@Column(name="CVC_AUD_KEY", updatable=false)
	private Long key;
	
	
	@Column(name="INTIMATION_KEY")
	private Long intimationKey;
	
	@OneToOne
	@JoinColumn(name="CLAIM_KEY")
	private Claim claim;
	
	@Column(name="TRANSACTION_KEY")
	private Long transactionKey;
	
	@Column(name="CLAIM_TYPE")
	private String claimType;
	
	@Column(name="AUDIT_REMARKS")
	private String auditRemarks;
	
	@Column(name="AUDIT_STATUS")
	private String auditStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;
	
	@Column(name="AUDIT_REMEDIATION_STS")
	private String remediationStatus;
	
	@Column(name="ERROR_CATEGORY")
	private String errorCategory;
	
	@Column(name="ERROR_TEAM")
	private String errorTeam;
	
	@Column(name="CVC_ERROR_PROCESSOR")
	private String errorProcessor;
	
	@Column(name="MONETARY_RESULT")
	private String monetaryResult;
	
	@Column(name="AMT_INVOLVED")
	private Long amountInvolved;
	
	@Column(name="REMEDIATION_REMARKS")
	private String remediationRemarks;
	
	@Column(name="FINAL_AUDIT_STATUS")
	private String finalAuditStatus;
	
	@Column(name="CVC_AUDIT_REASON")
	private String auditReason;	
	
	@Column(name="CVC_LOCK_FLAG")
	private int cvcLockFlag;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "COMPLETED_DATE")
	private Date completedDate;
	
	@Column(name = "AGEING")
	private Long ageing;
	
	@Column(name = "OUTCOME")
	private String outcome;
	
	@Column(name = "OUTCOME_REMARKS")
	private String outcomeRemarks;
	
	@Column(name = "COMPLETED_REASON")
	private String completedReason;
	
	@Column(name = "COMPLETED_REMARKS")
	private String completedRemarks;
	
	@Column(name = "LOCKED_BY")
	private String lockedBy;
	
	@Column(name = "LOCKED_DATE")
	private Date lockedDate;
	
	@Column(name = "RELEASED_BY")
	private String releasedBy;
	
	@Column(name = "RELEASED_DATE")
	private Date releasedDate;
	
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
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

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getAuditRemarks() {
		return auditRemarks;
	}

	public void setAuditRemarks(String auditRemarks) {
		this.auditRemarks = auditRemarks;
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

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getRemediationStatus() {
		return remediationStatus;
	}

	public void setRemediationStatus(String remediationStatus) {
		this.remediationStatus = remediationStatus;
	}

	public String getErrorCategory() {
		return errorCategory;
	}

	public void setErrorCategory(String errorCategory) {
		this.errorCategory = errorCategory;
	}

	public String getErrorTeam() {
		return errorTeam;
	}

	public void setErrorTeam(String errorTeam) {
		this.errorTeam = errorTeam;
	}

	public String getErrorProcessor() {
		return errorProcessor;
	}

	public void setErrorProcessor(String errorProcessor) {
		this.errorProcessor = errorProcessor;
	}

	public String getMonetaryResult() {
		return monetaryResult;
	}

	public void setMonetaryResult(String monetaryResult) {
		this.monetaryResult = monetaryResult;
	}

	public Long getAmountInvolved() {
		return amountInvolved;
	}

	public void setAmountInvolved(Long amountInvolved) {
		this.amountInvolved = amountInvolved;
	}

	public String getRemediationRemarks() {
		return remediationRemarks;
	}

	public void setRemediationRemarks(String remediationRemarks) {
		this.remediationRemarks = remediationRemarks;
	}

	public String getFinalAuditStatus() {
		return finalAuditStatus;
	}

	public void setFinalAuditStatus(String finalAuditStatus) {
		this.finalAuditStatus = finalAuditStatus;
	}

	public String getAuditReason() {
		return auditReason;
	}

	public void setAuditReason(String auditReason) {
		this.auditReason = auditReason;
	}

	public int getCvcLockFlag() {
		return cvcLockFlag;
	}

	public void setCvcLockFlag(int cvcLockFlag) {
		this.cvcLockFlag = cvcLockFlag;
	}

	public String getLockedBy() {
		return lockedBy;
	}

	public void setLockedBy(String lockedBy) {
		this.lockedBy = lockedBy;
	}

	public Date getLockedDate() {
		return lockedDate;
	}

	public void setLockedDate(Date lockedDate) {
		this.lockedDate = lockedDate;
	}

	public String getReleasedBy() {
		return releasedBy;
	}

	public void setReleasedBy(String releasedBy) {
		this.releasedBy = releasedBy;
	}

	public Date getReleasedDate() {
		return releasedDate;
	}

	public void setReleasedDate(Date releasedDate) {
		this.releasedDate = releasedDate;
	}

	public Date getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	public Long getAgeing() {
		return ageing;
	}

	public void setAgeing(Long ageing) {
		this.ageing = ageing;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public String getOutcomeRemarks() {
		return outcomeRemarks;
	}

	public void setOutcomeRemarks(String outcomeRemarks) {
		this.outcomeRemarks = outcomeRemarks;
	}

	public String getCompletedReason() {
		return completedReason;
	}

	public void setCompletedReason(String completedReason) {
		this.completedReason = completedReason;
	}

	public String getCompletedRemarks() {
		return completedRemarks;
	}

	public void setCompletedRemarks(String completedRemarks) {
		this.completedRemarks = completedRemarks;
	}	
	
}
