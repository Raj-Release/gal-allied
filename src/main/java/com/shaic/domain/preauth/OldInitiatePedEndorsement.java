package com.shaic.domain.preauth;

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
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.Status;

/**
 * The persistent class for the IMS_CLS_PED_INITIATE_T database table.
 * 
 */
@Entity

@Table(name="IMS_CLS_PED_INITIATE")
@NamedQueries({
	@NamedQuery(name="OldInitiatePedEndorsement.findAll", query="SELECT i FROM OldInitiatePedEndorsement i"),
	@NamedQuery(name="OldInitiatePedEndorsement.findByKey",query="SELECT i FROM OldInitiatePedEndorsement i where i.key=:primaryKey"),
	@NamedQuery(name="OldInitiatePedEndorsement.findByClaim", query="SELECT o FROM OldInitiatePedEndorsement o where o.claim.key =:claimKey"),
	@NamedQuery(name="OldInitiatePedEndorsement.findByClaimAndStatus", query="SELECT o FROM OldInitiatePedEndorsement o where o.claim.key =:claimKey and o.status.key in(:statusKey)"),
	@NamedQuery(name="OldInitiatePedEndorsement.findByInsuredAndpedSuggestion", query="SELECT o FROM OldInitiatePedEndorsement o where o.insuredKey = :insuredKey and o.pedSuggestion.key = :pedSuggestionKey"),
	@NamedQuery(name="OldInitiatePedEndorsement.findByPolicy", query="SELECT o FROM OldInitiatePedEndorsement o where o.policy is not null and o.policy.key =:policyKey"),
	@NamedQuery(name="OldInitiatePedEndorsement.findByPEDKeyInsuredKey", query="SELECT p FROM OldInitiatePedEndorsement p where p.pedSuggestion.key = :pedSuggestionKey and p.insuredKey = :insuredKey and p.status.key <> 44"),
	@NamedQuery(name="OldInitiatePedEndorsement.findByPolicyAndpedSuggestion", query="SELECT o FROM OldInitiatePedEndorsement o where o.policy is not null and o.policy.key =:policyKey and o.pedSuggestion.key = :pedSuggestionKey and o.status.key <> 44"),
	@NamedQuery(name="OldInitiatePedEndorsement.findByPedSuggestionForPolicy", query="SELECT o FROM OldInitiatePedEndorsement o where o.policy is not null and o.policy.key =:policyKey and o.pedSuggestion.key in (:pedSuggestionKeyList) and o.status.key <> 44"),
	@NamedQuery(name="OldInitiatePedEndorsement.findByServiceTransactionId", query="SELECT o FROM OldInitiatePedEndorsement o where o.serviceTransactionID =:serviceTransactionID"),
	@NamedQuery(name="OldInitiatePedEndorsement.findByWorkItemID", query="SELECT o FROM OldInitiatePedEndorsement o where o.workItemID =:workItemID")

})

public class OldInitiatePedEndorsement extends AbstractEntity{
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="IMS_CLS_PED_INITIATE_KEY_GENERATOR", sequenceName = "SEQ_PED_INITIATE_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_PED_INITIATE_KEY_GENERATOR" ) 
	@Column(name="PED_INITIATE_KEY", updatable=false)
	private Long key;
	
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

	@Column(name="OFFICE_CODE")
	private String officeCode;

	@Column(name="PED_INITIATE_NUMBER")
	private String pedInitiateId;

	//@OneToOne
	//@JoinColumn(name="PED_NAME", nullable=true)
	//private PreExistingDisease preExistingDisease;
	
	@Column(name="PED_NAME", nullable=true)
	private String pedName;
	
	@Column(name="REVIEW_REMARKS")
	private String reviewRemarks;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="PED_SUGGESTION_ID")
	private MastersValue pedSuggestion;

	@Column(name="REJECTION_REMARKS")
	private String rejectionRemarks;

	@Column(name="REMARKS")
	private String remarks;
	
	@Column(name="PROCESS_TYPE")
	private String processType;

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
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="WATCH_LIST_DATE")
	private Date watchListDate;
	
	
	
	@Column(name="WATCH_LIST_REMARKS")
	private String watchListRemarks;
	
	@Column(name="INSURED_KEY")
	private Long insuredKey;
	
	
