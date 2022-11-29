package com.shaic.domain.preauth;

import java.util.Date;

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
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.Status;

/**
 * The persistent class for the IMS_CLS_PED_VALIDATION_T database table.
 * 
 */
@Entity
@Table(name = "IMS_CLS_PED_VALIDATION")
@NamedQueries({
		@NamedQuery(name = "PedValidationDummy.findAll", query = "SELECT i FROM PedValidation i"),
		@NamedQuery(name = "PedValidationDummy.findByPreauthId", query = "SELECT o FROM PedValidationDummy o where o.preauthKey=:preAuthKey") })
public class PedValidationDummy extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "IMS_CLS_PED_VALIDATION_KEY_GENERATORR", sequenceName = "SEQ_PED_VALIDATION_KEY", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IMS_CLS_PED_VALIDATION_KEY_GENERATORR")
	@Column(name = "KEY")
	private Long key;

	// @OneToOne
	// @JoinColumn(name = "FK_PRE_AUTH_KEY", nullable = false)
	@Column(name = "FK_PRE_AUTH_KEY")
	private Long preauthKey;

	@OneToOne
	@JoinColumn(name = "FK_POLICY_KEY", nullable = false)
	private Policy policy;

	@OneToOne
	@JoinColumn(name = "FK_INTIMATION_KEY", nullable = false)
	private Intimation intimation;

	@Column(name = "ACTIVE_STATUS")
	private Integer activeStatus;

	@OneToOne
	@JoinColumn(name = "STAGE_ID", nullable = false)
	private Stage stage;

	@OneToOne
	@JoinColumn(name = "STATUS_ID", nullable = false)
	private Status status;

	@Column(name = "OFFICE_CODE")
	private String officeCode;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "STATUS_DATE")
	private Date statusDate;

	@Column(name = "VERSION")
	private Long version;

	@Column(name = "POLICY_AGING")
	private Long policyAging;

	@Column(name = "DIAGNOSIS_ID")
	private Long diagnosisId;

	@OneToOne
	@JoinColumn(name = "EXCLUSION_DETAILS_ID", nullable = false)
	private MastersValue exclusionDetails;

	@OneToOne
	@JoinColumn(name = "DIAGONSIS_IMPACT_ID", nullable = false)
	private MastersValue diagonsisImpact;

	@Column(name = "ICD_CHAPTER_ID")
	private Long icdChpterId;

	@Column(name = "ICD_BLOCK_ID")
	private Long icdBlockId;

	@Column(name = "ICD_CODE_ID")
	private Long icdCodeId;

	@OneToOne
	@JoinColumn(name = "SUB_LIMIT_NAME_ID", nullable = false)
	private ClaimSubLimit sublimit;

	@Column(name = "SI_RESTRICTION_ID")
	private Long sumInsuredRestrictionId;

	@Column(name = "APPROVED_AMOUNT")
	private Double approveAmount;

	@Column(name = "DOWNSIZED_AMOUNT")
	private Double downsizedAmount;

	@Column(name = "DIFF_AMOUNT")
	private Double diffAmount;
//
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "SUB_LIMIT_APPLICABLE", length = 1)	
	private String subLimitApplicable;
