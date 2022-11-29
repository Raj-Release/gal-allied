package com.shaic.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
 * The persistent class for the IMS_CLS_OMP_INTIMATION table.
 *
 */
@Entity
@Table(name="IMS_CLS_OMP_INTIMATION")
@NamedQueries({
	@NamedQuery(name="OMPIntimation.findAllOMP", query="SELECT o FROM OMPIntimation o where o.lobId = 443l"),
	@NamedQuery(name="OMPIntimation.findByKey", query="SELECT o FROM OMPIntimation o where o.key = :intiationKey"),
	@NamedQuery(name="OMPIntimation.findByInsuredId", query="SELECT o FROM OMPIntimation o where o.insured is not null and o.insured.insuredId = :insuredNumber"),
	@NamedQuery(name="OMPIntimation.findByPolicy", query="SELECT o FROM OMPIntimation o where o.policy is not null and o.policy.policyNumber = :policyNumber"),
	@NamedQuery(name="OMPIntimation.findByOMPIntimationNo", query="SELECT o FROM OMPIntimation o where o is not null and o.intimationId = :intimationNo and o.lobId = :lobId"),
	@NamedQuery(name="OMPIntimation.findByOMPIntimationNumber", query="SELECT o FROM OMPIntimation o where o is not null and o.intimationId like :intimationNo"),
	@NamedQuery(name="OMPIntimation.findOMPIntimationByIntimationNum", query="SELECT o FROM OMPIntimation o where o is not null and o.intimationId = :intimationNo"),
	})
public class OMPIntimation extends AbstractEntity {

	private static final long serialVersionUID = 5941163307961764159L;

