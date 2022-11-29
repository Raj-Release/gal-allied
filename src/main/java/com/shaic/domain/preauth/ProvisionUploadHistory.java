package com.shaic.domain.preauth;

import java.sql.Timestamp;

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

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.Status;

@Entity
@Table(name = "IMS_CLS_PROVISION_UPLOAD_HTRY")
@NamedQueries({
	@NamedQuery(name = "ProvisionUploadHistory.findAll", query = "SELECT i FROM ProvisionUploadHistory i"),
	@NamedQuery(name = "ProvisionUploadHistory.findByBatchNumber", query = "SELECT i FROM ProvisionUploadHistory i where Lower(i.batchNumber) = :batchNumber and i.statusFlag = 'F' order by i.key asc")
})

public class ProvisionUploadHistory extends AbstractEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_CLS_PROVISION_UPLOAD_HTRY_KEY_GENERATOR", sequenceName = "SEQ_HISTORY_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_PROVISION_UPLOAD_HTRY_KEY_GENERATOR" ) 
	@Column(name="HISTORY_KEY", updatable=false)
	private Long key;

	@Column(name = "INTIMATION_NUMBER")
	private String intimationNumber;
	
	@Column(name = "INTIMATION_KEY")
	private Long intimationKey;
	
	@Column(name = "CLAIM_NUMBER")
	private String claimNumber;
	
	@Column(name = "BATCH_NO")
	private String batchNumber;
	

	@Column(name = "CLAIM_KEY")
	private Long claimKey;
	
	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name = "CURRENT_PROVISION_AMT")
	private Double currentProvisonAmt;
	
	@Column(name = "PREVIOUS_PROVISION_AMT")
	private Double existingProvisionAmt;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STATUS",nullable=true)
	private Status status;
	
	@Column(name = "REMARKS")
	private String remarks;
	
	@Column(name = "STATUS_FLAG")
	private String statusFlag;
	
	@Column(name = "USER_ID")
	private String userId;
	
	

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getIntimationNumber() {
		return intimationNumber;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Double getCurrentProvisonAmt() {
		return currentProvisonAmt;
	}

	public void setCurrentProvisonAmt(Double currentProvisonAmt) {
		this.currentProvisonAmt = currentProvisonAmt;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Double getExistingProvisionAmt() {
		return existingProvisionAmt;
	}

	public void setExistingProvisionAmt(Double existingProvisionAmt) {
		this.existingProvisionAmt = existingProvisionAmt;
	}
	
}
