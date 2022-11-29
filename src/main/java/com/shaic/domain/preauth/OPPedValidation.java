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
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OPIntimation;
import com.shaic.domain.Policy;
import com.shaic.domain.Status;

/**
 * The persistent class for the IMS_CLS_PED_VALIDATION_T database table.
 * 
 */
@Entity
//@Table(name = "IMS_CLS_PED_VALIDATION")
@Table(name = "IMS_CLS_OPH_DIAGNOSIS")
@NamedQueries({
	@NamedQuery(name = "OPPedValidation.findAll", query = "SELECT i FROM OPPedValidation i"),
	@NamedQuery(name="OPPedValidation.findByPreauthKey", query="SELECT i FROM OPPedValidation i where i.transactionKey = :preauthKey and (i.deleteFlag is null or i.deleteFlag = 1)"),
	@NamedQuery(name="OPPedValidation.findByTransactionKey", query="SELECT i FROM OPPedValidation i where i.transactionKey = :transactionKey and (i.deleteFlag is null or i.deleteFlag = 1)"),
	@NamedQuery(name="OPPedValidation.findByIntimationKey",query="SELECT i FROM OPPedValidation i where i.intimation.key = :intimationKey and (i.deleteFlag is null or i.deleteFlag = 1)"),
	@NamedQuery(name="OPPedValidation.findByKey",query="SELECT i FROM OPPedValidation i where i.key = :primaryKey and (i.deleteFlag is null or i.deleteFlag = 1)"),
	@NamedQuery(name="OPPedValidation.findByPolicyKey",query="SELECT i FROM OPPedValidation i where i.policy.key = :policyKey and (i.deleteFlag is null or i.deleteFlag = 1)"),
})

public class OPPedValidation extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "IMS_CLS_OP_PED_VALIDATION_KEY_GENERATOR", sequenceName = "SEQ_OPH_DIAG_KEY", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IMS_CLS_OP_PED_VALIDATION_KEY_GENERATOR")
	@Column(name = "DIAGNOSIS_KEY")
	private Long key;

	@Column(name = "ACTIVE_STATUS")
	private Long activeStatus;

//	@Column(name = "ACTIVE_STATUS_DATE")
//	private Timestamp activeStatusDate;

	@Column(name = "APPROVED_AMOUNT")
	private Double approveAmount;
	
	@Column(name = "NET_APPROVED_AMOUNT")
	private Double netApprovedAmount;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name ="DELETED_FLAG")
	private Long deleteFlag;
	
	/**
	 * Commenting this below code, since there should
	 * not be a join for diagnosis id column.
	 * This is because for a single preAuth we 
	 * might have multiple diagnosis. Hence if we join,
	 * this then there might be a performance hit. Hence
	 * this should be long .
	 * */
/*

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "DIAGNOSIS_ID", nullable = false)
	private Diagnosis diagnosis;
*/
	@Column(name = "DIAGNOSIS_ID")
	private Long diagnosisId;
	
//	@Column(name = "DIAGNOSIS_REMARKS")
//	private String diagnosisRemarks;

//	@OneToOne
//	@JoinColumn(name = "DIAGONSIS_IMPACT_ID", nullable = false)
//	private MastersValue diagonsisImpact;

	@Column(name = "ICD_CHAPTER_ID")
	private Long icdChpterId;

	@Column(name = "ICD_BLOCK_ID")
	private Long icdBlockId;

	@Column(name = "ICD_CODE_ID")
	private Long icdCodeId;

//	@Column(name = "DOWNSIZED_AMOUNT")
//	private Long downsizedAmount;

//	@OneToOne
//	@JoinColumn(name = "EXCLUSION_DETAILS_ID", nullable = false)
//	private MastersValue exclusionDetails;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "INTIMATION_KEY", nullable = false)
	private OPIntimation intimation;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "POLICY_KEY", nullable = false)
	private Policy policy;

	@Column(name = "TRANSACTION_KEY", nullable = false)
	private Long transactionKey;

	@Column(name = "LIMIT_ID")
	private Long sublimitId;

	@Column(name = "SI_RESTRICTION_ID")
	private Long sumInsuredRestrictionId;

	@Column(name = "DIFF_AMOUNT")
	private Double diffAmount;

	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "SUB_LIMIT_APPLICABLE", length = 1)
	private String subLimitApplicable;

	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "PROCESS_FLAG", length = 1)
	private String processFlag;

	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "ACTION", length = 1)
	private String action;

	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "CONSIDER_FOR_PAYMENT", length = 1)
	private String considerForPayment;	
	
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "REC_TYPE_FLAG", length = 1)
	private String recTypeFlag;
	


	// @Column(name="MIGRATED_APPLICATION_ID")
	// private Long migratedApplicationId;

	// @Column(name="MIGRATED_CODE")
	// private String migratedCode;

	// @Column(name="MODIFIED_BY")
	// private String modifiedBy;

	// @Column(name="MODIFIED_DATE")
	// private Timestamp modifiedDate;

	@Column(name = "OFFICE_CODE")
	private String officeCode;

