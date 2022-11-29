

/**
 * 
 */
package com.shaic.domain.bancs;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.shaic.arch.fields.dto.AbstractEntity;

/**
 * @author vijayar
 *
 */

@Entity
@Table(name="GLX_BaNCS_INTIMATION")
//@Table(name="IMS_CLS_STG_INTIMATION")
@NamedQueries({

	//@NamedQuery(name="PremiaIntimationTable.findAll", query="SELECT o FROM PremiaIntimationTable o where o.giSavedType <> :savedType and o.giHospitalTypeYn <> :hospType order by o.giCreatedOn desc "),
//	@NamedQuery(name="PremiaIntimationTable.findAll", query="SELECT o FROM PremiaIntimationTable o where o.giSavedType <> :savedType and o.giPACategory = :claimType order by o.giCreatedOn asc"),
	@NamedQuery(name="BaNCSIntimationTable.findAll", query="SELECT o FROM BaNCSIntimationTable o where o.giSavedType <> :savedType and o.giSavedType <> 'ERROR' and o.giHospitalTypeYn = '1' and o.productCode <> 'MED-PRD-047' order by o.giCreatedOn asc"),
	//@NamedQuery(name="PremiaIntimationTable.findAllGmc", query="SELECT o FROM PremiaIntimationTable o where o.giSavedType <> :savedType and o.giSavedType <> 'ERROR' and o.giHospitalTypeYn = '1' and o.productCode = 'MED-PRD-047'  order by o.giCreatedOn asc"),
	@NamedQuery(name="BaNCSIntimationTable.findByNonNetwork", query="SELECT o FROM BaNCSIntimationTable o where o.giSavedType <> :savedType and o.giSavedType <> 'ERROR' and o.giHospitalTypeYn = '0' order by o.giCreatedOn asc"),
	//@NamedQuery(name="PremiaIntimationTable.findByIntimationNumber", query="SELECT o FROM PremiaIntimationTable o where o.giIntimationNo = :intimationNumber"),
	//@NamedQuery(name="PremiaIntimationTable.findByIntimationId", query="SELECT o FROM PremiaIntimationTable o where o.giIntimationId = :intimationId")
	
})

public class BaNCSIntimationTable extends AbstractEntity implements Serializable {
	
	@Id
	@Column(name = "BANCS_INTIMATION_ID")
	private Long giIntimationId;
	
	
	@Column(name = "BANCS_INSURED_NAME")
	private String giInsuredName;
	
	@Column(name = "BANCS_INTIMATION_MODE")
	private String giIntimationMode;
	
	@Column(name = "BANCS_INTIMATED_BY")
	private String giIntimatedBy;
	
	@Column(name = "BANCS_INTIMATOR_NAME")
	private String giIntimatorName;
	
	@Column(name = "BANCS_INTIMATOR_CONTACT_NO")
	private String giIntimatorContactNo;
	
	@Column(name = "BANCS_POL_NO")
	private String giPolNo;
	
	@Column(name = "BANCS_INTIMATION_NO")
	private String giIntimationNo;
	
	@Column(name = "BANCS_HOSP_CODE")
	private String giHospCode;
	
	@Column(name = "BANCS_HOSPITAL_TYPE_YN")
	private String giHospitalTypeYn;
	
	@Column(name = "BANCS_SHOSP_COMMENTS")
	private String giSHospComments;
	
	@Column(name = "BANCS_ADMISSION_TYPE")
	private String giAdmissionType;
	
	@Column(name = "BANCS_MANAGEMENT_TYPE")
	private String giManagementType;
	
	@Column(name = "BANCS_REASONFORADMISSION")
	private String giReasonForAdmission;
	
	@Column(name = "BANCS_ADMITTED")
	private Date giAdmitted;
	
	@Column(name = "BANCS_ROOM_CATEGORY")
	private String giRoomCategory;
	
	@Column(name = "BANCS_SAVED_TYPE")
	private String giSavedType;
	
	@Column(name = "BANCS_CLM_PROC_DIVN")
	private String giClmProcDivn;
	
	@Column(name = "BANCS_PATIENT_NAME_YN")
	private String giPatientNameYn;
	
	@Column(name = "BANCS_INPATIENT_NO")
	private String giInpatientNo;
	
