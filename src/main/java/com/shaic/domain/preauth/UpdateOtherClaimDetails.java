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
import com.shaic.domain.Status;

@Entity
@Table(name = "IMS_CLS_OTHERS_CLAIMS_DTLS")
@NamedQueries({
		@NamedQuery(name = "UpdateOtherClaimDetails.findAll", query = "SELECT i FROM UpdateOtherClaimDetails i"),
		@NamedQuery(name = "UpdateOtherClaimDetails.findByCashlessKey", query = "SELECT i FROM UpdateOtherClaimDetails i where i.cashlessKey = :cashlessKey"),
		@NamedQuery(name = "UpdateOtherClaimDetails.findByClaimKey", query = "SELECT i FROM UpdateOtherClaimDetails i where i.claimKey = :claimKey and i.reimbursementKey in (:reimbursementKey)"),
		@NamedQuery(name = "UpdateOtherClaimDetails.findByReimbursementKey", query = "SELECT i FROM UpdateOtherClaimDetails i where i.reimbursementKey = :reimbursementKey")
})

public class UpdateOtherClaimDetails extends AbstractEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_CLS_OTHERS_CLAIMS_DTLS_KEY_GENERATOR", sequenceName = "SEQ_CLAIMS_OTH_DTLS_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_OTHERS_CLAIMS_DTLS_KEY_GENERATOR" ) 
	@Column(name="CLAIMS_OTH_DTLS_KEY", updatable=false)
	private Long key;
	
	@Column(name = "INTIMATION_KEY")
	private Long intimationKey;
	
	@Column(name="INTIMATION_NUMBER", nullable=true)
	private String intimationId;
	
	@Column(name = "CLAIM_KEY")
	private Long claimKey;
	
	@Column(name = "CASHLESS_KEY")
	private Long cashlessKey;
	
	@Column(name = "ROD_KEY")
	private Long reimbursementKey;
	
	@Column(name="CLAIM_TYPE")
	private String claimType;
	
	@Column(name="INSURER_NAME")
	private String insurerName;
	
	@Column(name="DIAGN_PROC_PRIMARY")
	private String primaryDiagnosiProcedure;
	
	@Column(name="ICD_CHARACTER")
	private String icdChapter;
	
	@Column(name="ICD_BLOCK")
	private String icdBlock;
	
	@Column(name="ICD_CODE")
	private String icdCode;
	
	@Column(name = "CLAIMED_AMOUNT")
	private Long claimedAmount;
	
	@Column(name = "DEDUCTIBILITY")
	private Long deductibility;
	
	@Column(name = "PAID_ADMISSIBLE_AMT")
	private Long admissibleAmount;
	
	@Column(name="REMARKS")
	private String remarks;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_ID", nullable = false)
	private Status status;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STAGE_ID", nullable = false)
	private Stage stage;
	
	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;
	
	@Column(name="EDIT_FLAG")
	private String editFlag;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public String getIntimationId() {
		return intimationId;
	}

	public void setIntimationId(String intimationId) {
		this.intimationId = intimationId;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public Long getCashlessKey() {
		return cashlessKey;
	}

	public void setCashlessKey(Long cashlessKey) {
		this.cashlessKey = cashlessKey;
	}

	public Long getReimbursementKey() {
		return reimbursementKey;
	}

	public void setReimbursementKey(Long reimbursementKey) {
		this.reimbursementKey = reimbursementKey;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getInsurerName() {
		return insurerName;
	}

	public void setInsurerName(String insurerName) {
		this.insurerName = insurerName;
	}

	public String getPrimaryDiagnosiProcedure() {
		return primaryDiagnosiProcedure;
	}

	public void setPrimaryDiagnosiProcedure(String primaryDiagnosiProcedure) {
		this.primaryDiagnosiProcedure = primaryDiagnosiProcedure;
	}

	public String getIcdBlock() {
		return icdBlock;
	}

	public void setIcdBlock(String icdBlock) {
		this.icdBlock = icdBlock;
	}

	public String getIcdCode() {
		return icdCode;
	}

	public void setIcdCode(String icdCode) {
		this.icdCode = icdCode;
	}

	public Long getClaimedAmount() {
		return claimedAmount;
	}

	public void setClaimedAmount(Long claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

	public Long getDeductibility() {
		return deductibility;
	}

	public void setDeductibility(Long deductibility) {
		this.deductibility = deductibility;
	}

	public Long getAdmissibleAmount() {
		return admissibleAmount;
	}

	public void setAdmissibleAmount(Long admissibleAmount) {
		this.admissibleAmount = admissibleAmount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getEditFlag() {
		return editFlag;
	}

	public void setEditFlag(String editFlag) {
		this.editFlag = editFlag;
	}

	public String getIcdChapter() {
		return icdChapter;
	}

	public void setIcdChapter(String icdChapter) {
		this.icdChapter = icdChapter;
	}

}
