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
 * 
 * The persistent class for the IMS_CLS_OMP_CLAIM table.
 *
 */

@Entity
@Table(name="IMS_CLS_OMP_CLAIM")
@NamedQueries({
@NamedQuery(name="OMPClaim.findAllOMP", query="SELECT c FROM OMPClaim c"),
@NamedQuery(name ="OMPClaim.findByKeyOMP", query="SELECT c FROM OMPClaim c WHERE c.key = :primaryKey and c.intimation.lobId = :lobId"),
@NamedQuery(name ="OMPClaim.findByOMPIntimationKey", query="SELECT c FROM OMPClaim c WHERE c.intimation.key = :intimationKey  and c.intimation.lobId = :lobId"),
@NamedQuery(name ="OMPClaim.findByPolicyNumber", query="SELECT c FROM OMPClaim c Where c.intimation.policy.policyNumber = :policyNumber and c.intimation.lobId = :lobId"),
@NamedQuery(name ="OMPClaim.findOMPByIntimationNumber", query = "select c from OMPClaim c where c.intimation.intimationId = :intimationNumber and c.intimation.lobId = :lobId"),
@NamedQuery(name ="OMPClaim.findOMPByClaimNumber", query = "select c from OMPClaim c where c.claimId = :claimNumber and c.intimation.lobId = :lobId"),
@NamedQuery(name ="OMPClaim.findOMPByClaimKey", query = "select c from OMPClaim c where c.key = :claimkey")
})
public class OMPClaim  extends AbstractEntity {
	
	private static final long serialVersionUID = 7629344868554922910L;

	@Id
	@SequenceGenerator(name="IMS_CLS_OMP_CLAIM_KEY_GENERATOR", sequenceName = "SEQ_CLAIM_KEY" , allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_OMP_CLAIM_KEY_GENERATOR" )
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
	private OMPIntimation intimation;

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
	
	@Column(name="PRODUCT_CODE")
	private String productCode;
	
	@Column(name="PRODUCT_NAME")
	private String productName;
	
	@Column(name="LOSS_DATE_TIME")
	private Date lossDateTime;
		
	@Column(name="DOLLAR_INIT_PROVISION_AMT")
	private Double dollarInitProvisionAmount;
	
	@Column(name="HOSPITALISATION_FLAG")
	private String hospitalisationFlag;
	
	@Column(name="NON_HOSPITALISATION_FLAG")
	private String nonHospitalisationFlag;
	
	@Column(name="INR_CONVERSION_RATE")
	private Double inrConversionRate;
	
	@Column(name="INR_TOTAL_AMOUNT")
	private Double inrTotalAmount;
	
	@Column(name="HOSPITAL_NAME")
	private String hospitalName;
	
	@Column(name="CITY_NAME")
	private String cityName;

	@Column(name="COUNTRY_ID")
	private Long countryId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="EVENT_CODE_ID", nullable=true)
	private MastersEvents event;
	
	@Column(name="AILMENT_LOSS")
	private String ailmentLoss;
	
