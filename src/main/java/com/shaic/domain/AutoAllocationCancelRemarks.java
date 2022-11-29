package com.shaic.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;
@Entity
@Table(name="IMS_CLS_CANCEL_TRANSACTION")
@NamedQueries({
})

public class AutoAllocationCancelRemarks extends AbstractEntity{
	@Id
	@SequenceGenerator(name="IMS_CLS_GET_NEXT_CANCEL_KEY_GENERATOR", sequenceName = "SEQ_GN_CANCEL_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_GET_NEXT_CANCEL_KEY_GENERATOR" ) 
	@Column(name="GN_CANCEL_KEY", updatable=false)
	private Long key;	
	
	@Column(name="INTIMATION_KEY")
	private Long intimationKey;
	
	@Column(name="CLAIM_KEY")
	private Long claimKey;
	
	@Column(name="TRANSACTION_KEY", updatable=false)
	private Long transactionKey;
	
	@Column(name="TRANSACTION_TYPE")
	private String transactionType;
	
	@Column(name="CANCEL_REMARKS")
	private String cancelRemarks;
	
	@Column(name="CANCELLED_BY")
	private String cancelledBy;
	
	@Column(name="CANCELLED_DATE")
	private Timestamp cancelledDate;
	
	@Column(name="STAGE_ID")
	private Long stageId;

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

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getCancelRemarks() {
		return cancelRemarks;
	}

	public void setCancelRemarks(String cancelRemarks) {
		this.cancelRemarks = cancelRemarks;
	}

	public String getCancelledBy() {
		return cancelledBy;
	}

	public void setCancelledBy(String cancelledBy) {
		this.cancelledBy = cancelledBy;
	}

	public Timestamp getCancelledDate() {
		return cancelledDate;
	}

	public void setCancelledDate(Timestamp cancelledDate) {
		this.cancelledDate = cancelledDate;
	}

	public Long getStageId() {
		return stageId;
	}

	public void setStageId(Long stageId) {
		this.stageId = stageId;
	}

	public Long getTransactionKey() {
		return transactionKey;
	}

	public void setTransactionKey(Long transactionKey) {
		this.transactionKey = transactionKey;
	}
}
