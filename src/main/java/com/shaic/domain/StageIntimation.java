package com.shaic.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="IMS_CLS_STAGE_INTIMATION")
@NamedQueries({
@NamedQuery(name="StageIntimation.findAll", query="SELECT c FROM StageIntimation c"),
})
public class StageIntimation {
	
	@Id
	@SequenceGenerator(name="IMS_CLS_STAGE_INTIMATION_KEY_GENERATOR", sequenceName = "SEQ_STAGE_INTIMATION_KEY" , allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_STAGE_INTIMATION_KEY_GENERATOR" )
	@Column(name="STAGE_INTIMATION_KEY")
	private Long key;

	@Column(name="INSURED_NAME")
	private String insuredName;
	
	@Column(name="INTIMATION_MODE")
	private String intimationMode;
	
	@Column(name="INTIMATED_BY")
	private String intimatedBy;
	
	@Column(name="INTIMATOR_NAME")
	private String intimatorName;
	
	@Column(name="INTIMATOR_CONTACT_NO")
	private String intimatorContactNo;
	
	@Column(name="POL_NO")
	private String polNo;
	
	@Column(name="INTIMATION_NO")
	private String intimationNo;
	
	@Column(name="HOSP_CODE")
	private String hospCode;
	
	@Column(name="HOSPITAL_TYPE_YN")
	private String hospitalTypeYn;
	
	@Column(name="SHOSP_COMMENTS")
	private String hospComments;
	
	@Column(name="ADMISSION_TYPE")
	private String admissionType;
	
	@Column(name="MANAGEMENT_TYPE")
	private String managementType;
	
	@Column(name="REASONFORADMISSION")
	private String reasonForAdmission;
	
	@Temporal(TemporalType.DATE)
	@Column(name="ADMITTED")
	private Date admitted;
	
	@Column(name="ROOM_CATEGORY")
	private String roomCategory;
	
	@Column(name="SAVED_TYPE")
	private String savedType;
	
	@Column(name="CLM_PROC_DIVN")
	private String clmProcDivn;
	
	@Column(name="PATIENT_NAME_YN")
	private String patientNameYn;
	
	@Column(name="INPATIENT_NO")
	private String inpatientNo;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_ON")
	private Date createdOn;
	
	@Column(name="ATT_MOB_NO")
	private String attMobNo;
	
	@Column(name="POLICY_YR")
	private String policyYr;
	
	@Column(name="ACC_DEATH_YN")
	private String accDeathYn;
	
	@Column(name="HOSP_REQU_YN")
	private String hospRequYn;
	
	@Column(name="PA_CATEGORY")
	private String paCategory;
	
	@Column(name="PA_PATIENTNAME")
	private String paPatientName;
	
	@Column(name="PA_PARENTNAME")
	private String paParentName;
	
	@Column(name="PA_PARENTDOB")
	private String paParentDob;
	
	@Column(name="PA_PARENTAGE")
	private String paParentAge;
	
	@Column(name="PROD_CODE")
	private String prodCode;
	
	@Column(name="PA_PATIENTDOB")
	private String paPatientDob;
	
	@Column(name="PA_PATIENTAGE")
	private String paPatientAge;
	
	@Column(name="SUSPICIOUS_MULTIPLE")
	private String suspiciousMultiple;
	
	@Column(name="ADMITTED_TIME")
	private String admittedTime;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DISCHARGE_DATE")
	private Date dischargeDate;
	
	@Column(name="DISCHARGE_TIME")
	private String dischargeTime;
	
	@Column(name="GALAXY_READ_YN")
	private String galaxyReadYn;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="GALAXY_READ_DT")
	private Date galaxyReadDt;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
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

	public String getPolNo() {
		return polNo;
	}