	@Column(name="CONFIRM_REJECTION_REMARKS")
	private String confirmRejectionRemarks;	

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="INSURED_KEY", nullable=true)
	private Insured insuredKey;
	
	@Column(name="LOB_ID")
	private Long lobId;
	
	@Column(name="LOSS_DETAILS")
	private String lossDetails;	
	
	@Column(name="PLACE_VISIT")
	private String plaveOfVisit;	
	
	@Column(name="PLACE_LOSS_DELAY")
	private String plaveOfLossOrDelay;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LOSS_TIME")
	private Date lossTime;
	
	@Column(name="LEGAL_OPINION_TAKEN")
	private String legalOpinionFlag;
	
	@Column(name="HOSPITAL_ID")
	private Long hospital;
	
	@Column(name="ACCIDENT_EVENT_PLACE")
	private String placeOfAccident;
	
	@Column(name="PATIENT_STATUS_ID")
	private Long patientStatusId;
	
	@Column(name="CASH_GUARANTEE_FLAG")
	private String cgOption;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CASH_GUARANTEE_DATE")
	private Date dateOfCashGuarantee;
	
	@Column(name="CASH_GUARANTEE_AMT")
	private Long cgAmount;
	
	@Column(name="CASH_GUARANTEE_REMARKS")
	private String cgRemarks;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_OF_DEATH")
	private Date dateOfDeath;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}	

	public MastersValue getClaimType() {
		return claimType;
	}

	public void setClaimType(MastersValue claimType) {
		this.claimType = claimType;
	}

	public OMPIntimation getIntimation() {
		return intimation;
	}

	public void setIntimation(OMPIntimation intimation) {
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

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Date getLossDateTime() {
		return lossDateTime;
	}

	public void setLossDateTime(Date lossDateTime) {
		this.lossDateTime = lossDateTime;
	}

	public Double getDollarInitProvisionAmount() {
		return dollarInitProvisionAmount;
	}

	public void setDollarInitProvisionAmount(Double dollarInitProvisionAmount) {
		this.dollarInitProvisionAmount = dollarInitProvisionAmount;
	}

	public String getHospitalisationFlag() {
		return hospitalisationFlag;
	}

	public void setHospitalisationFlag(String hospitalisationFlag) {
		this.hospitalisationFlag = hospitalisationFlag;
	}

	public String getNonHospitalisationFlag() {
		return nonHospitalisationFlag;
	}

	public void setNonHospitalisationFlag(String nonHospitalisationFlag) {
		this.nonHospitalisationFlag = nonHospitalisationFlag;
	}

	public Double getInrConversionRate() {
		return inrConversionRate;
	}

	public void setInrConversionRate(Double inrConversionRate) {
		this.inrConversionRate = inrConversionRate;
	}

	public Double getInrTotalAmount() {
		return inrTotalAmount;
	}

	public void setInrTotalAmount(Double inrTotalAmount) {
		this.inrTotalAmount = inrTotalAmount;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public String getAilmentLoss() {
		return ailmentLoss;
	}

	public void setAilmentLoss(String ailmentLoss) {
		this.ailmentLoss = ailmentLoss;
	}

	public String getConfirmRejectionRemarks() {
		return confirmRejectionRemarks;
	}

	public void setConfirmRejectionRemarks(String confirmRejectionRemarks) {
		this.confirmRejectionRemarks = confirmRejectionRemarks;
	}	
	
	public MastersEvents getEvent() {
		return event;
	}

	public void setEvent(MastersEvents event) {
		this.event = event;
	}

	public Insured getInsuredKey() {
		return insuredKey;
	}

	public void setInsuredKey(Insured insuredKey) {
		this.insuredKey = insuredKey;
	}

	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getKey() == null) {
            return obj == this;
        } else {
            return getKey().equals(((OMPClaim) obj).getKey());
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

	public Long getLobId() {
		return lobId;
	}

	public void setLobId(Long lobId) {
		this.lobId = lobId;
	}

	public String getLossDetails() {
		return lossDetails;
	}

	public void setLossDetails(String lossDetails) {
		this.lossDetails = lossDetails;
	}

	public String getPlaveOfVisit() {
		return plaveOfVisit;
	}

	public void setPlaveOfVisit(String plaveOfVisit) {
		this.plaveOfVisit = plaveOfVisit;
	}

	public String getPlaveOfLossOrDelay() {
		return plaveOfLossOrDelay;
	}

	public void setPlaveOfLossOrDelay(String plaveOfLossOrDelay) {
		this.plaveOfLossOrDelay = plaveOfLossOrDelay;
	}

	public Date getLossTime() {
		return lossTime;
	}

	public void setLossTime(Date lossTime) {
		this.lossTime = lossTime;
	}

	public String getLegalOpinionFlag() {
		return legalOpinionFlag;
	}

	public void setLegalOpinionFlag(String legalOpinionFlag) {
		this.legalOpinionFlag = legalOpinionFlag;
	}

	public Long getHospital() {
		return hospital;
	}

	public void setHospital(Long hospital) {
		this.hospital = hospital;
	}

	public String getPlaceOfAccident() {
		return placeOfAccident;
	}

	public void setPlaceOfAccident(String placeOfAccident) {
		this.placeOfAccident = placeOfAccident;
	}

	public Long getPatientStatusId() {
		return patientStatusId;
	}

	public void setPatientStatusId(Long patientStatusId) {
		this.patientStatusId = patientStatusId;
	}

	public String getCgOption() {
		return cgOption;
	}

	public void setCgOption(String cgOption) {
		this.cgOption = cgOption;
	}

	public Date getDateOfCashGuarantee() {
		return dateOfCashGuarantee;
	}

	public void setDateOfCashGuarantee(Date dateOfCashGuarantee) {
		this.dateOfCashGuarantee = dateOfCashGuarantee;
	}

	public Long getCgAmount() {
		return cgAmount;
	}

	public void setCgAmount(Long cgAmount) {
		this.cgAmount = cgAmount;
	}

	public String getCgRemarks() {
		return cgRemarks;
	}

	public void setCgRemarks(String cgRemarks) {
		this.cgRemarks = cgRemarks;
	}

	public Date getDateOfDeath() {
		return dateOfDeath;
	}

	public void setDateOfDeath(Date dateOfDeath) {
		this.dateOfDeath = dateOfDeath;
	}
	
}