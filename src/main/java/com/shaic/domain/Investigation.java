package com.shaic.domain;

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
import javax.persistence.Transient;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.preauth.Stage;


/**
 * The persistent class for the IMS_CLS_INVESTIGATION_T database table.
 * 
 */
@Entity
@Table(name="IMS_CLS_INVESTIGATION")
@NamedQueries({
@NamedQuery(name="Investigation.findAll", query="SELECT o FROM Investigation o"),
@NamedQuery(name="Investigation.findByIntimation", query="SELECT o FROM Investigation o where o.intimation.key = :intiationKey order by o.key asc"),
@NamedQuery(name="Investigation.findByClaimKey", query="SELECT o FROM Investigation o where o.claim is not null and o.claim.key = :claimKey order by o.key asc"),
@NamedQuery(name="Investigation.findLatestByClaimKey", query="SELECT o FROM Investigation o where o.claim is not null and o.claim.key = :claimKey order by o.key desc"),
@NamedQuery(name="Investigation.findByInvestigationKey", query="SELECT o FROM Investigation o where o.key = :investigationKey order by o.key asc"),
@NamedQuery(name="Investigation.findByTransactionKey", query="SELECT o FROM Investigation o where o.transactionKey = :transactionKey order by o.key desc"),
@NamedQuery(name="Investigation.findByTransactionKeyAndStatus", query="SELECT o FROM Investigation o where o.transactionKey = :transactionKey and o.status.key in (:statusList)"),
@NamedQuery(name="Investigation.findByClaimKeyAndTransactionFlag", query="SELECT o FROM Investigation o where o.claim is not null and o.claim.key = :claimKey and o.transactionFlag = :transactionFlag order by o.key desc"),
@NamedQuery(name="Investigation.findLatestByClaimKeyAndStatus", query="SELECT o FROM Investigation o where o.claim is not null and o.claim.key = :claimKey and o.status.key not in (:statusList) order by o.key desc"),
@NamedQuery(name="Investigation.findByClaimKeyAndStageDesc", query="SELECT o FROM Investigation o where o.claim is not null and o.claim.key = :claimKey and o.stage.key in (:stageList)order by o.key desc"),
@NamedQuery(name="Investigation.findByClaimKeyAndStatus", query="SELECT o FROM Investigation o where o.claim is not null and o.claim.key = :claimKey and o.status.key in (:statusList) order by o.key desc")
})
public class Investigation extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_CLS_INVESTIGATION_KEY_GENERATOR", sequenceName = "SEQ_INVESTIGATION_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_INVESTIGATION_KEY_GENERATOR" ) 
	@Column(name="INVESTIGATION_KEY")
	private Long key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="INTIMATION_KEY", nullable=false)
	private Intimation intimation;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="POLICY_KEY")
	private Policy policy;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CLAIM_KEY")
	private Claim claim;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ALLOCATION_TO_ID",  nullable=false)
	private MastersValue allocationTo;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="FILE_TYPE_ID")
	private MastersValue fileTypeId;
	
	@Column(name="INVESTIGATOR_CODE")
	private String investigatorCode;

	@Column(name="INVESTIGATOR_NAME")
	private String investigatorName;
	
	@Column(name="REASON_FOR_REFERRING")
	private String reasonForReferring;
	
	@Column(name="TRIGGER_POINTS_FOCUS")
	private String triggerPoints;
	
	@Column(name="REMARKS")
	private String remarks;
	
	@Column(name="ASSIGNED_BY")
	private String assignedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ASSIGNED_DATE")
	private Date assignedDate;
	
	@Column(name="CLAIM_BACKGROUND_DETAILS")
	private String claimBackgroundDetails;
	
	@Column(name="INVESTIGATION_TRIGGER_POINTS")
	private String investigationTriggerPoints;
	
	
	@Column(name="COMPLETION_DATE")
	private Timestamp completionDate;
	
	@Column(name="CONFIRMED_BY_ID")
	private Long confirmedById;
	
	@Column(name="COMPLETION_REMARKS")
	private String completionRemarks;
	
	@Temporal(TemporalType.DATE)
	@Column(name="HOSPITAL_VISITED_DATE")
	private Date hospitalVisitedDate;
	
	@Column(name="DOCUMENT_RECEIVED_FLAG")
	private Long documentReceivedFlag;
	
