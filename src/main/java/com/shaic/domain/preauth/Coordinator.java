package com.shaic.domain.preauth;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.Status;

/**
 * The persistent class for the IMS_CLS_COORDINATOR_T database table.
 * 
 */
@Entity
@Table(name="IMS_CLS_COORDINATOR")
@NamedQueries({
	@NamedQuery(name="Coordinator.findAll", query="SELECT i FROM Coordinator i"),
	@NamedQuery(name="Coordinator.findByKey",query="SELECT i FROM Coordinator i where i.key=:primaryKey"),
	@NamedQuery(name="Coordinator.findByPreauthKey", query="SELECT i FROM Coordinator i where i.claim.key = :claimKey"),
	@NamedQuery(name="Coordinator.findByClaimKey", query="SELECT i FROM Coordinator i where i.claim.key = :claimKey"),
	@NamedQuery(name="Coordinator.findByIntimationKey",query="SELECT i FROM Coordinator i where i.intimation.key = :intimationKey"),
	@NamedQuery(name="Coordinator.findByTransactionKey",query="SELECT i FROM Coordinator i where i.transactionKey = :transaction")
})
public class Coordinator extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="COORDINATOR_KEY")
	@SequenceGenerator(name="IMS_CLS_COORDINATOR_KEY_GENERATOR", sequenceName = "SEQ_COORDINATOR_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_COORDINATOR_KEY_GENERATOR")
	private Long key;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

//	@Column(name="ACTIVE_STATUS_DATE")
//	private Timestamp activeStatusDate;

	@Column(name="COORDINATOR_REMARKS")	
	private String coordinatorRemarks;
	
	@Column(name="DOCUMENT_TOKEN")
	private String fileToken;
	
	@Column(name="FILE_NAME")
	private String fileName;
	
	@Column(name="REQUESTOR_REMARKS")
	private String requestorRemarks;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="COORDINATOR_REPLY_DATE")
	private Date coordinatorReplyDate;

	@OneToOne
	@JoinColumn(name="REQUEST_TYPE_ID", nullable=true)	
	private MastersValue coordinatorRequestType;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

//	@Lob
//	@Column(name="FILE_UPLOAD")
//	private byte[] fileUpload;

	@OneToOne
	@JoinColumn(name="INTIMATION_KEY", nullable=false)
	private Intimation intimation;

	@OneToOne
	@JoinColumn(name="POLICY_KEY", nullable=false)
	private Policy policy;

	@OneToOne
	@JoinColumn(name="CLAIM_KEY", nullable=false)
	private Claim claim;
	
	@OneToOne
	@JoinColumn(name="STAGE_ID")
	private Stage stage;
	
//	@Column(name="SUB_STATUS_ID")
//	private Long subStatusId;

//	@Column(name="MIGRATED_APPLICATION_ID")
//	private Long migratedApplicationId;

//	@Column(name="MIGRATED_CODE")
//	private String migratedCode;

//	@Column(name="MODIFIED_BY")
//	private String modifiedBy;

//	@Column(name="MODIFIED_DATE")
//	private Timestamp modifiedDate;

	@Column(name="OFFICE_CODE")
	private String officeCode;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	
	/*@Column(name="REFERING_REASON")
	private String referingReason;*/

	@OneToOne
	@JoinColumn(name="STATUS_ID")
	private Status status;
	
	@Column(name = "TRANSACTION_KEY", nullable = true)
	private Long transactionKey;
	
	@Column(nullable = true, columnDefinition = "VARCHAR(1)", name="TRANSACTION_FLAG", length=1)
	private String transactionFlag;

//	@Column(name="STATUS_DATE")
//	private Timestamp statusDate;

//	@Column(name="VERSION")
//	private Long version;

	public Coordinator() {
	}

	public Long getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getCoordinatorRemarks() {
		return this.coordinatorRemarks;
	}

	public void setCoordinatorRemarks(String coordinatorRemarks) {
		this.coordinatorRemarks = coordinatorRemarks;
	}

	public String getRequestorRemarks() {
		return requestorRemarks;
	}

	public void setRequestorRemarks(String requestorRemarks) {
		this.requestorRemarks = requestorRemarks;
	}

	public Date getCoordinatorReplyDate() {
		return this.coordinatorReplyDate;
	}

	public void setCoordinatorReplyDate(Date coordinatorReplyDate) {
		this.coordinatorReplyDate = coordinatorReplyDate;
	}	

	public MastersValue getCoordinatorRequestType() {
		return coordinatorRequestType;
	}

	public void setCoordinatorRequestType(MastersValue coordinatorRequestType) {
		this.coordinatorRequestType = coordinatorRequestType;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

//	public byte[] getFileUpload() {
//		return this.fileUpload;
//	}
//
//	public void setFileUpload(byte[] fileUpload) {
//		this.fileUpload = fileUpload;
//	}

	public Intimation getIntimation() {
		return this.intimation;
	}

	public void setIntimation(Intimation intimation) {
		this.intimation = intimation;
	}

	public Policy getPolicy() {
		return this.policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	public Long getKey() {
		return this.key;
	}

	public Claim getClaim() {
		return claim;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}

	public void setKey(Long key) {
		this.key = key;
	}

//	public Long getMigratedApplicationId() {
//		return this.migratedApplicationId;
//	}

//	public void setMigratedApplicationId(Long migratedApplicationId) {
//		this.migratedApplicationId = migratedApplicationId;
//	}

//	public String getMigratedCode() {
//		return this.migratedCode;
//	}

//	public void setMigratedCode(String migratedCode) {
//		this.migratedCode = migratedCode;
//	}

//	public String getModifiedBy() {
//		return this.modifiedBy;
//	}

//	public void setModifiedBy(String modifiedBy) {
//		this.modifiedBy = modifiedBy;
//	}

//	public Timestamp getModifiedDate() {
//		return this.modifiedDate;
//	}

//	public void setModifiedDate(Timestamp modifiedDate) {
//		this.modifiedDate = modifiedDate;
//	}

	public String getOfficeCode() {
		return this.officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	/*public String getReferingReason() {
		return this.referingReason;
	}

	public void setReferingReason(String referingReason) {
		this.referingReason = referingReason;
	}*/

	public Status getStatus() {
		return this.status;
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

	public String getFileToken() {
		return fileToken;
	}

	public void setFileToken(String fileToken) {
		this.fileToken = fileToken;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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



}