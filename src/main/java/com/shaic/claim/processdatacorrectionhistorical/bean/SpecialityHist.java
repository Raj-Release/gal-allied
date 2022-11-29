package com.shaic.claim.processdatacorrectionhistorical.bean;

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
import com.shaic.domain.preauth.ProcedureSpecialityMaster;
import com.shaic.domain.preauth.SpecialityType;
import com.shaic.domain.preauth.Stage;

@Entity
@Table(name="IMS_CLS_CODHIS_SPECIALITY")
@NamedQueries({
	@NamedQuery(name="SpecialityHist.findAll", query="SELECT i FROM SpecialityHist i"),
	@NamedQuery(name="SpecialityHist.findByPreauthKey", query="SELECT i FROM SpecialityHist i where i.claim.key = :claimKey"),
	@NamedQuery(name="SpecialityHist.findByClaimKey", query="SELECT i FROM SpecialityHist i where i.claim.key = :claimKey"),
	@NamedQuery(name="SpecialityHist.findByKey", query="SELECT i FROM SpecialityHist i where i.key = :primaryKey")
})
public class SpecialityHist extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SPECIALITY_KEY")
	@SequenceGenerator(name="IMS_CLS_SPECIALITY_KEY_GENERATOR", sequenceName = "SEQ_SPECIALITY_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_SPECIALITY_KEY_GENERATOR")
	private Long key;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CLAIM_KEY", nullable=false)
	private Claim claim;

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
	
	@Column(name="REMARKS")
	private String remarks;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="SPECIALITY_TYPE_ID")
	private SpecialityType specialityType;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="OLD_SPECIALITY_TYPE_ID")
	private SpecialityType oldspecialityType;
	
	@Column(name="CRCN_SPL_TYPE_FLAG")
	private String splFlag;
	
	@Column(name="OLD_REMARKS")
	private String oldRemark;
	
	@OneToOne
	@JoinColumn(name="OLD_PROC_SPECIALITY_KEY",nullable=false)
	private ProcedureSpecialityMaster oldprocedure;
	
	@Column(name="DM_CODE")
	private Long dmCode;

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

	public String getOldRemark() {
		return oldRemark;
	}

	public void setOldRemark(String oldRemark) {
		this.oldRemark = oldRemark;
	}

	public ProcedureSpecialityMaster getOldprocedure() {
		return oldprocedure;
	}

	public void setOldprocedure(ProcedureSpecialityMaster oldprocedure) {
		this.oldprocedure = oldprocedure;
	}

	public Long getDmCode() {
		return dmCode;
	}

	public void setDmCode(Long dmCode) {
		this.dmCode = dmCode;
	}

}
