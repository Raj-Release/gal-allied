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
@Table(name="IMS_CRM_INTIMATION")
@NamedQueries({
	@NamedQuery(name="GalaxyCRMIntimation.findAll", query="SELECT o FROM GalaxyCRMIntimation o"),
	@NamedQuery(name="GalaxyCRMIntimation.findByIntimationNumber", query="SELECT o FROM GalaxyCRMIntimation o where o is not null and o.intimationNumber = :intimationNo"),
	@NamedQuery(name="GalaxyCRMIntimation.findByIntimationNoWithLike", query="SELECT o FROM GalaxyCRMIntimation o where o is not null and o.intimationNumber like :intimationNo"),
	@NamedQuery(name="GalaxyCRMIntimation.findByPolicyNumber", query="SELECT o FROM GalaxyCRMIntimation o where o is not null and o.policyNo = :policyNumber and o.dummyHospFlag='Y'"),
	@NamedQuery(name="GalaxyCRMIntimation.findByPolicyNumberAndRiskId", query="SELECT o FROM GalaxyCRMIntimation o where o is not null and o.policyNo = :policyNumber and o.insuredNumber = :riskId and o.dummyHospFlag='Y'"),
})

public class GalaxyCRMIntimation extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_GLX_INTIMATION_KEY_GENERATOR", sequenceName = "SEQ_GLX_INTIMATION_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_GLX_INTIMATION_KEY_GENERATOR" ) 
	@Column(name="INTIMATION_KEY")
	private Long key;
	@Column(name="INTIMATION_NUMBER")
	private String intimationNumber;   
	@Column(name="POLICY_KEY")
	private Long policyKey;   
	@Column(name="INSURED_ID")
	private Long insuredId;   
	@Column(name="INSURED_NUMBER")
	private String insuredNumber;   
	@Column(name="INTIMATION_MODE")
	private String intimationMode;   
	@Column(name="INTIMATED_BY")
	private String intimatedBy;   
	@Column(name="INTIMATOR_NAME")
	private String intimatorName;   
	@Column(name="INTIMATOR_CONTACT_NO")
	private String intimatorContactNo;   
	@Column(name="POLICY_NO")
	private String policyNo;   
	@Column(name="HOSP_CODE")
	private String hospitalCode;   
	@Column(name="HOSP_NAME")
	private String hospitalName;   
	@Column(name="HOSP_ADDRESS")
	private String hospitalAddress;   
	@Column(name="HOSP_CITY")
	private String hospitalCity;   
	@Column(name="HOSP_STATE")
	private String hospitalState;   
	@Column(name="HOSP_CONTACT_NO")
	private String hospitalContactNo;   
	@Column(name="HOSPFAXNO")
	private String hospitalFaxNo;   
	@Column(name="ADMISSION_TYPE")
	private String admissionType;   
	@Column(name="MANAGEMENT_TYPE")
	private String managementType;   
	@Column(name="REASON_FOR_ADMISSION")
	private String reasonForAdmission;   
	@Column(name="ADMITTED_DATE")
	private Date admittedDate;   
	@Column(name="ROOM_CATEGORY")
	private String roomCategory;   
	@Column(name="INPATIENT_NO")
	private String inpatientNo;   
	@Column(name="CREATED_BY")
	private String createdBy;   
	@Column(name="ATTENDER_MOB_NO")
	private String attenderMobileNo;   
	@Column(name="POLICY_YEAR")
	private String policyYear;   
	@Column(name="ACC_DEATH_YN")
	private String accDeath;   
	@Column(name="HOSP_REQU_YN")
	private String hospitalRequired;   
	@Column(name="PA_CATEGORY")
	private String paCategory;   
	@Column(name="PA_PATIENT_NAME")
	private String paPatientName;   
	@Column(name="PA_PARENT_NAME")
	private String paParentName;   
	@Column(name="PA_PARENT_DOB")
	private String paParentDob;   
	@Column(name="PA_PARENT_AGE")
	private String age;   
	@Column(name="PROD_CODE")
	private String prodCode;   
	@Column(name="PA_PATIENT_DOB")
	private String paPatientDob;   
	@Column(name="PA_PATIENT_AGE")
	private String paPatientAge;   
	@Column(name="ADMITTED_TIME")
	private String admittedTime;   
	@Column(name="DISCHARGE_DATE")
	private Date dischargeDate ;   
	@Column(name="DISCHARGE_TIME")
	private String dischargeTime;   
	@Column(name="INSURED_PATIENT_NAME")
	private String insuredPatientName;   
	@Column(name="INSURED_EMAIL")
	private String insuredEmail;   
	@Column(name="HOSPITAL_TYPE")
	private String hospitalType;   
	@Column(name="HOSPITAL_DOCTOR_NAME")
	private String hospitalDoctorName;   
	@Column(name="COMMENTS")
	private String Comments;   
	@Column(name="SUSPICIOUSCOMMENTS")
	private String suspiciousComments;   
	@Column(name="POLICY_SOURCE")
	private String policySource;   
	@Column(name="PIO_CODE")
	private String pioCode;   
	@Column(name="CREATED_DATE")
	private Date createdDate;   
	@Column(name="CPU_CODE")
	private Long cpuCode;
	@Column(name="INTM_FLAG")
	private String intmFlag;
	@Column(name="SOURCE")
	private String source;
	
	@Column(name="DUMMY_FLAG")
	private String dummyHospFlag;
	
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	public String getIntimationNumber() {
		return intimationNumber;
	}
	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}
	public Long getPolicyKey() {
		return policyKey;
	}
	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}
	public Long getInsuredId() {
		return insuredId;
	}
	public void setInsuredId(Long insuredId) {
		this.insuredId = insuredId;
	}
	public String getInsuredNumber() {
		return insuredNumber;
	}
	public void setInsuredNumber(String insuredNumber) {
		this.insuredNumber = insuredNumber;
	}
	public String getIntimationMode() {
		return intimationMode;
	}
	public void setIntimationMode(String intimationMode) {
		this.intimationMode = intimationMode;
	}
	public String getIntimatedBy() {
		return intimatedBy;
	}
	public void setIntimatedBy(String intimatedBy) {
		this.intimatedBy = intimatedBy;
	}
	public String getIntimatorName() {
		return intimatorName;
	}
	public void setIntimatorName(String intimatorName) {
		this.intimatorName = intimatorName;
	}
	public String getIntimatorContactNo() {
		return intimatorContactNo;
	}
	public void setIntimatorContactNo(String intimatorContactNo) {
		this.intimatorContactNo = intimatorContactNo;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
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
	public String getHospitalAddress() {
		return hospitalAddress;
	}
	public void setHospitalAddress(String hospitalAddress) {
		this.hospitalAddress = hospitalAddress;
	}
	public String getHospitalCity() {
		return hospitalCity;
	}
	public void setHospitalCity(String hospitalCity) {
		this.hospitalCity = hospitalCity;
	}
	public String getHospitalState() {
		return hospitalState;
	}
	public void setHospitalState(String hospitalState) {
		this.hospitalState = hospitalState;
	}
	public String getHospitalContactNo() {
		return hospitalContactNo;
	}
	public void setHospitalContactNo(String hospitalContactNo) {
		this.hospitalContactNo = hospitalContactNo;
	}
	public String getHospitalFaxNo() {
		return hospitalFaxNo;
	}
	public void setHospitalFaxNo(String hospitalFaxNo) {
		this.hospitalFaxNo = hospitalFaxNo;
	}
	public String getAdmissionType() {
		return admissionType;
	}
	public void setAdmissionType(String admissionType) {
		this.admissionType = admissionType;
	}
	public String getManagementType() {
		return managementType;
	}
	public void setManagementType(String managementType) {
		this.managementType = managementType;
	}
	public String getReasonForAdmission() {
		return reasonForAdmission;
	}
	public void setReasonForAdmission(String reasonForAdmission) {
		this.reasonForAdmission = reasonForAdmission;
	}
	public Date getAdmittedDate() {
		return admittedDate;
	}
	public void setAdmittedDate(Date admittedDate) {
		this.admittedDate = admittedDate;
	}
	public String getRoomCategory() {
		return roomCategory;
	}
	public void setRoomCategory(String roomCategory) {
		this.roomCategory = roomCategory;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getAttenderMobileNo() {
		return attenderMobileNo;
	}
	public void setAttenderMobileNo(String attenderMobileNo) {
		this.attenderMobileNo = attenderMobileNo;
	}
	public String getPolicyYear() {
		return policyYear;
	}
	public void setPolicyYear(String policyYear) {
		this.policyYear = policyYear;
	}
	public String getAccDeath() {
		return accDeath;
	}
	public void setAccDeath(String accDeath) {
		this.accDeath = accDeath;
	}
	public String getHospitalRequired() {
		return hospitalRequired;
	}
	public void setHospitalRequired(String hospitalRequired) {
		this.hospitalRequired = hospitalRequired;
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
	public String getPaParentDob() {
		return paParentDob;
	}
	public void setPaParentDob(String paParentDob) {
		this.paParentDob = paParentDob;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getProdCode() {
		return prodCode;
	}
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}
	public String getPaPatientDob() {
		return paPatientDob;
	}
	public void setPaPatientDob(String paPatientDob) {
		this.paPatientDob = paPatientDob;
	}
	public String getPaPatientAge() {
		return paPatientAge;
	}
	public void setPaPatientAge(String paPatientAge) {
		this.paPatientAge = paPatientAge;
	}
	public String getAdmittedTime() {
		return admittedTime;
	}
	public void setAdmittedTime(String admittedTime) {
		this.admittedTime = admittedTime;
	}
	public Date getDischargeDate() {
		return dischargeDate;
	}
	public void setDischargeDate(Date dischargeDate) {
		this.dischargeDate = dischargeDate;
	}
	public String getDischargeTime() {
		return dischargeTime;
	}
	public void setDischargeTime(String dischargeTime) {
		this.dischargeTime = dischargeTime;
	}
	public String getInsuredPatientName() {
		return insuredPatientName;
	}
	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}
	public String getInsuredEmail() {
		return insuredEmail;
	}
	public void setInsuredEmail(String insuredEmail) {
		this.insuredEmail = insuredEmail;
	}
	public String getHospitalType() {
		return hospitalType;
	}
	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}
	public String getHospitalDoctorName() {
		return hospitalDoctorName;
	}
	public void setHospitalDoctorName(String hospitalDoctorName) {
		this.hospitalDoctorName = hospitalDoctorName;
	}
	public String getComments() {
		return Comments;
	}
	public void setComments(String comments) {
		Comments = comments;
	}
	public String getSuspiciousComments() {
		return suspiciousComments;
	}
	public void setSuspiciousComments(String suspiciousComments) {
		this.suspiciousComments = suspiciousComments;
	}
	public String getPolicySource() {
		return policySource;
	}
	public void setPolicySource(String policySource) {
		this.policySource = policySource;
	}
	public String getPioCode() {
		return pioCode;
	}
	public void setPioCode(String pioCode) {
		this.pioCode = pioCode;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Long getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(Long cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getIntmFlag() {
		return intmFlag;
	}
	public void setIntmFlag(String intmFlag) {
		this.intmFlag = intmFlag;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDummyHospFlag() {
		return dummyHospFlag;
	}
	public void setDummyHospFlag(String dummyHospFlag) {
		this.dummyHospFlag = dummyHospFlag;
	}   
	
}