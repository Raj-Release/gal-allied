package com.shaic.domain;

import java.io.Serializable;
import java.sql.Timestamp;
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
 * The persistent class for the IMS_GLX_INTIMATION database table.
 * 
 */
@Entity
@Table(name="IMS_CLS_OPH_INTIMATION")
@NamedQueries({
	@NamedQuery(name="OPIntimation.findAll", query="SELECT o FROM OPIntimation o"),
	@NamedQuery(name="OPIntimation.findByKey", query="SELECT o FROM OPIntimation o where o.key = :intiationKey"),
	@NamedQuery(name="OPIntimation.findByPolicyKey", query="SELECT o.key FROM OPIntimation o where o.policy.key = :policyKey"),
	@NamedQuery(name="OPIntimation.findByAllCombi", query="SELECT o FROM OPIntimation o where (:intimStatus is null or(o.status is not null and o.status.processValue like :intimStatus))"
			+ "and (:policyNo is null or o.policy.policyNumber like :policyNo)"
			+ "and (:insuredName is null or o.insured.insuredName like :insuredName)"
			+ "and (:intimationNo is null or o.intimationId like :intimationNo)"), 
	//and (:healthCardNo is null or o.policy.healthCardNumber like :healthCardNo) and (:hospitalName is null or o.hospital.value like :hospitalName))
	@NamedQuery(name="OPIntimation.findByPolicy", query="SELECT o FROM OPIntimation o where o.policy.policyNumber like :policyNo"),
	@NamedQuery(name="OPIntimation.findByStatus", query="SELECT o FROM OPIntimation o where o.status is not null and o.status.processValue like :intimStatus"),
	@NamedQuery(name="OPIntimation.findByIntimationNumber", query="SELECT o FROM OPIntimation o where o is not null and o.intimationId = :intimationNo"),
	@NamedQuery(name="OPIntimation.findByIntimationId", query="SELECT o FROM OPIntimation o where o is not null and Lower(o.intimationId) like :intimationNo"),
	@NamedQuery(name="OPIntimation.findByCreatedDate", query="SELECT o FROM OPIntimation o where o.createdDate between :startDate and :endDate"),
	@NamedQuery(name="OPIntimation.findByHealthCard", query="SELECT o FROM OPIntimation o where o.insured.healthCardNumber like :healthCardNo"),
	@NamedQuery(name ="OPIntimation.findByHealthCardNo",query = "SELECT o FROM OPIntimation o where o.intimationId = :intimationId and o.insured.healthCardNumber = :healthCardNo"),
	@NamedQuery(name="OPIntimation.findByInsuredName", query="SELECT o FROM OPIntimation o where o.insured.insuredName like :insuredName"),
	@NamedQuery(name="OPIntimation.findByInsuredId", query="SELECT o FROM OPIntimation o where o.insured.insuredId =:insuredId"),
	@NamedQuery(name="OPIntimation.findDuplicateInitmation", query="SELECT o FROM OPIntimation o where o.hospital = :hospital and o.policy.policyNumber = :policy and to_char(o.admissionDate,'dd-MM-yyyy') = :admissionDate and o.insured.insuredId = :insuredId and o.status is not null and o.status.processValue like 'Submitted'"),
	@NamedQuery(name = "OPIntimation.findEmpSummary", query = "SELECT i.createdBy, count(i.key) FROM OPIntimation i WHERE Lower(i.createdBy) like :createdBy and i.intimationSource.key = :empSourceKey and i.createdDate >= :fromDate and i.createdDate <= :toDate group by i.createdBy"),
	@NamedQuery(name="OPIntimation.findByIntimationNo", query="SELECT o FROM OPIntimation o where o is not null and o.intimationId like :intimationNo"),
	@NamedQuery(name="OPIntimation.findByMaxByPolicy", query="SELECT max(o.key) FROM OPIntimation o where o.policy.policyNumber like :policyNo"),
	@NamedQuery(name="OPIntimation.findByIntimationNum", query="SELECT o FROM OPIntimation o where o is not null and o.intimationId like :intimationNo"),

})

