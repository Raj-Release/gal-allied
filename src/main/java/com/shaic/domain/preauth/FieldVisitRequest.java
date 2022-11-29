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
import com.shaic.domain.TmpEmployee;

/**
 * The persistent class for the IMS_CLS_FIELD_VISIT_REQUEST_T database table.
 * 
 */
@Entity
@Table(name="IMS_CLS_FIELD_VISIT_REQUEST")
@NamedQueries({
	
	@NamedQuery(name="FieldVisitRequest.findAll", query="SELECT i FROM FieldVisitRequest i"),
	@NamedQuery(name="FieldVisitRequest.findByKey",query="SELECT o FROM FieldVisitRequest o where o.key=:primaryKey order by o.key asc"),
	@NamedQuery(name="FieldVisitRequest.findByPreAuthKey",query="SELECT o FROM FieldVisitRequest o where o.claim.key=:claimKey order by o.key asc"),
	@NamedQuery(name="FieldVisitRequest.findByIntimationKey",query="SELECT o FROM FieldVisitRequest o where o.intimation.key=:intimationKey order by o.key asc"),
	@NamedQuery(name="FieldVisitRequest.findByClaimKey",query="SELECT o FROM FieldVisitRequest o where o.claim.key=:claimKey order by o.key asc"),
	@NamedQuery(name = "FieldVisitRequest.findByTransactionKey",query = "SELECT o FROM FieldVisitRequest o where o.transactionKey =:transactionKey"),
	@NamedQuery(name = "FieldVisitRequest.findByReimbursmentKey",query = "SELECT o FROM FieldVisitRequest o where o.transactionKey =:transactionKey order by o.key desc"),
	@NamedQuery(name="FieldVisitRequest.findByStatusKey",query="SELECT o FROM FieldVisitRequest o where o.claim.key=:claimKey and o.status.key IN (:statusList)"),
	@NamedQuery(name="FieldVisitRequest.findByClaimKeyAndStage",query="SELECT o FROM FieldVisitRequest o where o.claim.key=:claimKey and o.stage.key not in (:stageList)"),
	@NamedQuery(name="FieldVisitRequest.findByClaimKeyStageStatus",query="SELECT o FROM FieldVisitRequest o where o.claim.key=:claimKey and o.stage.key IN (:stageList) and o.status.key IN (:statusList)"),
	@NamedQuery(name="FieldVisitRequest.findByRodKeyAndStage",query="SELECT o FROM FieldVisitRequest o where o.transactionKey =:rodKey and o.stage.key not in (:stageList)"),
	@NamedQuery(name="FieldVisitRequest.findByRodKeyAndStatus",query="SELECT o FROM FieldVisitRequest o where o.transactionKey =:rodKey and o.status.key not in (:statusList)"),
	@NamedQuery(name="FieldVisitRequest.findFvrByClaimKey",query="SELECT o FROM FieldVisitRequest o where o.claim.key=:claimKey order by o.key desc"),
	@NamedQuery(name="FieldVisitRequest.findByClaimKeyStatus",query="SELECT o FROM FieldVisitRequest o where o.claim.key=:claimKey and o.stage.key in (:stageList) and o.status.key not in (:statusList)"),
	@NamedQuery(name="FieldVisitRequest.findByClaimKeyandStatus",query="SELECT o FROM FieldVisitRequest o where o.claim.key=:claimKey and o.status.key in (51) order by o.key asc"),
	@NamedQuery(name="FieldVisitRequest.findByClaimKeyandNotinStatus",query="SELECT o FROM FieldVisitRequest o where o.claim.key=:claimKey and o.status.key not in (49,51) order by o.key asc")

})
public class FieldVisitRequest extends AbstractEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SEQ_FIELD_VISIT_REQUEST_KEY", sequenceName = "SEQ_FIELD_VISIT_REQUEST_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_FIELD_VISIT_REQUEST_KEY" ) 
	@Column(name="FVR_KEY")
	private Long key;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;
	
	@Column(name="ASSIGNED_DATE")
	private Timestamp assignedDate;
	
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ALLOCATION_TO_ID", nullable=false)
	private MastersValue allocationTo;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ASSIGN_TO_ID", nullable=true)
	private MastersValue assignTo;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="PRIORITY_ID", nullable=true)
	private MastersValue priority;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="HOSPITAL_VISITED_DATE")
	private Date hospitalVisitedDate;

	/*
	 * documentReceivedFlag is a number field in DB. Hence below code is commented
	 * */
	/*@Column(nullable = false, columnDefinition = "NUMBER", name="DOCUMENT_RECEIVED_FLAG", length=1)
	private Boolean documentReceivedFlag;*/
	
	@Column(name = "DOCUMENT_RECEIVED_FLAG")
	private Long documentReceivedFlag;

	@Column(name="EXECUTIVE_COMMENTS")
	private String executiveComments;

