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

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.preauth.Stage;
@Entity
@Table(name="IMS_CLS_HIS_EXCHANGE_RATE")
@NamedQueries({
@NamedQuery(name="OMPClaimRateChange.findAll", query="SELECT r FROM OMPClaimRateChange r"),
@NamedQuery(name ="OMPClaimRateChange.findByrodKey",query="SELECT r FROM OMPClaimRateChange r WHERE r.reimbursementKey.key = :rodKey")
})
public class OMPClaimRateChange extends AbstractEntity{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_CLS_HIS_EXCHANGE_RATE_KEY_GENERATOR", sequenceName = "SEQ_HIS_EXCHANGE_RATE_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_HIS_EXCHANGE_RATE_KEY_GENERATOR" )
	@Column(name="HIS_EXCHANGE_RATE_KEY")
	private Long key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="INTIMATION_KEY", nullable=false)
	private OMPIntimation intimation;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CLAIM_KEY", nullable=false)
	private OMPClaim claim;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STATUS_ID",nullable=false)
	private Status status;
	
	@Column(name = "USER_ID")
	private String userId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="REIMBURSEMENT_KEY", nullable=false)
	private OMPReimbursement reimbursementKey;
	
	@Column(name="CONVERSION_RATE")
	private Long conversionRate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STAGE_ID", nullable=false)
	private Stage stage;
	
	@Column(name="CURRENCY")
	private Long currency;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus; 
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="HISTORY_DATE")
	private Date historyDate;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public OMPIntimation getIntimation() {
		return intimation;
	}

	public void setIntimation(OMPIntimation intimation) {
		this.intimation = intimation;
	}

	public OMPClaim getClaim() {
		return claim;
	}

	public void setClaim(OMPClaim claim) {
		this.claim = claim;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public OMPReimbursement getReimbursementKey() {
		return reimbursementKey;
	}

	public void setReimbursementKey(OMPReimbursement reimbursementKey) {
		this.reimbursementKey = reimbursementKey;
	}

	public Long getConversionRate() {
		return conversionRate;
	}

	public void setConversionRate(Long conversionRate) {
		this.conversionRate = conversionRate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Long getCurrency() {
		return currency;
	}

	public void setCurrency(Long currency) {
		this.currency = currency;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Date getHistoryDate() {
		return historyDate;
	}

	public void setHistoryDate(Date historyDate) {
		this.historyDate = historyDate;
	}
	
	
}
