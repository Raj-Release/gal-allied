package com.shaic.claim.reports.negotiationreport;

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
import com.shaic.domain.Intimation;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.preauth.Stage;

@Entity
@Table(name="IMS_CLS_NEGOTIATION_AMT_DTLS")
@NamedQueries({
		@NamedQuery(name="NegotiationAmountDetails.findAll",query="Select i from NegotiationAmountDetails i"),
		@NamedQuery(name="NegotiationAmountDetails.findByIntimationKey",query="Select i from NegotiationAmountDetails i where i.intimationKey = :intmnKey"),
		@NamedQuery(name="NegotiationAmountDetails.findByIntimationKeyDesc",query="Select i from NegotiationAmountDetails i where i.intimationKey = :intmnKey order by i.key desc"),
		@NamedQuery(name="NegotiationAmountDetails.findByTransactionKey",query="Select i from NegotiationAmountDetails i where i.transactionKey = :transactionKey")
		})
public class NegotiationAmountDetails extends AbstractEntity{

	@Id
	@SequenceGenerator(name="IMS_CLS_NEGOTIATION_AMOUNT_KEY_GENERATOR", sequenceName = "SEQ_NEGOTIATION_AMT_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_NEGOTIATION_AMOUNT_KEY_GENERATOR") 
	@Column(name="NEGOTIATION_AMT_KEY")
	private Long key;
	

	@Column(name = "INTIMATION_KEY")
	private Long intimationKey;
	
	@Column(name="INTIMATION_NUMBER")
	private String intimationNo;
	
	@Column(name="TRANSACTION_KEY")
	private Long transactionKey;
	
	@Column(name="TRANSACTION_FLAG")
	private String transactionFlag;
	
	@Column(name="CPU_ID")
	private Long cpuKey;
	
	@Column(name="NEGOTIATED_AMOUNT")
	private Double negotiatedAmt;
	
	@Column(name="SAVED_AMOUNT")
	private Double savedAmt;
	
	@Column(name="CLAIM_APPROVED_AMOUNT")
	private Double claimAppAmt;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STAGE_ID", nullable = false)
	private Stage stage;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_ID", nullable = false)
	private Status status;
	
	@Column(name="ACTIVES_STATUS")
	private Long activeStatus;
	
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
	
	@Column(name="NEGOTIATION_WITH")
	private String negotiationWith;
	
	@Column(name="HST_VALUE_OF_CL_TRANS")
	private Double hstCLTrans;
	
	@Column(name="TOTAL_NEGOTIATED_SAVINGS")
	private Double totalNegotiationSaved;
	
	@Column(name="CLAIMED_AMOUNT")
	private Double claimedAmt;

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

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
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

	public Long getCpuKey() {
		return cpuKey;
	}

	public void setCpuKey(Long cpuKey) {
		this.cpuKey = cpuKey;
	}

	public Double getNegotiatedAmt() {
		return negotiatedAmt;
	}

	public void setNegotiatedAmt(Double negotiatedAmt) {
		this.negotiatedAmt = negotiatedAmt;
	}

	public Double getSavedAmt() {
		return savedAmt;
	}

	public void setSavedAmt(Double savedAmt) {
		this.savedAmt = savedAmt;
	}

	public Double getClaimAppAmt() {
		return claimAppAmt;
	}

	public void setClaimAppAmt(Double claimAppAmt) {
		this.claimAppAmt = claimAppAmt;
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

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
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

	public String getNegotiationWith() {
		return negotiationWith;
	}

	public void setNegotiationWith(String negotiationWith) {
		this.negotiationWith = negotiationWith;
	}

	public Double getHstCLTrans() {
		return hstCLTrans;
	}

	public void setHstCLTrans(Double hstCLTrans) {
		this.hstCLTrans = hstCLTrans;
	}

	public Double getTotalNegotiationSaved() {
		return totalNegotiationSaved;
	}

	public void setTotalNegotiationSaved(Double totalNegotiationSaved) {
		this.totalNegotiationSaved = totalNegotiationSaved;
	}

	public Double getClaimedAmt() {
		return claimedAmt;
	}

	public void setClaimedAmt(Double claimedAmt) {
		this.claimedAmt = claimedAmt;
	}
	
	

}
