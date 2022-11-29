package com.shaic.claim.cashlessprocess.processicac.search;

import java.sql.Timestamp;
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
import com.shaic.domain.Claim;
import com.shaic.domain.Intimation;
import com.shaic.domain.Status;

@Entity
@Table(name = "IMS_CLS_ICAC_RESPONSE")
@NamedQueries({
@NamedQuery(name = "IcacResponse.findAll", query = "SELECT i FROM IcacResponse i"),
@NamedQuery(name = "IcacResponse.findByKey", query="SELECT i FROM IcacResponse i where i.key = :key"),
@NamedQuery(name = "IcacResponse.findResponseByIcacKey", query="SELECT i FROM IcacResponse i where i.icacKey = :icacKey"),
})
public class IcacResponse extends AbstractEntity{
	@Id
	@SequenceGenerator(name="IMS_ICAC_RESPONSE_GENERATOR", sequenceName = "SEQ_ICAC_RESPONSE_KAY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_ICAC_RESPONSE_GENERATOR" ) 
	@Column(name="ICAC_RESPONSE_KEY", updatable=false)
	private Long key;
	
	@Column(name="ICAC_KEY")
	private Long icacKey;
	
	@Column(name="ICAC_RESPONSE")
	private String responseRemarks;
	
	@Column(name="ICAC_FINAL_RESPONSE")
	private String responseFinalRemarks;
	
	@Column(name="ICAC_FINAL_DECISION_FLAG")
	private String finalDecFlag;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STATUS_ID",nullable=false)
	private Status statusId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name = "DELETED_FLAG")
	private String deleteFlag;
	
	@Column(name = "RECORD_TYPE")
	private String recordType;
	
	@Column(name = "REPLIED_DATE")
	private Timestamp repliedDate ;
	
	@Column(name = "REPLIED_BY")
	private String repliedBy;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getIcacKey() {
		return icacKey;
	}

	public void setIcacKey(Long icacKey) {
		this.icacKey = icacKey;
	}

	public String getResponseRemarks() {
		return responseRemarks;
	}

	public void setResponseRemarks(String responseRemarks) {
		this.responseRemarks = responseRemarks;
	}

	public String getResponseFinalRemarks() {
		return responseFinalRemarks;
	}

	public void setResponseFinalRemarks(String responseFinalRemarks) {
		this.responseFinalRemarks = responseFinalRemarks;
	}

	public Timestamp getRepliedDate() {
		return repliedDate;
	}

	public void setRepliedDate(Timestamp repliedDate) {
		this.repliedDate = repliedDate;
	}

	public String getRepliedBy() {
		return repliedBy;
	}

	public void setRepliedBy(String repliedBy) {
		this.repliedBy = repliedBy;
	}

	public String getFinalDecFlag() {
		return finalDecFlag;
	}

	public void setFinalDecFlag(String finalDecFlag) {
		this.finalDecFlag = finalDecFlag;
	}

	public Status getStatusId() {
		return statusId;
	}

	public void setStatusId(Status statusId) {
		this.statusId = statusId;
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

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	
}