//	@Lob	
//	@Column(name="FILE_UPLOAD")
//	private byte[] fileUpload;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REPORT_RECEIVED_DATE")
	private Date reportReceivedDate;
	
	@Column(name="ACTIVE_STATUS")
	private Integer activeStatus;
	
	@Column(name="OFFICE_CODE")
	private String officeCode;
	
	@Column(name="FILE_NAME")
	private String fileName;
	
	@Column(name="DOCUMENT_TOKEN")
	private String token;
	
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STAGE_ID", nullable=false)
	private Stage stage;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STATUS_ID", nullable=false)
	private Status status;
	
	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name = "TRANSACTION_KEY", nullable = true)
	private Long transactionKey;
	
	@Column(name = "FACTS_OF_CASE")
	private String factsOfCase;

	@Column(nullable = true, columnDefinition = "VARCHAR(1)", name="TRANSACTION_FLAG", length=1)
	private String transactionFlag;
	
	@Column(name="GRADING_CATEGORY")
	private String gradingCategory;
	
	@Column(name="GRADING_REMARKS")
	private String gradingRemarks;

	
	@Column(name = "PARLL_PROCEED_WITHOUT_RPT")
	private String invsProceedWithoutReport;
	
	@Column(name = "PARLL_CANCEL_REQ")
	private String invsCancelRequest;
	
	@Column(name = "PARLL_CANCEL_REMARKS")
	private String invsCancelRemarks;
	
	@Column(name = "INV_APPROVED_BY")
	private String approvedBy;
	
	@Column(name = "INV_DRAFT_LT_BY")
	private String draftLetterBy;
	
	@Column(name="INV_APPROVED_DATE")
	private Timestamp approvedDate;
	
	@Column(name="INV_DRAFT_LT_DATE")
	private Timestamp draftedDate;
	
	// CR2019058 New date columns added
	
	@Column(name="INV_INITIATED_DATE")
	private Timestamp initiatedDate;

	@Column(name="INV_REQ_DRAFTED_DATE")
	private Timestamp reqDraftedDate;


	@Column(name="UHID_IP_NO")
	private String uhidIpNo;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="REASON_FOR_INITIATE_INV",  nullable=false)
	private MastersValue reasonForInitiatingInv;

	
	public String getUhidIpNo() {
		return uhidIpNo;
	}

	public void setUhidIpNo(String uhidIpNo) {
		this.uhidIpNo = uhidIpNo;
	}

	public Investigation() {
	}

	public Long getKey() {
		return key;
	}


	public void setKey(Long key) {
		this.key = key;
	}




	public Intimation getIntimation() {
		return intimation;
	}




	public void setIntimation(Intimation intimation) {
		this.intimation = intimation;
	}




	public Policy getPolicy() {
		return policy;
	}




	public void setPolicy(Policy policy) {
		this.policy = policy;
	}




	public Claim getClaim() {
		return claim;
	}




	public void setClaim(Claim claim) {
		this.claim = claim;
	}




	public MastersValue getAllocationTo() {
		return allocationTo;
	}




	public void setAllocationTo(MastersValue allocationTo) {
		this.allocationTo = allocationTo;
	}




	public String getInvestigatorCode() {
		return investigatorCode;
	}




	public void setInvestigatorCode(String investigatorCode) {
		this.investigatorCode = investigatorCode;
	}




	public String getInvestigatorName() {
		return investigatorName;
	}




	public void setInvestigatorName(String investigatorName) {
		this.investigatorName = investigatorName;
	}




	public String getReasonForReferring() {
		return reasonForReferring;
	}




	public void setReasonForReferring(String reasonForReferring) {
		this.reasonForReferring = reasonForReferring;
	}




	public String getTriggerPoints() {
		return triggerPoints;
	}




	public void setTriggerPoints(String triggerPoints) {
		this.triggerPoints = triggerPoints;
	}




	public String getRemarks() {
		return remarks;
	}




	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}




	public String getAssignedBy() {
		return assignedBy;
	}




	public void setAssignedBy(String assignedBy) {
		this.assignedBy = assignedBy;
	}




	public Date getAssignedDate() {
		return assignedDate;
	}




	public void setAssignedDate(Date assignedDate) {
		this.assignedDate = assignedDate;
	}




	public String getClaimBackgroundDetails() {
		return claimBackgroundDetails;
	}




	public void setClaimBackgroundDetails(String claimBackgroundDetails) {
		this.claimBackgroundDetails = claimBackgroundDetails;
	}




	public String getInvestigationTriggerPoints() {
		return investigationTriggerPoints;
	}




	public void setInvestigationTriggerPoints(String investigationTriggerPoints) {
		this.investigationTriggerPoints = investigationTriggerPoints;
	}



	public Long getConfirmedById() {
		return confirmedById;
	}




	public void setConfirmedById(Long confirmedById) {
		this.confirmedById = confirmedById;
	}




	public String getCompletionRemarks() {
		return completionRemarks;
	}




	public void setCompletionRemarks(String completionRemarks) {
		this.completionRemarks = completionRemarks;
	}




	public Date getHospitalVisitedDate() {
		return hospitalVisitedDate;
	}




	public void setHospitalVisitedDate(Date hospitalVisitedDate) {
		this.hospitalVisitedDate = hospitalVisitedDate;
	}




	public Long getDocumentReceivedFlag() {
		return documentReceivedFlag;
	}




	public void setDocumentReceivedFlag(Long documentReceivedFlag) {
		this.documentReceivedFlag = documentReceivedFlag;
	}

