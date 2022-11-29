package com.shaic.domain;

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
import javax.persistence.Transient;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.preauth.Stage;


/**
 * The persistent class for the IMS_CLS_CLAIM_T database table.
 * 
 */
@Entity
@Table(name="IMS_CLS_OPH_CLAIM")
@NamedQueries({
@NamedQuery(name="OPClaim.findAll", query="SELECT c FROM OPClaim c"),
@NamedQuery(name="OPClaim.findByCreatedDates", query="SELECT c FROM OPClaim c WHERE c.createdDate between :fromDate and :endDate"),
@NamedQuery(name ="OPClaim.findByKey",query="SELECT c FROM OPClaim c WHERE c.key = :primaryKey"),
@NamedQuery(name ="OPClaim.findCashlessClaim",query="SELECT c FROM OPClaim c WHERE c.claimType.key = :claimType"),
@NamedQuery(name ="OPClaim.findByCPUKey",query="SELECT c FROM OPClaim c WHERE c.intimation is not null and c.intimation.cpuCode.key = :cpuKey"),
@NamedQuery(name="OPClaim.findByIntimationKey", query="SELECT c FROM OPClaim c WHERE c.intimation is not null and c.intimation.key = :intimationKey"),
@NamedQuery(name="OPClaim.findByIntimationKeyAndClaimNumber", query="SELECT c FROM OPClaim c WHERE c.intimation is not null and c.intimation.key = :intimationKey and c.claimId =:claimNumber"),
@NamedQuery(name="OPClaim.findByClaimNumber", query="SELECT c FROM OPClaim c WHERE c.claimId = :claimNumber"),
@NamedQuery(name="OPClaim.findByIntimationNumber", query="SELECT c FROM OPClaim c WHERE c.intimation is not null and c.intimation.intimationId = :intimationNumber"),
@NamedQuery(name="OPClaim.findByPolicyNumber", query="SELECT c FROM OPClaim c WHERE c.intimation is not null and c.intimation.policy is not null and c.intimation.policy.policyNumber = :policyNumber order by c.key desc"),
@NamedQuery(name="OPClaim.findByInsuredID", query="SELECT c FROM OPClaim c WHERE c.intimation is not null and c.intimation.policy is not null and c.intimation.insured.insuredId = :insuredId"),
@NamedQuery(name="OPClaim.findByClaimKey", query="SELECT c FROM OPClaim c WHERE c.key = :claimKey"),
@NamedQuery(name="OPClaim.findByCreatedUser", query="SELECT c FROM OPClaim c WHERE c.createdBy like :createdBy and c.createdDate between :fromDate and :endDate"),
@NamedQuery(name="OPClaim.findByIntimationNoAndPolicyNo", query="SELECT c FROM OPClaim c WHERE c.intimation.policy is not null and c.intimation.policy.policyNumber = :policyNumber and c.intimation is not null and c.intimation.intimationId = :intimationNumber"),
@NamedQuery(name="OPClaim.findByIntimationNo", query="SELECT c FROM OPClaim c WHERE c.intimation is not null and c.intimation.intimationId = :intimationNumber"),
@NamedQuery(name="OPClaim.findByIntimationId", query="SELECT c FROM OPClaim c WHERE c.intimation is not null and c.intimation.intimationId like :intimationNumber"),
@NamedQuery(name="OPClaim.findByPolicyKey", query="SELECT c FROM OPClaim c WHERE c.intimation.policy.key = :policyKey")
})
public class OPClaim  extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="IMS_CLS_OP_CLAIM_KEY_GENERATOR", sequenceName = "SEQ_CLAIM_KEY" , allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_OP_CLAIM_KEY_GENERATOR" )
	@Column(name="CLAIM_KEY")
	private Long key;

	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

