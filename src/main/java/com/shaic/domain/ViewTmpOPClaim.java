package com.shaic.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.preauth.Stage;

@Entity
@Table(name="VW_CLAIM_OP_TMP")
@NamedQueries({
@NamedQuery(name="ViewTmpOPClaim.findAll", query="SELECT c FROM ViewTmpOPClaim c"),
@NamedQuery(name="ViewTmpOPClaim.findByCreatedDates", query="SELECT c FROM ViewTmpOPClaim c WHERE c.createdDate between :fromDate and :endDate"),
@NamedQuery(name ="ViewTmpOPClaim.findByKey",query="SELECT c FROM ViewTmpOPClaim c WHERE c.key = :primaryKey"),
@NamedQuery(name ="ViewTmpOPClaim.findCashlessClaim",query="SELECT c FROM ViewTmpOPClaim c WHERE c.claimType.key = :claimType"),
@NamedQuery(name ="ViewTmpOPClaim.findByCPUKey",query="SELECT c FROM ViewTmpOPClaim c WHERE c.intimation is not null and c.intimation.cpuCode.key = :cpuKey"),
@NamedQuery(name="ViewTmpOPClaim.findByIntimationKey", query="SELECT c FROM ViewTmpOPClaim c WHERE c.intimation is not null and c.intimation.key = :intimationKey"),
@NamedQuery(name="ViewTmpOPClaim.findByIntimationKeyAndClaimNumber", query="SELECT c FROM ViewTmpOPClaim c WHERE c.intimation is not null and c.intimation.key = :intimationKey and c.claimId =:claimNumber"),
@NamedQuery(name="ViewTmpOPClaim.findByClaimNumber", query="SELECT c FROM ViewTmpOPClaim c WHERE c.claimId = :claimNumber"),
@NamedQuery(name="ViewTmpOPClaim.findByIntimationNumber", query="SELECT c FROM ViewTmpOPClaim c WHERE c.intimation is not null and c.intimation.intimationId = :intimationNumber"),
@NamedQuery(name="ViewTmpOPClaim.findByPolicyNumber", query="SELECT c FROM ViewTmpOPClaim c WHERE c.intimation is not null and c.intimation.policy is not null and c.intimation.policyNumber = :policyNumber and c.policyNumber = :policyNumber order by c.key desc"),
@NamedQuery(name="ViewTmpOPClaim.findByInsuredID", query="SELECT c FROM ViewTmpOPClaim c WHERE c.intimation is not null and c.intimation.policy is not null and c.intimation.insured.insuredId = :insuredId"),
@NamedQuery(name="ViewTmpOPClaim.findByClaimKey", query="SELECT c FROM ViewTmpOPClaim c WHERE c.key = :claimKey"),
@NamedQuery(name="ViewTmpOPClaim.findByCreatedUser", query="SELECT c FROM ViewTmpOPClaim c WHERE c.createdBy like :createdBy and c.createdDate between :fromDate and :endDate"),
@NamedQuery(name="ViewTmpOPClaim.findByIntimationNoAndPolicyNo", query="SELECT c FROM ViewTmpOPClaim c WHERE c.intimation.policy is not null and c.intimation.policyNumber = :policyNumber and c.intimation is not null and c.intimation.intimationId = :intimationNumber"),
@NamedQuery(name="ViewTmpOPClaim.findByIntimationKeys", query="SELECT c FROM ViewTmpOPClaim c WHERE c.intimation is not null and c.intimation.key in (:intimationKeys)"),
@NamedQuery(name="ViewTmpOPClaim.findByIntimationNo", query="SELECT c FROM ViewTmpOPClaim c WHERE c.intimation is not null and c.intimation.intimationId = :intimationNumber")
})
public class ViewTmpOPClaim  extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
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
	private ViewTmpOPIntimation intimation;

	@Column(name="IS_VIP_CUSTOMER")
	private Long isVipCustomer;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;

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
	
	@Column(name = "LEGAL_FLAG")
	private String legalFlag;
	
	@Column(name = "RECORD_FLAG")
	private String recordFlag;
	
	@Column(name="SETL_AMT")
	private Double settledAmount;
	
	@Column(name="DIAGNOSIS")
	private String diagnosis;
	
	@Column(name="POLICY_NUMBER")
	private String policyNumber;
	
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

	@Column(name="MEDICAL_REMARKS")
	private String medicalRemarks;
	
	@Column(name="DOCTOR_NOTE")
	private String doctorNote;

	@Column(name="INCIDENCE_FLAG")
	private String incidenceFlag;

	

	public ViewTmpOPClaim() {
		
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
            return getKey().equals(((ViewTmpOPClaim) obj).getKey());
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

	

	public ViewTmpOPIntimation getIntimation() {
		return intimation;
	}

	public void setIntimation(ViewTmpOPIntimation intimation) {
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

	public String getRecordFlag() {
		return recordFlag;
	}

	public void setRecordFlag(String recordFlag) {
		this.recordFlag = recordFlag;
	}

	public Double getSettledAmount() {
		return settledAmount;
	}

	public void setSettledAmount(Double settledAmount) {
		this.settledAmount = settledAmount;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getIncidenceFlag() {
		return incidenceFlag;
	}

	public void setIncidenceFlag(String incidenceFlag) {
		this.incidenceFlag = incidenceFlag;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
