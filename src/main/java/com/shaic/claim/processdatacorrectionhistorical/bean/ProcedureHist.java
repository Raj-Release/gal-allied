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
@Table(name="IMS_CLS_CODHIS_PROCEDURE")
@NamedQueries({
	@NamedQuery(name="ProcedureHist.findAll", query="SELECT i FROM ProcedureHist i"),
	@NamedQuery(name="ProcedureHist.findByPreauthKey", query="SELECT i FROM ProcedureHist i where i.transactionKey = :preauthKey and  (i.deleteFlag is null or i.deleteFlag = 1 )"),
	@NamedQuery(name="ProcedureHist.findByTransactionKey", query="SELECT i FROM ProcedureHist i where i.transactionKey = :transactionKey and  (i.deleteFlag is null or i.deleteFlag = 1 ) order by i.key asc"),
	@NamedQuery(name="ProcedureHist.findByKey", query="SELECT i FROM ProcedureHist i where i.key = :primaryKey"),
})
public class ProcedureHist extends AbstractEntity{
	

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PROCEDURE_KEY")
	private Long key;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

	@Column(name="APPROVED_AMOUNT")
	private Double approvedAmount;
	
	@Column(name = "NET_APPROVED_AMOUNT")
	private Double netApprovedAmount;

	@Column(name="DIFF_AMOUNT")
	private Double diffAmount;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name="DAY_CARE_PROCEDURE", length=1)
	private String dayCareProcedure;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name="CONSIDER_FOR_DAY_CARE", length=1)
	private String considerForDayCare;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name="CONSIDER_FOR_PAYMENT", length=1)
	private String considerForPayment;
		
	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="EXCULSION_DETAILS_ID", nullable=false)
	private MastersValue exculsionDetails;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STAGE_ID",nullable=false)
	private Stage stage;
		
	@Column(name = "TRANSACTION_KEY", nullable = false)
	private Long transactionKey;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	

	@Column(name ="DELETED_FLAG")
	private Long deleteFlag;

	@Column(name="OFFICE_CODE")
	private String officeCode;

	@Column(name="PACKAGE_RATE")
	private Long packageRate;

	@Column(name="POLICY_AGEING")
	private String policyAgeing;
	
	@Column(name="PROCEDURE_ID")
	private Long procedureID;

	@Column(name="PROCEDURE_CODE")
	private String procedureCode;

	@Column(name="PROCEDURE_NAME")
	private String procedureName;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="PROCEDURE_STATUS_ID")
	private MastersValue procedureStatus;
	
	@Column(name="NEW_PROCEDURE_FLAG", length=1)
	private Long newProcedureFlag;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name="PROCESS_FLAG", length=1)
	private String processFlag;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name="ACTION", length=1)
	private String action;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name = "REC_TYPE_FLAG", length = 1)
	private String recTypeFlag;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STATUS_ID",nullable=false)
	private Status status;
	
	@Column(name="SUB_LIMIT_APPLICABLE")
	private String subLimitApplicable;
	
	@Column(name="LIMIT_ID")
	private Long sublimitNameId;
	
	@Column(name="VALIDATION_REMARKS")
	private String validationRemarks;
	
	@Column(name="PROCEDURE_REMARKS")
	private String procedureRemarks;
	
	@Column(name="APPROVED_REMARKS")
	private String approvedRemarks;
	
	@Column(name = "AMOUNT_CONSIDERED")
	private Double amountConsideredAmount;
	
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
	
	@Column(name="OLD_PROCEDURE_ID")
	private Long oldprocedureID; 
	
	@Column(name="PROCEDURE_FLAG")
	private String procedureFlag; 

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}
	
	public Double getApprovedAmount() {
		return approvedAmount;
	}

	public void setApprovedAmount(Double approvedAmount) {
		this.approvedAmount = approvedAmount;
	}

	public Double getDiffAmount() {
		return diffAmount;
	}

	public void setDiffAmount(Double diffAmount) {
		this.diffAmount = diffAmount;
	}

	public String getDayCareProcedure() {
		return dayCareProcedure;
	}

	public void setDayCareProcedure(String dayCareProcedure) {
		this.dayCareProcedure = dayCareProcedure;
	}

	public String getConsiderForDayCare() {
		return considerForDayCare;
	}

	public Long getProcedureID() {
		return procedureID;
	}

	public void setProcedureID(Long procedureID) {
		this.procedureID = procedureID;
	}

	public void setConsiderForDayCare(String considerForDayCare) {
		this.considerForDayCare = considerForDayCare;
	}

	public String getConsiderForPayment() {
		return considerForPayment;
	}

	public void setConsiderForPayment(String considerForPayment) {
		this.considerForPayment = considerForPayment;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public String getRecTypeFlag() {
		return recTypeFlag;
	}

	public void setRecTypeFlag(String recTypeFlag) {
		this.recTypeFlag = recTypeFlag;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	
	public MastersValue getExculsionDetails() {
		return exculsionDetails;
	}

	public void setExculsionDetails(MastersValue exculsionDetails) {
		this.exculsionDetails = exculsionDetails;
	}

	public Stage getStage() {
		return stage;
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

	public String getProcedureRemarks() {
		return procedureRemarks;
	}

	public void setProcedureRemarks(String procedureRemarks) {
		this.procedureRemarks = procedureRemarks;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public Long getPackageRate() {
		return packageRate;
	}

	public void setPackageRate(Long packageRate) {
		this.packageRate = packageRate;
	}

	public String getPolicyAgeing() {
		return policyAgeing;
	}

	public void setPolicyAgeing(String policyAgeing) {
		this.policyAgeing = policyAgeing;
	}

	public String getProcedureCode() {
		return procedureCode;
	}

	public void setProcedureCode(String procedureCode) {
		this.procedureCode = procedureCode;
	}

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	public MastersValue getProcedureStatus() {
		return procedureStatus;
	}

	public void setProcedureStatus(MastersValue procedureStatus) {
		this.procedureStatus = procedureStatus;
	}

	public Long getNewProcedureFlag() {
		return newProcedureFlag;
	}

	public void setNewProcedureFlag(Long newProcedureFlag) {
		this.newProcedureFlag = newProcedureFlag;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public String getSubLimitApplicable() {
		return subLimitApplicable;
	}

	public void setSubLimitApplicable(String subLimitApplicable) {
		this.subLimitApplicable = subLimitApplicable;
	}

	public Long getSublimitNameId() {
		return sublimitNameId;
	}

	public void setSublimitNameId(Long sublimitNameId) {
		this.sublimitNameId = sublimitNameId;
	}

	public String getValidationRemarks() {
		return validationRemarks;
	}

	public void setValidationRemarks(String validationRemarks) {
		this.validationRemarks = validationRemarks;
	}

	public String getApprovedRemarks() {
		return approvedRemarks;
	}

	public void setApprovedRemarks(String approvedRemarks) {
		this.approvedRemarks = approvedRemarks;
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

	public Long getOldprocedureID() {
		return oldprocedureID;
	}

	public void setOldprocedureID(Long oldprocedureID) {
		this.oldprocedureID = oldprocedureID;
	}

	public String getProcedureFlag() {
		return procedureFlag;
	}

	public void setProcedureFlag(String procedureFlag) {
		this.procedureFlag = procedureFlag;
	}
	
}