//	@Column(name="ACTIVE_STATUS_DATE")
//	private Timestamp activeStatusDate;

	//Column renamed form Id to number
	@Column(name="CLAIM_NUMBER")
	private String claimId;

	@Column(name="CLAIM_LINK")
	private Long claimLink;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CLAIM_TYPE_ID", nullable=true)
	private MastersValue claimType;

	@Column(name="CLAIMED_AMOUNT")
	private Double claimedAmount;

	@Column(name="CLAIMED_BASE_AMOUNT")
	private Double claimedHomeAmount;

	@Column(name="CURRENT_PROVISION_AMOUNT")
	private Double currentProvisionAmount;
	
	@Transient
	private Long claimedamountCurrencyId;

	@Transient
	private Long claimedhomeamountCurrencyId;

	@Column(name="CONVERSION_FLAG")
	private Long conversionFlag;

	@Column(name="COVERING_LETTER_FLAG")
	private Long conversionLetter;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CONVERSION_REASON_ID")
	private MastersValue conversionReason;

	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name = "LATEST_ITERATION_KEY")
	private Long latestPreauthKey;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="INTIMATION_KEY", nullable=true)
	private OPIntimation intimation;

	@Column(name="IS_VIP_CUSTOMER")
	private Long isVipCustomer;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REMINDER_DATE1")
	private Date firstReminderDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REMINDER_DATE2")
	private Date secondReminderDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REMINDER_DATE3")
	private Date thirdReminderDate;
	
	@Column(name="REMINDER_COUNT")
	private Long reminderCount;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CONVERSION_DATE")
	private Date conversionDate;

	@Column(name="OFFICE_CODE")
	private String officeCode;

	@Transient
	private Long proamountCurrencyId;

	@Transient
	private Long prohomeamountCurrencyId;

	@Column(name="PROVISION_AMOUNT")
	private Double provisionAmount;

	@Column(name="PROVISION_BASE_AMOUNT")
	private Double provisionHomeAmount;

	@Column(name="REJECTION_LETTER_FLAG")
	private Long rejectionLetterflag;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STATUS_ID",nullable=true)
	private Status status;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STAGE_ID",nullable=true)
	private Stage stage;
	
//	@Column(name="SUB_STATUS_ID")
//	private Long substatusId;	

//	@Column(name="STATUS_DATE")
//	private Timestamp statusDate;

//	@Column(name="VERSION")
//	private Integer version;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CURRENCY_ID", nullable=true)
	private MastersValue currencyId;

//	@OneToOne
//	@JoinColumn(name="SUGGEST_REJECTION_ID", nullable=true)
//	//@Column(name="SUGGEST_REJECTION_ID", nullable=true)
//	private MastersValue suggestRejection;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="REJECTION_CATEGORY_ID", nullable=true)
	private MastersValue rejectionCategoryId;
	
	@Column(name="REGISTRATION_REMARKS")
	private String registrationRemarks;
	
	@Column(name="SUGGESTED_REJECTION_REMARKS")
	private String suggestedRejectionRemarks;
	
	@Column(name="REJECTION_REMARKS")
	private String rejectionRemarks;
	
	@Column(name="WAIVE_REMARKS")
	private String waiverRemarks;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_OF_ADMISSION")
	private Date dataOfAdmission;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_OF_DISCHARGE")
	private Date dataOfDischarge;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CLM_REGISTERED_DATE")
	private Date claimRegisteredDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CLOSING_DATE")
//	@Transient
	private Date closeDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REOPEN_DATE")
