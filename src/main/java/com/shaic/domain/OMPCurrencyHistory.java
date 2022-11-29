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
@Table(name="IMS_CLS_HIS_CURRENCY_RATE")
@NamedQueries({
@NamedQuery(name="OMPCurrencyHistory.findAll", query="SELECT r FROM OMPCurrencyHistory r"),
@NamedQuery(name ="OMPCurrencyHistory.findByrodKey",query="SELECT r FROM OMPCurrencyHistory r WHERE r.reimbursementKey.key = :rodKey")
})
public class OMPCurrencyHistory extends AbstractEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_CLS_HIS_CURRENCY_RATE_KEY_GENERATOR", sequenceName = "SEQ_HIS_EXCHANGE_RATE_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_HIS_CURRENCY_RATE_KEY_GENERATOR" )
	@Column(name="HIS_CURRENCY_RATE_KEY")
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
	

	@Column(name="CURRENCY_RATE")
	private Long currencyRate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STAGE_ID", nullable=false)
	private Stage stage;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CURRENCY", nullable=false)
	private Currency currency;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus; 
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="HISTORY_DATE")
	private Date historyDate;


	@Column(name="USER_NAME")
	private String userName;
	
	@Override
	public Long getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setKey(Long key) {
		// TODO Auto-generated method stub
		
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

	public Long getCurrencyRate() {
		return currencyRate;
	}

	public void setCurrencyRate(Long currencyRate) {
		this.currencyRate = currencyRate;
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

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