//
//	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "CONSIDER_FOR_PAYMENT", length = 1)
//	@Type(type = "org.hibernate.type.NumericBooleanType")
//	private Boolean considerForPayment;
//
//	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "PROCESS_FLAG", length = 1)
//	@Type(type = "org.hibernate.type.NumericBooleanType")
//	private Boolean processFlag;
//
//	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "ACTION", length = 1)
//	@Type(type = "org.hibernate.type.NumericBooleanType")
//	private Boolean action;
//
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "DIAGNOSIS_REMARKS")
	private String diagnosisRemarks;

	@Column(name = "PED_NAME")
	private String pedName;

	@Column(name = "APPROVED_REMARKS")
	private String approvedRemarks;

	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	@Column(name = "ACTIVE_STATUS_DATE")
	private Date activeStatusDate;

	@Column(name = "SUB_STATUS_ID")
	private Long subStatusId;

	public Long getKey() {
		return this.key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getPreauthKey() {
		return preauthKey;
	}

	public void setPreauthKey(Long preauthKey) {
		this.preauthKey = preauthKey;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	public Intimation getIntimation() {
		return intimation;
	}

	public void setIntimation(Intimation intimation) {
		this.intimation = intimation;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
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

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
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

	public Date getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Long getPolicyAging() {
		return policyAging;
	}

	public void setPolicyAging(Long policyAging) {
		this.policyAging = policyAging;
	}

	public Long getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(Long diagnosisId) {
		this.diagnosisId = diagnosisId;
	}

	public MastersValue getExclusionDetails() {
		return exclusionDetails;
	}

	public void setExclusionDetails(MastersValue exclusionDetails) {
		this.exclusionDetails = exclusionDetails;
	}

	public MastersValue getDiagonsisImpact() {
		return diagonsisImpact;
	}

	public void setDiagonsisImpact(MastersValue diagonsisImpact) {
		this.diagonsisImpact = diagonsisImpact;
	}

	public Long getIcdChpterId() {
		return icdChpterId;
	}

	public void setIcdChpterId(Long icdChpterId) {
		this.icdChpterId = icdChpterId;
	}

	public Long getIcdBlockId() {
		return icdBlockId;
	}

	public void setIcdBlockId(Long icdBlockId) {
		this.icdBlockId = icdBlockId;
	}

	public Long getIcdCodeId() {
		return icdCodeId;
	}

	public void setIcdCodeId(Long icdCodeId) {
		this.icdCodeId = icdCodeId;
	}

	public ClaimSubLimit getSublimit() {
		return sublimit;
	}

	public void setSublimit(ClaimSubLimit sublimit) {
		this.sublimit = sublimit;
	}

	public Long getSumInsuredRestrictionId() {
		return sumInsuredRestrictionId;
	}

	public void setSumInsuredRestrictionId(Long sumInsuredRestrictionId) {
		this.sumInsuredRestrictionId = sumInsuredRestrictionId;
	}

	public Double getApproveAmount() {
		return approveAmount;
	}

	public void setApproveAmount(Double approveAmount) {
		this.approveAmount = approveAmount;
	}

	public Double getDownsizedAmount() {
		return downsizedAmount;
	}

	public void setDownsizedAmount(Double downsizedAmount) {
		this.downsizedAmount = downsizedAmount;
	}

	public Double getDiffAmount() {
		return diffAmount;
	}

	public void setDiffAmount(Double diffAmount) {
		this.diffAmount = diffAmount;
	}
//
//	public Boolean getSubLimitApplicable() {
//		return subLimitApplicable;
//	}
//
//	public void setSubLimitApplicable(Boolean subLimitApplicable) {
//		this.subLimitApplicable = subLimitApplicable;
//	}
//
//	public Boolean getConsiderForPayment() {
//		return considerForPayment;
//	}
//
//	public void setConsiderForPayment(Boolean considerForPayment) {
//		this.considerForPayment = considerForPayment;
//	}
//
//	public Boolean getProcessFlag() {
//		return processFlag;
//	}
//
//	public void setProcessFlag(Boolean processFlag) {
//		this.processFlag = processFlag;
//	}
//
//	public Boolean getAction() {
//		return action;
//	}
//
//	public void setAction(Boolean action) {
//		this.action = action;
//	}
//
	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getDiagnosisRemarks() {
		return diagnosisRemarks;
	}

	public void setDiagnosisRemarks(String diagnosisRemarks) {
		this.diagnosisRemarks = diagnosisRemarks;
	}

	public String getPedName() {
		return pedName;
	}

	public void setPedName(String pedName) {
		this.pedName = pedName;
	}

	public String getApprovedRemarks() {
		return approvedRemarks;
	}

	public void setApprovedRemarks(String approvedRemarks) {
		this.approvedRemarks = approvedRemarks;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Date getActiveStatusDate() {
		return activeStatusDate;
	}

	public void setActiveStatusDate(Date activeStatusDate) {
		this.activeStatusDate = activeStatusDate;
	}

	public Long getSubStatusId() {
		return subStatusId;
	}

	public void setSubStatusId(Long subStatusId) {
		this.subStatusId = subStatusId;
	}
}