//	@Column(name = "VERSION", nullable = true)
//	@Transient
//	private Long version;
	
	@Column(nullable = true, columnDefinition = "VARCHAR(1)", name="TRANSACTION_FLAG", length=1)
	private String transactionFlag;
	
	@Column(nullable = true, columnDefinition = "VARCHAR(1)", name="WATCH_LIST_FLAG", length=1)
	private String watchListFlag;
	
	@Column(nullable = true, columnDefinition = "VARCHAR(1)", name="INITIATED_FLAG", length=1)
	private String initiateFlag;
	
	@Column(name="ESCALATEREMARKS")
	private String escalateRemarks;	

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
	
	@Column(name="SUGGESTION")
	private String uwSuggestion;
	
	@Column(nullable = true, columnDefinition = "VARCHAR(1)", name="DISCUSS_PED_TL", length=1)
	private String uwTlFlag;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="DISCUSS_WITH")
	private MastersValue uwDiscussWith;
	
	@Column(name="PED_APPR_CODE")
	private String pedApprCode;
	
	@Column(name="PED_APPR_NAME")
	private String pedApprName;
	
	@Column(name="PED_APPR_DATE")
	private Date pedApprDate;
	
	@Column(name="PED_APPR_REMARKS")
	private String pedApprRemarks;
	
	@Column(name="SERVICE_TRANSACTION_ID")
	private String serviceTransactionID;
	
	@Column(name="ENDORSEMENT_STATUS")
	private String endorsementStatus;
	
	@Column(name="ENDORSEMENT_EFFECTIVE_DATE")
	private Date endorsementDate;
	
	@Column(name="GAE_END_APPR_YN")
	private String pedApprYN;
	
	@Column(name="WORK_ITEM_ID")
	private String workItemID;
	
	@Column(name="B_STATUS")
	private String wsStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="PED_EFF_FRM_DT")
	private Date pedEffectiveFromDate;
	
	
	public OldInitiatePedEndorsement() {
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

	public Long getInsuredKey() {
		return insuredKey;
	}

	public void setInsuredKey(Long insuredKey) {
		this.insuredKey = insuredKey;
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

	public Date getWatchListDate() {
		return watchListDate;
	}

	public void setWatchListDate(Date watchListDate) {
		this.watchListDate = watchListDate;
	}

	public String getWatchListFlag() {
		return watchListFlag;
	}

	public void setWatchListFlag(String watchListFlag) {
		this.watchListFlag = watchListFlag;
	}

	public String getWatchListRemarks() {
		return watchListRemarks;
	}

	public void setWatchListRemarks(String watchListRemarks) {
		this.watchListRemarks = watchListRemarks;
	}

	public String getInitiateFlag() {
		return initiateFlag;
	}

	public void setInitiateFlag(String initiateFlag) {
		this.initiateFlag = initiateFlag;
	}

	public String getEscalateRemarks() {
		return escalateRemarks;
	}

	public void setEscalateRemarks(String escalateRemarks) {
		this.escalateRemarks = escalateRemarks;
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

	public String getUwSuggestion() {
		return uwSuggestion;
	}

	public void setUwSuggestion(String uwSuggestion) {
		this.uwSuggestion = uwSuggestion;
	}

	public String getUwTlFlag() {
		return uwTlFlag;
	}

	public void setUwTlFlag(String uwTlFlag) {
		this.uwTlFlag = uwTlFlag;
	}

	public MastersValue getUwDiscussWith() {
		return uwDiscussWith;
	}

	public void setUwDiscussWith(MastersValue uwDiscussWith) {
		this.uwDiscussWith = uwDiscussWith;
	}

	public String getPedApprCode() {
		return pedApprCode;
	}

	public void setPedApprCode(String pedApprCode) {
		this.pedApprCode = pedApprCode;
	}

	public String getPedApprName() {
		return pedApprName;
	}

	public void setPedApprName(String pedApprName) {
		this.pedApprName = pedApprName;
	}

	public Date getPedApprDate() {
		return pedApprDate;
	}

	public void setPedApprDate(Date pedApprDate) {
		this.pedApprDate = pedApprDate;
	}

	public String getPedApprRemarks() {
		return pedApprRemarks;
	}

	public void setPedApprRemarks(String pedApprRemarks) {
		this.pedApprRemarks = pedApprRemarks;
	}

	public String getServiceTransactionID() {
		return serviceTransactionID;
	}

	public void setServiceTransactionID(String serviceTransactionID) {
		this.serviceTransactionID = serviceTransactionID;
	}

	public String getEndorsementStatus() {
		return endorsementStatus;
	}

	public void setEndorsementStatus(String endorsementStatus) {
		this.endorsementStatus = endorsementStatus;
	}

	public Date getEndorsementDate() {
		return endorsementDate;
	}

	public void setEndorsementDate(Date endorsementDate) {
		this.endorsementDate = endorsementDate;
	}

	public String getPedApprYN() {
		return pedApprYN;
	}

	public void setPedApprYN(String pedApprYN) {
		this.pedApprYN = pedApprYN;
	}

	public String getWorkItemID() {
		return workItemID;
	}

	public void setWorkItemID(String workItemID) {
		this.workItemID = workItemID;
	}

	public String getWsStatus() {
		return wsStatus;
	}

	public void setWsStatus(String wsStatus) {
		this.wsStatus = wsStatus;
	}

	public Date getPedEffectiveFromDate() {
		return pedEffectiveFromDate;
	}

	public void setPedEffectiveFromDate(Date pedEffectiveFromDate) {
		this.pedEffectiveFromDate = pedEffectiveFromDate;
	}

	
	

}