//	@Column(name = "PED_NAME")
//	private String pedName;

	@Column(name = "POLICY_AGING")
	private String policyAging;

	@Column(name = "APPROVED_REMARKS")
	private String approvedRemarks;
	
	@Column(name = "DIAGNOSIS_REMARKS")
	private String diagnosisRemarks;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_ID", nullable = false)
	private Status status;

//	@Column(name = "SUB_STATUS_ID")
//	private Long subStatusId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STAGE_ID", nullable = false)
	private Stage stage;

//	@Column(name = "STATUS_DATE")
//	private Timestamp statusDate;
	
//	@OneToOne
//	@JoinColumn(name = "DIAGONSIS_IMPACT_ID", nullable = false)
//	private MastersValue diagnosisImpact;
	
	
	// New columns....
	@Column(name = "AMOUNT_CONSIDERED")
	private Double amountConsideredAmount;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	public String getRecTypeFlag() {
		return recTypeFlag;
	}

	public void setRecTypeFlag(String recTypeFlag) {
		this.recTypeFlag = recTypeFlag;
	}

	@Column(name = "MINIMUM_AMOUNT")
	private Double minimumAmount;
	
	@Column(name = "COPAY_PERCENTAGE")
	private Double copayPercentage;
	
	@Column(name = "COPAY_AMOUNT")
	private Double copayAmount;
	
	@Column(name = "NET_AMOUNT")
	private Double netAmount;
	
	@Column(name ="SITTINGS_COUNT")
	private Long sittingsInput;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "AMB_CHR_FLAG", length = 1)
	private String ambulanceChargeFlag;
	
	@Column(name = "AMB_CHARGES")
	private Double ambulanceCharges;
	
	@Column(name = "AMB_CHRS_WT_AMT")
	private Double ambulanceChargeWithAmount;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COPAY_TYPE_ID", nullable = true)
	private MastersValue coPayTypeId;
	/*@Column(name = "SUB_LIMIT_AMOUNT")
	private Long subLimitAmount;*/
	// @Column(name="SUB_LIMIT_AMOUNT")
	// private Long subLimitAmount;