	@Id
	@SequenceGenerator(name="IMS_CLS_OMP_INTIMATION_KEY_GENERATOR", sequenceName = "SEQ_INTIMATION_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_OMP_INTIMATION_KEY_GENERATOR" ) 
	@Column(name="INTIMATION_KEY")
	private Long key;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="POLICY_KEY", nullable = false,updatable=false)
	private Policy policy;
	
	@JoinColumn(name = "INSURED_KEY", nullable = false, unique = true, insertable = true)
	@OneToOne(fetch = FetchType.LAZY)
	private Insured insured;
		
	@Column(name="INTIMATION_NUMBER", nullable=true)
	private String intimationId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="HOSPITAL_TYPE_ID",  nullable=true)
	private MastersValue hospitalType;
	
	@Column(name="HOSPITAL_NAME_ID")
	private Long hospital;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="INTIMATED_BY_ID",  nullable=true)
	private MastersValue intimatedBy;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="INTIMATION_MODE_ID", nullable=true)
	private MastersValue intimationMode;
	
	@Column(name="INTIMATED_NAME")
	private String intimaterName;
	
	/*@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ADMISSION_TYPE_ID", nullable=true)
	private MastersValue admissionType;*/
	
	/*@Column(name="ADMISSION_REASON")
	private String admissionReason;*/
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ADMISSION_DATE")
	private Date admissionDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_OF_DISCHARGE")
	private Date dischargeDate;
	
	/*@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="MANAGEMENT_TYPE_ID",  nullable=true)
	private MastersValue managementType;
	
	@ManyToOne(fetch=FetchType.EAGER )
	@JoinColumn(name="ROOM_CATEGORY_ID",  nullable=true)
	private MastersValue roomCategory;*/
	
	/*@Column(name="INPATIENT_NUMBER")
	private String inpatientNumber;
	
	@Column(name="LATEINTIMATION_REASON")
	private String lateIntimationReason;*/
	
	@Column(name="HOSPITAL_COMMENTS")
	private String hospitalComments;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CPU_ID", nullable=true)
	private TmpCPUCode cpuCode;
	
	/*@Column(name="DOCTOR_NAME")
	private String doctorName;*/
	
	@Column(name="CALLER_PHONE_NUMBER")
	private String callerLandlineNumber;
	
	@Column(name="CALLER_MOBILE_NUMBER")
	private String callerMobileNumber;
	
	/*@Column(name="ATTENDERS_PHONE_NUMBER")
	private String attendersLandlineNumber;
	
	@Column(name="ATTENDERS_MOBILE_NUMBER")
	private String attendersMobileNumber;*/
	
	/*@Column(nullable = true, columnDefinition = "VARCHAR", name="RELAPSE_OF_ILLNESS", length = 1)
	private String relapseofIllness;*/
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="INTIMATION_SOURCE_ID", nullable=true)
	private MastersValue intimationSource;
	
	@Column(nullable = true, columnDefinition = "NUMBER", name="PATIENT_NOT_COVERED", length = 1)
	private Boolean patientNotCovered;
	
	@Column(name="REGISTRATION_TYPE")
    private String registrationType;
	
	@Column(name="REGISTRATION_STATUS")
    private String registrationStatus;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STAGE_ID")
	private Stage stage;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STATUS_ID")
	private Status status;
	
	@Column(nullable = true, columnDefinition = "Varchar", name="ACTIVE_STATUS", length = 1)
	private String activeStatus;
	
	@Column(name="OFFICE_CODE")
	private String officeCode;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE", insertable = false, updatable=false)
	private Date createdDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@ManyToOne(fetch=FetchType.EAGER )
	@JoinColumn(name="CLAIM_TYPE_ID",  nullable=true)
	private MastersValue claimType;
	
	// New Columns introduced for OMP--- Start
	@Column(name="DM_CODE")
	private Long dmCode;
	
	@Column(name="DOC_URL")
	private String docURL;
	
	@Transient
	@Column(name="POLICY_NUMBER")
	private String policyNumber;
	
	@Column(name="POLICY_YEAR")
	private String policyYear;
	
	@Column(name="LOB_ID")
	private Long lobId;
	
	@Column(name="PROCESS_CLM_TYPE")
	private String processClaimType;
	
	@Column(name="INCIDENCE_FLAG")
	private String incidenceFlag;
	
	@Column(name="HOS_REQ_FLAG")
	private String hospitalReqFlag;
	
	@Column(name="ACC_DEA_DATE")
	private Date accountDeactivatedDate;
	
	@Column(name="PASSPORT_NUMBER")
	private String passportNumber;
	
	@Column(name="INSURED_NAME")
	private String insuredName;
	
	@Column(name="LOSS_DATE_TIME")
	private Date lossDateTime;
	
	@Column(name="AILMENT_LOSS")
	private String ailmentLoss;
	
	@Column(name="TPA_INTIMATION_NUMBER")
	private String tpaIntimationNumber;
	
	/*@Column(name="EVENT_CODE_ID")
	private Long  eventCodeId;*/
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="EVENT_CODE_ID", nullable=true)
	private MastersEvents event;
	
	@Column(name="HOSPITALISATION_FLAG")
	private String  hospitalizationFlag;
	
	@Column(name="NON_HOSPITALISATION_FLAG")
	private String  nonHospitalizationFlag;
	
	@Column(name="PLACE_VISIT")
	private String  placeVisit;
	
	@Column(name="PLACE_LOSS_DELAY")
	private String  placeLossDelay;
	
	@Column(name="SPONSOR_NAME")
	private String  sponsorName;
	
	@Column(name="HOSPITAL_CODE")
	private String  hospitalCode;
	
	@Column(name="HOSPITAL_NAME")
	private String  hospitalName;

	@Column(name="CITY_NAME")
	private String  cityName;
	
	@Column(name="COUNTRY_ID")
	private Long  countryId;
	
	@Column(name="REMARKS")
	private String  remarks;
	
	@Column(name="DOLLAR_INIT_PROVISION_AMT")
	private Double  dollarInitProvisionAmt;
	
	@Column(name="INR_CONVERSION_RATE")
	private Double  inrConversionRate;
	
	@Column(name="INR_TOTAL_AMOUNT")
	private Double  inrTotalAmount;
	
	@Column(name="PASSPORT_EXPIRY_DATE")
	private Date  passportExpiryDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="INTIMATION_DATE")
	private Date intimationDate;
	
	@Column(name="EVENT_PLACE")
	private String eventPlace;
	
	@Column(name="LOSS_DETAILS")
	private String lossDetails;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LOSS_TIME")
	private Date lossTime;
	
	@Column(name="CLAIMED_AMOUNT")
	private Double claimedAmount;
	
	// New Columns introduced for OMP--- End
	
	
	@Override
	public Long getKey() {
		return this.key;
	}

	@Override
	public void setKey(Long key) {
		this.key = key;		
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}
	
	public Insured getInsured() {
		return insured;
	}

	public void setInsured(Insured insured) {
		this.insured = insured;
	}

	public String getIntimationId() {
		return intimationId;
	}

	public void setIntimationId(String intimationId) {
		this.intimationId = intimationId;
	}

	public MastersValue getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(MastersValue hospitalType) {
		this.hospitalType = hospitalType;
	}

	public Long getHospital() {
		return hospital;
	}

	public void setHospital(Long hospital) {
		this.hospital = hospital;
	}

	public MastersValue getIntimatedBy() {
		return intimatedBy;
	}

	public void setIntimatedBy(MastersValue intimatedBy) {
		this.intimatedBy = intimatedBy;
	}

	public MastersValue getIntimationMode() {
		return intimationMode;
	}

	public void setIntimationMode(MastersValue intimationMode) {
		this.intimationMode = intimationMode;
	}

	public String getIntimaterName() {
		return intimaterName;
	}

	public void setIntimaterName(String intimaterName) {
		this.intimaterName = intimaterName;
	}

	/*public MastersValue getAdmissionType() {
		return admissionType;
	}

	public void setAdmissionType(MastersValue admissionType) {
		this.admissionType = admissionType;
	}

	public String getAdmissionReason() {
		return admissionReason;
	}

	public void setAdmissionReason(String admissionReason) {
		this.admissionReason = admissionReason;
	}*/

	public Date getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(Date admissionDate) {
		this.admissionDate = admissionDate;
	}

	/*public MastersValue getManagementType() {
		return managementType;
	}

	public void setManagementType(MastersValue managementType) {
		this.managementType = managementType;
	}

	public MastersValue getRoomCategory() {
		return roomCategory;
	}

	public void setRoomCategory(MastersValue roomCategory) {
		this.roomCategory = roomCategory;
	}

	public String getInpatientNumber() {
		return inpatientNumber;
	}

	public void setInpatientNumber(String inpatientNumber) {
		this.inpatientNumber = inpatientNumber;
	}

	public String getLateIntimationReason() {
		return lateIntimationReason;
	}

	public void setLateIntimationReason(String lateIntimationReason) {
		this.lateIntimationReason = lateIntimationReason;
	}*/

	public String getHospitalComments() {
		return hospitalComments;
	}

	public void setHospitalComments(String hospitalComments) {
		this.hospitalComments = hospitalComments;
	}

	public TmpCPUCode getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(TmpCPUCode cpuCode) {
		this.cpuCode = cpuCode;
	}

	/*public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}*/

	public String getCallerLandlineNumber() {
		return callerLandlineNumber;
	}

	public void setCallerLandlineNumber(String callerLandlineNumber) {
		this.callerLandlineNumber = callerLandlineNumber;
	}

	public String getCallerMobileNumber() {
		return callerMobileNumber;
	}

	public void setCallerMobileNumber(String callerMobileNumber) {
		this.callerMobileNumber = callerMobileNumber;
	}

	/*public String getAttendersLandlineNumber() {
		return attendersLandlineNumber;
	}

	public void setAttendersLandlineNumber(String attendersLandlineNumber) {
		this.attendersLandlineNumber = attendersLandlineNumber;
	}

	public String getAttendersMobileNumber() {
		return attendersMobileNumber;
	}

	public void setAttendersMobileNumber(String attendersMobileNumber) {
		this.attendersMobileNumber = attendersMobileNumber;
	}

	public String getRelapseofIllness() {
		return relapseofIllness;
	}

	public void setRelapseofIllness(String relapseofIllness) {
		this.relapseofIllness = relapseofIllness;
	}*/

	public MastersValue getIntimationSource() {
		return intimationSource;
	}

	public void setIntimationSource(MastersValue intimationSource) {
		this.intimationSource = intimationSource;
	}

	public Boolean getPatientNotCovered() {
		return patientNotCovered;
	}

	public void setPatientNotCovered(Boolean patientNotCovered) {
		this.patientNotCovered = patientNotCovered;
	}

	public String getRegistrationType() {
		return registrationType;
	}

	public void setRegistrationType(String registrationType) {
		this.registrationType = registrationType;
	}

	public String getRegistrationStatus() {
		return registrationStatus;
	}

	public void setRegistrationStatus(String registrationStatus) {
		this.registrationStatus = registrationStatus;
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

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
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

	public MastersValue getClaimType() {
		return claimType;
	}

	public void setClaimType(MastersValue claimType) {
		this.claimType = claimType;
	}

	public Long getDmCode() {
		return dmCode;
	}

	public void setDmCode(Long dmCode) {
		this.dmCode = dmCode;
	}

	public String getDocURL() {
		return docURL;
	}

	public void setDocURL(String docURL) {
		this.docURL = docURL;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getPolicyYear() {
		return policyYear;
	}

	public void setPolicyYear(String policyYear) {
		this.policyYear = policyYear;
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

	public String getIncidenceFlag() {
		return incidenceFlag;
	}

	public void setIncidenceFlag(String incidenceFlag) {
		this.incidenceFlag = incidenceFlag;
	}

	public String getHospitalReqFlag() {
		return hospitalReqFlag;
	}

	public void setHospitalReqFlag(String hospitalReqFlag) {
		this.hospitalReqFlag = hospitalReqFlag;
	}

	public Date getAccountDeactivatedDate() {
		return accountDeactivatedDate;
	}

	public void setAccountDeactivatedDate(Date accountDeactivatedDate) {
		this.accountDeactivatedDate = accountDeactivatedDate;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public Date getLossDateTime() {
		return lossDateTime;
	}

	public void setLossDateTime(Date lossDateTime) {
		this.lossDateTime = lossDateTime;
	}

	public String getAilmentLoss() {
		return ailmentLoss;
	}

	public void setAilmentLoss(String ailmentLoss) {
		this.ailmentLoss = ailmentLoss;
	}

	public String getTpaIntimationNumber() {
		return tpaIntimationNumber;
	}

	public void setTpaIntimationNumber(String tpaIntimationNumber) {
		this.tpaIntimationNumber = tpaIntimationNumber;
	}

	public String getHospitalizationFlag() {
		return hospitalizationFlag;
	}

	public void setHospitalizationFlag(String hospitalizationFlag) {
		this.hospitalizationFlag = hospitalizationFlag;
	}

	public String getNonHospitalizationFlag() {
		return nonHospitalizationFlag;
	}

	public void setNonHospitalizationFlag(String nonHospitalizationFlag) {
		this.nonHospitalizationFlag = nonHospitalizationFlag;
	}

	public String getPlaceVisit() {
		return placeVisit;
	}

	public void setPlaceVisit(String placeVisit) {
		this.placeVisit = placeVisit;
	}

	public String getPlaceLossDelay() {
		return placeLossDelay;
	}

	public void setPlaceLossDelay(String placeLossDelay) {
		this.placeLossDelay = placeLossDelay;
	}

	public String getSponsorName() {
		return sponsorName;
	}

	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Double getDollarInitProvisionAmt() {
		return dollarInitProvisionAmt;
	}

	public void setDollarInitProvisionAmt(Double dollarInitProvisionAmt) {
		this.dollarInitProvisionAmt = dollarInitProvisionAmt;
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

	public Date getPassportExpiryDate() {
		return passportExpiryDate;
	}

	public void setPassportExpiryDate(Date passportExpiryDate) {
		this.passportExpiryDate = passportExpiryDate;
	}	
	
	public MastersEvents getEvent() {
		return event;
	}

	public void setEvent(MastersEvents event) {
		this.event = event;
	}

	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getKey() == null) {
            return obj == this;
        } else {
            return getKey().equals(((OMPIntimation) obj).getKey());
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

	public Date getIntimationDate() {
		return intimationDate;
	}

	public void setIntimationDate(Date intimationDate) {
		this.intimationDate = intimationDate;
	}

	public Date getDischargeDate() {
		return dischargeDate;
	}

	public void setDischargeDate(Date dischargeDate) {
		this.dischargeDate = dischargeDate;
	}

	public String getEventPlace() {
		return eventPlace;
	}

	public void setEventPlace(String eventPlace) {
		this.eventPlace = eventPlace;
	}

	public String getLossDetails() {
		return lossDetails;
	}

	public void setLossDetails(String lossDetails) {
		this.lossDetails = lossDetails;
	}

	public Date getLossTime() {
		return lossTime;
	}

	public void setLossTime(Date lossTime) {
		this.lossTime = lossTime;
	}

	public Double getClaimedAmount() {
		return claimedAmount;
	}

	public void setClaimedAmount(Double claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

}
