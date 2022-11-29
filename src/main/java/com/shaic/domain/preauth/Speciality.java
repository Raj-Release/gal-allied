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
import com.shaic.domain.Claim;
import com.shaic.domain.Status;

/**
 * The persistent class for the IMS_CLS_SPECIALITY_T database table.
 * 
 */
@Entity
@Table(name="IMS_CLS_SPECIALITY")
@NamedQueries({
	@NamedQuery(name="Speciality.findAll", query="SELECT i FROM Speciality i"),
	@NamedQuery(name="Speciality.findByPreauthKey", query="SELECT i FROM Speciality i where i.claim.key = :claimKey"),
	@NamedQuery(name="Speciality.findByClaimKey", query="SELECT i FROM Speciality i where i.claim.key = :claimKey"),
	@NamedQuery(name="Speciality.findByKey", query="SELECT i FROM Speciality i where i.key = :primaryKey")
})

public class Speciality  extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SPECIALITY_KEY")
	@SequenceGenerator(name="IMS_CLS_SPECIALITY_KEY_GENERATOR", sequenceName = "SEQ_SPECIALITY_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_SPECIALITY_KEY_GENERATOR")
	private Long key;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

//	@Column(name="ACTIVE_STATUS_DATE")
//	private Timestamp activeStatusDate;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CLAIM_KEY", nullable=false)
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

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STAGE_ID",nullable=false)
	private Stage stage;
	
	@OneToOne
	@JoinColumn(name="PROC_SPECIALITY_KEY",nullable=false)
	private ProcedureSpecialityMaster procedure;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STATUS_ID",nullable=false)
	private Status status;
	
	@Column(name = "TRANSACTION_KEY", nullable = false)
	private Long transactionKey;
	
	@Column(nullable = true, columnDefinition = "VARCHAR(1)", name="TRANSACTION_FLAG", length=1)
	private String transactionFlag;
	
//	@Column(name="SUB_STATUS_ID",nullable=false)
//	private Long substatusId;
	
	@Column(name="REMARKS")
	private String remarks;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="SPECIALITY_TYPE_ID")
	private SpecialityType specialityType;
	//private MastersValue specialityType;

//	@Column(name="STATUS_DATE")
//	private Timestamp statusDate;
//
//	@Column(name="VERSION")
//	private Long version;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="OLD_SPECIALITY_TYPE_ID")
	private SpecialityType oldspecialityType;
	
	@Column(name="CRCN_SPL_TYPE_FLAG")
	private String splFlag;
	
	@OneToOne
	@JoinColumn(name="OLD_PROC_SPECIALITY_KEY",nullable=false)
	private ProcedureSpecialityMaster oldProcedure;
	
	@Column(name="OLD_REMARKS")
	private String oldRemarks;
	
	@Column(name="DM_CODE")
	private Long dmCode;

	public Speciality() {
	}

	public Long getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
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

	public Claim getClaim() {
		return this.claim;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
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

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

/*	public MastersValue getSpecialityType() {
		return this.specialityType;
	}

	public void setSpecialityType(MastersValue specialityType) {
		this.specialityType = specialityType;
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

	public ProcedureSpecialityMaster getProcedure() {
		return procedure;
	}

	public void setProcedure(ProcedureSpecialityMaster procedure) {
		this.procedure = procedure;
	}

	public SpecialityType getSpecialityType() {
		return specialityType;
	}

	public void setSpecialityType(SpecialityType specialityType) {
		this.specialityType = specialityType;
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

	public SpecialityType getOldspecialityType() {
		return oldspecialityType;
	}

	public void setOldspecialityType(SpecialityType oldspecialityType) {
		this.oldspecialityType = oldspecialityType;
	}

	public String getSplFlag() {
		return splFlag;
	}

	public void setSplFlag(String splFlag) {
		this.splFlag = splFlag;
	}

	public ProcedureSpecialityMaster getOldProcedure() {
		return oldProcedure;
	}

	public void setOldProcedure(ProcedureSpecialityMaster oldProcedure) {
		this.oldProcedure = oldProcedure;
	}

	public String getOldRemarks() {
		return oldRemarks;
	}

	public void setOldRemarks(String oldRemarks) {
		this.oldRemarks = oldRemarks;
	}

	public Long getDmCode() {
		return dmCode;
	}

	public void setDmCode(Long dmCode) {
		this.dmCode = dmCode;
	}
	
}