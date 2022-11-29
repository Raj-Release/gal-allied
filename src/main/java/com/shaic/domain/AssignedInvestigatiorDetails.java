package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.preauth.Stage;

@Entity
@Table(name="IMS_CLS_INV_ASSIGNMENT")
@NamedQueries({
	@NamedQuery(name="AssignedInvestigatiorDetails.findAll", query="SELECT o FROM AssignedInvestigatiorDetails o"),
	@NamedQuery(name="AssignedInvestigatiorDetails.findByAssignInvestigaitonKey", query="SELECT o FROM AssignedInvestigatiorDetails o where o.key =:key"),
	@NamedQuery(name="AssignedInvestigatiorDetails.findByInvestigaitonKey", query="SELECT o FROM AssignedInvestigatiorDetails o where o.investigation.key =:investigationkey order by o.key asc"),
	@NamedQuery(name="AssignedInvestigatiorDetails.findByInvestigaitonCode", query="SELECT o FROM AssignedInvestigatiorDetails o where o.investigatorCode =:investigatorCode"),
	@NamedQuery(name="AssignedInvestigatiorDetails.findByInvestigaitonCodeNIntimation", query="SELECT o FROM AssignedInvestigatiorDetails o where o.investigatorCode =:investigatorCode and o.reimbursement is not null and o.reimbursement.claim is not null and o.reimbursement.claim.intimation is not null and o.reimbursement.claim.intimation.key =:intimationKey"),
	@NamedQuery(name="AssignedInvestigatiorDetails.findByinvestigatorName",query="SELECT  o FROM AssignedInvestigatiorDetails o where o.investigatorName =:investigatorName"),
	@NamedQuery(name="AssignedInvestigatiorDetails.findByRodKey",query="SELECT  o FROM AssignedInvestigatiorDetails o where o.reimbursement.key =:rodKey"),
	@NamedQuery(name="AssignedInvestigatiorDetails.findByInvestigaitonCodeAndIntimation", query="SELECT o FROM AssignedInvestigatiorDetails o where o.investigatorCode =:investigatorCode and o.investigation is not null and o.investigation.intimation is not null and o.investigation.intimation.key =:intimationKey"),
	@NamedQuery(name="AssignedInvestigatiorDetails.findByStatusAndReportFlag", query="SELECT o FROM AssignedInvestigatiorDetails o where o.investigation.key =:investigationkey and (o.reportReviewed is null or o.reportReviewed = 'N' or o.reportReviewed = 'Y') and o.status.key in (:statusList) order by o.key desc"),
	@NamedQuery(name="AssignedInvestigatiorDetails.findByStatusAndReviewFlag", query="SELECT o FROM AssignedInvestigatiorDetails o where o.investigation.key =:investigationkey order by o.key asc"),
})
public class AssignedInvestigatiorDetails implements Serializable{

