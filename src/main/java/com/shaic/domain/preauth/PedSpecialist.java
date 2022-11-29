package com.shaic.domain.preauth;

import java.io.Serializable;
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
import com.shaic.domain.MastersValue;
import com.shaic.domain.Status;

/**
 * The persistent class for the IMS_CLS_SPECIALIST_T database table.
 * 
 */
@Entity
@Table(name="IMS_CLS_PED_SPECIALIST")
@NamedQueries({
	@NamedQuery(name="PedSpecialist.findAll", query="SELECT i FROM PedSpecialist i"),
	@NamedQuery(name="PedSpecialist.findbyKey",query="SELECT i FROM PedSpecialist i where i.key=:primaryKey"),
	@NamedQuery(name="PedSpecialist.findInitiatePedDetails",query="SELECT i FROM PedSpecialist i where i.initiatePedDetails.key=:initiateKey")

})
public class PedSpecialist extends AbstractEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_CLS_PED_SPECIALIST_KEY_GENERATOR", sequenceName = "SEQ_PED_SPECIALIST_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_PED_SPECIALIST_KEY_GENERATOR") 
	@Column(name="PED_SPECIALIST_KEY")
	private Long key;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

//	@Column(name="ACTIVE_STATUS_DATE")
//	private Timestamp activeStatusDate;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

//	@Lob
//	@Column(name="FILE_UPLOAD")
//	private byte[] fileUpload;

	@OneToOne
	@JoinColumn(name="PED_INITIATE_KEY")
	private OldInitiatePedEndorsement initiatePedDetails;

	@Column(name="INITIATOR_TYPE")
	private String initiatorType;

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

//	@Column(name="QUERY_REMARKS")
//	private String queryRemarks;

	@Column(name="REFERRING_REASON")
	private String referringReason;

//	@Column(name="SEQUENCE_NUMBER")
//	private Long sequenceNumber;

	@Column(name="SPECIALIST_REMARKS")
	private String specialistRemarks;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;

	@OneToOne
	@JoinColumn(name="SPECIALIST_TYPE_ID", nullable=false)
	private MastersValue specialistType;

	@OneToOne
	@JoinColumn(name="STATUS_ID",nullable=false)
	private Status status;
	
	@OneToOne
	@JoinColumn(name="STAGE_ID")
	private Stage stage;
	
	@Column(name="FILE_NAME")
	private String fileName;
	
	@Column(name="DOCUMENT_TOKEN")
	private String documentToken;
	
//	@Column(name="SUB_STATUS_ID")
//	private Long substatusId;
//	
//	@Column(name="STATUS_DATE")
//	private Timestamp statusDate;
//
//	@Column(name="VERSION")
//	private Long version;

	public PedSpecialist() {
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

//	public byte[] getFileUpload() {
//		return this.fileUpload;
//	}
//
//	public void setFileUpload(byte[] fileUpload) {
//		this.fileUpload = fileUpload;
//	}

	public String getInitiateType() {
		return this.initiatorType;
	}

	public void setInitiateType(String initiateType) {
		this.initiatorType = initiateType;
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

	public String getOfficeCode() {
		return this.officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

//	public String getQueryRemarks() {
//		return this.queryRemarks;
//	}
//
//	public void setQueryRemarks(String queryRemarks) {
//		this.queryRemarks = queryRemarks;
//	}

	public String getReferringReason() {
		return this.referringReason;
	}

	public void setReferringReason(String referringReason) {
		this.referringReason = referringReason;
	}

	public String getSpecialistRemarks() {
		return this.specialistRemarks;
	}

	public void setSpecialistRemarks(String specialistRemarks) {
		this.specialistRemarks = specialistRemarks;
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

	
	public String getInitiatorType() {
		return initiatorType;
	}

	public void setInitiatorType(String initiatorType) {
		this.initiatorType = initiatorType;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	
	public OldInitiatePedEndorsement getInitiatePedDetails() {
		return initiatePedDetails;
	}

	public void setInitiatePedDetails(OldInitiatePedEndorsement initiatePedDetails) {
		this.initiatePedDetails = initiatePedDetails;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDocumentToken() {
		return documentToken;
	}

	public void setDocumentToken(String documentToken) {
		this.documentToken = documentToken;
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