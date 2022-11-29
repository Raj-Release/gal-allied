package com.shaic.arch.table;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.shaic.arch.fields.dto.AbstractEntity;

/**
 * @author GokulPrasath.A
 *
 */
@Entity
@Table(name = "IMS_CLS_CVC_AUDIT_TRAILS")
@NamedQueries({
@NamedQuery(name = "AuditTrails.findAll", query = "SELECT i FROM AuditTrails i"),
@NamedQuery(name = "AuditTrails.findByIntimationKey", query="SELECT i FROM AuditTrails i where i.intimationKey = :intimationKey order by i.createdDate asc"),
})
public class AuditTrails extends AbstractEntity{
	@Id
	@Column(name="CVC_AUD_TRL_KEY", updatable=false)
	private Long key;
	
	@Column(name="INTIMATION_KEY")
	private Long intimationKey;
	
	@Column(name="TRANSACTION_KEY")
	private Long transactionKey;
	
	@Column(name="CLAIM_TYPE")
	private String claimType;
	
	@Column(name="AUDIT_REMEDIATION_STS")
	private String remediationStatus;
	
	@Column(name = "REMARKS")
	private String remarks;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;
	
	@Column(name = "HISTORY_DATE")
	private Timestamp historyDate;
	
	@Column(name = "REPLY_REMARKS")
	private String qryRplRemarks;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REPLY_DATE")
	private Date qryRplDate;
	
	@Column(name = "REPLY_BY")
	private String rplyUser;
	
	@Column(name="CLAIMS_REPLY")
	private String claimsReply;

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

	public String getRemediationStatus() {
		return remediationStatus;
	}

	public void setRemediationStatus(String remediationStatus) {
		this.remediationStatus = remediationStatus;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Timestamp getHistoryDate() {
		return historyDate;
	}

	public void setHistoryDate(Timestamp historyDate) {
		this.historyDate = historyDate;
	}

	public String getQryRplRemarks() {
		return qryRplRemarks;
	}

	public void setQryRplRemarks(String qryRplRemarks) {
		this.qryRplRemarks = qryRplRemarks;
	}

	public Date getQryRplDate() {
		return qryRplDate;
	}

	public void setQryRplDate(Date qryRplDate) {
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
}