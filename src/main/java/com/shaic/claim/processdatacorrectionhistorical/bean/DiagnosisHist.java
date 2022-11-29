package com.shaic.claim.processdatacorrectionhistorical.bean;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Stage;

@Entity
@Table(name = "IMS_CLS_CODHIS_DIAGNOSIS")
@NamedQueries({
	@NamedQuery(name = "DiagnosisHist.findAll", query = "SELECT i FROM DiagnosisHist i"),
	@NamedQuery(name="DiagnosisHist.findByPreauthKey", query="SELECT i FROM DiagnosisHist i where i.transactionKey = :preauthKey and (i.deleteFlag is null or i.deleteFlag = 1)"),
	@NamedQuery(name="DiagnosisHist.findByTransactionKey", query="SELECT i FROM DiagnosisHist i where i.transactionKey = :transactionKey and (i.deleteFlag is null or i.deleteFlag = 1)"),
	@NamedQuery(name="DiagnosisHist.findByIntimationKey",query="SELECT i FROM DiagnosisHist i where i.intimation = :intimationKey and (i.deleteFlag is null or i.deleteFlag = 1)"),
	@NamedQuery(name="DiagnosisHist.findByKey",query="SELECT i FROM DiagnosisHist i where i.key = :primaryKey and (i.deleteFlag is null or i.deleteFlag = 1)"),
	@NamedQuery(name="DiagnosisHist.findByPolicyKey",query="SELECT i FROM DiagnosisHist i where i.policy = :policyKey and (i.deleteFlag is null or i.deleteFlag = 1)"),
})
public class DiagnosisHist  extends AbstractEntity{
	

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "DIAGNOSIS_KEY")
	private Long key;

	@Column(name = "ACTIVE_STATUS")
	private Long activeStatus;

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

	@Column(name = "DIAGNOSIS_ID")
	private Long diagnosisId;

	@Column(name = "ICD_CHAPTER_ID")
	private Long icdChpterId;

	@Column(name = "ICD_BLOCK_ID")
	private Long icdBlockId;

	@Column(name = "ICD_CODE_ID")
	private Long icdCodeId;

	@Column(name = "INTIMATION_KEY")
	private Long intimation;

	@Column(name = "POLICY_KEY")
	private Long policy;

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

	@Column(name = "OFFICE_CODE")
	private String officeCode;

	@Column(name = "POLICY_AGING")
	private String policyAging;

	@Column(name = "APPROVED_REMARKS")
	private String approvedRemarks;
	
	@Column(name = "DIAGNOSIS_REMARKS")
	private String diagnosisRemarks;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_ID", nullable = false)
	private Status status;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STAGE_ID", nullable = false)
	private Stage stage;
		
	@Column(name = "AMOUNT_CONSIDERED")
	private Double amountConsideredAmount;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;

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
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PED_IMP_DIAGNOSIS_ID", nullable = true)
	private MastersValue pedImpactId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PED_PAY_NOT_CONS_ID", nullable = true)
	private MastersValue notPayingReason;
	
	@Column(name ="SUBLIMIT_UPD_REMARKS")
	private String sublimitUpdateRemarks;
	
	@Column(name = "OLD_ICD_CODE_ID")
	private Long oldIcdCode;
	
	@Column(name = "OLD_DIAGNOSIS_ID")
	private Long oldDiagnosisId;
	
	@Column(name = "CRCN_ICD_FLAG")
	private String icdFlag;
	
	@Column(name = "DIAGNOSIS_FLAG")
	private String diagnosisFlag;

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

	
	public String getPolicyAging() {
		return this.policyAging;
	}

	public void setPolicyAging(String policyAging) {
		this.policyAging = policyAging;
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

	public Long getOldIcdCode() {
		return oldIcdCode;
	}

	public void setOldIcdCode(Long oldIcdCode) {
		this.oldIcdCode = oldIcdCode;
	}

	public Long getOldDiagnosisId() {
		return oldDiagnosisId;
	}

	public void setOldDiagnosisId(Long oldDiagnosisId) {
		this.oldDiagnosisId = oldDiagnosisId;
	}

	public String getIcdFlag() {
		return icdFlag;
	}

	public void setIcdFlag(String icdFlag) {
		this.icdFlag = icdFlag;
	}

	public String getDiagnosisFlag() {
		return diagnosisFlag;
	}

	public void setDiagnosisFlag(String diagnosisFlag) {
		this.diagnosisFlag = diagnosisFlag;
	}

	public Long getIntimation() {
		return intimation;
	}

	public void setIntimation(Long intimation) {
		this.intimation = intimation;
	}

	public Long getPolicy() {
		return policy;
	}

	public void setPolicy(Long policy) {
		this.policy = policy;
	}

	public String getRecTypeFlag() {
		return recTypeFlag;
	}

	public void setRecTypeFlag(String recTypeFlag) {
		this.recTypeFlag = recTypeFlag;
	}

}