//
//	@Column(name = "VERSION")
//	private Long version;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PED_IMP_DIAGNOSIS_ID", nullable = true)
	private MastersValue pedImpactId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PED_PAY_NOT_CONS_ID", nullable = true)
	private MastersValue notPayingReason;
	
	@Column(name ="SUBLIMIT_UPD_REMARKS")
	private String sublimitUpdateRemarks;

	public OPPedValidation() {
	}

	public Long getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}
	
	public Double getApproveAmount() {
		return this.approveAmount;
	}

	public void setApproveAmount(Double approveAmount) {
		this.approveAmount = approveAmount;
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


	public OPIntimation getIntimation() {
		return this.intimation;
	}

	public void setIntimation(OPIntimation intimation) {
		this.intimation = intimation;
	}

	public Policy getPolicy() {
		return this.policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}


	public Long getKey() {
		return this.key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	// public Long getMigratedApplicationId() {
	// return this.migratedApplicationId;
	// }

	// public void setMigratedApplicationId(Long migratedApplicationId) {
	// this.migratedApplicationId = migratedApplicationId;
	// }

	// public String getMigratedCode() {
	// return this.migratedCode;
	// }

	// public void setMigratedCode(String migratedCode) {
	// this.migratedCode = migratedCode;
	// }

	// public String getModifiedBy() {
	// return this.modifiedBy;
	// }

	// public void setModifiedBy(String modifiedBy) {
	// this.modifiedBy = modifiedBy;
	// }

	// public Timestamp getModifiedDate() {
	// return this.modifiedDate;
	// }

	// public void setModifiedDate(Timestamp modifiedDate) {
	// this.modifiedDate = modifiedDate;
	// }

	public String getOfficeCode() {
		return this.officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	
	public String getPolicyAging() {
		return this.policyAging;
	}

	public void setPolicyAging(String policyAging) {
		this.policyAging = policyAging;
	}

	/*public Long getSubLimitAmount() {
		return this.subLimitAmount;
	}

	public void setSubLimitAmount(Long subLimitAmount) {
		this.subLimitAmount = subLimitAmount;
	}*/
	
	// public Long getSubLimitAmount() {
	// return this.subLimitAmount;
	// }
	//
	// public void setSubLimitAmount(Long subLimitAmount) {
	// this.subLimitAmount = subLimitAmount;
	// }
	
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

	public Long getSumInsuredRestrictionId() {
		return sumInsuredRestrictionId;
	}

	public void setSumInsuredRestrictionId(Long sumInsuredRestrictionId) {
		this.sumInsuredRestrictionId = sumInsuredRestrictionId;
	}

	public Double getDiffAmount() {
		return diffAmount;
	}

	public void setDiffAmount(Double diffAmount) {
		this.diffAmount = diffAmount;
	}

	public String getApprovedRemarks() {
		return approvedRemarks;
	}

	public void setApprovedRemarks(String approvedRemarks) {
		this.approvedRemarks = approvedRemarks;
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
	
	public Long getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(Long diagnosisId) {
		this.diagnosisId = diagnosisId;
	}

	public String getSubLimitApplicable() {
		return subLimitApplicable;
	}

	public void setSubLimitApplicable(String subLimitApplicable) {
		this.subLimitApplicable = subLimitApplicable;
	}

	public String getProcessFlag() {
		return processFlag;
	}

	public void setProcessFlag(String processFlag) {
		this.processFlag = processFlag;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getConsiderForPayment() {
		return considerForPayment;
	}

	public void setConsiderForPayment(String considerForPayment) {
		this.considerForPayment = considerForPayment;
	}

	public Long getSublimitId() {
		return sublimitId;
	}

	public void setSublimitId(Long sublimitId) {
		this.sublimitId = sublimitId;
	}
	
	public Double getAmountConsideredAmount() {
		return amountConsideredAmount;
	}

	public void setAmountConsideredAmount(Double amountConsideredAmount) {
		this.amountConsideredAmount = amountConsideredAmount;
	}

	public Double getMinimumAmount() {
		return minimumAmount;
	}

	public void setMinimumAmount(Double minimumAmount) {
		this.minimumAmount = minimumAmount;
	}

	public Double getCopayPercentage() {
		return copayPercentage;
	}

	public void setCopayPercentage(Double copayPercentage) {
		this.copayPercentage = copayPercentage;
	}

	public Double getCopayAmount() {
		return copayAmount;
	}

	public void setCopayAmount(Double copayAmount) {
		this.copayAmount = copayAmount;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Long getTransactionKey() {
		return transactionKey;
	}

	public void setTransactionKey(Long transactionKey) {
		this.transactionKey = transactionKey;
	}

	public Long getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Long deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public Long getSittingsInput() {
		return sittingsInput;
	}

	public void setSittingsInput(Long sittingsInput) {
		this.sittingsInput = sittingsInput;
	}

	public Double getNetApprovedAmount() {
		return netApprovedAmount;
	}

	public void setNetApprovedAmount(Double netApprovedAmount) {
		this.netApprovedAmount = netApprovedAmount;
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

	public String getAmbulanceChargeFlag() {
		return ambulanceChargeFlag;
	}

	public void setAmbulanceChargeFlag(String ambulanceChargeFlag) {
		this.ambulanceChargeFlag = ambulanceChargeFlag;
	}

	public Double getAmbulanceCharges() {
		return ambulanceCharges;
	}

	public void setAmbulanceCharges(Double ambulanceCharges) {
		this.ambulanceCharges = ambulanceCharges;
	}

	public Double getAmbulanceChargeWithAmount() {
		return ambulanceChargeWithAmount;
	}

	public void setAmbulanceChargeWithAmount(Double ambulanceChargeWithAmount) {
		this.ambulanceChargeWithAmount = ambulanceChargeWithAmount;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (this.key == null || obj == null || !(this.getClass().equals(obj.getClass()))) {
			return false;
		}

		AbstractEntity that = (AbstractEntity) obj;

		return this.key.equals(that.getKey());
	}

	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return key == null ? 0 : key.hashCode();
	}

	public MastersValue getCoPayTypeId() {
		return coPayTypeId;
	}

	public void setCoPayTypeId(MastersValue coPayTypeId) {
		this.coPayTypeId = coPayTypeId;
	}

	public MastersValue getPedImpactId() {
		return pedImpactId;
	}

	public void setPedImpactId(MastersValue pedImpactId) {
		this.pedImpactId = pedImpactId;
	}

	public MastersValue getNotPayingReason() {
		return notPayingReason;
	}

	public void setNotPayingReason(MastersValue notPayingReason) {
		this.notPayingReason = notPayingReason;
	}
	
	public String getSublimitUpdateRemarks() {
		return sublimitUpdateRemarks;
	}

	public void setSublimitUpdateRemarks(String sublimitUpdateRemarks) {
		this.sublimitUpdateRemarks = sublimitUpdateRemarks;
	}
	public String getDiagnosisRemarks() {
		return diagnosisRemarks;
	}

	public void setDiagnosisRemarks(String diagnosisRemarks) {
		this.diagnosisRemarks = diagnosisRemarks;
	}

}