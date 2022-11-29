
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
import com.shaic.domain.MastersValue;
import com.shaic.domain.Status;

/**
 * The persistent class for the IMS_CLS_PED_INITIATE_DETAILS_T database table.
 * 
 */
@Entity
@Table(name="IMS_CLS_PED_INITIATE_DTLS_HIS")
@NamedQueries({
	@NamedQuery(name="PedEndorsementDetailsHistory.findAll", query="SELECT i FROM PedEndorsementDetailsHistory i"),
	@NamedQuery(name="PedEndorsementDetailsHistory.findByInitateKey",query="SELECT o FROM PedEndorsementDetailsHistory o where o.oldInitiatePedEndorsement.key=:initiateKey and (o.deletedFlag is null or o.deletedFlag = 'N')")

})
public class PedEndorsementDetailsHistory extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name="IMS_CLS_PED_INITIATE_DTLS_HIS_KEY_GENERATOR", sequenceName = "SEQ_PED_DTLS_HIS_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_PED_INITIATE_DTLS_HIS_KEY_GENERATOR" ) 
	@Column(name="PED_DTLS_HIS_KEY")
	private Long key;
	
	@Column(name="PED_INITIATE_DETAILS_KEY")
	private Long pedInitiateDetailsKey;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

//	@Column(name="ACTIVE_STATUS_DATE")
//	private Timestamp activeStatusDate;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	private String description;

	@Column(name="DOCTOR_REMARKS")
	private String doctorRemarks;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="PED_INITIATE_KEY", nullable=false)
	private PedEndorementHistory oldInitiatePedEndorsement;

	@Column(name="ICD_BLOCK_ID")
	private Long icdBlockId;
	
	@Column(name="ICD_CHAPTER_ID")
	private Long icdChapterId;

	@Column(name="ICD_CODE_ID",nullable=false)
	private Long icdCodeId;

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

	@Column(name="OTHERS_SPECIFY")
	private String othesSpecify;

	@Column(name="PED_CODE_ID")
	private Long pedCode;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="SOURCE_ID", nullable=false)
	private MastersValue source;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STATUS_ID",nullable=false)
	private Status status;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STAGE_ID", nullable=false)
	private Stage stage;
	
//	@Column(name="STATUS_DATE")
//	private Timestamp statusDate;
//
//	@Column(name="SUB_STATUS_ID")
//	private Long subStatusId;
//	
//	@Column(name="VERSION")
//	private Long version;
	
//	@Column(name = "VERSION", nullable = true)
//	@Transient
//	private Long version;
	
	@Column(nullable = true, columnDefinition = "VARCHAR(1)", name = "DELETED_FLAG", length = 1)
	private String deletedFlag;

	public PedEndorsementDetailsHistory() {
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

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDoctorRemarks() {
		return this.doctorRemarks;
	}

	public void setDoctorRemarks(String doctorRemarks) {
		this.doctorRemarks = doctorRemarks;
	}
	public PedEndorementHistory getOldInitiatePedEndorsement() {
		return oldInitiatePedEndorsement;
	}

	public void setOldInitiatePedEndorsement(
			PedEndorementHistory oldInitiatePedEndorsement) {
		this.oldInitiatePedEndorsement = oldInitiatePedEndorsement;
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
//
//	public void setMigratedApplicationId(Long migratedApplicationId) {
//		this.migratedApplicationId = migratedApplicationId;
//	}
//
//	public String getMigratedCode() {
//		return this.migratedCode;
//	}
//
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

	public String getOthesSpecify() {
		return this.othesSpecify;
	}

	public void setOthesSpecify(String othesSpecify) {
		this.othesSpecify = othesSpecify;
	}

	public Long getPedCode() {
		return pedCode;
	}

	public void setPedCode(Long pedCode) {
		this.pedCode = pedCode;
	}

	public MastersValue getSource() {
		return this.source;
	}

	public void setSource(MastersValue source) {
		this.source = source;
	}
	
	public Long getIcdBlockId() {
		return icdBlockId;
	}

	public void setIcdBlockId(Long icdBlockId) {
		this.icdBlockId = icdBlockId;
	}

	public Long getIcdChapterId() {
		return icdChapterId;
	}

	public void setIcdChapterId(Long icdChapterId) {
		this.icdChapterId = icdChapterId;
	}

	public Long getIcdCodeId() {
		return icdCodeId;
	}

	public void setIcdCodeId(Long icdCodeId) {
		this.icdCodeId = icdCodeId;
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

	/*public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}*/

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public Long getPedInitiateDetailsKey() {
		return pedInitiateDetailsKey;
	}

	public void setPedInitiateDetailsKey(Long pedInitiateDetailsKey) {
		this.pedInitiateDetailsKey = pedInitiateDetailsKey;
	}


}