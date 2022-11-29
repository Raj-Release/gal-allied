package com.shaic.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name="VW_INTIMATION_TMP")
@NamedQueries({
	@NamedQuery(name="ViewTmpIntimation.findAll", query="SELECT o FROM ViewTmpIntimation o"),
	@NamedQuery(name="ViewTmpIntimation.findByKey", query="SELECT o FROM ViewTmpIntimation o where o.key = :intiationKey"),
	@NamedQuery(name="ViewTmpIntimation.findByAllCombi", query="SELECT o FROM ViewTmpIntimation o where (:intimStatus is null or(o.status is not null and o.status.processValue like :intimStatus))"
			+ "and (:policyNo is null or o.policyNumber like :policyNo)"
			+ "and (:insuredName is null or o.insured.insuredName like :insuredName)"
			+ "and (:intimationNo is null or o.intimationId like :intimationNo)"), 
	//and (:healthCardNo is null or o.policy.healthCardNumber like :healthCardNo) and (:hospitalName is null or o.hospital.value like :hospitalName))
	@NamedQuery(name="ViewTmpIntimation.findByPolicy", query="SELECT o FROM ViewTmpIntimation o where o.policyNumber like :policyNo"),
	@NamedQuery(name="ViewTmpIntimation.findByStatus", query="SELECT o FROM ViewTmpIntimation o where o.status is not null and o.status.processValue like :intimStatus"),
	@NamedQuery(name="ViewTmpIntimation.findByIntimationNumber", query="SELECT o FROM ViewTmpIntimation o where o is not null and o.intimationId = :intimationNo"),
	@NamedQuery(name="ViewTmpIntimation.findByPolicyKey", query="SELECT o.key FROM ViewTmpIntimation o where o.policy = :policyKey"),
	@NamedQuery(name="ViewTmpIntimation.findByCreatedDate", query="SELECT o FROM ViewTmpIntimation o where o.createdDate between :startDate and :endDate"),
	@NamedQuery(name="ViewTmpIntimation.findByHealthCard", query="SELECT o FROM ViewTmpIntimation o where o.insured.healthCardNumber like :healthCardNo"),
	@NamedQuery(name ="ViewTmpIntimation.findByHealthCardNo",query = "SELECT o FROM ViewTmpIntimation o where o.intimationId = :intimationId and o.insured.healthCardNumber = :healthCardNo"),
	@NamedQuery(name="ViewTmpIntimation.findByInsuredName", query="SELECT o FROM ViewTmpIntimation o where o.insured.insuredName like :insuredName"),
	@NamedQuery(name="ViewTmpIntimation.findByInsuredId", query="SELECT o FROM ViewTmpIntimation o where o.insured.insuredId =:insuredId"),
	@NamedQuery(name="ViewTmpIntimation.findDuplicateInitmation", query="SELECT o FROM ViewTmpIntimation o where o.hospital = :hospital and o.policyNumber = :policy and o.admissionDate = :admissionDate and o.insured.insuredId = :insuredId and o.status is not null and o.status.processValue like 'Submitted'"),
	@NamedQuery(name = "ViewTmpIntimation.findEmpSummary", query = "SELECT i.createdBy, count(i.key) FROM ViewTmpIntimation i WHERE Lower(i.createdBy) like :createdBy and i.intimationSource.key = :empSourceKey and i.createdDate >= :fromDate and i.createdDate <= :toDate group by i.createdBy")
})

public class ViewTmpIntimation extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
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
	@Column(name="CREATED_DATE", insertable = false, updatable=false)
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

	@Temporal(TemporalType.DATE)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;

	@Column(name="OFFICE_CODE")
	private String officeCode;

	@Column(nullable = true, columnDefinition = "NUMBER", name="PATIENT_NOT_COVERED", length = 1)
	private Boolean patientNotCovered;

	@Column(nullable = true, columnDefinition = "VARCHAR", name="RELAPSE_OF_ILLNESS", length = 1)
	private String relapseofIllness;
	
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

	/*@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="POLICY_KEY", nullable = false,updatable=false)*/
	@Column(name="POLICY_KEY")
	private Long policy;
	
	 @Column(name="POLICY_NUMBER")
	private String policyNumber;
    
    @Transient
	private String insuredPatientName;
    
    @Column(name="REGISTRATION_TYPE")
    private String registrationType;
    
	@Column(name="REGISTRATION_STATUS")
    private String registrationStatus; 
	
	@JoinColumn(name = "INSURED_KEY", nullable = false, unique = true, insertable = true)
	@OneToOne
	private Insured insured; 
	
	@Column(name = "RECORD_FLAG")
	private String recordFlag;

	@Column(name = "PROCESS_CLM_TYPE")// PA/health
	private String processClaimType;
	
	@Column(name = "INCIDENCE_FLAG") //Accident/Death
	private String incidenceFlag;
	
	@Column(name="UNDERWRITING_YEAR")
	private String policyYear;

	
	@Transient
	private String dmValue;
    
    
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

	
//	public ViewTmpIntimation() {
//		policy = new Policy();
//		cpuCode = new TmpCPUCode();
//	}

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

	public void setCreatedDate(Timestamp createdDate) {
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

	public String getDmValue() {
		return dmValue;
	}

	public void setDmValue(String dmValue) {
		this.dmValue = dmValue;
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

	public void setAdmissionDate(Timestamp admissionDate) {
		this.admissionDate = admissionDate;
	}

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

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getRelapseofIllness() {
		return relapseofIllness;
	}

	public void setRelapseofIllness(String relapseofIllness) {
		this.relapseofIllness = relapseofIllness;
	}
	

	@Override
	public String toString() {
		//System.out.println("----------------------- " + this.policy.getKey() + " : " + this.policy.getPolicyNumber());
		return super.toString();
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getKey() == null) {
            return obj == this;
        } else {
            return getKey().equals(((ViewTmpIntimation) obj).getKey());
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

	public String getRecordFlag() {
		return recordFlag;
	}

	public void setRecordFlag(String recordFlag) {
		this.recordFlag = recordFlag;
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

	public Long getPolicy() {
		return policy;
	}

	public void setPolicy(Long policy) {
		this.policy = policy;
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

	
	
}