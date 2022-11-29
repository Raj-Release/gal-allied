package com.shaic.domain;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.preauth.Stage;


@Entity
@Table(name="IMS_CLS_CVC_STAGE_HDR")
@NamedQueries( {
@NamedQuery(name="CVCStageHeader.findByKey", query="SELECT m FROM CVCStageHeader m where m.key = :key"),
@NamedQuery(name="CVCStageHeader.findByIntimationIdandlock", query="SELECT c FROM CVCStageHeader c WHERE c.intimation is not null and c.intimation.intimationId = :intimationNumber and c.lockedBy is not null"),
@NamedQuery(name="CVCStageHeader.findByTransactionKey", query="SELECT c FROM CVCStageHeader c WHERE c.transactionKey =:transactionKey")

})

public class CVCStageHeader extends AbstractEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CVC_STG_KEY")
	private Long key;
	
	@Column(name = "CVC_AUDIT_COMPLETED")
	private int cvcAuditCompleted;

	@Column(name = "CVC_LOCK_FLAG")
	private int cvcLockFlag;
	
	@Column(name = "RELEASED_DATE")
	private Date releasedDate;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="INTIMATION_KEY", nullable=true)
	private Intimation intimation;
	
	@Column(name = "LOCKED_BY")
	private String lockedBy;
	
	@Column(name = "LOCKED_DATE")
	private Date lockedDate;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STATUS_ID",nullable=true)
	private Status status;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STAGE_ID",nullable=true)
	private Stage stage;
	
	@Column(name = "TRANSACTION_KEY")
	private Long transactionKey;
	
	@Column(name = "RELEASED_BY")
	private String releasedBy;
	
	public String getReleasedBy() {
		return releasedBy;
	}

	public void setReleasedBy(String releasedBy) {
		this.releasedBy = releasedBy;
	}

	public Long getTransactionKey() {
		return transactionKey;
	}

	public void setTransactionKey(Long transactionKey) {
		this.transactionKey = transactionKey;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public int getCvcAuditCompleted() {
		return cvcAuditCompleted;
	}

	public void setCvcAuditCompleted(int cvcAuditCompleted) {
		this.cvcAuditCompleted = cvcAuditCompleted;
	}

	public int getCvcLockFlag() {
		return cvcLockFlag;
	}

	public void setCvcLockFlag(int cvcLockFlag) {
		this.cvcLockFlag = cvcLockFlag;
	}
	
	public Date getReleasedDate() {
		return releasedDate;
	}

	public void setReleasedDate(Date releasedDate) {
		this.releasedDate = releasedDate;
	}

	public Intimation getIntimation() {
		return intimation;
	}

	public void setIntimation(Intimation intimation) {
		this.intimation = intimation;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
}
