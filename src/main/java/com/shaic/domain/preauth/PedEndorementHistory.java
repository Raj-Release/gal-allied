package com.shaic.domain.preauth;

import java.io.Serializable;
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

import com.shaic.domain.Claim;
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.Status;

/**
 * The persistent class for the IMS_CLS_PED_INITIATE_T database table.
 * 
 */
@Entity

@Table(name="IMS_CLS_PED_INITIATE_HIS")
@NamedQueries({
@NamedQuery(name="PedEndorementHistory.findAll", query="SELECT i FROM PedEndorementHistory i"),
@NamedQuery(name="PedEndorementHistory.findByKey",query="SELECT i FROM PedEndorementHistory i where i.key=:primaryKey"),
@NamedQuery(name="PedEndorementHistory.findByClaim", query="SELECT o FROM PedEndorementHistory o where o.claim.key =:claimKey"),
@NamedQuery(name="PedEndorementHistory.findByPedInitiate", query="SELECT o FROM PedEndorementHistory o where o.pedInitiateKey =:pedInitiateKey"),
@NamedQuery(name="PedEndorementHistory.findByPolicy", query="SELECT o FROM PedEndorementHistory o where o.policy is not null and o.policy.key =:policyKey")
})

public class PedEndorementHistory implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@SequenceGenerator(name="IMS_CLS_PED_INITIATE_HIS_KEY_GENERATOR", sequenceName = "SEQ_PED_HIS_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_PED_INITIATE_HIS_KEY_GENERATOR" ) 
	@Column(name="PED_HIS_KEY", updatable=false)
	private Long key;
	
	@Column(name="PED_INITIATE_KEY")
	private Long pedInitiateKey;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;
	
//	@Column(name="ACTIVE_STATUS_DATE")
//	private Timestamp activeStatusDate;

	@Column(name="APPROVAL_REMARKS")
	private String approvalRemarks;

	@Column(name="PROCESSOR_REMARKS")
	private String processorRemarks;
	
	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;


	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="INTIMATION_KEY", nullable=false)
	private Intimation intimation;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="POLICY_KEY", nullable=false)
	private Policy policy;

	//Column name renamed form preauth key to claim key
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CLAIM_KEY",nullable=true)
	private Claim claim;
	
	
	

//	@Column(name="MIGRATED_APPLICATION_ID")
//	private Long migratedApplicationId;

//	@Column(name="MIGRATED_CODE")
//	private String migratedCode;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="REVIEW_REMARKS")
	private String reviewRemarks;

	@Column(name="PROCESS_TYPE")
	private String processType;

	@Column(name="OFFICE_CODE")
	private String officeCode;

	@Column(name="PED_INITIATE_NUMBER")
	private String pedInitiateId;

	//@OneToOne
	//@JoinColumn(name="PED_NAME", nullable=true)
	//private PreExistingDisease preExistingDisease;
	
	@Column(name="PED_NAME", nullable=true)
	private String pedName;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="PED_SUGGESTION_ID")
	private MastersValue pedSuggestion;

	@Column(name="REJECTION_REMARKS")
	private String rejectionRemarks;

	@Column(name="REMARKS")
	private String remarks;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REPUDIATION_LETTER_DATE")
	private Date repudiationLetterDate;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STATUS_ID",nullable=false)
	private Status status;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STAGE_ID",nullable=false)
	private Stage stage;
	
	@Column(name = "TRANSACTION_KEY", nullable = true)
	private Long transactionKey;
	
//	@Column(name = "VERSION", nullable = true)
//	@Transient
//	private Long version;
	
	@Column(nullable = true, columnDefinition = "VARCHAR(1)", name="TRANSACTION_FLAG", length=1)
	private String transactionFlag;

//	@Column(name="STATUS_DATE")
//	private Timestamp statusDate;
	
//	@Column(name="SUB_STATUS_ID")
//	private Long subStatusId;
	
//	@Column(name="VERSION")
//	private Long version;
	
	@Column(name="ADDTOWATCHLIST")
	private String addWatchListFlag;
	
	@Column(name="WATCHLIST_REMARKS")
	private String watchListRmrks;
	
	public PedEndorementHistory() {
	}

	public Long getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getApprovalRemarks() {
		return this.approvalRemarks;
	}

	public void setApprovalRemarks(String approvalRemarks) {
		this.approvalRemarks = approvalRemarks;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Intimation getIntimation() {
		return this.intimation;
	}

	public void setIntimation(Intimation intimation) {
		this.intimation = intimation;
	}

	public Policy getpolicy() {
		return this.policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}
	
	public Long getKey() {
		return this.key;
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

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getOfficeCode() {
		return this.officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public String getPedInitiateId() {
		return this.pedInitiateId;
	}

	public void setPedInitiateId(String pedInitiateId) {
		this.pedInitiateId = pedInitiateId;
	}

	public MastersValue getPedSuggestion() {
		return this.pedSuggestion;
	}

	public void setPedSuggestion(MastersValue pedSuggestion) {
		this.pedSuggestion = pedSuggestion;
	}

	public String getRejectionRemarks() {
		return this.rejectionRemarks;
	}

	public void setRejectionRemarks(String rejectionRemarks) {
		this.rejectionRemarks = rejectionRemarks;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public String getProcessorRemarks() {
		return processorRemarks;
	}

	public void setProcessorRemarks(String processorRemarks) {
		this.processorRemarks = processorRemarks;
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
	
	public Policy getPolicy() {
		return policy;
	}

	public Claim getClaim() {
		return claim;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}

	public Date getRepudiationLetterDate() {
		return repudiationLetterDate;
	}

	public void setRepudiationLetterDate(Date repudiationLetterDate) {
		this.repudiationLetterDate = repudiationLetterDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getPedName() {
		return pedName;
	}

	public void setPedName(String pedName) {
		this.pedName = pedName;
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

//	public Long getVersion() {
//		return version;
//	}
//
//	public void setVersion(Long version) {
//		this.version = version;
//	}

	public Long getPedInitiateKey() {
		return pedInitiateKey;
	}

	public void setPedInitiateKey(Long pedInitiateKey) {
		this.pedInitiateKey = pedInitiateKey;
	}
	
	public String getReviewRemarks() {
		return reviewRemarks;
	}

	public void setReviewRemarks(String reviewRemarks) {
		this.reviewRemarks = reviewRemarks;
	}

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	public String getAddWatchListFlag() {
		return addWatchListFlag;
	}

	public void setAddWatchListFlag(String addWatchListFlag) {
		this.addWatchListFlag = addWatchListFlag;
	}

	public String getWatchListRmrks() {
		return watchListRmrks;
	}

	public void setWatchListRmrks(String watchListRmrks) {
		this.watchListRmrks = watchListRmrks;
	}

	

}