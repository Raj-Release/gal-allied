package com.shaic.domain.preauth;

import java.sql.Timestamp;

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

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.Claim;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Status;

/**
 * The persistent class for the IMS_CLS_PRE_AUTH_ESCALATE_T database table.
 * 
 */
@Entity
@Table(name="IMS_CLS_ESCALATION")
@NamedQueries({
	@NamedQuery(name="PreauthEscalate.findAll", query="SELECT i FROM PreauthEscalate i"),
	@NamedQuery(name="PreauthEscalate.findByEsclateId", query="SELECT o FROM PreauthEscalate o where o.escalateTo.key=:escalateKey"),
	@NamedQuery(name="PreauthEscalate.findByKey",query="SELECT o FROM PreauthEscalate o where o.key=:parentKey"),
	@NamedQuery(name="PreauthEscalate.findByClaimKey",query="SELECT o FROM PreauthEscalate o where o.claim.key=:claimKey order by o.key desc")
})
public class PreauthEscalate extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_CLS_ESCALATION_KEY_GENERATOR", sequenceName = "SEQ_ESCALATION_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_ESCALATION_KEY_GENERATOR" ) 
	@Column(name="ESCALATION_KEY")
	private Long key;
	
	@OneToOne
	@JoinColumn(name="CLAIM_KEY", nullable=false)
	private Claim claim;
	
	@OneToOne
	@JoinColumn(name="ESCALATE_TO_ID", nullable=false)
	private MastersValue escalateTo;
	
	@Column(name="ESCALATE_REMARKS")
	private String escalateRemarks;
	
//	@Lob
//	@Column(name="FILE_UPLOAD")
//	private byte[] fileUpload;
	
	@OneToOne
	@JoinColumn(name="SPECIALIST_TYPE_ID", nullable=false)
	private MastersValue specialistType;
	
	@Column(name="SPECIALIST_REMARKS")
	private String specialistRemarks;	
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;
	
	@Column(name="OFFICE_CODE")
	private String officeCode;
	
	@OneToOne
	@JoinColumn(name="STATUS_ID",nullable=false)
	private Status status;
	
	@OneToOne
	@JoinColumn(name="STAGE_ID",nullable=false)
	private Stage stage;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="ESCALATE_REPLY_REMARKS")
	private String esclateReplyRemarks;	
	
	@Column(name = "TRANSACTION_KEY", nullable = true)
	private Long transactionKey;
	
	@Column(nullable = true, columnDefinition = "VARCHAR(1)", name="TRANSACTION_FLAG", length=1)
	private String transactionFlag;

	public PreauthEscalate() {
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

	public String getEscalateRemarks() {
		return this.escalateRemarks;
	}

	public void setEscalateRemarks(String escalateRemarks) {
		this.escalateRemarks = escalateRemarks;
	}

	public MastersValue getEscalateTo() {
		return this.escalateTo;
	}

	public void setEscalateTo(MastersValue escalateTo) {
		this.escalateTo = escalateTo;
	}

//	public byte[] getFileUpload() {
//		return this.fileUpload;
//	}
//
//	public void setFileUpload(byte[] fileUpload) {
//		this.fileUpload = fileUpload;
//	}
	
	public String getSpecialistRemarks() {
		return specialistRemarks;
	}

	public void setSpecialistRemarks(String specialistRemarks) {
		this.specialistRemarks = specialistRemarks;
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

	public MastersValue getSpecialistType() {
		return this.specialistType;
	}

	public void setSpecialistType(MastersValue specialistType) {
		this.specialistType = specialistType;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Claim getClaim() {
		return claim;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}	
	
	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public String getEsclateReplyRemarks() {
		return esclateReplyRemarks;
	}

	public void setEsclateReplyRemarks(String esclateReplyRemarks) {
		this.esclateReplyRemarks = esclateReplyRemarks;
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

}