	@Id
	@SequenceGenerator(name="IMS_CLS_SEQ_INV_ASSIGNMENT_KEY_GENERATOR", sequenceName = "SEQ_INV_ASSIGNMENT_KEY" , allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_SEQ_INV_ASSIGNMENT_KEY_GENERATOR" )	
	@Column(name = "INV_ASSIGNMENT_KEY")
	private Long key;
	     
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "INVESTIGATION_KEY")
	private Investigation investigation;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="REIMBURSEMENT_KEY", nullable=false)
	private Reimbursement reimbursement;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ALLOCATION_TO_ID",  nullable=false)
	private MastersValue allocationTo;
	               
	@Column(name="INVESTIGATOR_CODE")
	private String investigatorCode;

	@Column(name="INVESTIGATOR_NAME")
	private String investigatorName;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STATE_ID",  nullable=false)
	private State state;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CITY_ID",  nullable=false)
	private CityTownVillage city;	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="COMPLETION_DATE")
	private Date completionDate;

	@Column(name="CONFIRMED_BY_ID")
	private Long confirmedById;
	
	@Column(name="COMPLETION_REMARKS")
	private String completionRemarks;
	
	@Temporal(TemporalType.DATE)
	@Column(name="HOSPITAL_VISITED_DATE")
	private Date hospitalVisitedDate;
	
	@Column(name="DOCUMENT_RECEIVED_FLAG")
	private Long documentReceivedFlag;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REPORT_RECEIVED_DATE")
	private Date reportReceivedDate;

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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="GRADING_CATEGORY")
	private String gradingCategory;
	
	@Column(name="GRADING_REMARKS")
	private String gradingRemarks;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="GRADING_DATE")
	private Date gradingDate;
	
	@Column(name="GRADED_BY")
	private String gradedBy;
	
	@Column(name="ASSIGNED_FROM")
	private String assignedFrom;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="RE_ASSIGNMENT_DT")
	private Date reassignedDate;
	
	@Column(name="REASSIGN_COMMENTS")
	private String reassignComments;
	
	@Column(name = "PARLL_PROCEED_WITHOUT_RPT")
	private String invsProceedWithoutReport;
	
	@Column(name = "PARLL_CANCEL_REQ")
	private String invsCancelRequest;
	
	@Column(name = "PARLL_CANCEL_REMARKS")
	private String invsCancelRemarks;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "REPORT_REVIEWED", length = 1)
	private String reportReviewed;
	
	@Column(name = "REVIEW_REMARKS")
	private String reviewRemarks;
	
	@Column(name= "ZONE_CODE")
	private String zoneCode;
	
	@Column(name ="STAR_CORDINATOR_NAME")
	private String starCoordinatorName;
	
	@Column(name = "DOCUMENT_TOKEN")
	private String fileToken;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="RVO_FINDINGS",  nullable=false)
	private MastersValue rvoFindings;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="RVO_REASON",  nullable=false)
	private MastersValue rvoReason;
	
	@Transient
	private Integer rollNo;
	
	@Transient
	private SelectValue rvoFindingsKey;
	
	@Transient
	private SelectValue rvoReasonKey;
	
	@Transient
	private SelectValue reviewRemarkskey;
	
	@Transient
	private List<AssignedInvestigatiorDetails> assignedlist;

	public Long getKey() {
		return key;
	}

	public Investigation getInvestigation() {
		return investigation;
	}

	public Reimbursement getReimbursement() {
		return reimbursement;
	}

	public MastersValue getAllocationTo() {
		return allocationTo;
	}

	public String getInvestigatorCode() {
		return investigatorCode;
	}

	public String getInvestigatorName() {
		return investigatorName;
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	public Long getConfirmedById() {
		return confirmedById;
	}

	public String getCompletionRemarks() {
		return completionRemarks;
	}

	public Date getHospitalVisitedDate() {
		return hospitalVisitedDate;
	}

	public Long getDocumentReceivedFlag() {
		return documentReceivedFlag;
	}

	public Date getReportReceivedDate() {
		return reportReceivedDate;
	}

	public Stage getStage() {
		return stage;
	}

	public Status getStatus() {
		return status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public String getGradingCategory() {
		return gradingCategory;
	}

	public String getGradingRemarks() {
		return gradingRemarks;
	}

	public Date getGradingDate() {
		return gradingDate;
	}

	public String getGradedBy() {
		return gradedBy;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public void setInvestigation(Investigation investigation) {
		this.investigation = investigation;
	}

	public void setReimbursement(Reimbursement reimbursement) {
		this.reimbursement = reimbursement;
	}

	public void setAllocationTo(MastersValue allocationTo) {
		this.allocationTo = allocationTo;
	}

	public void setInvestigatorCode(String investigatorCode) {
		this.investigatorCode = investigatorCode;
	}

	public void setInvestigatorName(String investigatorName) {
		this.investigatorName = investigatorName;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	public void setConfirmedById(Long confirmedById) {
		this.confirmedById = confirmedById;
	}

	public void setCompletionRemarks(String completionRemarks) {
		this.completionRemarks = completionRemarks;
	}

	public void setHospitalVisitedDate(Date hospitalVisitedDate) {
		this.hospitalVisitedDate = hospitalVisitedDate;
	}

	public void setDocumentReceivedFlag(Long documentReceivedFlag) {
		this.documentReceivedFlag = documentReceivedFlag;
	}

	public void setReportReceivedDate(Date reportReceivedDate) {
		this.reportReceivedDate = reportReceivedDate;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public void setGradingCategory(String gradingCategory) {
		this.gradingCategory = gradingCategory;
	}

	public void setGradingRemarks(String gradingRemarks) {
		this.gradingRemarks = gradingRemarks;
	}

	public void setGradingDate(Date gradingDate) {
		this.gradingDate = gradingDate;
	}

	public void setGradedBy(String gradedBy) {
		this.gradedBy = gradedBy;
	}

	public State getState() {
		return state;
	}

	public CityTownVillage getCity() {
		return city;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void setCity(CityTownVillage city) {
		this.city = city;
	}

	public String getAssignedFrom() {
		return assignedFrom;
	}

	public Date getReassignedDate() {
		return reassignedDate;
	}

	public String getReassignComments() {
		return reassignComments;
	}

	public void setAssignedFrom(String assignedFrom) {
		this.assignedFrom = assignedFrom;
	}

	public void setReassignedDate(Date reassignedDate) {
		this.reassignedDate = reassignedDate;
	}

	public void setReassignComments(String reassignComments) {
		this.reassignComments = reassignComments;
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

	public String getReportReviewed() {
		return reportReviewed;
	}

	public void setReportReviewed(String reportReviewed) {
		this.reportReviewed = reportReviewed;
	}

	public String getReviewRemarks() {
		return reviewRemarks;
	}

	public void setReviewRemarks(String reviewRemarks) {
		this.reviewRemarks = reviewRemarks;
	}

	public Integer getRollNo() {
		return rollNo;
	}

	public void setRollNo(Integer rollNo) {
		this.rollNo = rollNo;
	}

	public String getZoneCode() {
		return zoneCode;
	}

	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}

	public String getStarCoordinatorName() {
		return starCoordinatorName;
	}

	public String getFileToken() {
		return fileToken;
	}

	public void setFileToken(String fileToken) {
		this.fileToken = fileToken;
	}

	public void setStarCoordinatorName(String starCoordinatorName) {
		this.starCoordinatorName = starCoordinatorName;
	}

	public MastersValue getRvoFindings() {
		return rvoFindings;
	}

	public void setRvoFindings(MastersValue rvoFindings) {
		this.rvoFindings = rvoFindings;
	}

	public MastersValue getRvoReason() {
		return rvoReason;
	}

	public void setRvoReason(MastersValue rvoReason) {
		this.rvoReason = rvoReason;
	}

	public SelectValue getRvoFindingsKey() {
		return rvoFindingsKey;
	}

	public void setRvoFindingsKey(SelectValue rvoFindingsKey) {
		this.rvoFindingsKey = rvoFindingsKey;
	}

	public SelectValue getRvoReasonKey() {
		return rvoReasonKey;
	}

	public void setRvoReasonKey(SelectValue rvoReasonKey) {
		this.rvoReasonKey = rvoReasonKey;
	}

	public SelectValue getReviewRemarkskey() {
		return reviewRemarkskey;
	}

	public void setReviewRemarkskey(SelectValue reviewRemarkskey) {
		this.reviewRemarkskey = reviewRemarkskey;
	}

	public List<AssignedInvestigatiorDetails> getAssignedlist() {
		return assignedlist;
	}

	public void setAssigndlist(List<AssignedInvestigatiorDetails> assignedlist) {
		this.assignedlist = assignedlist;
	}
	
}