	@Column(name = "BANCS_CREATED_BY")
	private String giCreatedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BANCS_CREATED_ON")
	private Date giCreatedOn;
	
	@Column(name = "BANCS_POLICY_YR")
	private String giPolicyYear;
	
	@Column(name = "BANCS_ATT_MOB_NO")
	private String giAttenderMobileNumber;
	
	@Column(name = "BANCS_ACC_DEATH_YN")
//	@Transient
	private String giAccidentDeathFlag;
	
	
	@Column(name = "BANCS_HOSP_REQU_YN")
//	@Transient
	private String giHospitalRequiredFlag;
	
	@Column(name = "BANCS_PA_CATEGORY")
//	@Transient
	private String giPACategory;
	
	@Column(name = "BANCS_PA_PATIENTNAME")
//	@Transient
	private String giPAPatientName;
	
	@Column(name = "BANCS_PA_PARENTNAME")
//	@Transient
	private String giPAParentName;
	
	@Column(name = "BANCS_PA_PARENTDOB")
//	@Transient
	private String giPAParentDOB;
	
	@Column(name = "BANCS_PA_PARENTAGE")
//	@Transient
	private String giPAParentAge;
	
	@Column(name = "BANCS_PROD_CODE")
//	@Transient
	private String productCode;
	
	@Column(name = "BANCS_CALLER_ADDR")
//	@Transient
	private String callerAddress;
	
	//@Column(name = "GI_INTIMATIOR_MOBILE_NO")
	@Transient
	private String intimatorMobileNumber;
	
	//@Column(name = "GI_ATTENDERS_PHONE_NO")
	@Transient
	private String attenderPhoneNumber;
	
	@Override
	public Long getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setKey(Long key) {
		// TODO Auto-generated method stub
		
	}

	public Long getGiIntimationId() {
		return giIntimationId;
	}

	public void setGiIntimationId(Long giIntimationId) {
		this.giIntimationId = giIntimationId;
	}

	public String getGiInsuredName() {
		return giInsuredName;
	}

	public void setGiInsuredName(String giInsuredName) {
		this.giInsuredName = giInsuredName;
	}

	public String getGiIntimationMode() {
		return giIntimationMode;
	}

	public void setGiIntimationMode(String giIntimationMode) {
		this.giIntimationMode = giIntimationMode;
	}

	public String getGiIntimatedBy() {
		return giIntimatedBy;
	}

	public void setGiIntimatedBy(String giIntimatedBy) {
		this.giIntimatedBy = giIntimatedBy;
	}

	public String getGiIntimatorName() {
		return giIntimatorName;
	}

	public void setGiIntimatorName(String giIntimatorName) {
		this.giIntimatorName = giIntimatorName;
	}

	public String getGiIntimatorContactNo() {
		return giIntimatorContactNo;
	}

	public void setGiIntimatorContactNo(String giIntimatorContactNo) {
		this.giIntimatorContactNo = giIntimatorContactNo;
	}

	public String getGiPolNo() {
		return giPolNo;
	}

	public void setGiPolNo(String giPolNo) {
		this.giPolNo = giPolNo;
	}

	public String getGiIntimationNo() {
		return giIntimationNo;
	}

	public void setGiIntimationNo(String giIntimationNo) {
		this.giIntimationNo = giIntimationNo;
	}

	public String getGiHospCode() {
		return giHospCode;
	}

	public void setGiHospCode(String giHospCode) {
		this.giHospCode = giHospCode;
	}

	public String getGiHospitalTypeYn() {
		return giHospitalTypeYn;
	}

	public void setGiHospitalTypeYn(String giHospitalTypeYn) {
		this.giHospitalTypeYn = giHospitalTypeYn;
	}

	public String getGiSHospComments() {
		return giSHospComments;
	}

	public void setGiSHospComments(String giSHospComments) {
		this.giSHospComments = giSHospComments;
	}

	public String getGiAdmissionType() {
		return giAdmissionType;
	}

	public void setGiAdmissionType(String giAdmissionType) {
		this.giAdmissionType = giAdmissionType;
	}

	public String getGiManagementType() {
		return giManagementType;
	}

	public void setGiManagementType(String giManagementType) {
		this.giManagementType = giManagementType;
	}

