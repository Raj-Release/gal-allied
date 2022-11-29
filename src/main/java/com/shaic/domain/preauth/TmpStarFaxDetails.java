package com.shaic.domain.preauth;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the MAS_STAGE_T database table.
 * 
 */
@Entity
@Table(name="IMS_TMP_STAR_FAX_DETAILS")

public class TmpStarFaxDetails implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5281486837892765340L;

	@Id	
	@Column(name="KEY")
	private Long key;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public Long getStageID() {
		return stageID;
	}

	public void setStageID(Long stageID) {
		this.stageID = stageID;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
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

	public Date getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Date sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public Date getActiveStatusDate() {
		return activeStatusDate;
	}

	public void setActiveStatusDate(Date activeStatusDate) {
		this.activeStatusDate = activeStatusDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getClaimId() {
		return claimId;
	}

	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public Date getDocumentDate() {
		return documentDate;
	}

	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

	@Column(name="STATUS_ID")
	private  Long statusId;

	@Column(name="STAGE_ID")
	private Long stageID;

	@Column(name="OFFICE_CODE")
	private String officeCode;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="STATUS_DATE")
	private Date sequenceNumber;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ACTIVE_STATUS_DATE")
	private Date activeStatusDate;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="TRANSACTION_TYPE")
	private String transactionType;

	@Column(name="INTIMATION_NO")
	private String intimationNo;
	
	@Column(name="CLAIM_ID")
	private String claimId;

	@Column(name="STAGE")
	private String stage;
	
	@Column(name ="ENHANCEMENT_REQ_AMT")
	private String enhancementReqAmt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DOCUMENT_DATE")
	private Date documentDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;

	public String getEnhancementReqAmt() {
		return enhancementReqAmt;
	}

	public void setEnhancementReqAmt(String enhancementReqAmt) {
		this.enhancementReqAmt = enhancementReqAmt;
	}

	
}
