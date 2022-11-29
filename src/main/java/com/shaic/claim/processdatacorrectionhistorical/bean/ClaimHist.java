package com.shaic.claim.processdatacorrectionhistorical.bean;

import java.lang.reflect.Field;
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
import com.shaic.domain.Claim;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Stage;

@Entity
@Table(name="IMS_CLS_CODHIS_CLAIM")
@NamedQueries({
@NamedQuery(name="ClaimHist.findAll", query="SELECT c FROM ClaimHist c"),
@NamedQuery(name="ClaimHist.findByCreatedDates", query="SELECT c FROM ClaimHist c WHERE c.createdDate between :fromDate and :endDate"),
@NamedQuery(name ="ClaimHist.findByKey",query="SELECT c FROM ClaimHist c WHERE c.key = :primaryKey"),
@NamedQuery(name ="ClaimHist.findCashlessClaimHist",query="SELECT c FROM ClaimHist c WHERE c.claimType.key = :claimType"),
@NamedQuery(name ="ClaimHist.findByCPUKey",query="SELECT c FROM ClaimHist c WHERE c.intimation is not null and c.intimation.cpuCode.key = :cpuKey"),
@NamedQuery(name="ClaimHist.findByIntimationKey", query="SELECT c FROM ClaimHist c WHERE c.intimation is not null and c.intimation.key = :intimationKey"),
@NamedQuery(name="ClaimHist.findByIntimationKeyAndClaimHistNumber", query="SELECT c FROM ClaimHist c WHERE c.intimation is not null and c.intimation.key = :intimationKey and c.claimId =:claimNumber"),
@NamedQuery(name="ClaimHist.findByClaimHistNumber", query="SELECT c FROM ClaimHist c WHERE c.claimId = :claimNumber"),
@NamedQuery(name="ClaimHist.findByIntimationNumber", query="SELECT c FROM ClaimHist c WHERE c.intimation is not null and c.intimation.intimationId = :intimationNumber"),
@NamedQuery(name="ClaimHist.findByPolicyNumber", query="SELECT c FROM ClaimHist c WHERE c.intimation is not null and c.intimation.policy is not null and c.intimation.policy.policyNumber = :policyNumber order by c.key desc"),
@NamedQuery(name="ClaimHist.findByInsuredID", query="SELECT c FROM ClaimHist c WHERE c.intimation is not null and c.intimation.policy is not null and c.intimation.insured.insuredId = :insuredId"),
@NamedQuery(name="ClaimHist.findByClaimHistKey", query="SELECT c FROM ClaimHist c WHERE c.key = :claimKey"),
@NamedQuery(name="ClaimHist.findByCreatedUser", query="SELECT c FROM ClaimHist c WHERE c.createdBy like :createdBy and c.createdDate between :fromDate and :endDate"),
@NamedQuery(name="ClaimHist.findByIntimationNoAndPolicyNo", query="SELECT c FROM ClaimHist c WHERE c.intimation.policy is not null and c.intimation.policy.policyNumber = :policyNumber and c.intimation is not null and c.intimation.intimationId = :intimationNumber"),
@NamedQuery(name="ClaimHist.findByIntimationNo", query="SELECT c FROM ClaimHist c WHERE c.intimation is not null and c.intimation.intimationId = :intimationNumber"),
@NamedQuery(name="ClaimHist.findByIntimationId", query="SELECT c FROM ClaimHist c WHERE c.intimation is not null and c.intimation.intimationId like :intimationNumber"),
@NamedQuery(name="ClaimHist.findByPolicyKey", query="SELECT c FROM ClaimHist c WHERE c.intimation.policy.key = :policyKey")
})
public class ClaimHist extends AbstractEntity{
	

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="CLAIM_KEY")
	private Long key;

	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

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
	private Intimation intimation;

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
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CURRENCY_ID", nullable=true)
	private MastersValue currencyId;
	
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
	private Date closeDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REOPEN_DATE")
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
	
	@Column(name = "SFX_CPU_CODE")
	private Long sfxCpuCode;
	
	@Column(name = "SFX_PROCESSING_CPU_CODE")
	private Long sfxProcessingCpuCode;
	
	@Column(name="CRCN_ICD_RC_SPL_FLAG")
	private String coadingFlag;
	
	@Column(name="CODING_USER")
	private String coadingUser;
	
	@Column(name="CODING_DATE")
	private Date coadingDate;
	
	@Column(name="CODING_REMARKS")
	private String coadingRemark;
	
	@Column(name="DC_CODHIS_FLAG")
	private String dcCoadingFlag;
	
	@Column(name="PP_FLAG")
	private String ppFlag;
	
	@Column(name="PP_PROTECTED")
	private String ppProtected;
	
	@Column(name="DC_PP_FLAG")
	private String dcppFlag;
	
	@Column(name="OLD_PP_PROTECTED")
	private String oldppProtected;

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
            return getKey().equals(((Claim) obj).getKey());
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

	public Intimation getIntimation() {
		return intimation;
	}

	public void setIntimation(Intimation intimation) {
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
	public Long getSfxCpuCode() {
		return sfxCpuCode;
	}
	public void setSfxCpuCode(Long sfxCpuCode) {
		this.sfxCpuCode = sfxCpuCode;
	}
	public Long getSfxProcessingCpuCode() {
		return sfxProcessingCpuCode;
	}
	public void setSfxProcessingCpuCode(Long sfxProcessingCpuCode) {
		this.sfxProcessingCpuCode = sfxProcessingCpuCode;
	}

	public String getCoadingFlag() {
		return coadingFlag;
	}
	public void setCoadingFlag(String coadingFlag) {
		this.coadingFlag = coadingFlag;
	}
	public String getCoadingUser() {
		return coadingUser;
	}
	public void setCoadingUser(String coadingUser) {
		this.coadingUser = coadingUser;
	}
	public Date getCoadingDate() {
		return coadingDate;
	}
	public void setCoadingDate(Date coadingDate) {
		this.coadingDate = coadingDate;
	}
	public String getCoadingRemark() {
		return coadingRemark;
	}
	public void setCoadingRemark(String coadingRemark) {
		this.coadingRemark = coadingRemark;
	}

	public String getDcCoadingFlag() {
		return dcCoadingFlag;
	}

	public void setDcCoadingFlag(String dcCoadingFlag) {
		this.dcCoadingFlag = dcCoadingFlag;
	}

	public String getPpFlag() {
		return ppFlag;
	}

	public void setPpFlag(String ppFlag) {
		this.ppFlag = ppFlag;
	}

	public String getPpProtected() {
		return ppProtected;
	}

	public void setPpProtected(String ppProtected) {
		this.ppProtected = ppProtected;
	}

	public String getDcppFlag() {
		return dcppFlag;
	}

	public void setDcppFlag(String dcppFlag) {
		this.dcppFlag = dcppFlag;
	}

	public String getOldppProtected() {
		return oldppProtected;
	}

	public void setOldppProtected(String oldppProtected) {
		this.oldppProtected = oldppProtected;
	}	

}