public class OPIntimation extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_CLS_OP_INTIMATION_KEY_GENERATOR", sequenceName = "SEQ_INTIMATION_KEY" ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_OP_INTIMATION_KEY_GENERATOR" ) 
	@Column(name="INTIMATION_KEY")
	private Long key;
	
	@Column(nullable = true, columnDefinition = "Varchar", name="ACTIVE_STATUS", length = 1)
	private String activeStatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ADMISSION_DATE")
	private Date admissionDate;

	@Column(name="ADMISSION_REASON")
	private String admissionReason;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ADMISSION_TYPE_ID", nullable=true)
	private MastersValue admissionType;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CPU_ID", nullable=true)
	private TmpCPUCode cpuCode;

	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE", updatable=false)
	private Date createdDate;

	@Column(name="DOCTOR_NAME")
	private String doctorName;

	@Column(name="HOSPITAL_COMMENTS")
	private String hospitalComments;

	@Column(name="HOSPITAL_NAME_ID")
	private Long hospital;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="HOSPITAL_TYPE_ID",  nullable=true)
	private MastersValue hospitalType;

	@Column(name="INPATIENT_NUMBER")
	private String inpatientNumber;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="INTIMATED_BY_ID",  nullable=true)
	private MastersValue intimatedBy;

	@Column(name="INTIMATED_NAME")
	private String intimaterName;

	//Column Renamed from intimationId to intimationNumber
	@Column(name="INTIMATION_NUMBER", nullable=true)
	private String intimationId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="INTIMATION_MODE_ID", nullable=true)
	private MastersValue intimationMode;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="INTIMATION_SOURCE_ID", nullable=true)
	private MastersValue intimationSource;
	
	@Column(name="LATEINTIMATION_REASON")
	private String lateIntimationReason;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="MANAGEMENT_TYPE_ID",  nullable=true)
	private MastersValue managementType;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;

	@Column(name="OFFICE_CODE")
	private String officeCode;

	@Column(nullable = true, columnDefinition = "NUMBER", name="PATIENT_NOT_COVERED", length = 1)
	private Boolean patientNotCovered;

	@Column(nullable = true, columnDefinition = "VARCHAR", name="RELAPSE_OF_ILLNESS", length = 1)
	private String relapseofIllness;
	
	@Column(nullable = true, columnDefinition = "VARCHAR", name="DUMMY", length = 5)
	private String dummy;
	
	@ManyToOne(fetch=FetchType.EAGER )
	@JoinColumn(name="ROOM_CATEGORY_ID",  nullable=true)
	private MastersValue roomCategory;
	
	@ManyToOne(fetch=FetchType.EAGER )
	@JoinColumn(name="CLAIM_TYPE_ID",  nullable=true)
	private MastersValue claimType;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STATUS_ID")
	private Status status;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STAGE_ID")
	private Stage stage;
	
	@Column(name="CALLER_MOBILE_NUMBER")
	private String callerMobileNumber;
	
	@Column(name="CALLER_PHONE_NUMBER")
	private String callerLandlineNumber;
	
	@Column(name="ATTENDERS_MOBILE_NUMBER")
	private String attendersMobileNumber;
	
	@Column(name="ATTENDERS_PHONE_NUMBER")
	private String attendersLandlineNumber;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="POLICY_KEY", nullable = false,updatable=false)
	private Policy policy;
	
	@Column(name="POLICY_YEAR")
	private String policyYear;
	
    @Transient
	private String policyNumber;
    
    @Transient
	private String insuredPatientName;
    
    @Column(name="REGISTRATION_TYPE")
    private String registrationType;
    
	@Column(name="REGISTRATION_STATUS")
    private String registrationStatus; 
	
	@JoinColumn(name = "INSURED_KEY", nullable = false, unique = true, insertable = true)
	@OneToOne(fetch = FetchType.LAZY)
	private Insured insured;
    
	@ManyToOne(fetch=FetchType.EAGER )
	@JoinColumn(name="LOB_ID",  nullable=true)
	private MastersValue lobId;
	
	@Column(name = "PROCESS_CLM_TYPE")// PA/health
	private String processClaimType;
	
	@Column(name = "INCIDENCE_FLAG") //Accident/Death
	private String incidenceFlag;
	
	@Column(name = "HOS_REQ_FLAG") // Y/N
	private String hospitalReqFlag;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ACC_DEA_DATE", insertable = false, updatable=false)
	private Date accidentDeathDate;
	
	@Column(name = "PA_CATEGORY")
	private String paCategory;
	
	@Column(name = "PA_PATIENT_NAME")
	private String paPatientName;
	
	@Column(name = "PA_PARENT_NAME")
	private String paParentName;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PA_PARENT_DOB")
	private Date paParentDOB;
	
	@Column(name = "PA_PARENT_AGE")
	private Double paParentAge;
	
	@Column(name = "UN_NAMED_KEY")
	private Long unNamedKey;
	
	@Column(name = "ORIGINAL_CPU_CODE")
	private Long originalCpuCode;
	
	@Column(name = "CALLER_ADDRESS")
	private String callerAddress;
	
	@Column(name = "CALLER_EMAIL")
	private String callerEmail;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_OF_DISCHARGE")
	private Date dateOfDischarge;
	
	@Column(name = "INSURED_TYPE")
	private String insuredType;
	
	/*@Column(name="COMMENTS")
	private String comments;
	
	@Column(name="DES_APPLICATION")
	private String applicationFlag;*/
	@Column(name = "ALLOW_APPROVAL_FLAG")
	private String allowApprovalFlag;
	
	@Column(name = "ALLOW_APPROVAL_COMMENTS")
	private String allowApprovalComments;
	
	@Column(name = "EMERGENCY_FLAG")
	private String emergencyFlag;
	
	@Column(name = "ACCIDENT_FLAG")
	private String accidentFlag;
	
	@Column(name = "EMERGENCY_ACCIDENT_REMARKS")
	private String remarksForEmergencyAccident;
	
	@Column(name = "DOCTOR_CONTACT_NO")
	private Long doctorNo;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="TREATMENT_TYPE_ID", nullable=true)
	private MastersValue treatmentTypeId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="MODE_OF_RECEIPT_ID", nullable=true)
	private MastersValue modeOfReceiptId;
	
	@Column(name="AGGREGATOR_CODE")
	private String aggregatorCode;
	
	@Column(name="SERVICE_TYPE")
	private String serviceType;
	
	@Column(name="CLAIM_TYPE")
	private Long claimTypeKey;
	
	@Column(name="AGGREGATOR_BOOKING_ID")
	private String aggregatorBookingId;
	
	@Column(name="REQUESTED_AMOUNT")
	private Double requestedAmount;
	
	@Column(name="SERVICE_CATEGORY")
	private String serviceCategory;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="BOOKING_DATE")
	private Date bookingDate;
	
	@Column(name="SERVICE_PROVIDER_NAME")
	private String serviceProviderName;
	
	@Column(name="SERVICE_PROVIDER_ADDRESS")
	private String serviceProviderAddress;
	
    public Insured getInsured() {
		return insured;
	}

	public void setInsured(Insured insured) {
		this.insured = insured;
	}

	public Long getHospital() {
		return hospital;
	}

	public void setHospital(Long hospital) {
		this.hospital = hospital;
	}
	
	public OPIntimation() {
		policy = new Policy();
		cpuCode = new TmpCPUCode();
	}

	public String getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Date getAdmissionDate() {
		return this.admissionDate;
	}

	public MastersValue getAdmissionType() {
		return admissionType;
	}

	public void setAdmissionType(MastersValue admissionType) {
		this.admissionType = admissionType;
	}

	public void setAdmissionDate(Date admissionDate) {
		this.admissionDate = admissionDate;
	}

	public String getAdmissionReason() {
		return this.admissionReason;
	}

	public void setAdmissionReason(String admissionReason) {
		this.admissionReason = admissionReason;
	}

	public TmpCPUCode getCpuCode() {
		return this.cpuCode;
	}

	public void setCpuCode(TmpCPUCode cpuCode) {
		this.cpuCode = cpuCode;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getDoctorName() {
		return this.doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	
	public String getHospitalComments() {
		return this.hospitalComments;
	}

	public void setHospitalComments(String hospitalComments) {
		this.hospitalComments = hospitalComments;
	}

	public MastersValue getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(MastersValue hospitalType) {
		this.hospitalType = hospitalType;
	}

	public String getInpatientNumber() {
		return this.inpatientNumber;
	}

	public void setInpatientNumber(String inpatientNumber) {
		this.inpatientNumber = inpatientNumber;
	}

	public MastersValue getIntimatedBy() {
		return this.intimatedBy;
	}

	public String getIntimationId() {
		return this.intimationId;
	}

	public void setIntimationId(String intimationId) {
		this.intimationId = intimationId;
	}

	public MastersValue getIntimationMode() {
		return this.intimationMode;
	}

	public Long getKey() {
		return this.key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getOfficeCode() {
		return this.officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public Boolean getPatientNotCovered() {
		return this.patientNotCovered;
	}

	public void setPatientNotCovered(Boolean patientNotCovered) {
		this.patientNotCovered = patientNotCovered;
	}

	public MastersValue getRoomCategory() {
		return this.roomCategory;
	}

	public Status getStatus() {
		return this.status;
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

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	public String getPolicyNumber() {
		return this.policy.getPolicyNumber();
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	/*public void setAdmissionDate(Timestamp admissionDate) {
		this.admissionDate = admissionDate;
	}*/

	public String getIntimaterName() {
		return this.intimaterName;
	}
	
	public void setIntimaterName(String intimaterName) {
		this.intimaterName = intimaterName;
	}

	public MastersValue getManagementType() {
		return managementType;
	}

	public void setManagementType(MastersValue managementType) {
		this.managementType = managementType;
	}
	
	public String getInsuredPatientName() {
		return this.insured.getInsuredName();
	//	return this.policy.getInsuredFirstName() + " " + this.policy.getInsuredLastName();
	}

	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}

	public void setIntimatedBy(MastersValue intimatedBy) {
		this.intimatedBy = intimatedBy;
	}

	public void setIntimationMode(MastersValue intimationMode) {
		this.intimationMode = intimationMode;
	}

	public void setRoomCategory(MastersValue roomCategory) {
		this.roomCategory = roomCategory;
	}

	public MastersValue getIntimationSource() {
		return intimationSource;
	}

	public void setIntimationSource(MastersValue intimationSource) {
		this.intimationSource = intimationSource;
	}
		
	public String getLateIntimationReason() {
		return lateIntimationReason;
	}

	public void setLateIntimationReason(String lateIntimationReason) {
		this.lateIntimationReason = lateIntimationReason;
	}

	public String getCallerMobileNumber() {
		return callerMobileNumber;
	}

	public void setCallerMobileNumber(String callerMobileNumber) {
		this.callerMobileNumber = callerMobileNumber;
	}

	public String getCallerLandlineNumber() {
		return callerLandlineNumber;
	}

	public void setCallerLandlineNumber(String callerLandlineNumber) {
		this.callerLandlineNumber = callerLandlineNumber;
	}

	public String getAttendersMobileNumber() {
		return attendersMobileNumber;
	}

	public void setAttendersMobileNumber(String attendersMobileNumber) {
		this.attendersMobileNumber = attendersMobileNumber;
	}

	public String getAttendersLandlineNumber() {
		return attendersLandlineNumber;
	}

	public void setAttendersLandlineNumber(String attendersLandlineNumber) {
		this.attendersLandlineNumber = attendersLandlineNumber;
	}

	public String getRelapseofIllness() {
		return relapseofIllness;
	}

	public void setRelapseofIllness(String relapseofIllness) {
		this.relapseofIllness = relapseofIllness;
	}
	
	public String getDummy() {
		return dummy;
	}

	public void setDummy(String dummy) {
		this.dummy = dummy;
	}

	public String getCallerAddress() {
		return callerAddress;
	}

	public void setCallerAddress(String callerAddress) {
		this.callerAddress = callerAddress;
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

	public MastersValue getClaimType() {
		return claimType;
	}

	public void setClaimType(MastersValue claimType) {
		this.claimType = claimType;
	}

	public String getPolicyYear() {
		return policyYear;
	}

	public void setPolicyYear(String policyYear) {
		this.policyYear = policyYear;
	}

	public String getHospitalReqFlag() {
		return hospitalReqFlag;
	}

	public void setHospitalReqFlag(String hospitalReqFlag) {
		this.hospitalReqFlag = hospitalReqFlag;
	}

	public Date getAccidentDeathDate() {
		return accidentDeathDate;
	}

	public void setAccidentDeathDate(Date accidentDeathDate) {
		this.accidentDeathDate = accidentDeathDate;
	}

	public String getPaCategory() {
		return paCategory;
	}

	public void setPaCategory(String paCategory) {
		this.paCategory = paCategory;
	}

	public String getPaPatientName() {
		return paPatientName;
	}

	public void setPaPatientName(String paPatientName) {
		this.paPatientName = paPatientName;
	}
	
	public String getPaParentName() {
		return paParentName;
	}

	public void setPaParentName(String paParentName) {
		this.paParentName = paParentName;
	}

	public Date getPaParentDOB() {
		return paParentDOB;
	}

	public void setPaParentDOB(Date paParentDOB) {
		this.paParentDOB = paParentDOB;
	}

	public Double getPaParentAge() {
		return paParentAge;
	}

	public void setPaParentAge(Double paParentAge) {
		this.paParentAge = paParentAge;
	}

	public Long getUnNamedKey() {
		return unNamedKey;
	}

	public void setUnNamedKey(Long unNamedKey) {
		this.unNamedKey = unNamedKey;
	}
	public MastersValue getLobId() {
		return lobId;
	}

	public void setLobId(MastersValue lobId) {
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

	public Long getOriginalCpuCode() {
		return originalCpuCode;
	}

	public void setOriginalCpuCode(Long originalCpuCode) {
		this.originalCpuCode = originalCpuCode;
	}

	public String getCallerEmail() {
		return callerEmail;
	}

	public void setCallerEmail(String callerEmail) {
		this.callerEmail = callerEmail;
	}

	public Date getDateOfDischarge() {
		return dateOfDischarge;
	}

	public void setDateOfDischarge(Date dateOfDischarge) {
		this.dateOfDischarge = dateOfDischarge;
	}

	public String getInsuredType() {
		return insuredType;
	}

	public void setInsuredType(String insuredType) {
		this.insuredType = insuredType;
	}


	public String getAllowApprovalFlag() {
		return allowApprovalFlag;
	}

	public void setAllowApprovalFlag(String allowApprovalFlag) {
		this.allowApprovalFlag = allowApprovalFlag;
	}

	public String getAllowApprovalComments() {
		return allowApprovalComments;
	}

	public void setAllowApprovalComments(String allowApprovalComments) {
		this.allowApprovalComments = allowApprovalComments;
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

	public MastersValue getTreatmentTypeId() {
		return treatmentTypeId;
	}

	public void setTreatmentTypeId(MastersValue treatmentTypeId) {
		this.treatmentTypeId = treatmentTypeId;
	}

	public MastersValue getModeOfReceiptId() {
		return modeOfReceiptId;
	}

	public void setModeOfReceiptId(MastersValue modeOfReceiptId) {
		this.modeOfReceiptId = modeOfReceiptId;
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

	public String getAggregatorBookingId() {
		return aggregatorBookingId;
	}

	public void setAggregatorBookingId(String aggregatorBookingId) {
		this.aggregatorBookingId = aggregatorBookingId;
	}

	public Double getRequestedAmount() {
		return requestedAmount;
	}

	public void setRequestedAmount(Double requestedAmount) {
		this.requestedAmount = requestedAmount;
	}

	public String getServiceCategory() {
		return serviceCategory;
	}

	public void setServiceCategory(String serviceCategory) {
		this.serviceCategory = serviceCategory;
	}

	public Date getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}

	public String getServiceProviderName() {
		return serviceProviderName;
	}

	public void setServiceProviderName(String serviceProviderName) {
		this.serviceProviderName = serviceProviderName;
	}

	public String getServiceProviderAddress() {
		return serviceProviderAddress;
	}

	public void setServiceProviderAddress(String serviceProviderAddress) {
		this.serviceProviderAddress = serviceProviderAddress;
	}

	@Override
	public String toString() {
		System.out.println("----------------------- " + this.policy.getKey() + " : " + this.policy.getPolicyNumber());
		return super.toString();
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getKey() == null) {
            return obj == this;
        } else {
            return getKey().equals(((OPIntimation) obj).getKey());
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
	
	

}