	public String getGiReasonForAdmission() {
		return giReasonForAdmission;
	}

	public void setGiReasonForAdmission(String giReasonForAdmission) {
		this.giReasonForAdmission = giReasonForAdmission;
	}

	public Date getGiAdmitted() {
		return giAdmitted;
	}

	public void setGiAdmitted(Date giAdmitted) {
		this.giAdmitted = giAdmitted;
	}

	public String getGiRoomCategory() {
		return giRoomCategory;
	}

	public void setGiRoomCategory(String giRoomCategory) {
		this.giRoomCategory = giRoomCategory;
	}

	public String getGiSavedType() {
		return giSavedType;
	}

	public void setGiSavedType(String giSavedType) {
		this.giSavedType = giSavedType;
	}

	public String getGiClmProcDivn() {
		return giClmProcDivn;
	}

	public void setGiClmProcDivn(String giClmProcDivn) {
		this.giClmProcDivn = giClmProcDivn;
	}

	public String getGiPatientNameYn() {
		return giPatientNameYn;
	}

	public void setGiPatientNameYn(String giPatientNameYn) {
		this.giPatientNameYn = giPatientNameYn;
	}

	public String getGiInpatientNo() {
		return giInpatientNo;
	}

	public void setGiInpatientNo(String giInpatientNo) {
		this.giInpatientNo = giInpatientNo;
	}

	public String getGiCreatedBy() {
		return giCreatedBy;
	}

	public void setGiCreatedBy(String giCreatedBy) {
		this.giCreatedBy = giCreatedBy;
	}

	public Date getGiCreatedOn() {
		return giCreatedOn;
	}

	public void setGiCreatedOn(Date giCreatedOn) {
		this.giCreatedOn = giCreatedOn;
	}

	public String getGiAccidentDeathFlag() {
		return giAccidentDeathFlag;
	}

	public void setGiAccidentDeathFlag(String giAccidentDeathFlag) {
		this.giAccidentDeathFlag = giAccidentDeathFlag;
	}

	public String getGiHospitalRequiredFlag() {
		return giHospitalRequiredFlag;
	}

	public void setGiHospitalRequiredFlag(String giHospitalRequiredFlag) {
		this.giHospitalRequiredFlag = giHospitalRequiredFlag;
	}

	public String getGiPACategory() {
		return giPACategory;
	}

	public void setGiPACategory(String giPACategory) {
		this.giPACategory = giPACategory;
	}

	public String getGiPAPatientName() {
		return giPAPatientName;
	}

	public void setGiPAPatientName(String giPAPatientName) {
		this.giPAPatientName = giPAPatientName;
	}

	public String getGiPAParentName() {
		return giPAParentName;
	}

	public void setGiPAParentName(String giPAParentName) {
		this.giPAParentName = giPAParentName;
	}

	public String getGiPAParentDOB() {
		return giPAParentDOB;
	}

	public void setGiPAParentDOB(String giPAParentDOB) {
		this.giPAParentDOB = giPAParentDOB;
	}

	public String getGiPAParentAge() {
		return giPAParentAge;
	}

	public void setGiPAParentAge(String giPAParentAge) {
		this.giPAParentAge = giPAParentAge;
	}

	public String getGiPolicyYear() {
		return giPolicyYear;
	}

	public void setGiPolicyYear(String giPolicyYear) {
		this.giPolicyYear = giPolicyYear;
	}

	public String getGiAttenderMobileNumber() {
		return giAttenderMobileNumber;
	}

	public void setGiAttenderMobileNumber(String giAttenderMobileNumber) {
		this.giAttenderMobileNumber = giAttenderMobileNumber;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getIntimatorMobileNumber() {
		return intimatorMobileNumber;
	}

	public void setIntimatorMobileNumber(String intimatorMobileNumber) {
		this.intimatorMobileNumber = intimatorMobileNumber;
	}

	public String getAttenderPhoneNumber() {
		return attenderPhoneNumber;
	}

	public void setAttenderPhoneNumber(String attenderPhoneNumber) {
		this.attenderPhoneNumber = attenderPhoneNumber;
	}

	public String getCallerAddress() {
		return callerAddress;
	}

	public void setCallerAddress(String callerAddress) {
		this.callerAddress = callerAddress;
	}

}
