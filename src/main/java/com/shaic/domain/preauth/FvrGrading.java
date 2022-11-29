//package com.shaic.domain.preauth;
//
//import java.io.Serializable;
//
//import javax.persistence.*;
//
//import com.shaic.arch.fields.dto.AbstractEntity;
//import com.shaic.domain.Status;
//
//import java.sql.Timestamp;
//
///**
// * The persistent class for the IMS_CLS_FVR_GRADING_T database table.
// * 
// */
//@Entity
//@Table(name="IMS_CLS_FVR_GRADING")
//@NamedQuery(name="FvrGrading.findAll", query="SELECT i FROM FvrGrading i")
//public class FvrGrading extends AbstractEntity {
//	private static final long serialVersionUID = 1L;
//
//	@Id
//	@SequenceGenerator(name="IMS_CLS_FVR_GRADING_KEY_GENERATOR", sequenceName = "SEQ_FVR_GRADING_KEY", allocationSize = 1)
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_FVR_GRADING_KEY_GENERATOR" ) 
//	@Column(name="KEY")
//	private Long key;
//	
//	@Column(name="ACTIVE_STATUS")
//	private Long activeStatus;
//
////	@Column(name="ACTIVE_STATUS_DATE")
////	private Timestamp activeStatusDate;
//
//	@Column(name="CREATED_BY")
//	private String createdBy;
//
//	@Column(name="CREATED_DATE")
//	private Timestamp createdDate;
//
//	@OneToOne
//	@JoinColumn(name="FVR_KEY", nullable=false)
//	private FieldVisitRequest fvr;
//
////	@Column(name="MIGRATED_APPLICATION_ID")
////	private Long migratedApplicationId;
//
////	@Column(name="MIGRATED_CODE")
////	private String migratedCode;
//
////	@Column(name="MODIFIED_BY")
////	private String modifiedBy;
//
////	@Column(name="MODIFIED_DATE")
////	private Timestamp modifiedDate;
//
//	@Column(nullable = false, columnDefinition = "NUMBER", name="NONE_OF_THE_ABOVE", length=1)
//	private Boolean noneOfTheAbove;
//
//	@Column(name="OFFICE_CODE")
//	private String officeCode;
//
//	@OneToOne
//	@JoinColumn(name="STATUS_ID",nullable=false)
//	private Status status;
//	
////	@Column(name="SUB_STATUS_ID")
////	private Long substatusId;
//	
//	@OneToOne
//	@JoinColumn(name="STAGE_ID",nullable=false)
//	private Stage stage;
//	
////	@Column(name="STATUS_DATE")
////	private Timestamp statusDate;
//
//	@Column(nullable = false, columnDefinition = "NUMBER", name="TARIFF_VERIFIED", length=1)
//	private Boolean tariffVerified;
//
//	@Column(nullable = false, columnDefinition = "NUMBER", name="TERMINATED_FV_PATIENT_DISCHA", length=1)
//	private Boolean terminatedFvPatientDischa;
//
//	@Column(nullable = false, columnDefinition = "NUMBER", name="TOT_QUANTUM_REDU_ACH", length=1)
//	private Boolean totQuantumReduAch;
//
//	@Column(nullable = false, columnDefinition = "NUMBER", name="TRACED_PED", length=1)
//	private Boolean tracedPed;
//
//	@Column(nullable = false, columnDefinition = "NUMBER", name="TRIGGER_POINT_ATTACHED", length=1)
//	private Boolean triggerPointAttached;
//
////	@Column(name="VERSION")
////	private Long version;
//
//	public FvrGrading() {
//	}
//
//	public Long getActiveStatus() {
//		return this.activeStatus;
//	}
//
//	public void setActiveStatus(Long activeStatus) {
//		this.activeStatus = activeStatus;
//	}
//
////	public Timestamp getActiveStatusDate() {
////		return this.activeStatusDate;
////	}
////
////	public void setActiveStatusDate(Timestamp activeStatusDate) {
////		this.activeStatusDate = activeStatusDate;
////	}
//
//	public String getCreatedBy() {
//		return this.createdBy;
//	}
//
//	public void setCreatedBy(String createdBy) {
//		this.createdBy = createdBy;
//	}
//
//	public Timestamp getCreatedDate() {
//		return this.createdDate;
//	}
//
//	public void setCreatedDate(Timestamp createdDate) {
//		this.createdDate = createdDate;
//	}
//
//	public FieldVisitRequest getFvr() {
//		return this.fvr;
//	}
//
//	public void setFkFvrKey(FieldVisitRequest fvr) {
//		this.fvr = fvr;
//	}
//
//	public Long getKey() {
//		return this.key;
//	}
//
//	public void setKey(Long key) {
//		this.key = key;
//	}
//
////	public Long getMigratedApplicationId() {
////		return this.migratedApplicationId;
////	}
//
////	public void setMigratedApplicationId(Long migratedApplicationId) {
////		this.migratedApplicationId = migratedApplicationId;
////	}
//
////	public String getMigratedCode() {
////		return this.migratedCode;
////	}
//
////	public void setMigratedCode(String migratedCode) {
////		this.migratedCode = migratedCode;
////	}
//
////	public String getModifiedBy() {
////		return this.modifiedBy;
////	}
//
////	public void setModifiedBy(String modifiedBy) {
////		this.modifiedBy = modifiedBy;
////	}
//
////	public Timestamp getModifiedDate() {
////		return this.modifiedDate;
////	}
//
////	public void setModifiedDate(Timestamp modifiedDate) {
////		this.modifiedDate = modifiedDate;
////	}
//
//	public Boolean getNoneOfTheAbove() {
//		return this.noneOfTheAbove;
//	}
//
//	public void setNoneOfTheAbove(Boolean noneOfTheAbove) {
//		this.noneOfTheAbove = noneOfTheAbove;
//	}
//
//	public String getOfficeCode() {
//		return this.officeCode;
//	}
//
//	public void setOfficeCode(String officeCode) {
//		this.officeCode = officeCode;
//	}
//
//	public Status getStatus() {
//		return this.status;
//	}
//
//	public void setStatus(Status status) {
//		this.status = status;
//	}
//
//	public Timestamp getStatusDate() {
//		return this.statusDate;
//	}
//
//	public void setStatusDate(Timestamp statusDate) {
//		this.statusDate = statusDate;
//	}
//
//	public Boolean getTariffVerified() {
//		return this.tariffVerified;
//	}
//
//	public void setTarffVerified(Boolean tariffVerified) {
//		this.tariffVerified = tariffVerified;
//	}
//
//	public Boolean getTerminatedFvPatientDischa() {
//		return this.terminatedFvPatientDischa;
//	}
//
//	public void setTerminatedFvPatientDischa(Boolean terminatedFvPatientDischa) {
//		this.terminatedFvPatientDischa = terminatedFvPatientDischa;
//	}
//
//	public Boolean getTotQuantumReduAch() {
//		return this.totQuantumReduAch;
//	}
//
//	public void setTotQuantumReduAch(Boolean totQuantumReduAch) {
//		this.totQuantumReduAch = totQuantumReduAch;
//	}
//
//	public Boolean getTracedPed() {
//		return this.tracedPed;
//	}
//
//	public void setTracedPed(Boolean tracedPed) {
//		this.tracedPed = tracedPed;
//	}
//
//	public Boolean getTriggerPointAttached() {
//		return this.triggerPointAttached;
//	}
//
//	public void setTriggerPointAttached(Boolean triggerPointAttached) {
//		this.triggerPointAttached = triggerPointAttached;
//	}
//
////	public Long getVersion() {
////		return this.version;
////	}
////
////	public void setVersion(Long version) {
////		this.version = version;
////	}
//
//	public Stage getStage() {
//		return stage;
//	}
//
//	public void setStage(Stage stage) {
//		this.stage = stage;
//	}
//
//	public void setFvr(FieldVisitRequest fvr) {
//		this.fvr = fvr;
//	}
//
//	public void setTariffVerified(Boolean tariffVerified) {
//		this.tariffVerified = tariffVerified;
//	}
//
////	public Long getSubstatusId() {
////		return substatusId;
////	}
////
////	public void setSubstatusId(Long substatusId) {
////		this.substatusId = substatusId;
////	}
//
//}