//
//	public byte[] getFileUpload() {
//		return fileUpload;
//	}
//
//
//
//
//	public void setFileUpload(byte[] fileUpload) {
//		this.fileUpload = fileUpload;
//	}
//

	public Date getReportReceivedDate() {
		return reportReceivedDate;
	}




	public void setReportReceivedDate(Date reportReceivedDate) {
		this.reportReceivedDate = reportReceivedDate;
	}




	public Integer getActiveStatus() {
		return activeStatus;
	}




	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}




	public String getOfficeCode() {
		return officeCode;
	}




	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
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




	public Timestamp getModifiedDate() {
		return modifiedDate;
	}




	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}




	public MastersValue getFileTypeId() {
		return fileTypeId;
	}




	public void setFileTypeId(MastersValue fileTypeId) {
		this.fileTypeId = fileTypeId;
	}




	public String getFileName() {
		return fileName;
	}




	public void setFileName(String fileName) {
		this.fileName = fileName;
	}




	public String getToken() {
		return token;
	}




	public void setToken(String token) {
		this.token = token;
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




	public Timestamp getCompletionDate() {
		return completionDate;
	}




	public void setCompletionDate(Timestamp completionDate) {
		this.completionDate = completionDate;
	}




	public String getGradingCategory() {
		return gradingCategory;
	}




	public void setGradingCategory(String gradingCategory) {
		this.gradingCategory = gradingCategory;
	}




	public String getGradingRemarks() {
		return gradingRemarks;
	}




	public void setGradingRemarks(String gradingRemarks) {
		this.gradingRemarks = gradingRemarks;
	}

	
	public String getFactsOfCase() {
		return factsOfCase;
	}




	public void setFactsOfCase(String factsOfCase) {
		this.factsOfCase = factsOfCase;
	}




	public String getInvsProceedWithoutReport() {
		return invsProceedWithoutReport;
	}




	public void setInvsProceedWithoutReport(String invsProceedWithoutReport) {
		this.invsProceedWithoutReport = invsProceedWithoutReport;
	}




	public String getInvsCancelRequest() {
		return invsCancelRequest;
	}




	public void setInvsCancelRequest(String invsCancelRequest) {
		this.invsCancelRequest = invsCancelRequest;
	}




	public String getInvsCancelRemarks() {
		return invsCancelRemarks;
	}




	public void setInvsCancelRemarks(String invsCancelRemarks) {
		this.invsCancelRemarks = invsCancelRemarks;
	}




	public String getApprovedBy() {
		return approvedBy;
	}




	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}




	public String getDraftLetterBy() {
		return draftLetterBy;
	}




	public void setDraftLetterBy(String draftLetterBy) {
		this.draftLetterBy = draftLetterBy;
	}




	public Timestamp getApprovedDate() {
		return approvedDate;
	}




	public void setApprovedDate(Timestamp approvedDate) {
		this.approvedDate = approvedDate;
	}




	public Timestamp getDraftedDate() {
		return draftedDate;
	}




	public void setDraftedDate(Timestamp draftedDate) {
		this.draftedDate = draftedDate;
	}

	public Timestamp getInitiatedDate() {
		return initiatedDate;
	}

	public void setInitiatedDate(Timestamp initiatedDate) {
		this.initiatedDate = initiatedDate;
	}

	public Timestamp getReqDraftedDate() {
		return reqDraftedDate;
	}

	public void setReqDraftedDate(Timestamp reqDraftedDate) {
		this.reqDraftedDate = reqDraftedDate;
	}

	public MastersValue getReasonForInitiatingInv() {
		return reasonForInitiatingInv;
	}

	public void setReasonForInitiatingInv(MastersValue reasonForInitiatingInv) {
		this.reasonForInitiatingInv = reasonForInitiatingInv;
	}
	
	
}