//	@Lob
//	@Column(name="FILE_UPLOAD")
//	private byte[] fileUpload;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="INTIMATION_KEY", nullable=false)
	private Intimation intimation;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="POLICY_KEY", nullable=false)
	private Policy policy;

	//preauth Key changed to claim key Need to change variable name
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CLAIM_KEY", nullable=false)
	private Claim claim;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ASSIGNED_BY", nullable=true)
	private TmpEmployee asigneeName;

	
	@Column(name="FVR_ID")
	private String fvrId;

	@Column(name="FVR_CPU_ID")
	private Long fvrCpuId;


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REPORT_RECEIVED_DATE")
	private Date fvrReceivedDate;

	@Column(name="FVR_TRIGGER_POINTS")
	private String fvrTriggerPoints;

	@Column(name="OFFICE_CODE")
	private String officeCode;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;

	@Column(name="REPRESENTIVE_CODE")
	private String representativeCode;

	@Column(name="REPRESENTIVE_NAME")
	private String representativeName;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STATUS_ID",nullable=false)
	private Status status;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STAGE_ID",nullable=false)
	private Stage stage;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "PATIENT_VERIFIED", length = 1)
	private String  patientVerified;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "DIAGNOSIS_VERIFIED", length = 1)
	private String  diagnosisVerfied;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "ROOM_CATEGORY_VERIFIED", length = 1)
	private String  roomCategoryVerfied;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "TRIGGER_POINTS_FOCUSED", length = 1)
	private String  triggerPointsFocused;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "PED_VERIFIED", length = 1)
	private String  pedVerified;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "PATIENT_DISCHARGED", length = 1)
	private String  patientDischarged;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "PATIENT_NOT_ADMITTED", length = 1)
	private String  patientNotAdmitted;	
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "OUTSTANDING_FVR", length = 1)
	private String  outstandingFvr;
	
	@Column(name = "TRANSACTION_KEY", nullable = true)
	private Long transactionKey;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "REASSIGN_FLAG", length = 1)
	private String  reAssignFlag;
	
	
	@Column(nullable = true, columnDefinition = "VARCHAR(1)", name="TRANSACTION_FLAG", length=1)
	private String transactionFlag;
	
	@Column(name="FVR_GRADING_DATE ")
	private Timestamp fvrGradingDate;
	
	@Column(name = "GRADING_REMARKS")
	private String gradingRmrks;
	
	
	@Column(name = "REPRESENTATIVE_MOBILE_NO")
	private String representativeContactNumber;

	@Column(name = "PARLL_PROCEED_WITHOUT_RPT")
	private String fvrProceedWithoutReport;
	
	public FieldVisitRequest() {
	}

	public Long getFvrCpuId() {
		return fvrCpuId;
	}

	public void setFvrCpuId(Long fvrCpuId) {
		this.fvrCpuId = fvrCpuId;
	}

	public Long getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public MastersValue getAllocationTo() {
		return this.allocationTo;
	}

	public void setAllocationTo(MastersValue allocationTo) {
		this.allocationTo = allocationTo;
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

	/*public Boolean getDocumentReceivedFlag() {
		return this.documentReceivedFlag;
	}

	public void setDocumentReceivedFlag(Boolean documentReceivedFlag) {
		this.documentReceivedFlag = documentReceivedFlag;
	}
*/
	public String getExecutiveComments() {
		return this.executiveComments;
	}

	public void setExecutiveComments(String executiveComments) {
		this.executiveComments = executiveComments;
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

	public Claim getClaim() {

		return this.claim;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}

	public String getFvrId() {
		return this.fvrId;
	}

	public void setFvrId(String fvrId) {
		this.fvrId = fvrId;
	}

	public Date getFvrReceivedDate() {
		return this.fvrReceivedDate;
	}

	public void setFvrReceivedDate(Date fvrReceivedDate) {
		this.fvrReceivedDate = fvrReceivedDate;
	}

	public String getFvrTriggerPoints() {
		return this.fvrTriggerPoints;
	}

	public void setFvrTriggerPoints(String fvrTriggerPoints) {
		this.fvrTriggerPoints = fvrTriggerPoints;
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

	public String getPatientVerified() {
		return patientVerified;
	}

	public void setPatientVerified(String patientVerified) {
		this.patientVerified = patientVerified;
	}

	public String getDiagnosisVerfied() {
		return diagnosisVerfied;
	}

	public void setDiagnosisVerfied(String diagnosisVerfied) {
		this.diagnosisVerfied = diagnosisVerfied;
	}

	public String getRoomCategoryVerfied() {
		return roomCategoryVerfied;
	}

	public void setRoomCategoryVerfied(String roomCategoryVerfied) {
		this.roomCategoryVerfied = roomCategoryVerfied;
	}

	public String getTriggerPointsFocused() {
		return triggerPointsFocused;
	}

	public void setTriggerPointsFocused(String triggerPointsFocused) {
		this.triggerPointsFocused = triggerPointsFocused;
	}

	public String getPedVerified() {
		return pedVerified;
	}

	public void setPedVerified(String pedVerified) {
		this.pedVerified = pedVerified;
	}

	public String getPatientDischarged() {
		return patientDischarged;
	}

	public void setPatientDischarged(String patientDischarged) {
		this.patientDischarged = patientDischarged;
	}

	public String getPatientNotAdmitted() {
		return patientNotAdmitted;
	}

	public void setPatientNotAdmitted(String patientNotAdmitted) {
		this.patientNotAdmitted = patientNotAdmitted;
	}

	public String getOutstandingFvr() {
		return outstandingFvr;
	}

	public void setOutstandingFvr(String outstandingFvr) {
		this.outstandingFvr = outstandingFvr;
	}

	public String getOfficeCode() {
		return this.officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public String getRepresentativeCode() {
		return this.representativeCode;
	}

	public void setRepresentativeCode(String representativeCode) {
		this.representativeCode = representativeCode;
	}

	public String getRepresentativeName() {
		return this.representativeName;
	}

	public void setRepresentativeName(String representativeName) {
		this.representativeName = representativeName;
	}

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

	public Date getHospitalVisitedDate() {
		return hospitalVisitedDate;
	}

	public void setHospitalVisitedDate(Date hospitalVisitedDate) {
		this.hospitalVisitedDate = hospitalVisitedDate;
	}

	public Timestamp getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(Timestamp assignedDate) {
		this.assignedDate = assignedDate;
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

	public Long getDocumentReceivedFlag() {
		return documentReceivedFlag;
	}

	public void setDocumentReceivedFlag(Long documentReceivedFlag) {
		this.documentReceivedFlag = documentReceivedFlag;
	}


	public TmpEmployee getAsigneeName() {
		return asigneeName;
	}

	public void setAsigneeName(TmpEmployee asigneeName) {
		this.asigneeName = asigneeName;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public MastersValue getAssignTo() {
		return assignTo;
	}

	public void setAssignTo(MastersValue assignTo) {
		this.assignTo = assignTo;
	}

	public MastersValue getPriority() {
		return priority;
	}

	public void setPriority(MastersValue priority) {
		this.priority = priority;
	}

	public String getReAssignFlag() {
		return reAssignFlag;
	}

	public void setReAssignFlag(String reAssignFlag) {
		this.reAssignFlag = reAssignFlag;
	}

	public Timestamp getFvrGradingDate() {
		return fvrGradingDate;
	}

	public void setFvrGradingDate(Timestamp fvrGradingDate) {
		this.fvrGradingDate = fvrGradingDate;
	}

	public String getGradingRmrks() {
		return gradingRmrks;
	}

	public void setGradingRmrks(String gradingRmrks) {
		this.gradingRmrks = gradingRmrks;
	}

	public String getRepresentativeContactNumber() {
		return representativeContactNumber;
	}

	public void setRepresentativeContactNumber(String representativeContactNumber) {
		this.representativeContactNumber = representativeContactNumber;
	}
	
	public String getFvrProceedWithoutReport() {
		return fvrProceedWithoutReport;
	}

	public void setFvrProceedWithoutReport(String fvrProceedWithoutReport) {
		this.fvrProceedWithoutReport = fvrProceedWithoutReport;
	}

	
}