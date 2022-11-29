package com.shaic.domain.preauth;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "IMS_CLS_CLAIM_AMOUNT_DETL_AUTO")
@NamedQueries({
		@NamedQuery(name = "AutoClaimAmountDetails.findAll", query = "SELECT i FROM AutoClaimAmountDetails i"),
		@NamedQuery(name = "AutoClaimAmountDetails.findByCashlessKey", query = "SELECT i FROM AutoClaimAmountDetails i where i.key = :cashlessKey"),
})
public class AutoClaimAmountDetails implements Serializable{
	@Id
	@Column(name="CLAIM_AMOUNT_DETAILS_KEY", updatable=false)
	private Long key;
	
	@Column(name="CASHLESS_KEY")
	private Long cashlessKey;
	
	@Column(name="INSURED_KEY")
	private Long insuredKey;	
	
	@Column(name="COMPONENT_ID")
	private Long conponentId;

	@Column(name="SEQUENCE_NUMBER")
	private String sequenceNo;
	
	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;	

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="STAGE_ID")
	private Long statusId;

	@Column(name="STATUS_ID")
	private Long stageId;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getCashlessKey() {
		return cashlessKey;
	}

	public void setCashlessKey(Long cashlessKey) {
		this.cashlessKey = cashlessKey;
	}

	public Long getInsuredKey() {
		return insuredKey;
	}

	public void setInsuredKey(Long insuredKey) {
		this.insuredKey = insuredKey;
	}

	public Long getConponentId() {
		return conponentId;
	}

	public void setConponentId(Long conponentId) {
		this.conponentId = conponentId;
	}

	public String getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public Long getStageId() {
		return stageId;
	}

	public void setStageId(Long stageId) {
		this.stageId = stageId;
	}
}