//	@Transient
	private Date reopenDate;
	
	@Column(name = "LEGAL_FLAG")
	private String legalFlag;
	
	@Column(name = "AUTO_RESTORATION_FLAG")
	private String autoRestroationFlag;
		
	@Column(name = "TXT_NORMAL_CLAIM_FLAG")
	private String normalClaimFlag;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INT_CR_DATE")
	private Date intimationCreatedDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DOC_REC_DATE")
	private Date documentReceivedDate;

	
	@Column(name="REVISED_CURRENT_PROVISION_AMT")
	private Double revisedProvisionAmount;

	@Column(name="MEDICAL_REMARKS")
	private String medicalRemarks;
	
	@Column(name="DOCTOR_NOTE")
	private String doctorNote;
	
	@Column(name="CLM_SEC_CODE")
	private String claimSectionCode;
	
	@Column(name="CLM_CVR_CODE")
	private String claimCoverCode;
	
	@Column(name="CLM_SUB_CVR_CODE")
	private String claimSubCoverCode;

	@Column(name="CORPORATE_BUFFER_LIMIT")
	private Double gmcCorpBufferLmt;
	
	@Column(name="CORPORATE_BUFFER_FLAG")
	private String gmcCorpBufferFlag;
	
	@Column(name="INCIDENCE_FLAG")
	private String incidenceFlag;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ACC_DEA_DATE")
	private Date incidenceDate;
	
	@Column(name="LOB_ID")
	private Long lobId;
	
	@Column(name="PROCESS_CLM_TYPE")
	private String processClaimType;
	
	@Column(name="HOS_REQ_FLAG")
	private String hospReqFlag;
	
	@Column(name="INJURY_REMARKS")
	private String injuryRemarks;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ACCIDENT_DATE")
	private Date accidentDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DEATH_DATE")
	private Date deathDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DISABLEMENT_DATE")
	private Date disablementDate;
	
	@Column(name="PA_HOSP_EXP_AMT")
	private Double paHospExpenseAmt;
	
	@Column(name="GPA_PARENT_NAME")
	private String gpaParentName;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "GPA_PARENT_DOB")
	private Date gpaParentDOB;
	
	@Column(name="GPA_PARENT_AGE")
	private Double gpaParentAge;

	@Column(name="GPA_RISK_NAME")
	private String gpaRiskName;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "GPA_RISK_DOB")
	private Date gpaRiskDOB;
	
	@Column(name="GPA_RISK_AGE")
	private Double gpaRiskAge;
	
	@Column(name="GPA_CATEGORY")
	private String gpaCategory;
	
	@Column(name="GPA_SECTION")
	private String gpaSection;
	
	@Column(name = "ORIGINAL_CPU_CODE")
	private Long originalCpuCode;
	
	@Column(name = "CRC_FLAG")
	private String crcFlag;
	
	@Column(name = "CRC_FLAGGED_REASON")
	private String crcFlaggedReason;
	
	@Column(name = "CRC_FLAGGED_REMARKS")
	private String crcFlaggedRemark;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CRC_FLAGGED_DATE")
	private Date crcFlaggedDate;
	
	@Column(name="DOCTOR_INTERNAL_REMARKS")
	private String internalNotes;
	
	@Column(name = "CRC_PRIORITY_CODE")
	private String crcPriorityCode;
	
	@Column(name = "CRC_PRIORITY_DESC")
	private String crcPriorityDesc;
	
	@Column(name = "ALLOW_GHI_REG_USER")
	private String ghiAllowUser;
	
	@Column(name = "ALLOW_GHI_REG_FLAG")
	private String ghiAllowFlag;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="INSURED_KEY", nullable=true)
	private Insured insuredKey;
	
	@Column(name = "EMERGENCY_FLAG")
	private String emergencyFlag;
	
	@Column(name = "ACCIDENT_FLAG")
	private String accidentFlag;
	
	@Column(name = "EMERGENCY_ACCIDENT_REMARKS")
	private String remarksForEmergencyAccident;
	
	@Column(name = "DOCTOR_CONTACT_NO")
	private Long doctorNo;
	
	@Column(name = "TREATMENT_TYPE_ID")
	private Long treatmentTypeId;
	
	@Column(name = "MODE_OF_RECEIPT_ID")
	private Long modeOfReceiptId;
	
	@Column(name = "PHY_DOC_RECEVIED_FLAG")
	private String physicalDocReceivedFlag;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PHY_DOC_RECEVIED_DATE")
	private Date physicalDocReceivedDate;
	
	@Column(name="CONSULTATION_TYPE")
	private Long consulationTypeId;
	
	@Column(name="OP_COVER_SECTION")
	private String opcoverSection;
	
	@Column(name="AGGREGATOR_CODE")
	private String aggregatorCode;
	
	@Column(name="SERVICE_TYPE")
	private String serviceType;
	
	@Column(name="CLAIM_TYPE")
	private Long claimTypeKey;
	
	
	public OPClaim() {
		
	}
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;

	}	

	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getKey() == null) {
            return obj == this;
        } else {
            return getKey().equals(((OPClaim) obj).getKey());
        }
    }

    @Override
    public int hashCode() {
        if (key != null) {
            return key.hashCode();
        } else {
            return super.hashCode();
        }
    }

	public MastersValue getClaimType() {
		return claimType;
	}

	public void setClaimType(MastersValue claimType) {
		this.claimType = claimType;
	}

	public OPIntimation getIntimation() {
		return intimation;
	}

	public void setIntimation(OPIntimation intimation) {
		this.intimation = intimation;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getClaimId() {
		return claimId;
	}

	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}

	public Long getClaimLink() {
		return claimLink;
	}

	public void setClaimLink(Long claimLink) {
		this.claimLink = claimLink;
	}

	public Double getClaimedAmount() {
		return claimedAmount;
	}

	public void setClaimedAmount(Double claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

	public Double getClaimedHomeAmount() {
		return claimedHomeAmount;
	}

	public void setClaimedHomeAmount(Double claimedHomeAmount) {
		this.claimedHomeAmount = claimedHomeAmount;
	}

	public Long getClaimedamountCurrencyId() {
		return claimedamountCurrencyId;
	}

	public void setClaimedamountCurrencyId(Long claimedamountCurrencyId) {
		this.claimedamountCurrencyId = claimedamountCurrencyId;
	}

	public Long getClaimedhomeamountCurrencyId() {
		return claimedhomeamountCurrencyId;
	}

	public void setClaimedhomeamountCurrencyId(Long claimedhomeamountCurrencyId) {
		this.claimedhomeamountCurrencyId = claimedhomeamountCurrencyId;
	}

	public Long getConversionFlag() {
		return conversionFlag;
	}

	public void setConversionFlag(Long conversionFlag) {
		this.conversionFlag = conversionFlag;
	}

	public Long getConversionLetter() {
		return conversionLetter;
	}

	public void setConversionLetter(Long conversionLetter) {
		this.conversionLetter = conversionLetter;
	}

	public MastersValue getConversionReason() {
		return conversionReason;
	}

	public void setConversionReason(MastersValue conversionReason) {
		this.conversionReason = conversionReason;
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

	public Long getIsVipCustomer() {
		return isVipCustomer;
	}

	public void setIsVipCustomer(Long isVipCustomer) {
		this.isVipCustomer = isVipCustomer;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public Long getProamountCurrencyId() {
		return proamountCurrencyId;
	}

	public void setProamountCurrencyId(Long proamountCurrencyId) {
		this.proamountCurrencyId = proamountCurrencyId;
	}

	public Long getProhomeamountCurrencyId() {
		return prohomeamountCurrencyId;
	}

	public void setProhomeamountCurrencyId(Long prohomeamountCurrencyId) {
		this.prohomeamountCurrencyId = prohomeamountCurrencyId;
	}

	public Double getProvisionAmount() {
		return provisionAmount;
	}

	public void setProvisionAmount(Double provisionAmount) {
		this.provisionAmount = provisionAmount;
	}

	public Double getProvisionHomeAmount() {
		return provisionHomeAmount;
	}

	public void setProvisionHomeAmount(Double provisionHomeAmount) {
		this.provisionHomeAmount = provisionHomeAmount;
	}

	public Long getRejectionLetterflag() {
		return rejectionLetterflag;
	}

	public void setRejectionLetterflag(Long rejectionLetterflag) {
		this.rejectionLetterflag = rejectionLetterflag;
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

	public MastersValue getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(MastersValue currencyId) {
		this.currencyId = currencyId;
	}

	
	public MastersValue getRejectionCategoryId() {
		return rejectionCategoryId;
	}

	public void setRejectionCategoryId(MastersValue rejectionCategoryId) {
		this.rejectionCategoryId = rejectionCategoryId;
	}

	public String getRegistrationRemarks() {
		return registrationRemarks;
	}

	public void setRegistrationRemarks(String registrationRemarks) {
		this.registrationRemarks = registrationRemarks;
	}

	public String getSuggestedRejectionRemarks() {
		return suggestedRejectionRemarks;
	}

	public void setSuggestedRejectionRemarks(String suggestedRejectionRemarks) {
		this.suggestedRejectionRemarks = suggestedRejectionRemarks;
	}

	public String getRejectionRemarks() {
		return rejectionRemarks;
	}

	public void setRejectionRemarks(String rejectionRemarks) {
		this.rejectionRemarks = rejectionRemarks;
	}

	public String getWaiverRemarks() {
		return waiverRemarks;
	}

	public void setWaiverRemarks(String waiverRemarks) {
		this.waiverRemarks = waiverRemarks;
	}

	public String getMedicalRemarks() {
		return medicalRemarks;
	}

	public void setMedicalRemarks(String medicalRemarks) {
		this.medicalRemarks = medicalRemarks;
	}

	public String getDoctorNote() {
		return doctorNote;
	}

	public void setDoctorNote(String doctorNote) {
		this.doctorNote = doctorNote;
	}

	public Date getDataOfAdmission() {
		return dataOfAdmission;
	}

	public void setDataOfAdmission(Date dataOfAdmission) {
		this.dataOfAdmission = dataOfAdmission;
	}

	public Date getDataOfDischarge() {
		return dataOfDischarge;
	}

	public void setDataOfDischarge(Date dataOfDischarge) {
		this.dataOfDischarge = dataOfDischarge;
	}

	public Long getLatestPreauthKey() {
		return latestPreauthKey;
	}

	public void setLatestPreauthKey(Long latestPreauthKey) {
		this.latestPreauthKey = latestPreauthKey;
	}

	public String getLegalFlag() {
		return legalFlag;
	}

	public void setLegalFlag(String legalFlag) {
		this.legalFlag = legalFlag;
	}

	public Date getFirstReminderDate() {
		return firstReminderDate;
	}

	public void setFirstReminderDate(Date firstReminderDate) {
		this.firstReminderDate = firstReminderDate;
	}

	public Date getSecondReminderDate() {
		return secondReminderDate;
	}

	public void setSecondReminderDate(Date secondReminderDate) {
		this.secondReminderDate = secondReminderDate;
	}

	public Date getThirdReminderDate() {
		return thirdReminderDate;
	}

	public void setThirdReminderDate(Date thirdReminderDate) {
		this.thirdReminderDate = thirdReminderDate;
	}
	
	public Date getConversionDate() {
		return conversionDate;
	}

	public void setConversionDate(Date conversionDate) {
		this.conversionDate = conversionDate;
	}

	public Long getReminderCount() {
		return reminderCount;
	}

	public void setReminderCount(Long reminderCount) {
		this.reminderCount = reminderCount;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public Date getReopenDate() {
		return reopenDate;
	}

	public void setReopenDate(Date reopenDate) {
		this.reopenDate = reopenDate;
	}

	public String getAutoRestroationFlag() {
		return autoRestroationFlag;
	}

	public void setAutoRestroationFlag(String autoRestroationFlag) {
		this.autoRestroationFlag = autoRestroationFlag;
	}

	public String getNormalClaimFlag() {
		return normalClaimFlag;
	}

	public void setNormalClaimFlag(String normalClaimFlag) {
		this.normalClaimFlag = normalClaimFlag;
	}

	public Date getIntimationCreatedDate() {
		return intimationCreatedDate;
	}

	public void setIntimationCreatedDate(Date intimationCreatedDate) {
		this.intimationCreatedDate = intimationCreatedDate;
	}

	public Date getDocumentReceivedDate() {
		return documentReceivedDate;
	}

	public void setDocumentReceivedDate(Date documentReceivedDate) {
		this.documentReceivedDate = documentReceivedDate;
	}
	public String getClaimSectionCode() {
		return claimSectionCode;
	}

	public void setClaimSectionCode(String claimSectionCode) {
		this.claimSectionCode = claimSectionCode;
	}

	public String getClaimCoverCode() {
		return claimCoverCode;
	}

	public void setClaimCoverCode(String claimCoverCode) {
		this.claimCoverCode = claimCoverCode;
	}

	public String getClaimSubCoverCode() {
		return claimSubCoverCode;
	}

	public void setClaimSubCoverCode(String claimSubCoverCode) {
		this.claimSubCoverCode = claimSubCoverCode;
	}

	public Double getRevisedProvisionAmount() {
		return revisedProvisionAmount;
	}

	public void setRevisedProvisionAmount(Double revisedProvisionAmount) {
		this.revisedProvisionAmount = revisedProvisionAmount;
	}

	public String getGmcCorpBufferFlag() {
		return gmcCorpBufferFlag;
	}

	public void setGmcCorpBufferFlag(String gmcCorpBufferFlag) {
		this.gmcCorpBufferFlag = gmcCorpBufferFlag;
	}

	public Double getGmcCorpBufferLmt() {
		return gmcCorpBufferLmt;
	}

	public void setGmcCorpBufferLmt(Double gmcCorpBufferLmt) {
		this.gmcCorpBufferLmt = gmcCorpBufferLmt;
	}
	
	public Date getClaimRegisteredDate() {
		return claimRegisteredDate;
	}

	public void setClaimRegisteredDate(Date claimRegisteredDate) {
		this.claimRegisteredDate = claimRegisteredDate;
	}

	public Double getCurrentProvisionAmount() {
		return currentProvisionAmount;
	}

	public void setCurrentProvisionAmount(Double currentProvisionAmount) {
		this.currentProvisionAmount = currentProvisionAmount;
	}

	public Date getIncidenceDate() {
		return incidenceDate;
	}

	public void setIncidenceDate(Date incidenceDate) {
		this.incidenceDate = incidenceDate;
	}

	public String getIncidenceFlag() {
		return incidenceFlag;
	}

	public void setIncidenceFlag(String incidenceFlag) {
		this.incidenceFlag = incidenceFlag;
	}

	public Long getLobId() {
		return lobId;
	}

	public void setLobId(Long lobId) {
		this.lobId = lobId;
	}

	public String getProcessClaimType() {
		return processClaimType;
	}

	public void setProcessClaimType(String processClaimType) {
		this.processClaimType = processClaimType;
	}

	public String getHospReqFlag() {
		return hospReqFlag;
	}

	public void setHospReqFlag(String hospReqFlag) {
		this.hospReqFlag = hospReqFlag;
	}

	public String getInjuryRemarks() {
		return injuryRemarks;
	}

	public void setInjuryRemarks(String injuryRemarks) {
		this.injuryRemarks = injuryRemarks;
	}

	public Date getAccidentDate() {
		return accidentDate;
	}

	public void setAccidentDate(Date accidentDate) {
		this.accidentDate = accidentDate;
	}

	public Date getDeathDate() {
		return deathDate;
	}

	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}

	public Date getDisablementDate() {
		return disablementDate;
	}

	public void setDisablementDate(Date disablementDate) {
		this.disablementDate = disablementDate;
	}

	public Double getPaHospExpenseAmt() {
		return paHospExpenseAmt;
	}

	public void setPaHospExpenseAmt(Double paHospExpenseAmt) {
		this.paHospExpenseAmt = paHospExpenseAmt;
	}

	public String getGpaParentName() {
		return gpaParentName;
	}

	public void setGpaParentName(String gpaParentName) {
		this.gpaParentName = gpaParentName;
	}

	public Date getGpaParentDOB() {
		return gpaParentDOB;
	}

	public void setGpaParentDOB(Date gpaParentDOB) {
		this.gpaParentDOB = gpaParentDOB;
	}

	public Double getGpaParentAge() {
		return gpaParentAge;
	}

	public void setGpaParentAge(Double gpaParentAge) {
		this.gpaParentAge = gpaParentAge;
	}

	public String getGpaRiskName() {
		return gpaRiskName;
	}

	public void setGpaRiskName(String gpaRiskName) {
		this.gpaRiskName = gpaRiskName;
	}

	public Date getGpaRiskDOB() {
		return gpaRiskDOB;
	}

	public void setGpaRiskDOB(Date gpaRiskDOB) {
		this.gpaRiskDOB = gpaRiskDOB;
	}

	public Double getGpaRiskAge() {
		return gpaRiskAge;
	}

	public void setGpaRiskAge(Double gpaRiskAge) {
		this.gpaRiskAge = gpaRiskAge;
	}

	public String getGpaCategory() {
		return gpaCategory;
	}

	public void setGpaCategory(String gpaCategory) {
		this.gpaCategory = gpaCategory;
	}

	public String getGpaSection() {
		return gpaSection;
	}

	public void setGpaSection(String gpaSection) {
		this.gpaSection = gpaSection;
	}
	public Long getOriginalCpuCode() {
		return originalCpuCode;
	}
	public void setOriginalCpuCode(Long originalCpuCode) {
		this.originalCpuCode = originalCpuCode;
	}

	public String getInternalNotes() {
		return internalNotes;
	}
	public void setInternalNotes(String internalNotes) {
		this.internalNotes = internalNotes;

	}

	public String getCrcFlag() {
		return crcFlag;
	}
	public void setCrcFlag(String crcFlag) {
		this.crcFlag = crcFlag;
	}
	public String getCrcFlaggedReason() {
		return crcFlaggedReason;
	}
	public void setCrcFlaggedReason(String crcFlaggedReason) {
		this.crcFlaggedReason = crcFlaggedReason;
	}
	public String getCrcFlaggedRemark() {
		return crcFlaggedRemark;
	}
	public void setCrcFlaggedRemark(String crcFlaggedRemark) {
		this.crcFlaggedRemark = crcFlaggedRemark;
	}
	public Date getCrcFlaggedDate() {
		return crcFlaggedDate;
	}
	public void setCrcFlaggedDate(Date crcFlaggedDate) {
		this.crcFlaggedDate = crcFlaggedDate;
	}
	public String getCrcPriorityCode() {
		return crcPriorityCode;
	}
	public void setCrcPriorityCode(String crcPriorityCode) {
		this.crcPriorityCode = crcPriorityCode;
	}
	public String getCrcPriorityDesc() {
		return crcPriorityDesc;
	}
	public void setCrcPriorityDesc(String crcPriorityDesc) {
		this.crcPriorityDesc = crcPriorityDesc;
	}
	public String getGhiAllowUser() {
		return ghiAllowUser;
	}
	public void setGhiAllowUser(String ghiAllowUser) {
		this.ghiAllowUser = ghiAllowUser;
	}
	public String getGhiAllowFlag() {
		return ghiAllowFlag;
	}
	public void setGhiAllowFlag(String ghiAllowFlag) {
		this.ghiAllowFlag = ghiAllowFlag;
	}
	public Insured getInsuredKey() {
		return insuredKey;
	}
	public void setInsuredKey(Insured insuredKey) {
		this.insuredKey = insuredKey;
	}
	public String getEmergencyFlag() {
		return emergencyFlag;
	}
	public void setEmergencyFlag(String emergencyFlag) {
		this.emergencyFlag = emergencyFlag;
	}
	public String getAccidentFlag() {
		return accidentFlag;
	}
	public void setAccidentFlag(String accidentFlag) {
		this.accidentFlag = accidentFlag;
	}
	public String getRemarksForEmergencyAccident() {
		return remarksForEmergencyAccident;
	}
	public void setRemarksForEmergencyAccident(String remarksForEmergencyAccident) {
		this.remarksForEmergencyAccident = remarksForEmergencyAccident;
	}
	public Long getDoctorNo() {
		return doctorNo;
	}
	public void setDoctorNo(Long doctorNo) {
		this.doctorNo = doctorNo;
	}
	public Long getTreatmentTypeId() {
		return treatmentTypeId;
	}
	public void setTreatmentTypeId(Long treatmentTypeId) {
		this.treatmentTypeId = treatmentTypeId;
	}
	public Long getModeOfReceiptId() {
		return modeOfReceiptId;
	}
	public void setModeOfReceiptId(Long modeOfReceiptId) {
		this.modeOfReceiptId = modeOfReceiptId;
	}
	public String getPhysicalDocReceivedFlag() {
		return physicalDocReceivedFlag;
	}
	public void setPhysicalDocReceivedFlag(String physicalDocReceivedFlag) {
		this.physicalDocReceivedFlag = physicalDocReceivedFlag;
	}
	public Date getPhysicalDocReceivedDate() {
		return physicalDocReceivedDate;
	}
	public void setPhysicalDocReceivedDate(Date physicalDocReceivedDate) {
		this.physicalDocReceivedDate = physicalDocReceivedDate;
	}
	public Long getConsulationTypeId() {
		return consulationTypeId;
	}
	public void setConsulationTypeId(Long consulationTypeId) {
		this.consulationTypeId = consulationTypeId;
	}
	public String getOpcoverSection() {
		return opcoverSection;
	}
	public void setOpcoverSection(String opcoverSection) {
		this.opcoverSection = opcoverSection;
	}
	public String getAggregatorCode() {
		return aggregatorCode;
	}
	public void setAggregatorCode(String aggregatorCode) {
		this.aggregatorCode = aggregatorCode;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public Long getClaimTypeKey() {
		return claimTypeKey;
	}
	public void setClaimTypeKey(Long claimTypeKey) {
		this.claimTypeKey = claimTypeKey;
	}
	
	
}