	public void setPolNo(String polNo) {
		this.polNo = polNo;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getHospCode() {
		return hospCode;
	}

	public void setHospCode(String hospCode) {
		this.hospCode = hospCode;
	}

	public String getHospitalTypeYn() {
		return hospitalTypeYn;
	}

	public void setHospitalTypeYn(String hospitalTypeYn) {
		this.hospitalTypeYn = hospitalTypeYn;
	}

	public String getHospComments() {
		return hospComments;
	}

	public void setHospComments(String hospComments) {
		this.hospComments = hospComments;
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

	public Date getAdmitted() {
		return admitted;
	}

	public void setAdmitted(Date admitted) {
		this.admitted = admitted;
	}

	public String getRoomCategory() {
		return roomCategory;
	}

	public void setRoomCategory(String roomCategory) {
		this.roomCategory = roomCategory;
	}

	public String getSavedType() {
		return savedType;
	}

	public void setSavedType(String savedType) {
		this.savedType = savedType;
	}

	public String getClmProcDivn() {
		return clmProcDivn;
	}

	public void setClmProcDivn(String clmProcDivn) {
		this.clmProcDivn = clmProcDivn;
	}

	public String getPatientNameYn() {
		return patientNameYn;
	}

	public void setPatientNameYn(String patientNameYn) {
		this.patientNameYn = patientNameYn;
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

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getAttMobNo() {
		return attMobNo;
	}

	public void setAttMobNo(String attMobNo) {
		this.attMobNo = attMobNo;
	}

	public String getPolicyYr() {
		return policyYr;
	}

	public void setPolicyYr(String policyYr) {
		this.policyYr = policyYr;
	}

	public String getAccDeathYn() {
		return accDeathYn;
	}

	public void setAccDeathYn(String accDeathYn) {
		this.accDeathYn = accDeathYn;
	}

	public String getHospRequYn() {
		return hospRequYn;
	}

	public void setHospRequYn(String hospRequYn) {
		this.hospRequYn = hospRequYn;
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

	public String getPaParentAge() {
		return paParentAge;
	}

	public void setPaParentAge(String paParentAge) {
		this.paParentAge = paParentAge;
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

	public String getSuspiciousMultiple() {
		return suspiciousMultiple;
	}

	public void setSuspiciousMultiple(String suspiciousMultiple) {
		this.suspiciousMultiple = suspiciousMultiple;
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

	public String getGalaxyReadYn() {
		return galaxyReadYn;
	}

	public void setGalaxyReadYn(String galaxyReadYn) {
		this.galaxyReadYn = galaxyReadYn;
	}

	public Date getGalaxyReadDt() {
		return galaxyReadDt;
	}

	public void setGalaxyReadDt(Date galaxyReadDt) {
		this.galaxyReadDt = galaxyReadDt;
	}

	@Override
	public String toString() {
		return "StageIntimation [key=" + key + ", insuredName=" + insuredName
				+ ", intimationMode=" + intimationMode + ", intimatedBy="
				+ intimatedBy + ", intimatorName=" + intimatorName
				+ ", intimatorContactNo=" + intimatorContactNo + ", polNo="
				+ polNo + ", intimationNo=" + intimationNo + ", hospCode="
				+ hospCode + ", hospitalTypeYn=" + hospitalTypeYn
				+ ", hospComments=" + hospComments + ", admissionType="
				+ admissionType + ", managementType=" + managementType
				+ ", reasonForAdmission=" + reasonForAdmission + ", admitted="
				+ admitted + ", roomCategory=" + roomCategory + ", savedType="
				+ savedType + ", clmProcDivn=" + clmProcDivn
				+ ", patientNameYn=" + patientNameYn + ", inpatientNo="
				+ inpatientNo + ", createdBy=" + createdBy + ", createdOn="
				+ createdOn + ", attMobNo=" + attMobNo + ", policyYr="
				+ policyYr + ", accDeathYn=" + accDeathYn + ", hospRequYn="
				+ hospRequYn + ", paCategory=" + paCategory
				+ ", paPatientName=" + paPatientName + ", paParentName="
				+ paParentName + ", paParentDob=" + paParentDob
				+ ", paParentAge=" + paParentAge + ", prodCode=" + prodCode
				+ ", paPatientDob=" + paPatientDob + ", paPatientAge="
				+ paPatientAge + ", suspiciousMultiple=" + suspiciousMultiple
				+ ", admittedTime=" + admittedTime + ", dischargeDate="
				+ dischargeDate + ", dischargeTime=" + dischargeTime
				+ ", galaxyReadYn=" + galaxyReadYn + ", galaxyReadDt="
				+ galaxyReadDt + "]";
	}
	
}

